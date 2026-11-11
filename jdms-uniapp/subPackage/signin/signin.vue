<template>
	<view class="signin container">
		<u-navbar :back-text="$t('bar.qdzx')"
			:background="{ background: 'linear-gradient(90deg, #0297FF 0%, #70C5FE 100%)' }"
			:back-text-style="{ color: '#fff' }" back-icon-color="#fff" :border-bottom="false"></u-navbar>
		<view class="back"></view>
		<view style="position: relative;z-index: 11;">
			<view class="user_row flex sb alic">
				<view class="user flex alic">
					<u-image width="70rpx" height="70rpx" shape="circle" :src="userInfo.avatar || defaultAvatar"></u-image>
					<text style="margin-left: 26rpx;">{{ userInfo.userName }}</text>
				</view>
				<image class="vip_logo" src="../../static/images/vip_logo.png" mode="scaleToFill" v-if="userInfo.isVip == 1" />
				<view class="bi flex se alic">
					<image src="../../static/images/bi.png" mode="scaleToFill" />
					<text>{{ userInfo.goldCoins }}{{$t('pagesText.jb')}}</text>
				</view>
			</view>
			<view class="date_card bf">
				<view class="date flex alic">
					<view style="font-size: 70rpx;">{{new Date().getDate()}}</view>
					<view>
						<view style="font-size: 28rpx;font-weight: 700;">{{ timeW }}</view>
						<view style="font-size: 26rpx;color:#999;">{{ timeM }}{{ timeY }}</view>
					</view>
				</view>
				<view class="line">
					<view class="l">
						<u-line-progress active-color="#67C0FF" inactive-color="#D6EEFF" :height="4" :percent="percent">
							<text></text>
						</u-line-progress>
					</view>
					<view class="grid flex alic">
						<view
							:class="['bi', { active_: item.dayCount < item.userSignCount || item.dayCount === item.userSignCount }]"
							v-for="(item, index) in signinList" :key="item.configId">
							<image :src="(index + 1) <= consecutiveDays ? bi : bi_in" mode="scaleToFill" />
							<text>{{ item.dayCount }}{{$t('unit.tian')}}</text>
						</view>
					</view>
					<view class="grid">
					</view>
				</view>
				<view class="signin_l flex jc alic bt f" :class="{ grayscale: bool }" @click="signin">
					{{ bool ? $t('pagesText.yqd') : $t('pagesText.ljqd') }}
				</view>
			</view>
		</view>
		<view class="exchange">
			<view style="padding: 24rpx;font-size: 32rpx;color:#333;">{{$t('pagesText.jfdh')}}</view>
			<view class="exchange_list" v-if="exchangeList.length">
				<view class="exchange_item bf" v-for="(item,index) in exchangeList" :key="index"
					@click="navigate(`/pages/vipCourseDetails/DetailsIntegral?goodId=${item.goodId}`)">
					<u-image :src="item.coverImg" width="100%" height="172rpx"></u-image>
					<view class="label">
						<view class="title">{{ item.title }}</view>
						<text style="font-size: 31rpx;">{{ item.goldCoinsMoney }}{{$t('pagesText.jb')}}</text>
					</view>
				</view>
			</view>
			<u-empty mode="data" :margin-top="45" :text="$t('pagesText.zwsp')" v-else></u-empty>
			<v-loading :status="status" :load-text="loadText" v-if="exchangeList.length"></v-loading>
		</view>
	</view>
</template>


<script>
	import bi from '@/static/images/bi.png'
	import bi_in from '@/static/images/bi_in.png'
	export default {
		data() {
			return {
				defaultAvatar: 'https://dati.xianmxkj.com/file/uploadPath/2024/08/14/af0fcbfd52faec81e4b8a6d0af51906d.png',
				bool: false,
				consecutiveDays: 0,
				bi,
				bi_in,
				exchangeList: [],
				signinList: [],
				exchangePageOption: {
					page: 1,
					limit: 6
				},
				signinPageOption: {
					page: 1,
					limit: 10
				},
				userInfo: uni.getStorageSync('userInfo'),
				timeY: '',
				timeM: '',
				timeD: '',
				timeW: '',
				loadText: '',
				status: ''
			}
		},
		computed: {
			percent() {
				return this.signinList[0]?.userSignCount / this.signinList[this.signinList.length]?.dayCount || -'0'
			},
		},
		onReachBottom() {
			this.exchangePageOption.page++
			this.getExchangeList()
		},
		methods: {
			// 获取积分兑换课程
			getExchangeList() {
				this.$Request.getT('/app/topicGoods/getGoldCoinsGoodsList', this.exchangePageOption).then(res => {
					if (res.data.records && res.code == 0) {
						this.exchangeList = [...this.exchangeList, ...res.data.records]
						if (res.data.records.length < this.exchangePageOption.limit) {
							this.status = 'normal'
							this.loadText = this.$t('load.nomore') + '~'
							return
						}
					}
				})
			},
			// 获取签到列表
			getSigninList() {
				this.$Request.getT('/app/signConfig/getSignConfigList', {
					...this.signinPageOption,
					loginUserId: uni.getStorageSync('userId')
				}).then(res => {
					this.signinList = res.data.records
				})
			},
			// 点击签到
			signin() {
				this.$Request.postT('/app/signRecord/userSign').then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('pagesText.qdcg'),
							icon: 'success',
							mask: true
						})
						this.getSigninList()
						this.isSign()
						this.getUserInfoById()
						// 更新用户信息
						// this.$store.dispatch('userModule/getUserInfoById')
					}
				})
			},
			// 获取个人信息
			getUserInfoById() {
				this.$Request.getT('/app/user/selectUserById', {
					userId: uni.getStorageSync('userId')
				}).then(res => {
					this.userInfo = res.data
					uni.setStorageSync('userInfo', res.data)
				})
			},
			isSign() {
				this.$Request.getT('/app/signRecord/isSignToday', {
					userId: uni.getStorageSync('userId')
				}).then(res => {
					if (res.code == 0) {
						this.bool = res.data.isSign
						this.consecutiveDays = res.data.consecutiveDays
					}
				})
			}
		},
		onLoad(options) {
			this.isSign()
			this.getExchangeList()
			this.getSigninList()
			let zh = ['一' + this.$t('unit.yue'), '二' + this.$t('unit.yue'), '三' + this.$t('unit.yue'), '四' + this.$t(
					'unit.yue'), '五' + this.$t('unit.yue'), '六' + this.$t('unit.yue'), '七' + this.$t('unit.yue'), '八' + this.$t(
					'unit.yue'), '九' + this.$t('unit.yue'), '十' + this.$t('unit.yue'), '十一' + this.$t('unit.yue'), '十二' + this
				.$t('unit.yue')
			]
			const d = new Date()
			this.timeY = d.getFullYear()
			this.timeM = zh.find((item, index) => index === d.getMonth())
			this.timeD = d.getDate()
			this.timeW = new Intl.DateTimeFormat('zh-CN', {
				weekday: 'short'
			}).format(d)
		}
	}
</script>


<style lang="scss" scoped>
	.grayscale {
		background: gray;
	}

	.user {
		position: relative;
	}

	.vip_logo {
		position: absolute;
		width: 40rpx;
		height: 40rpx;
		top: 30rpx;
		left: 8%;
		transform: translate(0, -60%);
	}

	.signin {
		background: #f5f5f5 !important;
		position: relative;

		.back {
			position: absolute;
			width: 100%;
			height: 456rpx;
			top: 0;
			background: linear-gradient(90deg, #0297FF 0%, #70C5FE 100%);
			z-index: 0;
		}

		.user_row {
			padding: 32rpx 0;
			padding-left: 30rpx;
			color: #fff;

			.bi {
				padding: 0 18rpx;
				height: 60rpx;
				background: #BEE4FF;
				border-radius: 30rpx 0rpx 0rpx 30rpx;
				color: #000;

				image {
					width: 37rpx;
					height: 37rpx;
					margin-right: 10rpx;
				}
			}
		}

		.date_card {
			width: 686rpx;
			height: 412rpx;
			border-radius: 24rpx;
			box-sizing: border-box;
			padding: 45rpx 37rpx;
			margin: 0 auto;

			.date {
				color: #333;
			}

			.line {
				padding: 50rpx 0;
				position: relative;

				.l {
					position: absolute;
					top: 0;
					left: 0;
					width: 100%;
					top: 25%;
				}

				.grid {
					position: relative;
					z-index: 1;
					display: grid;
					grid-template-columns: 42rpx 42rpx 42rpx 42rpx 42rpx 42rpx 42rpx;
					grid-column-gap: 54rpx;

					text {
						white-space: nowrap;
						font-size: 24rpx;
						color: #999;
					}

					image {
						width: 34rpx;
						height: 34rpx;
					}

					.active_ {
						text {
							color: $theme-color !important;
						}

						image {
							width: 44rpx;
							height: 44rpx;
						}

					}
				}
			}

			.signin_l {
				height: 76rpx;
				border-radius: 38rpx;
			}
		}

		.exchange {
			padding: 0 33rpx;

			&_list {
				display: grid;
				grid-template-columns: 328rpx 32rpx;
				grid-column-gap: 27rpx;
			}

			&_item {
				width: 328rpx;
				margin-bottom: 26rpx;
				border-radius: 16rpx;
				overflow: hidden;

				.label {
					padding: 24rpx 16rpx;

					.title {
						font-size: 32rpx;
						color: #333;
						font-weight: 700;
					}

					text {
						color: #D53E45;
						font-size: 26rpx;
						font-weight: 700;
					}
				}
			}
		}
	}
</style>