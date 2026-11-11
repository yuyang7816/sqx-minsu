import { request } from '@/utils/http'

export const getAddress = data => request({
  url: '/app/address/getAddressList',
  method: 'get',
  data
})
export const getAddressInfo = data => request({
  url: '/app/address/getAddressInfo',
  method: 'get',
  data
})
export const localAddress = data => request({
  url: '/app/Login/selectCity',
  method: 'get',
  data
})

export const delAddressById = data => request({
  url: `/app/address/deleteAddress?addressId=${data}`,
  method: 'post',
  isLoading: true
})