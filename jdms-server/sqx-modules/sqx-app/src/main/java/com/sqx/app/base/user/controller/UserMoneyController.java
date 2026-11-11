package com.sqx.app.base.user.controller;

import com.sqx.app.base.user.service.UserMoneyService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequestMapping("/admin/userMoney/")
@RestController
@Tag(name = "用户管理---管理端")
public class UserMoneyController {
    @Autowired
    private UserMoneyService userMoneyService;

    @PostMapping("/updateUserMoney")
    @Operation(summary = "修改用户余额")
    public Result updateUserMoney(@RequestParam(required = false) @Parameter(description = "1增 2减") Integer type,
                                  @RequestParam(required = false) @Parameter(description = "用户id") Long userId,
                                  @RequestParam(required = false) @Parameter(description = "金额") BigDecimal money) {
        return userMoneyService.updateUserMoney(type, userId, money);
    }

}
