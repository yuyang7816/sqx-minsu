package com.sqx.app.base.help.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.help.article.db.service.IHelpArticleService;
import com.sqx.app.base.help.article.domain.bo.HelpArticleInfoBo;
import com.sqx.app.base.help.article.domain.bo.HelpArticlePageBo;
import com.sqx.app.base.help.article.domain.bo.HelpArticleSaveBo;
import com.sqx.app.base.help.article.domain.bo.HelpArticleUpdateBo;
import com.sqx.app.base.help.article.domain.entity.HelpArticle;
import com.sqx.app.base.help.article.domain.vo.HelpArticleVo;
import com.sqx.app.base.help.article.service.HelpArticleService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 帮助中心文章 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:44:06
 */
@Service
public class HelpArticleServiceImpl implements HelpArticleService {

    @Resource
    private IHelpArticleService iHelpArticleService;

    @Override
    public void saveInfo(HelpArticleSaveBo helpArticleSaveBo) {
        HelpArticle helpArticle = new HelpArticle();
        BeanUtils.copyProperties(helpArticleSaveBo, helpArticle);

        iHelpArticleService.save(helpArticle);
    }

    @Override
    public IPage<HelpArticleVo> pageInfo(HelpArticlePageBo helpArticlePageBo) {
        Page<HelpArticle> initPage = PageUtil.initPage(helpArticlePageBo.getPage(), helpArticlePageBo.getLimit());

        QueryWrapper<HelpArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .orderByDesc(HelpArticle::getCreateTime);

        IPage<HelpArticle> iPage = iHelpArticleService.page(initPage, queryWrapper);
        List<HelpArticleVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (HelpArticle record : iPage.getRecords()) {
                HelpArticleVo vo = new HelpArticleVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<HelpArticleVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public HelpArticleVo info(HelpArticleInfoBo helpArticleInfoBo) {
        HelpArticle helpArticle = iHelpArticleService.getById(helpArticleInfoBo.getId());
        if (Objects.isNull(helpArticle)) {
            throw new BizException("信息不存在");
        }

        HelpArticleVo vo = new HelpArticleVo();
        BeanUtils.copyProperties(helpArticle, vo);
        return vo;
    }

    @Override
    public void updateInfo(HelpArticleUpdateBo helpArticleUpdateBo) {
        HelpArticle helpArticle = new HelpArticle();
        BeanUtils.copyProperties(helpArticleUpdateBo, helpArticle);

        iHelpArticleService.updateById(helpArticle);
    }

    @Override
    public void delete(HelpArticleInfoBo helpArticleInfoBo) {
        iHelpArticleService.removeById(helpArticleInfoBo.getId());
    }
}
