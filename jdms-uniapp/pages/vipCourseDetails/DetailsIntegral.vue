<template>
	<view class="main">
		<u-navbar :back-text="backText" :title-bold="true"></u-navbar>
		<view class="courseImage">
			<swiper autoplay circular style="height: 440rpx;"
				v-if="detail.carouselImg && detail.goodType==1&&detail.isBuy == 0 || detail.carouselImg &&detail.goodType == 2">
				<swiper-item v-for="(item, index) in detail.carouselImg.split(',')" :key="index">
					<u-image :src="item" mode="scaleToFill" width="100%" height="100%" />
				</swiper-item>
			</swiper>
			<!-- goodType--1视频  goodType--2资料 -->
			<template v-else-if="detail.goodType === 1 && detail.isBuy === 1">
				<video id="video" :src="currentPlay" ref="videoPlayAuto" @play="videoStatus = 1" @pause="videoStatus = 2"
					@ended="changeCurrentPlay" duration="5"></video>
			</template>
		</view>
		<view class="courseInfo">
			<view class="courseInfo_content">
				<view class="courseInfo_content_top">
					<view class="courseInfo_content_top_left">
						<text>{{ detail.title }}</text>
					</view>
					<button open-type="share" class="courseInfo_content_top_right" @click="share">
						<image src="../../static/images/share.png" mode="heightFix"></image>
					</button>
				</view>
				<view class="courseInfo_content_bot courseInfo_content_bot_">
					<view class="courseInfo_content_bot_left" v-if="detail.label">
						<view class="courseInfo_content_bot_tags" v-for="(item, index) in detail.label.split(',')" :key="index">
							<text>{{ item }}</text>
						</view>
					</view>
				</view>
				<view class="courseInfo_content_bot">
					<template>
						<view class="gold" v-if="detail.goodType == 1 && detail.isBuy == 0">
							<tetx style="font-size: 40rpx;">{{ detail.goldCoinsMoney }}</tetx>{{$t('pagesText.jb')}}
						</view>
						<view class="gold" v-else-if="detail.goodType == 2">
							<tetx style="font-size: 46rpx;">{{ detail.goldCoinsMoney }}</tetx>{{$t('pagesText.jb')}}
						</view>
					</template>
					<view class="courseInfo_content_bot_number">
						{{ detail.buyCount }}{{$t('pageText.rygm')}}
					</view>
				</view>
			</view>
		</view>
		<view class="team_work" v-if="detail.courseMoneyType && enterType === 'group'">
			<u-cell-group :border="false">
				<u-cell-item :value="$t('repeatUse.all')" :border-bottom="false"
					@click="navigate('/pages/vipCourseDetails/allGroup?detail='+ encodeURIComponent(JSON.stringify(detail))+'&enterType='+enterType)">
					<view slot="title" style="font-weight: 700;color:#000">{{ groupList.length }}{{$t('repeatUse.kzjcy')}}</view>
				</u-cell-item>
				<u-cell-item :arrow="false" :border-bottom="false" v-for="(item, index) in groupList" :key="item.groupId">
					<u-image slot="icon" shape="circle" :src="item.avatar" width="70rpx" height="70rpx"></u-image>
					<view slot="title" style="margin-left: 12rpx;">{{ item.userName }}</view>
					<view slot="right-icon" class="value flex alic">
						<view class="flex-c" style="margin-right: 20rpx;">
							<text style="font-size: 26rpx;margin-bottom: 10rpx;font-weight: 700;">{{$t('pagesText.haicha')}}<text
									style="color:#D64046;">1{{$t('unit.ren')}}</text>{{$t('pagesText.pc')}}</text>
							<text style="color:#D64046;font-size: 24rpx;">{{$t('pagesText.sy')}}{{ formatTimeArr[index] }}</text>
						</view>
						<v-button bg-color="#D64046" border-color="transparent"
							@click="pay('cheap', item.groupId, item.userId)">{{$t('pagesText.qpt')}}</v-button>
					</view>
				</u-cell-item>
			</u-cell-group>
		</view>
		<view class="evaluate" v-if="detail.evaluateList">
			<view class="evaluate_title">
				<view class="evaluate_title_left">
					<text>{{$t('order.pj')}}（<text>{{ detail.commentCount }}</text>）</text>
				</view>
				<view class="evaluate_title_right"
					@click="navigate('/pages/vipCourseDetails/commitlist', { goodId: detail.goodId })">
					<text>{{$t('repeatUse.all')}}</text>
				</view>
			</view>
			<view class="evaluate_content" style="margin-top: 40rpx;" v-for="(item, index) in detail.evaluateList"
				:key="index">
				<view class="evaluate_content_left">
					<u-image :src="item.avatar" width="100%" height="100%"></u-image>
				</view>
				<view class="evaluate_content_right">
					<view class="flex alic">
						<text class="evaluate_content_right_name">{{ item.userName }}</text>
						<image src="../../static/images/user_vip.png" style="width:100rpx;height:36rpx;" mode="scaleToFill"
							v-if="item.isVip" />
					</view>
					<text class="evaluate_content_right_comment">{{ item.evaluateMessage }}</text>
				</view>
			</view>
		</view>
		<u-tabs :list="list" :is-scroll="false" :current="current" @change="change" class="course_tabs"
			v-if="detail.goodType == 1"></u-tabs>
		<view class="course_tabs tab_title" v-else>{{$t('pagesText.zlxq')}}</view>
		<!-- 课程详情富文本  -->
		<view class="courseBigImage" v-if="current == 0">
			<!-- <u-image :src="detail.coverImg" mode="widthFix" width="100%" /> -->
			<u-parse :html="detail.courseDetails"></u-parse>
		</view>
		<!-- 课程详情课程表 -->
		<view class="curriculumSchedule" v-if="current == 1">
			<view class="curriculumSchedule_item" hover-class="active" v-for="(it, index) in sectionList" :key="it.sectionId"
				@click="currentVideoIndex = index">
				<view class="curriculumSchedule_item_left">
					<text style="font-size: 38rpx;font-weight: 600;">{{ index > 9 ? `0${index + 1}` : index + 1 }}</text>
				</view>
				<view class="curriculumSchedule_item_right">
					<text class="curriculumSchedule_item_right_title">{{ it.videoTitle }}</text>
					<!-- <text class="curriculumSchedule_item_right_time">{{ it.createTime }}</text> -->
				</view>
				<view class="lock" style="justify-items: flex-end;">
					<u-icon :name="detail.isBuy ? 'play-right' : 'lock'" color="#999" size="32"></u-icon>
				</view>
			</view>

		</view>
		<v-loading :status="status" :load-text="loadText" v-show="current === 1"></v-loading>
		<!-- 课程详情授课老师 -->

		<!-- 撑出底部栏的高度 -->
		<view style="width: 100%;height:120rpx;">

		</view>
		<view class="bottomNavbar">
			<view class="bottomNavbar_left">
				<view class="bottomNavbar_left_btn" @click="navigate('/my/setting/customer')">
					<image src="@/static/images/ears.png" mode=""></image>
					<text>{{$t('pageText.zx')}}</text>
				</view>
			</view>
			<view class="bottomNavbar_right">
				<view class="teach_btn flex jc alic" style="font-weight: bold;" @click="showPopup = true"
					v-if="detail.goodType == 1&&detail.isBuy == 0 && !detail.courseMoneyType && detail.goldCoinsMoney > 0">
					{{ detail.goldCoinsMoney }}{{$t('pagesText.jb')}}
				</view>
				<!-- 免费  开始学习 -->
				<view class="teach_btn flex jc alic" @click="toStudy" v-else-if="detail.isBuy == 1&&detail.goodType == 1">
					<!-- 开始学习 -->
					{{btntext}}
				</view>

			</view>
		</view>
		<u-popup v-model="showPopup" mode="bottom" :border-radius="50">
			<view class="popup_view flex-c alic">
				<view class="header flex sb alic">
					<text>{{$t('order.qrdd')}}</text>
					<image src="../../static/images/popup_close.png" style="border-radius: 0;width: 23rpx;height: 23rpx;"
						mode="scaleToFill" @click="closeShowPopup" />
				</view>
				<view class="card good_price">
					<u-cell-item :arrow="false">
						<view slot="title"
							style="margin-right: 60rpx;color:#000;line-height: 110%;font-size: 30rpx;font-weight: bold;">
							{{ detail.title }}
						</view>
						<view class="gold">{{ detail.goldCoinsMoney }}<text style="font-size: 22rpx;">{{$t('pagesText.jb')}}</text>
						</view>
					</u-cell-item>
				</view>
				<view class="pay_btn flex jc alic active" @click="confirm">
					<view style="font-weight: bold;color:#fff;"><text
							style="font-size: 38rpx;">{{ detail.goldCoinsMoney }}</text>{{$t('pagesText.jbdh')}}</view>
					<text>{{ payBtnText }}</text>
				</view>
			</view>
		</u-popup>
		<u-modal v-model="showConfirmModal" :content="$t('pagesText.suredh')+'？'" :show-cancel-button="true"
			@confirm="payConfirm" :confirm-text="$t('btnText.qd')" :cancel-text="$t('pageText.dcl')"
			:title="$t('repeatUse.ts')"></u-modal>
		<u-modal v-model="showShareModal" :content="shareContent" :show-cancel-button="true"
			:confirm-text="$t('repeatUse.copy')" :cancel-text="$t('repeatUse.qx')" :title="$t('repeatUse.fxlj')"
			@confirm="shareConfirm"></u-modal>
	</view>
</template>

<script>
	import payMixin from './payMixin'
	import {
		phoneEncrypt
	} from '@/utils'
	import collect from '@/static/images/collect.png'
	import collect_ac from '@/static/images/collect_ac.png'
	import config from '../../config.js'
	export default {
		mixins: [payMixin],
		data() {
			return {
				shareInfo: {
					title: '这是分享的标题',
					path: '/pages/index/index',
					imageUrl: 'https://example.com/path/to/your/image.png'
				},
				showTeamBox: false,
				showShareModal: false,
				shareContent: '',
				enterType: '',
				time: 0,
				videoContext: null,
				videoStatus: 2,
				collect,
				collect_ac,
				status: 'loading',
				loadText: this.$t('repeatUse.zzjz') + '...',
				sectionPageOption: {
					page: 1,
					limit: 10
				},
				currentVideoIndex: 0,
				currentPlay: '',
				goodId: null,
				groupList: [],
				groupId: null,
				timeContainer: [],
				sectionList: [],
				showPopup: false,
				showConfirmModal: false,
				type: null,
				timer: null,
				price: '',
				payBtnText: '',
				detail: {},
				groupPageOption: {
					page: 1,
					limit: 10
				},
				groupFinished: false,
				list: [{
					name: this.$t('pagesText.kcxq'),
					id: 1
				}, {
					name: this.$t('pagesText.kcb'),
					id: 2
				}],
				current: 0,
				backText: '',
				buyType: '',
				orderType: '', //订单类型
				btntext: '',
				ticketList: [],
				discountId: '',
				videoState: false
			};
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
		},
		filters: {
			phoneEncrypt(val) {
				return phoneEncrypt(val)
			}
		},
		watch: {
			currentPlay(newv) {
				console.log(newv, "newv");
			},
			currentVideoIndex: {
				handler(newv) {
					if (!this.detail.isBuy) return
					this.$nextTick(() => {
						this.currentPlay = this.sectionList[newv]?.videoLink || ''
					})
				}
			},
			videoStatus(newv) {
				console.log(newv, "videoStatus")
				this.$Request.getT('/app/topicCourseMinute/saveCourseMinute', {
					status: newv,
					typeId: this.detail.typeId
				}).then(res => {
					console.log(res, "res")
				})
			},
			showPopup(newv) {
				if (!newv) {
					// 关闭弹窗  删除groupId
					this.groupId = null
					this.buyType = ''
				}
			}
		},
		methods: {
			closeShowPopup() {
				this.showPopup = false
			},
			// 分享给朋友
			setShareAppMessage(shareInfo) {
				return function() {
					return {
						title: shareInfo.title,
						path: shareInfo.path,
						imageUrl: shareInfo.imageUrl
					};
				};
			},

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
			share() {
				// #ifdef H5
				this.showShareModal = true
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
			},
			changeCurrentPlay() {
				this.currentVideoIndex = this.currentVideoIndex + 1;
			},
			// 积分支付
			payConfirm() {
				this.$Request.getT('/app/topicGoods/goldCoinsExchange', {
					goodId: this.detail.goodId
				}).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('pageText.dhcg'),
							mask: true
						})
						// this.$store.dispatch('userModule/getUserInfoById')
						setTimeout(() => {
							uni.redirectTo({
								url: `/pages/vipCourseDetails/DetailsIntegral?goodId=${this.detail.goodId}&enterType=${this.enterType}`
							})
						}, 1000);
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none',
							mask: true
						})
					}
				})
				this.showPopup = false
			},
			videoPlay() {
				if (this.videoState) {
					this.$refs.videoPlayAuto.pause()
					this.videoState = false
				} else {
					this.$refs.videoPlayAuto.play()
					this.videoState = true
				}
			},
			toStudy() {
				this.orderType = '1';
				if (this.detail.isBuy == 0) {
					this.showConfirmModal = true
				} else {
					this.videoPlay()
					uni.pageScrollTo({
						scrollTop: 0,
						duration: 300
					})
				}
				setTimeout(() => {
					if (this.detail.goodType == 1) {
						this.getGoodDetail()
					}
				}, 1000)
			},
			confirm() {
				this.showConfirmModal = true
			},
			change(index) {
				this.current = index;
			},
			// 获取商品信息
			async getGoodDetail() {
				this.$Request.getT('/app/topicGoods/getTopicGoodsInfo', {
					goodId: this.goodId,
					userId: uni.getStorageSync('userId')
				}).then(res => {
					this.detail = res.data
					console.log(this.detail, "detail")
					this.backText = res.data.title
					if (this.detail.goodType == 1 && this.detail.isBuy == 1) {
						this.btntext = this.$t('pageText.ksxx')
					}
					if (!this.detail.isBuy) return
				})
			},
			// 获取课程列表
			getSectionListById() {
				if (this.status === 'normal') return
				this.$Request.getT('/app/topicSection/getTopicSectionByGoodsId', {
					...this.sectionPageOption,
					goodId: this.goodId
				}).then(res => {
					this.sectionList = [...this.sectionList, ...res.data.records]
					if (this.sectionPageOption.page == 1) {
						// 首次赋值视频
						this.currentPlay = this.sectionList[0]?.videoLink
					}
					if (res.data.records.length < this.sectionPageOption.limit) {
						this.status = 'normal'
						this.loadText = this.$t('load.nomore') + '~'
						return
					}
					this.$set(this.sectionPageOption, 'page', this.sectionPageOption.page + 1)
				})
			}
		},
		async onLoad({
			goodId,
			enterType
		}) {
			// #ifdef H5
			this.shareContent = config.BASE_URL + this.$route.fullPath
			// #endif
			uni.showShareMenu({
				withShareTicket: true,
				menus: ['shareAppMessage', 'shareTimeline']
			});
			this.onShareAppMessage = this.setShareAppMessage(this.shareInfo);
			this.onShareTimeline = this.setShareTimeline(this.shareInfo);
			if (!goodId) {
				uni.showToast({
					title: this.$t('pageText.spbcz'),
					icon: 'none',
					mask: true
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 1500);
			}
			this.enterType = enterType
			this.goodId = goodId
			this.getGoodDetail()
			this.getSectionListById()
		},
		onReady() {
			this.videoContext = uni.createVideoContext('video')
			console.log(this.$store.state.userModule.user);

		},
		onHide() {
			this.videoContext.pause();
			// uni.$off('getDiscountId')
		},
		onShow() {
			if (this.videoContext) {
				this.videoContext.play()
			}
			uni.$on('getDiscountId', (data) => {
				this.discountId = data.discountId
				this.$nextTick(() => {
					console.log(data.discountId, 'this.discountId ')
				})
			})
			console.log(this.discountId, 'this.discountId --out')
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background: #F5F7FF;
	}

	.team_box {
		padding-top: 72rpx;
		padding-bottom: 32rpx;
		width: 600rpx;
		display: flex;
		flex-direction: column;
		align-items: center;

		/deep/.btn {
			margin: 0 !important;
		}

		.box_title {
			margin-bottom: 24rpx;
		}

		.team_avatar {
			margin-bottom: 24rpx;

			image {
				width: 80rpx;
				height: 80rpx;
				border-radius: 50%;
			}

			.default {
				width: 80rpx;
				height: 80rpx;
				border-radius: 50%;
				background: gray;
				display: flex;
				justify-content: center;
				align-items: center;
				color: #fff;
				font-size: 40rpx;
			}
		}
	}

	.good_price {
		.gold {
			font-size: 38rpx;
		}
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
			font-size: 32rpx;
			font-weight: bold;
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

	.main {
		width: 750rpx;
		// height: 1500rpx;
		background-color: #f5f7ff;
	}

	.courseImage {
		width: 750rpx;
		// height: 440rpx;
		border-radius: 16rpx 16rpx 0rpx 0rpx;

		video {
			width: 100%;
		}
	}

	.gold {
		color: #D64046;
		font-weight: bold;
		font-size: 36rpx;
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

	.font_bold {
		font-weight: bold;
	}

	.courseInfo {
		width: 750rpx;
		// height: 194rpx;
		background-color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		padding-top: 36rpx;
		padding-bottom: 24rpx;

		.courseInfo_content {
			width: 686rpx;
			// height: 131rpx;
			padding-bottom: 20rpx;

			.courseInfo_content_top {
				width: 686rpx;
				// height: 74rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.courseInfo_content_top_left {
					width: 584rpx;
					// height: 74rpx;

					text {
						font-weight: bold;
						font-size: 32rpx;
						color: #333333;
					}
				}

				.courseInfo_content_top_right {
					position: relative;
					width: 30rpx;
					height: 40rpx;
					background: transparent;
					border: none;
					border-radius: 0;

					&::after {
						display: none;
					}

					image {
						display: block;
						max-width: 1000vw;
						width: 30rpx !important;
						height: 40rpx !important;
					}
				}

			}

			.courseInfo_content_bot_ {
				width: calc(100% - 100rpx);
				margin: auto;
				overflow: hidden;
			}

			.courseInfo_content_bot {
				width: 686rpx;
				// height: 33rpx;
				margin-top: 14rpx;
				display: flex;
				justify-content: space-between;
				align-items: center;

				.courseInfo_content_bot_left {
					// height: 41rpx;
					display: flex;
					justify-content: flex-start;
					align-items: center;
					flex: 1;

					.courseInfo_content_bot_tags {
						padding: 6rpx 14rpx;
						// height: 41rpx;
						background: #FFF4F3;
						border-radius: 10rpx;
						text-align: center;
						white-space: nowrap;

						text {
							font-family: PingFang SC;
							// font-weight: 500;
							font-size: 26rpx;
							color: #EF6870;
						}

						margin: 14rpx 11rpx 0 0;
					}

				}

				.courseInfo_content_bot_number {
					font-family: PingFang SC;
					font-size: 24rpx;
					color: #999999;
					padding-top: 8rpx;
				}

				.courseInfo_content_bot_right {

					font-size: 26rpx;
					color: #D64046;
					margin-top: 24rpx;
				}
			}
		}
	}

	.evaluate {
		width: 750rpx;
		background: #FFFFFF;
		margin-top: 22rpx;
		padding: 45rpx 29rpx 45rpx 35rpx;
		height: fit-content;

		.evaluate_title {
			height: 29rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;

			.evaluate_title_left {
				font-weight: bold;
				font-size: 30rpx;
				color: #333333;
			}

			.evaluate_title_right {
				font-weight: 500;
				font-size: 26rpx;
				color: #333333;
			}
		}

		.evaluate_content {
			margin-top: 37rpx;
			display: flex;

			.evaluate_content_left {
				width: 70rpx;
				height: 70rpx;
				border-radius: 50%;
				overflow: hidden;
			}

			.evaluate_content_right {
				max-width: 600rpx;
				margin-left: 15rpx;
				display: flex;
				flex-direction: column;

				.evaluate_content_right_name {
					font-weight: 500;
					font-size: 28rpx;
					color: #666666;
					margin-right: 12rpx;
				}

				.evaluate_content_right_comment {
					font-weight: 500;
					font-size: 26rpx;
					color: #333333;
				}

			}
		}
	}

	.course_tabs {
		margin-top: 22rpx;
	}

	.tab_title {
		font-size: 30rpx;
		font-weight: bold;
		padding: 18rpx;
		display: flex;
		justify-content: center;
		background: #fff;
	}

	.courseBigImage {
		width: 100%;
		padding: 30rpx;
		background-color: #FFFFFF;
	}


	.curriculumSchedule {
		width: 100%;
		height: auto;
		background-color: #FFFFFF;
		overflow: scroll;

		.curriculumSchedule_item {
			padding: 29rpx 60rpx 20rpx 38rpx;
			display: flex;
			align-items: center;
			background: #fff;

			.curriculumSchedule_item_left {
				font-weight: 400;
				font-size: 28rpx;
				height: 66rpx;
				color: #999999;
				display: flex;
				flex-direction: column;
				justify-content: center;
			}

			.curriculumSchedule_item_right {
				display: flex;
				flex: 1;
				flex-direction: column;
				align-items: flex-start;
				margin-left: 35rpx;

				.curriculumSchedule_item_right_title {
					font-weight: 500;
					font-size: 30rpx;
					color: #333333;
				}

				.curriculumSchedule_item_right_time {
					font-weight: 500;
					font-size: 24rpx;
					color: #999999;
				}
			}
		}
	}

	.lecturer {
		width: 100%;
		height: auto;
		background-color: #FFFFFF;
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		overflow: scroll;

		.lecturer_item {
			width: 660rpx;
			height: auto;
			display: flex;
			justify-content: space-between;
			margin-top: 60rpx;
			margin-bottom: 50rpx;

			.lecturer_item_image {
				display: flex;
				flex-direction: column;
				justify-content: flex-start;
				align-items: center;

				image {
					width: 114rpx;
					height: 114rpx;
				}
			}

			.lecturer_item_text {
				display: flex;
				flex-direction: column;
				align-items: flex-start;
				margin-left: 24rpx;

				.lecturer_item_text_name {
					font-weight: bold;
					font-size: 30rpx;
					color: #333333;

				}

				.lecturer_item_text_tag {
					font-weight: 500;
					font-size: 26rpx;
					color: #666666;
					margin-top: 17rpx;
				}

				.lecturer_item_text_introduce {
					font-weight: 500;
					font-size: 26rpx;
					color: #666666;
					margin-top: 27rpx;
				}
			}
		}
	}

	.bottomNavbar {
		width: 100%;
		height: 120rpx;
		background-color: #FFFFFF;
		position: fixed;
		bottom: 0;
		padding: 19rpx 31rpx 18rpx 31rpx;
		display: flex;
		justify-content: space-between;

		.bottomNavbar_left {

			// background-color: #0096FF;
			display: flex;

			.bottomNavbar_left_btn {
				display: flex;
				flex-direction: column;
				// margin-right: 35rpx;
				align-items: center;

				image {
					width: 36rpx;
					height: 35rpx;
					margin-top: 5rpx;
				}

				text {
					width: 55rpx;
					height: 27rpx;
					font-weight: 500;
					font-size: 26rpx;
					color: #333333;
					margin-top: 11rpx;
					white-space: nowrap;
				}
			}
		}

		.bottomNavbar_right {
			display: flex;
			justify-content: space-between;
			white-space: nowrap;

			.teach_btn {
				width: 620rpx;
				height: 82rpx;
				background: #D64046;
				color: #fff;
				font-size: 30rpx;
				font-weight: cold;
				border-radius: 41rpx;
			}

			.bottomNavbar_right_left {
				padding: 14rpx 76rpx 13rpx 90rpx;
				display: flex;
				flex-direction: column;
				background: #FFE0E1;
				border-radius: 41rpx 0rpx 0rpx 41rpx;
				align-items: center;
				white-space: nowrap;

				.bottomNavbar_right_left_price {
					white-space: nowrap;
					font-weight: bold;
					font-size: 30rpx;
					color: #D64046;
					line-height: 1;
				}

				.bottomNavbar_right_left_title {
					width: 96rpx;
					font-weight: 500;
					font-size: 24rpx;
					color: #D64046;
				}

			}

			.team_btn {
				background: #D64046 !important;

				* {
					color: #fff !important;
				}
			}

			.bottomNavbar_right_right {
				padding: 14rpx 83rpx 16rpx 83rpx;
				display: flex;
				flex-direction: column;
				background: linear-gradient(65deg, #FEF2DC 0%, #FEE4BF 100%);
				border-radius: 0rpx 41rpx 41rpx 0rpx;
				align-items: center;

				.bottomNavbar_right_right_price {
					font-weight: bold;
					font-size: 30rpx;
					color: #74552B;
					line-height: 1;
				}

				.bottomNavbar_right_right_title {
					width: 96rpx;
					font-weight: 500;
					font-size: 24rpx;
					color: #74552B;
					white-space: nowrap;
				}
			}
		}
	}

	.team_work {
		margin: 22rpx 0;
	}

	/deep/.btn {
		width: 128rpx;
		height: 56rpx;
		font-size: 26rpx;
	}

	/deep/.u-cell-item-box {
		.u-cell {

			// &:not(:first-child) {
			// 	padding-top: 0 !important;
			// }
			&:first-child {
				padding-bottom: 0 !important;
			}
		}
	}

	/deep/.u-back-text {
		font-weight: bold !important;
		color: #000 !important;
	}

	.payline {
		position: relative;

		&:first-child {

			&::before {
				content: '';
				position: absolute;
				width: calc(100vw);
				height: 2rpx;
				background: #eee;
				bottom: -24rpx;
			}
		}
	}

	.discount {
		color: #DE4046 !important;
	}

	/deep/.u-model__content__message {
		word-break: break-all;
	}
</style>