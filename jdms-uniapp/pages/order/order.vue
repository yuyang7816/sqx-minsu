<template>
	<view class="group_1 container">
		<view class="bar">
			<!-- :style="[{ height: `${eq_statusBarHeight}px` }] -->
			<view class="status_bar" :style="{ height: iStatusBarHeight + 'px'}"></view>
			<u-dropdown>
				<u-dropdown-item v-model="dorpIndex" :title="dorpIndex == 1 ? $t('bar.kcdd') : $t('bar.zldd')"
					:options="dropOptions" @change='checkDrop'></u-dropdown-item>
			</u-dropdown>
		</view>
		<custom-tabs :data="tabItem" :current="tabIndex" @changeTab="onclickItem" scroll-into-view>
		</custom-tabs>
		<view class="order">
			<order-card v-for="item in list" :key="item.indentId" :detail="item" @show="show" :dorpIndex="dorpIndex"
				:tabIndex="tabIndex" />
			<v-loading :status="status" :load-text="loadText" v-if="list.length"></v-loading>
			<u-empty mode="search" :text="$t('repeatUse.noData')" :margin-top="200" v-if="!list.length"></u-empty>
		</view>
		<u-modal v-model="showModal" :show-cancel-button="true" :content="content" :async-close="true" @confirm="confirm"
			@cancel="close" :confirm-text="$t('btnText.qd')" :cancel-text="$t('repeatUse.qx')"
			:title="$t('repeatUse.ts')"></u-modal>
	</view>
</template>

<script>
	import customTabs from '@/components/custom-tabs/custom-tabs.vue'
	import orderCard from './components/orderCard.vue'

	export default {
		data() {
			return {
				iStatusBarHeight: 0,
				showModal: false,
				content: '',
				operateIndentId: null,
				operateStatus: '',
				status: 'loading',
				loadText: this.$t('repeatUse.zzjz') + '...',
				tabIndex: 0,
				dropOptions: [{
						label: this.$t('bar.kcdd'),
						value: 1,
					},
					{
						label: this.$t('bar.zldd'),
						value: 2,
					},

				],
				dorpIndex: 1,
				list: [],
				isdataeShow: false,
				page: 1,
				limit: 10,
				tab: [{
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
			};
		},
		computed: {
			tabItem() {
				return [{
					name: this.$t('repeatUse.all'),
					id: ''
				}, ...this.tab.map(item => ({
					name: item.name,
					id: item.value
				}))]
			}
		},
		components: {
			customTabs,
			orderCard
		},
		methods: {
			// geStatusBarHeight(){
			//     return uni.getSystemInfoSync()['statusBarHeight']
			// },
			show({
				indentId,
				status,
				content
			}) {
				this.showModal = true
				this.content = content
				this.operateIndentId = indentId
				this.operateStatus = status
			},
			close() {
				this.showModal = false
				setTimeout(() => {
					this.content = ''
					this.operateIndentId = null
					this.operateStatus = ''
				}, 500);
			},
			confirm() {
				switch (this.operateStatus) {
					case '取消订单':
						this.$Request.postT('/app/topicIndent/userCancelIndent', {
							indentId: this.operateIndentId
						}).then(res => {
							if (res.code == 500) return;
							uni.showToast({
								title: this.$t('pagesText.qxddcg'),
								icon: 'none'
							})
							this.page = 1
							this.getOrderList(false)
						})
						break;
					case '确认收货':
						this.$Request.postT('/app/topicIndent/finishIndent', {
							indentId: this.operateIndentId
						}).then(res => {
							if (res.code == 500) return;
							uni.showToast({
								title: this.$t('pagesText.qrshcg'),
								icon: 'none'
							})
							this.page = 1
							this.getOrderList()
						})
						break;
				}
				this.close()
			},
			onclickItem(val) {
				if (uni.getStorageSync('userId')) {
					this.tabIndex = val
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
			checkDrop(e) {
				if (uni.getStorageSync('userId')) {
					if (e == 1) {
						this.isdataeShow = false
					} else {
						this.isdataeShow = true
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
			// 获取订单列表
			getOrderList(bool = false, indentStatus = this.tabIndex - 1, indentType = this.dorpIndex) {
				if (bool) {
					if (this.status === 'normal') return
				}
				let param = {
					page: this.page,
					limit: this.limit,
					indentType
				}
				indentStatus > -1 && (param.indentStatus = indentStatus)
				this.$Request.getT('/app/topicIndent/getTopicIndentList', param).then(res => {
					if (res.code == 0) {
						this.page == 1 && (this.list = [])
						this.list = [...this.list, ...res.data.records]
						console.log(this.list);
						if (res.data.records.length < this.limit) {
							this.status = 'normal'
							this.loadText = this.$t('load.nomore') + '~'
							return
						}
					} else {
						this.status = 'normal'
						this.loadText = this.$t('load.nomore') + '~'
					}
					// this.page++
				})
			},
			reloadList() {
				this.list = []
				this.page = 1
				this.status = 'loading'
				this.loadText = this.$t('repeatUse.zzjz') + '...'
			}
		},
		watch: {
			dorpIndex: {
				async handler(newv) {
					this.reloadList()
					await this.getOrderList(false, this.tabIndex - 1, newv)
				}
			},
			tabIndex(newv) {
				this.reloadList()
				this.getOrderList(false, newv - 1, this.dorpIndex)
			}
		},
		onLoad() {
			this.iStatusBarHeight = uni.getSystemInfoSync().statusBarHeight;
		},
		onShow() {
			// this.list = []
			this.page = 1
			this.getOrderList(false);
		},
		onReachBottom() {
			this.page++
			this.getOrderList(true)

		}
	}
</script>

<style lang="scss" scoped>
	/deep/.u-dropdown__menu__item__text {
		font-weight: 700 !important;
		color: #000 !important;
	}

	.status_bar {}

	.bar {
		background: #fff;
		position: sticky;
		top: 0;
		z-index: 999;

	}

	.u-dropdown {
		flex: 0 !important;
	}

	.status-bar-box {
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.page {
		background-color: #f5f7ff;
		position: relative;
		width: 750rpx;
		height: 1365rpx;
		overflow: hidden;
		display: flex;
		flex-direction: column;
		justify-content: flex-end;
	}

	.group_1 {
		background-color: #f5f7ff;
		width: 748rpx;
		// height: 1360rpx;
		margin-top: 2rpx;
		display: flex;
		flex-direction: column;
	}

	.image_2 {
		width: 26rpx;
		height: 16rpx;
		margin-left: 10rpx;
		// margin: 22rpx 0 0 7rpx;
		// vertical-align: middle;
	}

	.order {
		width: 748rpx;
		height: 478rpx;

		.order_title {
			width: 676rpx;
			// height: 28rpx;
			margin: 36rpx 34rpx 0 38rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.order_title_left {
				height: 25rpx;
				font-weight: 500;
				font-size: 26rpx;
				color: #999999;
			}

			.order_title_right {
				height: 25rpx;
				font-weight: 500;
				font-size: 26rpx;
				color: #0096FF;
			}
		}

		.order_info {
			width: 676rpx;
			height: 165rpx;
			margin: 29rpx 44rpx 0 39rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;

			.order_img {
				width: 164rpx;
				height: 165rpx;
				background: #0D68FC;
				border-radius: 6rpx;
			}

			.order_price {
				width: 486rpx;
				height: 165rpx;
				display: flex;
				flex-direction: column;
				justify-content: space-between;
				align-items: center;

				.order_price_top {
					width: 486rpx;
					height: 90rpx;
					font-weight: bold;
					font-size: 30rpx;
					color: #333333;
					position: relative;

					.order_price_num {
						position: absolute;
						right: 25rpx;
						bottom: 0;
						font-weight: bold;
						font-size: 28rpx;
						color: #999999;
					}
				}

				.order_price_bottom {
					width: 486rpx;
					height: 22rpx;
					background-color: #FFFFFF;
					font-weight: 500;
					font-size: 26rpx;
					color: #333333;
					display: flex;
					align-items: center;
					justify-content: flex-end;
				}
			}
		}

		.order_line {
			width: 676rpx;
			height: 1rpx;
			background: #999999;
			border: 1px solid #E6E6E6;
			margin: 21rpx 34rpx 0 38rpx;
		}

		.order_money {
			width: 676rpx;
			height: 27rpx;
			margin: 33rpx 34rpx 0 38rpx;
			display: flex;
			justify-content: flex-end;
			align-items: center;

			.order_money_content {
				width: 185rpx;
				height: 27rpx;
				display: flex;
				justify-content: flex-end;
				align-items: center;

				.order_money_content_left {
					font-size: 28rpx;
					color: #333333;
					margin-right: 25rpx;
				}

				.order_money_content_right {
					font-weight: 700;
					font-size: 26rpx;
					color: #333333;
				}
			}
		}

		.order_pay {
			width: 676rpx;
			height: 72rpx;
			margin: 44rpx 45rpx 0 38rpx;
			display: flex;
			justify-content: flex-end;
			align-items: center;

			.order_pay_btn_title {
				padding: 22rpx 35rpx 23rpx 35rpx;
				background: #0096FF;
				border-radius: 36rpx;
				width: 110rpx;
				height: 27rpx;
				font-weight: bold;
				font-size: 28rpx;
				color: #FEFFFE;
			}

			.order_pay_btn_title_grey {
				padding: 22rpx 35rpx 23rpx 35rpx;
				background: #ffffff;
				border-radius: 36rpx;
				width: 110rpx;
				height: 27rpx;
				font-weight: bold;
				font-size: 28rpx;
				color: #666666;
				border: 1px solid #999999;
				margin-right: 15rpx;
			}
		}
	}
</style>