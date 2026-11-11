package com.sqx.app.base.banner.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.banner.dao.BannerDao;
import com.sqx.app.base.banner.entity.Banner;
import com.sqx.app.base.banner.service.BannerService;
import com.sqx.app.base.utils.Result;
import com.sqx.common.mybatis.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * banner图
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerDao, Banner> implements BannerService {

    @Autowired
    private BannerDao bannerDao;


    @Override
    public List<Banner> selectBannerList(Integer classify) {
        return bannerDao.selectList(classify);
    }

    @Override
    public List<Banner> selectBannerLists(Integer classify) {
        return bannerDao.selectLists(classify);
    }

    @Override
    public PageUtils selectPage(Integer page, Integer limit, Integer classify, Integer state){
        IPage<Banner> pages=new Page<>(page,limit);
        return new PageUtils(bannerDao.selectPage(pages,classify,state));
    }


    @Override
    public int saveBody(String image, String url, Integer sort) {
        Banner banner = new Banner();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        banner.setImageUrl(image);
        banner.setCreateTime(sdf.format(now));
        banner.setState(1);
        banner.setUrl(url);
        banner.setSort(sort == null ? 1 : sort);
        return bannerDao.insert(banner);
    }

    @Override
    public int insertBanner(Banner banner) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        banner.setCreateTime(sdf.format(now));
        if (banner.getState() == null) {
            banner.setState(1);
        }
        return bannerDao.insert(banner);
    }


    @Override
    public Banner selectBannerById(Long id) {
        return bannerDao.selectById(id);
    }

    @Override
    public Result updateBannerStateById(Long id) {
        Banner banner = selectBannerById(id);
        if (banner != null) {
            if (banner.getState() == 1) {
                banner.setState(2);
            } else {
                banner.setState(1);
            }
            bannerDao.updateById(banner);
            return Result.success();
        } else {
            return Result.error("修改对象为空！");
        }
    }

    @Override
    public int updateBannerById(Banner banner) {
        return bannerDao.updateById(banner);
    }


}
