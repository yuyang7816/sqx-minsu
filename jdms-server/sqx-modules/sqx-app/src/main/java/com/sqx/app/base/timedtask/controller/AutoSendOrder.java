package com.sqx.app.base.timedtask.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.orders.service.IOrdersService;
import com.sqx.app.base.roomOrderRecord.entity.RoomOrderRecord;
import com.sqx.app.base.roomOrderRecord.service.IRoomOrderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
@Slf4j
public class AutoSendOrder {

    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private IRoomOrderRecordService orderRecordService;

    /**
     * 订单定时取消
     */
    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void cancelIndent() {
        List<Orders> orderList = ordersService.list(new QueryWrapper<Orders>().eq("order_status", 0));
        int minute = Integer.parseInt(commonInfoService.findOne(346).getValue());
        for (Orders orders : orderList) {
            Duration duration = Duration.between(orders.getCreateTime(), LocalDateTime.now());
            if (duration.toMinutes() > minute) {
                ordersService.restoreOrderCoupon(orders.getCouponId());
                Orders cancelOrder = new Orders();
                cancelOrder.setOrderStatus(4);
                cancelOrder.setOrderId(orders.getOrderId());
                ordersService.updateById(cancelOrder);
            }
        }
    }

//    /**
//     * 订单到入住时间自动改为进行中
//     */
//    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
//    public void orderStatus(){
//        List<Orders> orderList = ordersService.list(new QueryWrapper<Orders>().eq("order_status", 1));
//        for (Orders orders : orderList) {
//            if (!LocalDate.now().isBefore(orders.getStartDate())){
//                Orders newOrder = new Orders();
//                newOrder.setOrderStatus(2);
//                newOrder.setOrderId(orders.getOrderId());
//                ordersService.updateById(newOrder);
//
//            }
//        }
//    }

    /**
     * 订单到离店时间自动改为已完成
     */
    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void orderCompleteStatus() {
        List<Orders> orderList = ordersService.list(new QueryWrapper<Orders>().eq("order_status", 2));
        for (Orders orders : orderList) {
            if (LocalDate.now().isAfter(orders.getEndDate())) {
                //完成订单
                ordersService.finishOrder(orders.getOrderId());
                //删除订单房间记录表
                orderRecordService.remove(new QueryWrapper<RoomOrderRecord>().eq("order_id", orders.getOrderId()));
            }
        }
    }
}
