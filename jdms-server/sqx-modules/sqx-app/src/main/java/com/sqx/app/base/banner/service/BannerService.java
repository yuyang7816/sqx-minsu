package com.sqx.app.base.banner.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.banner.entity.Banner;
import com.sqx.app.base.utils.Result;
import com.sqx.common.mybatis.utils.PageUtils;

import java.util.List;

public interface BannerService extends IService<Banner> {

    List<Banner> selectBannerList(Integer classify);

    List<Banner> selectBannerLists(Integer classify);

    int saveBody(String image, String url, Integer sort);

    Banner selectBannerById(Long id);

    Result updateBannerStateById(Long id);

    int updateBannerById(Banner banner);

    int insertBanner(Banner banner);

    PageUtils selectPage(Integer page, Integer limit, Integer classify, Integer state);

}
