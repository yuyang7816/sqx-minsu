package com.sqx.app.base.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 广告位
 */
@Data
@TableName("app")
@Schema(description = "app升级配置")
public class App implements Serializable {

    @TableId
    @Schema(description = "app升级配置id")
    private Long id;

    @Schema(description = "创建时间")
    private String createAt;

    @Schema(description = "安卓升级地址")
    private String androidWgtUrl;

    @Schema(description = "苹果升级地址")
    private String iosWgtUrl;

    @Schema(description = "通用升级地址")
    private String wgtUrl;

    @Schema(description = "安卓版本")
    private String version;

    @Schema(description = "苹果版本")
    private String iosVersion;

    @Schema(description = "是否强制升级")
    private String method;

    @Schema(description = "描述")
    private String des;

}

