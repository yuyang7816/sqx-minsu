<template>
	<view class="root">
		<view class="user">
			<view class="user_info" :style="{ height: iStatusBarHeight + 'px'}">
				<view class="user_Info_right">
					<view class="user_Info_img" @click="gotoPage('/subPackage/modify/modify')">
						<view>
							<image :src="userInfo.avatar" mode="scaleToFill" v-if="userInfo.avatar" />
							<image src="/static/images/defImage.png" mode="scaleToFill" v-else />
						</view>
						<image class="vip_logo" src="../../static/images/vip_logo.png" mode="scaleToFill" v-if="userInfo.isVip" />
					</view>
					<view class="user_Info_phone" v-if="userInfo.userId">
						<view class="ellipsis" style="margin-left: 20rpx;margin-bottom: 10rpx;">{{
							userInfo.userName
						}}</view>
						<!-- <p style="height: 25rpx;margin-left: 20rpx;margin-top: 18rpx;">30个</p> -->
						<view class="bi flex alic">
							<image src="../../static/images/bi.png" mode="scaleToFill" />
							<text>{{ userInfo.goldCoins }}{{$t('unit.ge')}}</text>
						</view>
					</view>
					<view class="login_text" @click="navigate('/pages/public/login')" v-else>{{$t('repeatUse.login')}}</view>
				</view>

				<view class="user_Info_card flex" @click="gotoPage('/subPackage/signin/signin')">
					<image src="../../static/images/signin.png" style="width: 31rpx;height: 31rpx;margin-right: 6rpx;"
						mode="scaleToFill" />
					{{$t('repeatUse.qd')}}
				</view>
				<view @tap="qhTypeSel" class="signin_btn  flex jc alic f26 c000 langebtn">
					<image src="/static/images/my/guoji.png" style="width: 50rpx;height:50rpx;"></image>
				</view>

			</view>
			<view class="learnDuration">
				<view class="learnDuration_item">
					<view class="learnDuration_item_title"><text
							class="font_bold">{{ Math.floor(userData.durationCount/60) || '0'}}</text>
						{{$t('repeatUse.fenzhong')}}
					</view>
					<view class="learnDuration_item_text">{{$t('pagesText.tksc')}}</view>
				</view>
				<view class="learnDuration_item">
					<view class="learnDuration_item_title"><text
							class="font_bold">{{ userData.answerCount  || '0'}}</text>{{$t('unit.ge')}}</view>
					<view class="learnDuration_item_text">{{$t('pagesText.dtsl')}}</view>
				</view>
				<view class="learnDuration_item">
					<view class="learnDuration_item_title"><text
							class="font_bold">{{ userData.examCount  || '0'}}</text>{{$t('order.ci')}}</view>
					<view class="learnDuration_item_text">{{$t('pagesText.mkcs')}}</view>
				</view>
			</view>
			<view class="Vip">
				<view class="icon">
					<image src="../../static/images/vip_.png" mode="scaleToFill" />
					<text>VIP</text>
				</view>
				<view class="vip_text text-cut">
					{{$t('pagesText.openVip')}}
				</view>
				<view class="openVip" @click="gotoPage('/subPackage/vip/vip')">
					{{ userInfo.isVip ? $t('btn.ljck') : $t('repeatUse.openVip') }}
				</view>
			</view>
		</view>
		<view class="navmenu">
			<view class="navmenu_item" @click="gotoPage('/pages/my/my-universal?type=1')">
				<image src="../../static/images/wdtk.png" mode=""></image>
				<text>{{$t('pagesText.dtjl')}}</text>
			</view>
			<view class="navmenu_item" @click="gotoPage('/pages/my/my-universal?type=2')">
				<image src="../../static/images/wdkc_.png" mode=""></image>
				<text>{{$t('pagesText.wdkc')}}</text>
			</view>
			<view class="navmenu_item" @click="toOrder">
				<image src="../../static/images/wddd.png" mode=""></image>
				<text>{{$t('pagesText.myOrder')}}</text>
			</view>

			<view class="navmenu_item" @click="gotoPage('/pages/my/my-universal?type=3')">
				<image src="../../static/images/wdzl.png" mode=""></image>
				<text>{{$t('bar.wdzl')}}</text>
			</view>

		</view>



		<view class="myService">
			<view class="myService_title">
				<text>{{$t('pagesText.wdfw')}}</text>
			</view>
			<view class="myService_list">
				<view class="myService_list_item" @click="gotoPage('/my/ticket/ticket')">
					<image src="../../static/images/yhq.png" mode=""></image>
					<text>{{$t('yhj.name')}}</text>
				</view>
				<view class="myService_list_item" @click="gotoPage('/pages/my/my-universal?type=4')">
					<image src="../../static/images/scct.png" mode=""></image>
					<text>{{$t('pagesText.ctsc')}}</text>
				</view>
				<view class="myService_list_item" @click="gotoPage('/subPackage/invitation/invitation')">
					<image src="../../static/images/yqhy.png" mode=""></image>
					<text>{{$t('repeatUse.yqhy')}}</text>
				</view>
				<view class="myService_list_item" @click="gotoPage('/subPackage/addressManage/addressManage')">
					<image src="../../static/images/dzgl.png" mode=""></image>
					<text>{{$t('bar.dzgl')}}</text>
				</view>
			</view>
		</view>
		<view class="myService">
			<view class="myService_title">
				<text>{{$t('pagesText.cygj')}}</text>
			</view>
			<view class="myService_list">
				<view class="myService_list_item" @click="customerType">
					<image src="../../static/images/kfzx.png" mode=""></image>
					<text>{{$t('repeatUse.kfzx')}}</text>
				</view>
				<view class="myService_list_item" @click="gotoPage('/my/setting/help')">
					<image src="../../static/images/bzzx.png" mode=""></image>
					<text>{{$t('repeatUse.bzzx')}}</text>
				</view>
				<view class="myService_list_item" @click="gotoPage('/my/feedback/index')">
					<image src="../../static/images/yjfk.png" mode=""></image>
					<text>{{$t('repeatUse.yjfk')}}</text>
				</view>
				<view class="myService_list_item" @click="gotoPage('/my/setting/index')">
					<image src="../../static/images/szzx.png" mode=""></image>
					<text>{{$t('repeatUse.szzx')}}</text>
				</view>
				<view class="myService_list_item" style="height: 0;">

				</view>
				<view class="myService_list_item" style="height: 0;">

				</view>
			</view>
		</view>
		<u-select v-model="Languageshow" :list="listLanguage" @confirm="confirm" :confirm-text="$t('btnText.qd')"
			:cancel-text="$t('btnText.qx')" @cancel="cancelBtn"></u-select>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				userData: {},
				userInfo: {},
				avatar: 'https://tu.mianyazuju.com/20240807/2290bf9b59e34082b05d3a9a26645ad5.png',
				iStatusBarHeight: 0,

				listLanguage: [], //语言选择
				Languageshow: false,
				language: uni.getStorageSync('language')
			}
		},
		onLoad() {
			this.iStatusBarHeight = uni.getSystemInfoSync().statusBarHeight + 60;
		},
		methods: {
			cancelBtn() {
				let that = this;
				that.Languageshow = false
			},
			confirm(e) {
				console.log(e, "eeeeeeeeeee")
				let itemValue = e[0]
				console.log(itemValue, 'itemValue')
				let languagelabel = itemValue.label
				let languagevalue = itemValue.value.language
				let languageObj = itemValue.value
				console.log(languageObj, "languageObjlanguageObj")
				let that = this;
				if (uni.getStorageSync('language') == itemValue.value.language) {} else {
					that.confimChange(languagevalue, languageObj)
				}
				that.Languageshow = false
			},
			confimChange(languagevalue, languageObj) {
				let that = this;
				that.$Request.getT('/app/configI18n/getLanguageByKey?language=' + languagevalue +
					'&primaryKey=moadlText.language,moadlText.languageC,btnText.qx,btnText.qd').then(res => { // 选择语言弹框
					if (res.code == 0 && res.data) {
						let ts = ''
						let title = ''
						let no = ''
						let yes = ''
						res.data.forEach((d, index) => {
							if (d.primaryKey == 'moadlText.languageC') {
								ts = d.languageValue
							}
							if (d.primaryKey == 'moadlText.language') {
								title = d.languageValue
							}
							if (d.primaryKey == 'btnText.qx') {
								no = d.languageValue
							}
							if (d.primaryKey == 'btnText.qd') {
								yes = d.languageValue
							}
						})
						uni.showModal({
							title: ts,
							content: title,
							showCancel: true,
							cancelText: no,
							confirmText: yes,
							success: res => {
								if (res.confirm) {
									console.log(languageObj, "languageObj")
									that.zyType = languagevalue;
									uni.setLocale(languagevalue);
									uni.setStorageSync("UNI_LOCALE", languagevalue)
									uni.setStorageSync("language", languagevalue)
									uni.setStorageSync('mapLanguage', languageObj.mapLanguage)
									// 必须更新首页顶部选项默认值
									uni.setStorageSync('currents', [0, 0])
									uni.setStorageSync('typeId', '')
									that.$i18n.locale = languagevalue;
									// #ifdef APP-PLUS
									plus.runtime.restart();
									// #endif
									// #ifdef H5
									that.$router.go(0);
									// #endif
								}
							}
						});
					}
				});
			},
			// 获取语种列表
			qhTypeSel() {
				this.listLanguage = []
				this.$Request.getT('/app/languageType/getLanguageTypeList').then(res => { // 获取国际化语种类别
					if (res.code == 0 && res.data) {
						res.data.records.map(item => {
							this.listLanguage.push({
								label: item.remark,
								value: item,
							})
						})
						// this.listLanguage = res.data.records
						uni.setStorageSync("languageList", res.data.records)
						this.Languageshow = true
						console.log(this.listLanguage, "this.listLanguage")
					}
				});
			},
			customerType() {
				if (uni.getStorageSync('token')) {
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
				} else {
					uni.showModal({
						title: this.$t('repeatUse.ts'),
						content: this.$t('repeatUse.nologin'),
						confirmText: this.$t('repeatUse.qr'),
						cancelText: this.$t('repeatUse.qx'),
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.reLaunch({
									url: '/pages/public/login'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}
			},
			handleClick(e) {
				console.log(e);
			},
			gotoPage(url, val) {
				if (uni.getStorageSync('token')) {
					if (val == 1) {
						uni.navigateTo({
							url: '/my/setting/customer'
						})
					} else {
						uni.navigateTo({
							url: url // 目标页面的路径，需在 `pages.json` 中配置
						});
					}
				} else {
					uni.showModal({
						title: this.$t('repeatUse.ts'),
						content: this.$t('repeatUse.nologin'),
						confirmText: this.$t('repeatUse.qr'),
						cancelText: this.$t('repeatUse.qx'),
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.reLaunch({
									url: '/pages/public/login'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}
			},
			toOrder() {
				if (uni.getStorageSync('token')) {
					uni.switchTab({
						url: '/pages/order/order'
					})
				} else {
					uni.showModal({
						title: this.$t('repeatUse.ts'),
						content: this.$t('repeatUse.nologin'),
						confirmText: this.$t('repeatUse.qr'),
						cancelText: this.$t('repeatUse.qx'),
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.reLaunch({
									url: '/pages/public/login'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}

			},

			// 获取听课--答题---模考
			getMyData() {
				if (uni.getStorageSync('token')) {
					this.$Request.getT('/app/dataCenter/getMyTopicInfo', {
						typeId: uni.getStorageSync('typeId')
					}).then(res => {
						this.userData = res.data
					})
				}
			},
			// 获取个人信息
			getUserInfoById() {
				if (uni.getStorageSync('userId')) {
					this.$Request.getT('/app/user/selectUserById', {
						userId: uni.getStorageSync('userId')
					}).then(res => {
						this.userInfo = res.data
						uni.setStorageSync('userInfo', res.data)
					})
				}
			}
		},
		onShow() {
			this.getMyData()
			this.getUserInfoById()
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #F5F7FF;
	}

	.font_bold {
		font-weight: 700;
		font-size: 44rpx;
	}

	.user {
		width: 750rpx;
		height: 600rpx;
		background: linear-gradient(90deg, #0096FF 0%, #79C8FF 100%);
		position: relative;
	}

	.user_info {
		width: 686rpx;
		height: 120rpx;
		position: absolute;
		margin: 155rpx 32rpx 0 32rpx;
		box-sizing: border-box;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.user_Info_right {
		display: flex;
		align-items: center;
	}

	.user_Info_img {
		width: 120rpx;
		height: 120rpx;
		position: relative;

		&>view {
			width: 120rpx;
			height: 120rpx;
			border-radius: 50%;
			overflow: hidden;
		}

		image {
			width: 100%;
			height: 100%;
		}

		.vip_logo {
			position: absolute;
			width: 40rpx;
			height: 40rpx;
			top: 0;
			left: 60%;
			transform: translate(0, -60%);
		}
	}

	.user_Info_phone {
		width: 300rpx;
		font-family: PingFang SC;
		font-weight: bold;
		font-size: 32rpx;
		color: #FFFFFF;

		.bi {
			margin-left: 20rpx;

			image {
				width: 38rpx;
				height: 38rpx;
				margin-right: 12rpx;
			}

			text {
				font-size: 26rpx;
				color: #fff;
				font-weight: 500;
			}
		}
	}

	.user_Info_card {
		width: max-content;
		padding: 0 25rpx;
		height: 62rpx;
		background: #FFFFFF;
		border-radius: 31rpx;
		border: 1px solid #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
		transform: scale(1.1);

		font-family: PingFang SC;
		font-weight: 500;
		font-size: 26rpx;
		color: #0096FF;
		line-height: 2rpx;
		margin-left: auto;
		margin-right: 20rpx;
	}

	.learnDuration {
		width: 100%;
		// height: 100rpx;

		margin: 350rpx auto 0 auto;
		position: absolute;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.learnDuration_item {
			width: calc(100%/3);

			.learnDuration_item_title {
				// width: 180rpx;
				font-family: DINPro;
				font-weight: 500;
				font-size: 28rpx;
				color: #FFFFFF;
				margin-bottom: 14rpx;
				text-align: center;
			}

			.learnDuration_item_text {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #FFFFFF;
				text-align: center;
			}
		}
	}

	.login_text {
		font-size: 32rpx;
		color: #fff;
		margin-left: 24rpx;
	}

	.Vip {
		width: 686rpx;
		height: 101rpx;
		background: linear-gradient(90deg, #1B81FF 0%, #3CAEFF 100%);
		border-radius: 16rpx;
		margin: 475rpx 32rpx 1rpx 32rpx;
		position: absolute;
		display: flex;
		// justify-content: space-around;
		align-items: center;
		padding-left: 25rpx;

		.icon {
			position: relative;
			margin-right: 20rpx;

			image {
				width: 48rpx;
				height: 42rpx;
			}

			text {
				position: absolute;
				font-size: 24rpx;
				color: $theme-color;
				left: 50%;
				top: 50%;
				transform: translate(-50%, -40%) scale(.7);
				font-weight: 800;
			}
		}

		.vip_text {
			width: 337rpx;
			// height: 33rpx;
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #FFFFFF;
			// line-height: 2rpx;
			display: flex;
			align-items: center;
		}

		.openVip {
			width: 167rpx;
			height: 64rpx;
			background: linear-gradient(90deg, #FEF2DC 0%, #FEE4BF 100%);
			border-radius: 32rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-left: 55rpx;

			font-family: PingFang SC;
			font-weight: bold;
			font-size: 26rpx;
			color: #5C4D37;
		}
	}

	.navmenu {
		width: 686rpx;
		height: auto;
		margin: 23rpx 32rpx 0 32rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		justify-content: space-between;
		padding-bottom: 10rpx;
		padding-top: 46rpx;
	}

	.navmenu_item {
		width: 25%;
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		margin-bottom: 20rpx;

		image {
			width: 56rpx;
			height: 56rpx;
		}

		text {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 26rpx;
			color: #333333;
			margin-top: 16rpx;
		}


	}


	.myService {
		width: 686rpx;
		height: auto;
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 32rpx 0 32rpx;
		padding-bottom: 10rpx;

		.myService_title {
			width: 100%;
			padding: 30rpx 0 0 32rpx;

			text {
				font-weight: bold;
				font-size: 30rpx;
				color: #333333;
			}
		}

		.myService_list {
			width: 686rpx;
			display: flex;
			justify-content: space-between;
			flex-wrap: wrap;
			align-items: center;
			margin-top: 20rpx;


		}

		.myService_list_item {
			width: 25%;
			display: flex;
			flex-direction: column;
			justify-content: space-around;
			align-items: center;
			margin-bottom: 20rpx;

			image {
				width: 56rpx;
				height: 56rpx;
			}

			text {
				font-weight: 500;
				font-size: 26rpx;
				color: #333333;
				margin-top: 10rpx;
			}

		}
	}
</style>