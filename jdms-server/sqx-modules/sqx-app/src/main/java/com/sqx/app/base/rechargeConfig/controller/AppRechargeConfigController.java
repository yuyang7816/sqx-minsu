package com.sqx.app.base.rechargeConfig.controller;

import com.sqx.app.base.rechargeConfig.entity.RechargeConfig;
import com.sqx.app.base.rechargeConfig.service.RechargeConfigService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wuchang
 * @since 2022-11-04
 */
@RestController
@RequestMapping("/app/rechargeConfig/")
@Tag(name = "充值配置---用户端")
public class AppRechargeConfigController {
    @Autowired
    private RechargeConfigService rechargeConfigService;


    @Operation(summary = "获取充值配置详情")
    @GetMapping("getConfigInfo")
    public Result getConfigInfo(@RequestParam @Parameter(description = "配置id") Long rechargeId) {
        return Result.success().put("data", rechargeConfigService.getRechargeConfigInfo(rechargeId));
    }

    @Operation(summary = "获取充值配置列表")
    @GetMapping("getConfigInfoList")
    public Result getConfigInfoList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                    @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                    RechargeConfig rechargeConfig) {
        rechargeConfig.setIsEnable(1);
        return Result.success().put("data", rechargeConfigService.getConfigInfoList(page, limit, rechargeConfig));
    }
}

