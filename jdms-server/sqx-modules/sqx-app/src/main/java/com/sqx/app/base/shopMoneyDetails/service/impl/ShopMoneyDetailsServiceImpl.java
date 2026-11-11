package com.sqx.app.base.shopMoneyDetails.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.shopMoneyDetails.dao.ShopMoneyDetailsDao;
import com.sqx.app.base.shopMoneyDetails.entity.ShopMoneyDetails;
import com.sqx.app.base.shopMoneyDetails.service.IShopMoneyDetailsService;
import com.sqx.app.base.shopsUserRole.entity.ShopsUserRole;
import com.sqx.app.base.shopsUserRole.service.IShopsUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 酒店钱包明细 服务实现类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
@Service
public class ShopMoneyDetailsServiceImpl extends ServiceImpl<ShopMoneyDetailsDao, ShopMoneyDetails> implements IShopMoneyDetailsService {

    @Autowired
    private IShopsUserRoleService userRoleService;

    @Override
    public IPage<ShopMoneyDetails> getShopMoneyDetailsList(Integer page, Integer limit, ShopMoneyDetails shopMoneyDetails) {
        Page<ShopMoneyDetails> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1,100);
        }
        LambdaQueryWrapper<ShopMoneyDetails> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ShopMoneyDetails::getClassify, Arrays.asList(1,3,4));
        wrapper.orderByDesc(ShopMoneyDetails::getCreateTime);
        return baseMapper.selectPage(pages,wrapper);



    }

    @Override
    public IPage<ShopMoneyDetails> getCashMoneyDetailsList(Integer page, Integer limit, ShopMoneyDetails shopMoneyDetails) {
        Page<ShopMoneyDetails> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1,100);
        }
        LambdaQueryWrapper<ShopMoneyDetails> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopMoneyDetails::getClassify, 2);
        wrapper.orderByDesc(ShopMoneyDetails::getCreateTime);
        return baseMapper.selectPage(pages,wrapper);
    }
}
