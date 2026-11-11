package com.sqx.app.base.orders.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.invoice.domain.entity.Invoice;
import com.sqx.app.base.invoice.mapper.InvoiceDao;
import com.sqx.app.base.orders.dao.OrdersDao;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.orders.service.IOrdersService;
import com.sqx.app.base.orders.vo.CancelOrderPreviewVo;
import com.sqx.app.base.orders.vo.OrderVo;
import com.sqx.app.base.pay.controller.app.AliPayController;
import com.sqx.app.base.pay.dao.PayDetailsDao;
import com.sqx.app.base.pay.entity.PayDetails;
import com.sqx.app.base.pay.service.WxService;
import com.sqx.app.base.roomOrderRecord.entity.RoomOrderRecord;
import com.sqx.app.base.roomOrderRecord.service.IRoomOrderRecordService;
import com.sqx.app.base.shopMoneyDetails.dao.ShopMoneyDetailsDao;
import com.sqx.app.base.shopMoneyDetails.entity.ShopMoneyDetails;
import com.sqx.app.base.shopMoneyDetails.service.IShopMoneyDetailsService;
import com.sqx.app.base.shops.dao.ShopsDao;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shops.service.IShopsService;
import com.sqx.app.base.shopsRoom.entity.ShopsRoom;
import com.sqx.app.base.shopsRoom.service.IShopsRoomService;
import com.sqx.app.base.shopsUserRole.entity.ShopsUserRole;
import com.sqx.app.base.shopsUserRole.service.IShopsUserRoleService;
import com.sqx.app.base.tbCoupon.entity.TbCouponUser;
import com.sqx.app.base.tbCoupon.service.TbCouponUserService;
import com.sqx.app.base.user.dao.UserAndInfoDao;
import com.sqx.app.base.user.entity.UserAndInfo;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.service.UserMoneyService;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.userMoneyDetails.entity.UserMoneyDetails;
import com.sqx.app.base.userMoneyDetails.service.UserMoneyDetailsService;
import com.sqx.app.base.utils.Result;
import com.sqx.app.base.utils.SpringContextUtils;
import com.sqx.common.core.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-11
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements IOrdersService {
    @Autowired
    private IShopsService shopsService;
    @Autowired
    private IShopsRoomService shopsRoomService;
    @Autowired
    private UserService userService;
    @Autowired
    private TbCouponUserService couponUserService;
    @Autowired
    private IRoomOrderRecordService orderRecordService;
    @Autowired
    private UserMoneyService userMoneyService;
    //    @Autowired
//    private WxService wxService;
//    @Autowired
//    private AliPayController aliPayController;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private IShopsUserRoleService shopsUserRoleService;
    @Autowired
    private IShopMoneyDetailsService shopMoneyDetailsService;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private UserAndInfoDao userAndInfoDao;
    @Autowired
    private ShopMoneyDetailsDao shopMoneyDetailsDao;

    @Autowired
    private ShopsDao shopsDao;

    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    @Override
    public Result addOrder(Orders orders) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            return addLockOrder(orders);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Override
    public IPage<OrderVo> getMyOrderList(Integer page, Integer limit, OrderVo orderVo) {
        Page<OrderVo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getMyOrderList(pages, orderVo);


    }

    @Override
    public OrderVo getMyOrderInfo(Long orderId) {
        OrderVo orderVo = baseMapper.getMyOrderInfo(orderId);
        if (orderVo != null) {
            if (orderVo.getShopId() != null) {
                Shops shops = shopsDao.selectById(orderVo.getShopId());
                if (shops != null) {
                    orderVo.setShop(shops);
                }
            }
            fillCouponInfo(orderVo);
        }
        return orderVo;
    }

    private void fillCouponInfo(Orders orders) {
        if (orders.getCouponId() == null) {
            return;
        }
        TbCouponUser couponUser = couponUserService.getById(orders.getCouponId());
        if (couponUser != null) {
            orders.setCouponName(couponUser.getCouponName());
            if (orders.getCouponMoney() == null && couponUser.getMoney() != null) {
                orders.setCouponMoney(couponUser.getMoney());
            }
        }
    }

    /**
     * 用户取消订单前预览扣费信息（与 cancelLockOrder 扣费规则保持一致）
     */
    @Override
    public Result cancelOrderPreview(Long userId, Long orderId) {
        Orders orders = baseMapper.selectById(orderId);
        if (orders == null) {
            return Result.error("订单不存在");
        }
        if (orders.getOrderStatus() != 1) {
            return Result.error("当前状态无法取消");
        }
        if (!userId.equals(orders.getUserId())) {
            return Result.error("您无权操作当前订单");
        }

        CancelOrderPreviewVo previewVo = new CancelOrderPreviewVo();
        BigDecimal payMoney = orders.getPayMoney() == null ? BigDecimal.ZERO : orders.getPayMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
        previewVo.setPayMoney(payMoney);

        // 未支付：不扣服务费
        if (orders.getIsPay() == null || orders.getIsPay() != 1) {
            previewVo.setNeedDeduct(false);
            previewVo.setServiceFee(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
            previewVo.setRefundMoney(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
            previewVo.setTip("取消不会扣除服务费");
            return Result.success().put("data", previewVo);
        }

        if (!LocalDate.now().isBefore(orders.getStartDate())) {
            return Result.error("请提前至少一天时间取消订单");
        }

        BigDecimal refundRate = new BigDecimal(commonInfoService.findOne(417).getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal serviceFee;
        BigDecimal refundMoney;
        if (refundRate.compareTo(BigDecimal.ZERO) <= 0) {
            serviceFee = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
            refundMoney = payMoney;
        } else {
            serviceFee = payMoney.multiply(refundRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            refundMoney = payMoney.subtract(serviceFee).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        previewVo.setServiceFee(serviceFee);
        previewVo.setRefundMoney(refundMoney);
        if (serviceFee.compareTo(BigDecimal.ZERO) > 0) {
            previewVo.setNeedDeduct(true);
            previewVo.setTip("取消会扣除服务费" + serviceFee + "元，是否取消？");
        } else {
            previewVo.setNeedDeduct(false);
            previewVo.setTip("取消不会扣除服务费");
        }
        return Result.success().put("data", previewVo);
    }

    /**
     * @param userId
     * @param orderId
     * @param refundUser  1用户端取消订单 2员工端取消订单 3管理端取消订单
     * @param refundMoney
     * @return
     */
    @Override
    public Result cancelOrder(Long userId, Long orderId, Integer refundUser, BigDecimal refundMoney) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            return cancelLockOrder(userId, orderId, refundUser, refundMoney);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    /**
     * 判断员工是否有权操作指定订单的权限
     *
     * @param orderId 订单id
     * @param userId  员工id
     * @return true 有 false 没有
     */
    private boolean isHasOperateOrder(Long orderId, Long userId) {
        Orders orders = baseMapper.selectById(orderId);
        long count = shopsUserRoleService.count(new QueryWrapper<ShopsUserRole>()
                .eq("user_id", userId)
                .eq("shop_id", orders.getShopId()));
        Shops shop = shopsService.getById(orders.getShopId());
        if (count <= 0 && shop == null) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public Result cancelLockOrder(Long userId, Long orderId, Integer refundUser, BigDecimal refundMoney) {
        Orders orders = baseMapper.selectById(orderId);
        if (orders == null) {
            return Result.error("订单不存在");
        }
        //用户取消
        if (refundUser == 1) {
            if (orders.getOrderStatus() != 0 && orders.getOrderStatus() != 1) {
                return Result.error("当前状态无法取消");
            }
            if (!userId.equals(orders.getUserId())) {
                return Result.error("您无权操作当前订单");
            }
            //员工取消
        } else if (refundUser == 2) {
            if (!isHasOperateOrder(orderId, userId)) {
                return Result.error("您没有权限操作当前酒店的订单");
            }
        }
        if (orders.getOrderStatus() == 4) {
            return Result.error("订单状态已变更,请刷新后重试");
        }
        boolean refund = false;
        //是否已支付订单
        if (orders.getIsPay() == 1) {
            if (!LocalDate.now().isBefore(orders.getStartDate())) {
                return Result.error("请提前至少一天时间取消订单");
            } else {
                BigDecimal refundRate = new BigDecimal(commonInfoService.findOne(417).getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
                //退款金额判断
                if (refundMoney == null || refundMoney.compareTo(BigDecimal.ZERO) == 0) {
                    //不填写退款金额表示全退
                    refundMoney = orders.getPayMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
                    //全退则取消订单
                    orders.setOrderStatus(4);
                } else {
                    if (refundMoney.compareTo(orders.getPayMoney()) > 0) {
                        return Result.error("退款金额不能大于付款金额");
                    }
                    refundMoney = orders.getPayMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                // 扣除手续费
                BigDecimal rateMoney = BigDecimal.ZERO;
                // 扣除金额
                BigDecimal deductMoney = BigDecimal.ZERO;
                if (refundRate.compareTo(BigDecimal.ZERO) <= 0) {
                    // 真实退款
                     deductMoney = orders.getPayMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
                } else {
                    //退款手续费
                    rateMoney = refundMoney.multiply(refundRate);
                    deductMoney = refundMoney.subtract(rateMoney);
                }
                    orders.setRefundMoney(deductMoney);
                    //重新计算酒店和平台收入
//                    BigDecimal newPayMoney = orders.getPayMoney().subtract(orders.getRefundMoney());
//                    //上级佣金收入
//                    updateOrderRateMoney(orders, orders.getUserId(), newPayMoney);

                    Shops shops = shopsService.getById(orders.getShopId());
                    //酒店收入
                    BigDecimal shopMoney = rateMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
//                    //平台收入
//                    BigDecimal platformMoney = newPayMoney.subtract(shopMoney).subtract(orders.getUserScaleMoney()).subtract(orders.getUserTwoMoney());
//                    orders.setPlatformMoney(platformMoney);
                    orders.setShopMoney(shopMoney);
                    shops.setAllMoney(shops.getAllMoney().add(shopMoney));
                    shops.setCashMoney(shops.getCashMoney().add(shopMoney));
                PayDetails details = payDetailsDao.selectOne(new QueryWrapper<PayDetails>().eq("order_id", orders.getOrderNumber()));
                if (orders.getPayWay() == 0) {
                    //余额
                    userMoneyService.updateMoney(1, orders.getUserId(), deductMoney);
                    refund = true;
                } else if (orders.getPayWay() == 1) {
                    //微信
                    WxService wxService = SpringContextUtils.getBean(WxService.class);
                    refund = wxService.refund(details, deductMoney);

                } else if (orders.getPayWay() == 2) {
                    //支付宝
                    AliPayController aliPayController = SpringContextUtils.getBean(AliPayController.class);
                    refund = aliPayController.alipayRefund(orders.getOrderNumber(), deductMoney);
                }
                if (!refund) {
                    return Result.error("退款失败，请联系客服处理！");
                }
                shopsDao.updateById(shops);
                if (rateMoney.compareTo(BigDecimal.ZERO) > 0){
                    ShopMoneyDetails shopMoneyDetails = new ShopMoneyDetails();
                    shopMoneyDetails.setClassify(1);
                    shopMoneyDetails.setType(1);
                    shopMoneyDetails.setShopId(shops.getShopId());
                    shopMoneyDetails.setTitle("用户取消订单，扣除用户手续费");
                    shopMoneyDetails.setContent("扣除用户手续费："+ rateMoney);
                    shopMoneyDetails.setMoney(rateMoney);
                    shopMoneyDetails.setCreateTime(LocalDateTime.now());
                    shopMoneyDetailsDao.insert(shopMoneyDetails);
                }
                orders.setRefundMoney(refundMoney);
                //释放并删除订单房间占用
                orderRecordService.releaseRoomForOrder(orders);
                orderRecordService.remove(new QueryWrapper<RoomOrderRecord>().eq("order_id", orderId));

                baseMapper.updateById(orders);

                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setUserId(orders.getUserId());
                userMoneyDetails.setTitle("订单退款,订单号:" + orders.getOrderNumber());
                userMoneyDetails.setContent("订单退款：" + orders.getPayMoney() + "元,服务费扣款:" + orders.getPayMoney().multiply(refundRate).setScale(2, BigDecimal.ROUND_HALF_UP) + "元," +
                        "原路退回" + deductMoney + "元");
                userMoneyDetails.setType(1);
                userMoneyDetails.setClassify(6);
                userMoneyDetails.setMoney(deductMoney);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyDetails.setCreateTime(sdf.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
                restoreOrderCoupon(orders.getCouponId());
                return Result.success();
            }
        } else {
            orders.setOrderStatus(4);
            restoreOrderCoupon(orders.getCouponId());
            return Result.upStatus(baseMapper.updateById(orders));
        }
    }

    @Override
    public void restoreOrderCoupon(Long couponUserId) {
        if (couponUserId == null) {
            return;
        }
        TbCouponUser couponUser = couponUserService.getById(couponUserId);
        if (couponUser == null || couponUser.getStatus() == null || couponUser.getStatus() != 1) {
            return;
        }
        if (couponUser.getExpirationTime() != null && couponUser.getExpirationTime().before(new Date())) {
            couponUser.setStatus(2);
        } else {
            couponUser.setStatus(0);
        }
        couponUser.setEmployTime(null);
        couponUserService.updateById(couponUser);
    }

    @Transactional
    public Result addLockOrder(Orders orders) {
        UserEntity userEntity = userService.getById(orders.getUserId());
        orders.setIsEvaluate(0);
        orders.setUserName(userEntity.getUserName());
        orders.setUserPhone(userEntity.getPhone());
        orders.setCreateTime(LocalDateTime.now());
        orders.setOrderStatus(0);
        orders.setOrderNumber(generateOrder());
        orders.setUserScaleMoney(BigDecimal.ZERO);
        orders.setUserTwoMoney(BigDecimal.ZERO);
        orders.setShopMoney(BigDecimal.ZERO);
        orders.setPlatformMoney(BigDecimal.ZERO);
        orders.setIsPay(0);

        Shops shops = shopsService.getById(orders.getShopId());
        ShopsRoom shopsRoom = shopsRoomService.getById(orders.getRoomId());
        if (shopsRoom == null || shopsRoom.getIsEnable() == 0) {
            return Result.error("当前房间不存在或未启用");
        }
        Long roomTypeId = orders.getRoomTypeId() != null ? orders.getRoomTypeId() : shopsRoom.getRoomTypeId();
        if (orders.getRoomTypeId() != null && !orders.getRoomTypeId().equals(shopsRoom.getRoomTypeId())) {
            return Result.error("房间与房型不匹配");
        }
        orders.setRoomTypeId(roomTypeId);
        orders.setRoomName(shopsRoom.getRoomName());
        if (StringUtils.isNotBlank(shopsRoom.getHotelTips())) {
            orders.setWarmReminder(shopsRoom.getHotelTips());
        }
        if (orders.getUserInfoId() != null) {
            UserAndInfo userAndInfo = userAndInfoDao.selectById(orders.getUserInfoId());
            if (userAndInfo != null) {
                if (StringUtils.isNotBlank(userAndInfo.getInfoName())) {
                    orders.setRealName(userAndInfo.getInfoName());
                }
                if (StringUtils.isNotBlank(userAndInfo.getInfoPhone())) {
                    orders.setPhone(userAndInfo.getInfoPhone());
                }
            }
        }
        //算出共多少相差多少天
        long days = ChronoUnit.DAYS.between(orders.getStartDate(), orders.getEndDate());
        BigDecimal orderMoney;
        //如果下单人是会员
        if (userEntity.getIsVip() == 1) {
            orders.setIsVipOrder(1);
            //订单总金额
            orderMoney = shopsRoom.getMemberMoney().multiply(new BigDecimal(orders.getRoomCount()).multiply(new BigDecimal(days)));
        } else {
            orders.setIsVipOrder(0);
            orderMoney = shopsRoom.getMoney().multiply(new BigDecimal(orders.getRoomCount()).multiply(new BigDecimal(days)));
        }
        orders.setOrderMoney(orderMoney);
        if (shops.getIsEnable() == 0) {
            return Result.error("酒店已下架,请选择其他酒店");
        } else {
            orders.setShopName(shops.getShopName());
            orders.setOrderType(shops.getShopType());
            orders.setCoverImage(shops.getTitleImg());
        }
        String roomValidateError = orderRecordService.validateOrderRoomAvailability(orders);
        if (roomValidateError != null) {
            return Result.error(roomValidateError);
        }
        if (orders.getCouponId() != null) {
            TbCouponUser tbCouponUser = couponUserService.getById(orders.getCouponId());
            if (tbCouponUser == null) {
                return Result.error("未持有当前优惠券");
            }
            if (tbCouponUser.getStatus() == 1) {
                return Result.error("当前优惠券已使用");
            }
            if (tbCouponUser.getStatus() == 2) {
                return Result.error("当前优惠券已失效");
            }
            //如果订单金额大于优惠券最小订单金额
            if (orders.getOrderMoney().compareTo(tbCouponUser.getMinMoney()) >= 0) {
                orders.setCouponMoney(tbCouponUser.getMoney());
                tbCouponUser.setStatus(1);
                orders.setCouponMoney(tbCouponUser.getMoney());
                couponUserService.updateById(tbCouponUser);
            } else {
                return Result.error("订单金额不满足最低满减金额");
            }
        } else {
            orders.setCouponMoney(BigDecimal.ZERO);
        }
        orders.setPayMoney(orders.getOrderMoney().subtract(orders.getCouponMoney()));
        int result = baseMapper.insert(orders);
        if (result > 0) {
            if (orders.getCheckInvoice() == 1) {
                Invoice invoice = new Invoice();
                invoice.setUserId(orders.getUserId());
                if (StringUtils.isNotBlank(orders.getTitle())) {
                    invoice.setTitle(orders.getTitle());
                }
                if (StringUtils.isNotBlank(orders.getContent())) {
                    invoice.setContent(orders.getContent());
                }
                invoice.setAmount(orders.getPayMoney());
                if (orders.getType() != null) {
                    invoice.setType(orders.getType());
                }
                invoice.setStatus(1);
                invoice.setOrderId(orders.getOrderId());
                if (StringUtils.isNotBlank(orders.getCreditCode())) {
                    invoice.setCreditCode(orders.getCreditCode());
                }
                if (StringUtils.isNotBlank(orders.getAddress())) {
                    invoice.setAddress(orders.getAddress());
                }
                if (StringUtils.isNotBlank(orders.getUserPhone())) {
                    invoice.setPhone(orders.getUserPhone());
                }
                if (StringUtils.isNotBlank(orders.getOpenBank())) {
                    invoice.setOpenBank(orders.getOpenBank());
                }
                if (StringUtils.isNotBlank(orders.getBankNumber())) {
                    invoice.setBankNumber(orders.getBankNumber());
                }
                if (StringUtils.isNotBlank(orders.getReceiveEmail())) {
                    invoice.setReceiveEmail(orders.getReceiveEmail());
                }
                if (StringUtils.isNotBlank(orders.getReceivePhone())) {
                    invoice.setReceivePhone(orders.getReceivePhone());
                }
                invoiceDao.insert(invoice);
            }
            return Result.success().put("data", orders);
        }
        return Result.error();
    }

    /**
     * 添加上级佣金比例
     * 此函数不对数据库做修改,需调用时主动发送修改sql
     *
     * @param orders 订单
     * @param userId 下单用户id
     */
    public void updateOrderRateMoney(Orders orders, Long userId, BigDecimal payMoney) {
        orders.setUserScaleMoney(BigDecimal.ZERO);
        orders.setUserTwoMoney(BigDecimal.ZERO);
        orders.setUserScaleUserId(null);
        orders.setUserTwoUserId(null);
        if (payMoney == null || payMoney.compareTo(BigDecimal.ZERO) <= 0) {
            payMoney = orders.getPayMoney().subtract(orders.getShopMoney());
        }
        if (payMoney == null || payMoney.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        UserEntity userEntity = userService.getById(userId);
        if (userEntity == null || StringUtils.isEmpty(userEntity.getInviterCode())) {
            return;
        }
        //计算用户上级收益。
        UserEntity parentBuyerUser = userService.getOne(new QueryWrapper<UserEntity>().eq("invitation_code", userEntity.getInviterCode()));
        if (parentBuyerUser != null) {
            if (StringUtils.isNotEmpty(parentBuyerUser.getZhiRate())) {
                BigDecimal userScaleMoney = payMoney.multiply(new BigDecimal(parentBuyerUser.getZhiRate()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                orders.setUserScaleMoney(userScaleMoney);
                orders.setUserScaleUserId(parentBuyerUser.getUserId());
            }
            //上上级收益
            UserEntity parentParentBuyerUser = userService.getOne(new QueryWrapper<UserEntity>().eq("invitation_code", parentBuyerUser.getInviterCode()));
            if (parentParentBuyerUser != null) {
                if (StringUtils.isNotEmpty(parentParentBuyerUser.getFeiRate())) {
                    BigDecimal userScaleTwoMoney = (payMoney.subtract(orders.getUserScaleMoney())).multiply(new BigDecimal(parentParentBuyerUser.getFeiRate()))
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                    orders.setUserTwoMoney(userScaleTwoMoney);
                    orders.setUserTwoUserId(parentParentBuyerUser.getUserId());
                }
            }
        }
    }

    @Override
    public Result finishOrder(Long orderId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            return finishLockOrder(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单完成异常" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("核销订单失败");

    }

    /**
     * @param orderId 订单id
     * @return
     */
    @Transactional
    public Result finishLockOrder(Long orderId) {
        //修改订单状态
        Orders orders = baseMapper.selectById(orderId);
        if (orders.getOrderStatus() == 4) {
            return Result.error("订单已取消");
        }
        orders.setOrderStatus(3);
        orders.setVerificationTime(LocalDateTime.now());
        baseMapper.updateById(orders);
        Shops shops = shopsService.getById(orders.getShopId());
        //修改店铺金额
        Shops newShop = new Shops();
        newShop.setShopId(shops.getShopId());
        newShop.setAllMoney(shops.getAllMoney().add(orders.getShopMoney()));
        newShop.setCashMoney(shops.getCashMoney().add(orders.getShopMoney()));
        shopsService.updateById(newShop);
        //增加店铺钱包明细
        ShopMoneyDetails shopMoneyDetails = new ShopMoneyDetails();
        shopMoneyDetails.setUserId(orders.getVerificationUserId());
        shopMoneyDetails.setShopId(shops.getShopId());
        shopMoneyDetails.setMoney(orders.getShopMoney());
        shopMoneyDetails.setTitle("订单号:" + orders.getOrderNumber() + "已完成");
        shopMoneyDetails.setContent("订单:" + orders.getOrderNumber() + "已完成,用户支付" + orders.getPayMoney() + "元," + "平台手续费" + orders.getPlatformMoney() + "元,入账" + orders.getShopMoney() + "元");
        shopMoneyDetails.setType(1);
        shopMoneyDetails.setClassify(1);
        shopMoneyDetails.setCreateTime(LocalDateTime.now());
        shopMoneyDetailsService.save(shopMoneyDetails);
        //将上级分成金额添加到钱包账户
        setCommission(orderId, 1);
        orderRecordService.releaseRoomForOrder(orders);
        orderRecordService.remove(new QueryWrapper<RoomOrderRecord>().eq("order_id", orderId));
        return Result.success();

    }

    @Override
    public void setCommission(Long orderId, Integer classify) {
        Orders orders = baseMapper.selectById(orderId);
        UserEntity userParent = userService.getOne(new QueryWrapper<UserEntity>().eq("user_id", orders.getUserScaleUserId()));
        if (userParent != null) {
            this.setIndentMoneyDetails(userParent, orders, orders.getUserScaleMoney(), 1, classify);
            UserEntity userParentParent = userService.getOne(new QueryWrapper<UserEntity>().eq("user_id", orders.getUserTwoUserId()));
            if (userParentParent != null) {
                this.setIndentMoneyDetails(userParentParent, orders, orders.getUserTwoMoney(), 2, classify);
            }
        }
    }

    /**
     * @param userEntity 用户id
     * @param orders     订单
     * @param type       1一级用户 2二级用户
     * @param classify   1订单分成
     */
    @Transactional
    public void setIndentMoneyDetails(UserEntity userEntity, Orders orders, BigDecimal money, Integer type, Integer classify) {
        String title;
        if (classify == 1) {
            title = "分成";
        } else {
            title = "退款";
        }
        userService.updateMoney(1, userEntity.getUserId(), money);
        //添加用户邀请人钱包详情
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userEntity.getUserId());
        userMoneyDetails.setUserType(1);
        if (type == 1) {
            userMoneyDetails.setTitle("一级用户订单" + title);
            userMoneyDetails.setMoney(orders.getUserScaleMoney());
            userMoneyDetails.setContent("订单号:" + orders.getOrderNumber() + ",订单总价:" + orders.getOrderMoney() + "元," + title + "金额:" + orders.getUserScaleMoney());
            userMoneyDetails.setUserLevel(1);
            userMoneyDetails.setRelationId(orders.getUserId());
        } else if (type == 2) {
            userMoneyDetails.setTitle("二级用户订单" + title);
            userMoneyDetails.setMoney(orders.getUserTwoMoney());
            userMoneyDetails.setContent("订单号:" + orders.getOrderNumber() + ",订单总价:" + orders.getOrderMoney() + "元," + title + "金额:" + orders.getUserTwoMoney());
            userMoneyDetails.setRelationId(orders.getUserId());
            userMoneyDetails.setUserLevel(2);
        }
        userMoneyDetails.setType(1);
        userMoneyDetails.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userMoneyDetails.setClassify(3);
        //app端用户消息推送
        userService.pushToSingle("订单分成", "订单号:" + orders.getOrderNumber() + ",订单总价:" + orders.getOrderMoney() + "," + title + "金额:" + orders.getUserScaleMoney(), userEntity.getClientidId());
        userMoneyDetailsService.save(userMoneyDetails);
    }

    @Override
    public Result orderCallback(Long orderId, Integer classify, BigDecimal payMoney) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            return orderCallbackLock(orderId, classify, payMoney);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Transactional
    public Result orderCallbackLock(Long orderId, Integer classify, BigDecimal payMoney) {
        Orders orders = baseMapper.selectById(orderId);
        if (orders == null) {
            return Result.error("订单不存在");
        }
        if (orders.getIsPay() == 1) {
            return Result.success("支付成功");
        }
        if (orders.getOrderStatus() != 0) {
            return Result.error("订单状态已变更,请刷新后重试");
        }
        if (orders.getRoomTypeId() == null && orders.getRoomId() != null) {
            ShopsRoom shopsRoom = shopsRoomService.getById(orders.getRoomId());
            if (shopsRoom != null) {
                orders.setRoomTypeId(shopsRoom.getRoomTypeId());
            }
        }
        String roomValidateError = orderRecordService.validateOrderRoomAvailability(orders);
        if (roomValidateError != null) {
            return Result.error(roomValidateError);
        }
        orders.setPayTime(LocalDateTime.now());
        orders.setOrderStatus(1);
        //酒店收入
        Shops shops = shopsService.getById(orders.getShopId());
        BigDecimal shopMoney = orders.getPayMoney().multiply(shops.getRate()).setScale(2, BigDecimal.ROUND_HALF_UP);
        orders.setShopMoney(shopMoney);
        //写入下单人上级id及上级分成金额
        updateOrderRateMoney(orders, orders.getUserId(), null);
        if (classify == 0) {
            orders.setPayWay(0);
        } else if (classify == 1 || classify == 2 || classify == 3) {
            orders.setPayWay(1);
        } else if (classify == 4 || classify == 5) {
            orders.setPayWay(2);
        }
        orders.setIsPay(1);
        //平台收入
        BigDecimal userScaleMoney = orders.getUserScaleMoney() == null ? BigDecimal.ZERO : orders.getUserScaleMoney();
        BigDecimal userTwoMoney = orders.getUserTwoMoney() == null ? BigDecimal.ZERO : orders.getUserTwoMoney();
        BigDecimal platformMoney = orders.getPayMoney()
                .subtract(shopMoney)
                .subtract(userScaleMoney)
                .subtract(userTwoMoney)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        orders.setPlatformMoney(platformMoney);
        orders.setShopMoney(shopMoney);
        baseMapper.updateById(orders);

        // 按 SKU 组分配物理房间并写入占用记录
        orderRecordService.createOrderRoomRecords(orders);
        //用户钱包明细
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setMoney(orders.getPayMoney());
        userMoneyDetails.setUserId(orders.getUserId());
        userMoneyDetails.setType(2);
        userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetails.setClassify(2);
        userMoneyDetails.setUserType(1);
        String title = "";
        if (classify == 0) {
            title = "余额支付";
        } else if (classify == 1 || classify == 2 || classify == 3) {
            title = "微信支付";
        } else if (classify == 4 || classify == 5) {
            title = "支付宝支付";
        }
        userMoneyDetails.setTitle(title);
        userMoneyDetails.setContent("支付订单号" + orders.getOrderNumber() + "共" + orders.getPayMoney() + "元");
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success("支付成功");
    }

    @Override
    public Result cancelVerification(Long userId, String orderNumber) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            return cancelLockVerification(userId, orderNumber);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单核销异常" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("订单核销失败");
    }

    @Override
    public Result updateRemarks(Long userId, Long orderId, String remarks) {
        Orders orders = baseMapper.selectById(orderId);
        if (!userId.equals(orders.getUserId())) {
            return Result.error("您无权操作当前订单");
        } else {
            orders.setRemarks(remarks);
            return Result.upStatus(baseMapper.updateById(orders));
        }

    }

    @Override
    public IPage<OrderVo> getOrderList(Integer page, Integer limit, OrderVo orderVo) {
        Page<OrderVo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getOrderList(pages, limit, orderVo);


    }

    @Override
    public Result staffFinishOrder(Long userId, Long orderId) {
        if (isHasOperateOrder(orderId, userId)) {
            return finishOrder(orderId);
        } else {
            return Result.error("您无权操作当前订单");
        }

    }

    @Override
    public HashMap<String, Object> selectOrdersDate(Integer flag, String time) {
        HashMap<String, Object> hashMap = new HashMap<>();
//        订单状态 0待支付 1待核销 2进行中 3已完成 4已取消
        Integer sumAllCount = baseMapper.selectOrdersCount(flag, time, null);
        Integer sumDzfCount = baseMapper.selectOrdersCount(flag, time, 0);
        Integer sumDhxCount = baseMapper.selectOrdersCount(flag, time, 1);
        Integer sumJxzCount = baseMapper.selectOrdersCount(flag, time, 2);
        Integer sumYwcCount = baseMapper.selectOrdersCount(flag, time, 3);
        Integer sumYqxCount = baseMapper.selectOrdersCount(flag, time, 4);

        hashMap.put("sumAllCount", sumAllCount);
        hashMap.put("sumDzfCount", sumDzfCount);
        hashMap.put("sumDhxCount", sumDhxCount);
        hashMap.put("sumJxzCount", sumJxzCount);
        hashMap.put("sumYwcCount", sumYwcCount);
        hashMap.put("sumYqxCount", sumYqxCount);
        //金额统计
        // 全部金额
        BigDecimal sumAllMoney = baseMapper.selectOrdersMoney(flag, time, null);
        // 待支付金额
        BigDecimal sumDzfMoney = baseMapper.selectOrdersMoney(flag, time, 0);
        // 待核销金额
        BigDecimal sumDhxMoney = baseMapper.selectOrdersMoney(flag, time, 1);
        // 进行中金额
        BigDecimal sumJxzMoney = baseMapper.selectOrdersMoney(flag, time, 2);
        // 已完成金额
        BigDecimal sumYwcMoney = baseMapper.selectOrdersMoney(flag, time, 3);
        // 已取消金额
        BigDecimal sumYqxMoney = baseMapper.selectOrdersMoney1(flag, time, 4);

        hashMap.put("sumAllMoney", sumAllMoney);
        hashMap.put("sumDzfMoney", sumDzfMoney);
        hashMap.put("sumDhxMoney", sumDhxMoney);
        hashMap.put("sumJxzMoney", sumJxzMoney);
        hashMap.put("sumYwcMoney", sumYwcMoney);
        hashMap.put("sumYqxMoney", sumYqxMoney);

        return hashMap;
    }

    @Transactional
    public Result cancelLockVerification(Long userId, String orderNumber) {
        Orders orders = baseMapper.selectOne(new QueryWrapper<Orders>().eq("order_number", orderNumber));
        if (orders.getOrderStatus() == 4) {
            return Result.error("订单已取消了");
        }
        if (orders.getOrderStatus() == 2) {
            return Result.error("订单已核销过了");
        }
        if (LocalDate.now().isBefore(orders.getStartDate())) {
            return Result.error("现在还不是入住时间");
        }
        //判断员工是否有权操作指定订单的权限
        if (!isHasOperateOrder(orders.getOrderId(), userId)) {
            return Result.error("您无权操作当前酒店的订单");
        }
        orders.setVerificationUserId(userId);
        orders.setVerificationTime(LocalDateTime.now());
        orders.setOrderStatus(2);
        return Result.upStatus(baseMapper.updateById(orders));
    }

    public String generateOrder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        Random random = new Random();
        int i = (int) ((Math.random() * 9 + 1) * 10000);
        return format + i;
    }

    @Override
    public List<Map<String, Object>> selectOrderTrend(String startTime, String endTime) {
        return baseMapper.selectOrderTrend(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> selectOrderStatusDistribution(String startTime, String endTime) {
        return baseMapper.selectOrderStatusDistribution(startTime, endTime);
    }

    @Override
    public Integer selectTodayOrderCount(String time) {
        return baseMapper.selectTodayOrderCount(time);
    }

}
