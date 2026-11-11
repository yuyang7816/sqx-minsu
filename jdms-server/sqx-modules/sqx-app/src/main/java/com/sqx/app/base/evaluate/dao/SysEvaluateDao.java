package com.sqx.app.base.evaluate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.evaluate.entity.SysEvaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wuchang
 * @since 2022-11-10
 */
@Mapper
public interface SysEvaluateDao extends BaseMapper<SysEvaluate> {

    String getTotalScore(@Param("shopId") Long shopId);

    IPage<SysEvaluate> getUserEvaluateList(@Param("pages") Page<SysEvaluate> pages, @Param("evaluate") SysEvaluate evaluate);

    SysEvaluate getEvaluateInfo(@Param("evaluateId") Long evaluateId);
}
