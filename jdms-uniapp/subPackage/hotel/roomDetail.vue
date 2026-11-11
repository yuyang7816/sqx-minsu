<template>
	<view class="plat-detail-page plat-room-detail-page">
		<view class="plat-detail-banner">
			<swiper :autoplay="true" interval="2000" duration="500" :circular="true" style="width: 100%;height: 500rpx;">
				<swiper-item v-for="(item,index) in swiperList" :key='index' @click="goNav(item)">
					<image :src="item" mode="aspectFill" style="width: 100%;height: 100%;"></image>
				</swiper-item>
			</swiper>
		</view>

		<view class="plat-detail-summary">
			<view class="plat-detail-summary__name">{{roomInfo.roomName||''}}</view>
			<view class="flex align-center">
				<view class="plat-cover-price">
					<text class="unit">￥</text>
					<text class="num">{{roomInfo.money||'0.00'}}</text>
				</view>
				<view class="plat-detail-room__vip" v-if="roomInfo.memberMoney">
					<text class="plat-detail-room__vip-badge">VIP</text>
					<text class="plat-detail-room__vip-price">￥{{ Number(roomInfo.memberMoney || 0).toFixed(2) }}</text>
				</view>
			</view>
			<view class="plat-room-classify">
				<view class="plat-room-classify__item" v-for="(item, index) in roomInfo.classifyList" :key="index"
					:style="{gridColumn: isLongTitle(item.title) ? 'span 2' : 'span 1'}">
					<image :src="item.img" mode="" class="plat-room-classify__icon"></image>
					<view class="plat-room-classify__text">
						<text v-for="(char, idx) in (item.title || '')" :key="idx"
							:style="{color: isFreeChar(item.title, idx) ? '#2C8EFE' : ''}">
							{{char}}
						</text>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-detail-section">
			<view class="plat-form-card" style="margin:0;">
				<view class="plat-form-card__title">预定须知</view>
				<view class="plat-room-detail__rule-title">入离时间</view>
				<view class="plat-room-detail__rule-text">
					最早入住时间：{{roomInfo.checkInTime||''}}，最晚退房时间：{{roomInfo.checkOutTime||''}}之前</view>
				<view class="plat-room-detail__rule-title">餐食政策</view>
				<view class="plat-room-detail__rule-text">{{roomInfo.foodPolicy||''}}</view>
				<view class="plat-room-detail__rule-title">宠物政策</view>
				<view class="plat-room-detail__rule-text">{{roomInfo.petRule||''}}</view>
				<view class="plat-room-detail__rule-title">酒店提示</view>
				<view class="plat-room-detail__rule-text">{{roomInfo.hotelTips||''}}</view>
			</view>
		</view>

		<view class="plat-detail-spacer"></view>

		<view class="plat-detail-foot">
			<view class="plat-detail-foot__btn plat-detail-foot__btn--full"
				@click="routerGo('/subpackage/hotel/comfirOrder?roomId='+roomId)">立即预订</view>
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

	const swiperList = reactive([]);
	const roomId = ref(null);
	const roomInfo = reactive({});


	const isLongTitle = (title) => {
		let len = 0
		for (let char of String(title || '')) {
			len += (char.charCodeAt(0) > 127) ? 2 : 1
		}
		return len > 12
	}
	const isFreeChar = (title, idx) => {
		if (!title) return false
		const freeIndex = title.indexOf('免费')
		if (freeIndex === -1) return false
		return idx >= freeIndex && idx < freeIndex + 2
	}

	const getShopRoomInfo = () => {
		let data = {
			roomId: roomId.value
		}
		proxy.$http.getT('/app/shopsRoom/getShopRoomInfo', data).then(res => {
			if (res.code == 0) {
				Object.assign(roomInfo, res.data)
				swiperList.splice(0, swiperList.length, ...res.data.roomImg.split(','))
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

	onLoad(option => {
		if (option.roomId) {
			roomId.value = option.roomId
			getShopRoomInfo()
		}
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	.plat-detail-room__vip {
		margin-top: 10rpx;
		display: inline-flex;
		align-items: baseline;
		gap: 8rpx;
		padding: 6rpx 10rpx;
		border-radius: 12rpx;
		background: linear-gradient(135deg, #eff6ff, #dbeafe);
		border: 1rpx solid rgba(44, 142, 254, 0.18);
		margin-left: 20rpx;
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