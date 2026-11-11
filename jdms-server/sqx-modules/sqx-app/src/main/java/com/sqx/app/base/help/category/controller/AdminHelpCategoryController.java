package com.sqx.app.base.help.category.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryInfoBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryPageBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategorySaveBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryUpdateBo;
import com.sqx.app.base.help.category.domain.vo.HelpCategoryVo;
import com.sqx.app.base.help.category.service.HelpCategoryService;
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
 * 帮助中心分类 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:43:39
 */
@Tag(name = "管理端-帮助中心分类", description = "管理端-帮助中心分类")
@RestController
@RequestMapping("/admin/helpCategory")
public class AdminHelpCategoryController {

    @Resource
    private HelpCategoryService helpCategoryService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated HelpCategorySaveBo helpCategorySaveBo) {
        helpCategoryService.saveInfo(helpCategorySaveBo);
        return Result.success();
    }

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

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated HelpCategoryUpdateBo helpCategoryUpdateBo) {
        helpCategoryService.updateInfo(helpCategoryUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated HelpCategoryInfoBo helpCategoryInfoBo) {
        helpCategoryService.delete(helpCategoryInfoBo);
        return Result.success();
    }
}
