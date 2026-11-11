package com.sqx.app.base.pay.service;

import com.sqx.app.base.pay.entity.PayDetails;
import com.sqx.app.base.utils.Result;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxService {
    /**
     *
     * @param orderId 订单id
     * @param classify 1微信app支付  2微信公众号支付  3微信小程序支付
     * @param request
     * @return
     * @throws Exception
     */
    Result payOrder(Long orderId, Integer classify, HttpServletRequest request) throws Exception;

    Result rechargeMoney(Long userId,  Integer classify, Long payClassifyId) throws Exception;


    String payBack(String resXml,Integer type);

    boolean refund(PayDetails payDetails, BigDecimal refundMoney);

    Result wxPayVip(Long userId, Long vipId, Integer classify) throws Exception;
}