package com.sqx.app.base.shopMoneyDetails.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.shopMoneyDetails.entity.ShopMoneyDetails;

/**
 * <p>
 * 酒店钱包明细 服务类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
public interface IShopMoneyDetailsService extends IService<ShopMoneyDetails> {

    IPage<ShopMoneyDetails> getShopMoneyDetailsList(Integer page, Integer limit, ShopMoneyDetails shopMoneyDetails);

    IPage<ShopMoneyDetails> getCashMoneyDetailsList(Integer page, Integer limit, ShopMoneyDetails shopMoneyDetails);

}
