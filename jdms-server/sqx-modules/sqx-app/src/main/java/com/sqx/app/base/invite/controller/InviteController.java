package com.sqx.app.base.invite.controller;

import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Tag(name = "邀请收益---管理端", description = "邀请收益---管理端")
@RequestMapping(value = "/admin/invite")
public class InviteController {

    @Autowired
    private UserService userService;
    @Operation(summary = "用户推广信息")
    @GetMapping(value = "getExtensionData")
    public Result getExtensionData(@RequestParam(required = false) @Parameter(description = "用户id")Long userId) {
        return Result.success().put("data", userService.getExtensionData(userId));
    }

    @Operation(summary = "获取推广用户列表")
    @GetMapping(value = "getExtensionList")
    public Result getExtensionList(@RequestParam(required = false) @Parameter(description = "用户id")Long userId,
                                   @RequestParam(required = false) @Parameter(description = "1 是一级用户  2是二级用户")Integer type,
                                   @RequestParam(required = false) @Parameter(description = "页码")Integer page,
                                   @RequestParam(required = false) @Parameter(description = "条数")Integer limit) {
        return Result.success().put("data", userService.getExtensionList(userId,type, page, limit));
    }

    @Operation(summary = "获取推广用户收益列表")
    @GetMapping(value = "getUserIncomeList")
    public Result getUserIncomeList(@RequestParam(required = false) @Parameter(description = "用户id")Long userId,
                                    @RequestParam(required = false) @Parameter(description = "1 是一级用户  2是二级用户")Integer type,
                                    @RequestParam(required = false) @Parameter(description = "页码")Integer page,
                                    @RequestParam(required = false) @Parameter(description = "条数")Integer limit) {
        return Result.success().put("data", userService.getUserIncomeList(userId,type, page, limit));
    }

    @Operation(summary = "获取邀请的用户收益列表")
    @GetMapping(value = "getProfitList")
    public Result getProfitList(@RequestParam(required = false) @Parameter(description = "用户id")Long userId,
                                @RequestParam(required = false) @Parameter(description = "1 是一级用户  2是二级用户")Integer type,
                                @RequestParam(required = false) @Parameter(description = "页码")Integer page,
                                @RequestParam(required = false) @Parameter(description = "条数")Integer limit) {
        return Result.success().put("data", userService.getProfitList(userId,type, page, limit));
    }


}