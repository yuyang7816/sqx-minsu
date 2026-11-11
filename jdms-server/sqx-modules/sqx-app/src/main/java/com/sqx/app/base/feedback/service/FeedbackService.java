package com.sqx.app.base.feedback.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.app.base.feedback.domain.bo.FeedbackInfoBo;
import com.sqx.app.base.feedback.domain.bo.FeedbackPageBo;
import com.sqx.app.base.feedback.domain.bo.FeedbackSaveBo;
import com.sqx.app.base.feedback.domain.bo.FeedbackUpdateBo;
import com.sqx.app.base.feedback.domain.vo.FeedbackVo;

/**
 * 意见反馈 服务类
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-06 15:53:00
 */
public interface FeedbackService {

    void saveInfo(FeedbackSaveBo feedbackSaveBo);

    IPage<FeedbackVo> pageInfo(FeedbackPageBo feedbackPageBo);

    FeedbackVo info(FeedbackInfoBo feedbackInfoBo);

    void updateInfo(FeedbackUpdateBo feedbackUpdateBo);

    void delete(FeedbackInfoBo feedbackInfoBo);
}
