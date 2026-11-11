package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统用户Token
 */
@Data
@TableName("sys_user_token")
@Schema(description = "管理端系统用户Token")
public class SysUserTokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //用户ID
    @TableId(type = IdType.INPUT)
    @Schema(description = "用户id")
    private Long userId;

    //token
    @Schema(description = "token")
    private String token;

    //过期时间
    @Schema(description = "过期时间")
    private Date expireTime;

    //更新时间
    @Schema(description = "更新时间")
    private Date updateTime;
}
