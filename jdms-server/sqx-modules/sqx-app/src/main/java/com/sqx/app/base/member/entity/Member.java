package com.sqx.app.base.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * 会员特权
 *
 * @author wuChang
 * @since 2023-12-21
 */
@Data
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员特权id
     */
    @Schema(description = "会员特权id")
    @TableId(value = "member_id", type = IdType.AUTO)
    private Long memberId;

    /**
     * 特权图标
     */
    @Schema(description = "特权图标")
    private String memberImg;

    /**
     * 特权名称
     */
    @Schema(description = "特权名称")
    private String memberName;
    /**
     * 特权描述
     */
    @Schema(description = "特权描述")
    private String memberContent;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private String sort;


}
