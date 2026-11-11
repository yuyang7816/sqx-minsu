<template>
	<view class="plat-page plat-setting-page plat-uppwd-page">
		<view class="plat-head plat-head--immersive">
			<view class="plat-setting-head">
				<view class="plat-setting-head__title">修改密码</view>
				<view class="plat-setting-head__hint">请输入旧密码并设置6位以上新密码</view>
			</view>
		</view>

		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-setting-group">
				<view class="plat-setting-group__title">账号安全</view>
				<view class="plat-setting-card">
					<view class="plat-uppwd-field">
						<text class="plat-uppwd-field__label">旧密码</text>
						<input type="password" v-model="oldPwd" placeholder="请输入旧密码"
							placeholder-class="plat-uppwd-input-ph" maxlength="20" minlength="6"
							data-key="oldpassword" @confirm="toLogin" class="plat-uppwd-field__input" />
					</view>
					<view class="plat-uppwd-field">
						<text class="plat-uppwd-field__label">新密码</text>
						<input type="password" v-model="pwd" placeholder="请设置新密码"
							placeholder-class="plat-uppwd-input-ph" maxlength="20" minlength="6"
							data-key="password" @confirm="toLogin" class="plat-uppwd-field__input" />
					</view>
				</view>
			</view>
			<view class="plat-btn plat-uppwd-btn" @click="toLogin">修改密码</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const oldPwd = ref('');
	const pwd = ref('');
	const toLogin = () => {
		if (!oldPwd.value) {
			uni.showToast({
				title: '请输入旧密码',
				icon: 'none'
			})
			return
		}
		if (oldPwd.value.length < 6) {
			uni.showToast({
				title: '旧密码位数必须大于六位',
				icon: 'none'
			})
			return
		}
		if (!pwd.value) {
			uni.showToast({
				title: '请设置新密码',
				icon: 'none'
			})
			return
		}
		if (pwd.value.length < 6) {
			uni.showToast({
				title: '新密码位数必须大于六位',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '正在修改密码中...'
		})
		proxy.$http.post("/app/user/updatePwd", {
			pwd: pwd.value,
			oldPwd: oldPwd.value,
		}).then(res => {
			uni.hideLoading();
			if (res.code === 0) {
				uni.showToast({
					title: '密码修改成功！下次请使用新密码登录！',
					icon: 'none'
				})
				setTimeout(function() {
					uni.navigateBack()
				}, 1000)
			} else {
				uni.showModal({
					showCancel: false,
					title: '密码修改失败',
					content: res.msg,
				});
			}
		});
	};
</script>

<style lang='scss'>
</style>
