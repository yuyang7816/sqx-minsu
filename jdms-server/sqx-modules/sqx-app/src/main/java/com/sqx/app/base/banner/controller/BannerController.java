package com.sqx.app.base.banner.controller;

import com.sqx.app.base.banner.entity.Banner;
import com.sqx.app.base.banner.service.BannerService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Tag(name = "banner图---管理端")
@RequestMapping(value = "/banner")
public class BannerController {


    @Autowired
    private BannerService bannerService;


    @RequestMapping(value = "/selectBannerList", method = RequestMethod.GET)
    @Operation(summary = "查询所有banner图")
    @ResponseBody
    public Result selectBannerList(@RequestParam(required = false) @Parameter(description = "分类: 1 banner图  2 首页分类")Integer classify){
        return Result.success().put("data",bannerService.selectBannerLists(classify));
    }


    @RequestMapping(value = "/selectBannerPage", method = RequestMethod.GET)
    @Operation(summary = "查询所有banner图")
    @ResponseBody
    public Result selectBannerPage(@RequestParam(required = false) @Parameter(description = "页码")Integer page,
                                   @RequestParam(required = false) @Parameter(description = "条数")Integer limit,
                                   @RequestParam(required = false) @Parameter(description = "分类: 1 banner图  2 首页分类")Integer classify,
                                   @RequestParam(required = false) @Parameter(description = "状态 1正常 2隐藏")Integer state){
        return Result.success().put("data",bannerService.selectPage(page,limit,classify,state));
    }


    @RequestMapping(value = "/selectBannerById", method = RequestMethod.GET)
    @Operation(summary = "根据id查看详细信息")
    @ResponseBody
    public Result selectBannerById(@RequestParam(required = false) @Parameter(description = "banner图id")Long id){
        return Result.success().put("data",bannerService.selectBannerById(id));
    }

    @RequestMapping(value = "/updateBannerStateById", method = RequestMethod.POST)
    @Operation(summary = "隐藏banner图")
    @ResponseBody
    public Result updateBannerStateById(@RequestParam(required = false) @Parameter(description = "banner图id")Long id){
        return bannerService.updateBannerStateById(id);
    }

    @RequestMapping(value = "/updateBannerById", method = RequestMethod.POST)
    @Operation(summary = "修改banner图")
    @ResponseBody
    public Result updateBannerById(@RequestBody Banner banner){
        bannerService.updateBannerById(banner);
        return Result.success();
    }

    @RequestMapping(value = "/deleteBannerById", method = RequestMethod.GET)
    @Operation(summary = "删除banner图")
    @ResponseBody
    public Result deleteBannerById(@RequestParam(required = false) @Parameter(description = "banner图id，多个逗号分隔")String ids){
        bannerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.success();
    }

    @RequestMapping(value = "/insertBanner", method = RequestMethod.POST)
    @Operation(summary = "添加banner图")
    @ResponseBody
    public Result insertBanner(@RequestBody Banner banner){
        bannerService.insertBanner(banner);
        return Result.success();
    }



}