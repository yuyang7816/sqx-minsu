package com.sqx.app.base.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2023-12-21
 */
@Mapper
public interface MemberDao extends BaseMapper<Member> {

    IPage<Member> selectPageByLanguageType(@Param("pages") Page<Member> pages,
                                           @Param("member") Member member);

    Integer updateByMemberId(@Param("member") Member member);

    Integer insertByMember(@Param("member") Member member);

    Member selectByMemberId(@Param("memberId") Long memberId);

    int deleteByMemberId(@Param("memberId") Long memberId);
}
