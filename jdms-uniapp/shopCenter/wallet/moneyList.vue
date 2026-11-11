<template>
	<view class="plat-page plat-record-page">
		<view class="plat-record-head">
			<view>
				<view class="plat-record-head__title">{{ userType == 2 ? '资金明细' : '提现明细' }}</view>
				<view class="plat-record-head__desc">按时间查看门店资金流向与提现处理记录</view>
			</view>
			<view class="plat-record-head__badge">{{ list.length }} 条</view>
		</view>
		<view class="plat-list-feed" style="padding: 0rpx;padding-top: 20rpx;">
			<view v-for="(item, index) in list" :key="index" class="plat-card plat-record-card">
				<view v-if="userType == 2">
					<view class="plat-record-card__title">{{item.title}}</view>
					<view class="plat-record-card__content">{{item.content}}</view>
					<view class="plat-record-card__time">创建时间：{{item.createTime}}</view>
					<view class="plat-record-card__foot">
						<text v-if="item.type == 1" class="plat-record-card__amount--in">+{{item.money}}元</text>
						<text v-if="item.type == 2" class="plat-record-card__amount--out">-{{item.money}}元</text>
					</view>
				</view>
				<view v-if="userType == 1">
					<view class="plat-record-card__head">
						<view class="flex align-center">
							<text v-if="item.classify==2" class="plat-record-card__status" :class="item.state!=3?'':'plat-record-card__status--fail'">微信</text>
							<text class="plat-record-card__status" v-if="item.state==1">提现成功</text>
							<text class="plat-record-card__status" v-if="item.state==0">提现中</text>
							<text class="plat-record-card__status plat-record-card__status--fail" v-if="item.state==-1">提现失败</text>
						</view>
						<text class="plat-record-card__money">￥{{item.money}}</text>
					</view>
					<view class="plat-record-card__line text-cut" v-if="item.classify==1">支付宝账号：{{item.zhifubao}}</view>
					<view class="plat-record-card__line" v-if="item.classify==1">支付宝姓名：{{item.zhifubaoName}}</view>
					<view class="plat-record-card__line">提现时间：{{item.createAt}}</view>
					<view class="plat-record-card__line" v-if="item.state==1">成功时间：{{item.outAt}}</view>
					<view class="plat-record-card__line">费用说明：提现{{item.money||'0'}}，手续费{{item.rate||'0'}}元</view>
					<view class="plat-record-card__line plat-record-card__status--fail" v-if="item.state==-1">失败原因：{{item.refund}}</view>
				</view>
			</view>

			<empty v-if="list.length == 0" content="暂无明细"></empty>

			<view class="plat-list-load" v-if="list.length > 0">
				<text v-if="status === 'loading'">正在加载...</text>
				<text v-if="status === 'nomore'">没有更多数据了</text>
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
		onReachBottom,
		onPullDownRefresh,
		onPageScroll
	} from '@dcloudio/uni-app';

	const {
		proxy
	} = getCurrentInstance();


	const list = ref([]);
	const page = ref(1);
	const limit = ref(10);
	const totalPages = ref(1);
	const status = ref('loadmore'); // loadmore / loading / nomore
	const userType = ref(1);
	const scrollTop = ref(false);

	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value
		}
		let url = '';
		if (userType.value == 2) {
			url = "/app/shopMoneyDetails/getShopMoneyDetailsList"
		} else {
			url = "/app/cash/selectPayDetails"
			data.userType = 2
		}
		proxy.$http.getT(url, data).then(res => {
			if (res.code === 0) {
				let dataList = userType.value == 2 ? res.data.records : res.data.list
				totalPages.value = res.data.pages || 1;
				if (page.value === 1) {
					list.value = dataList || [];
				} else {
					list.value = [...list.value, ...dataList];
				}
				// 更新加载状态
				if (page.value >= totalPages.value) {
					status.value = 'nomore';
				} else {
					status.value = 'loadmore';
				}
			}
			uni.stopPullDownRefresh();
			uni.hideLoading();
		}).catch(() => {
			uni.stopPullDownRefresh();
			uni.hideLoading();
		});
	};

	onLoad((options) => {
		if (options.userType) {
			userType.value = options.userType;
		}
		uni.showLoading({
			title: "加载中..."
		});
		getList();
	});

	onReachBottom(() => {
		// 分页检查：只有还有下一页时才加载
		if (page.value < totalPages.value) {
			page.value = page.value + 1;
			status.value = 'loading';
			getList();
		}
	});

	onPullDownRefresh(() => {
		page.value = 1;
		status.value = 'loadmore';
		getList();
	});

	onPageScroll((e) => {
		scrollTop.value = e.scrollTop > 200;
	});
</script>

<style lang="scss" scoped>
	page {
		background: #f4f7fb;
	}

	.plat-record-page {
		min-height: 100vh;
		padding: 24rpx 24rpx 32rpx;
		background: linear-gradient(180deg, #eef4ff 0, #f4f7fb 220rpx);
	}

	.plat-record-head {
		padding: 28rpx;
		border-radius: 24rpx;
		background: #ffffff;
		box-shadow: 0 8rpx 24rpx rgba(15, 23, 42, 0.04);
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 16rpx;
	}

	.plat-record-head__title {
		font-size: 32rpx;
		font-weight: 700;
		color: #1e293b;
	}

	.plat-record-head__desc {
		margin-top: 8rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: #8b94a5;
	}

	.plat-record-head__badge {
		flex-shrink: 0;
		height: 52rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		background: #eef4ff;
		color: #2b6fe8;
		font-size: 22rpx;
		display: inline-flex;
		align-items: center;
	}

	.plat-record-page .plat-list-feed {
		padding-top: 16rpx !important;
	}

	.plat-record-card {
		margin-bottom: 16rpx;
		padding: 24rpx;
		border-radius: 24rpx;
		box-shadow: 0 8rpx 24rpx rgba(15, 23, 42, 0.04);
	}

	.plat-record-card__title,
	.plat-record-card__money {
		font-size: 30rpx;
		font-weight: 700;
		color: #1e293b;
	}

	.plat-record-card__content,
	.plat-record-card__line,
	.plat-record-card__time {
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #8b94a5;
	}

	.plat-record-card__foot {
		margin-top: 16rpx;
		display: flex;
		justify-content: flex-end;
	}

	.plat-record-card__amount--in {
		color: #16a34a;
		font-size: 30rpx;
		font-weight: 700;
	}

	.plat-record-card__amount--out,
	.plat-record-card__status--fail {
		color: #ef4444;
	}

	.plat-record-card__head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.plat-record-card__status {
		display: inline-flex;
		align-items: center;
		height: 44rpx;
		padding: 0 16rpx;
		border-radius: 999rpx;
		background: #eef4ff;
		color: #2b6fe8;
		font-size: 22rpx;
		margin-right: 10rpx;
	}

	.plat-list-load {
		padding: 18rpx 0 8rpx;
		text-align: center;
		font-size: 22rpx;
		color: #94a3b8;
	}
</style>
