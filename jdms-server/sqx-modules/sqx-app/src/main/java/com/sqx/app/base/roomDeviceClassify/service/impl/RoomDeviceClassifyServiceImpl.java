package com.sqx.app.base.roomDeviceClassify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.roomDeviceClassify.dao.RoomDeviceClassifyDao;
import com.sqx.app.base.roomDeviceClassify.entity.RoomDeviceClassify;
import com.sqx.app.base.roomDeviceClassify.service.IRoomDeviceClassifyService;
import com.sqx.app.base.utils.Result;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 房间配置表 服务实现类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@Service
public class RoomDeviceClassifyServiceImpl extends ServiceImpl<RoomDeviceClassifyDao, RoomDeviceClassify> implements IRoomDeviceClassifyService {

    @Override
    public Result saveClassify(RoomDeviceClassify classify) {
        if (classify.getClassifyId()!=null){
            return Result.upStatus(baseMapper.updateById(classify));
        }else {
            classify.setCreateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.insert(classify));
        }



    }

    @Override
    public IPage<RoomDeviceClassify> getClassifyList(Integer page, Integer limit, RoomDeviceClassify classify) {
        Page<RoomDeviceClassify> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.selectPage(pages, new QueryWrapper<>(classify).orderByAsc("sort"));
    }

    @Override
    public Result deleteClassify(String classifyIds) {
        String[] classifyList = classifyIds.split(",");
        for (String s : classifyList) {
            baseMapper.deleteById(s);
        }
        return Result.success();

    }
}
