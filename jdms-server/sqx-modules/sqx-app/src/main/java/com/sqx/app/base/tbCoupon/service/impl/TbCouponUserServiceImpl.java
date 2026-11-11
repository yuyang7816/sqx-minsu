package com.sqx.app.base.tbCoupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.tbCoupon.dao.TbCouponUserDao;
import com.sqx.app.base.tbCoupon.entity.TbCoupon;
import com.sqx.app.base.tbCoupon.entity.TbCouponUser;
import com.sqx.app.base.tbCoupon.service.TbCouponService;
import com.sqx.app.base.tbCoupon.service.TbCouponUserService;
import com.sqx.app.base.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
@Service
public class TbCouponUserServiceImpl extends ServiceImpl<TbCouponUserDao, TbCouponUser> implements TbCouponUserService {

    @Autowired
    private TbCouponService tbCouponService;

    @Autowired
    private TbCouponUserDao couponUserDao;

    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    @Override
    public IPage<TbCouponUser> getMyCouponList(Integer page, Integer limit, TbCouponUser couponUser) {

        Page<TbCouponUser> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return couponUserDao.getMyCouponList(pages, couponUser);
    }


    @Override
    public Result receiveActivity(Long userId, Long couponId) {
        reentrantReadWriteLock.readLock().lock();
        try {
            TbCoupon tbCoupon = tbCouponService.getById(couponId);
            if (tbCoupon.getCouponNum() > 0) {
                if (tbCoupon.getIsEnable() != 1) {
                    return Result.error("当前优惠券未启用");
                }
                Long num = couponUserDao.selectCount(new QueryWrapper<TbCouponUser>().eq("user_id", userId).eq("coupon_id", tbCoupon.getCouponId()));
                if (tbCoupon.getMaxReceive() != 0) {
                    if (tbCoupon.getMaxReceive() <= num) {
                        return Result.error("您已领取过!");
                    }
                }
                giveCoupon(tbCoupon, userId);
                tbCoupon.setCouponNum(tbCoupon.getCouponNum() - 1);
                tbCouponService.updateById(tbCoupon);
                return Result.success();
            } else {
                return Result.error("当前优惠券已被领完!");
            }
        } catch (Exception e) {
            log.error("领取异常,请重试!", e);
        } finally {
            reentrantReadWriteLock.readLock().unlock();
        }
        return Result.error("领取异常,请重试!");
    }


    /**
     * 赠送优惠券计算
     *
     * @param tbCoupon
     * @param userId
     */
    @Override
    public void giveCoupon(TbCoupon tbCoupon, Long userId) {
        TbCouponUser couponUser = new TbCouponUser();
        //copy对象
        BeanUtils.copyProperties(tbCoupon, couponUser);
        couponUser.setUserId(userId);
        couponUser.setCreateTime(new Date());
        //是否是永久有效
        if (tbCoupon.getValidDays() != null && tbCoupon.getValidDays() != 0) {
            //如果不是永久
            Calendar instance = Calendar.getInstance();
            instance.setTime(new Date());
            instance.add(Calendar.DATE, tbCoupon.getValidDays());
            couponUser.setExpirationTime(instance.getTime());
            couponUser.setValidDays(tbCoupon.getValidDays());
        } else {
            couponUser.setValidDays(0);
        }
        couponUser.setStatus(0);
        couponUserDao.insert(couponUser);
    }

    @Override
    public Result giveUserCoupon(String userIds, Long couponId, Integer num) {
        TbCoupon tbCoupon = tbCouponService.getById(couponId);
        String[] split = userIds.split(",");
        for (String userId : split) {
            for (int i = 0; i < num; i++) {
                giveCoupon(tbCoupon, Long.valueOf(userId));
            }
        }
        return Result.success();
    }

    @Override
    public IPage<TbCouponUser> getOrderCouponList(Integer page, Integer limit, Long userId, Integer orderType, BigDecimal payMoney) {

        Page<TbCouponUser> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getOrderCouponList(pages, userId, orderType, payMoney);


    }


}
