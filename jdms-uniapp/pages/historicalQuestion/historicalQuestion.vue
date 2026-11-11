<template>
	<view class="container bf">
		<u-navbar :back-text="$t('pagesText.lnzt')"></u-navbar>
		<view class="before">
			<view class="cell flex ">
				<view style="flex:1;"> 教师资格证考试中学【教育知识与 能力】真题</view>
				<view>
					<text>20套</text>
					<u-icon name="arrow-right"></u-icon>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			};
		}
	}
</script>

<style lang="scss" scoped>
	.cell {
		padding: 30rpx;
		font-size: 32rpx;
		color: #333;
		position: relative;
		background: #fff;
	}

	.before {
		width: fit-content;

		&::before {
			content: '';
			width: calc(100vw - 60rpx);
			height: 2rpx;
			background: #eee;
			bottom: 0;
			left: 50%;
			transform: translate(-50%);
		}
	}
</style>