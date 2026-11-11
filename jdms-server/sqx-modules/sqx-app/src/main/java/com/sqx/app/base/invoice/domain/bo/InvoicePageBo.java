package com.sqx.app.base.invoice.domain.bo;

import com.sqx.common.mybatis.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chengming
 * @since 2025/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoicePageBo extends PageParam {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "类型 1个人 2公司")
    private Integer type;

    @Schema(description = "状态 1待开票 2已开票")
    private Integer status;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "纳税人识别号")
    private String creditCode;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "开户行")
    private String openBank;

    @Schema(description = "银行账号")
    private String bankNumber;

    @Schema(description = "接收邮箱")
    private String receiveEmail;

    @Schema(description = "接收手机号")
    private String receivePhone;

}
