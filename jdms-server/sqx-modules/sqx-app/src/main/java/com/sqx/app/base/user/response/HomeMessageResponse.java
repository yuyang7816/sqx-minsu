package com.sqx.app.base.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 首页信息返回实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "首页信息返回实体")
public class HomeMessageResponse implements Serializable {

    /**
     * 总用户数
     */
    @Schema(description = "总用户数")
    private  int totalUsers;

    /**
     *今日新增
     */
    @Schema(description = "今日新增")
    private  int newToday;

    /**
     *本月新增
      */
    @Schema(description = "本月新增")
    private  int newMonth;

    /**
     * 本年新增
     */
    @Schema(description = "本年新增")
    private  int newYear;

    /**
     * 总收入
     */
    @Schema(description = "总收入")
    private Double totalRevenue;

    /**
     * 今日收入
     */
    @Schema(description = "今日收入")
    private Double todayRevenue;

    /**
     * 本月收入
     */
    @Schema(description = "本月收入")
    private Double monthRevenue;

    /**
     * 本年收入
     */
    @Schema(description = "本年收入")
    private Double yearRevenue;

    /**
     * 今日订单数
     */
    @Schema(description = "今日订单数")
    private Integer todayOrderCount;

}
