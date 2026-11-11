<template>
	<view class="plat-page plat-form-page">
		<view class="plat-head">
			<view class="plat-wallet-hero">
				<view class="plat-wallet-hero__label">可用余额</view>
				<view class="plat-wallet-hero__num">{{canCashMoney||'0.00'}}<text style="font-size:32rpx;margin-left:8rpx;">元</text></view>
			</view>
		</view>

		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-form-card" style="margin-top:0;">
				<view class="plat-form-card__title">充值金额</view>
				<view class="plat-money-grid">
					<view class="plat-money-item" v-for="item,index in configList" :key="index" @click="changeSelMoney(item,index)"
						:class="moneyIndex == index ? 'plat-money-item--active' : ''">
						<view class="text-center">{{item.money||'0'}}<text style="font-size:30rpx;">元</text></view>
					</view>
				</view>
			</view>

			<view class="plat-form-card">
				<view class="plat-form-card__title">支付方式</view>
				<view class="plat-pay-item" v-for="item,index in payList" :key="index"
					:class="isSelType == item.id ? 'plat-pay-item--active' : ''" @click="changeType(item.id)">
					<view class="flex align-center">
						<image :src="item.image" mode="" style="width: 50rpx;height: 50rpx;border-radius: 50%;"></image>
						<view class="margin-left-xs f28">{{item.name}}</view>
					</view>
					<view>
						<image v-if="isSelType == item.id" src="/my/static/xuanze.png" mode=""
							style="width: 40rpx;height: 40rpx;border-radius: 50%;">
						</image>
						<image v-else src="/my/static/xuanze (1).png" mode="" style="width: 40rpx;height: 40rpx;border-radius: 50%;">
						</image>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot">
			<view class="plat-btn">充值</view>
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
	const isSelType = ref(2)
	const payList = ref([{
		name: '微信',
		id: 2,
		image: '/my/static/icon_weixin.png'
	}, {
		name: '支付宝',
		id: 1,
		image: '/my/static/zhifubao.png'
	}]);
	const changeType = (id) => {
		isSelType.value = id
	}
	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};
	const canCashMoney = ref('0.00')
	const configList = reactive([]);
	const moneyIndex = ref(0);
	const moneyInfo = reactive({});
	const getList = () => {
		proxy.$http.getT('/app/payClassify/selectPayClassifyList').then(res => {
			if (res.code == 0) {
				configList.splice(0, configList.length, ...res.data)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	const changeSelMoney = (item, index) => {
		moneyIndex.value = index
		Object.assign(moneyInfo, item)
	};
	/* 获取用户总资金 */
	const getUserInfo = () => {
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				canCashMoney.value = res.data.canCashMoney // 可提现金额
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	}
	onLoad(option => {
		getList()
		getUserInfo()
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
