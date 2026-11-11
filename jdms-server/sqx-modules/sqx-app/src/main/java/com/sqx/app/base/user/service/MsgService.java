package com.sqx.app.base.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.user.entity.Msg;

/**
 * 验证码
 *
 */
public interface MsgService extends IService<Msg> {

	Msg findByPhone(String phone);

	Msg findByPhoneAndCode(String phone, String msg);

}
