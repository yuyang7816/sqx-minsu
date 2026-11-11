package com.sqx.app.base.shops.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.shops.entity.ScFollow;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shops.entity.TbFootprint;
import com.sqx.app.base.utils.Result;

import java.time.LocalDate;

/**
 * <p>
 * 酒店 服务类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
public interface IShopsService extends IService<Shops> {

    Result saveShops(Shops shops);

    Result deleteShopId(Long shopId);

    IPage<Shops> getShopList(Integer page, Integer limit, Shops shops);

    IPage<Shops> getAppShopList(Integer page, Integer limit , LocalDate startTime, LocalDate endTime, Shops shops);

    Shops getStaffShop(Long userId);

    Result exitShop(Long userId);

    IPage<TbFootprint> getFootprintList(Integer page, Integer limit, Long userId);

    IPage<Shops> selectCollectShopsList(Integer page, Integer limit, Long userId);

    Result auditShop(Shops shops);

    Result statisticsShop(Long userId, Long shopId);

    Result statisticsShopOrder(Long userId, Long shopId);

    Result statisticsShopOrder1(Long shopId);

    Result statisticsShopRoom(Long userId, Long shopId);

    Result statisticsShopRoom1(Long shopId);

    Result selectOrderList(Long userId,Integer page,Integer limit, Orders orders);

    Result selectTodayOrderList(Long userId, Integer page, Integer limit, Orders orders);

    Result selectOrderById(Long userId, Long orderId,Long shopId);

    Result statisticsShop1(Long shopId);

}
