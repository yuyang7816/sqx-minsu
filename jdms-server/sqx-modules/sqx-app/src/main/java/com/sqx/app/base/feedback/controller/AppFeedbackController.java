package com.sqx.app.base.feedback.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.feedback.domain.bo.FeedbackInfoBo;
import com.sqx.app.base.feedback.domain.bo.FeedbackPageBo;
import com.sqx.app.base.feedback.domain.bo.FeedbackSaveBo;
import com.sqx.app.base.feedback.domain.bo.FeedbackUpdateBo;
import com.sqx.app.base.feedback.domain.vo.FeedbackVo;
import com.sqx.app.base.feedback.service.FeedbackService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 意见反馈 前端控制器
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-06 15:53:00
 */
@Tag(name = "用户端-意见反馈", description = "用户端-意见反馈")
@RestController
@RequestMapping("/app/feedback")
public class AppFeedbackController {

    @Resource
    private FeedbackService feedbackService;

    /**
     * 保存
     */
    @Login
    @Operation(summary = "保存")
    @PostMapping("save")
    public Result<Void> save(@RequestAttribute Long userId, @RequestBody @Validated FeedbackSaveBo feedbackSaveBo) {
        feedbackSaveBo.setUserId(userId);
        feedbackService.saveInfo(feedbackSaveBo);
        return Result.success();
    }

    /**
     * 列表
     */
    @Login
    @Operation(summary = "列表")
    @PostMapping("page")
    public Result<IPage<FeedbackVo>> page(@RequestAttribute Long userId, @RequestBody @Validated FeedbackPageBo feedbackPageBo) {
        feedbackPageBo.setUserId(userId);
        IPage<FeedbackVo> voIPage = feedbackService.pageInfo(feedbackPageBo);
        return Result.success(voIPage);
    }

    /**
     * 信息
     */
    @Operation(summary = "信息")
    @PostMapping("info")
    public Result<FeedbackVo> info(@RequestBody @Validated FeedbackInfoBo feedbackInfoBo) {
        FeedbackVo vo = feedbackService.info(feedbackInfoBo);
        return Result.success(vo);
    }

    /**
     * 更新
     */
    @Operation(summary = "更新")
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated FeedbackUpdateBo feedbackUpdateBo) {
        feedbackService.updateInfo(feedbackUpdateBo);
        return Result.success();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除")
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody @Validated FeedbackInfoBo feedbackInfoBo) {
        feedbackService.delete(feedbackInfoBo);
        return Result.success();
    }
}
