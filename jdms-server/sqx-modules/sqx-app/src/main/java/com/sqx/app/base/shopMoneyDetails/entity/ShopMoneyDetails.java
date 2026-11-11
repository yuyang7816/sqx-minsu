package com.sqx.app.base.shopMoneyDetails.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 酒店钱包明细
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
@Getter
@Setter
@TableName("shop_money_details")
@Schema(name = "ShopMoneyDetails对象")
public class ShopMoneyDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "明细id")
    @TableId(value = "details_id", type = IdType.AUTO)
    private Long detailsId;

    @Schema(description = "酒店id")
    private Long shopId;

     @Schema(description = "员工id")
    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "金额")
    private BigDecimal money;

    /**
     * 1增加 2减少
     */
    @Schema(description = "1增加 2减少")
    private Integer type;

    /**
     * 类型 1收入 2提现 3退款 4系统操作
     */
    @Schema(description = "类型 1收入 2提现 3退款 4系统操作")
    private Integer classify;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
