import { ObjToStringQuery } from '@/utils'

export default {
  methods: {
    navigate(url, params = null) {
      if (params)
        url = `${url}${url.indexOf('?') === -1 ? '?' : '&'}${ObjToStringQuery(
          params
        )}`
      uni.navigateTo({ url })
    },
    redirect(url, params = null) {
      if (params)
        url = `${url}${url.indexOf('?') === -1 ? '?' : '&'}${ObjToStringQuery(
          params
        )}`
      uni.redirectTo({ url })
    },
    deltaBack(delta) {
      uni.navigateBack({ delta })
    }
  }
}