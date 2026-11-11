// import store from '../store/store.js';
import config from '../config.js';
import { getToken } from '@/utils'

const host = config.BASE_URL;


let isAgainLogin = false;

export function request({ url, method, data, header, isLoading = false, loadingTxt = '' }) {
	if (isLoading) {
		uni.showLoading({
			title: loadingTxt, // 数据请求前loading
			mask: true
		});
	}
	let token = getToken()
	// 默认header
	let headers = {
		'Accept': 'application/json',
		'content-type': 'application/json',
		'token': token || '',
		// 'Cookie': 'JSESSIONID=85e56736-e985-4628-9910-4d969a342666; Path=/; httponly; secure; SameSite=NonesmartHome; HttpOnly',
		// 'set-Cookie': 'JSESSIONID=85e56736-e985-4628-9910-4d969a342666; Path=/; httponly; secure; SameSite=NonesmartHome; HttpOnly'
	};
	// 如果存在自定义header则与默认header合并
	if (header) {
		Object.assign(headers, header);
	}
	return new Promise((resolve, reject) => {
		uni.request({
			url: host + url, // 接口地址
			method: method,
			data: data,
			header: headers,
			success: function (res) {
				if (res.data.code != 0) {
					uni.showToast({ title: res.data.msg, icon: 'none' })
				}
				if (res.data.code == 401) {
					uni.navigateTo({ url: '/pages/public/login' })
					// uni.navigateTo({ url: '/subPackage/login/login' })
				}
				resolve(res.data);
				if (isLoading) uni.hideLoading();
				// if(res.data.code==401){
				// 	uni.clearStorageSync();
				// }
				// if (res.data && res.data.code === 0) {
				// 	resolve(res.data);
				// 	if (isLoading) uni.hideLoading();
				// } else {
				// 	if (isLoading) uni.hideLoading();
				// 	reject(res.data);
				// 	uni.showToast({
				// 		icon: 'none',
				// 		title: `${res.data.msg}`,
				// 		duration: 3000
				// 	});
				// }
			},
			fail: function (err) {
				reject(err);
				let errMsg = err.data && err.data.msg ? err.data.msg : '请求失败,请检查网络'
				uni.showToast({
					icon: 'none',
					title: `请检查网络`,
					// title: errMsg,
					duration: 3000
				});
				if (isLoading) uni.hideLoading()
			}
		});
	});
}

function get(obj) {
	obj.header = {
		"content-type": 'application/x-www-form-urlencoded'
	}
	return request({ ...obj, method: 'get' });
}
function gets(obj) {
	obj.header = {
		"content-type": 'application/json'
	}
	return request({ ...obj, method: 'get' });
}

function post(obj) {
	obj.header = {
		"content-type": 'application/x-www-form-urlencoded'
	}
	return request({ ...obj, method: 'post' });
}

function posts(obj) {
	obj.header = {
		"content-type": 'application/json'
	}
	return request({ ...obj, method: 'post' });
}

function postUpload(obj) {
	obj.header = {
		// "content-type": 'multipart/form-data'
		"content-type": 'multipart/form-data'
	}
	return request({ ...obj, method: 'post' });
}


function put(obj) {
	obj.header = {
		"content-type": 'application/json'
	}
	return request({ ...obj, method: 'put' });
}

function del(obj) {
	obj.header = {}
	return request({ ...obj, method: 'delete' });
}

export default {
	// request,
	get,
	gets,
	post,
	put,
	del,
	host,
	posts,
	postUpload
};
