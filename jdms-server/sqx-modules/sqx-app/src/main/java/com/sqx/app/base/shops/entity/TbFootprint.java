package com.sqx.app.base.shops.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName tb_footprint(用户浏览足迹表)
 */
@Data
@Schema(description = "用户浏览足迹表")
public class TbFootprint implements Serializable {
    /**
     * 商品id
     */
    @Schema(description = "商品id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 酒店id
     */
    @Schema(description = "酒店id")
    private Long shopId;

    /**
     * 访问时间
     */
    @Schema(description = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;

    /**
     * 商品描述
     */
    @TableField(exist = false)
    @Schema(description = "商品描述")
    private String goodTitle;

    /**
     * 封面图
     */
    @TableField(exist = false)
    @Schema(description = "封面图")
    private String coverImg;

    /**
     * 商品名称
     */
    @TableField(exist = false)
    @Schema(description = "商品名称")
    private String goodName;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    @TableField(exist = false)
    private String label;

    /**
     * 分类名称
     */
    @TableField(exist = false)
    @Schema(description = "分类名称")
    private String typeName;

    /**
     * 标价
     */
    @TableField(exist = false)
    @Schema(description = "标价")
    private BigDecimal price;

    /**
     * 统计有多少人想要这件商品
     */
    @TableField(exist = false)
    @Schema(description = "统计有多少人想要这件商品")
    private int followCount;

    /**
     * 是否包邮 0否 1是
     */
    @Schema(description = "是否包邮 0否 1是")
    @TableField(exist = false)
    private Integer isFreeMail;

    /**
     * 是否已出售
     */
    @Schema(description = "是否已出售")
    @TableField(exist = false)
    private Integer status;

    /**
     * 商品视频路径
     */
    @Schema(description = "商品视频路径")
    @TableField(exist = false)
    private String videos;

    @TableField(exist = false)
    @Schema(description = "店铺")
    private Shops shops;

    private static final long serialVersionUID = 1L;
}