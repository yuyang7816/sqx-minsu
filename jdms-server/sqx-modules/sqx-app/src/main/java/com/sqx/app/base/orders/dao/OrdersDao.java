package com.sqx.app.base.orders.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.orders.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@Mapper
public interface OrdersDao extends BaseMapper<Orders> {

    IPage<OrderVo> getMyOrderList(@Param("pages") Page<OrderVo> pages, @Param("orderVo") OrderVo orderVo);

    OrderVo getMyOrderInfo(@Param("orderId") Long orderId);

    IPage<OrderVo> getOrderList(@Param("pages") Page<OrderVo> pages, @Param("limit") Integer limit, @Param("orderVo") OrderVo orderVo);

    Integer selectOrdersCount(@Param("flag") Integer flag, @Param("time") String time, @Param("orderStatus") Integer orderStatus);

    BigDecimal selectOrdersMoney(@Param("flag") Integer flag, @Param("time") String time, @Param("orderStatus") Integer orderStatus);

    BigDecimal selectOrdersMoney1(@Param("flag") Integer flag, @Param("time") String time, @Param("orderStatus") Integer orderStatus);

    /**
     * 按日期统计订单数、订单金额、平台收入
     */
    List<Map<String, Object>> selectOrderTrend(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 按订单状态统计数量
     */
    List<Map<String, Object>> selectOrderStatusDistribution(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 统计今日订单数
     */
    Integer selectTodayOrderCount(@Param("time") String time);
}
