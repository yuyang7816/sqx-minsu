package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 系统验证码
 */
@Data
@TableName("sys_captcha")
@Schema(description = "系统验证码")
public class SysCaptchaEntity {

    @TableId(type = IdType.INPUT)
    @Schema(description = "uuid")
    private String uuid;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String code;

    /**
     * 过期时间
     */
    @Schema(description = "过期时间")
    private Date expireTime;

}
