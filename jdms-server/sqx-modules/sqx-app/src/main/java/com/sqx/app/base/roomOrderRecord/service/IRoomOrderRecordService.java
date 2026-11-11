package com.sqx.app.base.roomOrderRecord.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.roomOrderRecord.entity.RoomOrderRecord;
import com.sqx.app.base.roomOrderRecord.vo.OrderDateCheckVo;

import java.time.LocalDate;

/**
 * <p>
 * 订单房间表 服务类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-14
 */
public interface IRoomOrderRecordService extends IService<RoomOrderRecord> {

    int getRoomCountByDate(Long roomId, String searchDate);

    /**
     * 获取指定日期某房型下的剩余可订房间数
     */
    int getResidueCountByRoomType(Long shopId, Long roomTypeId, String searchDate);

    /**
     * 获取指定日期某商品下的剩余可订房间数
     */
    int getResidueCountByRoomId(Long roomId, String searchDate);

    /**
     * @param startTime 入住时间
     * @param endTime 离店时间
     * @param shopId 酒店id
     * @param roomTypeId 房间类型id
     * @param orderRoomCount 所需房间数
     */
    OrderDateCheckVo checkDateByRoomType(LocalDate startTime, LocalDate endTime, Long shopId, Long roomTypeId, int orderRoomCount);

    /**
     * 校验房型库存及日期占用，通过返回 null，失败返回错误信息
     */
    String validateRoomAvailability(Long shopId, Long roomTypeId, Integer orderRoomCount, LocalDate startDate, LocalDate endDate);

    /**
     * 根据订单信息校验房型库存及日期占用
     */
    String validateOrderRoomAvailability(Orders orders);

    /**
     * 支付成功后按 SKU 组分配物理房间并写入占用记录。
     */
    void createOrderRoomRecords(Orders orders);

    /**
     * 支付成功后占用房间（设置为使用中）
     */
    void occupyRoomForOrder(Orders orders);

    /**
     * 订单完成或取消后释放房间（设置为空闲）
     */
    void releaseRoomForOrder(Orders orders);
}
