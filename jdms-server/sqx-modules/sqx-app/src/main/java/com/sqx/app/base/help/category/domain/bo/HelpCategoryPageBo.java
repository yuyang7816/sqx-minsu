package com.sqx.app.base.help.category.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:43:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HelpCategoryPageBo extends PageParam {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "客户端类型 1: 用户端 2:回收员端")
    private Integer clientType;
}
