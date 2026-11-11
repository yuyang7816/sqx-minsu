package com.sqx.app.base.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.oss.entity.SysOssEntity;
import com.sqx.common.mybatis.utils.PageUtils;

import java.util.Map;

/**
 * 文件上传
 *
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
