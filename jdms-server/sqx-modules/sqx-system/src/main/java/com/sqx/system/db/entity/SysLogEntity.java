package com.sqx.system.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统日志
 */
@Data
@TableName("sys_log")
@Schema(description = "系统日志")
public class SysLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    @Schema(description = "id")
    private Long id;

    //用户名
    @Schema(description = "用户名")
    private String username;

    //用户操作
    @Schema(description = "用户操作")
    private String operation;

    //请求方法
    @Schema(description = "请求方法")
    private String method;

    //请求参数
    @Schema(description = "请求参数")
    private String params;

    //执行时长(毫秒)
    @Schema(description = "执行时长(毫秒)")
    private Long time;

    //IP地址
    @Schema(description = "IP地址")
    private String ip;

    //创建时间
    @Schema(description = "创建时间")
    private Date createDate;

}
