package com.sqx.app.base.shopsRoom.controller;

import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import com.sqx.app.base.shopsRoom.service.IShopsRoomService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 房间表
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@RestController
@RequestMapping("/admin/shopsRoom/")
@Tag(name = "房间表---管理端")
public class ShopsRoomController {
    @Autowired
    private IShopsRoomService shopsRoomService;

    @PostMapping("saveShopRoom")
    @Operation(summary = "添加|修改房间")
    public Result saveShopRoom(ShopsRoom shopsRoom) {
        return shopsRoomService.saveShopRoom(shopsRoom);
    }

    @GetMapping("getShopRoomList")
    @Operation(summary = "获取房间列表")
    public Result getShopRoomList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                  @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                  ShopsRoom shopsRoom) {
        return Result.success().put("data", shopsRoomService.getShopRoomList(page, limit, shopsRoom));
    }

    @GetMapping("getShopRoomInfo")
    @Operation(summary = "获取房间信息")
    public Result getShopRoomInfo(@RequestParam(required = false) @Parameter(description = "房间id") Long roomId,
                                  @RequestParam(required = false) @Parameter(description = "复制来源房间id") Long copyFromRoomId) {
        if (copyFromRoomId != null) {
            return Result.success().put("data", shopsRoomService.getShopRoomInfoForCopy(copyFromRoomId));
        }
        return Result.success().put("data", shopsRoomService.getShopRoomInfo(roomId));
    }

    @PostMapping("deleteRoom")
    @Operation(summary = "删除房间")
    public Result deleteRoom(@RequestParam(required = false) @Parameter(description = "房间id，多个逗号分隔") String roomIds) {
        return shopsRoomService.deleteRoom(roomIds);
    }

    @GetMapping("getRoomTypeList")
    @Operation(summary = "获取房间类型列表")
    public Result getRoomTypeList() {
        return Result.success().put("data", shopsRoomService.getRoomTypeList(2));
    }
}
