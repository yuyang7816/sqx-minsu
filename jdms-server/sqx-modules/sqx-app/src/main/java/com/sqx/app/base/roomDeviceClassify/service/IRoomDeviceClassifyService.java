package com.sqx.app.base.roomDeviceClassify.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.roomDeviceClassify.entity.RoomDeviceClassify;
import com.sqx.app.base.utils.Result;

/**
 * <p>
 * 房间配置表 服务类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
public interface IRoomDeviceClassifyService extends IService<RoomDeviceClassify> {

    Result saveClassify(RoomDeviceClassify classify);

    IPage<RoomDeviceClassify> getClassifyList(Integer page, Integer limit, RoomDeviceClassify classify);

    Result deleteClassify(String classifyIds);
}
