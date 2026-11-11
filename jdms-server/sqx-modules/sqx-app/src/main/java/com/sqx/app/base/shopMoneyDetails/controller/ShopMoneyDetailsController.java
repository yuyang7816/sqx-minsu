package com.sqx.app.base.shopMoneyDetails.controller;

import com.sqx.app.base.shopMoneyDetails.entity.ShopMoneyDetails;
import com.sqx.app.base.shopMoneyDetails.service.IShopMoneyDetailsService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 酒店钱包明细 前端控制器
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
@RestController
@RequestMapping("/app/shopMoneyDetails/")
@Tag(name = "酒店钱包明细---用户端")
public class ShopMoneyDetailsController {
    @Autowired
    private IShopMoneyDetailsService shopMoneyDetailsService;

    @Login
    @GetMapping("getShopMoneyDetailsList")
    @Operation(summary = "获取酒店钱包明细列表")
    public Result getShopMoneyDetailsList(@RequestAttribute("userId") Long userId,
                                          @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                          @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                          ShopMoneyDetails shopMoneyDetails) {
        shopMoneyDetails.setUserId(userId);
        return Result.success().put("data", shopMoneyDetailsService.getShopMoneyDetailsList(page, limit, shopMoneyDetails));
    }

    @Login
    @GetMapping("getCashMoneyDetailsList")
    @Operation(summary = "获取酒店钱包提现明细列表")
    public Result getCashMoneyDetailsList(@RequestAttribute("userId") Long userId,
                                          @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                          @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                          ShopMoneyDetails shopMoneyDetails) {
        shopMoneyDetails.setUserId(userId);
        return Result.success().put("data", shopMoneyDetailsService.getCashMoneyDetailsList(page, limit, shopMoneyDetails));
    }
}
