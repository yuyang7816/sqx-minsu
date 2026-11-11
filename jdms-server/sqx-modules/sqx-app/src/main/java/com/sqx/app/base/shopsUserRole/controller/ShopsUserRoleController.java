package com.sqx.app.base.shopsUserRole.controller;

import com.sqx.app.base.shopsUserRole.service.IShopsUserRoleService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户酒店权限
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
@RestController
@RequestMapping("/app/shopsUserRole/")
@Tag(name = "用户酒店权限表---用户端（暂未使用）")
public class ShopsUserRoleController {
    @Autowired
    private IShopsUserRoleService userRoleService;


    @Login
    @PostMapping("/insertStaffUser")
    @Operation(summary = "关联员工")
    public Result insertStaffUser(@RequestAttribute Long userId,
                                  String phone,
                                  String msg,
                                  Long shopId,
                                  String userName) {
        return userRoleService.insertStaffUser(userId, phone, msg, shopId, userName);
    }

    @Login
    @Operation(summary = "获取酒店员工列表")
    @GetMapping("getUserList")
    public Result getUserList(@RequestParam(required = false) @Parameter(description = "页码")Integer page,
                              @RequestParam(required = false) @Parameter(description = "条数")Integer limit,
                              @RequestParam(required = false) @Parameter(description = "酒店id")Long shopId,
                              UserEntity userEntity){
        return Result.success().put("data",userRoleService.getUserList(page,limit,shopId,userEntity));
    }

    @Login
    @Operation(summary = "获取酒店id")
    @GetMapping("getShopId")
    public Result getShopId(@RequestAttribute Long userId){
        return userRoleService.getShopId(userId);
    }

    @Login
    @Operation(summary = "保存员工配置")
    @PostMapping("saveShopUser")
    public Result saveShopUser(@RequestParam(required = false) @Parameter(description = "酒店id")Long shopId,
                               @RequestParam(required = false) @Parameter(description = "用户id，多个逗号分隔")String userIds){
        return userRoleService.saveShopUser(shopId,userIds);
    }
    @Login
    @Operation(summary = "刪除酒店员工")
    @PostMapping("deleteShopUser")
    public Result deleteShopUser(@RequestParam(required = false) @Parameter(description = "酒店id")Long shopId,
                                 @RequestParam(required = false) @Parameter(description = "用户id，多个逗号分隔")String userIds){
        return userRoleService.deleteShopUser(shopId,userIds);
    }

}
