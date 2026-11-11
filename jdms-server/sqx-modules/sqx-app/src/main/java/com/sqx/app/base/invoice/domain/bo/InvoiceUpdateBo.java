package com.sqx.app.base.invoice.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chengming
 * @since 2025/2/21
 */
@Data
public class InvoiceUpdateBo {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "类型 1个人 2公司")
    private Integer type;
}
