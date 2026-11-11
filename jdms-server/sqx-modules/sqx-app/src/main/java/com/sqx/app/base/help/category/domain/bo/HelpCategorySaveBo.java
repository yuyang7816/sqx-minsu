package com.sqx.app.base.help.category.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 西安省钱兄网络科技有限公司
 * @since 2025-09-30 13:43:39
 */
@Data
public class HelpCategorySaveBo {

    @Schema(description = "名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Schema(description = "客户端类型 1: 用户端 2:回收员端")
    private Integer clientType;

    @Schema(description = "排序")
    private Integer sort;
}
