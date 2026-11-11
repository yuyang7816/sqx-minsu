package com.sqx.app.base.pay.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.pay.entity.CashOut;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.utils.Result;
import com.sqx.common.mybatis.utils.PageUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CashOutService {

    PageUtils selectCashOutList(Map<String, Object> params);

    int saveBody(CashOut cashOut);

    int update(CashOut cashOut);

    CashOut selectById(Long id);

    void cashOutSuccess(UserEntity userByWxId, String date, String money, String payWay, String url);

    List<CashOut> selectCashOutLimit3();

    void refundSuccess(UserEntity userByWxId, String date, String money, String url, String content);

    Double selectCashOutSum(Long userId, Date startTime, Date endTime);

    Double sumMoney(String time, Integer flag);

    Integer countMoney(String time, Integer flag);

    Integer stayMoney(String time, Integer flag);

    void updateMayMoney(int i, Long userId, Double money);

    Result cashMoney(Long userId, Double money, Integer classify,Integer userType,Long shopId);

    IPage<CashOut> selectAdminHelpProfit(Integer page, Integer limit, String startTime, String endTime, CashOut cashOut);

    HashMap<String,Object> statisticsMoney(String time, Integer flag);
}
