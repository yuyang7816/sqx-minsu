<template>
	<view class="acct-page" v-if="XCXIsSelect != '否'">
		<!-- 顶部品牌区 -->
		<view class="acct-hero">
			<view class="acct-hero__icon">
				<view class="acct-hero__icon-text">支</view>
			</view>
			<view class="acct-hero__info">
				<view class="acct-hero__title">提现账号</view>
				<view class="acct-hero__desc">绑定支付宝收款账户，用于提现到账</view>
			</view>
		</view>

		<!-- 提示横幅 -->
		<view class="acct-banner">
			<view class="acct-banner__dot"></view>
			<view class="acct-banner__text">账户信息将用于提现打款，请确认姓名与收款账户真实有效</view>
		</view>

		<!-- 表单卡片 -->
		<view class="acct-card">
			<view class="acct-field">
				<view class="acct-field__label">收款人姓名</view>
				<view class="acct-field__input-wrap">
					<up-input placeholder="请输入支付宝收款人姓名" border="none" type="text" v-model="zhiFuBaoName"
						placeholder-class="acct-field__ph"></up-input>
				</view>
			</view>
			<view class="acct-field">
				<view class="acct-field__label">支付宝账号</view>
				<view class="acct-field__input-wrap">
					<up-input placeholder="请输入要绑定的支付宝手机号" border="none" type="text" v-model="zhiFuBao"
						placeholder-class="acct-field__ph"></up-input>
				</view>
			</view>
		</view>

		<!-- 底部提示 -->
		<view class="acct-tip">
			<view class="acct-tip__icon">!</view>
			<view class="acct-tip__text">请正确填写收款人的支付宝账户和真实的收款人姓名，否则将无法正常收款</view>
		</view>

		<!-- 底部按钮 -->
		<view class="acct-foot">
			<view class="acct-btn" @click="toLogin()" v-if="!cashOutId">绑定账户</view>
			<view class="acct-btn" @click="updateMag()" v-if="cashOutId">修改绑定账户</view>
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
	const XCXIsSelect = ref('否');
	const zhiFuBao = ref('');
	const zhiFuBaoName = ref('');
	const cashOutId = ref('');
	onLoad(option => {
		XCXIsSelect.value = uni.getStorageSync('XCXIsSelect');
		if (XCXIsSelect.value == '否') {
			uni.setNavigationBarTitle({
				title: '隐私协议'
			})
		}
		getUserInfo();
	});
	// 获取提现账号详情
	const getUserInfo = () => {
		proxy.$http.getT("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				if (res.data.zhiFuBao) {
					zhiFuBao.value = res.data.zhiFuBao;
				}
				if (res.data.zhiFuBaoName) {
					zhiFuBaoName.value = res.data.zhiFuBaoName;
				}
			}
		});
	};
	// 修改账户信息
	const updateMag = () => {
		if (!zhiFuBaoName.value) {
			uni.showToast({
				title: '请输入收款人姓名',
				icon: 'none'
			})
			return
		}
		if (!zhiFuBao.value) {
			uni.showToast({
				title: '请输入支付宝账号',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '提交中...'
		})
		let data = {
			zhiFuBao: zhiFuBao.value,
			userId: uni.getStorageSync('userId'),
			zhiFuBaoName: zhiFuBaoName.value
		}
		proxy.$http.postJson('/app/user/updateUser', data).then(res => {
			if (res.code === 200) {
				uni.hideLoading();
				uni.showToast({
					title: '已提交',
					icon: 'none',
					complete() {
						setTimeout(function() {
							uni.navigateBack();
						}, 1000)
					}
				})
			} else {
				uni.hideLoading();
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	const toLogin = () => {
		if (!zhiFuBaoName.value) {
			uni.showToast({
				title: '请输入收款人姓名',
				icon: 'none'
			})
			return
		}
		if (!zhiFuBao.value) {
			uni.showToast({
				title: '请输入支付宝账号',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '提交中...'
		})
		let data = {
			zhiFuBao: zhiFuBao.value,
			userId: uni.getStorageSync('userId'),
			zhiFuBaoName: zhiFuBaoName.value
		}
		proxy.$http.postJson('/app/user/updateUser', data).then(res => {
			if (res.code === 0) {
				uni.hideLoading();
				uni.showToast({
					title: '已提交',
					icon: 'none',
					complete() {
						setTimeout(function() {
							uni.navigateBack();
						}, 1000)
					}
				})
			} else {
				uni.hideLoading();
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
</script>

<style lang='scss' scoped>
	$brand: #2C8EFE;

	.acct-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #EAF3FF 0%, #F5F8FC 320rpx, #F5F8FC 100%);
		padding: 32rpx 28rpx 220rpx;
		box-sizing: border-box;
	}

	/* 顶部品牌区 */
	.acct-hero {
		display: flex;
		align-items: center;
	}

	.acct-hero__icon {
		width: 96rpx;
		height: 96rpx;
		border-radius: 28rpx;
		background: linear-gradient(135deg, $brand 0%, #5AA7FE 100%);
		box-shadow: 0 12rpx 28rpx rgba(44, 142, 254, 0.30);
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.acct-hero__icon-text {
		font-size: 44rpx;
		font-weight: 700;
		color: #FFFFFF;
	}

	.acct-hero__info {
		margin-left: 24rpx;
	}

	.acct-hero__title {
		font-size: 40rpx;
		font-weight: 700;
		color: #1A2433;
		letter-spacing: 1rpx;
	}

	.acct-hero__desc {
		margin-top: 8rpx;
		font-size: 24rpx;
		color: #7A8AA0;
	}

	/* 提示横幅 */
	.acct-banner {
		margin-top: 32rpx;
		display: flex;
		align-items: center;
		padding: 22rpx 26rpx;
		border-radius: 20rpx;
		background: rgba(44, 142, 254, 0.08);
	}

	.acct-banner__dot {
		width: 12rpx;
		height: 12rpx;
		border-radius: 50%;
		background: $brand;
		flex-shrink: 0;
		margin-right: 16rpx;
	}

	.acct-banner__text {
		font-size: 24rpx;
		line-height: 1.6;
		color: #3D6DB4;
	}

	/* 表单卡片 */
	.acct-card {
		margin-top: 32rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 12rpx 30rpx;
		box-shadow: 0 8rpx 32rpx rgba(30, 80, 160, 0.06);
	}

	.acct-field {
		padding: 28rpx 0;
	}

	.acct-field + .acct-field {
		border-top: 1rpx solid #F0F3F8;
	}

	.acct-field__label {
		font-size: 26rpx;
		font-weight: 600;
		color: #1A2433;
		margin-bottom: 18rpx;
		display: flex;
		align-items: center;

		&::before {
			content: '';
			width: 8rpx;
			height: 26rpx;
			border-radius: 4rpx;
			background: $brand;
			margin-right: 12rpx;
		}
	}

	.acct-field__input-wrap {
		display: flex;
		align-items: center;
		min-height: 92rpx;
		padding: 0 26rpx;
		border-radius: 16rpx;
		background: #F6F9FD;
		border: 2rpx solid transparent;
		transition: all 0.2s;
	}

	:deep(.acct-field__input-wrap .u-input) {
		width: 100%;
	}

	:deep(.acct-field__input-wrap .u-input__content) {
		min-height: 92rpx;
	}

	:deep(.acct-field__input-wrap input) {
		font-size: 28rpx;
		color: #1A2433;
	}

	:deep(.acct-field__ph),
	:deep(.acct-field__input-wrap input::placeholder) {
		font-size: 26rpx;
		color: #A5B2C4;
	}

	/* 底部提示 */
	.acct-tip {
		margin-top: 28rpx;
		display: flex;
		align-items: flex-start;
		padding: 0 6rpx;
	}

	.acct-tip__icon {
		width: 30rpx;
		height: 30rpx;
		border-radius: 50%;
		background: #FFB020;
		color: #FFFFFF;
		font-size: 20rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
		margin-right: 14rpx;
		margin-top: 4rpx;
	}

	.acct-tip__text {
		font-size: 23rpx;
		line-height: 1.7;
		color: #94A3B8;
	}

	/* 底部按钮 */
	.acct-foot {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		padding: 20rpx 28rpx calc(20rpx + constant(safe-area-inset-bottom));
		padding: 20rpx 28rpx calc(20rpx + env(safe-area-inset-bottom));
		background: linear-gradient(180deg, rgba(245, 248, 252, 0) 0%, #F5F8FC 40%);
	}

	.acct-btn {
		height: 96rpx;
		border-radius: 48rpx;
		background: linear-gradient(90deg, $brand 0%, #5AA7FE 100%);
		box-shadow: 0 12rpx 28rpx rgba(44, 142, 254, 0.32);
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		font-weight: 600;
		color: #FFFFFF;
		letter-spacing: 2rpx;

		&:active {
			opacity: 0.88;
			transform: scale(0.98);
		}
	}
</style>