package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与菜单对应关系
 */
@Data
@TableName("sys_role_menu")
@Schema(description = "角色与菜单对应关系")
public class SysRoleMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @Schema(description = "id")
    private Long id;

    /**
     * 角色ID
     */
    @Schema(description = "角色id")
    private Long roleId;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单id")
    private Long menuId;

}
