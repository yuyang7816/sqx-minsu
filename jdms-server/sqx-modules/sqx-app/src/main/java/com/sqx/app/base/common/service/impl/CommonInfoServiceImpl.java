package com.sqx.app.base.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.common.dao.CommonInfoDao;
import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author fang
 * @date 2020/7/8
 */
@Service
public class CommonInfoServiceImpl extends ServiceImpl<CommonInfoDao, CommonInfo> implements CommonInfoService {


    private final CommonInfoDao commonInfoDao;

    @Autowired
    public CommonInfoServiceImpl(CommonInfoDao commonInfoDao) {
        this.commonInfoDao = commonInfoDao;
    }

    @Override
    public Result update(CommonInfo commonInfo) {
        commonInfo.setCreateAt(LocalDateTime.now());
        commonInfoDao.updateById(commonInfo);
        return Result.success();
    }


    @Override
    public CommonInfo findOne(int id) {
        return commonInfoDao.findOne(id);
    }

    @Override
    public Result delete(long id) {
        commonInfoDao.deleteById(id);
        return Result.success();
    }


    @Override
    public Result updateBody(CommonInfo commonInfo) {
        commonInfoDao.updateById(commonInfo);
        return Result.success();
    }

    @Override
    public Result findByType(Integer type) {
        return Result.success().put("data", commonInfoDao.findOne(type));
    }

    @Override
    public Result findByTypeAndCondition(String condition) {
        return Result.success().put("data", commonInfoDao.findByCondition(condition));
    }

    @Override
    public CommonInfo selectMinLike(String min) {
        return baseMapper.selectOne(new QueryWrapper<CommonInfo>().like("min", min));
    }

}