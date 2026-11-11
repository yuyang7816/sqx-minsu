<template>
	<view class="vip container">
		<u-navbar :back-text="$t('pagesText.hyzx')" :background="{ background: '#000' }"
			:back-text-style="{ color: '#fff' }" back-icon-color="#fff" :border-bottom="false"></u-navbar>
		<view class="content">
			<view class="back"></view>
			<view class="vip_card" style="height: 318rpx;">
				<view style="font-weight: bold;font-size: 36rpx;color: #836432;">{{$t('pagesText.openVip')}}</view>
				<view style="font-size: 24rpx;color: #AC8B54;margin-top: 12rpx;">{{$t('pagesText.zsqy')}}</view>
				<!-- <view class="btn">
					<v-button bg-color="linear-gradient(0deg, #292723 0%, #433E36 100%)" color="#fff"
						border-color="transparent">立即加入</v-button>
				</view> -->
				<image class="vip_" src="../../static/images/vip_pic.png" style="width: 176rpx;height: 156rpx;"
					mode="scaleToFill" />
				<view style="margin-top: 32rpx;color: #AC8B54;" v-if="user.isVip">{{$t('pagesText.dqsj')}}：{{ user.vipEndTime }}
				</view>
			</view>
			<scroll-view style="white-space: nowrap;flex-wrap: nowrap;" :scroll-x="true" :scroll-with-animation="true"
				:scroll-into-view="scrollIntoView">
				<view class="check_vip flex sb">
					<view class="item flex-c alic" :class="{ item_active: index === current }" v-for="(item, index) in vipList"
						:key="index" :id="`vip_${index}`" @click="chooseThis(index)">
						<view>{{ item.vipName }}</view>
						<v-price-format :custom-style="{ color: '#333', fontSize: '38rpx' }">{{ item.money }}</v-price-format>
					</view>
				</view>
			</scroll-view>
			<view class="activate bt f flex jc alic" @click="toActivate">
				{{$t('pagesText.liji')}}{{ user.isVip ? $t('pagesText.xf') : $t('repeatUse.kt') }}
			</view>
			<view class="flex jc alic " style="color:#999;">
				<u-checkbox-group active-color="#139DFF" width="650rpx" shape="circle">
					<u-checkbox v-model="value">
						<view class="flex alic flex-wrap" style="font-size: 26rpx;">
							{{$t('order.yixi')}}
							<navigator url="/my/setting/xieyi?type=417" class="ft">《{{$t('pageText.hyfwxy')}}》</navigator>
							{{$t('set.ji')}}
							<navigator url="/my/setting/xieyi?type=418" class="ft">《{{$t('pageText.wfsygz')}}》</navigator>
						</view>
					</u-checkbox>
				</u-checkbox-group>
			</view>
		</view>
		<view class="vip_desc">
			<view class="title flex jc alic">
				<image src="../../static/images/icon_l.png" mode="scaleToFill" />
				<text>{{$t('pageText.yhtq')}}</text>
				<image src="../../static/images/icon_r.png" mode="scaleToFill" />
			</view>
			<view class="flex sa alic">
				<view class="item flex-c alic" v-for="(item,index) in bannerList">
					<image :src="item.imageUrl" mode="scaleToFill" />
					<text>{{item.name}}</text>
				</view>
			</view>
		</view>
		<u-popup v-model="showPopup" border-radius="26" mode="bottom">
			<view class="popup">
				<view class="price_header flex jc alic"><text style="font-size: 60rpx;">{{$t('unit.yuan')}}</text>{{payMoney}}
				</view>
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
				<v-button width="calc(100% - 48rpx)" height="90rpx" @click="confirm">{{$t('btnText.qd')}}</v-button>
			</view>
		</u-popup>
	</view>
</template>


<script>
	export default {
		data() {
			return {
				current: 0,
				check: true,
				value: false,
				payValue: 'Paypal',
				vipList: [],
				scrollIntoView: '',
				showPopup: false,
				bannerList: [],
				user: {},
				payMoney: '',
				payId: '',
				isPaypal: uni.getStorageSync('isPaypal'),
				isSripe: uni.getStorageSync('isSripe')
			}
		},
		methods: {
			radioGroupChange(e) {
				this.payValue === e
				console.log(e, "eeeeeeeeeee")
			},
			toActivate() {
				if (!this.value) return uni.showToast({
					title: this.$t('pageText.xtyfwxy'),
					icon: 'none'
				})
				this.showPopup = true
			},
			chooseThis(index) {
				this.current = index
				this.$nextTick(() => {
					this.scrollIntoView = `vip_${index}`
				})
				this.payMoney = this.vipList[this.current].money.toFixed(2) || '0.00'
				this.payId = this.vipList[this.current].id
			},
			confirm() {
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
					id: this.payId,
					successUrl: '/subPackage/vip/vip',
					cancelUrl: '/subPackage/vip/vip',
				}
				if (this.payValue === 'Paypal') {
					this.$Request.postT("/app/vipDetails/userBuyVip", payData).then(red => {
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
															uni.navigateBack()
														}, 1000)
													}, 1000);
													uni.hideLoading();
												} else {
													uni.hideLoading();
													that.isTrue = true
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
											that.isTrue = true
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
					this.$Request.postT("/app/vipDetails/userBuyVip", payData).then(red => {
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
													uni.navigateBack()
												}, 1000)
											}, 1000);
											uni.hideLoading();
										},
										fail: function(err) {
											console.log('fail:' + JSON.stringify(
												err));
											that.isTrue = true
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
				this.getUserInfoById()
			},
			getUserInfoById() {
				uni.showLoading({
					title: this.$t('loading.qsh') + '...'
				})
				if (uni.getStorageSync('userId')) {
					this.$Request.getT('/app/user/selectUserById', {
						userId: uni.getStorageSync('userId')
					}).then(res => {
						uni.hideLoading()
						this.user = res.data
						uni.setStorageSync('userInfo', res.data)
					})
				}
			},
			// 底部会员特权
			getHomeBannerList() {
				this.$Request.getT('/app/banner/selectBannerList', {
					classify: 10,
					state: 1
				}).then(res => {
					this.bannerList = res.data
				})
			},
			// 获取vip充值类别
			getVipPayInfoList() {
				this.$Request.getT('/app/vipDetails/getVipDetailsList', {
					page: 1,
					limit: 10
				}).then(res => {
					this.vipList = res.data.records
					if (this.current == 0) {
						this.payMoney = this.vipList[this.current].money.toFixed(2) || '0.00'
						this.payId = this.vipList[this.current].id
					}
				})
			},
		},
		onLoad() {
			this.getHomeBannerList();
			this.getVipPayInfoList()
		},
		onShow() {
			this.getUserInfoById()
		}
	}
</script>


<style lang="scss" scoped>
	/deep/.btn {
		border-radius: 50rpx;
		margin: 60rpx auto;
		margin-bottom: 24rpx;
		font-size: 32rpx;
		font-weight: bold;
	}

	/deep/ .u-checkbox__label {
		margin-right: 0rpx;
	}

	.price_header {
		font-size: 80rpx;
		color: red;
		position: relative;
		padding-top: 100rpx;
		padding-bottom: 50rpx;

		&::before {
			content: '';
			position: absolute;
			bottom: 0;
			width: 95%;
			height: 2rpx;
			background: #ccc;
		}
	}

	.popup {
		/deep/.u-radio-group {
			width: 100% !important;
			box-sizing: border-box;
			padding: 0 24rpx;
			margin-bottom: 100rpx;

			.u-radio {
				padding: 24rpx 0;
				width: 100% !important;
				flex-direction: row-reverse;
			}

			.u-radio__label {
				flex: 1;
				position: relative;

			}
		}
	}

	/deep/.u-checkbox {
		overflow: unset !important;
	}

	.navigator-hover {
		opacity: 1 !important;
		background: transparent !important;
	}

	.vip {
		.content {
			z-index: 22;
			position: relative;
			margin-bottom: 24rpx;
			background: #fff;
			padding-bottom: 32rpx;
		}

		.back {
			position: fixed;
			left: 0;
			right: 0;
			top: 0;
			height: 350rpx;
			background: linear-gradient(180deg, #3BAEFF 0%, #000 0%, #000 100%);
			z-index: 0;
			border-radius: 0 0 375rpx 375rpx/0 0 70rpx 70rpx;
		}

		&_card {
			z-index: 555;
			margin: 44rpx auto;
			width: 686rpx;
			// height: 318rpx;
			background: linear-gradient(0deg, #FEF2DC 0%, #FEE4BF 100%);
			box-shadow: 0rpx 1rpx 32rpx 0rpx rgba(0, 0, 0, 0.07);
			border-radius: 24rpx;
			box-sizing: border-box;
			padding: 54rpx 60rpx;
			position: relative;

			.btn {
				position: absolute;
				bottom: 25rpx;
				left: 30rpx;

				/deep/view {
					margin: 0 !important;
				}
			}

			.vip_ {
				position: absolute;
				width: 176rpx;
				height: 156rpx;
				right: 72rpx;
				bottom: 60rpx;
			}
		}

		scroll-view {
			padding: 45rpx 32rpx;
			padding-top: 0;
			box-sizing: border-box;
		}

		.check_vip {

			.item_active {
				background: #EEF8FF !important;
				border: 2rpx solid $theme-color !important;
			}

			.item {
				padding-top: 62rpx;
				padding-bottom: 80rpx;
				border-radius: 30rpx;
				width: 213rpx;
				background-color: #fff;
				border: 2rpx solid #E6E6E6;
				height: 230rpx;
				box-sizing: border-box;
				min-width: 213rpx;
				margin-right: 40rpx;

				view {
					&:first-child {
						margin-bottom: 26rpx;
						font-weight: 700;
					}
				}
			}

			.active_ {
				background-color: #EEF8FF;
				border: 2rpx solid $theme-color;
			}

		}

		.activate {
			font-size: 28rpx;
			width: 597rpx !important;
			height: 82rpx;
			margin: 0 auto;
			margin-bottom: 20rpx;
			border-radius: 42rpx;
		}

		.check {
			font-size: 24rpx;
			width: 28rpx;
			height: 28rpx;
			border-radius: 50%;
			transition: background .3s;
		}

		.read_text {
			margin-left: 8rpx;
			position: relative;
			transform-origin: left center;
			transform: scale(.82);
		}

		.vip_desc {
			width: 100%;
			height: 273rpx;
			background: #fff;

			.title {
				padding: 44rpx 0;
				color: $theme-color;
				font-weight: 700;

				&>text {
					margin: 0 10rpx;
				}

				image {
					width: 24rpx;
					height: 22rpx;
				}
			}

			.item {
				image {
					width: 80rpx;
					height: 80rpx;
					margin-bottom: 12rpx;
				}

				text {
					font-size: 26rpx;
					color: #252830;
				}
			}
		}
	}

	/deep/.u-radio__label {
		margin: 0;
	}
</style>