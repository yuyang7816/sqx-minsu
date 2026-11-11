package com.sqx.app.base.roomDeviceClassify.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 房间配置表
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@Data
@TableName("room_device_classify")
@Schema(description = "房间配置表")
public class RoomDeviceClassify implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "选项id")
    @TableId(value = "classify_id", type = IdType.AUTO)
    private Long classifyId;

    @TableField(condition = SqlCondition.LIKE)
    @Schema(description = "描述")
    private String title;

    @Schema(description = "图片")
    private String img;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
