package com.sqx.app.base.help.category.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryInfoBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryPageBo;
import com.sqx.app.base.help.category.domain.vo.HelpCategoryVo;
import com.sqx.app.base.help.category.service.HelpCategoryService;
import com.sqx.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 帮助中心分类 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:43:39
 */
@Tag(name = "用户端-帮助中心分类", description = "用户端-帮助中心分类")
@RestController
@RequestMapping("/app/help/category")
public class AppHelpCategoryController {

    @Resource
    private HelpCategoryService helpCategoryService;

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @ApiResponse(content = @Content(schema = @Schema(implementation = HelpCategoryVo.class)))
    @PostMapping("page")
    public Result<IPage<HelpCategoryVo>> page(@RequestBody @Validated HelpCategoryPageBo helpCategoryPageBo) {
        IPage<HelpCategoryVo> voIPage = helpCategoryService.pageInfo(helpCategoryPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @ApiResponse(content = @Content(schema = @Schema(implementation = HelpCategoryVo.class)))
    @PostMapping("info")
    public Result<HelpCategoryVo> info(@RequestBody @Validated HelpCategoryInfoBo helpCategoryInfoBo) {
        HelpCategoryVo vo = helpCategoryService.info(helpCategoryInfoBo);
        return Result.success(vo);
    }

    @Operation(summary = "列表(不分页)")
    @ApiResponse(content = @Content(schema = @Schema(implementation = HelpCategoryVo.class)))
    @GetMapping("list")
    public Result<List<HelpCategoryVo>> list() {
        List<HelpCategoryVo> voList = helpCategoryService.listInfo();
        return Result.success(voList);
    }
}
