package com.sqx.app.base.rechargeConfig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sqx.app.base.tbCoupon.entity.TbCoupon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author wuchang
 * @since 2022-11-04
 */
@Data
@Schema(description = "充值配置")
public class RechargeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置id
     */
    @TableId(value = "recharge_id", type = IdType.AUTO)
    @Schema(description = "配置id")
    private Long rechargeId;

    /**
     * 金额
     */
    @Schema(description = "金额")
    private BigDecimal money;

    /**
     * 优惠券id
     */
    @Schema(description = "优惠券id")
    private Long couponId;

    /**
     * 优惠券数量
     */
    @Schema(description = "优惠券数量")
    private Integer couponNum;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 是否开启(0否 1是)
     */
    @Schema(description = "是否开启(0否 1是)")
    private Integer isEnable;

    @TableField(exist = false)
    @Schema(description = "优惠券信息")
    private TbCoupon tbCoupon;

}
