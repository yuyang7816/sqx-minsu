package com.sqx.app.base.help.category.domain.vo;

import com.sqx.app.base.help.article.domain.entity.HelpArticle;
import com.sqx.app.base.help.category.domain.entity.HelpCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:43:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HelpCategoryVo extends HelpCategory {

    @Schema(description = "文章列表")
    private List<HelpArticle> articleList;
}
