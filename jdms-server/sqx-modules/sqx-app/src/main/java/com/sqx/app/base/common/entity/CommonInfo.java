package com.sqx.app.base.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用配置管理
 */
@Data
@TableName("common_info")
@Schema(description = "通用配置管理")
public class CommonInfo implements Serializable {

    @TableId(type = IdType.INPUT)
    @Schema(description = "通用配置id")
    private long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createAt;

    @Schema(description = "通用配置类型")
    private Integer type; //1表示客服二维码 2表示公众号二维码 3表示全局佣金是否开启 4注册客服渠道id配置 5、佣金规则 6、

    @Schema(description = "通用配置值")
    private String value;

    @Schema(description = "暂未使用")
    private String max;

    @Schema(description = "通用配置描述")
    private String min;

    @Schema(description = "通用配置条件分类")
    private String conditionFrom;

}

