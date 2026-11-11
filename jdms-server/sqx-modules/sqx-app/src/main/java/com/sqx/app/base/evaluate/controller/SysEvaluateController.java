package com.sqx.app.base.evaluate.controller;

import com.sqx.app.base.evaluate.entity.SysEvaluate;
import com.sqx.app.base.evaluate.service.SysEvaluateService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wuchang
 * @since 2022-11-10
 */
@RestController
@Tag(name = "评分---用户端")
@RequestMapping("/app/evaluate/")
public class SysEvaluateController {
    @Autowired
    private SysEvaluateService evaluateService;

    @Operation(summary = "获取用户评价列表")
    @GetMapping("getUserEvaluate")
    public Result getUserEvaluateList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                      @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                      SysEvaluate evaluate) {
        return Result.success().put("data", evaluateService.getUserEvaluateList(page, limit, evaluate));
    }

    @Login
    @Operation(summary = "获取我的评价列表")
    @GetMapping("getMyEvaluate")
    public Result getUserEvaluateList(@RequestAttribute("userId") Long userId,
                                      @RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                      @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                      SysEvaluate evaluate) {
        evaluate.setUserId(userId);
        return Result.success().put("data", evaluateService.getUserEvaluateList(page, limit, evaluate));
    }

    @Login
    @Operation(summary = "用户评价")
    @PostMapping("addEvaluate")
    public Result addEvaluate(@RequestAttribute("userId") Long userId,
                              SysEvaluate evaluate) {
        evaluate.setUserId(userId);
        return evaluateService.addEvaluate(evaluate);
    }

    @Login
    @Operation(summary = "删除评价")
    @GetMapping("deleteEvaluate")
    public Result deleteEvaluate(@RequestAttribute("userId") Long userId,
                                 @Parameter(description = "评论id") @RequestParam(required = false) Long evaluateId) {
        return evaluateService.deleteEvaluate(userId, evaluateId, 0);
    }

    @Operation(summary = "获取评论详情")
    @GetMapping("getEvaluateInfo")
    public Result getEvaluateInfo(@Parameter(description = "评论id") @RequestParam(required = false) Long evaluateId) {
        return Result.success().put("data", evaluateService.getEvaluateInfo(evaluateId));
    }
}

