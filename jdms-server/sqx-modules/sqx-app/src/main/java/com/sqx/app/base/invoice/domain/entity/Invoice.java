package com.sqx.app.base.invoice.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 发票
 *
 * @author sqx
 * @since 2025-02-21 14:13:26
 */
@Getter
@Setter
@TableName("invoice")
@Schema(name = "Invoice", description = "发票")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Schema(description = "内容")
    @TableField("content")
    private String content;

    @Schema(description = "金额")
    @TableField("amount")
    private BigDecimal amount;

    @Schema(description = "类型 1个人 2公司")
    @TableField("type")
    private Integer type;

    /**
     * 状态 1待开票 2已开票
     */
    @Schema(description = "状态 1待开票 2已开票")
    @TableField("status")
    private Integer status;

    /**
     * 纳税人识别号
     */
    @Schema(description = "纳税人识别号")
    @TableField("credit_code")
    private String creditCode;

    /**
     * 地址
     */
    @Schema(description = "地址")
    @TableField("address")
    private String address;

    /**
     * 电话
     */
    @Schema(description = "电话")
    @TableField("phone")
    private String phone;

    /**
     * 开户行
     */
    @Schema(description = "开户行")
    @TableField("open_bank")
    private String openBank;

    /**
     * 银行账号
     */
    @Schema(description = "银行账号")
    @TableField("bank_number")
    private String bankNumber;

    /**
     * 接收邮箱
     */
    @Schema(description = "接收邮箱")
    @TableField("receive_email")
    private String receiveEmail;

    /**
     * 接收手机号
     */
    @Schema(description = "接收手机号")
    @TableField("receive_phone")
    private String receivePhone;


    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 订单id
     */
    @Schema(description = "订单id")
    @TableField("order_id")
    private Long orderId;

    /**
     * 页码
     */
    @TableField(exist = false)
    @Schema(description = "页码")
    private Integer page;

    /**
     * 条数
     */
    @TableField(exist = false)
    @Schema(description = "条数")
    private Integer limit;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @Schema(description = "订单id，多个逗号拼接")
    @TableField(exist = false)
    private String orderIds;

    @Schema(description = "用户名")
    @TableField(exist = false)
    private String userName;

}
