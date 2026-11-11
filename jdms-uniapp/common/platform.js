/**
 * 多端运行时识别与支付分类
 * 支付 classify：1=App微信 2=公众号 3=小程序 4=App支付宝 5=H5支付宝
 */

export const isWeixinBrowser = () => {
	// #ifdef H5
	try {
		return /micromessenger/i.test(navigator.userAgent);
	} catch (e) {
		return false;
	}
	// #endif
	return false;
};

export const getRuntimePlatform = () => {
	// #ifdef MP-WEIXIN
	return 'mp-weixin';
	// #endif
	// #ifdef APP-PLUS
	return 'app';
	// #endif
	// #ifdef H5
	return isWeixinBrowser() ? 'h5-weixin' : 'h5';
	// #endif
	return 'unknown';
};

export const getLoginPlatform = () => {
	const runtime = getRuntimePlatform();
	if (runtime === 'mp-weixin') return '小程序';
	if (runtime === 'h5-weixin') return '公众号';
	if (runtime === 'app') return 'app';
	if (runtime === 'h5') return 'H5';
	return 'unknown';
};

export const getSysPhone = () => {
	// #ifdef APP-PLUS
	try {
		return uni.getSystemInfoSync().platform === 'ios' ? 2 : 1;
	} catch (e) {
		return 1;
	}
	// #endif
	return 1;
};

/**
 * @param {number} payWay 1=微信 2=支付宝 5=余额
 */
export const getPayClassify = (payWay = 1) => {
	const runtime = getRuntimePlatform();
	if (payWay === 2) {
		return runtime === 'app' ? 4 : 5;
	}
	if (payWay === 1) {
		if (runtime === 'mp-weixin') return 3;
		if (runtime === 'h5-weixin') return 2;
		return 1;
	}
	return 1;
};

export const buildWeixinOAuthUrl = (appId, redirectUri, state = 'login') => {
	const uri = encodeURIComponent(redirectUri);
	return `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=${uri}&response_type=code&scope=snsapi_userinfo&state=${state}#wechat_redirect`;
};
