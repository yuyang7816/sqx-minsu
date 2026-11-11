package com.sqx.app.base.shopsRoom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.roomDeviceClassify.entity.RoomDeviceClassify;
import com.sqx.app.base.roomDeviceClassify.service.IRoomDeviceClassifyService;
import com.sqx.app.base.roomOrderRecord.service.IRoomOrderRecordService;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shops.service.IShopsService;
import com.sqx.app.base.shopsRoom.dao.ShopsRoomDao;
import com.sqx.app.base.shopsRoom.dao.ShopsRoomTypeDao;
import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import com.sqx.app.base.shopsRoom.entity.ShopsRoomType;
import com.sqx.app.base.shopsRoom.service.IShopsRoomService;
import com.sqx.app.base.utils.Result;
import com.sqx.app.base.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@Service
public class ShopsRoomServiceImpl extends ServiceImpl<ShopsRoomDao, ShopsRoom> implements IShopsRoomService {
    @Autowired
    private IShopsService shopsService;
    @Autowired
    private IRoomDeviceClassifyService classifyService;
    @Autowired
    private ShopsRoomTypeDao shopsRoomTypeDao;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveShopRoom(ShopsRoom shopsRoom) {
        if (shopsRoom.getShopId() != null) {
            Shops shops = shopsService.getById(shopsRoom.getShopId());
            if (shops == null) {
                return Result.error("所选酒店不存在");
            }
            shopsRoom.setShopName(shops.getShopName());
        }

        if (shopsRoom.getRoomId() != null) {
            return updateExistingRoom(shopsRoom);
        }

        fillRoomTypeName(shopsRoom);

        if (StringUtils.isBlank(shopsRoom.getRoomName())) {
            return Result.error("请填写房间名称");
        }

        int roomCount = shopsRoom.getRoomCount() != null && shopsRoom.getRoomCount() > 0
                ? shopsRoom.getRoomCount() : 1;
        shopsRoom.setRoomCount(roomCount);

        if (existsRoomName(shopsRoom.getShopId(), shopsRoom.getRoomName(), null)) {
            return Result.error("房间名称「" + shopsRoom.getRoomName() + "」已存在，请更换");
        }

        LocalDateTime now = LocalDateTime.now();
        shopsRoom.setRoomId(null);
        shopsRoom.setCopyFromRoomId(null);
        shopsRoom.setBatchCount(null);
        shopsRoom.setAggregate(null);
        shopsRoom.setCreateTime(now);
        shopsRoom.setUpdateTime(now);
        if (shopsRoom.getRoomStatus() == null) {
            shopsRoom.setRoomStatus(1);
        }
        if (baseMapper.insert(shopsRoom) > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    private Result updateExistingRoom(ShopsRoom shopsRoom) {
        if (StringUtils.isNotBlank(shopsRoom.getRoomName())
                && existsRoomName(shopsRoom.getShopId(), shopsRoom.getRoomName(), shopsRoom.getRoomId())) {
            return Result.error("房间名称「" + shopsRoom.getRoomName() + "」已存在，请更换");
        }
        if (shopsRoom.getRoomCount() != null && shopsRoom.getRoomCount() <= 0) {
            return Result.error("房间数量至少为 1 间");
        }
        fillRoomTypeName(shopsRoom);
        shopsRoom.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(shopsRoom);
        return Result.success("修改成功");
    }

    private void fillRoomTypeName(ShopsRoom shopsRoom) {
        if (shopsRoom.getRoomTypeId() != null) {
            ShopsRoomType shopsRoomType = shopsRoomTypeDao.selectById(shopsRoom.getRoomTypeId());
            if (shopsRoomType != null) {
                shopsRoom.setRoomType(shopsRoomType.getTypeName());
            }
        }
    }

    private boolean existsRoomName(Long shopId, String roomName, Long excludeRoomId) {
        if (shopId == null || StringUtils.isBlank(roomName)) {
            return false;
        }
        LambdaQueryWrapper<ShopsRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopsRoom::getShopId, shopId);
        wrapper.eq(ShopsRoom::getRoomName, roomName);
        if (excludeRoomId != null) {
            wrapper.ne(ShopsRoom::getRoomId, excludeRoomId);
        }
        return baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public IPage<ShopsRoom> getShopRoomList(Integer page, Integer limit, ShopsRoom shopsRoom) {
        if (shopsRoom == null) {
            shopsRoom = new ShopsRoom();
        }
        LambdaQueryWrapper<ShopsRoom> wrapper = buildShopRoomQueryWrapper(shopsRoom);
        boolean hasDateFilter = shopsRoom.getStartDate() != null && shopsRoom.getEndDate() != null;

        if (hasDateFilter) {
            List<ShopsRoom> records = baseMapper.selectList(wrapper);
            enrichWithDateAvailability(records, shopsRoom.getStartDate(), shopsRoom.getEndDate());
            if (isAggregateEnabled(shopsRoom)) {
                records = aggregateRoomList(records, shopsRoom.getStartDate(), shopsRoom.getEndDate());
                records = records.stream()
                        .filter(room -> room.getResidueCount() > 0)
                        .collect(Collectors.toList());
            } else {
                records = records.stream()
                        .filter(room -> room.getResidueCount() > 0)
                        .collect(Collectors.toList());
            }
            return buildManualPage(records, page, limit);
        }

        Page<ShopsRoom> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1, 100);
        }
        IPage<ShopsRoom> shopsRoomIPage = baseMapper.selectPage(pages, wrapper);
        fillClassifyList(shopsRoomIPage.getRecords());
        if (isAggregateEnabled(shopsRoom)) {
            List<ShopsRoom> aggregated = aggregateRoomList(shopsRoomIPage.getRecords(), null, null);
            shopsRoomIPage.setRecords(aggregated);
            shopsRoomIPage.setTotal(aggregated.size());
        }
        return shopsRoomIPage;
    }

    private boolean isAggregateEnabled(ShopsRoom shopsRoom) {
        return shopsRoom.getAggregate() != null && shopsRoom.getAggregate() == 1;
    }

    List<ShopsRoom> aggregateRoomList(List<ShopsRoom> rooms, LocalDate startDate, LocalDate endDate) {
        if (rooms == null || rooms.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, List<ShopsRoom>> grouped = rooms.stream()
                .collect(Collectors.groupingBy(this::buildAggregateKey, LinkedHashMap::new, Collectors.toList()));

        List<ShopsRoom> aggregated = new ArrayList<>();
        for (List<ShopsRoom> group : grouped.values()) {
            ShopsRoom representative = group.get(0);
            int totalCount = group.stream()
                    .mapToInt(room -> room.getRoomCount() != null && room.getRoomCount() > 0 ? room.getRoomCount() : 1)
                    .sum();
            int residueCount = Math.min(
                    resolveAggregatedResidueCount(group, totalCount, startDate, endDate),
                    totalCount);

            representative.setTotalRoomCount(totalCount);
            representative.setAvailableRoomCount(totalCount);
            representative.setRoomCount(totalCount);
            representative.setResidueCount(residueCount);
            representative.setMemberMoney(resolveMinMemberMoney(group));
            aggregated.add(representative);
        }
        return aggregated;
    }

    private BigDecimal resolveMinMemberMoney(List<ShopsRoom> group) {
        return group.stream()
                .map(ShopsRoom::getMemberMoney)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(group.get(0).getMemberMoney());
    }

    private int resolveAggregatedResidueCount(List<ShopsRoom> group, int availableCount,
                                              LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return group.stream().mapToInt(ShopsRoom::getResidueCount).sum();
        }
        return group.stream().mapToInt(this::resolveResidueWithoutDate).sum();
    }

    private int resolveResidueWithoutDate(ShopsRoom room) {
        // 无日期时无法判断具体晚数占用，按可售总量展示；真实可订以带日期查询为准
        return room.getRoomCount() != null && room.getRoomCount() > 0 ? room.getRoomCount() : 1;
    }

    /**
     * C 端售卖 SKU 指纹：任一售卖相关字段不同即拆分为独立卡片。
     */
    String buildAggregateKey(ShopsRoom room) {
        return String.join("|",
                String.valueOf(room.getShopId()),
                String.valueOf(room.getRoomTypeId()),
                normalizeRoomDisplayName(room),
                normalizeMoney(room.getMoney()),
                normalizeMoney(room.getMemberMoney()),
                StringUtils.defaultString(room.getFloorArea()),
                StringUtils.defaultString(room.getFloor()),
                StringUtils.defaultString(room.getClassifyIds()),
                StringUtils.defaultString(room.getRoomTitleImg()),
                StringUtils.defaultString(room.getRoomImg()),
                normalizeTime(room.getCheckInTime()),
                normalizeTime(room.getCheckOutTime()),
                StringUtils.defaultString(room.getFoodPolicy()),
                StringUtils.defaultString(room.getPetRule()),
                StringUtils.defaultString(room.getHotelTips()));
    }

    private String normalizeTime(LocalTime time) {
        return time == null ? "" : time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String normalizeRoomDisplayName(ShopsRoom room) {
        if (StringUtils.isNotBlank(room.getRoomName())) {
            String normalized = room.getRoomName().replaceAll("[-_]?\\d+$", "");
            if (StringUtils.isNotBlank(normalized)) {
                return normalized;
            }
        }
        return StringUtils.defaultString(room.getRoomType());
    }

    private String normalizeMoney(BigDecimal money) {
        return money == null ? "0" : money.stripTrailingZeros().toPlainString();
    }

    private LambdaQueryWrapper<ShopsRoom> buildShopRoomQueryWrapper(ShopsRoom shopsRoom) {
        LambdaQueryWrapper<ShopsRoom> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(shopsRoom.getRoomTypeIds())) {
            List<String> ids = Arrays.stream(shopsRoom.getRoomTypeIds().split(","))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
            if (!ids.isEmpty()) {
                wrapper.in(ShopsRoom::getRoomTypeId, ids);
            }
        }
        wrapper.eq(shopsRoom.getShopId() != null, ShopsRoom::getShopId, shopsRoom.getShopId());
        wrapper.like(StringUtils.isNotBlank(shopsRoom.getRoomName()), ShopsRoom::getRoomName, shopsRoom.getRoomName());
        wrapper.like(StringUtils.isNotBlank(shopsRoom.getRoomType()), ShopsRoom::getRoomType, shopsRoom.getRoomType());
        wrapper.like(StringUtils.isNotBlank(shopsRoom.getShopName()), ShopsRoom::getShopName, shopsRoom.getShopName());
        wrapper.eq(shopsRoom.getIsEnable() != null, ShopsRoom::getIsEnable, shopsRoom.getIsEnable());
        wrapper.eq(shopsRoom.getRoomStatus() != null, ShopsRoom::getRoomStatus, shopsRoom.getRoomStatus());
        wrapper.ge(shopsRoom.getMinPrice() != null, ShopsRoom::getMoney, shopsRoom.getMinPrice());
        wrapper.le(shopsRoom.getMaxPrice() != null, ShopsRoom::getMoney, shopsRoom.getMaxPrice());
        wrapper.orderByAsc(ShopsRoom::getMoney).orderByAsc(ShopsRoom::getSort);
        return wrapper;
    }

    private void enrichWithDateAvailability(List<ShopsRoom> rooms, LocalDate startDate, LocalDate endDate) {
        if (rooms == null || rooms.isEmpty() || endDate.isBefore(startDate)) {
            return;
        }
        IRoomOrderRecordService roomOrderRecordService = SpringContextUtils.getBean(IRoomOrderRecordService.class);
        for (ShopsRoom room : rooms) {
            if (room.getRoomId() != null) {
                room.setResidueCount(getMinResidueCountByRoom(
                        room.getRoomId(), startDate, endDate, roomOrderRecordService));
            }
        }
    }

    private int getMinResidueCountByRoom(Long roomId, LocalDate startDate, LocalDate endDate,
                                         IRoomOrderRecordService roomOrderRecordService) {
        int minResidue = Integer.MAX_VALUE;
        LocalDate lastStayNight = endDate.isAfter(startDate) ? endDate.minusDays(1) : startDate;
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(lastStayNight)) {
            int residueCount = roomOrderRecordService.getResidueCountByRoomId(
                    roomId, currentDate.format(DATE_FORMATTER));
            minResidue = Math.min(minResidue, residueCount);
            currentDate = currentDate.plusDays(1);
        }
        return minResidue == Integer.MAX_VALUE ? 0 : minResidue;
    }

    private IPage<ShopsRoom> buildManualPage(List<ShopsRoom> records, Integer page, Integer limit) {
        int current = page != null && page > 0 ? page : 1;
        int size = limit != null && limit > 0 ? limit : 100;
        Page<ShopsRoom> result = new Page<>(current, size, records.size());
        int fromIndex = (current - 1) * size;
        if (fromIndex >= records.size()) {
            result.setRecords(Collections.emptyList());
            return result;
        }
        int toIndex = Math.min(fromIndex + size, records.size());
        List<ShopsRoom> pageRecords = new ArrayList<>(records.subList(fromIndex, toIndex));
        fillClassifyList(pageRecords);
        result.setRecords(pageRecords);
        return result;
    }

    private void fillClassifyList(List<ShopsRoom> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        Set<String> allClassifyIds = records.stream()
                .map(ShopsRoom::getClassifyIds)
                .filter(StringUtils::isNotBlank)
                .flatMap(idStr -> Arrays.stream(idStr.split(",")))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        Map<Long, RoomDeviceClassify> classifyMap = new HashMap<>();
        if (!allClassifyIds.isEmpty()) {
            List<RoomDeviceClassify> allClassifies = classifyService.list(new LambdaQueryWrapper<RoomDeviceClassify>()
                    .in(RoomDeviceClassify::getClassifyId, allClassifyIds));
            classifyMap = allClassifies.stream()
                    .collect(Collectors.toMap(RoomDeviceClassify::getClassifyId, c -> c));
        }
        for (ShopsRoom room : records) {
            String idsStr = room.getClassifyIds();
            if (StringUtils.isBlank(idsStr)) {
                continue;
            }
            Map<Long, RoomDeviceClassify> finalClassifyMap = classifyMap;
            List<RoomDeviceClassify> roomClassifies = Arrays.stream(idsStr.split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::parseLong)
                    .map(finalClassifyMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            room.setClassifyList(roomClassifies);
            room.setClassifyNames(roomClassifies.stream().map(RoomDeviceClassify::getTitle).collect(Collectors.joining("|")));
        }
    }

    @Override
    public Result deleteRoom(String roomIds) {
        return Result.upStatus(baseMapper.deleteBatchIds(Arrays.asList(roomIds.split(","))));
    }

    @Override
    public ShopsRoom getShopRoomInfo(Long roomId) {
        ShopsRoom shopsRoom = baseMapper.selectById(roomId);
        if (shopsRoom == null) {
            return null;
        }
        fillClassifyList(Collections.singletonList(shopsRoom));
        return shopsRoom;
    }

    @Override
    public ShopsRoom getShopRoomInfoForCopy(Long roomId) {
        ShopsRoom shopsRoom = getShopRoomInfo(roomId);
        if (shopsRoom == null) {
            return null;
        }
        shopsRoom.setCopySourceRoomName(shopsRoom.getRoomName());
        shopsRoom.setRoomId(null);
        shopsRoom.setRoomName(null);
        shopsRoom.setRoomStatus(1);
        shopsRoom.setCreateTime(null);
        shopsRoom.setUpdateTime(null);
        return shopsRoom;
    }

    @Override
    public List<Map<String, Object>> getRoomTypeList(Integer type) {
        return baseMapper.getRoomTypeList(type);
    }

    @Override
    public String buildSkuKey(ShopsRoom room) {
        return buildAggregateKey(room);
    }

    @Override
    public List<ShopsRoom> findSkuGroupRooms(Long roomId) {
        if (roomId == null) {
            return Collections.emptyList();
        }
        ShopsRoom anchor = getById(roomId);
        if (anchor == null || anchor.getShopId() == null) {
            return Collections.emptyList();
        }
        String skuKey = buildAggregateKey(anchor);
        return list(new LambdaQueryWrapper<ShopsRoom>()
                        .eq(ShopsRoom::getShopId, anchor.getShopId())
                        .eq(ShopsRoom::getIsEnable, 1))
                .stream()
                .filter(room -> skuKey.equals(buildAggregateKey(room)))
                .collect(Collectors.toList());
    }
}
