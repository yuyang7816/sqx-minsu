<template>
	<view class="">
		<view class="header_view">
			<view class="header_view_tj">
				<view class="">
					<view class="f40 text-bold ">20</view>
					<view class="f26 text-heavy ">累计发布</view>
				</view>
				<view class="">
					<view class="f40 text-bold ">20</view>
					<view class="f26 text-heavy ">累计上架</view>
				</view>
				<view class="">
					<view class="f40 text-bold ">20</view>
					<view class="f26 text-heavy ">累计入住</view>
				</view>
			</view>
		</view>

		<!-- 我的发布 -->
		<view class="container_view" @click="routerGo('/shopCenter/myPosts/index')">
			<view class="f30 text-heavy">我的发布</view>
			<view class="flex align-center justify-between margin-top">
				<view class="text-center">
					<image src="/shopCenter/static/dsj.png" mode="" style="width: 52rpx;height: 52rpx;"></image>
					<view class="f26 ">待上架</view>
				</view>
				<view class="text-center">
					<image src="/shopCenter/static/xsz.png" mode="" style="width: 52rpx;height: 52rpx;"></image>
					<view class="f26 ">显示中</view>
				</view>
				<view class="text-center">
					<image src="/shopCenter/static/yxj.png" mode="" style="width: 52rpx;height: 52rpx;"></image>
					<view class="f26 ">已下架</view>
				</view>
			</view>
		</view>
		<!-- 我的房间 -->
		<view class="container_view">
			<view class="f30 text-heavy">我的房间</view>
			<view class="flex align-center justify-between margin-top">
				<view class="text-center">
					<view class="f40 text-bold ">20</view>
					<view class="f26 ">房间总计</view>
				</view>
				<view class="text-center">
					<view class="f40 text-bold ">20</view>
					<view class="f26 ">剩余房间</view>
				</view>
				<view class="text-center" @click="routerGo('/shopCenter/rooms/addRoom')">
					<up-icon name="plus-circle-fill" color="#6186ff" size="28" style="justify-content: center;"></up-icon>
					<view class="f26 ">添加房间</view>
				</view>
			</view>
		</view>
		<!-- 我的订单 -->
		<view class="container_view" @click="routerGo('/shopCenter/order/index')">
			<view class="f30 text-heavy">我的订单</view>
			<view class="flex align-center justify-between margin-top">
				<view class="text-center">
					<view class="f40 text-bold ">20</view>
					<view class="f26 ">待使用</view>
				</view>
				<view class="text-center">
					<view class="f40 text-bold ">20</view>
					<view class="f26 ">待售后</view>
				</view>
				<view class="text-center">
					<view class="f40 text-bold ">20</view>
					<view class="f26 ">全部</view>
				</view>
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
		onShow,
		onPullDownRefresh,
		onReachBottom,
		onPageScroll
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();


	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	}

	onPageScroll(e => {});
	onLoad(option => {

	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		background-color: #F5F7FF;
	}

	.container_view {
		width: calc(100% - 120rpx);
		padding: 30rpx;
		margin: auto;
		background-color: #FFFFFF;
		border-radius: 24rpx;
		margin-top: 30rpx;
	}

	.header_view_tj {
		width: calc(100% - 120rpx);
		padding: 30rpx;
		margin: auto;
		background-color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: space-between;
		border-radius: 24rpx 24rpx 0rpx 0rpx;
		text-align: center;
	}

	.header_view {
		background-color: #2C8EFE;
		padding-top: 60rpx;
	}
</style>