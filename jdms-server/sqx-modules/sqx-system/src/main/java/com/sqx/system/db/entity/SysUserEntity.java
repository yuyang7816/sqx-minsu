package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.core.validator.group.AddGroup;
import com.sqx.common.core.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 */
@Data
@TableName("sys_user")
@Schema(description = "管理端系统用户")
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = AddGroup.class)
    @Schema(description = "密码")
    private String password;

    /**
     * 盐
     */
    @Schema(description = "盐")
    private String salt;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Email(message = "邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    @Schema(description = "状态  0：禁用   1：正常")
    private Integer status;

    /**
     * 角色ID列表
     */
    @TableField(exist = false)
    @Schema(description = "角色id列表")
    private List<Long> roleIdList;

    /**
     * 创建者ID
     */
    @Schema(description = "创建者id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(exist = false)
    @Schema(description = "邀请码")
    private String inviterCode;

}
