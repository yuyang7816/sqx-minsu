package com.sqx.app.base.pay.controller;

import com.sqx.app.base.pay.entity.PayClassify;
import com.sqx.app.base.pay.service.PayClassifyService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fang
 * @date 2022/4/16
 */
@Slf4j
@RestController
@Tag(name = "充值分类---管理端")
@RequestMapping(value = "/admin/payClassify")
public class PayClassifyController {

    @Autowired
    private PayClassifyService payClassifyService;


    @PostMapping("/insertPayClassify")
    @Operation(summary = "添加充值分类")
    public Result insertPayClassify(PayClassify payClassify) {
        return payClassifyService.save(payClassify) ? Result.success() : Result.error();
    }

    @PostMapping("/updatePayClassify")
    @Operation(summary = "修改充值分类")
    public Result updatePayClassify(PayClassify payClassify) {
        return payClassifyService.updateById(payClassify) ? Result.success() : Result.error();
    }

    @PostMapping("/deletePayClassify")
    @Operation(summary = "删除充值分类")
    public Result deletePayClassify(@RequestParam(required = false) @Parameter(description = "充值分类id") Long payClassifyId) {
        return payClassifyService.removeById(payClassifyId) ? Result.success() : Result.error();
    }

    @GetMapping("/selectPayClassifyList")
    @Operation(summary = "查询充值分类")
    public Result selectPayClassifyList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                        @RequestParam(required = false) @Parameter(description = "条数") Integer limit) {
        return Result.success().put("data", payClassifyService.selectPayClassifyList(page, limit));
    }
}