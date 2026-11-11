package com.sqx.app.base.shopsUserRole.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.common.dao.CommonInfoDao;
import com.sqx.app.base.shops.dao.ShopsDao;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shopsUserRole.dao.ShopsUserRoleDao;
import com.sqx.app.base.shopsUserRole.entity.ShopsUserRole;
import com.sqx.app.base.shopsUserRole.service.IShopsUserRoleService;
import com.sqx.app.base.user.dao.MsgDao;
import com.sqx.app.base.user.dao.UserDao;
import com.sqx.app.base.user.entity.Msg;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.utils.InvitationCodeUtil;
import com.sqx.app.base.utils.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 用户酒店权限 服务实现类
 * </p>
 *
 * @author wuChang
 * @since 2023-08-10
 */
@Service
public class ShopsUserRoleServiceImpl extends ServiceImpl<ShopsUserRoleDao, ShopsUserRole> implements IShopsUserRoleService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ShopsUserRoleDao shopsUserRoleDao;

    @Autowired
    private ShopsDao shopsDao;

    @Autowired
    private MsgDao msgDao;

    @Autowired
    private CommonInfoDao commonInfoDao;

    @Override
    public IPage<UserEntity> getUserList(Integer page, Integer limit, Long shopId, UserEntity userEntity) {
        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return userDao.getUserList(pages, shopId, userEntity);
    }

    @Override
    public Result saveShopUser(Long shopId, String userIds) {
        String[] ids = userIds.split(",");
        for (String userId : ids) {
            //判断用户是否存在
            UserEntity userEntity = userDao.selectById(userId);
            if (userEntity != null) {
                //过滤已被当前酒店绑定的员工
                Long count = baseMapper.selectCount(new QueryWrapper<ShopsUserRole>().eq("user_id", userId).eq("shop_id", shopId));
                if (count == 0) {
                    //过滤已被其他酒店绑定的员工
                    Long shopCount = baseMapper.selectCount(new QueryWrapper<ShopsUserRole>().eq("user_id", userId).ne("shop_id", shopId));
                    if (shopCount == 0) {
                        ShopsUserRole shopsUserRole = new ShopsUserRole();
                        shopsUserRole.setUserId(Long.valueOf(userId));
                        shopsUserRole.setShopId(shopId);
                        shopsUserRole.setCreateTime(LocalDateTime.now());
                        baseMapper.insert(shopsUserRole);
                    } else {
                        return Result.error("用户id:" + userId + "已被其他酒店绑定");
                    }
                }
            } else {
                return Result.error("用户id:" + userId + "不存在");
            }
        }
        return Result.success();
    }

    @Override
    public Result deleteShopUser(Long shopId, String userIds) {
        String[] userList = userIds.trim().split(",");
        for (String userId : userList) {
            baseMapper.delete(new QueryWrapper<ShopsUserRole>().eq("shop_id", shopId).eq("user_id", userId));
            userDao.update(null,new LambdaUpdateWrapper<UserEntity>()
                    .eq(UserEntity::getUserId, userId)
                    .set(UserEntity::getIsShopRider, 2));
        }
        return Result.success();

    }

    @Override
    public Result getShopId(Long userId) {
        Shops shops = shopsDao.selectOne(new LambdaQueryWrapper<Shops>().eq(Shops::getUserId, userId));
        if (shops != null) {
            return Result.success().put("data", shops.getShopId());
        } else {
            ShopsUserRole shopUserRole = shopsUserRoleDao.selectOne(new LambdaQueryWrapper<ShopsUserRole>().eq(ShopsUserRole::getUserId, userId));
            if (shopUserRole != null) {
                return Result.success().put("data", shopUserRole.getShopId());
            }
        }
        return Result.success().put("data", 0);
    }

    @Override
    public Result insertStaffUser(Long userId, String phone, String msg, Long shopId, String userName) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        if (msg1 == null) {
            return Result.error("验证码不正确");
        }
        Shops shop = shopsDao.selectById(shopId);
        if (shop == null) {
            return Result.error("店铺不存在");
        }
        if (StringUtils.isBlank(userName)) {
            return Result.error("用户名不能为空");
        }
        UserEntity userInfo = userDao.selectOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getPhone, phone));
        if (userInfo == null) {
            userInfo = new UserEntity();
            userInfo.setUserType(2);
            userInfo.setPhone(phone);
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            userInfo.setUserName(userName);
            userInfo.setAvatar("https://xianyu.xianmaxiong.com/file/uploadPath/2024/11/05/e7a5be497b46d23840c644935cb6f047.png");
            userInfo.setPlatform("app");
            userInfo.setCreateTime(LocalDateTime.now());
            String salt = RandomStringUtils.randomAlphanumeric(20);
            userInfo.setSalt(salt);
            userInfo.setPassword(new Sha256Hash("123456", salt).toHex());
            userInfo.setInviterCode(commonInfoDao.findOne(419).getValue());
            userInfo.setBalance(BigDecimal.ZERO);
            userInfo.setStatus(1);
            userInfo.setZhiRate(commonInfoDao.findOne(166).getValue());
            userInfo.setFeiRate(commonInfoDao.findOne(167).getValue());
            userInfo.setUpdateTime(LocalDateTime.now());
            userInfo.setShopId(shopId);
            userInfo.setIsShopRider(1);
            userDao.insert(userInfo);
            ShopsUserRole shopsUserRole1 = new ShopsUserRole();
            shopsUserRole1.setUserId(userInfo.getUserId());
            shopsUserRole1.setShopId(shopId);
            shopsUserRole1.setCreateTime(LocalDateTime.now());
            baseMapper.insert(shopsUserRole1);
            return Result.success("添加成功");
        } else {
            if (userId.equals(userInfo.getUserId())) {
                return Result.error("您不能添加自己为员工");
            }
            ShopsUserRole shopsUserRole = shopsUserRoleDao.selectOne(new LambdaQueryWrapper<ShopsUserRole>()
                    .eq(ShopsUserRole::getUserId, userInfo.getUserId())
                    .eq(ShopsUserRole::getShopId, shopId));
            if (shopsUserRole != null) {
                return Result.error("该员工已存在");
            }
            userInfo.setUpdateTime(LocalDateTime.now());
            userInfo.setShopId(shopId);
            userInfo.setIsShopRider(1);
            userInfo.setUserType(2);
            userDao.updateById(userInfo);
            ShopsUserRole shopsUserRole1 = new ShopsUserRole();
            shopsUserRole1.setUserId(userInfo.getUserId());
            shopsUserRole1.setShopId(shopId);
            shopsUserRole1.setCreateTime(LocalDateTime.now());
            baseMapper.insert(shopsUserRole1);
            return Result.success("添加成功");
        }
    }

    @Override
    public Result insertStaffUser1(Long userId, String phone, Long shopId, String userName) {
        Shops shop = shopsDao.selectById(shopId);
        if (shop == null) {
            return Result.error("店铺不存在");
        }
        if (StringUtils.isBlank(userName)) {
            return Result.error("用户名不能为空");
        }
        if (shop.getUserId().equals(userId)) {
            return Result.error("不能绑定自己");
        }
        UserEntity userInfo = userDao.selectOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getPhone, phone));
        if (userInfo == null) {
            userInfo = new UserEntity();
            userInfo.setUserType(2);
            userInfo.setPhone(phone);
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            userInfo.setUserName(userName);
            userInfo.setAvatar("https://xianyu.xianmaxiong.com/file/uploadPath/2024/11/05/e7a5be497b46d23840c644935cb6f047.png");
            userInfo.setPlatform("app");
            userInfo.setCreateTime(LocalDateTime.now());
            String salt = RandomStringUtils.randomAlphanumeric(20);
            userInfo.setSalt(salt);
            userInfo.setPassword(new Sha256Hash("123456", salt).toHex());
            userInfo.setInviterCode(commonInfoDao.findOne(419).getValue());
            userInfo.setBalance(BigDecimal.ZERO);
            userInfo.setStatus(1);
            userInfo.setZhiRate(commonInfoDao.findOne(166).getValue());
            userInfo.setFeiRate(commonInfoDao.findOne(167).getValue());
            userInfo.setUpdateTime(LocalDateTime.now());
            userInfo.setShopId(shopId);
            userInfo.setIsShopRider(1);
            userDao.insert(userInfo);
            ShopsUserRole shopsUserRole1 = new ShopsUserRole();
            shopsUserRole1.setUserId(userInfo.getUserId());
            shopsUserRole1.setShopId(shopId);
            shopsUserRole1.setCreateTime(LocalDateTime.now());
            baseMapper.insert(shopsUserRole1);
            return Result.success("添加成功");
        } else {
            if (userId.equals(userInfo.getUserId())) {
                return Result.error("您不能添加自己为员工");
            }
            ShopsUserRole shopsUserRole = shopsUserRoleDao.selectOne(new LambdaQueryWrapper<ShopsUserRole>()
                    .eq(ShopsUserRole::getUserId, userInfo.getUserId())
                    .eq(ShopsUserRole::getShopId, shopId));
            if (shopsUserRole != null) {
                return Result.error("该员工已存在");
            }
            userInfo.setUpdateTime(LocalDateTime.now());
            userInfo.setShopId(shopId);
            userInfo.setIsShopRider(1);
            userInfo.setUserType(2);
            userDao.updateById(userInfo);
            ShopsUserRole shopsUserRole1 = new ShopsUserRole();
            shopsUserRole1.setUserId(userInfo.getUserId());
            shopsUserRole1.setShopId(shopId);
            shopsUserRole1.setCreateTime(LocalDateTime.now());
            baseMapper.insert(shopsUserRole1);
            return Result.success("添加成功");
        }
    }
}
