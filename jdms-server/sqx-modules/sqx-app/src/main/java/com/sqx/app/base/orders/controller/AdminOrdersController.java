package com.sqx.app.base.orders.controller;

import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.orders.service.IOrdersService;
import com.sqx.app.base.orders.vo.OrderVo;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author wuChang
 * @since 2023-08-11
 */
@RestController
@RequestMapping("/admin/orders/")
@Tag(name = "订单管理---管理端")
public class AdminOrdersController {
    @Autowired
    private IOrdersService ordersService;

    @PostMapping("addOrder")
    @Operation(summary = "用户下单")
    public Result addOrder(@RequestParam(required = false) @Parameter(description = "用户id") Long userId, @RequestBody Orders orders) {
        orders.setUserId(userId);
        return ordersService.addOrder(orders);
    }

    @Login
    @GetMapping("getOrderList")
    @Operation(summary = "获取订单列表")
    public Result getOrderList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                               @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                               OrderVo orderVo) {
        return Result.success().put("data", ordersService.getOrderList(page, limit, orderVo));
    }


    @GetMapping("getMyOrderInfo")
    @Operation(summary = "获取订单详情")
    public Result getMyOrderInfo(@RequestParam(required = false) @Parameter(description = "订单id") Long orderId) {
        return Result.success().put("data", ordersService.getMyOrderInfo(orderId));
    }

    @Operation(summary = "管理端取消订单")
    @PostMapping("adminCancelOrder")
    public Result adminCancelOrder(@RequestParam(required = false) @Parameter(description = "用户id") Long userId,
                                   @RequestParam(required = false) @Parameter(description = "订单id") Long orderId,
                                   @RequestParam(required = false) @Parameter(description = "退款金额") BigDecimal refundMoney) {
        return ordersService.cancelOrder(userId, orderId, 3, refundMoney);
    }

    @Operation(summary = "修改备注")
    @PostMapping("updateRemarks")
    public Result updateRemarks(@RequestParam(required = false) @Parameter(description = "用户id") Long userId,
                                @RequestParam(required = false) @Parameter(description = "订单id") Long orderId,
                                @RequestParam(required = false) @Parameter(description = "备注") String remarks) {
        return ordersService.updateRemarks(userId, orderId, remarks);
    }

    @PostMapping("cancelVerification")
    @Operation(summary = "员工核销")
    public Result cancelVerification(@RequestParam(required = false) @Parameter(description = "用户id") Long userId,
                                     @RequestParam(required = false) @Parameter(description = "订单号") String orderNumber) {
        return ordersService.cancelVerification(userId, orderNumber);
    }
}
