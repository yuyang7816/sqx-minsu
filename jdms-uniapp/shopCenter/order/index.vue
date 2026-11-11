<template>
	<view class="plat-page plat-shop-order-page">
		<view class="plat-filter-bar">
			<up-tabs :list="list1" @click="click" lineWidth="30" lineColor="#2C8EFE" :scrollable="false" :current="tabIndex"
				:activeStyle="{
					color: '#2C8EFE',
					fontWeight: 'bold',
					fontSize: '28rpx'
				}" :inactiveStyle="{
					color: '#64748B',
					fontSize: '28rpx'
				}"></up-tabs>
		</view>

		<view class="plat-list-feed">
			<view class="plat-card plat-shop-order-card" v-for="(item, index) in orderList" :key="item.orderId || index"
				@click="routerGo('/shopCenter/order/orderDetail?orderId=' + item.orderId)">
				<view class="plat-shop-order-card__head">
					<view class="plat-shop-order-card__type-wrap">
						<view class="plat-shop-order-card__type">
							<image src="/static/image/order/jiudianyuding.png" mode="aspectFit" class="plat-shop-order-card__type-icon"></image>
							<text>{{ item.orderType == 1 ? '酒店订单' : '民宿订单' }}</text>
						</view>
					</view>
					<view class="plat-shop-order-card__status" :class="statusClass(item.orderStatus)">{{ orderStatusText(item.orderStatus) || '' }}</view>
				</view>

				<view class="plat-shop-order-card__main">
					<view class="plat-shop-order-card__main-body">
						<view class="plat-cover-title">{{ item.roomName || '' }}</view>
						<view class="plat-shop-order-card__guest" v-if="item.realName || item.phone">
							<view class="plat-shop-order-card__guest-item" v-if="item.realName">入住人 {{ item.realName }}</view>
							<view class="plat-shop-order-card__guest-item" v-if="item.phone">{{ hideMiddleDigits(item.phone) }}</view>
						</view>
					</view>
					<image :src="item.coverImage || '/static/logo.png'" mode="aspectFill" class="plat-shop-order-card__cover"></image>
				</view>

				<view class="plat-shop-stay">
					<view class="plat-shop-stay__item">
						<view class="plat-shop-stay__val">{{ formatDate(item.startDate) || '--' }}</view>
						<view class="plat-shop-stay__label">入住时间</view>
					</view>
					<view class="plat-shop-stay__sep" style="background-color: transparent;">
						<view class="plat-shop-stay__sep-line"></view>
						<view class="plat-shop-stay__sep-night">{{ getStayNights(item.startDate, item.endDate) }}</view>
						<view class="plat-shop-stay__sep-line"></view>
					</view>
					<view class="plat-shop-stay__item plat-shop-stay__item--end">
						<view class="plat-shop-stay__val">{{ formatDate(item.endDate) || '--' }}</view>
						<view class="plat-shop-stay__label">离店时间</view>
					</view>
				</view>

				<view class="plat-shop-order-card__foot">
					<view class="plat-shop-order-card__hint">{{ statusHint(item.orderStatus) }}</view>
					<view class="plat-shop-order-card__actions">
						<view class="plat-btn plat-btn--sm plat-btn--ghost" v-if="item.phone" @click.stop="makePhoneCall(item.phone)">联系住客</view>
						<view class="plat-btn plat-btn--sm" v-if="item.orderStatus == 1"
							@click.stop="routerGo('/shopCenter/order/sacnCode?orderNumber=' + item.orderNumber)">去核销</view>
						<view class="plat-btn plat-btn--sm" v-if="item.orderStatus == 2" @click.stop="finishOrder(item)">确认退房</view>
					</view>
				</view>
			</view>

			<up-loadmore :status="status" v-if="orderList.length >= 10" />

			<view class="plat-order-empty" v-if="orderList.length === 0 && !loading">
				<up-empty mode="order"></up-empty>
				<view class="plat-order-empty__text">当前筛选下暂无订单</view>
				<view class="plat-order-empty__desc">切换状态标签后，可查看其他履约阶段的订单。</view>
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

	const tabIndex = ref(0);
	const page = ref(1);
	const limit = ref(10);
	const totalPages = ref(1);
	const status = ref('loadmore');
	const orderList = reactive([]);
	const orderStatus = ref('');
	const loading = ref(false);

	const list1 = reactive([{
			name: '全部',
			id: ''
		},
		{
			name: '待核销',
			id: 1
		}, {
			name: '进行中',
			id: 2
		},
		{
			name: '已完成',
			id: 3
		},
		{
			name: '退款/售后',
			id: 4
		}
	]);

	const finishOrder = (item) => {
		uni.showModal({
			title: '提示',
			content: '确认用户已退房吗？',
			success: function(res) {
				if (res.confirm) {
					proxy.$http.postT('/app/orders/staffFinishOrder', {
						orderId: item.orderId
					}).then(red => {
						if (red.code == 0) {
							uni.showToast({
								title: '退房成功',
								icon: 'none'
							});
							setTimeout(() => {
								page.value = 1;
								getOrderList();
							});
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							});
						}
					});
				}
			}
		});
	};

	const getOrderList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			orderStatus: orderStatus.value,
			shopId: uni.getStorageSync('shopId')
		};
		loading.value = true;
		proxy.$http.get('/app/shops/selectOrderList', data).then(res => {
			loading.value = false;
			uni.stopPullDownRefresh();
			if (res.code == 0) {
				totalPages.value = res.data.pages || 1;
				if (page.value < totalPages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (page.value == 1) {
					orderList.splice(0, orderList.length, ...(res.data.records || []));
				} else {
					orderList.push(...(res.data.records || []));
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		}).catch(() => {
			loading.value = false;
			uni.stopPullDownRefresh();
		});
	};

	const orderStatusText = (val) => {
		switch (val) {
			case 1:
				return '待核销';
			case 2:
				return '进行中';
			case 3:
				return '已完成';
			case 4:
				return '已取消';
			default:
				return '全部';
		}
	};

	const statusClass = (val) => {
		switch (val) {
			case 1:
				return 'plat-shop-order-card__status--pending';
			case 2:
				return 'plat-shop-order-card__status--progress';
			case 3:
				return 'plat-shop-order-card__status--done';
			case 4:
				return 'plat-shop-order-card__status--cancel';
			default:
				return '';
		}
	};

	const statusHint = (val) => {
		switch (val) {
			case 1:
				return '用户待到店核销，前台可直接扫码办理入住';
			case 2:
				return '订单已入住，可在离店时完成退房';
			case 3:
				return '订单已完成，已结束本次履约';
			case 4:
				return '订单已取消，请关注退款或售后进度';
			default:
				return '进入详情可查看完整履约信息';
		}
	};

	const formatDate = (dateStr) => {
		if (!dateStr) {
			return '';
		}
		const date = new Date(dateStr.replace(/-/g, '/'));
		if (Number.isNaN(date.getTime())) {
			return '';
		}
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${month}月${day}日`;
	};

	const getStayNights = (startDate, endDate) => {
		if (!startDate || !endDate) {
			return '--';
		}
		const start = new Date(startDate.replace(/-/g, '/'));
		const end = new Date(endDate.replace(/-/g, '/'));
		if (Number.isNaN(start.getTime()) || Number.isNaN(end.getTime())) {
			return '--';
		}
		const diff = Math.round((end - start) / (24 * 60 * 60 * 1000));
		return diff > 0 ? `${diff}晚` : '--';
	};

	const isPhoneNumber = (phone) => {
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone);
	};

	const hideMiddleDigits = (phone) => {
		if (!phone) {
			return '';
		}
		if (isPhoneNumber(phone)) {
			return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
		}
		return phone;
	};

	const makePhoneCall = (phone) => {
		if (!phone) {
			return;
		}
		uni.makePhoneCall({
			phoneNumber: phone
		});
	};

	const syncOrderTab = (index = 0) => {
		const currentIndex = Number(index) || 0;
		tabIndex.value = currentIndex;
		const currentTab = list1[currentIndex] || list1[0];
		orderStatus.value = currentTab.id;
	};

	const click = (item) => {
		syncOrderTab(item.index);
		page.value = 1;
		getOrderList();
	};

	const routerGo = (url) => {
		uni.navigateTo({
			url
		});
	};

	onLoad(() => {});
	onShow(() => {
		syncOrderTab(uni.getStorageSync('index') || 0);
		page.value = 1;
		getOrderList();
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getOrderList();
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1;
			status.value = 'loading';
			getOrderList();
		}
	});
</script>

<style lang="scss" scoped>
	page {
		background: #f3f6fb;
	}

	.plat-shop-order-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #eaf2ff 0, #f3f6fb 220rpx, #f8fafc 100%);
	}

	.plat-shop-order-page .plat-filter-bar {
		position: sticky;
		top: 0;
		z-index: 20;
		padding: 18rpx 24rpx 14rpx;
		background: rgba(243, 246, 251, 0.94);
		backdrop-filter: blur(12px);
	}

	.plat-shop-order-page .plat-filter-bar :deep(.u-tabs) {
		padding: 8rpx;
		border-radius: 20rpx;
		background: rgba(255, 255, 255, 0.98);
		box-shadow: 0 8rpx 20rpx rgba(15, 23, 42, 0.04);
	}

	.plat-shop-order-page .plat-filter-bar :deep(.u-tabs__wrapper__nav__item) {
		height: 64rpx !important;
	}

	.plat-list-feed {
		padding: 20rpx 24rpx 28rpx;
	}

	.plat-shop-order-card {
		margin-bottom: 16rpx;
		padding: 22rpx;
		border-radius: 24rpx;
		background: rgba(255, 255, 255, 0.98);
		border: 1rpx solid rgba(226, 232, 240, 0.9);
		box-shadow: 0 10rpx 26rpx rgba(15, 23, 42, 0.05);
	}

	.plat-shop-order-card__head,
	.plat-shop-order-card__type-wrap,
	.plat-shop-order-card__main,
	.plat-shop-order-card__actions,
	.plat-shop-order-card__foot {
		display: flex;
	}

	.plat-shop-order-card__head,
	.plat-shop-order-card__foot {
		align-items: flex-start;
		justify-content: space-between;
		gap: 16rpx;
	}

	.plat-shop-order-card__type-wrap {
		align-items: center;
		gap: 10rpx;
		min-width: 0;
	}

	.plat-shop-order-card__type {
		height: 42rpx;
		padding: 0 14rpx;
		border-radius: 999rpx;
		background: #f3f7ff;
		font-size: 21rpx;
		font-weight: 600;
		color: #1d4ed8;
		display: inline-flex;
		align-items: center;
		gap: 8rpx;
	}

	.plat-shop-order-card__type-icon {
		width: 24rpx;
		height: 24rpx;
		flex-shrink: 0;
	}

	.plat-shop-order-card__status {
		flex-shrink: 0;
		height: 42rpx;
		padding: 0 14rpx;
		border-radius: 999rpx;
		font-size: 20rpx;
		font-weight: 600;
		display: inline-flex;
		align-items: center;
		justify-content: center;
	}

	.plat-shop-order-card__status--pending {
		background: #eff6ff;
		color: #2563eb;
	}

	.plat-shop-order-card__status--progress {
		background: #ecfdf5;
		color: #059669;
	}

	.plat-shop-order-card__status--done {
		background: #f8fafc;
		color: #475569;
	}

	.plat-shop-order-card__status--cancel {
		background: #fff1f2;
		color: #e11d48;
	}

	.plat-shop-order-card__main {
		align-items: center;
		gap: 16rpx;
		margin-top: 14rpx;
	}

	.plat-shop-order-card__main-body {
		flex: 1;
		min-width: 0;
	}

	.plat-shop-order-card__guest {
		display: flex;
		flex-wrap: wrap;
		gap: 8rpx;
		margin-top: 10rpx;
	}

	.plat-shop-order-card__guest-item {
		height: 38rpx;
		padding: 0 12rpx;
		border-radius: 12rpx;
		background: #f8fafc;
		font-size: 21rpx;
		color: #475569;
		display: inline-flex;
		align-items: center;
	}

	.plat-shop-order-card__cover {
		width: 124rpx;
		height: 124rpx;
		border-radius: 18rpx;
		flex-shrink: 0;
	}

	.plat-shop-stay {
		margin-top: 16rpx;
		padding: 18rpx 20rpx;
		border-radius: 20rpx;
		background: linear-gradient(135deg, #f8fbff, #f1f6ff);
		border: 1rpx solid rgba(219, 234, 254, 0.9);
		display: flex;
		align-items: center;
		gap: 16rpx;
	}

	.plat-shop-stay__item {
		flex: 1;
		min-width: 0;
	}

	.plat-shop-stay__item--end {
		text-align: right;
	}

	.plat-shop-stay__val {
		font-size: 26rpx;
		font-weight: 700;
		color: #0f172a;
	}

	.plat-shop-stay__label {
		margin-top: 6rpx;
		font-size: 22rpx;
		color: #64748b;
	}

	.plat-shop-stay__sep {
		width: 120rpx;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		gap: 8rpx;
	}

	.plat-shop-stay__sep-line {
		flex: 1;
		height: 2rpx;
		background: rgba(37, 99, 235, 0.18);
	}

	.plat-shop-stay__sep-night {
		flex-shrink: 0;
		font-size: 20rpx;
		font-weight: 600;
		color: #2563eb;
	}

	.plat-shop-order-card__foot {
		margin-top: 16rpx;
		align-items: flex-end;
	}

	.plat-shop-order-card__hint {
		flex: 1;
		min-width: 0;
		font-size: 21rpx;
		line-height: 1.5;
		color: #64748b;
	}

	.plat-shop-order-card__actions {
		flex-shrink: 0;
		gap: 12rpx;
		flex-wrap: wrap;
		justify-content: flex-end;
	}

	.plat-shop-order-page .plat-btn--sm {
		min-width: 136rpx;
		height: 54rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		font-size: 23rpx;
		box-sizing: border-box;
	}

	.plat-order-empty {
		padding: 120rpx 48rpx 0;
		text-align: center;
	}

	.plat-order-empty__text {
		margin-top: 20rpx;
		font-size: 28rpx;
		font-weight: 600;
		color: #0f172a;
	}

	.plat-order-empty__desc {
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #64748b;
	}

	.plat-shop-order-page :deep(.u-loadmore) {
		margin-top: 12rpx;
	}
</style>
