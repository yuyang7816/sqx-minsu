<!-- 帮助反馈页面 -->
<template>
	<view>
		<!-- <view class="text-top">常见问题</view> -->

		<view class="bg-list">
			<view v-for="(item,index) in helpClassifyList" :key="index" :title="item.helpClassifyName"
				class="list-title padding-bottom">
				<view class="flex align-center justify-between" @click.stop="openList(item)">
					<view class="text-title">{{item.helpClassifyName}}</view>
					<view @click.stop="openList(item)">
						<image src="../static/up.png" style="width: 21rpx;height: 15rpx;" v-if="item.parentId==0">
						</image>
						<image src="../static/dowm.png" style="width: 21rpx;height: 15rpx;" v-else></image>
					</view>
				</view>
				<view v-for="(problemItem,problemIndex) in item.helpWordList" :key="problemIndex" class="list-question"
					hover-class="hover" @click="onClick(problemItem)" v-if="item.parentId==0">
					<view class="text-item">{{problemItem.helpWordTitle}}</view>
					<view class="line" v-if="problemIndex!=item.helpWordList.length-1"></view>
				</view>
			</view>
		</view>
		<view class="bg-box">
			<view class="bg-white-box">
				<image src="../../static/images/icon-letter.png" class="image"></image>
				<view class="text-feedback" hover-class="hover" @click="Changekefu">{{$t('repeatUse.lxkf')}}</view>

				<view class="vertical-line"></view>

				<image src="../../static/images/icon-edit.png" class="image"></image>
				<view class="text-feedback" hover-class="hover" @click="toFeedback">{{$t('repeatUse.wyfk')}}</view>
			</view>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				helpClassifyList: []
			}
		},
		onLoad() {
			this.getlist()
			uni.setNavigationBarTitle({
				title: this.$t('repeatUse.bzzx')
			})
		},
		methods: {
			openList(item) {
				var oldhelpClassifyList = item
				if (oldhelpClassifyList.parentId == 1) {
					item.parentId = 0
				} else {
					item.parentId = 1
				}
			},
			// 列表
			getlist() {
				this.$Request.getT('/app/help/selectHelpList').then(res => {
					if (res.code == 0) {
						this.helpClassifyList = res.data
						for (var i = 0; i < this.helpClassifyList.length; i++) {
							this.helpClassifyList[i].parentId = 1
						}
					}
				})
			},
			onClick(item) {
				uni.navigateTo({
					url: '/my/helpDetail/helpDetail?title=' + item.helpWordTitle + '&helpWordId=' + item
						.helpWordId,
				})

			},
			Changekefu() {
				// let kefu = this.$queue.getData('kefu'); // 用户端联系方式 1 手机号 2企业微信
				// let kefuPhone = this.$queue.getData('kefuPhone');
				// if (kefu == 1) {
				// 	uni.makePhoneCall({
				// 		phoneNumber: kefuPhone //仅为示例
				// 	});
				// } else if (kefu == 2) {
				// 	// #ifdef MP-WEIXIN
				// 	let that = this
				// 	try {
				// 		wx.openCustomerServiceChat({
				// 			extInfo: {
				// 				url: that.$queue.getData('kefuUrl')
				// 			},
				// 			corpId: that.$queue.getData('kefuAppId'),
				// 			success(res) {},
				// 			fail(res) {
				// 				console.error(res)
				// 			}
				// 		})
				// 	} catch (error) {
				// 		console.error("catchcatch" + error)
				// 		uni.showToast({
				// 			title: '请更新至微信最新版本'
				// 		});
				// 	}
				// 	// #endif
				// 	// #ifndef MP-WEIXIN
				// 	let url = this.$queue.getData('kefuUrl');
				// 	if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
				// 		uni.navigateTo({
				// 			url
				// 		});
				// 	} else {
				// 		//#ifndef H5
				// 		uni.navigateTo({
				// 			url: '/pages/index/webView?url=' + url
				// 		});
				// 		//#endif
				// 		//#ifdef H5
				// 		window.location.href = url;
				// 		//#endif
				// 	}
				// 	// #endif
				// } else if (kefu == 3) {
				// 	uni.navigateTo({
				// 		url: '/my/kefu/kefu'
				// 	});
				// }
				// 获取客服
				this.$Request.getT(`/app/common/type/` + 296).then(res => {
					if (res.code == 0) {
						console.log(res.data, "console.log(res.data.value)")
						if (res.data.value == 1) {
							this.$Request.getT(`/app/common/type/` + 298).then(res => {
								if (res.code == 0) {
									if (res.data && res.data.value) {
										uni.makePhoneCall({
											phoneNumber: res.data.value //仅为示例
										});
									}
								}
							})
						} else if (res.data.value == 3) {
							uni.navigateTo({
								url: '/my/setting/customer'
							})
						} else if (res.data.value == 2) {
							// uni.showToast({
							// 	title:''
							// })
							// 	// #ifdef MP-WEIXIN
							// 	let that = this
							// 	try {
							// 		wx.openCustomerServiceChat({
							// 			extInfo: {
							// 				url: that.$queue.getData('kefuUrl')
							// 			},
							// 			corpId: that.$queue.getData('kefuAppId'),
							// 			success(res) {},
							// 			fail(res) {
							// 				console.error(res)
							// 			}
							// 		})
							// 	} catch (error) {
							// 		console.error("catchcatch" + error)
							// 		uni.showToast({
							// 			title: '请更新至微信最新版本'
							// 		});
							// 	}
							// 	// #endif
							// 	// #ifndef MP-WEIXIN
							// 	let url = this.$queue.getData('kefuUrl');
							// 	if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
							// 		uni.navigateTo({
							// 			url
							// 		});
							// 	} else {
							// 		//#ifndef H5
							// 		uni.navigateTo({
							// 			url: '/pages/index/webView?url=' + url
							// 		});
							// 		//#endif
							// 		//#ifdef H5
							// 		window.location.href = url;
							// 		//#endif
							// 	}
							// 	// #endif
						}
					}
				})
			},

			toFeedback() {
				if (uni.getStorageSync('userId')) {
					uni.navigateTo({
						url: '/my/feedback/index',
						success: res => {},
						fail: () => {},
						complete: () => {}
					});
				} else {
					uni.showModal({
						title: this.$t('repeatUse.ts'),
						content: this.$t('repeatUse.nologin'),
						confirmText: this.$t('repeatUse.qr'),
						cancelText: this.$t('repeatUse.qx'),
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.navigateTo({
									url: '/pages/public/login'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}


			}
		}
	}
</script>

<style>
	page {
		background-color: #FFFFFF;
		height: 100%;
	}




	.bg-box {
		background-color: #FFFFFF;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
	}

	.bg-list {
		margin-bottom: 100rpx;
		background-color: #FFFFFF;
		padding: 30rpx
	}

	.bg-white-box {
		background-color: #F7F7F7;
		margin: 30rpx;
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		padding: 10rpx;
		border-radius: 20rpx;
		color: #000;
		font-size: 32rpx;
	}

	.vertical-line {
		height: 20rpx;
		background-color: #cecece;
		width: 2rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
	}

	.line {
		width: 100%;
		height: 1rpx;
		background-color: #d3d3d3;
	}



	.text-title {
		color: #000;
		font-size: 32rpx;
		font-weight: bold;
	}

	.text-item {
		color: #999999;
		font-size: 28rpx;
		padding: 24rpx 0rpx;
	}

	.list-title {
		margin-bottom: 30rpx;
	}

	.list-question {
		color: #000;
		font-size: 28rpx;
	}

	.hover {
		background-color: #ffffff;
		opacity: 0.6;
	}

	.image {
		width: 40rpx;
		height: 40rpx;
		margin-left: 20rpx;
	}

	.text-feedback {
		padding: 20rpx;
		/* color: #000000; */
	}

	.text-top {
		margin: 30rpx;
		color: #000;
		font-size: 34rpx;
	}
</style>