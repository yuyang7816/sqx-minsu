package com.sqx.app.base.help.article.controller;

import com.sqx.app.base.help.article.domain.bo.HelpArticleInfoBo;
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
@Tag(name = "用户端-帮助中心文章", description = "用户端-帮助中心文章")
@RestController
@RequestMapping("/app/help/article")
public class AppHelpArticleController {

    @Resource
    private HelpArticleService helpArticleService;

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
}
