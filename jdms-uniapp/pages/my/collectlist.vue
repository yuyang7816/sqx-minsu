<template>
	<view class="collect_list container">
		<u-navbar :back-text="$t('repeatUse.wdsc')"></u-navbar>
		<view class="num_row" v-if="collectList.length>0"><text
				style="margin-right: 6rpx;">{{ swiperCurrent + 1 }}</text><text style="color:#999">/ {{
      total
    }}</text></view>
		<swiper class="collect_" :style="[{ height: swiperHeight }]" :current="swiperCurrent" @change="onChange($event)"
			v-if="collectList.length">
			<swiper-item v-for="(item, index) in collectList" :key="item.matterId" class="swiper-item">
				<sub-err type="collect" :detail="item" :height="swiperHeight" :typerout="'4'" />
			</swiper-item>
		</swiper>
		<view class="footer flex-c jc alic" @click="open" v-if="collectList.length>0">
			<image src="/static/images/star.png" mode="scaleToFill" />
			<text>{{$t('repeatUse.qxsc')}}</text>
		</view>
		<u-empty mode="data" :margin-top="150" v-if="collectList.length == 0"></u-empty>
		<u-modal v-model="showModal" :content="$t('repeatUse.modalQxsc')+'？'" :show-cancel-button="true"
			:async-close="false" @concel="close" @confirm="collectMethod" :confirm-text="$t('btnText.qd')"
			:title="$t('repeatUse.ts')" :cancel-text="$t('repeatUse.qx')"></u-modal>
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
				showModal: false,
				swiperHeight: '',
				collectList: [],
				swiperCurrent: 0,
				pageOption: {
					page: 1,
					limit: 999
				},
				finished: false,
				total: 0,

			}
		},
		onLoad() {
			this.getTopicCollectOfLists()
			this.$nextTick(() => {
				uni.createSelectorQuery().in(this).select('.collect_list').boundingClientRect(data => {
					this.swiperHeight = `calc(${data.height}px - 44px)`
				}).exec()
			})
		},
		methods: {
			onChange(e) {
				this.swiperCurrent = e.detail.current
				if (this.swiperCurrent == this.collectList.length - 1) {
					this.getTopicCollectOfLists()
				}
			},
			// 获取我的收藏列表
			getTopicCollectOfLists() {
				this.$Request.getT('/app/scFollow/getMyFollowList', this.pageOption).then(res => {
					if (this.pageOption.page == 1) {
						this.collectList = res.data.records
					} else {
						this.collectList = [...this.collectList, ...res.data.records]
					}

					this.total = res.data.total
					this.$set(this.pageOption, 'page', this.pageOption.page + 1)
				})
			},
			open() {
				this.showModal = true
			},
			close() {
				this.showModal = false
			},
			// 底部取消收藏
			collectMethod() {
				this.$Request.postJson('/app/scFollow/updateFollow', {
					byId: this.collectList[this.swiperCurrent].byId,
					type: 1
				}).then(res => {
					if (res.code == 0) {
						this.pageOption.page = 1
						this.getTopicCollectOfLists()
						this.close()
						console.log(res.code, "22222222+")
					}
				})
			}
		},
		// onReachBottom() {
		// 	if (this.pageOption.page < this.total) {
		// 		this.page += 1
		// 		this.getTopicCollectOfLists()
		// 	}
		// },
	}
</script>


<style lang="scss" scoped>
	.collect_list {
		background: #fff;
	}

	.num_row {
		display: flex;
		justify-content: flex-end;
		align-items: center;
		height: 80rpx;
		padding-right: 40rpx;
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