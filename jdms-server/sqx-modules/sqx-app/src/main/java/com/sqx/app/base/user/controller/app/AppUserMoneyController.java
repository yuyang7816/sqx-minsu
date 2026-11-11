package com.sqx.app.base.user.controller.app;

import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.user.service.UserMoneyService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/userMoney")
@Tag(name = "用户钱包表---用户端")
public class AppUserMoneyController {

    @Autowired
    private UserMoneyService userMoneyService;

    @Login
    @PostMapping(value = "/payOrder")
    @Operation(summary = "余额支付订单")
    public Result payOrder(@RequestAttribute("userId") Long userId,
                           @RequestParam(required = false) @Parameter(description = "订单id") Long ordersId) {
        return userMoneyService.payOrder(userId, ordersId);
    }

    @Login
    @PostMapping(value = "/payVip")
    @Operation(summary = "余额购买vip")
    public Result payVip(@RequestAttribute("userId") Long userId,
                         @RequestParam(required = false) @Parameter(description = "vipId") Long vipId) {
        return userMoneyService.payVip(userId, vipId);
    }

}
