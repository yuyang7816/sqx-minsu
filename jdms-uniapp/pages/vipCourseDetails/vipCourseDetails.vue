<template>
	<view>
		<u-navbar v-if="isShowTitle" :back-text="backText" :title-bold="true"></u-navbar>
		<view class="courseImage">
			<swiper autoplay circular style="height: 440rpx;"
				v-if="detail.carouselImg && detail.goodType==1&&detail.isBuy == 0 || detail.carouselImg &&detail.goodType == 2">
				<swiper-item v-for="(item, index) in detail.carouselImg.split(',')" :key="index">
					<u-image :src="item" mode="scaleToFill" width="100%" height="100%" />
				</swiper-item>
			</swiper>
			<!-- {{detail.goodType === 1 && detail.isBuy === 1}} -->
			<video id="myVideo" v-else-if="detail.goodType === 1 && detail.isBuy === 1" :src="currentPlay" controls
				@play="onVideoPlay(1)" @pause="onPause(2)" @ended="onVideoEnded" ref="videoPlayer" />
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
					<view class="courseInfo_content_bot_left " v-if="detail.label">
						<view class="courseInfo_content_bot_tags" v-for="(item, index) in detail.label.split(',')" :key="index">
							<text>{{ item }}</text>
						</view>
					</view>
				</view>
				<view class="courseInfo_content_bot" style="margin-top: 10rpx;">
					<template v-if="detail.isBuy == 0">
						<v-price-format :custom-style="{ fontSize: '36rpx', color: '#D64046' }"
							v-if="detail.courseMoneyType || !detail.goldCoinsMoney"><text style="font-size: 46rpx;">{{
								detail.nowBuyMoney
							}}</text></v-price-format>
						<view class="gold" v-else>{{ detail.goldCoinsMoney }}{{$t('pagesText.jb')}}</view>
					</template>
					<view class="courseInfo_content_bot_number">
						{{ detail.buyCount }}{{$t('pageText.rygm')}}
					</view>
				</view>
			</view>
		</view>
		<view class="team_work" v-if="detail.courseMoneyType && enterType === 'group' ">
			<u-cell-group :border="false">
				<u-cell-item :value="$t('repeatUse.all')" :border-bottom="false" @click="routeGo">
					<view slot="title" style="font-weight: 700;color:#000">{{ groupList.length }}{{$t('repeatUse.kzjcy')}}</view>
				</u-cell-item>
				<u-cell-item :arrow="false" :border-bottom="false" v-for="(item, index) in groupList">
					<u-image slot="icon" shape="circle" :src="item.avatar" width="70rpx" height="70rpx"></u-image>
					<view slot="title" class="titleSolt">{{ item.userName }}</view>
					<view slot="right-icon" class="value flex alic">
						<view class="flex-c" style="margin-right: 20rpx;">
							<text style="font-size: 26rpx;margin-bottom: 10rpx;font-weight: 700;">{{$t('pagesText.haicha')}}<text
									style="color:#D64046;">1{{$t('unit.ren')}}</text>{{$t('pagesText.pc')}}</text>
							<view style="color:#D64046;font-size: 24rpx;display: flex;align-items: center;">{{$t('pagesText.sy')}}
								<u-count-down :timestamp="item.expiresTime" color="#D64046" font-size="24"
									@end="downEnd(item.expiresTime,index)" :show-hours="false"></u-count-down>
							</view>
						</view>
						<v-button bg-color="#D64046" border-color="transparent"
							@click="pay('cheap', item.groupId, item.userId)">{{$t('pagesText.qpt')}}</v-button>
					</view>
				</u-cell-item>
			</u-cell-group>
		</view>
		<view class="evaluate" v-if="detail.evaluateList && detail.evaluateList.length > 0">
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
		<view class="course_tabs" v-if="detail.goodType == 1">
			<u-tabs :list="list" :is-scroll="false" :current="current" @change="change"></u-tabs>
		</view>

		<view class="course_tabs tab_title" v-else>{{$t('pagesText.zlxq')}}</view>
		<!-- 课程详情富文本  -->
		<view class="courseBigImage" v-if="current == 0">
			<!-- <u-image :src="detail.coverImg" mode="widthFix" width="100%" /> -->
			<u-parse :html="detail.courseDetails"></u-parse>
		</view>
		<!-- 课程详情课程表 -->
		<view class="curriculumSchedule" v-if="current == 1">
			<view class="curriculumSchedule_item" hover-class="active" v-for="(it, index) in sectionList" :key="it.sectionId"
				@click="changeVideo(index)">
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
			<v-loading :status="status" :load-text="loadText" v-show="current === 1"></v-loading>
		</view>
		<!-- 课程详情授课老师 -->

		<!-- 撑出底部栏的高度 -->
		<view style="width: 100%;height:120rpx;">

		</view>
		<view class="bottomNavbar">
			<view class="bottomNavbar_">
				<view class="bottomNavbar_left">
					<view class="bottomNavbar_left_btn" @click="customerType">
						<image src="@/static/images/ears.png" mode=""></image>
						<text>{{$t('pageText.zx')}}</text>
					</view>
				</view>
				<view class="bottomNavbar_right">
					<view class="teach_btn flex jc alic" style="font-weight: bold;" @click="showPopup = true"
						v-if="!detail.isBuy && !detail.courseMoneyType && detail.goldCoinsMoney">
						{{ detail.goldCoinsMoney }}{{$t('pagesText.jb')}}
					</view>
					<!-- 免费  开始学习 -->
					<view class="" v-else-if="detail.goodType == 1&&detail.isBuy == 1">
						<view class="teach_btn flex jc alic" @click="toStudy"
							v-if="detail.isBuy == 1 || (detail.goldCoinsMoney == 0 && detail.nowBuyMoney == 0 && detail.courseMoneyType == 0 && detail.groupPrice == 0)">
							{{$t('pageText.ksxx')}}
						</view>
					</view>
					<view class="teach_btn flex jc alic" @click="toStudy"
						v-else-if=" detail.goodType==2 && detail.nowBuyMoney == 0 || detail.goodType==1 && detail.nowBuyMoney == 0 ">
						{{$t('unit.yuan')}}0{{$t('bar.xd')}}
					</view>
					<template v-else-if="type != 2">
						<view class="bottomNavbar_right_left" @click="pay">
							<text
								class="bottomNavbar_right_left_price">{{$t('unit.yuan')}}{{ (detail.nowBuyMoney || 0).toFixed(2) }}</text>
							<text class="bottomNavbar_right_left_title">{{$t('goods.pay')}}</text>
						</view>
						<view class="bottomNavbar_right_right" :class="{ team_btn: type == 3 }" @click="pay('cheap')">
							<text class="bottomNavbar_right_right_price">{{$t('unit.yuan')}}{{ (detail.courseMoneyType && enterType === 'group' ?
								detail.groupPrice :
								vipPrice || 0).toFixed(2) || '0.00'
							}}</text>
							<text class="bottomNavbar_right_right_title">{{ detail.courseMoneyType && enterType === 'group' ? $t('pageText.fqpt') :
								$t('pageText.hygm')
							}}</text>
						</view>
					</template>
					<!-- 名师直接购买 -->
					<view class="teach_btn flex jc alic" @click="pay" v-else>
						{{$t('goods.pay')}}
					</view>
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

				<view class="card good_price">
					<u-cell-item :arrow="false">
						<view slot="title"
							style="margin-right: 60rpx;color:#000;line-height: 110%;font-size: 30rpx;font-weight: bold;">
							{{ detail.title }}
						</view>
						<view class="" v-if="detail.courseMoneyType || !detail.goldCoinsMoney">
							<text style="font-weight: bold;font-size: 24rpx;color:#D64046;">{{$t('unit.yuan')}}<text
									style="font-weight: bold;font-size: 38rpx;"
									v-if="discountAmount > 0">{{Number(price - discountAmount).toFixed(2)}}</text>
								<text v-else style="font-weight: bold;font-size: 38rpx;">{{Number(price).toFixed(2)}}</text></text>
						</view>
						<!-- <view class="gold" v-else>{{ detail.goldCoinsMoney }}金币</view> -->
					</u-cell-item>
				</view>
				<!-- 添加 -->
				<view class="card good_price_">
					<u-cell-item :border-bottom="false"
						:value="!ticketList.length ? $t('pagesText.zwkyyhq') : (!discountAmount ? `${$t('pagesText.dqy')} ${ticketList.length} ${$t('pagesText.zkyyhq')}` : `-${$t('unit.yuan')}${discountAmount}`)"
						:value-style="{ color: !discountAmount ? '#999' : '#DE4046' }"
						@click="navigate(`/my/ticket/myList`,{price:price,couponId:discountId})">
						<image slot="icon" src="../../static/images/popup_yhq.png" style="width: 50rpx;height:50rpx;"></image>
						<view slot="title" style="margin-left: 16rpx;color:#000;">{{$t('yhj.name')}}</view>
					</u-cell-item>
				</view>
				<view class="pay_btn flex jc alic active" @click="confirm" v-if="btnStatus == 0">
					<v-price-format :custom-style="{ fontWeight: 'bold' }"
						v-if="detail.courseMoneyType || !detail.goldCoinsMoney">
						<text v-if="discountAmount > 0"
							style="font-weight: bold;font-size: 38rpx;padding-left: 8rpx;">{{Number(price - discountAmount).toFixed(2)}}</text>
						<text v-else
							style="font-weight: bold;font-size: 38rpx;padding-left: 8rpx;">{{Number(price).toFixed(2)}}</text>
					</v-price-format>
					<view style="font-weight: bold;color:#fff;" v-else>{{ detail.goldCoinsMoney }}{{$t('pagesText.jbdh')}}</view>
					<text>{{ payBtnText }}</text>
				</view>
				<view class="pay_btn flex jc alic active" @click="addOrder" v-if="btnStatus == 1">
					<v-price-format :custom-style="{ fontWeight: 'bold' }">0</v-price-format>
					<text>{{ payBtnText }}</text>
				</view>
			</view>
		</u-popup>
		<u-popup v-model="showTeamBox" mode="center" :border-radius="32" :mask-close-able="false" :closeable="true">
			<view class="team_box">
				<view class="box_title"><text
						style="font-size: 32rpx;color:#D64046;font-weight: bold;">userName</text>{{$t('pagesText.yqncy')}}
				</view>
				<view class="team_avatar flex alic">
					<image src="" mode="scaleToFill" />
					<template>
						<image src="" mode="scaleToFill" v-if="false" />
						<view class="default">?</view>
					</template>
				</view>
				<v-button bg-color="#D64046" border-color="#D64046" width="540rpx"
					height="80rpx">{{$t('pagesText.cypt')}}</v-button>
			</view>
		</u-popup>
		<u-modal v-model="showConfirmModal" :content="$t('pagesText.suredh')+'？'" :show-cancel-button="true"
			@confirm="payConfirm" :confirm-text="$t('btnText.qd')" :cancel-text="$t('pageText.dcl')"></u-modal>
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
				couponId: '', //优惠券id
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
				backText: this.$t('repeatUse.back'),
				buyType: '',
				orderType: '', //订单类型
				btnStatus: 0, //0 -- 正常订单  1--提交订单但不支付
				btntext: '',
				ticketList: [],
				discountId: '',
				videoState: false,
				discountAmount: '',
				currentAddress: {}, //用户地址
				userisVip: '',
				number: '',
				videonum: 1,
				timerVideo: null,
				timerVideoNum: 0, //计时数
				playing: false, // 是否正在播放
				isShowTitle: false,
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
						this.currentPlay = this.sectionList[newv].videoLink || ''
					})
				}
			},
			showPopup(newv) {
				if (!newv) {
					// 关闭弹窗  删除groupId
					this.groupId = null
					this.buyType = ''
				}
			},
			timerVideoNum() {
				if (this.timerVideoNum == 60) {
					this.videoRequests()
				}
			},
		},
		onReady: function(res) {
			this.isShowTitle = true
			// #ifndef MP-ALIPAY
			this.videoContext = uni.createVideoContext('myVideo')
			// #endif
			console.log(this.videoContext, 'this.videoContext')
		},
		methods: {
			changeVideo(index) {
				this.currentVideoIndex = index
				this.videonum == 1 ? this.videoContext.play() : this.videoContext.pause()
			},
			closeShowPopup() {
				this.showPopup = false
			},
			routeGo() {
				uni.navigateTo({
					url: '/pages/vipCourseDetails/allGroup?detail=' + encodeURIComponent(JSON.stringify(this.detail))
				})
				console.log(this.detail, "detaildetaildetail")
			},
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
			// 获取客服
			customerType() {
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


			videoRequests() {
				this.$Request.getT('/app/topicCourseMinute/saveCourseMinute', {
					status: 2,
					typeId: uni.getStorageSync('typeId')
				}).then(res => {
					if (res.code == 0) {
						this.$Request.getT('/app/topicCourseMinute/saveCourseMinute', {
							status: 1,
							typeId: uni.getStorageSync('typeId')
						})
					}
				})
				this.timerVideoNum = 0
			},
			startTimer() {
				this.timerVideo = setInterval(() => {
					this.timerVideoNum += 1
				}, 1000)
			},
			clearTimer() {
				clearInterval(this.timerVideo)
				this.timerVideo = null
			},
			onPause(e) {
				this.videonum = e
				this.videoRequest();
				this.clearTimer()
			},
			onVideoPlay(e) {
				this.videonum = e
				this.videoRequest();
				this.startTimer()
			},
			onVideoEnded(e) {
				// this.videoRequest();
				this.clearTimer(); // 视频结束时清除定时器
				if (this.currentVideoIndex == this.sectionList.length - 1) {
					this.currentVideoIndex = 0;
				} else {
					this.currentVideoIndex++;
				}
			},
			// 播放 status--1 结束/暂停 status--2
			videoRequest() {
				this.$Request.getT('/app/topicCourseMinute/saveCourseMinute', {
					status: this.videonum,
					typeId: uni.getStorageSync('typeId')
				}).then(res => {

				})
				this.videonum = this.videonum == 1 ? 2 : 1
			},
			// 开始学习播放暂停视频
			videoPlay() {
				// this.videonum == 1 ? this.$refs.videoPlayer.play() : this.$refs.videoPlayer.pause()
				this.videonum == 1 ? this.videoContext.play() : this.videoContext.pause()

			},







			// 判断用户是否登录
			isUserLogin() {
				if (uni.getStorageSync('token') && uni.getStorageSync('userId')) {
					console.log("222222isUserLogin")
					this.getTeamListById()
					this.getSectionListById()
					this.getUserAddress()
					// setTimeout(() => {
					// 	this.timePlay()
					// }, 500)
				} else {
					// courseMoneyType是否支持拼团 0:否 1:是
					let option = this.$route.fullPath
					let data = this.$route.query
					console.log(data, "data333333333")
					uni.redirectTo({
						url: '/pages/public/login?goodInfo=' + encodeURIComponent(JSON.stringify(data))
					})
				}
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
				let that = this;
				// #ifdef H5
				this.showShareModal = true
				// #endif
				// #ifdef APP-PLUS
				let downloadLink = config.BASE_URLS + '/pages/public/appEq'
				// let downloadLink = 'http://192.168.0.131:8080/pages/public/appEq'
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
										title: this.$t('repeatUse.copyOk'),
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
				let shareContent = this.shareContent
				uni.setClipboardData({
					data: shareContent,
					success() {
						uni.showToast({
							title: this.$t('repeatUse.copyOk'),
							icon: 'successs'
						})
					}
				})
			},
			// 积分兑换商品
			payConfirm() {
				this.$Request.getT('/app/topicGoods/goldCoinsExchange', {
					goodId: this.detail.goodId
				}).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('repeatUse.gmcg'),
							mask: true
						})
					}
				})
			},
			toStudy() {
				this.orderType = '1';
				if (this.detail.goodType == 2) {
					this.showPopup = true;
					this.price = this.$t('unit.yuan') + '0';
					this.payBtnText = this.$t('bar.xd');
				} else {
					if (this.detail.isBuy == 0) {
						this.addOrder()
					} else {
						this.videoPlay()
						uni.pageScrollTo({
							scrollTop: 0,
							duration: 300
						})
					}
				}
				this.btnStatus = 1;
				setTimeout(() => {
					if (this.detail.goodType == 1) {
						this.getGoodDetail()
					}
				}, 1000)
			},
			// 资料下单
			addOrder() {
				let data = {
					groupId: this.groupId || '',
					goodId: this.detail.goodId,
					isGroup: this.orderType === '3' ? 1 : 0,
					goodType: this.detail.goodType,
					couponId: this.discountId,
					addressId: this.currentAddress.addressId,
					orderType: this.orderType
				}
				if (this.detail.goodType == 2) {
					if (!data.addressId) {
						return uni.showToast({
							title: this.$t('pagesText.txdzxx'),
							icon: 'none'
						})
					}
				}
				this.$Request.postJson('/app/topicIndent/addTopicIndent', data).then(res => {
					if (res.code == 0) {
						// if (this.detail.goodType == 2) {
						this.showPopup = false;
						uni.showToast({
							title: this.$t('pagesText.xdcg'),
							icon: 'none'
						})
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/order/order'
							})
						}, 2000)
						// }
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			// 课程付款
			confirm() {
				console.log(this.currentAddress, 'this.currentAddress')
				let data = {
					groupId: this.groupId || '',
					goodId: this.detail.goodId,
					isGroup: this.orderType === '3' ? 1 : 0,
					goodType: this.detail.goodType,
					couponId: this.discountId || '',
					addressId: this.currentAddress.addressId,
					orderType: this.orderType
				}
				this.buyType === 'cheap' && this.detail.vipRate && (data.isVip = 1)
				if (this.detail.goodType == 2) {
					if (!data.addressId) {
						return uni.showToast({
							title: this.$t('pagesText.txdzxx'),
							icon: 'none'
						})
					}
				}
				if (!this.detail.courseMoneyType && this.detail.goldCoinsMoney) {
					// 金币购买
					this.showConfirmModal = true
					return
				}
				this.$Request.postJson('/app/topicIndent/addTopicIndent', data).then(res => {
					// 用户不是会员
					if (res.code == 50001) {
						this.navigate('/subPackage/vip/vip')
					} else if (res.code == 0) {
						if (!res.data) return
						this.navigate('/pages/vipCourseDetails/pay?order=' + encodeURIComponent(JSON.stringify(res.data)) +
							'&payMoney=' + this.price)
						// uni.setStorageSync('order',res.data)
						this.showPopup = false
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			change(index) {
				this.current = index;
			},
			radioGroupChange(e) {
				console.log(e, 'group');
			},
			pay(type, groupId, userId) {
				if (userId) {
					// 参与别人的拼团
					if (userId === uni.getStorageSync('userId')) {
						return uni.showToast({
							title: this.$t('pagesText.bncyzj'),
							icon: 'none'
						})
					}
				}
				this.buyType = type
				if (this.buyType === 'cheap') {
					// 区分拼团/会员
					if (this.enterType == 'group' && this.detail.courseMoneyType) {
						// 拼团
						groupId && (this.groupId = groupId)
						this.price = this.detail.groupPrice
						this.payBtnText = this.$t('pagesText.kspt')
						this.orderType = '3'
						this.showPopup = true
						console.log('拼团购买')
					} else {
						if (this.userisVip != 1) {
							// this.showPopup = false
							console.log(this.showPopup, "this.showPopup")
							uni.navigateTo({
								url: '/subPackage/vip/vip'
							})
						} else {
							this.payBtnText = this.$t('repeatUse.surePay')
							this.price = this.vipPrice.toFixed(2)
							this.orderType = '2'
							this.showPopup = true
							console.log('会员购买', this.price)
						}
					}
				} else {
					this.price = this.detail.nowBuyMoney
					this.payBtnText = this.$t('repeatUse.surePay')
					this.orderType = '1'
					this.showPopup = true
					console.log('立即购买');
				}
			},
			// 获取拼团商品的拼团信息
			getTeamListById() {
				if (this.groupFinished) return
				this.$Request.getT('/app/topicIndent/getGroupIndentList', {
					...this.groupPageOption,
					goodId: this.goodId
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
			// 获取商品信息
			getGoodDetail() {
				// uni.showLoading({
				// 	title: '加载中...'
				// })
				this.$Request.getT('/app/topicGoods/getTopicGoodsInfo', {
					goodId: this.goodId,
					userId: uni.getStorageSync('userId')
				}).then(res => {
					// uni.hideLoading()
					this.detail = res.data
					console.log(this.detail, "detail")
					this.backText = res.data.title
					if (this.detail.goodType == 2) {
						this.btntext = this.$t('unit.yuan') + '0' + this.$t('bar.xd')
					} else if (this.detail.goodType == 1) {
						this.btntext = this.$t('pageText.ksxx')
					}
					if (!this.detail.isBuy) return
				})
			},
			// 获取课程的课程表
			getSectionListById() {
				if (this.status === 'normal') return
				this.$Request.getT('/app/topicSection/getTopicSectionByGoodsId', {
					...this.sectionPageOption,
					goodId: this.goodId
				}).then(res => {
					this.sectionList = [...this.sectionList, ...res.data.records]
					this.currentPlay = this.sectionList[0].videoLink
					if (res.data.records.length < this.sectionPageOption.limit) {
						this.status = 'normal'
						this.loadText = this.$t('load.nomore') + '~'
						return
					}
					this.$set(this.sectionPageOption, 'page', this.sectionPageOption.page + 1)
				})
			},
			// 获取用户优惠券
			getMyList() {
				uni.showLoading({
					title: this.$t('repeatUse.load') + '...'
				})
				this.$Request.getT('/app/couponUser/getMyCouponList?page=1&limit=10&status=0').then(res => {
					if (res.code == 0) {
						uni.hideLoading()
						let data = res.data.records
						for (var i = 0; i < data.length; i++) {
							if (data[i].expirationTime) {
								data[i].expirationTime = data[i].expirationTime.substring(0, 10)
							}
						}
						this.ticketList = res.data.records;
					}
				});
			},
			// 获取个人信息
			getUserInfoById() {
				if (uni.getStorageSync('userId')) {
					this.$Request.getT('/app/user/selectUserById', {
						userId: uni.getStorageSync('userId')
					}).then(res => {
						this.userisVip = res.data.isVip
						uni.setStorageSync('userInfo', res.data)
					})
				}
			}
		},
		onLoad({
			goodId,
			enterType
		}) {
			// #ifdef H5
			this.shareContent = config.BASE_URLS + this.$route.fullPath
			// #endif
			// #ifdef MP-WEIXIN
			uni.showShareMenu({
				withShareTicket: true,
				menus: ['shareAppMessage', 'shareTimeline']
			});
			this.onShareAppMessage = this.setShareAppMessage(this.shareInfo);
			this.onShareTimeline = this.setShareTimeline(this.shareInfo);
			// #endif


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
			if (uni.getStorageInfoSync('token')) {
				this.getMyList()
				this.getGoodDetail()
			}
			this.isUserLogin()
		},
		onUnload() {
			console.log(this.detail, "this.detailthis.detail22")
			if (this.timerVideoNum > 0) {
				// this.$refs.videoPlayer.stop()
				this.videoContext.stop()
				this.clearTimer()
			}
		},
		onHide() {
			// this.$refs.videoPlayer.pause()
			this.videoContext.pause()
			this.clearTimer()
		},
		onShow() {
			this.getGoodDetail()
			this.getUserInfoById()
			this.getTeamListById()
			uni.$once('getDiscountId', data => {
				this.discountId = data.discountId
				this.discountAmount = data.price
			})
			uni.$once('getUserAddress', data => {
				this.currentAddress = data
			})
			if (this.detail.isBuy == 1 && this.detail.goodType == 1) {
				this.videoContext.play()
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #F5F7FF;
		height: 100%;
	}



	.u-countdown {
		align-items: baseline !important;
	}

	.titleSolt {
		margin-left: 12rpx;
		width: 180rpx;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: normal;
	}

	/deep/ .u-countdown-colon {
		color: rgb(214, 64, 70) !important;
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
			font-size: 28rpx;
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

	// .main {
	// 	width: 750rpx;
	// 	// height: 1500rpx;
	// 	background-color: #f5f7ff;
	// }

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

	.good_price_ {
		margin-bottom: 26rpx !important;
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
		padding-top: 24rpx;
		padding-bottom: 16rpx;

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
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: normal;

					text {
						font-weight: bold;
						font-size: 36rpx;
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
				display: flex;
				justify-content: space-between;
				align-items: baseline;

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
					// width: 300rpx;
					font-family: PingFang SC;
					// font-weight: 500;
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
				align-items: baseline;
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

	.bottomNavbar_ {
		width: calc(100% - 60rpx);
		display: flex;
		justify-content: space-between;
		padding: 19rpx 0;
		margin: auto;
	}

	.bottomNavbar {
		width: 100%;
		height: 120rpx;
		background-color: #FFFFFF;
		position: fixed;
		bottom: 0;


		.bottomNavbar_left {

			// background-color: #0096FF;
			display: flex;

			.bottomNavbar_left_btn {
				display: flex;
				flex-direction: column;
				// margin-right: 35rpx;
				align-items: center;

				image {
					width: 40rpx;
					height: 40rpx;
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
			width: 85%;

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
				padding: 14rpx 0;
				display: flex;
				flex-direction: column;
				background: #FFE0E1;
				border-radius: 41rpx 0rpx 0rpx 41rpx;
				align-items: center;
				white-space: nowrap;
				width: 50%;
				text-align: center;

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
				padding: 14rpx 0;
				display: flex;
				flex-direction: column;
				background: linear-gradient(65deg, #FEF2DC 0%, #FEE4BF 100%);
				border-radius: 0rpx 41rpx 41rpx 0rpx;
				align-items: center;
				width: 50%;
				text-align: center;

				.bottomNavbar_right_right_price {
					font-weight: bold;
					font-size: 30rpx;
					color: #74552B;
					line-height: 1;
				}

				.bottomNavbar_right_right_title {
					width: 100%;
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
				padding-bottom: 15rpx !important;
				padding-top: 15rpx !important;
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