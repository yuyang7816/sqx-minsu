package com.sqx.app.base.orders.vo;

import com.sqx.app.base.orders.entity.Orders;
import com.sqx.app.base.shops.entity.Shops;
import lombok.Data;

import java.io.Serializable;
@Data
public class OrderVo extends Orders implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 配置名称(多个)
     */
    private String classifyName;

    /**
     * 酒店信息
     */
    private Shops shop;
}
