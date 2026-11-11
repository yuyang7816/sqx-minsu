package com.sqx.app.base.vipConfig.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.base.vipConfig.entity.VipConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * vip配置表 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
@Mapper
public interface VipConfigDao extends BaseMapper<VipConfig> {

}
