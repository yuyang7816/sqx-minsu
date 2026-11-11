package com.sqx.app.base.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.base.user.entity.App;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户升级
 *
 */
@Mapper
public interface AppDao extends BaseMapper<App> {

    List<App> selectNewApp();



}
