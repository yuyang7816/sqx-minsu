package com.sqx.app.base.feedback.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-06 15:53:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FeedbackPageBo extends PageParam {

    private Long userId;
}
