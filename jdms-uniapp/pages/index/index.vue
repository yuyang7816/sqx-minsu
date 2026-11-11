<template>
	<view class="guest-page">
		<view class="home-banner">
			<swiper :autoplay="true" interval="2000" duration="500" :circular="true" class="home-banner__swiper">
				<swiper-item v-for="(item,index) in swiperList" :key='index' @click="goNav(item)">
					<image :src="item.imageUrl" mode="scaleToFill" class="home-banner__img"></image>
				</swiper-item>
			</swiper>
		</view>

		<view class="home-search guest-card">
			<view class="home-search__tabs">
				<view class="home-search__tab" :class="tabIndex==index?'is-on':''" v-for="(item,index) in tabIte" :key="index"
					@click="changeTabItem(item,index)">
					{{item.name}}
				</view>
			</view>

			<view class="home-search__loc" @click="chooseLocation">
				<view class="home-search__loc-main text-cut" v-if="displayLocationName">
					{{ displayLocationText }}
				</view>
				<view class="home-search__loc-main home-search__loc-main--ph" v-else>选择目的地或附近位置</view>
				<view class="home-search__loc-btn">
					<text>当前定位</text>
					<image src="/static/image/index/wz.png" mode="aspectFit" class="home-search__loc-icon"></image>
				</view>
			</view>

			<view class="home-search__date" @click="calendarShow = true">
				<view class="home-search__date-col">
					<view class="home-search__date-label">入住</view>
					<view class="home-search__date-val">{{startTimeStr}}</view>
				</view>
				<view class="home-search__date-mid">
					<view class="home-search__date-line"></view>
					<view class="home-search__date-nights">共{{nightCount}}晚</view>
					<view class="home-search__date-line"></view>
				</view>
				<view class="home-search__date-col home-search__date-col--end">
					<view class="home-search__date-label">离店</view>
					<view class="home-search__date-val">{{endTimeStr}}</view>
				</view>
			</view>

			<view class="home-search__keyword">
				<up-input placeholder="搜索酒店/地名/地标" border="none" v-model="keyValue" @confirm="confirmInpt"
					placeholder-class="home-search__input-ph"></up-input>
			</view>

			<view class="home-search__btn" @click="routerGo('/pages/index/search?shopType='+tabIndex)">查找房源</view>
		</view>

		<view class="guest-section">
			<view class="guest-card">
				<view class="guest-section__head">
					<image src="/static/image/index/rmtj.png" mode="" style="width: 150rpx;height: 30rpx;"></image>
					<view class="guest-badge" style="margin-left: 12rpx;">精选推荐</view>
				</view>
				<view v-for="item,index in hotelList" :key="index">
					<view class="guest-hotel" @click="routerGo('/subpackage/hotel/hotelDetail?shopId='+item.shopId)">
						<image :src="item.titleImg?item.titleImg:'/static/logo.png'" mode="aspectFill" class="guest-hotel__img"></image>
						<view class="guest-hotel__body">
							<view>
								<view class="guest-hotel__name">{{item.shopName||''}}</view>
								<view class="flex align-center margin-tb-xs">
									<view class="guest-badge">{{item.score||'3.5'}} 分</view>
									<view class="guest-badge guest-badge--soft" v-for="tag in item.tags.split(',').slice(0,2)" v-if="item.tags">{{tag||''}}</view>
								</view>
								<view class="guest-hotel__addr">{{item.county||''}}/{{item.address||''}}</view>
							</view>
							<view class="guest-hotel__foot">
								<view class="guest-price"><text class="sym">￥</text><text class="num">{{item.minMoney||'0.00'}}</text><text class="f26 c99" style="font-weight:400;">起</text></view>
								<view class="guest-btn guest-btn--sm">预订</view>
							</view>
						</view>
					</view>
					<view class="guest-divider" v-if="index != hotelList.length - 1"></view>
				</view>
				<empty v-if="hotelList.length == 0"></empty>
			</view>
		</view>

		<up-calendar :show="calendarShow" mode="range" :defaultDate="[startTimeDate, endTimeDate]"
			@confirm="calendarConfirm" startText="入住" endText="离店" monthNum="6" round="12" title="选择日期"
			@close="calendarShow = false"></up-calendar>
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
	import {
		parseInviteCodeFromOptions,
		saveInviteCode
	} from '@/common/queue.js';
	import {
		saveAddressInfo,
		restoreAddressInfo,
		isDevMockLocation,
		clearAddressInfo
	} from '@/common/location.js';
	const {
		proxy
	} = getCurrentInstance();

	const calendarShow = ref(false);
	const keyValue = ref('');
	const swiperList = reactive([]);
	const tabIte = reactive([{
		name: '国内'
	}, {
		name: '钟点房'
	}, {
		name: '民宿'
	}]);
	const tabIndex = ref(0);
	const isTop = ref(false);
	const hotelList = reactive([]);
	const startTime = ref('');
	const endTime = ref('');
	const startTimeDate = ref('');
	const endTimeDate = ref('');
	const shopName = ref('');
	const page = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const totalPages = ref(1);
	const addressInfo = reactive({});

	const calendarConfirm = (e) => {
		console.log(e);
		const start = e[0];
		const end = e[e.length - 1];
		startTime.value = formatDates(start);
		endTime.value = formatDates(end);
		startTimeDate.value = start;
		endTimeDate.value = end;
		calendarShow.value = false;
		page.value = 1
		getHotelList()
	};

	const formatDates = (dateStr) => {
		const date = new Date(dateStr.replace(/-/g, '/'));
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${month}月${day}日`;
	};

	const formatDateStandard = (date) => {
		const year = date.getFullYear();
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${year}-${month}-${day}`;
	};

	const formatDate = (date) => {
		const month = (date.getMonth() + 1).toString().padStart(2, '0');
		const day = date.getDate().toString().padStart(2, '0');
		return `${month}月${day}日`;
	};

	const initTime = () => {
		const now = new Date();
		const tomorrow = new Date(now);
		tomorrow.setDate(now.getDate() + 1);
		const dayAfterTomorrow = new Date(now);
		dayAfterTomorrow.setDate(now.getDate() + 2);
		startTime.value = formatDate(tomorrow);
		endTime.value = formatDate(dayAfterTomorrow);
		startTimeDate.value = formatDateStandard(tomorrow);
		endTimeDate.value = formatDateStandard(dayAfterTomorrow);
	};

	const startTimeStr = computed(() => startTime.value || '06月05日');
	const endTimeStr = computed(() => endTime.value || '06月05日');

	const nightCount = computed(() => {
		if (!startTimeDate.value || !endTimeDate.value) return 1;
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

	const chooseLocation = () => {
		uni.chooseLocation({
			success: function(res) {
				selectCity(res.longitude, res.latitude, res.name, true)
			}
		});
	}

	const confirmInpt = (e) => {
		keyValue.value = e
		page.value = 1
		getHotelList()
		console.log(e, "eeeeeeee")
	};

	const getBannerList = () => {
		proxy.$http.getT('/app/banner/selectBannerList', {
			classify: 1
		}).then(res => {
			if (res.code == 0 && res.data && res.data.length) {
				swiperList.splice(0, swiperList.length, ...res.data.map(item => ({
					imageUrl: item.imageUrl,
					url: item.url || ''
				})))
			}
		})
	};

	const goNav = (item) => {
		if (!item || !item.url) {
			return
		}
		if (/^https?:\/\//.test(item.url)) {
			// #ifdef H5
			window.open(item.url)
			// #endif
			// #ifdef APP-PLUS
			plus.runtime.openURL(item.url)
			// #endif
			return
		}
		routerGo(item.url.startsWith('/') ? item.url : '/' + item.url)
	};

	const buildListParams = () => {
		const data = {
			page: page.value,
			limit: limit.value,
			lng: addressInfo.longitude,
			lat: addressInfo.latitude,
			city: addressInfo.city || addressInfo.province || addressInfo.district || '',
			isRecommend: 1
		}
		if (keyValue.value) {
			data.shopName = keyValue.value
		}
		if (tabIndex.value === 1) {
			data.hasHourlyRoom = 1
		} else if (tabIndex.value === 2) {
			data.shopType = 2
		}
		return data
	}

	const getHotelList = () => {
		let data = buildListParams()
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/shops/getAppShopList', data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
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
		})
	};

	const routerGo = (url) => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url
			})
		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
	};
	const goSearch = () => {
		uni.navigateTo({
			url: '/pages/index/search'
		})
	};
	const changeTabItem = (item, index) => {
		tabIndex.value = index
		page.value = 1
		getHotelList()
	};

	const selectCity = (longitude, latitude, name, fromManualPick = false) => {
		proxy.$http.getT('/app/Login/selectCity', {
			lat: latitude,
			lng: longitude
		}).then(res => {
			if (res.code == 0) {
				const nextInfo = {
					...(res.data || {}),
					longitude,
					latitude
				}
				if (name) {
					nextInfo.building = name
				}
				if (!fromManualPick && isDevMockLocation(nextInfo)) {
					uni.showToast({
						title: '当前为模拟定位，请手动选择目的地',
						icon: 'none'
					})
					return
				}
				Object.assign(addressInfo, nextInfo)
				saveAddressInfo({
					...addressInfo
				})
				page.value = 1
				getHotelList()
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	onPageScroll(e => {
		if (e.scrollTop > 20) {
			isTop.value = true
		} else {
			isTop.value = false
		}
	});
	onLoad(option => {
		const inviteCode = parseInviteCodeFromOptions(option);
		if (inviteCode) {
			saveInviteCode(inviteCode);
		}
		initTime();
		getBannerList();
		restoreAddressInfo(addressInfo);
		getHotelList();
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
			getHotelList()
		}
	});

	onPullDownRefresh(() => {
		page.value = 1;
		getBannerList();
		getHotelList();
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1
			status.value = 'loading';
			getHotelList();
		}
	});
</script>

<style lang="scss" scoped>
	.home-banner {
		width: 100%;
		height: 400rpx;
	}

	.home-banner__swiper,
	.home-banner__img {
		width: 100%;
		height: 400rpx;
	}

	.home-search {
		margin: -72rpx 28rpx 0;
		position: relative;
		z-index: 10;
		padding: 32rpx 28rpx 36rpx;
		border-radius: 32rpx;
		border: 1rpx solid #EEF1F6;
	}

	.home-search__tabs {
		display: flex;
		background: #F3F6FA;
		border-radius: 20rpx;
		padding: 6rpx;
		margin-bottom: 28rpx;
	}

	.home-search__tab {
		flex: 1;
		text-align: center;
		font-size: 28rpx;
		color: #94A3B8;
		padding: 18rpx 0;
		border-radius: 16rpx;
		transition: all 0.2s;
	}

	.home-search__tab.is-on {
		background: #FFFFFF;
		color: #2C8EFE;
		font-weight: 500;
	}

	.home-search__loc {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding-bottom: 28rpx;
		border-bottom: 1rpx solid #EEF2F6;
	}

	.home-search__loc-main {
		flex: 1;
		min-width: 0;
		font-size: 32rpx;
		font-weight: 600;
		color: #334155;
		padding-right: 16rpx;
	}

	.home-search__loc-main--ph {
		font-weight: 500;
		color: #9CA3AF;
		font-size: 28rpx;
	}

	.home-search__loc-btn {
		display: flex;
		align-items: center;
		flex-shrink: 0;
		font-size: 26rpx;
		color: #2C8EFE;
		font-weight: 500;
	}

	.home-search__loc-icon {
		width: 28rpx;
		height: 36rpx;
		margin-left: 8rpx;
	}

	.home-search__date {
		display: flex;
		align-items: center;
		padding: 28rpx 0;
		border-bottom: 1rpx solid #EEF2F6;
	}

	.home-search__date-col {
		flex: 1;
	}

	.home-search__date-col--end {
		text-align: right;
	}

	.home-search__date-label {
		font-size: 24rpx;
		color: #9CA3AF;
		margin-bottom: 8rpx;
	}

	.home-search__date-val {
		font-size: 34rpx;
		font-weight: 600;
		color: #334155;
	}

	.home-search__date-mid {
		display: flex;
		align-items: center;
		padding: 0 20rpx;
		flex-shrink: 0;
	}

	.home-search__date-line {
		width: 24rpx;
		height: 1rpx;
		background: #D1D5DB;
	}

	.home-search__date-nights {
		font-size: 22rpx;
		color: #2C8EFE;
		background: #E8F3FF;
		padding: 6rpx 16rpx;
		border-radius: 999rpx;
		margin: 0 8rpx;
		white-space: nowrap;
	}

	.home-search__keyword {
		margin: 24rpx 0 28rpx;
		padding: 20rpx 24rpx;
		background: #F4F9FF;
		border-radius: 20rpx;
	}

	.home-search__keyword :deep(.u-input__content__field-wrapper__field) {
		font-size: 28rpx;
		color: #334155;
	}

	.home-search__input-ph {
		font-size: 28rpx;
		color: #9CA3AF !important;
	}

	.home-search__btn {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 92rpx;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #7EBAFF 0%, #2C8EFE 100%);
		color: #FFFFFF;
		font-size: 32rpx;
		font-weight: 500;
	}

	.guest-section {
		margin-top: 24rpx;
	}
</style>
