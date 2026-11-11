<template>
	<view class="home-container">
		<!-- 顶部商家信息区 -->
		<view class="home-header" v-if="isShopRider == 3">
			<view class="home-header-bg">
				<image src="/static/image/my/shopTx.png" mode="aspectFit" class="home-header-deco"></image>
				<view class="home-header-inner">
					<view class="home-shop" @click="routerGo('/my/shopRz/shopCheck?shopId='+shopInfo.shopId)">
						<image src="/static/image/my/shopTx.png" mode="" class="home-shop__avatar"></image>
						<view class="home-shop__text">
							<view class="home-shop__title-row">
								<view class="home-shop__name">{{shopInfo.shopName||'商家工作台'}}</view>
								<view class="home-shop__status">
									<text class="home-shop__status-text">{{isOpen ? '营业中' : '已打烊'}}</text>
									<up-switch v-model="isOpen" @change="change" asyncChange size="20" space="3" inactiveColor="#CCCCCC" activeColor="#49CC90"></up-switch>
								</view>
							</view>
							<view class="home-shop__desc">今日经营、订单履约、房态管理一屏处理</view>
						</view>
					</view>
				</view>
			</view>

			<!-- 收益浮卡 -->
			<view class="home-earnings">
				<view class="home-earnings__item">
					<view class="home-earnings__value"><text class="home-earnings__unit">¥</text>{{shopProfitInfo.todayMoney||'0'}}</view>
					<view class="home-earnings__label">今日收益</view>
				</view>
				<view class="home-earnings__item">
					<view class="home-earnings__value"><text class="home-earnings__unit">¥</text>{{shopProfitInfo.monthMoney||'0'}}</view>
					<view class="home-earnings__label">本月收益</view>
				</view>
				<view class="home-earnings__item">
					<view class="home-earnings__value"><text class="home-earnings__unit">¥</text>{{shopProfitInfo.allMoney||'0'}}</view>
					<view class="home-earnings__label">总收益</view>
				</view>
			</view>
		</view>

		<!-- 员工头部 -->
		<view class="home-staff-head" v-if="isShopRider == 1">
			<view class="home-staff-head__main">
				<image :src="userInfo.avatar?userInfo.avatar:'/static/logo.png'" mode="" class="home-staff-head__avatar"></image>
				<view>
					<view class="home-staff-head__name">{{userInfo.userName||'游客'}}</view>
					<view class="home-staff-head__desc">欢迎进入商家工作台</view>
				</view>
			</view>
		</view>

		<!-- 主体内容区 -->
		<view class="home-body">
			<!-- 快捷入口 -->
			<view class="home-quick" v-if="isShopRider == 3">
				<view class="home-quick__card" @click="routerGo('/shopCenter/wallet/index')">
					<view class="home-quick__main">
						<view class="home-quick__label">收益概览</view>
						<view class="home-quick__value">{{shopProfitInfo.cashMoney||'0.00'}}</view>
					</view>
					<view class="home-quick__btn">提现</view>
				</view>
				<view class="home-quick__card" @click="routerGo('/shopCenter/employee/indexList')">
					<view class="home-quick__info">
						<view class="home-quick__label">员工管理</view>
						<view class="home-quick__sub">{{shopProfitInfo.allUser||'0'}}</view>
					</view>
					<view class="home-quick__arrow">
						<up-icon name="arrow-right" color="#FFFFFF" size="14" bold></up-icon>
					</view>
				</view>
			</view>

			<!-- 入住订单 -->
			<view class="home-section">
				<view class="home-section-head" @click="routerGo('/shopCenter/order/index')">
					<view class="home-section-title">入住订单</view>
					<view class="home-section-link">
						<text>查看全部</text>
						<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
					</view>
				</view>
				<view class="home-card home-stats">
					<view class="home-stats__item">
						<view class="home-stats__num">{{servedNum||'0'}}</view>
						<view class="home-stats__label">待核销</view>
					</view>
					<view class="home-stats__item">
						<view class="home-stats__num">{{completeNum||'0'}}</view>
						<view class="home-stats__label">已完成</view>
					</view>
					<view class="home-stats__item">
						<view class="home-stats__num">{{refundedNum||'0'}}</view>
						<view class="home-stats__label">已取消</view>
					</view>
				</view>
			</view>

			<!-- 房间管理 -->
			<view class="home-section">
				<view class="home-section-head" @click="routerGo('/shopCenter/myPosts/index')">
					<view class="home-section-title">房间管理</view>
					<view class="home-section-link">
						<text>查看全部</text>
						<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
					</view>
				</view>
				<view class="home-card home-stats">
					<view class="home-stats__item">
						<view class="home-stats__num">{{shangjiaNum||'0'}}</view>
						<view class="home-stats__label">上架中</view>
					</view>
					<view class="home-stats__item">
						<view class="home-stats__num">{{xiajiaNum||'0'}}</view>
						<view class="home-stats__label">已下架</view>
					</view>
					<view class="home-stats__item home-stats__item--action" @click="routerGo('/shopCenter/rooms/addRoom')">
						<view class="home-stats__icon home-stats__icon--add">
							<text>+</text>
						</view>
						<view class="home-stats__label">添加房间</view>
					</view>
				</view>
			</view>

			<!-- 今日订单 -->
			<view class="home-section">
				<view class="home-section-head" @click="routerGo('/shopCenter/order/index')">
					<view class="home-section-title">今日订单</view>
					<view class="home-section-link">
						<text>查看全部</text>
						<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
					</view>
				</view>
				<view class="home-card home-today">
					<view class="home-today__empty" v-if="orderList.length==0">
						<up-empty mode="order" text="今日暂无订单"></up-empty>
					</view>
					<view class="home-today__item" v-for="item,index in orderList" :key="index"
						@click="routerGo('/shopCenter/order/orderDetail?orderId='+item.orderId)">
						<view class="home-today__date">
							<view class="home-today__day">今天</view>
							<view class="home-today__range" v-if="item.startDate&&item.endDate">
								{{item.startDate.slice(5,10)}} ~ {{item.endDate.slice(5,10)}}
							</view>
							<view class="home-today__range" v-else>暂无</view>
						</view>
						<view class="home-today__line"></view>
						<view class="home-today__body">
							<view class="home-today__title">{{item.roomName||'暂无'}}</view>
							<view class="home-today__meta">预定人：<text class="home-today__meta-val">{{item.realName||'暂无'}}</text></view>
							<view class="home-today__phone" @click.stop="callUser(item.phone)">
								<view class="home-today__meta">预定手机号：<text class="home-today__meta-val">{{maskedPhone(item.phone) || '暂无'}}</text></view>
								<image src="/shopCenter/static/dianhua (1).png" mode="" class="home-today__phone-icon"></image>
							</view>
							<view class="home-today__meta">预定数量：<text class="home-today__meta-val">{{item.roomCount||''}}</text></view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="home-spacer"></view>
		<view class="home-footer">
			<view class="home-submit" @click="routerGo('/shopCenter/order/sacnCode')">订单核销</view>
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
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();


	const shangjiaNum = ref(0);
	const xiajiaNum = ref(0);
	const servedNum = ref(0);
	const completeNum = ref(0);
	const refundedNum = ref(0);
	const getDataInfo = () => {
		let data = {
			shopId: uni.getStorageSync('shopId')
		}
		proxy.$http.get('/app/shops/statisticsShopOrder', data).then(res => {
			if (res.code == 0) {
				servedNum.value = res.data.pendingVerifyCount
				completeNum.value = res.data.completedCount
				refundedNum.value = res.data.refundedCount
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
		proxy.$http.get('/app/shops/statisticsShopRoom', data).then(res => {
			if (res.code == 0) {
				shangjiaNum.value = res.data.enabledCount
				xiajiaNum.value = res.data.disabledCount
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
	};



	// 手机号隐藏
	const maskedPhone = (phone) => {
		const val = phone;
		if (val.length < 7) return val;
		return val.slice(0, 3) + '****' + val.slice(-4);
	};

	const callUser = (phone) => {
		uni.makePhoneCall({
			phoneNumber: phone
		});
	};

	const dateTime = ref(null);
	// 总页数
	const totalPages = ref(1);
	// 页数
	const page = ref(1);
	const limit = ref(10);
	//加载状态
	const status = ref('loadmore');
	const orderList = reactive([]);
	// 获取今日订单列表
	const getOrderList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			orderStatus: 1,
			shopId: uni.getStorageSync('shopId')
		}
		proxy.$http.get('/app/shops/selectTodayOrderList', data).then(res => {
			uni.stopPullDownRefresh()
			if (res.code == 0) {
				totalPages.value = res.data.pages;
				if (page.value < totalPages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (page.value === 1) {
					orderList.splice(0, orderList.length, ...res.data.records);
				} else {
					orderList.push(...res.data.records);
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
	};
	const isOpen = ref(true); // 营业中 true / 打烊 false
	const change = (e) => {
		uni.showModal({
			title: '提示',
			content: e ? '确定切换为打烊状态吗？' : '确定切换为营业状态吗？',
			success: function(res) {
				if (res.confirm) {
					isOpen.value = e
					uni.showLoading({
						title: "切换中..."
					})
					proxy.$http.postT('/app/shops/save', {
						isEnable: e ? 1 : 0, // 0未营业 1营业中
						shopId: shopInfo.shopId
					}).then(rep => {
						uni.hideLoading()
						if (rep.code == 0) {
							uni.showToast({
								title: '切换成功'
							})
							setTimeout(() => {
								getStoreInfo()
							}, 500)
						} else {
							// 请求失败时回滚开关状态
							isOpen.value = !e
							uni.showToast({
								title: rep.msg,
								icon: 'none'
							})
						}
					})
				}
			}
		})
	};


	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};
	const shopProfitInfo = reactive({});
	// 获取收益统计
	const getShopProfit = () => {
		proxy.$http.get('/app/shops/statisticsShop', {
			shopId: uni.getStorageSync('shopId')
		}).then(res => {
			if (res.code == 0) {
				Object.assign(shopProfitInfo, res.data)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const shopInfo = reactive({});
	const amount = ref(0.00);
	const storeStatus = ref(false); // 0未营业 1营业中 2待审核 3:审核拒绝   组件的true和false是为了配合前面的文字，所以是反着的
	// 获取店铺信息
	const getStoreInfo = () => {
		let data = {
			shopId: uni.getStorageSync('shopId')
		}
		proxy.$http.get('/app/shops/getShopById', data).then(res => {
			if (res.code == 0) {
				Object.assign(shopInfo, res.data)
				isOpen.value = res.data.isEnable == 1
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
	};
	const userInfo = reactive({});
	const isShopRider = ref(null);
	const getUserInfo = () => {
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				Object.assign(userInfo, res.data)
				// 是否是酒店员工：1是 2否 3商户
				isShopRider.value = res.data.isShopRider
				console.log(userInfo, "userInfo")
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	}

	onLoad(option => {
		getUserInfo()
	});
	onShow(() => {
		const now = new Date();
		const year = now.getFullYear();
		const month = String(now.getMonth() + 1).padStart(2, '0'); // 月份记得+1，并转为字符串补零
		const day = String(now.getDate()).padStart(2, '0');

		const formattedDate1 = `${year}-${month}-${day}`;
		dateTime.value = formattedDate1

		if (uni.getStorageSync('shopId')) {
			getShopProfit()
			getStoreInfo()
			getDataInfo()
			getOrderList()
		}
	});

	onPullDownRefresh(() => {
		page.value = 1;
		getOrderList()
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1
			status.value = 'loading';
			getOrderList()
		}
	});
</script>

<style lang="scss" scoped>
	page {
		background-color: #F5F7FA;
	}

	.home-container {
		min-height: 100vh;
		padding-bottom: calc(160rpx + env(safe-area-inset-bottom));
		box-sizing: border-box;
	}

	.home-header {
		position: relative;
		padding-bottom: 72rpx;
	}

	.home-header-bg {
		background: linear-gradient(180deg, #2B6FE8 0%, #4B9DFF 100%);
		border-radius: 0 0 32rpx 32rpx;
		padding: 28rpx 24rpx 92rpx;
		position: relative;
		overflow: hidden;
	}

	.home-header-deco {
		position: absolute;
		width: 320rpx;
		height: 320rpx;
		opacity: 0.12;
		right: -40rpx;
		top: 20rpx;
		z-index: 0;
	}

	.home-header-inner {
		position: relative;
		z-index: 1;
	}

	.home-shop {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}

	.home-shop__avatar {
		width: 76rpx;
		height: 76rpx;
		border-radius: 20rpx;
		background: rgba(255, 255, 255, 0.2);
		flex-shrink: 0;
	}

	.home-shop__text {
		flex: 1;
		min-width: 0;
	}

	.home-shop__title-row {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 12rpx;
	}

	.home-shop__name {
		font-size: 34rpx;
		font-weight: 800;
		color: #FFFFFF;
		line-height: 1.25;
		flex: 1;
		min-width: 0;
	}

	.home-shop__desc {
		margin-top: 8rpx;
		font-size: 22rpx;
		color: rgba(255, 255, 255, 0.75);
		line-height: 1.5;
	}

	.home-shop__status {
		display: flex;
		align-items: center;
		gap: 10rpx;
		flex-shrink: 0;
		margin-left: 8rpx;
	}

	.home-shop__status-text {
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.95);
		font-weight: 600;
	}

	.home-earnings {
		position: absolute;
		left: 24rpx;
		right: 24rpx;
		bottom: 8rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 24rpx 16rpx;
		box-shadow: 0 8rpx 22rpx rgba(43, 111, 232, 0.08);
		display: flex;
		align-items: center;
		justify-content: space-around;
		z-index: 2;
	}

	.home-earnings__item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
		flex: 1;
	}

	.home-earnings__value {
		font-size: 34rpx;
		font-weight: 800;
		color: #1E2A3A;
		line-height: 1;
	}

	.home-earnings__unit {
		font-size: 24rpx;
		margin-right: 2rpx;
	}

	.home-earnings__label {
		font-size: 22rpx;
		color: #8B94A5;
	}

	.home-staff-head {
		margin: 24rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 28rpx 30rpx;
		box-shadow: 0 6rpx 18rpx rgba(15, 23, 42, 0.04);
	}

	.home-staff-head__main {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}

	.home-staff-head__avatar {
		width: 88rpx;
		height: 88rpx;
		border-radius: 50%;
		flex-shrink: 0;
	}

	.home-staff-head__name {
		font-size: 32rpx;
		font-weight: 700;
		color: #1E2A3A;
	}

	.home-staff-head__desc {
		margin-top: 8rpx;
		font-size: 24rpx;
		color: #8B94A5;
	}

	.home-body {
		padding: 16rpx 24rpx 0;
	}

	.home-quick {
		display: flex;
		gap: 16rpx;
		margin-bottom: 20rpx;
	}

	.home-quick__card {
		flex: 1;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 4rpx 22rpx;
		box-shadow: 0 6rpx 18rpx rgba(15, 23, 42, 0.04);
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
		min-height: 132rpx;
	}

	.home-quick__label {
		font-size: 26rpx;
		color: #8B94A5;
		line-height: 1;
	}

	.home-quick__value {
		margin-top: 20rpx;
		font-size: 38rpx;
		font-weight: 800;
		color: #1E2A3A;
		line-height: 1;
	}

	.home-quick__btn {
		flex-shrink: 0;
		padding: 0 22rpx;
		height: 54rpx;
		background: linear-gradient(135deg, #2B6FE8 0%, #4B9DFF 100%);
		color: #FFFFFF;
		font-size: 22rpx;
		font-weight: 600;
		border-radius: 999rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.home-quick__main,
	.home-quick__info {
		display: flex;
		flex-direction: column;
		justify-content: center;
	}

	.home-quick__sub {
		margin-top: 20rpx;
		font-size: 38rpx;
		color: #1E2A3A;
		font-weight: 800;
		line-height: 1;
	}

	.home-quick__arrow {
		flex-shrink: 0;
		width: 50rpx;
		height: 50rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #2B6FE8 0%, #4B9DFF 100%);
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.home-section {
		margin-bottom: 20rpx;
	}

	.home-section-head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 14rpx;
		padding: 0 6rpx;
	}

	.home-section-title {
		font-size: 28rpx;
		font-weight: 700;
		color: #1E2A3A;
	}

	.home-section-link {
		display: flex;
		align-items: center;
		gap: 4rpx;
		font-size: 24rpx;
		color: #8B94A5;
	}

	.home-card {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 24rpx 20rpx;
		box-shadow: 0 6rpx 18rpx rgba(15, 23, 42, 0.04);
	}

	.home-stats {
		display: flex;
		align-items: center;
		justify-content: space-around;
	}

	.home-stats__item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 10rpx;
		flex: 1;
	}

	.home-stats__num {
		font-size: 38rpx;
		font-weight: 800;
		color: #1E2A3A;
		line-height: 1;
	}

	.home-stats__label {
		font-size: 22rpx;
		color: #8B94A5;
	}

	.home-stats__item--action {
		justify-content: flex-end;
	}

	.home-stats__icon {
		width: 64rpx;
		height: 64rpx;
		border-radius: 50%;
		background: #2B6FE8;
		color: #FFFFFF;
		font-size: 36rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		justify-content: center;
		line-height: 1;
	}

	.home-stats__icon--add {
		padding-bottom: 4rpx;
		box-sizing: border-box;
	}

	.home-today {
		padding: 18rpx 16rpx;
		position: relative;
	}

	.home-today__empty {
		display: flex;
		align-items: center;
		justify-content: center;
		min-height: 220rpx;
		padding: 16rpx 0;
	}

	.home-today__empty :deep(.u-empty) {
		transform: scale(0.88);
		transform-origin: center center;
	}

	.home-today__item {
		display: flex;
		align-items: stretch;
		padding: 24rpx 0;
		border-bottom: 2rpx solid #eef2f7;

		&:last-child {
			border-bottom: none;
			padding-bottom: 0;
		}

		&:first-child {
			padding-top: 0;
		}
	}

	.home-today__date {
		width: 146rpx;
		flex-shrink: 0;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		gap: 8rpx;
	}

	.home-today__day {
		font-size: 28rpx;
		font-weight: 700;
		color: #1E2A3A;
	}

	.home-today__range {
		font-size: 20rpx;
		color: #8B94A5;
		line-height: 1.4;
		text-align: center;
		white-space: nowrap;
	}

	.home-today__line {
		width: 2rpx;
		background: #F0F2F5;
		margin: 0 16rpx;
		flex-shrink: 0;
	}

	.home-today__body {
		flex: 1;
		min-width: 0;
	}

	.home-today__title {
		font-size: 30rpx;
		font-weight: 700;
		color: #1E2A3A;
		margin-bottom: 14rpx;
	}

	.home-today__meta {
		font-size: 24rpx;
		color: #8B94A5;
		line-height: 1.8;
	}

	.home-today__meta-val {
		color: #1E2A3A;
	}

	.home-today__phone {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 12rpx;
	}

	.home-today__phone-icon {
		width: 36rpx;
		height: 36rpx;
		flex-shrink: 0;
	}

	.home-spacer {
		height: 28rpx;
	}

	.home-footer {
		position: fixed;
		left: 0;
		bottom: 0;
		width: 100%;
		padding: 16rpx 24rpx calc(16rpx + env(safe-area-inset-bottom));
		background: #FFFFFF;
		box-shadow: 0 -4rpx 18rpx rgba(15, 23, 42, 0.05);
		box-sizing: border-box;
		z-index: 10;
	}

	.home-submit {
		width: 100%;
		height: 84rpx;
		border-radius: 42rpx;
		background: linear-gradient(135deg, #2B6FE8 0%, #4B9DFF 100%);
		color: #FFFFFF;
		font-size: 30rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 18rpx rgba(43, 111, 232, 0.18);
	}
</style>
