package com.sqx.app.base.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.user.entity.UserAndInfo;
import com.sqx.app.base.user.service.UserAndInfoService;
import com.sqx.app.base.user.dao.UserAndInfoDao;
import com.sqx.app.base.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Admin
 * @description 针对表【user_and_info(用户常用信息表)】的数据库操作Service实现
 * @createDate 2026-05-21 13:49:28
 */
@Service
public class UserAndInfoServiceImpl extends ServiceImpl<UserAndInfoDao, UserAndInfo>
        implements UserAndInfoService {

    @Override
    public Result addUserAndInfo(UserAndInfo userAndInfo) {
        if (StringUtils.isBlank(userAndInfo.getInfoName())) {
            return Result.error("请填写常用人信息名称");
        }
        if (StringUtils.isBlank(userAndInfo.getInfoPhone())) {
            return Result.error("请填写常用人信息手机号");
        }
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<UserAndInfo>()
                .eq(UserAndInfo::getInfoName, userAndInfo.getInfoName())
                .eq(UserAndInfo::getInfoPhone, userAndInfo.getInfoPhone()));
        if (count > 0) {
            return Result.error("该常用人信息已存在");
        }
        if (userAndInfo.getIsDefault() != null && userAndInfo.getIsDefault() == 1) {
            Long userCount = baseMapper.selectCount(new LambdaQueryWrapper<UserAndInfo>()
                    .eq(UserAndInfo::getUserId, userAndInfo.getUserId()));
            if (userCount > 0) {
                baseMapper.update(null, new LambdaUpdateWrapper<UserAndInfo>()
                        .eq(UserAndInfo::getUserId, userAndInfo.getUserId())
                        .set(UserAndInfo::getIsDefault, 2));
            } else if (userCount == 0) {
                userAndInfo.setIsDefault(1);
            }
        } else {
            Long userCount = baseMapper.selectCount(new LambdaQueryWrapper<UserAndInfo>()
                    .eq(UserAndInfo::getUserId, userAndInfo.getUserId()));
            if (userCount == 0) {
                userAndInfo.setIsDefault(1);
            }
        }
        userAndInfo.setCreateTime(LocalDateTime.now());
        if (baseMapper.insert(userAndInfo) > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result updateUserAndInfo(UserAndInfo userAndInfo) {
        if (userAndInfo.getId() == null) {
            return Result.error("请选择要修改的常用人信息");
        }
        if (StringUtils.isBlank(userAndInfo.getInfoName())) {
            return Result.error("请填写常用人信息名称");
        }
        if (StringUtils.isBlank(userAndInfo.getInfoPhone())) {
            return Result.error("请填写常用人信息手机号");
        }
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<UserAndInfo>()
                .eq(UserAndInfo::getInfoName, userAndInfo.getInfoName())
                .eq(UserAndInfo::getInfoPhone, userAndInfo.getInfoPhone())
                .eq(UserAndInfo::getUserId, userAndInfo.getUserId())
                .ne(UserAndInfo::getId, userAndInfo.getId()));
        if (count > 0) {
            return Result.error("该常用人信息已存在");
        }
        if (userAndInfo.getIsDefault() != null && userAndInfo.getIsDefault() == 1) {
            Long userCount = baseMapper.selectCount(new LambdaQueryWrapper<UserAndInfo>()
                    .eq(UserAndInfo::getUserId, userAndInfo.getUserId()));
            if (userCount > 0) {
                baseMapper.update(null, new LambdaUpdateWrapper<UserAndInfo>()
                        .eq(UserAndInfo::getUserId, userAndInfo.getUserId())
                        .set(UserAndInfo::getIsDefault, 2));
            }
        }
//        else {
//            UserAndInfo userAndInfo1 = baseMapper.selectOne(new LambdaQueryWrapper<UserAndInfo>()
//                    .eq(UserAndInfo::getId, userAndInfo.getId()));
//            if (userAndInfo1.getIsDefault() == 1) {
//                UserAndInfo userCount = baseMapper.selectOne(new LambdaQueryWrapper<UserAndInfo>()
//                        .ne(UserAndInfo::getId, userAndInfo.getId())
//                        .eq(UserAndInfo::getUserId, userAndInfo.getUserId())
//                        .orderByDesc(UserAndInfo::getCreateTime)
//                        .last("limit 1"));
//                if (userCount != null) {
//                    userAndInfo.setIsDefault(1);
//                }
//            }
//        }
        return Result.upStatus(baseMapper.updateById(userAndInfo));
    }

    @Override
    public Result deleteUserAndInfo(Long id) {
        UserAndInfo userAndInfo = baseMapper.selectById(id);
        if (userAndInfo != null) {
            return Result.upStatus(baseMapper.deleteById(id));
        }
        return Result.error("该常用信息人不存在");
    }

    @Override
    public Result getUserAndInfo(Long id) {
        UserAndInfo userAndInfo = baseMapper.selectById(id);
        if (userAndInfo != null) {
            return Result.success().put("data", userAndInfo);
        }
        return Result.error("该常用信息人不存在");
    }

    @Override
    public Result getUserAndInfoList(UserAndInfo userAndInfo) {
        Page<UserAndInfo> pages;
        if (userAndInfo.getPage() != null && userAndInfo.getLimit() != null) {
            pages = new Page<>(userAndInfo.getPage(), userAndInfo.getLimit());
        } else {
            pages = new Page<>(1, 100);
        }
        return Result.success().put("data", baseMapper.selectPage(pages, new LambdaQueryWrapper<UserAndInfo>()
                .like(StringUtils.isNotBlank(userAndInfo.getInfoName()), UserAndInfo::getInfoName, userAndInfo.getInfoName())
                .like(StringUtils.isNotBlank(userAndInfo.getInfoPhone()), UserAndInfo::getInfoPhone, userAndInfo.getInfoPhone())
                .eq(UserAndInfo::getUserId, userAndInfo.getUserId())
                .orderByDesc(UserAndInfo::getCreateTime)));
    }
}




