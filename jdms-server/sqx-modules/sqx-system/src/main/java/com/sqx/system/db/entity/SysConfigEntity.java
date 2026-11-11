package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统配置信息
 */
@Data
@TableName("sys_config")
@Schema(description = "系统配置信息")
public class SysConfigEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;

    @Schema(description = "参数名")
    private String paramKey;

    @Schema(description = "参数值")
    private String paramValue;

    @Schema(description = "状态 0：隐藏 1：显示")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @TableField(exist = false)
    @Schema(description = "参数类型")
    private String paramType;

    @TableField(exist = false)
    @Schema(description = "参数类型描述")
    private String paramTypeDesc;

}
