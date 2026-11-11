<template>
	<scroll-view :scroll-y="true" :style="[{ height }]">
		<view class="sub_err">
			<view class="sub_title">{{ topic.matterContent ||detail.matterContent }}</view>
			<view class="options">
				<view class="" v-if="typerout == 4">
					<view class="option_row flex alic"
						:class="[{ active_: key.split(',').includes(item.serialNumber) }, { active_err_: key1 === item.serialNumber }]"
						v-for="item in topic.matterJson" :key="item.serialNumber">
						<view class="circle" :class="[
					    { circle_active: key.split(',').includes(item.serialNumber) },
					    { circle_err: !key.split(',').includes(item.serialNumber) && key1.split(',').includes(item.serialNumber) }
					  ]">
							{{ item.serialNumber }}
						</view>
						<text style="margin-left: 18rpx;">{{ item.answerOption }}</text>
					</view>
				</view>

				<view class="" v-else>
					<view class="option_row flex alic"
						:class="[{ active_: key.split(',').includes(item.serialNumber) }, { active_err_: key1 === item.serialNumber }]"
						v-for="item in detail.matterJsonList" :key="item.serialNumber">
						<view class="circle" :class="[
					    { circle_active: key.split(',').includes(item.serialNumber) },
					    { circle_err: !key.split(',').includes(item.serialNumber) && key1.split(',').includes(item.serialNumber) }
					  ]">
							{{ item.serialNumber }}
						</view>
						<text style="margin-left: 18rpx;">{{ item.answerOption }}</text>
					</view>
				</view>
			</view>


			<view class="rate">
				<view>{{$t('pagesText.zqda')}}：<text style="color:#63ECA2">{{ detail.rightAnswer || topic.answer }}</text>
				</view>
				<view>
					{{$t('pagesText.btgbd')}}{{ detail.answerCount ||  detail.topicMatter.answerCount}}{{$t('order.ci')}}，{{$t('pagesText.zql')}}:
					<text
						v-if="detail.rightChance != null">{{ (detail.rightChance * 100).toFixed(2) || ( detail.topicMatter.rightChance * 100).toFixed(2)}}%</text>
					<text v-else>0.00%</text>
				</view>
				<view v-if="detail.topicMatter != undefined">{{$t('pagesText.ycx')}}： <text
						style="color:#FEAEA1">{{ detail.topicMatter.mostErrorAnswer || detail.topicMatter.mostErrorAnswer || $t('repeatUse.zw')}}</text>
				</view>
			</view>
			<scroll-view scroll-y class="scrollable-content" scroll-y="true" style="max-height: 600rpx;">
				<view class="desc" style="padding-bottom: 200rpx;padding: 0 20rpx;">
					<view style="margin: 20rpx 0;">
						<u-tag mode="dark" :text="$t('pagesText.tmjx')"></u-tag>
					</view>
					<view>{{ detail.topicMatter && detail.topicMatter.matterAnalysis || detail.matterAnalysis}}</view>
				</view>
				<view class="" style="height: 120rpx;"></view>
			</scroll-view>
		</view>
	</scroll-view>
</template>


<script>
	export default {
		props: {
			detail: {
				type: Object,
				required: true
			},
			height: {
				type: String,
				required: true
			},
			type: {
				type: String,
				required: true
			},
			typerout: {
				type: String,
			}
		},
		methods: {},
		computed: {
			topic() {
				return this.detail.topicMatter || {}
			},
			key() {
				return this.detail[this.type === 'error' ? 'rightAnswer' : 'answer'] || ''
			},
			key1() {
				return this.detail.answer || ''
			}
		},
		mounted() {
			console.log(this.detail, "detaildetail")
		}
	}
</script>


<style lang="scss" scoped>
	.sub_err {
		box-sizing: border-box;
		background: #fff !important;

		&>view {
			padding: 0 30rpx;
		}

		.sub_title {
			font-size: 34rpx;
			color: #333;
		}

		.options {
			margin-bottom: 60rpx;
			padding: 0 !important;

			.option_row {
				margin-top: 11rpx;
				padding: 22rpx 30rpx;

				&:first-child {
					margin-top: 44rpx;
				}
			}

			.active_ {
				// background: #E1FFEF !important;
			}

			.active_err_ {
				// background: #fde9e5 !important;
			}
		}

		.rate {
			&>view {
				margin-bottom: 29rpx;
			}
		}
	}
</style>