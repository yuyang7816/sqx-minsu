<template>
	<view class="invoice-page invoice-home-page">
		<view class="invoice-home-hero">
			<view class="invoice-home-hero__eyebrow">INVOICE CENTER</view>
			<view class="invoice-home-hero__title">开票服务</view>
			<view class="invoice-home-hero__desc">集中处理发票申请、记录查询与开票说明，流程更清晰。</view>
			<view class="invoice-home-hero__tags">
				<view class="invoice-home-hero__tag">电子发票</view>
				<view class="invoice-home-hero__tag">记录可追踪</view>
				<view class="invoice-home-hero__tag">在线提交</view>
			</view>
		</view>

		<view class="invoice-home-body">
			<view class="invoice-home-grid">
				<view class="invoice-home-card invoice-home-card--primary" @click="goNav('./Invoicing')">
					<view class="invoice-home-card__icon-wrap">
						<image src="/subpackage/static/rider/kp.png" mode="aspectFit" class="invoice-home-card__icon"></image>
					</view>
					<view class="invoice-home-card__title">申请开票</view>
					<view class="invoice-home-card__desc">选择可开票订单并提交开票信息</view>
					<view class="invoice-home-card__action">立即申请</view>
				</view>

				<view class="invoice-home-card" @click="goNav('./kpjl')">
					<view class="invoice-home-card__icon-wrap invoice-home-card__icon-wrap--soft">
						<image src="/subpackage/static/rider/kpjl.png" mode="aspectFit" class="invoice-home-card__icon"></image>
					</view>
					<view class="invoice-home-card__title">开票记录</view>
					<view class="invoice-home-card__desc">查看处理进度、金额与收件信息</view>
					<view class="invoice-home-card__action invoice-home-card__action--link">查看记录</view>
				</view>
			</view>

			<view class="invoice-home-list">
				<view class="invoice-home-list__item" @click="goNav('/subpackage/invoice/kpsm')">
					<view class="invoice-home-list__left">
						<view class="invoice-home-list__dot"></view>
						<view>
							<view class="invoice-home-list__title">开票说明</view>
							<view class="invoice-home-list__desc">查看开票规则、限制与处理方式</view>
						</view>
					</view>
					<u-icon name="arrow-right" color="#9AA3B0" size="16"></u-icon>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		computed
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();


	const goNav = (url) => {
		uni.navigateTo({
			url
		})
	}
</script>

<style lang="scss" scoped>
	.invoice-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #eef5ff 0%, #f6f8fc 34%, #f5f7fb 100%);
	}

	.invoice-home-page {
		padding: 30rpx 24rpx 40rpx;
	}

	.invoice-home-hero {
		padding: 40rpx 34rpx 36rpx;
		border-radius: 30rpx;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		box-shadow: 0 12rpx 30rpx rgba(43, 111, 232, 0.22);
		color: #ffffff;
		position: relative;
		overflow: hidden;
	}

	.invoice-home-hero::after {
		content: '';
		position: absolute;
		width: 300rpx;
		height: 300rpx;
		right: -60rpx;
		top: -120rpx;
		border-radius: 50%;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.14) 0%, transparent 72%);
	}

	.invoice-home-hero__eyebrow {
		position: relative;
		z-index: 1;
		font-size: 20rpx;
		letter-spacing: 2rpx;
		opacity: 0.86;
	}

	.invoice-home-hero__title {
		position: relative;
		z-index: 1;
		margin-top: 16rpx;
		font-size: 48rpx;
		font-weight: 700;
		line-height: 1.2;
	}

	.invoice-home-hero__desc {
		position: relative;
		z-index: 1;
		margin-top: 16rpx;
		font-size: 26rpx;
		line-height: 1.7;
		max-width: 560rpx;
		color: rgba(255, 255, 255, 0.84);
	}

	.invoice-home-hero__tags {
		position: relative;
		z-index: 1;
		display: flex;
		flex-wrap: wrap;
		gap: 14rpx;
		margin-top: 28rpx;
	}

	.invoice-home-hero__tag {
		padding: 10rpx 18rpx;
		border-radius: 999rpx;
		font-size: 22rpx;
		background: rgba(255, 255, 255, 0.16);
	}

	.invoice-home-body {
		margin-top: 24rpx;
	}

	.invoice-home-grid {
		display: grid;
		grid-template-columns: repeat(2, minmax(0, 1fr));
		gap: 18rpx;
	}

	.invoice-home-card {
		background: #ffffff;
		border-radius: 24rpx;
		padding: 28rpx 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.invoice-home-card--primary {
		background: linear-gradient(180deg, #ffffff 0%, #f7fbff 100%);
	}

	.invoice-home-card__icon-wrap {
		width: 84rpx;
		height: 84rpx;
		border-radius: 22rpx;
		background: rgba(43, 111, 232, 0.14);
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.invoice-home-card__icon-wrap--soft {
		background: #eef5ff;
	}

	.invoice-home-card__icon {
		width: 46rpx;
		height: 46rpx;
	}

	.invoice-home-card__title {
		margin-top: 22rpx;
		font-size: 30rpx;
		font-weight: 700;
		color: #1e2a3a;
	}

	.invoice-home-card__desc {
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #8b94a5;
		min-height: 76rpx;
	}

	.invoice-home-card__action {
		margin-top: 22rpx;
		display: inline-flex;
		align-items: center;
		justify-content: center;
		height: 64rpx;
		padding: 0 22rpx;
		border-radius: 999rpx;
		font-size: 24rpx;
		font-weight: 700;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		color: #ffffff;
	}

	.invoice-home-card__action--link {
		background: #f0f7ff;
		color: #2b6fe8;
	}

	.invoice-home-list {
		margin-top: 18rpx;
		background: #ffffff;
		border-radius: 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
		padding: 0 24rpx;
	}

	.invoice-home-list__item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 20rpx;
		padding: 30rpx 0;
	}

	.invoice-home-list__left {
		display: flex;
		align-items: center;
		gap: 16rpx;
		min-width: 0;
	}

	.invoice-home-list__dot {
		width: 14rpx;
		height: 14rpx;
		border-radius: 50%;
		background: #2b6fe8;
		flex-shrink: 0;
	}

	.invoice-home-list__title {
		font-size: 28rpx;
		font-weight: 600;
		color: #1e2a3a;
	}

	.invoice-home-list__desc {
		margin-top: 6rpx;
		font-size: 24rpx;
		line-height: 1.5;
		color: #8b94a5;
	}
</style>


