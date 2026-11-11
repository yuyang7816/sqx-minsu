<template>
	<view>
		<u-navbar :back-text="$t('pagesText.pllb')"></u-navbar>
		<view class="box">
			<template v-if="commit.length">
				<view v-for="(item, index) in commit" :key="index">
					<view class="plbox">
						<view class="flex align-center ">
							<!-- 	<image :src="item.userAvatar ? item.userAvatar : '../../static/logo.png'"
								style="width: 64rpx;height: 64rpx;border-radius: 50%;"></image> -->
							<image :src="item.avatar" style="width: 64rpx;height: 64rpx;border-radius: 50%;"></image>
							<view class="flex align-center justify-between flex-sub">
								<view class="margin-left-xs">{{ item.userName }}</view>
								<view class="time">{{ item.createTime }}</view>
							</view>
						</view>
						<view class="margin-top-sm" style="font-size: 26rpx;color: #333333;">{{ item.evaluateMessage }}</view>
					</view>
					<view class="margin-tb-sm" style="width: 100%;height: 1rpx;background: #F5F5F2;"></view>
				</view>
			</template>
			<u-empty mode="message" :text="$t('pagesText.zwpl')" :margin-top="250" v-else></u-empty>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				commitotal: 0,
				commit: [],
				goodId: null
			}
		},
		onLoad({
			goodId
		}) {
			this.goodId = goodId
			this.getconmmit()
		},
		onShow() {

		},
		methods: {
			getconmmit() {
				let data = {
					page: this.page,
					limit: 10,
					goodId: this.goodId
				}
				this.$Request.getT('/app/evaluate/getMyEvaluateList', data).then(res => {
					if (res && res.code == 0) {
						if (this.page == 1) {
							this.commit = res.data.records
						} else {
							this.commit = [...this.commit, ...res.data.records]
						}
						this.commitotal = res.data.total
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
		},
		onPullDownRefresh() {
			this.page = 1
			this.getconmmit()
		},
		onReachBottom() {
			if (this.page < this.commitotal) {
				this.page += 1
				this.getconmmit()
			}
		},
	}
</script>

<style lang="scss">
	page {
		// background: #F5F5F5;
		background: #FFFFFF;
	}

	.box {
		// background: #FFFFFF;
		margin: 30rpx;
	}

	.plbox {
		margin-top: 25rpx;

		.time {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 24rpx;
			color: #999999;
		}
	}
</style>