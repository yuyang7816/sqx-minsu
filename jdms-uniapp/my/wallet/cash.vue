<template>
	<view class="wallet-page wallet-page--cash">
		<view class="wallet-header">
			<view class="wallet-header-bg">
				<view class="wallet-header-inner">
					<view class="wallet-balance-label">可提现金额（元）</view>
					<view class="wallet-balance-row">
						<text class="wallet-balance-unit">¥</text>
						<text class="wallet-balance-num">{{userInfo.canCashMoney || '0.00'}}</text>
					</view>
					<view class="wallet-balance-tip">提现申请提交后，将按配置规则进行处理</view>
				</view>
			</view>
		</view>

		<view class="wallet-body">
			<view class="wallet-panel">
				<view class="wallet-panel-title">提现金额</view>
				<view class="wallet-amount-input">
					<text class="wallet-amount-symbol">¥</text>
					<input class="wallet-amount-field" type="digit" v-model="money" placeholder="请输入提现金额"
						placeholder-style="color:#BCC3CE;" />
				</view>
				<view class="wallet-amount-hint">{{placeholder}}</view>
			</view>

			<view class="wallet-section">
				<view class="wallet-section-title">提现方式</view>
				<view class="wallet-panel wallet-panel--compact">
					<view class="wallet-pay-item" :class="{ 'wallet-pay-item--checked': cashType == item.id }"
						v-for="item,index in payList" :key="index" @click="changeCashType(item)">
						<view class="wallet-pay-left">
							<view class="wallet-pay-icon" :class="item.id == 2 ? 'wallet-pay-icon--wechat' : 'wallet-pay-icon--alipay'">
								<image :src="item.image" mode="aspectFit"></image>
							</view>
							<view class="wallet-pay-info">
								<view class="wallet-pay-name">{{ item.id == 2 ? '微信提现' : '支付宝提现' }}</view>
								<view class="wallet-pay-status">{{ item.id == 2 ? (wxImg ? '已上传微信收款码' : '需上传微信收款码') : (userInfo.zhiFuBaoName ? userInfo.zhiFuBaoName : '需绑定支付宝账号') }}</view>
							</view>
						</view>
						<view class="wallet-pay-right" @click.stop="item.id == 2 ? wxShow = true : routerGo('/my/wallet/zhifubao')">
							<text class="wallet-pay-action">{{ item.id == 2 ? (wxImg ? '更换' : '上传') : (userInfo.zhiFuBao ? '更换' : '绑定') }}</text>
							<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
						</view>
					</view>
				</view>
			</view>

			<view class="wallet-section">
				<view class="wallet-section-title">相关记录</view>
				<view class="wallet-panel wallet-panel--compact">
					<view class="wallet-record-item" @click="routerGo('/my/wallet/zhifubao')">
						<view class="wallet-record-label">
							<view class="wallet-record-dot wallet-record-dot--blue"></view>
							<text>提现账号</text>
						</view>
						<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
					</view>
					<view class="wallet-record-divider"></view>
					<view class="wallet-record-item" @click="wxShow = true">
						<view class="wallet-record-label">
							<view class="wallet-record-dot wallet-record-dot--green"></view>
							<text>微信收款码</text>
						</view>
						<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
					</view>
					<view class="wallet-record-divider"></view>
					<view class="wallet-record-item" @click="routerGo('/my/wallet/cashList')">
						<view class="wallet-record-label">
							<view class="wallet-record-dot wallet-record-dot--orange"></view>
							<text>提现明细</text>
						</view>
						<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
					</view>
					<view class="wallet-record-divider"></view>
					<view class="wallet-record-item" @click="routerGo('/my/wallet/moneyList')">
						<view class="wallet-record-label">
							<view class="wallet-record-dot wallet-record-dot--purple"></view>
							<text>资金明细</text>
						</view>
						<up-icon name="arrow-right" color="#9AA3B0" size="12"></up-icon>
					</view>
				</view>
			</view>
		</view>

		<view class="wallet-footer">
			<view class="wallet-submit" @click="cashMoney">立即提现</view>
		</view>

		<up-modal :show="wxShow" title="微信收款码" :showCancelButton="true" cancelText="关闭" @cancel="wxShow = false"
			@confirm="confirmWechatQrCode">
			<view class="wallet-modal-body">
				<image src="/static/logo.png" mode="aspectFill" class="wallet-modal-img" @click="upImg" v-if="!wxImg"></image>
				<image :src="wxImg" mode="aspectFill" class="wallet-modal-img" @click="upImg" v-else></image>
				<view class="wallet-modal-hint">点击上传或更换微信收款码</view>
			</view>
		</up-modal>
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
	import config from '@/common/config.js'
	const {
		proxy
	} = getCurrentInstance();

	const wxShow = ref(false);
	// 1 支付宝 2 微信小程序 3 微信公众号
	const cashType = ref(2)

	const payList = ref([{
		name: '微信',
		id: 2,
		image: '/my/static/icon_weixin.png'
	}, {
		name: '支付宝',
		id: 1,
		image: '/my/static/zhifubao.png'
	}]);
	const wxImg = ref('');
	const money = ref(null);
	const hasWechatCashQrCode = () => !!String(wxImg.value || userInfo.cashQrCode || '').trim();
	const confirmWechatQrCode = () => {
		if (!hasWechatCashQrCode()) {
			uni.showToast({
				title: '请上传微信提现二维码',
				icon: 'none'
			})
			return
		}
		wxShow.value = false
	};
	const cashMoney = () => {
		if (!money.value) {
			uni.showToast({
				title: '请输入提现金额',
				icon: 'none'
			})
			return
		}
		// 1支付宝 2微信小程序 3微信公众号
		if (!userInfo.zhiFuBao && !userInfo.zhiFuBao && cashType.value == 1) {
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
		if (!hasWechatCashQrCode() && cashType.value == 2) {
			uni.showToast({
				title: '请上传微信提现二维码',
				icon: 'none'
			})
			return
		}

		proxy.$http.getT('/app/cash/cashMoney', {
			money: money.value,
			classify: cashType.value,
			userType: 1,
		}).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
				setTimeout(() => {
					money.value = ''
					getUserInfo()
				}, 500)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const changeCashType = (item) => {
		cashType.value = item.id
		console.log(cashType.value, "cashType")
	};

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};

	const userInfo = reactive({});
	const getUserInfo = () => {
		proxy.$http.getT("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				Object.assign(userInfo, res.data);
				wxImg.value = res.data.cashQrCode || ''
				uni.setStorageSync('zhiFuBao', res.data.zhiFuBao)
				uni.setStorageSync('zhiFuBaoName', res.data.zhiFuBaoName)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
			uni.hideLoading();
		});
	};
	//提现说明
	const placeholder = ref('');
	const tipText = ref('');
	const charge = ref('');
	const minMoney = ref(0);
	const maxMoney = ref(0);

	// 提现手续费
	const getCharge = () => {
		proxy.$http.get("/app/common/type/114").then(res => {
			if (res.code == 0) {
				charge.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};
	// 最低提现额度
	const getMinMoney = () => {
		proxy.$http.get("/app/common/type/87").then(res => {
			if (res.code == 0) {
				minMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
			}
		});
	};
	// 最高提现额度
	const getMaxMoney = () => {
		proxy.$http.get("/app/common/type/11").then(res => {
			if (res.code == 0) {
				maxMoney.value = res.data.value
				placeholder.value = '服务费:' + charge.value * 100 + '%' + ',最低提现:' +
					minMoney.value * 1 + ',最高提现:' + maxMoney.value * 1
				console.log(placeholder.value, "	placeholder.value")
			}
		});
	};

	const upImg = (type, detailImg) => {
		let that = this;
		uni.chooseImage({
			count: 1,
			success(ret) {
				uni.showLoading({
					title: '上传中...'
				});
				uni.uploadFile({
					// url: 'https://peizhen.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
					url: config.UPLOADAPI + '/alioss/upload',
					filePath: ret.tempFilePaths[0],
					header: {
						token: uni.getStorageSync('token')
					},
					name: 'file',
					success: uploadFileRes => {
						let url = JSON.parse(uploadFileRes.data);
						console.log(url)
						if (url.code == 0) {
							let data = {
								cashQrCode: url.data
							}
							proxy.$http.postJson('/app/user/updateUser', data).then(res => {
								if (res.code === 0) {
									uni.hideLoading();
									uni.showToast({
										title: '添加成功',
										icon: 'none',
										duration: 2000
									})
									setTimeout(function() {
										wxShow.value = false
										wxImg.value = url.data
										getUserInfo()
									}, 1000)
								} else {
									uni.hideLoading();
									uni.showToast({
										title: res.msg,
										icon: 'none',
										duration: 2000
									})
								}
							});
						} else {
							uni.showToast({
								title: url.msg,
								icon: 'none'
							})
						}
						uni.hideLoading();
					},
					fail() {
						uni.hideLoading()
						uni.showToast({
							title: '上传失败，请重试',
							icon: 'none'
						})
					}
				});
			},
			fail() {
				uni.showToast({
					title: '上传失败，请重试',
					icon: 'none'
				})
			}
		})
	};

	onLoad(option => {
		getUserInfo()
		getCharge()
		getMinMoney()
		getMaxMoney()
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	.wallet-page {
		min-height: 100vh;
		padding-bottom: calc(160rpx + env(safe-area-inset-bottom));
		background: linear-gradient(180deg, #eef5ff 0%, #f6f8fc 32%, #f5f7fb 100%);
	}

	.wallet-header {
		padding: 30rpx 24rpx 60rpx;
	}

	.wallet-header-bg {
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		border-radius: 28rpx;
		padding: 44rpx 36rpx;
		box-shadow: 0 12rpx 30rpx rgba(43, 111, 232, 0.22);
		position: relative;
		overflow: hidden;
	}

	.wallet-header-bg::after {
		content: '';
		position: absolute;
		width: 300rpx;
		height: 300rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.12) 0%, transparent 70%);
		top: -100rpx;
		right: -60rpx;
		border-radius: 50%;
	}

	.wallet-header-inner {
		position: relative;
		z-index: 1;
	}

	.wallet-balance-label {
		font-size: 26rpx;
		color: rgba(255, 255, 255, 0.85);
		line-height: 1;
	}

	.wallet-balance-row {
		display: flex;
		align-items: baseline;
		margin-top: 24rpx;
		margin-bottom: 16rpx;
	}

	.wallet-balance-unit {
		font-size: 32rpx;
		font-weight: 700;
		color: #ffffff;
		margin-right: 8rpx;
	}

	.wallet-balance-num {
		font-size: 60rpx;
		font-weight: 800;
		line-height: 1;
		color: #ffffff;
	}

	.wallet-balance-tip {
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.72);
		line-height: 1.4;
	}

	.wallet-body {
		padding: 0 24rpx;
		margin-top: -30rpx;
		position: relative;
		z-index: 2;
	}

	.wallet-section {
		margin-top: 24rpx;
	}

	.wallet-section-title {
		font-size: 28rpx;
		font-weight: 700;
		color: #1e2a3a;
		margin-bottom: 18rpx;
		padding-left: 6rpx;
	}

	.wallet-panel {
		background: #ffffff;
		border-radius: 24rpx;
		padding: 32rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.wallet-panel--compact {
		padding: 12rpx 24rpx;
	}

	.wallet-panel-title {
		font-size: 30rpx;
		font-weight: 700;
		color: #1e2a3a;
		margin-bottom: 24rpx;
	}

	.wallet-amount-input {
		display: flex;
		align-items: center;
		padding: 18rpx 0;
		border-bottom: 2rpx solid #f0f2f5;
	}

	.wallet-amount-symbol {
		font-size: 40rpx;
		font-weight: 700;
		color: #1e2a3a;
		margin-right: 16rpx;
	}

	.wallet-amount-field {
		flex: 1;
		font-size: 42rpx;
		font-weight: 700;
		color: #1e2a3a;
		height: 72rpx;
	}

	.wallet-amount-hint {
		margin-top: 22rpx;
		font-size: 24rpx;
		line-height: 1.5;
		color: #8b94a5;
	}

	.wallet-pay-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 26rpx 12rpx;
		border-radius: 18rpx;
		transition: background 0.2s;
	}

	.wallet-pay-item--checked {
		background: #f0f7ff;
	}

	.wallet-pay-left {
		display: flex;
		align-items: center;
		gap: 20rpx;
		flex: 1;
		min-width: 0;
	}

	.wallet-pay-icon {
		width: 64rpx;
		height: 64rpx;
		border-radius: 50%;
		overflow: hidden;
		flex-shrink: 0;
		background: #ffffff;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
	}

	.wallet-pay-icon image {
		width: 44rpx;
		height: 44rpx;
	}

	.wallet-pay-icon--wechat {
		background: #e8f6eb;
	}

	.wallet-pay-icon--alipay {
		background: #e8f4fd;
	}

	.wallet-pay-info {
		flex: 1;
		min-width: 0;
	}

	.wallet-pay-name {
		font-size: 30rpx;
		font-weight: 600;
		color: #1e2a3a;
		line-height: 1.3;
	}

	.wallet-pay-status {
		margin-top: 6rpx;
		font-size: 24rpx;
		color: #8b94a5;
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
		font-size: 26rpx;
		color: #2b6fe8;
	}

	.wallet-record-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx 12rpx;
	}

	.wallet-record-label {
		display: flex;
		align-items: center;
		gap: 16rpx;
		font-size: 30rpx;
		color: #1e2a3a;
		font-weight: 500;
	}

	.wallet-record-dot {
		width: 14rpx;
		height: 14rpx;
		border-radius: 50%;
	}

	.wallet-record-dot--blue {
		background: #2b6fe8;
	}

	.wallet-record-dot--green {
		background: #22c55e;
	}

	.wallet-record-dot--orange {
		background: #f59e0b;
	}

	.wallet-record-dot--purple {
		background: #8b5cf6;
	}

	.wallet-record-divider {
		height: 2rpx;
		background: #f0f2f5;
		margin: 0 12rpx;
	}

	.wallet-footer {
		position: fixed;
		left: 0;
		bottom: 0;
		width: 100%;
		padding: 20rpx 32rpx calc(24rpx + env(safe-area-inset-bottom));
		background: #ffffff;
		box-shadow: 0 -4rpx 20rpx rgba(23, 43, 77, 0.06);
		box-sizing: border-box;
		z-index: 10;
	}

	.wallet-submit {
		width: 100%;
		height: 92rpx;
		border-radius: 46rpx;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		color: #ffffff;
		font-size: 32rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 20rpx rgba(43, 111, 232, 0.28);
	}

	.wallet-modal-body {
		padding: 24rpx 0 6rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.wallet-modal-img {
		width: 260rpx;
		height: 260rpx;
		border-radius: 16rpx;
		background: #f5f7fa;
	}

	.wallet-modal-hint {
		margin-top: 18rpx;
		font-size: 24rpx;
		color: #8b94a5;
	}
</style>
