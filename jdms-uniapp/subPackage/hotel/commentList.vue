<template>
	<view class="plat-page plat-review-page">
		<view class="plat-card plat-review-card" v-for="item in evaluateList" :key="item.evaluateId || item.id">
			<view class="flex align-start">
				<image :src="item.avatar?item.avatar:'/static/logo.png'" mode="aspectFill" class="plat-review-card__avatar">
				</image>
				<view class="plat-review-card__body">
					<view class="plat-review-card__top">
						<view class="plat-review-card__name">{{item.userName||'匿名'}}</view>
						<view class="flex align-center">
							<up-rate :count="item.satisfactionFlag" readonly size="14" gutter="1" :modelValue="item.satisfactionFlag"
								activeColor="#FF6411"></up-rate>
							<up-rate :count="5 - Number(item.satisfactionFlag)" :modelValue="5 - Number(item.satisfactionFlag)"
								readonly disabled size="14" gutter="1"></up-rate>
						</view>
					</view>
					<view class="plat-review-card__text">{{item.evaluateMessage||''}}</view>
					<view class="plat-review-card__imgs" v-if="item.evaluateImg">
						<image :src="img" mode="aspectFill" class="plat-review-card__img"
							v-for="(img,index) in item.evaluateImg.split(',')" :key="index"
							@click="previewImages(item.evaluateImg.split(','), index)">
						</image>
					</view>
					<view class="plat-review-card__time">{{item.createTime.slice(0,10)}}</view>
				</view>
			</view>
		</view>
		<up-loadmore :status="status" v-if="evaluateList.length>=10" />
		<up-empty mode="order" style="margin-top: 70%;" text="暂无评价" v-if="evaluateList.length == 0"></up-empty>
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

	const page = ref(1);
	const limit = ref(10);
	const totalPages = ref(1);
	const status = ref('loadmore');
	const evaluateList = reactive([]);
	const getUserEvaluate = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			shopId: shopId.value
		}
		proxy.$http.getT('/app/evaluate/getUserEvaluate', data).then(res => {
			if (res.code == 0) {
				totalPages.value = res.data.pages
				if (page.value < totalPages.value) {
					status.value = 'loadmore'
				} else {
					status.value = 'nomore'
				}
				if (page.value == 1) {
					evaluateList.splice(0, evaluateList.length, ...res.data.records)
				} else {
					evaluateList.push(...res.data.records)
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	const previewImages = (images, current) => {
		uni.previewImage({
			urls: images,
			current: images[current]
		})
	}


	const shopId = ref(null);
	onLoad(option => {
		if (option.shopId) {
			shopId.value = option.shopId
			getUserEvaluate()
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
</style>