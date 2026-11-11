package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理
 */
@Data
@TableName("sys_menu")
@Schema(description = "菜单管理")
public class SysMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId
    @Schema(description = "菜单id")
    private Long menuId;

    /**
     * 父菜单ID，一级菜单为0
     */
    @Schema(description = "父菜单id")
    private Long parentId;

    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    @Schema(description = "父菜单名称")
    private String parentName;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 菜单URL
     */
    @Schema(description = "菜单url")
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    @Schema(description = "授权(多个用逗号分隔，如：user:list,user:create)")
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    @Schema(description = "类型  0：目录  1：菜单  2：按钮")
    private Integer type;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer orderNum;

    /**
     * ztree属性
     */
    @TableField(exist = false)
    @Schema(description = "zTree属性")
    private Boolean open;

    @TableField(exist = false)
    @Schema(description = "list列表")
    private List<?> list;

}
