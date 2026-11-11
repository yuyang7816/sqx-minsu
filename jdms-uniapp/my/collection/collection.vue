<template>
	<view class="plat-page">
		<view class="plat-list-feed" style="margin-top: 30rpx;">
			<view class="plat-card plat-cover-card" v-for="item,index in list" :key="index"
				@click="routerGo('/subpackage/hotel/hotelDetail?shopId='+item.shopId)">
				<image :src="item.titleImg?item.titleImg:'/static/logo.png'" mode="aspectFill" class="plat-cover-card__img"></image>
				<view class="plat-cover-card__body">
					<view class="plat-cover-title">{{item.shopName||''}}</view>
					<view class="plat-cover-tags">
						<text class="plat-tag">{{item.score||'3.5'}} 评分</text>
						<text class="plat-tag plat-tag--soft" v-for="tag in item.tags.split(',').slice(0,2)" v-if="item.tags" :key="tag">{{tag||''}}</text>
					</view>
					<view class="plat-cover-sub">{{item.county||''}}/{{item.address||''}}</view>
					<view class="plat-cover-card__foot">
						<view class="plat-cover-price">
							<text class="unit">￥</text>
							<text class="num">{{item.minMoney||'0.00'}}</text>
							<text class="suffix">起</text>
						</view>
						<view class="plat-btn plat-btn--sm">立即预订</view>
					</view>
				</view>
			</view>
			<up-loadmore :status="status" v-if="list.length>0" />
			<empty v-if="list.length==0"></empty>
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

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

	// 总页数
	const totalPages = ref(1);
	// 页数
	const page = ref(1);
	const limit = ref(10);
	//加载状态
	const status = ref('loadmore');
	const list = reactive([]);
	const getCollectionList = () => {
		let data = {
			page: page.value,
			limit: limit.value
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/shops/selectCollectShopsList', data).then(res => {
			uni.hideLoading()
			uni.stopPullDownRefresh()
			if (res.code == 0) {

				if (res.code == 0) {
					totalPages.value = res.data.pages;
					if (page.value < totalPages.value) {
						status.value = 'loadmore';
					} else {
						status.value = 'nomore';
					}
					if (page.value === 1) {
						list.splice(0, list.length, ...res.data.records);
					} else {
						list.push(...res.data.records);
					}
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	onLoad(option => {

	});
	onShow(() => {
		getCollectionList();
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getCollectionList();
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1
			status.value = 'loading';
			getCollectionList();
		}
	});
</script>

<style lang="scss" scoped>
</style>
