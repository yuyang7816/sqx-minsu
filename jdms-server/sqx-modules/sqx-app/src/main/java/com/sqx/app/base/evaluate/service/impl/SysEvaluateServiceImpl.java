package com.sqx.app.base.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.evaluate.dao.SysEvaluateDao;
import com.sqx.app.base.evaluate.entity.SysEvaluate;
import com.sqx.app.base.evaluate.service.SysEvaluateService;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.orders.service.impl.OrdersServiceImpl;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shops.service.IShopsService;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.utils.BaiDuUtils;
import com.sqx.app.base.utils.Result;
import com.sqx.app.base.utils.SenInfoCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wuchang
 * @since 2022-11-10
 */
@Service
public class SysEvaluateServiceImpl extends ServiceImpl<SysEvaluateDao, SysEvaluate> implements SysEvaluateService {
    @Autowired
    SysEvaluateDao evaluateDao;
    @Autowired
    OrdersServiceImpl ordersService;
    @Autowired
    UserService userService;
    @Autowired
    private IShopsService shopsService;

    @Override
    public Result addEvaluate(SysEvaluate evaluate) {

        if (StringUtils.isNotBlank(evaluate.getEvaluateMessage())) {
            if (BaiDuUtils.auditBaiDuText(evaluate.getEvaluateMessage())) {
                return Result.error("您评价的内容包含敏感词汇");
            }
        }
        if (StringUtils.isNotBlank(evaluate.getEvaluateImg())) {
            if (BaiDuUtils.auditBaiDuImg(evaluate.getEvaluateImg())) {
                return Result.error("您评价的图片不合规");
            }
        }

        evaluate.setCreateTime(LocalDateTime.now());
        Orders orders = ordersService.getOne(new QueryWrapper<Orders>().eq("order_number", evaluate.getOrderNumber()));
        if (orders == null) {
            return Result.error("当前订单不存在");
        }
        if (orders.getOrderStatus() != 3) {
            return Result.error("当前订单未完成");
        }
        if (orders.getIsEvaluate() == 1) {
            return Result.error("订单已评价");
        }
        //修改为已评价
        Orders evalOrder = new Orders();
        evalOrder.setOrderId(orders.getOrderId());
        evalOrder.setIsEvaluate(1);
        ordersService.updateById(evalOrder);

        evaluate.setShopId(orders.getShopId());
        evaluateDao.insert(evaluate);
        //更新接单人评分
        Shops shops = shopsService.getById(evaluate.getShopId());
        if (shops == null) {
            return Result.error("当前用酒店不存在");
        }
        //总分数
        String totalScore = evaluateDao.getTotalScore(shops.getShopId());
        //评论总条数
        Long count = evaluateDao.selectCount(new QueryWrapper<SysEvaluate>().eq("shop_id", shops.getShopId()));
        //更新用户评分
        shops.setScore(new BigDecimal(totalScore).divide(new BigDecimal(count), 1, BigDecimal.ROUND_DOWN));
        shopsService.updateById(shops);
        return Result.success();

    }

    @Override
    public IPage<SysEvaluate> getUserEvaluateList(Integer page, Integer limit, SysEvaluate evaluate) {
        Page<SysEvaluate> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }

        return evaluateDao.getUserEvaluateList(pages, evaluate);

    }

    @Override
    public Result deleteEvaluate(Long userId, Long evaluateId, Integer auth) {
        SysEvaluate sysEvaluate = evaluateDao.selectById(evaluateId);
        if (sysEvaluate == null) {
            return Result.error("评论不存在");
        }
        Long shopId = sysEvaluate.getShopId();
        Shops shops = shopsService.getById(sysEvaluate.getShopId());
        if (auth != 1) {
            if (!userId.equals(sysEvaluate.getUserId())) {
                return Result.error("您无法操作当前评论");
            }
        }
        evaluateDao.deleteById(evaluateId);
        //总分数
        String totalScore = evaluateDao.getTotalScore(shopId);
        if (StringUtils.isBlank(totalScore)) {
            shops.setScore(new BigDecimal("5.0"));
        } else {
            //评论总条数
            Long count = evaluateDao.selectCount(new QueryWrapper<SysEvaluate>().eq("shop_id", sysEvaluate.getShopId()));
            //更新用户评分
            shops.setScore(new BigDecimal(totalScore).divide(new BigDecimal(count), 1, BigDecimal.ROUND_DOWN));
        }
        return shopsService.updateById(shops) ? Result.success() : Result.error();

    }

    @Override
    public SysEvaluate getEvaluateInfo(Long evaluateId) {
        return baseMapper.getEvaluateInfo(evaluateId);
    }
}
