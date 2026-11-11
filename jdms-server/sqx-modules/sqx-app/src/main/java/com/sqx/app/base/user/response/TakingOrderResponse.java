package com.sqx.app.base.user.response;

import lombok.Data;

import java.io.Serializable;

@Data
// 暂未使用
public class TakingOrderResponse implements Serializable {

  private  int orderNumber;
  private  String gameName;
  private  double  payMoney;







}
