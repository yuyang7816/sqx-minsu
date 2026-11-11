package com.sqx.app.base.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.user.entity.App;
import com.sqx.app.base.user.service.AppService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/appinfo")
@Tag(name = "APP升级管理---管理端")
public class AppUpgradeController {

    @Autowired
    private AppService iAppService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Operation(summary = "管理平台升级详情")
    @ResponseBody
    public Result list(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                       @RequestParam(required = false) @Parameter(description = "条数") Integer limit) {
        IPage<App> pages = new Page<>(page, limit);
        return Result.success().put("data", iAppService.page(pages));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Operation(summary = "管理平台升级详情")
    @ResponseBody
    public Result getBanner(@Parameter(description = "升级id") @PathVariable Long id) {
        return Result.success().put("data", iAppService.selectAppById(id));
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Operation(summary = "管理平台添加升级信息")
    @ResponseBody
    public Result addBanner(@RequestBody App app) {
        if (app.getId() != null) {
            iAppService.updateAppById(app);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            app.setCreateAt(sdf.format(new Date()));
            iAppService.insertApp(app);
        }
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @Operation(summary = "管理平台删除升级信息")
    public Result deleteBanner(@Parameter(description = "app升级id") @PathVariable Long id) {
        iAppService.deleteAppById(id);
        return Result.success();
    }


}
