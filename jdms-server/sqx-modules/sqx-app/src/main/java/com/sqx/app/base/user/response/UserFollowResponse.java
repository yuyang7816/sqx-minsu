package com.sqx.app.base.user.response;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
// 暂未使用
public class UserFollowResponse implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户id")
    private Long userId;
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String userName;
    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;
    /**
     * 关注状态
     */
    private int status;
    /**
     * 时间
     */
    private String updateTime;

    private Integer sex;

    private Integer age;

    /**
     * 接单id
     */
    @TableField(exist = false)
    private Long  takingId;

}
