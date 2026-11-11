package com.sqx.app.base.userMoneyDetails.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_money_details")
@Schema(description = "钱包详情")
public class UserMoneyDetails implements Serializable {

    /**
     * 钱包详情id
     */
    @Schema(description = "钱包详情id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 途径 1充值 2订单支付 3分成 4购买会员 5提现 6退款 7系统操作用户余额
     */
    @Schema(description = "途径 1充值 2订单支付 3分成 4购买会员 5提现 6退款 7系统操作用户余额")
    private Integer classify;

    /**
     * 类型 （1：收入；2：支出）
     */
    @Schema(description = "类别（1：收入；2：支出）")
    private Integer type;

    /**
     * 金额
     */
    @Schema(description = "金额")
    private BigDecimal money;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @Schema(description = "创建时间")
    private String createTime;

    /**
     * 关联id
     */
    @Schema(description = "关联id")
    private Long relationId;

    /**
     * 下级等级(1:一级用户 2:二级用户)
     */
    @Schema(description = "下级等级(1:一级用户 2:二级用户)")
    private Integer userLevel;

    /**
     * 消息类型 1用户端 2员工端
     */
    @Schema(description = "消息类型 1用户端 2员工端")
    private Integer userType;
}
