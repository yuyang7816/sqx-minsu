import './assets/main.css'
import {
	createApp
} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/theme.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import api from '../comment/api.js'
import App from './App.vue'
import router from './router'
import {
	createPinia
} from 'pinia'
import {
	copyText,
	countMoney,
	validatePhoneNumber,
	isAuth
} from '../comment/utils.js'
const app = createApp(App)
const pinia = createPinia()
//请求挂在到全局
app.config.globalProperties.$Request = api;
//复制文本的方法挂在到全局
app.config.globalProperties.$copyText = copyText
//金额保留两位小数的方法挂在到全局
app.config.globalProperties.$countMoney = countMoney
//校验手机号
app.config.globalProperties.$validatePhoneNumber = validatePhoneNumber
//挂在权限检测到全局
app.config.globalProperties.isAuth = isAuth
// 使用 Element Plus
app.use(ElementPlus, {
	locale: zhCn,
})
// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	// console.log(key, '111')
	// console.log(component, '111')
	app.component(key, component)
}

app.use(pinia)
app.use(router)
app.mount('#app')