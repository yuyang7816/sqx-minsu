<template>
	<view class="container">
		<u-navbar :back-text="$t('pagesText.mkds')" :background="{ background: 'transparent' }" :border-bottom="false"
			:placeholder="false"></u-navbar>
		<view class="title_text">{{$t('pagesText.mkds')}}</view>
		<image src="/static/images/my/mkds.png" mode="widthFix" style="position: relative;"></image>
		<view class="title_exam"
			style="top: 340rpx;font-size: 36rpx;color:#fff;font-weight: bold;left: 0;right: 0;margin: auto;">{{
      detail.title }}</view>
		<view style="top: 795rpx;left: 155rpx;">{{$t('pagesText.ksjs')}}：{{ detail.signEndTime | formatPrefix }}</view>
		<view style="top: 880rpx;left: 155rpx;">
			{{$t('pagesText.ksfs')}}：{{ detail.type == 1 ? $t('pagesText.xs') : $t('pagesText.xs') }}{{$t('pagesText.ks')}}
		</view>
		<view class="huodong">
			<view class="flex alic" style="width: 100%;border-bottom: #30a9ff solid 4rpx;padding-bottom: 20rpx;">
				<image src="/static/images/my/shoucang (2).png" mode=""
					style="width: 30rpx;height: 30rpx;display: block;position: static;">
				</image>
				<view style="font-weight: bold;padding-left: 10rpx;">活动内容</view>
			</view>
			<scroll-view :scroll-y="true" class="scroll_f padding-top-sm">
				<view class="exam_list">
					<view class="exam_item" v-for="(item, index) in detail.contestSubjectList" :key="index">
						<view class="sub_title flex jc alic" :style="{background:item.status == 2 ? '#0096FF' : '#CCCCCC'}">
							{{ item.subjectName || $t('index.zwssjg')}}
						</view>
						<view>{{ item.examStartTime.split(' ')[0] }} {{ item.examStartTime.slice(0,16) | formatSuffix }}
							-{{ item.examEndTime.slice(0,16) | formatSuffix }}</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<view class="wenxin">
			<view class="flex alic" style="width: 100%;border-bottom: #30a9ff solid 4rpx;padding-bottom: 20rpx;">
				<image src="/static/images/my/shoucang (2).png" mode=""
					style="width: 30rpx;height: 30rpx;display: block;position: static;">
				</image>
				<view style="font-weight: bold;padding-left: 10rpx;">温馨提示</view>
			</view>
			<scroll-view :scroll-y="true" class="scroll_t padding-top-sm">
				<u-parse :html="detail.remark"></u-parse>
			</scroll-view>
		</view>

		<view class="btn flex jc alic bf" @click="toExam" v-if="detail.type == 1">{{$t('pagesText.ksks')}}</view>
	</view>
</template>


<script>
	export default {
		data() {
			return {
				detail: {},
				contestId: null,
				successCode: ''
			}
		},
		onLoad({
			contestId
		}) {
			if (contestId) {
				this.contestId = contestId
				this.getContestDetail()
				this.getExamStatus()
			}
		},
		methods: {
			// 获取考试信息
			getContestDetail() {
				this.$Request.getT('/app/topicContest/getAppTopicContestInfo', {
					contestId: this.contestId,
					loginUserId: uni.getStorageSync('userId')
				}).then(res => {
					this.detail = res.data
				})
			},
			getExamStatus() {
				this.$Request.getT('/app/topicContestAnswer/getTopicContestAnswerByContestId', {
					contestId: this.contestId
				}).then(res => {
					console.log(res);
				})
			},
			// 开始考试
			toExam() {
				// item.status 0未开始 1进行中 2报名已结束 3已结束 4已取消
				let status = ''
				this.detail.contestSubjectList.find(item => {
					if (item.status == 2) {
						status = item.status
					}
				})
				if (status != '') {
					let subjectDuration = this.detail.contestSubjectList.find(item => item.status == 2).subjectDuration
					const subjectId = this.detail.contestSubjectList.find(item => item.status == 2).subjectId
					this.$Request.postJson('/app/topicContestSubjectRecord/save', {
						contestId: this.contestId,
						userId: uni.getStorageSync('userId'),
						subjectId: subjectId,
						score: 0
					}).then(res => {
						if (res.code == 500) {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
							setTimeout(() => {
								uni.navigateBack()
							}, 2000)
						} else {
							this.getContestDetail()
							if (!subjectId) {
								let v = this.detail.contestSubjectList.find(item => item.status == 1).subjectId
								return uni.showToast({
									title: $t('pagesText.ks') + v ? $t('pagesText.wks') : $t('pageText.yjs'),
									icon: 'none',
									mask: true
								})
							} else {
								this.navigate('/subPackage/chapter/chapterDetailMkds', {
									subjectId,
									contestId: this.contestId,
									subjectDuration: subjectDuration,
									contestStatus: 1
								})
							}
						}
					})
				} else {
					uni.showToast({
						title: this.$t('pagesText.kswks'),
						icon: 'none'
					})
				}
			}
		},
		filters: {
			formatSuffix(val) {
				return val?.split(' ')[1]
			},
			formatPrefix(val) {
				return val?.split(' ')[0]
			}
		}
	}
</script>


<style lang="scss" scoped>
	.wenxin {
		position: absolute;
		top: 1420rpx !important;
		left: 110rpx;
	}

	.huodong {
		position: absolute;
		top: 1030rpx !important;
		left: 100rpx;
		z-index: 67;
	}

	.title_text {
		position: absolute;
		top: 150rpx !important;
		z-index: 66;
		left: 0;
		margin: auto;
		right: 0;
		width: calc(100% - 60rpx);
		text-align: center;
		color: #30a9ff;
		font-size: 80rpx;
		font-weight: bold;
	}

	.title_exam {
		width: max-content;
		margin: auto;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: normal;
	}

	.container {
		position: relative;

		image {
			width: 100%;
			position: absolute;
			top: 0;
		}

		&>view {
			position: absolute;
			top: 0;
		}

		.scroll_f {

			height: 180rpx;
			width: 550rpx;

			.exam_list {

				height: fit-content;
				position: relative;
				padding: 32rpx 0;
				padding-left: 250rpx;

				&::before {
					content: '';
					position: absolute;
					left: 10rpx;
					top: 0;
					min-height: 180rpx;
					width: 4rpx;
					background: repeating-linear-gradient(to bottom, $theme-color, $theme-color 10rpx, transparent 20rpx, transparent 2rpx);
				}

				.exam_item {
					display: flex;
					flex-direction: column;
					position: relative;
					font-size: 24rpx;
					color: #000;
					margin-bottom: 30rpx;

					&::before {
						content: '';
						position: absolute;
						left: -250rpx;
						top: 50%;
						transform: translateY(-50%);
						width: 20rpx;
						height: 20rpx;
						border-radius: 50%;
						background: $theme-color;
					}

					.sub_title {
						position: absolute;
						color: #FFF;
						font-weight: bold;
						top: 50%;
						transform: translateY(-50%);
						text-indent: 15rpx;
						width: 200rpx;
						height: 50rpx;
						background: $theme-color;
						left: -230rpx;
						clip-path: polygon(0 50%, 30rpx 0, 100% 0%, 100% 100%, 30rpx 100%);
					}
				}
			}
		}

		.scroll_t {
			width: 560rpx;
			height: 280rpx;
		}

		.tip {
			position: absolute;
			width: 560rpx;
			font-size: 24rpx;

			.sort {
				width: 30rpx;
				height: 30rpx;
				background: $theme-color;
				box-shadow: red -1rpx 1rpx;
				margin-right: 16rpx;
			}

			.text {
				flex: 1;
			}
		}

		.btn {
			width: 240rpx;
			height: 80rpx;
			border-radius: 60rpx;
			color: $theme-color;
			position: absolute;
			top: 1820rpx;
			left: 50%;
			transform: translate(-50%);
		}
	}
</style>