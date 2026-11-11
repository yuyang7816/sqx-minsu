<template>
	<view class="chapter_card container">
		<u-navbar :back-text="$t('pageText.dtka')"></u-navbar>
		<view class="grid">
			<view class="circle" v-for="(item, index) in sheetList" :key="item.answerId"
				:class="{ circle_active: item.answer && item.circleShow == true, circle_err: item.answer && item.circleShow == false}"
				@click="toDetail(index)" v-if="item != ''">
				{{ index + 1 }}
			</view>
		</view>
		<view class="operate_btns flex sb">
			<view class="btn deep flex jc alic" @click="toFirst">{{$t('pagesText.tmjx')}}</view>
			<view class="btn shallow flex jc alic" @click="toContinue">{{$t('pageText.jxlx')}}</view>
		</view>
	</view>
</template>


<script>
	/**
	 * answerType
	 * 1:正确
	 * 2:错误
	 */
	export default {
		data() {
			return {
				inteType: '',
				sheetList: [],
				circle_err_Show: null
			}
		},
		onLoad(option) {
			if (option.answerlist) {
				this.sheetList = JSON.parse(option.answerlist);
			}
			this.currentNum = option.current;
			this.inteType = option.inteType
			this.areArraysEqualRegardlessOfOrder()
		},
		onShow() {},
		methods: {
			areArraysEqualRegardlessOfOrder() {
				this.sheetList.map((item, index) => {
					if (item.rightAnswer && item.answer) {
						let rightAnswer = item.rightAnswer.split(",")
						let answer;
						let sortedArr1;
						console.log('item.answer', )
						// debugger
						if (item.answer.length > 1) {
							if (item.answer) {
								answer = item.answer.split(",")
								sortedArr1 = answer.sort();
							}
						} else {
							if (item.answer) {
								answer = new Array(item.answer)
								sortedArr1 = answer[0];
							}
						}
						console.log(this.sheetList, "data")
						if (answer[0].length != rightAnswer.length) {
							item.circleShow = null
						} else {
							item.circleShow = null
						}
						// 对两个数组进行排序
						const sortedArr2 = rightAnswer.slice().sort();
						// 比较排序后的数组是否完全相等
						for (let i = 0; i < sortedArr1.length; i++) {
							if (item.answer.length == item.rightAnswer.length) {
								if (sortedArr1[i] !== sortedArr2[i]) {
									item.circleShow = false
								} else if (sortedArr1.toString() == sortedArr2.toString()) {
									item.circleShow = true
								}
							} else {
								if (item.answer.length != item.rightAnswer.length) {
									this.circle_err_Show = answer.every(element => item.rightAnswer.includes(element));
									if (this.circle_err_Show == false) {
										item.circleShow = false
									} else {
										item.circleShow = null
									}
								}
							}
						}
					}


				})
			},


			toFirst() {
				// this.$store.commit('chapterModule/setChangeCurrent', {
				// 	index: 0
				// })
				uni.navigateBack()
			},
			toContinue() {
				// this.$store.commit('chapterModule/setChangeCurrent', {
				// 	index: this.sheetList.findIndex(item => !item.answer) || (this.sheetList.length - 1)
				// })
				uni.navigateBack()
			},
			toDetail(index) {
				uni.$emit('setChangeCurrent', index)
				uni.navigateBack()
			},
		}
	}
</script>


<style lang="scss" scoped>
	.circle {
		width: 36px;
		height: 36px;
		border-radius: 50%;
		background-color: #F0F0F0;
		color: #333;
	}

	.circle_active {
		background-color: #0096FF;
	}

	.chapter_card {
		background: #fff;
	}

	.grid {
		padding: 60rpx;
		display: grid;
		grid-template-columns: 70rpx 70rpx 70rpx 70rpx 70rpx;
		grid-column-gap: 70rpx;
		grid-row-gap: 43rpx;
	}

	.operate_btns {
		padding: 16rpx 67rpx;
		position: fixed;
		left: 0;
		right: 0;
		bottom: env(safe-area-inset-bottom);

		.btn {
			width: 292rpx;
			height: 80rpx;
			border-radius: 40rpx;
			box-sizing: border-box;
			font-size: 28rpx;
		}

		.deep {
			background: $theme-color;
			color: #fff;
		}

		.shallow {
			border: 4rpx solid $theme-color;
			color: $theme-color;
		}
	}
</style>