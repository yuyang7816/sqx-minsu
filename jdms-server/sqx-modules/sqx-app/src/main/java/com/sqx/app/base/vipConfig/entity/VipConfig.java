package com.sqx.app.base.vipConfig.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sqx.app.base.utils.EmptyStringToNullLongDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * vip配置表
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
@Getter
@Setter
@TableName("vip_config")
@Schema(name = "VIP配置对象")
public class VipConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "vipId")
    @TableId(value = "vip_id", type = IdType.AUTO)
    @JsonDeserialize(using = EmptyStringToNullLongDeserializer.class)
    private Long vipId;

    @Schema(description = "vip标题")
    @TableField(condition = SqlCondition.LIKE)
    private String vipTitle;

    @Schema(description = "vip价格")
    private BigDecimal vipMoney;

    @Schema(description = "月份")
    private Integer monthCount;

    @Schema(description = "是否开启0否 1是")
    private Integer isEnable;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
