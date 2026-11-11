<template>
	<view class="flex jc alic loading">
		<u-loading :show="true" v-if="status !== 'normal'"></u-loading>
		<text>{{ loadText ||$t('repeatUse.zzjz')+'...'}}</text>
	</view>
</template>


<script>
	export default {
		props: {
			loadText: {
				type: String,
				default: ''
			},
			/**
			 * @property {String} status
			 * @enum {
			 *  'normal'
			 *  'loading'
			 * }
			 */
			status: {
				type: String,
				default: 'loading'
			}
		}
	}
</script>


<style lang="scss" scoped>
	.loading {
		font-size: 24rpx;
		color: #999;
		padding: 24rpx 0;

		text {
			margin-left: 12rpx;
		}
	}
</style>