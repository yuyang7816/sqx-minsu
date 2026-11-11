package com.sqx.app.base.shopsRoom.controller;

import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import com.sqx.app.base.shopsRoom.service.IShopsRoomService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 房间表
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@RestController
@RequestMapping("/app/shopsRoom/")
@Tag(name = "房间表---用户端")
public class AppShopsRoomController {

    @Autowired
    private IShopsRoomService shopsRoomService;


    @PostMapping("saveShopRoom")
    @Operation(summary = "添加|修改房间")
    public Result saveShopRoom(@RequestBody ShopsRoom shopsRoom) {
        return shopsRoomService.saveShopRoom(shopsRoom);
    }

    @PostMapping("deleteRoom")
    @Operation(summary = "删除房间")
    public Result deleteRoom(@RequestParam(required = false) @Parameter(description = "房间id，多个逗号分隔") String roomIds) {
        return shopsRoomService.deleteRoom(roomIds);
    }

    @GetMapping("getShopRoomList")
    @Operation(summary = "获取房间列表")
    public Result getShopRoomList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                  @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") @Parameter(description = "入住日期") LocalDate startDate,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") @Parameter(description = "离店日期") LocalDate endDate,
                                  @RequestParam(required = false) @Parameter(description = "房型id，多个逗号分隔") String roomTypeIds,
                                  @RequestParam(required = false) @Parameter(description = "最低价格筛选") BigDecimal minPrice,
                                  @RequestParam(required = false) @Parameter(description = "最高价格筛选") BigDecimal maxPrice,
                                  @RequestParam(required = false) @Parameter(description = "是否聚合展示 1是") Integer aggregate,
                                  ShopsRoom shopsRoom) {
        if (shopsRoom == null) {
            shopsRoom = new ShopsRoom();
        }
        if (shopsRoom.getIsEnable() == null) {
            shopsRoom.setIsEnable(1);
        }
        if (startDate != null) {
            shopsRoom.setStartDate(startDate);
        }
        if (endDate != null) {
            shopsRoom.setEndDate(endDate);
        }
        if (StringUtils.isNotBlank(roomTypeIds)) {
            shopsRoom.setRoomTypeIds(roomTypeIds);
        }
        if (minPrice != null) {
            shopsRoom.setMinPrice(minPrice);
        }
        if (maxPrice != null) {
            shopsRoom.setMaxPrice(maxPrice);
        }
        if (aggregate != null) {
            shopsRoom.setAggregate(aggregate);
        }
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

    @GetMapping("getRoomTypeList")
    @Operation(summary = "获取房间类型列表")
    public Result getRoomTypeList() {
        return Result.success().put("data", shopsRoomService.getRoomTypeList(1));
    }
}
