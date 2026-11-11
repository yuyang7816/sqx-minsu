package com.sqx.app.base.shops.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liyuan
 * @description sc_follow
 * @date 2021-08-12
 */
@Data
@Schema(description = "关注信息")
public class ScFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "信息id")
    private Long followId;
    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;
    /**
     * 被关注的id
     */
    @Schema(description = "被关注的id")
    private Long byId;
    /**
     * 类型 1酒店收藏 2商品想要 3关注用户 4收藏动态
     */
    @Schema(description = "类型 1酒店收藏 2商品想要 3关注用户 4收藏动态")
    private Integer classify;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(exist = false)
    @Schema(description = "用户名")
    private String userName;

    @TableField(exist = false)
    @Schema(description = "用户头像")
    private String avatar;

    @TableField(exist = false)
    @Schema(description = "创建时间")
    private String createTimeStr;

    /**
     * isMutualFollow为0是单向关注(即回关)，1是互相关注
     */
    @Schema(description = "是否互相关注")
    @TableField(exist = false)
    private Integer isMutualFollow;

}
