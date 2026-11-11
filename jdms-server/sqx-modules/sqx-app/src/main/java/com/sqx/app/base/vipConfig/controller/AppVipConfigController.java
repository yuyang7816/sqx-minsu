package com.sqx.app.base.vipConfig.controller;

import com.sqx.app.base.utils.Result;
import com.sqx.app.base.vipConfig.entity.VipConfig;
import com.sqx.app.base.vipConfig.service.IVipConfigService;
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
 * vip配置表 前端控制器
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
@RestController
@RequestMapping("/app/vipConfig/")
@Tag(name = "vip配置表---用户端")
public class AppVipConfigController {
    @Autowired
    private IVipConfigService vipConfigService;


    @GetMapping("getVipConfigList")
    @Operation(summary = "获取vip配置列表")
    public Result getVipConfigList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                   @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                   VipConfig vipConfig) {
        vipConfig.setIsEnable(1);
        return Result.success().put("data", vipConfigService.getVipConfigList(page, limit, vipConfig));
    }


}
