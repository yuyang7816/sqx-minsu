package com.sqx.app.base.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.entity.UserMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserCashOutDao extends BaseMapper<UserEntity> {

    UserMoney findBalanceById(@Param("userId") Long userId);

    int updateMoney(@Param("type") int i, @Param("userId") Long userId, @Param("money") Double moneySum);

}
