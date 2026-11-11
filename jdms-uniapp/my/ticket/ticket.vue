<template>
	<view class="plat-page plat-coupon-page">
		<view class="plat-head plat-coupon-hero">
			<view class="plat-coupon-hero__text">优惠劵商城，您的省钱小帮手</view>
		</view>

		<view class="plat-coupon-section">
			<view class="plat-card plat-coupon-my">
				<view class="plat-coupon-my__head">
					<view class="plat-coupon-my__title">我的优惠券</view>
					<view class="flex align-center" @tap="routerGo('/my/ticket/myTicket')">
						<view class="plat-coupon-my__more">查看更多</view>
						<u-icon name="arrow-right" color="#64748B" size="11"></u-icon>
					</view>
				</view>
				<view class="plat-coupon-my__list" v-if="myCouponList.length > 0">
					<scroll-view class="plat-coupon-scroll plat-coupon-scroll--mine" scroll-x="true" scroll-left="0">
						<view class="plat-coupon-row plat-coupon-row--mine">
							<view class="plat-coupon-card" v-for="(item,index) in myCouponList" :key="index">
								<image src="../static/yhqbackgroud.png" class="plat-coupon-card__bg"></image>
								<view class="plat-coupon-card__body">
									<view class="plat-coupon-card__money">￥<text>{{item.money}}</text></view>
									<view class="plat-coupon-card__info">
										<view class="plat-coupon-card__name">{{item.couponName}}</view>
										<view class="plat-coupon-card__time">将于{{item.expirationTime.replace(/T/g," ")}}到期</view>
									</view>
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
				<view class="plat-coupon-empty" v-else>
					<image src="../static/noyhq.png" class="plat-coupon-empty__img"></image>
				</view>
			</view>
		</view>

		<view class="plat-sec">
			<view class="plat-card plat-coupon-my">
				<view class="plat-sec-head" style="padding: 0 0 20rpx;">
					<view class="plat-sec-head__bar"></view>
					<view class="plat-sec-head__title">优惠券</view>
				</view>
				<view v-if="couponList.length > 0">
					<scroll-view class="plat-coupon-scroll plat-coupon-scroll--shop" scroll-x="true" scroll-left="0">
						<view class="plat-coupon-row plat-coupon-row--shop">
							<view class="plat-coupon-shop__item" v-for="(item,index) in couponList" :key="index">
								<image src="../static/hdyhq1.png" class="plat-coupon-shop__bg"></image>
								<view class="plat-coupon-shop__content">
									<view class="plat-coupon-shop__name">{{item.couponName}}</view>
									<view class="plat-coupon-shop__money">￥<text>{{item.money}}</text></view>
									<view class="plat-coupon-shop__rule">满{{item.minMoney}}元使用</view>
									<view class="plat-btn plat-btn--sm plat-coupon-shop__btn" style="background-color: red;" @click="saveCoupon(item)">去领取</view>
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
				<view class="plat-coupon-empty" v-else>
					<image src="../static/noyhq1.png" class="plat-coupon-empty__img"></image>
				</view>
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



	const myCouponList = reactive([]);
	// 获取我的优惠券列表
	const getMyCouponList = () => {
		let data = {
			page: 1,
			limit: 10,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/couponUser/getMyCouponList', data).then(res => {
			if (res.code == 0) {
				myCouponList.splice(0, myCouponList.length, ...res.data.records)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
	};

	const couponList = reactive([]);
	// 获取未领取优惠券列表
	const getCouponList = () => {
		let data = {
			page: 1,
			limit: 999,
			// type: 0,
			// issueType: 0
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/coupon/getCouponPageList', data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				couponList.splice(0, couponList.length, ...res.data.records)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
	};
	// 领取活动优惠券
	const saveCoupon = (item) => {
		let data = {
			userId: uni.getStorageSync('userId'),
			couponId: item.couponId
		}
		proxy.$http.getT('/app/couponUser/receiveActivity', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '领取成功',
					icon: 'none',
					duration: 2000
				})
				setTimeout(() => {
					getCouponList()
					getMyCouponList()
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
	};












	onLoad(option => {
		getCouponList()
		getMyCouponList()
	});
	onShow(() => {
		getCouponList()
		getMyCouponList()
	});
	onPullDownRefresh(() => {
		getCouponList()
		getMyCouponList()
		uni.stopPullDownRefresh()
	});
	onReachBottom(() => {

	});
	const routerGo = (url) => {
		uni.navigateTo({
			url: url
		})
	}
</script>

<style lang="scss" scoped>
.plat-coupon-page {
	padding-bottom: calc(44rpx + env(safe-area-inset-bottom));
	background: linear-gradient(180deg, #f2f6ff 0%, #f6f8fc 180rpx, #f4f6fa 100%);
}

.plat-coupon-hero {
	padding: 40rpx 30rpx 104rpx;
}

.plat-coupon-hero__text {
	width: 100%;
	white-space: nowrap;
	font-size: 34rpx;
	line-height: 1.45;
	letter-spacing: 0;
	text-shadow: 0 6rpx 16rpx rgba(0, 73, 173, 0.16);
}

.plat-coupon-section {
	margin-top: -58rpx;
	padding: 0 24rpx;
}

.plat-sec {
	margin: 24rpx 24rpx 0;
}

.plat-coupon-my {
	padding: 30rpx 28rpx;
	border-radius: 24rpx;
	box-shadow: 0 14rpx 36rpx rgba(15, 23, 42, 0.06);
}

.plat-coupon-my__head,
.plat-sec-head {
	min-height: 48rpx;
	align-items: center;
}

.plat-coupon-my__title,
.plat-sec-head__title {
	letter-spacing: 0;
}

.plat-coupon-my__more {
	font-size: 24rpx;
	margin-right: 6rpx;
}

.plat-coupon-my__list {
	margin-top: 20rpx;
	padding-bottom: 4rpx;
}

.plat-coupon-scroll--mine {
	height: 156rpx;
}

.plat-coupon-scroll--shop {
	height: 300rpx;
	margin-top: 8rpx;
}

.plat-coupon-row {
	display: inline-flex;
	align-items: stretch;
	padding-right: 8rpx;
}

.plat-coupon-row--shop {
	padding-top: 6rpx;
	padding-bottom: 8rpx;
}

.plat-coupon-card {
	margin-right: 0;
	flex-shrink: 0;
}

.plat-coupon-row--mine .plat-coupon-card {
	margin-right: 20rpx;
}

.plat-coupon-row--mine .plat-coupon-card:last-child {
	margin-right: 0;
}

.plat-coupon-card__bg,
.plat-coupon-card__body {
	width: 560rpx;
	border-radius: 22rpx;
}

.plat-coupon-card__money {
	width: 156rpx;
	font-size: 24rpx;
	line-height: 1;
	padding-top: 6rpx;
}

.plat-coupon-card__money text {
	font-size: 50rpx;
}

.plat-coupon-card__info {
	margin-left: 20rpx;
	padding-right: 34rpx;
	display: flex;
	flex-direction: column;
	justify-content: center;
}

.plat-coupon-card__name {
	max-width: none;
	font-size: 30rpx;
	line-height: 1.3;
}

.plat-coupon-card__time {
	margin-top: 0;
	line-height: 1.4;
}

.plat-coupon-card__name + .plat-coupon-card__time {
	margin-top: 10rpx;
}

.plat-coupon-shop__item {
	margin-right: 24rpx;
	width: 220rpx;
	height: 260rpx;
	flex-shrink: 0;
	filter: drop-shadow(0 12rpx 22rpx rgba(255, 88, 67, 0.18));
}

.plat-coupon-row--shop .plat-coupon-shop__item:last-child {
	margin-right: 0;
}

.plat-coupon-shop__bg {
	width: 220rpx;
}

.plat-coupon-shop__content {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 22rpx 0 18rpx;
}

.plat-coupon-shop__name {
	width: 100%;
	padding: 0 18rpx;
	font-size: 28rpx;
	line-height: 1.25;
}

.plat-coupon-shop__money {
	margin: 14rpx 0 8rpx;
	line-height: 1;
}

.plat-coupon-shop__money text {
	font-size: 52rpx;
}

.plat-coupon-shop__rule {
	line-height: 1.3;
	padding: 0 12rpx;
}

.plat-coupon-shop__btn {
	width: 142rpx;
	height: 56rpx;
	padding: 0;
	border-radius: 16rpx;
	font-size: 24rpx;
	background: linear-gradient(135deg, #ff6b57 0%, #ff2f45 100%);
	box-shadow: 0 10rpx 20rpx rgba(255, 71, 87, 0.22);
}

.plat-coupon-empty {
	padding: 56rpx 0 44rpx;
}
</style>
