<template>
	<view>
		<u-navbar :back-text="$t('bar.yhq')"></u-navbar>
		<view class="flex align-center justify-between  bg padding">
			<view v-for="(item, index) in list" :key="index" :class="tabIndex == index ? 'active' : ''"
				@click="change(index)">
				{{ item.name }}
			</view>
		</view>
		<u-checkbox-group @change="checkboxGroupChange" v-if="tabIndex == 1000">
			<u-checkbox @change="checkboxChange" shape="circle" v-model="item.checkbox" v-for="(item, index) in ticketList"
				:key="index" :name="item.name" active-color="#FD3C44">
				<view class="flex align-start justify-between" style="width: 100%;">
					<view>
						<view style="color: #000000;font-size: 30upx;">{{ item.couponName }}</view>
						<view style="color: #999999;font-size: 24upx;margin-top: 10upx;" v-if="item.expirationTime">
							{{$t('repeatUse.yxqz')}}{{ item.expirationTime }}
						</view>
						<view style="color: #999999;font-size: 24upx;margin-top: 10upx;" v-else>{{$t('yhj.yjyx')}}</view>
					</view>
					<view style="color: #FD3C44;font-size: 30upx;">{{$t('unit.yuan')}}<text
							style="font-size: 48upx;font-weight: bold;">{{ item.money
					  }}</text></view>
				</view>
				<view style="width: 100%;border-top:1rpx dashed #E6E6E6;margin: 30upx 0upx;"></view>
				<view class="flex align-center justify-between">
					<view v-if="item.minMoney">{{$t('unit.man')}}{{$t('unit.yuan')}}{{ item.minMoney }}{{$t('repeatUse.ky')}}
					</view>
					<view v-else>{{$t('yhj.wmk')}}</view>
					<!-- <view class="btn" @click="shiyong(item)" v-if="item.status == 0">立即使用</view> -->
				</view>
			</u-checkbox>
		</u-checkbox-group>
		<view class="listbox" v-for="(item, index) in ticketList" :key="index">
			<view class="flex align-start justify-between" style="width: 100%;">
				<view>
					<view style="color: #000000;font-size: 30upx;">{{ item.couponName }}</view>
					<view style="color: #999999;font-size: 24upx;margin-top: 10upx;" v-if="item.expirationTime">
						{{$t('repeatUse.yxqz')}}{{ item.expirationTime }}
					</view>
					<view style="color: #999999;font-size: 24upx;margin-top: 10upx;" v-else>{{$t('yhj.yjyx')}}</view>
				</view>
				<view style="color: #FD3C44;font-size: 30upx;">{{$t('unit.yuan')}}<text
						style="font-size: 48upx;font-weight: bold;">{{ item.money
			  }}</text></view>
			</view>
			<view style="width: 100%;border-top:1rpx dashed #E6E6E6;margin: 30upx 0upx;"></view>
			<view class="flex align-center justify-between">
				<view v-if="item.minMoney">{{$t('unit.man')}}{{$t('unit.yuan')}}{{ item.minMoney }}{{$t('repeatUse.ky')}}</view>
				<view v-else>{{$t('yhj.wmk')}}</view>
				<view class="btn" @click="noShiyong(item)" v-if="item.status == 0 && couponId == item.couponId">
					{{$t('repeatUse.qxsy')}}
				</view>
				<view class="btn" @click="shiyong(item)" v-if="item.status == 0 && couponId != item.couponId">
					{{$t('repeatUse.ljsy')}}
				</view>
			</view>
		</view>

		<empty v-if="ticketList.length == 0"></empty>
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	import {
		mapState
	} from 'vuex'
	export default {
		components: {
			empty,
		},
		data() {
			return {
				valueIndex: [],
				price: 0,
				list: [{
					id: 1,
					name: this.$t('yhj.ksy')
				}, {
					id: 2,
					name: this.$t('youhui.ysy')
				}, {
					id: 3,
					name: this.$t('youhui.ysx')
				}],
				tabIndex: 0,
				page: 1,
				limit: 10,
				ticketList: [],
				count: 0,
				price: '',
				couponId: '', //已选中的优惠券id
			}
		},
		onShow() {},
		onLoad(option) {
			this.price = option.price
			if (option.couponId) {
				this.couponId = option.couponId
			}
			this.getList()
			uni.setNavigationBarTitle({
				title: this.$t('yhj.name')
			})
		},
		methods: {
			checkboxChange(n) {
				let index = n.name
				if (this.price < this.ticketList[index].minMoney) {
					uni.showToast({
						title: this.$t('repeatUse.wdmjje'),
						icon: 'none'
					})
				} else if (this.price > 0) {
					uni.$emit('getDiscountId', {
						discountId: this.ticketList[index].couponId
					})
					uni.navigateBack()
					this.$emit('countMoney', this.ticketList[index].money)
					// this.$store.commit('goodModule/setDiscountAmount', this.ticketList[index].money)
				}
			},
			// 选中任一checkbox时，由checkbox-group触发
			checkboxGroupChange(e) {
				console.log(e, "xx");
			},
			noShiyong(item) {
				uni.$emit('getDiscountId', {
					discountId: '',
					price: ''
				})
				uni.navigateBack()
			},
			shiyong(item) {
				if (this.price < item.minMoney) {
					uni.showToast({
						title: this.$t('repeatUse.wdmjje'),
						icon: 'none'
					})
				} else {
					uni.$emit('getDiscountId', {
						discountId: item.couponId,
						price: item.money
					})
					uni.navigateBack()
				}
			},
			change(index) {
				this.tabIndex = index
				this.page = 1;
				this.getList()
			},
			// 获取我的优惠券列表
			getList() {
				this.$Request.getT('/app/couponUser/getMyCouponList', {
					page: this.page,
					limit: this.limit,
					status: this.tabIndex
				}).then(res => {
					if (res.code == 0) {
						this.count = res.data.pages
						let data = res.data.records
						for (var i = 0; i < data.length; i++) {
							if (data[i].expirationTime) {
								data[i].expirationTime = data[i].expirationTime.substring(0, 10)
							}
						}
						if (this.page == 1) {
							this.ticketList = data
						} else {
							this.ticketList = [...this.ticketList, data]
							this.page = this.page + 1
						}
					}
				})
			},
		},
		onReachBottom: function() {
			if (this.ticketList.length == this.count) {
				uni.showToast({
					title: this.$t('repeatUse.yjddl') + '~',
					icon: 'none'
				})
			} else {
				this.getList()
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getList()
		},
		watch: {
			tabIndex: {
				immediate: true,
				handler(newv) {
					this.getList()
				}
			}
		}
	}
</script>

<style>
	page {
		background: #F5F5F5;
	}

	.centre uni-image {
		height: 400rpx;
	}

	::v-deep .u-checkbox-group {
		width: 100%;
	}

	::v-deep .u-checkbox {
		background: #fff;
		width: calc(100% - 60rpx) !important;
		margin: 20rpx auto;
		border-radius: 20rpx;
	}

	::v-deep .u-checkbox__label {
		width: 100%;
		padding-bottom: 20rpx;
	}

	/deep/.u-checkbox__icon-wrap {
		margin: 0 15rpx;
	}

	.bg {
		background: #ffffff;
	}

	.active {
		color: #025EFD;
		font-size: 32upx;
		font-weight: bold;
	}

	.listbox {
		margin: 30upx;
		background: #FFFFFF;
		border-radius: 8upx;
		padding: 30upx;
	}

	.btn {
		color: #FD3C44;
		border: 1rpx solid #FD3C44;
		border-radius: 55upx;
		padding: 8upx 25upx;
	}
</style>