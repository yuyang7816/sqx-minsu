package com.sqx.app.base.tbCoupon.controller.couponUser;

import com.sqx.app.base.tbCoupon.entity.TbCouponUser;
import com.sqx.app.base.tbCoupon.service.TbCouponUserService;
import com.sqx.app.base.utils.Result;
import com.sqx.system.controller.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "优惠券---管理端")
@RequestMapping(value = "/admin/couponUser/")
public class AdminTbCouponUserController extends AbstractController {

    @Autowired
    private TbCouponUserService couponUserService;


    @Operation(summary = "查看指定用户优惠券列表")
    @GetMapping(value = "getMyCouponList")
    public Result getMyCouponList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                  @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                  TbCouponUser couponUser) {
        return Result.success().put("data", couponUserService.getMyCouponList(page, limit, couponUser));
    }

    @Operation(summary = "赠送用户优惠券")
    @PostMapping(value = "giveUserCoupon")
    public Result giveUserCoupon(@RequestParam(required = false) @Parameter(description = "用户id，多个逗号分隔") String userIds,
                                 @RequestParam(required = false) @Parameter(description = "优惠券id") Long couponId,
                                 @RequestParam(required = false) @Parameter(description = "赠送数量") Integer num) {
        return couponUserService.giveUserCoupon(userIds, couponId, num);
    }
}