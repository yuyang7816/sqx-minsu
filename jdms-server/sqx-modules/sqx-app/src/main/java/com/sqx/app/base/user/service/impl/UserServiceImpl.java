package com.sqx.app.base.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;
import com.getui.push.v2.sdk.dto.req.message.harmony.HarmonyDTO;
import com.getui.push.v2.sdk.dto.req.message.harmony.HarmonyNotification;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.file.utils.Md5Utils;
import com.sqx.app.base.message.entity.MessageInfo;
import com.sqx.app.base.message.service.MessageService;
import com.sqx.app.base.user.dao.MsgDao;
import com.sqx.app.base.user.dao.UserDao;
import com.sqx.app.base.user.dao.UserMoneyDao;
import com.sqx.app.base.user.entity.AppUserInfo;
import com.sqx.app.base.user.entity.Msg;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.entity.UserMoney;
import com.sqx.app.base.user.service.UserMoneyService;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.user.utils.JwtUtils;
import com.sqx.app.base.user.utils.UserConstantInterface;
import com.sqx.app.base.userMoneyDetails.dao.UserMoneyDetailsDao;
import com.sqx.app.base.userMoneyDetails.service.UserMoneyDetailsService;
import com.sqx.app.base.utils.HttpClientUtil;
import com.sqx.app.base.utils.InvitationCodeUtil;
import com.sqx.app.base.utils.Result;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.common.mybatis.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.SnsAPI;
import weixin.popular.util.JsonUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */

@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MsgDao msgDao;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserMoneyDao userMoneyDao;

    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;

    @Override
    public UserEntity queryByPhone(String phone) {
        if (StringUtils.isNotEmpty(phone)) {
            return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
        }
        return null;
    }

    @Override
    public UserEntity queryByPhoneAndType(String phone) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
    }

    @Override
    public UserEntity queryByOpenId(String openId, Integer userType) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id", openId));
    }

    public UserEntity queryByRiderOpenId(String openId, Integer userType) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("rider_open_id", openId));
    }

    @Override
    public UserEntity queryByWxOpenId(String openId, Integer userType) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_open_id", openId).eq("user_type", userType));
    }

    public UserEntity queryWxOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_open_id", openId));
    }

    public UserEntity queryRiderAccountOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("rider_account_open_id", openId));
    }

    @Override
    public UserEntity queryByAppleId(String appleId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("apple_id", appleId));
    }

    @Override
    public UserEntity queryByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
    }

    @Override
    public UserEntity queryByInvitationCode(String invitationCode) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", invitationCode));
    }

    @Override
    public Result updatePhone(String phone, String msg, Long userId) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 != null) {
            UserEntity userInfo = queryByPhone(phone);
            if (userInfo != null) {
                return Result.error("手机号已经被其他账号绑定");
            } else {
                UserEntity one = baseMapper.selectById(userId);
                one.setPhone(phone);
                baseMapper.updateById(one);
                return Result.success();
            }
        }
        return Result.error("验证码不正确");
    }

    @Override
    public Result iosRegister(String appleId) {
        if (StringUtils.isEmpty(appleId)) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByAppleId(appleId);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(userInfo);
            //返回用户信息
            UserEntity user = queryByAppleId(appleId);
            return getResult(user);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }

    @Override
    public Result wxLogin(String code, Integer type) {
        try {
            String appid;
            String secret;
            if (type == 1) {
                appid = commonInfoService.findOne(45).getValue();
                secret = commonInfoService.findOne(46).getValue();
            } else {
                appid = commonInfoService.findOne(248).getValue();
                secret = commonInfoService.findOne(249).getValue();
            }
            // 配置请求参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", appid);
            param.put("secret", secret);
            param.put("js_code", code);
            param.put("grant_type", UserConstantInterface.WX_LOGIN_GRANT_TYPE);
            param.put("scope", "snsapi_userinfo");
            // 发送请求
            String wxResult = HttpClientUtil.doGet(UserConstantInterface.WX_LOGIN_URL, param);
            log.info(wxResult);
            JSONObject jsonObject = JSONObject.parseObject(wxResult);
            // 获取参数返回的
            String session_key = jsonObject.get("session_key").toString();
            //返回微信小程序openId
            String open_id = jsonObject.get("openid").toString();
            //判断是否注册过
            Map<String, String> map = new HashMap<>();
            // 封装返回小程序
            map.put("session_key", session_key);
            map.put("open_id", open_id);
            //    UserEntity userEntity = null;
            UserEntity userEntity;//存在手机号
            if (type == 1) {
                userEntity = queryByOpenId(open_id, type);
            } else {
                userEntity = queryByRiderOpenId(open_id, type);
            }
            if (userEntity != null && StringUtils.isNotEmpty(userEntity.getPhone())) {
                //存在手机号
                map.put("isPhone", "1");
            } else {
                //不存在手机号
                map.put("isPhone", "2");
            }
            if (jsonObject.get("unionid") != null) {
                String unionid = jsonObject.get("unionid").toString();
                map.put("unionid", unionid);
            } else {
                map.put("unionid", "-1");
            }
            return Result.success("登陆成功").put("data", map);
        } catch (Exception e) {
            System.err.println(e.toString());
            return Result.error("登录失败！");
        }
    }


    @Override
    public Result wxRegister(UserEntity userInfo1) {
        UserEntity userInfo;
        if (userInfo1.getUserType() == 1) {
            userInfo = queryByOpenId(userInfo1.getOpenId(), userInfo1.getUserType());
        } else {
            userInfo = queryByRiderOpenId(userInfo1.getRiderOpenId(), userInfo1.getUserType());
        }


        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }

            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(userInfo);
        } else {
            if (StringUtils.isEmpty(userInfo1.getPhone())) {
                return Result.error("请先授权手机号！");
            }
            //判断是否在app登陆过  手机号是否有账号
            UserEntity userByMobile = queryByPhone(userInfo1.getPhone());
            if (userByMobile != null) {
                //有账号则绑定账号
                if (userInfo1.getUserType() == 1) {
                    userByMobile.setOpenId(userInfo1.getOpenId());
                } else {
                    userByMobile.setRiderOpenId(userInfo1.getRiderOpenId());
                }
                baseMapper.updateById(userByMobile);

                //数据库消息保存
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("小程序账号绑定成功！");
                messageInfo.setTitle("系统通知");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(userByMobile.getUserName());
                messageInfo.setUserId(String.valueOf(userByMobile.getUserId()));
                messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                //app消息推送
                if (StringUtils.isNotEmpty(userByMobile.getClientidId())) {
                    pushToSingle("系统通知", "小程序账号绑定成功", userByMobile.getClientidId());
                }
                if (userByMobile.getStatus().equals(2)) {
                    return Result.error("账号已被封禁，请联系客服处理！");
                }
            } else {
                UserEntity userEntity = queryByInvitationCode(userInfo1.getInviterCode());
                if (StringUtils.isNotBlank(userInfo1.getInviterCode())) {
                    if (userEntity != null) {
                        userInfo1.setInviterCode(userInfo1.getInviterCode());
                        //判断邀请员是否有上级
                        UserEntity parentParentUser = baseMapper.selectOne(new QueryWrapper<UserEntity>()
                                .eq("invitation_code", userEntity.getInviterCode()));
                        if (parentParentUser != null) {
                            userInfo1.setInviterInviterCode(parentParentUser.getInvitationCode());
                        }
                    } else {
                        return Result.error("邀请码不存在");
                    }
                } else {
                    userInfo1.setInviterCode(commonInfoService.findOne(419).getValue());
                }
                //没有则生成新账号
                userInfo1.setCreateTime(LocalDateTime.now());
                userInfo1.setPlatform("小程序");
                userInfo1.setStatus(1);
                userInfo1.setBalance(BigDecimal.valueOf(0));
                //新用户注册，加新用户标识为1
                if (userInfo1.getUserName() == null && userInfo1.getPhone() != null) {
                    String phone = userInfo1.getPhone();
                    String userName = phone.substring(0, 3) + "****" + phone.substring(7);
                    userInfo1.setUserName(userName);
                }
                CommonInfo levelOne = commonInfoService.findOne(166);
                CommonInfo levelTwo = commonInfoService.findOne(167);
                userInfo1.setZhiRate(levelOne.getValue());
                userInfo1.setFeiRate(levelTwo.getValue());
                userInfo1.setInvitationCode(InvitationCodeUtil.toSerialCode());
                //添加默认头像
                userInfo1.setAvatar("https://xianyu.xianmaxiong.com/file/uploadPath/2024/11/05/e7a5be497b46d23840c644935cb6f047.png");
                baseMapper.insert(userInfo1);
                //生成钱包
                UserMoney userMoney = new UserMoney();
                userMoney.setUserId(userInfo1.getUserId());
                userMoney.setMoney(BigDecimal.ZERO);
                userMoneyDao.insert(userMoney);

                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("恭喜您，账号注册成功！");
                messageInfo.setTitle("系统通知");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(userInfo1.getUserName());
                messageInfo.setUserId(String.valueOf(userInfo1.getUserId()));
                messageInfo.setCreateAt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);

            }
        }
        //返回用户信息
        UserEntity user;
        if (userInfo1.getUserType() == 1) {
            user = selectByOpenId(userInfo1.getOpenId());
        } else {
            user = selectByRiderOpenId(userInfo1.getRiderOpenId());
        }
        return getResult(user);
    }

    private UserEntity selectByRiderOpenId(String riderOpenId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("rider_open_id", riderOpenId));
    }

    private UserEntity selectByOpenId(String openId) {

        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id", openId));
    }


    @Override
    public Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getAccountOpenId())) {
                return Result.error("当前手机号已经被其他微信绑定");
            }
            //小程序登陆过
            userInfo.setAccountOpenId(wxOpenId);
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            userInfo.setSex(user.getSex());
            if (StringUtils.isBlank(userInfo.getUserName()) && user.getNickname() != null) {
                userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            }
            baseMapper.updateById(userInfo);
        } else {
            //小程序没有登陆过
            userInfo = new UserEntity();
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            userInfo.setSex(user.getSex());
            if (StringUtils.isBlank(userInfo.getUserName()) && user.getNickname() != null) {
                userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            }
            userInfo.setInviterCode(commonInfoService.findOne(419).getValue());
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            userInfo.setBalance(BigDecimal.ZERO);
            CommonInfo levelOne = commonInfoService.findOne(166);
            CommonInfo levelTwo = commonInfoService.findOne(167);
            userInfo.setZhiRate(levelOne.getValue());
            userInfo.setFeiRate(levelTwo.getValue());
            userInfo.setAccountOpenId(wxOpenId);
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(LocalDateTime.now());
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(userInfo);
            UserMoney userMoney = new UserMoney();
            userMoney.setUserId(userInfo.getUserId());
            userMoney.setMoney(BigDecimal.ZERO);
            userMoneyDao.insert(userMoney);
        }
        UserEntity userEntity = queryWxOpenId(userInfo.getAccountOpenId());
        return getResult(userEntity);
    }

    @Override
    public Result iosBindMobile(String phone, String code, String appleId, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getAppleId())) {
                return Result.error("当前手机号已经被其他绑定");
            }
            userInfo.setAppleId(appleId);
            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(userInfo);
        } else {
            userInfo = new UserEntity();
            userInfo.setInviterCode(commonInfoService.findOne(419).getValue());
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            userInfo.setBalance(BigDecimal.ZERO);
            CommonInfo levelOne = commonInfoService.findOne(166);
            CommonInfo levelTwo = commonInfoService.findOne(167);
            userInfo.setZhiRate(levelOne.getValue());
            userInfo.setFeiRate(levelTwo.getValue());
            userInfo.setSex(0);
            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(LocalDateTime.now());
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(userInfo);

        }
        UserEntity userEntity = queryByAppleId(userInfo.getAppleId());
        return getResult(userEntity);
    }


    @Override
    public Result wxAppLogin(String wxOpenId, String token) {
        UserEntity userEntity = queryWxOpenId(wxOpenId);
        if (userEntity != null) {
            if (userEntity.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            userEntity.setSex(user.getSex());
            if (StringUtils.isBlank(userEntity.getUserName()) && user.getNickname() != null) {
                userEntity.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            }
            userEntity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(userEntity);
            return getResult(userEntity);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }


    @Override
    public Result registerCode(String phone, String msg, String platform, Integer sysPhone, String password, Integer userType, String openId, String inviterCode, String userName) {
        UserEntity userInfo = queryByPhoneAndType(phone);
        //校验手机号是否存在   判断当前手机号是否已经注册过了  如果注册过了  则判断是验证码登录还是账号密码登录
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(password)) {
                if (StringUtils.isEmpty(userInfo.getPassword())) {
                    return Result.error("当前账号未设置密码，请使用短信登录或忘记密码去重置密码！");
                }
                //密码登录  判断输入密码加密之后是否和数据库的加密密码相同
                String pwd = new Sha256Hash(password, userInfo.getSalt()).toHex();
                if (!userInfo.getPassword().equals(pwd)) {
                    return Result.error("账号或密码不正确！");
                }
            } else {
                //验证码的登录  判断输入的验证码 和发送验证码时存入数据库的验证码是否一致
                Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
                //校验短信验证码
                if (msg1 == null) {
                    return Result.error("验证码不正确");
                }
                msgDao.deleteById(msg1.getId());
            }

            //判断当前手机号是否被管理员封禁
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //更新最新的登录时间
            userInfo.setUpdateTime(LocalDateTime.now());
            if (userType == 1) {
                if (StringUtils.isNotBlank(openId)) {
                    userInfo.setAccountOpenId(openId);
                }
            } else if (userType == 2) {
                if (StringUtils.isNotBlank(openId)) {
                    userInfo.setRiderAccountOpenId(openId);
                }
            }
            baseMapper.updateById(userInfo);
            //返回登录信息  tokne  用户信息的等
        } else {
            //没有注册过  则注册新的账号
            //注册账号需要先判断当前手机号是否是真实的手机号 则需要校验验证码
            Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
            //校验短信验证码
            if (msg1 == null) {
                return Result.error("验证码不正确");
            }
            msgDao.deleteById(msg1.getId());
            //验证码校验完成  则创建用户

            //设置注册时间
            userInfo = new UserEntity();
            //填充前端传递的相关参数
            userInfo.setPhone(phone);
            if (StringUtils.isNotBlank(userName)) {
                userInfo.setUserName(userName);
            } else {
                userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            }
            //添加默认头像
            userInfo.setAvatar("https://xianyu.xianmaxiong.com/file/uploadPath/2024/11/05/e7a5be497b46d23840c644935cb6f047.png");
            userInfo.setPlatform(platform);
            if (StringUtils.isNotEmpty(password)) {
                String salt = RandomStringUtils.randomAlphanumeric(20);
                userInfo.setSalt(salt);
                userInfo.setPassword(new Sha256Hash(password, salt).toHex());
            }
            userInfo.setCreateTime(LocalDateTime.now());
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            //用户类型
            userInfo.setUserType(userType);
            if (userType == 1) {
                if (StringUtils.isNotBlank(openId)) {
                    userInfo.setAccountOpenId(openId);
                }
            } else if (userType == 2) {
                if (StringUtils.isNotBlank(openId)) {
                    userInfo.setRiderAccountOpenId(openId);
                }
            }
            userInfo.setUpdateTime(LocalDateTime.now());
            UserEntity userEntity = queryByInvitationCode(inviterCode);
            if (StringUtils.isNotBlank(inviterCode)) {
                if (userEntity != null) {
                    userInfo.setInviterCode(inviterCode);
                    //判断邀请员是否有上级
                    UserEntity parentParentUser = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", (userEntity.getInviterCode())));
                    if (parentParentUser != null) {
                        userInfo.setInviterInviterCode(parentParentUser.getInvitationCode());
                    }
                } else {
                    return Result.error("邀请码不存在");
                }
            } else {
                userInfo.setInviterCode(commonInfoService.findOne(419).getValue());
            }
            userInfo.setBalance(BigDecimal.valueOf(0));
            CommonInfo levelOne = commonInfoService.findOne(166);
            CommonInfo levelTwo = commonInfoService.findOne(167);
            userInfo.setZhiRate(levelOne.getValue());
            userInfo.setFeiRate(levelTwo.getValue());
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            baseMapper.insert(userInfo);
            //生成钱包
            UserMoney userMoney1 = userMoneyDao.selectOne(new LambdaQueryWrapper<UserMoney>()
                    .eq(UserMoney::getUserId, userInfo.getUserId()));
            if (userMoney1 == null) {
                UserMoney userMoney = new UserMoney();
                userMoney.setUserId(userInfo.getUserId());
                userMoney.setMoney(BigDecimal.ZERO);
                userMoneyDao.insert(userMoney);
            }
        }
        return getResult(userInfo);
    }


    @Override
    public Result wxOpenIdLogin(String openId, Integer userType) {
        UserEntity userEntity;
        if (userType == 1) {
            userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("account_open_id", openId));
            if (userEntity == null) {
                userEntity = queryWxOpenId(openId);
            }
        } else {
            userEntity = queryRiderAccountOpenId(openId);
        }
        if (userEntity != null) {
            return getResult(userEntity);
        }
        return Result.error("未注册！");
    }


    @Override
    public Result login(String phone, String pwd) {
        UserEntity userEntity = queryByPhone(phone);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(new Sha256Hash(pwd, userEntity.getSalt()).toHex())) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        userEntity.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(userEntity);
        return getResult(userEntity);
    }

    @Override
    public Result getResult(UserEntity user) {
        //生成token
        String token = jwtUtils.generateToken(user.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", user);
        return Result.success(map);
    }


    @Override
    public Result sendMsg(String phone, String state) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        System.out.println("sendMsg code is " + code);
        CommonInfo three = commonInfoService.findOne(79);
        //默认使用腾讯云
        if (three == null || "1".equals(three.getValue())) {
            //腾讯云短信发送
            return sendMsgTencent(phone, state, code);
        } else if ("2".equals(three.getValue())) {
            //阿里云短信发送
            return sendMsgAlibaba(phone, code);
        } else {
            return sendMsgDXB(phone, state, code);
        }
    }


    private Result sendMsgAlibaba(String phone, int code) {
        //阿里云短信accessKeyId
        CommonInfo three = commonInfoService.findOne(93);
        String accessKeyId = three.getValue();
        //阿里云短信accessSecret
        CommonInfo four = commonInfoService.findOne(94);
        String accessSecret = four.getValue();
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonInfo name = commonInfoService.findOne(12);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", name.getValue());
        String value = commonInfoService.findOne(90).getValue();
        request.putQueryParameter("TemplateCode", value);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            String data = response.getData();
            JSONObject jsonObject = JSON.parseObject(data);
            if ("OK".equals(jsonObject.get("Code"))) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                return Result.success("login");
            } else {
                if (jsonObject.get("Message").toString().contains("分钟")) {
                    return Result.error("短信发送过于频繁，请一分钟后再试！");
                } else if (jsonObject.get("Message").toString().contains("小时")) {
                    return Result.error("短信发送过于频繁，请一小时后再试！");
                } else if (jsonObject.get("Message").toString().contains("天")) {
                    return Result.error("短信发送过于频繁，请明天再试！");
                }
                log.info(jsonObject.get("Message").toString());
                return Result.error("短信发送失败！");
            }
        } catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgTencent(String phone, String state, int code) {
        SmsSingleSenderResult result = null;
        try {
            CommonInfo three = commonInfoService.findOne(31);
            String clientId = three.getValue();

            CommonInfo four = commonInfoService.findOne(32);
            String clientSecret = four.getValue();
            CommonInfo name = commonInfoService.findOne(12);
            /**
             * 发送短信验证码的状态、
             *
             * 在h5登录环境中 传的状态不是以下三种状态
             */
            SmsSingleSender ssender = new SmsSingleSender(Integer.valueOf(clientId), clientSecret);
            switch (state) {
                case "register":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "forget":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "bind":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                default:
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
            }


            System.out.println(result);
            if (result.result == 0) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }
            } else {
                return Result.error(6, result.errMsg);
            }
        } catch (HTTPException | JSONException | IOException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }

    private Result sendMsgDXB(String phone, String state, int code) {
        CommonInfo three = commonInfoService.findOne(164);
        CommonInfo four = commonInfoService.findOne(165);
        CommonInfo name = commonInfoService.findOne(12);
        String testUsername = three.getValue(); //在短信宝注册的用户名
        String testPassword = four.getValue(); //在短信宝注册的密码
        String value = "";
        switch (state) {
            case "register":
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "forget":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "bind":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "dx":
                value = "【" + name.getValue() + "】您有" + code + "条未读消息，赶快上线查看吧！";
                break;
            default:
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
        }
        StringBuilder httpArg = new StringBuilder();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(Md5Utils.md5s(testPassword)).append("&");
        httpArg.append("m=").append(phone).append("&");
        httpArg.append("c=").append(Md5Utils.encodeUrlString(value, "UTF-8"));
        String result = Md5Utils.request("https://api.smsbao.com/sms", httpArg.toString());
        log.error("短信包返回值：" + result);
        if ("0".equals(result)) {
            Msg byPhone = msgDao.findByPhone(phone);
            if (byPhone != null) {
                byPhone.setCode(String.valueOf(code));
                byPhone.setPhone(phone);
                msgDao.updateById(byPhone);
            } else {
                Msg msg = new Msg();
                msg.setCode(String.valueOf(code));
                msg.setPhone(phone);
                msgDao.insert(msg);
            }
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null) {
                return Result.success("login");
            } else {
                return Result.success("register");
            }
        } else {
//            return ResultUtil.error(6, result.errMsg);
            if ("30".equals(result)) {
                return Result.error("错误密码");
            } else if ("40".equals(result)) {
                return Result.error("账号不存在");
            } else if ("41".equals(result)) {
                return Result.error("余额不足");
            } else if ("43".equals(result)) {
                return Result.error("IP地址限制");
            } else if ("50".equals(result)) {
                return Result.error("内容含有敏感词");
            } else if ("51".equals(result)) {
                return Result.error("手机号码不正确");
            }
        }

        return Result.error("验证码发送失败");
    }


    @Override
    public Result getOpenId(String code, Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            String openid = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code).getOpenid();
            if (StringUtils.isNotEmpty(openid)) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(userId);
                userEntity.setOpenId(openid);
                baseMapper.updateById(userEntity);
                return Result.success().put("data", openid);
            }
            return Result.error("获取失败");
        } catch (Exception e) {
            log.error("GET_OPENID_FAIL");
            return Result.error("获取失败,出错了！");
        }
    }

    @Override
    public UserEntity selectUserById(Long userId) {
        return baseMapper.selectById(userId);
    }


    @Override
    public PageUtils selectUserPage(Integer page, Integer limit, String search, String userName, Integer sex, String platform, String sysPhone, Integer status, Integer isAgent) {
        Page<UserEntity> pages = new Page<>(page, limit);
        return new PageUtils(baseMapper.selectUserPage(pages, search,userName, sex, platform, sysPhone, status, isAgent));
    }

    @Override
    public int queryInviterCount(String inviterCode) {
        return baseMapper.queryInviterCount(inviterCode);
    }


    @Override
    public int queryUserCount(int type, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        if (date == null || date == "") {
            date = simpleDateFormat.format(new Date());
        }
        return baseMapper.queryUserCount(type, date);
    }

    @Override
    public Double queryPayMoney(int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String date = simpleDateFormat.format(new Date());
        return baseMapper.queryPayMoney(type, date);
    }

    @Override
    public IPage<Map<String, Object>> queryCourseOrder(Page<Map<String, Object>> iPage, int type, String date) {
        return baseMapper.queryCourseOrder(iPage, type, date);
    }

    @Override
    public int userMessage(String date, int type) {
        return baseMapper.userMessage(date, type);
    }

    @Override
    public List<Map<String, Object>> selectNewUserTrend(String startTime, String endTime) {
        return baseMapper.selectNewUserTrend(startTime, endTime);
    }


    @Override
    public void pushToSingle(String title, String content, String clientId) {
        try {
            if (StringUtils.isNotEmpty(clientId)) {
                UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("clientid", clientId));
                GtApiConfiguration apiConfiguration = new GtApiConfiguration();
                //填写应用配置
                apiConfiguration.setAppId(commonInfoService.findOne(61).getValue());
                apiConfiguration.setAppKey(commonInfoService.findOne(60).getValue());
                apiConfiguration.setMasterSecret(commonInfoService.findOne(62).getValue());
                // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
                apiConfiguration.setDomain("https://restapi.getui.com/v2/");
                // 实例化ApiHelper对象，用于创建接口对象
                ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
                // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
                PushApi pushApi = apiHelper.creatApi(PushApi.class);
                //根据cid进行单推
                PushDTO<Audience> pushDTO = new PushDTO<Audience>();
                // 设置推送参数
                pushDTO.setRequestId(System.currentTimeMillis() + "");
                PushMessage pushMessage = new PushMessage();
                if (userEntity == null || userEntity.getSysPhone() == null || userEntity.getSysPhone() == 1) {
                    //安卓推送
                    GTNotification notification = new GTNotification();
                    pushDTO.setPushMessage(pushMessage);
                    // 配置通知栏图标
                    notification.setLogo(commonInfoService.findOne(19).getValue() + "/logo.png"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
                    // 配置通知栏网络图标
                    notification.setLogoUrl(commonInfoService.findOne(19).getValue() + "/logo.png");
                    notification.setTitle(title);
                    notification.setBody(content);
                    notification.setClickType("startapp");
                    notification.setUrl(commonInfoService.findOne(19).getValue());
                    notification.setChannelLevel("3");
                    pushMessage.setNotification(notification);

                    PushChannel pushChannel = new PushChannel();
                    AndroidDTO androidDTO = new AndroidDTO();
                    Ups ups = new Ups();
                    ThirdNotification thirdNotification = new ThirdNotification();
                    thirdNotification.setTitle(title);
                    thirdNotification.setBody(content);
                    thirdNotification.setClickType("startapp");
                    ups.setNotification(thirdNotification);
                    Map<String, Map<String, Object>> options = new HashMap<>();
                    Map<String, Object> hw = new HashMap<>();
                    hw.put("/message/android/notification/badge/class", "pages/index/index");
                    hw.put("/message/android/notification/badge/add_num", 1);
                    hw.put("/message/android/notification/badge/set_num", 1);

                    hw.put("/message/android/notification/image", commonInfoService.findOne(19).getValue() + "/logo.png");
                    hw.put("/message/android/notification/style", 0);
                    hw.put("/message/android/notification/big_title", title);
                    hw.put("/message/android/notification/big_body", content);
                    hw.put("/message/android/category", "IM");

                    hw.put("/message/android/notification/default_sound", false);
                    hw.put("/message/android/notification/sound", "");
                    hw.put("/message/android/notification/importance", "NORMAL");

                    hw.put("/message/android/ttl", "86400s");
                    options.put("HW", hw);

                    Map<String, Object> ry = new HashMap<>();
                    ry.put("/android/notification/badge/addNum", 1);
                    ry.put("/android/notification/badge/setNum", 1);
                    ry.put("/android/notification/badge/badgeClass", "pages/index/index");

//        ry.put("/android/notification/icon","https://zbb.dailianbao.net/logo.png");
                    ry.put("/android/notification/image", commonInfoService.findOne(19).getValue() + "/logo.png");
                    ry.put("/android/notification/style", "pages/index/index");
                    ry.put("/android/notification/importance", "NORMAL");
                    ry.put("/android/ttl", "86400s");
                    options.put("HO", ry);

                    Map<String, Object> xm = new HashMap<>();
                    xm.put("/extra.channel_id", 142840);
                    xm.put("/extra.locale", "zh_CN");
                    xm.put("/time_to_live", 86400000);
                    options.put("XM", xm);

                    Map<String, Object> OP = new HashMap<>();
                    Map<String, Object> OPG = new HashMap<>();
                    OP.put("/channel_id", "push_oplus_category_content");
                    OPG.put("/channel_id", "push_oplus_category_content");
                    OP.put("/category", "SOCIAL");
                    OP.put("/notify_level", 1);
                    OP.put("/app_message_id", UUID.randomUUID().toString());
                    OPG.put("/app_message_id", UUID.randomUUID().toString());
                    OP.put("/style", 1);
//                    OP.put("/small_picture_id",1);
                    OP.put("/off_line", true);
                    OP.put("/off_line_ttl", 86400);
                    OPG.put("/off_line", true);
                    OPG.put("/off_line_ttl", 86400);
                    options.put("OP", OP);
                    options.put("OPG", OPG);

                    Map<String, Object> VV = new HashMap<>();
                    VV.put("/category", "systemId");
                    VV.put("/notifyType", 4);
                    VV.put("/networkType", -1);
                    VV.put("/timeToLive", 86400);
                    options.put("VV", VV);

                    Map<String, Object> MZ = new HashMap<>();
                    MZ.put("/noticeMsgType", 0);
                    options.put("MZ", MZ);

                    ups.setOptions(options);
                    androidDTO.setUps(ups);
                    pushChannel.setAndroid(androidDTO);
                    HarmonyDTO harmony = new HarmonyDTO();
                    HarmonyNotification harmonyNotification = new HarmonyNotification();
                    harmonyNotification.setTitle(title);
                    harmonyNotification.setBody(content);
                    harmonyNotification.setClickType("startapp");
                    harmonyNotification.setCategory("IM");
                    harmony.setNotification(harmonyNotification);

                    Map<String, Map<String, Object>> harmonyOptions = new HashMap<>();
                    Map<String, Object> hwHarmony = new HashMap<>();
                    //{"addNum":1}
                    JSONObject badge = new JSONObject();
                    badge.put("addNum", 1);
                    hwHarmony.put("/payload/notification/badge", badge);
                    hwHarmony.put("/payload/notification/image", commonInfoService.findOne(19).getValue() + "/logo.png");
                    hwHarmony.put("/payload/notification/style", 0);
                    hwHarmony.put("/payload/notification/inboxContent", badge);
                    harmonyOptions.put("HW", hwHarmony);
                    harmony.setOptions(harmonyOptions);
                    pushChannel.setHarmony(harmony);
                    ;


                    pushDTO.setPushChannel(pushChannel);
                } else {
                    pushMessage.setTransmission(title);
                    pushDTO.setPushMessage(pushMessage);
                    PushChannel pushChannel = new PushChannel();
                    IosDTO iosDTO = new IosDTO();
                    Aps aps = new Aps();
                    Alert alert = new Alert();
                    alert.setTitle(title);
                    alert.setBody(content);
                    aps.setAlert(alert);
                    aps.setSound("default");
                    iosDTO.setAps(aps);
                    pushChannel.setIos(iosDTO);
                    pushDTO.setPushChannel(pushChannel);
                }
                // 设置接收人信息
                Audience audience = new Audience();
                audience.addCid(clientId);
                pushDTO.setAudience(audience);
                // 进行cid单推
                ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
                if (apiResult.isSuccess()) {
                    // success
                    log.error("消息推送成功：" + apiResult.getData());
                } else {
                    // failed
                    log.error("消息推送成功失败：code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息推送异常：" + e.getMessage(), e);
        }
    }

    /**
     * 师傅消息推送
     *
     * @param title    标题
     * @param content  内容
     * @param clientId 师傅客户端id
     */
    @Override
    public void pushToSingleQiShou(String title, String content, String clientId) {
        try {
            if (StringUtils.isNotEmpty(clientId)) {
                UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("client_rider_id", clientId));
                GtApiConfiguration apiConfiguration = new GtApiConfiguration();
                //填写应用配置
                apiConfiguration.setAppId(commonInfoService.findOne(290).getValue());
                apiConfiguration.setAppKey(commonInfoService.findOne(289).getValue());
                apiConfiguration.setMasterSecret(commonInfoService.findOne(291).getValue());
                // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
                apiConfiguration.setDomain("https://restapi.getui.com/v2/");
                // 实例化ApiHelper对象，用于创建接口对象
                ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
                // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
                PushApi pushApi = apiHelper.creatApi(PushApi.class);
                //根据cid进行单推
                PushDTO<Audience> pushDTO = new PushDTO<Audience>();
                // 设置推送参数
                pushDTO.setRequestId(System.currentTimeMillis() + "");
                PushMessage pushMessage = new PushMessage();
                if (userEntity == null || userEntity.getSysPhone() == null || userEntity.getSysPhone() == 1) {
                    //安卓推送
                    GTNotification notification = new GTNotification();
                    pushDTO.setPushMessage(pushMessage);
                    // 配置通知栏图标
                    notification.setLogo(commonInfoService.findOne(19).getValue() + "/logo.png"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
                    // 配置通知栏网络图标
                    notification.setLogoUrl(commonInfoService.findOne(19).getValue() + "/logo.png");
                    notification.setTitle(title);
                    notification.setBody(content);
                    notification.setClickType("startapp");
                    notification.setUrl(commonInfoService.findOne(19).getValue());
                    notification.setChannelLevel("3");
                    pushMessage.setNotification(notification);
                } else {
                    pushMessage.setTransmission(title);
                    pushDTO.setPushMessage(pushMessage);
                    PushChannel pushChannel = new PushChannel();
                    IosDTO iosDTO = new IosDTO();
                    Aps aps = new Aps();
                    Alert alert = new Alert();
                    alert.setTitle(title);
                    alert.setBody(content);
                    aps.setAlert(alert);
                    aps.setSound("default");
                    iosDTO.setAps(aps);
                    pushChannel.setIos(iosDTO);
                    pushDTO.setPushChannel(pushChannel);
                }
                // 设置接收人信息
                Audience audience = new Audience();
                audience.addCid(clientId);
                pushDTO.setAudience(audience);
                // 进行cid单推
                ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
                if (apiResult.isSuccess()) {
                    // success
                    log.error("消息推送成功：" + apiResult.getData());
                } else {
                    // failed
                    log.error("消息推送成功失败：code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息推送异常：" + e.getMessage(), e);
        }
    }


    @Override
    public Result loginApp(String phone, String password) {
        //md5加密
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserEntity userEntity = baseMapper.selectOne(queryWrapper);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(new Sha256Hash(password, userEntity.getSalt()).toHex())) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        userEntity.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(userEntity);
        return getResult(userEntity);

    }


    @Override
    public Result forgetPwd(String pwd, String phone, String msg) {
        try {
            Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, msg);
            //校验短信验证码
            if (byPhoneAndCode == null) {
                return Result.error("验证码不正确");
            }
            UserEntity userByPhone = queryByPhone(phone);
            String salt = RandomStringUtils.randomAlphanumeric(20);
            userByPhone.setPassword(new Sha256Hash(pwd, salt).toHex());
            userByPhone.setSalt(salt);
            msgDao.deleteById(byPhoneAndCode.getId());
            baseMapper.updateById(userByPhone);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("服务器内部错误");
        }
    }


    @Override
    public Result selectUserId(String invitationCode) {
        UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", invitationCode));
        if (userEntity.getUserId() == null) {
            return Result.error("该用户不存在！");
        }
        return Result.success().put("data", userEntity.getUserId());
    }

    @Override
    public List<UserEntity> getNearByRider(Double range, Double lng, Double lat) {

        return baseMapper.getNearByRider(range, lng, lat);


    }

    @Override
    public int updateMoney(Integer type, Long userId, BigDecimal money) {

        return baseMapper.updateMoney(type, userId, money);
    }

    @Override
    public HashMap<String, Object> getExtensionData(Long userId) {
        UserEntity userEntity = baseMapper.selectById(userId);
        HashMap<String, Object> map = new HashMap<>();
        Long countChildren = baseMapper.selectCount(new QueryWrapper<UserEntity>().eq("inviter_code", userEntity.getInvitationCode()));
        Long countChildrenChildren = baseMapper.selectCount(new QueryWrapper<UserEntity>().eq("inviter_inviter_code", userEntity.getInvitationCode()));
        BigDecimal oneMoney = userMoneyDetailsService.getOneProfit(userId, 1);
        BigDecimal twoMoney = userMoneyDetailsService.getOneProfit(userId, 2);
        map.put("countChildren", countChildren);
        map.put("countChildrenChildren", countChildrenChildren);
        map.put("totalCount",countChildren + countChildrenChildren);
        map.put("extensionProfit", oneMoney.add(twoMoney));
        map.put("oneMoney", oneMoney);
        map.put("twoMoney", twoMoney);
        return map;
    }

    @Override
    public IPage<UserEntity> getExtensionList(Long userId, Integer type, Integer page, Integer limit) {
        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity loginEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        if (loginEntity != null) {
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
            if (type == 1) {
                queryWrapper.eq("inviter_code", loginEntity.getInvitationCode());
//                return userService.page(pages, new QueryWrapper<UserEntity>().eq("inviter_code", loginEntity.getInvitationCode()));
            } else if (type == 2) {
                queryWrapper.eq("inviter_inviter_code", loginEntity.getInvitationCode());
//                return userService.page(pages, new QueryWrapper<UserEntity>().eq("inviter_inviter_code", loginEntity.getInvitationCode()));
            } else {
                // 如果type为null，返回一级和二级团队成员
                queryWrapper.or().eq("inviter_code", loginEntity.getInvitationCode())
                        .or().eq("inviter_inviter_code", loginEntity.getInvitationCode());
//                return null;
            }
            IPage<UserEntity> userEntityIPage = baseMapper.selectPage(pages, queryWrapper);
            return userEntityIPage;
        } else {
            return null;
        }
    }

    @Override
    public IPage<UserEntity> getAuthExtensionList(Long userId, Integer page, Integer limit) {

        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity loginEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        return baseMapper.getAuthExtensionList(pages, loginEntity.getInvitationCode());
    }

    @Override
    public IPage<HashMap<String, Object>> getProfitList(Long userId, Integer type, Integer page, Integer limit) {

        Page<HashMap<String, Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return userMoneyDetailsDao.getProfitList(pages, userId, type);
    }

    @Override
    public IPage<HashMap<String, Object>> getUserIncomeList(Long userId, Integer type, Integer page, Integer limit) {
        Page<HashMap<String, Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity userEntity = baseMapper.selectById(userId);
        return baseMapper.getUserIncomeList(pages, type, userEntity.getInvitationCode(), userEntity.getUserId());
    }


}