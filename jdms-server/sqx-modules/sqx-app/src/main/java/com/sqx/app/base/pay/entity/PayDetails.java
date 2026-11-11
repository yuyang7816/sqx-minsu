package com.sqx.app.base.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 充值记录
 *
 * @author fang 2020-05-14
 */
@Data
@TableName("pay_details")
@Schema(description = "充值记录")
public class PayDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值记录id
     */
    @TableId(type = IdType.INPUT)
    @Schema(description = "充值记录id")
    private Long id;

    /**
     * 分类（1微信 1app微信 2微信公众号 3微信小程序 4支付宝）
     */
    @Schema(description = "分类（1微信 1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝）")
    private Integer classify;

    /**
     * 支付类型  1.订单支付 2余额充值 3开通会员
     */
    @Schema(description = "支付类型  1.订单支付 2余额充值 3开通会员")
    private Integer type;

    /**
     * 订单id
     */
    @Schema(description = "订单id")
    private String orderId;

    /**
     * 支付宝交易订单号
     */
    @Schema(description = "支付宝交易订单号")
    private String tradeNo;

    /**
     * 充值积分
     */
    @Schema(description = "充值积分")
    private Double money;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 0待支付 1支付成功 2失败
     */
    @Schema(description = "0待支付 1支付成功 2失败")
    private Integer state;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String payTime;

    /**
     * 关联id
     */
    @Schema(description = "关联id")
    private String relationId;

    /**
     * 退款订单号
     */
    @Schema(description = "退款订单号")
    private String refundNumber;

}
