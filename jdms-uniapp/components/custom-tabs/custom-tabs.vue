<template>
	<view class="custom-tabs">
		<scroll-view :scroll-x="true" class="left-tab" :scroll-into-view="scrollIntoView ? `tab${current}` : null"
			scroll-with-animation>
			<view style="display: flex;">
				<view :class="[current === index ? 'active' : '']" class="tab-item" :id="`tab${index}`"
					:style="[tabStyleFn(tabStyle)]" v-for="(item, index) in data" :key="index" @click="onClickTab(index)">
					{{ item[name] }}
				</view>
			</view>
		</scroll-view>
		<!-- 右侧按钮插槽：可在父组件中插入文字或图片(非必填) -->
		<view class="right-btn click-state">
			<slot name="rightTtn"></slot>
		</view>
	</view>
</template>

<script>
export default {
	props: {
		// tab列表数据
		data: {
			type: Array,
			required: false,
		},
		// 当前高亮的tab
		current: {
			type: [Number, String],
			default: 0,
		},
		// 传值的属性名
		value: {
			type: String,
			default: 'id',
		},
		// tab的排列方式
		tabStyle: {
			type: String,
			default: 'flexStart',
		},
		//是否开启高亮滚动
		scrollIntoView: {
			type: Boolean,
			default: false,
		},
		name: {
			type: String,
			default: 'name',
		},
	},
	data() {
		return {

		};
	},
	methods: {
		tabStyleFn(name) {
			let style = {
				flexStart: { justifyContent: 'flex-start' },
				spaceBetween: { justifyContent: 'space-between' },
				flex1: { flex: 1 },
			}
			return style[name];
		},
		onClickTab(index) {
			this.$emit('changeTab', index)
		},
	},
};
</script>

<style lang="scss" scoped>
.custom-tabs {
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;
	border-bottom: 1rpx solid #f5f5f5;
	padding: 0 28rpx;
	color: rgba(0, 0, 0, .5);
	font-size: 28rpx;
	overflow-x: auto;
	background-color: #fff;

	.left-tab {
		// display: flex;
		// align-items: center;
		// justify-content: flex-start;
		overflow-x: auto;

		/deep/ .uni-scroll-view-content {
			display: flex;
			align-items: center;
			justify-content: flex-start;
		}

		.tab-item {
			text-align: center;
			padding: 24rpx 28rpx;
			position: relative;
			transition: all .4s;
			white-space: nowrap;

			&::after {
				content: '';
				position: absolute;
				bottom: 0;
				left: 50%;
				height: 8rpx;
				width: 0;
				opacity: 0;
				border-radius: 8rpx;
				background-color: #1fa3ff;
				transition: all .4s;
			}
		}
	}

	.right-btn {
		color: #1fa3ff;
		position: relative;
		padding-left: 4rpx;
		white-space: nowrap;

		&::before {
			content: '';
			width: 1em;
			height: 100%;
			position: absolute;
			top: 0;
			left: -1em;
			background: linear-gradient(to left, rgba(255, 255, 255, 1), rgba(255, 255, 255, .2));
		}
	}

	.active {
		color: rgba(0, 0, 0, .9);
		font-weight: bold;
		// font-size: 36rpx;

		&.tab-item::after {
			left: calc(50% - 16rpx);
			width: 32rpx;
			opacity: 1;
		}
	}
}
</style>