package com.sqx.app.base.cancelAccount.controller;

import com.sqx.app.base.cancelAccount.domain.bo.CancelAccountSaveBo;
import com.sqx.app.base.cancelAccount.service.CancelAccountService;
import com.sqx.app.base.user.annotation.Login;
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
@Tag(name = "用户端-用户注销申请", description = "用户端-用户注销申请")
@RestController
@RequestMapping("/app/cancelAccount")
public class AppCancelAccountController {

    @Resource
    private CancelAccountService cancelAccountService;

    /**
     * 提交申请
     */
    @Login
    @Operation(summary = "提交注销申请")
    @PostMapping("commit")
    public Result<Void> save(@RequestAttribute Long userId, @RequestBody @Validated CancelAccountSaveBo cancelAccountSaveBo) {
        cancelAccountSaveBo.setUserId(userId);
        cancelAccountService.saveInfo(cancelAccountSaveBo);
        return Result.success();
    }

    @Login
    @Operation(summary = "用户撤销申请")
    @GetMapping("revoke")
    public Result<Void> revoke(@RequestAttribute Long userId) {
        cancelAccountService.revoke(userId);
        return Result.success();
    }
}
