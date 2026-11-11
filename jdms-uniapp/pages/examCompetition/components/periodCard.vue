<template>
	<view class="period_card flex-c alic">
		<view class="title">
			<text>{{ detail.title }}</text>
		</view>
		<view class="project_list">
			<view v-for="(item, index) in detail.contestSubjectList" :key="item.subjectId">
				{{ item.subjectName || $t('index.zwssjg')}}：
				{{ item.examStartTime.slice(0,16) }} - {{ item.examEndTime.slice(0,16) | formatSuffix }}
			</view>
		</view>
		<view class="submit bt flex jc alic f"
			:class="{ grayscale: detail.status == 2 ? !detail.isSign : detail.status != 1 }" @click="toSignup">
			{{ btnText }}
		</view>
		<div class="desc">{{$t('pagesText.yiy')}}<text class="ft">{{ detail.signCount }}</text>{{$t('pagesText.rbm')}}</div>
	</view>
</template>


<script>
	export default {
		props: {
			detail: {
				type: Object,
				required: true
			}
		},
		computed: {
			btnText() {
				switch (this.detail.status) {
					case 0:
						return this.$t('pagesText.bmwks')
					case 1:
						return this.detail.isSign ? this.$t('pagesText.qwks') : this.$t('pagesText.mfbm')
					case 2:
						return this.detail.isSign ? this.$t('pagesText.qwks') : this.$t('pagesText.bmyjs')
					case 3:
						return this.$t('pagesText.ksyjs')
					case 4:
						return this.$t('pagesText.ksyqx')
				}
			},
		},
		methods: {
			changeData() {
				// 假设这是从后端获取的时间字符串
				const datetimeStr = "2024-08-01 17:46:47";

				// 使用 Date 构造函数将字符串转换为 Date 对象
				const datetimeObj = new Date(datetimeStr);

				// 获取年、月、日、小时和分钟
				const year = datetimeObj.getFullYear();
				const month = (datetimeObj.getMonth() + 1).toString().padStart(2, '0'); // 月份是从0开始的，所以要加1
				const day = datetimeObj.getDate().toString().padStart(2, '0');
				const hours = datetimeObj.getHours().toString().padStart(2, '0');
				const minutes = datetimeObj.getMinutes().toString().padStart(2, '0');

				// 组合成新的日期时间字符串
				const formattedDateTime = `${year}-${month}-${day} ${hours}:${minutes}`;

				console.log(formattedDateTime); // 输出: 2024-08-01 17:46
			},

			async toSignup() {
				if (this.detail.status != 1 && this.detail.status != 2) return uni.showToast({
					title: this.btnText,
					icon: 'none'
				})
				if (!this.detail.isSign) {
					if (this.detail.status != 2) {
						// const res = await signExamRace({
						// 	contestId: this.detail.contestId
						// })
						// if (res.code != 0) return
						this.$Request.postT(`/app/topicContestSign/userSign?contestId=${this.detail.contestId}`).then(res => {
							if (res.code != 0) return
						})
					} else {
						return uni.showToast({
							title: this.$t('pagesText.bmyjs'),
							icon: 'none',
							mask: true
						})
					}
				}
				this.navigate('/pages/examCompetition/examDetail', {
					contestId: this.detail.contestId
				})
			}
		},
		filters: {
			formatSuffix(val) {
				return val.split(' ')[1]
			}
		},
	}
</script>


<style lang="scss" scoped>
	.period_card {
		background: #fff;
		margin-bottom: 28rpx;
		padding: 80rpx 0;

		.title {
			position: relative;
			font-size: 32rpx;
			margin: 40rpx;
		}

		.project_list {
			&>view {
				margin-bottom: 28rpx;
			}
		}

		.submit {
			margin-top: 57rpx;
			margin-bottom: 14rpx;
			width: max-content;
			height: 85rpx;
			border-radius: 43rpx;
			font-size: 32rpx;
			padding: 0 25rpx;
		}
	}
</style>