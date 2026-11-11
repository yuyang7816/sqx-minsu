package com.sqx.app.base.orders.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.orders.vo.OrderVo;
import com.sqx.app.base.utils.Result;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
public interface IOrdersService extends IService<Orders> {

    Result addOrder(Orders orders);

    IPage<OrderVo> getMyOrderList(Integer page, Integer limit, OrderVo orderVo);

    OrderVo getMyOrderInfo( Long orderId);

    /**
     * 用户取消订单前预览扣费信息（供弹框提示）
     */
    Result cancelOrderPreview(Long userId, Long orderId);

    /**
     *
     * @param userId
     * @param orderId
     * @param refundUser 1用户端取消订单 2员工端取消订单 3管理端取消订单
     * @param refundMoney
     * @return
     */
    Result cancelOrder(Long userId, Long orderId, Integer refundUser, BigDecimal refundMoney);

    /**
     * 订单取消后退还用户优惠券
     */
    void restoreOrderCoupon(Long couponUserId);

    Result finishOrder(Long orderId);

    void setCommission(Long orderId, Integer classify);

    Result orderCallback(Long orderId, Integer classify, BigDecimal payMoney);

    Result cancelVerification(Long userId, String orderNumber);

    Result updateRemarks(Long userId, Long orderId, String remarks);

    IPage<OrderVo> getOrderList(Integer page, Integer limit, OrderVo orderVo);

    Result staffFinishOrder(Long userId, Long orderId);

    HashMap<String,Object> selectOrdersDate(Integer flag, String time);

    /**
     * 按日期统计订单趋势
     */
    List<Map<String, Object>> selectOrderTrend(String startTime, String endTime);

    /**
     * 按订单状态统计分布
     */
    List<Map<String, Object>> selectOrderStatusDistribution(String startTime, String endTime);

    /**
     * 统计今日订单数
     */
    Integer selectTodayOrderCount(String time);
}
