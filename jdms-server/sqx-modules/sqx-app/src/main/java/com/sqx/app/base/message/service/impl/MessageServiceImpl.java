package com.sqx.app.base.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.base.common.entity.CommonInfo;
import com.sqx.app.base.common.service.CommonInfoService;
import com.sqx.app.base.message.dao.MessageInfoDao;
import com.sqx.app.base.message.entity.MessageInfo;
import com.sqx.app.base.message.service.MessageService;
import com.sqx.app.base.user.dao.UserDao;
import com.sqx.app.base.user.entity.UserEntity;
import com.sqx.app.base.user.service.UserService;
import com.sqx.app.base.utils.HttpClientUtil;
import com.sqx.app.base.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageInfoDao, MessageInfo> implements MessageService {

    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommonInfoService commonInfoService;
    @Override
    public IPage<MessageInfo> selectMessageList(Integer page, Integer limit, MessageInfo messageInfo) {
        Page<MessageInfo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        if (StringUtils.isNotBlank(messageInfo.getUserId())) {
            baseMapper.updateSendState(Long.valueOf(messageInfo.getUserId()));
        }
        IPage<MessageInfo> selectPage = baseMapper.selectPage(pages, new QueryWrapper<>(messageInfo).orderByDesc("create_at"));
        for (MessageInfo record : selectPage.getRecords()) {
            if (("8".equals(record.getState()))) {
                record.setUserEntity(userDao.selectById(record.getUserId()));
            }
        }
        return selectPage;
    }

    @Override
    public int saveBody(MessageInfo messageInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        UserEntity userEntity = userDao.selectById(messageInfo.getUserId());
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setCreateAt(sdf.format(now));
        if ("9".equals(messageInfo.getState())) {
            CommonInfo one = commonInfoService.findOne(128);
            String url = "https://apis.map.qq.com/ws/geocoder/v1/";
            Map<String, String> maps = new HashMap<>();
            maps.put("location", messageInfo.getPlatform() + "," + messageInfo.getType());
            maps.put("key", one.getValue());
            String data = HttpClientUtil.doGet(url, maps);
            JSONObject jsonObject = JSON.parseObject(data);
            String status = jsonObject.getString("status");
            if ("0".equals(status)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject formatted_addresses = result.getJSONObject("formatted_addresses");
                messageInfo.setContent(formatted_addresses.getString("standard_address"));
            }
            messageInfo.setUrl("0");
        }
        return messageInfoDao.insert(messageInfo);
    }

    @Override
    public int update(MessageInfo messageInfo){
        return messageInfoDao.updateById(messageInfo);
    }

    @Override
    public int delete(Long id){
        return messageInfoDao.deleteById(id);
    }

    @Override
    public MessageInfo selectMessageById(Long id){
        return messageInfoDao.selectById(id);
    }

    @Override
    public int updateSendState(Long userId) {
        return messageInfoDao.updateSendState(userId);
    }

    @Override
    public Result getOrderMessage(Long userId) {
        List<MessageInfo> selectList = baseMapper.selectList(new QueryWrapper<MessageInfo>().eq("user_id", userId).eq("is_see", "0").eq("type", 1));
        for (MessageInfo messageInfo : selectList) {
            messageInfo.setIsSee("2");
            baseMapper.updateById(messageInfo);
        }
        return Result.success().put("data", selectList.size());


    }


}
