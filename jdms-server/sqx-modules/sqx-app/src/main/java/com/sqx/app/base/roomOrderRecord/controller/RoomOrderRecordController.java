package com.sqx.app.base.roomOrderRecord.controller;

import com.sqx.app.base.roomOrderRecord.service.IRoomOrderRecordService;
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
 * 订单房间表 前端控制器
 * </p>
 *
 * @author wuChang
 * @since 2023-08-14
 */
@RestController
@RequestMapping("/app/roomOrderRecord/")
@Tag(name = "订单房间表---用户端")
public class RoomOrderRecordController {
    @Autowired
    private IRoomOrderRecordService roomOrderRecordService;

    @GetMapping("getRoomCountByDate")
    @Operation(summary = "根据日期获取房间数")
    public Result getRoomCountByDate(@RequestParam(required = false) @Parameter(description = "房间id") Long roomId,
                                     @RequestParam(required = false) @Parameter(description = "查找日期") String searchDate){
        return Result.success().put("data", roomOrderRecordService.getRoomCountByDate(roomId,searchDate));
    }
}
