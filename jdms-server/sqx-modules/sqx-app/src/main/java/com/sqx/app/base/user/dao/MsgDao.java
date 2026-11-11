package com.sqx.app.base.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.base.user.entity.Msg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户
 *
 */
@Mapper
public interface MsgDao extends BaseMapper<Msg> {

    Msg findByPhone(@Param("phone") String phone);

    Msg findByPhoneAndCode(@Param("phone")String phone,
                           @Param("msg")String msg);



}
