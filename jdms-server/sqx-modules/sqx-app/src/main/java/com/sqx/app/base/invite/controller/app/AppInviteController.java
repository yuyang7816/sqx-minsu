package com.sqx.app.base.invite.controller.app;

import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.utils.Result;
import com.sqx.app.base.utils.SenInfoCheckUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Tag(name = "邀请收益---用户端", description = "邀请收益---用户端")
@RequestMapping(value = "/app/invite")
public class AppInviteController {

    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;

    @Login
    @RequestMapping(value = "/selectInviteAndPoster", method = RequestMethod.GET)
    @Operation(summary = "查看我的邀请码和海报二维码")
    @ResponseBody
    public Result selectInviteAndPoster(@RequestAttribute Long userId){
        UserEntity userEntity = userService.queryByUserId(userId);
        CommonInfo one = commonInfoService.findOne(19);
        Map<String,Object> map=new HashMap<>();
        map.put("url",one.getValue());
        map.put("user",userEntity);
        return Result.success().put("data",map);
    }



    @GetMapping("/mpCreateQr")
    @Operation(summary = "小程序推广二维码")
    public void mpCreateQr(@Parameter(description = "推广码")@RequestParam String invitationCode,String page, HttpServletResponse response) {
        SenInfoCheckUtil.getImg(invitationCode,page,response);
    }



    @Login
    @Operation(summary = "用户推广信息")
    @GetMapping(value = "getExtensionData")
    public Result getExtensionData(@RequestAttribute("userId") Long userId) {
        return Result.success().put("data", userService.getExtensionData(userId));
    }

    @Login
    @Operation(summary = "获取推广用户列表")
    @GetMapping(value = "getExtensionList")
    public Result getExtensionList(@RequestAttribute("userId") Long userId,
                                   @RequestParam(required = false) @Parameter(description = "1 是一级用户  2是二级用户")Integer type,
                                   @RequestParam(required = false) @Parameter(description = "页码")Integer page,
                                   @RequestParam(required = false) @Parameter(description = "条数")Integer limit) {
        return Result.success().put("data", userService.getExtensionList(userId,type, page, limit));
    }

    @Login
    @Operation(summary = "获取推广用户列表")
    @GetMapping(value = "getAuthExtensionList")
    public Result getAuthExtensionList(@RequestAttribute("userId") Long userId,
                                       @RequestParam(required = false) @Parameter(description = "页码")Integer page,
                                       @RequestParam(required = false) @Parameter(description = "条数")Integer limit) {
        return Result.success().put("data", userService.getAuthExtensionList(userId, page, limit));
    }


    @Login
    @Operation(summary = "获取邀请的用户收益列表")
    @GetMapping(value = "getProfitList")
    public Result getProfitList(@RequestAttribute("userId") Long userId,
                                @RequestParam(required = false) @Parameter(description = "1 一级用户  2二级用户")Integer type,
                                @RequestParam(required = false) @Parameter(description = "页码")Integer page,
                                @RequestParam(required = false) @Parameter(description = "条数")Integer limit) {
        return Result.success().put("data", userService.getProfitList(userId,type, page, limit));
    }

}