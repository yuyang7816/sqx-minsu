package com.sqx.app.base.help.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.help.article.domain.bo.HelpArticleInfoBo;
import com.sqx.app.base.help.article.domain.bo.HelpArticlePageBo;
import com.sqx.app.base.help.article.domain.bo.HelpArticleSaveBo;
import com.sqx.app.base.help.article.domain.bo.HelpArticleUpdateBo;
import com.sqx.app.base.help.article.domain.vo.HelpArticleVo;

/**
 * 帮助中心文章 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:44:06
 */
public interface HelpArticleService {

    void saveInfo(HelpArticleSaveBo helpArticleSaveBo);

    IPage<HelpArticleVo> pageInfo(HelpArticlePageBo helpArticlePageBo);

    HelpArticleVo info(HelpArticleInfoBo helpArticleInfoBo);

    void updateInfo(HelpArticleUpdateBo helpArticleUpdateBo);

    void delete(HelpArticleInfoBo helpArticleInfoBo);
}
