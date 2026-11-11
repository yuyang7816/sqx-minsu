package com.sqx.app.base.shopsRoom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 房间类型表
 * @TableName shops_room_type
 */
@TableName(value ="shops_room_type")
@Data
@Schema(description = "房间类型表")
public class ShopsRoomType extends PageParam implements Serializable {
    /**
     * 房间类型表id
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "房间类型表id")
    private Long id;

    /**
     * 类型名称
     */
    @Schema(description = "类型名称")
    private String typeName;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}