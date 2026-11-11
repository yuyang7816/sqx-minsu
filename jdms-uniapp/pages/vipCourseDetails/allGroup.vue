<template>
	<view class="main">
		<u-navbar :back-text="backText" :title-bold="true"></u-navbar>
		<view class="team_work">
			<u-cell-group :border="false">
				<u-cell-item :arrow="false" :border-bottom="false" v-for="(item,index) in groupList" :key="item.groupId">
					<u-image slot="icon" shape="circle" :src="item.avatar" width="70rpx" height="70rpx"></u-image>
					<view slot="title" class="slot">{{item.userName}}</view>
					<view slot="right-icon" class="value flex alic">
						<view class="flex-c" style="margin-right: 20rpx;">
							<text style="font-size: 26rpx;margin-bottom: 10rpx;font-weight: 700;">{{$t('pagesText.haicha')}}<text
									style="color:#D64046;">1{{$t('unit.ren')}}</text>{{$t('pagesText.pc')}}</text>
							<view style="color:#D64046;font-size: 24rpx;">{{$t('pagesText.sy')}}<u-count-down
									:timestamp="item.expiresTime" color="#D64046" font-size="24" @end="downEnd(item.expiresTime,index)"
									:show-hours="false"></u-count-down></view>
						</view>
						<v-button bg-color="#D64046" border-color="transparent"
							@click="confirm(item.groupId,item.userId)">{{$t('pagesText.qpt')}}</v-button>
					</view>
				</u-cell-item>
			</u-cell-group>
		</view>
		<u-popup v-model="showPopup" mode="bottom" :border-radius="50">
			<view class="popup_view flex-c alic">
				<view class="header flex sb alic">
					<text>{{$t('order.qrdd')}}</text>
					<image src="../../static/images/popup_close.png" style="border-radius: 0;width: 23rpx;height: 23rpx;"
						mode="scaleToFill" @click="showPopup = false" />
				</view>
				<view class="card" v-if="detail.goodType == 2"
					@click="navigate('/subPackage/addressManage/addressManage?choose=choose')">
					<u-cell-item>
						<image slot="icon" src="../../static/images/popup_local.png"
							style="width: 65rpx;height: 65rpx;margin-right: 18rpx;"></image>
						<view slot="title">
							<template v-if="Object.keys(currentAddress).length">
								<text style="margin-right: 12rpx;">{{ currentAddress.userName }}</text>
								<text>{{ currentAddress.userPhone | phoneEncrypt }}</text>
							</template>
							<text v-else>{{$t('pagesText.noAddress')}}</text>
						</view>
						<view slot="label" v-if="currentAddress">{{ currentAddress.province }}{{ currentAddress.city }}{{
							currentAddress.county }}{{ currentAddress.address }}</view>
					</u-cell-item>
				</view>
				<v-button bg-color="#D64046" border-color="#D64046" width="540rpx" height="80rpx"
					@click="topay()">{{$t('pagesText.ljpt')}}</v-button>
			</view>
		</u-popup>
		<u-empty mode="search" :text="$t('repeatUse.noData')" :margin-top="-300" v-if="!groupList.length"></u-empty>
	</view>
</template>

<script>
	import {
		phoneEncrypt
	} from '@/utils'
	export default {
		data() {
			return {
				groupList: [],
				groupPageOption: {
					page: 1,
					limit: 10
				},
				groupFinished: false,
				backText: this.$t('repeatUse.back'),
				goodId: null,
				timeContainer: [],
				detail: {},
				enterType: '',
				showPopup: false,
				buyType: '',
				groupId: '',
				currentAddress: {},
			}
		},
		onLoad({
			detail,
			enterType
		}) {
			this.detail = JSON.parse(decodeURIComponent(detail));
			console.log(this.detail, "detail")
			this.enterType = enterType
			this.getTeamListById()
			this.getUserAddress()
		},
		onShow() {
			uni.$once('getUserAddress', data => {
				this.currentAddress = data
			})
		},
		computed: {
			vipPrice() {
				return this.detail.nowBuyMoney * (this.detail.vipRate || '1')
			},
			formatTimeArr() {
				return this.timeContainer.map(d => {
					let m = Math.floor(d / 60)
					let s = Math.floor(d - m * 60)
					return `${m}:${s > 9 ? s : '0' + s}`
				})
			},
			// ...mapGetters(['currentAddress'])
		},
		filters: {
			phoneEncrypt(val) {
				return phoneEncrypt(val)
			}
		},
		watch: {
			showPopup(newv) {
				if (!newv) {
					// 关闭弹窗  删除groupId
					this.groupId = null
					this.buyType = ''
				}
			}
		},
		methods: {
			// 倒计时结束
			downEnd(expiresTime, index) {
				// this.groupList[index].expiresTime = 0
				// expiresTime = 0
				// if (expiresTime < 0) {
				this.groupFinished = false
				setTimeout(() => {
					this.getTeamListById();
				}, 2000)
				// }
				console.log(expiresTime, 'expiresTime')
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
			// 获取拼团商品的拼团信息
			getTeamListById() {
				if (this.groupFinished) return
				this.$Request.getT('/app/topicIndent/getGroupIndentList', {
					...this.groupPageOption,
					goodId: this.detail.goodId
				}).then(res => {
					this.groupPageOption.page == 1 && (this.groupList = [])
					this.groupList = [...this.groupList, ...res.data.records]
					this.groupList.map((item, index) => {
						const currentTime = new Date();
						item.expiresTime = Math.max(0, this.dateToSeconds(item.expiresTime) - this.dateToSeconds(
							currentTime));
					})
					if (res.data.records.length < this.groupPageOption.limit) {
						this.groupFinished = true
					}
				})
			},
			// 拼团时间--转秒
			dateToSeconds(dateString) {
				var date = new Date(dateString);
				if (isNaN(date.getTime())) {
					return null;
				}
				// 转换为毫秒并除以 1000 得到秒数
				var seconds = Math.floor(date.getTime() / 1000);
				return seconds;
			},
			topay() {
				let body = {
					groupId: this.groupId || '',
					goodId: this.detail.goodId,
					isGroup: this.enterType === 'group' ? this.detail.courseMoneyType : 0,
					couponId: '',
					addressId: this.currentAddress.addressId,
					orderType: '3'
				}
				this.buyType === 'cheap' && this.detail.vipRate && (body.isVip = 1)
				if (this.detail.goodType == 2) {
					if (!body.addressId) {
						return uni.showToast({
							title: this.$t('pagesText.txdzxx'),
							icon: 'none'
						})
					}
				}
				this.$Request.postJson('/app/topicIndent/addTopicIndent', body).then(res => {
					if (res.code == 50001)
						// 用户不是会员
						return this.navigate('/subPackage/vip/vip')

					if (res.code == 500) {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					} else {
						this.showPopup = false;
						uni.showLoading({
							title: '',
							mask: true
						})
						this.navigate('/pages/vipCourseDetails/pay?order=' + encodeURIComponent(JSON.stringify(res.data)) +
							'&payMoney=' + this.detail.groupPrice)
					}


				})
			},
			confirm(groupId, userId) {
				this.groupId = groupId;
				if (userId) {
					// 参与别人的拼团
					if (userId === uni.getStorageSync('userId')) {
						return uni.showToast({
							title: this.$t('pagesText.bncyzj'),
							icon: 'none'
						})
					}
					if (this.detail.goodType == 2) {
						this.showPopup = true;
					} else {
						this.topay()
					}
				}
			},
			createTimeContainer(expirseArr) {
				return expirseArr.map(item => (new Date(item).getTime() - Date.now()) / 1000)
			},
		},

	}
</script>

<style lang="scss" scoped>
	page {
		background: #F5F7FF;
	}

	.slot {
		margin-left: 12rpx;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: normal;
		width: 200rpx;
	}

	.main {
		height: 100vh;
		width: 750rpx;
		// height: 1500rpx;
		background-color: #f5f7ff;
	}

	.team_work {
		// margin: 22rpx 0;
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