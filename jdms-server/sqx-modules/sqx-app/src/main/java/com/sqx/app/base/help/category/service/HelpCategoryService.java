package com.sqx.app.base.help.category.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryInfoBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryPageBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategorySaveBo;
import com.sqx.app.base.help.category.domain.bo.HelpCategoryUpdateBo;
import com.sqx.app.base.help.category.domain.vo.HelpCategoryVo;

import java.util.List;

/**
 * 帮助中心分类 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:43:39
 */
public interface HelpCategoryService {

    void saveInfo(HelpCategorySaveBo helpCategorySaveBo);

    IPage<HelpCategoryVo> pageInfo(HelpCategoryPageBo helpCategoryPageBo);

    HelpCategoryVo info(HelpCategoryInfoBo helpCategoryInfoBo);

    void updateInfo(HelpCategoryUpdateBo helpCategoryUpdateBo);

    void delete(HelpCategoryInfoBo helpCategoryInfoBo);

    List<HelpCategoryVo> listInfo();
}
