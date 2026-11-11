package com.sqx.app.base.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "课程订单信息（暂未使用）")
public class CourseOrderResponse implements Serializable {
    /**
     * 课程名称
     */
    private  String  coursename;
    /**
     * 售卖笔数
     */
    private  int  coursenum;
    /**
     * 售卖金额
     */
    private  Double coursemoney;
}
