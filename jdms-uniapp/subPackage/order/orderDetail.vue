<template>
	<view class="plat-detail-page plat-order-detail-page">
		<view class="plat-order-status">
			<view class="plat-order-status__main">
				<view class="plat-order-status__title">{{orderStatusText(orderInfo.orderStatus)}}...</view>
				<view class="plat-order-status__desc">{{orderStatusText1(orderInfo.orderStatus)}}</view>
			</view>
			<view class="plat-order-status__badge">订单详情</view>
		</view>

		<view class="plat-detail-section" style="margin-top:0;">
			<view class="plat-card plat-order-hotel">
				<view class="plat-order-hotel__head">
					<view class="plat-order-hotel__text">
						<view class="plat-cover-title">{{orderInfo.shopName||''}}</view>
						<view class="plat-cover-sub" style="margin-top:8rpx;">{{orderInfo.classifyName||''}}</view>
					</view>
					<image :src="orderInfo.coverImage" mode="aspectFill" class="plat-order-hotel__cover"
						v-if="orderInfo.coverImage">
					</image>
				</view>
				<view class="plat-order-stay" v-if="orderInfo.startDate||orderInfo.endDate">
					<view class="plat-order-stay__item">
						<view class="plat-order-stay__val">{{formatDate(orderInfo.startDate)||''}}</view>
						<view class="plat-order-stay__label">入住时间</view>
					</view>
					<view class="plat-order-stay__dash"></view>
					<view class="plat-order-stay__item">
						<view class="plat-order-stay__val">{{formatDate(orderInfo.endDate)||''}}</view>
						<view class="plat-order-stay__label">离店时间</view>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-detail-section" v-if="orderInfo.orderStatus == 1">
			<view class="plat-card plat-order-verify">
				<view class="plat-order-verify__panel">
					<view class="plat-order-verify__main">
						<view class="plat-order-verify__qr">
							<up-qrcode cid="ex1" :size="120" :val="orderInfo.orderNumber"></up-qrcode>
						</view>
						<view class="plat-order-verify__side" v-if="orderInfo.orderNumber">
							<view class="plat-order-verify__barcode-wrap">
								<up-barcode :value="orderInfo.orderNumber" :width="220" :height="44" :display-value="false"
									class="plat-order-verify__barcode" />
							</view>
						</view>
					</view>
					<view class="plat-order-verify__no">{{orderInfo.orderNumber||'暂无'}}</view>
				</view>
				<view class="plat-form-divider"></view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">总金额</view>
					<view class="plat-cover-price">
						<text class="unit">￥</text>
						<text class="num">{{orderInfo.payMoney||'0.00'}}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-detail-section">
			<view class="plat-form-card" style="margin:0;">
				<view class="plat-form-card__title">到店信息</view>
				<view class="plat-order-shop" @click="openLocation" v-if="orderInfo.shop">
					<view class="plat-order-shop__text">
						<view class="plat-order-shop__name">{{orderInfo.shopName||''}}</view>
						<view class="plat-order-shop__addr">
							{{orderInfo.shop.province||''}}{{orderInfo.shop.city||''}}{{orderInfo.shop.county||''}}{{orderInfo.shop.address||''}}
						</view>
					</view>
					<up-icon name="arrow-right" color="#64748B" size="16"></up-icon>
				</view>
				<view class="plat-form-divider"></view>
				<view class="plat-btn plat-btn--ghost plat-order-contact" @click="callShop">联系商家</view>
			</view>
		</view>

		<view class="plat-detail-section">
			<view class="plat-form-card" style="margin:0;">
				<view class="plat-form-card__title">入住信息</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">姓名</view>
					<view class="plat-order-info-row__value">{{orderInfo.realName||'暂无'}}</view>
				</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">联系方式</view>
					<view class="plat-order-info-row__value">{{hideMiddleDigits(orderInfo.phone)||'暂无'}}</view>
				</view>
			</view>
		</view>

		<view class="plat-detail-section">
			<view class="plat-form-card" style="margin:0;">
				<view class="plat-form-card__title">温馨提示</view>
				<view class="plat-info-content">{{orderInfo.warmReminder||'暂无'}}</view>
			</view>
		</view>

		<view class="plat-detail-section">
			<view class="plat-form-card" style="margin:0;">
				<view class="plat-form-card__title">订单信息</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">订单金额</view>
					<view class="plat-order-info-row__value">￥{{orderInfo.orderMoney||'0.00'}}</view>
				</view>
				<view class="plat-order-info-row" v-if="orderInfo.couponId && Number(orderInfo.couponMoney) > 0">
					<view class="plat-order-info-row__label">优惠券</view>
					<view class="plat-order-info-row__value plat-order-info-row__value--discount plat-order-info-row__coupon">
						<text class="plat-order-info-row__coupon-name">{{ orderInfo.couponName || '优惠券' }}</text>
						<text class="plat-order-info-row__coupon-amount">-￥{{ orderInfo.couponMoney }}</text>
					</view>
				</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">实付金额</view>
					<view class="plat-order-info-row__value">￥{{orderInfo.payMoney||'0.00'}}</view>
				</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">订单编号</view>
					<view class="plat-order-info-row__copy" @click="copyClick(orderInfo.orderNumber)">
						<text>{{orderInfo.orderNumber||''}}</text>
						<up-icon name="order" color="#64748B" size="24"></up-icon>
					</view>
				</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">下单时间</view>
					<view class="plat-order-info-row__value">{{orderInfo.createTime||'暂无'}}</view>
				</view>
			</view>
		</view>

		<view class="plat-detail-spacer"></view>

		<view class="plat-fixed-foot plat-order-detail-foot">
			<view class="plat-order-detail-actions">
				<view class="plat-btn plat-btn--ghost plat-btn--sm" v-if="orderInfo.orderStatus == 1" @click="userCancelOrder(orderInfo)">取消订单</view>
				<view class="plat-btn plat-btn--sm" v-if="orderInfo.orderStatus == 0" @click="openPayPopup">立即支付</view>
				<view class="plat-btn plat-btn--sm" v-if="orderInfo.orderStatus == 3&&orderInfo.isEvaluate == 0"
					@click.stop="routerGo('/subpackage/order/evaluate?orderId='+orderInfo.orderNumber+'&shopId='+orderInfo.shopId)">
					去评价
				</view>
			</view>
		</view>

		<up-popup :show="payShow" @close="payShow = false" :closeOnClickOverlay="true" round="12" :closeable="true">
			<view class="plat-form-card padding" style="margin:0;border-radius:24rpx 24rpx 0 0;">
				<view class="plat-form-card__title margin-bottom-lg">支付方式</view>
				<view class="flex align-center pay-row" v-for="(item, index) in payList"
					:key="index">
					<view>
						<image :src="item.image" mode=""
							style="width: 55rpx;height: 55rpx;margin-right: 20rpx;border-radius: 50rpx;">
						</image>
					</view>
					<up-radio-group v-model="payWay" iconPlacement="right" @change="selectWay(item.id)">
						<up-radio :customStyle="{marginBottom: '8px'}" :label="item.name" :name="item.id" activeColor="#2C8EFE"
							inactiveColor="#272833">
						</up-radio>
					</up-radio-group>
				</view>
				<view class="plat-pay-item" :class="{'plat-pay-item--active': isBalance}" style="display: flex;height: 100rpx;align-items: center;"
					v-if="walletInfo.canCashMoney > 0" @click="selectWay(5)">
					<view style="display: flex;width:80%;align-items: center;">
						<image src="/my/static/jb.png" style="width: 55rpx;height: 55rpx;border-radius: 50rpx;"></image>
						<view style="font-size: 30rpx;margin-left: 20rpx;width: 70%;color: var(--plat-text);">余额</view>
					</view>
					<view @click="selectWay(5)">
						<image src="/subpackage/static/detailImage/weixuanzhongyuanquan (2).png" mode=""
							style="width: 38rpx;height: 38rpx;border-radius: 50%;" v-if="!isBalance"></image>
						<image src="/my/static/xuanze (1).png" mode="" v-if="isBalance"
							style="width: 38rpx;height: 38rpx;border-radius: 50%;"></image>
					</view>
				</view>
				<view class="plat-btn payBtn" @click="payOrder">立即支付</view>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		nextTick
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	import {
		getPayClassify
	} from '@/common/platform.js';
	import {
		requestMpWxPayment,
		invokeH5WxPay
	} from '@/common/pay.js';
	const {
		proxy
	} = getCurrentInstance();

	const orderInfo = reactive({});
	const orderId = ref('');
	const payShow = ref(false)
	const payWay = ref('')
	const isBalance = ref(false)
	const payList = reactive([])
	const walletInfo = reactive({})
	const isFinish = ref(true)
	const autoOpenPay = ref(false)

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	}

	const openPayPopup = () => {
		if (orderInfo.orderStatus != 0) {
			return
		}
		payShow.value = true
	}

	const doCancelOrder = () => {
		uni.showLoading({
			title: '请稍后...'
		})
		proxy.$http.postT('/app/orders/userCancelOrder', {
			orderId: orderInfo.orderId
		}).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				uni.showToast({
					title: '取消成功',
					icon: 'none'
				})
				setTimeout(() => {
					getOrderDetail()
				}, 500)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		}).catch(() => {
			uni.hideLoading()
			uni.showToast({
				title: '网络异常，请稍后重试',
				icon: 'none'
			})
		})
	}

	const userCancelOrder = () => {
		uni.showLoading({
			title: '请稍后...'
		})
		proxy.$http.getT('/app/orders/cancelOrderPreview', {
			orderId: orderInfo.orderId
		}).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				uni.showModal({
					title: '提示',
					content: res.data?.tip || '确认取消订单？',
					confirmText: '确定取消',
					cancelText: '再想想',
					success: (modalRes) => {
						if (modalRes.confirm) {
							doCancelOrder()
						}
					}
				})
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		}).catch(() => {
			uni.hideLoading()
			uni.showToast({
				title: '网络异常，请稍后重试',
				icon: 'none'
			})
		})
	};
	const copyClick = (title) => {
		uni.setClipboardData({
			data: title,
			success: function(res) {
				uni.showToast({
					title: "复制成功",
					icon: 'none',
				});
			},
		});
	};
	const isPhoneNumber = (phone) => {
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone);
	};
	const hideMiddleDigits = (phone) => {
		if (isPhoneNumber(phone)) {
			return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
		} else {
			return phone;
		}
	};

	const openLocation = () => {
		uni.openLocation({
			longitude: orderInfo.shop.lng,
			latitude: orderInfo.shop.lat,
			name: orderInfo.shop.name,
			address: orderInfo.shop.address
		})
	};

	const callShop = () => {
		uni.makePhoneCall({
			phoneNumber: orderInfo.shop.shopPhone
		});
	};

	const formatDate = (dateStr) => {
		const date = new Date(dateStr.replace(/-/g, '/'));
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${month}月${day}日`;
	};

	const orderStatusText = (val) => {
		switch (val) {
			case 0:
				return '待支付';
			case 1:
				return '待核销';
			case 2:
				return '进行中';
			case 3:
				return '已完成';
			case 4:
				return '已取消';
		}
	};
	const orderStatusText1 = (val) => {
		switch (val) {
			case 0:
				return '请尽快完成支付，超时未支付订单将自动取消';
			case 1:
				return '订单已支付成功，请到店出示核销码进行消费';
			case 2:
				return '服务正在进行中，如有问题请联系商家';
			case 3:
				return '订单已完成，期待您的评价';
			case 4:
				return '订单已取消，如需购买请重新下单';
		}
	};

	const selectWay = (id) => {
		payWay.value = id;
		isBalance.value = id == 5
	};

	const getWalletInfo = () => {
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				Object.assign(walletInfo, res.data)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	const initPayList = () => {
		// #ifdef MP-WEIXIN
		payList.splice(0, payList.length)
		payWay.value = 1
		payList.push({
			id: 1,
			image: 'https://renwu.xiansqx.com/img/20210813/e71382c1b8404e4f96c6d6e89d305329.png',
			name: '微信'
		})
		// #endif

		// #ifdef APP-PLUS
		payList.splice(0, payList.length)
		payWay.value = 1
		payList.push({
			id: 1,
			image: 'https://renwu.xiansqx.com/img/20210813/e71382c1b8404e4f96c6d6e89d305329.png',
			name: '微信'
		}, {
			id: 2,
			image: 'https://renwu.xiansqx.com/img/20210813/56cde02b040e453ead8b5d2c0dc17e18.png',
			name: '支付宝'
		})
		// #endif

		// #ifdef H5
		const target = navigator.userAgent.toLowerCase();
		const isWeixin = (/micromessenger/.test(target)) ? true : false;
		payList.splice(0, payList.length)
		if (isWeixin) {
			payWay.value = 1
			payList.push({
				id: 1,
				image: 'https://renwu.xiansqx.com/img/20210813/e71382c1b8404e4f96c6d6e89d305329.png',
				name: '微信'
			})
		} else {
			payWay.value = 2
			payList.push({
				id: 2,
				image: 'https://renwu.xiansqx.com/img/20210813/56cde02b040e453ead8b5d2c0dc17e18.png',
				name: '支付宝'
			})
		}
		// #endif
	}

	const payOrder = () => {
		if (!isFinish.value || !orderInfo.orderId) {
			return
		}
		isFinish.value = false
		const data = {
			ordersId: orderInfo.orderId,
			orderId: orderInfo.orderId,
			classify: getPayClassify(payWay.value)
		}
		uni.showLoading({
			title: '支付中...'
		})

		if (payWay.value == 5) {
			proxy.$http.postT('/app/userMoney/payOrder', data).then(res => {
				uni.hideLoading()
				if (res.code == 0) {
					uni.showToast({
						title: '支付成功',
						icon: 'none'
					})
					payShow.value = false
					getOrderDetail()
					getWalletInfo()
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
					payShow.value = false
				}
				isFinish.value = true
			})
		} else if (payWay.value == 1) {
			data.wxPayParam = {
				tradeType: data.tradeType
			}
			// #ifdef H5
			proxy.$http.postT('/app/wxPay/wxPayOrder', data).then(red => {
				invokeH5WxPay(red, {
					onSuccess: () => {
						uni.hideLoading();
						uni.showToast({
							title: '支付成功',
							icon: 'none'
						});
						payShow.value = false;
						getOrderDetail();
						getWalletInfo();
						isFinish.value = true;
					},
					onFail: () => {
						isFinish.value = true;
						uni.hideLoading();
					}
				});
			})
			// #endif
			// #ifdef APP-PLUS
			proxy.$http.postT('/app/wxPay/wxPayOrder', data).then(red => {
				setPayment('wxpay', red.data)
			})
			// #endif
			// #ifdef MP-WEIXIN
			data.openId = uni.getStorageSync('openId')
			proxy.$http.postT('/app/wxPay/wxPayOrder', data).then(red => {
				uni.hideLoading()
				requestMpWxPayment(red, {
					onSuccess: () => {
						uni.showToast({
							title: '支付成功',
							icon: 'none'
						})
						payShow.value = false
						getOrderDetail()
						getWalletInfo()
						isFinish.value = true
					},
					onFail: (err) => {
						isFinish.value = true
						console.log(err, 'mp wx pay fail')
						uni.hideLoading();
						uni.showToast({
							title: '支付失败',
							icon: 'none'
						})
					}
				})
			})
			// #endif
		} else if (payWay.value == 2) {
			data.aliPayParam = {
				tradeType: data.tradeType
			}
			// #ifdef H5
			proxy.$http.postT('/app/aliPay/payOrder', data).then(red => {
				const div = document.createElement('div')
				div.innerHTML = red.data
				document.body.appendChild(div)
				document.forms[0].submit()
			})
			// #endif
			// #ifdef APP-PLUS
			proxy.$http.postT('/app/aliPay/payOrder', data).then(red => {
				setPayment('alipay', red.data)
			})
			// #endif
		} else {
			isFinish.value = true
			uni.hideLoading()
			uni.showToast({
				title: '请选择支付方式',
				icon: 'none'
			})
		}
	}

	const setPayment = (name, order) => {
		let paymentData = {};
		if (name === 'wxpay' && order) {
			paymentData = JSON.stringify({
				appid: order.appId,
				noncestr: order.nonceStr,
				package: order.packageValue,
				partnerid: order.partnerId,
				prepayid: order.prepayId,
				timestamp: order.timeStamp,
				sign: order.sign
			})
		} else {
			paymentData = order
		}
		uni.requestPayment({
			provider: name,
			orderInfo: paymentData,
			success: () => {
				uni.hideLoading();
				uni.showToast({
					title: '支付成功',
					icon: 'none'
				})
				payShow.value = false
				getOrderDetail()
				getWalletInfo()
				isFinish.value = true
			},
			fail: () => {
				isFinish.value = true
				uni.hideLoading();
			},
			complete() {
				isFinish.value = true
				uni.hideLoading();
			}
		})
	}

	const callPay = (response) => {
		if (typeof WeixinJSBridge === 'undefined') {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady(response), false)
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady(response))
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(response))
			}
		} else {
			onBridgeReady(response)
		}
	}

	const onBridgeReady = (response) => {
		if (!response.package) {
			isFinish.value = true
			uni.hideLoading()
			return
		}
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', {
				appId: response.appid,
				timeStamp: response.timestamp,
				nonceStr: response.noncestr,
				package: response.package,
				signType: response.signType,
				paySign: response.sign
			},
			(res) => {
				if (res.err_msg === 'get_brand_wcpay_request:ok') {
					uni.hideLoading();
					uni.showToast({
						title: '支付成功',
						icon: 'none'
					})
					payShow.value = false
					getOrderDetail()
					getWalletInfo()
				} else {
					uni.hideLoading();
				}
				isFinish.value = true
				WeixinJSBridge.log(response.err_msg)
			}
		)
	}


	const getOrderDetail = () => {
		let data = {
			orderId: orderId.value
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/orders/getMyOrderInfo', data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				Object.assign(orderInfo, res.data)
				if (autoOpenPay.value && res.data.orderStatus == 0) {
					nextTick(() => {
						openPayPopup()
					})
				}
				autoOpenPay.value = false
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
				autoOpenPay.value = false
			}
		})
	};

	onLoad(option => {
		initPayList()
		getWalletInfo()
		if (option.orderId) {
			orderId.value = option.orderId
			autoOpenPay.value = option.showPay == 1 || option.showPay == '1'
			getOrderDetail()
		}
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	.pay-row {
		margin: auto;
		margin-top: 20rpx;
	}

	.payBtn {
		margin-top: 30rpx;
	}

	.plat-order-verify {
		padding: 32rpx 28rpx;
	}

	.plat-order-verify__panel {
		width: 100%;
		max-width: 560rpx;
		padding: 24rpx 24rpx 18rpx;
		border-radius: 24rpx;
		background: #f8fafc;
		border: 1rpx solid #e2e8f0;
		box-sizing: border-box;
	}

	.plat-order-verify__main {
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 12rpx;
		flex-wrap: nowrap;
	}

	.plat-order-verify__qr {
		flex-shrink: 0;
		width: 240rpx;
		height: 240rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.plat-order-verify__side {
		width: 104rpx;
		height: 240rpx;
		flex-shrink: 0;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.plat-order-verify__barcode-wrap {
		height: 240rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		overflow: hidden;
		width: 104rpx;
	}

	.plat-order-verify__barcode {
		margin: 0;
		transform: rotate(90deg);
		transform-origin: center center;
	}

	.plat-order-verify__no {
		margin-top: 16rpx;
		font-size: 26rpx;
		font-weight: 600;
		color: #1e293b;
		line-height: 1.4;
		text-align: center;
		word-break: break-all;
	}

	.plat-order-info-row__value--discount {
		color: #ef4444;
	}
</style>
