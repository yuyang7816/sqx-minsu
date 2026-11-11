<template>
	<view class="plat-page plat-emp-time-page">
		<view class="plat-head">
			<view class="plat-emp-head">
				<view class="plat-emp-head__title">TA的忙时</view>
				<view class="plat-emp-head__hint">查看员工忙闲时段安排</view>
			</view>
		</view>

		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-card plat-emp-date-panel">
				<scroll-view scroll-x="true" class="plat-emp-date-scroll">
					<view class="plat-emp-date-row">
						<view class="plat-emp-date-tab"
							:class="{ 'plat-emp-date-tab--on': tabIndex == index, 'plat-emp-date-tab--busy': tabIndex != index && item.hasAppointment == true }"
							@click="changeTabIndex(index)" v-for="item,index in dateList" :key="index">
							<view>{{item.weekday}}</view>
							<view>{{item.displayDate}}</view>
						</view>
					</view>
				</scroll-view>
			</view>

			<view class="plat-card plat-emp-time-panel">
				<view class="plat-emp-time-grid">
					<view class="plat-emp-time-slot" v-for="item,index in timePeriod" :key="index"
						:class="item.busy == true ? 'plat-emp-time-slot--busy' : 'plat-emp-time-slot--free'"
						@click="changeTimeIndex(index)">
						<view>{{item.startTime.slice(0,5)}}</view>
						<view class="plat-emp-time-slot__sep">|</view>
						<view>{{item.endTime.slice(0,5)}}</view>
					</view>
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

	const timePeriod = reactive([]);
	const getTimeList = () => {
		uni.showLoading({
			title: "加载中..."
		})
		proxy.$http.postJson('/app/hairdresser/busyHour', {
			hairdresserId: hairdresserId.value,
		}).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				timePeriod.splice(0, timePeriod.length, ...res.data.dayBusyInfos[0].timeSlots)
				dateList.splice(0, dateList.length, ...res.data.dayBusyInfos.map(item => {
					const dateStr = item.date;
					const date = new Date(dateStr);

					// 获取月和日（不补零）
					const month = date.getMonth() + 1; // getMonth() 返回 0-11
					const day = date.getDate();
					const formattedDate = `${month}月${day}日`;

					// 获取星期几
					const weekday = weekDays[date.getDay()];

					return {
						...item,
						displayDate: formattedDate, // 如：2月3日
						weekday: weekday // 如：星期二
					};
				}))
				console.log(dateList, "dateListdateList")
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const tabIndex = ref(0);
	const changeTabIndex = (index) => {
		tabIndex.value = index
	};

	const timeIndex = ref(0);
	const changeTimeIndex = (index) => {
		timeIndex.value = index
	};


	// 星期映射（getDay() 返回 0 表示周日）
	const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
	// 7天
	const dateList = reactive([])





	// const generateNext7Days = () => {
	// 	const today = new Date()
	// 	const list = []

	// 	for (let i = 0; i < 7; i++) {
	// 		// 创建新日期：今天 + i 天
	// 		const date = new Date(today)
	// 		date.setDate(today.getDate() + i)

	// 		// 获取年月日
	// 		const year = date.getFullYear()
	// 		const month = date.getMonth() + 1 // 月份从0开始
	// 		const day = date.getDate()

	// 		// 星期几
	// 		const weekIndex = date.getDay()
	// 		const week = weekDays[weekIndex]

	// 		// 格式化
	// 		const fullDate = ` ${year}-${month}-${day}`
	// 		const monthDay = ` ${month}月${day}日`

	// 		list.push({
	// 			week,
	// 			monthDay,
	// 			fullDate,
	// 			dateObj: date, // 可选：保留原始 Date 对象
	// 			status: Math.floor(Math.random() * 2)
	// 		})
	// 	}
	// 	dateList.splice(0, dateList.length, ...list);
	// 	console.log(dateList, "dateListdateList")
	// };

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

	const hairdresserId = ref('');
	onLoad((option) => {
		hairdresserId.value = option.hairdresserId
		// generateNext7Days()
		getTimeList()
	});
</script>
