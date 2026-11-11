<template>
	<view class="pages">
		<!-- <u-navbar back-text="去首页" :custom-back="back" background="#292C3D"
			:back-text-style="{ color: '#FFFFFF' }"></u-navbar> -->
		<view class="back-image">
			<image src="https://dati.xianmxkj.com/file/uploadPath/2024/08/14/47ab2ca6e5ee3c0c87795963bb9d3f9d.png" mode="">
			</image>
		</view>
		<view class="result-cont">
			<!-- 		<view class="" v-if="resultMsg.pass == false">
				<view class="result-cont-f">仍需再接再厉</view>
				<view class="result-cont-s">你的考试不及格</view>
			</view>
			<view class="" v-else>
				<view class="result-cont-f">恭喜你</view>
				<view class="result-cont-s">考试及格</view>
			</view> -->
			<view class="" v-if="resultMsg.pass == false">
				<view class="result-cont-f">{{$t('pagesText.zjzl')}}</view>
				<view class="result-cont-s" v-if="contentStatus == 1">{{$t('pagesText.hy')}}{{userName}}{{$t('pagesText.kshg')}}
				</view>
				<view class="result-cont-s" v-else>{{$t('pagesText.ksbjg')}}</view>
			</view>
			<view class="" v-else>
				<view class="result-cont-f">{{$t('pagesText.kxn')}}</view>
				<view class="result-cont-s" v-if="contentStatus == 1">
					{{$t('pagesText.hy')}}{{userName}}{{$t('pagesText.ksyhg')}}
				</view>
				<view class="result-cont-s" v-else>{{$t('pagesText.ksjg')}}</view>
			</view>



			<view class="result-cont_det">
				<view class="result-cont_det_l">
					<view class="result_num" style="font-size: 70rpx;">{{resultMsg.finalScore}}<text
							style="font-size: 24rpx;">{{$t('unit.fen')}}</text></view>
					<view class="result_t">{{$t('pagesText.bcdf')}}</view>
				</view>
			</view>
			<button open-type="share" class="btn_" style="background-color: #5BC9A4;" v-if="contentStatus == 0"
				@click="share">
				{{$t('pagesText.fxyx')}}
			</button>
			<view class="btn" style="background-color: #5873F6;" v-if="contentStatus == 1" @click="routeGo">
				{{$t('pagesText.wyyk')}}
			</view>
			<view class="btn" style="background-color: #5873F6;" v-if="contentStatus == 1" @click="routeGoIndex">
				{{$t('goods.backindex')}}
			</view>
			<view class="btn" style="background-color: #5BC9A4;" v-if="contentStatus == 0" @click="back">
				{{$t('goods.backindex')}}
			</view>
		</view>
		<u-modal v-model="showShareModal" :content="shareContent" :show-cancel-button="true"
			:confirm-text="$t('repeatUse.copy')" :cancel-text="$t('pageText.dcl')" :title="$t('repeatUse.fxlj')"
			@confirm="shareConfirm"></u-modal>
	</view>
</template>

<script>
	import config from '../../config.js'
	export default {
		// 新增
		data() {
			return {
				showShareModal: false,
				userInfo: uni.getStorageSync('userInfo'),
				shareContent: '',
				userId: null,
				goodStatus: false,
				resultMsg: {},
				contentStatus: 0,
				userName: '',
				shareInfo: {
					title: '',
					path: '',
					imageUrl: ''
				},
			}
		},
		onLoad(option) {

			this.resultMsg = JSON.parse(decodeURIComponent(option.resultMsg));
			if (option.userName) {
				this.contentStatus = 1
				this.userName = option.userName
			}
			// #ifdef H5 
			this.shareContent = config.BASE_URLS + '/subPackage/chapter/result' +
				`?resultMsg=${encodeURIComponent(JSON.stringify(this.resultMsg))}&userName=${this.userInfo.userName}`
			// #endif
			uni.showShareMenu({
				withShareTicket: true,
				menus: ['shareAppMessage', 'shareTimeline']
			});
			// this.onShareAppMessage = this.setShareAppMessage(this.shareInfo);
			this.onShareTimeline = this.setShareTimeline(this.shareInfo);
		},
		onShareAppMessage(res) {
			var that = this;
			let path = '/subPackage/chapter/result' +
				`?resultMsg=${encodeURIComponent(JSON.stringify(that.resultMsg))}&userName=${that.userInfo.userName}`
			return {
				title: '',
				path: path,
			}
		},
		methods: {
			back() {
				uni.switchTab({
					url: '/pages/index/index'
				})
			},
			// 分享给朋友
			// setShareAppMessage(shareInfo) {
			// 	return function() {
			// 		return {
			// 			title: shareInfo.title,
			// 			path: shareInfo.path,
			// 			imageUrl: shareInfo.imageUrl
			// 		};
			// 	};
			// },

			// 分享到朋友圈
			setShareTimeline(shareInfo) {
				return function() {
					return {
						title: shareInfo.title,
						path: shareInfo.path,
						imageUrl: shareInfo.imageUrl
					};
				};
			},
			routeGo() {
				uni.navigateTo({
					url: '/pages/examCompetition/examCompetition'
				})
			},
			routeGoIndex() {
				uni.switchTab({
					url: '/pages/index/index'
				})
			},
			share() {
				let that = this;
				// #ifdef H5
				this.showShareModal = true
				// #endif
				// #ifdef APP-PLUS
				let downloadLink = config.BASE_URLS + '/pages/public/appEq'
				uni.showModal({
					title: this.$t('repeatUse.fxlj'),
					content: downloadLink,
					confirmText: this.$t('repeatUse.copy'),
					success: function(res) {
						if (res.confirm) {
							uni.setClipboardData({
								data: downloadLink,
								success() {
									uni.showToast({
										title: that.$t('repeatUse.copyOk'),
										icon: 'successs'
									})
								}
							})
							console.log('用户点击确定');
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
				// #endif
			},
			shareConfirm() {
				uni.setClipboardData({
					data: this.shareContent,
					success() {
						uni.showToast({
							title: this.$t('repeatUse.copyOk'),
							icon: 'successs'
						})
					}
				})
			}
		},
	}
</script>

<style lang="scss" scoped>
	.pages {
		background-color: #292C3D;
		height: 100vh;
	}

	/deep/.u-model__content__message {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.back-image {
		width: calc(100% - 68rpx);
		height: 700rpx;
		position: absolute;
		// top: 17%;
		top: 9%;
		left: 34rpx;
		z-index: 1;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.result-cont_det {
		display: flex;
		align-items: center;
		justify-content: space-evenly;
		margin: 150rpx 0;
	}

	.result-cont {
		width: calc(100% - 80rpx);
		margin: auto;
		background-color: #fff;
		border-radius: 32rpx;
		padding: 100rpx 0 70rpx 0;
		position: relative;
		top: 240rpx;
	}

	.result-cont-f {
		font-size: 50rpx;
		color: #333333;
		text-align: center;
		font-weight: 800;
		padding-bottom: 20rpx;
	}

	.result-cont-s {
		font-size: 36rpx;
		color: #F1A954;
		text-align: center;
		font-weight: 800;
	}

	.result_num {
		color: #333333;
		font-weight: 800;
		text-align: center;
		padding-right: 20rpx;
	}

	.result_t {
		color: #999999;
		font-size: 30rpx;
		font-weight: 500;
		text-align: center;
		padding-right: 20rpx;
	}

	.border {
		background-color: #CCCCCC;
		width: 1rpx;
		height: 70rpx;
	}

	.result-cont_det_l {
		margin-left: 40rpx;
	}

	.result-cont_det_r {
		margin-right: 40rpx;
	}

	.btn {
		width: 490rpx;
		padding: 30rpx 0;
		border-radius: 70rpx;
		color: #ffffff;
		font-size: 28rpx;
		text-align: center;
		margin: 30rpx auto;
	}

	.btn_ {
		width: 490rpx;
		padding: 15rpx 0;
		border-radius: 70rpx;
		color: #ffffff;
		font-size: 28rpx;
		text-align: center;
		margin: 30rpx auto;
	}
</style>