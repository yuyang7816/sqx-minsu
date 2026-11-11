package com.sqx.app.base.roomOrderRecord.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单房间表
 * </p>
 *
 * @author wuChang
 * @since 2023-08-14
 */
@Data
@TableName("room_order_record")
@Schema(name = "订单房间表")
public class RoomOrderRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "记录id")
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;

    @Schema(description = "订单id")
    private Long orderId;

    @Schema(description = "酒店id")
    private Long shopId;

    @Schema(description = "房间id")
    private Long roomId;

    @Schema(description = "房间占用数量")
    private Integer roomCount;

    @Schema(description = "入驻时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @Schema(description = "离店时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
