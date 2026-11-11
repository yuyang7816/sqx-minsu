package com.sqx.app.base.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.orders.service.IOrdersService;
import com.sqx.app.base.roomOrderRecord.service.IRoomOrderRecordService;
import com.sqx.app.base.pay.config.WXConfig;
import com.sqx.app.base.pay.dao.PayDetailsDao;
import com.sqx.app.base.pay.entity.PayClassify;
import com.sqx.app.base.pay.entity.PayDetails;
import com.sqx.app.base.pay.service.PayClassifyService;
import com.sqx.app.base.pay.service.WxService;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.utils.AmountCalUtils;
import com.sqx.app.base.utils.MD5Util;
import com.sqx.app.base.utils.Result;
import com.sqx.app.base.utils.WXConfigUtil;
import com.sqx.app.base.vipConfig.entity.VipConfig;
import com.sqx.app.base.vipConfig.service.IVipConfigService;
import com.sqx.common.core.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/2/26
 */
@Service
@Slf4j
public class WxServiceImpl implements WxService {
    private static final String SPBILL_CREATE_IP = "127.0.0.1";
    private static final String TRADE_TYPE_APP = "APP";
    private static final String TRADE_TYPE_NATIVE = "NATIVE";
    private static final String TRADE_TYPE_JSAPI = "JSAPI";

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private PayClassifyService classifyService;
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IRoomOrderRecordService orderRecordService;
    @Autowired
    private IVipConfigService vipConfigService;


    @Override
    public Result payOrder(Long orderId, Integer classify, HttpServletRequest request) throws Exception {
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
            payDetails.setClassify(classify);
            payDetails.setType(1);
            payDetailsDao.insert(payDetails);
        } else {
            payDetails.setClassify(classify);
            payDetailsDao.updateById(payDetails);
        }
        return pay(orders.getPayMoney().doubleValue(), classify, orders.getUserId(), payDetails.getOrderId());

    }

    @Override
    public Result rechargeMoney(Long userId, Integer classify, Long payClassifyId) throws Exception {
        PayDetails payDetails = new PayDetails();
        PayClassify payClassify = classifyService.getById(payClassifyId);
        if (payClassify == null || payClassify.getIsEnable() == 0) {
            return Result.error("充值参数错误,请重试!");
        }
        payDetails.setRelationId(payClassifyId.toString());
        UserEntity userEntity = userService.getById(userId);
        if (userEntity == null) {
            return Result.error("当前用户不存在,请重新登录后重试!");
        }
        String generalOrder = getGeneralOrder();
        payDetails.setState(0);
        payDetails.setCreateTime(LocalDateTime.now());
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(payClassify.getPrice().doubleValue());
        payDetails.setClassify(classify);
        payDetails.setType(1);
        payDetailsDao.insert(payDetails);
        return pay(payClassify.getPrice().doubleValue(), classify, userId, generalOrder);
    }


    /**
     * 微信支付订单生成
     *
     * @param moneys     支付金额 带小数点
     * @param classify   类型 1app  2 公众号支付  3小程序 公众号支付
     * @param userId     用户id
     * @param outTradeNo 单号
     * @return
     * @throws Exception
     */
    private Result pay(Double moneys, Integer classify, Long userId, String outTradeNo) throws Exception {
        //h5服务域名配置
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        if (classify == 3) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyJsApi";
        } else if (classify == 2) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyMp";
        } else {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notify";
        }
        String currentTimeMillis = (System.currentTimeMillis() / 1000) + "";
        //后台服务名称
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址：" + url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        String money = String.valueOf(mul.intValue());
        String generateNonceStr = WXPayUtil.generateNonceStr();
        WXConfig config = new WXConfig();
        //微信小程序APPID        微信公众号APPID
        if (classify == 1) {
            config.setAppId(commonInfoService.findOne(74).getValue());
        } else if (classify == 2) {
            config.setAppId(commonInfoService.findOne(5).getValue());
        } else {
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        //微信商户key
        config.setKey(commonInfoService.findOne(75).getValue());
        //微信商户号mchId
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", generateNonceStr);
        String body = one.getValue();
        data.put("body", body);
        //生成商户订单号，不可重复
        data.put("out_trade_no", outTradeNo);
        data.put("total_fee", money);
        //自己的服务器IP地址
        data.put("spbill_create_ip", SPBILL_CREATE_IP);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", url);
        //交易类型
        data.put("trade_type", classify == 1 ? TRADE_TYPE_APP : TRADE_TYPE_JSAPI);
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        data.put("attach", "");
        data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                WXPayConstants.SignType.MD5));
        UserEntity userEntity = userService.queryByUserId(userId);
        if (classify == 3) {
            data.put("openid", userEntity.getOpenId());
        } else if (classify == 2) {
            data.put("openid", userEntity.getAccountOpenId());
        }
        //使用官方API请求预付订单
        Map<String, String> response = wxpay.unifiedOrder(data);
        for (String key : response.keySet()) {
            log.info("微信支付订单微信返回参数：keys:" + key + "    value:" + response.get(key).toString());
        }
        if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
            if (classify == 1) {
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("package", "Sign=WXPay");
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                        WXPayConstants.SignType.MD5));
                param.put("outtradeno", outTradeNo);
                return Result.success().put("data", param);
            } else {
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                    /*param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                            WXPayConstants.SignType.MD5));*/
                String stringSignTemp = "appId=" + config.getAppID() + "&nonceStr=" + generateNonceStr + "&package=prepay_id=" + response.get("prepay_id") + "&signType=MD5&timeStamp=" + currentTimeMillis + "" + "&key=" + config.getKey();
                String sign = MD5Util.md5Encrypt32Upper(stringSignTemp).toUpperCase();
                param.put("sign", sign);
                param.put("outtradeno", outTradeNo);
                param.put("package", "prepay_id=" + response.get("prepay_id"));//给前端返回的值
                param.put("mweb_url", response.get("mweb_url"));
                param.put("trade_type", response.get("trade_type"));
                param.put("return_msg", response.get("return_msg"));
                param.put("result_code", response.get("result_code"));
                param.put("signType", "MD5");
                return Result.success().put("data", param);
            }
        }
        return Result.error("获取订单失败");
    }

    @Override
    public String payBack(String resXml, Integer type) {
        WXConfig config = null;
        try {
            config = new WXConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("进入回调了！！！");
        if (type == 1) {
            config.setAppId(commonInfoService.findOne(74).getValue());
        } else if (type == 2) {
            config.setAppId(commonInfoService.findOne(5).getValue());
        } else {
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理
                log.error("验证成功！！！");
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    log.error("验证成功222！！！");
                    if (out_trade_no != null) {
                        log.error("订单号！！！" + out_trade_no);
                        PayDetails payDetails = payDetailsDao.selectByOrderId(out_trade_no);
                        if (payDetails.getState() == 0) {
                            payDetailsDao.updateState(payDetails.getId(), 1, DateUtils.format(new Date()), out_trade_no);
                            //订单支付
                            if (payDetails.getType() == 1) {
                                Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("order_number", payDetails.getOrderId()));
                                ordersService.orderCallback(orders.getOrderId(), payDetails.getClassify(), orders.getPayMoney());
                            } else if (payDetails.getType() == 3) {
                                //购买会员
                                vipConfigService.buyVipCallback(payDetails.getUserId(), Long.valueOf(payDetails.getRelationId()), payDetails.getClassify());
                            }
                        }

                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                } else {
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                System.err.println("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (
                Exception e) {
            System.err.println("手机支付回调通知失败" + e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }


    @Override
    public boolean refund(PayDetails payDetails, BigDecimal refundMoney) {
        if (StringUtils.isBlank(payDetails.getRefundNumber())) {
            payDetails.setRefundNumber(getGeneralOrder());
        }
        WXConfigUtil config = null;
        try {
            config = new WXConfigUtil();
        } catch (Exception e) {
            log.error("微信支付配置初始化失败", e);
        }
        int commInfoId = 0;
        Integer payWay = payDetails.getType(); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
        switch (payWay) {
            case 1:
                commInfoId = 74;
                break; //appId
            case 2:
                commInfoId = 5;
                break; //公众号id
            case 3:
                commInfoId = 45;
                break; //小程序id
        }
        config.setAppId(commonInfoService.findOne(commInfoId).getValue());
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5));
        } catch (Exception e) {
            log.error("微信退款签名生成失败", e);
            return false;
        }
        data.put("out_trade_no", payDetails.getOrderId()); //订单号,支付单号一致
        data.put("out_refund_no", payDetails.getRefundNumber()); //退款单号，同一笔用不同的退款单号
        data.put("total_fee", String.valueOf((BigDecimal.valueOf(payDetails.getMoney()).multiply(BigDecimal.valueOf(100))).intValue())); //1块等于微信支付传入100);
        data.put("refund_fee", String.valueOf((refundMoney.multiply(BigDecimal.valueOf(100))).intValue())); //1块等于微信支付传入100);
        //使用官方API退款
        try {
            Map<String, String> response = wxpay.refund(data);
            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                //修改成功后将退款订单号置空
                payDetailsDao.updateRefund(payDetails.getId());
                log.info("退款成功");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("返回");
            log.error("微信退款失败！，订单号" + payDetails.getOrderId() + "，失败原因：" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Result wxPayVip(Long userId, Long vipId, Integer classify) throws Exception {
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
        return pay(payDetails.getMoney(), classify, payDetails.getUserId(), payDetails.getOrderId());
    }
}