<template>
	<view class="cell flex">
		<image :src="content.coverImg" mode="scaleToFill" />
		<view class="content flex-c sb">
			<view class="" style="display: flex;justify-content: space-between;">
				<view class="name flex sb">{{ content.title }}</view>
				<view style="color:#999;font-size:28rpx;align-self: flex-end;font-weight: 500;">X1</view>
			</view>
			
			<view class="price flex">
				<!-- isGroup---是否是团购 0:否 1:是 -->
				<v-price-format v-if="content.isGroup == 1" :custom-style="{ color: '#333', fontWeight: 500 }"><text
						style="font-size: 36rpx;">{{ content.payMoney }}</text></v-price-format>
				<v-price-format v-else-if="content.isVip == 1" :custom-style="{ color: '#333', fontWeight: 500 }"><text
						style="font-size: 36rpx;">{{ content.payMoney }}</text></v-price-format>
				<v-price-format v-else :custom-style="{ color: '#333', fontWeight: 500 }"><text
						style="font-size: 36rpx;">{{ content.nowBuyMoney }}</text></v-price-format>
			</view>
		</view>
	</view>
</template>


<script>
	export default {
		props: {
			content: {
				type: Object,
				required: true
			}
		},
		mounted() {
			console.log(this.content, "nowBuyMoney")
		}
	}
</script>


<style lang="scss" scoped>
	.cell {
		// padding-bottom: 21rpx;
		position: relative;
		padding-bottom: 20rpx;
		image {
			width: 165rpx;
			height: 165rpx;
			border-radius: 24rpx;
		}

		// &::before {
		//   content: '';
		//   position: absolute;
		//   width: 100%;
		//   height: 2rpx;
		//   bottom: 0;
		//   background: #e6e6e6;
		// }

		image {
			width: 165rpx;
			height: 165rpx;
			border-radius: 24rpx;
			margin-right: 15rpx;
		}

		.content {
			flex: 1;

			.name {
				font-size: 32rpx;
				color: #333;
				font-weight: bold;
				display: -webkit-box;
				-webkit-box-orient: vertical;
				-webkit-line-clamp: 2;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: normal;
			}

			.price {
				align-self: flex-end;
				margin-right: -2rpx;
			}
		}
	}
</style>