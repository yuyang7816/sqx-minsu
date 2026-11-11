<template>
	<view class="wallet-page">
		<view class="wallet-header">
			<view class="wallet-header-bg">
				<view class="wallet-header-inner">
					<view class="wallet-balance-label">账户余额（元）</view>
					<view class="wallet-balance-row">
						<text class="wallet-balance-unit">¥</text>
						<text class="wallet-balance-num">{{canCashMoney || '0.00'}}</text>
					</view>
					<view class="wallet-balance-tip">累计提现 {{totalCashMoney || '0.00'}} 元</view>
				</view>
			</view>
		</view>

		<view class="wallet-body">
			<view class="wallet-summary-grid">
				<view class="wallet-summary-card">
					<view class="wallet-summary-card__label">可提现金额</view>
					<view class="wallet-summary-card__num">{{canCashMoney || '0.00'}}</view>
				</view>
				<view class="wallet-summary-card wallet-summary-card--soft">
					<view class="wallet-summary-card__label">累计提现</view>
					<view class="wallet-summary-card__num">{{totalCashMoney || '0.00'}}</view>
				</view>
			</view>

			<view class="wallet-cta" @click="routerGo('/my/wallet/cash')">
				<view>
					<view class="wallet-cta__title">申请提现</view>
					<view class="wallet-cta__desc">支持微信提现与支付宝提现</view>
				</view>
				<view class="wallet-cta__btn">去提现</view>
			</view>

			<view class="wallet-section">
				<view class="wallet-section-title">账单明细</view>
				<view class="wallet-panel wallet-panel--compact">
					<view class="wallet-bill-item" v-for="item,index in hotelList" :key="index">
						<view class="wallet-bill-main">
							<view class="wallet-bill-title">{{item.content || ''}}</view>
							<view class="wallet-bill-time">{{item.createTime || ''}}</view>
						</view>
						<view class="wallet-bill-money" :class="item.type == 1 ? 'wallet-bill-money--plus' : 'wallet-bill-money--minus'">
							{{item.type == 1 ? '+' : '-'}}{{item.money || '0.00'}}
						</view>
					</view>
					<empty v-if="hotelList.length == 0" text="暂无明细"></empty>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
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

	const totalCashMoney = ref('0.00');
	const canCashMoney = ref('0.00');
	const page = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const totalPages = ref(1);
	const hotelList = reactive([]);
	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

	/* 获取用户总资金 */
	const getUserInfo = () => {
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				canCashMoney.value = res.data.canCashMoney // 可提现金额
				totalCashMoney.value = res.data.totalCashMoney //  累计提现金额
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	}
	// 账单明细
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId'),
			// classify: 1,//1注册 2购买 3提现
			// type: 1,//类型（1充值 2.提现）
			// state: 1//状态 1待支付 2已到账 3取消
		}
		proxy.$http.getT('/app/moneyDetails/getMoneyDetailsList', data).then(res => {
			if (res.code === 0) {
				totalPages.value = res.data.pages
				if (page.value < totalPages.value) {
					status.value = 'loadmore'
				} else {
					status.value = 'nomore'
				}
				if (page.value == 1) {
					hotelList.splice(0, hotelList.length, ...res.data.records)
				} else {
					hotelList.push(...res.data.records)
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
			uni.stopPullDownRefresh();
			uni.hideLoading();
		});
	};

	onLoad(option => {
		getUserInfo()
		getList()
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	.wallet-page {
		min-height: 100vh;
		padding-bottom: 40rpx;
		background: linear-gradient(180deg, #eef5ff 0%, #f6f8fc 32%, #f5f7fb 100%);
	}

	.wallet-header {
		padding: 30rpx 24rpx 60rpx;
	}

	.wallet-header-bg {
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		border-radius: 28rpx;
		padding: 44rpx 36rpx;
		box-shadow: 0 12rpx 30rpx rgba(43, 111, 232, 0.22);
		position: relative;
		overflow: hidden;
	}

	.wallet-header-bg::after {
		content: '';
		position: absolute;
		width: 300rpx;
		height: 300rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.12) 0%, transparent 70%);
		top: -100rpx;
		right: -60rpx;
		border-radius: 50%;
	}

	.wallet-header-inner {
		position: relative;
		z-index: 1;
	}

	.wallet-balance-label {
		font-size: 26rpx;
		color: rgba(255, 255, 255, 0.85);
		line-height: 1;
	}

	.wallet-balance-row {
		display: flex;
		align-items: baseline;
		margin-top: 24rpx;
		margin-bottom: 16rpx;
	}

	.wallet-balance-unit {
		font-size: 40rpx;
		font-weight: 700;
		color: #ffffff;
		margin-right: 8rpx;
	}

	.wallet-balance-num {
		font-size: 76rpx;
		font-weight: 800;
		line-height: 1;
		color: #ffffff;
	}

	.wallet-balance-tip {
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.72);
		line-height: 1.4;
	}

	.wallet-body {
		padding: 0 24rpx;
		margin-top: -30rpx;
		position: relative;
		z-index: 2;
	}

	.wallet-summary-grid {
		display: grid;
		grid-template-columns: repeat(2, minmax(0, 1fr));
		gap: 18rpx;
	}

	.wallet-summary-card {
		background: #ffffff;
		border-radius: 24rpx;
		padding: 28rpx 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.wallet-summary-card--soft {
		background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
	}

	.wallet-summary-card__label {
		font-size: 24rpx;
		color: #8b94a5;
		line-height: 1.4;
	}

	.wallet-summary-card__num {
		margin-top: 14rpx;
		font-size: 40rpx;
		font-weight: 700;
		line-height: 1.2;
		color: #1e2a3a;
	}

	.wallet-cta {
		margin-top: 22rpx;
		background: #ffffff;
		border-radius: 24rpx;
		padding: 30rpx 28rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 20rpx;
	}

	.wallet-cta__title {
		font-size: 30rpx;
		font-weight: 700;
		color: #1e2a3a;
		line-height: 1.4;
	}

	.wallet-cta__desc {
		margin-top: 8rpx;
		font-size: 24rpx;
		line-height: 1.5;
		color: #8b94a5;
	}

	.wallet-cta__btn {
		flex-shrink: 0;
		min-width: 148rpx;
		height: 72rpx;
		padding: 0 24rpx;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		color: #ffffff;
		font-size: 28rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.wallet-section {
		margin-top: 24rpx;
	}

	.wallet-section-title {
		font-size: 28rpx;
		font-weight: 700;
		color: #1e2a3a;
		margin-bottom: 18rpx;
		padding-left: 6rpx;
	}

	.wallet-panel {
		background: #ffffff;
		border-radius: 24rpx;
		padding: 32rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.wallet-panel--compact {
		padding: 12rpx 24rpx;
	}

	.wallet-bill-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 24rpx;
		padding: 28rpx 0;
		border-bottom: 2rpx solid #f0f2f5;
	}

	.wallet-bill-item:last-of-type {
		border-bottom: none;
	}

	.wallet-bill-main {
		flex: 1;
		min-width: 0;
	}

	.wallet-bill-title {
		font-size: 28rpx;
		font-weight: 600;
		line-height: 1.4;
		color: #1e2a3a;
	}

	.wallet-bill-time {
		margin-top: 8rpx;
		font-size: 24rpx;
		line-height: 1.4;
		color: #8b94a5;
	}

	.wallet-bill-money {
		flex-shrink: 0;
		font-size: 34rpx;
		font-weight: 700;
		line-height: 1.2;
	}

	.wallet-bill-money--plus {
		color: #18a058;
	}

	.wallet-bill-money--minus {
		color: #ff5b7a;
	}
</style>
