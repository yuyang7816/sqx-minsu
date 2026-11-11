<template>
	<view class="container">
		<u-navbar :back-text="$t('pagesText.mkds')"></u-navbar>
		<period-card :detail="item" v-for="(item, index) in list" :key="index" />
		<u-loadmore :status="status" :load-text="loadText"></u-loadmore>
	</view>
</template>


<script>
	import periodCard from './components/periodCard.vue';
	export default {
		components: {
			periodCard
		},
		data() {
			return {
				list: [],
				pageOption: {
					page: 1,
					limit: 10
				},
				status: 'loadmore',
				loadText: {
					loadmore: this.$t('load.la'),
					loading: this.$t('load.nljz'),
					nomore: this.$t('load.szmyl')
				}
			}
		},
		methods: {
			getTopicContestList() {
				if (this.status === 'nomore') return
				this.status = 'loading'
				this.$Request.getT('/app/topicContest/getAppTopicContestList', {
					...this.pageOption,
					loginUserId: uni.getStorageSync('userId')
				}).then(res => {
					this.total = res.data.pages
					if (this.pageOption.page < this.total) {
						this.status = 'loadmore';
					} else {
						this.status = 'nomore';
					}
					if (this.pageOption.page == 1) {
						this.list = res.data.records
					} else {
						this.list = [...this.list, ...res.data.records]
					}
				})
			}
		},
		onLoad() {
			this.getTopicContestList()
		},
		onReachBottom() {
			if (this.pageOption.page < this.total) {
				this.pageOption.page += 1
				this.getTopicContestList()
			}
		},
	}
</script>


<style lang="scss" scoped>
	.container {
		padding-top: 28rpx;
	}
</style>