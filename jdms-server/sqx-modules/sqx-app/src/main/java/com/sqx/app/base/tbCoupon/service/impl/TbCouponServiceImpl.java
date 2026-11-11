package com.sqx.app.base.tbCoupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.rechargeConfig.dao.RechargeConfigDao;
import com.sqx.app.base.rechargeConfig.entity.RechargeConfig;
import com.sqx.app.base.rechargeConfig.service.RechargeConfigService;
import com.sqx.app.base.tbCoupon.dao.TbCouponDao;
import com.sqx.app.base.tbCoupon.entity.TbCoupon;
import com.sqx.app.base.tbCoupon.service.TbCouponService;
import com.sqx.app.base.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2022-11-18
 */
@Service
public class TbCouponServiceImpl extends ServiceImpl<TbCouponDao, TbCoupon> implements TbCouponService {


    @Autowired
    private TbCouponDao tbCouponDao;
    @Autowired
    private RechargeConfigDao rechargeConfigDao;
    @Override
    public Result addCoupon(TbCoupon tbCoupon) {
        tbCoupon.setDeleteFlag(0);
        tbCouponDao.insert(tbCoupon);
        return Result.success();
    }

    @Override
    public IPage<TbCoupon> getCouponPageList(Integer page, Integer limit, TbCoupon tbCoupon) {
        Page<TbCoupon> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return tbCouponDao.selectPage(pages, new QueryWrapper<>(tbCoupon));
    }

    @Override
    public Result deleteCoupon(Long couponId) {
        long count = rechargeConfigDao.selectCount(new QueryWrapper<RechargeConfig>().eq("coupon_id", couponId));
        if (count>0){
            return Result.error("当前优惠券已被充值配置使用,无法删除");
        }
        return Result.upStatus(tbCouponDao.deleteById(couponId));
    }

    @Override
    public Result updateCoupon(TbCoupon tbCoupon) {
        return Result.upStatus(tbCouponDao.updateById(tbCoupon));
    }




}
