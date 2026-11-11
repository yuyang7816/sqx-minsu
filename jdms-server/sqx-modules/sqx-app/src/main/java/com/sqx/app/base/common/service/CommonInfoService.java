package com.sqx.app.base.common.service;


import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.utils.Result;

/**
 * @author fang
 * @date 2020/7/8
 */
public interface CommonInfoService {

    /**
     * 保存对象
     *
     * @param
     */
    Result update(CommonInfo commonInfo);

    /**
     * 获取一个对象
     */
    CommonInfo findOne(int id);

    /**
     * 删除一个
     */
    Result delete(long id);

    /**
     * 修改
     */
    Result updateBody(CommonInfo commonInfo);
    /**
     * 通过类型查询
     */
    Result findByType(Integer type);

    /**
     * 通过类型查询
     */
    Result findByTypeAndCondition(String condition);

    CommonInfo selectMinLike(String min);
}