<template>
	<view class="order_card" @click="toDetail">
		<view class="title flex sb alic">
			<view class="icon flex alic">
				<image :src="detail.indentType == 1 ? play : icon_kc" mode="scaleToFill" />
				<text
					style="font-size: 28rpx;">{{ detail.indentType == 1 ? $t('pagesText.spkc') : $t('pagesText.zlkc') }}</text>
			</view>
			<text class="ft" style="font-size: 26rpx;">{{ statusText }}</text>
		</view>

		<Cell :content="goodInfo" />
		<view class="actual flex alic" style="justify-content: flex-end;" v-if="detail.isPay == 1">
			<view>{{$t('pagesText.sf')}}</view>
			<v-price-format :custom-style="{ fontSize: '36rpx' }">{{ detail.payMoney }}</v-price-format>
		</view>
		<view class="operate_btns flex jce" v-show="detail.indentStatus != null">
			<v-button border-color="#999" bg-color="#fff" color="#666" @click.native.stop="handleClick('联系客服')"
				v-if="detail.indentStatus == 0 || detail.indentStatus == 3">{{$t('repeatUse.lxkf')}}</v-button>
			<v-button border-color="#999" bg-color="#fff" color="#666" @click.native.stop="handleClick('取消订单')"
				v-if="detail.indentStatus == 0 || detail.indentStatus == 2">{{$t('btn.wxdd')}}</v-button>
			<v-button border-color="#999" bg-color="#fff" color="#666" @click.native.stop="handleClick('提醒发货')"
				v-if="detail.indentStatus == 2">{{$t('order.txfh')}}</v-button>
			<v-button border-color="transparent" bg-color="#0096FF" color="#fff" @click.native.stop="handleClick('立即支付')"
				v-if="detail.indentStatus == 0">{{$t('repeatUse.payNow')}}</v-button>
			<v-button border-color="transparent" bg-color="#0096FF" color="#fff" @click.native.stop="handleClick('继续学习')"
				v-if="detail.indentStatus == 3 && dorpIndex == 1">{{$t('pagesText.jxxx')}}</v-button>
			<v-button border-color="transparent" bg-color="#0096FF" color="#fff" @click.native.stop="handleClick('确认收货')"
				v-if="detail.indentStatus == 5">{{$t('order.qrsh')}}</v-button>
			<!-- 	<v-button border-color="transparent" bg-color="#0096FF" color="#fff" @click.native.stop="handleClick('再次购买')"
				v-if="detail.indentStatus == status[3] && !(detail.indentMoney == 0)">再次购买</v-button> -->
			<v-button border-color="transparent" bg-color="#0096FF" color="#fff" @click.native.stop="handleClick('去评价')"
				v-if="detail.indentStatus == 3 && detail.isEvaluate != 1">{{$t('order.qpj')}}</v-button>
		</view>
	</view>
</template>


<script>
	import Cell from './Cell.vue'
	import play from '@/static/images/play.png'
	import icon_kc from '@/static/images/icon_kc.png'

	export default {
		components: {
			Cell
		},
		computed: {
			goodInfo() {
				return {
					title: this.detail.title,
					payMoney: this.detail.payMoney,
					coverImg: this.detail.coverImg,
					indentMoney: this.detail.indentMoney,
					nowBuyMoney: this.detail.nowBuyMoney,
					isGroup: this.detail.isGroup,
					isVip: this.detail.isVip
				}
			},
			statusText() {
				switch (this.detail.indentStatus) {
					case 0:
						return this.$t('pagesText.ddmjfk');
					case 1:
						return this.$t('pagesText.ptz')
					case 2:
						return this.$t('order.dfh')
					case 3:
						return this.$t('order.ywc')
					case 4:
						return this.$t('repeatUse.yqx')
					case 5:
						return this.$t('repeatUse.yfh')
				}
			}
		},
		data() {
			return {
				play,
				icon_kc,
			}
		},
		props: {
			detail: {
				type: Object,
				required: true
			},
			dorpIndex: {
				type: Number,
				required: true
			},
			tabIndex: {
				type: Number,
				required: true
			}
		},
		methods: {
			toDetail() {
				if (uni.getStorageSync('userId')) {
					uni.navigateTo({
						url: `/pages/order/orderDetail?indentId=${this.detail.indentId}&routerType=1`
					})
				} else {
					uni.showModal({
						title: this.$t('repeatUse.ts'),
						content: this.$t('repeatUse.nologin'),
						confirmText: this.$t('repeatUse.qr'),
						cancelText: this.$t('repeatUse.qx'),
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.reLaunch({
									url: '/pages/public/login'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}
			},
			customerType() {
				this.$Request.getT(`/app/common/type/` + 296).then(res => {
					if (res.code == 0) {
						console.log(res.data, "console.log(res.data.value)")
						if (res.data.value == 1) {
							this.$Request.getT(`/app/common/type/` + 298).then(res => {
								if (res.code == 0) {
									if (res.data && res.data.value) {
										uni.makePhoneCall({
											phoneNumber: res.data.value //仅为示例
										});
									}
								}
							})
						} else if (res.data.value == 3) {
							uni.navigateTo({
								url: '/my/setting/customer'
							})
						} else if (res.data.value == 2) {
							// uni.showToast({
							// 	title:''
							// })
							// 	// #ifdef MP-WEIXIN
							// 	let that = this
							// 	try {
							// 		wx.openCustomerServiceChat({
							// 			extInfo: {
							// 				url: that.$queue.getData('kefuUrl')
							// 			},
							// 			corpId: that.$queue.getData('kefuAppId'),
							// 			success(res) {},
							// 			fail(res) {
							// 				console.error(res)
							// 			}
							// 		})
							// 	} catch (error) {
							// 		console.error("catchcatch" + error)
							// 		uni.showToast({
							// 			title: '请更新至微信最新版本'
							// 		});
							// 	}
							// 	// #endif
							// 	// #ifndef MP-WEIXIN
							// 	let url = this.$queue.getData('kefuUrl');
							// 	if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
							// 		uni.navigateTo({
							// 			url
							// 		});
							// 	} else {
							// 		//#ifndef H5
							// 		uni.navigateTo({
							// 			url: '/pages/index/webView?url=' + url
							// 		});
							// 		//#endif
							// 		//#ifdef H5
							// 		window.location.href = url;
							// 		//#endif
							// 	}
							// 	// #endif
						}
					}
				})
			},
			async handleClick(e) {
				console.log(this.detail, "detail")
				switch (e) {
					case '联系客服':

						this.customerType()
						break;
					case '取消订单':
						this.$emit('show', {
							indentId: this.detail.indentId,
							status: e,
							content: this.$t('order.qdqx') + '？'
						})
						break;
					case '提醒发货':
						this.$Request.getT('/app/message/reminderOrder', {
							userId: uni.getStorageSync('userId'),
							orderNo: this.detail.indentNumber,
							topicGoodsTitle: this.detail.title
						}).then(res => {
							if (res.code == 0) {
								uni.showToast({
									title: this.$t('order.ytxsj'),
								})
							}
						})
						break;
					case '立即支付':
						let money = 0
						if (this.detail.isGroup == 1) {
							money = this.detail.payMoney
						} else {
							if (this.detail.isVip == 1) {
								money = this.detail.payMoney
							} else {
								money = this.detail.nowBuyMoney
							}
						}
						this.navigate('/pages/vipCourseDetails/pay?order=' + encodeURIComponent(JSON.stringify(this.detail)) +
							'&payMoney=' + money)
						break;
					case '继续学习':
						this.navigate(`/pages/vipCourseDetails/vipCourseDetails`, {
							goodId: this.detail.goodId
						})
						break;
					case '确认收货':
						this.$emit('show', {
							indentId: this.detail.indentId,
							status: e,
							content: this.$t('pagesText.qdsdhm') + '？'
						})
						break;
					case '再次购买':
						this.navigate('/pages/vipCourseDetails/vipCourseDetails', {
							goodId: this.detail.goodId
						})
						break;
					case '去评价':
						this.navigate(`/pages/vipCourseDetails/comment?indentId=${this.detail.indentId}`)
						break;
				}
			}
		},
	}
</script>


<style lang="scss" scoped>
	.order_card {
		padding: 30rpx 40rpx;
		margin-bottom: 22rpx;
		background: #fff;

		.title {
			font-size: 24rpx;
			margin-bottom: 32rpx;

			.icon {
				image {
					width: 30rpx;
					height: 30rpx;
					margin-right: 12rpx;
				}

				text {
					font-size: 26rpx;
					color: #999;
				}
			}
		}

		.actual {
			text-align: right;
			padding: 20rpx 0;
			border-top: solid 1rpx #E6E6E6;
		}

		.operate_btns {

			.btn {
				width: max-content;
				height: 72rpx;
				box-sizing: border-box;
				font-size: 28rpx;
				margin-left: 21rpx;
				border-radius: 36rpx;
				text-align: center;
				padding: 0 20rpx;
				// margin-top: 40rpx;
			}

			.active_ {
				background: $theme-color;
				color: #fff;
			}

			.inactive_ {
				background: #fff;
				border: 2rpx solid #999;
				color: #666;
			}
		}
	}
</style>