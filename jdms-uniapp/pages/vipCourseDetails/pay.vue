<template>
	<view class="">
		<u-navbar :back-text="$t('repeatUse.back')"></u-navbar>
		<view class="pay">
			<view class="" style="text-align: center;padding-top: 100rpx;font-size: 24rpx;">
				{{$t('pagesText.syzfsj')}}：<u-count-down :timestamp="timerdown" color="#333333" font-size="24" @end="downEnd"
					:show-hours="false"></u-count-down>
			</view>
			<view class="pay_time flex-c alic">
				<v-price-format :custom-style="{ fontSize: '40rpx' }"><text
						style="font-size: 70rpx;">{{Number(payMoney).toFixed(2)}}</text></v-price-format>
			</view>
			<view class="card">
				<u-radio-group v-model="payValue" @change="radioGroupChange">
					<u-radio name="Paypal" v-if="isPaypal == '是'">
						<view class="flex alic payline" style="flex:1;">
							<image src="/static/images/zhifubao.png" style="width: 70rpx;height:70rpx" mode="scaleToFill" />
							<text style="font-size: 28rpx;margin-left: 14rpx;font-weight: bold;">Paypal</text>
						</view>
					</u-radio>
					<u-radio name="Stripe" v-if="isSripe== '是'">
						<view class="flex alic payline" style="flex:1;">
							<image src="/static/images/icon_weixin.png" style="width: 70rpx;height:70rpx" mode="scaleToFill" />
							<text style="font-size: 28rpx;margin-left: 14rpx;font-weight: bold;">Stripe</text>
						</view>
					</u-radio>
				</u-radio-group>
			</view>
			<v-button width="686rpx" height="88rpx" @click="confirm">{{$t('gwc.pay')}}</v-button>
		</view>
	</view>

</template>


<script>
	export default {

		data() {
			return {
				payValue: 'Paypal',
				payForm: {
					indentId: '',
					classify: ''
				},
				ticketList: [],
				orderInfo: {},
				show: 0,
				timerdown: 0, //倒计时时间
				payMoney: 0.00,
				isPaypal: uni.getStorageSync('isPaypal'),
				isSripe: uni.getStorageSync('isSripe')
			}
		},
		methods: {
			// 订单支付倒计时
			orderTimerDown() {
				let orderTime = new Date(Date.parse(this.orderInfo.createTime));
				const expirationTime = new Date(orderTime.getTime() + 30 * 60 * 1000);
				const currentTime = new Date();
				this.timerdown = Math.max(0, this.dateToSeconds(expirationTime) - this.dateToSeconds(currentTime));
			},
			dateToSeconds(dateString) {
				var date = new Date(dateString);
				if (isNaN(date.getTime())) {
					return null;
				}
				// 转换为毫秒并除以 1000 得到秒数
				var seconds = Math.floor(date.getTime() / 1000);
				return seconds;
			},
			downEnd() {
				this.timerdown = 0
				if (this.timerdown < 0) {
					uni.showToast({
						title: this.$t('pagesText.cxgmsp'),
						icon: 'none',
						mask: true
					})
					setTimeout(() => {
						uni.navigateBack()
					}, 1500)
				}
			},
			radioGroupChange(e) {
				this.$set(this.payForm, 'indentId', this.orderInfo.indentId)
				console.log(this.orderInfo, "this.orderInfo.indentId")
				console.log(e, 'group');
			},
			confirm() {
				this.$set(this.payForm, 'indentId', this.orderInfo.indentId)
				console.log(this.orderInfo, "this.orderInfo")

				uni.showLoading({
					title: this.$t('loading.zfz') + '...'
				});
				this.showPopup = false
				let that = this;

				let payWay = 1;
				// #ifdef APP
				payWay = 2;
				// #endif
				// #ifdef H5
				payWay = 1;
				// #endif
				let payData = {
					classify: this.payValue == 'Paypal' ? 6 : 7, // 6paypal 7stripe
					payWay: payWay, //1H5 2APP
					indentId: this.orderInfo.indentId,
					successUrl: '/pages/order/order',
					cancelUrl: '/pages/order/order',
				}
				if (this.payValue === 'Paypal') {
					this.$Request.postT("/app/topicIndent/payIndent", payData).then(red => {
						if (red.code == 0) {
							// #ifdef H5
							uni.hideLoading();
							window.location.href = red.data; //H5端跳转到paypal支付页面
							// #endif
							// #ifdef APP-PLUS
							let clientId = uni.getStorageSync('paypalClientId')
							let orderInfo = {
								"clientId": clientId, //开发者中心clientId
								"orderId": red.data, //订单id
								"userAction": "continue", //按钮样式 paynow/continue
								"currency": "USD", //币种
								"environment": "sandbox" //运行环境 sandbox(沙盒) / live(线上)
							};
							uni.getProvider({
								service: 'payment',
								success: function(res) {
									console.log(res, orderInfo, 'orderInfoorderInfoorderInfo')
									uni.requestPayment({
										"provider": "paypal",
										"orderInfo": orderInfo,
										success: function(response) {
											uni.showLoading({
												title: that.$t('loadText.jzz')
											})
											that.$Request.getT(
												'/app/paypal/callback?token=' +
												red.data).then(ret => {
												console.log(ret)
												if (ret === 'ok') {
													setTimeout(function() {
														uni.showToast({
															title: that.$t('pagesText.paySuccess'),
															icon: 'success'
														})
														setTimeout(function() {
															uni.switchTab({
																url: '/pages/order/order'
															})
														}, 1000)
													}, 1000);
													uni.hideLoading();
												} else {
													uni.hideLoading();
													uni.showToast({
														title: that.$t('pagesText.payFail'),
														icon: 'none'
													})
												}
											});
										},
										fail: function(err) {
											console.log('fail:' + JSON.stringify(
												err));
											uni.showToast({
												title: that.$t('pagesText.payFail'),
												icon: 'none'
											})
										}
									});
								},
								fail(e) {
									console.log(e)
								}
							});
							// #endif
						} else {
							uni.showToast({
								icon: 'none',
								title: red.msg
							});
						}
					});
				} else {
					this.$Request.postT("/app/topicIndent/payIndent", payData).then(red => {
						if (red.code == 0) {
							// #ifdef H5
							uni.hideLoading();
							window.location.href = red.data; //H5端跳转到stripe支付页面
							// #endif
							// #ifdef APP-PLUS
							//订单对象
							var orderInfo = {
								"customer": "", //Customer
								"ephemeralKey": "", //临时访问Customer的Key
								"isAllowDelay": true, //是否支持延迟支付  默认false
								"merchantName": "DCloud", //商户名
								"paymentIntent": "" + red.data, //订单信息
								"publishKey": "" + publishKey, //公钥
								"billingDetails": { //账单信息(可选)
									"name": "",
									"email": "",
									"phone": "",
									"address": {
										"city": "",
										"country": "CN", //国家代码(ISO 3166-1 alpha-2)
										"line1": "",
										"line2": "",
										"postalCode": "",
										"state": ""
									}
								}
							};
							//发起支付
							uni.getProvider({
								service: 'payment',
								success: function(res) {
									uni.requestPayment({
										"provider": "stripe",
										"orderInfo": orderInfo,
										success(res) {
											setTimeout(function() {
												uni.showToast({
													title: that.$t('pagesText.paySuccess'),
													icon: 'success'
												})
												setTimeout(function() {
													uni.switchTab({
														url: '/pages/order/order'
													})
												}, 1000)
											}, 1000);
											uni.hideLoading();
										},
										fail: function(err) {
											console.log('fail:' + JSON.stringify(
												err));
											uni.showToast({
												title: that.$t('pagesText.payFail'),
												icon: 'none'
											})
										}
									});
								},
								fail: function(err) {
									console.log('fail:' + JSON.stringify(
										err));
								}
							});
							// #endif
						} else {
							uni.showToast({
								icon: 'none',
								title: red.msg
							});
						}
					});
				}
			},
			paymentAPI(provider, orderInfo, deconstruct = false) {
				deconstruct && (orderInfo = {
					timeStamp: orderInfo.timestamp,
					nonceStr: orderInfo.noncestr,
					package: orderInfo.package,
					signType: orderInfo.signType,
					paySign: orderInfo.sign,
				})
				uni.requestPayment({
					provider,
					// #ifndef MP-WEIXIN
					orderInfo,
					// #endif
					// #ifdef MP-WEIXIN
					...orderInfo,
					// #endif
					success(res1) {
						console.log(res1);
						uni.hideLoading()
						uni.switchTab({
							url: '/pages/order/order'
						})
					},
					fail(err) {
						console.log(err);
					}
				})
			},
			choosePayMethod(orderInfo, deconstruct) {
				console.log()
				switch (this.payForm.classify) {
					case '1': // app内微信支付
						this.paymentAPI('wxpay', orderInfo)
						break;
					case '2': // 公众号支付
						this.callPay(orderInfo)
						break;
					case '3':
						this.paymentAPI('wxpay', orderInfo, deconstruct)
						break;
					case '4':
						this.paymentAPI('alipay', orderInfo)
						break;
					case '5': // 支付宝H5
						const div = document.createElement('div')
						div.innerHTML = orderInfo //此处form就是后台返回接收到的数据
						document.body.appendChild(div)
						document.forms[0].submit()
						break;
				}
			},
			callPay: function(response) {
				if (typeof WeixinJSBridge === "undefined") {
					if (document.addEventListener) {
						document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady(response), false);
					} else if (document.attachEvent) {
						document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady(response));
						document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady(response));
					}
				} else {
					this.onBridgeReady(response);
				}
			},
			onBridgeReady: function(response) {
				let that = this;
				// if (!response.package) {
				// 	return;
				// }
				console.log(response, 111111)
				WeixinJSBridge.invoke(
					'getBrandWCPayRequest', {
						"appId": response.appid, //公众号名称，由商户传入
						"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
						"nonceStr": response.noncestr, //随机串
						"package": response.package,
						"signType": response.signType, //微信签名方式：
						"paySign": response.sign //微信签名
					},
					function(res) {
						console.log(res, '/*-/*-/*-')
						if (res.err_msg === "get_brand_wcpay_request:ok") {
							// 使用以上方式判断前端返回,微信团队郑重提示：
							//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
							uni.showLoading({
								title: '支付成功'
							});
							setTimeout(function() {
								uni.hideLoading();
								uni.navigateBack()
							}, 1000);
						} else {
							uni.hideLoading();
						}
						WeixinJSBridge.log(response.err_msg);
					}
				)
			}
		},
		onLoad(option) {
			if (option.order) {
				this.orderInfo = JSON.parse(decodeURIComponent(option.order))
				console.log(this.orderInfo, 'orderInfo')
				// this.orderInfo = (option.order);
				this.payMoney = option.payMoney;
				console.log(this.payMoney, 'this.payMoney')
			}
			if (!this.orderInfo) {
				uni.showToast({
					title: this.$t('pagesText.ddyc'),
					icon: 'none',
					mask: true
				})
				setTimeout(() => {
					uni.switchTab({
						url: '/pages/index/index'
					})
				}, 1500);
			}
			this.orderTimerDown()
			// this.$store.commit('orderModule/setTemporaryIndentMoney', this.orderInfo.indentMoney)
		}
	}
</script>


<style lang="scss" scoped>
	page {
		background: #F5F5F5;
	}

	/deep/ .u-countdown-item {
		background-color: #F5F5F5 !important;
	}

	/deep/ .u-radio__icon-wrap {
		width: 38rpx !important;
		height: 38rpx !important;
	}

	.pay {
		// position: relative;
		margin-top: 100rpx;
	}

	/deep/.btn {
		margin: 0 !important;
		position: fixed;
		bottom: 45rpx;
		left: 50%;
		transform: translate(-50%);
		border-radius: 12rpx;
	}

	.card {
		background: #fff;
		border-radius: 16rpx;
		width: calc(100% - 64rpx);
		margin: 0 auto;
		margin-bottom: 12rpx;

		.title {
			color: #999;
			padding: 28rpx;
			padding-bottom: 0;
		}
	}

	.pay_time {
		padding-bottom: 55rpx;

		text {
			margin-bottom: 40rpx;
		}
	}

	.time {
		font-size: 24rpx;
		color: #333;
	}

	.shop_name {
		font-size: 24rpx;
		color: #999;
	}

	/deep/.u-radio-group {
		width: 100% !important;
		box-sizing: border-box;
		padding: 0 24rpx;

		.u-radio {
			padding: 40rpx 0;
			width: 100% !important;
			flex-direction: row-reverse;
		}

		.u-radio__label {
			flex: 1;
			position: relative;

		}
	}
</style>