package com.sqx.app.base.feedback.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.feedback.db.mapper.FeedbackMapper;
import com.sqx.app.base.feedback.db.service.IFeedbackService;
import com.sqx.app.base.feedback.domain.entity.Feedback;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 意见反馈 服务实现类
 * </p>
 *
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-06 15:53:00
 */
@Service
public class IFeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

}
