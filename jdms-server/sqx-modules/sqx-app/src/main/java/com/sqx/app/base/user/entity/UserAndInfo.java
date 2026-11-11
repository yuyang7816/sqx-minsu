package com.sqx.app.base.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户常用信息表
 * @TableName user_and_info
 */
@TableName(value ="user_and_info")
@Data
@Schema(description = "用户常用信息表")
public class UserAndInfo extends PageParam implements Serializable {
    /**
     * 用户常用信息id
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "用户常用信息id")
    private Long id;

    /**
     * 常用信息姓名
     */
    @Schema(description = "常用信息姓名")
    private String infoName;

    /**
     * 常用信息电话
     */
    @Schema(description = "常用信息电话")
    private String infoPhone;

    /**
     * 是否默认：1是 2否
     */
    @Schema(description = "是否默认：1是 2否")
    private Integer isDefault;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}