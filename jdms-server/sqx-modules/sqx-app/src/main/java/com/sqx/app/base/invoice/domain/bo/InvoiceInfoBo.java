package com.sqx.app.base.invoice.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chengming
 * @since 2025/2/21
 */
@Data
public class InvoiceInfoBo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "状态 1待开票 2已开票")
    private Integer status;
}
