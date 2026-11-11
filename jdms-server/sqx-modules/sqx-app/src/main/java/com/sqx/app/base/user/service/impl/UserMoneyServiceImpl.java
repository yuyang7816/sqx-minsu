package com.sqx.app.base.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.orders.service.IOrdersService;
import com.sqx.app.base.roomOrderRecord.service.IRoomOrderRecordService;
import com.sqx.app.base.user.dao.UserDao;
import com.sqx.app.base.user.dao.UserMoneyDao;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.entity.UserMoney;
import com.sqx.app.base.user.service.UserMoneyService;
import com.sqx.app.base.userMoneyDetails.entity.UserMoneyDetails;
import com.sqx.app.base.userMoneyDetails.service.UserMoneyDetailsService;
import com.sqx.app.base.utils.BuyVipStatus;
import com.sqx.app.base.utils.Result;
import com.sqx.app.base.utils.SpringContextUtils;
import com.sqx.app.base.vipConfig.entity.VipConfig;
import com.sqx.app.base.vipConfig.service.IVipConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Service
@Slf4j
public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private IVipConfigService vipConfigService;
    @Autowired
    private IRoomOrderRecordService orderRecordService;

    @Override
    public void updateMoney(int i, Long userId, BigDecimal money) {
        baseMapper.updateMayMoney(i, userId, money);
    }



    @Override
    public Result updateUserMoney(Integer type, Long userId, BigDecimal money) {

        long count = userMoneyDao.selectCount(new QueryWrapper<UserMoney>().eq("user_id", userId));
        //如果用户钱包不存在
        if (count == 0) {
            UserMoney userMoney = new UserMoney();
            userMoney.setMoney(BigDecimal.ZERO);
            userMoney.setUserId(userId);
            userMoneyDao.insert(userMoney);
        }
        if (type == 1) {
            userMoneyDao.updateMayMoney(type, userId, money);
        } else if (type == 2) {
            userMoneyDao.updateMayMoney(2, userId, money);

        }
        UserEntity userEntity = userDao.selectById(userId);
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userEntity.getUserId());
        userMoneyDetails.setUserType(1);
        userMoneyDetails.setClassify(7);
        if (type == 1) {
            userMoneyDetails.setTitle("系统赠送用户余额");
            userMoneyDetails.setContent("赠送用户余额:" + money + "元");
            userMoneyDetails.setType(1);
        } else if (type == 2) {
            userMoneyDetails.setTitle("系统扣除用户余额");
            userMoneyDetails.setContent("扣除用户余额:" + money + "元");
            userMoneyDetails.setType(2);
        }
        userMoneyDetails.setMoney(money);
        userMoneyDetails.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success();

    }



    @Override
    public Result payOrder(Long userId, Long ordersId) {
        IOrdersService ordersService = SpringContextUtils.getBean(IOrdersService.class);
        Orders orders = ordersService.getById(ordersId);
        if (orders == null) {
            return Result.error("订单不存在");
        }
        if (orders.getOrderStatus() != 0) {
            return Result.error("订单状态已变更,请刷新后重试!");
        }
        String roomValidateError = orderRecordService.validateOrderRoomAvailability(orders);
        if (roomValidateError != null) {
            return Result.error(roomValidateError.replace("请重新选择日期", "请取消订单后重新选择日期"));
        }
        UserMoney userMoney = baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
        if (userMoney.getMoney().compareTo(orders.getPayMoney()) >= 0) {
            baseMapper.updateMayMoney(2, userId, orders.getPayMoney());
            return ordersService.orderCallback(ordersId, 0, orders.getPayMoney());
        } else {
            return Result.error("余额不足,请充值后再试!");
        }

    }

    @Override
    public Result payVip(Long userId, Long vipId) {
        VipConfig vipConfig = vipConfigService.getById(vipId);
        if (vipConfig == null || vipConfig.getIsEnable() == 0) {
            return Result.error(BuyVipStatus.GOOD_DOWN.getCode(), BuyVipStatus.GOOD_DOWN.getInfo());
        }
        UserMoney userMoney = userMoneyDao.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
        if (userMoney.getMoney().compareTo(vipConfig.getVipMoney()) >= 0) {
            return vipConfigService.buyVipCallback(userId, vipId, 0);
        } else {
            return Result.error(BuyVipStatus.BALANCE_FUNDS.getCode(), BuyVipStatus.BALANCE_FUNDS.getInfo());
        }

    }


}
