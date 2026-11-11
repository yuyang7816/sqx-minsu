package com.sqx.app.base.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户信息返回实体")
public class UserMessageResponse implements Serializable {

    /**
     * 总人数
     */
    @Schema(description = "总人数")
    private int totalNumber;

    /**
     * 普通用户人数
     */
    @Schema(description = "普通用户人数")
    private int userNumber;

    /**
     * 会员人数
     */
    @Schema(description = "会员人数")
    private int vipUserNumber;
}
