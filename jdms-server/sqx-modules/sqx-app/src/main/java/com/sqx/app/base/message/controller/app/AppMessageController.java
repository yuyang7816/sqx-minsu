package com.sqx.app.base.message.controller.app;

import com.sqx.app.base.message.entity.MessageInfo;
import com.sqx.app.base.message.service.MessageService;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Tag(name = "消息管理---用户端")
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;

    @Login
    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @Operation(summary = "查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                     @RequestParam(required = false) @Parameter(description = "条数")Integer limit,
                                     @RequestAttribute("userId") Long userId,
                                     MessageInfo messageInfo) {
        messageService.updateSendState(userId);
        return Result.success().put("data", messageService.selectMessageList(page, limit, messageInfo));
    }

    @Login
    @RequestMapping(value = "/selectMessageByUserIdLimit1", method = RequestMethod.GET)
    @Operation(summary = "查询用户消息")
    @ResponseBody
    public Result selectMessageByUserIdLimit1(@RequestAttribute("userId") Long userId,
                                              MessageInfo messageInfo) {
        return Result.success().put("data", messageService.selectMessageList(1, 1, messageInfo));
    }


    @Login
    @PostMapping("/insertMessage")
    @Operation(summary = "添加投诉")
    public Result insertMessage(@RequestBody MessageInfo messageInfo) {
        messageService.saveBody(messageInfo);
        return Result.success();
    }
    @Login
    @GetMapping("/getOrderMessage")
    @Operation(summary = "获取新订单通知")
    public Result getOrderMessage(@RequestAttribute("userId") Long userId){
        return messageService.getOrderMessage(userId);
    }

}