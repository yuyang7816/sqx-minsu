<template>
	<view class="plat-page plat-form-page">
		<view class="plat-form-head plat-cert-head">
			<view class="plat-form-head__title">企业认证</view>
			<view class="plat-form-head__hint">上传证照信息，完成企业主体认证</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">营业执照</view>
			<view class="plat-upload-box plat-upload-box--block" @click="chooseImages(0)" v-if="!licenseImg">
				<view class="plat-upload-box__camera">
					<up-icon name="camera-fill" color="#FFFFFF" size="40"></up-icon>
				</view>
				<view class="plat-upload-box__text">上传营业执照证件</view>
			</view>
			<view class="plat-upload-box plat-upload-box--block plat-upload-box--preview" @click="chooseImages(0)" v-else>
				<image :src="licenseImg" mode="aspectFill" class="plat-upload-box__preview"></image>
			</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">门头照片</view>
			<view class="plat-upload-box plat-upload-box--block" @click="chooseImages(1)" v-if="!storefrontImg">
				<view class="plat-upload-box__camera">
					<up-icon name="camera-fill" color="#FFFFFF" size="40"></up-icon>
				</view>
				<view class="plat-upload-box__text">上传真实门头照片</view>
			</view>
			<view class="plat-upload-box plat-upload-box--block plat-upload-box--preview" @click="chooseImages(1)" v-else>
				<image :src="storefrontImg" mode="aspectFill" class="plat-upload-box__preview"></image>
			</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">法人证件</view>
			<view class="plat-cert-grid">
				<view class="plat-upload-box plat-upload-box--cert" @click="chooseImages(2)" v-if="!legalFrontImg">
					<view class="plat-upload-box__camera">
						<up-icon name="camera-fill" color="#FFFFFF" size="36"></up-icon>
					</view>
					<view class="plat-upload-box__text">身份证人像面</view>
				</view>
				<view class="plat-upload-box plat-upload-box--cert plat-upload-box--preview" @click="chooseImages(2)" v-else>
					<image :src="legalFrontImg" mode="aspectFill" class="plat-upload-box__preview"></image>
				</view>
				<view class="plat-upload-box plat-upload-box--cert" @click="chooseImages(3)" v-if="!legalBackImg">
					<view class="plat-upload-box__camera">
						<up-icon name="camera-fill" color="#FFFFFF" size="36"></up-icon>
					</view>
					<view class="plat-upload-box__text">身份证国徽面</view>
				</view>
				<view class="plat-upload-box plat-upload-box--cert plat-upload-box--preview" @click="chooseImages(3)" v-else>
					<image :src="legalBackImg" mode="aspectFill" class="plat-upload-box__preview"></image>
				</view>
			</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">企业信息</view>
			<view class="plat-form-section">
				<view class="plat-form-field">
					<view class="plat-form-field__label">企业名称</view>
					<view class="plat-form-field__value">
						<input type="text" placeholder="请输入" v-model="shopName" class="plat-form-field__input"
							placeholder-class="plat-form-field__placeholder" />
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">法人姓名</view>
					<view class="plat-form-field__value">
						<input type="text" placeholder="请输入" v-model="name" class="plat-form-field__input"
							placeholder-class="plat-form-field__placeholder" />
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">身份证号</view>
					<view class="plat-form-field__value">
						<input type="idcard" placeholder="请输入" v-model="idNumber" class="plat-form-field__input"
							placeholder-class="plat-form-field__placeholder" />
					</view>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot">
			<view class="plat-btn" @click="submitBtn">提交</view>
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
		onReachBottom,
		onPageScroll
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	import config from '@/common/config.js'

	const shopName = ref('');
	const name = ref('');
	const idNumber = ref('');
	const licenseImg = ref('');
	const storefrontImg = ref('');
	const legalFrontImg = ref('');
	const legalBackImg = ref('');



	const submitBtn = () => {
		
		let data = {

		}
		proxy.$http.postJson('', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: '提交成功',
					icon: 'none'
				})
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const chooseImages = (type) => {
		uni.chooseImage({
			count: 1,
			sourceType: ['album', 'camera'],
			success: res => {
				console.log(res.tempFilePaths, "res.tempFilePaths")
				for (let i = 0; i < res.tempFilePaths.length; i++) {
					uni.showLoading({
						title: "上传中..."
					})
					uni.uploadFile({ // 上传接口
						url: config.UPLOADAPI + '/alioss/upload',
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							uni.hideLoading()
							let isImagegs = '';
							let url = JSON.parse(uploadFileRes.data).data;
							if (url) {
								isImagegs = isImage(url);
								if (isImagegs == false) {
									uni.showToast({
										title: '不支持上传当前图片格式',
										icon: 'none'
									})
								} else {
									if (type === 0) licenseImg.value = url
									if (type === 1) storefrontImg.value = url
									if (type === 2) legalFrontImg.value = url
									if (type === 3) legalBackImg.value = url
								}
							}
						}
					});
				}
			}
		})
	};
	const isImage = (url) => {
		return url.endsWith('.jpg') || url.endsWith('.jpeg') || url.endsWith('.png') || url.endsWith('.gif') || url
			.endsWith('.JPG') || url.endsWith('.JPEG') || url.endsWith('.PNG') || url.endsWith('.GIF');
	};


	onPageScroll(e => {});
	onLoad(option => {

	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		background: #f4f7fb;
	}

	.plat-form-page {
		min-height: 100vh;
		padding: 24rpx 24rpx 160rpx;
		background: linear-gradient(180deg, #eef4ff 0, #f4f7fb 220rpx);
	}

	.plat-cert-head {
		padding: 30rpx 28rpx;
		border-radius: 24rpx;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		box-shadow: 0 10rpx 24rpx rgba(43, 111, 232, 0.14);
	}

	.plat-cert-head .plat-form-head__title {
		color: #ffffff;
	}

	.plat-cert-head .plat-form-head__hint {
		color: rgba(255, 255, 255, 0.82);
	}

	.plat-form-card {
		margin: 18rpx 0 0 0 !important;
		margin-top: 18rpx;
		border-radius: 24rpx;
		box-shadow: 0 8rpx 24rpx rgba(15, 23, 42, 0.04);
	}

	.plat-upload-box {
		margin-top: 18rpx;
		height: 220rpx;
		border-radius: 20rpx;
		background: #f8fafc;
		border: 1rpx dashed #d8e2ef;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		overflow: hidden;
	}

	.plat-upload-box--block {
		width: 100%;
	}

	.plat-upload-box__camera {
		width: 72rpx;
		height: 72rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #2b6fe8 0%, #4b9dff 100%);
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.plat-upload-box__text {
		margin-top: 16rpx;
		font-size: 24rpx;
		color: #64748b;
	}

	.plat-upload-box--preview {
		padding: 0;
		border-style: solid;
	}

	.plat-upload-box__preview {
		width: 100%;
		height: 100%;
	}

	.plat-cert-grid {
		margin-top: 18rpx;
		display: flex;
		align-items: stretch;
		gap: 16rpx;
	}

	.plat-upload-box--cert {
		flex: 1;
		height: 240rpx;
		min-width: 0;
		padding: 0 20rpx;
		box-sizing: border-box;
	}

	.plat-upload-box--cert .plat-upload-box__text {
		text-align: center;
		line-height: 1.5;
	}

	.plat-form-field {
		padding: 26rpx 0;
		border-bottom: 1rpx solid #eef2f7;
	}

	.plat-form-field:last-child {
		border-bottom: none;
	}

	.plat-form-field__label {
		font-size: 26rpx;
		color: #64748b;
	}

	.plat-form-field__value {
		margin-top: 14rpx;
		padding: 20rpx 24rpx;
		border-radius: 18rpx;
		background: #f8fafc;
	}

	.plat-form-field__input {
		font-size: 28rpx;
		color: #1e293b;
	}

	.plat-fixed-foot {
		padding: 20rpx 32rpx calc(24rpx + env(safe-area-inset-bottom));
		background: rgba(255, 255, 255, 0.96);
		box-shadow: 0 -4rpx 18rpx rgba(15, 23, 42, 0.05);
	}
</style>
