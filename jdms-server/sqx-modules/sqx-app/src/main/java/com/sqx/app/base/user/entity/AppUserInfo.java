package com.sqx.app.base.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "微信用户信息")
public class AppUserInfo {

    @Schema(description = "微信用户openId")
    private String openid;

    @Schema(description = "微信用户昵称")
    private String nickname;

    @Schema(description = "微信用户性别")
    private int sex;

    @Schema(description = "微信用户所在省份")
    private String province;

    @Schema(description = "微信用户所在城市")
    private String city;

    @Schema(description = "微信用户所在地区")
    private String country;

    @Schema(description = "微信用户头像")
    private String headimgurl;

    @Schema(description = "微信用户UnionId(跨应用识别同一个用户的关键)")
    private String unionid;

    @Schema(description = "微信用户权限")
    private List<String> privilege;


}
