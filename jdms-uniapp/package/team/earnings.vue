<template>
	<view class="plat-page plat-earnings-page">
		<view class="plat-head">
			<view class="plat-earnings-hero">
				<view class="plat-earnings-hero__title">我的收益</view>
				<view class="plat-earnings-hero__row" @click="navigate('/my/wallet/cash')">
					<view class="plat-earnings-hero__item">
						<view class="plat-earnings-hero__num">{{ teamMoney }}</view>
						<view class="plat-earnings-hero__label">总收益</view>
					</view>
					<view class="plat-earnings-hero__item">
						<view class="plat-earnings-hero__num">{{ oneTeamMoney }}</view>
						<view class="plat-earnings-hero__label">一级收益</view>
					</view>
					<view class="plat-earnings-hero__item">
						<view class="plat-earnings-hero__num">{{ twoTeamMoney }}</view>
						<view class="plat-earnings-hero__label">二级收益</view>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-seg plat-team-seg">
				<view v-for="(item, index) in tabList" :key="index" class="plat-seg-item"
					:class="{ 'plat-seg-item--active': type == item.type }" @click="changeList(item.type)">
					{{item.text}}
				</view>
			</view>

			<view class="plat-list-feed plat-team-feed">
				<view class="plat-card plat-team-member" v-for="(item, index) in list" :key="index">
					<view class="plat-team-member__main">
						<image :src="item.avatar || '/static/logo.png'" class="plat-team-member__avatar" mode="aspectFill"></image>
						<view class="plat-team-member__body">
							<view class="plat-team-member__name">{{ item.userName }}</view>
							<view class="plat-team-member__time">奖励佣金</view>
						</view>
						<view class="plat-team-member__reward">￥{{ item.money || 0 }}</view>
					</view>
				</view>
				<empty v-if="list.length == 0"></empty>
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

	const total = ref(0);
	const first = ref(0);
	const second = ref(0);
	const list = ref([]);
	const page = ref(1);
	const limit = ref(10);
	const tabIndex = ref(1);
	const checkReZhiShu = ref('否');
	const checkReTuanZhang = ref('否');
	const checkReFeiZhiShu = ref('否');
	const scrollTop = ref(false);
	const type = ref(1);
	const teamMoney = ref(0);
	const oneTeamMoney = ref(0);
	const twoTeamMoney = ref(0);
	const count = ref(0);

	const tabList = reactive([{
		state: 'zs',
		text: '一级',
		type: 1,
		number: 0
	}, {
		state: 'fzs',
		text: '二级',
		type: 2,
		number: 0
	}]);

	const navigate = (url) => {
		uni.navigateTo({
			url
		});
	};

	const changeList = (e) => {
		page.value = 1;
		type.value = e;
		getList();
	};

	const getTeamMoney = () => {
		proxy.$http.getT('/app/invite/getExtensionData').then(res => {
			if (res.code == 0) {
				teamMoney.value = res.data.extensionProfit;
				oneTeamMoney.value = res.data.oneMoney;
				twoTeamMoney.value = res.data.twoMoney;
			}
		});
	};

	// 收益列表
	const getList = () => {
		uni.showLoading({
			title: '加载中...'
		});
		let data = {
			page: page.value,
			limit: limit.value,
			type: type.value
		};
		proxy.$http.getT('/app/invite/getProfitList', data).then(res => {
			if (res.code == 0 && res.data) {
				count.value = res.data.total;
				if (page.value == 1) {
					list.value = res.data.records;
				} else {
					list.value = [...list.value, ...res.data.records];
				}
			}
			uni.hideLoading();
			uni.stopPullDownRefresh();
		});
	};

	onLoad(() => {
		uni.showLoading({
			title: '加载中...'
		});
		getTeamMoney();
		getList();
	});

	onPageScroll((e) => {
		scrollTop.value = e.scrollTop > 200;
	});

	onReachBottom(() => {
		if (list.value.length < count.value) {
			page.value = page.value + 1;
			getList();
		} else {
			uni.showToast({
				title: '没有更多了',
				icon: 'none'
			});
		}
	});

	onPullDownRefresh(() => {
		page.value = 1;
		getList();
	});
</script>
