package com.sqx.app.base.cancelAccount.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-07 09:31:24
 */
@Data
public class CancelAccountSaveBo {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

}
