<template>
	<view class="plat-page plat-setting-page plat-feedback-page">
		<view class="plat-head plat-head--immersive">
			<view class="plat-setting-head">
				<view class="plat-setting-head__title">意见反馈</view>
				<view class="plat-setting-head__hint">您的建议是我们进步的动力</view>
			</view>
		</view>

		<view class="plat-body plat-body--pad plat-body--overlap">

			<view class="plat-setting-group">
				<view class="plat-setting-card plat-feedback-card">
					<view class="plat-feedback-field">
						<view class="plat-feedback-field__head">
							<view>
								<view class="plat-feedback-field__label">反馈内容</view>
								<view class="plat-feedback-field__hint">尽量描述清楚问题出现的场景，便于我们定位</view>
							</view>
							<view class="plat-feedback-quick" hover-class="plat-feedback-quick--hover" @tap="chooseMsg">
								<up-icon name="edit-pen" size="14" color="#2C8EFE"></up-icon>
								<text>快速键入</text>
							</view>
						</view>
						<textarea
							v-model="sendDate.content"
							class="plat-feedback__area"
							placeholder="请详细描述你的问题和意见..."
							maxlength="300"
						/>
						<view class="plat-feedback-field__foot">
							<text>建议包含页面位置、操作步骤、异常现象</text>
							<text>{{sendDate.content.length}}/300</text>
						</view>
					</view>
				</view>
			</view>

			<view class="plat-setting-group">
				<view class="plat-setting-card plat-feedback-card">
					<view class="plat-feedback-field">
						<view class="plat-feedback-field__head plat-feedback-field__head--stack">
							<view class="plat-feedback-field__label">联系方式</view>
							<view class="plat-feedback-field__hint">可填写手机号、微信号或邮箱，方便我们联系你</view>
						</view>
						<input
							class="plat-feedback__input"
							v-model="sendDate.contact"
							placeholder="请输入联系方式"
						/>
					</view>
				</view>
			</view>

			<view class="plat-feedback-submit">
				<view class="plat-feedback-submit__tips">提交后我们会尽快查看并持续优化产品体验</view>
				<view class="plat-btn plat-feedback-btn" @tap="send">提交反馈</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const msgContents = reactive(['界面显示错乱', '启动缓慢，卡出翔了', 'UI无法直视，丑哭了', '偶发性崩溃']);
	const sendDate = reactive({
		score: 5,
		content: '',
		contact: ''
	});
	onLoad(option => {
		//#ifdef APP
		let deviceInfo = {
			appid: plus.runtime.appid,
			imei: plus.device.imei, //设备标识
			p: plus.os.name === 'Android' ? 'a' : 'i', //平台类型，i表示iOS平台，a表示Android平台。
			md: plus.device.model, //设备型号
			app_version: plus.runtime.version,
			plus_version: plus.runtime.innerVersion, //基座版本号
			os: plus.os.version,
			net: '' + plus.networkinfo.getCurrentType()
		};
		Object.assign(sendDate, deviceInfo);
		//#endif

	});
	const chooseMsg = () => {
		//快速输入
		uni.showActionSheet({
			itemList: msgContents,
			success: res => {
				sendDate.content = msgContents[res.tapIndex];
			}
		});
	};
	const send = () => {
		//发送反馈
		console.log(JSON.stringify(sendDate));

		if (!sendDate.content) {
			uni.showToast({
				icon: 'none',
				title: '请输入反馈内容'
			});
			return;
		}
		if (!sendDate.contact) {
			uni.showToast({
				icon: 'none',
				title: '请输入联系方式'
			});
			return;
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.postJson('/app/feedback/save', {
			contact: sendDate.contact,
			opinion: JSON.stringify(sendDate),
			userId: uni.getStorageSync('userId')
		}).then(res => {
			if (res.code === 0) {
				uni.showToast({
					title: '提交成功'
				});
				setTimeout(function() {
					uni.navigateBack();
				}, 1000);
			} else {
				uni.hideLoading();
				uni.showModal({
					showCancel: false,
					title: '提交失败',
					content: res.msg
				});
			}
		});
	};
</script>

<style lang="scss" scoped>
	.plat-feedback-page {
		padding-bottom: calc(56rpx + env(safe-area-inset-bottom));
	}

	.plat-feedback-panel {
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.98) 0%, rgba(246, 250, 255, 0.98) 100%);
		border: 1rpx solid rgba(44, 142, 254, 0.14);
		border-radius: 32rpx;
		box-shadow: 0 20rpx 50rpx rgba(44, 142, 254, 0.08);
		padding: 32rpx 30rpx;
		margin-bottom: 24rpx;
	}

	.plat-feedback-panel__badge {
		display: inline-flex;
		align-items: center;
		height: 44rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		background: rgba(44, 142, 254, 0.1);
		color: #2c8efe;
		font-size: 22rpx;
		font-weight: 600;
	}

	.plat-feedback-panel__title {
		margin-top: 22rpx;
		font-size: 36rpx;
		font-weight: 700;
		color: #1f2937;
		line-height: 1.4;
	}

	.plat-feedback-panel__desc {
		margin-top: 14rpx;
		font-size: 26rpx;
		line-height: 1.7;
		color: #64748b;
	}

	.plat-feedback-card {
		padding: 32rpx 30rpx;
		border-radius: 32rpx;
	}

	.plat-feedback-field__head {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 24rpx;
	}

	.plat-feedback-field__head--stack {
		display: block;
	}

	.plat-feedback-field__label {
		font-size: 30rpx;
		font-weight: 600;
		color: #111827;
		line-height: 1.4;
	}

	.plat-feedback-field__hint {
		margin-top: 8rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #94a3b8;
	}

	.plat-feedback-quick {
		flex-shrink: 0;
		display: inline-flex;
		align-items: center;
		justify-content: center;
		gap: 8rpx;
		height: 56rpx;
		padding: 0 20rpx;
		border-radius: 999rpx;
		background: rgba(44, 142, 254, 0.08);
		border: 1rpx solid rgba(44, 142, 254, 0.14);
		color: #2c8efe;
		font-size: 24rpx;
		font-weight: 600;
	}

	.plat-feedback-quick--hover {
		opacity: 0.82;
	}

	.plat-feedback__area,
	.plat-feedback__input {
		width: 94%;
		margin-top: 24rpx;
		border-radius: 24rpx;
		background: #f8fbff;
		border: 1rpx solid rgba(148, 163, 184, 0.2);
		font-size: 28rpx;
		color: #1f2937;
		line-height: 1.6;
	}

	.plat-feedback__area {
		height: 320rpx;
	}

	.plat-feedback-field__foot {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
		margin-top: 18rpx;
		font-size: 22rpx;
		color: #94a3b8;
		line-height: 1.5;
	}

	.plat-feedback-submit {
		margin-top: 40rpx;
	}

	.plat-feedback-submit__tips {
		text-align: center;
		font-size: 24rpx;
		color: #94a3b8;
		line-height: 1.6;
		margin-bottom: 22rpx;
		padding: 0 24rpx;
	}

	.plat-feedback-btn {
		margin: 0;
		border-radius: 24rpx;
		font-size: 30rpx;
		font-weight: 600;
		box-shadow: 0 18rpx 40rpx rgba(44, 142, 254, 0.2);
	}
</style>
