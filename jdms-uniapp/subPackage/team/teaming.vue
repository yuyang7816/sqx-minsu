<template>
	<view class="team_self container">
		<u-navbar :back-text="$t('pagesText.wypt')" :background="{ background: 'transparent' }"
			:back-text-style="{ color: '#fff' }" back-icon-color="#fff" :border-bottom="false"
			:placeholder="false"></u-navbar>
		<image src="https://dati.xianmxkj.com/file/uploadPath/2024/08/14/e19b1995ce7e80875d5c37db978b22a2.png" mode=""
			style="position: fixed;top: 0;width:100%;height:480rpx"></image>
		<view style="height: calc(480rpx - 44px);"></view>
		<custom-tabs class="custom" :data="tabList" :current="tabIndex" @changeTab="onclickItem"
			scroll-into-view></custom-tabs>
		<view v-if="goodList.length">
			<view class="team">
				<team-strip v-for="(item,index) in goodList" :key="item.goodId" :good="item" @showModal="showModal" />
			</view>
			<view class="teamlast"></view>
		</view>
		<u-empty :text="$t('pagesText.zwsp')" mode="car" :margin-top="180" v-else></u-empty>
		<view class="footer_operater flex">
			<view class="ing flex jc alic inactive_">{{$t('pagesText.zzpt')}}</view>
			<view class="mine flex jc alic active_" @click="toSelf">{{$t('pagesText.wdpt')}}</view>
		</view>
	</view>
</template>

<script>
	import CustomTabs from '@/components/custom-tabs/custom-tabs.vue'
	import teamStrip from './components/teamStrip.vue'
	export default {
		components: {
			CustomTabs,
			teamStrip
		},
		methods: {
			toSelf() {
				uni.redirectTo({
					url: '/subPackage/team/teamSelf'
				})
			},
			onclickItem(val) {
				this.tabIndex = val;
				this.getGoodListById()
			},
			// 获取拼团商品列表
			getGoodListById() {
				this.$Request.getT('/app/topicGoodsType/getTopicGoodsTypeList', {
					...this.pageOption,
					childTopicTypeId: uni.getStorageSync('typeId'),
					courseMoneyType: '1'
				}).then(res => {
					this.list = res.data.records
					console.log(this.status, 'this.status')
					this.list.map((item) => {
						item.topicGoodsList.map((itemdetail => {
							itemdetail.goodStatus = 1
						}))
					})
					this.goodList = this.list.find((item, index) => index === this.tabIndex).topicGoodsList.filter(item => item
						.courseMoneyType) || []
				})
			}
		},
		data() {
			return {
				show: false,
				goodId: null,
				pageOption: {
					page: 1,
					limit: 999
				},
				tabIndex: 0,
				list: [],
				goodStatus: '',
				goodList: [],
				status: 'loadmore',
				loadText: {
					loadmore: this.$t('load.la'),
					loading: this.$t('load.nljz'),
					nomore: this.$t('load.szmyl')
				}
			}
		},
		computed: {
			tabList() {
				return this.list.filter(item => item.chargeType == 2).map(item => ({
					name: item.typeName
				}))
			},
		},
		onLoad() {
			this.getGoodListById();
		},
		// onReachBottom() {
		// 	if (this.pageOption.page < this.total) {
		// 		this.pageOption.page += 1
		// 		this.getGoodListById()
		// 	}
		// },
	}
</script>


<style lang="scss" scoped>
	.custom {
		position: fixed;
		z-index: 1;
		width: 100%;
		top: 29%;
	}

	/deep/.tab-item {
		margin-right: 50rpx !important;
		padding-left: 0 !important;
		padding-right: 0 !important;
	}

	.team {
		// position: fixed;
		// z-index: 1;
		// width: 100%;
		// top: 34.2%;
		margin-top: 10%;
	}

	.teamlast {
		padding-bottom: 120rpx !important;
	}

	.team_self {
		u-navbar {
			/deep/.u-navbar-placeholder {
				display: none;
			}
		}
	}

	.footer_operater {
		position: fixed;
		left: 0;
		right: 0;
		// bottom: env(safe-area-inset-bottom);
		bottom: 0;
		height: 100rpx;
		font-size: 30rpx;
		white-space: nowrap;
		z-index: 99;

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
</style>