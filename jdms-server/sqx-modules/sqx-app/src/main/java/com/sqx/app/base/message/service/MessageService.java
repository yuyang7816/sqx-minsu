package com.sqx.app.base.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.app.base.message.entity.MessageInfo;
import com.sqx.app.base.utils.Result;

public interface MessageService extends IService<MessageInfo> {

    IPage<MessageInfo> selectMessageList(Integer page, Integer limit, MessageInfo messageInfo);

    int saveBody(MessageInfo messageInfo);

    int update(MessageInfo messageInfo);

    int delete(Long id);

    MessageInfo selectMessageById(Long id);

    int updateSendState(Long userId);


    Result getOrderMessage(Long userId);
}
