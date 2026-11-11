<template>
	<view class="chapter_detail">
		<u-navbar back-text=" " :custom-back="back"></u-navbar>
		<view class="sub_type flex sb alic">
			<text style="font-weight: 700;"
				v-if="topicLists.length>0">{{ topicLists[currentIndex].matterType == 1 ? $t('pageText.dan') : $t('pageText.duo') }}{{$t('pageText.xkgt')}}</text>
			<view class="newadd"><u-count-down :timestamp="countdownTime" color="#017EFF" @end="downEnd"></u-count-down>
			</view>
			<text>{{ currentIndex + 1 }}<text style="color:#999;">/{{ topicLists.length }}</text></text>
		</view>
		<!-- <view class="containers"> -->
		<swiper class="swiper" :current="currentIndex" @change="swiperChange"
			:style="[{ height: `calc(100vh - ${height}px)` }]" v-if="showQue == 1">
			<swiper-item v-for="(item, index) in topicLists" :key="item.matterId">
				<view class="sub_card question">
					<view class="sub">{{ item.matterContent }}</view>
					<view class="checkbox">
						<view class="checkItem flex alic options" v-for="(option, opIndex) in item.matterJson"
							:key="option.serialNumber" @click="selectOption(index, option.serialNumber)">
							<view class="" :class="{
                'option-selected': isSelected(index, option.serialNumber),
                'correct': isCorrect(index, option.serialNumber) && showAnalysis[index],
                'incorrect': isSelected(index, option.serialNumber) && !isCorrect(index, option.serialNumber) && showAnalysis[index],
                'disabled': showAnalysis[index] // 如果展示解析，则禁用选项
              }">
								{{ option.serialNumber }}
							</view>
							<text style="margin-left: 18rpx;">{{ option.answerOption }}</text>
						</view>
					</view>
					<view v-if="showAnalysis[index]">
						<view class="rate">
							<view>{{$t('pagesText.zqda')}}：<text style="color:#63ECA2">{{ item.answer }}</text>
							</view>
							<view>{{$t('pagesText.btgbd')}}{{ analysis.answerCount }}{{$t('order.ci')}}，{{$t('pagesText.zql')}}:
								{{ ((analysis.rightChance)*100).toFixed(2) }}%
							</view>
							<view>{{$t('pagesText.ycx')}}： <text
									style="color:#FEAEA1">{{ analysis.mostErrorAnswer || $t('index.zwssjg')}}</text>
							</view>
						</view>
						<scroll-view scroll-y class="scrollable-content" scroll-y="true" style="max-height: 600rpx;">
							<view class="desc" style="padding-bottom: 200rpx;">
								<view style="margin: 20rpx 0;">
									<u-tag mode="dark" :text="$t('pagesText.tmjx')"></u-tag>
								</view>
								<view>{{ analysis.matterAnalysis }}</view>
							</view>
							<view class="" style="height: 300rpx;"></view>
						</scroll-view>
					</view>
				</view>
			</swiper-item>
		</swiper>
		<!-- </view> -->


		<view class="footerBtn">
			<view class="changeBtn">
				<view class="" v-if=" currentIndex + 1 != 1" @click="changeCuttent('0')"
					:style="{width:currentIndex + 1 != 1 ? '100%' : '50%'}">{{$t('pagesText.syt')}}</view>
				<view class="" v-if="currentIndex + 1 !== topicLists.length" @click="changeCuttent('1')"
					:style="{width:currentIndex + 1 !== topicLists.length ? '100%' : '50%'}">{{$t('pagesText.xyt')}}
				</view>
			</view>
			<view class="operate_bar flex sa alic">
				<view class="flex-c alic" @click="toCard">
					<image src="../../static/images/answer.png" mode="scaleToFill" />
					<text>{{$t('pageText.dtka')}}</text>
				</view>
				<!-- <view class="changeBtn">
					<view class="" v-if=" currentIndex + 1 != 1" @click="changeCuttent('0')">上一题</view>
					<view class="" v-if="currentIndex + 1 !== topicLists.length" @click="changeCuttent('1')">下一题</view>
				</view> -->
				<view class="flex-c alic" @click="showModal = true">
					<image src="../../static/images/submit.png" mode="scaleToFill" />
					<text>{{$t('pageText.jj')}}</text>
				</view>
			</view>
		</view>





		<!-- <view class="footerBtn">
			<view class="newadd">考试结束时间：<u-count-down :timestamp="countdownTime" color="#017EFF"
					@end="downEnd"></u-count-down>
			</view>
			<view class="operate_bar flex sa alic">
				<view class="flex-c alic" @click="toCard">
					<image src="../../static/images/answer.png" mode="scaleToFill" />
					<text>答题卡</text>
				</view>
				<view class="changeBtn">
					<view class="" v-if=" currentIndex + 1 != 1" @click="changeCuttent('0')">上一题</view>
					<view class="" v-if="currentIndex + 1 !== topicLists.length" @click="changeCuttent('1')">下一题</view>
				</view>
				<view class="flex-c alic" @click="showModal = true">
					<image src="../../static/images/submit.png" mode="scaleToFill" />
					<text>交卷</text>
				</view>
			</view>
		</view> -->
		<u-modal v-model="showModal"
			:content="`${userAnswers.length < topicLists.length ? $t('pageText.tmwzd')+'，' : ''}${$t('pageText.qrjjma')}？`"
			:show-cancel-button="true" :confirm-text="$t('repeatUse.qr')" :cancel-text="$t('pageText.dcl')"
			:title="$t('repeatUse.ts')" @confirm="confirm"></u-modal>
		<u-modal v-model="showExitTipModal" :content="$t('pageText.zttc')+'？'" :show-cancel-button="true" @confirm="exit"
			:confirm-text="$t('repeatUse.qr')" :cancel-text="$t('pageText.dcl')" :title="$t('repeatUse.ts')"></u-modal>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				height: 0,
				userAnswers: [], // 用户的答案数组
				showAnalysis: [], // 是否展示解析的数组
				currentIndex: 0, // 当前题目索引
				topicLists: [],
				star: '/static/images/star.png',
				star_ac: require('../../static/images/star_ac.png'),
				analysis: {}, //题目解析处内容
				answerCard: [], //答题卡列表
				showModal: false,
				showExitTipModal: false,
				countdownTime: 0,
				date: '', //考试时间倒计时
				countdownStatus: 0,
				userInfo: uni.getStorageSync('userInfo'),
				showQue: 0
			};
		},
		onLoad() {
			this.getMatterList();
			this.getTime()
		},
		onShow() {
			uni.$once('setChangeCurrent', data => {
				this.currentIndex = data
			})
		},
		onReady() {
			this.height += uni.getSystemInfoSync().statusBarHeight + 48
			uni.createSelectorQuery().in(this).select('.sub_type').boundingClientRect(data => {
				this.height += data.height
			}).exec()
		},
		methods: {
			exit() {
				setTimeout(() => {
					this.confirm()
					// this.showModal = true
				}, 1500)
			},
			downEnd() {
				this.countdownTime = 0
				console.log(this.countdownTime, "this.countdownTime")
				if (this.countdownTime <= 0) {
					uni.showToast({
						title: this.$t('pageText.kssjyjs'),
						icon: 'none',
						mask: true
					})
					setTimeout(() => {
						this.confirm()
						// this.showModal = true
					}, 1500)
				}
			},
			// 按钮切换题目
			changeCuttent(val) {
				if (val == 1) {
					this.currentIndex = this.currentIndex + 1
				} else {
					this.currentIndex = this.currentIndex - 1
				}
				this.showQue = 0
				this.getMatterById()
			},
			// 只能模考所有的考试时间倒计时取得都是这个配置
			getTime() {
				this.$Request.getT('/app/common/419').then(res => {
					if (res.code == 0) {
						this.countdownTime = res.data.value * 60
					}
				})
			},
			toCard() {
				// 已答汇总
				let arr = new Array(this.topicLists.length).fill(null);
				this.answerCard.map(item => {
					if (item.answer) {
						item.answer = item.answer.toString()
					}
				})
				arr = arr.map((item, index) => this.answerCard.find(answer => answer.index === index) || {})
				uni.$emit('getAnswerList', arr)
				uni.navigateTo({
					url: '/subPackage/chapter/chapterCard?answerlist=' + JSON.stringify(arr)
				})
			},
			back() {
				if (this.answerCard.length != this.topicLists.length) {
					this.showExitTipModal = true
				} else {
					uni.switchTab({
						url: '/pages/index/index'
					})
				}
			},
			// 获取题目列表
			getMatterList() {
				let data = {
					typeId: uni.getStorageSync('typeId'),
				}
				this.$Request.getT('/app/topicMatter/getRandomTopicMatterList', data).then(res => {
					if (res.code == 0) {
						this.topicLists = res.data
						this.getMatterById()
						console.log(this.topicLists, '0000000000000000')
					}
				})
			},
			// 获取单个题目详情
			getMatterById() {
				this.$Request.getT('/app/topicMatter/getTopicMatterById', {
					matterId: this.topicLists[this.currentIndex].matterId,
					userId: uni.getStorageSync('userId')
				}).then(res => {
					this.analysis = res.data
					this.showQue = 1
				})
			},
			// 获取成绩
			submitanswerCard(answerList) {

			},
			// 确认交卷
			confirm() {
				let answer = []
				this.answerCard.map(item => {
					answer.push({
						// answer: Array.from(item.answer, String).join(','),
						answer: item.answer.toString(),
						matterId: item.matterId
					})
				})
				const answerList = answer.filter((item, index, self) =>
					index === self.findIndex((t) => t.matterId === item.matterId)
				);
				this.$Request.postJson('/app/topicAnswer/saveTopicAnswer', {
					answerSheetVoList: answerList
				}).then(res => {
					this.countdownTime = 0
				})
				this.$Request.postJson('/app/topicExamMatter/saveTopicExamMatter', {
					answerSheetVoList: answerList
				}).then(res => {
					if (res.code == 500) return
					uni.showToast({
						title: this.$t('pageText.jjcg'),
						icon: 'success',
						mask: true
					})
					setTimeout(() => {
						uni.redirectTo({
							url: '/pages/intelligentExamination/result?resultMsg=' + encodeURIComponent(JSON.stringify(res
								.data))
						})
						this.countdownTime = 0
					}, 1500);

				})
			},
			selectOption(questionIndex, serialNumber) {
				// 用户选择选项
				if (!this.showAnalysis[questionIndex]) { // 只有在未展示解析时才能选择
					if (typeof this.topicLists[questionIndex].answer === 'string') {
						// 对于多选题，处理用户的选择
						let selectedOptions = this.userAnswers[questionIndex] || [];
						if (selectedOptions.includes(serialNumber)) {
							// 如果已经选中，则取消选择
							selectedOptions = selectedOptions.filter(sn => sn !== serialNumber);
						} else {
							// 如果没有选中，则添加到已选中列表
							selectedOptions.push(serialNumber);
						}
						this.$set(this.userAnswers, questionIndex, selectedOptions);
						this.answerCard.push({
							index: questionIndex,
							matterId: this.topicLists[questionIndex].matterId,
							answer: selectedOptions,
							rightAnswer: this.topicLists[questionIndex].answer
						})
						let answer = this.topicLists[questionIndex].answer.split(",")
						this.allElementsExist = selectedOptions.every(element => answer.includes(element));
					} else {
						console.log("serialNumber", serialNumber)
						// 对于单选题，直接记录用户选择
						this.$set(this.userAnswers, questionIndex, serialNumber);
						this.answerCard.push({
							answer: serialNumber,
							index: questionIndex,
							matterId: this.topicLists[questionIndex].matterId,
							rightAnswer: this.topicLists[questionIndex].answer
						})
					}
				}
			},
			isCorrect(questionIndex, serialNumber) {
				// 判断选项是否是正确答案
				const answer = this.topicLists[questionIndex].answer.split(',');
				if (Array.isArray(answer)) {
					return answer.includes(serialNumber);
				} else {
					return serialNumber === answer;
				}
			},
			getanswers(questionIndex) {
				// 获取正确答案
				const answer = this.topicLists[questionIndex].answer.split(',');
				return answer.join(', ');
			},
			isSelected(questionIndex, serialNumber) {
				// 判断选项是否被选择
				const selectedOptions = this.userAnswers[questionIndex] || [];
				return Array.isArray(selectedOptions) ? selectedOptions.includes(serialNumber) : selectedOptions ===
					serialNumber;
			},
			swiperChange(e) {
				this.currentIndex = e.detail.current;
				this.showQue = 0
				this.getMatterById()
			},
			showAnswerAnalysis(questionIndex) {
				// 展示答案解析
				const answer = this.topicLists[questionIndex].answer.split(',');
				const userAnswer = this.userAnswers[questionIndex];

				if (Array.isArray(answer)) {
					// 对于多选题，检查用户选择的数量是否与正确答案数量相同
					if (userAnswer && userAnswer.length === answer.length) {
						this.$set(this.showAnalysis, questionIndex, true);
					} else {
						console.log("用户答案不完整", this.allElementsExist);
						if (this.allElementsExist == false) {
							this.$set(this.showAnalysis, questionIndex, true);
						}
					}
				} else {
					// 对于单选题，只要用户做出选择就显示解析
					if (userAnswer !== undefined) {
						this.$set(this.showAnalysis, questionIndex, true);
					}
				}
			}
		},
		watch: {
			userAnswers: {
				handler() {
					// 监听用户答案变化，在用户完成选择后展示答案解析
					this.showAnswerAnalysis(this.currentIndex);
				},
				deep: true
			}
		}
	};
</script>

<style lang="scss" scoped>
	.changeBtn {
		display: flex;
		width: 100%;

		view {
			width: calc(100%/2);
			text-align: center;
			// color: #017EFF;
			padding: 30rpx 0;
			padding: 30rpx 0rpx;
		}
	}

	.changeBtn view:first-child {
		background-color: #F0F0F0;
		color: #333333;
	}

	.changeBtn view:last-child {
		background-color: #017EFF;
		color: #ffffff;
	}

	.footerBtn {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
	}

	.newadd {
		color: #017EFF;
		text-align: center;
		margin: auto;
		padding-right: 60rpx;
		// height: 100rpx;
		// line-height: 100rpx;
	}

	/deep/ .u-countdown-item {
		background: $default-back !important;
	}

	.containers {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: calc(100vh - 190rpx);
	}

	// /deep/ .uni-swiper-wrapper {
	// 	width: 300px;
	// }

	.swiper {
		width: 100%;
		height: 100%;
	}

	.question {
		padding: 20px;
	}

	.title {
		font-weight: bold;
		margin-bottom: 10px;
	}

	.options {
		margin-top: 10px;
		// font-size: 34rpx;
	}

	.options view {
		border-radius: 50%;
		margin-bottom: 5px;
		cursor: pointer;
		border: 1px solid #ccc;
		width: 70rpx;
		height: 70rpx;
		text-align: center;
		line-height: 70rpx;
	}

	.options .option-selected {
		background-color: #0096FF;
		color: #ffffff;
		border: none;
	}

	.options .correct {
		background-color: #5BEB9E;
		color: #ffffff;
		border: none;
	}

	.options .incorrect {
		background-color: #FE785F;
		color: #ffffff;
		border: none;
	}

	.options .disabled {
		pointer-events: none;
		/* 禁用点击事件 */
		opacity: 0.6;
		/* 按钮半透明显示 */
	}

	.analysis {
		margin-top: 10px;
		font-style: italic;
	}

	.answer-sheet {
		display: flex;
		justify-content: center;
		margin-top: 20px;
	}

	.answer-item {
		width: 30px;
		height: 30px;
		display: flex;
		justify-content: center;
		align-items: center;
		border: 1px solid #ccc;
		border-radius: 50%;
		margin: 0 5px;
		cursor: pointer;
	}

	.answer-item.answered {
		font-weight: bold;
	}

	.answer-item.correct {
		background-color: green;
		color: white;
	}

	.answer-item.incorrect {
		background-color: red;
		color: white;
	}





	.chapter_detail {
		background: #fff;

		.sub_type {
			padding: 35rpx;
			font-size: 24rpx;
			color: #333;
			background: $default-back;
		}

		.sub_card {
			padding: 48rpx 32rpx 48rpx 32rpx;
			background: #fff;
		}

		.checkbox {
			margin-top: 50rpx;

			.checkItem {
				margin-bottom: 50rpx;
			}
		}

		.operate_bar {
			background-color: #fff;
			box-sizing: border-box;
			border: 2rpx solid transparent;
			border-top-color: #eee;
			padding-top: 12rpx;

			image {
				width: 50rpx;
				height: 50rpx;
			}
		}
	}

	.rate {
		&>view {
			margin-bottom: 29rpx;
		}
	}

	.choose {
		background: $theme-color !important;
		color: #fff;
	}
</style>