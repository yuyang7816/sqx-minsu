package com.sqx.app.base.tbCoupon.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.tbCoupon.entity.TbCoupon;
import com.sqx.app.base.tbCoupon.entity.TbCouponUser;
import com.sqx.app.base.utils.Result;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
public interface TbCouponUserService extends IService<TbCouponUser> {

    IPage<TbCouponUser> getMyCouponList(Integer page, Integer limit, TbCouponUser couponUser);


    Result receiveActivity(Long userId, Long couponId);


    void giveCoupon(TbCoupon tbCoupon, Long userId);

    Result giveUserCoupon(String userIds, Long couponId, Integer num);

    IPage<TbCouponUser> getOrderCouponList(Integer page, Integer limit, Long userId, Integer orderType, BigDecimal payMoney);
}
