package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 数据字典
 */
@Data
@TableName("sys_dict")
@Schema(description = "数据字典")
public class SysDictEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @Schema(description = "id")
    private Long id;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    @Schema(description = "字典名称")
    private String name;

    /**
     * 字典类型
     */
    @NotBlank(message = "字典类型不能为空")
    @Schema(description = "字典类型")
    private String type;

    /**
     * 字典码
     */
    @Schema(description = "字典码")
    @NotBlank(message = "字典码不能为空")
    private String code;

    /**
     * 字典值
     */
    @NotBlank(message = "字典值不能为空")
    @Schema(description = "字典值")
    private String value;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer orderNum;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
    /**
     * 删除标记  -1：已删除  0：正常
     */
    @TableLogic
    @Schema(description = "删除标记  -1：已删除  0：正常")
    private Integer delFlag;

}
