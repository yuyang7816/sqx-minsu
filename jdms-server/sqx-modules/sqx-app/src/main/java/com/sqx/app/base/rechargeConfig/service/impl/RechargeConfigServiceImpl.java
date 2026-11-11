package com.sqx.app.base.rechargeConfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.rechargeConfig.dao.RechargeConfigDao;
import com.sqx.app.base.rechargeConfig.entity.RechargeConfig;
import com.sqx.app.base.rechargeConfig.service.RechargeConfigService;
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
 * @author wuchang
 * @since 2022-11-04
 */
@Service
public class RechargeConfigServiceImpl extends ServiceImpl<RechargeConfigDao, RechargeConfig> implements RechargeConfigService {

    @Autowired
    private RechargeConfigDao rechargeConfigDao;
    @Autowired
    private TbCouponService couponService;

    @Override
    public Result addConfig(RechargeConfig rechargeConfig) {
        return Result.upStatus(rechargeConfigDao.insert(rechargeConfig));

    }

    @Override
    public Result updateConfig(RechargeConfig rechargeConfig) {

        return Result.upStatus(rechargeConfigDao.updateById(rechargeConfig));


    }

    @Override
    public RechargeConfig getRechargeConfigInfo(Long rechargeId) {
        return rechargeConfigDao.selectById(rechargeId);


    }

    @Override
    public Result deleteConfig(Long rechargeId) {
        return Result.upStatus(rechargeConfigDao.deleteById(rechargeId));

    }

    @Override
    public IPage<RechargeConfig> getConfigInfoList(Integer page, Integer limit, RechargeConfig rechargeConfig) {
        Page<RechargeConfig> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        IPage<RechargeConfig> selectPage = rechargeConfigDao.selectPage(pages, new QueryWrapper<>(rechargeConfig).orderByAsc("sort"));
        for (RechargeConfig record : selectPage.getRecords()) {
            TbCoupon coupon = couponService.getById(record.getCouponId());
            if (coupon != null) {
                record.setTbCoupon(coupon);
            }
        }
        return selectPage;
    }

}
