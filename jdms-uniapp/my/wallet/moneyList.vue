<template>
	<view class="plat-page">
		<view class="plat-list-feed" style="padding-top: 20rpx;">
			<view v-if="list.length > 0">
				<view v-for="(item, index) in list" :key="index" class="plat-card plat-record-card">
					<view class="plat-record-card__title">{{item.title || ''}}</view>
					<view class="plat-record-card__content">{{item.content || ''}}</view>
					<view class="plat-record-card__time">创建时间：{{item.createTime || ''}}</view>
					<view class="plat-record-card__foot">
						<text v-if="item.type == 1" class="plat-record-card__amount--in">+{{item.money || '0'}}元</text>
						<text v-if="item.type == 2" class="plat-record-card__amount--out">-{{item.money || '0'}}元</text>
					</view>
				</view>
			</view>
			<empty v-if="list.length == 0" contont="暂无明细"></empty>
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
	//分页
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
	const copyClick = (title) => {
		uni.setClipboardData({
			data: title,
			success: function(res) {
				uni.showToast({
					title: "复制成功",
					icon: 'none',
				});
			},
		});
	};
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			// userId: uni.getStorageSync('userId'),
			// classify: 1,//1注册 2购买 3提现
			// type: 1,//类型（1充值 2.提现）
			// state: 1//状态 1待支付 2已到账 3取消
		}
		proxy.$http.get('/app/moneyDetails/getMoneyDetailsList', data).then(res => {
			if (res.code === 0) {
				res.data.records.map(item => {
					if ((item.title).indexOf('订单') != -1) {
						item.show = true
					} else {
						item.show = false
					}
				})
				pages.value = res.data.totalPage
				if (page.value === 1) {
					list.splice(0, list.length, ...res.data.records);
					console.log(list.value)
				} else {
					list.push(...res.data.records)
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
			uni.stopPullDownRefresh();
			uni.hideLoading();
		});
	};
</script>

<style lang="scss">
</style>
