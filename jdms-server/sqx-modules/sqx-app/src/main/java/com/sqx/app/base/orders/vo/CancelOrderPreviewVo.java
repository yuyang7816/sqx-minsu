package com.sqx.app.base.orders.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 取消订单扣费预览
 */
@Data
@Schema(description = "取消订单扣费预览")
public class CancelOrderPreviewVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "是否扣除服务费")
    private Boolean needDeduct;

    @Schema(description = "服务费金额")
    private BigDecimal serviceFee;

    @Schema(description = "原路退回金额")
    private BigDecimal refundMoney;

    @Schema(description = "订单实付金额")
    private BigDecimal payMoney;

    @Schema(description = "弹框提示文案")
    private String tip;
}
