<template>
	<view class="">
		<view class="tab_fixed">
			<up-tabs :list="list1" @click="tabClick" :scrollable="false" :activeStyle="{
            color: '#2C8EFE',
            fontWeight: 'bold',
            transform: 'scale(1.05)'
        }" :inactiveStyle="{
            color: '#606266',
            transform: 'scale(1)'
        }" lineColor="#2C8EFE"></up-tabs>
		</view>

		<view class="container_list" style="padding-bottom: 0;">
			<view class="room_list" v-for="(item, index) in list" :key="item.roomId || index">
				<view class="flex align-center">
					<image :src="item.roomTitleImg || '/shopCenter/static/defaultRoom.png'" mode="aspectFill" class="room_Image">
					</image>
					<view class="room-info">
						<view class="room-name-row">
							<text class="f32 text-heavy room-name">{{ item.roomName || '' }}</text>
							<text class="room-type-tag" v-if="item.roomType">{{ item.roomType }}</text>
						</view>

						<view class="f26 c99 room-meta" style="margin-top: 8rpx;">
							{{ item.floorArea }}㎡ | {{ item.floor }}层 | 入住{{ item.checkInTime }} | 退房{{ item.checkOutTime }}
						</view>

						<view class="flex align-center" style="margin-top: 8rpx;" v-if="item.roomCount">
							<text class="f24" style="color: #2C8EFE;">剩余{{ item.roomCount }}间</text>
						</view>

						<view class="flex align-center justify-between padding-top-xs">
							<view class="flex" style="align-items: baseline;">
								<text class="text-bold f24 CF33">￥</text>
								<text class="text-bold f38 CF33">{{ item.money || 0 }}</text>
								<view class="vipBox">
									<view class="cFF padding-lr-xs"
										style="background: linear-gradient(122deg, #1F1F1F 0%, #6B696C 46%, #0C0304 100%);border-radius: 4rpx;height: 30rpx;">
										VIP
									</view>
									<view class="text-bold" style="padding: 0 6rpx 0 2rpx;">￥{{ item.memberMoney ||  0 }}</view>
								</view>
							</view>
						</view>
					</view>
				</view>
				<view class="flex align-center justify-end margin-top" style="gap: 12rpx;">
					<view class="yuding_btn" @click="editRoom(item.roomId)">编辑</view>
					<view class="yuding_btn_xj" @click="toggleStatus(item)">
						{{ item.isEnable == 1 ? '下架' : '上架' }}
					</view>
				</view>
			</view>

			<u-loadmore :status="status" v-if="list.length > 0" />
		</view>

		<!-- 空状态 -->
		<view class="text-center margin-top-xl" v-if="list.length === 0 && !loading">
			<image src="/shopCenter/static/noData.png" mode="" style="width: 340rpx;height: 270rpx;"></image>
			<view class="f28" style="margin-top: -80rpx;">暂无{{ currentTabName }}房源</view>
		</view>

		<view class="fb_btn" v-if="list.length === 0 && !loading" @click="addRoom">立即发布</view>
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
		onReachBottom,
		onPageScroll
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();

	const list1 = reactive([{
			name: '上架中',
			status: 1
		},
		{
			name: '已下架',
			status: 0
		}
	]);
	const roomStatus = ref(1);
	const currentTab = ref(0);
	const currentTabName = computed(() => list1[currentTab.value]?.name || '');

	const page = ref(1);
	const limit = ref(10);
	const totalPages = ref(1);
	const status = ref('loadmore');
	const list = reactive([]);
	const loading = ref(false);

	const tabClick = (item) => {
		currentTab.value = item.index;
		roomStatus.value = item.status
		page.value = 1;
		list.splice(0, list.length);
		getRoomList();
	};

	const getRoomList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			shopId: uni.getStorageSync('shopId'),
			isEnable: roomStatus.value
		}
		loading.value = true;
		uni.showLoading({
			title: '加载中...'
		});
		proxy.$http.getT('/app/shopsRoom/getShopRoomList', data).then(res => {
			uni.hideLoading();
			loading.value = false;
			if (res.code == 0) {
				totalPages.value = res.data.pages || 1;
				const records = res.data.records || [];
				if (page.value == 1) {
					list.splice(0, list.length, ...records);
				} else {
					list.push(...records);
				}
				if (page.value >= totalPages.value) {
					status.value = 'nomore';
				} else {
					status.value = 'loadmore';
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		}).catch(() => {
			uni.hideLoading();
			loading.value = false;
		});
	};

	// 编辑房间
	const editRoom = (roomId) => {
		uni.navigateTo({
			url: `/shopCenter/rooms/addRoom?roomId=${roomId}`
		});
	};

	// 上架/下架
	const toggleStatus = (item) => {
		const newStatus = item.isEnable == 1 ? 0 : 1;
		const actionText = newStatus == 1 ? '上架' : '下架';

		uni.showModal({
			title: '提示',
			content: `确定要${actionText}该房间吗？`,
			success: (res) => {
				if (res.confirm) {
					proxy.$http.postJson('/app/shopsRoom/saveShopRoom', {
						roomId: item.roomId,
						isEnable: newStatus
					}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: `${actionText}成功`,
								icon: 'none'
							});
							getRoomList();
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							});
						}
					});
				}
			}
		});
	};

	// 添加房间
	const addRoom = () => {
		uni.navigateTo({
			url: '/shopCenter/rooms/addRoom'
		});
	};

	onPageScroll(e => {});
	onLoad(option => {});
	onShow(() => {
		page.value = 1;
		getRoomList();
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getRoomList();
		uni.stopPullDownRefresh();
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1;
			status.value = 'loading';
			getRoomList();
		}
	});
</script>

<style lang="scss" scoped>
	page {
		background-color: #F5F7FF;
	}

	.vipBox {
		width: max-content;
		background-color: #F1E0CE;
		display: flex;
		align-items: center;
		border-radius: 4rpx;
		height: 30rpx;
		font-size: 20rpx;
		margin-top: 4rpx;
		margin-left: 10rpx;
	}

	.fb_btn {
		width: 333rpx;
		height: 88rpx;
		border-radius: 12rpx;
		text-align: center;
		line-height: 88rpx;
		color: #FFFFFF;
		margin: auto;
		background-color: #2C8EFE;
		margin-top: 20rpx;
	}

	.yuding_btn_xj {
		width: 113rpx;
		height: 53rpx;
		background: #CCCCCC;
		border-radius: 27rpx;
		font-size: 24rpx;
		color: #FFFFFF;
		text-align: center;
		line-height: 55rpx;
	}

	.yuding_btn {
		width: 113rpx;
		height: 53rpx;
		background: #2C8EFE;
		border-radius: 27rpx;
		font-size: 24rpx;
		color: #FFFFFF;
		text-align: center;
		line-height: 55rpx;
	}

	.room_list {
		background-color: #FFFFFF;
		width: calc(100% - 120rpx);
		margin: auto;
		padding: 30rpx;
		margin-bottom: 20rpx;
		border-radius: 24rpx;
	}

	.container_list {
		width: 100%;
		overflow: hidden;
		margin-top: 15%;
		padding-bottom: 40rpx;
	}

	.room_Image {
		width: 210rpx;
		height: 188rpx;
		border-radius: 24rpx;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	/* 房间信息区域 */
	.room-info {
		width: calc(100% - 230rpx);
		min-width: 0;
		/* 关键：允许 flex 子项收缩 */
	}

	/* 房间名称行 */
	.room-name-row {
		display: flex;
		align-items: center;
		gap: 12rpx;
	}

	/* 房间名称 - 超出省略 */
	.room-name {
		flex: 1;
		min-width: 0;
		/* 关键：允许文本收缩 */
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	/* 房间类型标签 */
	.room-type-tag {
		flex-shrink: 0;
		padding: 4rpx 12rpx;
		background-color: #EEF4FF;
		color: #2C8EFE;
		font-size: 22rpx;
		border-radius: 8rpx;
		white-space: nowrap;
	}

	/* 房型信息 - 超出省略 */
	.room-meta {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.CF33 {
		color: #FF3333;
	}

	.ccc {
		color: #cccccc;
	}

	.c99 {
		color: #999999;
	}

	.tab_fixed {
		position: fixed;
		top: 0;
		width: 100%;
		background-color: #FFFFFF;
		z-index: 99;
	}
</style>