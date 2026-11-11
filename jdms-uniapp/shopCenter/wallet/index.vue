<template>
	<view class="wallet-container">
		<!-- 顶部余额区 -->
		<view class="wallet-header">
			<view class="wallet-header-bg">
				<view class="wallet-header-inner">
					<view class="wallet-balance-label">收益余额（元）</view>
					<view class="wallet-balance-row">
						<text class="wallet-balance-unit">￥</text>
						<text class="wallet-balance-num">{{ shopBalance || '0.00' }}</text>
					</view>
					<view class="wallet-balance-tip">可提现至微信或支付宝账户</view>
				</view>
			</view>
		</view>

		<!-- 主体内容区 -->
		<view class="wallet-body">
			<!-- 提现金额 -->
			<view class="wallet-panel">
				<view class="wallet-panel-title">提现金额</view>
				<view class="wallet-amount-input">
					<text class="wallet-amount-symbol">¥</text>
					<input
						class="wallet-amount-field"
						type="digit"
						v-model="moneyInfo.money"
						placeholder="请输入提现金额"
						placeholder-style="color:#BCC3CE;"
					/>
				</view>
				<view class="wallet-amount-hint">{{ placeholder }}</view>
			</view>

			<!-- 提现方式 -->
			<view class="wallet-section">
				<view class="wallet-section-title">提现方式</view>
				<view class="wallet-panel wallet-panel--compact">
					<view
						class="wallet-pay-item"
						:class="{ 'wallet-pay-item--checked': opentype == 2 }"
						@click.stop="selCashWay(2)"
						v-if="isWxOpen !== '0'"
					>
						<view class="wallet-pay-left">
							<view class="wallet-pay-icon wallet-pay-icon--wechat">
								<image src="/my/static/icon_weixin.png" mode="aspectFit"></image>
							</view>
							<view class="wallet-pay-info">
								<view class="wallet-pay-name">微信提现</view>
								<view class="wallet-pay-status">{{ wxImg ? '已上传收款码' : '需上传微信收款码' }}</view>
							</view>
						</view>
						<view class="wallet-pay-right" @click.stop="show = true">
							<text class="wallet-pay-action">{{ wxImg ? '更换' : '上传' }}</text>
							<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
						</view>
					</view>

					<view class="wallet-pay-divider" v-if="isWxOpen !== '0' && isZfbOpen !== '0'"></view>

					<view
						class="wallet-pay-item"
						:class="{ 'wallet-pay-item--checked': opentype == 1 }"
						@click.stop="selCashWay(1)"
						v-if="isZfbOpen !== '0'"
					>
						<view class="wallet-pay-left">
							<view class="wallet-pay-icon wallet-pay-icon--alipay">
								<image src="/my/static/zhifubao.png" mode="aspectFit"></image>
							</view>
							<view class="wallet-pay-info">
								<view class="wallet-pay-name">支付宝提现</view>
								<view class="wallet-pay-status">{{ zhiFuBaoName ? zhiFuBaoName : '需绑定支付宝账号' }}</view>
							</view>
						</view>
						<view class="wallet-pay-right" @click.stop="goNav('/my/wallet/zhifubao')">
							<text class="wallet-pay-action">{{ zhiFuBao ? '更换' : '绑定' }}</text>
							<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
						</view>
					</view>
				</view>
			</view>

			<!-- 账单记录 -->
			<view class="wallet-section">
				<view class="wallet-section-title">账单记录</view>
				<view class="wallet-panel wallet-panel--compact">
					<view class="wallet-record-item" @click="goNav('/shopCenter/wallet/moneyList?userType=2&shopId=' + shopId)">
						<view class="wallet-record-label">
							<view class="wallet-record-dot wallet-record-dot--blue"></view>
							<text>资金明细</text>
						</view>
						<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
					</view>
					<view class="wallet-record-divider"></view>
					<view class="wallet-record-item" @click="goNav('/shopCenter/wallet/moneyList?userType=1&shopId=' + shopId)">
						<view class="wallet-record-label">
							<view class="wallet-record-dot wallet-record-dot--green"></view>
							<text>提现明细</text>
						</view>
						<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部按钮 -->
		<view class="wallet-footer">
			<view class="wallet-submit" @click="cashMoney">立即提现</view>
		</view>

		<!-- 微信收款码弹窗 -->
		<u-popup :show="show" mode="center" round="16" @close="show = false">
			<view class="wallet-popup">
				<view class="wallet-popup-head">
					<view class="wallet-popup-title">添加微信收款码</view>
					<view class="wallet-popup-close" @click="show = false">×</view>
				</view>
				<view class="wallet-popup-body" @click.stop="weixin">
					<image :src="wxImg ? wxImg : erweima" mode="aspectFit" class="wallet-popup-img"></image>
					<view class="wallet-popup-hint">点击上传或更换收款码</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow
	} from '@dcloudio/uni-app';
	import configdata from '@/common/config.js';

	const {
		proxy
	} = getCurrentInstance();

	const erweima = ref('https://xianyu.xianmaxiong.com/file/uploadPath/2024/11/14/f537b096d4357ccb68437ee8b8b56cae.png');
	const show = ref(false);
	const shopBalance = ref(0);
	const contentgz = ref('');
	const zhiFuBao = ref('');
	const zhiFuBaoName = ref('');
	const isWxOpen = ref('');
	const isZfbOpen = ref('');
	const wxImg = ref('');
	const moneyInfo = ref({
		money: '',
		changeFee: 0
	});
	const opentype = ref(2);
	const shopId = ref(uni.getStorageSync('shopId'));
	const charge = ref(0);
	const minMoney = ref(0);
	const maxMoney = ref(0);
	const placeholder = ref('');
	const hasWechatCashQrCode = () => !!String(wxImg.value || '').trim();

	const selCashWay = (type) => {
		opentype.value = type;
	};

	const cashMoney = () => {
		if (!moneyInfo.value.money) {
			uni.showToast({
				title: '请输入提现金额',
				icon: 'none'
			})
			return
		}
		if (!zhiFuBao.value && !zhiFuBaoName.value && opentype.value == 1) {
			uni.showModal({
				title: '提示',
				content: '请先绑定支付宝提现账号',
				confirmText: '前往',
				success: function(res) {
					if (res.confirm) {
						uni.navigateTo({
							url: '/my/wallet/zhifubao'
						})
					}
				}
			})
			return
		}
		if (!hasWechatCashQrCode() && opentype.value == 2) {
			uni.showToast({
				title: '请上传微信提现二维码',
				icon: 'none'
			})
			return
		}

		proxy.$http.getT('/app/cash/cashMoney', {
			money: moneyInfo.value.money,
			classify: opentype.value,
			userType: 2,
			shopId: uni.getStorageSync('shopId')
		}).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
				setTimeout(() => {
					moneyInfo.value.money = ''
					getUserInfo()
					getShopInfo()
				}, 500)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const getCharge = () => {
		proxy.$http.get("/app/common/type/114").then(res => {
			if (res.code == 0) {
				charge.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};

	const getMinMoney = () => {
		proxy.$http.get("/app/common/type/87").then(res => {
			if (res.code == 0) {
				minMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};

	const getMaxMoney = () => {
		proxy.$http.get("/app/common/type/11").then(res => {
			if (res.code == 0) {
				maxMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};

	const goNav = (url) => {
		uni.navigateTo({
			url
		});
	};

	const getUserInfo = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		};
		proxy.$http.getT('/app/user/selectUserById', data).then(res => {
			if (res.code == 0) {
				wxImg.value = res.data.cashQrCode;
				zhiFuBao.value = res.data.zhiFuBao;
				zhiFuBaoName.value = res.data.zhiFuBaoName;
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		});
	};

	const getShopInfo = () => {
		uni.showLoading({
			title: '加载中...'
		});
		let data = {
			shopId: uni.getStorageSync('shopId')
		}
		proxy.$http.get('/app/shops/getShopById', data).then(res => {
			if (res.code == 0) {
				shopBalance.value = res.data.cashMoney
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				})
			}
		})
		uni.hideLoading();
	};

	const weixin = () => {
		uni.chooseImage({
			count: 1,
			sizeType: ['original', 'compressed'],
			sourceType: ['album', 'camera'],
			success: (res) => {
				uni.showLoading({
					title: "上传中..."
				});
				for (let i = 0; i < 1; i++) {
					uni.uploadFile({
						url: config("UPLOADAPI") + "/alioss/upload",
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							let data = {
								cashQrCode: JSON.parse(uploadFileRes.data).data
							};
							proxy.$http.postJson('/app/user/updateUser', data).then(res => {
								if (res.code == 0) {
									setTimeout(function() {
										uni.showToast({
											title: '上传成功',
											icon: 'none'
										});
										getUserInfo();
									}, 1000);
									show.value = false;
								}
							});
							uni.hideLoading();
						}
					});
				}
			}
		});
	};

	const config = (name) => {
		var info = null;
		if (name) {
			var name2 = name.split(".");
			if (name2.length > 1) {
				info = configdata[name2[0]][name2[1]] || null;
			} else {
				info = configdata[name] || null;
			}
			if (info == null) {
				let web_config = cache.get("web_config");
				if (web_config) {
					if (name2.length > 1) {
						info = web_config[name2[0]][name2[1]] || null;
					} else {
						info = web_config[name] || null;
					}
				}
			}
		}
		return info;
	};

	onLoad(() => {
		getCharge();
		getMinMoney();
		getMaxMoney();
		getShopInfo();
		getUserInfo();

		proxy.$http.get('/app/common/type/243').then(res => {
			if (res.data && res.data.value) {
				isWxOpen.value = res.data.value;
			}
		});
		proxy.$http.get('/app/common/type/245').then(res => {
			if (res.data && res.data.value) {
				isZfbOpen.value = res.data.value;
			}
		});
		proxy.$http.getT('/app/common/type/951').then(res => {
			if (res.code == 0) {
				if (res.data && res.data.value) {
					contentgz.value = res.data.value;
				}
			}
		});
	});

	onShow(() => {
		getShopInfo();
		getUserInfo();
	});
</script>

<style lang="scss" scoped>
	page {
		background-color: #F5F7FA;
	}

	.wallet-container {
		min-height: 100vh;
		padding-bottom: calc(160rpx + env(safe-area-inset-bottom));
		box-sizing: border-box;
	}

	.wallet-header {
		padding: 24rpx 24rpx 52rpx;
	}

	.wallet-header-bg {
		background: linear-gradient(135deg, #2B6FE8 0%, #4B9DFF 100%);
		border-radius: 24rpx;
		padding: 30rpx 26rpx 32rpx;
		box-shadow: 0 10rpx 24rpx rgba(43, 111, 232, 0.14);
		position: relative;
		overflow: hidden;

		&::after {
			content: '';
			position: absolute;
			width: 300rpx;
			height: 300rpx;
			background: radial-gradient(circle, rgba(255, 255, 255, 0.12) 0%, transparent 70%);
			top: -100rpx;
			right: -60rpx;
			border-radius: 50%;
		}
	}

	.wallet-header-inner {
		position: relative;
		z-index: 1;
	}

	.wallet-balance-label {
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.85);
		line-height: 1;
	}

	.wallet-balance-row {
		display: flex;
		align-items: baseline;
		margin-top: 18rpx;
		margin-bottom: 12rpx;
	}

	.wallet-balance-unit {
		font-size: 28rpx;
		font-weight: 700;
		color: #FFFFFF;
		margin-right: 6rpx;
	}

	.wallet-balance-num {
		font-size: 56rpx;
		font-weight: 800;
		line-height: 1;
		color: #FFFFFF;
		letter-spacing: 0;
	}

	.wallet-balance-tip {
		font-size: 22rpx;
		color: rgba(255, 255, 255, 0.72);
		line-height: 1.4;
	}

	.wallet-body {
		padding: 0 24rpx;
		margin-top: -18rpx;
		position: relative;
		z-index: 2;
	}

	.wallet-section {
		margin-top: 20rpx;
	}

	.wallet-section-title {
		font-size: 26rpx;
		font-weight: 700;
		color: #1E2A3A;
		margin-bottom: 14rpx;
		padding-left: 6rpx;
	}

	.wallet-panel {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 26rpx 24rpx;
		box-shadow: 0 6rpx 18rpx rgba(15, 23, 42, 0.04);
	}

	.wallet-panel--compact {
		padding: 10rpx 18rpx;
	}

	.wallet-panel-title {
		font-size: 28rpx;
		font-weight: 700;
		color: #1E2A3A;
		margin-bottom: 18rpx;
	}

	.wallet-amount-input {
		display: flex;
		align-items: center;
		padding: 14rpx 0 16rpx;
		border-bottom: 2rpx solid #F0F2F5;
	}

	.wallet-amount-symbol {
		font-size: 36rpx;
		font-weight: 700;
		color: #1E2A3A;
		margin-right: 12rpx;
	}

	.wallet-amount-field {
		flex: 1;
		font-size: 34rpx;
		font-weight: 700;
		color: #1E2A3A;
		height: 64rpx;
	}

	.wallet-amount-field::placeholder {
		font-size: 34rpx;
		font-weight: 600;
		color: #c2cad8;
	}

	.wallet-amount-hint {
		margin-top: 16rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: #8B94A5;
	}

	.wallet-pay-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 18rpx 14rpx;
		border-radius: 18rpx;
		border: 1rpx solid transparent;
		transition: background 0.2s, border-color 0.2s;
	}

	.wallet-pay-item--checked {
		background: #eef4ff;
		border-color: #cfe0ff;
	}

	.wallet-pay-left {
		display: flex;
		align-items: center;
		gap: 16rpx;
		flex: 1;
		min-width: 0;
	}

	.wallet-pay-icon {
		width: 58rpx;
		height: 58rpx;
		border-radius: 50%;
		overflow: hidden;
		flex-shrink: 0;
		background: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);

		image {
			width: 38rpx;
			height: 38rpx;
		}
	}

	.wallet-pay-icon--wechat {
		background: #E8F6EB;
	}

	.wallet-pay-icon--alipay {
		background: #E8F4FD;
	}

	.wallet-pay-info {
		flex: 1;
		min-width: 0;
	}

	.wallet-pay-name {
		font-size: 28rpx;
		font-weight: 600;
		color: #1E2A3A;
		line-height: 1.3;
	}

	.wallet-pay-status {
		margin-top: 4rpx;
		font-size: 22rpx;
		color: #8B94A5;
		line-height: 1.3;
	}

	.wallet-pay-right {
		display: flex;
		align-items: center;
		gap: 6rpx;
		flex-shrink: 0;
		margin-left: 16rpx;
	}

	.wallet-pay-action {
		font-size: 24rpx;
		color: #2B6FE8;
		font-weight: 600;
	}

	.wallet-pay-divider {
		height: 2rpx;
		background: #F0F2F5;
		margin: 0 14rpx;
	}

	.wallet-record-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 24rpx 14rpx;
		border-radius: 18rpx;
	}

	.wallet-record-item:active {
		background: #f8fafc;
	}

	.wallet-record-label {
		display: flex;
		align-items: center;
		gap: 14rpx;
		font-size: 28rpx;
		color: #1E2A3A;
		font-weight: 500;
	}

	.wallet-record-dot {
		width: 12rpx;
		height: 12rpx;
		border-radius: 50%;
	}

	.wallet-record-dot--blue {
		background: #2B6FE8;
	}

	.wallet-record-dot--green {
		background: #22C55E;
	}

	.wallet-record-divider {
		height: 2rpx;
		background: #F0F2F5;
		margin: 0 14rpx;
	}

	.wallet-footer {
		position: fixed;
		left: 0;
		bottom: 0;
		width: 100%;
		padding: 16rpx 24rpx calc(16rpx + env(safe-area-inset-bottom));
		background: #FFFFFF;
		box-shadow: 0 -4rpx 18rpx rgba(15, 23, 42, 0.05);
		box-sizing: border-box;
		z-index: 10;
	}

	.wallet-submit {
		width: 100%;
		height: 84rpx;
		border-radius: 42rpx;
		background: linear-gradient(135deg, #2B6FE8 0%, #4B9DFF 100%);
		color: #FFFFFF;
		font-size: 30rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 18rpx rgba(43, 111, 232, 0.18);
	}

	.wallet-popup {
		width: 620rpx;
		background: #FFFFFF;
		border-radius: 18rpx;
		overflow: hidden;
	}

	.wallet-popup-head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx;
		border-bottom: 2rpx solid #F0F2F5;
	}

	.wallet-popup-title {
		font-size: 32rpx;
		font-weight: 700;
		color: #1E2A3A;
	}

	.wallet-popup-close {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 40rpx;
		color: #8B94A5;
	}

	.wallet-popup-body {
		padding: 40rpx 30rpx 48rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.wallet-popup-img {
		width: 360rpx;
		height: 360rpx;
		border-radius: 16rpx;
		background: #F5F7FA;
	}

	.wallet-popup-hint {
		margin-top: 24rpx;
		font-size: 24rpx;
		color: #8B94A5;
	}
</style>
