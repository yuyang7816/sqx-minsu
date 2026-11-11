package com.sqx.app.base.roomOrderRecord.service.impl;

import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoomOrderRecordServiceImplTest {

    private RoomOrderRecordServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RoomOrderRecordServiceImpl();
    }

    @Test
    void resolvePhysicalResidue_noOrderAndIdle_returnsFullCount() {
        ShopsRoom room = buildRoom(1);

        assertEquals(1, service.resolvePhysicalResidue(room, 0));
    }

    @Test
    void resolvePhysicalResidue_orderOccupied_returnsZero() {
        ShopsRoom room = buildRoom(1);

        assertEquals(0, service.resolvePhysicalResidue(room, 1));
    }

    @Test
    void resolvePhysicalResidue_roomStatusInUse_ignoresStatusAndUsesOrderOnly() {
        ShopsRoom room = buildRoom(1);
        room.setRoomStatus(2);

        assertEquals(1, service.resolvePhysicalResidue(room, 0));
    }

    @Test
    void resolvePhysicalResidue_roomStatusInUseAndOrder_returnsZeroByOrder() {
        ShopsRoom room = buildRoom(1);
        room.setRoomStatus(2);

        assertEquals(0, service.resolvePhysicalResidue(room, 1));
    }

    @Test
    void resolvePhysicalResidue_multiCountRoom_partialOrderOccupied() {
        ShopsRoom room = buildRoom(3);

        assertEquals(1, service.resolvePhysicalResidue(room, 2));
    }

    @Test
    void checkDateBySkuGroup_eightAvailable_allowsTwoRooms() {
        StubRoomOrderRecordService stubService = new StubRoomOrderRecordService();
        List<ShopsRoom> skuGroup = buildSkuGroup(8, 1);
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        assertNull(stubService.checkDateBySkuGroup(startDate, endDate, skuGroup, 2));
    }

    @Test
    void checkDateBySkuGroup_oneOccupied_allowsTwoRoomsFromEight() {
        StubRoomOrderRecordService stubService = new StubRoomOrderRecordService();
        List<ShopsRoom> skuGroup = buildSkuGroup(8, 1);
        stubService.setResidue(1L, 0);
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        assertNull(stubService.checkDateBySkuGroup(startDate, endDate, skuGroup, 2));
        assertEquals(7, stubService.sumSkuGroupResidue(skuGroup, "2026-08-03"));
    }

    @Test
    void checkDateBySkuGroup_onlyOneAvailable_rejectsTwoRooms() {
        StubRoomOrderRecordService stubService = new StubRoomOrderRecordService();
        List<ShopsRoom> skuGroup = buildSkuGroup(3, 1);
        stubService.setResidue(1L, 0);
        stubService.setResidue(2L, 0);
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        assertEquals(1, stubService.checkDateBySkuGroup(startDate, endDate, skuGroup, 2).getRoomCount());
    }

    @Test
    void allocateAvailableRooms_picksFirstAvailableRooms() {
        StubRoomOrderRecordService stubService = new StubRoomOrderRecordService();
        List<ShopsRoom> skuGroup = buildSkuGroup(5, 1);
        stubService.setUnavailable(2L);
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        List<RoomOrderRecordServiceImpl.RoomAllocation> allocated =
                stubService.allocateAvailableRooms(skuGroup, startDate, endDate, 2);

        assertEquals(2, allocated.size());
        assertEquals(Long.valueOf(1L), allocated.get(0).roomId);
        assertEquals(1, allocated.get(0).roomCount);
        assertEquals(Long.valueOf(3L), allocated.get(1).roomId);
        assertEquals(1, allocated.get(1).roomCount);
    }

    @Test
    void allocateAvailableRooms_singlePhysicalRoomWithMultiCount_takesTwoUnits() {
        StubRoomOrderRecordService stubService = new StubRoomOrderRecordService();
        List<ShopsRoom> skuGroup = buildSkuGroup(1, 2);
        stubService.setResidue(1L, 2);
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        List<RoomOrderRecordServiceImpl.RoomAllocation> allocated =
                stubService.allocateAvailableRooms(skuGroup, startDate, endDate, 2);

        assertEquals(1, allocated.size());
        assertEquals(Long.valueOf(1L), allocated.get(0).roomId);
        assertEquals(2, allocated.get(0).roomCount);
    }

    @Test
    void resolveSkuGroupTotalCount_sumsPhysicalRooms() {
        List<ShopsRoom> skuGroup = buildSkuGroup(8, 1);

        assertEquals(8, service.resolveSkuGroupTotalCount(skuGroup));
    }

    private List<ShopsRoom> buildSkuGroup(int count, int roomCountEach) {
        List<ShopsRoom> rooms = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ShopsRoom room = buildRoom(roomCountEach);
            room.setRoomId((long) i);
            rooms.add(room);
        }
        return rooms;
    }

    private ShopsRoom buildRoom(int roomCount) {
        ShopsRoom room = new ShopsRoom();
        room.setRoomCount(roomCount);
        room.setRoomStatus(1);
        room.setIsEnable(1);
        return room;
    }

    private static class StubRoomOrderRecordService extends RoomOrderRecordServiceImpl {
        private final java.util.Map<Long, Integer> residueByRoomId = new java.util.HashMap<>();

        void setResidue(Long roomId, int residue) {
            residueByRoomId.put(roomId, residue);
        }

        void setUnavailable(Long roomId) {
            setResidue(roomId, 0);
        }

        @Override
        public int getResidueCountByRoomId(Long roomId, String searchDate) {
            return residueByRoomId.getOrDefault(roomId, 1);
        }
    }
}
