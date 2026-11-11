<template>
	<view class="team_self container">
		<u-navbar :back-text="$t('pagesText.wdpt')"></u-navbar>
		<custom-tabs :data="tabItem" :current="tabIndex" @changeTab="onclickItem" scroll-into-view>
		</custom-tabs>
		<!-- <template v-if="list.length"> -->
		<!-- <team-strip @mpShare="mpShare" v-for="item in list" :key="item.goodId" :good="item" @showModal="showModal" /> -->
		<view class="team_strip" v-for="good in list">
			<view class="title">{{ good.title }}</view>
			<view class="info flex sb alic">
				<template>
					<view class="flex" style="align-items: baseline;font-size: 26rpx;">
						<v-price-format :custom-style="{ fontSize: '32rpx', color: '#D43532' }">{{ good.groupPrice || good.indentMoney
			        }}</v-price-format>
					</view>
				</template>
				<view class="operate_btns flex">
					<!-- <v-button bg-color="#fff" color="#0096FF" v-if="goodStatus == 1"
							@click="navigate('/pages/vipCourseDetails/vipCourseDetails', { goodId: good.goodId, enterType: 'group' })">查看详情</v-button> -->
					<v-button bg-color="#fff" color="#0096FF" @click="routeGo(good)">{{$t('index.ckxq')}}</v-button>
					<!-- <v-button
							@click="navigate('/pages/vipCourseDetails/vipCourseDetails', { goodId: good.goodId, enterType: 'group' })"
							v-if="good.indentStatus == status[0]">发起拼团</v-button> -->
					<v-button @click="topay(good)" v-if="good.indentStatus == 0">{{$t('repeatUse.payNow')}}</v-button>
					<button open-type="share" v-if="good.indentStatus == 1" class="btn_" @click="share(good)">
						{{$t('repeatUse.yqhy')}}
					</button>
					<v-button bg-color="#fff" color="#0096FF" v-if="good.indentStatus == 2">{{$t('btn.wxdd')}}</v-button>
				</view>
			</view>
			<!-- 新增 -->

		</view>
		<view class="" style="height: 100rpx;"></view>

		<!-- </template> -->
		<u-empty :text="$t('repeatUse.noData')" mode="search" :margin-top="180" v-if="list.length == 0"></u-empty>
		<view class="footer_operater flex">
			<view class="ing flex jc alic active_" @click="toIng">{{$t('pagesText.zzpt')}}</view>
			<view class="mine flex jc alic inactive_">{{$t('pagesText.wdpt')}}</view>
		</view>
		<u-modal v-model="showShareModal" :content="shareContent" :show-cancel-button="true"
			:confirm-text="$t('repeatUse.copy')" :cancel-text="$t('btnText.qx')" :title="$t('repeatUse.fxlj')"
			@confirm="shareConfirm"></u-modal>
	</view>
</template>

<script>
	import CustomTabs from '@/components/custom-tabs/custom-tabs.vue'
	import teamStrip from './components/teamStrip.vue';
	import config from '../../config.js'
	export default {
		data() {
			return {
				tabIndex: 1,
				list: [],
				pageOption: {
					page: 1,
					limit: 10
				},
				goodStatus: '',
				tabItem: [{
						name: this.$t('pageText.dfk'),
						value: 0
					},
					{
						name: this.$t('pagesText.ptz'),
						value: 1
					},
					{
						name: this.$t('order.dfh'),
						value: 2
					},
					{
						name: this.$t('order.ywc'),
						value: 3
					},
					{
						name: this.$t('repeatUse.yqx'),
						value: 4
					},
					{
						name: this.$t('repeatUse.yfh'),
						value: 5
					}
				],
				indentStatus: '',
				good: {},
				showShareModal: false,
				shareContent: '',
				userId: null,
				goodStatus: false,
				total: ''
			}
		},
		components: {
			CustomTabs,
			teamStrip
		},
		onShow() {
			this.getOrderByType()
		},
		onLoad() {
			console.log(this.good, "234556666")
			let userInfo = uni.getStorageSync('userInfo');
			this.userId = userInfo.userId

		},
		onShareAppMessage(res) {
			var that = this;
			console.log(that.good, 'that.good')
			let path = '/subPackage/team/teamPay?goodInfo=' + encodeURIComponent(JSON.stringify(that.good))
			// 
			return {
				title: '',
				path: path,
				imageUrl: that.good.coverImg
			}
		},
		methods: {
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
			routeGo(good) {
				if (good.goodStatus == 1) {
					uni.navigateTo({
						url: '/pages/vipCourseDetails/vipCourseDetails?goodId=' + good.goodId + '&enterType=group'
					})
				} else {
					uni.navigateTo({
						url: '/pages/order/orderDetail?indentId=' + good.indentId
					})
				}
			},
			topay(good) {
				this.navigate('/pages/vipCourseDetails/pay?order=' + encodeURIComponent(JSON.stringify(good)) +
					'&payMoney=' + good.indentMoney)
			},
			toIng() {
				uni.redirectTo({
					url: '/subPackage/team/teaming'
				})
			},
			onclickItem(val) {
				this.tabIndex = val
			},
			// 获取我的拼团订单
			getOrderByType() {
				this.$Request.getT('/app/topicIndent/getTopicIndentList', {
					...this.pageOption,
					indentStatus: this.indentStatus,
					isGroup: 1
				}).then(res => {
					this.total = res.data.pages
					if (this.pageOption.page == 1) {
						this.list = res.data.records
					} else {
						this.list = [...this.list, ...res.data.records]
					}
					this.list.map((item) => {
						item.goodStatus = 0
					})

				})
			},
			// 新增
			share(info) {
				// #ifdef H5
				this.showShareModal = true
				this.shareContent = config.BASE_URLS + '/subPackage/team/teamPay?goodInfo=' + encodeURIComponent(JSON.stringify(
					info))
				// #endif
				this.good = info

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
		},
		watch: {
			tabIndex: {
				immediate: true,
				async handler(newv) {
					this.indentStatus = newv
					this.getOrderByType()
				}
			}
		},
		onReachBottom() {
			if (this.pageOption.page < this.total) {
				this.pageOption.page += 1
				this.getOrderByType()
			}
		},
	}
</script>


<style lang="scss" scoped>
	.footer_operater {
		position: fixed;
		left: 0;
		right: 0;
		// bottom: env(safe-area-inset-bottom);
		bottom: 0;
		height: 100rpx;
		font-size: 30rpx;
		white-space: nowrap;

		.ing {
			height: 100%;
			position: absolute;
			left: 0;
			width: 50%;
			clip-path: polygon(0 0, 100% 0, 90% 100%, 0 100%);
			z-index: 1;
		}

		.mine {
			height: 100%;
			position: absolute;
			right: 0;
			width: 60%;
			text-indent: 10%;
			z-index: 0;
		}

		.active_ {
			background: #D8EFFF;
			color: $theme-color;
		}

		.inactive_ {
			background: $theme-color;
			color: #fff;
		}
	}

	/deep/.u-model__content__message {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.btn_ {
		color: #0096FF;
		background-color: #fff;
		border: solid #0096FF 2rpx;
		border-radius: 40rpx;
		line-height: 70rpx;
		font-size: 28rpx;
		padding: 0 34rpx;
		margin-left: 10rpx;
	}

	/deep/ .uni-button:after {
		border: none;
	}

	.team_strip {
		background: #fff;
		padding: 30rpx;
		border-bottom: 1rpx solid #F5F5F5;

		.title {
			font-size: 32rpx;
			font-weight: bold;
		}

		.info {
			padding-top: 18rpx;

			.price {}
		}
	}

	.team_strip:last-child {
		border-bottom: none !important;
	}
</style>