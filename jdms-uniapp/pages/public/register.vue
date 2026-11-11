<template>
	<view class="auth-entry-page auth-entry-page--register">
		<view class="auth-entry-page__glow auth-entry-page__glow--top"></view>
		<view class="auth-entry-page__glow auth-entry-page__glow--bottom"></view>

		<view class="auth-entry-hero auth-entry-hero--compact">
			<image class="auth-entry-hero__logo" src="/static/logo.png" mode="aspectFill"></image>
			<view class="auth-entry-hero__title">注册账号</view>
		</view>

		<view class="auth-entry-panel auth-entry-panel--compact">
			

			<view class="auth-entry-form auth-entry-form--compact">
				<view class="auth-entry-field">
					<view class="auth-entry-field__label">用户名</view>
					<view class="auth-entry-field__control">
						<up-input v-model="userName" border="none" placeholder="请输入用户名"
							placeholder-class="auth-entry-field__placeholder"></up-input>
					</view>
				</view>

				<view class="auth-entry-field">
					<view class="auth-entry-field__label">手机号</view>
					<view class="auth-entry-field__control">
						<up-input v-model="phone" maxlength="11" type="number" border="none" placeholder="请输入手机号"
							placeholder-class="auth-entry-field__placeholder"></up-input>
					</view>
				</view>

				<view class="auth-entry-field">
					<view class="auth-entry-field__label-wrap">
						<view class="auth-entry-field__label">验证码</view>
					</view>
					<view class="auth-entry-field__control">
						<up-input v-model="code" maxlength="20" border="none" type="number" placeholder="请输入验证码"
							placeholder-class="auth-entry-field__placeholder"></up-input>
					</view>
					<view class="auth-entry-field__inline-action" :class="timer ? 'auth-entry-field__inline-action--disabled' : ''" @click="getCode()">{{text}}</view>
				</view>

				<view class="auth-entry-field">
					<view class="auth-entry-field__label">密码</view>
					<view class="auth-entry-field__control">
						<up-input v-model="password" maxlength="20" border="none" type="password" placeholder="请设置登录密码"
							placeholder-class="auth-entry-field__placeholder"></up-input>
					</view>
				</view>

				<view class="auth-entry-field">
					<view class="auth-entry-field__label">邀请码</view>
					<view class="auth-entry-field__control">
						<up-input v-model="invitation" maxlength="6" border="none" placeholder="邀请码（选填）"
							placeholder-class="auth-entry-field__placeholder" @confirm="toLogin"></up-input>
					</view>
				</view>
			</view>

			<view class="auth-entry-submit" @click="toLogin">立即注册</view>

			<view class="auth-entry-switch">
				<text>已有账号？</text>
				<text class="auth-entry-switch__link" @click="goLogin">去登录</text>
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
		onReachBottom
	} from '@dcloudio/uni-app';
	import {
		parseInviteCodeFromOptions,
		saveInviteCode,
		getStoredInviterCode
	} from '@/common/queue.js';
	import {
		getLoginPlatform,
		getSysPhone
	} from '@/common/platform.js';
	import {
		saveUserSession
	} from '@/common/auth.js';
	const {
		proxy
	} = getCurrentInstance();
	// 用户名
	const userName = ref('');
	//手机号
	const phone = ref('');
	//密码
	const password = ref('');
	// 验证码
	const code = ref('');
	// 邀请码
	const invitation = ref('');
	//倒计时
	const countDown = ref(60);
	//倒计时定时器
	const timer = ref(null);
	const text = ref('获取验证码')
	const goLogin = () => {
		uni.navigateTo({
			url: '/pages/public/loginphone'
		});
	};
	//手机号效验
	const validatePhone = (phone) => {
		// 中国手机号码正则表达式
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone);
	};
	//获取验证码
	const getCode = () => {
		if (timer.value) {
			return
		}
		if (phone.value == '') {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		proxy.$http.get('/app/Login/sendMsg/' + phone.value + '/1').then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '验证码发送成功请注意查收',
					icon: 'none'
				})
				timer.value = setInterval(() => {
					countDown.value--;
					if (countDown.value == 0) {
						clearInterval(timer.value)
						timer.value = null;
						countDown.value = 60;
						text.value = '获取验证码'
					} else {
						text.value = countDown.value + 's后重新获取'
					}
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})

	};
	// 找回密码
	const toLogin = () => {
		if (!userName.value) {
			uni.showToast({
				title: '请输入用户名',
				icon: 'none'
			});
			return
		};
		if (!phone.value) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			});
			return
		};

		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		if (!code.value) {
			uni.showToast({
				title: '请输入验证码',
				icon: 'none'
			});
			return
		}
		if (!password.value) {
			uni.showToast({
				title: '请输入密码',
				icon: 'none'
			});
			return
		}
		uni.showLoading({
			title: '正在注册中...'
		})

		// #ifndef MP-WEIXIN
		let platform = getLoginPlatform();
		// #endif
		// #ifdef MP-WEIXIN
		let platform = '小程序';
		// #endif

		proxy.$http.postT('/app/Login/registerCode', {
			userName: userName.value,
			password: password.value,
			phone: phone.value,
			openId: uni.getStorageSync('openId') ? uni.getStorageSync('openId') : uni.getStorageSync('accountOpenId') || '',
			msg: code.value,
			inviterCode: invitation.value,
			platform: platform,
			userType: 1,
			sysPhone: getSysPhone()
		}).then(res => {
			if (res.code == 0) {
				saveUserSession(res);

				uni.showToast({
					title: '注册成功'
				})

				setTimeout(function() {
					uni.switchTab({
						url: '/pages/my/index'
					})
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	const sysphone = ref('');
	onLoad(option => {
		const inviteCode = parseInviteCodeFromOptions(option);
		if (inviteCode) {
			saveInviteCode(inviteCode);
		}
		invitation.value = getStoredInviterCode();
		// #ifdef APP
		if (plus.os.name.toLowerCase() === 'android') {
			sysphone.value = 1;
		} else {
			sysphone.value = 2;
		}
		// #endif
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	.auth-entry-page {
		position: relative;
		min-height: 100vh;
		padding: 72rpx 32rpx 48rpx;
		box-sizing: border-box;
		background:
			radial-gradient(circle at top left, rgba(44, 142, 254, 0.16), transparent 34%),
			linear-gradient(180deg, #f3f7ff 0%, #f7f9fc 46%, #ffffff 100%);
		overflow: hidden;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
	}

	.auth-entry-page--register {
		padding-top: 48rpx;
	}

	.auth-entry-page__glow {
		position: absolute;
		border-radius: 50%;
		pointer-events: none;
		filter: blur(18rpx);
		opacity: 0.78;
	}

	.auth-entry-page__glow--top {
		width: 280rpx;
		height: 280rpx;
		right: -90rpx;
		top: -40rpx;
		background: rgba(72, 161, 255, 0.14);
	}

	.auth-entry-page__glow--bottom {
		width: 240rpx;
		height: 240rpx;
		left: -70rpx;
		bottom: 90rpx;
		background: rgba(29, 92, 206, 0.08);
	}

	.auth-entry-hero,
	.auth-entry-panel {
		position: relative;
		z-index: 1;
		width: 100%;
		max-width: 686rpx;
		margin-left: auto;
		margin-right: auto;
		box-sizing: border-box;
	}

	.auth-entry-hero {
		text-align: center;
		padding: 4rpx 8rpx 0;
	}

	.auth-entry-hero--compact {
		padding-top: 0;
	}

	.auth-entry-hero__logo {
		width: 112rpx;
		height: 112rpx;
		border-radius: 32rpx;
		box-shadow: 0 14rpx 34rpx rgba(30, 92, 206, 0.12);
		background: #ffffff;
		margin: 0 auto 24rpx;
		display: block;
	}

	.auth-entry-hero--compact .auth-entry-hero__logo {
		width: 96rpx;
		height: 96rpx;
		border-radius: 24rpx;
		margin-bottom: 16rpx;
	}

	.auth-entry-hero__eyebrow {
		display: none;
		font-size: 20rpx;
		line-height: 1.4;
		letter-spacing: 2rpx;
		color: #5d7ea8;
	}

	.auth-entry-hero__title {
		margin-top: 14rpx;
		font-size: 40rpx;
		line-height: 1.24;
		font-weight: 700;
		color: #13233e;
	}

	.auth-entry-hero--compact .auth-entry-hero__title {
		margin-top: 10rpx;
		font-size: 38rpx;
	}

	.auth-entry-hero__desc {
		display: none;
	}

	.auth-entry-hero--compact .auth-entry-hero__desc {
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.5;
	}

	.auth-entry-hero__meta {
		display: none;
	}

	.auth-entry-hero__meta--compact {
		margin-top: 18rpx;
		gap: 12rpx;
	}

	.auth-entry-hero__meta-item {
		padding: 12rpx 22rpx;
		border-radius: 999rpx;
		font-size: 22rpx;
		line-height: 1.4;
		color: #4f6d9d;
		background: rgba(255, 255, 255, 0.72);
		box-shadow: 0 8rpx 24rpx rgba(20, 42, 79, 0.06);
	}

	.auth-entry-hero__meta--compact .auth-entry-hero__meta-item {
		padding: 8rpx 16rpx;
		font-size: 20rpx;
	}

	.auth-entry-panel {
		margin-top: 28rpx;
		padding: 24rpx 22rpx 18rpx;
		border-radius: 34rpx;
		background: rgba(255, 255, 255, 0.92);
		box-shadow: 0 18rpx 40rpx rgba(19, 39, 74, 0.06);
		backdrop-filter: blur(8rpx);
	}

	.auth-entry-panel--compact {
		margin-top: 24rpx;
		padding: 24rpx 22rpx 18rpx;
		border-radius: 28rpx;
	}

	.auth-entry-panel__head {
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		gap: 10rpx;
		text-align: center;
	}

	.auth-entry-panel__title {
		font-size: 28rpx;
		line-height: 1.4;
		font-weight: 700;
		color: #172946;
	}

	.auth-entry-panel--compact .auth-entry-panel__title {
		font-size: 30rpx;
	}

	.auth-entry-panel__hint {
		display: none;
	}

	.auth-entry-form {
		margin-top: 18rpx;
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}

	.auth-entry-form--compact {
		margin-top: 18rpx;
		gap: 12rpx;
	}

	.auth-entry-field {
		padding: 18rpx 20rpx;
		border-radius: 22rpx;
		background: #f8fbff;
		border: 1rpx solid rgba(47, 118, 212, 0.1);
	}

	.auth-entry-form--compact .auth-entry-field {
		padding: 18rpx 20rpx;
		border-radius: 22rpx;
	}

	.auth-entry-field__label-wrap {
		display: flex;
		align-items: center;
		justify-content: flex-start;
	}

	.auth-entry-field__label {
		font-size: 24rpx;
		line-height: 1.4;
		font-weight: 600;
		color: #20314f;
	}

	.auth-entry-form--compact .auth-entry-field__label {
		font-size: 24rpx;
	}

	.auth-entry-field__control {
		margin-top: 8rpx;
		padding: 0 2rpx;
	}

	.auth-entry-field__inline-action {
		margin-top: 12rpx;
		font-size: 20rpx;
		line-height: 1.4;
		color: #2c8efe;
	}

	.auth-entry-field__inline-action--disabled {
		color: #9aa8be;
	}

	.auth-entry-submit {
		height: 104rpx;
		line-height: 104rpx;
		text-align: center;
		border-radius: 26rpx;
		margin-top: 22rpx;
		font-size: 30rpx;
		font-weight: 700;
		color: #ffffff;
		background: linear-gradient(135deg, #2f8fff 0%, #1f5dd0 100%);
		box-shadow: 0 16rpx 34rpx rgba(44, 142, 254, 0.22);
	}

	.auth-entry-panel--compact .auth-entry-submit {
		height: 96rpx;
		line-height: 96rpx;
		margin-top: 22rpx;
		font-size: 28rpx;
	}

	.auth-entry-switch {
		margin-top: 18rpx;
		text-align: center;
		font-size: 22rpx;
		line-height: 1.6;
		color: #7b889e;
	}

	.auth-entry-panel--compact .auth-entry-switch {
		margin-top: 18rpx;
		font-size: 22rpx;
		line-height: 1.6;
	}

	.auth-entry-switch__link {
		color: #2c8efe;
		margin-left: 8rpx;
	}

	.auth-entry-field__placeholder {
		color: #9aa8be;
		font-size: 26rpx;
	}

	:deep(.u-input) {
		background: transparent !important;
	}

	:deep(.u-input__content) {
		padding: 0 !important;
	}

	:deep(.u-input__content__field-wrapper__field) {
		font-size: 28rpx;
		color: #13233e;
		text-align: left;
	}
</style>
