package com.sqx.app.base.userMoneyDetails.controller;

import com.sqx.app.base.userMoneyDetails.entity.UserMoneyDetails;
import com.sqx.app.base.userMoneyDetails.service.UserMoneyDetailsService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/moneyDetails/")
@RestController
@Tag(name = "钱包明细---管理端")
public class AdminUserMoneyDetailsController {
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Operation(summary = "钱包明细")
    @GetMapping("/getMoneyDetailsList")
    public Result getMoneyDetailsList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                      @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                      UserMoneyDetails userMoneyDetails) {
        return Result.success().put("data", userMoneyDetailsService.queryUserMoneyDetails(page, limit, userMoneyDetails));
    }

}
