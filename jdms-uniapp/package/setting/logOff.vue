<template>
	<view class="plat-page plat-logoff-page">
		<view class="plat-head plat-head--immersive">
			<view class="plat-setting-head">
				<view class="plat-setting-head__title">注销账号</view>
				<view class="plat-setting-head__hint">请仔细阅读以下须知</view>
			</view>
		</view>

		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-card plat-logoff-warn">
				<view class="plat-logoff-warn__icon">
					<u-icon name="info-circle-fill" color="#2C8EFE" size="80"></u-icon>
				</view>
				<view class="plat-logoff-warn__title">注销酒店民宿</view>
				<view class="plat-logoff-warn__desc">在线酒店民宿帐号注销后，您将放弃以下权益且无法找回</view>
			</view>

			<view class="plat-card" style="margin-top: 20rpx; padding: 30rpx;">
				<view class="plat-logoff-item">· 您将无法通过该帐号登录、使用在线酒店民宿平台；</view>
				<view class="plat-logoff-item">· 您将无法访问帐号的个人信息（包括帐号名称、昵称、头像等）；</view>
				<view class="plat-logoff-item">· 您帐号的所有权益，将视为主动放弃</view>
			</view>

			<view class="plat-logoff-agree">
				点击【确认注销】即代表您已同意<text @click="goXieOff()">《用户注销协议》</text>
			</view>
			<view class="plat-btn plat-logoff-btn" @click="submit()">确认注销</view>
		</view>
	</view>
</template>

<script setup>
	import {
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance()
	//去注销协议
	const goXieOff = () => {
		uni.navigateTo({
			url: '/package/setting/cancelAgreement'
		})
	};
	//确认注销
	const submit = () => {
		uni.showModal({
			title: '温馨提示',
			content: '注销后您的账号信息与会员权益将会永久清除且无法恢复，继续注销将在三日内清除完毕，确认继续注销吗？',
			showCancel: true,
			cancelText: '暂不注销',
			confirmText: '继续注销',
			confirmColor: '#2C8EFE',
			success: res => {
				if (res.confirm) {
					uni.showLoading({
						title: '注销中...'
					})
					proxy.$http.postJson('/app/cancelAccount/commit', {
						userName: uni.getStorageSync('userName'),
						title: uni.getStorageSync('phone') ? uni.getStorageSync('phone') : '注销账号',
						content: '我要注销账号！',
						// phone: uni.getStorageSync('phone'),
						userId: uni.getStorageSync('userId')
					}).then(res => {
						if (res.code === 0) {
							uni.showToast({
								title: '注销成功！'
							});
							logout();
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/index/index'
								})
							}, 1000);
						} else {
							uni.hideLoading();
							uni.showModal({
								showCancel: false,
								title: '注销失败',
								content: res.msg
							});
						}
					}).catch(err => {
						uni.hideLoading()
						uni.showToast({
							title: '网络异常，请重试',
							icon: 'none'
						})
					})
				}
			}
		});
	};
	//退出登录
	const logout = () => {
		uni.removeStorageSync('token')
		uni.removeStorageSync('userId')
	};
</script>

<style lang="scss">
</style>
