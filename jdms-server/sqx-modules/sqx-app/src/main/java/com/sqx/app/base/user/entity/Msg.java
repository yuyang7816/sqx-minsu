package com.sqx.app.base.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fang
 * @date 2020/7/10
 */
@Data
@TableName("msg")
@Schema(description = "用户验证码")
public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    @Schema(description = "id")
    private Long id;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "手机号")
    private String phone;


}