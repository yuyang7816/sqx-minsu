package com.sqx.app.base.oss.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 文件上传
 *
 */
@Data
@TableName("sys_oss")
@Schema(description = "文件上传")
public class SysOssEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@TableId
	@Schema(description = "文件上传id")
	private Long id;

	//URL地址
	@Schema(description = "URL地址")
	private String url;

	//创建时间
	@Schema(description = "创建时间")
	private Date createDate;

}
