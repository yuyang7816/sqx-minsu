<template>
	<view class="plat-page plat-form-page plat-evaluate-page">
		<view class="plat-form-head">
			<view class="plat-form-head__title">发表评价</view>
			<view class="plat-form-head__hint">分享您的入住体验，帮助更多用户选择</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">评价内容</view>
			<up-textarea v-model="content" placeholder="请输入评价内容" class="plat-evaluate__area" autoHeight :maxlength="-1" border="none"
				maxlength="-1" :height="150"></up-textarea>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">订单评分</view>
			<view class="plat-evaluate__rate">
				<up-rate active-color="#2C8EFE" inactive-color="#EEF1F6" gutter="15" @change="changeStart"
					v-model="starNum"></up-rate>
			</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">上传图片</view>
			<view class="plat-evaluate__imgs">
				<view class="plat-evaluate__img-wrap" v-for="(item,index) in contentImg" :key="index">
					<image :src="item" mode="aspectFill" class="plat-evaluate__img"></image>
					<view class="plat-evaluate__img-del" @click="delImg(index)">
						<up-icon name="close-circle-fill" color="#2C8EFE" size="40rpx"></up-icon>
					</view>
				</view>
				<view class="plat-upload-box" @click="chooseImages" v-if="contentImg.length <= 8">
					<up-icon name="camera" size="30" color="#64748B" style="justify-content: center;margin-top: 30rpx;"></up-icon>
					<view class="plat-upload-box__text">添加图片</view>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot">
			<view class="plat-btn" @click="saveCommen">提交评价</view>
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
	import config from '../../common/config.js'
	const {
		proxy
	} = getCurrentInstance();

	const content = ref('');
	const contentImg = reactive([]);
	const orderId = ref('');
	const starNum = ref(5);
	const shopId = ref('');

	onLoad(option => {
		if (option.orderId) {
			orderId.value = option.orderId
		}
		if (option.shopId) {
			shopId.value = option.shopId
		}
	})

	const saveCommen = () => {
		let images = '';
		if (!content.value) {
			uni.showToast({
				icon: 'none',
				title: '请输入评价内容'
			});
			return;
		}
		if (contentImg.length == 0) {
			uni.hideLoading();
			uni.showToast({
				title: '请添加图片!',
				icon: 'none'
			})
			return;
		} else {
			for (var i = 0; i < contentImg.length; i++) {
				if (i === 0) {
					images = contentImg[i];
				} else {
					images = images + ',' + contentImg[i];
				}
			}
		}

		let data = {
			userId: uni.getStorageSync('userId'),
			shopId: shopId.value,
			satisfactionFlag: starNum.value,
			evaluateMessage: content.value,
			evaluateImg: images,
			orderNumber: orderId.value
		}
		uni.showLoading({
			title: '评价中...'
		})
		proxy.$http.postT('/app/evaluate/addEvaluate', data).then(
			res => {
				uni.hideLoading()
				if (res.code == 0) {
					uni.showToast({
						title: '评价成功'
					})
					setTimeout(() => {
						uni.navigateBack()
					}, 500)
				} else {
					uni.showToast({
						title: '评价失败',
						icon: 'none'
					})
				}
			})
	};
	const changeStart = (e) => {
		starNum.value = e
		console.log(e, "eeeeeeeeeeee")
	};
	const delImg = (index) => {
		contentImg.splice(index, 1);
	};
	const chooseImages = () => {
		uni.chooseImage({
			count: 9,
			sourceType: ['album', 'camera'],
			success: res => {
				console.log(res.tempFilePaths, "res.tempFilePaths")
				for (let i = 0; i < res.tempFilePaths.length; i++) {
					uni.showLoading({
						title: "上传中..."
					})
					uni.uploadFile({
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
									contentImg.push(url)
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

	const routeGo = (url) => {
		uni.navigateTo({
			url: url
		})
	};
</script>


<style lang="scss" scoped>
	::v-deep .uni-textarea-wrapper {
		min-height: 150rpx !important;
	}
</style>
