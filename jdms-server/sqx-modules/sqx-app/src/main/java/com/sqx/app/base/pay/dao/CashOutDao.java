package com.sqx.app.base.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.pay.entity.CashOut;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author fang
 * @date 2020/7/8
 */
@Mapper
public interface CashOutDao extends BaseMapper<CashOut> {

    List<CashOut> selectCashOutLimit3();

    Double selectCashOutSum(@Param("userId") Long userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Double sumMoney(@Param("time") String time, @Param("flag") Integer flag);

    Integer countMoney(@Param("time") String time, @Param("flag") Integer flag);

    Integer stayMoney(@Param("time") String time, @Param("flag") Integer flag);

    void updateMayMoney(@Param("type") Integer type, @Param("userId") Long userId, @Param("money") Double money);

    Double selectMayMoney(@Param("userId") Long userId);

    IPage<CashOut> selectAdminHelpProfit(@Param("pages") Page<CashOut> pages,
                                         @Param("startTime") String startTime,
                                         @Param("endTime") String endTime,
                                         @Param("cashOut") CashOut cashOut);

    BigDecimal sumCashMoney(@Param("time") String time,
                            @Param("flag") Integer flag,
                            @Param("state") Integer state,
                            @Param("classify") Integer classify);

    BigDecimal sumCashMoneyCount(@Param("time") String time,
                                 @Param("flag") Integer flag,
                                 @Param("state") Integer state,
                                 @Param("classify") Integer classify);

    BigDecimal selectSumMoney(@Param("userId") Long userId);
}
