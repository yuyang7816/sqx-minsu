package com.sqx.app.base.user.dao;

import com.sqx.app.base.user.entity.UserAndInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Admin
* @description 针对表【user_and_info(用户常用信息表)】的数据库操作Mapper
* @createDate 2026-05-21 13:49:28
* @Entity com.sqx.app.base.user.entity.UserAndInfo
*/
@Mapper
public interface UserAndInfoDao extends BaseMapper<UserAndInfo> {

}




