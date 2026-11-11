<template>
	<view class="plat-page plat-form-page plat-emp-add-page">

		<view class="plat-form-card" style="margin-top: 24rpx;">
			<view class="plat-form-card__title">员工信息</view>
			<view class="plat-form-section">
				<view class="plat-form-field">
					<view class="plat-form-field__label">员工姓名</view>
					<input type="text" placeholder="请输入员工姓名" v-model="userName" class="plat-form-field__input" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">员工手机号</view>
					<input type="number" placeholder="请输入员工手机号" maxlength="11" v-model="phone" class="plat-form-field__input" />
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">验证码</view>
					<view class="plat-emp-code-row">
						<input type="number" placeholder="请输入验证码" v-model="msg"
							class="plat-form-field__input plat-emp-code-input" />
						<button class="plat-btn plat-btn--sm plat-emp-code-btn" @click="sendMsg"
							:disabled="sending">{{ sendTime }}</button>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot">
			<view class="plat-btn" @click="insertStaffUser">确认添加</view>
		</view>
	</view>
</template>


<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue'

	const {
		proxy
	} = getCurrentInstance()

	const storeId = ref(uni.getStorageSync('storeId'))
	const phone = ref('')
	const msg = ref('')
	const sending = ref(false)
	const sendTime = ref('获取验证码')
	const count = ref(60)
	const userName = ref('')

	const countDown = () => {
		if (count.value === 1) {
			count.value = 60
			sending.value = false
			sendTime.value = '获取验证码'
		} else {
			count.value -= 1
			sending.value = true
			sendTime.value = count.value + '秒后重新获取'
			setTimeout(countDown, 1000)
		}
	}

	const sendMsg = () => {
		if (!phone.value) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
		} else if (phone.value.length !== 11) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			})
		} else {
			uni.showLoading({
				title: '正在发送验证码...'
			})
			proxy.$http.getT(`/app/Login/sendMsg/${phone.value}/bingStaffUser`).then(res => {
				console.log(res)
				if (res.code === 0) {
					sending.value = true
					uni.showToast({
						title: '验证码发送成功请注意查收',
						icon: 'none'
					})
					countDown()
					uni.hideLoading()
				} else {
					uni.hideLoading()
					uni.showModal({
						showCancel: false,
						title: '短信发送失败',
						content: res.msg ? res.msg : '请一分钟后再获取验证码'
					})
				}
			})
		}
	}

	const insertStaffUser = () => {
		if (!userName.value) {
			uni.showToast({
				title: '请输入员工姓名',
				icon: 'none'
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: '请输入员工手机号',
				icon: 'none'
			})
			return
		}
		if (phone.value.length != 11) {
			uni.showToast({
				title: '请输入正确的员工手机号',
				icon: 'none'
			})
			return
		}
		if (!msg.value) {
			uni.showToast({
				title: '请输入验证码',
				icon: 'none'
			})
			return
		}

		const data = {
			userName: userName.value,
			phone: phone.value,
			msg: msg.value,
			shopId: uni.getStorageSync('shopId')
		}
		proxy.$http.postT('/app/shopsUserRole/insertStaffUser', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '添加成功',
					icon: 'none'
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 500)
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
	page {
		background: #f4f7fb;
	}

	.plat-emp-add-page {
		min-height: 100vh;
		padding: 24rpx 24rpx 160rpx;
		background: linear-gradient(180deg, #eef4ff 0, #f4f7fb 220rpx);
	}

	.plat-emp-add-hero {
		padding: 30rpx 28rpx;
		border-radius: 24rpx;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		color: #ffffff;
		box-shadow: 0 10rpx 24rpx rgba(43, 111, 232, 0.14);
	}

	.plat-emp-add-hero__title {
		font-size: 36rpx;
		font-weight: 700;
	}

	.plat-emp-add-hero__desc {
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: rgba(255, 255, 255, 0.82);
	}

	.plat-form-card {
		margin: 0;
		margin-top: 18rpx !important;
		border-radius: 24rpx;
		box-shadow: 0 8rpx 24rpx rgba(15, 23, 42, 0.04);
	}

	.plat-form-field {
		padding: 28rpx 0;
	}

	.plat-emp-code-row {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}

	.plat-emp-code-input {
		flex: 1;
	}

	.plat-emp-code-btn {
		min-width: 220rpx;
		height: 72rpx;
		padding: 0 20rpx;
		border-radius: 18rpx;
		font-size: 24rpx;
		box-shadow: none;
	}

	.plat-fixed-foot {
		padding: 20rpx 32rpx calc(24rpx + env(safe-area-inset-bottom));
		background: rgba(255, 255, 255, 0.96);
		box-shadow: 0 -4rpx 20rpx rgba(15, 23, 42, 0.06);
	}

	:deep(uni-button:after) {
		border: none !important;
	}

	:deep(uni-button) {
		margin-right: 0 !important;
	}

	:deep(wx-button:after) {
		border: none !important;
	}

	:deep(wx-button) {
		margin: 0 !important;
	}
</style>
