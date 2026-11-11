<template>
	<view class="plat-page plat-emp-service-page">
		<view class="plat-head">
			<view class="plat-emp-head">
				<view class="plat-emp-head__title">TA的服务</view>
				<view class="plat-emp-head__hint">查看员工服务订单记录</view>
			</view>
		</view>

		<view class="plat-body plat-body--pad">
			<view class="plat-filter-bar plat-emp-filter">
				<scroll-view scroll-x="true" class="plat-emp-tab-scroll">
					<view class="plat-shop-subtabs plat-emp-subtabs">
						<view class="plat-shop-subtab" :class="tabIndex == index ? 'plat-shop-subtab--on' : ''"
							@click="changeTabIndex(item.id,index)" v-for="item,index in tabLIst" :key="index">{{item.text}}</view>
					</view>
				</scroll-view>
			</view>

			<view class="plat-list-feed plat-emp-feed">
				<view class="plat-card plat-cover-card plat-cover-card--row plat-emp-service-card" v-for="item,index in serviceItemList"
					:key="index" @click="routerGo('/shopViews/orderList/orderDetail?orderId='+item.id)">
					<image :src="item.shopImg?item.shopImg:'/static/logo.png'" mode="aspectFill" class="plat-cover-card__img"></image>
					<view class="plat-cover-card__body">
						<view class="plat-emp-service-card__head">
							<view class="plat-cover-title text-cut">{{item.itemName||''}}</view>
							<view class="plat-emp-service-card__status"
								:class="item.status == 1 ? 'plat-emp-service-card__status--active' : ''">
								{{orderStatusText(item.status)}}
							</view>
						</view>
						<view class="plat-cover-sub">服务时间: {{item.startTime}} ~ {{item.endTime}}</view>
						<view class="plat-emp-service-card__foot">
							<view class="plat-cover-price">
								<text class="unit">￥</text>
								<text class="num">{{item.amount||'0'}}</text>
							</view>
							<view class="plat-btn plat-btn--sm" @click.stop="orderVerify(item)" v-if="item.status == 1">核销订单</view>
						</view>
					</view>
				</view>

				<up-loadmore :status="status" v-if="serviceItemList.length>0" />
				<view class="plat-empty" v-if="serviceItemList.length==0">
					<empty></empty>
				</view>
			</view>
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

	const tabLIst = ref([{ //	订单状态 0:未支付 1:已支付 2:已完成 3:已取消 4:退款中 5:已退款
		text: '全部',
		id: ''
	}, {
		text: '待服务',
		id: 1
	}, {
		text: '已完成',
		id: 2
	}, {
		text: '已取消',
		id: 3
	}, {
		text: '已退款',
		id: 5
	}])
	const orderVerify = (item) => {
		uni.showModal({
			title: "提示",
			content: "确认核销该订单吗?",
			success: function(res) {
				if (res.confirm) {
					let data = {
						id: item.id
					}
					proxy.$http.postJson("/app/order/verify", data).then(res => {
						uni.hideLoading()
						if (res && res.code == 0) {
							console.log(res)
							uni.showToast({
								title: '核销成功'
							})
							setTimeout(() => {
								getOrderList()
							}, 500)
						} else {
							uni.showToast({
								title: res.msg,
								icon: "none"
							})
						}
					})
				}
			}
		})
	};

	const totalNum = ref(0);
	// 总页数
	const totalPages = ref(1);
	// 页数
	const page = ref(1);
	const limit = ref(10);
	//加载状态
	const status = ref('loadmore');
	const serviceItemList = reactive([]);
	// 获取订单列表
	const getOrderList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			hairdresserId: hairdresserId.value,
			status: orderStatus.value
		}
		uni.showLoading({
			title: "加载中..."
		})
		proxy.$http.postJson('/app/order/page', data).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading()
			if (res.code == 0) {
				totalPages.value = res.data.pages;
				totalNum.value = res.data.total;
				if (page.value < totalPages.value) {
					status.value = 'loadmore';
				} else {
					status.value = 'nomore';
				}
				if (page.value === 1) {
					serviceItemList.splice(0, serviceItemList.length, ...res.data.records);
				} else {
					serviceItemList.push(...res.data.records);
				}
				console.log(serviceItemList, "serviceItemList")
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
	};
	const orderStatus = ref(null);
	const tabIndex = ref(0);
	const changeTabIndex = (id, index) => {
		tabIndex.value = index
		orderStatus.value = id
		page.value = 1
		getOrderList()
	}

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};
	const orderStatusText = (val) => {
		switch (val) {
			case 0:
				return '待支付';
			case 1:
				return '待使用';
			case 2:
				return '已完成';
			case 3:
				return '已取消';
			case 4:
				return '退款中';
			case 5:
				return '已退款';
		}
	};
	const hairdresserId = ref('');
	onLoad(option => {
		if (option.hairdresserId) {
			hairdresserId.value = option.hairdresserId
		}

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
</script>
