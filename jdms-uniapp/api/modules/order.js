import { request } from '@/utils/http'

export const userOrder = data => request({
  url: '/app/topicIndent/addTopicIndent',
  method: 'post',
  data,
  isLoading: true,
  // header:{
  //   'content-type':'multipart/form-data'
  // }
})

export const payment = data => request({
  url: '/app/topicIndent/payIndent',
  method: 'post',
  data,
  header: {
    'content-type': 'application/x-www-form-urlencoded'
  },
  isLoading: true
})

export const getOrderByType = data => request({
  url: '/app/topicIndent/getTopicIndentList',
  method: 'get',
  data
})

export const getOrderDetail = data => request({
  url: '/app/topicIndent/getTopicIndentInfo',
  method: 'get',
  data
})

export const cancelOrder = data => request({
  url: '/app/topicIndent/userCancelIndent',
  method: 'post',
  data,
  header: {
    'content-type': 'application/x-www-form-urlencoded'
  }
})

export const confirmOrder = data => request({
  url: '/app/topicIndent/finishIndent',
  method: 'post',
  data,
  header: {
    'content-type': 'application/x-www-form-urlencoded'
  }
})

export const getTeamListById = data => request({
  url: '/app/topicIndent/getGroupIndentList',
  method: 'get',
  data
})

export const deleteOrder = data => request({
  url: '/app/topicIndent/deleteIndent',
  method: 'get',
  data,
  isLoading: true
})

export const getTicketList = data => request({
  // url: '/app/couponUser/getMyOrderCouponList',
	url:'/app/couponUser/getMyCouponList',
  method: 'get',
  data
})

export const payByGold = data => request({
  url: '/app/topicGoods/goldCoinsExchange',
  method: 'get',
  data,
  isLoading: true
})