package com.sqx.app.base.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.app.base.message.entity.MessageInfo;
import com.sqx.app.base.message.service.MessageService;
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
@Tag(name = "消息管理---管理端")
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private MessageService messageService;


    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @Operation(summary = "查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                     @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                     @RequestParam(required = false) @Parameter(description = "用户id") Long userId, MessageInfo messageInfo) {
        messageInfo.setUserId(userId.toString());
        return Result.success().put("data", messageService.selectMessageList(page, limit, messageInfo));
    }

    @GetMapping("/selectMessageCount")
    @Operation(summary = "查询投诉待审核消息总和")
    public Result selectMessageCount(@RequestParam(required = false) @Parameter(description = "分类 4订单消息 5用户消息")Integer state) {
        if (state == 9) {
            return Result.success().put("data", messageService.count(new QueryWrapper<MessageInfo>().eq("state", state).isNull("type")));
        }
        return Result.success().put("data", messageService.count(new QueryWrapper<MessageInfo>().eq("state", state).eq("type", 0)));
    }

    @RequestMapping(value = "/selectMessageByType", method = RequestMethod.GET)
    @Operation(summary = "获取消息 type1为公告2位用户反馈 3为投诉服务 4为投诉店铺 5为用户消息 6客服消息")
    @ResponseBody
    public Result selectMessageByType(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                      @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                      MessageInfo messageInfo) {
        return Result.success().put("data", messageService.selectMessageList(page, limit, messageInfo));
    }

    @RequestMapping(value = "/selectMessageDetails", method = RequestMethod.GET)
    @Operation(summary = "获取消息详细信息")
    @ResponseBody
    public Result selectMessageDetails(@RequestParam(required = false) @Parameter(description = "消息id")Long id) {
        return Result.success().put("data", messageService.selectMessageById(id));
    }

    @RequestMapping(value = "/updateMessage", method = RequestMethod.POST)
    @Operation(summary = "修改消息")
    @ResponseBody
    public Result updateMessage(@RequestBody MessageInfo messageInfo) {
        return Result.success().put("data", messageService.update(messageInfo));
    }


    @RequestMapping(value = "/deleteMessageById", method = RequestMethod.POST)
    @Operation(summary = "删除消息")
    @ResponseBody
    public Result deleteMessageById(@RequestParam(required = false) @Parameter(description = "消息id")Long id) {
        return Result.success().put("data", messageService.delete(id));
    }

    @RequestMapping(value = "/insertMessage", method = RequestMethod.POST)
    @Operation(summary = "添加消息")
    @ResponseBody
    public Result insertMessage(MessageInfo messageInfo) {
        return Result.success().put("data", messageService.saveBody(messageInfo));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Operation(summary = "管理平台公告详情")
    @ResponseBody
    public Result getMessage(@Parameter(description = "消息id")@PathVariable Long id) {
        return Result.success().put("data", messageService.selectMessageById(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Operation(summary = "管理平台和用户端通用接口添加公告")
    @ResponseBody
    public Result addMessage(@RequestBody MessageInfo messageInfo) {
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Operation(summary = "管理平台修改公告接口")
    @ResponseBody
    public Result uUpdate(@RequestBody MessageInfo messageInfo) {
        messageService.update(messageInfo);
        return Result.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @Operation(summary = "管理平台删除公告接口")
    public Result deleteMessage(@Parameter(description = "消息id")@PathVariable Long id) {
        messageService.delete(id);
        return Result.success();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Operation(summary = "管理平台获取全部公告接口")
    @ResponseBody
    public Result getMessageList(@RequestParam(required = false) @Parameter(description = "页码") Integer page,
                                 @RequestParam(required = false) @Parameter(description = "条数") Integer limit,
                                 MessageInfo messageInfo) {
        return Result.success().put("data", messageService.selectMessageList(page, limit, messageInfo));
    }

    @RequestMapping(value = "/page/{state}/{page}/{limit}", method = RequestMethod.GET)
    @Operation(summary = "管理平台分页查询公告接口")
    @ResponseBody
    public Result getBodyPage(@RequestParam(required = false) @Parameter(description = "分类 4订单消息 5用户消息")@PathVariable Integer state,
                              @RequestParam(required = false) @Parameter(description = "页码")@PathVariable Integer page,
                              @RequestParam(required = false) @Parameter(description = "条数")@PathVariable Integer limit, MessageInfo messageInfo) {
        messageInfo.setState(state.toString());
        return Result.success().put("data", messageService.selectMessageList(page, limit, messageInfo));
    }


}