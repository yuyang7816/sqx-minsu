package com.sqx.app.base.user.entity;

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
@TableName("user_money")
@Schema(description = "用户钱包")
public class UserMoney implements Serializable {

    /**
     * 主键id
     */
    @Schema(description = "主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 钱包金额
     */
    @Schema(description = "钱包金额")
    private BigDecimal money;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @TableField(exist = false)
    @Schema(description = "暂未使用")
    private BigDecimal accumulateMoney;

}
