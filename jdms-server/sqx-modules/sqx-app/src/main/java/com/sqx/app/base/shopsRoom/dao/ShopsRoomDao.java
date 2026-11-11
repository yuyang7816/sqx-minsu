package com.sqx.app.base.shopsRoom.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房间表 Mapper 接口
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@Mapper
public interface ShopsRoomDao extends BaseMapper<ShopsRoom> {

    List<Map<String, Object>> getRoomTypeList(Integer type);
}
