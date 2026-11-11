package com.sqx.app.base.shopsRoom.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import com.sqx.app.base.utils.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房间表 服务类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
public interface IShopsRoomService extends IService<ShopsRoom> {

    Result saveShopRoom(ShopsRoom shopsRoom);

    IPage<ShopsRoom> getShopRoomList(Integer page, Integer limit, ShopsRoom shopsRoom);

    Result deleteRoom(String roomIds);

    ShopsRoom getShopRoomInfo(Long roomId);

    ShopsRoom getShopRoomInfoForCopy(Long roomId);

    /**
     *
     * @param type 1用户端查询 2管理端查询
     * @return
     */
    List<Map<String,Object>> getRoomTypeList(Integer type);

    /**
     * C 端售卖 SKU 指纹，相同指纹的房间在列表中聚合展示。
     */
    String buildSkuKey(ShopsRoom room);

    /**
     * 查找与指定房间同属一个售卖 SKU 的全部物理房间。
     */
    List<ShopsRoom> findSkuGroupRooms(Long roomId);
}
