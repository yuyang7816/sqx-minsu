package com.sqx.app.base.evaluate.controller;

import com.sqx.app.base.evaluate.entity.SysEvaluate;
import com.sqx.app.base.evaluate.service.SysEvaluateService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "评分---管理端")
@RequestMapping("/admin/evaluate/")
public class AdminSysEvaluateController {
    @Autowired
    private SysEvaluateService evaluateService;

    @Operation(summary = "获取用户评价列表")
    @GetMapping("getUserEvaluate")
    public Result getUserEvaluateList(@RequestParam(required = false) @Parameter(description = "页码")Integer page,
                                      @RequestParam(required = false) @Parameter(description = "条数")Integer limit,
                                      SysEvaluate evaluate) {
        return Result.success().put("data", evaluateService.getUserEvaluateList(page, limit, evaluate));
    }

    @Operation(summary = "删除评论")
    @GetMapping("deleteEvaluate")
    public Result deleteEvaluate(@RequestParam @Parameter(description = "评论id")Long evaluateId) {
        evaluateService.removeById(evaluateId);
        return Result.success();
    }
}

