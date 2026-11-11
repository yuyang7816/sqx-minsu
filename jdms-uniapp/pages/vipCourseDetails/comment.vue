<template>
	<view>
		<u-navbar :back-text="$t('order.pj')"></u-navbar>
		<view class="margin-lr">
			<!-- <view class="text-30 ">评价</view> -->
			<view class="content-box-txt">
				<u-input v-model="value" :placeholder="$t('toast.pjnr')" type="textarea" :border="false" :height="300"
					:auto-height="false" style="background-color: #f9f9f9;border-radius: 20rpx;padding: 10rpx 20rpx;" />
			</view>
		</view>

		<view class="btn" @click="submit">{{$t('btn.tj')}}</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				value: '',
				indentId: ''
			}
		},
		onLoad(e) {
			if (e.indentId) {
				this.indentId = e.indentId
			}
		},
		onShow() {

		},
		methods: {
			submit() {
				if (!this.value) {
					uni.showToast({
						title: this.$t('toast.pjnr'),
						icon: 'none'
					})
					return
				}
				let data = {
					indentId: this.indentId,
					evaluateMessage: this.value,
					evaluateImg: '',
					satisfactionFlag: ''
				}
				this.$Request.postT("/app/evaluate/addEvaluate", data).then(red => {
					if (red.code == 0) {
						uni.showToast({
							title: this.$t('toast.tjcg'),
							icon: 'none'
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					} else {
						uni.showToast({
							title: red.msg,
							icon: 'none'
						})
					}
					uni.hideLoading()
				});
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #FFFFFF;
	}

	.content-box-txt {
		margin-top: 20rpx;
	}

	.btn {
		margin: 0 30rpx;
		height: 88rpx;
		background-color: #416DF5;
		margin-top: 80rpx;
		border-radius: 10rpx;
		color: #ffffff;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>