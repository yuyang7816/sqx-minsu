package com.sqx.app.base.evaluate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.evaluate.entity.SysEvaluate;
import com.sqx.app.base.utils.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wuchang
 * @since 2022-11-10
 */
public interface SysEvaluateService extends IService<SysEvaluate> {

    Result addEvaluate(SysEvaluate evaluate);

    IPage<SysEvaluate> getUserEvaluateList(Integer page, Integer limit, SysEvaluate evaluate);

    /**
     *
     * @param userId
     * @param evaluateId
     * @param auth 1管理员删除 2用户删除
     * @return
     */
    Result deleteEvaluate(Long userId, Long evaluateId,Integer auth);

    SysEvaluate getEvaluateInfo(Long evaluateId);
}
