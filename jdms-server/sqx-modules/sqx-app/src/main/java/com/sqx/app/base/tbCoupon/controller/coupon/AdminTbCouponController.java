package com.sqx.app.base.tbCoupon.controller.coupon;

import com.sqx.app.base.tbCoupon.entity.TbCoupon;
import com.sqx.app.base.tbCoupon.service.TbCouponService;
import com.sqx.app.base.utils.Result;
import com.sqx.system.controller.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "优惠券---管理端")
@RequestMapping(value = "/admin/coupon/")
public class AdminTbCouponController extends AbstractController {

    @Autowired
    private TbCouponService tbCouponService;

    @Operation(summary = "发布优惠券")
    @PostMapping(value = "addCoupon")
    public Result addCoupon(@RequestBody TbCoupon tbCoupon) {
        return tbCouponService.addCoupon(tbCoupon);
    }

    @Operation(summary = "获取优惠券列表")
    @GetMapping(value = "getCouponPageList")
    public Result getCouponPageList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                    @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                    TbCoupon tbCoupon) {
        return Result.success().put("data", tbCouponService.getCouponPageList(page, limit, tbCoupon));
    }

    @Operation(summary = "删除发布的优惠券")
    @PostMapping(value = "deleteCoupon")
    public Result deleteCoupon(@RequestParam(required = false) @Parameter(description = "酒店id") Long couponId) {
        return tbCouponService.deleteCoupon(couponId);
    }

    @Operation(summary = "修改优惠券信息")
    @PostMapping(value = "updateCoupon")
    public Result updateCoupon(@RequestBody TbCoupon tbCoupon) {
        return tbCouponService.updateCoupon(tbCoupon);
    }
}