<template>
	<view>
		<view class="icon flex align-center justify-center">
			<u-icon name="info-circle-fill" color="#4F75FB" size="100"></u-icon>
		</view>
		<view class="title">
			{{$t('repeatUse.zhuxiao')}}{{$t('repeatUse.cpmc')}}{{$t('repeatUse.zh')}}
		</view>
		<view class="shuoming">
			{{$t('repeatUse.fqqy')}}
		</view>
		<view class="list flex align-center justify-center">
			<view class="list-box">
				<view class="list-box-item">
					·<text
						style="margin-left: 10rpx;">{{$t('repeatUse.wfsyzh')}}{{$t('repeatUse.cpmc')}}{{$t('repeatUse.pt')}}；</text>
				</view>
				<view class="list-box-item">
					·<text style="margin-left: 10rpx;">{{$t('repeatUse.wffwGrzh')}}；</text>
				</view>
				<view class="list-box-item">
					·<text style="margin-left: 10rpx;">{{$t('repeatUse.zdfq')}}</text>
				</view>
			</view>
		</view>
		<view class="tishi">
			{{$t('zx.xieyi')}}<text @click="goXieOff()">《{{$t('zx.yhxieyi')}}》</text>
		</view>
		<view class="submit flex align-center justify-center">
			<view class="submit-box flex align-center justify-center" @click="submit()">
				{{$t('pageText.surezx')}}
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			};
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('pageText.zxzh')
			})
		},
		methods: {
			//用户注销协议
			goXieOff() {
				uni.navigateTo({
					url: '/my/setting/xieyi?type=293'
				})
			},
			//确认注销
			submit() {
				let that = this
				uni.showModal({
					title: this.$t('repeatUse.ts'),
					content: this.$t('zx.tk'),
					cancelText: this.$t('zx.xx'),
					confirmText: this.$t('pageText.surezx'),
					confirmColor: '#4F75FB',
					complete(ret) {
						if (ret.confirm) {
							let data = {
								state: 8,
							}
							that.$Request.postJson('/app/message/insertMessage', {
								state: 8
							}).then(res => {
								if (res.code == 0) {
									// uni.showModal({
									// 	title:'提示',
									// 	content:'注销申请已提交，等待系统确认',
									// 	showCancel:false,
									// 	confirmText:'确认',
									// 	confirmColor:'#4F75FB'
									// })
									// uni.showToast({
									// 	title:'注销申请已提交，等待系统确认',
									// 	icon:'none',
									// 	duration:2000
									// })
									uni.removeStorageSync('token')
									uni.removeStorageSync('userId')
									uni.removeStorageSync('userInfo')
									uni.removeStorageSync('currents')
									uni.showToast({
										title: this.$t('toast.tjcg') + '！',
										icon: 'none'
									})
									uni.switchTab({
										url: '/pages/my/my'
									})
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							})
						}
					}
				})
			},
		},
	}
</script>

<style lang="scss">
	page {
		background: #F5F7FF;
	}

	.icon {
		width: 100%;
		padding-top: 100rpx;
	}

	.title {
		width: 100%;
		text-align: center;
		margin-top: 20rpx;
		font-weight: bold;
		font-size: 40rpx;
	}

	.shuoming {
		font-size: 16rpx;
		color: #333333;
		margin-top: 20rpx;
		text-align: center;
	}

	.list {
		width: 100%;
		// height: 100rpx;
		margin-top: 20rpx;

		.list-box {
			padding: 20rpx;
			width: 686rpx;
			height: 100%;
			border-radius: 24rpx;
			background: #ffffff;

			.list-box-item {
				margin-top: 10rpx;
				font-size: 16rpx;
				color: #333333;
			}
		}
	}

	.tishi {
		width: 100%;
		text-align: center;
		color: #999999;
		margin-top: 40rpx;
		font-size: 18rpx;

		text {
			color: #4F75FB;
		}
	}

	.submit {
		width: 100%;
		height: 88rpx;
		margin-top: 60rpx;

		.submit-box {
			width: 686rpx;
			height: 88rpx;
			border-radius: 16rpx;
			color: #ffffff;
			background-color: #4F75FB;
		}
	}
</style>