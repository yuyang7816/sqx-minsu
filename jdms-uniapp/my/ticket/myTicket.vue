<template>
	<view class="ticket-page ticket-list-page">
		<view class="ticket-list-hero">
			<view class="ticket-list-hero__title">我的优惠券</view>
			<view class="ticket-list-hero__desc">按状态查看卡券，选择合适优惠直接用于订单。</view>
		</view>

		<view class="ticket-tabs-wrap">
			<view class="ticket-tabs">
				<view v-for="(item, index) in list" :key="index" class="ticket-tab"
					:class="tabIndex == index ? 'ticket-tab--active' : ''" @click="change(item,index)">
					{{ item.name }}
				</view>
			</view>
		</view>

		<view v-if="ticketList && ticketList.length > 0" class="ticket-list">
			<view class="ticket-item" v-for="(item, index) in ticketList" :key="index">
				<view class="ticket-item__main">
					<view class="ticket-item__left">
						<view class="ticket-item__money">¥<text>{{ item.money }}</text></view>
						<view class="ticket-item__rule" v-if="item.minMoney">满{{ item.minMoney }}元可用</view>
						<view class="ticket-item__rule" v-else>无门槛使用</view>
					</view>
					<view class="ticket-item__info">
						<view class="ticket-item__name">{{ item.couponName }}</view>
						<view class="ticket-item__meta" v-if="item.expirationTime">有效期至 {{ item.expirationTime }}</view>
						<view class="ticket-item__meta" v-else>永久有效</view>
						<view class="ticket-item__status">{{ getStatusText(item) }}</view>
					</view>
					<view class="ticket-item__action-wrap" v-if="item.status == 0">
						<view class="ticket-item__action ticket-item__action--ghost" @click="noShiyong(item)" v-if="couponId == item.id">取消使用</view>
						<view class="ticket-item__action" @click="shiyong(item)" v-if="couponId != item.id">立即使用</view>
					</view>
				</view>
			</view>
			<up-loadmore :status="status" v-if="ticketList.length > 0" />
		</view>

		<empty v-if="ticketList.length == 0"></empty>
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
	import empty from '@/components/empty/empty.vue';
	const {
		proxy
	} = getCurrentInstance();
	const list = ref([{
		id: 0,
		name: '可使用'
	}, {
		id: 1,
		name: '已使用'
	}, {
		id: 2,
		name: '已失效'
	}]);
	const tabIndex = ref(0);
	// 总页数
	const totalPages = ref(1);
	// 页数
	const page = ref(1);
	const limit = ref(10);
	//加载状态
	const status = ref('loadmore');
	// 商品金额
	const price = ref(0);
	// 已选优惠券id
	const couponId = ref('');
	const couponStatus = ref(0);

	const choose = ref(false);

	onLoad(option => {
		if (option.price) {
			price.value = option.price
		}
		if (option.couponId) {
			couponId.value = option.couponId
		}
		if (option.choose) {
			choose.value = true
		}
		getList()
	});
	const change = (item, index) => {
		tabIndex.value = index
		couponStatus.value = item.id
		ticketList.splice(0, ticketList.length, []);
		page.value = 1
		getList()
	};
	const shiyong = (item) => {
		if (price.value > 0) {
			if (price.value < item.minMoney) {
				uni.showToast({
					title: '未达到优惠券满减金额',
					icon: 'none'
				})
			} else {
				uni.$emit('getDiscountId', item)
				uni.navigateBack()
			}
		} else {
			uni.switchTab({
				url: '/pages/index/index'
			})
		}
	};
	const noShiyong = (item) => {
		uni.$emit('getDiscountId', {
			id: '',
			money: ''
		})
		uni.navigateBack()
	};

	const getStatusText = (item) => {
		if (item.status == 1) {
			return '已使用'
		}
		if (item.status == 2) {
			return '已失效'
		}
		return couponId.value == item.id ? '当前已选' : '可立即使用'
	}


	const ticketList = reactive([]);
	// 获取优惠券列表
	const getList = async () => {
		const data = {
			page: page.value,
			limit: limit.value,
			status: couponStatus.value,
			userId: uni.getStorageSync('userId')
		};
		uni.showLoading({
			title: '加载中...'
		});
		try {
			const res = await proxy.$http.getT('/app/couponUser/getMyCouponList', data);
			if (res.code === 0) {
				totalPages.value = res.data.pages;
				status.value = page.value < totalPages.value ? 'loadmore' : 'nomore';
				if (page.value == 1) {
					ticketList.splice(0, ticketList.length, ...res.data.records);
				} else {
					ticketList.push(...res.data.records);
				}
				uni.hideLoading();
			} else {
				uni.hideLoading();
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				});
			}
		} catch (error) {
			uni.hideLoading();
			uni.showToast({
				title: res.msg,
				icon: 'none',
				duration: 2000
			});
			console.error('请求出错:', error);
		}
	};




	// const getList = () => {
	// 	let data = {
	// 		page: page.value,
	// 		limit: limit.value,
	// 		status: couponStatus.value
	// 	}
	// 	uni.showLoading({
	// 		title: '加载中...'
	// 	})
	// 	proxy.$http.postJson('/app/userCoupon/page', data).then(res => {
	// 		uni.hideLoading()
	// 		if (res.code == 0) {
	// 			if (res.data.records) {
	// 				totalPages.value = res.data.pages;
	// 				if (page.value < totalPages.value) {
	// 					status.value = 'loadmore';
	// 				} else {
	// 					status.value = 'nomore';
	// 				}
	// 				if (page.value === 1) {
	// 					ticketList.splice(0, ticketList.length, ...res.data.records);
	// 				} else {
	// 					ticketList.push(...res.data.records);
	// 				}
	// 			}
	// 		} else {
	// 			uni.showToast({
	// 				title: res.msg,
	// 				icon: 'none',
	// 				duration: 2000
	// 			})
	// 		}
	// 	})
	// };
	onPullDownRefresh(() => {
		page.value = 1;
		getList();
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1
			status.value = 'loading';
			getList();
		}
	});
</script>

<style lang="scss" scoped>
	.ticket-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #eef5ff 0%, #f6f8fc 34%, #f5f7fb 100%);
	}

	.ticket-list-page {
		padding: 24rpx 24rpx 40rpx;
	}

	.ticket-list-hero {
		padding: 34rpx 30rpx;
		border-radius: 26rpx;
		background: #ffffff;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.ticket-list-hero__title {
		font-size: 38rpx;
		font-weight: 700;
		color: #1e2a3a;
	}

	.ticket-list-hero__desc {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #8b94a5;
	}

	.ticket-tabs-wrap {
		margin-top: 18rpx;
	}

	.ticket-tabs {
		background: #ffffff;
		border-radius: 22rpx;
		padding: 10rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
		display: flex;
		gap: 10rpx;
	}

	.ticket-tab {
		flex: 1;
		height: 72rpx;
		border-radius: 18rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 26rpx;
		font-weight: 600;
		color: #607089;
	}

	.ticket-tab--active {
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		color: #ffffff;
	}

	.ticket-list {
		margin-top: 20rpx;
		display: flex;
		flex-direction: column;
		gap: 18rpx;
	}

	.ticket-item {
		background: #ffffff;
		border-radius: 24rpx;
		padding: 26rpx 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.ticket-item__main {
		display: flex;
		gap: 22rpx;
		align-items: stretch;
	}

	.ticket-item__left {
		width: 170rpx;
		flex-shrink: 0;
		padding-right: 18rpx;
		border-right: 2rpx dashed #e7edf7;
		display: flex;
		flex-direction: column;
		justify-content: center;
	}

	.ticket-item__money {
		font-size: 26rpx;
		color: #ff5b7a;
	}

	.ticket-item__money text {
		font-size: 46rpx;
		font-weight: 700;
		line-height: 1;
		margin-left: 4rpx;
	}

	.ticket-item__rule {
		margin-top: 10rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: #8b94a5;
	}

	.ticket-item__info {
		flex: 1;
		min-width: 0;
		display: flex;
		flex-direction: column;
		justify-content: center;
	}

	.ticket-item__name {
		font-size: 30rpx;
		font-weight: 700;
		line-height: 1.4;
		color: #1e2a3a;
	}

	.ticket-item__meta {
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.5;
		color: #8b94a5;
	}

	.ticket-item__status {
		margin-top: 12rpx;
		font-size: 22rpx;
		font-weight: 600;
		color: #2b6fe8;
	}

	.ticket-item__action-wrap {
		flex-shrink: 0;
		display: flex;
		align-items: center;
	}

	.ticket-item__action {
		min-width: 144rpx;
		height: 70rpx;
		padding: 0 20rpx;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		color: #ffffff;
		font-size: 24rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.ticket-item__action--ghost {
		background: #f0f7ff;
		color: #2b6fe8;
	}
</style>

