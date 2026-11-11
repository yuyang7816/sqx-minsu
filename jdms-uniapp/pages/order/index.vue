<template>
	<view class="guest-page">
		<view class="guest-sticky">
			<up-tabs :list="list1" @click="click" lineWidth="30" lineColor="#2C8EFE" :scrollable="false" :current="tabIndex"
				:activeStyle="{
            color: '#2C8EFE',
            fontWeight: 'bold',
						fontSize:'28rpx'
        }" :inactiveStyle="{
            color: '#6B7280',
						fontSize:'28rpx'
        }"></up-tabs>
		</view>

		<view style="padding-top: 100rpx; padding-bottom: 30rpx;">
			<view class="guest-card guest-order-card" v-for="item,index in orderList" :key="index"
				@click="routerGo('/subpackage/order/orderDetail?orderId='+item.orderId)">
				<view class="guest-order-card__head">
					<view class="flex align-center">
						<image src="/static/image/order/jiudianyuding.png" mode="" style="width: 36rpx;height: 37rpx;"></image>
						<view class="f28 text-heavy margin-left-xs">{{item.orderType == 1?'酒店':'民宿'}}</view>
					</view>
					<view class="guest-order-card__status">{{orderStatusText(item.orderStatus)||''}}</view>
				</view>
				<view class="flex align-center justify-between margin-top-sm">
					<view style="width: 80%;">
						<view class="f34 text-heavy text-cut">{{item.shopName||''}}</view>
						<view class="f24 c99 text-cut" style="margin-top: 8rpx;width: 90%;">{{item.classifyName||''}}</view>
					</view>
					<image :src="item.coverImage" mode="aspectFill" style="width: 110rpx;height: 110rpx;border-radius: 16rpx;"
						v-if="item.coverImage">
					</image>
				</view>
				<view class="guest-stay-bar">
					<view class="text-center">
						<view class="f32 text-heavy">{{formatDate(item.startDate)||''}}</view>
						<view class="f24 c99">入住</view>
					</view>
					<view class="text-center guest-stay-bar__mid">
						<view class="guest-stay-bar__days">共{{getStayDays(item.startDate, item.endDate)}}晚</view>
					</view>
					<view class="text-center">
						<view class="f32 text-heavy">{{formatDate(item.endDate)||''}}</view>
						<view class="f24 c99">离店</view>
					</view>
				</view>
				<view class="guest-actions">
					<view class="guest-btn guest-btn--sm guest-btn--muted" v-if="item.orderStatus == 1||item.orderStatus == 0"
						@click.stop="userCancelOrder(item)">取消</view>
					<view class="guest-btn guest-btn--sm" v-if="item.orderStatus == 1" @click.stop="goVerify(item)">去核销</view>
					<view class="guest-btn guest-btn--sm" v-if="item.orderStatus == 0" @click.stop="goPay(item)">支付</view>
					<view class="guest-btn guest-btn--sm" v-if="item.orderStatus == 3&&item.isEvaluate == 0"
						@click.stop="routerGo('/subpackage/order/evaluate?orderId='+item.orderNumber+'&shopId='+item.shopId)">评价
					</view>
				</view>
			</view>
			<up-loadmore :status="status" v-if="orderList.length>=10" />
			<up-empty mode="order" style="margin-top: 70%;" v-if="orderList.length == 0"></up-empty>
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


	const doCancelOrder = (orderId) => {
		proxy.$http.postT('/app/orders/userCancelOrder', {
			orderId
		}).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '取消成功',
					icon: 'none'
				})
				setTimeout(() => {
					getOrderList()
				}, 500)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	const confirmCancel = (tip, orderId) => {
		uni.showModal({
			title: '提示',
			content: tip,
			confirmText: '确定取消',
			cancelText: '再想想',
			success: (modalRes) => {
				if (modalRes.confirm) {
					doCancelOrder(orderId)
				}
			}
		})
	}

	const userCancelOrder = (item) => {
		// 待支付未产生服务费，直接提示后取消
		if (item.orderStatus == 0) {
			confirmCancel('取消不会扣除服务费', item.orderId)
			return
		}
		uni.showLoading({
			title: '请稍后...'
		})
		proxy.$http.getT('/app/orders/cancelOrderPreview', {
			orderId: item.orderId
		}).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				confirmCancel(res.data?.tip || '确认取消订单？', item.orderId)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		}).catch(() => {
			uni.hideLoading()
			uni.showToast({
				title: '网络异常，请稍后重试',
				icon: 'none'
			})
		})
	};

	const formatDate = (dateStr) => {
		const date = new Date(dateStr.replace(/-/g, '/'));
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${month}月${day}日`;
	};

	const getStayDays = (startDate, endDate) => {
		const start = new Date(String(startDate || '').replace(/-/g, '/'))
		const end = new Date(String(endDate || '').replace(/-/g, '/'))
		const diff = end.getTime() - start.getTime()
		if (Number.isNaN(diff) || diff <= 0) {
			return 1
		}
		return Math.max(Math.round(diff / (24 * 60 * 60 * 1000)), 1)
	}



	const getOrderList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId'),
			orderStatus: orderStatus.value
		}
		proxy.$http.getT('/app/orders/getMyOrderList', data).then(res => {
			if (res.code == 0) {
				totalPages.value = res.data.pages
				if (page.value < totalPages.value) {
					status.value = 'loadmore'
				} else {
					status.value = 'nomore'
				}
				if (page.value == 1) {
					orderList.splice(0, orderList.length, ...res.data.records)
				} else {
					orderList.push(...res.data.records)
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		}).finally(() => {
			uni.stopPullDownRefresh()
		})
	};

	const syncOrderTab = (index = 0) => {
		const currentIndex = Number(index) || 0
		tabIndex.value = currentIndex
		const currentTab = list1[currentIndex] || list1[0]
		orderStatus.value = currentTab.id
	}

	const click = (item) => {
		syncOrderTab(item.index)
		page.value = 1
		getOrderList()
		console.log('item', item);
	};

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

	const goVerify = (item) => {
		routerGo('/subpackage/order/orderDetail?orderId=' + item.orderId)
	}

	const goPay = (item) => {
		routerGo('/subpackage/order/orderDetail?orderId=' + item.orderId + '&showPay=1')
	}

	const list1 = reactive([{
			name: '全部',
			id: ''
		},
		{
			name: '待付款',
			id: 0
		},
		{
			name: '待核销',
			id: 1
		}, {
			name: '进行中',
			id: 2
		},
		{
			name: '待点评',
			id: 3
		},
		{
			name: '退款/售后',
			id: 4
		}
	]);
	const orderStatusText = (val) => {
		switch (val) {
			case 0:
				return '待支付';
			case 1:
				return '待核销';
			case 2:
				return '进行中';
			case 3:
				return '已完成';
			case 4:
				return '已取消';
		}
	};

	onLoad(option => {

	});
	onShow(() => {
		syncOrderTab(uni.getStorageSync('index') || 0)
		if (uni.getStorageSync('token')) {
			getOrderList();
		}
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getOrderList();
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1
			status.value = 'loading';
			getOrderList();
		}
	});
</script>

<style lang="scss">
	page {
		background-color: var(--g-bg, #F0F4F8);
	}

	.guest-stay-bar__mid {
		min-width: 120rpx;
	}

	.guest-stay-bar__days {
		display: inline-flex;
		align-items: center;
		justify-content: center;
		padding: 8rpx 18rpx;
		border-radius: 999rpx;
		background: #edf4ff;
		font-size: 22rpx;
		color: #2c8efe;
		white-space: nowrap;
	}
</style>
