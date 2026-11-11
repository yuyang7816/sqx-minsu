package com.sqx.common.shiro.service.impl;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.sqx.common.core.constant.Constant;
import com.sqx.common.redis.utils.RedisUtil;
import com.sqx.common.shiro.constant.ShiroConstant;
import com.sqx.common.shiro.model.AuthSysMenu;
import com.sqx.common.shiro.service.ShiroService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chengming
 * @since 2024/7/11
 */
@Service
public class ShiroServiceImpl implements ShiroService {
    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList = new ArrayList<>();

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            String menuListJson = RedisUtil.getCacheObject(ShiroConstant.AUTH_SYS_MENU);
            if (StringUtils.isNotBlank(menuListJson)) {
                List<String> sysPermsList = JSONObject.parseObject(menuListJson, new TypeReference<List<String>>(){});
                if (!CollectionUtils.isEmpty(sysPermsList)) {
                    permsList.addAll(sysPermsList);
                }
            }
        } else {
            String menuListJson = RedisUtil.getCacheObject(String.format(ShiroConstant.AUTH_USER_MENU, userId));
            if (StringUtils.isNotBlank(menuListJson)) {
                List<String> userPermsList = JSONObject.parseObject(menuListJson, new TypeReference<List<String>>(){});
                if (!CollectionUtils.isEmpty(userPermsList)) {
                    permsList.addAll(userPermsList);
                }
            }
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }
}
