package com.sqx.app.base.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.base.user.entity.UserMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface UserMoneyDao extends BaseMapper<UserMoney> {

    void updateMayMoney(@Param("type") Integer type, @Param("userId") Long userId, @Param("money") BigDecimal money);

    int updateCashDeposit(@Param("userId") Long userId,@Param("money") BigDecimal money, @Param("type") Integer type);

}
