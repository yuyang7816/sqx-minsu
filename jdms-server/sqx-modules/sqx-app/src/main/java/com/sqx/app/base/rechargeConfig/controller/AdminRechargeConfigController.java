package com.sqx.app.base.rechargeConfig.controller;

import com.sqx.app.base.rechargeConfig.entity.RechargeConfig;
import com.sqx.app.base.rechargeConfig.service.RechargeConfigService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wuchang
 * @since 2022-11-04
 */
@RestController
@RequestMapping("/admin/rechargeConfig/")
@Tag(name = "充值配置---管理端")
public class AdminRechargeConfigController {

    @Autowired
    private RechargeConfigService rechargeConfigService;

    @Operation(summary = "添加充值配置")
    @PostMapping("addConfig")
    public Result addConfig(RechargeConfig rechargeConfig) {
        return rechargeConfigService.addConfig(rechargeConfig);
    }

    @Operation(summary = "修改充值配置")
    @PostMapping("updateConfig")
    public Result updateConfig(RechargeConfig rechargeConfig) {
        return rechargeConfigService.updateConfig(rechargeConfig);
    }

    @Operation(summary = "获取充值配置详情")
    @GetMapping("getConfigInfo")
    public Result getConfigInfo(@RequestParam @Parameter(description = "配置id") Long rechargeId) {
        return Result.success().put("data", rechargeConfigService.getRechargeConfigInfo(rechargeId));
    }

    @Operation(summary = "删除充值配置")
    @GetMapping("deleteConfig")
    public Result deleteConfig(@RequestParam @Parameter(description = "配置id") Long rechargeId) {
        return rechargeConfigService.deleteConfig(rechargeId);
    }

    @Operation(summary = "获取充值配置列表")
    @GetMapping("getConfigInfoList")
    public Result getConfigInfoList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                    @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                    RechargeConfig rechargeConfig) {
        return Result.success().put("data", rechargeConfigService.getConfigInfoList(page, limit, rechargeConfig));
    }
}

