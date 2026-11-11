package com.sqx.app.base.shops.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.base.shops.entity.ScFollow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScFollowDao extends BaseMapper<ScFollow> {



    int getMyFollowCount(Long userId);

    int getMyFollowGoodsCount(Long userId);

//    Page<Goods> getFollowGoodsList(@Param("pages") Page<GoodsVo> pages,
//                                   @Param("userId") Long userId,
//                                   @Param("follow") ScFollow follow);

    List<ScFollow> selectByUserId(Long userId);

    List<ScFollow> selectByFanId(Long userId);

/*    Page<Trends> selectByMyCollect(@Param("pages") Page<GoodsVo> pages,
                                   @Param("userId") Long userId,
                                   @Param("follow") ScFollow follow);*/
}
