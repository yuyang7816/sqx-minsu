package com.sqx.app.base.cancelAccount.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-08-07 09:31:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CancelAccountPageBo extends PageParam {
    @Schema(description = "状态: 1申请中 2已注销 3撤销申请")
    private Integer status;
}
