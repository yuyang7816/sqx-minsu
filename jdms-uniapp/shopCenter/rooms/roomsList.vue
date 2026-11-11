<template>
	<view class="plat-page plat-shop-list-page">
		<view class="plat-filter-bar">
			<up-tabs :list="list1" @click="click" :scrollable="false" :activeStyle="{
            color: '#2C8EFE',
            fontWeight: 'bold',
            transform: 'scale(1.05)'
        }" :inactiveStyle="{
            color: '#64748B',
            transform: 'scale(1)'
        }" lineColor="#2C8EFE" :current="currentTab"></up-tabs>
		</view>

		<view class="plat-list-feed" style="padding-top: 88rpx;">
			<view class="plat-shop-room-feed-head">
				<view>
					<view class="plat-shop-room-feed-head__title">房源管理</view>
					<view class="plat-shop-room-feed-head__desc">每条房源独立展示，可设置可售数量</view>
				</view>
				<view class="plat-shop-room-feed-head__badge">{{ currentTabName }}</view>
			</view>

			<view class="plat-card plat-cover-card plat-cover-card--row plat-shop-room-card" v-for="(item, index) in list" :key="item.roomId || index">
				<image :src="item.roomTitleImg || '/shopCenter/static/defaultRoom.png'" mode="aspectFill" class="plat-cover-card__img"></image>
				<view class="plat-cover-card__body">
					<view class="plat-shop-room-card__head">
						<text class="plat-cover-title">{{ item.roomName || '' }}</text>
						<view class="plat-shop-room-status" :class="item.isEnable == 1 ? 'plat-shop-room-status--free' : 'plat-shop-room-status--busy'">
							{{ item.isEnable == 1 ? '上架中' : '已下架' }}
						</view>
					</view>
					<view class="plat-cover-sub">
						{{ item.roomType || '' }} · {{ item.floorArea }}㎡  · 可售 {{ item.roomCount || 1 }} 间
					</view>
					<view class="plat-cover-sub">
						入住{{ item.checkInTime }} | 退房{{ item.checkOutTime }}
					</view>
					<view class="plat-cover-card__foot">
						<view class="plat-cover-price">
							<text class="unit">￥</text>
							<text class="num">{{ item.money || 0 }}</text>
						</view>
						<view class="plat-room-filter__vip" v-if="item.memberMoney">
							<text class="plat-room-filter__vip-badge">VIP</text>
							<text class="plat-room-filter__vip-price">￥{{ item.memberMoney || 0 }}</text>
						</view>
					</view>
					<view class="plat-shop-room-card__actions">
						<view class="plat-btn plat-btn--sm" @click="editRoom(item.roomId)">编辑</view>
						<view class="plat-btn plat-btn--sm plat-btn--ghost" @click="copyRoom(item.roomId)">复制</view>
						<view class="plat-btn plat-btn--sm plat-btn--ghost" @click="toggleStatus(item)">
							{{ item.isEnable == 1 ? '下架' : '上架' }}
						</view>
					</view>
				</view>
			</view>

			<up-loadmore :status="status" v-if="list.length > 0" />
		</view>

		<view class="plat-detail-empty" v-if="list.length === 0 && !loading">
			<image src="/shopCenter/static/noData.png" mode="" class="plat-detail-empty__img"></image>
			<view class="plat-detail-empty__text">暂无{{ currentTabName }}房源</view>
		</view>

		<view class="plat-sec" v-if="list.length === 0 && !loading">
			<view class="plat-btn" @click="addRoom">立即发布</view>
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
		onReachBottom,
		onPageScroll
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();

	const list1 = reactive([{
			name: '待上架',
			isEnable: 0
		},
		{
			name: '显示中',
			isEnable: 1
		},
		{
			name: '已下架',
			isEnable: 0
		}
	]);
	const currentTab = ref(0);
	const roomStatus = ref(0);
	const currentTabName = computed(() => list1[currentTab.value]?.name || '');

	const page = ref(1);
	const limit = ref(100);
	const totalPages = ref(1);
	const status = ref('loadmore');
	const list = reactive([]);
	const loading = ref(false);

	const click = (item) => {
		currentTab.value = item.index;
		roomStatus.value = list1[item.index].isEnable;
		page.value = 1;
		list.splice(0, list.length);
		getRoomList();
	};

	const getRoomList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			shopId: uni.getStorageSync('shopId'),
			isEnable: roomStatus.value
		}
		loading.value = true;
		uni.showLoading({
			title: '加载中...'
		});
		proxy.$http.getT('/app/shopsRoom/getShopRoomList', data).then(res => {
			uni.hideLoading();
			uni.stopPullDownRefresh();
			loading.value = false;
			if (res.code == 0) {
				totalPages.value = res.data.pages || 1;
				const records = res.data.records || [];
				if (page.value == 1) {
					list.splice(0, list.length, ...records);
				} else {
					list.push(...records);
				}
				if (page.value >= totalPages.value) {
					status.value = 'nomore';
				} else {
					status.value = 'loadmore';
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		}).catch(() => {
			uni.hideLoading();
			loading.value = false;
		});
	};

	const editRoom = (roomId) => {
		uni.navigateTo({
			url: `/shopCenter/rooms/addRoom?roomId=${roomId}`
		});
	};

	const copyRoom = (roomId) => {
		uni.navigateTo({
			url: `/shopCenter/rooms/addRoom?copyFromRoomId=${roomId}`
		});
	};

	const toggleStatus = (item) => {
		const newStatus = item.isEnable == 1 ? 0 : 1;
		const actionText = newStatus == 1 ? '上架' : '下架';

		uni.showModal({
			title: '提示',
			content: `确定要${actionText}该房间吗？`,
			success: (res) => {
				if (res.confirm) {
					proxy.$http.postJson('/app/shopsRoom/saveShopRoom', {
						roomId: item.roomId,
						isEnable: newStatus
					}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: `${actionText}成功`,
								icon: 'none'
							});
							getRoomList();
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							});
						}
					});
				}
			}
		});
	};

	const addRoom = () => {
		uni.navigateTo({
			url: '/shopCenter/rooms/addRoom'
		});
	};

	onPageScroll(e => {});
	onLoad(option => {});
	onShow(() => {
		page.value = 1;
		getRoomList();
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getRoomList();
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1;
			status.value = 'loading';
			getRoomList();
		}
	});
</script>

<style lang="scss" scoped>
	page {
		background: #f4f7fb;
	}

	.plat-shop-list-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #eef4ff 0, #f4f7fb 220rpx);
	}

	.plat-shop-list-page .plat-filter-bar {
		padding: 16rpx 24rpx 18rpx;
		background: rgba(244, 247, 251, 0.94);
		backdrop-filter: blur(10px);
	}

	.plat-shop-list-page .plat-filter-bar :deep(.u-tabs) {
		padding: 10rpx;
		border-radius: 24rpx;
		background: #ffffff;
		box-shadow: 0 6rpx 18rpx rgba(15, 23, 42, 0.04);
	}

	.plat-shop-list-page .plat-list-feed {
		padding-left: 24rpx;
		padding-right: 24rpx;
		padding-bottom: 32rpx;
	}

	.plat-shop-room-feed-head {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
		margin-bottom: 18rpx;
		padding: 0 4rpx;
	}

	.plat-shop-room-feed-head__title {
		font-size: 30rpx;
		font-weight: 700;
		color: #1e293b;
	}

	.plat-shop-room-feed-head__desc {
		margin-top: 8rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: #8b94a5;
	}

	.plat-shop-room-feed-head__badge {
		flex-shrink: 0;
		height: 52rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		background: #eef4ff;
		color: #2b6fe8;
		font-size: 22rpx;
		display: inline-flex;
		align-items: center;
	}

	.plat-room-group {
		margin-bottom: 16rpx;
		border-radius: 24rpx;
		background: #ffffff;
		box-shadow: 0 8rpx 24rpx rgba(15, 23, 42, 0.04);
		overflow: hidden;
	}

	.plat-room-group__head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 22rpx 24rpx;
		background: #f8fbff;
		border-bottom: 1rpx solid #edf1f6;
	}

	.plat-room-group__info {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}

	.plat-room-group__title {
		font-size: 28rpx;
		font-weight: 700;
		color: #1e293b;
	}

	.plat-room-group__meta {
		font-size: 22rpx;
		color: #64748b;
	}

	.plat-shop-list-page .plat-shop-room-card {
		margin: 0;
		border-radius: 0;
		box-shadow: none;
		border-bottom: 1rpx solid #edf1f6;
	}

	.plat-shop-list-page .plat-cover-card__img {
		border-radius: 20rpx;
	}

	.plat-shop-list-page .plat-shop-room-card__actions {
		margin-top: 18rpx;
		display: flex;
		gap: 12rpx;
	}

	.plat-shop-list-page .plat-btn--sm {
		flex: 1;
		height: 60rpx;
		border-radius: 999rpx;
	}

	.plat-shop-list-page .plat-detail-empty {
		padding: 120rpx 24rpx 0;
	}
</style>
