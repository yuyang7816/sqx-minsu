package com.sqx.app.base.user.controller.app;

import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.user.entity.UserAndInfo;
import com.sqx.app.base.user.service.UserAndInfoService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/5/21 14:25
 */
@RestController
@RequestMapping("/app/userAndInfo")
@Tag(name = "常用人信息---用户端")
public class AppUserAndInfoController {

    @Autowired
    private UserAndInfoService userAndInfoService;

    @Login
    @Operation(summary = "添加常用人信息")
    @PostMapping("/addUserAndInfo")
    public Result addUserAndInfo(@RequestBody UserAndInfo userAndInfo,@RequestAttribute Long userId) {
        userAndInfo.setUserId(userId);
        return userAndInfoService.addUserAndInfo(userAndInfo);
    }

    @Login
    @Operation(summary = "修改常用人信息")
    @PostMapping("/updateUserAndInfo")
    public Result updateUserAndInfo(@RequestBody UserAndInfo userAndInfo,@RequestAttribute Long userId) {
        userAndInfo.setUserId(userId);
        return userAndInfoService.updateUserAndInfo(userAndInfo);
    }

    @Login
    @Operation(summary = "删除常用人信息")
    @PostMapping("/deleteUserAndInfo")
    public Result deleteUserAndInfo(Long id) {
        return userAndInfoService.deleteUserAndInfo(id);
    }

    @Login
    @Operation(summary = "查询常用人信息")
    @GetMapping("/getUserAndInfo")
    public Result getUserAndInfo(Long id) {
        return userAndInfoService.getUserAndInfo(id);
    }

    @Login
    @Operation(summary = "查询常用人信息列表")
    @GetMapping("/getUserAndInfoList")
    public Result getUserAndInfoList(UserAndInfo userAndInfo) {
        return userAndInfoService.getUserAndInfoList(userAndInfo);
    }
}
