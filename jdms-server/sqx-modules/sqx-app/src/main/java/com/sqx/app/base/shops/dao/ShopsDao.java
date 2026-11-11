package com.sqx.app.base.shops.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.shops.entity.Shops;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 酒店 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
@Mapper
public interface ShopsDao extends BaseMapper<Shops> {

    IPage<Shops> getAppShopList(@Param("pages") Page<Shops> pages,
                                @Param("startTime") LocalDate startTime,
                                @Param("endTime") LocalDate endTime,
                                @Param("shops") Shops shops);

    Shops getStaffShop(@Param("userId") Long userId);

    Map<String, Object> statisticsShop(@Param("shopId") Long shopId,
                                       @Param("userId") Long userId);

    Map<String, Object> statisticsShop1(@Param("shopId") Long shopId);

    Map<String, Object> statisticsShopOrder(@Param("shopId") Long shopId,
                                            @Param("userId") Long userId);

    Map<String, Object> statisticsShopOrder1(@Param("shopId") Long shopId);

    Map<String, Object> statisticsShopRoom(@Param("shopId") Long shopId,
                                           @Param("userId") Long userId);

    Map<String, Object> statisticsShopRoom1(@Param("shopId") Long shopId);

    IPage<Shops> selectPages(Page<Shops> pages,
                             @Param("shops") Shops shops);

}
