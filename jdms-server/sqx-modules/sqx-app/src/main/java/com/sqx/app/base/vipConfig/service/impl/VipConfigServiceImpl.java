package com.sqx.app.base.vipConfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.userMoneyDetails.entity.UserMoneyDetails;
import com.sqx.app.base.userMoneyDetails.service.UserMoneyDetailsService;
import com.sqx.app.base.utils.BuyVipStatus;
import com.sqx.app.base.utils.Result;
import com.sqx.app.base.vipConfig.dao.VipConfigDao;
import com.sqx.app.base.vipConfig.entity.VipConfig;
import com.sqx.app.base.vipConfig.service.IVipConfigService;
import com.sqx.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * vip配置表 服务实现类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-18
 */
@Service
public class VipConfigServiceImpl extends ServiceImpl<VipConfigDao, VipConfig> implements IVipConfigService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Override
    public Result saveVipConfig(VipConfig vipConfig) {
        if (vipConfig.getVipId() != null) {
            vipConfig.setUpdateTime(LocalDateTime.now());
            return Result.upStatus(baseMapper.updateById(vipConfig));
        } else {
            vipConfig.setUpdateTime(LocalDateTime.now());
            vipConfig.setCreateTime(LocalDateTime.now());
            if (vipConfig.getIsEnable() == null) {
                vipConfig.setIsEnable(1);
            }
            return Result.upStatus(baseMapper.insert(vipConfig));
        }
    }

    @Override
    public IPage<VipConfig> getVipConfigList(Integer page, Integer limit, VipConfig vipConfig) {

        Page<VipConfig> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }

        return baseMapper.selectPage(pages, new QueryWrapper<>(vipConfig).orderByAsc("sort"));

    }

    @Override
    @Transactional
    public Result buyVipCallback(Long userId, Long vipId, Integer classify) {
        VipConfig vipConfig = baseMapper.selectById(vipId);
        if (giveUserVip(userId, vipConfig) == 0) {
            return Result.error(BuyVipStatus.PAY_ERROR.getCode(), BuyVipStatus.PAY_ERROR.getInfo());
        } else {
            String payWay = "";
            if (classify == 0) {
                payWay = "余额";
            } else if (classify == 1 || classify == 2 || classify == 3) {
                payWay = "微信";
            } else {
                payWay = "支付宝";
            }
            UserMoneyDetails moneyDetails = new UserMoneyDetails();
            moneyDetails.setTitle(payWay + "购买会员《" + vipConfig.getVipTitle() + "》成功");
            moneyDetails.setMoney(vipConfig.getVipMoney());
            moneyDetails.setUserId(userId);
            moneyDetails.setClassify(4);
            moneyDetails.setType(2);
            moneyDetails.setUserType(1);
            moneyDetails.setContent(payWay + "购买会员《" + vipConfig.getVipTitle() + "》成功,共花费:" + vipConfig.getVipMoney() + "元");
            moneyDetails.setCreateTime(DateUtils.format(new Date()));
            userMoneyDetailsService.save(moneyDetails);
            setCommission(userId, vipConfig.getVipMoney(), 1);
            return Result.success(BuyVipStatus.OK.getInfo());
        }
    }

    public void setCommission(Long userId, BigDecimal money, Integer classify) {
        UserEntity user = userService.getById(userId);
        if (user.getInviterCode() != null) {
            UserEntity userParent = userService.getOne(new QueryWrapper<UserEntity>().eq("invitation_code", user.getInviterCode()));
            if (userParent != null) {
                if (userParent.getZhiRate() != null) {
                    money = new BigDecimal(user.getZhiRate()).multiply(money);
                    this.setIndentMoneyDetails(userParent, money, 1, classify, user);
                }
            }
        }
        if (user.getInviterInviterCode() != null) {
            UserEntity userParentParent = userService.getOne(new QueryWrapper<UserEntity>().eq("invitation_code", user.getInviterInviterCode()));
            if (userParentParent != null) {
                if (userParentParent.getFeiRate() != null) {
                    money = new BigDecimal(user.getFeiRate()).multiply(money);
                }
                this.setIndentMoneyDetails(userParentParent, money, 2, classify, user);
            }
        }
    }

    /**
     * @param userEntity 用户id
     * @param type       1一级用户 2二级用户
     * @param classify   1订单分成
     * @param user       开通会员用户
     */
    public void setIndentMoneyDetails(UserEntity userEntity, BigDecimal money, Integer type, Integer classify, UserEntity user) {
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
            userMoneyDetails.setTitle("一级用户开通VIP" + title);
            userMoneyDetails.setMoney(money);
            userMoneyDetails.setContent("一级用户开通会员" + title + "金额:" + money);
            userMoneyDetails.setUserLevel(1);
            userMoneyDetails.setRelationId(user.getUserId());
        } else if (type == 2) {
            userMoneyDetails.setTitle("二级用户开通VIP" + title);
            userMoneyDetails.setMoney(money);
            userMoneyDetails.setContent("二级用户开通会员" + title + "金额:" + money);
            userMoneyDetails.setRelationId(user.getUserId());
            userMoneyDetails.setUserLevel(2);
        }
        userMoneyDetails.setType(1);
        userMoneyDetails.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userMoneyDetails.setClassify(3);
        //app端用户消息推送
        userService.pushToSingle("开通会员分成", "用户开通VIP"+ title + "金额:" + money, userEntity.getClientidId());
        userMoneyDetailsService.save(userMoneyDetails);
    }

    @Override
    public int giveUserVip(Long userId, VipConfig vipConfig) {
        UserEntity userEntity = userService.getById(userId);
        userEntity.setIsVip(1);
        if (userEntity.getVipEndTime() != null) {
            if (LocalDateTime.now().isAfter(userEntity.getVipEndTime())) {
                userEntity.setVipEndTime(LocalDateTime.now().plusMonths(vipConfig.getMonthCount()));
            } else {
                userEntity.setVipEndTime(userEntity.getVipEndTime().plusMonths(vipConfig.getMonthCount()));
            }
        } else {
            userEntity.setVipEndTime(LocalDateTime.now().plusMonths(vipConfig.getMonthCount()));
        }

        if (userService.updateById(userEntity)) {
            return 1;
        } else {
            return 0;
        }
    }
}
