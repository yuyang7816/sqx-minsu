package com.sqx.app.base.user.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.service.IAppleService;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.user.utils.UserConstantInterface;
import com.sqx.app.base.user.utils.WxPhone;
import com.sqx.app.base.utils.HttpClientUtil;
import com.sqx.app.base.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;

import java.util.HashMap;
import java.util.Map;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/app/Login")
@Tag(name = "APP登录接口---用户端")
@Slf4j
public class AppLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private IAppleService appleService;
    @Autowired
    private CommonInfoService commonInfoService;

    @Operation(summary = "用户端微信小程序登陆")
    @RequestMapping(value = "/wxLogin", method = RequestMethod.GET)
    public Result wxLogin(@Parameter(description = "小程序code码") @RequestParam(required = false) String code) {
        return userService.wxLogin(code, 1);
    }


    @Operation(summary = "用户端小程序登录新增或修改个人信息")
    @RequestMapping(value = "/insertWxUser", method = RequestMethod.POST)
    public Result insertWxUser(@RequestBody UserEntity userInfo) {
        userInfo.setUserType(1);
        return userService.wxRegister(userInfo);
    }


    @Operation(summary = "员工微信小程序登陆")
    @RequestMapping(value = "/wxRiderLogin", method = RequestMethod.GET)
    public Result wxRiderLogin(@Parameter(description = "小程序code码") @RequestParam(required = false) String code) {
        return userService.wxLogin(code, 2);
    }


    @Operation(summary = "员工小程序登录新增或修改个人信息")
    @RequestMapping(value = "/insertWxRiderUser", method = RequestMethod.POST)
    public Result insertWxRiderUser(@RequestBody UserEntity userInfo) {
        userInfo.setUserType(2);
        return userService.wxRegister(userInfo);
    }


    @RequestMapping(value = "/appleLogin", method = RequestMethod.GET)
    @Operation(summary = "苹果登陆获取appleUserId")
    public Result loginVerify(@RequestParam("identityToken") @Parameter(description = "identityToken") String identityToken) {
        try {
            log.info("苹果token：{}", identityToken);
            JSONObject jsonObject = JSON.parseObject(identityToken);
            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
            String identityTokens = userInfo.getString("identityToken");
            return appleService.getAppleUserInfo(identityTokens);
        } catch (Exception e) {
            log.error("苹果token校验失败：{}", identityToken, e);
            return Result.error("苹果账号验证失败，请退出重试！");
        }
    }


    @Operation(summary = "苹果登录")
    @RequestMapping(value = "/insertAppleUser", method = RequestMethod.GET)
    public Result insertAppleUser(@RequestParam @Parameter(description = "苹果id") String appleId) {
        return userService.iosRegister(appleId);
    }

    @RequestMapping(value = "/iosBindMobile", method = RequestMethod.POST)
    @Operation(summary = "苹果登录绑定手机号")
    @ResponseBody
    public Result iosBindMobile(@RequestParam(required = false) @Parameter(description = "手机号") String phone,
                                @RequestParam(required = false) @Parameter(description = "验证码") String code,
                                @RequestParam(required = false) @Parameter(description = "苹果id") String appleId,
                                @RequestParam(required = false) @Parameter(description = "来源信息") String platform,
                                @RequestParam(required = false) @Parameter(description = "手机类型 1安卓 2ios") Integer sysPhone) {
        return userService.iosBindMobile(phone, code, appleId, platform, sysPhone);
    }


    @RequestMapping(value = "/wxAppLogin", method = RequestMethod.POST)
    @Operation(summary = "微信APP登录")
    @ResponseBody
    public Result wxAppLogin(@RequestParam(required = false) @Parameter(description = "微信openId") String wxOpenId,
                             @RequestParam(required = false) @Parameter(description = "token") String token) {
        return userService.wxAppLogin(wxOpenId, token);
    }


    @RequestMapping(value = "/wxBindMobile", method = RequestMethod.POST)
    @Operation(summary = "微信登录绑定手机号")
    @ResponseBody
    public Result wxBindMobile(@RequestParam(required = false) @Parameter(description = "手机号") String phone,
                               @RequestParam(required = false) @Parameter(description = "验证码") String code,
                               @RequestParam(required = false) @Parameter(description = "微信openId") String wxOpenId,
                               @RequestParam(required = false) @Parameter(description = "token") String token,
                               @RequestParam(required = false) @Parameter(description = "来源信息") String platform,
                               @RequestParam(required = false) @Parameter(description = "手机类型 1安卓 2ios") Integer sysPhone) {
        return userService.wxBindMobile(phone, code, wxOpenId, token, platform, sysPhone);
    }

    @GetMapping("/wxOpenIdLogin")
    @Operation(summary = "公众号openId登录")
    public Result wxOpenIdLogin(@RequestParam(required = false) @Parameter(description = "openId") String openId,
                                @RequestParam(required = false) @Parameter(description = "1用户 2员工") Integer userType) {
        return userService.wxOpenIdLogin(openId, userType);
    }

    @RequestMapping(value = "/registerCode", method = RequestMethod.POST)
    @Operation(summary = "app或h5注册或登录")
    @ResponseBody
    public Result registerCode(@RequestParam(required = false) @Parameter(description = "手机号") String phone,
                               @RequestParam(required = false) @Parameter(description = "验证码") String msg,
                               @RequestParam(required = false) @Parameter(description = "来源信息") String platform,
                               @RequestParam(required = false) @Parameter(description = "手机类型 1安卓 2ios") Integer sysPhone,
                               @RequestParam(required = false) @Parameter(description = "密码") String password,
                               @RequestParam(required = false) @Parameter(description = "1用户 2员工") Integer userType,
                               @RequestParam(required = false) @Parameter(description = "微信openId") String openId,
                               @RequestParam(required = false) @Parameter(description = "邀请码") String inviterCode,
                               @RequestParam(required = false) @Parameter(description = "用户名称")String userName) {
        return userService.registerCode(phone, msg, platform, sysPhone, password, userType, openId, inviterCode,userName);
    }

    @Operation(summary = "用户端发送验证码")
    @RequestMapping(value = "/sendMsg/{phone}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Result sendMsg(@Parameter(description = "手机号") @PathVariable String phone,
                          @Parameter(description = "验证码类型 login 登录或忘记密码时传递") @PathVariable String state) {
        return userService.sendMsg(phone, state);
    }

    @Operation(summary = "解密手机号")
    @RequestMapping(value = "/selectPhone", method = RequestMethod.POST)
    public Result getPhoneNumberBeanS5(@RequestBody WxPhone wxPhone) {
        return UserConstantInterface.decryptS5(wxPhone.getDecryptData(), wxPhone.getKey(), wxPhone.getIv());
    }

    @Parameter(description = "登录app")
    @RequestMapping(value = "/loginApp", method = RequestMethod.POST)
    public Result loginApp(@RequestParam(required = false) @Parameter(description = "手机号") String phone,
                           @RequestParam(required = false) @Parameter(description = "密码") String password) {
        return userService.loginApp(phone, password);
    }

    @Operation(summary = "用户端忘记密码")
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
    @ResponseBody
    public Result forgetPwd(@RequestParam(required = false) @Parameter(description = "密码") String pwd,
                            @RequestParam(required = false) @Parameter(description = "手机号") String phone,
                            @RequestParam(required = false) @Parameter(description = "验证码") String msg) {
        return userService.forgetPwd(pwd, phone, msg);
    }


    @GetMapping("/selectCity")
    @Operation(summary = "根据经纬度获取城市")
    public Result selectCity(@RequestParam String lat, @RequestParam String lng) {
        String way = commonInfoService.findOne(414).getValue();
        if ("1".equals(way)) {
            CommonInfo one = commonInfoService.findOne(128);
            String url = "https://apis.map.qq.com/ws/geocoder/v1/";
            Map<String, String> maps = new HashMap<>();
            maps.put("location", lat + "," + lng);
            maps.put("key", one.getValue());
            String data = HttpClientUtil.doGet(url, maps);
            JSONObject jsonObject = JSON.parseObject(data);
            String status = jsonObject.getString("status");
            if ("0".equals(status)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject adInfo = result.getJSONObject("ad_info");
//                adInfo.put("building", result.getString("building"));
                adInfo.put("address", extractDetailAddress(
                        result.getString("address"),
                        adInfo.getString("province"),
                        adInfo.getString("city"),
                        adInfo.getString("district")));
                return Result.success().put("data", adInfo);
            } else {
                log.error("转换失败！！！原因:" + jsonObject.getString("message"));
            }
            return Result.error("获取定位失败！");
        } else {
            String value = commonInfoService.findOne(415).getValue();
            String url = "http://api.tianditu.gov.cn/geocoder";
            Map<String, String> param = new HashMap<>();
            JSONObject postStr = new JSONObject();
            postStr.put("lon", lng);
            postStr.put("lat", lat);
            postStr.put("ver", "1");
            param.put("postStr", postStr.toJSONString());
            param.put("type", "geocode");
            param.put("tk", value);
            String s = HttpClientUtil.doGet(url, param);
            JSONObject jsonObject = JSONObject.parseObject(s);
            String status = jsonObject.getString("status");
            if ("0".equals(status)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject addressComponent = result.getJSONObject("addressComponent");
                String province = addressComponent.getString("province");
                String city = addressComponent.getString("city");
                String county = addressComponent.getString("county");
                String building = addressComponent.getString("poi");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("province", province);
                jsonObject1.put("city", city);
                jsonObject1.put("district", county);
                jsonObject1.put("building", building);
                jsonObject1.put("address", extractDetailAddress(
                        result.getString("formatted_address"), province, city, county));
                return Result.success().put("data", jsonObject1);
            }
            return Result.error("获取定位失败！");
        }

    }

    /**
     * 从完整地址中提取详细地址（去掉省市区前缀），避免 split("市")[1] 越界
     */
    private String extractDetailAddress(String fullAddress, String province, String city, String district) {
        if (fullAddress == null || fullAddress.isEmpty()) {
            return "";
        }
        String detail = fullAddress;
        if (province != null && !province.isEmpty() && detail.startsWith(province)) {
            detail = detail.substring(province.length());
        }
        if (city != null && !city.isEmpty() && detail.startsWith(city)) {
            detail = detail.substring(city.length());
        }
        if (district != null && !district.isEmpty() && detail.startsWith(district)) {
            detail = detail.substring(district.length());
        }
        detail = detail.trim();
        if (!detail.isEmpty()) {
            return detail;
        }
        String[] parts = fullAddress.split("市", 2);
        return parts.length > 1 ? parts[1].trim() : fullAddress;
    }


    @GetMapping("/getOpenId")
    @Operation(summary = "用户公众号根据code换取openId")
    public Result getOpenId(@RequestParam(required = false) @Parameter(description = "微信code") String code) {
        try {

            CommonInfo one = commonInfoService.findOne(5);
            CommonInfo two = commonInfoService.findOne(21);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            String openid = snsToken.getOpenid();
            return Result.success().put("data", openid);
        } catch (Exception e) {
            throw new RuntimeException("GET_OPENID_FAIL");
        }

    }

    @GetMapping("/getRiderOpenId")
    @Operation(summary = "员工公众号根据code换取openId")
    public Result getRiderOpenId(@RequestParam(required = false) @Parameter(description = "微信code") String code) {
        try {
            CommonInfo one = commonInfoService.findOne(262);
            CommonInfo two = commonInfoService.findOne(263);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            String openid = snsToken.getOpenid();
            return Result.success().put("data", openid);
        } catch (Exception e) {
            throw new RuntimeException("GET_OPENID_FAIL");
        }

    }


    @GetMapping("/bindOpenId")
    @Operation(summary = "用户绑定公众号openId")
    public Result bindOpenId(@RequestParam(required = false) @Parameter(description = "用户id") Long userId,
                             @RequestParam(required = false) @Parameter(description = "公众号openId") String openId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setOpenId(openId);
        userService.updateById(userEntity);
        return Result.success();
    }


}
