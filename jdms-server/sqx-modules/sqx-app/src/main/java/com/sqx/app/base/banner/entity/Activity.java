package com.sqx.app.base.banner.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动推广
 */
@Data
@TableName("activity")
@Schema(description = "活动推广")
public class Activity implements Serializable {
    @TableId(type = IdType.INPUT)
    @Schema(description = "活动推广id")
    private Long id;

    @Schema(description = "创建时间")
    private String createAt;

    @Schema(description = "图片地址")
    private String imageUrl;

    @Schema(description = "跳转地址")
    private String url;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "状态 1正常 2隐藏")
    private String state;

}
