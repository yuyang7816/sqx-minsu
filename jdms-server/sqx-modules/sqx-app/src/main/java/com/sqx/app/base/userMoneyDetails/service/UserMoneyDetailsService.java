package com.sqx.app.base.userMoneyDetails.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.userMoneyDetails.entity.UserMoneyDetails;

import java.math.BigDecimal;

public interface UserMoneyDetailsService extends IService<UserMoneyDetails> {


    IPage<UserMoneyDetails> queryUserMoneyDetails(Integer page, Integer limit, UserMoneyDetails userMoneyDetails);

    BigDecimal getOneProfit(Long userId, Integer level);
}
