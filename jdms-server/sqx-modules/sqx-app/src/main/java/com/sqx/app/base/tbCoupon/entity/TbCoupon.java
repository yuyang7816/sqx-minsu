package com.sqx.app.base.tbCoupon.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
@Data
@Schema(description = "优惠券信息")
public class TbCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "coupon_id", type = IdType.AUTO)
    @Schema(description = "优惠券id")
    private Long couponId;

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
     * 有效期天数
     */
    @Schema(description = "有效期天数")
    private Integer validDays;

    /**
     * 优惠券可使用订单最低金额
     */
    @Schema(description = "优惠券可使用订单最低金额")
    private BigDecimal minMoney;

    /**
     * 优惠券抵扣金额
     */
    @Schema(description = "优惠券抵扣金额")
    private BigDecimal money;

    /**
     * 是否删除 0未删除 1已删除
     */
    @TableLogic
    @Schema(description = "是否删除 0未删除 1已删除")
    private Integer deleteFlag;

    /**
     * 是否启用 0未启用 1已启用
     */
    @Schema(description = "是否启用 0未启用 1已启用")
    private Integer isEnable;

    /**
     * 优惠券类型 0全部可用  1酒店 2民宿
     */
    @Schema(description = "优惠券类型 0全部可用  1酒店 2民宿")
    private Integer couponType;

    /**
     * 最多领取或购买数量(0为不限制数量)
     */
    @Schema(description = "最多领取或购买数量(0为不限制数量)")
    private Integer maxReceive;

    /**
     * 优惠券数量
     */
    @Schema(description = "优惠券数量")
    private Integer couponNum;

}
