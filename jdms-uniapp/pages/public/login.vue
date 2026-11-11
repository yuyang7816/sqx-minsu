<template>
	<view class="login-page">
		<view class="login-page__glow login-page__glow--top"></view>
		<view class="login-page__glow login-page__glow--bottom"></view>

		<view class="login-hero">
			<view class="login-hero__brand">
				<image class="login-hero__logo" src="/static/logo.png" mode="aspectFill"></image>
				<view class="login-hero__brand-copy">
					<text class="login-hero__eyebrow">HOTEL HOMESTAY</text>
					<!-- #ifdef MP-WEIXIN -->
					<view class="login-hero__title">欢迎使用酒店民宿系统</view>
					<view class="login-hero__desc">酒店预订、民宿入住、订单管理与会员服务，一站式完成。</view>
					<!-- #endif -->
					<!-- #ifndef MP-WEIXIN -->
					<view class="login-hero__title">欢迎登录酒店民宿系统</view>
					<view class="login-hero__desc">选择一种方式继续。</view>
					<!-- #endif -->
				</view>
			</view>

		</view>

		<view class="login-panel">
			<view class="login-panel__head">
				<view class="login-panel__title">登录方式</view>
				<view class="login-panel__hint">{{ platformHint }}</view>
			</view>

			<view class="login-actions">
				<!-- #ifdef MP-WEIXIN -->
				<button class="login-action login-action--mp" @tap="wxGetUserInfokJ" v-if="!isUserPhone">
					<view class="login-action__mp-head">
						<image class="login-action__wechat-icon" src="/my/static/icon_weixin.png" mode="aspectFit"></image>
						<text class="login-action__mp-label">微信登录</text>
					</view>
					<text class="login-action__mp-desc">授权后自动绑定手机号并同步账号信息</text>
				</button>

				<view v-else>
					<button v-if="!check" class="login-action login-action--mp" @tap="wxPhone">
						<view class="login-action__mp-head">
							<image class="login-action__wechat-icon" src="/my/static/icon_weixin.png" mode="aspectFit"></image>
							<text class="login-action__mp-label">微信登录</text>
						</view>
						<text class="login-action__mp-desc">请先勾选协议后继续</text>
					</button>
					<button v-else class="login-action login-action--mp" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">
						<view class="login-action__mp-head">
							<image class="login-action__wechat-icon" src="/my/static/icon_weixin.png" mode="aspectFit"></image>
							<text class="login-action__mp-label">微信登录</text>
						</view>
						<text class="login-action__mp-desc">授权后自动绑定手机号并同步账号信息</text>
					</button>
				</view>
				<!-- #endif -->

				<!-- #ifndef MP-WEIXIN -->
				<!-- #ifdef H5 -->
				<button v-if="isH5Weixin && weixinLogin" class="login-action login-action--primary" @click="weixinLo">
					<text class="login-action__title">微信登录</text>
				</button>
				<!-- #endif -->

				<!-- #ifdef APP-PLUS -->
				<button v-if="weixinLogin" class="login-action login-action--primary" @click="weixinLo">
					<text class="login-action__title">微信登录</text>
				</button>
				<!-- #endif -->

				<button class="login-action login-action--primary login-action--account" @click="goAccountLogin">
					<text class="login-action__title">账号登录</text>
				</button>
				<button class="login-action login-action--secondary login-action--register" @click="goRegister">
					<text class="login-action__title">注册账号</text>
				</button>
				<!-- #endif -->
			</view>
		</view>

		<view class="login-agree">
			<up-checkbox activeColor="#2C8EFE" shape="circle" label="" name="agree" usedAlone v-model:checked="check">
			</up-checkbox>
			<view @click="check = !check" class="login-agree__text">
				登录即代表同意
				<text @click.stop.prevent="goNav('/package/setting/userAgreement')">《用户服务协议》</text>
				及
				<text @click.stop.prevent="goNav('/package/setting/userAgreement')">《隐私政策》</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		computed,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow
	} from '@dcloudio/uni-app';
	import {
		getStoredInviterCode
	} from '@/common/queue.js';
	import {
		getRuntimePlatform,
		isWeixinBrowser,
		buildWeixinOAuthUrl
	} from '@/common/platform.js';
	import {
		saveUserSession,
		normalizeWxLoginData
	} from '@/common/auth.js';

	const {
		proxy
	} = getCurrentInstance();

	const check = ref(false);
	const isUserPhone = ref(true);
	const sendDataList = ref({});
	const phone = ref('');
	const weixinLogin = ref(false);
	const mpAppId = ref('');

	const isH5Weixin = computed(() => {
		// #ifdef H5
		return isWeixinBrowser();
		// #endif
		return false;
	});

	const platformHint = computed(() => {
		const runtime = getRuntimePlatform();
		if (runtime === 'mp-weixin') return '微信小程序快捷登录';
		if (runtime === 'h5-weixin') return '微信内浏览器 · 支持公众号登录';
		if (runtime === 'app') return 'App · 支持微信与账号登录';
		return 'H5 · 支持账号与验证码登录';
	});

	const ensureAgreed = () => {
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私政策》',
				icon: 'none'
			});
			return false;
		}
		return true;
	};

	const goAccountLogin = () => {
		if (!ensureAgreed()) return;
		uni.navigateTo({
			url: '/pages/public/loginphone'
		});
	};

	const goRegister = () => {
		if (!ensureAgreed()) return;
		uni.navigateTo({
			url: '/pages/public/register'
		});
	};

	const goNav = (url) => {
		uni.navigateTo({
			url
		});
	};

	const rightClick = () => {
		const pages = getCurrentPages();
		if (pages.length > 1) {
			uni.navigateBack();
		} else {
			uni.switchTab({
				url: '/pages/index/index'
			});
		}
	};

	const finishLogin = (res) => {
		saveUserSession(res);
		rightClick();
	};

	const fetchWxLoginConfig = () => {
		proxy.$http.getT('/app/common/type/53').then(res => {
			if (res.code == 0 && res.data && res.data.value === '是') {
				weixinLogin.value = true;
			}
		});
		// #ifdef H5
		if (isWeixinBrowser()) {
			proxy.$http.get('/app/common/type/5').then(res => {
				if (res.code == 0 && res.data && res.data.value) {
					mpAppId.value = res.data.value;
					uni.setStorageSync('wxMpAppId', res.data.value);
				}
			});
		}
		// #endif
	};

	const handleMpWxLogin = (code, autoLogin = false) => {
		proxy.$http.get('/app/Login/wxLogin', {
			code
		}).then(res => {
			if (res.code != 0 || !res.data) return;
			const wxData = normalizeWxLoginData(res.data);
			uni.setStorageSync('openId', wxData.openId);
			uni.setStorageSync('unionId', wxData.unionId);
			uni.setStorageSync('sessionkey', wxData.sessionKey);

			const inviterCode = getStoredInviterCode();
			sendDataList.value = {
				openId: wxData.openId,
				unionId: wxData.unionId,
				userName: '微信用户',
				avatar: '',
				sex: '',
				inviterCode
			};

			if (wxData.hasPhone) {
				isUserPhone.value = false;
				if (autoLogin) {
					loginByMpOpenId(wxData.openId);
				}
			} else {
				isUserPhone.value = true;
			}
		});
	};

	const loginByMpOpenId = (openId) => {
		proxy.$http.postJson('/app/Login/insertWxUser', {
			openId,
			userType: 1
		}).then(res => {
			if (res.code == 0) {
				finishLogin(res);
			} else {
				uni.showModal({
					showCancel: false,
					title: '登录失败',
					content: res.msg
				});
			}
		});
	};

	const wxPhone = () => {
		ensureAgreed();
	};

	const wxGetUserInfokJ = () => {
		if (!ensureAgreed()) return;
		uni.login({
			provider: 'weixin',
			success(loginRes) {
				handleMpWxLogin(loginRes.code, true);
			}
		});
	};

	const getPhoneNumber = (e) => {
		if (e.detail.errMsg === 'getPhoneNumber:fail user deny') return;
		if (e.detail.iv) {
			setPhoneByInsert(e.detail.encryptedData, e.detail.iv);
		}
	};

	const setPhoneByInsert = (decryptData, iv) => {
		proxy.$http.postJson('/app/Login/selectPhone', {
			decryptData,
			key: uni.getStorageSync('sessionkey'),
			iv
		}).then(res => {
			if (res.code == 0) {
				phone.value = res.data.phoneNumber;
				getWeixinInfo(sendDataList.value);
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		});
	};

	const getWeixinInfo = (sendData) => {
		const payload = sendData || sendDataList.value;
		uni.showLoading({
			title: '登录中...'
		});
		proxy.$http.postJson('/app/Login/insertWxUser', {
			openId: payload.openId || uni.getStorageSync('openId'),
			userName: payload.userName || '游客',
			avatar: payload.avatar || '',
			sex: payload.sex || '',
			phone: phone.value,
			inviterCode: payload.inviterCode || getStoredInviterCode(),
			userType: 1
		}).then(res => {
			uni.hideLoading();
			if (res.code == 0) {
				finishLogin(res);
			} else {
				uni.showModal({
					showCancel: false,
					title: '登录失败',
					content: res.msg
				});
			}
		});
	};

	// H5 公众号 / App 微信登录
	const weixinLo = () => {
		if (!ensureAgreed()) return;

		// #ifdef H5
		const appId = mpAppId.value || uni.getStorageSync('wxMpAppId');
		if (!appId) {
			uni.showToast({
				title: '公众号配置未就绪，请稍后重试',
				icon: 'none'
			});
			return;
		}
		const redirectUri = window.location.href.split('?')[0].split('#')[0];
		window.location.href = buildWeixinOAuthUrl(appId, redirectUri, 'login');
		// #endif

		// #ifdef APP-PLUS
		uni.login({
			provider: 'weixin',
			success(loginRes) {
				uni.getUserInfo({
					provider: 'weixin',
					success(infoRes) {
						const auth = loginRes.authResult || {};
						const wxOpenId = auth.openid || auth.openId;
						const token = auth.access_token || auth.accessToken;
						if (!wxOpenId) {
							uni.showToast({
								title: '微信授权失败',
								icon: 'none'
							});
							return;
						}
						proxy.$http.postT('/app/Login/wxAppLogin', {
							wxOpenId,
							token
						}).then(res => {
							if (res.code == 0) {
								finishLogin(res);
							} else if (res.code == -200) {
								uni.setStorageSync('wxOpenId', wxOpenId);
								uni.setStorageSync('wxAccessToken', token);
								uni.navigateTo({
									url: '/pages/public/loginphone?bindWeixin=1'
								});
							} else {
								uni.showToast({
									title: res.msg,
									icon: 'none'
								});
							}
						});
					},
					fail() {
						uni.showToast({
							title: '获取微信用户信息失败',
							icon: 'none'
						});
					}
				});
			},
			fail() {
				uni.showToast({
					title: '微信登录失败，请检查是否安装微信',
					icon: 'none'
				});
			}
		});
		// #endif
	};

	const handleH5OAuthCallback = (code) => {
		if (!code) return;
		proxy.$http.get('/app/Login/getOpenId', {
			code
		}).then(res => {
			if (res.code != 0 || !res.data) return;
			const openId = res.data;
			uni.setStorageSync('accountOpenId', openId);
			proxy.$http.get('/app/Login/wxOpenIdLogin', {
				openId,
				userType: 1
			}).then(loginRes => {
				if (loginRes.code == 0) {
					finishLogin(loginRes);
				} else {
					uni.setStorageSync('openId', openId);
					uni.showToast({
						title: '请使用账号或验证码完成登录绑定',
						icon: 'none'
					});
				}
			});
		});
	};

	onLoad((option) => {
		fetchWxLoginConfig();

		// #ifdef MP-WEIXIN
		uni.login({
			provider: 'weixin',
			success(loginRes) {
				handleMpWxLogin(loginRes.code);
			}
		});
		// #endif

		// #ifdef H5
		try {
			const params = new URLSearchParams(window.location.search);
			const code = params.get('code');
			const state = params.get('state');
			if (code && state === 'login' && isWeixinBrowser()) {
				handleH5OAuthCallback(code);
				const cleanUrl = window.location.href.split('?')[0];
				window.history.replaceState({}, '', cleanUrl);
			}
		} catch (e) {}
		// #endif
	});

	onShow(() => {});
</script>

<style lang="scss" scoped>
	.login-page {
		position: relative;
		min-height: 100vh;
		padding: 72rpx 32rpx 48rpx;
		box-sizing: border-box;
		background:
			radial-gradient(circle at top left, rgba(44, 142, 254, 0.18), transparent 34%),
			linear-gradient(180deg, #f2f7ff 0%, #f7f8fc 42%, #ffffff 100%);
		overflow: hidden;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
	}

	.login-page__glow {
		position: absolute;
		border-radius: 50%;
		filter: blur(18rpx);
		opacity: 0.75;
		pointer-events: none;
	}

	.login-page__glow--top {
		width: 280rpx;
		height: 280rpx;
		right: -70rpx;
		top: -30rpx;
		background: rgba(65, 160, 255, 0.14);
	}

	.login-page__glow--bottom {
		width: 240rpx;
		height: 240rpx;
		left: -80rpx;
		bottom: 120rpx;
		background: rgba(30, 92, 206, 0.08);
	}

	.login-hero,
	.login-panel,
	.login-agree {
		position: relative;
		z-index: 1;
		width: 100%;
		max-width: 686rpx;
		margin-left: auto;
		margin-right: auto;
		box-sizing: border-box;
	}

	.login-hero {
		padding: 4rpx 8rpx 0;
		text-align: center;
		margin-top: 130rpx;
	}

	.login-hero__brand {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		gap: 20rpx;
	}

	.login-hero__logo {
		width: 112rpx;
		height: 112rpx;
		border-radius: 32rpx;
		box-shadow: 0 14rpx 34rpx rgba(30, 92, 206, 0.12);
		background: #ffffff;
		flex-shrink: 0;
	}

	.login-hero__brand-copy {
		min-width: 0;
		text-align: center;
	}

	.login-hero__eyebrow {
		display: block;
		font-size: 20rpx;
		line-height: 1.4;
		letter-spacing: 2rpx;
		color: #5e7fb5;
		margin-bottom: 12rpx;
	}

	.login-hero__title {
		font-size: 40rpx;
		line-height: 1.25;
		font-weight: 700;
		color: #15233b;
	}

	.login-hero__desc {
		margin-top: 14rpx;
		font-size: 24rpx;
		line-height: 1.55;
		color: #5d6b82;
	}

	.login-hero__highlights {
		display: none;
	}

	.login-panel {
		margin-top: 28rpx;
		padding: 24rpx 22rpx 18rpx;
		border-radius: 28rpx;
		background: rgba(255, 255, 255, 0.9);
		box-shadow: 0 18rpx 40rpx rgba(22, 40, 74, 0.06);
		backdrop-filter: blur(8rpx);
	}

	.login-panel__head {
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 12rpx;
		flex-direction: column;
		text-align: center;
	}

	.login-panel__title {
		font-size: 28rpx;
		line-height: 1.4;
		font-weight: 700;
		color: #162845;
	}

	.login-panel__hint {
		font-size: 18rpx;
		line-height: 1.5;
		color: #7e8ca4;
		text-align: center;
	}

	.login-actions {
		margin-top: 18rpx;
		display: flex;
		flex-direction: column;
		gap: 14rpx;
	}

	.login-action {
		width: 100%;
		padding: 0 26rpx;
		height: 96rpx;
		border-radius: 24rpx;
		border: none;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: center;
		text-align: center;
	}

	.login-action__title {
		font-size: 28rpx;
		line-height: 1.4;
		font-weight: 700;
		text-align: center;
	}

	.login-action__desc {
		margin-top: 6rpx;
		font-size: 20rpx;
		line-height: 1.5;
		opacity: 0.92;
		display: none;
	}

	.login-action--primary {
		color: #ffffff;
		background: linear-gradient(135deg, #2f8fff 0%, #1e5cce 100%);
		box-shadow: 0 14rpx 30rpx rgba(44, 142, 254, 0.2);
	}

	.login-action--account {
		border-radius: 26rpx;
		box-shadow: 0 16rpx 34rpx rgba(44, 142, 254, 0.22);
	}

	.login-action--secondary {
		color: #17325e;
		background: #eef4ff;
		border: 1rpx solid rgba(47, 118, 212, 0.22);
		box-shadow: none;
	}

	.login-action--register {
		height: 84rpx;
		border-radius: 22rpx;
	}

	.login-agree {
		margin-top: 34rpx;
		padding: 0 10rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 8rpx;
		max-width: none;
		margin-left: auto;
		margin-right: auto;
		flex-wrap: nowrap;
	}

	.login-agree__text {
		flex: 0 0 auto;
		font-size: 22rpx;
		line-height: 1.4;
		color: #74829a;
		text-align: center;
		white-space: nowrap;
	}

	.login-agree__text text {
		color: #2c8efe;
	}

	button.login-action::after {
		border: none;
	}

	:deep(.up-checkbox) {
		margin-top: 0;
		flex-shrink: 0;
		display: flex;
		align-items: center;
	}

	/* #ifdef MP-WEIXIN */
	.login-page {
		padding: 32rpx 28rpx 48rpx;
		background:
			linear-gradient(180deg, #edf5ff 0%, #f6f9ff 24%, #ffffff 58%, #ffffff 100%);
		display: flex;
		flex-direction: column;
		justify-content: center;
	}

	.login-hero,
	.login-panel,
	.login-agree {
		max-width: none;
	}

	.login-page__glow--top {
		width: 280rpx;
		height: 280rpx;
		right: -96rpx;
		top: 56rpx;
		opacity: 0.5;
	}

	.login-page__glow--bottom {
		width: 240rpx;
		height: 240rpx;
		left: -108rpx;
		bottom: 56rpx;
		opacity: 0.35;
	}

	.login-hero {
		padding: 12rpx 6rpx 0;
		text-align: center;
		margin-top: -280rpx;
	}

	.login-hero__brand {
		flex-direction: column;
		align-items: center;
		justify-content: center;
		gap: 18rpx;
	}

	.login-hero__logo {
		width: 88rpx;
		height: 88rpx;
		border-radius: 24rpx;
		box-shadow: 0 12rpx 26rpx rgba(44, 142, 254, 0.1);
	}

	.login-hero__eyebrow {
		font-size: 16rpx;
		margin-bottom: 8rpx;
		color: #7f9bc8;
	}

	.login-hero__title {
		font-size: 44rpx;
		line-height: 1.24;
	}

	.login-hero__desc {
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #64748b;
		text-align: center;
	}

	.login-hero__highlights {
		display: none;
	}

	.login-hero__highlight {
		padding: 18rpx 16rpx 20rpx;
		border-radius: 22rpx;
		background: rgba(255, 255, 255, 0.9);
		box-shadow: 0 10rpx 24rpx rgba(22, 40, 74, 0.04);
	}

	.login-hero__highlight-num {
		font-size: 16rpx;
	}

	.login-hero__highlight-text {
		margin-top: 8rpx;
		font-size: 22rpx;
		line-height: 1.45;
	}

	.login-panel {
		margin-top: 34rpx;
		padding: 24rpx 0 12rpx;
		border-radius: 0;
		background: transparent;
		box-shadow: none;
		width: 100%;
	}

	.login-panel__head {
		display: none;
	}

	.login-panel__title {
		font-size: 30rpx;
	}

	.login-panel__hint {
		font-size: 20rpx;
		line-height: 1.5;
		color: #94a3b8;
	}

	.login-actions {
		margin-top: 0;
		gap: 8rpx;
		align-items: center;
	}

	.login-action {
		height: 112rpx;
		padding: 0 26rpx;
		border-radius: 24rpx;
	}

	.login-action__title {
		font-size: 28rpx;
	}

	.login-action__desc {
		margin-top: 6rpx;
		font-size: 20rpx;
	}

	.login-action--primary {
		background: linear-gradient(135deg, #3d97ff 0%, #2c7df3 100%);
		box-shadow: 0 14rpx 30rpx rgba(44, 142, 254, 0.2);
	}

	.login-action--mp {
		height: auto;
		min-height: 96rpx;
		padding: 10rpx 0 8rpx;
		border-radius: 0;
		background: transparent;
		box-shadow: none;
		border: none;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		text-align: center;
		width: 100%;
	}

	.login-action__mp-head {
		display: inline-flex;
		align-items: center;
		justify-content: center;
		gap: 12rpx;
	}

	.login-action__wechat-icon {
		width: 34rpx;
		height: 34rpx;
		display: block;
	}

	.login-action__mp-label {
		font-size: 30rpx;
		line-height: 1.35;
		font-weight: 700;
		color: #15233b;
	}

	.login-action__mp-desc {
		margin-top: 8rpx;
		font-size: 18rpx;
		line-height: 1.5;
		color: #7b8799;
		text-align: center;
	}

	.login-action--secondary {
		background: #f8fbff;
		border: 1rpx solid rgba(44, 142, 254, 0.12);
	}

	.login-panel__tips {
		display: none;
	}

	.login-panel__tip {
		padding: 10rpx 16rpx;
		font-size: 20rpx;
		color: #6b84ad;
		background: #f1f6ff;
	}

	.login-agree {
		margin-top: 24rpx;
		padding: 0 4rpx;
		justify-content: center;
		gap: 8rpx;
		max-width: none;
		margin-left: auto;
		margin-right: auto;
		align-items: center;
		flex-wrap: nowrap;
	}

	.login-agree__text {
		flex: 0 0 auto;
		font-size: 18rpx;
		line-height: 1.4;
		color: #7b8799;
		text-align: center;
		white-space: nowrap;
	}

	:deep(.up-checkbox) {
		margin-top: 0;
		display: flex;
		align-items: center;
	}
	/* #endif */
</style>
