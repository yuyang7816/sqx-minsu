package com.sqx.app.base.dataCenter;

import com.sqx.app.base.orders.service.IOrdersService;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/dataCenter/")
@Tag(name = "数据中心")
public class DataCenterController {

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private UserService userService;

    @GetMapping("/selectOrdersDate")
    @Operation(summary = "订单统计")
    public Result selectOrdersDate(@RequestParam(required = false) @Parameter(description = "1:今天 2:本月 3:本年") Integer type,
                                   @RequestParam(required = false) @Parameter(description = "时间") String time) {
        return Result.success().put("data", ordersService.selectOrdersDate(type, time));
    }

    @GetMapping("/overview")
    @Operation(summary = "数据中心概览")
    public Result overview(@RequestParam @Parameter(description = "开始时间 yyyy-MM-dd") String startTime,
                           @RequestParam @Parameter(description = "结束时间 yyyy-MM-dd") String endTime) {
        LocalDate start = LocalDate.parse(startTime);
        LocalDate end = LocalDate.parse(endTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 生成日期列表并初始化默认值
        List<String> dateList = new ArrayList<>();
        Map<String, Integer> orderCountMap = new LinkedHashMap<>();
        Map<String, BigDecimal> orderAmountMap = new LinkedHashMap<>();
        Map<String, BigDecimal> platformIncomeMap = new LinkedHashMap<>();
        Map<String, Integer> newUserMap = new LinkedHashMap<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            String dateStr = date.format(formatter);
            dateList.add(dateStr);
            orderCountMap.put(dateStr, 0);
            orderAmountMap.put(dateStr, BigDecimal.ZERO);
            platformIncomeMap.put(dateStr, BigDecimal.ZERO);
            newUserMap.put(dateStr, 0);
        }

        // 订单趋势
        List<Map<String, Object>> orderTrend = ordersService.selectOrderTrend(startTime, endTime);
        for (Map<String, Object> row : orderTrend) {
            String date = row.get("date").toString();
            if (orderCountMap.containsKey(date)) {
                orderCountMap.put(date, ((Number) row.get("orderCount")).intValue());
                orderAmountMap.put(date, toBigDecimal(row.get("orderAmount")));
                platformIncomeMap.put(date, toBigDecimal(row.get("platformIncome")));
            }
        }

        // 新增用户趋势
        List<Map<String, Object>> userTrend = userService.selectNewUserTrend(startTime, endTime);
        for (Map<String, Object> row : userTrend) {
            String date = row.get("date").toString();
            if (newUserMap.containsKey(date)) {
                newUserMap.put(date, ((Number) row.get("newUserCount")).intValue());
            }
        }

        // 订单状态分布
        List<Map<String, Object>> statusDistribution = ordersService.selectOrderStatusDistribution(startTime, endTime);
        Map<Integer, String> statusNameMap = new HashMap<>();
        statusNameMap.put(0, "待支付");
        statusNameMap.put(1, "待核销");
        statusNameMap.put(2, "进行中");
        statusNameMap.put(3, "已完成");
        statusNameMap.put(4, "已取消");

        List<Map<String, Object>> statusData = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("status", i);
            item.put("name", statusNameMap.get(i));
            item.put("value", 0);
            statusData.add(item);
        }
        for (Map<String, Object> row : statusDistribution) {
            Integer status = ((Number) row.get("status")).intValue();
            Integer count = ((Number) row.get("count")).intValue();
            for (Map<String, Object> item : statusData) {
                if (item.get("status").equals(status)) {
                    item.put("value", count);
                    break;
                }
            }
        }

        Map<String, Object> trend = new HashMap<>();
        trend.put("dates", dateList);
        trend.put("orderCountList", new ArrayList<>(orderCountMap.values()));
        trend.put("orderAmountList", new ArrayList<>(orderAmountMap.values()));
        trend.put("platformIncomeList", new ArrayList<>(platformIncomeMap.values()));
        trend.put("newUserCountList", new ArrayList<>(newUserMap.values()));

        Map<String, Object> result = new HashMap<>();
        result.put("trend", trend);
        result.put("statusDistribution", statusData);

        return Result.success().put("data", result);
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return new BigDecimal(value.toString());
    }
}
