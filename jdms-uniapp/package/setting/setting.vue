<template>
	<view class="plat-page plat-setting-page">
		<view class="plat-head plat-head--immersive">
			<view class="plat-setting-head">
				<view class="plat-setting-head__title">设置中心</view>
				<view class="plat-setting-head__hint">账号安全与协议须知</view>
			</view>
		</view>

		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-setting-group">
				<view class="plat-setting-card">
					<!-- <view @click="goNav('/package/setting/upPwd')" class="plat-setting-item">
						<text>修改密码</text>
						<up-icon name="arrow-right" color="#64748B" size="18"></up-icon>
					</view> -->
					<view @click="goNav('/package/setting/logOff')" class="plat-setting-item">
						<text>注销账户</text>
						<up-icon name="arrow-right" color="#64748B" size="18"></up-icon>
					</view>
				</view>
			</view>

			<view class="plat-setting-group">
				<view class="plat-setting-group__title">关于与协议</view>
				<view class="plat-setting-card">
					<view @click="goNav('/package/setting/about')" class="plat-setting-item">
						<text>关于我们</text>
						<up-icon name="arrow-right" color="#64748B" size="18"></up-icon>
					</view>
					<view @click="goNav('/package/setting/userAgreement')" class="plat-setting-item">
						<text>用户协议</text>
						<up-icon name="arrow-right" color="#64748B" size="18"></up-icon>
					</view>
					<view @click="goNav('/package/setting/privacyPolicy')" class="plat-setting-item">
						<text>隐私政策</text>
						<up-icon name="arrow-right" color="#64748B" size="18"></up-icon>
					</view>
					<view @click="goNav('/package/setting/cancelAgreement')" class="plat-setting-item">
						<text>用户注销协议</text>
						<up-icon name="arrow-right" color="#64748B" size="18"></up-icon>
					</view>
					<!-- <view @click="goNav('/package/setting/vipAgreement')" class="plat-setting-item">
						<text>会员开通协议</text>
						<up-icon name="arrow-right" color="#64748B" size="18"></up-icon>
					</view>
					<view @click="goNav('/package/setting/Informed')" class="plat-setting-item">
						<text>知情同意书</text>
						<up-icon name="arrow-right" color="#64748B" size="18"></up-icon>
					</view> -->
				</view>
			</view>

			<view class="plat-logout" @click="outLogin()">退出登录</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	//跳转
	const goNav = (url) => {
		uni.navigateTo({
			url: url
		})
	};
	//退出登录
	const outLogin = () => {
		uni.showModal({
			title: '提示',
			content: '确认退出登录吗？',
			complete(ret) {
				if (ret.confirm) {
					// uni.removeStorageSync('token');
					// uni.removeStorageSync('userId');
					// uni.removeStorageSync('inviterCode');
					// uni.removeStorageSync('invitationCode');
					uni.clearStorageSync()
					uni.navigateBack()
				}
			}
		})
	};
</script>

<style lang="scss">
</style>
