<template>
	<view class="guest-page search-page">
		<view class="search-shell">
			<view class="search-panel guest-card">
				<view class="search-tabs">
					<view class="search-tab" :class="currentTabIndex === index ? 'is-on' : ''" v-for="(item, index) in tabList"
						:key="item.value" @click="changeTab(index)">
						{{ item.name }}
					</view>
				</view>

				<view class="search-field search-field--location" @click="chooseLocation">
					<view class="search-field__icon">
						<up-icon name="map-fill" size="18" color="#2C8EFE"></up-icon>
					</view>
					<view class="search-field__body">
						<view class="search-field__label">目的地</view>
						<view class="search-location__main text-cut" v-if="displayLocationName">
							{{ displayLocationText }}
						</view>
						<view class="search-location__main search-location__main--ph" v-else>选择目的地或附近位置</view>
					</view>
					<view class="search-location__action">
						<text>当前定位</text>
						<image src="/static/image/index/wz.png" mode="aspectFit" class="search-location__icon"></image>
					</view>
				</view>

				<view class="search-date" @click="calendarShow = true">
					<view class="search-date__col">
						<view class="search-date__label">入住</view>
						<view class="search-date__val">{{ startTimeStr }}</view>
					</view>
					<view class="search-date__mid">
						<view class="search-date__line"></view>
						<view class="search-date__nights">共{{ nightCount }}晚</view>
						<view class="search-date__line"></view>
					</view>
					<view class="search-date__col search-date__col--end">
						<view class="search-date__label">离店</view>
						<view class="search-date__val">{{ endTimeStr }}</view>
					</view>
				</view>

				<view class="search-keyword">
					<view class="search-field__icon search-field__icon--keyword">
						<up-icon name="search" size="18" color="#94A3B8"></up-icon>
					</view>
					<up-input v-model="keyValue" placeholder="搜索酒店/地名/地标" border="none" confirmType="search"
						placeholder-class="search-keyword__ph" @confirm="confirmSearch"></up-input>
				</view>

				<view class="search-actions">
					<view class="guest-btn guest-btn--muted search-actions__reset" @click="resetFilter">重置</view>
					<view class="guest-btn search-actions__primary" @click="triggerSearch">搜索</view>
				</view>
			</view>
		</view>

		<view class="guest-section search-feed">
			<!-- <view class="search-feed__head" v-if="hotelList.length > 0">
				<view class="search-feed__title">为你找到 {{ hotelList.length }} 家可订房源</view>
				<view class="search-feed__desc">{{ addressInfo.city ? addressInfo.city.replace(/市$/, '') : '当前城市' }} · {{ nightCount }}晚</view>
			</view> -->

			<view class="guest-card search-feed__item" v-for="(item, index) in hotelList" :key="item.shopId || index">
				<view class="guest-hotel search-hotel" @click="routerGo('/subpackage/hotel/hotelDetail?shopId=' + item.shopId)">
					<image :src="item.titleImg ? item.titleImg : '/static/logo.png'" mode="aspectFill" class="guest-hotel__img"></image>
					<view class="guest-hotel__body">
						<view>
							<view class="guest-hotel__name search-hotel__name">{{ item.shopName || '' }}</view>
							<view class="search-hotel__tags">
								<view class="guest-badge">{{ item.score || '3.5' }} 分</view>
								<view class="guest-badge guest-badge--soft" v-for="tag in getTags(item.tags)" :key="tag">{{ tag }}</view>
							</view>
							<view class="guest-hotel__addr search-hotel__addr">{{ item.county || '' }}/{{ item.address || '' }}</view>
						</view>
						<view class="guest-hotel__foot search-hotel__foot">
							<view class="guest-price">
								<text class="sym">￥</text>
								<text class="num">{{ item.minMoney || '0.00' }}</text>
								<text class="search-hotel__price-suffix">起</text>
							</view>
							<view class="guest-btn guest-btn--sm search-hotel__btn">预订</view>
						</view>
					</view>
				</view>
			</view>

			<empty v-if="hotelList.length === 0 && !loading" text="暂无符合条件的酒店"></empty>
			<uni-load-more v-if="hotelList.length > 0" :status="status"></uni-load-more>
		</view>

		<up-calendar :show="calendarShow" mode="range" :defaultDate="[startTimeDate, endTimeDate]" @confirm="calendarConfirm"
			startText="入住" endText="离店" monthNum="6" round="12" title="选择日期" @close="calendarShow = false"></up-calendar>
	</view>
</template>

<script setup>
	import {
		computed,
		getCurrentInstance,
		reactive,
		ref
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	import {
		saveAddressInfo,
		restoreAddressInfo,
		isDevMockLocation,
		clearAddressInfo
	} from '@/common/location.js';

	const {
		proxy
	} = getCurrentInstance();

	const tabList = [{
		name: '国内',
		value: 0
	}, {
		name: '钟点房',
		value: 1
	}, {
		name: '民宿',
		value: 2
	}];
	const currentTabIndex = ref(0);
	const calendarShow = ref(false);
	const keyValue = ref('');
	const hotelList = reactive([]);
	const addressInfo = reactive({});
	const startTime = ref('');
	const endTime = ref('');
	const startTimeDate = ref('');
	const endTimeDate = ref('');
	const page = ref(1);
	const limit = ref(10);
	const totalPages = ref(1);
	const status = ref('loadmore');
	const loading = ref(false);

	const formatDateStandard = (date) => {
		const year = date.getFullYear();
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${year}-${month}-${day}`;
	};

	const formatDateDisplay = (date) => {
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${month}月${day}日`;
	};

	const initTime = () => {
		const now = new Date();
		const tomorrow = new Date(now);
		tomorrow.setDate(now.getDate() + 1);
		const dayAfterTomorrow = new Date(now);
		dayAfterTomorrow.setDate(now.getDate() + 2);
		startTime.value = formatDateDisplay(tomorrow);
		endTime.value = formatDateDisplay(dayAfterTomorrow);
		startTimeDate.value = formatDateStandard(tomorrow);
		endTimeDate.value = formatDateStandard(dayAfterTomorrow);
	};

	const startTimeStr = computed(() => startTime.value || '06月05日');
	const endTimeStr = computed(() => endTime.value || '06月06日');
	const nightCount = computed(() => {
		if (!startTimeDate.value || !endTimeDate.value) {
			return 1;
		}
		const start = new Date(startTimeDate.value.replace(/-/g, '/'));
		const end = new Date(endTimeDate.value.replace(/-/g, '/'));
		const diff = Math.ceil((end - start) / (1000 * 60 * 60 * 24));
		return diff > 0 ? diff : 1;
	});

	const displayLocationName = computed(() => {
		return addressInfo.city || addressInfo.province || addressInfo.district || '';
	});

	const displayLocationText = computed(() => {
		const name = displayLocationName.value.replace(/市$/, '');
		if (!name) return '';
		return `${name}.${addressInfo.building || '附近'}`;
	});

	const getTags = (tags) => {
		if (!tags) {
			return [];
		}
		return String(tags).split(',').filter(Boolean).slice(0, 2);
	};

	const calendarConfirm = (dates) => {
		const start = dates[0];
		const end = dates[dates.length - 1];
		startTime.value = formatDateDisplay(new Date(start.replace(/-/g, '/')));
		endTime.value = formatDateDisplay(new Date(end.replace(/-/g, '/')));
		startTimeDate.value = start;
		endTimeDate.value = end;
		calendarShow.value = false;
		triggerSearch();
	};

	const selectCity = (longitude, latitude, name, fromManualPick = false) => {
		proxy.$http.getT('/app/Login/selectCity', {
			lat: latitude,
			lng: longitude
		}).then(res => {
			if (res.code === 0) {
				const nextInfo = {
					...(res.data || {}),
					longitude,
					latitude
				};
				if (name) {
					nextInfo.building = name;
				}
				if (!fromManualPick && isDevMockLocation(nextInfo)) {
					uni.showToast({
						title: '当前为模拟定位，请手动选择目的地',
						icon: 'none'
					});
					return;
				}
				Object.assign(addressInfo, nextInfo);
				saveAddressInfo({
					...addressInfo
				});
				triggerSearch();
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		});
	};

	const buildListParams = () => {
		const params = {
			page: page.value,
			limit: limit.value,
			lng: addressInfo.longitude,
			lat: addressInfo.latitude,
			city: addressInfo.city || addressInfo.province || addressInfo.district || '',
			startDate: startTimeDate.value,
			endDate: endTimeDate.value
		}
		if (keyValue.value) {
			params.shopName = keyValue.value
		}
		if (currentTabIndex.value === 1) {
			params.hasHourlyRoom = 1
		} else if (currentTabIndex.value === 2) {
			params.shopType = 2
		}
		return params
	}

	const getHotelList = () => {
		loading.value = true;
		status.value = page.value < totalPages.value ? 'loading' : status.value;
		proxy.$http.getT('/app/shops/getAppShopList', buildListParams()).then(res => {
			loading.value = false;
			uni.stopPullDownRefresh();
			if (res.code === 0) {
				totalPages.value = res.data?.pages || 1;
				status.value = page.value < totalPages.value ? 'loadmore' : 'nomore';
				const records = res.data?.records || [];
				if (page.value === 1) {
					hotelList.splice(0, hotelList.length, ...records);
				} else {
					hotelList.push(...records);
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
			status.value = 'loadmore';
		});
	};

	const triggerSearch = () => {
		page.value = 1;
		status.value = 'loading';
		getHotelList();
	};

	const confirmSearch = (value) => {
		keyValue.value = typeof value === 'string' ? value : keyValue.value;
		triggerSearch();
	};

	const changeTab = (index) => {
		if (currentTabIndex.value === index) {
			return;
		}
		currentTabIndex.value = index;
		triggerSearch();
	};

	const chooseLocation = () => {
		uni.chooseLocation({
			success: (res) => {
				selectCity(res.longitude, res.latitude, res.name, true);
			}
		});
	};

	const resetFilter = () => {
		keyValue.value = '';
		initTime();
		triggerSearch();
	};

	const routerGo = (url) => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url
			});
			return;
		}
		uni.showModal({
			title: '提示',
			content: '您还未登录,请先登录',
			success: function(res) {
				if (res.confirm) {
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}
			}
		});
	};

	onLoad((option) => {
		initTime();
		const tabIndex = Number(option?.shopType);
		if (!Number.isNaN(tabIndex) && tabIndex >= 0 && tabIndex < tabList.length) {
			currentTabIndex.value = tabIndex;
		}
		if (option?.keyword) {
			keyValue.value = option.keyword;
		}
		restoreAddressInfo(addressInfo);
		triggerSearch();
	});

	const resetMockAddress = () => {
		if (!isDevMockLocation(addressInfo)) {
			return false
		}
		clearAddressInfo()
		Object.keys(addressInfo).forEach((key) => {
			delete addressInfo[key]
		})
		return true
	}

	onShow(() => {
		if (resetMockAddress()) {
			triggerSearch()
		}
	});

	onPullDownRefresh(() => {
		page.value = 1;
		getHotelList();
	});

	onReachBottom(() => {
		if (page.value >= totalPages.value || loading.value) {
			return;
		}
		page.value += 1;
		status.value = 'loading';
		getHotelList();
	});
</script>

<style lang="scss" scoped>
	.search-page {
		padding-bottom: 40rpx;
	}

	.search-shell {
		padding: 24rpx 24rpx 0;
	}

	.search-panel {
		margin: 0;
		padding: 20rpx;
		border-radius: 36rpx;
		box-shadow: 0 18rpx 44rpx rgba(15, 23, 42, 0.04);
	}

	.search-tabs {
		display: flex;
		padding: 8rpx;
		gap: 12rpx;
		border-radius: 999rpx;
		background: #f4f7fb;
	}

	.search-tab {
		flex: 1;
		height: 68rpx;
		padding: 0 18rpx;
		border-radius: 36rpx;
		background: transparent;
		font-size: 26rpx;
		color: #64748b;
		display: flex;
		align-items: center;
		justify-content: center;
		font-weight: 500;
	}

	.search-tab.is-on {
		background: linear-gradient(135deg, #5ea7fb 0%, #1f78f0 100%);
		color: #ffffff;
		font-weight: 600;
		box-shadow: 0 10rpx 22rpx rgba(44, 142, 254, 0.24);
	}

	.search-field,
	.search-date,
	.search-keyword {
		margin-top: 18rpx;
		border-radius: 28rpx;
		background: linear-gradient(180deg, #fbfdff 0%, #f7faff 100%);
		border: 1rpx solid #edf2f8;
	}

	.search-field {
		padding: 22rpx 24rpx;
		display: flex;
		align-items: center;
		gap: 18rpx;
	}

	.search-field__icon {
		width: 68rpx;
		height: 68rpx;
		border-radius: 20rpx;
		background: rgba(44, 142, 254, 0.1);
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.search-field__icon--keyword {
		width: 56rpx;
		height: 56rpx;
		border-radius: 16rpx;
		background: #f1f5f9;
	}

	.search-field__body {
		flex: 1;
		min-width: 0;
	}

	.search-field__label {
		font-size: 22rpx;
		color: #94a3b8;
		line-height: 1.2;
	}

	.search-location__main {
		margin-top: 8rpx;
		font-size: 30rpx;
		font-weight: 600;
		color: #0f172a;
		line-height: 1.4;
	}

	.search-location__main--ph {
		font-weight: 400;
		color: #94a3b8;
	}

	.search-location__action {
		flex-shrink: 0;
		display: flex;
		align-items: center;
		gap: 6rpx;
		font-size: 24rpx;
		color: #2c8efe;
		background: rgba(44, 142, 254, 0.08);
		padding: 12rpx 16rpx;
		border-radius: 999rpx;
	}

	.search-location__icon {
		width: 24rpx;
		height: 24rpx;
	}

	.search-date {
		padding: 26rpx 24rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 12rpx;
	}

	.search-date__col {
		flex: 1;
		min-width: 0;
	}

	.search-date__col--end {
		text-align: right;
	}

	.search-date__label {
		font-size: 22rpx;
		color: #94a3b8;
	}

	.search-date__val {
		margin-top: 8rpx;
		font-size: 36rpx;
		font-weight: 600;
		color: #0f172a;
	}

	.search-date__mid {
		flex-shrink: 0;
		display: flex;
		align-items: center;
		gap: 12rpx;
	}

	.search-date__line {
		width: 40rpx;
		height: 2rpx;
		background: #dbe3ee;
	}

	.search-date__nights {
		font-size: 22rpx;
		color: #2c8efe;
		white-space: nowrap;
	}

	.search-keyword {
		padding: 0 18rpx;
		display: flex;
		align-items: center;
		gap: 14rpx;
	}

	.search-keyword__ph {
		color: #94a3b8;
		font-size: 28rpx;
	}

	.search-actions {
		margin-top: 22rpx;
		display: flex;
		gap: 16rpx;
	}

	.search-actions__primary,
	.search-actions .guest-btn {
		height: 88rpx;
		border-radius: 24rpx;
	}

	.search-actions__reset {
		width: 184rpx;
		flex: none;
	}

	.search-actions__primary {
		flex: 1;
		height: 88rpx;
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 16rpx 30rpx rgba(44, 142, 254, 0.22);
	}

	.search-feed {
	}

	.search-feed__head {
		display: flex;
		align-items: flex-end;
		justify-content: space-between;
		gap: 16rpx;
		padding: 6rpx 4rpx 18rpx;
	}

	.search-feed__title {
		font-size: 30rpx;
		font-weight: 600;
		color: #0f172a;
	}

	.search-feed__desc {
		font-size: 22rpx;
		color: #94a3b8;
		white-space: nowrap;
	}

	.search-feed__item {
		border-radius: 32rpx;
		box-shadow: 0 12rpx 32rpx rgba(148, 163, 184, 0.08);
	}

	.search-feed .guest-card {
		margin-bottom: 20rpx;
	}

	.search-hotel {
		padding: 22rpx;
	}

	.search-hotel .guest-hotel__img {
		width: 188rpx;
		height: 188rpx;
		margin-right: 18rpx;
		border-radius: 24rpx;
	}

	.search-hotel__name {
		font-size: 32rpx;
	}

	.search-hotel__tags {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		gap: 10rpx;
		margin-top: 12rpx;
	}

	.search-hotel__tags .guest-badge {
		margin-right: 0;
	}

	.search-hotel__addr {
		margin-top: 12rpx;
		line-height: 1.5;
	}

	.search-hotel__foot {
		margin-top: 20rpx;
	}

	.search-hotel__price-suffix {
		font-size: 24rpx;
		font-weight: 400;
		color: #94a3b8;
		margin-left: 4rpx;
	}

	.search-hotel__btn {
		min-width: 108rpx;
		padding: 0 24rpx;
	}
</style>
