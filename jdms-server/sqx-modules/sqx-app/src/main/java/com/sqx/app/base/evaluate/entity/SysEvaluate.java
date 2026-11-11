package com.sqx.app.base.evaluate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wuchang
 * @since 2022-11-10
 */
@Data
@Schema(description = "评分表")
public class SysEvaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "evaluate_id", type = IdType.AUTO)
    @Schema(description = "评分id")
    private Long evaluateId;

    /**
     * 用户id(评分人)
     */
    @Schema(description = "用户id(评分人)")
    private Long userId;

    /**
     * 酒店id
     */
    @Schema(description = "酒店id")
    private Long shopId;

    /**
     * 订单号
     */
    @Schema(description = "订单号")
    private String orderNumber;

    /**
     * 评价内容
     */
    @Schema(description = "评价内容")
    private String evaluateMessage;
    /**
     * 评价图片
     */
    @Schema(description = "评价图片")
    private String evaluateImg;

    /**
     * 评级 1:非常差 2:差 3:一般 4:满意 5:非常满意
     */
    @Schema(description = "评级 1:非常差 2:差 3:一般 4:满意 5:非常满意")
    private Integer satisfactionFlag;

    /**
     * 评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "评价时间")
    private LocalDateTime createTime;

    /**
     * 是否已删除0否 -1是
     */
    @TableLogic
    @Schema(description = "是否已删除0否 -1是")
    private Integer isDelete;
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
     * 酒店名称
     */
    @TableField(exist = false)
    @Schema(description = "酒店名称")
    private String shopName;
    /**
     * 酒店图片
     */
    @TableField(exist = false)
    @Schema(description = "酒店图片")
    private String titleImg;
}
