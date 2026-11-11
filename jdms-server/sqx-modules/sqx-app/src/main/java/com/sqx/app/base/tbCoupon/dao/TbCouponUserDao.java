package com.sqx.app.base.tbCoupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.tbCoupon.entity.TbCouponUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
@Mapper
public interface TbCouponUserDao extends BaseMapper<TbCouponUser> {

    IPage<TbCouponUser> getMyCouponList(@Param("pages") Page<TbCouponUser> pages,
                                        @Param("couponUser") TbCouponUser couponUser);

    IPage<TbCouponUser> getOrderCouponList(@Param("pages") Page<TbCouponUser> pages,
                                           @Param("userId") Long userId,
                                           @Param("orderType") Integer orderType,
                                           @Param("payMoney") BigDecimal payMoney);
}
