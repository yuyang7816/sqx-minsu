<template>
	<view class="choose_board">
		<u-popup :value="value" length="auto" mode="right" :closeable="true" close-icon="arrow-left"
			close-icon-pos="top-left" @close="$emit('input', false)">
			<view class="" :style="{ height: iStatusBarHeight + 'px'}"></view>
			<view :style="{ paddingTop: iStatusBarHeight + 'px'}">
				<!-- <view class="area_choose flex sb alic" @click="chooseArea">
          <text>点击选择</text>
          <text>西安市</text>
        </view> -->
				<view class="board flex">
					<scroll-view :style="[{ height: scrollHeight }]" style="background: #f5f5f5;" :scroll-y="true">
						<view class="type flex jc alic" v-for="(item, index) in typeList" :key="item.typeId"
							:class="{ type_active: currents[0] === index }" @click="$emit('update:currents', [index, 0])">{{
                item.typeName }}</view>
					</scroll-view>
					<scroll-view :style="[{ height: scrollHeight }]" :scroll-y="true">
						<view class="children flex jc alic" v-for="(item, index) in childrenArray" :key="item.typeId"
							:class="{ children_active: currents[1] === index }" @click="confirm(index,item)">{{ item.typeName }}
						</view>
					</scroll-view>
				</view>
			</view>
		</u-popup>
	</view>
</template>


<script>
	export default {
		options: {
			styleIsolation: 'shared',
		},
		props: {
			value: {
				type: Boolean,
				default: false
			},
			typeList: {
				type: Array,
				default: () => []
			},
			currents: {
				type: Array,
				default: () => [0, 0]
			},
			iStatusBarHeight: {
				type: Number
			}
		},
		data() {
			return {
				areaHeight: 0,
			}
		},
		mounted() {
			// this.iStatusBarHeight = uni.getSystemInfoSync().statusBarHeight;
			console.log(this.iStatusBarHeight, 'this.iStatusBarHeight')
		},
		computed: {
			scrollHeight() {
				return `calc(100vh - ${this.areaHeight}px - 44px)`
			},
			paddingTop() {
				return {
					paddingTop: `calc(${this.statusBarHeight}px + 44px)`
				}
			},
			childrenArray() {
				return this.typeList.length ? this.typeList.find(item => item.typeId === this.typeList[this.currents[0]].typeId)
					?.topicTypeList : []
			}
		},
		methods: {
			chooseArea() {},
			confirm(index, item) {
				uni.setStorageSync('typeId', item.typeId)
				this.$emit('update:currents', [this.currents[0], index])
				this.$emit('input', false)
				uni.$emit('typeIdNumber',item.typeId)
			}
		},
		// watch: {
		//   value(newv) {
		//     if (newv) {
		//       !this.areaHeight && this.$nextTick(() => {
		//         uni.createSelectorQuery().in(this).select('.area_choose').boundingClientRect(data => this.areaHeight = data.height).exec()
		//       })
		//     }
		//   }
		// }
	}
</script>


<style lang="scss" scoped>
	.choose_board {
		.area_chhose {
			width: 100%;
			height: 100rpx;
			box-sizing: border-box;
			padding: 0 32rpx;
		}

		.type,
		.children {
			height: 120rpx;
			width: 100%;
		}

		.type {
			// background: #F6F6F6;
		}

		.children {
			background: #fff;
			position: relative;

			&::before {
				content: '';
				position: absolute;
				width: 80%;
				height: 2rpx;
				background-color: #eee;
				bottom: 0;
				left: 50%;
				transform: translate(-50%);
			}
		}

		.type_active {
			background: #EDF8FF !important;
			color: #0096FF !important;
		}

		.children_active {
			color: #0096FF !important;
		}

		.board {
			scroll-view {
				width: 50vw;
				font-size: 30rpx;
				font-weight: bold;
			}
		}
	}


	// #ifdef MP-WEIXIN
	/deep/.u-close--top-left {
		top: 44px !important;
	}

	// #endif
</style>