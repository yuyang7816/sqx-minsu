<template>
	<view class="invoice-page invoice-select-page">
		<view class="invoice-select-hero">
			<view>
				<view class="invoice-select-hero__title">选择开票订单</view>
				<view class="invoice-select-hero__desc">勾选需要开票的订单后统一提交开票信息</view>
			</view>
			<view class="invoice-select-hero__filter" @click="qiehuan">
				<u-icon name="list" color="#2B6FE8" size="24"></u-icon>
				<text>筛选</text>
			</view>
		</view>

		<!-- 筛选下拉 -->
		<u-popup :show="show" mode="top" @close="show = false" round="18">
			<view class="invoice-filter-popup">
				<view class="invoice-filter-popup__title">筛选时间范围</view>
				<view class="invoice-filter-popup__time">
					<view class="invoice-filter-popup__time-item" @click="showStartTime=true">
						{{startTime?startTime:'开始时间'}}
					</view>
					<view class="invoice-filter-popup__time-sep">至</view>
					<view class="invoice-filter-popup__time-item" @click="showEndTime=true">
						{{endtime?endtime:'结束时间'}}
					</view>
				</view>
				<view class="invoice-filter-popup__btns">
					<view class="invoice-filter-popup__btn invoice-filter-popup__btn--cancel" @click="qiehuan">取消</view>
					<view class="invoice-filter-popup__btn invoice-filter-popup__btn--ok" @click="submit">确定</view>
				</view>
			</view>
		</u-popup>

		<!-- 可开发票列表 -->
		<view class="invoice-select-body">
			<view class="invoice-select-summary">
				<view class="invoice-select-summary__item">
					<view class="invoice-select-summary__label">已选订单</view>
					<view class="invoice-select-summary__value">{{count}}</view>
				</view>
				<view class="invoice-select-summary__item invoice-select-summary__item--soft">
					<view class="invoice-select-summary__label">合计金额</view>
					<view class="invoice-select-summary__value">{{price.toFixed(2)}}</view>
				</view>
			</view>

			<view class="invoice-select-list">
				<view class="invoice-order-card" v-for="(item,index) in orderList" :key="item.id">
					<view class="invoice-order-card__check">
						<checkbox-group @change="checkboxChange($event, item)">
							<checkbox :checked="item.checked" :value="String(item.id)" shape="circle" activeBackgroundColor="#2C8EFE"
								iconColor="#FFFFFF" activeBorderColor="#FFFFFF" style="transform:scale(0.8)" />
						</checkbox-group>
					</view>
					<view class="invoice-order-card__main">
						<view class="invoice-order-card__title">{{item.shopName||''}}</view>
						<view class="invoice-order-card__meta-grid">
							<view class="invoice-order-card__meta">房型：{{item.roomName}}</view>
							<view class="invoice-order-card__meta">数量：{{item.roomCount}}</view>
							<view class="invoice-order-card__meta invoice-order-card__meta--full">时间：{{item.startDate.slice(0,10)}} -
								{{item.endDate.slice(0,10)}}</view>
						</view>
						<view class="invoice-order-card__foot">
							<view class="invoice-order-card__price">
								<text class="num">{{item.orderMoney}}</text>
								<text class="suffix">元</text>
							</view>
							<!-- <view class="invoice-order-card__link">查看订单</view> -->
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 加载数据 -->
		<u-loadmore :status="status" v-if="orderList.length>0" />
		<!-- 暂无数据 -->
		<u-empty style="position: fixed;top: 56%;left: 50%;transform: translate(-50%,-50%);" v-if="orderList.length==0"
			mode="order">
		</u-empty>

		<!-- 底部操作栏 -->
		<view class="invoice-footer">
			<view class="invoice-footer__summary">
				已选<text>{{count}}</text>个记录，共<text>{{price.toFixed(2)}}</text>元
			</view>
			<view class="invoice-footer__actions">
				<view class="flex align-center">
					<up-checkbox-group v-model="selectAllValues" @change="checkboxChanges">
						<up-checkbox name="all" shape="circle" activeColor="#2C8EFE" label=""></up-checkbox>
					</up-checkbox-group>
					<view class="invoice-footer__all">本页全选</view>
				</view>
				<view class="invoice-footer__btn invoice-footer__btn--disabled" v-if="ids.length==0">开发票</view>
				<view @click="gotokp()" class="invoice-footer__btn" v-if="ids.length>0">开发票</view>
			</view>
		</view>

		<!-- 开始时间 -->
		<u-datetime-picker :show="showStartTime" v-model="startTimevalue" mode="date" @confirm="confirmStartTime"
			@cancel="showStartTime = false"></u-datetime-picker>

		<!-- 结束时间 -->
		<u-datetime-picker :show="showEndTime" v-model="endTimevalue" mode="date" @confirm="confirmEndTime"
			@cancel="showEndTime = false"></u-datetime-picker>
	</view>
</template>

<script setup>
	import {
		ref,
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

	const orderList = ref([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	//加载状态
	const status = ref('loadmore');
	const tabs = ref([{
		id: 1,
		name: '个人'
	}]);
	const current = ref(0);
	const show = ref(false);
	const startTime = ref('');
	const startTimevalue = ref(Date.now());
	const showStartTime = ref(false);
	const showEndTime = ref(false);
	const endtime = ref('');
	const endTimevalue = ref(Date.now());
	const count = ref(0);
	const price = ref(0);
	const selectAll = ref(false);
	const selectAllValues = ref([]);
	const ids = ref([]);

	const resetSelection = () => {
		selectAll.value = false
		selectAllValues.value = []
		ids.value = []
		count.value = 0
		price.value = 0
	}

	const syncSelectionState = () => {
		const selectedIds = []
		let totalPrice = 0

		orderList.value.forEach(item => {
			if (item.checked) {
				selectedIds.push(item.id)
				totalPrice += Number(item.orderMoney) || 0
			}
		})

		ids.value = selectedIds
		count.value = selectedIds.length
		price.value = totalPrice
		selectAll.value = orderList.value.length > 0 && orderList.value.every(item => item.checked)
		selectAllValues.value = selectAll.value ? ['all'] : []
	}

	const refreshOrderList = () => {
		page.value = 1
		orderList.value = []
		resetSelection()
		getOrderList()
	}

	onLoad(option => {
		resetSelection()
	})

	onShow(() => {
		refreshOrderList()
	})

	// 多个开票
	const gotokp = () => {
		let id = ids.value.join(',')
		uni.navigateTo({
			url: '/subpackage/invoice/kaipiao?indentNumber=' + id + '&price=' + price.value
		})
	}
	const qiehuan = () => {
		show.value = !show.value
	}

	//选择开始时间
	const confirmStartTime = (e) => {
		let time = timeFormat(e, 'yyyy-mm-dd');
		startTime.value = time
		showStartTime.value = false
	}

	// 选择结束时间
	const confirmEndTime = (e) => {
		let time = timeFormat(e, 'yyyy-mm-dd');
		console.log(time)
		endtime.value = time
		showEndTime.value = false
	}

	// 点击筛选数据
	const submit = () => {
		show.value = false
		refreshOrderList()
	}

	// 顶部tab 切换筛选
	const bindTab = (index, id) => {
		current.value = index
		startTime.value = ''
		endtime.value = ''
		refreshOrderList()
	}

	//计算单数与价格
	const getPrice = () => {
		orderList.value.forEach(item => {
			item.checked = false
		})
		syncSelectionState()
	};

	// 选中某个复选框时触发
	const checkboxChange = (e, item) => {
		item.checked = !item.checked
		syncSelectionState()
	};

	// 重新计算选中的数量和总金额
	const calcTotal = () => {
		syncSelectionState()
	};

	// 全选
	const checkboxChanges = (values) => {
		const checked = values.includes('all')
		orderList.value.forEach(item => {
			item.checked = checked
		})
		syncSelectionState()
	};

	//订单数据
	const getOrderList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			startTime: startTime.value ? startTime.value + ' 00:00:00' : '',
			endTime: endtime.value ? endtime.value + ' 00:00:00' : ''
		}
		proxy.$http.postJson("/app/invoice/orderList", data).then(res => {
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value < pages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}

				// 给每条数据添加 checked 属性，并保持之前选中的状态
				const records = res.data.records.map(item => {
					return {
						...item,
						checked: ids.value.includes(item.id)
					}
				})

				if (page.value === 1) {
					orderList.value = records
				} else {
					orderList.value.push(...records)
				}

				syncSelectionState()
			} else {
				status.value = 'nomore';
			}
			uni.stopPullDownRefresh();
		}).catch(() => {
			status.value = 'nomore';
			uni.stopPullDownRefresh();
		});
	}

	onPullDownRefresh(() => {
		refreshOrderList()
	})

	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading';
			getOrderList();
		}
	})
</script>

<style lang="scss" scoped>
	.invoice-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #eef5ff 0%, #f6f8fc 34%, #f5f7fb 100%);
	}

	.invoice-select-page {
		padding: 24rpx 24rpx calc(180rpx + env(safe-area-inset-bottom));
	}

	.invoice-select-hero {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
		padding: 34rpx 30rpx;
		border-radius: 26rpx;
		background: #ffffff;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.invoice-select-hero__title {
		font-size: 38rpx;
		font-weight: 700;
		line-height: 1.2;
		color: #1e2a3a;
	}

	.invoice-select-hero__desc {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #8b94a5;
	}

	.invoice-select-hero__filter {
		flex-shrink: 0;
		display: flex;
		align-items: center;
		gap: 8rpx;
		height: 64rpx;
		padding: 0 20rpx;
		border-radius: 999rpx;
		background: #f0f7ff;
		font-size: 24rpx;
		font-weight: 600;
		color: #2b6fe8;
	}

	.invoice-filter-popup {
		padding: 34rpx 28rpx 30rpx;
		background: #ffffff;
	}

	.invoice-filter-popup__title {
		font-size: 30rpx;
		font-weight: 700;
		color: #1e2a3a;
	}

	.invoice-filter-popup__time {
		display: flex;
		align-items: center;
		gap: 14rpx;
		margin-top: 24rpx;
	}

	.invoice-filter-popup__time-item {
		flex: 1;
		height: 84rpx;
		border-radius: 18rpx;
		background: #f6f8fc;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 26rpx;
		color: #1e2a3a;
	}

	.invoice-filter-popup__time-sep {
		font-size: 24rpx;
		color: #8b94a5;
	}

	.invoice-filter-popup__btns {
		display: flex;
		gap: 18rpx;
		margin-top: 26rpx;
	}

	.invoice-filter-popup__btn {
		flex: 1;
		height: 84rpx;
		border-radius: 20rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		font-weight: 700;
	}

	.invoice-filter-popup__btn--cancel {
		background: #f4f6fa;
		color: #5f6f88;
	}

	.invoice-filter-popup__btn--ok {
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		color: #ffffff;
	}

	.invoice-select-body {
		margin-top: 20rpx;
	}

	.invoice-select-summary {
		display: grid;
		grid-template-columns: repeat(2, minmax(0, 1fr));
		gap: 16rpx;
	}

	.invoice-select-summary__item {
		background: #ffffff;
		border-radius: 22rpx;
		padding: 24rpx 22rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.invoice-select-summary__item--soft {
		background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
	}

	.invoice-select-summary__label {
		font-size: 24rpx;
		color: #8b94a5;
	}

	.invoice-select-summary__value {
		margin-top: 12rpx;
		font-size: 36rpx;
		font-weight: 700;
		color: #1e2a3a;
	}

	.invoice-select-list {
		margin-top: 20rpx;
		display: flex;
		flex-direction: column;
		gap: 18rpx;
	}

	.invoice-order-card {
		background: #ffffff;
		border-radius: 24rpx;
		padding: 26rpx 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
		display: flex;
		gap: 18rpx;
	}

	.invoice-order-card__check {
		padding-top: 4rpx;
	}

	.invoice-order-card__main {
		flex: 1;
		min-width: 0;
	}

	.invoice-order-card__title {
		font-size: 30rpx;
		font-weight: 700;
		line-height: 1.4;
		color: #1e2a3a;
	}

	.invoice-order-card__meta-grid {
		display: grid;
		grid-template-columns: repeat(2, minmax(0, 1fr));
		gap: 12rpx 18rpx;
		margin-top: 16rpx;
	}

	.invoice-order-card__meta {
		font-size: 24rpx;
		line-height: 1.5;
		color: #7b889e;
	}

	.invoice-order-card__meta--full {
		grid-column: 1 / -1;
	}

	.invoice-order-card__foot {
		margin-top: 20rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.invoice-order-card__price {
		font-size: 24rpx;
		color: #ff5b7a;
	}

	.invoice-order-card__price .num {
		font-size: 42rpx;
		font-weight: 700;
		line-height: 1;
	}

	.invoice-order-card__link {
		font-size: 24rpx;
		font-weight: 600;
		color: #2b6fe8;
	}

	.invoice-footer {
		position: fixed;
		left: 0;
		bottom: 0;
		width: 100%;
		padding: 18rpx 24rpx calc(24rpx + env(safe-area-inset-bottom));
		background: #ffffff;
		box-shadow: 0 -4rpx 20rpx rgba(23, 43, 77, 0.06);
		box-sizing: border-box;
		z-index: 12;
	}

	.invoice-footer__summary {
		font-size: 24rpx;
		line-height: 1.5;
		color: #7b889e;
	}

	.invoice-footer__summary text {
		font-size: 28rpx;
		font-weight: 700;
		color: #2b6fe8;
		margin: 0 4rpx;
	}

	.invoice-footer__actions {
		margin-top: 16rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 20rpx;
	}

	.invoice-footer__all {
		font-size: 24rpx;
		color: #5f6f88;
	}

	.invoice-footer__btn {
		min-width: 180rpx;
		height: 78rpx;
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

	.invoice-footer__btn--disabled {
		opacity: 0.45;
	}
</style>