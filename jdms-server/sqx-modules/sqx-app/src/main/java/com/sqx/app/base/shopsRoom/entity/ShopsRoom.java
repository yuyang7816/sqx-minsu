package com.sqx.app.base.shopsRoom.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.app.base.roomDeviceClassify.entity.RoomDeviceClassify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 房间表
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@Getter
@Setter
@TableName("shops_room")
@Schema(description = "房间表")
public class ShopsRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "房间id")
    @TableId(value = "room_id", type = IdType.AUTO)
    private Long roomId;

    @Schema(description = "酒店id")
    private Long shopId;

    @Schema(description = "房间类型id")
    private Long roomTypeId;

    @Schema(description = "房间类型ids")
    @TableField(exist = false)
    private String roomTypeIds;

    @Schema(description = "房间类型名称")
    @TableField(condition = SqlCondition.LIKE)
    private String roomType;

    @Schema(description = "酒店名称")
    @TableField(condition = SqlCondition.LIKE)
    private String shopName;

    @Schema(description = "房间名称")
    @TableField(condition = SqlCondition.LIKE)
    private String roomName;

    @Schema(description = "房间数量")
    private Integer roomCount;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "房间封面图")
    private String roomTitleImg;

    @Schema(description = "房间轮播图")
    private String roomImg;

    @Schema(description = "原价格")
    private BigDecimal money;

    @Schema(description = "会员价格")
    private BigDecimal memberMoney;

    @Schema(description = "配置ids")
    private String classifyIds;

    /**
     * 是否启用0否 1是
     */
    @Schema(description = "是否启用0否 1是")
    private Integer isEnable;

    @Schema(description = "入住时间")
    @JsonFormat(pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime checkInTime;

    @Schema(description = "离店时间")
    @JsonFormat(pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime checkOutTime;

    @Schema(description = "餐食规则")
    private String foodPolicy;

    @Schema(description = "宠物规则")
    private String petRule;

    @Schema(description = "酒店提示")
    private String hotelTips;

    /**
     * 房屋面积
     */
    @Schema(description = "房屋面积")
    private String floorArea;

    /**
     * 楼层
     */
    @Schema(description = "楼层")
    private String floor;

    /**
     * 房间状态：1空闲 2使用中
     */
    @Schema(description = "房间状态：1空闲 2使用中")
    private Integer roomStatus;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(exist = false)
    @Schema(description = "配置列表")
    private List<RoomDeviceClassify> classifyList;

    /**
     * 配置列表名称拼接
     */
    @TableField(exist = false)
    @Schema(description = "配置列表名称拼接")
    private String classifyNames;

    /**
     * 剩余房间数
     */
    @TableField(exist = false)
    @Schema(description = "剩余房间数")
    private int residueCount;

    /**
     * 入住日期
     */
    @TableField(exist = false)
    @Schema(description = "入住日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 离店日期
     */
    @TableField(exist = false)
    @Schema(description = "离店日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 最低价格筛选
     */
    @TableField(exist = false)
    @Schema(description = "最低价格筛选")
    private BigDecimal minPrice;

    /**
     * 最高价格筛选
     */
    @TableField(exist = false)
    @Schema(description = "最高价格筛选")
    private BigDecimal maxPrice;

    /**
     * 复制来源房间id（仅发布时使用）
     */
    @TableField(exist = false)
    @Schema(description = "复制来源房间id")
    private Long copyFromRoomId;

    /**
     * 批量创建数量（仅新建时使用，>1时批量创建多条记录）
     */
    @TableField(exist = false)
    @Schema(description = "批量创建数量")
    private Integer batchCount;

    /**
     * 是否聚合展示（C端列表按售卖单元合并）
     */
    @TableField(exist = false)
    @Schema(description = "是否聚合展示 1是 0否")
    private Integer aggregate;

    /**
     * 同组房间总数（聚合展示时使用）
     */
    @TableField(exist = false)
    @Schema(description = "同组房间总数")
    private Integer totalRoomCount;

    /**
     * 可售房间数（聚合展示时使用）
     */
    @TableField(exist = false)
    @Schema(description = "可售房间数")
    private Integer availableRoomCount;

    /**
     * 复制来源房间名称（复制预填时使用）
     */
    @TableField(exist = false)
    @Schema(description = "复制来源房间名称")
    private String copySourceRoomName;
}
