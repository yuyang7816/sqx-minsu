<template>
	<view class="plat-page">
		<view class="plat-list-feed" style="padding-top: 20rpx;">
			<view v-for="(item, index) in list" :key="index" class="plat-card plat-record-card">
				<view class="plat-record-card__head">
					<view class="flex align-center">
						<text class="plat-record-card__status" v-if="item.classify==2">微信</text>
						<text class="plat-record-card__status" v-if="item.state==1">提现成功</text>
						<text class="plat-record-card__status" v-if="item.state==0">提现中</text>
						<text class="plat-record-card__status plat-record-card__status--fail" v-if="item.state==-1">提现失败</text>
					</view>
					<text class="plat-record-card__money">￥{{item.money}}</text>
				</view>
				<view class="plat-record-card__line text-cut" v-if="item.classify==1">支付宝账号：{{item.zhifubao}}</view>
				<view class="plat-record-card__line" v-if="item.classify==1">支付宝姓名：{{item.zhifubaoName}}</view>
				<view class="plat-record-card__line">提现时间：{{item.createAt}}</view>
				<view class="plat-record-card__line" v-if="item.state==1">成功时间 {{item.outAt}}</view>
				<view class="plat-record-card__line">提现金额{{item.money||'0'}}，手续费扣除{{item.rate||'0'}}元</view>
				<view class="plat-record-card__line plat-record-card__status--fail" v-if="item.state==-1">{{item.refund}}</view>
			</view>
			<empty v-if="list.length === 0" contont="暂无提现记录"></empty>
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
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	import empty from '@/components/empty/empty.vue';
	const {
		proxy
	} = getCurrentInstance();
	const list = reactive([]);
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	onLoad(option => {
		getList();
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getList();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1;
			getList();
		}
	});
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId'),
			userType: 1
		}
		proxy.$http.getT('/app/cash/selectPayDetails', data).then(res => {
			uni.stopPullDownRefresh()
			if (res.code == 0) {
				pages.value = res.data.totalPage
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.list)
				} else {
					list.push(...res.data.list)
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
</script>

<style lang='scss'>
</style>
