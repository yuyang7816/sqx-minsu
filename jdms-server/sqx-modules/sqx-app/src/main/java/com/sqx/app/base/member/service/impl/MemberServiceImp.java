package com.sqx.app.base.member.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.member.dao.MemberDao;
import com.sqx.app.base.member.entity.Member;
import com.sqx.app.base.member.service.MemberService;
import com.sqx.app.base.utils.Result;
import org.springframework.stereotype.Service;

/**
 * 会员特权 服务实现类
 *
 * @author wuChang
 * @since 2023-12-21
 */
@Service
public class MemberServiceImp extends ServiceImpl<MemberDao, Member> implements MemberService {


    @Override
    public Result saveMember(Member member) {
        if (member.getMemberId() != null) {
            return Result.upStatus(baseMapper.updateByMemberId(member));
        } else {
            return Result.upStatus(baseMapper.insertByMember(member));
        }
    }

    @Override
    public IPage<Member> getMemberList(Integer page, Integer limit, Member member) {
        Page<Member> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1,Integer.MAX_VALUE);
        }
        return baseMapper.selectPageByLanguageType(pages, member);
    }

    @Override
    public Result deleteByMemberId(Long memberId) {
        Member member = baseMapper.selectByMemberId(memberId);
        if (member != null) {
            baseMapper.deleteByMemberId(memberId);
            return Result.success();
        }
        return Result.error("该会员特权不存在");
    }
}
