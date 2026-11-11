<template>
	<view class="mine_universal container bf">
		<u-navbar :back-text="backText"></u-navbar>
		<custom-tabs :data="tabs" name="bankName" :current="current" @changeTab="onclickItem" scroll-into-view
			v-if="type == 4 || type == 1" class="custom"></custom-tabs>
		<view v-if="type != 4 || type != 1">
			<u-cell-group>
				<u-cell-item v-for="item in list" :key="item.goodId" @click="toDetail(item.goodId)">
					<view slot="title">{{ item.title }}</view>
					<view slot="label" class="label flex">
						<view v-for="(item1, index1) in item.labelList" :key="index1" class="tag">{{ item1 }}</view>
					</view>
				</u-cell-item>
			</u-cell-group>
		</view>
		<template v-if="(type == 1 || type == 4)">
			<template v-if="errorList.length">
				<!-- :key="item.infoId" -->
				<view class="flex sb alic row" v-for="item in errorList"
					@click="navigate(`/pages/my/errlist?infoId=${item.infoId}&type=${type}&bankId=${item.bankId}`)">
					<text>{{ item.name }}</text>
					<text>{{ type == 1 ? item.hasCount : item.errorCount }}{{$t('pagesText.dao')}}</text>
				</view>
			</template>
			<!-- <u-empty mode="data" :margin-top="150" v-else></u-empty> -->
		</template>
		<template v-if="type == 4 && current == 1">
			<swiper class="collect_" :style="[{ height: swiperHeight }]" :current="swiperCurrent" @change="swiperChange">
				<swiper-item v-for="(item, index) in collectList" :key="item.matterId" class="swiper-item">
					<sub-err type="collect" :detail="item" :height="swiperHeight" />
				</swiper-item>
			</swiper>
		</template>
		<u-empty mode="search" :text="$t('repeatUse.noData')" :margin-top="200"
			v-if="!list.length && !errorList.length"></u-empty>
		<v-loading :status="status" :load-text="loadText" v-if="!(type == 4 && current != 0) && list.length"></v-loading>
		<!-- <view class="footer flex-c jc alic" @click="$emit('showModal', detail[type === 'error' ? 'errorId' : 'matterId'])" -->
		<view class="footer_placeholder"></view>
	</view>
</template>


<script>
	import CustomTabs from '@/components/custom-tabs/custom-tabs.vue'
	import subErr from './components/subErr.vue'
	export default {
		components: {
			CustomTabs,
			subErr
		},
		onLoad({
			type
		}) {
			this.type = type
			if (type == 1) {
				this.getBankList()
				// this.getTopicLibrary()
			} else if (type != 4) {
				this.getTopicListByTypeId()
			} else {
				// this.getTopicErrorTypeList()
			}
		},
		onShow() {
			if (this.type == 4) {
				this.errorList = []
				this.page = 1
				this.status = 'loading'
				this.getTopicErrorTypeList()
			}
		},
		onHide() {
			if (this.type == 4) {
				this.current = 0
			}
		},
		computed: {
			backText() {
				let backText = ''
				switch (this.type) {
					case '1':
						backText = this.$t('pagesText.dtjl')
						break
					case '2':
						backText = this.$t('pagesText.wdkc')
						break
					case '3':
						backText = this.$t('bar.wdzl')
						break
					case '4':
						backText = this.$t('bar.ctsc')
						break
				}
				return backText
			},
			tabs() {
				let tabs = []
				switch (this.type) {
					case '1':
						tabs = this.tabsList
						break;
					case '2':
						tabs = [];
						break;
					case '3':
						tabs = [];
						break;
					case '4':
						tabs = [{
								bankName: this.$t('bar.wdct')
							},
							{
								bankName: this.$t('repeatUse.wdsc')
							}
						];
						break;
				}
				return tabs
			}
		},
		data() {
			return {
				swiperCurrent: 0,
				matterId: null,
				swiperHeight: '',
				type: null,
				current: 0,
				loadText: this.$t('repeatUse.zzjz') + '...',
				status: 'loading',
				page: 1,
				limit: 10,
				errorList: [],
				collectList: [],
				list: [], // 我的资料/课程/题库
				tabsList: []
			}
		},
		onReachBottom() {
			this.getTopicErrorTypeList()
		},
		methods: {
			// 获取我的错题列表
			getTopicErrorTypeList() {
				this.$Request.getT('/app/topicLibrary/getMyErrorLibraryList', {
					page: this.page,
					limit: this.limit
				}).then(res => {
					this.errorList = [...this.errorList, ...res.data.records]
					if (res.data.records.length < this.limit) {
						this.status = 'normal'
						this.loadText = this.$t('load.nomore') + '~'
						return
					}
					this.page++
				})
			},
			toDetail(goodId) {
				if (this.type == 2 || this.type == 3) {
					this.navigate('/pages/vipCourseDetails/vipCourseDetails', {
						goodId
					})
				} else {
					// 资料展示  pdf
				}
			},
			// 获取答题记录题库
			getBankList() {
				this.$Request.getT('/app/topicBank/getTopicBankList', {
					typeId: uni.getStorageSync('typeId')
				}).then(res => {
					this.tabsList = res.data.records
					this.getTopicLibrary()
				})
			},
			// 获取答题记录题库类型下的类别
			getTopicLibrary() {
				this.$Request.getT('/app/topicLibrary/getAnswerRecordList', {
					bankId: this.tabsList[this.current].bankId,
					page: this.page,
					limit: this.limit
				}).then(res => {
					this.errorList = res.data.records
				})
			},
			swiperChange({
				detail: {
					current
				}
			}) {
				this.swiperCurrent = current
			},
			onclickItem(val) {
				this.current = val
				console.log(val, "列表")
				if (this.type == 1) {
					this.getTopicLibrary()
				}
			},
			// 获取我的课程列表
			getTopicListByTypeId() {
				if (this.status === 'normal') return
				let type = this.type == 2 ? '1' : (this.type == 3 ? '2' : '')
				this.$Request.getT('/app/topicUserGoods/getMyGoodsList', {
					page: this.page,
					limit: this.limit,
					typeId: type
				}).then(res => {
					this.list = [...this.list, ...res.data.records]
					// this.list = list.flat(Infinity).filter(item => item.isBuy)
					if (res.data.records.length < this.limit) {
						this.status = 'normal'
						this.loadText = this.$t('load.nomore') + '~'
						return
					}
					this.page = this.page + 1
				})
			}
		},
		watch: {
			current: {
				handler(newv) {
					if (this.type == 4) { // 错题接口
						this.page = 1
						if (newv === 0) {
							this.getTopicErrorTypeList()
						} else {
							this.navigate('/pages/my/collectlist')
						}
						// 计算swiper高度
						/* if (!this.swiperHeight && newv === 1) {
							this.$nextTick(() => {
								uni.createSelectorQuery().in(this).select('.collect_').boundingClientRect(({
									top
								}) => {
									this.swiperHeight = `calc(100vh - ${top}px)`
								}).exec()
							})
						} */
					}
				}
			}
		}
	}
</script>


<style lang="scss" scoped>
	.custom {
		border-bottom: none !important;
	}

	.tag {
		padding: 6rpx 14rpx;
		background: #FFF4F3;
		color: #EF6870;
		border-radius: 10rpx;
		margin-right: 12rpx;
		font-size: 24rpx;
	}

	.row {
		height: 100rpx;
		position: relative;
		color: #333;
		padding: 0 32rpx;

		&::before {
			content: '';
			position: absolute;
			bottom: 0;
			left: 50%;
			transform: translate(-50%);
			width: calc(100% - 64rpx);
			height: 2rpx;
			background-color: #F0F0F0;
		}

		text {
			&:first-child {
				font-size: 30rpx;
			}

			&:last-child {
				font-size: 28rpx;
			}
		}

		&:first-child {
			font-size: 30rpx;
		}

		&:last-child {
			font-size: 28rpx;
		}
	}

	/deep/.tab-item {
		padding-left: 0 !important;
		padding-right: 0 !important;
		margin-right: 65rpx;
	}
</style>