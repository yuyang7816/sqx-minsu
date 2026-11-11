package com.sqx.app.base.pay.controller.app;

import com.sqx.app.base.pay.service.CashOutService;
import com.sqx.app.base.pay.service.PayDetailsService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import com.sqx.common.mybatis.utils.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/5/15
 */
@Slf4j
@RestController
@Tag(name = "提现---用户端")
@RequestMapping(value = "/app/cash")
public class AppCashController {

    /**
     * 提现记录
     */
    @Autowired
    private CashOutService cashOutService;
    @Autowired
    private PayDetailsService payDetailsService;


    @Login
    @GetMapping(value = "/cashMoney")
    @Operation(summary = "发起提现")
    public Result cashMoney(@RequestAttribute("userId") Long userId,
                            @RequestParam(required = false) @Parameter(description = "提现金额") Double money,
                            @RequestParam(required = false) @Parameter(description = "提现方式 1支付宝 2微信小程序 3微信公众号") Integer classify,
                            @RequestParam(required = false) @Parameter(description = "1用户 2商户")Integer userType,
                            @RequestParam(required = false) @Parameter(description = "商户id")Long shopId) {
        return cashOutService.cashMoney(userId, money, classify,userType,shopId);
    }

    @Login
    @RequestMapping(value = "/selectUserRechargeByUserId", method = RequestMethod.GET)
    @Operation(summary = "查询某个用户充值信息列表")
    @ResponseBody
    public Result selectUserRechargeByUserId(@RequestParam(required = false) @Parameter(description = "页码") int page,
                                             @RequestParam(required = false) @Parameter(description = "条数") int limit,
                                             @RequestParam(required = false) @Parameter(description = "开始时间") String startTime,
                                             @RequestParam(required = false) @Parameter(description = "结束时间") String endTime,
                                             @RequestAttribute("userId") Long userId,
                                             @RequestParam(required = false) @Parameter(description = "0待支付 1支付成功 2失败") Integer state) {
        return Result.success().put("data", payDetailsService.selectPayDetails(page, limit, startTime, endTime, userId, state, null));
    }

    @Login
    @RequestMapping(value = "/selectPayDetails", method = RequestMethod.GET)
    @Operation(summary = "查询提现记录列表")
    @ResponseBody
    public Result selectHelpProfit(@RequestParam(required = false) @Parameter(description = "页码") int page,
                                   @RequestParam(required = false) @Parameter(description = "条数") int limit,
                                   @RequestAttribute("userId") Long userId,
                                   @RequestParam(required = false) @Parameter(description = "1用户 2商户")Integer userType) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("limit", limit);
        map.put("userId", userId);
        map.put("userType",userType);
        PageUtils pageUtils = cashOutService.selectCashOutList(map);
        return Result.success().put("data", pageUtils);
    }


}