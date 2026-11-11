<template>
	<view class="chapter container bf">
		<u-navbar :back-text="$t('pageText.zjlx')"></u-navbar>
		<custom-tabs :data="topicBankList" name="bankName" id="bankId" :current="current" @changeTab="handleChange"
			scroll-into-view></custom-tabs>
		<view class="num_block" :class="{ second: current === 2 }">
			<view class="flex alic sa" style="height: 100%;">
				<view v-for="(item, index) in record" :key="index" class="flex-c alic box">
					<text class="num" v-if="index == 0">{{ item.num.toFixed(0)}}</text>
					<text class="num" v-if="index == 1">{{ item.num.toFixed(0)}}</text>
					<text class="num" v-if="index == 2">{{ item.num | formatPrice}}%</text>
					<text class="num_title">{{ item.title }}</text>
				</view>
			</view>
		</view>
		<Tree :node-data="treeData" children-name="topicLibraryList" unique-key="infoId" @childClick="getMatterList" />
		<!-- <u-empty mode="search" text="暂无数据" :margin-top="200" v-if="!treeData.length && current == 0"></u-empty> -->
		<!-- 修改 删除&&current == 0-->
		<u-empty mode="search" :text="$t('repeatUse.noData')" :margin-top="200" v-if="!treeData.length"></u-empty>
	</view>
</template>


<script>
	import Tree from './components/Tree.vue';
	import customTabs from '@/components/custom-tabs/custom-tabs.vue';
	export default {
		components: {
			Tree,
			customTabs
		},
		data() {
			return {
				topicBankList: [],
				treeData: [],
				current: 0,
				activeItemStyle: {
					color: '#000',
					fontWeight: 'bold'
				},
				record: [{
						title: this.$t('pageText.zts'),
						num: 0
					},
					{
						title: this.$t('pageText.zuots'),
						num: 0
					},
					{
						title: this.$t('pagesText.zql'),
						num: 0
					}
				],
			}
		},
		// computed: {
		// 	...mapState({
		// 		topicBankList: state => state.chapterModule.topicBankList
		// 	})
		// },
		filters: {
			formatPrice(data) {
				console.log(typeof data, '11111111111111')
				console.log(data, '11111111111111')
				let num = 0
				if (Number(data) > 0) {
					num = parseFloat(data).toFixed(2);
				}
				return num;
			}
		},
		onShow() {
			this.getSubjectTypeList()
		},
		methods: {
			// async getBankTypeList() {
			// 	const {
			// 		data: {
			// 			records
			// 		}
			// 	} = await getBankOfTypeList({
			// 		typeId: uni.getStorageSync('typeId')
			// 	})
			// 	this.topicBankList = records
			// 	console.log(this.topicBankList,"this.topicBankListthis.topicBankList")
			// },
			// async toAnswer({
			// 	infoId,
			// 	bankId
			// }) {
			// 	this.$store.commit('homeModule/setInfoId', infoId)
			// 	this.$store.commit('homeModule/setBankId', bankId)
			// 	const res = await this.$store.dispatch('chapterModule/getList')
			// 	if (!res) return uni.showToast({
			// 		title: '暂无内容',
			// 		icon: 'none',
			// 		mask: true
			// 	})
			// 	// this.navigate('/subPackage/chapter/chapterDetail')
			// },
			// 获取题目
			getMatterList({
				infoId
			}) {
				this.$Request.getT('/app/topicMatter/getTopicMatterList', {
					page: 1,
					limit: 10,
					infoId: infoId,
					typeId: uni.getStorageSync('typeId'),
				}).then(res => {
					if (res.data.records.length <= 0) {
						uni.showToast({
							title: this.$t('order.zwnr'),
							icon: 'none',
							mask: true
						})
						return
					} else {
						this.navigate('/subPackage/chapter/chapterDetail?infoId=' + infoId + '&zjType=1')
					}
				})
			},
			handleChange(val) {
				this.current = val
			},
			// 题目类型下的小题
			getTreeData(bankId) {
				let data = {
					bankId: bankId
				}
				this.$Request.getT('/app/topicLibrary/getAppTopicLibraryList', data).then(res => {
					if (res.code == 0) {
						this.record.map((item, index) => {
							item.num = index == 0 ? res.data.allCount : (index == 1 ? res.data.finishCount :
								`${res.data.rightChance * 100}`)
						})
						this.treeData = res.data.topicLibraryList
					}
				})
			},
			getSubjectTypeList() {
				let data = {
					typeId: uni.getStorageSync('typeId')
				}
				this.$Request.getT('/app/topicBank/getTopicBankList', data).then(res => {
					if (res.code == 0) {
						this.topicBankList = res.data.records
						this.getTreeData(this.topicBankList[this.current].bankId)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						setTimeout(() => {
							uni.navigateBack()
						}, 1000)
					}
				})
			}
		},
		watch: {
			current: {
				immediate: true,
				handler(newv) {
					this.record = [{
							title: this.$t('pageText.zts'),
							num: 0
						},
						{
							title: this.$t('pageText.zuots'),
							num: 0
						},
						{
							title: this.$t('pagesText.zql'),
							num: 0
						}
					]
					this.treeData = []
					this.getSubjectTypeList()
				}
			}
		}
	}
</script>


<style lang="scss" scoped>
	.chapter {
		padding: 0 32rpx;

		.num_block {
			background: #EDF8FF;
			border-radius: 8rpx;
			width: 100%;
			height: 100rpx;
			margin-top: 30rpx;
			position: relative;

			&::before {
				content: '';
				position: absolute;
				left: 90rpx;
				width: 0;
				height: 0;
				border: 20rpx solid transparent;
				border-bottom-color: #EDF8FF;
				transform: translateY(-90%);
			}

			.box {
				text {
					color: $theme-color;
					font-size: 24rpx;

					&:first-child {
						font-weight: 800;
						font-size: 32rpx !important;
					}
				}
			}
		}

		.second {
			&::before {
				left: 330rpx !important;
			}
		}
	}
</style>