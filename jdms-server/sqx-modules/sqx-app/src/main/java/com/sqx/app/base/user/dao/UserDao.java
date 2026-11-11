package com.sqx.app.base.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {


    IPage<UserEntity> selectUserPage(Page<UserEntity> page,
                                     @Param("search") String search,
                                     @Param("userName")String userName,
                                     @Param("sex")Integer sex,
                                     @Param("platform")String platform,
                                     @Param("sysPhone")String sysPhone,
                                     @Param("status")Integer status,
                                     @Param("isAgent")Integer isAgent);

    int queryInviterCount(@Param("inviterCode") String inviterCode);

    int queryUserCount(@Param("type") int type, @Param("date") String date);

    Double queryPayMoney(@Param("type") int type, @Param("date") String date);

    List<UserEntity> selectRiderUserByOnLineFlag(@Param("distance")String distance,
                                                 @Param("lng")String lng,
                                                 @Param("lat")String lat);

    IPage<Map<String, Object>> queryCourseOrder(Page iPage,
                                                @Param("type") int type,
                                                @Param("date") String date);

    int userMessage(@Param("date")String date,
                    @Param("type")int type);






    /**
     * 按日期统计新增用户
     */
    List<Map<String, Object>> selectNewUserTrend(@Param("startTime") String startTime, @Param("endTime") String endTime);

    int updateCashDeposit(@Param("userId") Long userId,@Param("cashDeposit") Double cashDeposit);

    int updateMoney(@Param("type")Integer type,
                    @Param("userId")Long userId,
                    @Param("money")BigDecimal money);



    int deleteCertification(@Param("userId")Long userId);


    List<UserEntity> getNearByRider(@Param("range")Double range,
                                    @Param("lng")Double lng,
                                    @Param("lat")Double lat);

    IPage<UserEntity> getUserList(@Param("pages") Page<UserEntity> pages, @Param("shopId") Long shopId, @Param("userEntity") UserEntity userEntity);

    IPage<UserEntity> getAuthExtensionList(@Param("pages") Page<UserEntity> pages,
                                           @Param("invitationCode") String invitationCode);

    IPage<HashMap<String, Object>> getUserIncomeList(@Param("pages") Page<HashMap<String, Object>> pages,
                                                     @Param("type") Integer type,
                                                     @Param("invitationCode") String invitationCode,
                                                     @Param("userId") Long userId);
}
