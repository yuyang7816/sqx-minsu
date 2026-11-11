<template>
	<view class="plat-page plat-coupon-page">
		<view class="plat-filter-bar">
			<view class="plat-seg">
				<view v-for="item,index in list" :key="index" class="plat-seg-item"
					:class="tabIndex == index ? 'plat-seg-item--active' : ''" @click="changeTab(index)">
					{{item.name}}
				</view>
			</view>
		</view>

		<view class="plat-list-feed">
			<view class="plat-card plat-coupon-item" v-for="item,index in 3" :key="index">
				<view class="plat-coupon-item__main">
					<view>
						<view class="plat-coupon-item__name">满198元可使用</view>
						<view class="plat-coupon-item__meta">有效期至2022-03-18</view>
					</view>
					<view class="plat-coupon-item__price"><text style="font-size:30rpx;">￥</text><text>42.00</text></view>
				</view>
				<view class="plat-coupon-item__divider"></view>
				<view class="flex align-center justify-between">
					<view class="plat-coupon-item__rule">现金券一张</view>
					<view class="plat-btn plat-btn--sm plat-btn--ghost">立即使用</view>
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

	const list = reactive([{
		name: '可使用'
	}, {
		name: '已使用'
	}, {
		name: '已失效'
	}]);
	const tabIndex = ref(0);

	const changeTab = (index) => {
		tabIndex.value = index
	};

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

	onLoad(option => {

	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
</style>
