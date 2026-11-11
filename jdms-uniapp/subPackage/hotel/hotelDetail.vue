<template>
	<view class="plat-detail-page">
		<view class="plat-detail-banner">
			<up-swiper :list="swiperList" height="250"></up-swiper>
		</view>

		<view class="plat-detail-summary">
			<view class="plat-detail-summary__name">{{shopInfo.shopName||''}}</view>
			<view class="plat-detail-summary__tags">
				<text class="plat-tag">{{shopInfo.score||'3.5'}} 分</text>
				<text class="plat-tag plat-tag--soft" v-if="shopInfo.tags" v-for="tag in shopInfo.tags.split(',').slice(0,2)" :key="tag">{{tag||''}}</text>
			</view>
			<view class="plat-detail-summary__addr" @click="openLocation">
				<text class="plat-detail-summary__addr-text">{{shopInfo.county||''}}/{{shopInfo.address||''}}</text>
				<text class="plat-detail-summary__map">地图 ›</text>
			</view>
		</view>

		<view class="plat-detail-stay">
			<view class="plat-detail-stay__bar" @click="calendarShow = true">
				<view class="plat-detail-stay__dates">
					<text class="plat-detail-stay__val">{{todayInfo.display||''}}</text>
					<text class="plat-detail-stay__unit">入住</text>
					<view class="plat-detail-stay__dash"></view>
					<text class="plat-detail-stay__val">{{tomorrowInfo.display||''}}</text>
					<text class="plat-detail-stay__unit">离店</text>
				</view>
				<view class="plat-detail-stay__nights">共{{daysNum||'1'}}晚 ›</view>
			</view>
			<scroll-view scroll-x class="plat-detail-stay__scroll" :show-scrollbar="false">
				<view class="plat-detail-stay__chips">
					<template v-for="(item, index) in roomType">
						<view class="plat-detail-chip" :key="index" v-if="item&&item.roomTypeId"
							:class="{ 'plat-detail-chip--on': selectedRoomIds.includes(item.roomTypeId) }" @click="toggleRoomType(item)">
							{{item.roomType||''}}
						</view>
					</template>
				</view>
			</scroll-view>
		</view>

		<view class="plat-detail-section">
			<view class="plat-card plat-cover-card plat-cover-card--row plat-detail-room" v-for="(item, index) in roomList" :key="item.roomId || index">
				<image :src="item.roomTitleImg?item.roomTitleImg:'/static/logo.png'" mode="aspectFill" class="plat-cover-card__img"></image>
				<view class="plat-cover-card__body">
					<view class="plat-cover-title">{{getRoomDisplayTitle(item)}}</view>
					<view class="plat-cover-sub">{{getRoomSubline(item)}}</view>
					<view class="plat-cover-card__foot">
						<view>
							<view class="plat-cover-price">
								<text class="unit">￥</text>
								<text class="num">{{getRoomPrice(item)}}</text>
							</view>
							<view class="plat-detail-room__vip" v-if="item.memberMoney">
								<text class="plat-detail-room__vip-badge">VIP</text>
								<text class="plat-detail-room__vip-price">￥{{ Number(item.memberMoney || 0).toFixed(2) }}</text>
							</view>
						</view>
						<view class="plat-btn plat-btn--sm" @click="checkRoomBtn(item.roomId)">预订</view>
					</view>
				</view>
			</view>
			<view class="plat-detail-empty" v-if="roomList.length === 0">
				<image src="/shopCenter/static/noData.png" mode="aspectFit" class="plat-detail-empty__img"></image>
				<view class="plat-detail-empty__text">暂无房源</view>
			</view>
		</view>

		<view class="plat-detail-section">
			<view class="plat-detail-reviews">
				<view class="plat-detail-section__head" @click="routerGo('/subpackage/hotel/commentList?shopId='+shopId)">
					<text class="plat-detail-section__title">用户点评 ({{evaluateTotal||'0'}})</text>
					<text class="plat-detail-section__more">查看更多 ›</text>
				</view>
				<view class="flex" style="padding: 24rpx 0;border-bottom: 1rpx solid var(--plat-line);" v-for="item in evaluateList" :key="item.evaluateId || item.id || item.createTime">
					<image :src="item.avatar?item.avatar:'/static/logo.png'" mode="aspectFill" class="plat-review-card__avatar"></image>
					<view class="plat-review-card__body">
						<view class="plat-review-card__top">
							<text class="plat-review-card__name">{{item.userName||'匿名'}}</text>
							<view class="flex align-center">
								<up-rate :count="item.satisfactionFlag" readonly size="14" gutter="1" :modelValue="item.satisfactionFlag"
									activeColor="#FF6411"></up-rate>
								<up-rate :count="5 - Number(item.satisfactionFlag)" :modelValue="5 - Number(item.satisfactionFlag)"
									readonly disabled size="14" gutter="1"></up-rate>
							</view>
						</view>
						<view class="plat-review-card__text">{{item.evaluateMessage||''}}</view>
						<view class="plat-review-card__imgs" v-if="item.evaluateImg">
							<image :src="img" mode="aspectFill" class="plat-review-card__img" v-for="(img,index) in item.evaluateImg.split(',')"
								:key="index" @click="previewImages(item.evaluateImg.split(','), index)"></image>
						</view>
						<view class="plat-review-card__time">{{item.createTime.slice(0,10)}}</view>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-detail-spacer"></view>

		<view class="plat-detail-foot">
			<view class="plat-detail-foot__actions">
				<view class="plat-detail-foot__item" @click="collectShops">
					<image
						:src="shopInfo.isCollect == 0?'/static/image/index/shoucang.png':'/subpackage/static/rider/shoucang.png'"
						mode="aspectFit" class="plat-detail-foot__icon"></image>
					<text class="plat-detail-foot__label">收藏</text>
				</view>
				<button open-type="share" class="plat-detail-foot__item plat-detail-foot__share" @click="sharUrl()">
					<image src="/static/image/index/fenxiang.png" mode="aspectFit" class="plat-detail-foot__icon"></image>
					<text class="plat-detail-foot__label">分享</text>
				</button>
			</view>
			<view class="plat-detail-foot__btn" @click="routerGo('/subpackage/hotel/roomList?shopId='+shopId)">查看房型</view>
		</view>

		<up-calendar :show="calendarShow" :defaultDate="defaultDate" mode="range" @confirm="calendarConfirm" startText="入住"
			endText="离店" monthNum="6" round="12" title="选择日期" @close="calendarShow = false"></up-calendar>
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
		onShareAppMessage,
		onShareTimeline
	} from '@dcloudio/uni-app';
	import {
		getRoomDisplayTitle,
		getRoomSubline
	} from './roomDisplay.js';
	const {
		proxy
	} = getCurrentInstance();

	const url = ref('');
	const tuiguang1 = ref('');
	const shopId = ref('');
	const shopInfo = reactive({});
	const startDate = ref('');
	const endDate = ref('');
	const calendarShow = ref(false);
	const swiperList = reactive([]);
	const todayInfo = reactive({});
	const tomorrowInfo = reactive({});
	const defaultDate = reactive([])
	const daysNum = ref(1)
	const getStayDays = (selectedDates = []) => {
		return Math.max((selectedDates?.length || 1) - 1, 1)
	}
	const roomType = reactive([]);
	const selectedRoomIds = reactive([]); // 选中的房型ID数组
	const page = ref(1);
	const limit = ref(10);
	const status = ref('loadmore');
	const totalPages = ref(1);
	const roomList = reactive([]);
	const evaluateList = reactive([]);
	const evaluateTotal = ref(0);

	const checkRoomBtn = (roomId) => {
		uni.setStorageSync('tomorrowInfo', tomorrowInfo)
		uni.setStorageSync('todayInfo', todayInfo)
		routerGo('/subpackage/hotel/roomDetail?roomId=' + roomId)
	};

	// 计算属性：选中的房型ID逗号拼接
	const roomTypeIdsStr = computed(() => {
		return selectedRoomIds.join(',')
	})

	// 房型多选切换
	const toggleRoomType = (item) => {
		const id = item.roomTypeId
		const index = selectedRoomIds.indexOf(id)

		if (index > -1) {
			// 已选中，取消
			selectedRoomIds.splice(index, 1)
		} else {
			// 未选中，添加
			selectedRoomIds.push(id)
		}

		// 重新请求房间列表
		page.value = 1
		getShopRoomList()
	}

	const previewImages = (images, current) => {
		uni.previewImage({
			urls: images,
			current: images[current]
		})
	}

	const calendarConfirm = (e) => {
		calendarShow.value = false
		daysNum.value = getStayDays(e)
		todayInfo.api = e[0]
		todayInfo.display = e[0].slice(5).replace('-', '月') + '日'
		tomorrowInfo.api = e[e.length - 1]
		tomorrowInfo.display = e[e.length - 1].slice(5).replace('-', '月') + '日'
		page.value = 1
		getShopRoomList()
	};

	const getRoomPrice = (item) => {
		return (Number(item.money) || 0).toFixed(2)
	}

	// 获取评价列表
	const getUserEvaluate = () => {
		let data = {
			shopId: shopId.value,
			page: 1,
			limit: 3
		};
		proxy.$http.getT('/app/evaluate/getUserEvaluate', data).then(res => {
			if (res.code == 0) {
				evaluateList.splice(0, evaluateList.length, ...res.data.records);
				evaluateTotal.value = res.data.total
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};


	// 获取酒店详情
	const getHotelInfo = () => {
		let data = {
			shopId: shopId.value
		}
		proxy.$http.getT('/app/shops/getShopById', data).then(
			res => {
				if (res.code == 0) {
					Object.assign(shopInfo, res.data)
					swiperList.splice(0, swiperList.length, ...res
						.data.carouselUrl.split(','))
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			})
		proxy.$http.getT('/app/shopsRoom/getRoomTypeList',
			data).then(res => {
			if (res.code == 0) {
				roomType.splice(0, roomType.length, ...res.data)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	// 房间列表
	const getShopRoomList = () => {
		let data = {
			shopId: shopId.value,
			page: page.value,
			limit: limit.value,
			roomTypeIds: roomTypeIdsStr.value, // 逗号拼接的字符串
			startDate: todayInfo.api,
			endDate: tomorrowInfo.api,
			aggregate: 1
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/shopsRoom/getShopRoomList',
			data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				totalPages.value = res.data.pages
				if (page.value < totalPages.value) {
					status.value = 'loadmore'
				} else {
					status.value = 'nomore'
				}
				if (page.value == 1) {
					roomList.splice(0, roomList.length, ...res
						.data.records)
				} else {
					roomList.push(...res.data.records)
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}
	const collectShops = () => {
		let data = {
			shopId: shopId.value
		}
		let url = '';
		if (shopInfo.isCollect == 0) {
			url = '/app/shops/collectShops';
		} else {
			url = '/app/shops/cancelCollectShops';
		}
		proxy.$http.getT(url, data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: shopInfo.isCollect == 0 ? '收藏成功' : '取消成功',
					icon: 'none'
				})
				setTimeout(() => {
					proxy.$http.getT('/app/shops/getShopById', data).then(
						res => {
							if (res.code == 0) {
								Object.assign(shopInfo, res.data)
								swiperList.splice(0, swiperList.length, ...res
									.data.carouselUrl.split(','))
							} else {
								uni.showToast({
									title: res.msg,
									icon: 'none'
								})
							}
						})
				}, 500)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	// 地图导航
	const openLocation = () => {
		uni.openLocation({
			longitude: shopInfo.lng,
			latitude: shopInfo.lat,
			name: shopInfo.shopName,
			address: shopInfo.address
		})
	};
	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};
	// 返回两种格式
	const getDateFormats = (date = new Date()) => {
		const d = date instanceof Date ? date : new Date(date)
		const year = d.getFullYear()
		const month = d.getMonth() + 1
		const day = d.getDate()

		const pad = (n) => String(n).padStart(2, '0')

		return {
			api: `${year}-${pad(month)}-${pad(day)}`,
			display: `${pad(month)}月${pad(day)}日`
		}
	};
	//获取分享配置
	const getSharePage = () => {
		// 推广文案
		proxy.$http.get('/app/common/type/515').then(res => {
			if (res.code == 0 && res.data.value) {
				tuiguang1.value = res.data.value;
			}
		});
		// H5域名配置
		proxy.$http.get('/app/common/type/19').then(res => {
			if (res.code == 0 && res.data.value) {
				url.value = res.data.value;
			} else {
				url.value = proxy.$publicYuMing() + '/subpackage/hotel/hotelDetail?shopId=' + shopId.value +
					'&otherInviterCode=' + myInvitationCode.value;
			}
		});
	};
	//分享
	const sharUrl = () => {
		if (!uni.getStorageSync('token')) {
			uni.navigateTo({
				url: '/pages/public/login'
			})
			return
		}
		// #ifndef MP-WEIXIN
		let relationId = myInvitationCode.value;
		let modalContent = '';
		modalContent = tuiguang1.value + relationId + '\n' + url.value + '/subpackage/hotel/hotelDetail?shopId=' +
			shopId.value + '&otherInviterCode=' + relationId;
		uni.showModal({
			title: '分享',
			content: modalContent,
			showCancel: true,
			cancelText: '关闭',
			confirmText: '一键复制',
			confirmColor: '#4CB6FF',
			success: res => {
				if (res.confirm) {
					uni.setClipboardData({
						data: modalContent,
						success: function() {
							uni.showToast({
								title: '复制成功',
								icon: 'none'
							})
						}
					});
				}
			}
		});
		// #endif
	};
	onShareAppMessage(res => {
		return {
			path: shareMessage.value,
			title: goodInfo.name,
			imageUrl: list1[0]
		}
	});
	onShareTimeline(res => {
		return {
			path: shareMessage.value,
			title: goodInfo.name,
			imageUrl: list1[0]
		}
	});
	const myInvitationCode = ref('');
		onLoad(option => {
		getSharePage()
		const now = new Date()
		Object.assign(todayInfo, getDateFormats(now));

		const tomorrowDate = new Date(now.getTime() + 24 *
			60 * 60 * 1000)
		Object.assign(tomorrowInfo, getDateFormats(
			tomorrowDate));

		defaultDate.push(todayInfo.api, tomorrowInfo.api)
		if (uni.getStorageSync('userId')) {
			myInvitationCode.value = uni.getStorageSync('myInvitationCode');
		}
		if (option.otherInviterCode) {
			uni.setStorageSync('otherInviterCode', option.otherInviterCode)
		}
		if (option.shopId) {
			shopId.value = option.shopId
			getHotelInfo()
			getShopRoomList()
		}
		if (uni.getStorageSync('token')) {
			getUserEvaluate()
		}
		// #ifdef MP-WEIXIN
		if (option.scene) {
			const scene = decodeURIComponent(e.scene);
			let ss = scene.split(',');
			for (var i = 0; i < ss.length; i++) {
				if (i === 0) {
					shopId.value = ss[i];
				} else if (i === 1) {
					uni.setStorageSync('otherInviterCode', ss[i])
				}
			}
		}
		shareMessage.value = '/subpackage/hotel/hotelDetail?shopId=' + shopId.value + '&otherInviterCode=' +
			myInvitationCode.value;
		// #endif
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	:deep() wx-button:after,
	:deep() uni-button:after {
		border: none !important;
	}

	:deep() wx-button,
	:deep() uni-button,
	:deep() button {
		background-color: transparent !important;
	}

	:deep(.u-calendar .u-button) {
		background: #2C8EFE !important;
		border-color: #2C8EFE !important;
	}

	:deep(.u-calendar .u-button__text),
	:deep(.u-calendar .u-button__loading-text) {
		color: #FFFFFF !important;
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
