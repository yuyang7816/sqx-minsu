package com.sqx.app.base.help.article.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:44:06
 */
@Data
public class HelpArticleUpdateBo {

    @Schema(description = "id")
    private Long id;
    @Schema(description = "分类id")
    private Long categoryId;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "内容")
    private String content;
    @Schema(description = "排序")
    private Integer sort;
}
