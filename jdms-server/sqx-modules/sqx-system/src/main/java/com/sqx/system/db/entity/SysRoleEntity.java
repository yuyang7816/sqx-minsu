package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 */
@Data
@TableName("sys_role")
@Schema(description = "角色")
public class SysRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 创建者ID
     */
    @Schema(description = "创建者ID")
    private Long createUserId;

    @TableField(exist = false)
    @Schema(description = "菜单id列表")
    private List<Long> menuIdList;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;


}
