package com.sqx.app.base.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description pay_classify
 * @author fang
 * @date 2022-04-06
 */
@Data
@Schema(description = "充值分类表")
public class PayClassify implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 充值分类id
     */
    @Schema(description = "充值分类id")
    private Long payClassifyId;

    /**
     * 售价
     */
    @Schema(description = "售价")
    private BigDecimal price;

    /**
     * 积分数量
     */
    @Schema(description = "积分数量")
    private BigDecimal money;

    /**
     * 赠送数量
     */
    @Schema(description = "赠送数量")
    private BigDecimal giveMoney;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 是否启用 0否 1是
     */
    @Schema(description = "是否启用 0否 1是")
    private Integer isEnable;


    public PayClassify() {}
}
