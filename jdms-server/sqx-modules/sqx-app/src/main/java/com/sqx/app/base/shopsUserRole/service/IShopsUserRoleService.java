package com.sqx.app.base.shopsUserRole.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.shopsUserRole.entity.ShopsUserRole;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.utils.Result;

/**
 * <p>
 * 用户酒店权限 服务类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
public interface IShopsUserRoleService extends IService<ShopsUserRole> {

    IPage<UserEntity> getUserList(Integer page, Integer limit, Long shopId, UserEntity userEntity);

    Result saveShopUser(Long shopId, String userIds);

    Result deleteShopUser(Long shopId, String userIds);

    Result getShopId(Long userId);

    Result insertStaffUser(Long userId, String phone, String msg, Long shopId, String userName);

    Result insertStaffUser1(Long userId, String phone, Long shopId, String userName);
}
