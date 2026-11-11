package com.sqx.app.base.pay.business.service.impl;

import com.sqx.common.pay.notify.IPayNotifyBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author chengming
 * @since 2025/4/21
 *
 * 回调业务
 */
@Slf4j
@Service
public class PayNotifyBusinessServiceImpl implements IPayNotifyBusinessService {

    @Override
    public void onPaymentSuccess(String channel, String outTradeNo, String tradeNo) {
        log.info("进入回调成功业务");
    }

    @Override
    public void onPaymentFailed(String channel, String outTradeNo, String tradeNo) {
        log.info("进入回调失败业务");
    }
}
