<template>
	<view class="main">
		<u-navbar :back-text="$t('pagesText.hyzx')" :background="background" :back-text-style="color"
			back-icon-color="#fff"></u-navbar>
		<view class="member">
			<view class="member_undertone">

			</view>
			<view class="member_card">
				<text class="member_card_openVip">{{$t('pagesText.openVip')}}</text>
				<text class="member_card_title">{{$t('pagesText.zsqy')}}</text>
				<view class="member_card_btn">
					<text>{{$t('pagesText.ljjr')}}</text>
				</view>
			</view>
			<view class="member_type">
				<view :class="currentIndex==index?'member_type_item_check':'member_type_item'"
					v-for="(item,index) in memberList" :key="index" @click="checkMember(index)">
					<text class="member_type_item_title">{{item.title}}</text>
					<text class="member_type_item_price">{{item.price}}</text>
				</view>
			</view>
			<view class="openImmediately">
				<text>{{$t('pagesText.ljkt')}}</text>
			</view>
			<view class="memberaGreement">
				<u-checkbox-group>
					<u-checkbox v-model="checked" shape="circle"
						class="memberaGreement_title">{{$t('order.yixi')}}《{{$t('pageText.hyfwxy')}}》及《{{$t('pageText.wfsygz')}}》</u-checkbox>
				</u-checkbox-group>
			</view>
		</view>
		<view class="memberPrivileges">
			<view class="memberPrivileges_title">
				<text>{{$t('pageText.yhtq')}}</text>
			</view>
			<view class="memberPrivileges_list">
				<view class="memberPrivileges_list_item">
					<view class="memberPrivileges_list_item_icon">

					</view>
					<view class="memberPrivileges_list_item_title">
						{{$t('pageText.kczk')}}
					</view>
				</view>
				<view class="memberPrivileges_list_item">
					<view class="memberPrivileges_list_item_icon">

					</view>
					<view class="memberPrivileges_list_item_title">
						{{$t('pageText.zlzk')}}
					</view>
				</view>
				<view class="memberPrivileges_list_item">
					<view class="memberPrivileges_list_item_icon">

					</view>
					<view class="memberPrivileges_list_item_title">
						{{$t('pageText.kfmpd')}}
					</view>
				</view>
				<view class="memberPrivileges_list_item">
					<view class="memberPrivileges_list_item_icon">

					</view>
					<view class="memberPrivileges_list_item_title">
						{{$t('pageText.jqqd')}}
					</view>
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				background: {
					backgroundImage: 'linear-gradient(180deg, #3BAEFF 0%, #121517 0%, #0E161B 100%)'
				},
				color: {
					color: '#fff'
				},
				checked: false,
				memberList: [{
					title: "年度会员",
					price: "￥66.6"
				}, {
					title: "季度会员",
					price: "￥39.9"
				}, {
					title: "月度会员",
					price: "￥19.9"
				}],
				currentIndex: 0
			};
		},
		methods: {
			checkMember(i) {
				this.currentIndex = i
			}
		}
	}
</script>

<style lang="scss" scoped>
	::v-deep .u-border-bottom:after {
		border: 0 !important;
	}

	.main {
		background-color: #F5F7FF;
		height: 1500rpx;
	}

	.member {
		width: 750rpx;
		position: relative;
		background-color: #fff;
		padding-bottom: 32rpx;
	}

	.member_undertone {
		width: 750rpx;
		height: 332rpx;
		background: linear-gradient(180deg, #3BAEFF 0%, #121517 0%, #0E161B 100%);
		border-radius: 0 0 375rpx 375rpx/0 0 70rpx 70rpx;
	}

	.member_card {
		width: 686rpx;
		height: 318rpx;
		background: linear-gradient(0deg, #FEF2DC 0%, #FEE4BF 100%);
		box-shadow: 0rpx 1rpx 32rpx 0rpx rgba(0, 0, 0, 0.07);
		border-radius: 24rpx;
		position: absolute;
		display: flex;
		flex-direction: column;
		top: 41rpx;
		left: 32rpx;
		right: 32rpx;

		.member_card_openVip {
			margin: 53rpx 0 0 59rpx;
			font-weight: bold;
			font-size: 36rpx;
			color: #836432;

		}

		.member_card_title {
			margin: 16rpx 0 0 59rpx;
			font-weight: 500;
			font-size: 24rpx;
			color: #AC8B54;
		}

		.member_card_btn {
			width: 198rpx;
			height: auto;
			padding: 22rpx 51rpx 25rpx 52rpx;
			background: linear-gradient(0deg, #292723 0%, #433E36 100%);
			border-radius: 36rpx;
			display: flex;
			margin: 63rpx 0 0 59rpx;

			text {

				font-weight: 500;
				font-size: 24rpx;
				color: #FFF8EB;
			}

		}

	}

	.member_type {
		height: 227rpx;
		margin: 74rpx 32rpx 0 32rpx;
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.member_type_item_check {
			width: 213rpx;
			height: 227rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			background: #EEF8FF;
			border-radius: 30rpx;
			border: 2px solid #139DFF;

			.member_type_item_title {
				height: 27rpx;
				font-weight: bold;
				font-size: 28rpx;
				color: #333333;
				margin-top: 63rpx;
			}

			.member_type_item_price {
				height: 32rpx;
				font-weight: bold;
				font-size: 30rpx;
				color: #333333;
				margin-top: 26rpx;
			}
		}

		.member_type_item {
			width: 213rpx;
			height: 227rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			background: #FFFFFF;
			border-radius: 30rpx;
			border: 1px solid #E6E6E6;

			.member_type_item_title {
				height: 27rpx;
				font-weight: bold;
				font-size: 28rpx;
				color: #333333;
				margin-top: 63rpx;
			}

			.member_type_item_price {
				height: 32rpx;
				font-weight: bold;
				font-size: 30rpx;
				color: #333333;
				margin-top: 26rpx;
			}
		}

	}

	.openImmediately {
		margin: 47rpx 76rpx 0 77rpx;
		padding: 29rpx 243rpx 28rpx 243rpx;
		background: #139DFF;
		border-radius: 42rpx;

		text {
			width: 111rpx;
			height: 26rpx;
			font-weight: bold;
			font-size: 28rpx;
			color: #FFFFFF;
		}
	}

	.memberaGreement {
		display: flex;
		justify-content: flex-start;
		align-items: center;
		margin: 18rpx 87rpx 0 87rpx;
		height: 28rpx;

		.memberaGreement_title {
			white-space: nowrap;
			font-weight: 500;
			font-size: 24rpx;
			color: #999999;
		}
	}

	::v-deep .u-checkbox__label {
		font-size: 26rpx !important;
	}

	.memberPrivileges {
		width: 100%;
		height: auto;
		background: #FDFDFD;
		margin-top: 24rpx;

		.memberPrivileges_title {
			padding-top: 44rpx;
			display: flex;
			justify-content: center;

			text {
				font-weight: bold;
				font-size: 30rpx;
				color: #0096FF;
			}

		}

		.memberPrivileges_list {
			display: flex;
			justify-content: space-between;
			align-items: center;
			flex-wrap: wrap;
			padding: 41rpx 47rpx 20rpx 47rpx;

			.memberPrivileges_list_item {
				width: 25%;
				display: flex;
				flex-direction: column;
				align-items: center;
				margin-bottom: 20rpx;

				.memberPrivileges_list_item_icon {
					width: 82rpx;
					height: 82rpx;
					background: #EAF0FE;
					border-radius: 50%;
				}

				.memberPrivileges_list_item_title {
					font-weight: 500;
					font-size: 26rpx;
					color: #252830;
					margin-top: 12rpx;
				}
			}

		}
	}
</style>