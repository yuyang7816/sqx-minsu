import { request } from '@/utils/http'

export const getTopicErrorOfList = data => request({
  url: '/app/topicError/getTopicErrorList',
  method: 'get',
  data
})

export const getTopicErrorOfTypeList = data => request({
  url: '/app/topicLibrary/getMyErrorLibraryList',
  method: 'get',
  data
})

export const removeErr = data => request({
  url: '/app/topicError/deleteTopicError',
  method: 'get',
  data
})

export const getTopicCollectOfList = data => request({
  url: '/app/scFollow/getMyFollowList',
  method: 'get',
  data
})

export const collectTopic = data => request({
  url: '/app/scFollow/updateFollow',
  method: 'post',
  data
})

export const getExchangeList = data => request({
  url: '/app/topicGoods/getGoldCoinsGoodsList',
  method: 'get',
  data
})

export const getSigninList = data => request({
  url: '/app/signConfig/getSignConfigList',
  method: 'get',
  data
})

export const signin = data => request({
  url: '/app/signRecord/userSign',
  method: 'post',
  data
})

export const getTopicListByTypeId = data => request({
  url: '/app/topicUserGoods/getMyGoodsList',
  method: 'get',
  data
})

export const getUserInfoById = data => request({
  url: '/app/user/selectUserById',
  method: 'get',
  data
})

export const isSign = data => request({
  url: '/app/signRecord/isSignToday',
  method: 'get',
  data
})

export const updateUserInfo = data => request({
  url: '/app/user/updateUser',
  method: 'post',
  data,
  isLoading: true
})