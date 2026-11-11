package com.sqx.app.base.userMoneyDetails.controller;

import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.userMoneyDetails.entity.UserMoneyDetails;
import com.sqx.app.base.userMoneyDetails.service.UserMoneyDetailsService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/app/moneyDetails/")
@RestController
@Tag(name = "钱包明细---用户端")
public class UserMoneyDetailsController {
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Login
    @Operation(summary = "获取我的钱包明细列表")
    @GetMapping("/getMoneyDetailsList")
    public Result getMoneyDetailsList(@RequestAttribute("userId")Long userId,
                                      @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                      @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                      UserMoneyDetails userMoneyDetails) {
        userMoneyDetails.setUserId(userId);
        return Result.success().put("data", userMoneyDetailsService.queryUserMoneyDetails(page, limit, userMoneyDetails));
    }

}
