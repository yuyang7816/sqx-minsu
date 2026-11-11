package com.sqx.common.mybatis.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author chengming
 * @since 2024/9/11
 */
@Data
public class PageParam {

    /**
     * 当前页
     */
    @TableField(exist = false)
    private Long page;

    /**
     * 页大小
     */
    @TableField(exist = false)
    private Long limit;
}
