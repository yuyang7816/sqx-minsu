package com.sqx.app.base.user.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "微信手机号")
public class WxPhone {

    @Schema(description = "微信返回的加密数据")
    private String decryptData;

    @Schema(description = "微信会话密钥")
    private String key;

    @Schema(description = "微信返回的初始向量")
    private String iv;

}
