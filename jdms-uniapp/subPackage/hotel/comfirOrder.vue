<template>
	<view class="plat-page plat-order-page plat-form-page">
		<view class="plat-card plat-order-summary">
			<image :src="roomInfo.roomTitleImg" mode="aspectFill" class="plat-order-summary__img">
			</image>
			<view class="plat-order-summary__body">
				<view class="flex align-center justify-between">
					<view class="plat-order-summary__name">{{roomInfo.roomName||''}}</view>
					<view class="plat-cover-price" style="align-items: baseline;display:flex;">
						<text class="unit">￥</text>
						<text class="num">{{currentRoomPrice.toFixed(2)}}</text>
					</view>
				</view>
				<view class="plat-order-summary__meta text-cut">{{roomInfo.classifyNames||'暂无'}}</view>
				<view class="plat-order-summary__dates" @click="calendarShow = true">
					<text class="plat-order-summary__date-val">{{todayInfo.display}}</text>
					<text class="plat-order-summary__date-unit">入住</text>
					<text class="plat-order-summary__date-sep">—</text>
					<text class="plat-order-summary__date-val">{{tomorrowInfo.display}}</text>
					<text class="plat-order-summary__date-unit">离店</text>
				</view>
			</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">入住信息</view>
			<view class="plat-form-field">
				<view class="plat-form-field__label">房间数</view>
				<view class="plat-form-field__value plat-order-room-count">
					<up-number-box v-model="roomCount" :min="1" :max="maxRoomCount" integer>
						<template #minus>
							<view class="plat-order-room-count__btn">
								<up-icon name="minus-circle-fill" size="25"
									:color="roomCount <= 1 ? '#EEF1F6' : '#2C8EFE'"></up-icon>
							</view>
						</template>
						<template #input>
							<text class="input plat-order-room-count__num">{{roomCount}}</text>
						</template>
						<template #plus>
							<view class="plat-order-room-count__btn">
								<up-icon name="plus-circle-fill" size="25"
									:color="roomCount >= maxRoomCount ? '#EEF1F6' : '#2C8EFE'"></up-icon>
							</view>
						</template>
					</up-number-box>
					<text v-if="maxRoomCount > 1" class="plat-order-room-count__hint">最多可订 {{maxRoomCount}} 间</text>
				</view>
			</view>
			<view class="plat-form-field"
				@click="routerGo('/my/cyMsg/index?choose=choose&ydUserId='+ydUserInfo.id)">
				<view class="plat-form-field__label">姓名</view>
				<view class="plat-order-link">{{ydUserInfo.infoName?ydUserInfo.infoName:'请选择预定人信息'}}</view>
			</view>
			<view class="plat-form-field">
				<view class="plat-form-field__label">联系方式</view>
				<view class="plat-order-link">{{ydUserInfo.infoPhone?ydUserInfo.infoPhone:'请选择预定人信息'}}</view>
			</view>
		</view>

		<view class="plat-form-card flex align-center justify-between" @click="showFp">
			<view>
				<view class="plat-form-card__title" style="margin-bottom: 8rpx;">发票信息</view>
				<view class="plat-order-link">如需发票，请勾选并填写信息</view>
			</view>
			<view>
				<view v-if="isInvoice == 2">
					<up-icon name="checkmark-circle-fill" size="25" color="#EEF1F6"></up-icon>
				</view>
				<view @click.stop="isInvoice = 2" v-if="isInvoice == 1">
					<up-icon name="checkmark-circle-fill" size="25" color="#2C8EFE"></up-icon>
				</view>
			</view>
		</view>

		<view class="plat-form-card flex align-center justify-between"
			@click="routerGo('/my/ticket/myTicket?choose=choose&couponId='+discountInfo.id+'&price='+orderAmount.payMoney)">
			<view class="plat-form-card__title" style="margin-bottom: 0;">优惠券</view>
			<view class="plat-order-link" :class="{'plat-order-link--price': ticketListNum > 0}" v-if="orderAmount.discountMoney <= 0">
				{{ticketListNum > 0?'选择优惠券':'暂无优惠券'}}
			</view>
			<view class="plat-cover-price" v-else style="font-size: 24rpx;">- ￥{{orderAmount.discountMoney||'0.00'}}</view>
		</view>

		<view class="plat-fixed-foot plat-order-foot">
			<view class="flex" style="align-items: baseline;">
				<view class="f24" style="color:var(--plat-text-sub);">实付款：</view>
				<view class="plat-cover-price"><text class="num" style="font-size: 38rpx;">¥{{orderAmount.payMoney||'0.00'}}</text></view>
			</view>
			<view class="plat-btn plat-order-foot__btn" @click="showPopup">确定并支付</view>
		</view>

		<!-- 支付弹框 -->
		<up-popup :show="payShow" @close="payShow = false" :closeOnClickOverlay="true" round="12" :closeable="true">
			<view class="plat-form-card padding" style="margin:0;border-radius:24rpx 24rpx 0 0;">
				<view class="plat-form-card__title margin-bottom-lg">支付方式</view>
				<view class="plat-pay-item" :class="{'plat-pay-item--active': payWay == item.id}" v-for="(item, index) in payList"
					:key="index" @click="selectWay(item.id)">
					<view class="plat-pay-item__left">
						<image :src="item.image" mode="aspectFill" class="plat-pay-item__icon"></image>
						<up-radio-group v-model="payWay" iconPlacement="right" class="plat-pay-item__radio" @change='selectWay(item.id)'>
							<up-radio :label="item.name" :name="item.id" activeColor="#2C8EFE" inactiveColor="#CBD5E1">
							</up-radio>
						</up-radio-group>
					</view>
				</view>
				<view class="plat-pay-item" :class="{'plat-pay-item--active': isBalance}"
					v-if="walletInfo.canCashMoney > 0" @click='selectWay(5)'>
					<view class="plat-pay-item__left">
						<image src="/my/static/jb.png" class="plat-pay-item__icon"></image>
						<view class="plat-pay-item__label">余额</view>
					</view>
					<view class="plat-pay-item__right">
						<image src="/subpackage/static/detailImage/weixuanzhongyuanquan (2).png" mode=""
							class="plat-pay-item__check" v-if="!isBalance"></image>
						<image src="/my/static/xuanze (1).png" mode="" v-if="isBalance"
							class="plat-pay-item__check"></image>
					</view>
				</view>
				<view class="plat-btn payBtn" @click="addOrder">立即支付</view>
			</view>
		</up-popup>

		<up-popup :show="invoiceShow" @close="invoiceShow = false" :closeOnClickOverlay="true">
			<view class="plat-form-card padding invoicePanel" style="margin:0;border-radius:24rpx 24rpx 0 0;">
				<view class="plat-form-field">
					<view class="plat-form-field__label">发票抬头<text class="plat-form-required">*</text></view>
					<input type="text" placeholder="例:西安XXX有限公司" class="plat-form-field__input" v-model="taitou" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">纳税人识别号<text class="plat-form-required">*</text></view>
					<input type="text" placeholder="请填写纳税人识别号" class="plat-form-field__input" v-model="shibiehao" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">发票类型</view>
					<input type="text" placeholder="请填写发票类型" class="plat-form-field__input" v-model="types" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">发票内容</view>
					<input type="text" placeholder="请填写发票内容" class="plat-form-field__input" v-model="contents" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">发票金额<text class="plat-form-required">*</text></view>
					<view class="plat-form-field__price">{{orderAmount.payMoney||'0.00'}}元</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">注册地址</view>
					<input type="text" placeholder="请填写注册地址" class="plat-form-field__input" v-model="address" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">注册电话</view>
					<input type="number" placeholder="请填写注册电话" maxlength="11" class="plat-form-field__input" v-model="zcphone" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">开户银行</view>
					<input type="text" placeholder="请填写开户银行" class="plat-form-field__input" v-model="yh" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">银行账号</view>
					<input type="text" placeholder="请填写银行账号" class="plat-form-field__input" v-model="yhzh" />
				</view>
				<view class="plat-form-section-label" style="margin-left:0;">收件人信息</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">接收电子邮件<text class="plat-form-required">*</text></view>
					<input type="text" placeholder="用于向你发送电子发票" class="plat-form-field__input" v-model="email" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">接收手机号<text class="plat-form-required">*</text></view>
					<input type="text" placeholder="订单记录通过短信发送" class="plat-form-field__input" v-model="phone" />
				</view>
				<view class="plat-btn sureCk_btn" @click="invoiceShow = false">确认</view>
			</view>
		</up-popup>

		<!-- 日历 -->
		<up-calendar :show="calendarShow" :defaultDate="defaultDate" mode="range" @confirm="calendarConfirm" startText="入住"
			endText="离店" monthNum="6" round="12" title="选择日期" @close="calendarShow = false"></up-calendar>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		computed,
		watch
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

	const invoiceShow = ref(false);
	const roomCount = ref(1);
	const availableRoomCount = ref(null);
	const maxRoomCount = computed(() => {
		const available = Number(availableRoomCount.value)
		if (available > 0) return available
		const total = Number(roomInfo.roomCount) || 1
		return total > 0 ? total : 1
	})
	const payShow = ref(false); //支付弹框
	const payWay = ref('');
	const isBalance = ref(false);
	const payList = reactive([])
	const roomId = ref(null);
	const roomInfo = reactive({});
	const todayInfo = reactive({});
	const tomorrowInfo = reactive({});
	const userPhone = ref(null);
	const userName = ref(null);
	const discountInfo = reactive({});
	const isInvoice = ref(2);
	// 发票字段--开始
	const taitou = ref(null);
	const shibiehao = ref(null);
	const types = ref('电子发票');
	const contents = ref(null); //发票内容
	const address = ref(null); //注册地址
	const zcphone = ref(null); //注册电话
	const yh = ref(null); //开户银行
	const yhzh = ref(null); //银行账号
	const email = ref(null); //电子发票邮箱
	const phone = ref(null); //联系电话
	const isFinish = ref(true);
	const ydUserInfo = reactive({});

	// 日历相关
	const calendarShow = ref(false);
	const defaultDate = reactive([]);
	const daysNum = ref(1);

	const getStayDays = (startDate, endDate) => {
		if (!startDate || !endDate) {
			return 1
		}
		const [startYear, startMonth, startDay] = startDate.split('-').map(Number)
		const [endYear, endMonth, endDay] = endDate.split('-').map(Number)
		const start = new Date(startYear, startMonth - 1, startDay)
		const end = new Date(endYear, endMonth - 1, endDay)
		const diffDays = Math.round((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24))

		return Math.max(1, diffDays)
	}

	const currentRoomPrice = computed(() => {
		if (walletInfo.isVip == 1) {
			return Number(roomInfo.memberMoney) || Number(roomInfo.money) || 0
		}
		return Number(roomInfo.money) || 0
	})

	const orderAmount = computed(() => {
		const price = currentRoomPrice.value
		const count = Number(roomCount.value) || 0
		const stayDays = Math.max(Number(daysNum.value) || 1, 1)
		const discount = Number(discountInfo.money) || 0

		const orderMoney = parseFloat((price * count * stayDays).toFixed(2))
		const actualDiscount = parseFloat(Math.min(discount, orderMoney).toFixed(2))
		const actualPayMoney = parseFloat(Math.max(0, orderMoney - actualDiscount).toFixed(2))

		return {
			orderMoney,
			discountMoney: actualDiscount,
			payMoney: actualPayMoney,
			saveMoney: actualDiscount
		}
	})

	watch(() => isInvoice.value, (newVal) => {
		if (newVal == 2) {
			clearInvoiceInfo()
		}
	})

	const clearInvoiceInfo = () => {
		taitou.value = null
		shibiehao.value = null
		types.value = '电子发票'
		contents.value = null
		address.value = null
		zcphone.value = null
		yh.value = null
		yhzh.value = null
		email.value = null
		phone.value = null
	}

	const showPopup = () => {
		if (!ydUserInfo.id) {
			uni.showToast({
				title: '请选择预约人信息',
				icon: 'none'
			})
			return
		}
		if (!roomCount.value || roomCount.value < 1) {
			uni.showToast({
				title: '请选择房间数量',
				icon: 'none'
			})
			return
		}
		if (roomCount.value > maxRoomCount.value) {
			uni.showToast({
				title: `最多可预订${maxRoomCount.value}间`,
				icon: 'none'
			})
			return
		}
		payShow.value = true
	}

	const submit = () => {
		if (!taitou.value) {
			uni.showToast({
				title: '请输入发票抬头',
				icon: 'none'
			})
			return
		}
		if (!shibiehao.value) {
			uni.showToast({
				title: '请输入纳税人识别号',
				icon: 'none'
			})
			return
		}
		if (!email.value) {
			uni.showToast({
				title: '请输入接收电子邮件',
				icon: 'none'
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: '请输入接收手机号',
				icon: 'none'
			})
			return
		}
		invoiceShow.value = false
	};

	const calendarConfirm = (e) => {
		calendarShow.value = false
		todayInfo.api = e[0]
		todayInfo.display = e[0].slice(5).replace('-', '月') + '日'
		tomorrowInfo.api = e[e.length - 1]
		tomorrowInfo.display = e[e.length - 1].slice(5).replace('-', '月') + '日'
		daysNum.value = getStayDays(todayInfo.api, tomorrowInfo.api)
		syncAvailableRoomCount()
	};

	const normalizeMoney = (value) => {
		const num = Number(value)
		return Number.isFinite(num) ? num : 0
	}

	const clampRoomCount = () => {
		if (roomCount.value > maxRoomCount.value) {
			roomCount.value = maxRoomCount.value
		}
		if (roomCount.value < 1) {
			roomCount.value = 1
		}
	}

	const syncAvailableRoomCount = () => {
		if (!roomInfo.shopId || !roomId.value || !todayInfo.api || !tomorrowInfo.api) {
			return
		}

		const params = {
			shopId: roomInfo.shopId,
			startDate: todayInfo.api,
			endDate: tomorrowInfo.api,
			page: 1,
			limit: 200,
			aggregate: 1
		}
		if (roomInfo.roomTypeId) {
			params.roomTypeIds = String(roomInfo.roomTypeId)
		}

		proxy.$http.getT('/app/shopsRoom/getShopRoomList', params).then(res => {
			if (res.code !== 0 || !Array.isArray(res.data?.records)) {
				return
			}

			const records = res.data.records
			let matched = records.find(item => item.roomId == roomId.value)
			if (!matched) {
				matched = records.find(item =>
					item.roomTypeId == roomInfo.roomTypeId &&
					normalizeMoney(item.money) === normalizeMoney(roomInfo.money) &&
					normalizeMoney(item.memberMoney) === normalizeMoney(roomInfo.memberMoney)
				)
			}

			const residue = Number(matched?.residueCount)
			const total = Number(matched?.totalRoomCount ?? matched?.roomCount)
			const fallback = Number(roomInfo.roomCount) || 1
			const nextMax = residue > 0 ? residue : (total > 0 ? total : fallback)

			availableRoomCount.value = nextMax > 0 ? nextMax : 1
			clampRoomCount()
		})
	}

	const addOrder = () => {
		if (!isFinish.value) return;
		if (!roomCount.value || roomCount.value < 1) {
			uni.showToast({
				title: '请选择房间数量',
				icon: 'none'
			})
			return
		}
		if (roomCount.value > maxRoomCount.value) {
			uni.showToast({
				title: `最多可预订${maxRoomCount.value}间`,
				icon: 'none'
			})
			return
		}
		isFinish.value = false

		let data = {
			userId: uni.getStorageSync('userId'),
			roomId: roomId.value,
			shopId: roomInfo.shopId,
			realName: ydUserInfo.infoName,
			userPhone: ydUserInfo.infoPhone,
			roomCount: roomCount.value,
			startDate: todayInfo.api,
			endDate: tomorrowInfo.api,
			roomTypeId: roomInfo.roomTypeId,
			couponId: discountInfo.id,
			couponMoney: discountInfo.money,
			orderMoney: orderAmount.value.orderMoney,
			payMoney: orderAmount.value.payMoney,
			checkInvoice: isInvoice.value,
			userName: walletInfo.userName,
			userPhone: walletInfo.phone,
			userInfoId: ydUserInfo.id
		}
		if (isInvoice.value == 1) {
			data.type = 1
			data.creditCode = shibiehao.value
			data.address = address.value
			data.phone = zcphone.value
			data.openBank = yh.value
			data.bankNumber = yhzh.value
			data.receiveEmail = email.value
			data.receivePhone = phone.value
			data.content = contents.value
			data.title = taitou.value
		}
		proxy.$http.postJson('/app/orders/addOrder', data).then(res => {
			if (res.code == 0) {
				orderSave(res.data.orderId)
			} else {
				isFinish.value = true
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const orderSave = (orderNo) => {
		let data = {
			ordersId: orderNo,
			orderId: orderNo,
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
					setTimeout(() => {
						uni.switchTab({
							url: '/pages/order/index'
						})
						payShow.value = false
						isFinish.value = true
					}, 100)
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
					isFinish.value = true
					payShow.value = false
				}
			})
		} else if (payWay.value == 1) {
			console.log(data, "datadatadatadatadata")
			data.wxPayParam = {
				tradeType: data.tradeType
			};
			// #ifdef H5
			proxy.$http.postT('/app/wxPay/wxPayOrder', data).then(red => {
				invokeH5WxPay(red, {
					onSuccess: () => {
						uni.hideLoading();
						uni.showToast({
							title: '支付成功',
							icon: 'none'
						});
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/order/index'
							});
							isFinish.value = true;
							payShow.value = false;
						}, 1000);
					},
					onFail: () => {
						isFinish.value = true;
						uni.hideLoading();
					}
				});
			});
			// #endif
			// #ifdef APP-PLUS
			proxy.$http.postT('/app/wxPay/wxPayOrder', data).then(red => {
				console.log(red, "redredredAPP")
				setPayment('wxpay', red.data);
			});
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
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/order/index'
							})
							isFinish.value = true
							payShow.value = false
						}, 1000)
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
				});
			});
			// #endif
		} else if (payWay.value == 2) {
			data.aliPayParam = {
				tradeType: data.tradeType
			};
			// #ifdef H5
			proxy.$http.postT('/app/aliPay/payOrder', data).then(red => {
				const div = document.createElement('div')
				div.innerHTML = red.data
				document.body.appendChild(div)
				document.forms[0].submit()
			});
			// #endif
			// #ifdef APP-PLUS
			proxy.$http.postT('/app/aliPay/payOrder', data).then(red => {
				setPayment('alipay', red.data);
			});
			// #endif
		}
	};

	const setPayment = (name, order) => {
		console.log(order, '*-*-*')
		console.log(name, '*-*namenamename*')
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
		console.log(order, "app data微信支付")
		uni.requestPayment({
			provider: name,
			orderInfo: paymentData,
			success: function(res) {
				console.log(res, "app微信支付")
				uni.hideLoading();
				uni.showToast({
					title: '支付成功',
					icon: 'none'
				})
				setTimeout(() => {
					uni.switchTab({
						url: '/pages/order/index'
					})
					isFinish.value = true
					payShow.value = false
				}, 1000)
			},
			fail: function(err) {
				isFinish.value = true
				uni.hideLoading();
				console.log(err, "app微信支付失败")
				uni.hideLoading();
			},
			complete() {
				isFinish.value = true
				uni.hideLoading();
			}
		});
	};
	const callPay = (response) => {
		if (typeof WeixinJSBridge === "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady(response), false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady(response));
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(response));
			}
		} else {
			onBridgeReady(response);
		}
	};
	const onBridgeReady = (response) => {
		if (!response.package) {
			return;
		}
		console.log(response, '++++++++')
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', {
				"appId": response.appid,
				"timeStamp": response.timestamp,
				"nonceStr": response.noncestr,
				"package": response.package,
				"signType": response.signType,
				"paySign": response.sign
			},
			function(res) {
				console.log(res, '/*-/*-/*-')
				if (res.err_msg === "get_brand_wcpay_request:ok") {
					uni.hideLoading();
					uni.showToast({
						title: '支付成功',
						icon: 'none'
					})
					setTimeout(() => {
						uni.switchTab({
							url: '/pages/order/index'
						})
						isFinish.value = true
						payShow.value = false
					}, 1000)
				} else {
					uni.hideLoading();
				}
				WeixinJSBridge.log(response.err_msg);
			}
		);
	};



	const selectWay = (id) => {
		payWay.value = id;
		if (id == 5) {
			isBalance.value = true
		} else {
			isBalance.value = false
		}
		console.log(isBalance.value, 'payWay.value ')
	};

	const getShopRoomInfo = () => {
		let data = {
			roomId: roomId.value
		}
		proxy.$http.getT('/app/shopsRoom/getShopRoomInfo', data).then(res => {
			if (res.code == 0) {
				Object.assign(roomInfo, res.data)
				syncAvailableRoomCount()
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const ticketListNum = ref(0);
	const getList = async () => {
		const data = {
			page: 1,
			limit: 999,
			status: 0,
			userId: uni.getStorageSync('userId')
		};
		proxy.$http.getT('/app/couponUser/getMyCouponList', data).then(res => {
			if (res.code == 0) {
				ticketListNum.value = res.data.total
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const walletInfo = reactive({});
	const getWalletIndo = () => {
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



	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

	const showFp = () => {
		isInvoice.value = 1
		invoiceShow.value = true
	};

	onLoad(option => {
		Object.assign(tomorrowInfo, uni.getStorageSync('tomorrowInfo', tomorrowInfo))
		Object.assign(todayInfo, uni.getStorageSync('todayInfo', todayInfo))

		if (todayInfo.api && tomorrowInfo.api) {
			defaultDate.push(todayInfo.api, tomorrowInfo.api)
			daysNum.value = getStayDays(todayInfo.api, tomorrowInfo.api)
		}

		if (option.roomId) {
			roomId.value = option.roomId
			getShopRoomInfo()
		}
		getWalletIndo()
		getList()
		// #ifdef MP-WEIXIN
		payList.splice(0, payList.length);
		payWay.value = 1;
		let data = [];
		let data1 = [{
			id: 1,
			image: 'https://renwu.xiansqx.com/img/20210813/e71382c1b8404e4f96c6d6e89d305329.png',
			name: '微信'
		}]
		payList.splice(0, payList.length, ...data1)
		// #endif


		// #ifdef APP-PLUS
		payList.splice(0, payList.length);
		payWay.value = 1;
		let data1 = [{
			id: 1,
			image: 'https://renwu.xiansqx.com/img/20210813/e71382c1b8404e4f96c6d6e89d305329.png',
			name: '微信'
		}, {
			id: 2,
			image: 'https://renwu.xiansqx.com/img/20210813/56cde02b040e453ead8b5d2c0dc17e18.png',
			name: '支付宝'
		}]
		payList.splice(0, payList.length, ...data1)
		// #endif

		// #ifdef H5
		let target = navigator.userAgent.toLowerCase();
		let isWeixin = (/micromessenger/.test(target)) ? true : false;
		payList.splice(0, payList.length);
		if (isWeixin) {
			payWay.value = 1;
			let data1 = [{
				id: 1,
				image: 'https://renwu.xiansqx.com/img/20210813/e71382c1b8404e4f96c6d6e89d305329.png',
				name: '微信'
			}]
			payList.splice(0, payList.length, ...data1)
		} else {
			payWay.value = 2;
			let data1 = [{
				id: 2,
				image: 'https://renwu.xiansqx.com/img/20210813/56cde02b040e453ead8b5d2c0dc17e18.png',
				name: '支付宝'
			}]
			payList.splice(0, payList.length, ...data1)
		}
		// #endif
	});


	onShow(() => {
		uni.$once('getDiscountId', data => {
			Object.assign(discountInfo, data)
		})
		uni.$once('selUserInfo', data => {
			Object.assign(ydUserInfo, data[0])
		})
		setTimeout(() => {
			console.log(orderAmount.value, "orderAmount")
		}, 2000)
		console.log(ydUserInfo, "ydUserInfo")
	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	.invoicePanel {
		max-height: 80vh;
		overflow-y: auto;
	}

	.sureCk_btn {
		width: 100%;
		margin-top: 40rpx;
	}

	:deep(.u-number-box__minus--disabled) {
		background-color: transparent !important;
	}

	:deep(.u-number-box__minus) {
		background-color: transparent !important;
	}

	:deep(.u-number-box__plus) {
		background-color: transparent !important;
	}

	.payBtn {
		margin-top: 50rpx;
	}

	.plat-pay-item {
		margin-top: 20rpx;
		padding: 24rpx 22rpx;
		border-radius: 18rpx;
		background: #f8fafc;
		border: 1rpx solid #e2e8f0;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 20rpx;
		box-sizing: border-box;
	}

	.plat-pay-item--active {
		background: #f3f8ff;
		border-color: rgba(44, 142, 254, 0.45);
	}

	.plat-pay-item__left {
		flex: 1;
		min-width: 0;
		display: flex;
		align-items: center;
	}

	.plat-pay-item__icon {
		width: 55rpx;
		height: 55rpx;
		margin-right: 20rpx;
		border-radius: 50%;
		flex-shrink: 0;
	}

	.plat-pay-item__label {
		font-size: 30rpx;
		color: var(--plat-text);
	}

	.plat-pay-item__radio {
		flex: 1;
		min-width: 0;
	}

	.plat-pay-item__right {
		flex-shrink: 0;
		display: flex;
		align-items: center;
	}

	.plat-pay-item__check {
		width: 38rpx;
		height: 38rpx;
		border-radius: 50%;
	}

	:deep().u-checkbox {
		margin: 0rpx !important;
	}

	:deep().u-radio__text {
		font-size: 28rpx !important;
		color: var(--plat-text) !important;
	}

	:deep().u-radio {
		width: 100%;
		margin: 0 !important;
	}

	:deep().u-radio__label-wrap {
		flex: 1;
	}

	:deep().u-radio__icon-wrap {
		border-color: var(--plat-line) !important;
	}

	.plat-order-page > .plat-form-card:not(.invoicePanel) .plat-form-field {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 24rpx;
	}

	.plat-order-page > .plat-form-card:not(.invoicePanel) .plat-form-field__label {
		flex-shrink: 0;
	}

	.plat-order-page > .plat-form-card:not(.invoicePanel) .plat-form-field__value,
	.plat-order-page > .plat-form-card:not(.invoicePanel) .plat-order-link {
		flex: 1;
		min-width: 0;
		display: flex;
		justify-content: flex-end;
		text-align: right;
	}

	.plat-order-room-count {
		flex-direction: column;
		align-items: flex-end;
		gap: 8rpx;
	}

	.plat-order-room-count__btn {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 50rpx;
		height: 50rpx;
	}

	.plat-order-room-count__num {
		width: max-content;
		min-width: 48rpx;
		text-align: center;
		font-size: 38rpx;
		font-weight: 500;
	}

	.plat-order-room-count__hint {
		font-size: 22rpx;
		color: var(--plat-text-sub);
		line-height: 1.4;
	}

	.input {
		padding: 0 10px;
	}
</style>
