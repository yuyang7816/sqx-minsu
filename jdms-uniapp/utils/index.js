// import store from '@/store'

export const ObjToStringQuery = (params) =>
  Object.entries(params)
    .map((param) => param.join('='))
    .join('&')

// export const getToken = () => {
//   if (store.getters.token) return store.getters.token
//   const token = uni.getStorageSync('token')
//   store.commit('userModule/setToken', token)
//   return token
//   // 跳转登录
//   // uni.navigateTo({ url: '/subPackage/login/login' })
// }

export const complete_unit = (num) => (/^[0-9]*$/.test(num) ? num + 'px' : num)

/**
 * uni get DOM
 */
export const getRect = (_this, identification) => new Promise((resolve, reject) => {
  uni.createSelectorQuery().in(_this).select(identification).boundingClientRect(data => resolve(data)).exec()
})

/**
 * 手机号加密
 */
export const phoneEncrypt = phone => {
  const pattern = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
  if (!pattern.test(phone)) return phone
  const encrypt = /(\d{3})\d{4}(\d{4})/
  return String(phone).replace(encrypt, '$1****$2')
}

// /**
//  * vuex持久化数据读取
//  * @param {String} key  读取的key 
//  */
// export const vuexFromStorage = (key = 'vuex') => {
//   try {
//     console.log(uni.getStorageSync(key));
//     const res = JSON.parse(uni.getStorageSync(key))
//     Object.keys(res).forEach(moduleKey => {
//       store.commit(`${moduleKey}/RESET_STATE`, res[moduleKey])
//     })
//   } catch (e) {
//     if (process.env.NODE_ENV === 'development'){
//       console.log(e);
//     }
//   }
// }