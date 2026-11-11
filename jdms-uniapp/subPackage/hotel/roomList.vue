<template>
	<view class="plat-page plat-room-list-page">
		<view class="plat-filter-bar">
			<view class="plat-room-filter">
				<view class="plat-room-filter__dates" @click="calendarShow = true">
					<view class="plat-room-filter__date-block">
						<text class="plat-room-filter__val">{{todayInfo.display||''}}</text>
						<text class="plat-room-filter__unit">入住</text>
					</view>
					<view class="plat-room-filter__nights">共{{daysNum||'1'}}晚</view>
					<view class="plat-room-filter__date-block">
						<text class="plat-room-filter__val">{{tomorrowInfo.display||''}}</text>
						<text class="plat-room-filter__unit">离店</text>
					</view>
				</view>
				<view class="plat-room-filter__more" @click="filterPopupShow = true">
					<text>更多筛选</text>
					<up-icon name="arrow-down-fill" color="#64748B" size="11"></up-icon>
				</view>
			</view>
		</view>

		<view class="plat-list-feed">
			<view class="plat-card plat-cover-card" v-for="(item, index) in roomList" :key="item.roomId || index">
				<image :src="item.roomTitleImg?item.roomTitleImg:'/static/logo.png'" mode="aspectFill"
					class="plat-cover-card__img"></image>
				<view class="plat-cover-card__body">
					<view class="plat-cover-title">{{ getRoomDisplayTitle(item) }}</view>
					<view class="plat-cover-sub">{{ getRoomSubline(item) }}</view>
					<view class="plat-cover-card__foot">
						<view class="flex align-center">
							<view class="plat-cover-price">
								<text class="unit">￥</text>
								<text class="num">{{getRoomPrice(item)}}</text>
							</view>
							<view class="plat-detail-room__vip" v-if="item.memberMoney" style="margin-top:8rpx;">
								<text class="plat-detail-room__vip-badge">VIP</text>
								<text class="plat-detail-room__vip-price">￥{{ item.memberMoney || 0 }}</text>
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

		<!-- 更多筛选弹窗 -->
		<up-popup :show="filterPopupShow" mode="bottom" round="16" @close="filterPopupShow = false">
			<view class="plat-room-filter-popup">
				<!-- 房型筛选 -->
				<view class="plat-room-filter-section" v-if="roomTypeList.length > 0">
					<view class="plat-room-filter-section__title">房型</view>
					<view class="plat-room-filter-tags">
						<view class="plat-room-filter-tag"
							:class="{'plat-room-filter-tag--on': selectedRoomIds.includes(item.value)}"
							v-for="(item, index) in roomTypeList" :key="index" @click="toggleRoomType(item)">
							{{item.label}}
						</view>
					</view>
				</view>

				<!-- 价格区间 -->
				<view class="plat-room-filter-section">
					<view class="plat-room-filter-section__title">价格</view>
					<view class="plat-room-filter-price-range">
						<text>¥ {{priceSliderValue[0] || 0}}</text>
						<text>¥ {{priceSliderValue[1] || 500}}以上</text>
					</view>
					<up-slider :rangeValue="priceSliderValue" isRange @change="sliderChange" activeColor="#2C8EFE"
						inactiveColor="#EEF1F6" blockColor="#FFFFFF" blockSize="16" min="0" max="500" step="10">
					</up-slider>
					<view class="plat-room-filter-tags">
						<view class="plat-room-filter-tag" :class="{'plat-room-filter-tag--on': currentPriceTag === index}"
							v-for="(item, index) in priceTags" :key="index" @click="selectPriceTag(index)">
							{{item.label}}
						</view>
					</view>
				</view>

				<!-- 底部按钮 -->
				<view class="plat-room-filter-popup__foot">
					<view class="plat-btn plat-btn--ghost plat-room-filter-popup__reset" @click="resetFilter">重置</view>
					<view class="plat-btn plat-room-filter-popup__confirm" @click="confirmFilter">确定</view>
				</view>
			</view>
		</up-popup>

		<!-- 日历 -->
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
		onReachBottom
	} from '@dcloudio/uni-app';
	import {
		getRoomDisplayTitle,
		getRoomSubline
	} from './roomDisplay.js';
	const {
		proxy
	} = getCurrentInstance();

	const shopId = ref('');
	const calendarShow = ref(false);
	const todayInfo = reactive({});
	const tomorrowInfo = reactive({});
	const defaultDate = reactive([])
	const daysNum = ref(1)
	const getStayDays = (selectedDates = []) => {
		return Math.max((selectedDates?.length || 1) - 1, 1)
	}
	const selectedRoomIds = reactive([]); // 选中的房型ID数组
	const page = ref(1);
	const limit = ref(10);
	const roomList = reactive([]);
	const isVip = ref(0)

	const getRoomPrice = (item) => {
		if (isVip.value == 1) {
			return (Number(item.memberMoney) || Number(item.money) || 0).toFixed(2)
		}
		return (Number(item.money) || 0).toFixed(2)
	}

	// 筛选相关
	const filterPopupShow = ref(false)
	const roomTypeList = reactive([]) // 房型列表
	const radioConfigs = reactive([]) // 设施配置列表 {classifyId, label, key, options}
	const selectedClassifyIds = reactive([]) // 选中的设施classifyId数组
	const priceSliderValue = reactive([0, 500])
	const currentPriceTag = ref(-1)

	const priceTags = reactive([{
			label: '¥ 0-100',
			min: 0,
			max: 100
		},
		{
			label: '¥ 100-150',
			min: 100,
			max: 150
		},
		{
			label: '¥ 150-200',
			min: 150,
			max: 200
		},
		{
			label: '¥ 200-250',
			min: 200,
			max: 250
		},
		{
			label: '¥ 250-300',
			min: 250,
			max: 300
		},
		{
			label: '¥ 300-400',
			min: 300,
			max: 400
		},
		{
			label: '¥ 400-500',
			min: 400,
			max: 500
		},
		{
			label: '¥ 500+',
			min: 500,
			max: 10000
		},
	])

	// 获取分类列表（设施筛选）
	const getClassifyList = () => {
		proxy.$http.get('/app/roomDeviceClassify/getClassifyList', {
			page: 1,
			limit: 999
		}).then(res => {
			if (res.code == 0) {
				radioConfigs.splice(0, radioConfigs.length, ...res.data.records.map(item => ({
					key: 'classify_' + item.classifyId,
					label: item.title,
					classifyId: item.classifyId,
					options: item.options || ['有', '无']
				})));
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	// 获取房间类型列表
	const getRoomTypeList = () => {
		proxy.$http.get('/app/roomType/getRoomTypeList', {
			page: 1,
			limit: 999
		}).then(res => {
			if (res.code == 0) {
				roomTypeList.splice(0, roomTypeList.length, ...res.data.records.map(item => ({
					value: item.id,
					label: item.typeName
				})))
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	const checkRoomBtn = (roomId) => {
		uni.setStorageSync('tomorrowInfo', tomorrowInfo)
		uni.setStorageSync('todayInfo', todayInfo)
		routerGo('/subpackage/hotel/roomDetail?roomId=' + roomId)
	};

	// 计算属性：选中的房型ID逗号拼接
	const roomTypeIdsStr = computed(() => {
		return selectedRoomIds.join(',')
	})

	// 计算属性：选中的设施classifyId逗号拼接
	const classifyIdsStr = computed(() => {
		return selectedClassifyIds.join(',')
	})

	// 房型多选切换
	const toggleRoomType = (item) => {
		const id = item.value
		const index = selectedRoomIds.indexOf(id)
		if (index > -1) {
			selectedRoomIds.splice(index, 1)
		} else {
			selectedRoomIds.push(id)
		}
	}

	// 设施多选切换
	const toggleClassify = (item) => {
		const id = item.classifyId
		const index = selectedClassifyIds.indexOf(id)
		if (index > -1) {
			selectedClassifyIds.splice(index, 1)
		} else {
			selectedClassifyIds.push(id)
		}
	}

	// 价格滑动条变化
	const sliderChange = (e) => {
		priceSliderValue.splice(0, 2, ...e)
		currentPriceTag.value = -1
	}

	// 选择价格标签
	const selectPriceTag = (index) => {
		currentPriceTag.value = index
		const tag = priceTags[index]
		priceSliderValue.splice(0, 2, tag.min, tag.max)
	}

	// 重置筛选
	const resetFilter = () => {
		selectedRoomIds.splice(0, selectedRoomIds.length)
		selectedClassifyIds.splice(0, selectedClassifyIds.length)
		priceSliderValue.splice(0, 2, 0, 500)
		currentPriceTag.value = -1
	}

	// 确定筛选
	const confirmFilter = () => {
		filterPopupShow.value = false
		page.value = 1
		getShopRoomList()
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

	// 房间列表
	const getShopRoomList = () => {
		let data = {
			shopId: shopId.value,
			page: page.value,
			limit: limit.value,
			roomTypeIds: roomTypeIdsStr.value,
			startDate: todayInfo.api,
			endDate: tomorrowInfo.api,
			minPrice: priceSliderValue[0] > 0 ? priceSliderValue[0] : '',
			maxPrice: priceSliderValue[1] >= 500 ? '' : priceSliderValue[1],
			aggregate: 1
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/shopsRoom/getShopRoomList', data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				if (page.value == 1) {
					roomList.splice(0, roomList.length, ...res.data.records)
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

	/* 获取用户会员信息 */
	const getUserInfo = () => {
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				isVip.value = res.data.isVip
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	}

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

	onLoad(option => {
		const now = new Date()
		Object.assign(todayInfo, getDateFormats(now));
		const tomorrowDate = new Date(now.getTime() + 24 * 60 * 60 * 1000)
		Object.assign(tomorrowInfo, getDateFormats(tomorrowDate));
		defaultDate.push(todayInfo.api, tomorrowInfo.api)

		if (option.shopId) {
			shopId.value = option.shopId
			getRoomTypeList()
			getClassifyList()
			getShopRoomList()
		}
		if (uni.getStorageSync('token')) {
			getUserInfo()
		}
	});
	onShow(() => {});
	onPullDownRefresh(() => {});
	onReachBottom(() => {});
</script>

<style lang="scss" scoped>
	:deep(.u-slider-inner) {
		padding-top: 0 !important;
		padding-bottom: 0 !important;
	}

	.plat-room-list-page {
		background: linear-gradient(180deg, #f4f8ff 0, #f7f8fb 220rpx, #f4f6fa 100%);
	}

	.plat-filter-bar {
		position: sticky;
		top: 0;
		z-index: 20;
		padding-top: 20rpx;
		padding-bottom: 18rpx;
		background: linear-gradient(180deg, #f4f8ff 0, rgba(244, 248, 255, 0.96) 100%);
	}

	.plat-room-filter {
		padding: 18rpx 22rpx;
		border-radius: 24rpx;
		box-shadow: 0 10rpx 30rpx rgba(15, 23, 42, 0.06);
	}

	.plat-room-filter__dates {
		align-items: center;
		justify-content: center;
		gap: 22rpx;
	}

	.plat-room-filter__date-block {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		gap: 4rpx;
		min-width: 0;
		text-align: center;
	}

	.plat-room-filter__val {
		font-size: 28rpx;
		line-height: 1.2;
		font-weight: 700;
	}

	.plat-room-filter__unit {
		margin: 0;
		font-size: 22rpx;
		line-height: 1.2;
	}

	.plat-room-filter__nights {
		margin: 0;
		padding: 8rpx 18rpx;
		font-size: 20rpx;
		white-space: nowrap;
		background: #edf4ff;
		color: #2c8efe;
	}

	.plat-room-filter__more {
		padding-left: 18rpx;
		border-left: 1rpx solid #edf1f6;
		font-size: 24rpx;
		color: #475569;
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
		margin-left: 15rpx;
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

	.plat-room-residue {
		color: #2C8EFE;
		font-size: 22rpx;
	}
</style>
