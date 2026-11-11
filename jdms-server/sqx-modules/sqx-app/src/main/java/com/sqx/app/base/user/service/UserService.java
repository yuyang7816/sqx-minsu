package com.sqx.app.base.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.utils.Result;
import com.sqx.common.mybatis.utils.PageUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return
     */
    UserEntity queryByPhone(String phone);

    /**
     * 根据手机号和用户类型查询用户
     * @param phone  手机号
     * @return
     */
    UserEntity queryByPhoneAndType(String phone);

    /**
     * 根据小程序微信openId查询用户
     *
     * @param openId 微信小程序openId
     * @return
     */
    UserEntity queryByOpenId(String openId,Integer userType);

    /**
     * 根据微信APP openId查询用户
     *
     * @param openId 微信APP openId
     * @return
     */
    UserEntity queryByWxOpenId(String openId,Integer userType);

    /**
     * 根据userId查询用户
     *
     * @param userId userId
     * @return
     */
    UserEntity queryByUserId(Long userId);

    UserEntity queryByInvitationCode(String invitationCode);

    /**
     * 根据用户appleId查询用户
     *
     * @param appleId
     * @return
     */
    UserEntity queryByAppleId(String appleId);


    Result wxLogin(String code,Integer type);

    /**
     * 注册或更新用户信息
     *
     * @param userInfo1 用户信息
     * @return 用户信息
     */
    Result wxRegister(UserEntity userInfo1);

    /**
     * 注册或更新用户信息
     *
     * @param appleId 苹果账号id
     * @return 用户信息
     */
    Result iosRegister(String appleId);

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param state 验证码类型
     * @return
     */
    Result sendMsg(String phone, String state);

    /**
     * 绑定手机号
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone);

    /**
     * @param phone
     * @param code
     * @param appleId
     * @param platform
     * @param sysPhone
     * @return
     */
    Result iosBindMobile(String phone, String code, String appleId, String platform, Integer sysPhone);

    /**
     * 换绑手机号
     *
     * @param phone  手机号
     * @param msg    验证码
     * @param userId 用户id
     * @return
     */
    Result updatePhone(String phone, String msg, Long userId);

    /**
     * 登录token
     *
     * @param user 用户信息
     * @return
     */
    Result getResult(UserEntity user);

    /**
     * app注册或h5注册
     *
     * @param phone    手机号
     * @param msg      验证按
     * @param
     * @param platform 来源 app  h5
     * @return
     */
    Result registerCode(String phone, String msg, String platform, Integer sysPhone,String password,Integer type,String openId,String inviterCode,String userName);


    Result wxOpenIdLogin(String openId, Integer userType);

    Result wxAppLogin(String wxOpenId, String token);


    /**
     * app或h5登录
     *
     * @param phone 手机号
     * @param pwd   密码
     * @return
     */
    Result login(String phone, String pwd);


    /**
     * 根据 code 获取openId
     *
     * @param code
     * @param userId
     * @return
     */
    Result getOpenId(String code, Long userId);


    /**
     * 根据用户id查询用户
     *
     * @param userId 用户id
     * @return
     */
    UserEntity selectUserById(Long userId);

    void pushToSingle(String title, String content, String clientId);

    void pushToSingleQiShou(String title, String content, String clientId);

    PageUtils selectUserPage(Integer page, Integer limit, String search,String userName, Integer sex, String platform, String sysPhone, Integer status, Integer isAgent);

    int queryInviterCount(String inviterCode);


    int queryUserCount(int type, String date);

    Double queryPayMoney(int type);

    IPage<Map<String, Object>> queryCourseOrder(Page<Map<String, Object>> iPage, int type, String date);

    int userMessage(String date, int type);

    /**
     * 按日期统计新增用户
     */
    List<Map<String, Object>> selectNewUserTrend(String startTime, String endTime);

    Result loginApp(String phone, String password);

    Result forgetPwd(String pwd, String phone, String msg);

    Result selectUserId(String invitationCode);

    IPage<UserEntity> getExtensionList(Long userId, Integer type, Integer page, Integer limit);

    List<UserEntity> getNearByRider(Double range, Double lng, Double lat);

    int updateMoney(Integer type, Long userId, BigDecimal moneyd);

    HashMap<String ,Object> getExtensionData(Long userId);

    IPage<UserEntity> getAuthExtensionList(Long userId, Integer page, Integer limit);

    IPage<HashMap<String, Object>> getProfitList(Long userId,Integer type, Integer page, Integer limit);

    IPage<HashMap<String, Object>> getUserIncomeList(Long userId, Integer type, Integer page, Integer limit);


}

