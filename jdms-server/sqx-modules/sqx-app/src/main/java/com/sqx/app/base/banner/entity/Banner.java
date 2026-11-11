package com.sqx.app.base.banner.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * @author fang
 * @date 2020/7/9
 */
@Data
@TableName("banner")
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "banner图")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * banner图id
     */
    @TableId(type = IdType.INPUT)
    @Schema(description = "banner图id")
    private Long id;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private String createTime;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 图片地址
     */
    @Schema(description = "图片地址")
    private String imageUrl;

    /**
     * 状态 1正常 2隐藏
     */
    @Schema(description = "状态 1正常 2隐藏")
    private Integer state;

    /**
     * 分类 1 banner图  2 首页分类
     */
    @Schema(description = "分类 1 banner图  2 首页分类")
    private Integer classify;

    /**
     * 跳转地址
     */
    @Schema(description = "跳转地址")
    private String url;

    /**
     * 顺序
     */
    @Schema(description = "顺序")
    private Integer sort;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String describess;

}