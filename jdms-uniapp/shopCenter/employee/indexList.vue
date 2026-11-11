<template>
	<view class="plat-page plat-emp-list-page">

		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-card plat-emp-search">
				<up-search placeholder="搜索员工名称" v-model="keyword" searchIconSize="30" searchIconColor="#64748B" shape="square"
					:showAction="false" @search="searchInput" @change="changeInput" @clear="clearInput"></up-search>
			</view>

			<view class="plat-list-feed plat-emp-feed">
				<view class="plat-card plat-emp-member" v-for="item,index in hairdresserList" :key="index">
					<view class="plat-emp-member__main">
						<image :src="item.avatar?item.avatar:'/static/logo.png'" mode="aspectFill" class="plat-emp-member__avatar"></image>
						<view class="plat-emp-member__body">
							<view class="plat-emp-member__name">{{item.userName||''}}</view>
							<view class="plat-emp-member__phone">{{item.phone||''}}</view>
						</view>
						<view class="plat-btn plat-btn--sm plat-btn--ghost" @click="deleteHairdresser(item)">踢出</view>
					</view>
				</view>

				<up-loadmore :status="status" v-if="hairdresserList.length>0" />
				<view class="plat-empty" v-if="hairdresserList.length==0">
					<empty></empty>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot">
			<view class="plat-btn" @click="routerGo('/shopCenter/employee/addEmployee')">添加员工</view>
		</view>
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
	import config from '@/common/config.js';

	const keyword = ref('');

	const tabIndex = ref(0);
	const changeTabIndex = (index) => {
		tabIndex.value = index
		page.value = 1
		getOrderList()
	}

	const deleteHairdresser = (item) => {
		console.log(item, "itemitemitemitem")
		uni.showModal({
			title: "提示",
			content: "确定删除当前员工吗？",
			success: function(res) {
				if (res.confirm) {
					proxy.$http.postT('/app/shopsUserRole/deleteShopUser', {
						userIds: item.userId,
						shopId: uni.getStorageSync('shopId')
					}).then(res => {
						if (res.code == 0) {
							setTimeout(() => {
								getOrderList()
							}, 500)
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				}
			}
		})
	};


	// 总页数
	const totalPages = ref(1);
	// 页数
	const page = ref(1);
	const limit = ref(10);
	//加载状态
	const status = ref('loadmore');
	const hairdresserList = reactive([]);
	// 获取订单列表
	const getOrderList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			hairdresserName: keyword.value,
			shopId: uni.getStorageSync('shopId'),
			status: tabIndex.value == 0 ? '' : tabIndex.value //发型师状态 0: 正常 1: 请假 2: 禁用 3:踢出
		}
		uni.showLoading({
			title: "加载中..."
		})
		proxy.$http.get('/app/shopsUserRole/getUserList', data).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading()
			if (res.code == 0) {
				totalPages.value = res.data.pages;
				if (page.value < totalPages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (page.value === 1) {
					hairdresserList.splice(0, hairdresserList.length, ...res.data.records);
				} else {
					hairdresserList.push(...res.data.records);
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
	};

	const searchInput = (e) => {
		keyword.value = e
		page.value = 1
		getOrderList()
	}
	const clearInput = () => {
		keyword.value = ""
		page.value = 1
		getOrderList()
	}
	const changeInput = (e) => {
		if (!e) {
			keyword.value = ""
			page.value = 1
			getOrderList()
		}
	}

	onLoad(option => {

	});
	onShow(() => {
		getOrderList()
	});

	onPullDownRefresh(() => {
		page.value = 1;
		getOrderList()
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1
			status.value = 'loading';
			getOrderList()
		}
	});

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	}
</script>

<style lang="scss" scoped>
	page {
		background: #f4f7fb;
	}

	.plat-emp-list-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #eef4ff 0, #f4f7fb 220rpx);
	}

	.plat-head {
		padding: 24rpx 24rpx 0;
	}

	.plat-emp-head {
		padding: 28rpx 28rpx 24rpx;
		border-radius: 24rpx;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		color: #ffffff;
		box-shadow: 0 10rpx 24rpx rgba(43, 111, 232, 0.14);
	}

	.plat-emp-head__title {
		font-size: 36rpx;
		font-weight: 700;
	}

	.plat-emp-head__hint {
		margin-top: 10rpx;
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.82);
	}

	.plat-body--overlap {
		margin-top: 0;
		padding: 18rpx 24rpx 160rpx;
	}

	.plat-emp-summary,
	.plat-emp-search,
	.plat-emp-member {
		border-radius: 24rpx;
		box-shadow: 0 8rpx 24rpx rgba(15, 23, 42, 0.04);
	}

	.plat-emp-summary {
		margin-bottom: 16rpx;
		padding: 24rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.plat-emp-summary__title {
		font-size: 28rpx;
		font-weight: 700;
		color: #1e293b;
	}

	.plat-emp-summary__desc {
		margin-top: 8rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: #8b94a5;
	}

	.plat-emp-summary__count {
		min-width: 72rpx;
		height: 72rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		background: #eef4ff;
		color: #2b6fe8;
		font-size: 30rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.plat-emp-search {
		padding: 14rpx 18rpx;
	}

	.plat-emp-search :deep(.u-search) {
		background: transparent !important;
	}

	.plat-emp-search :deep(.u-search__content) {
		height: 72rpx !important;
		border-radius: 18rpx !important;
		background: #f8fafc !important;
		border: 1rpx solid #dbe5f1;
	}

	.plat-emp-search :deep(.u-search__content__input) {
		background: transparent !important;
	}

	.plat-emp-feed {
		margin-top: 16rpx;
	}

	.plat-emp-member {
		margin-bottom: 16rpx;
		padding: 24rpx;
	}

	.plat-emp-member__avatar {
		width: 84rpx;
		height: 84rpx;
		border-radius: 50%;
	}

	.plat-emp-member__name {
		font-size: 28rpx;
		font-weight: 700;
		color: #1e293b;
	}

	.plat-emp-member__phone {
		margin-top: 8rpx;
		font-size: 24rpx;
		color: #8b94a5;
	}

	.plat-emp-member .plat-btn--sm {
		min-width: 120rpx;
		border-radius: 999rpx;
	}

	.plat-fixed-foot {
		padding: 20rpx 32rpx calc(24rpx + env(safe-area-inset-bottom));
		background: rgba(255, 255, 255, 0.96);
		box-shadow: 0 -4rpx 20rpx rgba(15, 23, 42, 0.06);
	}
</style>
