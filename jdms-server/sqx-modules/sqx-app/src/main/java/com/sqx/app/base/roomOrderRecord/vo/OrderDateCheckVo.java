package com.sqx.app.base.roomOrderRecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 下单时订单时间检测对象
 */
@Data
@Schema(description = "下单时订单时间检测对象")
public class OrderDateCheckVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 不满足订单要求的日期
     */
    @Schema(description = "不满足订单要求的日期")
    private String searchDate;

    /**
     * 剩余房间数
     */
    @Schema(description = "剩余房间数")
    private int roomCount;
}
