import { request } from '@/utils/http'

export const getGoodListById = data => request({
  url: '/app/topicGoodsType/getTopicGoodsTypeList',
  method: 'get',
  data
})

export const getGoodTabList = data => request({
  url: '/app/topicGoodsType/getAppTopicGoodsTypeList',
  method: 'get',
  data
})

export const getGoodDetail = data => request({
  url: '/app/topicGoods/getTopicGoodsInfo',
  method: 'get',
  data
})

export const getSectionListById = data => request({
  url: '/app/topicSection/getTopicSectionByGoodsId',
  method: 'get',
  data
})
export const getGoodInfoList = data => request({
  url: '/app/topicGoods/getTopicGoodsList',
  method: 'get',
  data
})
// export const getGoldExchangeList = data => request({
//   url: '/app/topicGoods/getGoldCoinsGoodsList',
//   method: 'get',
//   data
// })

export const collectById = data => request({
  url: '/app/scFollow/updateFollow',
  method: 'post',
  data
})