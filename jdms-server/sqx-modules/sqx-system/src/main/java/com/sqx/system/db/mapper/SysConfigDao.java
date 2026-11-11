package com.sqx.system.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.system.db.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置信息
 *
 */
@Mapper
public interface SysConfigDao extends BaseMapper<SysConfigEntity> {

    SysConfigEntity queryByKey(String paramKey);

    /**
     * 根据key，更新value
     */
    int updateValueByKey(@Param("paramKey") String paramKey, @Param("paramValue") String paramValue);
}
