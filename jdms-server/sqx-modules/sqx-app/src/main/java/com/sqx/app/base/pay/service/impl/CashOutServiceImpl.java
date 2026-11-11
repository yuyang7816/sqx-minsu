package com.sqx.app.base.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.message.dao.MessageInfoDao;
import com.sqx.app.base.message.entity.MessageInfo;
import com.sqx.app.base.pay.dao.CashOutDao;
import com.sqx.app.base.pay.entity.CashOut;
import com.sqx.app.base.pay.service.CashOutService;
import com.sqx.app.base.shopMoneyDetails.dao.ShopMoneyDetailsDao;
import com.sqx.app.base.shopMoneyDetails.entity.ShopMoneyDetails;
import com.sqx.app.base.shops.dao.ShopsDao;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shopsUserRole.entity.ShopsUserRole;
import com.sqx.app.base.shopsUserRole.service.IShopsUserRoleService;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.entity.UserMoney;
import com.sqx.app.base.user.service.UserMoneyService;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.userMoneyDetails.entity.UserMoneyDetails;
import com.sqx.app.base.userMoneyDetails.service.UserMoneyDetailsService;
import com.sqx.app.base.utils.AmountCalUtils;
import com.sqx.app.base.utils.Result;
import com.sqx.common.mybatis.utils.PageUtils;
import com.sqx.common.mybatis.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;
import weixin.popular.bean.message.templatemessage.TemplateMessageResult;
import weixin.popular.support.TokenManager;

import javax.websocket.SendResult;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 提现申请记录
 */
@Service
public class CashOutServiceImpl extends ServiceImpl<CashOutDao, CashOut> implements CashOutService {

    /**
     * 提现申请记录
     */
    @Autowired
    private CashOutDao cashOutDao;
    /**
     * 通用配置
     */
    @Autowired
    private CommonInfoService commonInfoService;
    /**
     * app用户
     */
    @Autowired
    private UserService userService;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Autowired
    private ShopsDao shopsDao;

    @Autowired
    private ShopMoneyDetailsDao shopMoneyDetailsDao;

    @Autowired
    private IShopsUserRoleService shopsUserRoleService;


    @Override
    public PageUtils selectCashOutList(Map<String, Object> params) {
        String zhifubaoName = (String) params.get("zhifubaoName");
        String zhifubao = (String) params.get("zhifubao");
        String userId = String.valueOf(params.get("userId"));
        String classify = String.valueOf(params.get("classify"));
        String state = String.valueOf(params.get("state"));
        String userType = String.valueOf(params.get("userType"));
        IPage<CashOut> page = this.page(
                new Query<CashOut>().getPage(params),
                new QueryWrapper<CashOut>()
                        .eq(StringUtils.isNotBlank(zhifubaoName), "zhifubao_name", zhifubaoName)
                        .eq(StringUtils.isNotBlank(zhifubao), "zhifubao", zhifubao)
                        .eq(StringUtils.isNotBlank(userId) && !"null".equals(userId), "user_id", userId)
                        .eq(StringUtils.isNotBlank(classify) && !"null".equals(classify) && "1".equals(classify), "classify", classify)
                        .eq(StringUtils.isNotBlank(state) && !"null".equals(state), "state", state)
                        .and(StringUtils.isNotBlank(classify) && !"null".equals(classify) && "2".equals(classify), wrapper -> wrapper.eq("classify", 2).or().eq("classify", 3))
                        .eq(StringUtils.isNotBlank(userType),"user_type",userType)
                        .orderByDesc("id")
        );
        return new PageUtils(page);
    }


    @Override
    public CashOut selectById(Long id) {
        return cashOutDao.selectById(id);
    }

    @Override
    public int saveBody(CashOut cashOut) {
        return cashOutDao.insert(cashOut);
    }


    @Override
    public int update(CashOut cashOut) {
        return cashOutDao.updateById(cashOut);
    }


    @Override
    public void cashOutSuccess(UserEntity userByWxId, String date, String money, String payWay, String url) {
        if (userByWxId != null) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setState(String.valueOf(5));
            messageInfo.setContent("您好，您的提现转账成功，请注意查收！提现金额【" + money + "元】！支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服");
            messageInfo.setTitle("提现成功通知");
            messageInfo.setUserName(userByWxId.getUserName());
            messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            messageInfo.setCreateAt(sdf.format(now));
            messageInfo.setIsSee("0");
            messageInfoDao.insert(messageInfo);
            if (userByWxId.getClientidId() != null) {
                userService.pushToSingle("提现成功通知", "您好，您的提现转账成功，请注意查收！提现金额【" + money + "元】！支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", userByWxId.getClientidId());
            }
            CommonInfo three = commonInfoService.findOne(39);
            String apkey = "";
            if (three != null) {
                apkey = three.getValue();
            }
            if (StringUtils.isNotBlank(userByWxId.getOpenId())) {
                LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
                data.put("first", new TemplateMessageItem("您好，您的提现转账成功，请注意查收", "#d71345"));
                data.put("keyword1", new TemplateMessageItem(money + " 元", "#d71345"));
                data.put("keyword2", new TemplateMessageItem(date, "#d71345"));
                data.put("remark", new TemplateMessageItem("支付宝收款账号 " + payWay + "感谢您的使用！如有疑问请在公众号中发送您的问题联系客服", null));
                sendWxMessage(apkey, data, userByWxId.getOpenId(), url);
            }
        }

    }

    /**
     * 退款成功通知
     *
     * @param
     * @param date
     * @param money
     * @param url
     */
    @Override
    public void refundSuccess(UserEntity userByWxId, String date, String money, String url, String content) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setState(String.valueOf(5));
        messageInfo.setContent(content);
        messageInfo.setTitle("提现失败提醒");
        messageInfo.setUserName(userByWxId.getUserName());
        messageInfo.setUserId(String.valueOf(userByWxId.getUserId()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setCreateAt(sdf.format(now));
        messageInfo.setIsSee("0");
        messageInfoDao.insert(messageInfo);
        if (userByWxId.getClientidId() != null) {
            userService.pushToSingle("提现失败提醒", content, userByWxId.getClientidId());
        }
        CommonInfo three = commonInfoService.findOne(77);
        String apkey = "";
        if (three != null) {
            apkey = three.getValue();
        }
        if (StringUtils.isNotBlank(userByWxId.getOpenId())) {
            LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<>();
            data.put("first", new TemplateMessageItem("您好，您发起的提现失败了", "#d71345"));
            data.put("keyword1", new TemplateMessageItem(money + " 元", "#d71345"));
            data.put("keyword2", new TemplateMessageItem(date, "#d71345"));
            data.put("keyword3", new TemplateMessageItem(content, "#d71345"));
            data.put("remark", new TemplateMessageItem("请您按照失败原因修改相关信息后，重新提现！", null));
            sendWxMessage(apkey, data, userByWxId.getOpenId(), url);
        }

    }

    @Override
    public Double selectCashOutSum(Long userId, Date startTime, Date endTime) {
        return cashOutDao.selectCashOutSum(userId, startTime, endTime);
    }

    @Override
    public Double sumMoney(String time, Integer flag) {
        return cashOutDao.sumMoney(time, flag);
    }

    @Override
    public Integer countMoney(String time, Integer flag) {
        return cashOutDao.countMoney(time, flag);
    }

    @Override
    public Integer stayMoney(String time, Integer flag) {
        return cashOutDao.stayMoney(time, flag);
    }

    @Override
    public void updateMayMoney(int i, Long userId, Double money) {
        cashOutDao.updateMayMoney(i, userId, money);
    }


    @Override
    public List<CashOut> selectCashOutLimit3() {
        return cashOutDao.selectCashOutLimit3();
    }

    private void sendWxMessage(String templateId, LinkedHashMap<String, TemplateMessageItem> data, String openid, String url) {
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(openid);
        templateMessage.setTemplate_id(templateId);
        templateMessage.setData(data);
        templateMessage.setUrl(url);
        TemplateMessageResult templateMessageResult = MessageAPI.messageTemplateSend(getWxToken(), templateMessage);
        if (templateMessageResult.isSuccess()) {
            new SendResult();
        } else {
            new SendResult();
        }
    }

    private String getWxToken() {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            return TokenManager.getToken(one.getValue());
        } catch (Exception e) {
            throw new RuntimeException("GET_ACCESS_TOKEN_FAIL");
        }
    }


    @Override
    @Transactional
    public Result cashMoney(Long userId, Double money, Integer classify, Integer userType, Long shopId) {
        if (classify == null) {
            classify = 1;
        }
        if (money == null || money <= 0.00) {
            return Result.error("请不要输入小于0的数字,请输入正确的提现金额！");
        }
        CommonInfo one = commonInfoService.findOne(87);
        if (one != null && money < Double.parseDouble(one.getValue())) {
            return Result.error("输入金额不满足最低提现金额，请重新输入！");
        }
        CommonInfo one2 = commonInfoService.findOne(11);
        if (one2 != null && money > Double.parseDouble(one2.getValue())) {
            return Result.error(-100, "输入金额过大，不能大于" + one2.getValue() + "，请重新输入！");
        }
        if (userType != null && userType == 2) {
            return cashMoneyForShop(userId, money, classify, shopId,userType);
        }
        return cashMoneyForUser(userId, money, classify,userType);
    }

    private Result cashMoneyForUser(Long userId, Double money, Integer classify,Integer userType) {
        UserEntity userEntity = userService.selectUserById(userId);
        if (userEntity == null) {
            return Result.error("用户不存在");
        }
        if (classify == 2 || classify == 3) {
            String value = commonInfoService.findOne(280).getValue();
            if ("2".equals(value) && StringUtils.isEmpty(userEntity.getCashQrCode())) {
                return Result.error("请绑定微信提现收款码！");
            }
        }
        CommonInfo one3 = commonInfoService.findOne(154);
        CommonInfo one1 = commonInfoService.findOne(114);
        Double mul = AmountCalUtils.mul(money, Double.parseDouble(one1.getValue()));
        if (mul < 0.01) {
            mul = 0.01;
        }
        UserMoney userMoney = userMoneyService.getOne(new LambdaQueryWrapper<UserMoney>()
                .eq(UserMoney::getUserId, userId));
        if (userMoney == null) {
            return Result.error("用户钱包不存在");
        }
        BigDecimal balance = userMoney.getMoney();
        if (money > balance.doubleValue()) {
            return Result.error("金额不足，请输入正确的金额！");
        }
        Double moneySum = AmountCalUtils.add(new BigDecimal(money), new BigDecimal(mul)).doubleValue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (balance.compareTo(BigDecimal.valueOf(moneySum)) > -1) {
            userMoneyService.updateMoney(2, userId, new BigDecimal(moneySum));
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setTitle("提现：" + money+"，手续费："+ mul);
            userMoneyDetails.setType(2);
            userMoneyDetails.setMoney(new BigDecimal(money));
            userMoneyDetails.setCreateTime(sdf.format(new Date()));
            userMoneyDetails.setClassify(5);
            if (classify == 2 || classify == 3) {
                userMoneyDetails.setContent("微信提现：" + money + "，扣除：" + moneySum + "，手续费：" + mul);
            } else {
                userMoneyDetails.setContent("支付宝提现：" + money + "，扣除：" + moneySum + "，手续费：" + mul);
            }
            CashOut cashOut = buildCashOut(userEntity, classify, money, mul, sdf,userType);
            baseMapper.insert(cashOut);
            userMoneyDetailsService.save(userMoneyDetails);
            return Result.success("提现成功，将在三个工作日内到账，请耐心等待！");
        }
        Double moneySub = AmountCalUtils.sub(new BigDecimal(money), new BigDecimal(mul)).doubleValue(); //金额=提现金额+手续费
        userMoneyService.updateMoney(2, userId, new BigDecimal(moneySub));
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("提现：" + money+"，手续费："+ mul);
        userMoneyDetails.setType(2);
        userMoneyDetails.setMoney(new BigDecimal(money));
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        if (classify == 2 || classify == 3) {
            userMoneyDetails.setContent("微信提现：" + money + "，扣除：" + moneySub + "，手续费：" + mul);
        } else {
            userMoneyDetails.setContent("支付宝提现：" + money + "，扣除：" + moneySub + "，手续费：" + mul);
        }
        userMoneyDetailsService.save(userMoneyDetails);
        CashOut cashOut = buildCashOut(userEntity, classify, moneySub, mul, sdf,userType);
        baseMapper.insert(cashOut);
        return Result.success("提现成功，将在三个工作日内到账，请耐心等待！");
    }

    private Result cashMoneyForShop(Long userId, Double money, Integer classify, Long shopId,Integer userType) {
        if (shopId == null) {
            return Result.error("请选择商户");
        }
        Shops shops = shopsDao.selectById(shopId);
        if (shops == null) {
            return Result.error("提现商户不存在！");
        }
        if (!hasShopCashPermission(userId, shops)) {
            return Result.error("您没有权限操作该商户");
        }
        UserEntity userEntity = userService.selectUserById(userId);
        if (userEntity == null) {
            return Result.error("用户不存在");
        }
        if (classify == 2 || classify == 3) {
            String value = commonInfoService.findOne(280).getValue();
            if ("2".equals(value) && StringUtils.isEmpty(userEntity.getCashQrCode())) {
                return Result.error("请绑定微信提现收款码！");
            }
        }
        CommonInfo one3 = commonInfoService.findOne(154);
        CommonInfo one1 = commonInfoService.findOne(114);
        Double mul = AmountCalUtils.mul(money, Double.parseDouble(one1.getValue()));
        if (mul < 0.01) {
            mul = 0.01;
        }
        BigDecimal balance = shops.getCashMoney() == null ? BigDecimal.ZERO : shops.getCashMoney();
        if (money > balance.doubleValue()) {
            return Result.error("金额不足，请输入正确的金额！");
        }
        Double moneySum = AmountCalUtils.add(new BigDecimal(money), new BigDecimal(mul)).doubleValue();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (balance.compareTo(BigDecimal.valueOf(moneySum)) > -1) {
            updateShopCashMoney(shopId, balance, moneySum);
            saveShopCashOutDetails(shopId, userId, classify, money, moneySum, mul);
            CashOut cashOut = buildCashOut1(userEntity, classify, money, mul, sdf,userType,shopId);
            baseMapper.insert(cashOut);
            return Result.success("提现成功，将在三个工作日内到账，请耐心等待！");
        }
        Double moneySub = AmountCalUtils.sub(new BigDecimal(money), new BigDecimal(mul)).doubleValue();
        updateShopCashMoney(shopId, balance, moneySub);
        saveShopCashOutDetails(shopId, userId, classify, money, moneySub, mul);
        CashOut cashOut = buildCashOut1(userEntity, classify, moneySub, mul, sdf,userType,shopId);
        baseMapper.insert(cashOut);
        return Result.success("提现成功，将在三个工作日内到账，请耐心等待！");
    }

    private boolean hasShopCashPermission(Long userId, Shops shops) {
        if (userId.equals(shops.getUserId())) {
            return true;
        }
        return shopsUserRoleService.count(new LambdaQueryWrapper<ShopsUserRole>()
                .eq(ShopsUserRole::getUserId, userId)
                .eq(ShopsUserRole::getShopId, shops.getShopId())) > 0;
    }

    private void updateShopCashMoney(Long shopId, BigDecimal balance, Double deductMoney) {
        Shops updateShop = new Shops();
        updateShop.setShopId(shopId);
        updateShop.setCashMoney(balance.subtract(BigDecimal.valueOf(deductMoney)));
        updateShop.setUpdateTime(LocalDateTime.now());
        shopsDao.updateById(updateShop);
    }

    private void saveShopCashOutDetails(Long shopId, Long userId, Integer classify, Double moneys,
                                        Double deductMoney, Double mul) {
        ShopMoneyDetails shopMoneyDetails = new ShopMoneyDetails();
        shopMoneyDetails.setShopId(shopId);
        shopMoneyDetails.setUserId(userId);
        shopMoneyDetails.setTitle("提现：" + moneys);
        shopMoneyDetails.setType(2);
        shopMoneyDetails.setClassify(2);
        shopMoneyDetails.setMoney(BigDecimal.valueOf(moneys));
        shopMoneyDetails.setCreateTime(LocalDateTime.now());
        if (classify == 2 || classify == 3) {
            shopMoneyDetails.setContent("微信提现：" + moneys + "，扣除：" + deductMoney + "，手续费：" + mul);
        } else {
            shopMoneyDetails.setContent("支付宝提现：" + moneys + "，扣除：" + deductMoney + "，手续费：" + mul);
        }
        shopMoneyDetailsDao.insert(shopMoneyDetails);
    }

    private CashOut buildCashOut(UserEntity userEntity, Integer classify, Double moneys, Double rate, SimpleDateFormat sdf,Integer userType) {
        CashOut cashOut = new CashOut();
        cashOut.setState(0);
        cashOut.setClassify(classify);
        if (classify == 2 || classify == 3) {
            cashOut.setWxImg(userEntity.getCashQrCode());
        }
        cashOut.setUserType(userType);
        cashOut.setZhifubao(userEntity.getZhiFuBao());
        cashOut.setZhifubaoName(userEntity.getZhiFuBaoName());
        cashOut.setMoney(moneys.toString());
        cashOut.setCreateAt(sdf.format(new Date()));
        cashOut.setUserId(userEntity.getUserId());
        cashOut.setRate(rate);
        cashOut.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        return cashOut;
    }

    private CashOut buildCashOut1(UserEntity userEntity, Integer classify, Double moneys, Double rate, SimpleDateFormat sdf,Integer userType,Long shopId) {
        CashOut cashOut = new CashOut();
        cashOut.setState(0);
        cashOut.setClassify(classify);
        if (classify == 2 || classify == 3) {
            cashOut.setWxImg(userEntity.getCashQrCode());
        }
        cashOut.setRelationId(String.valueOf(shopId));
        cashOut.setUserType(userType);
        cashOut.setZhifubao(userEntity.getZhiFuBao());
        cashOut.setZhifubaoName(userEntity.getZhiFuBaoName());
        cashOut.setMoney(moneys.toString());
        cashOut.setCreateAt(sdf.format(new Date()));
        cashOut.setUserId(userEntity.getUserId());
        cashOut.setRate(rate);
        cashOut.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        return cashOut;
    }

    @Override
    public IPage<CashOut> selectAdminHelpProfit(Integer page, Integer limit, String startTime, String endTime, CashOut cashOut) {
        Page<CashOut> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectAdminHelpProfit(pages, startTime, endTime, cashOut);
    }

    @Override
    public HashMap<String, Object> statisticsMoney(String time, Integer flag) {
        HashMap<String, Object> hashMap = new HashMap<>();
        //总提现金额
        BigDecimal allMoney = cashOutDao.sumCashMoney(time, flag, null, null);
        //支付宝提现金额
        BigDecimal zfbAllMoney = cashOutDao.sumCashMoney(time, flag, null, 1);
        //微信提现金额
        BigDecimal wxAllMoney = cashOutDao.sumCashMoney(time, flag, null, 2);
        //待提现金额
        BigDecimal waitMoney = cashOutDao.sumCashMoney(time, flag, 0, null);
        //支付宝待提现金额
        BigDecimal zfbWaitMoney = cashOutDao.sumCashMoney(time, flag, 0, 1);
        //微信待提现金额
        BigDecimal wxWaitMoney = cashOutDao.sumCashMoney(time, flag, 0, 2);
        //同意提现金额
        BigDecimal traverseMoney = cashOutDao.sumCashMoney(time, flag, 1, null);
        //支付宝同意提现金额
        BigDecimal zfbTraverseMoney = cashOutDao.sumCashMoney(time, flag, 1, 1);
        //微信同意提现金额
        BigDecimal wxTraverseMoney = cashOutDao.sumCashMoney(time, flag, 1, 2);
        //驳回提现金额
        BigDecimal refuseMoney = cashOutDao.sumCashMoney(time, flag, -1, null);
        //支付宝驳回提现金额
        BigDecimal zfbRefuseMoney = cashOutDao.sumCashMoney(time, flag, -1, 1);
        //微信驳回提现金额
        BigDecimal wxRefuseMoney = cashOutDao.sumCashMoney(time, flag, -1, 2);

        //总提现次数
        BigDecimal allCount = cashOutDao.sumCashMoneyCount(time, flag, null, null);
        //支付宝提现次数
        BigDecimal zfbAllCount = cashOutDao.sumCashMoneyCount(time, flag, null, 1);
        //微信提现次数
        BigDecimal wxAllCount = cashOutDao.sumCashMoneyCount(time, flag, null, 2);
        //待提现次数
        BigDecimal waitCount = cashOutDao.sumCashMoneyCount(time, flag, 0, null);
        //支付宝待提现次数
        BigDecimal zfbWaitCount = cashOutDao.sumCashMoneyCount(time, flag, 0, 1);
        //微信待提现次数
        BigDecimal wxWaitCount = cashOutDao.sumCashMoneyCount(time, flag, 0, 2);
        //同意提现次数
        BigDecimal traverseCount = cashOutDao.sumCashMoneyCount(time, flag, 1, null);
        //支付宝同意提现次数
        BigDecimal zfbTraverseCount = cashOutDao.sumCashMoneyCount(time, flag, 1, 1);
        //微信同意提现次数
        BigDecimal wxTraverseCount = cashOutDao.sumCashMoneyCount(time, flag, 1, 2);
        //驳回提现次数
        BigDecimal refuseCount = cashOutDao.sumCashMoneyCount(time, flag, -1, null);
        //支付宝驳回提现次数
        BigDecimal zfbRefuseCount = cashOutDao.sumCashMoneyCount(time, flag, -1, 1);
        //微信驳回提现次数
        BigDecimal wxRefuseCount = cashOutDao.sumCashMoneyCount(time, flag, -1, 2);


        hashMap.put("allCount", allCount);
        hashMap.put("zfbAllCount", zfbAllCount);
        hashMap.put("wxAllCount", wxAllCount);
        hashMap.put("waitCount", waitCount);
        hashMap.put("zfbWaitCount", zfbWaitCount);
        hashMap.put("wxWaitCount", wxWaitCount);
        hashMap.put("traverseCount", traverseCount);
        hashMap.put("zfbTraverseCount", zfbTraverseCount);
        hashMap.put("wxTraverseCount", wxTraverseCount);
        hashMap.put("refuseCount", refuseCount);
        hashMap.put("zfbRefuseCount", zfbRefuseCount);
        hashMap.put("wxRefuseCount", wxRefuseCount);


        hashMap.put("allMoney", allMoney);
        hashMap.put("zfbAllMoney", zfbAllMoney);
        hashMap.put("wxAllMoney", wxAllMoney);
        hashMap.put("waitMoney", waitMoney);
        hashMap.put("zfbWaitMoney", zfbWaitMoney);
        hashMap.put("wxWaitMoney", wxWaitMoney);
        hashMap.put("traverseMoney", traverseMoney);
        hashMap.put("zfbTraverseMoney", zfbTraverseMoney);
        hashMap.put("wxTraverseMoney", wxTraverseMoney);
        hashMap.put("refuseMoney", refuseMoney);
        hashMap.put("zfbRefuseMoney", zfbRefuseMoney);
        hashMap.put("wxRefuseMoney", wxRefuseMoney);
        return hashMap;


    }

}
