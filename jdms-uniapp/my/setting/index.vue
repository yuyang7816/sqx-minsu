<template>
	<view class="padding-lr bg">
		<view class="flex  padding-tb" @click="goNav('/my/setting/xieyi?type=177')">
			<view class="flex-sub text-df" style="line-height: 50upx;">{{$t('set.yhxy')}}</view>
			<u-icon name="arrow-right"></u-icon>

		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/xieyi?type=176')">
			<view class="flex-sub text-df" style="line-height: 50upx;">{{$t('repeatUse.yszc')}}</view>
			<u-icon name="arrow-right"></u-icon>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/xieyi?type=187')">
			<view class="flex-sub text-df" style="line-height: 50upx;">{{$t('bar.gywm')}}</view>
			<u-icon name="arrow-right"></u-icon>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/logOff')">
			<view class="flex-sub text-df" style="line-height: 50upx;">{{$t('zx.zxtit')}}</view>
			<u-icon name="arrow-right"></u-icon>
		</view>
		<view class="flex  padding-tb" @click="TuiLogin()" v-if="token">
			<view class="flex-sub text-df" style="line-height: 50upx;">{{$t('my.tuilogin')}}</view>
			<u-icon name="arrow-right"></u-icon>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				token: uni.getStorageSync('token')
			}
		},
		onLoad() {

		},
		methods: {
			goNav(e) {
				uni.navigateTo({
					url: e
				})
			},
			//退出登录
			TuiLogin() {
				let that = this
				if (uni.getStorageSync('token')) {
					uni.showModal({
						title: this.$t('repeatUse.ts'),
						content: this.$t('modal.tcdl'),
						confirmText: this.$t('repeatUse.qr'),
						cancelText: this.$t('repeatUse.qx'),
						success: function(res) {
							if (res.confirm) {
								uni.removeStorageSync('token')
								uni.removeStorageSync('userId')
								uni.removeStorageSync('isMysf')
								uni.removeStorageSync('userInfo')
								uni.removeStorageSync('typeId')
								uni.removeStorageSync('currents')
								uni.reLaunch({
									url: '/pages/index/index'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}
			},
		}

	}
</script>

<style>
	page {
		background: #FFFFFF;
	}

	.btn {
		width: 100%;
		height: 80upx;
		background: #005dff;
		border-radius: 6upx;
		text-align: center;
		line-height: 80upx;
		margin-top: 40upx;
		font-size: 34upx;
		color: #fff;
	}
</style>