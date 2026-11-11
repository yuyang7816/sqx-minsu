package com.sqx.app.base.shopsUserRole.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户酒店权限
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
@Getter
@Setter
@TableName("shops_user_role")
@Schema(name = "用户酒店权限")
public class ShopsUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "权限id")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "酒店id")
    private Long shopId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
