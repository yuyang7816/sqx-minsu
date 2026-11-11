package com.sqx.app.base.user.service;

import com.sqx.app.base.user.entity.UserAndInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.utils.Result;

/**
* @author Admin
* @description 针对表【user_and_info(用户常用信息表)】的数据库操作Service
* @createDate 2026-05-21 13:49:28
*/
public interface UserAndInfoService extends IService<UserAndInfo> {

    Result addUserAndInfo(UserAndInfo userAndInfo);

    Result updateUserAndInfo(UserAndInfo userAndInfo);

    Result deleteUserAndInfo(Long id);

    Result getUserAndInfo(Long id);

    Result getUserAndInfoList(UserAndInfo userAndInfo);

}
