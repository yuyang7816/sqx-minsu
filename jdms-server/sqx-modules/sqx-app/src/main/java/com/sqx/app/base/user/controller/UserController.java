package com.sqx.app.base.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.app.base.pay.dao.CashOutDao;
import com.sqx.app.base.shops.dao.ShopsDao;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.user.dao.UserMoneyDao;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.entity.UserMoney;
import com.sqx.app.base.user.response.HomeMessageResponse;
import com.sqx.app.base.user.response.UserMessageResponse;
import com.sqx.app.base.orders.service.IOrdersService;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author fang
 * @date 2020/7/30
 */
@RestController
@Tag(name = "用户管理---管理端")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private ShopsDao shopsDao;

    @Autowired
    private UserMoneyDao userMoneyDao;

    @Autowired
    private CashOutDao cashOutDao;


    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET)
    @Operation(summary = "查询所有用户列表")
    @ResponseBody
    public Result selectUserList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                 @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                 @RequestParam(required = false) @Parameter(description = "用户id 手机号 昵称 模糊搜索") String phone,
                                 @RequestParam(required = false) @Parameter(description = "用户id 手机号 昵称 模糊搜索") String userName,
                                 @RequestParam(required = false) @Parameter(description = "性别 1男 2女") Integer sex,
                                 @RequestParam(required = false) @Parameter(description = "来源") String platform,
                                 @RequestParam(required = false) @Parameter(description = "手机终端") String sysPhone,
                                 @RequestParam(required = false) @Parameter(description = "是否是代理 1是 2否") Integer isAgent,
                                 @RequestParam(required = false) @Parameter(description = "状态 1正常 2封禁") Integer status) {
        return Result.success().put("data", userService.selectUserPage(page, limit, phone,userName, sex, platform, sysPhone, status, isAgent));
    }


    @RequestMapping(value = "/deleteUserByUserId/{userId}", method = RequestMethod.POST)
    @Operation(summary = "删除用户")
    @ResponseBody
    public Result deleteUserByUserId(@PathVariable("userId") @Parameter(description = "用户id") Long userId) {
        userService.removeById(userId);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserByUserId", method = RequestMethod.POST)
    @Operation(summary = "修改用户")
    @ResponseBody
    public Result updateUserByUserId(@RequestBody UserEntity userEntity) {

        userService.updateById(userEntity);
        return Result.success();
    }

    @RequestMapping(value = "/selectUserById", method = RequestMethod.GET)
    @Operation(summary = "获取用户详细信息")
    @ResponseBody
    public Result selectUserById(UserEntity user, Long userId) {
        UserEntity userEntity = userService.selectUserById(userId);
        if (userEntity.getShopId() != null) {
            Shops shop = shopsDao.selectOne(new LambdaQueryWrapper<Shops>()
                    .eq(Shops::getShopId, userEntity.getShopId()));
            if (shop != null) {
                userEntity.setShopStatus(shop.getAuditStatus());
            }
        }
        UserMoney userMoney = userMoneyDao.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userEntity.getUserId()));
        if (userMoney == null) {
            userMoney = new UserMoney();
            userMoney.setUserId(userEntity.getUserId());
            userMoney.setMoney(BigDecimal.ZERO);
            userMoneyDao.insert(userMoney);
            userEntity.setCanCashMoney(BigDecimal.ZERO);
        } else {
            userEntity.setCanCashMoney(userMoney.getMoney());
        }
        BigDecimal totalMoney = cashOutDao.selectSumMoney(userEntity.getUserId());
        userEntity.setTotalCashMoney(totalMoney == null ? BigDecimal.ZERO : totalMoney);
        return Result.success().put("data", userEntity);
    }

    @RequestMapping(value = "/updateUserStatusByUserId", method = RequestMethod.GET)
    @Operation(summary = "禁用或启用用户")
    @ResponseBody
    public Result updateUserByUserId(@RequestParam(required = false) @Parameter(description = "用户id") Long userId) {
        UserEntity byId = userService.getById(userId);
        if (byId.getStatus().equals(1)) {
            byId.setStatus(2);
        } else {
            byId.setStatus(1);
        }
        userService.updateById(byId);
        return Result.success();
    }


    /**
     * 获取openid
     *
     * @param code 微信code
     * @return openid
     */
    @GetMapping("/openId/{code:.+}/{userId}")
    @Operation(summary = "根据code获取openid")
    public Result getOpenid(@Parameter(description = "微信code") @PathVariable("code") String code,
                            @Parameter(description = "用户id") @PathVariable("userId") Long userId) {
        return userService.getOpenId(code, userId);
    }

    /**
     * 信息分析
     *
     * @return
     */
    @GetMapping("/homeMessage")
    @Operation(summary = "信息分析")
    public Result homeMessage() {
        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
        //   0查总   1查天  2查月  3查年
        //设置总用户人数
        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null));
        //设置今日新增
        homeMessageResponse.setNewToday(userService.queryUserCount(1, null));
        //设置本月新增
        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null));
        //设置本年新增
        homeMessageResponse.setNewYear(userService.queryUserCount(3, null));

        //设置总收入
        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0));
        //设置今日收入
        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1));
        //设置本月收入
        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2));
        //设置本年收入
        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3));
        //设置今日订单数
        homeMessageResponse.setTodayOrderCount(ordersService.selectTodayOrderCount(LocalDate.now().toString()));

        return Result.success().put("data", homeMessageResponse);
    }


    /**
     * 用户分析
     */
    @GetMapping("/userMessage")
    @Operation(summary = "用户分析")
    public Result userMessage(String date, int type) {
        UserMessageResponse userMessageResponse = new UserMessageResponse();
        userMessageResponse.setTotalNumber(userService.queryUserCount(type, date));
        userMessageResponse.setVipUserNumber(userService.userMessage(date, type));
        userMessageResponse.setUserNumber(userService.queryUserCount(type, date) - userService.userMessage(date, type));
        return Result.success().put("data", userMessageResponse);
    }


}