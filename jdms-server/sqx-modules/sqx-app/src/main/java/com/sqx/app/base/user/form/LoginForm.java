package com.sqx.app.base.user.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录表单
 *
 */
@Data
@Schema(description = "登录表单")
public class LoginForm {

    @Schema(description = "手机号")
    @NotBlank(message="手机号不能为空")
    private String mobile;

    @Schema(description = "密码")
    @NotBlank(message="密码不能为空")
    private String password;

}
