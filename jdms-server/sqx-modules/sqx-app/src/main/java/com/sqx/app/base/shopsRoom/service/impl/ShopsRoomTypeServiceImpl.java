package com.sqx.app.base.shopsRoom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.shopsRoom.entity.ShopsRoomType;
import com.sqx.app.base.shopsRoom.service.ShopsRoomTypeService;
import com.sqx.app.base.shopsRoom.dao.ShopsRoomTypeDao;
import com.sqx.app.base.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Admin
 * @description 针对表【shops_room_type(房间类型表)】的数据库操作Service实现
 * @createDate 2026-05-13 16:40:29
 */
@Service
public class ShopsRoomTypeServiceImpl extends ServiceImpl<ShopsRoomTypeDao, ShopsRoomType>
        implements ShopsRoomTypeService {

    @Override
    public Result addRoomType(ShopsRoomType shopsRoomType) {
        if (StringUtils.isNotBlank(shopsRoomType.getTypeName())) {
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<ShopsRoomType>()
                    .eq(ShopsRoomType::getTypeName, shopsRoomType.getTypeName()));
            if (count > 0) {
                return Result.error("房间类型已存在");
            }
        }
        if (StringUtils.isBlank(shopsRoomType.getTypeName())) {
            return Result.error("房间类型名称不能为空");
        }
        shopsRoomType.setCreateTime(new Date());
        if (baseMapper.insert(shopsRoomType) > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result updateRoomType(ShopsRoomType shopsRoomType) {
        if (shopsRoomType.getId() == null) {
            return Result.error("请选择要修改的房间类型");
        }
        if (StringUtils.isNotBlank(shopsRoomType.getTypeName())) {
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<ShopsRoomType>()
                    .eq(ShopsRoomType::getTypeName, shopsRoomType.getTypeName())
                    .ne(ShopsRoomType::getId, shopsRoomType.getId()));
            if (count > 0) {
                return Result.error("房间类型已存在");
            }
        }
        if (baseMapper.updateById(shopsRoomType) > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @Override
    public Result deleteRoomType(String roomTypeIds) {
        if (StringUtils.isNotBlank(roomTypeIds.trim())) {
            String[] split = roomTypeIds.trim().split(",");
            for (String roomTypeId : split) {
                ShopsRoomType roomType = baseMapper.selectById(roomTypeId);
                if (roomType == null) {
                    return Result.error("房间类型id：" + roomTypeId + "，不存在");
                }
                if (baseMapper.deleteById(roomTypeId) <= 0) {
                    return Result.error("删除失败");
                }
                return Result.success("删除成功");
            }
        }
        return Result.error("请选择要删除的房间类型");
    }

    @Override
    public Result getRoomTypeList(ShopsRoomType shopsRoomType) {
        Page<ShopsRoomType> pages;
        if (shopsRoomType.getPage() != null && shopsRoomType.getLimit() != null) {
            pages = new Page<>(shopsRoomType.getPage(), shopsRoomType.getLimit());
        } else {
            pages = new Page<>(1,100);
        }
        return Result.success().put("data",baseMapper.selectPage(pages, new LambdaQueryWrapper<ShopsRoomType>()
                .like(StringUtils.isNotBlank(shopsRoomType.getTypeName()), ShopsRoomType::getTypeName, shopsRoomType.getTypeName())
                .orderByDesc(ShopsRoomType::getSort)
                .orderByDesc(ShopsRoomType::getCreateTime)));
    }

    @Override
    public Result getRoomTypeInfo(Long roomTypeId) {
        ShopsRoomType roomType = baseMapper.selectById(roomTypeId);
        if (roomType != null) {
            return Result.success().put("data", roomType);
        }
        return Result.error("房间类型不存在");
    }
}




