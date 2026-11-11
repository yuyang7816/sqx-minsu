<template>
	<view class="main">
		<u-navbar :back-text="'VIP'+$t('pagesText.kc')"></u-navbar>
		<view class="content">
			<view class="content_nav" v-if="navList.length > 0">
				<view :class="currentIndex == index ? 'content_nav_item_check' : 'content_nav_item'"
					v-for="(item, index) in navList" :key="index" @click="checkNav(index)">
					<text>{{ item }}</text>
				</view>

			</view>
			<scroll-view :scroll-y="true" :scroll-into-view="scrollId" class="content_list" v-if="list[currentIndex]">

				<view class="content_list_item" v-for="(children, cIndex) in list[currentIndex].topicGoodsList"
					:key="children.goodId" @click="goDetail(children)">
					<view class="content_list_item_left">
						<u-image :src="children.coverImg" width="100%" height="100%" mode="aspectFill"></u-image>
						<!-- <image src="/static/logo.png" mode="aspectFill"></image> -->
						<!-- mode="aspectFit" -->
					</view>
					<view class="content_list_item_right">
						<view class="content_list_item_right_top">
							<text>{{ children.title }}</text>
						</view>
						<view class="content_list_item_right_bot">
							<text class="text_free" v-if="children.nowBuyMoney == 0">{{$t('pageText.mf')}}</text>
							<v-price-format :custom-style="{ fontSize: '38rpx', color: '#D54B59'}" v-else><text
									style="padding-left: 10rpx;">{{ children.nowBuyMoney
							}}</text></v-price-format>
							<text class="text_number">{{ children.buyCount }}{{$t('pageText.rygm')}}</text>
						</view>
					</view>
				</view>
			</scroll-view>
			<u-empty mode="data" margin-top="250" :text="$t('pagesText.zwsp')" v-else></u-empty>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				scrollId: '',
				list: [],
				currentIndex: 0,
				navList: [] //左侧导航栏
			};
		},
		onLoad() {
			this.getGoodsTypeList()
		},
		methods: {
			getGoodsTypeList() {
				// 增加courseMoneyType:'0' --- courseMoneyType 是否支持拼团 0:否 1:是
				this.$Request.getT('/app/topicGoodsType/getTopicGoodsTypeList', {
					page: 1,
					limit: 10,
					type: '1',
					childTopicTypeId: uni.getStorageSync('typeId'),
					courseMoneyType: '0'
				}).then(res => {
					this.list = res.data.records
					this.navList = this.list.map(item => item.typeName)
					console.log(this.list, "this.list");
				})
			},
			checkNav(i) {
				this.currentIndex = i
			},
			goDetail({
				nowBuyMoney,
				goodId,
				typeId
			}) {
				this.navigate('/pages/vipCourseDetails/vipCourseDetails', {
					goodId
				})
			},
		},

	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #f5f7ff;
	}

	.content {
		display: flex;
		justify-content: space-between;
	}

	.content_nav {
		margin-top: 30rpx;
		// width: 208rpx;
		height: calc(100vh - 60px);
		// #ifndef MP-WEIXIN
		height: calc(100vh - 100px);
		// #endif
		background-color: #fff;
		border-radius: 18rpx;

		.content_nav_item_check {
			width: 209rpx;
			padding: 24rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			background: linear-gradient(90deg, #D4EDFF 0%, #FFFFFF 100%);
			border-radius: 14rpx;
			margin: 40rpx 1rpx 40rpx 2rpx;
			text-align: center;

			text {
				font-weight: bold;
				font-size: 30rpx;
				color: #333333;
			}
		}

		.content_nav_item {
			width: 209rpx;
			padding: 24rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			border-radius: 14rpx;
			margin: 40rpx 1rpx 40rpx 2rpx;
			text-align: center;

			text {
				font-weight: 500;
				font-size: 30rpx;
				color: #999999;
			}
		}

	}

	.ellipese_2 {
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: normal;
	}

	.ellipese_1 {
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: normal;
	}

	.content_list {
		width: 100%;
		margin: 0rpx auto;

		.item {
			&:first-child {
				.content_list_title {
					margin-top: 0;
				}
			}
		}

		.content_list_title {
			width: 504rpx;
			height: 29rpx;
			display: flex;
			justify-content: flex-start;
			margin-top: 22rpx;

			text {
				margin-left: 18rpx;
				font-weight: bold;
				font-size: 30rpx;
				color: #333333;
			}

		}


		.content_list_item {
			width: calc(100% - 40rpx);
			height: 218rpx;
			background: #FFFFFF;
			border-radius: 15rpx;
			display: flex;
			align-items: center;
			margin: 30rpx auto 30rpx auto;

			.content_list_item_left {
				width: 205rpx;
				height: 167rpx;
				border-radius: 16rpx;
				margin: 9rpx 0rpx 9rpx 22rpx;

				/deep/ image {
					width: 100%;
					height: 100%;
					border-radius: 20rpx !important;
				}
			}

			.content_list_item_right {
				width: 222rpx;
				// height: 167rpx;
				margin: 19rpx 39rpx 19rpx 16rpx;
				display: flex;
				flex-direction: column;
				justify-content: space-between;

				.content_list_item_right_top {
					width: 222rpx;
					// margin-top: 7rpx;
					padding-bottom: 20rpx;
					text-overflow: ellipsis;
					white-space: nowrap;
					overflow: hidden;
					padding-top: 6rpx;

					text {
						font-weight: bold;
						font-size: 32rpx;
						color: #333333;
					}
				}

				.content_list_item_right_bot {
					width: 222rpx;
					display: flex;
					flex-direction: column;
					justify-content: flex-end;
					// padding-top: 16rpx;

					.text_free {
						font-weight: bold;
						font-size: 30rpx;
						color: #D64046;
						padding-top: 6rpx;
						padding-bottom: 10rpx;
					}

					.text_number {
						font-size: 24rpx;
						color: #999999;
						@extend .ellipese_1;
						// margin-bottom: 5rpx;
					}
				}
			}
		}
	}

	.content_vip {
		margin-top: 34rpx;
	}

	/deep/.price {
		view {
			margin-right: -4rpx !important;
			font-weight: bold !important;
			padding-bottom: 10rpx !important;
			margin-left: -12rpx;
			@extend .ellipese_1;
		}
	}

	/deep/.u-empty {
		width: 504rpx !important;
	}
</style>