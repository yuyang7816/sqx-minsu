<template>
	<view class="container" style="background: #fff;">
		<u-navbar :back-text="$t('pagesText.kszl')" :title-bold="true"></u-navbar>
		<u-tabs :list="list" :is-scroll="false" :current="current" @change="change"></u-tabs>
		<view class="materList">
			<view class="materList_content">
				<view class="mater_item" v-for="item in key" :key="item.goodId"
					@click="navigate('/pages/vipCourseDetails/vipCourseDetails', { goodId: item.goodId })">
					<view class="mater_item_top">
						<u-image :src="item.coverImg" width="100%" height="100%"></u-image>
					</view>
					<view class="mater_item_bot">
						<view class="mater_item_bot_title">{{ item.title }}</view>
						<text class="mater_item_bot_price" v-if="!current">{{$t('pagesText.hyj')}} {{$t('pagesText.hyj')}}<text
								class="font_bold">{{(item.nowBuyMoney *
							(item.vipRate || 1)).toFixed(2)
						}}</text> </text>
					</view>
				</view>
			</view>
		</view>
		<view class="promppt">
			<text>{{$t('pagesText.zsmygd')}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				current: 0,
				pageOption: {
					page: 1,
					limit: 10
				},
				goodList: [],
				finished: false
			};
		},
		computed: {
			list() {
				console.log(this.goodList);
				return this.goodList.map(({
					typeName: name
				}) => ({
					name
				}))
			},
			key() {
				return this.goodList[this.current]?.topicGoodsList || {}
			}
		},
		methods: {
			change(index) {
				this.current = index;
			},
			// 获取资料列表
			getGoodListById() {
				if (this.finished) return
				this.$Request.getT('/app/topicGoodsType/getTopicGoodsTypeList', {
					...this.pageOption,
					type: '2',
				}).then(res => {
					// this.goodList = res.data.records
					// if (res.data.records.length < this.pageOption.limit) this.finished = true
					// this.$set(this.pageOption, 'page', this.pageOption.page + 1)


					this.total = res.data.pages
					if (this.pageOption.page == 1) {
						this.goodList = res.data.records
					} else {
						this.goodList = [...this.goodList, ...res.data.records]
					}
				})
			}
		},
		watch: {
			current: {
				immediate: true,
				handler(newv) {
					this.getGoodListById()
				}
			}
		},
		onReachBottom() {
			if (this.pageOption.page < this.total) {
				this.pageOption.page += 1
				this.getGoodListById()
			}
		},
	}
</script>

<style lang="scss" scoped>
	.font_bold {
		font-weight: bold;
		font-size: 30rpx;
	}

	.materList {
		width: 750rpx;
		// height: 500rpx;
		margin-top: 27rpx;
		padding: 0rpx 28rpx 0 39rpx;

		.materList_content {
			width: 100%;
			// height: 500rpx;	
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;

			.mater_item {
				width: 328rpx;
				height: 287rpx;
				margin-bottom: 25rpx;

				.mater_item_top {
					width: 328rpx;
					height: 172rpx;
					// background: #5EB1FD;
					border-radius: 12rpx 12rpx 0rpx 0rpx;
				}

				.mater_item_bot {
					width: 328rpx;
					box-shadow: 0rpx 2rpx 4rpx 0rpx rgba(179, 179, 179, 0.13);
					border-radius: 0rpx 0rpx 12rpx 12rpx;
					padding: 24rpx 44rpx 24rpx 16rpx;

					.mater_item_bot_title {
						font-weight: bold;
						font-size: 30rpx;
						color: #333333;
						width: 100%;
						overflow: hidden;
						text-overflow: ellipsis;
						white-space: nowrap;
					}

					.mater_item_bot_price {
						font-weight: 500;
						font-size: 26rpx;
						color: #D64046;
						line-height: 2rpx;
					}

				}
			}
		}
	}

	.promppt {
		width: 750rpx;
		height: 25rpx;
		font-weight: 500;
		font-size: 26rpx;
		color: #999999;
		margin-top: 55rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>