package com.sqx.app.base.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 用户
 */
@Data
@Schema(description = "用户信息")
@TableName("tb_user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @Schema(description = "昵称")
    private String userName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "性别 1男 2女")
    private Integer sex;

    @Schema(description = "年龄")
    private Integer age;

    /**
     * 微信小程序openId
     */
    @Schema(description = "微信小程序openId")
    private String openId;

    /**
     * 微信app openId
     */
    @Schema(description = "微信app openId")
    private String appOpenId;

    /**
     * 微信公众号 openId
     */
    @Schema(description = "微信公众号 openId")
    private String accountOpenId;

    /**
     * 员工微信小程序openId
     */
    @Schema(description = "员工小程序openId")
    private String riderOpenId;

    /**
     * 员工微信app openId
     */
    @Schema(description = "员工微信app openId")
    private String riderAppOpenId;

    /**
     * 员工微信公众号 openId
     */
    @Schema(description = "员工微信公众号 openId")
    private String riderAccountOpenId;

    @Schema(description = "收益(可提现)")
    private BigDecimal balance;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "加密盐")
    private String salt;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "状态 1正常 2禁用")
    private Integer status;

    @Schema(description = "来源 APP 小程序  公众号")
    private String platform;

    @Schema(description = "用户设备id")
    private String clientidId;

    @Schema(description = "员工设备id")
    private String riderClientidId;

    @Schema(description = "手机类型 1安卓 2ios")
    private Integer sysPhone;

    @Schema(description = "是否是会员 0否 1是")
    private Integer isVip;

    @Schema(description = "会员到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime vipEndTime;

    @Schema(description = "苹果id")
    private String appleId;

    @Schema(description = "微信提现二维码")
    private String cashQrCode;

    @Schema(description = "支付宝名字")
    private String zhiFuBaoName;

    @Schema(description = "支付宝账号")
    private String zhiFuBao;

    @Schema(description = "1用户 2员工")
    private Integer userType;

    @Schema(description = "邀请码")
    private String invitationCode;

    @Schema(description = "邀请人邀请码")
    private String inviterCode;

    @Schema(description = "邀请人的上级邀请码")
    private String inviterInviterCode;


    @Schema(description = "直属佣金")
    private String zhiRate;

    @Schema(description = "非直属佣金")
    private String feiRate;

    /**
     * 是否是酒店员工：1是 2否 3商户老板
     */
    @Schema(description = "是否是酒店员工：1是 2否 3商户老板")
    private Integer isShopRider;

    /**
     * 酒店id
     */
    @Schema(description = "酒店id")
    private Long shopId;

    /**
     * 酒店审核状态：1已通过 2已拒绝 3待审核
     */
    @Schema(description = "酒店审核状态：1已通过 2已拒绝 3待审核")
    @TableField(exist = false)
    private Integer shopStatus;

    @Schema(description = "用户钱包金额")
    @TableField(exist = false)
    private BigDecimal userMoney;

    /**
     * 用户可提现金额
     */
    @Schema(description = "用户可提现金额")
    @TableField(exist = false)
    private BigDecimal canCashMoney;

    /**
     * 用户累计提现金额
     */
    @Schema(description = "用户累计提现金额")
    @TableField(exist = false)
    private BigDecimal totalCashMoney;
}
