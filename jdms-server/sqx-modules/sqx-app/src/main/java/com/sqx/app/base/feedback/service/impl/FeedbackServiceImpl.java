package com.sqx.app.base.feedback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.app.base.feedback.db.service.IFeedbackService;
import com.sqx.app.base.feedback.domain.bo.FeedbackInfoBo;
import com.sqx.app.base.feedback.domain.bo.FeedbackPageBo;
import com.sqx.app.base.feedback.domain.bo.FeedbackSaveBo;
import com.sqx.app.base.feedback.domain.bo.FeedbackUpdateBo;
import com.sqx.app.base.feedback.domain.entity.Feedback;
import com.sqx.app.base.feedback.domain.vo.FeedbackVo;
import com.sqx.app.base.feedback.service.FeedbackService;
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
 * 意见反馈 服务实现类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-06 15:53:00
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Resource
    private IFeedbackService iFeedbackService;

    @Override
    public void saveInfo(FeedbackSaveBo feedbackSaveBo) {
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(feedbackSaveBo, feedback);

        iFeedbackService.save(feedback);
    }

    @Override
    public IPage<FeedbackVo> pageInfo(FeedbackPageBo feedbackPageBo) {
        Page<Feedback> initPage = PageUtil.initPage(feedbackPageBo.getPage(), feedbackPageBo.getLimit());

        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .orderByDesc(Feedback::getCreateTime);

        IPage<Feedback> iPage = iFeedbackService.page(initPage, queryWrapper);
        List<FeedbackVo> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (Feedback record : iPage.getRecords()) {
                FeedbackVo vo = new FeedbackVo();
                BeanUtils.copyProperties(record, vo);

                voList.add(vo);
            }
        }

        IPage<FeedbackVo> voIPage = PageUtil.copyPage(iPage, voList);
        return voIPage;
    }

    @Override
    public FeedbackVo info(FeedbackInfoBo feedbackInfoBo) {
        Feedback feedback = iFeedbackService.getById(feedbackInfoBo.getId());
        if (Objects.isNull(feedback)) {
            throw new BizException("信息不存在");
        }

        FeedbackVo vo = new FeedbackVo();
        BeanUtils.copyProperties(feedback, vo);
        return vo;
    }

    @Override
    public void updateInfo(FeedbackUpdateBo feedbackUpdateBo) {
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(feedbackUpdateBo, feedback);

        iFeedbackService.updateById(feedback);
    }

    @Override
    public void delete(FeedbackInfoBo feedbackInfoBo) {
        iFeedbackService.removeById(feedbackInfoBo.getId());
    }
}
