package com.sqx.app.base.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.user.entity.UserMoney;
import com.sqx.app.base.utils.Result;

import java.math.BigDecimal;

public interface UserMoneyService extends IService<UserMoney> {

    void updateMoney(int i, Long userId, BigDecimal money);


    Result updateUserMoney(Integer type, Long userId, BigDecimal money);


    Result payOrder(Long userId, Long ordersId);

    Result payVip(Long userId, Long vipId);
}
