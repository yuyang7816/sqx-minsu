package com.sqx.app.base.common.controller;

import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "通用配置管理---管理端")
@RequestMapping(value = "/common")
public class CommonController {

    @Autowired
    private CommonInfoService commonService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Operation(summary = "管理平台通用配置详情")
    @ResponseBody
    public Result getCommon(@Parameter(description = "通用配置类型") @PathVariable Integer id) {
        return Result.success().put("data",commonService.findOne(id));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Operation(summary = "管理平台添加通用配置")
    @ResponseBody
    public Result addCommon(@RequestBody CommonInfo app) {
        return commonService.update(app);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @Operation(summary = "管理平台删除通用配置")
    public Result deleteCommon(@Parameter(description = "通用配置id") @PathVariable int id) {
        return commonService.delete(id);
    }

    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    @Operation(summary = "根据type获取对象")
    @ResponseBody
    public Result getCommonList(@Parameter(description = "通用配置类型") @PathVariable Integer type) {
        return commonService.findByType(type);
    }


    @RequestMapping(value = "/type/condition/{condition}", method = RequestMethod.GET)
    @Operation(summary = "根据condition去查询  xitong  xitongs  shouye")
    @ResponseBody
    public Result findByTypeAndCondition(@Parameter(description = "通用配置分类") @PathVariable String condition) {
        return commonService.findByTypeAndCondition(condition);
    }




}