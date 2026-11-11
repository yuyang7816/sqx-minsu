package com.sqx.app.base.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.member.entity.Member;
import com.sqx.app.base.utils.Result;

/**
 * 会员特权 服务类
 * @author wuChang
 * @since 2023-12-21
 */

public interface MemberService extends IService<Member> {
    /**
     * 新增或修改会员特权
     */
    Result saveMember (Member member);

    /**
     * 获取改会员特权列表
     */
    IPage<Member> getMemberList(Integer page, Integer limit, Member member);

    /**
     * 删除会员特权
     */
    Result deleteByMemberId(Long memberId);
}
