package com.sqx.app.base.pay.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.orders.service.IOrdersService;
import com.sqx.app.base.roomOrderRecord.service.IRoomOrderRecordService;
import com.sqx.app.base.pay.config.AliPayConstants;
import com.sqx.app.base.pay.dao.PayDetailsDao;
import com.sqx.app.base.pay.entity.PayDetails;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import com.sqx.app.base.vipConfig.entity.VipConfig;
import com.sqx.app.base.vipConfig.service.IVipConfigService;
import com.sqx.common.core.utils.DateUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝支付处理--暂不做同步处理、回调方式使用异步
 */
@Slf4j
@RestController
@Tag(name = "支付宝支付---用户端")
@RequestMapping("/app/aliPay")
public class AliPayController {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;

    @Autowired
    private IVipConfigService vipConfigService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IRoomOrderRecordService orderRecordService;

    @RequestMapping(value = "/notifyApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public void notifyApp(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonInfoService.findOne(64).getValue(), AliPayConstants.CHARSET, "RSA2");
            log.info(flag + "回调验证信息");
            if (flag) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    //支付宝返回的订单编号
                    String outTradeNo = params.get("out_trade_no");
                    log.error(outTradeNo);
                    //支付宝支付单号
                    PayDetails payDetails = payDetailsDao.selectByOrderId(outTradeNo);
                    if (payDetails.getState() == 0) {
                        payDetailsDao.updateState(payDetails.getId(), 1, DateUtils.format(new Date()), outTradeNo);
                        //订单支付
                        if (payDetails.getType() == 1) {
                            Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("order_number", payDetails.getOrderId()));
                            ordersService.orderCallback(orders.getOrderId(), payDetails.getClassify(), orders.getPayMoney());
                        } else if (payDetails.getType() == 3) {
                            //购买会员
                            vipConfigService.buyVipCallback(payDetails.getUserId(), Long.valueOf(payDetails.getRelationId()), payDetails.getClassify());
                        }
                    }
                }

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        }
    }


    @Login
    @Operation(description = "支付宝支付订单")
    @RequestMapping(value = "/payOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result paOrder(@RequestParam(required = false) @Parameter(description = "订单id") Long orderId,
                          @RequestParam(required = false) @Parameter(description = "分类（1微信 1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝）")Integer classify) {
        //通知页面地址
        CommonInfo one = commonInfoService.findOne(19);
        String returnUrl = one.getValue();
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "民宿" : one3.getValue();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        log.info("回调地址:" + url);
        Orders orders = ordersService.getById(orderId);
        if (orders == null) {
            return Result.error("订单不存在");
        }
        if (orders.getOrderStatus() != 0) {
            return Result.error("订单状态已变更,请刷新后重试");
        }
        String roomValidateError = orderRecordService.validateOrderRoomAvailability(orders);
        if (roomValidateError != null) {
            return Result.error(roomValidateError.replace("请重新选择日期", "请取消订单后重新选择日期"));
        }
        PayDetails payDetails = payDetailsDao.selectByOrderId(orders.getOrderNumber());
        if (payDetails == null) {
            payDetails = new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(LocalDateTime.now());
            payDetails.setOrderId(orders.getOrderNumber());
            payDetails.setUserId(orders.getUserId());
            payDetails.setMoney(orders.getPayMoney().doubleValue());
            payDetails.setType(1);
            payDetails.setClassify(classify);
            payDetailsDao.insert(payDetails);
        } else {
            payDetails.setClassify(classify);
            payDetailsDao.updateById(payDetails);
        }
        if (classify == 4) {
            return payApp(name, orders.getOrderNumber(), orders.getPayMoney().doubleValue());
        }
        return payH5(name, orders.getOrderNumber(), orders.getPayMoney().doubleValue(), returnUrl);
    }

    @Login
   @Operation(description = "支付宝购买会员")
    @RequestMapping(value = "/payVip", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payVip(@RequestAttribute("userId") Long userId,
                         @RequestParam(required = false) @Parameter(description = "会员id") Long vipId,
                         @RequestParam(required = false) @Parameter(description = "分类（1微信 1app微信 2微信公众号 3微信小程序 4app支付宝 5支付宝H5）") Integer classify) {
        //通知页面地址
        CommonInfo one = commonInfoService.findOne(19);
        String returnUrl = one.getValue();
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "民宿" : one3.getValue();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        log.info("回调地址:" + url);

        VipConfig vipConfig = vipConfigService.getById(vipId);
        if (vipConfig == null || vipConfig.getIsEnable() == 0) {
            return Result.error("充值配置不存在或未开启");
        }
        PayDetails payDetails = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("relation_id", vipId).eq("user_id", userId).eq("type", 3).eq("state", 0));
        if (payDetails == null) {
            payDetails = new PayDetails();
            payDetails.setUserId(userId);
            payDetails.setOrderId(getGeneralOrder());
            payDetails.setClassify(classify);
            payDetails.setCreateTime(LocalDateTime.now());
            payDetails.setType(3);
            payDetails.setMoney(vipConfig.getVipMoney().doubleValue());
            payDetails.setState(0);
            payDetails.setRelationId(String.valueOf(vipId));
            payDetailsDao.insert(payDetails);
        } else {
            payDetails.setClassify(classify);
            payDetailsDao.updateById(payDetails);
        }
        if (classify == 4) {
            return payApp(name, payDetails.getOrderId(), payDetails.getMoney());
        }
        return payH5(name, payDetails.getOrderId(), payDetails.getMoney(), returnUrl);
    }


    public Result payApp(String name, String generalOrder, Double money) {
        CommonInfo one = commonInfoService.findOne(19);
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        String result = "";
        CommonInfo payWay = commonInfoService.findOne(258);
        try {
            if ("1".equals(payWay.getValue())) {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(259);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);

                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(money + "");
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                //这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    result = response.getBody();
                } else {
                    return Result.error("获取订单失败！");
                }
                return Result.success().put("data", result);
            } else {
                //实例化客户端
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(String.valueOf(money));
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    result = response.getBody();
                } else {
                    return Result.error("获取订单失败！");
                }
                return Result.success().put("data", result);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.error(-100, "获取订单失败！");
    }

    public Result payH5(String name, String generalOrder, Double money, String returnUrl) {
        CommonInfo payWay = commonInfoService.findOne(258);
        CommonInfo one = commonInfoService.findOne(19);
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        try {
            if ("1".equals(payWay.getValue())) {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(259);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money + ""); //金额
                alipayRequest.setBizContent(order.toString());
                alipayRequest.setNotifyUrl(url); //在公共参数中设置回跳和通知地址
                alipayRequest.setReturnUrl(returnUrl); //线上通知页面地址
                String result = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success().put("data", result);
            } else {
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money); //金额
                alipayRequest.setBizContent(order.toString());
                //在公共参数中设置回跳和通知地址
                alipayRequest.setNotifyUrl(url);
                //通知页面地址
                alipayRequest.setReturnUrl(returnUrl);
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success().put("data", form);
            }
        } catch (AlipayApiException e) {
            log.error("CreatPayOrderForH5", e);
        }
        return Result.error("获取订单信息错误！");
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }


    /**
     * 说明： 支付宝订单退款
     *
     * @return 公共返回参数 code,msg,   响应参数实例: https://docs.open.alipay.com/api_1/alipay.trade.refund
     */
    public boolean alipayRefund(String ordersNo, BigDecimal refundMoney) {
        PayDetails payDetails = payDetailsDao.selectByOrderId(ordersNo);
        if (StringUtils.isBlank(payDetails.getRefundNumber())) {
            payDetails.setRefundNumber(getGeneralOrder());
        }
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
        AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(payDetails.getOrderId());//订单编号
        model.setTradeNo(payDetails.getTradeNo());//支付宝订单交易号
        model.setRefundAmount(refundMoney.toString());//退款金额 不得大于订单金额
        model.setRefundReason("服务退款");//退款说明
        model.setOutRequestNo(payDetails.getRefundNumber());//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
        alipay_request.setBizModel(model);
        try {
            AlipayTradeRefundResponse alipay_response = alipayClient.execute(alipay_request);
            if(alipay_response.isSuccess()){
               return true;
            } else {
                return false;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return false;
    }


}
