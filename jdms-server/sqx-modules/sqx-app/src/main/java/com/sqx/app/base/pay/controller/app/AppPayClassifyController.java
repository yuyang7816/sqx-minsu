package com.sqx.app.base.pay.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.app.base.pay.entity.PayClassify;
import com.sqx.app.base.pay.service.PayClassifyService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fang
 * @date 2022/4/16
 */
@Slf4j
@RestController
@Tag(name = "充值分类--用户端")
@RequestMapping(value = "/app/payClassify")
public class AppPayClassifyController {

    @Autowired
    private PayClassifyService payClassifyService;


    @GetMapping("/selectPayClassifyList")
    @Operation(summary = "查询充值分类")
    public Result selectPayClassifyList(PayClassify payClassify) {
        return Result.success().put("data", payClassifyService.list(new QueryWrapper<>(payClassify)
                .eq("is_enable", 1)
                .orderByAsc("sort")));
    }


}