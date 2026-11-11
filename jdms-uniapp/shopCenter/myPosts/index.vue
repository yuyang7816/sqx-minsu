<template>
	<view class="plat-page plat-shop-list-page">
		<view class="plat-filter-bar">
			<view class="plat-room-hero">
				<view class="plat-room-hero__main">
					<view class="plat-room-hero__eyebrow">房源中心</view>
					<view class="plat-room-hero__title">我的发布</view>
					<view class="plat-room-hero__desc">统一查看上架、房态与价格信息</view>
				</view>
				<view class="plat-room-hero__action" @click="addRoom">
					<up-icon name="plus" color="#1D4ED8" size="16" bold></up-icon>
					<text>发布房间</text>
				</view>
			</view>

			<up-tabs :list="list1" @click="tabClick" :scrollable="false" :activeStyle="{
				color: '#2C8EFE',
				fontWeight: 'bold',
				transform: 'scale(1.05)'
			}" :inactiveStyle="{
				color: '#64748B',
				transform: 'scale(1)'
			}" lineColor="#2C8EFE" :current="currentTab"></up-tabs>
			<view class="plat-shop-subtabs" v-if="currentTab === 0">
				<view class="plat-shop-subtab" :class="{ 'plat-shop-subtab--on': currentRoomStatus === 0 }"
					@click="changeRoomStatus(0)">
					全部
				</view>
				<view class="plat-shop-subtab" :class="{ 'plat-shop-subtab--on': currentRoomStatus === 1 }"
					@click="changeRoomStatus(1)">
					空闲
				</view>
				<view class="plat-shop-subtab" :class="{ 'plat-shop-subtab--on': currentRoomStatus === 2 }"
					@click="changeRoomStatus(2)">
					使用中
				</view>
			</view>
		</view>

		<view class="plat-list-feed">


			<view class="plat-card plat-cover-card plat-cover-card--row plat-shop-room-card" v-for="(item, index) in list"
				:key="item.roomId || index">
				<view class="plat-shop-room-card__cover">
					<image :src="item.roomTitleImg || '/static/logo.png'" mode="aspectFill" class="plat-cover-card__img"></image>
					<!-- <view class="plat-shop-room-card__cover-badge">{{ index + 1 }}</view> -->
				</view>
				<view class="plat-cover-card__body">
					<view class="plat-shop-room-card__head">
						<view class="plat-shop-room-card__title-wrap"
							style="display: flex;align-items: center;justify-content: space-between;">
							<view class="plat-cover-title">{{ item.roomName || '' }}</view>
							<view class="plat-shop-room-card__chips">
								<view class="plat-shop-room-status"
									:class="item.roomStatus == 1 ? 'plat-shop-room-status--free' : 'plat-shop-room-status--busy'">
									{{ item.roomStatus == 1 ? '空闲' : '使用中' }}
								</view>
								<view class="plat-shop-room-enable"
									:class="item.isEnable == 1 ? 'plat-shop-room-enable--on' : 'plat-shop-room-enable--off'">
									{{ item.isEnable == 1 ? '已上架' : '已下架' }}
								</view>
							</view>
						</view>
					</view>

					<view v-if="item.roomType" class="plat-shop-room-card__type-row">
						<text class="plat-shop-room-card__type">{{ item.roomType }}</text>
					</view>

					<view class="plat-shop-room-card__meta">
						<view class="plat-shop-room-card__meta-item">{{ item.floorArea || 0 }}㎡</view>
						
						<view class="plat-shop-room-card__meta-item">入住 {{ item.checkInTime || '--' }}</view>
						<view class="plat-shop-room-card__meta-item">退房 {{ item.checkOutTime || '--' }}</view>
					</view>

					<view class="plat-cover-sub">
						<up-icon name="clock" size="14" color="#94A3B8"></up-icon>
						<text>{{ item.isEnable == 1 ? '当前可对外售卖' : '当前处于暂停售卖状态' }}</text>
					</view>

					<view class="plat-cover-card__foot">
						<view class="plat-cover-price" style="display: flex;align-items: baseline;">
							<view class="plat-cover-price__label">门市价</view>
							<text class="unit">￥</text>
							<text class="num">{{ item.money || 0 }}</text>
						</view>
						<view class="plat-detail-room__vip" v-if="item.memberMoney">
							<text class="plat-detail-room__vip-badge">VIP</text>
							<text class="plat-detail-room__vip-price">￥{{ item.memberMoney || 0 }}</text>
						</view>
					</view>

					<view class="plat-shop-room-card__actions">
						<view class="plat-btn plat-btn--sm plat-btn--ghost plat-btn--icon" @click="editRoom(item.roomId)">
							<up-icon name="edit-pen" size="14" color="#2C8EFE"></up-icon>
							<text>编辑</text>
						</view>
						<view class="plat-btn plat-btn--sm plat-btn--ghost" @click="toggleStatus(item)">
							{{ item.isEnable == 1 ? '下架' : '上架' }}
						</view>
					</view>
				</view>
			</view>

			<up-loadmore :status="status" v-if="list.length > 0" />
		</view>

		<view class="plat-detail-empty" v-if="list.length === 0 && !loading">
			<image src="/shopCenter/static/noData.png" mode="aspectFit" class="plat-detail-empty__img"></image>
			<view class="plat-detail-empty__text">暂无{{ currentTabName }}房源</view>
			<view class="plat-detail-empty__desc">先新增房间，再回到这里统一管理房态与价格。</view>
			<view class="plat-btn plat-detail-empty__btn" @click="addRoom">立即发布</view>
		</view>

		<view class="plat-page-spacer" v-if="list.length > 0"></view>
		<view class="plat-page-action" v-if="list.length > 0">
			<view class="plat-page-action__inner">
				<view class="plat-page-action__summary">
					<view class="plat-page-action__title">继续发布新房间</view>
					<view class="plat-page-action__desc">补充房型后会自动出现在当前列表</view>
				</view>
				<view class="plat-btn plat-page-action__btn" @click="addRoom">
					<up-icon name="plus" color="#FFFFFF" size="14" bold></up-icon>
					<text>新增</text>
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
		onReachBottom,
		onPageScroll
	} from '@dcloudio/uni-app';

	const {
		proxy
	} = getCurrentInstance();

	const list1 = reactive([{
			name: '上架中',
			status: 1
		},
		{
			name: '已下架',
			status: 0
		}
	]);
	const roomStatus = ref(1);
	const currentTab = ref(0);
	const currentTabName = computed(() => list1[currentTab.value]?.name || '');
	const totalCount = ref(0);

	// 二级筛选：房间状态 0全部 1空闲 2使用中
	const currentRoomStatus = ref(0);
	const currentStatusLabel = computed(() => {
		if (roomStatus.value !== 1) {
			return '已下架';
		}
		return ['全部房态', '空闲', '使用中'][currentRoomStatus.value] || '全部房态';
	});
	const currentStatusSummary = computed(() => {
		return roomStatus.value === 1 ? `按${currentStatusLabel.value}查看在售房间` : '集中处理暂停售卖的房间';
	});

	const page = ref(1);
	const limit = ref(10);
	const totalPages = ref(1);
	const status = ref('loadmore');
	const list = reactive([]);
	const loading = ref(false);

	const tabClick = (item) => {
		currentTab.value = item.index;
		roomStatus.value = item.status;
		currentRoomStatus.value = 0;
		page.value = 1;
		list.splice(0, list.length);
		getRoomList();
	};

	const changeRoomStatus = (statusVal) => {
		currentRoomStatus.value = statusVal;
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
		};
		if (roomStatus.value === 1 && currentRoomStatus.value !== 0) {
			data.roomStatus = currentRoomStatus.value;
		}
		loading.value = true;
		uni.showLoading({
			title: '加载中...'
		});
		proxy.$http.getT('/app/shopsRoom/getShopRoomList', data).then(res => {
			uni.hideLoading();
			loading.value = false;
			if (res.code == 0) {
				totalPages.value = res.data.pages || 1;
				const records = res.data.records || [];
				totalCount.value = res.data.total || records.length;
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
				totalCount.value = 0;
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		}).catch(() => {
			uni.hideLoading();
			loading.value = false;
			if (page.value === 1) {
				totalCount.value = 0;
			}
		});
	};

	const editRoom = (roomId) => {
		uni.navigateTo({
			url: `/shopCenter/rooms/addRoom?roomId=${roomId}`
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
					}).then(result => {
						if (result.code == 0) {
							uni.showToast({
								title: `${actionText}成功`,
								icon: 'none'
							});
							page.value = 1;
							getRoomList();
						} else {
							uni.showToast({
								title: result.msg,
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

	onPageScroll(() => {});
	onLoad(() => {});
	onShow(() => {
		page.value = 1;
		getRoomList();
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getRoomList();
		uni.stopPullDownRefresh();
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
		background: #eef3f8;
	}

	.plat-shop-list-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #dbeafe 0, #eef3f8 240rpx, #f8fafc 100%);
	}

	.plat-shop-list-page .plat-filter-bar {
		position: sticky;
		top: 0;
		z-index: 20;
		padding: 24rpx 24rpx 16rpx;
		background: linear-gradient(180deg, rgba(219, 234, 254, 0.92), rgba(238, 243, 248, 0.9));
		backdrop-filter: blur(16px);
	}

	.plat-room-hero {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
		padding: 12rpx 4rpx 24rpx;
	}

	.plat-room-hero__main {
		flex: 1;
		min-width: 0;
	}

	.plat-room-hero__eyebrow {
		font-size: 20rpx;
		font-weight: 600;
		color: #2563eb;
	}

	.plat-room-hero__title {
		margin-top: 10rpx;
		font-size: 44rpx;
		font-weight: 700;
		line-height: 1.15;
		color: #0f172a;
	}

	.plat-room-hero__desc {
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.5;
		color: #475569;
	}

	.plat-room-hero__action {
		flex-shrink: 0;
		height: 68rpx;
		padding: 0 22rpx;
		border-radius: 999rpx;
		background: rgba(255, 255, 255, 0.92);
		box-shadow: 0 10rpx 30rpx rgba(37, 99, 235, 0.12);
		display: inline-flex;
		align-items: center;
		justify-content: center;
		gap: 8rpx;
		font-size: 24rpx;
		font-weight: 600;
		color: #1d4ed8;
	}

	.plat-room-overview {
		display: grid;
		grid-template-columns: repeat(3, minmax(0, 1fr));
		gap: 12rpx;
		margin-bottom: 16rpx;
	}

	.plat-room-overview__item {
		min-height: 116rpx;
		padding: 18rpx;
		border-radius: 22rpx;
		background: rgba(255, 255, 255, 0.86);
		box-shadow: 0 10rpx 24rpx rgba(15, 23, 42, 0.05);
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.plat-room-overview__label {
		font-size: 22rpx;
		line-height: 1.4;
		color: #64748b;
	}

	.plat-room-overview__value {
		margin-top: 10rpx;
		font-size: 34rpx;
		font-weight: 700;
		line-height: 1.2;
		color: #0f172a;
	}

	.plat-room-overview__value--sm {
		font-size: 26rpx;
		line-height: 1.35;
	}

	.plat-shop-list-page .plat-filter-bar :deep(.u-tabs) {
		padding: 8rpx;
		border-radius: 22rpx;
		background: rgba(255, 255, 255, 0.94);
		box-shadow: 0 10rpx 24rpx rgba(15, 23, 42, 0.05);
	}

	.plat-shop-list-page .plat-filter-bar :deep(.u-tabs__wrapper__nav__item) {
		height: 68rpx !important;
	}

	.plat-shop-subtabs {
		margin-top: 12rpx;
		padding: 8rpx;
		border-radius: 20rpx;
		background: rgba(255, 255, 255, 0.9);
		box-shadow: 0 10rpx 24rpx rgba(15, 23, 42, 0.04);
		display: flex;
		gap: 8rpx;
	}

	.plat-shop-subtab {
		flex: 1;
		height: 56rpx;
		padding: 0 12rpx;
		border-radius: 999rpx;
		font-size: 22rpx;
		color: #64748b;
		display: inline-flex;
		align-items: center;
		justify-content: center;
	}

	.plat-shop-subtab--on {
		background: linear-gradient(135deg, #dbeafe, #eff6ff);
		color: #2563eb;
		font-weight: 600;
	}

	.plat-shop-list-page .plat-list-feed {
		padding: 20rpx 24rpx 12rpx;
	}

	.plat-shop-room-feed-head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 20rpx;
		margin-bottom: 18rpx;
		padding: 6rpx 4rpx 0;
	}

	.plat-shop-room-feed-head__main {
		flex: 1;
		min-width: 0;
	}

	.plat-shop-room-feed-head__title {
		font-size: 32rpx;
		font-weight: 700;
		color: #0f172a;
	}

	.plat-shop-room-feed-head__desc {
		margin-top: 6rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: #64748b;
	}

	.plat-shop-room-feed-head__badge {
		flex-shrink: 0;
		height: 50rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		background: rgba(37, 99, 235, 0.1);
		color: #1d4ed8;
		font-size: 22rpx;
		font-weight: 600;
		display: inline-flex;
		align-items: center;
	}

	.plat-shop-list-page .plat-shop-room-card {
		margin-bottom: 18rpx;
		padding: 20rpx;
		border-radius: 28rpx;
		background: rgba(255, 255, 255, 0.94);
		border: 1rpx solid rgba(226, 232, 240, 0.85);
		box-shadow: 0 16rpx 40rpx rgba(15, 23, 42, 0.06);
	}

	.plat-shop-room-card__cover {
		position: relative;
		width: 132rpx;
		height: 132rpx;
		margin-right: 18rpx;
		flex-shrink: 0;
	}

	.plat-shop-room-card__cover-badge {
		position: absolute;
		left: 10rpx;
		top: 10rpx;
		min-width: 36rpx;
		height: 36rpx;
		padding: 0 10rpx;
		border-radius: 999rpx;
		background: rgba(15, 23, 42, 0.68);
		color: #ffffff;
		font-size: 20rpx;
		display: inline-flex;
		align-items: center;
		justify-content: center;
	}

	.plat-shop-list-page .plat-cover-card__img {
		width: 100%;
		height: 100%;
		border-radius: 20rpx;
	}

	.plat-shop-list-page .plat-cover-card__body {
		min-width: 0;
		padding: 0;
	}

	.plat-shop-list-page .plat-shop-room-card__head {
		margin-bottom: 8rpx;
	}

	.plat-shop-room-card__title-wrap {
		width: 100%;
		min-width: 0;
	}

	.plat-shop-list-page .plat-cover-title {
		font-size: 30rpx;
		line-height: 1.35;
		color: #0f172a;
	}

	.plat-shop-room-card__chips {
		margin-top: 10rpx;
		display: flex;
		flex-wrap: wrap;
		gap: 10rpx;
	}

	.plat-shop-list-page .plat-shop-room-status,
	.plat-shop-room-enable {
		height: 38rpx;
		padding: 0 14rpx;
		border-radius: 999rpx;
		font-size: 20rpx;
		font-weight: 600;
		display: inline-flex;
		align-items: center;
		justify-content: center;
	}

	.plat-shop-room-enable--on {
		background: #ecfdf5;
		color: #059669;
	}

	.plat-shop-room-enable--off {
		background: #f1f5f9;
		color: #64748b;
	}

	.plat-shop-room-card__type-row {
		margin-bottom: 10rpx;
	}

	.plat-shop-list-page .plat-shop-room-card__type {
		display: inline-flex;
		align-items: center;
		height: 42rpx;
		padding: 0 14rpx;
		border-radius: 999rpx;
		font-size: 24rpx;
		color: #334155;
		background: #f8fafc;
	}

	.plat-shop-room-card__meta {
		display: flex;
		flex-wrap: wrap;
		gap: 10rpx;
	}

	.plat-shop-room-card__meta-item {
		height: 42rpx;
		padding: 0 14rpx;
		border-radius: 12rpx;
		background: #f8fafc;
		font-size: 22rpx;
		color: #475569;
		display: inline-flex;
		align-items: center;
	}

	.plat-shop-list-page .plat-cover-sub {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.5;
		color: #64748b;
		display: flex;
		align-items: center;
		gap: 8rpx;
	}

	.plat-shop-list-page .plat-cover-card__foot {
		margin-top: 14rpx;
		align-items: flex-end;
	}

	.plat-cover-price__label {
		margin-bottom: 4rpx;
		font-size: 20rpx;
		color: #94a3b8;
	}

	.plat-shop-list-page .plat-cover-price .unit {
		font-size: 22rpx;
	}

	.plat-shop-list-page .plat-cover-price .num {
		font-size: 42rpx;
	}

	.plat-shop-list-page .plat-room-filter__vip {
		margin-left: 12rpx;
		display: inline-flex;
		align-items: baseline;
		gap: 6rpx;
		padding: 6rpx 10rpx;
		border-radius: 12rpx;
		background: linear-gradient(135deg, #eff6ff, #dbeafe);
		border: 1rpx solid rgba(44, 142, 254, 0.18);
	}

	.plat-shop-list-page .plat-room-filter__vip-badge {
		font-size: 18rpx;
		font-weight: 700;
		color: #2C8EFE;
	}

	.plat-shop-list-page .plat-room-filter__vip-price {
		font-size: 22rpx;
		font-weight: 700;
		color: #1E2A3A;
	}


	.plat-shop-list-page .plat-shop-room-card__actions {
		margin-top: 16rpx;
		display: flex;
		gap: 12rpx;
	}

	.plat-shop-list-page .plat-btn--sm {
		flex: 1;
		height: 58rpx;
		padding: 0 20rpx;
		border-radius: 999rpx;
		font-size: 24rpx;
	}

	.plat-shop-list-page .plat-btn--icon {
		display: inline-flex;
		align-items: center;
		justify-content: center;
		gap: 8rpx;
	}

	.plat-shop-list-page .plat-detail-empty {
		padding: 120rpx 48rpx 0;
		text-align: center;
	}

	.plat-detail-empty__desc {
		margin-top: 14rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #64748b;
	}

	.plat-detail-empty__btn {
		margin-top: 32rpx;
	}

	.plat-page-spacer {
		height: calc(136rpx + env(safe-area-inset-bottom));
	}

	.plat-page-action {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		padding: 18rpx 24rpx calc(18rpx + env(safe-area-inset-bottom));
		background: linear-gradient(180deg, rgba(248, 250, 252, 0), rgba(248, 250, 252, 0.94) 30%, #f8fafc 100%);
	}

	.plat-page-action__inner {
		padding: 18rpx 20rpx;
		border-radius: 24rpx;
		background: rgba(15, 23, 42, 0.94);
		box-shadow: 0 18rpx 40rpx rgba(15, 23, 42, 0.18);
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 18rpx;
	}

	.plat-page-action__summary {
		flex: 1;
		min-width: 0;
	}

	.plat-page-action__title {
		font-size: 28rpx;
		font-weight: 700;
		color: #ffffff;
	}

	.plat-page-action__desc {
		margin-top: 6rpx;
		font-size: 22rpx;
		line-height: 1.4;
		color: rgba(255, 255, 255, 0.72);
	}

	.plat-page-action__btn {
		flex-shrink: 0;
		min-width: 138rpx;
		height: 64rpx;
		padding: 0 20rpx;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #3b82f6, #2563eb);
		display: inline-flex;
		align-items: center;
		justify-content: center;
		gap: 8rpx;
	}

	.plat-shop-list-page :deep(.u-loadmore) {
		margin-top: 12rpx;
	}

	.plat-detail-room__vip {
		margin-top: 10rpx;
		display: inline-flex;
		align-items: baseline;
		gap: 8rpx;
		padding: 6rpx 10rpx;
		border-radius: 12rpx;
		background: linear-gradient(135deg, #eff6ff, #dbeafe);
		border: 1rpx solid rgba(44, 142, 254, 0.18);
	}

	.plat-detail-room__vip-badge {
		font-size: 18rpx;
		font-weight: 700;
		color: #2C8EFE;
		line-height: 1;
	}

	.plat-detail-room__vip-price {
		font-size: 22rpx;
		font-weight: 700;
		color: #1E2A3A;
		line-height: 1;
	}
</style>