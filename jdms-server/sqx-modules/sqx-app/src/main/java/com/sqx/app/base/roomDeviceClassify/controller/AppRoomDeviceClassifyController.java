package com.sqx.app.base.roomDeviceClassify.controller;

import com.sqx.app.base.roomDeviceClassify.entity.RoomDeviceClassify;
import com.sqx.app.base.roomDeviceClassify.service.IRoomDeviceClassifyService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 房间配置表 前端控制器
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@RestController
@RequestMapping("/app/roomDeviceClassify/")
@Tag(name = "房间配置表---用户端")
public class AppRoomDeviceClassifyController {

    @Autowired
    private IRoomDeviceClassifyService classifyService;

    @PostMapping("saveClassify")
    @Operation(summary = "添加|修改房间配置")
    public Result saveClassify(RoomDeviceClassify classify) {
        return classifyService.saveClassify(classify);
    }

    @GetMapping("getClassifyList")
    @Operation(summary = "获取房间配置列表")
    public Result getClassifyList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                  @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                  RoomDeviceClassify classify) {
        return Result.success().put("data", classifyService.getClassifyList(page, limit, classify));
    }

    @PostMapping("deleteClassify")
    @Operation(summary = "删除房间配置")
    public Result deleteClassify(@RequestParam(required = false) @Parameter(description = "选项id，多个逗号分隔") String classifyIds) {
        return classifyService.deleteClassify(classifyIds);
    }

}
