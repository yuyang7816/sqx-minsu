<!-- 帮助反馈页面 -->
<template>
	<view class="plat-page plat-setting-page plat-help-page">
		<view class="plat-head plat-head--immersive">
			<view class="plat-setting-head">
				<view class="plat-setting-head__title">帮助中心</view>
				<view class="plat-setting-head__hint">常见问题与客服支持</view>
			</view>
		</view>

		<view class="plat-body plat-body--pad plat-body--overlap plat-help-body">
			<view class="plat-setting-group" v-for="(item,index) in helpClassifyList" :key="index">
				<view class="plat-setting-card">
					<view class="plat-setting-item plat-help-category" style="padding-bottom: 0rpx;" @click.stop="openList(item)">
						<text class="plat-help-category__title">{{item.name}}</text>
						<image src="/static/image/feedbackIndex/up.png" class="plat-help-category__arrow" v-if="item.parentId==0"></image>
						<image src="/static/image/feedbackIndex/dowm.png" class="plat-help-category__arrow" v-else></image>
					</view>
					<view v-for="(problemItem,problemIndex) in item.articleList" :key="problemIndex"
						class="plat-help-question" hover-class="plat-help-hover" @click="onClick(problemItem)" v-if="item.parentId==0">
						<view class="plat-help-question__text">{{problemItem.title}}</view>
						<up-icon name="arrow-right" color="#64748B" size="16"></up-icon>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot plat-help-foot">
			<view class="plat-help-foot__bar">
				<image src="/my/static/icon-letter.png" class="plat-help-foot__icon"></image>
				<view class="plat-help-foot__btn" hover-class="plat-help-hover" @click="Changekefu">联系客服</view>
				<view class="plat-help-foot__sep"></view>
				<image src="/my/static/icon-edit.png" class="plat-help-foot__icon"></image>
				<view class="plat-help-foot__btn" hover-class="plat-help-hover" @click="toFeedback">我要反馈</view>
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
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	//列表
	const helpClassifyList = reactive([]);
	onLoad(option => {
		getlist();
	});
	const getlist = () => {
		proxy.$http.get('/app/help/category/list').then(res => {
			if (res.code == 0) {
				helpClassifyList.splice(0, helpClassifyList.length, ...res.data);
				helpClassifyList.map(item => {
					item.parentId = 1
				})
			}
		})
	};
	//显示/折叠更多
	const openList = (item) => {
		item.parentId == 1 ? item.parentId = 0 : item.parentId = 1;
	};
	//点击跳转详情
	const onClick = (item) => {
		uni.navigateTo({
			url: '/my/feedbackIndex/helpDetail?title=' + item.title + '&helpWordId=' + item
				.id,
		})
	};
	const Changekefu = () => {
		let SelKeFu = uni.getStorageSync('SelKeFu')
		if (SelKeFu + '' == '1') { //手机号
			uni.makePhoneCall({
				phoneNumber: uni.getStorageSync('kefuPhone')
			});
		} else if (SelKeFu + '' == '2') { //企业微信
			// #ifdef MP-WEIXIN
			wx.openCustomerServiceChat({
				extInfo: {
					url: uni.getStorageSync('kefu')
				},
				corpId: uni.getStorageSync('kefuAppId'),
				success(res) {}
			})
			// #endif
			// #ifdef H5
			window.location.href = uni.getStorageSync('kefu')
			// #endif
			// #ifdef APP
			let kefu = uni.getStorageSync('kefu')
			console.log(kefu)
			plus.runtime.openURL(kefu, function(res) {});
			// #endif
		} else { //客服二维码页面
			uni.navigateTo({
				url: '/my/kefu/kefu'
			})
		}
	};
	//反馈
	const toFeedback = () => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url: '/my/feedback/index'
			});
		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
	};
</script>

<style lang="scss">
</style>
