package com.sqx.app.base.banner.controller.app;

import com.sqx.app.base.banner.service.BannerService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyuan
 * @date 2021/8/9
 */
@Slf4j
@RestController
@Tag(name = "banner图---用户端")
@RequestMapping(value = "/app/banner")
public class AppBannerController {


    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/selectBannerList", method = RequestMethod.GET)
    @Operation(summary = "查询所有banner图")
    @ResponseBody
    public Result selectBannerList(@RequestParam(required = false) @Parameter(description = "分类:1banner图  2首页分类") Integer classify) {
        return Result.success().put("data", bannerService.selectBannerList(classify));
    }


}