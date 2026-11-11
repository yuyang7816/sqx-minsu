package com.sqx.app.base.shopsRoom.service.impl;

import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ShopsRoomServiceImplTest {

    private ShopsRoomServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ShopsRoomServiceImpl();
    }

    @Test
    void aggregateRoomList_sameSellingUnit_mergesIntoOne() {
        ShopsRoom room1 = buildRoom(1L, 10L, "豪华大床房", new BigDecimal("288"), new BigDecimal("258"), "28", "1,2", 2);
        ShopsRoom room2 = buildRoom(2L, 10L, "豪华大床房2", new BigDecimal("288"), new BigDecimal("258"), "28", "1,2", 3);
        ShopsRoom room3 = buildRoom(3L, 11L, "标准双床房", new BigDecimal("328"), new BigDecimal("298"), "30", "1,2", 1);

        List<ShopsRoom> aggregated = service.aggregateRoomList(Arrays.asList(room1, room2, room3), null, null);

        assertEquals(2, aggregated.size());
        assertEquals(5, aggregated.get(0).getTotalRoomCount());
        assertEquals(5, aggregated.get(0).getAvailableRoomCount());
        assertEquals(5, aggregated.get(0).getResidueCount());
        assertEquals(1, aggregated.get(1).getTotalRoomCount());
    }

    @Test
    void aggregateRoomList_differentPrice_keepsSeparateCards() {
        ShopsRoom room1 = buildRoom(1L, 10L, "801", new BigDecimal("288"), new BigDecimal("258"), "28", "1,2", 1);
        ShopsRoom room2 = buildRoom(2L, 10L, "802", new BigDecimal("328"), new BigDecimal("298"), "28", "1,2", 1);

        List<ShopsRoom> aggregated = service.aggregateRoomList(Arrays.asList(room1, room2), null, null);

        assertEquals(2, aggregated.size());
    }

    @Test
    void getShopRoomInfoForCopy_clearsUniqueFields() {
        ShopsRoomServiceImpl spyService = new ShopsRoomServiceImpl() {
            @Override
            public ShopsRoom getShopRoomInfo(Long roomId) {
                ShopsRoom room = new ShopsRoom();
                room.setRoomId(100L);
                room.setRoomName("801");
                room.setRoomTypeId(10L);
                room.setRoomStatus(2);
                room.setRoomCount(5);
                return room;
            }
        };

        ShopsRoom copy = spyService.getShopRoomInfoForCopy(100L);

        assertNull(copy.getRoomId());
        assertNull(copy.getRoomName());
        assertEquals("801", copy.getCopySourceRoomName());
        assertEquals(Integer.valueOf(1), copy.getRoomStatus());
    }

    @Test
    void aggregateRoomList_withPrecomputedResidue_sumsAcrossGroup() {
        ShopsRoom room1 = buildRoom(1L, 10L, "极简双床标间-1", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room2 = buildRoom(2L, 10L, "极简双床标间-2", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        room1.setResidueCount(1);
        room2.setResidueCount(1);

        List<ShopsRoom> aggregated = service.aggregateRoomList(Arrays.asList(room1, room2), null, null);

        assertEquals(1, aggregated.size());
        assertEquals(2, aggregated.get(0).getTotalRoomCount());
        assertEquals(2, aggregated.get(0).getResidueCount());
    }

    @Test
    void aggregateRoomList_withDates_sumsExistingResidueCount() {
        ShopsRoom room1 = buildRoom(1L, 10L, "极简双床标间-1", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room2 = buildRoom(2L, 10L, "极简双床标间-2", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        room1.setResidueCount(1);
        room2.setResidueCount(1);
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        List<ShopsRoom> aggregated = service.aggregateRoomList(Arrays.asList(room1, room2), startDate, endDate);

        assertEquals(1, aggregated.size());
        assertEquals(2, aggregated.get(0).getTotalRoomCount());
        assertEquals(2, aggregated.get(0).getResidueCount());
    }

    @Test
    void aggregateRoomList_withDates_oneOccupied_keepsTotalCount() {
        ShopsRoom room1 = buildRoom(1L, 10L, "极简双床标间-1", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room2 = buildRoom(2L, 10L, "极简双床标间-2", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room3 = buildRoom(3L, 10L, "极简双床标间-3", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        room1.setResidueCount(0);
        room2.setResidueCount(1);
        room3.setResidueCount(1);
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        List<ShopsRoom> aggregated = service.aggregateRoomList(
                Arrays.asList(room1, room2, room3), startDate, endDate);

        assertEquals(1, aggregated.size());
        assertEquals(3, aggregated.get(0).getTotalRoomCount());
        assertEquals(2, aggregated.get(0).getResidueCount());
    }

    @Test
    void aggregateRoomList_differentMemberMoney_splitsIntoSeparateSkus() {
        ShopsRoom room1 = buildRoom(1L, 10L, "极简双床标间-1", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room2 = buildRoom(2L, 10L, "极简双床标间-8", new BigDecimal("165"), new BigDecimal("145"), "35", "1,2", 1);
        room1.setResidueCount(1);
        room2.setResidueCount(1);
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        List<ShopsRoom> aggregated = service.aggregateRoomList(
                Arrays.asList(room1, room2), startDate, endDate);

        assertEquals(2, aggregated.size());
        assertEquals(1, aggregated.get(0).getTotalRoomCount());
        assertEquals(1, aggregated.get(0).getResidueCount());
        assertEquals(new BigDecimal("135"), aggregated.get(0).getMemberMoney());
        assertEquals(1, aggregated.get(1).getTotalRoomCount());
        assertEquals(new BigDecimal("145"), aggregated.get(1).getMemberMoney());
    }

    @Test
    void buildAggregateKey_differentMemberMoney_producesDifferentKeys() {
        ShopsRoom room1 = buildRoom(1L, 10L, "极简双床标间-1", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room2 = buildRoom(2L, 10L, "极简双床标间-2", new BigDecimal("165"), new BigDecimal("145"), "35", "1,2", 1);

        assertNotEquals(service.buildAggregateKey(room1), service.buildAggregateKey(room2));
    }

    @Test
    void buildAggregateKey_differentClassifyIds_producesDifferentKeys() {
        ShopsRoom room1 = buildRoom(1L, 10L, "极简双床标间-1", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room2 = buildRoom(2L, 10L, "极简双床标间-2", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2,3", 1);

        assertNotEquals(service.buildAggregateKey(room1), service.buildAggregateKey(room2));
    }

    @Test
    void buildAggregateKey_differentCheckInTime_producesDifferentKeys() {
        ShopsRoom room1 = buildRoom(1L, 10L, "极简双床标间-1", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room2 = buildRoom(2L, 10L, "极简双床标间-2", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        room2.setCheckInTime(LocalTime.of(15, 0));

        assertNotEquals(service.buildAggregateKey(room1), service.buildAggregateKey(room2));
    }

    @Test
    void aggregateRoomList_tenRooms_oneCustomizedAndOneOccupied_splitsAndCountsCorrectly() {
        List<ShopsRoom> rooms = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ShopsRoom room = buildRoom((long) i, 10L, "极简双床标间-" + i,
                    i == 3 ? new BigDecimal("188") : new BigDecimal("165"),
                    new BigDecimal("135"), "35", "1,2", 1);
            room.setResidueCount(i == 1 ? 0 : 1);
            rooms.add(room);
        }
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        List<ShopsRoom> aggregated = service.aggregateRoomList(rooms, startDate, endDate);

        assertEquals(2, aggregated.size());
        ShopsRoom standardGroup = aggregated.stream()
                .filter(r -> new BigDecimal("165").compareTo(r.getMoney()) == 0)
                .findFirst()
                .orElseThrow(() -> new AssertionError("standard group missing"));
        ShopsRoom customGroup = aggregated.stream()
                .filter(r -> new BigDecimal("188").compareTo(r.getMoney()) == 0)
                .findFirst()
                .orElseThrow(() -> new AssertionError("custom group missing"));

        assertEquals(9, standardGroup.getTotalRoomCount());
        assertEquals(8, standardGroup.getResidueCount());
        assertEquals(1, customGroup.getTotalRoomCount());
        assertEquals(1, customGroup.getResidueCount());
    }

    @Test
    void aggregateRoomList_nineStandard_oneInUseWithoutDate_showsFullSellableCount() {
        List<ShopsRoom> rooms = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            ShopsRoom room = buildRoom((long) i, 10L, "极简双床标间-" + i,
                    new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
            if (i == 1) {
                room.setRoomStatus(2);
            }
            rooms.add(room);
        }

        List<ShopsRoom> aggregated = service.aggregateRoomList(rooms, null, null);

        assertEquals(1, aggregated.size());
        assertEquals(9, aggregated.get(0).getTotalRoomCount());
        assertEquals(9, aggregated.get(0).getResidueCount());
    }

    @Test
    void aggregateRoomList_differentSalePrice_oneOccupied_otherGroupUnaffected() {
        ShopsRoom room1 = buildRoom(1L, 10L, "极简双床标间-1", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room2 = buildRoom(2L, 10L, "极简双床标间-2", new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
        ShopsRoom room3 = buildRoom(3L, 10L, "极简双床标间-特价", new BigDecimal("188"), new BigDecimal("158"), "35", "1,2", 1);
        room1.setResidueCount(1);
        room2.setResidueCount(1);
        room3.setResidueCount(0);
        LocalDate startDate = LocalDate.of(2026, 8, 3);
        LocalDate endDate = LocalDate.of(2026, 8, 4);

        List<ShopsRoom> aggregated = service.aggregateRoomList(
                Arrays.asList(room1, room2, room3), startDate, endDate);

        assertEquals(2, aggregated.size());
        assertEquals(2, aggregated.get(0).getTotalRoomCount());
        assertEquals(2, aggregated.get(0).getResidueCount());
        assertEquals(1, aggregated.get(1).getTotalRoomCount());
        assertEquals(0, aggregated.get(1).getResidueCount());
    }

    @Test
    void findSkuGroupRooms_sameSku_mergesByFingerprint() {
        ShopsRoomServiceImpl spyService = new ShopsRoomServiceImpl() {
            @Override
            public ShopsRoom getById(java.io.Serializable id) {
                return buildRoom((Long) id, 10L, "极简双床标间-" + id,
                        new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1);
            }

            @Override
            public List<ShopsRoom> list(com.baomidou.mybatisplus.core.conditions.Wrapper<ShopsRoom> queryWrapper) {
                List<ShopsRoom> rooms = new ArrayList<>();
                for (long i = 1; i <= 3; i++) {
                    rooms.add(buildRoom(i, 10L, "极简双床标间-" + i,
                            new BigDecimal("165"), new BigDecimal("135"), "35", "1,2", 1));
                }
                rooms.add(buildRoom(4L, 10L, "极简双床标间-特价",
                        new BigDecimal("188"), new BigDecimal("158"), "35", "1,2", 1));
                return rooms;
            }
        };

        List<ShopsRoom> group = spyService.findSkuGroupRooms(1L);

        assertEquals(3, group.size());
    }

    private ShopsRoom buildRoom(Long roomId, Long roomTypeId, String roomName, BigDecimal money,
                                BigDecimal memberMoney, String floorArea, String classifyIds, int roomCount) {
        ShopsRoom room = new ShopsRoom();
        room.setShopId(436L);
        room.setRoomId(roomId);
        room.setRoomTypeId(roomTypeId);
        room.setRoomName(roomName);
        room.setMoney(money);
        room.setMemberMoney(memberMoney);
        room.setFloorArea(floorArea);
        room.setClassifyIds(classifyIds);
        room.setRoomTitleImg("cover.jpg");
        room.setRoomImg("img1.jpg,img2.jpg");
        room.setRoomStatus(1);
        room.setRoomCount(roomCount);
        return room;
    }
}
