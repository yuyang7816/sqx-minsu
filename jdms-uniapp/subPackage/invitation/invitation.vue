<template>
	<view class="content">
		<u-navbar :back-text="$t('repeatUse.yqhy')"></u-navbar>
		<!-- <view class="margin-lr-sm margin-top padding" style="background: #FFFFFF;border-radius: 20upx;">
      <view class="flex align-center">
        <image :src="avatar ? avatar : '../../static/image/logo.png'"
          style="width: 72upx;height: 72upx;border-radius: 12upx;"></image>
        <view class="margin-left-sm">
          <view class="text-sm"><text class="text-bold" style="font-size: 48upx;">{{ peopleNum ? peopleNum : '0' }}</text>人
          </view>
          <view style="color: #82A9FE;">已成功邀请人数</view>
        </view>
      </view>
    </view> -->
		<view class="view1" v-bind:style="{ backgroundImage: 'url(' + backgroundImage + ')' }">
			<view style="padding-top: 820upx;" @longpress="logoTime(avatar)">
				<view style="width: 100%;height: 150upx;display: flex;background: #FFFFFF;padding: 20upx 10upx;">
					<image :src="user.avatar || defaultAvatar"
						style="border-radius: 100upx;width: 100upx;height: 100upx;margin-left: 30upx;">
					</image>
					<view class="login-view-text1" style="margin-left: 30upx;width: 59%;">
						<view style="color: #000000;font-size: 16px;">{{ user.userName }}</view>
						<view style="color: #000000; font-size: 12px;margin-top: 20upx;">
							{{$t('repeatUse.yqm')}}:{{ user.invitationCode }}
						</view>
					</view>
					<canvas canvas-id="qrcode" style="width: 140upx;height: 130upx;" />
				</view>
			</view>
		</view>
		<view style="display: flex;flex-direction: row; padding: 40upx;justify-content: center;">
			<!-- #ifdef H5 -->
			<button @click="share()" type="default"
				style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%;">{{$t('repeatUse.watg')}}</button>
			<button @tap="showModal()" type="default"
				style="background-color: #FF5808;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%; margin-left: 40upx;">{{$t('repeatUse.schb')}}</button>
			<!-- #endif -->
			<!-- #ifdef APP-PLUS -->
			<button v-if="isShowWxAPPShare == '是'" @click="shareWeiXin()" type="default"
				style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%;">{{$t('repeatUse.watg')}}</button>
			<button v-if="isShowWxAPPShare == '否'" @click="sharAPPUrl()" type="default"
				style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%;">{{$t('repeatUse.watg')}}</button>

			<button @tap="showModal()" type="default"
				style="background-color: #FF5808;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%; margin-left: 40upx;">{{$t('repeatUse.schb')}}</button>
			<!-- #endif -->

			<!-- #ifdef MP-WEIXIN -->
			<button open-type="share"
				style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 40%;">{{$t('repeatUse.yjfx')}}</button>
			<!-- <button @click="share()" type="default" style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 40%;">文案推广</button> -->
			<button @tap="showModal()" type="default"
				style="background-color: #FF5808;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 40%;">{{$t('repeatUse.schb')}}</button>

			<!-- #endif -->
			<!-- 生成海报 -->
			<!-- 图片展示由自己实现 -->
			<view class="flex_row_c_c modalView" :class="qrShow ? 'show' : ''" @tap="hideQr()">
				<view class="flex_column">
					<view class="backgroundColor-white padding1vh border_radius_10px">
						<image :src="poster.finalPath || ''" mode="widthFix" class="posterImage"></image>
					</view>
					<view class="flex_row marginTop2vh">
						<!-- #ifndef H5 -->
						<button type="primary" size="mini" @tap.prevent.stop="saveImage()">{{$t('repeatUse.savePhoto')}}</button>
						<!-- #endif -->
					</view>
				</view>
			</view>
			<!-- 画布 -->
			<view class="hideCanvasView">
				<canvas class="hideCanvas" canvas-id="default_PosterCanvasId"
					:style="{ width: (poster.width || 10) + 'px', height: (poster.height || 10) + 'px' }"></canvas>
			</view>
		</view>
		<!-- #ifndef MP-WEIXIN -->
		<tki-qrcode ref="qrcode" :val="url" :size="200" background="#fff" foreground="#000" pdground="#000" :onval="true"
			:loadMake="true" @result="qrR" :show="false"></tki-qrcode>
		<view class="cu-modal" :class="modalName == 'Image' ? 'show' : ''" @tap="hideModal">
			<view class="cu-dialog" v-if="backgroundImage && erweimapath && haibaoShow" @tap="hideModal">
				<view class="bg-img">
					<wm-poster @success="posterSuccess" :imgSrc="backgroundImage"
						:Referrer="$t('repeatUse.myCode')+':' + invitationCode" :QrSrc="erweimapath" :Title="tuiguang"
						:LineType="false"></wm-poster>
				</view>
			</view>
		</view>
		<!-- #endif -->

		<!-- #ifdef MP-WEIXIN -->
		<tki-qrcode ref="qrcode" :val="url" :size="200" background="#fff" foreground="#000" pdground="#000" :onval="true"
			:loadMake="true" @result="qrR" :show="false"></tki-qrcode>
		<view class="cu-modal" :class="modalName == 'Image' ? 'show' : ''" @tap="hideModal">
			<view class="cu-dialog" v-if="backgroundImage && xcxerweimapath && haibaoShow" @tap="hideModal">
				<view class="bg-img">
					<wm-poster @success="posterSuccess" :imgSrc="backgroundImage"
						:Referrer="$t('repeatUse.myCode')+':' + invitationCode" :QrSrc="xcxerweimapath" :Title="tuiguang"
						:LineType="false"></wm-poster>
				</view>
			</view>
		</view>
		<!-- #endif -->
	</view>
</template>

<script>
	let settingWritePhotosAlbum = false;
	import tkiQrcode from '@/components/tki-qrcode/tki-qrcode.vue';
	import appShare from '@/utils/share.js';
	import wmPoster from '@/components/wm-poster/wm-posterorders.vue';
	import uQRCode from "../../js_sdk/Sansnn-uQRCode/uqrcode.js"
	import _app from '../../js_sdk/QuShe-SharerPoster/QS-SharePoster/app.js';
	import configdata from '../../common/config.js';
	// import {
	// 	getSharePoster
	// } from '../../js_sdk/QuShe-SharerPoster/QS-SharePoster/QS-SharePoster.js';
	export default {
		components: {
			tkiQrcode,
			wmPoster,
			// getSharePoster
		},
		data() {
			return {
				defaultAvatar: '/static/logo.png',
				erweimapath: '',
				poster: {},
				qrShow: false,
				haibaoImg: null,
				haibaoShow: false,
				modalName: '',
				canvasId: 'default_PosterCanvasId',
				imageUrl: '',
				userImageUrl: '',
				isShowWxAPPShare: '否',
				nickName: '',
				invitationCode: '',
				backgroundImage: '',
				tuiguang: '',
				tuiguang1: '',
				url: '',
				peopleNum: '',
				xcxerweimapath: '',
				appurl: '',
				avatar: '',
				user: uni.getStorageSync('userInfo')
			};
		},
		onLoad() {
			this.getBackImageList();
			// this.avatar = this.$queue.getData('image_url');
			// if (!avatar) {
			// 	this.userImageUrl = avatar
			// } else {
			// 	this.userImageUrl = '../../static/image/logo.png';
			// }
			this.goList()
			this.$Request.getT('/app/common/type/276').then(res => {
				if (res.code === 0) {
					this.tuiguang = res.data.value;
				}
			});
			this.$Request.getT('/app/common/type/277').then(res => {
				if (res.code === 0) {
					if (res.data !== null) {
						this.tuiguangImg = res.data.value;
					}
				}
			});
			//文案推广
			this.$Request.getT('/app/common/type/101').then(res => {
				if (res.code === 0) {
					if (res.data && res.data.value) {
						this.tuiguang1 = res.data.value;
					}
				}
			});
			//检测书否开启APP微信分享
			this.$Request.getT('/app/common/type/136').then(res => {
				if (res.code === 0) {
					if (res.data && res.data.value) {
						this.isShowWxAPPShare = res.data.value;
					}
				}
			});

			this.invitationCode = this.$queue.getData('invitationCode');
			// #ifdef MP-WEIXIN
			this.xcxerweimapath = this.config("APIHOST1") + '/app/invite/mpCreateQr?invitationCode=' + this.invitationCode
			// #endif
			//#ifdef APP-PLUS
			this.$Request.getT('/app/common/type/422').then(res => {
				if (res.code === 0) {
					if (res.data && res.data.value) {
						this.url = res.data.value + '?invitation=' + this.invitationCode + '&platformApp=app';
						console.log(this.url, "this.url")
					}
				}
			});
			//#endif

			let userName = this.$queue.getData('nickName');
			if (userName && userName !== 'undefined') {
				this.nickName = userName;
			} else {
				this.nickName = this.$t('repeatUse.yk');
			}

		},
		onShareAppMessage(res) {
			return {
				path: '/pages/index/index?invitation=' + this.invitationCode, //这是为了传参   onload(data){let id=data.id;} 
				title: this.tuiguang,
				imageUrl: this.tuiguangImg
			}
		},
		/*
		 * uniapp微信小程序分享页面到微信朋友圈
		 */
		onShareTimeline(res) {
			return {
				path: '/pages/index/index?invitation=' + this.invitationCode, //这是为了传参   onload(data){let id=data.id;} 
				title: this.tuiguang,
				imageUrl: this.tuiguangImg
			}
		},
		methods: {
			//获取我邀请的人数
			// getYaoNum() {
			//   this.$Request.getT('/app/user/queryInviterCount', {}).then(res => {
			//     if (res.code === 0) {
			//       this.peopleNum = res.data
			//     }
			//   });
			// },
			posterSuccess(haibaoImg) {
				this.haibaoImg = haibaoImg;
				this.modalName = 'Image';
			},
			showModal() {
				console.log(this.url)
				if (!this.haibaoImg) {
					this.haibaoShow = true;
					this.$queue.showLoading(this.$t('repeatUse.hbscz') + '...');
				} else {
					this.modalName = 'Image';
				}
			},
			hideModal() {
				this.modalName = null;
			},
			qrR(path) {
				this.erweimapath = path;
			},
			getBackImageList() {
				// uni.showLoading({
				// 	title: '加载中...'
				// })
				// this.$Request.getT('/app/banner/selectBannerList?state=-1&classify=5').then(res => {
				// 	uni.hideLoading()
				// 	if (res.code === 0) {
				// 		this.backgroundImage = res.data[0].imageUrl;
				// 	}
				// });

				this.$Request.getT('/app/common/type/289').then(res => {
					if (res.code == 0) {
						if (res.data && res.data.value) {
							this.backgroundImage = res.data.value;
						}
					}
				});

				this.make();

			},
			make() {
				uQRCode.make({
					canvasId: 'default_PosterCanvasId',
					componentInstance: this,
					text: this.url,
					size: 68,
					margin: 4,
					backgroundColor: '#ffffff',
					foregroundColor: '#000000',
					fileType: 'jpg',
					correctLevel: uQRCode.errorCorrectLevel.H,
					success: res => {
						console.log(res)
					}
				})
			},
			shareWeiXin() {

				// this.$Request.getT('/app/common/type/103').then(res => {
				// 	console.log(res,'11111')
				// 	if (res.code === 0) {
				// 		console.log(res,'0000000000')
				// 		if (res.data && res.data.value) {

				let relationId = this.invitationCode;
				let shareData = {
					shareUrl: this.url + '?relationId=' + relationId,
					// shareUrl: this.$queue.publicYuMing() + '/pagesA/applogin/userphone',
					shareTitle: this.tuiguang,
					shareContent: this.$t('repeatUse.yqm') + '：' + relationId + '，' + this.tuiguang,
					shareImg: this.$queue.publicYuMing() + '/logo.png',
					type: 0
				};
				appShare(shareData, res => {
					console.log('分享成功回调', res);
				});
				// }
				// }
				// });

			},
			share() {
				this.sharurl();
			},
			sharAPPUrl() {
				let that = this;
				let relationId = this.invitationCode;
				uni.showModal({
					title: that.$t('repeatUse.watg'),
					// content: this.tuiguang1 + relationId + '\n' + this.url,
					content: this.url,
					showCancel: true,
					cancelText: that.$t('repeatUse.gb'),
					confirmText: that.$t('repeatUse.yjfz'),
					success: res => {
						if (res.confirm) {
							uni.setClipboardData({
								// data: this.tuiguang1 + relationId + '\n' + this.url,
								data: this.url,
								success: function() {
									console.log('success');
									// that.$queue.showToast('文案复制成功');
									uni.showToast({
										title: that.$t('repeatUse.wafzcg'),
										icon: 'none'
									})
								}
							});
						}
					}
				});
			},
			sharurl() {
				let that = this;
				// this.$queue.showLoading('加载中...');
				let relationId = this.invitationCode;
				uni.showModal({
					title: that.$t('repeatUse.watg'),
					// content: this.tuiguang1 + relationId + '\n' + this.url,
					content: this.url,
					showCancel: true,
					cancelText: that.$t('repeatUse.gb'),
					confirmText: that.$t('repeatUse.yjfz'),
					success: res => {
						if (res.confirm) {
							uni.setClipboardData({
								// data: this.tuiguang1 + relationId + '\n' + this.url,
								data: this.url,
								success: function() {
									console.log('success');
									// that.$queue.showToast('复制成功');
									uni.showToast({
										title: that.$t('repeatUse.copyOk'),
										icon: 'none'
									})
								}
							});
						}
					}
				});
			},
			logoTime(urlList) {
				uni.previewImage({
					current: 0,
					urls: urlList,
					loop: true,
					longPressActions: {
						itemList: [this.$t('yao.sc')],
						itemColor: "#007AFF"
					}
				})
			},
			goList() {
				let userId = this.$queue.getData('userId');
				this.$Request.getT('/app/invite/selectInviteAndPoster?userId=' + userId).then(res => {
					if (res.code === 0) {
						// if (res.data.user.imageUrl) {
						// 	this.userImageUrl = res.data.user.imageUrl;
						// } else {
						// 	this.userImageUrl = '/static/img/common/logo.jpg';
						// }
						if (res.data.user.nickName) {
							this.nickName = res.data.user.nickName;
						} else {
							this.nickName = res.data.user.phone;
						}
						this.invitationCode = res.data.user.invitationCode;
						this.imageUrl = res.data.url;
						//#ifdef H5
						this.$Request.getT('/app/common/type/422').then(ress => {
							if (ress.code === 0) {
								if (ress.data && ress.data.value) {
									this.url = ress.data.value + '?invitation=' + this.invitationCode;
									// this.url = 'http://192.168.0.131:8081/pages/public/register?invitation=' + this.invitationCode
								}
							} else {
								this.url = this.$queue.publicYuMing() + '/pages/public/register?invitation=' + this
									.invitationCode;
							}
						});
						//#endif
					}
				});
			},
			async shareFc() {
				let _this = this;
				try {
					const d = await getSharePoster({
						type: 'testShareType',
						backgroundImage: _this.backgroundImage,
						posterCanvasId: _this.canvasId, //canvasId
						delayTimeScale: 20, //延时系数
						drawArray: ({
							bgObj,
							type,
							bgScale
						}) => {
							const dx = bgObj.width * 0.3;
							const fontSize = bgObj.width * 0.045;
							const lineHeight = bgObj.height * 0.04;
							//可直接return数组，也可以return一个promise对象, 但最终resolve一个数组, 这样就可以方便实现后台可控绘制海报
							return new Promise((rs, rj) => {
								rs([{
										type: 'custom',
										setDraw(Context) {
											Context.setFillStyle('black');
											Context.setGlobalAlpha(0.3);
											Context.fillRect(0, bgObj.height - bgObj
												.height * 0.2, bgObj.width, bgObj
												.height * 0.2);
											Context.setGlobalAlpha(1);
										}
									},
									{
										type: 'text',
										fontStyle: 'italic',
										text: _this.$t('repeatUse.yqm') + ':' + _this.invitationCode,
										size: fontSize,
										color: 'white',
										alpha: 1,
										textAlign: 'left',
										textBaseline: 'middle',
										infoCallBack(textLength) {
											return {
												dx: bgObj.width - textLength - fontSize,
												dy: bgObj.height - lineHeight * 3
											}
										},
										serialNum: 0,
										id: 'tag1' //自定义标识
									},
									{
										type: 'qrcode',
										text: _this.url,
										size: bgObj.width * 0.2,
										dx: bgObj.width * 0.05,
										dy: bgObj.height - bgObj.width * 0.25
									}
								]);
							})
						},
						setCanvasWH: ({
							bgObj,
							type,
							bgScale
						}) => { // 为动态设置画布宽高的方法，
							_this.poster = bgObj;
						}
					});
					//_app.log('海报生成成功, 时间:' + new Date() + '， 临时路径: ' + d.poster.tempFilePath)
					_this.poster.finalPath = d.poster.tempFilePath;
					_this.qrShow = true;
				} catch (e) {
					_app.hideLoading();
				}
			},
			saveImage() {
				console.log('111111111')
				let that = this
				uni.saveImageToPhotosAlbum({
					filePath: that.poster.finalPath,
					success(res) {
						uni.showToast({
							title: that.$t('repeatUse.bccg'),
							icon: 'none'
						})
					}
				})
			},
			hideQr() {
				this.qrShow = false;
			},

			// 微信小程序保存图片
			onSaveImg() {
				// #ifdef MP-WEIXIN
				let imgUrl = this.createPoster();
				uni.showLoading({
					title: '海报下载中'
				});
				if (settingWritePhotosAlbum) {
					uni.getSetting({
						success: res => {
							if (res.authSetting['scope.writePhotosAlbum']) {
								uni.saveImageToPhotosAlbum({
									filePath: imgUrl,
									success: () => {
										uni.hideLoading();
										uni.showToast({
											title: '保存成功'
										});
									}
								});
							} else {
								uni.showModal({
									title: '提示',
									content: '请先在设置页面打开“保存相册”使用权限',
									confirmText: '去设置',
									cancelText: '算了',
									success: data => {
										if (data.confirm) {
											uni.hideLoading();
											uni.openSetting();
										}
									}
								});
							}
						}
					});
				} else {
					uni.hideLoading();
					settingWritePhotosAlbum = true;
					uni.authorize({
						scope: 'scope.writePhotosAlbum',
						success: () => {
							uni.saveImageToPhotosAlbum({
								filePath: imgUrl,
								success: () => {
									uni.hideLoading();
									uni.showToast({
										title: '保存成功'
									});
								}
							});
						}
					});
				}
				// #endif
			},
			//生成海报
			createPoster() {
				let that = this;
				return new Promise((resolve, reject) => {
					uni.showLoading({
						title: that.$t('repeatUse.hbscz')
					});
					const ctx = uni.createCanvasContext('poster');
					ctx.fillRect(0, 0, 375, 673);
					ctx.setFillStyle("#FFF");
					ctx.fillRect(0, 0, 375, 673);
					let imgUrl = that.backgroundImage;
					uni.downloadFile({
						url: imgUrl,
						success: (res) => {
							if (res.statusCode === 200) {
								uni.downloadFile({
									url: that.config("APIHOST1") + '/app/invite/mpCreateQr?invitationCode=' + that
										.invitationCode,
									success: (res2) => {
										uni.hideLoading();
										if (res.statusCode === 200) {
											console.log(res2, "1111111111")
											ctx.drawImage(res.tempFilePath, 0, 0, 375, 480);
											// 长按识别二维码访问
											let textTop = 0;
											ctx.setFontSize(19);
											ctx.setFillStyle('#333');
											ctx.fillText(that.$t('repeatUse.casbewm'), 17, textTop + 500);
											// 二维码
											ctx.drawImage(res2.tempFilePath, 200, textTop + 460, 100, 100);
											ctx.draw(true, () => {
												// canvas画布转成图片并返回图片地址
												uni.canvasToTempFilePath({
													canvasId: 'poster',
													width: 375,
													height: 673,
													success: (res) => {
														console.log("海报制作成功！11");
														resolve(res.tempFilePath);
													},
													fail: () => {
														uni.hideLoading();
														reject();
													}
												})
											});
										} else {
											uni.hideLoading();
											uni.showToast({
												title: that.$t('repeatUse.hbzzsb'),
												icon: 'none'
											});
										}
									},
									fail: err => {
										console.log(err)
										uni.hideLoading();
										uni.showToast({
											title: that.$t('repeatUse.hbzzsb'),
											icon: 'none'
										});
									},
									complete: com => {
										console.log(com)
										// uni.showToast({
										// 	title: com,
										// 	icon: 'none'
										// });
									},
								});
							} else {
								uni.hideLoading();
								uni.showToast({
									title: that.$t('repeatUse.hbzzsb'),
									icon: 'none'
								});
							}
						},
						fail: err => {
							// that.yu.toast(err)
							console.log(err)
							uni.hideLoading();
							uni.showToast({
								title: that.$t('repeatUse.hbzzsb'),
								icon: 'none',
							});
						}
					});
				});
			},
			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
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
			},
		}
	}
</script>

<style>
	.modal {
		position: fixed;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		z-index: 1110;
		opacity: 0;
		outline: 0;
		text-align: center;
		-ms-transform: scale(1.185);
		transform: scale(1.185);
		backface-visibility: hidden;
		perspective: 2000upx;
		background: rgba(0, 0, 0, 0.6);
		transition: all 0.3s ease-in-out 0s;
		pointer-events: none;
	}

	.modal.show {
		opacity: 1;
		transition-duration: 0.3s;
		-ms-transform: scale(1);
		transform: scale(1);
		overflow-x: hidden;
		overflow-y: auto;
		pointer-events: auto;
	}

	page {
		background: #F5F5F5;
	}

	.view1 {
		border-radius: 15upx;
		background-size: 100%;
		margin: 20upx 20upx 0 20upx;
	}

	.hideCanvasView {
		position: relative;
	}

	.hideCanvas {
		position: fixed;
		top: -99999upx;
		left: -99999upx;
		z-index: -99999;
	}

	.flex_row_c_c {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
	}

	.modalView {
		width: 100%;
		height: 100%;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		opacity: 0;
		outline: 0;
		transform: scale(1.2);
		perspective: 2500upx;
		background: rgba(0, 0, 0, 0.6);
		transition: all .3s ease-in-out;
		pointer-events: none;
		backface-visibility: hidden;
		z-index: 999;
	}

	.modalView.show {
		opacity: 1;
		transform: scale(1);
		pointer-events: auto;
	}

	.flex_column {
		display: flex;
		flex-direction: column;
	}

	.backgroundColor-white {
		background-color: white;
	}

	.border_radius_10px {
		border-radius: 10px;
	}

	.padding1vh {
		padding: 1vh;
	}

	.posterImage {
		width: 60vw;
	}

	.flex_row {
		display: flex;
		flex-direction: row;
	}

	.marginTop2vh {
		margin-top: 2vh;
	}

	.poster_canvas {
		width: 750upx;
		height: 1334upx;
		position: fixed;
		top: -10000upx;
		left: 0;
	}
</style>