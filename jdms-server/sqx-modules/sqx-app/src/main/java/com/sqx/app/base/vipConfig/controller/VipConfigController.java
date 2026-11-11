package com.sqx.app.base.vipConfig.controller;

import com.sqx.app.base.utils.Result;
import com.sqx.app.base.vipConfig.entity.VipConfig;
import com.sqx.app.base.vipConfig.service.IVipConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * vip配置表 前端控制器
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
@RestController
@RequestMapping("/admin/vipConfig/")
@Tag(name = "vip配置表---管理端")
public class VipConfigController {
    @Autowired
    private IVipConfigService vipConfigService;

    @PostMapping("saveVipConfig")
    @Operation(summary = "新增或修改vip配置")
    public Result saveVipConfig(@RequestBody VipConfig vipConfig) {
        return vipConfigService.saveVipConfig(vipConfig);
    }

    @GetMapping("getVipConfigList")
    @Operation(summary = "获取vip配置列表")
    public Result getVipConfigList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                   @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                   VipConfig vipConfig) {
        return Result.success().put("data", vipConfigService.getVipConfigList(page, limit, vipConfig));
    }

    @GetMapping("deleteVipConfig")
    @Operation(summary = "删除配置")
    public Result deleteVipConfig(@RequestParam(required = false) @Parameter(description = "配置id") Long vipId) {
        return vipConfigService.removeById(vipId) ? Result.success() : Result.error();
    }

}
