package com.sqx.app.base.tbCoupon.controller.couponUser;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.app.base.tbCoupon.entity.TbCouponUser;
import com.sqx.app.base.tbCoupon.service.TbCouponUserService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import com.sqx.system.controller.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@RestController
@Tag(name = "优惠券---用户端")
@RequestMapping(value = "/app/couponUser/")
public class AppTbCouponUserController extends AbstractController {

    @Autowired
    private TbCouponUserService couponUserService;


    @Login
    @Operation(summary = "查看我的优惠券列表")
    @GetMapping(value = "getMyCouponList")
    public Result getMyCouponList(@RequestAttribute Long userId,
                                  @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                  @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                  TbCouponUser couponUser) {
        couponUser.setUserId(userId);
        return Result.success().put("data", couponUserService.getMyCouponList(page, limit, couponUser));
    }

    @Login
    @Operation(summary = "获取订单可用优惠券列表")
    @GetMapping(value = "getOrderCouponList")
    public Result getOrderCouponList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                     @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                     @RequestAttribute("userId") Long userId,
                                     @RequestParam(required = false) @Parameter(description = "优惠券类型 0全部可用  1酒店 2民宿") Integer orderType,
                                     @RequestParam(required = false) @Parameter(description = "支付金额") BigDecimal payMoney) {
        return Result.success().put("data", couponUserService.getOrderCouponList(page, limit, userId, orderType, payMoney));
    }

    @Login
    @Operation(summary = "领取活动优惠券")
    @GetMapping("receiveActivity")
    public Result receiveActivity(@RequestAttribute("userId") Long userId,
                                  @RequestParam(required = false) @Parameter(description = "优惠券id") Long couponId) {
        return couponUserService.receiveActivity(userId, couponId);
    }


    /**
     * 检测使优惠券过期
     */
    @Scheduled(cron = "0/2 * * * * ?", zone = "Asia/Shanghai")
    public void couponOverdue() {
        List<TbCouponUser> couponUserList = couponUserService.list(new QueryWrapper<TbCouponUser>().eq("status", 0).isNotNull("expiration_time"));
        for (TbCouponUser tbCouponUser : couponUserList) {
            if (tbCouponUser.getExpirationTime().before(new Date())) {
                tbCouponUser.setStatus(2);
                couponUserService.updateById(tbCouponUser);
            }
        }

    }
}