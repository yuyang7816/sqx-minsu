<template>
	<view class="invoice-page invoice-record-page">
		<view class="invoice-record-hero">
			<view class="invoice-record-hero__title">开票记录</view>
			<view class="invoice-record-hero__desc">查看申请进度、发票信息与接收方式</view>
		</view>

		<view class="invoice-record-list">
			<view class="invoice-record-card" v-for="(item,index) in list" :key="index">
				<view class="invoice-record-card__head">
					<view class="invoice-record-card__status" :class="item.status==3 ? 'invoice-record-card__status--fail' : (item.status==2 ? 'invoice-record-card__status--done' : 'invoice-record-card__status--pending')">
						{{item.status==1?'处理中':(item.status==2?'开票完成':'开票失败')}}
					</view>
					<view class="invoice-record-card__amount">{{item.amount}}元</view>
				</view>

				<view class="invoice-record-card__section">
					<view class="invoice-record-card__line"><text>发票抬头</text><text>{{item.title}}</text></view>
					<view class="invoice-record-card__line"><text>识别号</text><text>{{item.creditCode}}</text></view>
					<view class="invoice-record-card__line"><text>发票金额</text><text>{{item.amount}}</text></view>
					<view class="invoice-record-card__line"><text>开票内容</text><text>{{item.content}}</text></view>
				</view>

				<view class="invoice-record-card__divider"></view>

				<view class="invoice-record-card__section">
					<view class="invoice-record-card__line"><text>开户行</text><text>{{item.openBank}}</text></view>
					<view class="invoice-record-card__line"><text>银行卡号</text><text>{{item.bankNumber}}</text></view>
					<view class="invoice-record-card__line"><text>开户手机号</text><text>{{item.phone}}</text></view>
					<view class="invoice-record-card__line"><text>接收邮箱</text><text>{{item.receiveEmail}}</text></view>
					<view class="invoice-record-card__line"><text>接收手机号</text><text>{{item.receivePhone}}</text></view>
				</view>

				<view class="invoice-record-card__time">申请时间 {{item.createTime}}</view>
			</view>
		</view>
		<u-loadmore :status="status" v-if="list.length>0" />
		<u-empty style="position: fixed;top: 56%;left: 50%;transform: translate(-50%,-50%);" v-if="list.length==0"
			text="数据为空" mode="order">
		</u-empty>
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

	const list = ref([])
	const page = ref(1)
	const pages = ref(1)
	const limit = ref(10)
	// 加载状态
	const status = ref('loadmore')

	onLoad((e) => {})

	onShow(() => {
		page.value = 1
		getlist()
	})

	const goNav = (url) => {
		uni.navigateTo({
			url
		})
	}

	// 订单数据
	const getlist = () => {
		const data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId')
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.postJson('/app/invoice/page', data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value < pages.value) {
					status.value = 'loadmore'
				} else {
					status.value = 'nomore'
				}
				if (page.value === 1) {
					list.value = res.data.records
				} else {
					list.value = list.value.concat(res.data.records)
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
			status.value = 'nomore'
			uni.stopPullDownRefresh()
		})

	}

	onPullDownRefresh(() => {
		page.value = 1
		getlist()
	})

	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			status.value = 'loading'
			getlist()
		}
	})
</script>

<style lang="scss" scoped>
	.invoice-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #eef5ff 0%, #f6f8fc 34%, #f5f7fb 100%);
	}

	.invoice-record-page {
		padding: 24rpx 24rpx 40rpx;
	}

	.invoice-record-hero {
		padding: 34rpx 30rpx;
		border-radius: 26rpx;
		background: #ffffff;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.invoice-record-hero__title {
		font-size: 38rpx;
		font-weight: 700;
		color: #1e2a3a;
	}

	.invoice-record-hero__desc {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #8b94a5;
	}

	.invoice-record-list {
		margin-top: 20rpx;
		display: flex;
		flex-direction: column;
		gap: 18rpx;
	}

	.invoice-record-card {
		background: #ffffff;
		border-radius: 24rpx;
		padding: 26rpx 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.invoice-record-card__head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.invoice-record-card__status {
		height: 54rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		font-size: 24rpx;
		font-weight: 700;
		display: inline-flex;
		align-items: center;
	}

	.invoice-record-card__status--pending {
		background: #fff7e8;
		color: #d97706;
	}

	.invoice-record-card__status--done {
		background: #edf9f0;
		color: #18a058;
	}

	.invoice-record-card__status--fail {
		background: #fff0f3;
		color: #e34d59;
	}

	.invoice-record-card__amount {
		font-size: 34rpx;
		font-weight: 700;
		color: #1e2a3a;
	}

	.invoice-record-card__section {
		margin-top: 20rpx;
		display: flex;
		flex-direction: column;
		gap: 14rpx;
	}

	.invoice-record-card__line {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
		font-size: 24rpx;
		line-height: 1.6;
	}

	.invoice-record-card__line text:first-child {
		color: #8b94a5;
		flex-shrink: 0;
	}

	.invoice-record-card__line text:last-child {
		color: #1e2a3a;
		text-align: right;
		word-break: break-all;
	}

	.invoice-record-card__divider {
		height: 2rpx;
		background: #f0f2f5;
		margin-top: 22rpx;
	}

	.invoice-record-card__time {
		margin-top: 22rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: #8b94a5;
	}
</style>


