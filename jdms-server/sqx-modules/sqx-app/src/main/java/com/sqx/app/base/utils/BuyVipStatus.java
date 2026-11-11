package com.sqx.app.base.utils;

/**
 * 会员支付状态枚举
 *
 * @author WUCHANG
 */
public enum BuyVipStatus {
    OK(10000,"购买成功"),
    BALANCE_FUNDS(10001, "余额不足"),
    USER_BAN(10002,"您的账户已封禁"),
    GOOD_DOWN(10003,"充值配置不存在或已下架"),
    PAY_ERROR(10004,"开通异常,请联系管理员");


    private final int code;

    private final String info;

    BuyVipStatus(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
