package com.sqx.app.base.shopsRoom.service;

import com.sqx.app.base.shopsRoom.entity.ShopsRoomType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.utils.Result;

import java.util.List;

/**
* @author Admin
* @description 针对表【shops_room_type(房间类型表)】的数据库操作Service
* @createDate 2026-05-13 16:40:30
*/
public interface ShopsRoomTypeService extends IService<ShopsRoomType> {

    Result addRoomType(ShopsRoomType shopsRoomType);

    Result updateRoomType(ShopsRoomType shopsRoomType);

    Result deleteRoomType(String roomTypeIds);

    Result getRoomTypeList(ShopsRoomType shopsRoomType);

    Result getRoomTypeInfo(Long roomTypeId);
}
