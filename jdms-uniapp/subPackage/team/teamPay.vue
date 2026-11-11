<template>
	<view class="main">
		<u-navbar :back-text="backText" :title-bold="true"></u-navbar>
		<view class="team_work" v-if="groupList.length > 0">
			<view class="content_list_item">
				<view class="content_list_item_left">
					<u-image :src="goodInfo.coverImg" width="100%" height="100%" mode="aspectFit"></u-image>
				</view>
				<view class="content_list_item_right">
					<view class="content_list_item_right_top">
						<text>{{goodInfo.goodTitle}}</text>
					</view>
					<view class="content_list_item_right_bot">
						<v-price-format :custom-style="{ fontSize: '34rpx', color: '#D54B59', }"><text
								style="padding-left: 10rpx;">{{goodInfo.indentMoney}}</text></v-price-format>
					</view>
				</view>
			</view>
			<view class="content_list_item_">
				<view class="team_header">
					<view class="">{{$t('pagesText.haicha')}}<text
							style="color: red;">{{goodInfo.groupCount - goodInfo.nowGroupCount}}</text>{{$t('pagesText.rpc')}}
					</view>
					<view class="">{{$t('pagesText.js')}}<u-count-down :timestamp="countdown" color="red"
							@end="downEnd"></u-count-down>{{$t('yhj.dq')}}</view>
				</view>
				<view class="">
					<view class="avatarlist flex" :style="{width:boxWidth+'rpx'}">
						<image :src="item.avatar" mode="scaleToFill" class="avatar" v-for="item in groupList"
							v-if="groupList.length > 0"></image>
						<image src="/static/images/tianjia.png" mode="scaleToFill" class="avatar"></image>
					</view>
				</view>
				<view class="">
					<view class="team_btn" @click="teamPay(0)" v-if="showBtn == 0">{{$t('pagesText.ljpt')}}</view>
					<view class="team_btn" v-else>{{$t('pagesText.ptyjs')}}</view>
					<view class="team_btn_" @click="teamPay(1)">{{$t('pagesText.qsygg')}}</view>
				</view>
			</view>
		</view>

		<u-popup v-model="showPopup" mode="bottom" :border-radius="50">
			<view class="popup_view flex-c alic">
				<view class="header flex sb alic">
					<text>{{$t('order.qrdd')}}</text>
					<image src="../../static/images/popup_close.png" style="border-radius: 0;width: 23rpx;height: 23rpx;"
						mode="scaleToFill" @click="showPopup = false" />
				</view>
				<!-- 商品goodType == 2表示该商品是资料 -->
				<view class="card" v-if="detail.goodType == 2"
					@click="navigate('/subPackage/addressManage/addressManage?choose=choose')">
					<u-cell-item>
						<image slot="icon" src="../../static/images/popup_local.png"
							style="width: 65rpx;height: 65rpx;margin-right: 18rpx;"></image>
						<view slot="title">
							<template v-if="Object.keys(currentAddress).length > 0">
								<text style="margin-right: 12rpx;">{{ currentAddress.userName }}</text>
								<text>{{ currentAddress.userPhone | phoneEncrypt }}</text>
							</template>
							<text v-else>{{$t('pagesText.noAddress')}}</text>
						</view>
						<view slot="label" v-if="currentAddress">{{ currentAddress.province }}{{ currentAddress.city }}{{
							currentAddress.county }}{{ currentAddress.address }}</view>
					</u-cell-item>
				</view>
				<view class="card good_price">
					<u-cell-item :arrow="false">
						<view slot="title" style="margin-right: 60rpx;color:#000;line-height: 110%;">{{ detail.title }}
						</view>
						<v-price-format slot="right-icon" :custom-style="{ color: '#D64046', fontWeight: 'bold' }">
							<text>{{goodInfo.indentMoney}}</text>
						</v-price-format>
					</u-cell-item>
				</view>
				<v-button bg-color="#D64046" border-color="#D64046" width="540rpx" height="80rpx"
					@click="topay()">{{$t('pagesText.ljpt')}}</v-button>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import {
		phoneEncrypt
	} from '@/utils'
	export default {
		data() {
			return {
				groupPageOption: {
					page: 1,
					limit: 10
				},
				backText: this.$t('pagesText.pt'),
				userId: '',
				userInfo: {},
				detail: {},
				enterType: '',
				showPopup: false,
				buyType: '',
				groupId: '',
				goodId: '',
				userId: '',
				avatar: '',
				currentAddress: {},
				groupPageOption: {
					page: 1,
					limit: 10
				},
				groupList: [],
				countdown: 0, // 初始化倒计时显示
				boxWidth: 0,
				showBtn: 0,
				goodData: {},
				goodInfo: {}
			}
		},
		// 我的拼团中分享链接传值
		onLoad(option) {
			console.log(option,"option.goodInfo")
			if (option) {
				this.goodData = JSON.parse(decodeURIComponent(option.goodInfo))
			}
			console.log(this.goodData, 'this.goodData')
			if (uni.getStorageSync('token')) {
				this.checkPartInGroup();
			}
			this.getUserShareInfoById();
		},
		filters: {
			phoneEncrypt(val) {
				return phoneEncrypt(val)
			}
		},
		onShow() {
			uni.$once('getUserAddress', data => {
				this.currentAddress = data
			})
		},
		methods: {
			getTime() {
				this.$Request.getT('/app/common/310').then(res => {
					if (res.code == 0) {
						if (this.groupList.length > 0) {
							this.groupList.map((item) => {
								if (this.goodData.indentId == item.indentId) {
									this.goodInfo = item
									this.countdown = res.data.value * 60
									console.log(this.countdown, "this.countdown")
									let time = new Date(item.expiresTime);
									this.startTime = this.dateToSeconds(time)
									const now = new Date();
									this.countdown = this.dateToSeconds(time) - this.dateToSeconds(now)
									console.log(item.indentId, "item.indentId")
									console.log(this.dateToSeconds(time) - this.dateToSeconds(now), "this.goodData.indentId")
								}
							})
						} else {
							uni.showToast({
								title: this.$t('pagesText.ptyjs'),
								icon: 'none',
								mask: true
							})
							setTimeout(() => {
								uni.switchTab({
									url: '/pages/index/index'
								})
							}, 1500)
						}
					}
				})
			},
			dateToSeconds(dateString) {
				var date = new Date(dateString);
				// 检查日期是否有效
				if (isNaN(date.getTime())) {
					// console.error('Invalid date: ' + dateString);
					return null;
				}
				// 转换为毫秒并除以 1000 得到秒数
				var seconds = Math.floor(date.getTime() / 1000);
				return seconds;
			},
			// 商品拼团倒计时
			downEnd() {
				this.countdown = 0
				console.log(this.countdown, "this.countdownTime")
				if (this.countdown < 0) {
					uni.showToast({
						title: this.$t('pagesText.ptsjEnd'),
						icon: 'none',
						mask: true
					})
					setTimeout(() => {
						uni.switchTab({
							url: '/pages/index/index'
						})
					}, 1500)
				}
			},
			// 获取商品的拼团用户信息
			getUserShareInfoById() {
				this.$Request.getT('/app/topicIndent/getGroupIndentList', {
					...this.groupPageOption,
					goodId: this.goodData.goodId
				}).then(res => {
					if (res.data.records.length > 0) {
						this.groupList = res.data.records.slice(0, 5)
					}
					this.groupList.map(item => {
						item.left
					})
					this.boxWidth = (this.groupList.length + 1) * 100
					this.getTime()
					console.log(this.groupList, "this.groupList")
				})
			},
			// 是否拼团过该商品
			checkPartInGroup() {
				this.$Request.getT('/app/topicIndent/checkPartInGroup', {
					groupId: this.goodData.groupId
				}).then(res => {
					if (res.data == false) {
						// this.getUserInfoById();
						this.getUserAddress();
						this.getGoodDetail()
					} else {
						uni.showToast({
							title: this.$t('pagesText.ygmg'),
							icon: 'none'
						})
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/index/index'
							})
						}, 1500)
					}
				})
			},
			// 获取商品信息
			getGoodDetail() {
				this.$Request.getT('/app/topicGoods/getTopicGoodsInfo', {
					goodId: this.goodData.goodId,
					userId: uni.getStorageSync('userId')
				}).then(res => {
					this.detail = res.data
					if (!this.detail.isBuy) return
				})
			},
			// 获取用户地址
			getUserAddress() {
				// isDefault--0：非默认   1：默认
				this.$Request.getT('/app/address/getAddressList', {
					page: 1,
					limit: 10
				}).then(res => {
					res.data.records.map(item => {
						if (item.isDefault == 1) {
							this.currentAddress = item
						}
					})
				})
			},
			topay() {
				let body = {
					groupId: this.goodData.groupId || '',
					goodId: this.detail.goodId,
					// isGroup: this.enterType === 'group' ? this.detail.courseMoneyType : 0,
					// 修改
					isGroup: '1',
					couponId: '',
					addressId: this.currentAddress.addressId,
					orderType: '3'
				}
				if (this.detail.goodType == 2) {
					if (!body.addressId) {
						return uni.showToast({
							title: this.$t('pagesText.txdzxx'),
							icon: 'none'
						})
					}
				}
				this.$Request.postJson('/app/topicIndent/addTopicIndent', body).then(res => {
					if (res.code == 0) {
						this.navigate('/pages/vipCourseDetails/pay?order=' + encodeURIComponent(JSON.stringify(res.data)) +
							'&payMoney=' + this.goodData.indentMoney)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
					this.showPopup = false;
				})
			},
			teamPay(val) {
				if (val == 0) {
					if (this.detail.goodType == 1) {
						this.showPopup = true;
						console.log(this.showPopup, "this.showPopup")
						// this.topay()
					} else {
						if (!uni.getStorageSync('token')) {
							let data = {
								groupId: this.goodData.groupId,
								goodId: this.goodData.goodId
							}
							uni.redirectTo({
								url: '/pages/public/login?goodInfo=' + encodeURIComponent(JSON.stringify(this.goodData))
							})
						} else {
							this.showPopup = true;
							console.log(this.showPopup, "this.showPopup")
						}
					}
				} else {
					uni.switchTab({
						url: '/pages/index/index'
					})
				}
			}
		},

	}
</script>

<style lang="scss" scoped>
	page {
		background: #F5F7FF;
	}

	.content_list_item_ {
		width: calc(100% - 64rpx);
		border-radius: 16rpx;
		background: #FFFFFF;
		margin: 30rpx auto;
		padding-bottom: 10rpx;
	}

	.content_list_item {
		width: calc(100% - 64rpx);
		height: 218rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		margin: 30rpx auto;

		.content_list_item_left {
			width: 205rpx;
			height: 167rpx;
			border-radius: 16rpx;
			margin: 19rpx 0rpx 39rpx 22rpx;
		}

		.content_list_item_right {
			width: 222rpx;
			height: 167rpx;
			margin: 19rpx 39rpx 31rpx 16rpx;
			display: flex;
			flex-direction: column;
			justify-content: space-between;

			.content_list_item_right_top {
				width: 222rpx;
				margin-top: 7rpx;
				// @extend .ellipese_2;

				text {
					font-weight: bold;
					font-size: 26rpx;
					color: #333333;
				}
			}

			.content_list_item_right_bot {
				width: 222rpx;
				height: 105rpx;
				display: flex;
				flex-direction: column;
				justify-content: flex-end;

				.text_free {
					font-weight: bold;
					font-size: 30rpx;
					color: #D64046;
				}

				.text_number {
					font-weight: 500;
					font-size: 22rpx;
					color: #999999;
					margin-bottom: 5rpx;
				}
			}
		}
	}

	.ellipese_2 {
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: normal;
	}

	.good_price {
		.gold {
			font-size: 28rpx;
		}
	}

	// .main {
	// 	width: 750rpx;
	// 	// height: 1500rpx;
	// 	background-color: #f5f7ff;
	// }

	.team_work {
		// margin: 22rpx 0;
		margin: 0 auto;
	}

	.team_header {
		font-size: 28rpx;
		border-bottom: solid 1rpx #E6E6E6;
		padding: 20rpx 30rpx;
		display: flex;
		justify-content: space-between;
	}

	.avatarlist {
		margin: 40rpx auto;
		margin-bottom: 60rpx;
	}

	.avatarbody {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: #FFFFFF;
	}

	.avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		margin-right: 20rpx;
	}

	.team_btn {
		background-color: #0096FF;
		color: #fff;
		width: calc(100% - 200rpx);
		margin: 50rpx auto 30rpx auto;
		border-radius: 12rpx;
		padding: 20rpx 0;
		text-align: center;
	}

	.team_btn_ {
		border: solid 2rpx #0096FF;
		color: #0096FF;
		width: calc(100% - 200rpx);
		margin: 30rpx auto;
		border-radius: 12rpx;
		padding: 20rpx 0;
		text-align: center;
	}

	.popup_view {
		background: #F5F5F5;
		padding-bottom: 12rpx;
		border-radius: 30rpx 30rpx 0 0;
		overflow: hidden;

		.header {
			width: 100%;
			height: 130rpx;
			padding: 48rpx;
			background: #fff;
			border-radius: 30rpx 30rpx 0 0;
			overflow: hidden;
			margin-bottom: 24rpx;
		}

		image {
			border-radius: 50%;
		}

		.pay_btn {
			width: 643rpx;
			height: 80rpx;
			border-radius: 40rpx;
			background: #D64046;
			color: #fff;
			margin: 0 auto;
		}
	}

	.card {
		background: #fff;
		border-radius: 16rpx;
		width: 710rpx;
		margin: 0 auto;
		margin-bottom: 12rpx;

		.title {
			color: #999;
			padding: 28rpx;
			padding-bottom: 0;
		}
	}

	.good_price {
		.gold {
			font-size: 28rpx;
		}
	}
</style>