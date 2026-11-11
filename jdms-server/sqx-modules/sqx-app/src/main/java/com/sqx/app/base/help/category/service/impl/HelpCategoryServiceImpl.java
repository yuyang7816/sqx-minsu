package com.sqx.app.base.help.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.help.article.db.service.IHelpArticleService;
import com.sqx.app.base.help.article.domain.entity.HelpArticle;
import com.sqx.app.base.help.category.db.service.IHelpCategoryService;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryInfoBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryPageBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategorySaveBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryUpdateBo;
import com.sqx.app.base.help.category.domain.entity.HelpCategory;
import com.sqx.app.base.help.category.domain.vo.HelpCategoryVo;
import com.sqx.app.base.help.category.service.HelpCategoryService;
import com.sqx.common.core.exception.BizException;
import com.sqx.common.mybatis.utils.PageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帮助中心分类 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:43:39
 */
@Service
public class HelpCategoryServiceImpl implements HelpCategoryService {

    @Resource
    private IHelpCategoryService iHelpCategoryService;
    @Resource
    private IHelpArticleService iHelpArticleService;

    @Override
    public void saveInfo(HelpCategorySaveBo helpCategorySaveBo) {
        if (checkExistsByName(helpCategorySaveBo.getName())) {
            throw new BizException("分类名称已存在");
        }

        HelpCategory helpCategory = new HelpCategory();
        BeanUtils.copyProperties(helpCategorySaveBo, helpCategory);

        iHelpCategoryService.save(helpCategory);
    }

    private boolean checkExistsByName(String name) {
        LambdaQueryWrapper<HelpCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HelpCategory::getName, name);

        return iHelpCategoryService.exists(queryWrapper);
    }

    @Override
    public IPage<HelpCategoryVo> pageInfo(HelpCategoryPageBo helpCategoryPageBo) {
        Page<HelpCategory> initPage = PageUtil.initPage(helpCategoryPageBo.getPage(), helpCategoryPageBo.getLimit());

        QueryWrapper<HelpCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(helpCategoryPageBo.getClientType()), HelpCategory::getClientType, helpCategoryPageBo.getClientType())
                .like(StringUtils.isNotBlank(helpCategoryPageBo.getName()), HelpCategory::getName, helpCategoryPageBo.getName())
                .orderByAsc(HelpCategory::getSort);

        IPage<HelpCategory> iPage = iHelpCategoryService.page(initPage, queryWrapper);
        List<HelpCategoryVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (HelpCategory record : iPage.getRecords()) {
                HelpCategoryVo vo = new HelpCategoryVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<HelpCategoryVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public HelpCategoryVo info(HelpCategoryInfoBo helpCategoryInfoBo) {
        HelpCategory helpCategory = iHelpCategoryService.getById(helpCategoryInfoBo.getId());
        if (Objects.isNull(helpCategory)) {
            throw new BizException("信息不存在");
        }

        HelpCategoryVo vo = new HelpCategoryVo();
        BeanUtils.copyProperties(helpCategory, vo);
        return vo;
    }

    @Override
    public void updateInfo(HelpCategoryUpdateBo helpCategoryUpdateBo) {
        if (checkExistsByName(helpCategoryUpdateBo.getName())) {
            throw new BizException("分类名称已存在");
        }

        HelpCategory helpCategory = new HelpCategory();
        BeanUtils.copyProperties(helpCategoryUpdateBo, helpCategory);

        iHelpCategoryService.updateById(helpCategory);
    }

    @Override
    public void delete(HelpCategoryInfoBo helpCategoryInfoBo) {
        iHelpCategoryService.removeById(helpCategoryInfoBo.getId());
    }

    @Override
    public List<HelpCategoryVo> listInfo() {
        LambdaQueryWrapper<HelpCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(HelpCategory::getSort);
        List<HelpCategory> categoryList = iHelpCategoryService.list(queryWrapper);
        if (CollectionUtils.isEmpty(categoryList)) {
            return Collections.emptyList();
        }

        List<Long> categoryIdList = categoryList.stream().map(HelpCategory::getId).collect(Collectors.toList());
        List<HelpArticle> helpArticleList = iHelpArticleService.list(new LambdaQueryWrapper<HelpArticle>()
                .in(HelpArticle::getCategoryId, categoryIdList)
                .orderByAsc(HelpArticle::getSort)
        );

        Map<Long, List<HelpArticle>> articleMap = helpArticleList
                .stream()
                .collect(Collectors.groupingBy(HelpArticle::getCategoryId));

        List<HelpCategoryVo> voList = new ArrayList<>();
        for (HelpCategory category : categoryList) {
            HelpCategoryVo vo = new HelpCategoryVo();
            BeanUtils.copyProperties(category, vo);

            vo.setArticleList(articleMap.getOrDefault(category.getId(), Collections.emptyList()));
            voList.add(vo);
        }

        return voList;
    }
}
