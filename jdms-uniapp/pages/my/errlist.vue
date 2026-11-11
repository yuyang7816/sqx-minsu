<template>
	<view class="errlist container">
		<u-navbar :back-text="backText"></u-navbar>
		<view v-if="errList.length">
			<view class="num_row"><text style="margin-right: 6rpx;">{{ current + 1 }}</text><text style="color:#999">/ {{ total
      }}</text></view>
			<swiper :current="current" :style="[{ height: swiperHeight }]" @change="swiperChange">
				<swiper-item v-for="(item, index) in errList" :key="item.errorId" class="swiper-item">
					<sub-err type="error" :detail="item" :typerout="type" :height="swiperHeight" @showModal="open" />
				</swiper-item>
			</swiper>
			<view class="footer flex-c jc alic" @click="open" v-if="type != 1">
				<image src="../../static/images/remove.png" mode="scaleToFill" />
				<text>{{$t('hmd.yc')}}</text>
			</view>
		</view>
		<u-empty mode="search" :text="$t('repeatUse.noData')" :margin-top="200" v-else></u-empty>
		<u-modal v-model="showModal" :content="$t('pagesText.sureYctm')+'？'" :show-cancel-button="true" :async-close="false" :title="$t('repeatUse.ts')"
			@concel="close" @confirm="remove" :confirm-text="$t('btnText.qd')" :cancel-text="$t('repeatUse.qx')"></u-modal>
	</view>
</template>


<script>
	import subErr from './components/subErr.vue'
	export default {
		components: {
			subErr
		},
		data() {
			return {
				infoId: null,
				showModal: false,
				page: 1,
				limit: 999,
				current: 0,
				swiperHeight: `calc(100vh - 44px)`,
				errList: [],
				total: 0,
				backText: '',
				userId: uni.getStorageSync('userId'),
				bankId: '',
				type: '',
			}
		},
		methods: {
			swiperChange(e) {
				this.current = e.detail.current
				// this.getMatterById()
			},
			// 获取错题和答题记录---题目详情
			getErrorList() {
				uni.showLoading({
					title: this.$t('repeatUse.load') + '...'
				})
				let url = '';
				let data = {};
				if (this.type == 1) {
					url = '/app/topicAnswer/getTopicMatterAnswerList';
					data = {
						page: this.page,
						limit: this.limit,
						infoId: this.infoId,
						userId: this.userId,
						bankId: this.bankId
					}
				} else {
					url = '/app/topicError/getTopicErrorList';
					data = {
						page: this.page,
						limit: this.limit,
						infoId: this.infoId
					}
				}
				this.$Request.getT(url, data).then(res => {
					this.errList = [...this.errList, ...res.data.records]
					this.total = res.data.total
					uni.hideLoading()
					// this.getMatterById()
					this.page++
				})
			},
			open() {
				this.showModal = true
			},
			close() {
				this.showModal = false
			},
			// 底部移除按钮
			remove() {
				this.$Request.getT('/app/topicError/deleteTopicError', {
					errorId: this.errList[this.current].errorId
				}).then(res => {
					this.errList = this.errList.filter(item => item.errorId !== this.errList[this.current]?.errorId)
					this.$nextTick(() => {
						this.current = this.current - 1 < 0 ? 0 : this.current - 1
						this.total = this.total - 1
						if (!this.errList.length) {
							uni.navigateBack({
								delta: 1
							})
						}
					})
					this.close()
				})
			},
		},
		onLoad(options) {
			this.infoId = options.infoId
			this.type = options.type
			this.bankId = options.bankId
			if (this.type == '1') {
				this.backText = this.$t('bar.tmxq')
			} else {
				this.backText = this.$t('bar.ctsc')
			}
			this.getErrorList()
		}
	}
</script>


<style lang="scss" scoped>
	.num_row {
		display: flex;
		justify-content: flex-end;
		align-items: center;
		height: 80rpx;
		padding-right: 40rpx;
	}

	.errlist {
		background: #fff;
	}

	.footer {
		width: 100%;
		height: 120rpx;
		box-sizing: border-box;
		background-color: #fff;
		border: 2rpx solid transparent;
		border-top-color: #F0F0F0;
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;

		&_placeholder {
			width: 100%;
			height: 120rpx;
		}

		image {
			width: 50rpx;
			height: 50rpx;
		}
	}
</style>