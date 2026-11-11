package com.sqx.app.base.orders.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.app.base.roomDeviceClassify.entity.RoomDeviceClassify;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@Getter
@Setter
@Schema(description = "订单实体类")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单id")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 下单用户id
     */
    @Schema(description = "下单用户id")
    private Long userId;
    /**
     * 下单用户昵称
     */
    @Schema(description = "下单用户昵称")
    private String userName;
    /**
     * 订单号
     */
    @Schema(description = "订单号")
    private String orderNumber;
    /**
     * 下单用户手机号
     */
    @Schema(description = "下单用户手机号")
    private String userPhone;
    /**
     * 酒店id
     */
    @Schema(description = "酒店id")
    private Long shopId;
    /**
     * 酒店名称
     */
    @Schema(description = "酒店名称")
    private String shopName;
    /**
     * 房间id
     */
    @Schema(description = "房间id")
    private Long roomId;
    /**
     * 房间名称
     */
    @Schema(description = "房间名称")
    private String roomName;

    /**
     * 房间类型id
     */
    @Schema(description = "房间类型id")
    private Long roomTypeId;
    /**
     * 订单备注
     */
    @Schema(description = "订单备注")
    private String remarks;
    /**
     * 订单类型 1酒店 2民宿
     */
    @Schema(description = "订单类型 1酒店 2民宿")
    private Integer orderType;
    /**
     * 房间数
     */
    @Schema(description = "房间数")
    private Integer roomCount;
    /**
     * 入住时间
     */
    @Schema(description = "入住时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    /**
     * 离店时间
     */
    @Schema(description = "离店时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    /**
     * 核销时间
     */
    @Schema(description = "核销时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime verificationTime;
    /**
     * 核销用户id
     */
    @Schema(description = "核销用户id")
    private Long verificationUserId;
    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String realName;
    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String phone;
    /**
     * 订单状态 0待支付 1待核销 2进行中 3完成 4已取消
     */
    @Schema(description = "订单状态 0待支付 1待核销 2进行中 3完成 4已取消")
    private Integer orderStatus;
    /**
     * 支付方式 0余额 1微信 2支付宝
     */
    @Schema(description = "支付方式 0余额 1微信 2支付宝")
    private Integer payWay;
    /**
     * 是否已支付0否 1是
     */
    @Schema(description = "是否已支付0否 1是")
    private Integer isPay;
    /**
     * 是否已评价0否 1是
     */
    @Schema(description = "是否已评价0否 1是")
    private Integer isEvaluate;
    /**
     * 优惠券id
     */
    @Schema(description = "优惠券id")
    private Long couponId;
    /**
     * 优惠金额
     */
    @Schema(description = "优惠金额")
    private BigDecimal couponMoney;

    /**
     * 优惠券名称（详情展示用，非数据库字段）
     */
    @TableField(exist = false)
    @Schema(description = "优惠券名称")
    private String couponName;

    /**
     * 订单金额
     */
    @Schema(description = "订单金额")
    private BigDecimal orderMoney;
    /**
     * 支付金额
     */
    @Schema(description = "支付金额")
    private BigDecimal payMoney;
    /**
     * 退款金额
     */
    @Schema(description = "退款金额")
    private BigDecimal refundMoney;
    /**
     * 酒店收入
     */
    @Schema(description = "酒店收入")
    private BigDecimal shopMoney;
    /**
     * 平台收入
     */
    @Schema(description = "平台收入")
    private BigDecimal platformMoney;
    /**
     * 上级id
     */
    @Schema(description = "上级id")
    private Long userScaleUserId;
    /**
     * 上级抽佣金额
     */
    @Schema(description = "上级抽佣金额")
    private BigDecimal userScaleMoney;
    /**
     * 上上级id
     */
    @Schema(description = "上上级id")
    private Long userTwoUserId;
    /**
     * 上上级抽佣金额
     */
    @Schema(description = "上上级抽佣金额")
    private BigDecimal userTwoMoney;
    /**
     * 到店时间
     */
    @Schema(description = "到店时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 支付时间
     */
    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    /**
     * 是否是会员订单 0否 1是
     */
    @Schema(description = "是否是会员订单 0否 1是")
    private Integer isVipOrder;


    /**
     * 是否开票 1=未开票 2=开票中 3=已开票
     */
    @TableField(value = "is_invoice")
    @Schema(description = "是否开票 1=未开票 2=开票中 3=已开票")
    private Integer isInvoice;

    /**
     * 是否勾选发票：1是 2否
     */
    @Schema(description = "是否勾选发票：1是 2否")
    private Integer checkInvoice;

    /**
     * 常用人信息id
     */
    @Schema(description = "常用人信息id")
    private Long userInfoId;

    /**
     * 酒店封面图
     */
    @Schema(description = "酒店封面图")
    private String coverImage;

    /**
     * 房间的酒店提示
     */
    @Schema(description = "房间的酒店提示")
    private String warmReminder;

    @TableField(exist = false)
    @Schema(description = "配置列表")
    private List<RoomDeviceClassify> classifyList;

    /**
     * 配置列表名称拼接
     */
    @TableField(exist = false)
    @Schema(description = "配置列表名称拼接")
    private String classifyNames;


    @Schema(description = "标题")
    @TableField(exist = false)
    private String title;

    @Schema(description = "内容")
    @TableField(exist = false)
    private String content;

    @Schema(description = "金额")
    @TableField(exist = false)
    private BigDecimal amount;

    @Schema(description = "类型 1个人 2公司")
    @TableField(exist = false)
    private Integer type;

    /**
     * 状态 1待开票 2已开票
     */
    @Schema(description = "状态 1待开票 2已开票")
    @TableField(exist = false)
    private Integer status;

    /**
     * 纳税人识别号
     */
    @Schema(description = "纳税人识别号")
    @TableField(exist = false)
    private String creditCode;

    /**
     * 地址
     */
    @Schema(description = "地址")
    @TableField(exist = false)
    private String address;


    /**
     * 开户行
     */
    @Schema(description = "开户行")
    @TableField(exist = false)
    private String openBank;

    /**
     * 银行账号
     */
    @Schema(description = "银行账号")
    @TableField(exist = false)
    private String bankNumber;

    /**
     * 接收邮箱
     */
    @Schema(description = "接收邮箱")
    @TableField(exist = false)
    private String receiveEmail;

    /**
     * 接收手机号
     */
    @Schema(description = "接收手机号")
    @TableField(exist = false)
    private String receivePhone;


    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
