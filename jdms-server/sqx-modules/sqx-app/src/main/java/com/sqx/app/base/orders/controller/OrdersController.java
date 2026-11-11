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
@RequestMapping("/app/orders/")
@Tag(name = "订单管理---用户端")
public class OrdersController {
    @Autowired
    private IOrdersService ordersService;

    @Login
    @PostMapping("addOrder")
    @Operation(summary = "用户下单")
    public Result addOrder(@RequestAttribute("userId") Long userId,
                           @RequestBody Orders orders) {
        orders.setUserId(userId);
        return ordersService.addOrder(orders);
    }

    @Login
    @GetMapping("getMyOrderList")
    @Operation(summary = "用户获取我的订单列表")
    public Result getMyOrderList(@RequestAttribute("userId") Long userId,
                                 @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                 @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                 OrderVo orderVo) {
        orderVo.setUserId(userId);
        return Result.success().put("data", ordersService.getMyOrderList(page, limit, orderVo));
    }


    @GetMapping("getMyOrderInfo")
    @Operation(summary = "获取订单详情")
    public Result getMyOrderInfo(@RequestParam(required = false) @Parameter(description = "订单id") Long orderId) {
        return Result.success().put("data", ordersService.getMyOrderInfo(orderId));
    }

    @Login
    @Operation(summary = "用户取消订单扣费预览")
    @GetMapping("cancelOrderPreview")
    public Result cancelOrderPreview(@RequestAttribute("userId") Long userId,
                                     @RequestParam(required = false) @Parameter(description = "订单id") Long orderId) {
        return ordersService.cancelOrderPreview(userId, orderId);
    }

    @Login
    @Operation(summary = "用户取消订单")
    @PostMapping("userCancelOrder")
    public Result cancelOrder(@RequestAttribute("userId") Long userId,
                              @RequestParam(required = false) @Parameter(description = "订单id") Long orderId) {
        return ordersService.cancelOrder(userId, orderId, 1, null);
    }

    @Login
    @Operation(summary = "员工取消订单")
    @PostMapping("staffCancelOrder")
    public Result staffCancelOrder(@RequestAttribute("userId") Long userId,
                                   @RequestParam(required = false) @Parameter(description = "订单id") Long orderId,
                                   @RequestParam(required = false) @Parameter(description = "退款金额") BigDecimal refundMoney) {
        return ordersService.cancelOrder(userId, orderId, 2, refundMoney);
    }

    @Login
    @Operation(summary = "员工完成订单")
    @PostMapping("staffFinishOrder")
    public Result staffFinishOrder(@RequestAttribute("userId") Long userId,
                                   @RequestParam(required = false) @Parameter(description = "订单id") Long orderId) {
        return ordersService.staffFinishOrder(userId, orderId);
    }

    @Login
    @Operation(summary = "修改备注")
    @PostMapping("updateRemarks")
    public Result updateRemarks(@RequestAttribute("userId") Long userId,
                                @RequestParam(required = false) @Parameter(description = "订单id") Long orderId,
                                @RequestParam(required = false) @Parameter(description = "备注") String remarks) {
        return ordersService.updateRemarks(userId, orderId, remarks);
    }

    @Login
    @PostMapping("cancelVerification")
    @Operation(summary = "员工核销")
    public Result cancelVerification(@RequestAttribute("userId") Long userId,
                                     @RequestParam(required = false) @Parameter(description = "订单号") String orderNumber) {
        return ordersService.cancelVerification(userId, orderNumber);
    }
}
