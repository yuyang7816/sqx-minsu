package com.sqx.app.base.tbCoupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
@Data
@Schema(description = "用户优惠券表")
public class TbCouponUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户优惠券id")
    private Long id;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 用户昵称
     */
    @TableField(exist = false)
    @Schema(description = "用户昵称")
    private String userName;

    /**
     * 用户头像
     */
    @TableField(exist = false)
    @Schema(description = "用户头像")
    private String avatar;

    /**
     * 用户手机号
     */
    @TableField(exist = false)
    @Schema(description = "用户手机号")
    private String phone;

    /**
     * 优惠券名称
     */
    @TableField(condition = SqlCondition.LIKE)
    @Schema(description = "优惠券名称")
    private String couponName;

    /**
     * 优惠券图片
     */
    @Schema(description = "优惠券图片")
    private String couponPicture;

    /**
     * 优惠券领取时间
     */
    @Schema(description = "优惠券领取时间")
    private Date createTime;

    /**
     * 优惠券使用时间
     */
    @Schema(description = "优惠券使用时间")
    private Date employTime;

    /**
     * 优惠券过期时间
     */
    @Schema(description = "优惠券过期时间")
    private Date expirationTime;

    /**
     * 优惠券类型 0全部可用  1酒店 2民宿
     */
    @Schema(description = "优惠券类型 0全部可用  1酒店 2民宿")
    private Integer couponType;

    /**
     * 优惠券可使用订单最低金额
     */
    @Schema(description = "优惠券可使用订单最低金额")
    private BigDecimal minMoney;

    /**
     * 优惠券金额
     */
    @Schema(description = "优惠券金额")
    private BigDecimal money;

    /**
     * 优惠券状态 0正常  1已使用  2已失效
     */
    @Schema(description = "优惠券状态 0正常  1已使用  2已失效")
    private Integer status;

    /**
     * 有效天数
     */
    @Schema(description = "有效天数")
    private Integer validDays;

    /**
     * 优惠券id
     */
    @Schema(description = "优惠券id")
    private Long couponId;

    /**
     * 优惠券信息
     */
    @TableField(exist = false)
    @Schema(description = "优惠券信息")
    private TbCoupon coupon;
}
