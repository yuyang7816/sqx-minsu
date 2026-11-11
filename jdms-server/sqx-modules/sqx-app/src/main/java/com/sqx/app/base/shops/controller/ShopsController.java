package com.sqx.app.base.shops.controller;

import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.shopMoneyDetails.dao.ShopMoneyDetailsDao;
import com.sqx.app.base.shopMoneyDetails.entity.ShopMoneyDetails;
import com.sqx.app.base.shops.dao.ShopsDao;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shops.service.IShopsService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
@RequestMapping("/admin/shops/")
@Tag(name = "酒店管理---管理端")
public class ShopsController {

    @Autowired
    private IShopsService shopsService;

    @Autowired
    private ShopsDao shopsDao;

    @Autowired
    private ShopMoneyDetailsDao shopMoneyDetailsDao;

    @Operation(summary = "添加或修改酒店")
    @PostMapping("save")
    public Result saveShops(Shops shops) {
        return shopsService.saveShops(shops);
    }

    @Operation(summary = "获取酒店详情")
    @GetMapping("getShopById")
    public Result getShopById(@RequestParam(required = false) @Parameter(description = "酒店id") Long shopId) {
        return Result.success().put("data", shopsService.getById(shopId));
    }

    @Operation(summary = "删除酒店")
    @PostMapping("deleteShopId")
    public Result deleteShopId(@RequestParam(required = false) @Parameter(description = "酒店id") Long shopId) {
        return shopsService.deleteShopId(shopId);
    }

    @Operation(summary = "审核酒店")
    @PostMapping("auditShop")
    public Result auditShop(@RequestBody Shops shops) {
        return shopsService.auditShop(shops);
    }

    @RequestMapping(value = "/addShopMoney", method = RequestMethod.POST)
    @Operation(summary = "修改商户金额")
    @ResponseBody
    public Result addCannotMoney(@RequestParam(required = false) @Parameter(description = "商户id") Long shopId,
                                 @RequestParam(required = false) @Parameter(description = "金额") Double money,
                                 @RequestParam(required = false) @Parameter(description = "1增加 2减少") Integer type) {
        Shops shop = shopsDao.selectById(shopId);
        if (shop == null) {
            return Result.error("商户不存在");
        }
        if (type == 1) {
            shop.setCashMoney(shop.getCashMoney().add(BigDecimal.valueOf(money)));
            shop.setAllMoney(shop.getAllMoney().add(BigDecimal.valueOf(money)));
            shopsDao.updateById(shop);
            ShopMoneyDetails userMoneyDetails = new ShopMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(shop.getUserId());
            userMoneyDetails.setContent("系统充值商户金额：" + money);
            userMoneyDetails.setTitle("系统充值商户金额");
            userMoneyDetails.setType(1);
            userMoneyDetails.setClassify(4);
            userMoneyDetails.setCreateTime(LocalDateTime.now());
            shopMoneyDetailsDao.insert(userMoneyDetails);
            return Result.success();
        } else {
            if (shop.getCashMoney().doubleValue() >= money) {
                shop.setCashMoney(shop.getCashMoney().subtract(BigDecimal.valueOf(money)));
            } else {
                shop.setCashMoney(BigDecimal.ZERO);
            }
            shopsDao.updateById(shop);
            ShopMoneyDetails userMoneyDetails = new ShopMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(shop.getUserId());
            userMoneyDetails.setContent("系统减少商户金额：" + money);
            userMoneyDetails.setTitle("系统减少商户金额");
            userMoneyDetails.setType(2);
            userMoneyDetails.setClassify(4);
            userMoneyDetails.setCreateTime(LocalDateTime.now());
            shopMoneyDetailsDao.insert(userMoneyDetails);
            return Result.success();
        }
    }

    @Operation(summary = "获取酒店列表")
    @GetMapping("getShopList")
    public Result getShopList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                              @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                              Shops shops) {
        return Result.success().put("data", shopsService.getShopList(page, limit, shops));
    }

    @Operation(summary = "统计酒店的今日收益、本月收益、总收益、酒店的可提现金额、员工总数")
    @GetMapping("statisticsShop")
    public Result statisticsShop(Long shopId) {
        return shopsService.statisticsShop1(shopId);
    }

    @Operation(summary = "统计酒店的待核销、已完成、已退款的订单数量")
    @GetMapping("statisticsShopOrder")
    public Result statisticsShopOrder(Long shopId) {
        return shopsService.statisticsShopOrder1(shopId);
    }

    @Operation(summary = "统计酒店的上架中、已下架的房间数量")
    @GetMapping("statisticsShopRoom")
    public Result statisticsShopRoom(Long shopId) {
        return shopsService.statisticsShopRoom1(shopId);
    }


    @Operation(summary = "查看当前酒店的订单列表")
    @GetMapping("selectOrderList")
    public Result selectOrderList(Long userId,
                                  @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                  @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                  Orders orders) {
        return shopsService.selectOrderList(userId, page, limit, orders);
    }

    @Operation(summary = "查看当前酒店今日的订单列表")
    @GetMapping("selectTodayOrderList")
    public Result selectTodayOrderList(Long userId,
                                       @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                       @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                       Orders orders) {
        return shopsService.selectTodayOrderList(userId, page, limit, orders);
    }
}
