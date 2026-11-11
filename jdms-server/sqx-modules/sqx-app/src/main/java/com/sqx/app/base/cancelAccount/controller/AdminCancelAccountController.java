package com.sqx.app.base.cancelAccount.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.cancelAccount.domain.bo.CancelAccountInfoBo;
import com.sqx.app.base.cancelAccount.domain.bo.CancelAccountPageBo;
import com.sqx.app.base.cancelAccount.domain.bo.CancelAccountSaveBo;
import com.sqx.app.base.cancelAccount.domain.bo.CancelAccountUpdateBo;
import com.sqx.app.base.cancelAccount.domain.vo.CancelAccountVo;
import com.sqx.app.base.cancelAccount.service.CancelAccountService;
import com.sqx.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户注销申请 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-07 09:31:24
 */
@Tag(name = "管理端-用户注销申请", description = "管理端-用户注销申请")
@RestController
@RequestMapping("/admin/cancelAccount")
public class AdminCancelAccountController {

    @Resource
    private CancelAccountService cancelAccountService;

    /**
     * 保存
     */
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated CancelAccountSaveBo cancelAccountSaveBo) {
        cancelAccountService.saveInfo(cancelAccountSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Operation(summary = "列表")
    @PostMapping("page")
    public Result<IPage<CancelAccountVo>> page(@RequestBody @Validated CancelAccountPageBo cancelAccountPageBo) {
        IPage<CancelAccountVo> voIPage = cancelAccountService.pageInfo(cancelAccountPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @PostMapping("info")
    public Result<CancelAccountVo> info(@RequestBody @Validated CancelAccountInfoBo cancelAccountInfoBo) {
        CancelAccountVo vo = cancelAccountService.info(cancelAccountInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated CancelAccountUpdateBo cancelAccountUpdateBo) {
        cancelAccountService.updateInfo(cancelAccountUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated CancelAccountInfoBo cancelAccountInfoBo) {
        cancelAccountService.delete(cancelAccountInfoBo);
        return Result.success();
    }

    @Operation(summary = "注销用户")
    @GetMapping("cancel")
    public Result<Void> cancel(Long id) {
        cancelAccountService.cancel(id);
        return Result.success();
    }

    @Operation(summary = "直接注销", description = "管理端直接注销此用户，无需用户申请")
    @GetMapping("adminCancel")
    public Result<Void> adminCancel(Long userId) {
        cancelAccountService.adminCancel(userId);
        return Result.success();
    }
}
