<template>
	<view style="height: 100vh;margin: 32upx;">
		<view style="text-align: center;background: #FFFFFF;padding: 40upx;border-radius: 32upx;">
			<view style="font-size: 38upx;">{{$t('pageText.addkf')}}</view>
			<!-- <view style="font-size: 32upx;margin-top: 32upx;">手机号：{{phone}}</view> -->
			<view style="font-size: 32upx;margin-top: 32upx;">{{$t('msg.wxh')}}：{{weixin}}</view>
			<view @click="copyHref"
				style="background: #557EFD;width:200upx;margin-top: 32upx;font-size: 30upx;margin-left: 36%;color: #FFFFFF;padding: 4upx 20upx;border-radius: 24upx;">
				{{$t('repeatUse.yjfz')}}
			</view>

			<image @click="saveImg" mode="aspectFit" style="margin-top: 32upx" :src="image"></image>
			<view style="font-size: 28upx;margin-top: 32upx" v-if="isWeiXin">{{ isWeiXin ? $t('repeatUse.casb') : '' }}</view>
			<!-- <view @click="goChat"
				style="width:260upx;margin-top: 32upx;font-size: 30upx;margin-left: 28%;color: #557EFD;padding: 4upx 20upx;border-radius: 24upx;">
				联系在线客服</view> -->
			<view v-if="isWeiXin" style="font-size: 24upx;margin-top: 80upx" @click="rests">{{$t('repeatUse.wfsb')}}？</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				image: 'https://game.shengqianxiong.com.cn/custom.jpg',
				isWeiXin: false,
				weixin: '',
				webviewStyles: {
					progress: {
						color: '#1A1929 '
					}
				},
				phone: ''
			};
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('bar.zxkf')
			})
			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				this.isWeiXin = true;
			}
			// #endif
			//获取客服二维码
			this.$Request.getT(`/app/common/type/` + 1).then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.image = res.data.value;
					}
				}
			})
			//获取电话
			this.$Request.getT(`/app/common/type/` + 298).then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						console.log(res.data.value)
						this.phone = res.data.value;
					}
				}
			})
			// 微信
			this.$Request.getT(`/app/common/type/` + 44).then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						console.log(res.data.value)
						this.weixin = res.data.value;
					}
				}
			})
			// 企业微信
			// this.$Request.getT(`/app/common/type/` + 297).then(res => {
			// 	if (res.code == 0) {
			// 		if (res.data && res.data.value) {
			// 			console.log(res.data.value)
			// 			this.weixin = res.data.value;
			// 		}
			// 	}
			// })
		},
		onPullDownRefresh: function() {
			uni.stopPullDownRefresh(); // 停止刷新
		},
		methods: {
			//邀请码复制
			copyHref() {
				uni.setClipboardData({
					data: this.weixin,
					success: r => {
						this.$queue.showToast(this.$t('repeatUse.copyOk'));
					}
				});
			},
			saveImg() {
				let that = this;
				let imgArr = []
				imgArr.push(that.image);
				//预览图片
				uni.previewImage({
					urls: imgArr,
					current: imgArr[0]
				});
				// uni.saveImageToPhotosAlbum({
				// 	filePath: that.image,
				// 	success(res) {
				// 		that.$queue.showToast('保存成功');
				// 	}
				// });
			},
			rests() {
				uni.showToast({
					title: this.$t('toast.casb'),
					mask: false,
					duration: 1500,
					icon: 'none'
				});
				window.location.reload();
			},
			// 在线客服
			goChat() {
				let token = this.$queue.getData('token');
				if (token) {
					uni.navigateTo({
						url: '/my/setting/chat'
					});
				} else {
					this.goLoginInfo();
				}
			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/loginphone'
				});
			},
		}
	};
</script>

<style>
	/* @import '../../static/css/index.css'; */

	page {
		background: #F5F5F5;
	}
</style>