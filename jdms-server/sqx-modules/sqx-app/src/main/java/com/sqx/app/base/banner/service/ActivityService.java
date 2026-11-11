package com.sqx.app.base.banner.service;

import com.sqx.app.base.banner.entity.Activity;

import java.util.List;

public interface ActivityService {


    List<Activity> selectByState(String state);

    Activity selectActivityById(Long id);

    int insertActivity(Activity info);

    int updateActivity(Activity info);

    int deleteActivity(Long id);

    List<Activity> selectActivity();

    List<Activity> selectActivitys();



}
