<template>
	<view class="plat-page plat-team-page">
		<view class="plat-head">
			<view class="plat-team-head">
				<view class="plat-team-head__title">推广中心</view>
				<view class="plat-team-head__hint">邀请好友，共享消费佣金奖励</view>
			</view>
		</view>

		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-card plat-team-stats">
				<view class="plat-team-stats__row">
					<view class="plat-team-stats__item">
						<view class="plat-team-stats__num">{{ totalCount || 0 }}</view>
						<view class="plat-team-stats__label">邀请好友（人）</view>
					</view>
					<view class="plat-team-stats__item" @click="goDet">
						<view class="plat-team-stats__num plat-team-stats__num--price">{{ teamMoney || 0 }}</view>
						<view class="plat-team-stats__label">我的收益 ›</view>
					</view>
				</view>
				<view class="plat-team-stats__tip" v-if="yijiyongjin > 0">*奖励一级好友消费佣金的{{ yijiyongjin * 100 }}%</view>
				<view class="plat-team-stats__tip" v-if="erjiyongjin > 0">*奖励二级好友消费佣金的{{ erjiyongjin * 100 }}%</view>
				<view class="plat-btn plat-team-stats__btn" @click="goYao">邀请好友</view>
			</view>

			<view class="plat-seg plat-team-seg">
				<view class="plat-seg-item" :class="{ 'plat-seg-item--active': type == 1 }" @click="changeList(1)">一级( {{ countChildren }} )</view>
				<view class="plat-seg-item" :class="{ 'plat-seg-item--active': type == 2 }" @click="changeList(2)">二级( {{ countChildrenChildren }} )</view>
			</view>

			<view class="plat-list-feed plat-team-feed">
				<view class="plat-card plat-team-member" v-for="(item, index) in list" :key="index">
					<view class="plat-team-member__main">
						<image :src="item.avatar || '/static/logo.png'" class="plat-team-member__avatar" mode="aspectFill"></image>
						<view class="plat-team-member__body">
							<view class="plat-team-member__name">{{ item.userName || '' }}</view>
							<view class="plat-team-member__time">加入时间 {{ item.createTime ? item.createTime.substr(0, 10) : '' }}</view>
						</view>
						<!-- <view class="plat-team-member__idx">#{{ index + 1 }}</view> -->
					</view>
				</view>
				<u-empty v-if="list.length == 0" text="暂无数据" mode="order"></u-empty>
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
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';

	const {
		proxy
	} = getCurrentInstance();

	const totalCount = ref(0);
	const isopen = ref('否');
	const firstPrice = ref('');
	const secondPice = ref('');
	const countChildren = ref(0);
	const countChildrenChildren = ref(0);
	const YaoqingShangJin = ref(0);
	const setting = ref(1);
	const list = ref([]);
	const page = ref(1);
	const limit = ref(10);
	const type = ref(1);
	const status = ref(0);
	const teamCount = ref(0);
	const teamMoney = ref(0);
	const isFenxiao = ref('否');
	const oneTeamCount = ref(0);
	const twoTeamCount = ref(0);
	const yijiyongjin = ref('');
	const erjiyongjin = ref('');
	const currentTab = ref(0);
	const loadingType = ref('');
	const count = ref(0);

	const tabsnva = reactive([{
		loadingType: ''
	}]);

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

	// 获取用户信息
	const getUserInfo = () => {
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				yijiyongjin.value = res.data.zhiRate || 0;
				erjiyongjin.value = res.data.feiRate || 0;
				uni.setStorageSync('avatar', res.data.avatar);
				uni.setStorageSync('invitation', res.data.invitationCode);
			}
		});
	};

	// 用户推广信息
	const getTeamMoney = () => {
		proxy.$http.getT('/app/invite/getExtensionData').then(res => {
			if (res.code == 0) {
				teamMoney.value = res.data.extensionProfit;
				countChildren.value = res.data.countChildren;
				countChildrenChildren.value = res.data.countChildrenChildren;
				totalCount.value  = res.data.totalCount;
			}
		});
	};

	const goYao = () => {
		uni.navigateTo({
			url: '/pages/my/invitationUser'
		});
	};

	const changeList = (zhishu) => {
		page.value = 1;
		type.value = zhishu;
		getMoney();
	};

	// 切换一级二级
	const changeTab = (e) => {
		currentTab.value = e.target.current;
		if (currentTab.value == 0) {
			type.value = 1;
			getMoney();
		} else if (currentTab.value == 1) {
			type.value = 2;
			getMoney();
		}
	};

	// 邀请的用户
	const getMoney = () => {
		loadingType.value = 1;
		uni.showLoading({
			title: '加载中...'
		});
		let data = {
			page: page.value,
			limit: limit.value,
			type: type.value,
		};
		proxy.$http.getT('/app/invite/getExtensionList', data).then(res => {
			if (res.code == 0) {
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

	const goDet = () => {
		uni.navigateTo({
			url: './earnings'
		});
	};

	onLoad(() => {
		const userId = uni.getStorageSync('userId');
		getUserInfo();
		getMoney();
		getTeamMoney();
	});

	onPullDownRefresh(() => {
		page.value = 1;
		getMoney();
	});

	onReachBottom(() => {
		if (list.value.length < count.value) {
			page.value = page.value + 1;
			getMoney();
		} else {
			uni.showToast({
				title: '没有更多了',
				icon: 'none'
			});
		}
	});
</script>
