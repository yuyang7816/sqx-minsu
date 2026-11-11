package com.sqx.app.base.vipConfig.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.utils.Result;
import com.sqx.app.base.vipConfig.entity.VipConfig;

/**
 * <p>
 * vip配置表 服务类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
public interface IVipConfigService extends IService<VipConfig> {

    Result saveVipConfig(VipConfig vipConfig);

    IPage<VipConfig> getVipConfigList(Integer page, Integer limit, VipConfig vipConfig);

    Result buyVipCallback(Long userId, Long vipId,Integer classify);

    /**
     * 给用户增加月数
     * @param userId
     * @param vipConfig
     * @return
     */
    int giveUserVip(Long userId, VipConfig vipConfig);
}
