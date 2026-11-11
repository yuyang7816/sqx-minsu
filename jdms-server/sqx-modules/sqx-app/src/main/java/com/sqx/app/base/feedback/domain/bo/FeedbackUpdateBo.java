package com.sqx.app.base.feedback.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-06 15:53:00
 */
@Data
public class FeedbackUpdateBo {

    @Schema(description = "id")
    private Long id;
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "意见")
    private String opinion;
    @Schema(description = "联系方式")
    private String contact;
}
