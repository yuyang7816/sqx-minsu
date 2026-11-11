package com.sqx.app.base.banner.controller;

import com.sqx.app.base.banner.entity.Activity;
import com.sqx.app.base.banner.service.ActivityService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Tag(name = "菜单和活动管理---管理端")
@RequestMapping(value = "/activity")
public class ActivityController {


    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Operation(summary = "管理平台详情")
    @ResponseBody
    public Result getBanner(@Parameter(description = "活动id") @PathVariable Long id) {
        return Result.success().put("data",activityService.selectActivityById(id));
    }

    @RequestMapping(value = "/state/{state}", method = RequestMethod.GET)
    @Operation(summary = "根据状态查询菜单列表")
    @ResponseBody
    public Result getBannerState(@Parameter(description = "分类状态：1正常  2隐藏") @PathVariable String state) {
        return Result.success().put("data",activityService.selectByState(state));
    }

    @RequestMapping(value = "/updateActivity", method = RequestMethod.POST)
    @Operation(summary = "管理平台修改")
    @ResponseBody
    public Result addBanner(@RequestBody Activity activity) {
        activityService.updateActivity(activity);
        return Result.success();
    }

    @RequestMapping(value = "/updateActivityStatus", method = RequestMethod.POST)
    @Operation(summary = "管理平台修改状态")
    @ResponseBody
    public Result updateActivity(@RequestParam(required = false) @Parameter(description = "活动id") Long id) {
        Activity activity = activityService.selectActivityById(id);
        if("1".equals(activity.getState())){
            activity.setState("2");
            activityService.updateActivity(activity);
        }else{
            activity.setState("1");
            activityService.updateActivity(activity);
        }
        return Result.success();
    }

    @PostMapping("/insertActivity")
    @Operation(summary = "添加")
    @ResponseBody
    public Result insertActivity(@RequestBody Activity activity){
        activityService.insertActivity(activity);
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @Operation(summary = "管理平台删除")
    public Result deleteBanner(@Parameter(description = "活动id")@PathVariable Long id) {
        activityService.deleteActivity(id);
        return Result.success();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Operation(summary = "用户端获取广告位")
    @ResponseBody
    public Result getBannerList() {
        return Result.success().put("data",activityService.selectActivity());
    }

    @RequestMapping(value = "/selectActivity", method = RequestMethod.GET)
    @Operation(summary = "管理平台获取全部广告位")
    @ResponseBody
    public Result selectActivity() {
        return Result.success().put("data",activityService.selectActivitys());
    }





}