package com.sqx.app.base.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.app.base.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fang
 * @date 2020/7/13
 */
@Data
@TableName("message_info")
@Schema(description = "消息")
public class MessageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "消息id")
    private Long id;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private String createAt;

    /**
     * 图片
     */
    @Schema(description = "图片")
    private String image;

    /**
     * is_see
     */
    @Schema(description = "是否已读 0未")
    private String isSee;

    /**
     * send_state
     */
    @Schema(description = "发送状态")
    private String sendState;

    /**
     * send_time
     */
    @Schema(description = "发送时间")
    private String sendTime;

    /**
     * 分类  4订单消息  5 用户消息
     */
    @Schema(description = "分类  4订单消息  5 用户消息")
    private String state;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String url;

    /**
     * type
     */
    @Schema(description = "类型")
    private String type;

    /**
     * platform
     */
    @Schema(description = "来源平台")
    private String platform;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private String userId;

    /**
     * 用户类型
     */
    @Schema(description = "用户类型")
    private Integer userType;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String userName;

    /**
     * 用户信息
     */
    @TableField(exist = false)
    @Schema(description = "用户信息")
    private UserEntity userEntity;

    public MessageInfo() {
    }

}