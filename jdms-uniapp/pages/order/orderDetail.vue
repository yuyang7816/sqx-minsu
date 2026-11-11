<template>
	<view class="order_detail container">
		<u-navbar :back-text="$t('bar.ddxq')"></u-navbar>
		<view class="card flex font-now">
			<image src="../../static/images/o_complete.png" mode="scaleToFill" style="margin-right: 12rpx;" />
			<text style="font-weight: 600;">{{$t('repeatUse.order')}}{{ statusText }}</text>
		</view>
		<view class="card">
			<Cell :content="content" />
		</view>
		<view class="card rows">
			<view class="flex sb alic">
				<text class="fg font">{{$t('repeatUse.monry')}}</text>
				<v-price-format :custom-style="priceStyle"><text
						style="font-size: 36rpx;">{{ content.nowBuyMoney }}</text></v-price-format>
			</view>
			<view class="flex sb alic">
				<text class="fg font">{{$t('repeatUse.youhui')}}</text>
				<view class="flex">
					<u-icon name="minus" size="16" color="#000"></u-icon>
					<v-price-format :custom-style="{ fontWeight: 500, fontSize: '30rpx' }"><text style="font-size: 36rpx;">{{ (content.nowBuyMoney - content.payMoney).toFixed(2) || '0.00'
					}}</text></v-price-format>
				</view>
				<!-- <view class="flex" v-else>
					<u-icon name="minus" size="16" color="#000"></u-icon>
					<v-price-format :custom-style="{ fontWeight: 500, fontSize: '30rpx' }"><text
							style="font-size: 38rpx;">0.00</text></v-price-format>
				</view> -->
			</view>
			<!-- 0待支付 1拼团中 2待发货 3已完成 4已取消 5已发货 6已超时 -->
			<view class="flex sb alic" v-if="content.payMoney">
				<text class="font"
					v-if="content.indentStatus == 1 || content.indentStatus == 2 || content.indentStatus == 3 || content.indentStatus == 5">{{$t('order.sfk')}}</text>
				<text class="font" v-else>{{$t('repeatUse.yingfuk')}}</text>
				<v-price-format :custom-style="priceStyleRed"><text
						style="font-size: 36rpx;">{{ content.payMoney }}</text></v-price-format>
			</view>
			<!-- <view class="flex sb alic" v-else>
				<v-price-format :custom-style="priceStyleRed">{{ content.payMoney }}</v-price-format>
			</view> -->
		</view>
		<view class="card">
			<view class="font30">{{$t('order.xx')}}</view>
			<view class="info">
				<view class="flex alic">
					<text>{{$t('order.ddbh')}}</text>
					<text style="margin-right: 12rpx;">：{{ content.indentNumber }}</text>
					<view @click="copy(0)" class="flex alic">
						<u-icon name="tags"></u-icon>
						<text style="font-size: 20rpx;margin-left: 5rpx;">{{$t('repeatUse.copy')}}</text>
					</view>
				</view>
				<view class=""
					v-if="!(content.indentStatus == 0 || content.indentStatus == 1 || content.indentStatus == 4) && content.goodType != 2 && content.payMoney != 0">
					<text>{{$t('repeatUse.payWay')}}：</text>
					<text>{{ content.payType == 1 ? $t('repeatUse.wxzf') : $t('repeatUse.zfb') }}</text>
				</view>
				<view>
					<text>{{$t('repeatUse.ccjsj')}}：</text>
					<text>{{ content.createTime }}</text>
				</view>
				<view v-if="content.payTime">
					<text>{{$t('repeatUse.fksj')}}：</text>
					<text>{{ content.payTime }}</text>
				</view>
				<template v-if="content.indentType == 2">
					<view>
						<text>{{$t('gwc.shdz')}}：</text>
						<text>{{ content.province }}{{ content.city }}{{ content.county }}{{ content.addressDetails }}</text>
					</view>
					<view>
						<text>{{$t('repeatUse.shsjh')}}：</text>
						<text>{{ content.takeUserPhone }}</text>
					</view>
					<view v-if="content.deliveryInfo != null">
						<text>{{$t('repeatUse.wldh')}}：</text>
						<text>{{ content.deliveryInfo }}</text>
						<view @click="copy(1)" class="flex alic">
							<u-icon name="tags"></u-icon>
							<text style="font-size: 20rpx;margin-left: 5rpx;">{{$t('repeatUse.copy')}}</text>
						</view>
					</view>

				</template>
			</view>
		</view>
		<view class="footer_order flex alic sb bf">
			<!-- <text class="fg" @click="show = true">删除订单</text> -->
			<view class="cancel" @click="show = true">{{$t('order.detdorder')}}</view>
			<!-- 新增routerType == 1 -->
			<view class="" v-if="routerType == 1">
				<!-- <v-button @click="navigate('/pages/vipCourseDetails/vipCourseDetails', { goodId: content.goodId })"
					v-if="content.goodType == 2 && content.indentStatus == 4">再次购买</v-button> -->
				<v-button v-if="content.indentStatus == 0" @click="topay">{{$t('repeatUse.payNow')}}</v-button>
			</view>
		</view>
		<view class="placeholder" :style="[{ height }]"></view>
		<u-modal v-model="show" :content="$t('repeatUse.sureDel')+'？'" :show-cancel-button="true" @cancel="close"
			@confirm="confirm" :confirm-text="$t('btnText.qd')" :cancel-text="$t('pageText.dcl')"
			:title="$t('repeatUse.ts')"></u-modal>
	</view>
</template>


<script>
	import Cell from './components/Cell.vue'
	export default {
		components: {
			Cell
		},
		data() {
			return {
				show: false,
				content: {},
				height: '0px',
				priceStyle: {
					fontSize: '30rpx'
				},
				priceStyleRed: {
					fontSize: '30rpx',
					color: '#D53540'
				},
				routerType: '',
				indentId: ''
			}
		},
		computed: {
			statusText() {
				switch (this.content.indentStatus) {
					case 0:
						return this.$t('pageText.dfk');
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
					case 6:
						return this.$t('repeatUse.ycs')
				}
			}
		},
		async onLoad({
			indentId,
			routerType
		}) {
			this.routerType = routerType
			this.indentId = indentId
			this.getOrderDetail()
		},
		onReady() {
			this.$nextTick(() => {
				uni.createSelectorQuery().in(this).select('.footer_order').boundingClientRect(data => {
					this.height = `${data.height}px`
				}).exec()
			})
		},
		methods: {
			topay() {
				this.navigate('/pages/vipCourseDetails/pay?order=' + encodeURIComponent(JSON.stringify(this.content)) +
					'&payMoney=' + this.content.payMoney)
			},
			// 查看订单详情
			getOrderDetail() {
				uni.showLoading({
					title: this.$t('repeatUse.load') + '...'
				})
				this.$Request.getT('/app/topicIndent/getTopicIndentInfo', {
					indentId: this.indentId
				}).then(res => {
					uni.hideLoading()
					this.content = res.data
				})
			},
			copy(val) {
				let content = null;
				if (val == 0) {
					content = this.content.indentNumber
				} else {
					content = this.content.deliveryInfo
				}
				uni.setClipboardData({
					data: content,
					success() {
						uni.showToast({
							title: this.$t('repeatUse.copyOk'),
							icon: 'none'
						})
					}
				})
			},
			close() {
				this.show = false
			},
			// 删除订单
			confirm() {
				this.close()
				this.$Request.getT('/app/topicIndent/deleteIndent', {
					indentId: this.content.indentId
				}).then(res => {
					uni.showToast({
						title: this.$t('toast.sccg'),
						icon: 'success',
						mask: true
					})
					setTimeout(() => {
						uni.navigateBack()
					}, 1500);
				})
			}
		}
	}
</script>


<style lang="scss" scoped>
	/deep/ .price {
		font-weight: 600;
	}

	.fg {
		color: #666 !important;
		font-size: 26rpx !important;
	}

	.font {
		font-size: 28rpx !important;
		color: #333 !important;
		font-weight: 600;
	}

	.font30 {
		font-size: 30rpx !important;
		color: #333 !important;
		font-weight: 600;
	}

	.font-now {
		font-size: 28rpx !important;
		color: #333 !important;
	}

	.order_detail {
		padding: 24rpx 0;

		.card {
			margin: 0 auto;
			width: 686rpx;
			box-sizing: border-box;
			padding: 36rpx;
			background: #fff;
			margin-bottom: 24rpx;
			border-radius: 24rpx;

			image {
				width: 40rpx;
				height: 40rpx;
			}

			.info {
				margin-top: 30rpx;

				&>view {
					margin-bottom: 36rpx;
					display: flex;

					&:last-child {
						margin: 0;
					}

					&>text {
						font-size: 26rpx;
						color: #666;

						&:first-child {
							color: #333 !important;
							white-space: nowrap;
						}
					}

				}
			}
		}

		.rows {
			&>view {
				&:not(:last-child) {
					margin-bottom: 30rpx;
				}
			}
		}

		.footer_order {
			position: fixed;
			bottom: 0;
			left: 0;
			right: 0;
			padding: 21rpx 40rpx 40rpx 40rpx;
			padding-bottom: calc(31rpx + env(safe-area-inset-bottom));
		}
	}

	.cancel {
		background: transparent !important;
		color: #999;
	}
</style>