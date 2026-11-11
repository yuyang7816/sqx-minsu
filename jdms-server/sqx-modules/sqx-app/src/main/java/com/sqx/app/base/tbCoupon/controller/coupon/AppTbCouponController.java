package com.sqx.app.base.tbCoupon.controller.coupon;

import com.sqx.app.base.tbCoupon.entity.TbCoupon;
import com.sqx.app.base.tbCoupon.service.TbCouponService;
import com.sqx.app.base.utils.Result;
import com.sqx.system.controller.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "优惠券---用户端")
@RequestMapping(value = "/app/coupon/")
public class AppTbCouponController extends AbstractController {

    @Autowired
    private TbCouponService tbCouponService;

    @Operation(summary = "获取优惠券列表")
    @GetMapping(value = "getCouponPageList")
    public Result getCouponPageList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                    @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                    TbCoupon tbCoupon) {
        tbCoupon.setIsEnable(1);
        tbCoupon.setDeleteFlag(0);
        return Result.success().put("data", tbCouponService.getCouponPageList(page, limit, tbCoupon));
    }


}