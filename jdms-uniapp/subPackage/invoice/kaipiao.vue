<template>
	<view class="invoice-page invoice-form-page">
		<view class="invoice-form-hero">
			<view>
				<view class="invoice-form-hero__title">填写开票信息</view>
				<view class="invoice-form-hero__desc">请准确填写发票抬头、税号与收件方式，提交后将用于电子发票开具。</view>
			</view>
			<view class="invoice-form-hero__amount">
				<view class="invoice-form-hero__amount-label">发票金额</view>
				<view class="invoice-form-hero__amount-num">{{price}}<text>元</text></view>
			</view>
		</view>

		<view class="invoice-form-section">
			<view class="invoice-form-section__title">发票信息</view>
			<view class="invoice-form-card">
				<view class="invoice-form-field">
					<view class="invoice-form-field__label">发票抬头<text>*</text></view>
					<view class="invoice-form-field__control">
						<u-input placeholder="例：西安XXX有限公司" border="none" v-model="taitou"></u-input>
					</view>
				</view>
				<view class="invoice-form-field">
					<view class="invoice-form-field__label">纳税人识别号<text>*</text></view>
					<view class="invoice-form-field__control">
						<u-input placeholder="请填写纳税人识别号" border="none" v-model="shibiehao"></u-input>
					</view>
				</view>
				<view class="invoice-form-field">
					<view class="invoice-form-field__label">发票类型</view>
					<view class="invoice-form-field__control">
						<u-input :readonly="true" border="none" v-model="types"></u-input>
					</view>
				</view>
				<view class="invoice-form-field">
					<view class="invoice-form-field__label">发票内容</view>
					<view class="invoice-form-field__control">
						<u-input placeholder="请填写发票内容" border="none" v-model="contents"></u-input>
					</view>
				</view>
				<view class="invoice-form-field invoice-form-field--grid">
					<view class="invoice-form-field__half">
						<view class="invoice-form-field__label">注册地址</view>
						<view class="invoice-form-field__control">
							<u-input placeholder="请填写注册地址" border="none" v-model="address"></u-input>
						</view>
					</view>
					<view class="invoice-form-field__half">
						<view class="invoice-form-field__label">注册电话</view>
						<view class="invoice-form-field__control">
							<u-input placeholder="请填写注册电话" border="none" v-model="zcphone"></u-input>
						</view>
					</view>
				</view>
				<view class="invoice-form-field invoice-form-field--grid">
					<view class="invoice-form-field__half">
						<view class="invoice-form-field__label">开户银行</view>
						<view class="invoice-form-field__control">
							<u-input placeholder="请填写开户银行" border="none" v-model="yh"></u-input>
						</view>
					</view>
					<view class="invoice-form-field__half">
						<view class="invoice-form-field__label">银行账号</view>
						<view class="invoice-form-field__control">
							<u-input placeholder="请填写银行账号" border="none" v-model="yhzh"></u-input>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="invoice-form-section">
			<view class="invoice-form-section__title">收件信息</view>
			<view class="invoice-form-card">
				<view class="invoice-form-field">
					<view class="invoice-form-field__label">接收电子邮件<text>*</text></view>
					<view class="invoice-form-field__control">
						<u-input placeholder="用于向你发送电子发票" border="none" v-model="email"></u-input>
					</view>
				</view>
				<view class="invoice-form-field">
					<view class="invoice-form-field__label">接收手机号<text>*</text></view>
					<view class="invoice-form-field__control">
						<u-input placeholder="订单记录通过短信发送" border="none" v-model="phone" type="number" maxlength="11"></u-input>
					</view>
				</view>
			</view>
		</view>

		<view class="invoice-form-footer">
			<view class="invoice-form-submit" @click="submit">提交开票申请</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		computed
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();

	// 发票信息
	const taitou = ref('') // 发票抬头
	const shibiehao = ref('') // 纳税人识别号
	const types = ref('电子发票') // 发票类型
	const contents = ref('') // 发票内容
	const price = ref(0) // 发票金额
	const indentNumber = ref('') // 订单号
	const address = ref('') // 注册地址
	const zcphone = ref('') // 注册电话
	const yh = ref('') // 开户银行
	const yhzh = ref('') // 银行账号
	const email = ref('') // 电子发票邮箱
	const phone = ref('') // 联系电话

	onLoad((option) => {
		if (option.price) {
			price.value = option.price
		}
		if (option.indentNumber) {
			indentNumber.value = option.indentNumber
		}
	})

	const submit = () => {
		if (!taitou.value) {
			uni.showToast({
				title: '请输入发票抬头',
				icon: 'none'
			})
			return
		}
		if (!shibiehao.value) {
			uni.showToast({
				title: '请输入纳税人识别号',
				icon: 'none'
			})
			return
		}
		if (!email.value) {
			uni.showToast({
				title: '请输入接收电子邮件',
				icon: 'none'
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: '请输入接收手机号',
				icon: 'none'
			})
			return
		}
		const data = {
			orderIds: indentNumber.value.toString(),
			title: taitou.value,
			userId: uni.getStorageSync('userId'),
			amount: price.value,
			type: 1, // 类型 1个人 2公司
			creditCode: shibiehao.value,
			address: address.value,
			phone: zcphone.value,
			openBank: yh.value,
			bankNumber: yhzh.value,
			receiveEmail: email.value,
			receivePhone: phone.value,
			content: contents.value
		}
		proxy.$http.postJson('/app/invoice/save', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '提交成功!'
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}
</script>

<style lang="scss" scoped>
	.invoice-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #eef5ff 0%, #f6f8fc 34%, #f5f7fb 100%);
	}

	.invoice-form-page {
		padding: 24rpx 24rpx calc(160rpx + env(safe-area-inset-bottom));
	}

	.invoice-form-hero {
		padding: 34rpx 30rpx;
		border-radius: 26rpx;
		background: #ffffff;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
	}

	.invoice-form-hero__title {
		font-size: 38rpx;
		font-weight: 700;
		line-height: 1.2;
		color: #1e2a3a;
	}

	.invoice-form-hero__desc {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #8b94a5;
		max-width: 420rpx;
	}

	.invoice-form-hero__amount {
		flex-shrink: 0;
		padding: 18rpx 20rpx;
		border-radius: 20rpx;
		background: #f6faff;
	}

	.invoice-form-hero__amount-label {
		font-size: 22rpx;
		color: #8b94a5;
	}

	.invoice-form-hero__amount-num {
		margin-top: 10rpx;
		font-size: 36rpx;
		font-weight: 700;
		color: #2b6fe8;
	}

	.invoice-form-hero__amount-num text {
		font-size: 22rpx;
		margin-left: 6rpx;
	}

	.invoice-form-section {
		margin-top: 24rpx;
	}

	.invoice-form-section__title {
		font-size: 28rpx;
		font-weight: 700;
		color: #1e2a3a;
		margin-bottom: 16rpx;
		padding-left: 6rpx;
	}

	.invoice-form-card {
		background: #ffffff;
		border-radius: 24rpx;
		padding: 28rpx 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(23, 43, 77, 0.04);
	}

	.invoice-form-field + .invoice-form-field {
		margin-top: 20rpx;
	}

	.invoice-form-field__label {
		font-size: 24rpx;
		font-weight: 600;
		color: #1e2a3a;
	}

	.invoice-form-field__label text {
		color: #ff5b7a;
		margin-left: 4rpx;
	}

	.invoice-form-field__control {
		margin-top: 12rpx;
		padding: 0 18rpx;
		height: 88rpx;
		border-radius: 18rpx;
		background: #f6f8fc;
		display: flex;
		align-items: center;
	}

	.invoice-form-field--grid {
		display: grid;
		grid-template-columns: repeat(2, minmax(0, 1fr));
		gap: 16rpx;
	}

	.invoice-form-field__half {
		min-width: 0;
	}

	.invoice-form-footer {
		position: fixed;
		left: 0;
		bottom: 0;
		width: 100%;
		padding: 20rpx 24rpx calc(24rpx + env(safe-area-inset-bottom));
		background: #ffffff;
		box-shadow: 0 -4rpx 20rpx rgba(23, 43, 77, 0.06);
		box-sizing: border-box;
	}

	.invoice-form-submit {
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

	:deep(.u-input) {
		background: transparent !important;
	}

	:deep(.u-input__content) {
		padding: 0 !important;
	}

	:deep(.u-input__content__field-wrapper__field) {
		font-size: 28rpx;
		color: #1e2a3a;
		text-align: left;
	}
</style>


