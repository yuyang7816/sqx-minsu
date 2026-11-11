// api.js  

import axios from 'axios';

import {
	ROOTPATH
} from './httpUrl'

// 创建axios实例  
const apiClient = axios.create({
	baseURL: ROOTPATH, // 设置你的API基础URL  
	timeout: 5000, // 设置请求超时时间（单位：毫秒）  
});

// 添加请求拦截器  
apiClient.interceptors.request.use(
	config => {
		//判断是否是表单提交form-data格式
		if (config.type === 'json') {
			config.headers['Content-Type'] = 'application/json'
		} else {
			config.headers['Content-Type'] = 'multipart/form-data'
		}
		// 在发送请求之前做些什么（例如：添加请求头，身份验证等）  
		config.headers['token'] = localStorage.getItem('token');
		return config;
	},
	error => {
		// 对请求错误做些什么（例如：返回错误信息）  
		return Promise.reject(error);
	}
);
// 添加响应拦截器  
apiClient.interceptors.response.use(
	response => {
		// 对响应数据做些什么（例如：处理返回的数据，提取有用的信息等）  
		return response.data;
	},
	error => {
		// 对响应错误做些什么（例如：返回错误信息，重试等）  
		return Promise.reject(error);
	}
);

export default apiClient;