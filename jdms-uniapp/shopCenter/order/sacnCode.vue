<template>
	<view class="plat-page plat-form-page plat-shop-scan-page">
		<view class="plat-form-head">
			<view class="plat-form-head__title">订单核销</view>
			<view class="plat-form-head__hint">输入订单核销码，或直接调用相机扫码完成到店核销。</view>
		</view>

		<view class="plat-card plat-shop-scan">
			<view class="plat-shop-scan__badge">核销中台</view>
			<view class="plat-shop-scan__label">核销码</view>
			<view class="plat-shop-scan__row">
				<view class="plat-shop-scan__input">
					<u-search bg-color="#eef4ff" placeholder="请输入核销码" :show-action="false" v-model="keyword"></u-search>
				</view>
				<!-- #ifndef H5 -->
				<view class="plat-shop-scan__action" @click="scanCode()">
					<u-icon name="scan" color="#2C8EFE" size="28"></u-icon>
				</view>
				<!-- #endif -->
			</view>
			<view class="plat-shop-scan__tip">支持手动输入核销码，也支持扫码自动回填并直接触发核销。</view>
		</view>

		<view class="plat-fixed-foot">
			<view class="plat-btn" @click="hexiao()">确认核销</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();

	const keyword = ref('');

	onLoad((option) => {
		if (option.orderNumber) {
			keyword.value = option.orderNumber
		}
	})

	// 扫码核销
	const scanCode = () => {
		keyword.value = ''
		uni.scanCode({
			onlyFromCamera: true,
			success: function(res) {
				keyword.value = res.result
				if (keyword.value) {
					hexiao()
				}
			},
			fail() {
				uni.showToast({
					title: '核销失败',
					icon: 'error'
				})
			},
		})
	}

	const hexiao = () => {
		if (!keyword.value) {
			uni.showToast({
				title: '请输入核销码',
				icon: 'none'
			})
			return
		}
		uni.showModal({
			title: '',
			content: '确认核销该订单吗?',
			success: function(res) {
				if (res.confirm) {
					let data = {
						orderNumber: keyword.value
					};
					uni.showLoading({
						title: '加载中...'
					})
					proxy.$http.postT('/app/orders/cancelVerification', data).then(res => {
						uni.hideLoading()
						if (res && res.code == 0) {
							uni.showToast({
								title: '核销成功'
							})
							keyword.value = ''
							setTimeout(() => {
								uni.navigateBack()
							}, 500)
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
	}
</script>

<style lang="scss">
	page {
		background: #f4f7fb;
	}

	.plat-shop-scan-page {
		min-height: 100vh;
		padding: 24rpx 24rpx 160rpx;
		background: linear-gradient(180deg, #eef4ff 0, #f4f7fb 220rpx);
	}

	.plat-shop-scan-page .plat-form-head {
		padding: 30rpx 28rpx;
		border-radius: 24rpx;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		box-shadow: 0 10rpx 24rpx rgba(43, 111, 232, 0.14);
	}

	.plat-shop-scan-page .plat-form-head__title {
		color: #ffffff;
	}

	.plat-shop-scan-page .plat-form-head__hint {
		color: rgba(255, 255, 255, 0.82);
	}

	.plat-shop-scan {
		margin-top: 18rpx;
		padding: 28rpx;
		border-radius: 24rpx;
		box-shadow: 0 8rpx 24rpx rgba(15, 23, 42, 0.04);
	}

	.plat-shop-scan__badge {
		display: inline-flex;
		align-items: center;
		height: 44rpx;
		padding: 0 16rpx;
		border-radius: 999rpx;
		background: #eef4ff;
		color: #2b6fe8;
		font-size: 22rpx;
		margin-bottom: 18rpx;
	}

	.plat-shop-scan__label {
		font-size: 28rpx;
		font-weight: 700;
		color: #1e293b;
	}

	.plat-shop-scan__row {
		margin-top: 18rpx;
		align-items: stretch;
		gap: 14rpx;
	}

	.plat-shop-scan__input {
		flex: 1;
		min-width: 0;
	}

	.plat-shop-scan__input :deep(.u-search__content) {
		height: 80rpx !important;
		border-radius: 18rpx !important;
		background: #eef4ff !important;
	}

	.plat-shop-scan__action {
		width: 80rpx;
		height: 80rpx;
		border-radius: 18rpx;
		background: #eef4ff;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.plat-shop-scan__tip {
		margin-top: 16rpx;
		font-size: 22rpx;
		line-height: 1.6;
		color: #8b94a5;
	}

	.plat-shop-scan-page .plat-fixed-foot {
		padding: 20rpx 32rpx calc(24rpx + env(safe-area-inset-bottom));
		background: rgba(255, 255, 255, 0.96);
		box-shadow: 0 -4rpx 18rpx rgba(15, 23, 42, 0.05);
	}

	.plat-card {
		margin: 0 !important;
	}
</style>
