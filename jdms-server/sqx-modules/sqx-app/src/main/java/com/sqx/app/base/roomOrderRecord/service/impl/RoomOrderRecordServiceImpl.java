package com.sqx.app.base.roomOrderRecord.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.roomOrderRecord.dao.RoomOrderRecordDao;
import com.sqx.app.base.roomOrderRecord.entity.RoomOrderRecord;
import com.sqx.app.base.roomOrderRecord.service.IRoomOrderRecordService;
import com.sqx.app.base.roomOrderRecord.vo.OrderDateCheckVo;
import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import com.sqx.app.base.shopsRoom.service.IShopsRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 订单房间表 服务实现类
 */
@Service
public class RoomOrderRecordServiceImpl extends ServiceImpl<RoomOrderRecordDao, RoomOrderRecord> implements IRoomOrderRecordService {

    @Autowired
    private IShopsRoomService shopsRoomService;

    @Override
    public int getRoomCountByDate(Long roomId, String searchDate) {
        ShopsRoom shopsRoom = shopsRoomService.getById(roomId);
        if (shopsRoom == null) {
            return 0;
        }
        Integer occupied = baseMapper.getRoomCountByDate(roomId, searchDate);
        return occupied == null ? 0 : occupied;
    }

    @Override
    public int getResidueCountByRoomId(Long roomId, String searchDate) {
        ShopsRoom shopsRoom = shopsRoomService.getById(roomId);
        if (shopsRoom == null || shopsRoom.getIsEnable() == null || shopsRoom.getIsEnable() == 0) {
            return 0;
        }
        int orderOccupied = getRoomCountByDate(roomId, searchDate);
        return resolvePhysicalResidue(shopsRoom, orderOccupied);
    }

    /**
     * 物理房间剩余可售数：仅按当日订单占用扣减（日历库存），不因 room_status=2 锁死其它日期。
     */
    int resolvePhysicalResidue(ShopsRoom shopsRoom, int orderOccupiedCount) {
        int totalCount = resolveRoomCount(shopsRoom);
        return Math.max(totalCount - orderOccupiedCount, 0);
    }

    @Override
    public int getResidueCountByRoomType(Long shopId, Long roomTypeId, String searchDate) {
        int totalCount = getTotalRoomCountByRoomType(shopId, roomTypeId);
        int occupiedCount = baseMapper.getOccupiedCountByRoomType(shopId, roomTypeId, searchDate);
        return Math.max(totalCount - occupiedCount, 0);
    }

    private int getTotalRoomCountByRoomType(Long shopId, Long roomTypeId) {
        List<ShopsRoom> rooms = shopsRoomService.list(new LambdaQueryWrapper<ShopsRoom>()
                .eq(ShopsRoom::getShopId, shopId)
                .eq(ShopsRoom::getRoomTypeId, roomTypeId)
                .eq(ShopsRoom::getIsEnable, 1));
        return rooms.stream().mapToInt(this::resolveRoomCount).sum();
    }

    private int resolveRoomCount(ShopsRoom shopsRoom) {
        return shopsRoom.getRoomCount() != null && shopsRoom.getRoomCount() > 0 ? shopsRoom.getRoomCount() : 1;
    }

    @Override
    public OrderDateCheckVo checkDateByRoomType(LocalDate startTime, LocalDate endTime, Long shopId, Long roomTypeId, int orderRoomCount) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        OrderDateCheckVo orderDateCheckVo = new OrderDateCheckVo();
        LocalDate lastStayNight = endTime.isAfter(startTime) ? endTime.minusDays(1) : startTime;
        LocalDate currentDate = startTime;

        while (!currentDate.isAfter(lastStayNight)) {
            String feeDate = currentDate.format(df);
            int residueCount = getResidueCountByRoomType(shopId, roomTypeId, feeDate);
            if (residueCount < orderRoomCount) {
                orderDateCheckVo.setSearchDate(feeDate);
                orderDateCheckVo.setRoomCount(residueCount);
                return orderDateCheckVo;
            }
            currentDate = currentDate.plusDays(1);
        }
        return null;
    }

    private OrderDateCheckVo checkDateByRoomId(LocalDate startTime, LocalDate endTime, Long roomId, int orderRoomCount) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        OrderDateCheckVo orderDateCheckVo = new OrderDateCheckVo();
        LocalDate lastStayNight = endTime.isAfter(startTime) ? endTime.minusDays(1) : startTime;
        LocalDate currentDate = startTime;

        while (!currentDate.isAfter(lastStayNight)) {
            String feeDate = currentDate.format(df);
            int residueCount = getResidueCountByRoomId(roomId, feeDate);
            if (residueCount < orderRoomCount) {
                orderDateCheckVo.setSearchDate(feeDate);
                orderDateCheckVo.setRoomCount(residueCount);
                return orderDateCheckVo;
            }
            currentDate = currentDate.plusDays(1);
        }
        return null;
    }

    OrderDateCheckVo checkDateBySkuGroup(LocalDate startTime, LocalDate endTime,
                                         List<ShopsRoom> skuGroup, int orderRoomCount) {
        if (skuGroup == null || skuGroup.isEmpty()) {
            OrderDateCheckVo orderDateCheckVo = new OrderDateCheckVo();
            orderDateCheckVo.setRoomCount(0);
            return orderDateCheckVo;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        OrderDateCheckVo orderDateCheckVo = new OrderDateCheckVo();
        LocalDate lastStayNight = endTime.isAfter(startTime) ? endTime.minusDays(1) : startTime;
        LocalDate currentDate = startTime;

        while (!currentDate.isAfter(lastStayNight)) {
            String feeDate = currentDate.format(df);
            int residueCount = sumSkuGroupResidue(skuGroup, feeDate);
            if (residueCount < orderRoomCount) {
                orderDateCheckVo.setSearchDate(feeDate);
                orderDateCheckVo.setRoomCount(residueCount);
                return orderDateCheckVo;
            }
            currentDate = currentDate.plusDays(1);
        }
        return null;
    }

    int sumSkuGroupResidue(List<ShopsRoom> skuGroup, String searchDate) {
        return skuGroup.stream()
                .mapToInt(room -> getResidueCountByRoomId(room.getRoomId(), searchDate))
                .sum();
    }

    int resolveSkuGroupTotalCount(List<ShopsRoom> skuGroup) {
        return skuGroup.stream().mapToInt(this::resolveRoomCount).sum();
    }

    @Override
    public String validateRoomAvailability(Long shopId, Long roomTypeId, Integer orderRoomCount, LocalDate startDate, LocalDate endDate) {
        if (shopId == null) {
            return "请选择酒店";
        }
        if (roomTypeId == null) {
            return "请选择房间类型";
        }
        if (orderRoomCount == null || orderRoomCount <= 0) {
            return "请选择正确的房间数量";
        }
        if (startDate == null || endDate == null) {
            return "请选择入住和离店日期";
        }
        if (endDate.isBefore(startDate)) {
            return "离店日期不能早于入住日期";
        }
        if (startDate.isBefore(LocalDate.now())) {
            return "请选择大于当前日期的时间";
        }
        int totalCount = getTotalRoomCountByRoomType(shopId, roomTypeId);
        if (totalCount <= 0) {
            return "当前房型暂无可售房间";
        }
        if (orderRoomCount > totalCount) {
            return "预定房间数不能大于可售数量，当前可售" + totalCount + "间";
        }
        OrderDateCheckVo orderDateCheckVo = checkDateByRoomType(startDate, endDate, shopId, roomTypeId, orderRoomCount);
        if (orderDateCheckVo != null) {
            if (orderDateCheckVo.getRoomCount() <= 0) {
                return "日期" + orderDateCheckVo.getSearchDate() + "无空余房间,请重新选择日期";
            }
            return "日期" + orderDateCheckVo.getSearchDate() + "仅剩" + orderDateCheckVo.getRoomCount() + "间空余房间,请重新选择日期";
        }
        return null;
    }

    private String validateRoomAvailabilityByRoomId(Long roomId, Integer orderRoomCount, LocalDate startDate, LocalDate endDate) {
        if (roomId == null) {
            return "请选择房间";
        }
        if (orderRoomCount == null || orderRoomCount <= 0) {
            return "请选择正确的房间数量";
        }
        if (startDate == null || endDate == null) {
            return "请选择入住和离店日期";
        }
        if (endDate.isBefore(startDate)) {
            return "离店日期不能早于入住日期";
        }
        if (startDate.isBefore(LocalDate.now())) {
            return "请选择大于当前日期的时间";
        }
        ShopsRoom shopsRoom = shopsRoomService.getById(roomId);
        if (shopsRoom == null || shopsRoom.getIsEnable() == null || shopsRoom.getIsEnable() == 0) {
            return "当前房间不存在或未启用";
        }
        List<ShopsRoom> skuGroup = shopsRoomService.findSkuGroupRooms(roomId);
        if (skuGroup.isEmpty()) {
            skuGroup = new ArrayList<>();
            skuGroup.add(shopsRoom);
        }
        int totalCount = resolveSkuGroupTotalCount(skuGroup);
        if (orderRoomCount > totalCount) {
            return "预定房间数不能大于可售数量，当前可售" + totalCount + "间";
        }
        OrderDateCheckVo orderDateCheckVo = checkDateBySkuGroup(startDate, endDate, skuGroup, orderRoomCount);
        if (orderDateCheckVo != null) {
            if (orderDateCheckVo.getRoomCount() <= 0) {
                return "日期" + orderDateCheckVo.getSearchDate() + "无空余房间,请重新选择日期";
            }
            return "日期" + orderDateCheckVo.getSearchDate() + "仅剩" + orderDateCheckVo.getRoomCount() + "间空余房间,请重新选择日期";
        }
        return null;
    }

    @Override
    public String validateOrderRoomAvailability(Orders orders) {
        if (orders == null) {
            return "订单不存在";
        }
        if (orders.getRoomId() != null) {
            return validateRoomAvailabilityByRoomId(orders.getRoomId(), orders.getRoomCount(),
                    orders.getStartDate(), orders.getEndDate());
        }
        Long roomTypeId = orders.getRoomTypeId();
        return validateRoomAvailability(orders.getShopId(), roomTypeId, orders.getRoomCount(),
                orders.getStartDate(), orders.getEndDate());
    }

    @Override
    public void createOrderRoomRecords(Orders orders) {
        if (orders == null || orders.getRoomId() == null || orders.getOrderId() == null
                || orders.getRoomCount() == null || orders.getRoomCount() <= 0) {
            return;
        }
        List<ShopsRoom> skuGroup = shopsRoomService.findSkuGroupRooms(orders.getRoomId());
        if (skuGroup.isEmpty()) {
            ShopsRoom anchor = shopsRoomService.getById(orders.getRoomId());
            if (anchor != null) {
                skuGroup = new ArrayList<>();
                skuGroup.add(anchor);
            }
        }
        List<RoomAllocation> allocations = allocateAvailableRooms(
                skuGroup, orders.getStartDate(), orders.getEndDate(), orders.getRoomCount());
        int allocatedCount = allocations.stream().mapToInt(item -> item.roomCount).sum();
        if (allocatedCount < orders.getRoomCount()) {
            throw new IllegalStateException("房间库存不足，请重新选择日期或减少间数");
        }
        LocalDateTime now = LocalDateTime.now();
        for (RoomAllocation allocation : allocations) {
            RoomOrderRecord roomOrderRecord = new RoomOrderRecord();
            roomOrderRecord.setOrderId(orders.getOrderId());
            roomOrderRecord.setShopId(orders.getShopId());
            roomOrderRecord.setRoomId(allocation.roomId);
            roomOrderRecord.setRoomCount(allocation.roomCount);
            roomOrderRecord.setStartTime(orders.getStartDate());
            roomOrderRecord.setEndTime(orders.getEndDate());
            roomOrderRecord.setCreateTime(now);
            save(roomOrderRecord);
            updateRoomStatus(allocation.roomId, 2, now);
        }
    }

    /**
     * 按日期区间剩余可售数分配物理房；同一物理房可分配多间（写入一条 room_count）。
     */
    List<RoomAllocation> allocateAvailableRooms(List<ShopsRoom> skuGroup, LocalDate startDate,
                                                LocalDate endDate, int count) {
        List<RoomAllocation> allocated = new ArrayList<>();
        int remaining = count;
        for (ShopsRoom room : skuGroup) {
            if (remaining <= 0) {
                break;
            }
            if (room.getRoomId() == null) {
                continue;
            }
            int available = getMinResidueAcrossDates(room.getRoomId(), startDate, endDate);
            if (available <= 0) {
                continue;
            }
            int take = Math.min(remaining, available);
            allocated.add(new RoomAllocation(room.getRoomId(), take));
            remaining -= take;
        }
        return allocated;
    }

    int getMinResidueAcrossDates(Long roomId, LocalDate startDate, LocalDate endDate) {
        if (roomId == null || startDate == null || endDate == null) {
            return 0;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int minResidue = Integer.MAX_VALUE;
        LocalDate lastStayNight = endDate.isAfter(startDate) ? endDate.minusDays(1) : startDate;
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(lastStayNight)) {
            minResidue = Math.min(minResidue, getResidueCountByRoomId(roomId, currentDate.format(df)));
            currentDate = currentDate.plusDays(1);
        }
        return minResidue == Integer.MAX_VALUE ? 0 : minResidue;
    }

    static class RoomAllocation {
        final Long roomId;
        final int roomCount;

        RoomAllocation(Long roomId, int roomCount) {
            this.roomId = roomId;
            this.roomCount = roomCount;
        }
    }

    @Override
    public void occupyRoomForOrder(Orders orders) {
        createOrderRoomRecords(orders);
    }

    @Override
    public void releaseRoomForOrder(Orders orders) {
        if (orders == null) {
            return;
        }
        Set<Long> roomIds = new HashSet<>();
        if (orders.getOrderId() != null) {
            List<RoomOrderRecord> records = list(new LambdaQueryWrapper<RoomOrderRecord>()
                    .eq(RoomOrderRecord::getOrderId, orders.getOrderId()));
            roomIds.addAll(records.stream()
                    .map(RoomOrderRecord::getRoomId)
                    .collect(Collectors.toSet()));
        }
        if (roomIds.isEmpty() && orders.getRoomId() != null) {
            roomIds.add(orders.getRoomId());
        }
        LocalDateTime now = LocalDateTime.now();
        for (Long roomId : roomIds) {
            syncRoomStatusByRemainingRecords(roomId, orders.getOrderId(), now);
        }
    }

    /**
     * 按是否还有其它订单占用同步 room_status（仅展示用，库存以日期占用为准）。
     */
    private void syncRoomStatusByRemainingRecords(Long roomId, Long excludeOrderId, LocalDateTime updateTime) {
        if (roomId == null) {
            return;
        }
        LambdaQueryWrapper<RoomOrderRecord> wrapper = new LambdaQueryWrapper<RoomOrderRecord>()
                .eq(RoomOrderRecord::getRoomId, roomId);
        if (excludeOrderId != null) {
            wrapper.ne(RoomOrderRecord::getOrderId, excludeOrderId);
        }
        long remaining = count(wrapper);
        updateRoomStatus(roomId, remaining > 0 ? 2 : 1, updateTime);
    }

    private void updateRoomStatus(Long roomId, int roomStatus, LocalDateTime updateTime) {
        if (roomId == null) {
            return;
        }
        ShopsRoom room = new ShopsRoom();
        room.setRoomId(roomId);
        room.setRoomStatus(roomStatus);
        room.setUpdateTime(updateTime);
        shopsRoomService.updateById(room);
    }
}
