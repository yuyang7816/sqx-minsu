package com.sqx.app.base.shops.service.impl;

import com.alipay.api.domain.Shop;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.common.dao.CommonInfoDao;
import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.message.dao.MessageInfoDao;
import com.sqx.app.base.message.entity.MessageInfo;
import com.sqx.app.base.orders.dao.OrdersDao;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.roomDeviceClassify.entity.RoomDeviceClassify;
import com.sqx.app.base.roomDeviceClassify.service.IRoomDeviceClassifyService;
import com.sqx.app.base.shops.dao.ScFollowDao;
import com.sqx.app.base.shops.dao.ShopsDao;
import com.sqx.app.base.shops.dao.TbFootprintDao;
import com.sqx.app.base.shops.entity.ScFollow;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shops.entity.TbFootprint;
import com.sqx.app.base.shops.service.IShopsService;
import com.sqx.app.base.shopsRoom.dao.ShopsRoomDao;
import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import com.sqx.app.base.shopsUserRole.entity.ShopsUserRole;
import com.sqx.app.base.shopsUserRole.service.IShopsUserRoleService;
import com.sqx.app.base.tbCoupon.entity.TbCouponUser;
import com.sqx.app.base.tbCoupon.service.TbCouponUserService;
import com.sqx.app.base.user.dao.UserDao;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 酒店 服务实现类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
@Service
public class ShopsServiceImpl extends ServiceImpl<ShopsDao, Shops> implements IShopsService {
    @Autowired
    private IShopsUserRoleService shopsUserRoleService;
    @Autowired
    private TbFootprintDao tbFootprintDao;
    @Autowired
    private ScFollowDao scFollowDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private CommonInfoDao commonInfoDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private ShopsDao shopsDao;
    @Autowired
    private ShopsRoomDao shopsRoomDao;
    @Autowired
    private IRoomDeviceClassifyService roomDeviceClassifyService;
    @Autowired
    private TbCouponUserService couponUserService;

    @Override
    public Result saveShops(Shops shops) {
        if (shops.getShopId() != null) {
            Shops oldShops = baseMapper.selectById(shops.getShopId());
            if (oldShops == null) {
                return Result.error("请选择要修改的数据");
            }
            if (!oldShops.getUserId().equals(shops.getUserId())) {
                return Result.error("您没有权限修改此数据！");
            }
            shops.setUpdateTime(LocalDateTime.now());
            if (oldShops.getAuditStatus() == 1) {
                shops.setAuditStatus(1);
            } else {
                shops.setAuditStatus(3);
            }
            return Result.upStatus(baseMapper.updateById(shops));
        } else {
            Shops oldShops = baseMapper.selectOne(new LambdaQueryWrapper<Shops>().eq(Shops::getUserId, shops.getUserId()));
            if (oldShops != null) {
                return Result.error("此用户已绑定酒店，请勿重复添加！");
            }
            String shopRate = commonInfoDao.findOne(129).getValue();
            shops.setRate(new BigDecimal(shopRate));
            shops.setAllMoney(BigDecimal.ZERO);
            shops.setCashMoney(BigDecimal.ZERO);
            shops.setCashDownMoney(BigDecimal.ZERO);
            shops.setAuditStatus(3);
            shops.setCreateTime(LocalDateTime.now());
            shops.setUpdateTime(LocalDateTime.now());
            int result = baseMapper.insert(shops);
            if (result > 0) {
                userDao.update(null, new LambdaUpdateWrapper<UserEntity>()
                        .eq(UserEntity::getUserId, shops.getUserId())
                        .set(UserEntity::getIsShopRider, 3)
                        .set(UserEntity::getShopId, shops.getShopId()));
                return Result.upStatus(result);
            }
            return Result.error();
        }
    }

    @Override
    public Result deleteShopId(Long shopId) {
        shopsUserRoleService.remove(new QueryWrapper<ShopsUserRole>().eq("shop_id", shopId));
        return Result.upStatus(baseMapper.deleteById(shopId));
    }

    @Override
    public IPage<Shops> getShopList(Integer page, Integer limit, Shops shops) {
        Page<Shops> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        IPage<Shops> shopsIPage = baseMapper.selectPages(pages,shops);
        return shopsIPage;
    }

    @Override
    public IPage<Shops> getAppShopList(Integer page, Integer limit, LocalDate startTime, LocalDate endTime, Shops shops) {

        Page<Shops> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getAppShopList(pages, startTime, endTime, shops);
    }

    @Override
    public Shops getStaffShop(Long userId) {

        return baseMapper.getStaffShop(userId);


    }

    @Override
    public Result exitShop(Long userId) {
        Shops staffShop = baseMapper.getStaffShop(userId);
        if (staffShop == null) {
            return Result.error("您当前未绑定任何酒店");
        }
        shopsUserRoleService.remove(new QueryWrapper<ShopsUserRole>().eq("user_id", userId).eq("shop_id", staffShop.getShopId()));
        userDao.update(null, new LambdaUpdateWrapper<UserEntity>()
                .eq(UserEntity::getUserId, userId)
                .set(UserEntity::getIsShopRider, 2));
        return Result.success();
    }

    @Override
    public IPage<TbFootprint> getFootprintList(Integer page, Integer limit, Long userId) {
        Page<TbFootprint> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        if (userId != null) {
            Page<TbFootprint> tbFootprintPage = tbFootprintDao.selectPage(pages, new QueryWrapper<TbFootprint>()
                    .eq("user_id", userId)
                    .groupBy("shop_id"));
            List<TbFootprint> records = tbFootprintPage.getRecords();
            if (records != null && !records.isEmpty()) {
                List<Long> shopIds = records.stream()
                        .map(TbFootprint::getShopId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                if (!shopIds.isEmpty()){
                    List<Shops> shopsList = shopsDao.selectBatchIds(shopIds);
                    List<ShopsRoom> allRooms = shopsRoomDao.selectList(new LambdaQueryWrapper<ShopsRoom>()
                            .in(ShopsRoom::getShopId, shopIds)
                            .orderByAsc(ShopsRoom::getMoney));
                    Map<Long, List<ShopsRoom>> roomMap = allRooms.stream()
                            .collect(Collectors.groupingBy(ShopsRoom::getShopId));
                    for (Shops record : shopsList) {
                        List<ShopsRoom> rooms = roomMap.get(record.getShopId());
                        if (rooms != null && !rooms.isEmpty()) {
                            // 因为前面已经按 memberMoney 升序排过序了，所以第一个就是最低价
                            record.setMinMoney(rooms.get(0).getMoney());
                        }
                    }
                    Map<Long,Shops> shopsMap = shopsList.stream()
                            .collect(Collectors.toMap(Shops::getShopId,Function.identity(),(a,b)-> a));
                    for (TbFootprint record : records) {
                        if (record.getShopId() != null){
                            record.setShops(shopsMap.get(record.getShopId()));
                        }
                    }
                }
            }
            return tbFootprintPage;
        }
        return pages;
    }

    @Override
    public IPage<Shops> selectCollectShopsList(Integer page, Integer limit, Long userId) {
        Page<Shops> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1, 100);
        }
        if (userId != null) {
            List<ScFollow> scFollows = scFollowDao.selectList(new LambdaQueryWrapper<ScFollow>()
                    .eq(ScFollow::getUserId, userId)
                    .eq(ScFollow::getClassify, 1));
            if (scFollows != null && !scFollows.isEmpty()) {
                List<Long> shopIds = scFollows.stream()
                        .map(ScFollow::getById)
                        .filter(Objects::nonNull)
                        .distinct() // 加上 distinct 防止重复查询
                        .collect(Collectors.toList());
                if (!shopIds.isEmpty()) {
                    Page<Shops> shopsPage = baseMapper.selectPage(pages, new LambdaQueryWrapper<Shops>()
                            .in(Shops::getShopId, shopIds));
                    List<ShopsRoom> allRooms = shopsRoomDao.selectList(new LambdaQueryWrapper<ShopsRoom>()
                            .in(ShopsRoom::getShopId, shopIds)
                            .orderByAsc(ShopsRoom::getMoney));
                    Map<Long, List<ShopsRoom>> roomMap = allRooms.stream()
                            .collect(Collectors.groupingBy(ShopsRoom::getShopId));
                    for (Shops record : shopsPage.getRecords()) {
                        List<ShopsRoom> rooms = roomMap.get(record.getShopId());
                        if (rooms != null && !rooms.isEmpty()) {
                            // 因为前面已经按 memberMoney 升序排过序了，所以第一个就是最低价
                            record.setMinMoney(rooms.get(0).getMoney());
                        }
                    }
                    return shopsPage;
                }
            }
        }
        return pages;
    }

    @Override
    public Result auditShop(Shops shops) {
        Shops shop = baseMapper.selectById(shops.getShopId());
        if (shop == null) {
            return Result.error("酒店不存在");
        }
        UserEntity userByPhone = userDao.selectById(shop.getUserId());
        if (userByPhone == null) {
            return Result.error("用户不存在");
        }
        shop.setAuditStatus(shops.getAuditStatus());
        if (StringUtils.isNotBlank(shops.getAuditRemark())) {
            shop.setAuditRemark(shops.getAuditRemark());
        }
        int result = baseMapper.updateById(shop);
        if (result <= 0) {
            return Result.error("审核失败");
        }
        String msg = shops.getAuditStatus() == 1 ? "通过" : "未通过";
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setTitle("商家入驻审核");
        messageInfo.setContent("商家入驻审核结果: " + msg);
        messageInfo.setState(String.valueOf(5));
        messageInfo.setUserName(userByPhone.getUserName());
        messageInfo.setUserId(String.valueOf(userByPhone.getUserId()));
        messageInfo.setCreateAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        messageInfo.setIsSee("0");
        messageInfoDao.insert(messageInfo);
        return Result.success();
    }

    @Override
    public Result statisticsShop(Long userId, Long shopId) {
        Shops shops = baseMapper.selectById(shopId);
        if (shops != null) {
            if (!shops.getUserId().equals(userId)) {
                ShopsUserRole shopsUserRole = shopsUserRoleService.getOne(new LambdaQueryWrapper<ShopsUserRole>()
                        .eq(ShopsUserRole::getUserId, userId)
                        .eq(ShopsUserRole::getShopId, shops.getShopId()));
                if (shopsUserRole == null) {
                    return Result.error("您没有权限查看");
                }
            }
            return Result.success().put("data", baseMapper.statisticsShop(shopId, userId));
        }
        return Result.error("当前店铺不存在");
    }

    @Override
    public Result statisticsShop1(Long shopId) {
        Shops shops = baseMapper.selectById(shopId);
        if (shops != null) {
            return Result.success().put("data", baseMapper.statisticsShop1(shopId));
        }
        return Result.error("当前店铺不存在");
    }

    @Override
    public Result statisticsShopOrder(Long userId, Long shopId) {
        Shops shops = baseMapper.selectById(shopId);
        if (shops != null) {
            if (!shops.getUserId().equals(userId)) {
                ShopsUserRole shopsUserRole = shopsUserRoleService.getOne(new LambdaQueryWrapper<ShopsUserRole>()
                        .eq(ShopsUserRole::getUserId, userId)
                        .eq(ShopsUserRole::getShopId, shops.getShopId()));
                if (shopsUserRole == null) {
                    return Result.error("您没有权限查看");
                }
            }
            return Result.success().put("data", baseMapper.statisticsShopOrder(shopId, userId));
        }
        return Result.error("当前店铺不存在");
    }
    @Override
    public Result statisticsShopOrder1(Long shopId) {
        Shops shops = baseMapper.selectById(shopId);
        if (shops != null) {
            return Result.success().put("data", baseMapper.statisticsShopOrder1(shopId));
        }
        return Result.error("当前店铺不存在");
    }

    @Override
    public Result statisticsShopRoom(Long userId, Long shopId) {
        Shops shops = baseMapper.selectById(shopId);
        if (shops != null) {
            if (!shops.getUserId().equals(userId)) {
                ShopsUserRole shopsUserRole = shopsUserRoleService.getOne(new LambdaQueryWrapper<ShopsUserRole>()
                        .eq(ShopsUserRole::getUserId, userId)
                        .eq(ShopsUserRole::getShopId, shops.getShopId()));
                if (shopsUserRole == null) {
                    return Result.error("您没有权限查看");
                }
            }
            return Result.success().put("data", baseMapper.statisticsShopRoom(shopId, userId));
        }
        return Result.error("当前店铺不存在");
    }

    @Override
    public Result statisticsShopRoom1(Long shopId) {
        Shops shops = baseMapper.selectById(shopId);
        if (shops != null) {
            return Result.success().put("data", baseMapper.statisticsShopRoom1(shopId));
        }
        return Result.error("当前店铺不存在");
    }

    @Override
    public Result selectOrderList(Long userId, Integer page, Integer limit, Orders orders) {
        Shops shops = baseMapper.selectById(orders.getShopId());
        if (shops == null) {
            return Result.error("当前酒店不存在");
        }
        Page<Orders> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1, 100);
        }
        Page<Orders> ordersPage = ordersDao.selectPage(pages, new LambdaQueryWrapper<Orders>()
                .eq(Orders::getShopId, orders.getShopId())
                .eq(orders.getOrderStatus() != null, Orders::getOrderStatus, orders.getOrderStatus())
                .orderByDesc(Orders::getCreateTime));
        List<Long> roomIds = ordersPage.getRecords().stream()
                .map(Orders::getRoomId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (!roomIds.isEmpty()) {
            List<ShopsRoom> records = shopsRoomDao.selectList(new LambdaQueryWrapper<ShopsRoom>()
                    .in(ShopsRoom::getRoomId, roomIds));
            Map<Long, ShopsRoom> roomMap = records.stream()
                    .collect(Collectors.toMap(ShopsRoom::getRoomId, Function.identity(), (a, b) -> a));
            Set<Long> allClassifyIds = records.stream()
                    .map(ShopsRoom::getClassifyIds)
                    .filter(StringUtils::isNotBlank)
                    .flatMap(idStr -> Arrays.stream(idStr.split(",")))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
            Map<Long, RoomDeviceClassify> classifyMap = new HashMap<>();
            if (!allClassifyIds.isEmpty()) {
                List<RoomDeviceClassify> allClassifies = roomDeviceClassifyService.list(
                        new LambdaQueryWrapper<RoomDeviceClassify>().in(RoomDeviceClassify::getClassifyId, allClassifyIds));
                classifyMap = allClassifies.stream()
                        .collect(Collectors.toMap(RoomDeviceClassify::getClassifyId, c -> c, (a, b) -> a));
            }
            for (Orders order : ordersPage.getRecords()) {
                ShopsRoom room = roomMap.get(order.getRoomId());
                if (room != null && StringUtils.isNotBlank(room.getClassifyIds())) {
                    List<RoomDeviceClassify> roomDeviceClassifies = Arrays.stream(room.getClassifyIds().split(","))
                            .filter(StringUtils::isNotBlank)
                            .map(Long::parseLong)
                            .map(classifyMap::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    order.setClassifyList(roomDeviceClassifies);
                    order.setClassifyNames(roomDeviceClassifies.stream()
                            .map(RoomDeviceClassify::getTitle)
                            .collect(Collectors.joining("|")));
                }
            }
        }
        return Result.success().put("data", ordersPage);
    }

    @Override
    public Result selectTodayOrderList(Long userId, Integer page, Integer limit, Orders orders) {
        Shops shops = baseMapper.selectById(orders.getShopId());
        if (shops == null) {
            return Result.error("当前酒店不存在");
        }
        LocalDateTime startTime = LocalDate.now().atStartOfDay();
        LocalDateTime endTime = LocalDate.now().plusDays(1).atStartOfDay();
        Page<Orders> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1, 100);
        }
        Page<Orders> ordersPage = ordersDao.selectPage(pages, new LambdaQueryWrapper<Orders>()
                .eq(Orders::getShopId, orders.getShopId())
                .eq(orders.getOrderStatus() != null, Orders::getOrderStatus, orders.getOrderStatus())
                .ge(Orders::getCreateTime, startTime)
                .lt(Orders::getCreateTime, endTime)
                .orderByDesc(Orders::getCreateTime));
        List<Long> roomIds = ordersPage.getRecords().stream()
                .map(Orders::getRoomId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (!roomIds.isEmpty()) {
            List<ShopsRoom> records = shopsRoomDao.selectList(new LambdaQueryWrapper<ShopsRoom>()
                    .in(ShopsRoom::getRoomId, roomIds));
            Map<Long, ShopsRoom> roomMap = records.stream()
                    .collect(Collectors.toMap(ShopsRoom::getRoomId, Function.identity(), (a, b) -> a));
            Set<Long> allClassifyIds = records.stream()
                    .map(ShopsRoom::getClassifyIds)
                    .filter(StringUtils::isNotBlank)
                    .flatMap(idStr -> Arrays.stream(idStr.split(",")))
                    .filter(StringUtils::isNotBlank)
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
            Map<Long, RoomDeviceClassify> classifyMap = new HashMap<>();
            if (!allClassifyIds.isEmpty()) {
                List<RoomDeviceClassify> allClassifies = roomDeviceClassifyService.list(
                        new LambdaQueryWrapper<RoomDeviceClassify>().in(RoomDeviceClassify::getClassifyId, allClassifyIds));
                classifyMap = allClassifies.stream()
                        .collect(Collectors.toMap(RoomDeviceClassify::getClassifyId, c -> c, (a, b) -> a));
            }
            for (Orders order : ordersPage.getRecords()) {
                ShopsRoom room = roomMap.get(order.getRoomId());
                if (room != null && StringUtils.isNotBlank(room.getClassifyIds())) {
                    List<RoomDeviceClassify> roomDeviceClassifies = Arrays.stream(room.getClassifyIds().split(","))
                            .filter(StringUtils::isNotBlank)
                            .map(Long::parseLong)
                            .map(classifyMap::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    order.setClassifyList(roomDeviceClassifies);
                    order.setClassifyNames(roomDeviceClassifies.stream()
                            .map(RoomDeviceClassify::getTitle)
                            .collect(Collectors.joining("|")));
                }
            }
        }
        return Result.success().put("data", ordersPage);
    }

    @Override
    public Result selectOrderById(Long userId, Long orderId, Long shopId) {
        Orders orders = ordersDao.selectById(orderId);
        if (orders != null) {
            Shops shops = baseMapper.selectById(shopId);
            if (!shops.getShopId().equals(orders.getShopId())) {
                return Result.error("非法操作");
            }
            ShopsRoom shopsRoom = shopsRoomDao.selectById(orders.getRoomId());
            if (shopsRoom == null || StringUtils.isBlank(shopsRoom.getClassifyIds())) {
                orders.setClassifyList(Collections.emptyList());
                orders.setClassifyNames("");
            }
            List<String> idStrList = null;
            if (shopsRoom != null) {
                idStrList = Arrays.stream(shopsRoom.getClassifyIds().split(","))
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList());
            }
            List<RoomDeviceClassify> roomClassifies = Collections.emptyList();
            if (idStrList != null) {
                roomClassifies = idStrList.stream()
                        .map(Integer::valueOf)
                        .map(id -> roomDeviceClassifyService.getById(id)) // 根据 ID 查库
                        .filter(Objects::nonNull) // 过滤掉数据库中已删除的配置
                        .collect(Collectors.toList());
                orders.setClassifyList(roomClassifies);
                orders.setClassifyNames(roomClassifies.stream().map(RoomDeviceClassify::getTitle).collect(Collectors.joining("|")));
            }
            if (orders.getCouponId() != null) {
                TbCouponUser couponUser = couponUserService.getById(orders.getCouponId());
                if (couponUser != null) {
                    orders.setCouponName(couponUser.getCouponName());
                }
            }
            return Result.success().put("data", orders);
        }
        return Result.error("订单不存在");
    }
}
