package com.sqx.app.base.member.controller;

import com.sqx.app.base.member.entity.Member;
import com.sqx.app.base.member.service.MemberService;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员特权-用户端
 * @author wuChang
 * @since 2023-12-21
 */
@RestController
@Tag(name = "会员特权---用户端")
@RequestMapping("/app/member/")
public class AppMemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 获取会员特权列表
     * @param page 第几页
     * @param limit 多少条
     * @param member
     * @return
     */
    @Operation(summary = "获取会员特权列表")
    @GetMapping(value = "getMemberList")
    public Result getMemberList(Integer page, Integer limit, Member member) {
        return Result.success().put("data", memberService.getMemberList(page, limit, member));
    }

}
