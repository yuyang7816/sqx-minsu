package com.sqx.app.base.shopsRoom.controller;

import com.sqx.app.base.shopsRoom.entity.ShopsRoomType;
import com.sqx.app.base.shopsRoom.service.ShopsRoomTypeService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/6/5 9:59
 */
@RestController
@RequestMapping("/app/roomType/")
@Tag(name = "房间类型表---用户端")
public class AppShopsRoomTypeController {

    @Autowired
    private ShopsRoomTypeService shopsRoomTypeService;

    @PostMapping("addRoomType")
    @Operation(summary = "添加房间类型")
    public Result addRoomType(ShopsRoomType shopsRoomType) {
        return shopsRoomTypeService.addRoomType(shopsRoomType);
    }

    @PostMapping("updateRoomType")
    @Operation(summary = "修改房间类型")
    public Result updateRoomType(ShopsRoomType shopsRoomType) {
        return shopsRoomTypeService.updateRoomType(shopsRoomType);
    }

    @PostMapping("deleteRoomType")
    @Operation(summary = "删除房间类型")
    public Result deleteRoomType(String roomTypeIds) {
        return shopsRoomTypeService.deleteRoomType(roomTypeIds);
    }

    @GetMapping("getRoomTypeList")
    @Operation(summary = "获取房间类型列表")
    public Result getRoomTypeList(ShopsRoomType shopsRoomType) {
        return shopsRoomTypeService.getRoomTypeList(shopsRoomType);
    }

    @GetMapping("getRoomTypeInfo")
    @Operation(summary = "获取房间类型信息")
    public Result getRoomTypeInfo(Long roomTypeId) {
        return shopsRoomTypeService.getRoomTypeInfo(roomTypeId);
    }


}
