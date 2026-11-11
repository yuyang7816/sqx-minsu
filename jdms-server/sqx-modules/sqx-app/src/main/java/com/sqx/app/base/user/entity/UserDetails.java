package com.sqx.app.base.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "用户详情")
public class UserDetails {

    /**
     * 本月订单数量
      */
    @Schema(description = "本月订单数量")
    private int monthlyOrderNum;

    /**
     * 本月充值金额
     */
    @Schema(description = "本月充值金额")
    private BigDecimal monthlyRechargeMoney;

    /**
     *本月提现数量
     */
    @Schema(description = "本月提现数量")
    private int monthWithdrawalNum;

    /**
     * 本月提现金额
     */
    @Schema(description = "本月提现金额")
    private BigDecimal monthlyWithdrawalMoney;


}
