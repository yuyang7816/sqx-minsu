package com.sqx.app.base.shops.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.shops.dao.ScFollowDao;
import com.sqx.app.base.shops.dao.TbFootprintDao;
import com.sqx.app.base.shops.entity.ScFollow;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shops.entity.TbFootprint;
import com.sqx.app.base.shops.service.IShopsService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 酒店
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
@RestController
@RequestMapping("/app/shops/")
@Tag(name = "酒店管理---用户端")
public class AppShopsController {

    @Autowired
    private IShopsService shopsService;
    @Autowired
    private TbFootprintDao tbFootprintDao;
    @Autowired
    private ScFollowDao scFollowDao;


    @Login
    @Operation(summary = "添加或修改酒店")
    @PostMapping("save")
    public Result saveShops(Shops shops, @RequestAttribute Long userId) {
        shops.setUserId(userId);
        return shopsService.saveShops(shops);
    }

    @Operation(summary = "删除酒店")
    @PostMapping("deleteShopId")
    public Result deleteShopId(@RequestParam(required = false) @Parameter(description = "酒店id") Long shopId) {
        return shopsService.deleteShopId(shopId);
    }

    @Login
    @Operation(summary = "统计酒店的今日收益、本月收益、总收益、酒店的可提现金额、员工总数")
    @GetMapping("statisticsShop")
    public Result statisticsShop(@RequestAttribute("userId") Long userId, Long shopId) {
        return shopsService.statisticsShop(userId, shopId);
    }

    @Login
    @Operation(summary = "统计酒店的待核销、已完成、已退款的订单数量")
    @GetMapping("statisticsShopOrder")
    public Result statisticsShopOrder(@RequestAttribute("userId") Long userId, Long shopId) {
        return shopsService.statisticsShopOrder(userId, shopId);
    }

    @Login
    @Operation(summary = "统计酒店的上架中、已下架的房间数量")
    @GetMapping("statisticsShopRoom")
    public Result statisticsShopRoom(@RequestAttribute("userId") Long userId, Long shopId) {
        return shopsService.statisticsShopRoom(userId, shopId);
    }

    @Login
    @Operation(summary = "查看当前酒店的订单列表")
    @GetMapping("selectOrderList")
    public Result selectOrderList(@RequestAttribute("userId") Long userId,
                                  @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                  @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                  Orders orders) {
        return shopsService.selectOrderList(userId, page, limit, orders);
    }

    @Login
    @Operation(summary = "查看当前酒店今日的订单列表")
    @GetMapping("selectTodayOrderList")
    public Result selectTodayOrderList(@RequestAttribute("userId") Long userId,
                                       @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                       @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                       Orders orders) {
        return shopsService.selectTodayOrderList(userId, page, limit, orders);
    }

    @Login
    @Operation(summary = "查看当前酒店的订单详情")
    @GetMapping("selectOrderById")
    public Result selectOrderById(@RequestAttribute("userId") Long userId,
                                  Long orderId,
                                  Long shopId) {
        return shopsService.selectOrderById(userId,orderId,shopId);
    }


    @Login
    @Operation(summary = "获取酒店详情")
    @GetMapping("getShopById")
    public Result getShopById(@RequestParam(required = false) @Parameter(description = "酒店id") Long shopId,
                              @RequestAttribute("userId") @Parameter(description = "用户id") Long userId) {
        Shops shops = shopsService.getById(shopId);
        if (shops != null) {
            TbFootprint tbFootprint = new TbFootprint();
            tbFootprint.setUserId(userId);
            tbFootprint.setShopId(shopId);
            tbFootprint.setVisitTime(new Date());
            tbFootprintDao.insert(tbFootprint);
            Long isCollect = scFollowDao.selectCount(new LambdaQueryWrapper<ScFollow>()
                    .eq(ScFollow::getUserId, userId)
                    .eq(ScFollow::getById, shopId)
                    .eq(ScFollow::getClassify, 1));
            shops.setIsCollect(isCollect == null || isCollect == 0 ? 0 : 1);
            return Result.success().put("data", shops);
        }
        return Result.error("酒店不存在");
    }

    @Login
    @Operation(summary = "获取足迹列表")
    @GetMapping("getFootprintList")
    public Result getFootprintList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                   @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                   @RequestAttribute("userId") Long userId) {
        return Result.success().put("data", shopsService.getFootprintList(page, limit, userId));
    }

    @Login
    @Operation(summary = "收藏酒店")
    @GetMapping("collectShops")
    public Result collectShops(@RequestParam(required = false) @Parameter(description = "酒店id") Long shopId,
                               @RequestAttribute("userId") Long userId) {
        Shops shops = shopsService.getById(shopId);
        if (shops == null) {
            return Result.error("酒店不存在");
        }
        ScFollow scFollow1 = scFollowDao.selectOne(new LambdaQueryWrapper<ScFollow>()
                .eq(ScFollow::getUserId, userId)
                .eq(ScFollow::getById, shopId)
                .eq(ScFollow::getClassify, 1));
        if (scFollow1 == null) {
            ScFollow scFollow = new ScFollow();
            scFollow.setUserId(userId);
            scFollow.setById(shops.getShopId());
            scFollow.setClassify(1);
            scFollow.setCreateTime(LocalDateTime.now());
            scFollowDao.insert(scFollow);
        }
        return Result.success();
    }

    @Login
    @Operation(summary = "取消收藏酒店")
    @GetMapping("cancelCollectShops")
    public Result cancelCollectShops(@RequestParam(required = false) @Parameter(description = "酒店id") Long shopId,
                                     @RequestAttribute("userId") Long userId) {
        ScFollow scFollow = scFollowDao.selectOne(new LambdaQueryWrapper<ScFollow>()
                .eq(ScFollow::getUserId, userId)
                .eq(ScFollow::getById, shopId)
                .eq(ScFollow::getClassify, 1));
        if (scFollow == null) {
            return Result.error("收藏记录不存在");
        }
        int result = scFollowDao.deleteById(scFollow.getFollowId());
        if (result <= 0) {
            return Result.error("取消收藏失败");
        }
        return Result.success();
    }

    @Login
    @Operation(summary = "查看收藏酒店的列表")
    @GetMapping("selectCollectShopsList")
    public Result selectCollectShopsList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                         @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                         @RequestAttribute("userId") Long userId) {
        return Result.success().put("data", shopsService.selectCollectShopsList(page, limit, userId));
    }


    @Operation(summary = "获取酒店列表")
    @GetMapping("getAppShopList")
    public Result getAppShopList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                 @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime,
                                 @RequestParam(required = false) @Parameter(description = "排序类型：1默认排序 2位置优先 3热门优先 4评分从高到低 5评分从低到高") Integer sortType,
                                 @RequestParam(required = false) @Parameter(description = "最低价格筛选") BigDecimal minPrice,
                                 @RequestParam(required = false) @Parameter(description = "最高价格筛选") BigDecimal maxPrice,
                                 Shops shops) {
        if (shops == null) {
            shops = new Shops();
        }
        if (sortType != null) {
            shops.setSortType(sortType);
        }
        if (minPrice != null) {
            shops.setMinPrice(minPrice);
        }
        if (maxPrice != null) {
            shops.setMaxPrice(maxPrice);
        }
        return Result.success().put("data", shopsService.getAppShopList(page, limit, startTime, endTime, shops));
    }

    @Login
    @Operation(summary = "获取员工所属酒店")
    @GetMapping("getStaffShop")
    public Result getStaffShop(@RequestAttribute("userId") Long userId) {
        return Result.success().put("data", shopsService.getStaffShop(userId));
    }

    @Login
    @Operation(summary = "员工退出酒店")
    @PostMapping("exitShop")
    public Result exitShop(@RequestAttribute("userId") Long userId) {
        return shopsService.exitShop(userId);
    }
}
