package com.sqx.app.base.userMoneyDetails.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.userMoneyDetails.dao.UserMoneyDetailsDao;
import com.sqx.app.base.userMoneyDetails.entity.UserMoneyDetails;
import com.sqx.app.base.userMoneyDetails.service.UserMoneyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class UserMoneyDetailsServiceImpl extends ServiceImpl<UserMoneyDetailsDao, UserMoneyDetails> implements UserMoneyDetailsService {

    @Autowired
    UserMoneyDetailsDao userMoneyDetailsDao;


    @Override
    public IPage<UserMoneyDetails> queryUserMoneyDetails(Integer page, Integer limit, UserMoneyDetails userMoneyDetails) {
        IPage<UserMoneyDetails> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1, 100);
        }
        LambdaQueryWrapper<UserMoneyDetails> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserMoneyDetails::getClassify, Arrays.asList(1,2,3,6,7));
        wrapper.orderByDesc(UserMoneyDetails::getCreateTime);
        if (userMoneyDetails.getUserId() != null){
            wrapper.eq(UserMoneyDetails::getUserId,userMoneyDetails.getUserId());
        }
        return baseMapper.selectPage(pages,wrapper);

    }

    @Override
    public BigDecimal getOneProfit(Long userId, Integer level) {
        return baseMapper.getOneProfit(userId, level);
    }
}
