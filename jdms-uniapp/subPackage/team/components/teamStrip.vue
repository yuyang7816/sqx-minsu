<template>
	<view class="team_strip">
		<view class="title">{{ good.title }}</view>
		<view class="info flex sb alic">
			<template>
				<view class="flex" style="align-items: baseline;font-size: 26rpx;">
					<v-price-format :custom-style="{ fontSize: '32rpx', color: '#D43532' }">{{ good.groupPrice || good.indentMoney
          }}</v-price-format>
				</view>
			</template>
			<view class="operate_btns flex">
				<!-- <v-button bg-color="#fff" color="#0096FF" v-if="goodStatus == 1"
					@click="navigate('/pages/vipCourseDetails/vipCourseDetails', { goodId: good.goodId, enterType: 'group' })">查看详情</v-button> -->
				<v-button bg-color="#fff" color="#0096FF" @click="routeGo">{{$t('index.ckxq')}}</v-button>
				<!-- <v-button
					@click="navigate('/pages/vipCourseDetails/vipCourseDetails', { goodId: good.goodId, enterType: 'group' })"
					v-if="good.indentStatus == status[0]">发起拼团</v-button> -->
				<v-button @click="topay" v-if="good.indentStatus == 0">{{$t('repeatUse.payNow')}}</v-button>
				<button open-type="share" v-if="good.indentStatus == 1" class="btn_" @click="share(good)">
					{{$t('repeatUse.yqhy')}}
				</button>
				<v-button bg-color="#fff" color="#0096FF" v-if="good.indentStatus == 2">{{$t('btn.wxdd')}}</v-button>
			</view>
		</view>
		<!-- 新增 -->
		<u-modal v-model="showShareModal" :content="shareContent" :show-cancel-button="true"
			:confirm-text="$t('repeatUse.copy')" :cancel-text="$t('btnText.qx')" :title="$t('repeatUse.fxlj')"
			@confirm="shareConfirm"></u-modal>
	</view>
</template>

<script>
	import config from '../../../config.js'
	export default {
		props: {
			good: {
				type: Object,
				required: true
			},
		},
		// 新增
		data() {
			return {
				showShareModal: false,
				shareContent: '',
				userId: null,
				goodStatus: false,
			}
		},
		mounted() {
			console.log(this.good, "234556666")
			let userInfo = uni.getStorageSync('userInfo');
			this.userId = userInfo.userId
			// #ifdef H5
			this.shareContent = config.BASE_URLS + '/subPackage/team/teamPay' +
				`?goodId=${this.good.goodId}&groupId=${this.good.groupId}`
			// #endif
		},
		methods: {
			routeGo() {
				if (this.good.goodStatus == 1) {
					uni.navigateTo({
						url: '/pages/vipCourseDetails/vipCourseDetails?goodId=' + this.good.goodId + '&enterType=group'
					})
				} else {
					uni.navigateTo({
						url: '/pages/order/orderDetail?indentId=' + this.good.indentId
					})
				}
			},
			topay() {
				this.navigate('/pages/vipCourseDetails/pay?order=' + encodeURIComponent(JSON.stringify(this.good)) +
					'&payMoney=' + this.good.indentMoney)
			},
			// 新增
			share(info) {
				// #ifdef H5
				this.showShareModal = true
				// #endif
			},
			shareConfirm() {
				uni.setClipboardData({
					data: this.shareContent,
					success() {
						uni.showToast({
							title: this.$t('repeatUse.copyOk'),
							icon: 'successs'
						})
					}
				})
			}
		},
	}
</script>


<style lang="scss" scoped>
	/deep/.u-model__content__message {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.btn_ {
		color: #0096FF;
		background-color: #fff;
		border: solid #0096FF 2rpx;
		border-radius: 40rpx;
		line-height: 70rpx;
		font-size: 28rpx;
		padding: 0 34rpx;
		margin-left: 10rpx;
	}

	/deep/ .uni-button:after {
		border: none;
	}

	.team_strip {
		background: #fff;
		padding: 30rpx;
		border-bottom: 1rpx solid #F5F5F5;

		.title {
			font-size: 32rpx;
			font-weight: bold;
		}

		.info {
			padding-top: 18rpx;

			.price {}
		}
	}

	// .team_strip:last-child {
	// 	border-bottom: none !important;
	// }
</style>