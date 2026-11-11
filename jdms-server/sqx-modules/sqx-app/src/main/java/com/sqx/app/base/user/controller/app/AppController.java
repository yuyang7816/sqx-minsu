package com.sqx.app.base.user.controller.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.Shop;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.pay.dao.CashOutDao;
import com.sqx.app.base.pay.service.impl.CashOutServiceImpl;
import com.sqx.app.base.shops.dao.ScFollowDao;
import com.sqx.app.base.shops.dao.ShopsDao;
import com.sqx.app.base.shops.dao.TbFootprintDao;
import com.sqx.app.base.shops.entity.ScFollow;
import com.sqx.app.base.shops.entity.Shops;
import com.sqx.app.base.shops.entity.TbFootprint;
import com.sqx.app.base.shopsUserRole.dao.ShopsUserRoleDao;
import com.sqx.app.base.shopsUserRole.entity.ShopsUserRole;
import com.sqx.app.base.tbCoupon.dao.TbCouponUserDao;
import com.sqx.app.base.tbCoupon.entity.TbCoupon;
import com.sqx.app.base.tbCoupon.entity.TbCouponUser;
import com.sqx.app.base.user.annotation.Login;
import com.sqx.app.base.user.annotation.LoginUser;
import com.sqx.app.base.user.dao.UserMoneyDao;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.entity.UserMoney;
import com.sqx.app.base.user.service.AppService;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.utils.HttpClientUtil;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/app/user")
@Tag(name = "APP管理---用户端")
public class AppController {
    @Autowired
    private CommonInfoService commonInfoService;
    /**
     * 储存车辆品牌
     */
    private static List<Map<String, Object>> carList;

    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;

    @Autowired
    private UserMoneyDao userMoneyDao;

    @Autowired
    private CashOutDao cashOutDao;

    @Autowired
    private TbCouponUserDao tbCouponUserDao;

    @Autowired
    private ScFollowDao scFollowDao;

    @Autowired
    private TbFootprintDao tbFootprintDao;

    @Autowired
    private ShopsDao shopsDao;

    @Autowired
    private ShopsUserRoleDao shopsUserRoleDao;

    @Login
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    @Operation(summary = "用户端修改密码")
    public Result updatePwd(@LoginUser UserEntity user,
                            @RequestParam(required = false) @Parameter(description = "新密码") String pwd,
                            @RequestParam(required = false) @Parameter(description = "旧密码") String oldPwd) {
        if (!user.getPassword().equals(new Sha256Hash(oldPwd, user.getSalt()).toHex())) {
            return Result.error("原始密码不正确！");
        }
        if (pwd.equals(oldPwd)) {
            return Result.error("新密码不能与旧密码相同！");
        }
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(new Sha256Hash(pwd, salt).toHex());
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @Operation(summary = "用户端换绑手机号")
    @ResponseBody
    public Result updatePhone(@RequestAttribute("userId") Long userId,
                              @RequestParam @Parameter(description = "手机号") String phone,
                              @RequestParam @Parameter(description = "验证码") String msg) {
        return userService.updatePhone(phone, msg, userId);
    }

    @Login
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @Operation(summary = "用户修改个人信息")
    @ResponseBody
    public Result updateUser(@RequestBody UserEntity userEntity,
                             @RequestAttribute("userId") Long userId) {
        userEntity.setUserId(userId);
        boolean success = userService.updateById(userEntity);
        return success ? Result.success() : Result.error();
    }

    @Login
    @RequestMapping(value = "/updateUserImageUrl", method = RequestMethod.POST)
    @Operation(summary = "用户修改头像")
    @ResponseBody
    public Result updateUserImageUrl(@LoginUser UserEntity user,
                                     @RequestParam(required = false) @Parameter(description = "头像") String avatar) {
        user.setAvatar(avatar);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @Operation(summary = "用户修改昵称")
    @ResponseBody
    public Result updateUserName(@LoginUser UserEntity user,
                                 @RequestParam(required = false) @Parameter(description = "用户名") String userName) {
        user.setUserName(userName);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/selectUserById", method = RequestMethod.GET)
    @Operation(summary = "获取用户详细信息")
    @ResponseBody
    public Result selectUserById(UserEntity user, @RequestAttribute Long userId) {
        UserEntity userEntity = userService.selectUserById(userId);
        if (userEntity.getShopId() != null) {
            Shops shop = shopsDao.selectOne(new LambdaQueryWrapper<Shops>()
                    .eq(Shops::getShopId, userEntity.getShopId()));
            if (shop != null) {
                userEntity.setShopStatus(shop.getAuditStatus());
            }
        }
        UserMoney userMoney = userMoneyDao.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userEntity.getUserId()));
        if (userMoney == null) {
            userMoney = new UserMoney();
            userMoney.setUserId(userEntity.getUserId());
            userMoney.setMoney(BigDecimal.ZERO);
            userMoneyDao.insert(userMoney);
            userEntity.setCanCashMoney(BigDecimal.ZERO);
        } else {
            userEntity.setCanCashMoney(userMoney.getMoney());
        }
        BigDecimal totalMoney = cashOutDao.selectSumMoney(userEntity.getUserId());
        userEntity.setTotalCashMoney(totalMoney == null ? BigDecimal.ZERO : totalMoney);
        return Result.success().put("data", userEntity);
    }

    @Login
    @RequestMapping(value = "/selectUserMoney", method = RequestMethod.GET)
    @Operation(summary = "获取用户全部资金、可用、可提现资金")
    @ResponseBody
    public Result selectUserMoney(@LoginUser UserEntity user) {
        HashMap<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.selectUserById(user.getUserId());
        UserMoney userMoney = userMoneyDao.selectOne(new QueryWrapper<UserMoney>().eq("user_id", user.getUserId()));
        if (userMoney == null) {
            userMoney = new UserMoney();
            userMoney.setUserId(user.getUserId());
            userMoney.setMoney(BigDecimal.ZERO);
            userMoneyDao.insert(userMoney);
            userEntity.setCanCashMoney(BigDecimal.ZERO);
        } else {
            userEntity.setCanCashMoney(userMoney.getMoney());
        }
        BigDecimal totalMoney = cashOutDao.selectSumMoney(user.getUserId());
        if (totalMoney == null || totalMoney.doubleValue() == 0) {
            totalMoney = BigDecimal.ZERO;
            totalMoney = totalMoney.add(userEntity.getCanCashMoney());
        }
        // 全部资金
        map.put("totalMoney", totalMoney);
        // 可用资金
        map.put("userMoney", userEntity.getCanCashMoney());
        // 可提现资金
        map.put("canCashMoney", userEntity.getCanCashMoney());
        return Result.success().put("data", map);
    }

    @Login
    @RequestMapping(value = "/selectUserNum", method = RequestMethod.GET)
    @Operation(summary = "获取用户优惠券、收藏、足迹的数量")
    @ResponseBody
    public Result selectUserNum(@LoginUser UserEntity user) {
        HashMap<String, Object> map = new HashMap<>();
        // 优惠券数量
        map.put("couponNum", tbCouponUserDao.selectCount(new LambdaQueryWrapper<TbCouponUser>()
                .eq(TbCouponUser::getUserId, user.getUserId())
                .eq(TbCouponUser::getStatus, 0)));
        // 收藏酒店数量
        map.put("collectNum", scFollowDao.selectCount(new LambdaQueryWrapper<ScFollow>()
                .eq(ScFollow::getUserId, user.getUserId())
                .eq(ScFollow::getClassify, 1)));
        // 我的足迹数量（按酒店去重，与足迹列表一致）
        Long footprintNum = tbFootprintDao.countDistinctShopByUserId(user.getUserId());
        map.put("footprintNum", footprintNum == null ? 0 : footprintNum);
        return Result.success().put("data", map);
    }


    @RequestMapping(value = "/selectNewApp", method = RequestMethod.GET)
    @Operation(summary = "升级检测")
    @ResponseBody
    public Result selectNewApp() {
        return Result.success().put("data", appService.selectNewApp());
    }

    @RequestMapping(value = "/updateClientId", method = RequestMethod.GET)
    @Operation(summary = "绑定ClientId")
    @ResponseBody
    public Result updateClientId(@RequestParam(required = false) @Parameter(description = "用户推送消息id") String clientId,
                                 @RequestParam(required = false) @Parameter(description = "用户id") Long userId,
                                 @RequestParam(required = false) @Parameter(description = "手机类型 1安卓 2ios") Integer sysPhone) {
        UserEntity userEntity = new UserEntity();
        userEntity.setSysPhone(sysPhone);
        userEntity.setUserId(userId);
        userEntity.setClientidId(clientId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @RequestMapping(value = "/updateClientRiderId", method = RequestMethod.GET)
    @Operation(summary = "绑定员工ClientId")
    @ResponseBody
    public Result updateClientRiderId(@RequestParam(required = false) @Parameter(description = "员工推送消息id") String clientRiderId,
                                      @RequestParam(required = false) @Parameter(description = "用户id") Long userId,
                                      @RequestParam @Parameter(description = "手机类型 1安卓 2ios") Integer sysPhone) {
        UserEntity userEntity = new UserEntity();
        userEntity.setSysPhone(sysPhone);
        userEntity.setUserId(userId);
        userEntity.setRiderClientidId(clientRiderId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @Login
    @GetMapping("/queryInviterCount")
    public Result select(@LoginUser UserEntity userEntity) {
        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        return Result.success().put("data", count);
    }


    @Login
    @GetMapping("/selectCarApi")
    @Operation(summary = "查询汽车品牌和车系")
    public Result selectCarApi() {
        if (carList != null) {
            return Result.success().put("data", carList);
        }
        String url = "http://apis.juhe.cn/cxdq/brand";
        Map<String, String> param = new HashMap<>();
        param.put("key", commonInfoService.findOne(363).getValue());
        String data = HttpClientUtil.doGet(url, param);
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONArray result = jsonObject.getJSONArray("result");
        List<Map<String, Object>> carList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        String oldL = "A";
        for (int i = 0; i < result.size(); i++) {
            JSONObject jsonObject1 = result.getJSONObject(i);
            String l = jsonObject1.getString("first_letter");
            if (oldL.equals(l)) {
                jsonArray.add(jsonObject1);
            } else {
                map.put("letter", oldL);
                map.put("carList", jsonArray);
                oldL = l;
                carList.add(map);
                map = new HashMap<>();
                jsonArray = new JSONArray();
                jsonArray.add(jsonObject1);
            }
        }
        map.put("letter", oldL);
        map.put("carList", jsonArray);
        carList.add(map);
        AppController.carList = carList;
        return Result.success().put("data", carList);

    }


}
