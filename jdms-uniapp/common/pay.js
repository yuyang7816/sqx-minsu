/**
 * 微信支付参数标准化
 * 后端 JSAPI/小程序返回: timestamp, noncestr, sign, package
 * uni.requestPayment 需要: timeStamp, nonceStr, paySign, package
 */

export const unwrapPayData = (response) => {
	if (!response) return {};
	if (response.data && typeof response.data === 'object') {
		return response.data;
	}
	return response;
};

export const normalizeWxJsapiPayParams = (raw = {}) => {
	const data = unwrapPayData(raw);
	return {
		timeStamp: String(data.timestamp || data.timeStamp || ''),
		nonceStr: data.noncestr || data.nonceStr || '',
		package: data.package || data.packageValue || '',
		signType: data.signType || 'MD5',
		paySign: data.sign || data.paySign || ''
	};
};

export const requestMpWxPayment = (raw, callbacks = {}) => {
	const params = normalizeWxJsapiPayParams(raw);
	if (!params.timeStamp || !params.nonceStr || !params.package || !params.paySign) {
		const err = new Error('支付参数不完整');
		uni.showToast({
			title: '支付参数异常',
			icon: 'none'
		});
		callbacks.onFail && callbacks.onFail(err);
		return Promise.reject(err);
	}
	return new Promise((resolve, reject) => {
		uni.requestPayment({
			provider: 'wxpay',
			timeStamp: params.timeStamp,
			nonceStr: params.nonceStr,
			package: params.package,
			signType: params.signType,
			paySign: params.paySign,
			success(res) {
				callbacks.onSuccess && callbacks.onSuccess(res);
				resolve(res);
			},
			fail(err) {
				callbacks.onFail && callbacks.onFail(err);
				reject(err);
			}
		});
	});
};

export const invokeH5WxPay = (raw, callbacks = {}) => {
	const response = unwrapPayData(raw);
	if (!response.package) {
		const err = new Error('支付参数不完整');
		callbacks.onFail && callbacks.onFail(err);
		return;
	}
	const payRequest = () => {
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', {
				appId: response.appid || response.appId,
				timeStamp: String(response.timestamp || response.timeStamp || ''),
				nonceStr: response.noncestr || response.nonceStr,
				package: response.package,
				signType: response.signType || 'MD5',
				paySign: response.sign || response.paySign
			},
			(res) => {
				if (res.err_msg === 'get_brand_wcpay_request:ok') {
					callbacks.onSuccess && callbacks.onSuccess(res);
				} else {
					callbacks.onFail && callbacks.onFail(res);
				}
			}
		);
	};
	if (typeof WeixinJSBridge === 'undefined') {
		if (document.addEventListener) {
			document.addEventListener('WeixinJSBridgeReady', payRequest, false);
		} else if (document.attachEvent) {
			document.attachEvent('WeixinJSBridgeReady', payRequest);
			document.attachEvent('onWeixinJSBridgeReady', payRequest);
		}
	} else {
		payRequest();
	}
};
