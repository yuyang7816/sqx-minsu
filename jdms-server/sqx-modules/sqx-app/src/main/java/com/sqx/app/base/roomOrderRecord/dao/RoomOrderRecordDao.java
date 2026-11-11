package com.sqx.app.base.roomOrderRecord.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.base.roomOrderRecord.entity.RoomOrderRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单房间表 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2023-08-14
 */
@Mapper
public interface RoomOrderRecordDao extends BaseMapper<RoomOrderRecord> {

    int getRoomCountByDate(@Param("roomId") Long roomId, @Param("searchDate") String searchDate);

    int getOccupiedCountByRoomType(@Param("shopId") Long shopId, @Param("roomTypeId") Long roomTypeId, @Param("searchDate") String searchDate);

    int getOccupiedIdleCountByRoomType(@Param("shopId") Long shopId, @Param("roomTypeId") Long roomTypeId, @Param("searchDate") String searchDate);
}
