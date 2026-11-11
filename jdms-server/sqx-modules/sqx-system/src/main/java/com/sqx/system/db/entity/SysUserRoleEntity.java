package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户与角色对应关系
 */
@Data
@TableName("sys_user_role")
@Schema(description = "用户与角色对应关系")
public class SysUserRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @Schema(description = "id")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 角色ID
     */
    @Schema(description = "角色id")
    private Long roleId;


}
