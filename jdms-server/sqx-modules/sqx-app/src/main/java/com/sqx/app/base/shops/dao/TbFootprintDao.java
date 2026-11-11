package com.sqx.app.base.shops.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.app.base.shops.entity.TbFootprint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Admin
* @description 针对表【tb_footprint】的数据库操作Mapper
* @createDate 2024-07-05 15:36:56
* @Entity generator.domain.TbFootprint
*/
@Mapper
public interface TbFootprintDao extends BaseMapper<TbFootprint> {

    List<TbFootprint> selectByUserId(@Param("userId") Long userId,
                                     @Param("status")Integer status);

    int selectCountByGoodId(Long userId);

    /**
     * 按酒店去重统计用户足迹数量
     */
    @Select("SELECT COUNT(DISTINCT shop_id) FROM tb_footprint WHERE user_id = #{userId}")
    Long countDistinctShopByUserId(@Param("userId") Long userId);
}
