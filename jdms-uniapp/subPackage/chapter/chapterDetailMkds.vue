<template>
	<view class="chapter_detail">
		<u-navbar back-text=" " :custom-back="back"></u-navbar>
		<view class="sub_type flex sb alic">
			<text style="font-weight: 700;"
				v-if="topicLists.length>0">{{ topicLists[currentIndex].matterType == 1 ?  $t('pageText.dan') : $t('pageText.duo') }}{{$t('pageText.xkgt')}}</text>
			<view class="newadd" v-if="contestStatus == 1"><u-count-down :timestamp="countdownTime" color="#017EFF"
					@end="downEnd"></u-count-down>
			</view>
			<text>{{ currentIndex + 1 }}<text style="color:#999;">/{{ total }}</text></text>
		</view>
		<!-- <view class="containers"> -->
		<swiper :current="currentIndex" @change="swiperChange" :style="[{ height: `calc(100vh - ${height}px)` }]"
			v-if="showQue == 1">
			<swiper-item v-for="(item, index) in topicLists" :key="index">
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
							<!-- {{isSelected(index, option.serialNumber)}} -->
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
								<view>{{ analysis.matterAnalysis}}</view>
							</view>
							<view class="" style="height: 300rpx;"></view>
						</scroll-view>
					</view>
				</view>
			</swiper-item>
		</swiper>
		<!-- </view> -->
		<!-- 模考大赛展示 -->

		<view class="footerBtn">
			<view class="changeBtn">
				<view class="" v-if="currentIndex + 1 != 1" @click="changeCuttent('0')"
					:style="{width:currentIndex + 1 != 1 ? '100%' : '50%'}">{{$t('pagesText.syt')}}</view>
				<view class="" v-if="currentIndex + 1 !== topicLists.length"
					:style="{width:currentIndex + 1 !== topicLists.length ? '100%' : '50%'}" @click="changeCuttent('1')">
					{{$t('pagesText.xyt')}}
				</view>
			</view>
			<view class="operate_bar flex sa alic">
				<view class="flex-c alic" @click="collectMethod()">
					<image :src="topicLists[currentIndex] && topicLists[currentIndex].isFollow == 1 ? star_ac : star"
						mode="scaleToFill" />
					<text>{{$t('yao.sc')}}</text>
				</view>

				<view class="flex-c alic" @click="toCard">
					<image src="../../static/images/answer.png" mode="scaleToFill" />
					<text>{{$t('pageText.dtka')}}</text>
				</view>
				<view class="flex-c alic" @click="showModal = true">
					<image src="../../static/images/submit.png" mode="scaleToFill" />
					<text>{{$t('pageText.jj')}}</text>
				</view>

			</view>
		</view>
		<u-modal v-model="showModal"
			:content="`${userAnswers.length < topicLists.length ? $t('pageText.tmwzd')+'，' : ''}${$t('pageText.qrjjm')}？`"
			:show-cancel-button="true" :confirm-text="$t('pageText.qrjj')" :cancel-text="$t('pageText.dcl')"
			:title="$t('repeatUse.ts')" @confirm="confirm"></u-modal>
		<u-modal v-model="showExitTipModal" :content="$t('pageText.zttc')+'？'" :show-cancel-button="true" @confirm="confirm"
			:title="$t('repeatUse.ts')" :confirm-text="$t('repeatUse.ts')" :cancel-text="$t('pageText.dcl')"></u-modal>
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
				page: 1,
				limit: 999,
				total: '',
				countdownTime: 0,
				contestStatus: null,
				userInfo: uni.getStorageSync('userInfo'),
				subjectId: '',
				contestId: '',
				zjType: '', //从章节练习进来的文字判断
				lastMatterId: '',
				exerList: [],
				answerMc: [],
				showQue: 0,
				allElementsExist: ''
			};
		},
		onLoad({
			examId,
			subjectId,
			returnFromCard,
			contestId,
			infoId,
			subjectDuration,
			contestStatus,
			zjType
		}) {
			this.examId = examId
			this.subjectId = subjectId
			this.contestId = contestId
			this.infoId = infoId
			this.zjType = zjType
			if (contestStatus && subjectDuration) {
				this.countdownTime = subjectDuration * 60
				this.contestStatus = contestStatus
			}
			this.getContestMatterList();
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
			// 倒计时结束
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
			toCard() {
				// 已答汇总
				let arr = new Array(this.topicLists.length).fill(null);
				arr = arr.map((item, index) => this.answerCard.find(answer => answer.index === index) || {})
				arr.map(item => {
					if (item.answer) {
						item.answer = item.answer.toString()
					}
				})
				console.log(arr, 'arrarrarr')
				uni.navigateTo({
					url: '/subPackage/chapter/chapterCard?answerlist=' + JSON.stringify(arr)
				})
			},
			// 收藏题目
			collectMethod() {
				const topic = this.topicLists[this.currentIndex].matterId
				this.$Request.postJson('/app/scFollow/updateFollow', {
					byId: topic,
					type: 1
				}).then(res => {
					if (this.topicLists[this.currentIndex].isFollow == 0) {
						this.topicLists[this.currentIndex].isFollow = 1
					} else {
						this.topicLists[this.currentIndex].isFollow = 0
					}
					if (res.code == 0) {
						this.getMatterById()
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			// 模考大赛交卷
			confirm() {
				let answer = []
				this.answerCard.map(item => {
					answer.push({
						answer: item.answer.toString(),
						// answer: Array.from(item.answer, String).join(','),
						matterId: item.matterId
					})
				})
				const answerList = answer.filter((item, index, self) =>
					index === self.findIndex((t) => t.matterId === item.matterId)
				);
				uni.showLoading({
					title: this.$t('loading.jjz') + "..."
				})
				this.$Request.postJson('/app/topicAnswer/saveTopicAnswer', {
					answerSheetVoList: answerList.map(item => {
						delete item.index
						delete item.rightAnswer
						delete item.recordId
						return item
					})
				}).then(res => {
					this.getResult()
				})
			},
			back() {
				this.showExitTipModal = true
			},
			// 模考大赛交卷
			getResult() {
				let answer = []
				this.answerCard.map(item => {
					answer.push({
						// answer: Array.from(item.answer, String).join(','),
						answer: item.answer.toString(),
						recordId: item.recordId
					})
				})
				console.log(answer, "answeranswer")
				const answerList = answer.filter((item, index, self) =>
					index === self.findIndex((t) => t.recordId === item.recordId)
				);
				console.log(answerList, "answerList")
				this.$Request.postJson('/app/topicContestAnswer/saveTopicContestAnswer', {
					contestId: this.contestId,
					subjectId: this.subjectId,
					contestAnswerVoList: answerList
				}).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						setTimeout(() => {
							uni.navigateTo({
								url: '/subPackage/chapter/result?resultMsg=' + encodeURIComponent(JSON.stringify(res.data))
							})
						}, 1500);
					} else {
						uni.showToast({
							title: res.msg
						})
						uni.navigateBack()
					}
				})
			},
			swiperChange(e) {
				this.currentIndex = e.detail.current;
				this.showQue = 0
				this.getMatterById()
			},
			// 获取单个题目详情
			getMatterById() {
				this.$Request.getT('/app/topicMatter/getTopicMatterById', {
					matterId: this.topicLists[this.currentIndex].matterId,
					userId: uni.getStorageSync('userId')
				}).then(res => {
					if (res.code == 0) {
						this.analysis = res.data
						this.topicLists[this.currentIndex].isFollow = res.data.isFollow
						this.showQue = 1
					}
				})
			},
			// 获取模考大赛题目列表
			getContestMatterList() {
				uni.showLoading({
					title: this.$t('repeatUse.load') + "..."
				})
				this.$Request.getT('/app/topicContestMatter/getTopicContestMatterList', {
					subjectId: this.subjectId,
					page: this.page,
					limit: this.limit
				}).then(res => {
					uni.hideLoading()
					if (res.code == 500) {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					} else {
						let list = []
						res.data.records.map(item => {
							item.topicMatterVo.recordId = item.recordId
							list.push(item.topicMatterVo)
						})
						this.topicLists = [...this.topicLists, ...list]
						this.total = res.data.total
						this.getMatterById()

						console.log(this.topicLists, 'this.topicLists')
						if (res.data.records.length < this.limit) return
					}
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
						console.log(selectedOptions, 'selectedOptions')
						this.$set(this.userAnswers, questionIndex, selectedOptions);
						this.answerCard.push({
							index: questionIndex,
							recordId: this.topicLists[questionIndex].recordId,
							matterId: this.topicLists[questionIndex].matterId,
							answer: selectedOptions,
							rightAnswer: this.topicLists[questionIndex].answer,
						})
						let answer = this.topicLists[questionIndex].answer.split(",")
						this.allElementsExist = selectedOptions.every(element => answer.includes(element));
					} else {
						console.log("serialNumber", serialNumber)
						// 对于单选题，直接记录用户选择
						this.$set(this.userAnswers, questionIndex, serialNumber);
						this.answerCard.push({
							index: questionIndex,
							recordId: this.topicLists[questionIndex].recordId,
							matterId: this.topicLists[questionIndex].matterId,
							answer: serialNumber,
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
			/**
			 * @param {Object} questionIndex //题下标
			 * @param {Object} serialNumber //当前答案选项
			 */
			isSelected(questionIndex, serialNumber) {
				// 判断选项是否被选择
				const selectedOptions = this.userAnswers[questionIndex] || [];
				return Array.isArray(selectedOptions) ? selectedOptions.includes(serialNumber) : selectedOptions ===
					serialNumber;
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
	.containers {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100vh;
	}

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