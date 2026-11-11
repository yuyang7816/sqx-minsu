package com.sqx.app.base.help.article.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.help.article.domain.bo.HelpArticleInfoBo;
import com.sqx.app.base.help.article.domain.bo.HelpArticlePageBo;
import com.sqx.app.base.help.article.domain.bo.HelpArticleSaveBo;
import com.sqx.app.base.help.article.domain.bo.HelpArticleUpdateBo;
import com.sqx.app.base.help.article.domain.vo.HelpArticleVo;
import com.sqx.app.base.help.article.service.HelpArticleService;
import com.sqx.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 帮助中心文章 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:44:06
 */
@Tag(name = "管理端-帮助中心文章", description = "管理端-帮助中心文章")
@RestController
@RequestMapping("/admin/helpArticle")
public class AdminHelpArticleController {

    @Resource
    private HelpArticleService helpArticleService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated HelpArticleSaveBo helpArticleSaveBo) {
        helpArticleService.saveInfo(helpArticleSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = HelpArticleVo.class)))
    @PostMapping("page")
    public Result<IPage<HelpArticleVo>> page(@RequestBody @Validated HelpArticlePageBo helpArticlePageBo) {
        IPage<HelpArticleVo> voIPage = helpArticleService.pageInfo(helpArticlePageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = HelpArticleVo.class)))
    @PostMapping("info")
    public Result<HelpArticleVo> info(@RequestBody @Validated HelpArticleInfoBo helpArticleInfoBo) {
        HelpArticleVo vo = helpArticleService.info(helpArticleInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated HelpArticleUpdateBo helpArticleUpdateBo) {
        helpArticleService.updateInfo(helpArticleUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated HelpArticleInfoBo helpArticleInfoBo) {
        helpArticleService.delete(helpArticleInfoBo);
        return Result.success();
    }
}
