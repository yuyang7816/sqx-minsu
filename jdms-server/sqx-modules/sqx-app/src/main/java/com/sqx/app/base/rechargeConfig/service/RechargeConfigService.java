package com.sqx.app.base.rechargeConfig.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.rechargeConfig.entity.RechargeConfig;
import com.sqx.app.base.utils.Result;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wuchang
 * @since 2022-11-04
 */
public interface RechargeConfigService extends IService<RechargeConfig> {

    Result addConfig(RechargeConfig rechargeConfig);

    Result updateConfig(RechargeConfig rechargeConfig);

    RechargeConfig getRechargeConfigInfo(Long rechargeId);

    Result deleteConfig(Long rechargeId);

    IPage<RechargeConfig> getConfigInfoList(Integer page, Integer limit, RechargeConfig rechargeConfig);

    /**
     * 赠送优惠券
     *
     * @param userId 充值用户id
     * @param money  充值金额
     */
}
