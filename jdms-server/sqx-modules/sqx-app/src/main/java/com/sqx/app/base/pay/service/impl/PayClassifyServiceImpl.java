package com.sqx.app.base.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.pay.dao.PayClassifyDao;
import com.sqx.app.base.pay.entity.PayClassify;
import com.sqx.app.base.pay.service.PayClassifyService;
import org.springframework.stereotype.Service;

/**
 * 充值分类
 */
@Service
public class PayClassifyServiceImpl extends ServiceImpl<PayClassifyDao, PayClassify> implements PayClassifyService {


    @Override
    public IPage<PayClassify> selectPayClassifyList(Integer page, Integer limit) {
        Page<PayClassify> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1, Integer.MAX_VALUE);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<PayClassify>().orderByAsc("sort"));
    }
}
