package com.sqx.app.base.user.service;


import com.sqx.app.base.utils.Result;

public interface IAppleService {

    Result getAppleUserInfo(String identityToken) throws Exception;

}
