package com.sqx.app.base.pay.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.pay.entity.PayClassify;

public interface PayClassifyService extends IService<PayClassify> {


    IPage<PayClassify> selectPayClassifyList(Integer page, Integer limit);
}
