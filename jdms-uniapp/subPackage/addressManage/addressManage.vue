<template>
	<view class="container">
		<u-navbar :back-text="$t('bar.dzgl')"></u-navbar>
		<view class="address_list" v-if="list.length">
			<view class="address_item bf" v-for="(item, index) in list" :key="index">
				<view class="" @click="choose && chooseThis(item)">
					<view class="flex alib">
						<text style="font-size: 30rpx;margin-right: 30rpx;">{{ item.userName }}</text>
						<text style="font-size: 26rpx;color:#666">{{ item.userPhone }}</text>
					</view>
					<view class="address_d flex alic sb">
						<view>
							{{ item.province?item.province:'' }}{{ item.city ?item.city :''}}{{ item.county?item.county:'' }}{{ item.address?item.address:'' }}
						</view>
						<view @click.stop="navigate('/subPackage/addressManage/add', { addressId: item.addressId })">
							<u-icon name="edit-pen" size="30"></u-icon>
						</view>
					</view>
				</view>
				<view class="operate_bar flex sb alic">
					<u-radio-group :value="item.isDefault ? `${index}` : ''">
						<u-radio label-size="26rpx" active-color="#0096FF" :name="`${index}`"
							:disabled="!item.isDefault">{{$t('add.mrdd')}}</u-radio>
					</u-radio-group>
					<view style="color:#999;font-size: 26rpx;" @click="showDialog(item.addressId)">{{$t('add.sc')}}</view>
				</view>
				<!-- <view class="operate_bar flex sb alic">
					<u-radio-group :value="item.isDefault == 1">
						<u-radio label-size="26rpx" active-color="#0096FF" :name="index" :disabled="!item.isDefault">当前默认地址</u-radio>
					</u-radio-group>
					<view style="color:#999;font-size: 26rpx;" @click="showDialog(item.addressId)">删除</view>
				</view> -->
			</view>
		</view>
		<u-empty v-else mode="data" :margin-top="160" :text="$t('repeatUse.noData')"></u-empty>
		<view class="add_btn flex alic jc" hover-class="active" @click="navigate('/subPackage/addressManage/add')">
			{{$t('btn.tjdz')}}
		</view>
		<view class="placeholder" style="width: 100%;height: calc(50rpx + 80rpx + 50rpx);"></view>
		<u-modal v-model="show" :content="$t('pagesText.sureDel')+'？'" :show-cancel-button="true" @confirm="delAddress"
			:title="$t('repeatUse.fxlj')" :confirm-text="$t('btnText.qd')" :cancel-text="$t('repeatUse.qx')"></u-modal>
	</view>
</template>


<script>
	export default {
		data() {
			return {
				choose: false,
				value: '',
				page: 1,
				limit: 10,
				list: [],
				show: false,
				addressId: null
			}
		},
		onLoad(option) {
			if (option.index) {
				this.index = option.index
			}
			if (option.choose === 'choose') {
				this.choose = true
			}
		},
		onShow() {
			this.page = 1
			this.getAddlist()
		},
		methods: {
			showDialog(addressId) {
				this.addressId = addressId
				this.show = true
			},
			// 删除
			delAddress() {
				this.$Request.postT('/app/address/deleteAddress', {
					addressId: this.addressId
				}).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('toast.sccg'),
							icon: 'success'
						})
						this.page = 1
						setTimeout(() => {
							this.getAddlist()
						}, 1500)
					}
				})
			},
			radioChange(index, e) {
				return
			},
			chooseThis(address) {
				uni.$emit('getUserAddress', address)
				uni.navigateBack()
			},
			openMap() { //打开地图选择位置
				let that = this
				uni.chooseLocation({
					success: function(res) {
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
						// let data = {
						// 	address: res.name, //地址
						// 	addressDetail: res.address,
						// 	addressLongitude: res.longitude, //地址经度
						// 	addressLatitude: res.latitude, //地址维度
						// 	addressId: '',
						// 	addressDefault: '',
						// }
						// that.$Request.postJson('/app/indent/addUserAddress', data).then(res => {
						// 	if (res.code === 0) {
						// 		that.getAddlist()
						// 	}
						// });
					}
				});
			},
			getAddlist(page) {
				this.$Request.getT('/app/address/getAddressList', {
					page: this.page,
					limit: this.limit
				}).then(res => {
					if (this.page == 1) {
						this.list = res.data.records
					} else {
						this.list = [...this.list, ...res.data.records]
					}
					this.page++
				})
			}
		},
		watch: {
			show(newv) {
				if (!newv) {
					this.addressId = null
				}
			}
		}
	}
</script>


<style lang="scss" scoped>
	.container {
		background: #F5F5F5;

		.address {
			&_list {
				padding: 12rpx 20rpx;
			}

			&_item {
				padding: 0 28rpx;
				padding-top: 24rpx;
				border-radius: 16rpx;
				margin-bottom: 24rpx;

				&>view {
					&:first-child {
						margin-bottom: 14rpx;
					}
				}

				.address_d {
					font-size: 26rpx;
					color: #333;
					padding-bottom: 22rpx;
					position: relative;

					&::after {
						content: '';
						position: absolute;
						width: 100%;
						height: 2rpx;
						background: #eee;
						bottom: 0;
					}

					view {
						&:first-child {
							@include mul-ellipsis(2);
						}
					}
				}
			}
		}

		.operate_bar {
			padding-top: 26rpx;
			padding-bottom: 16rpx;
		}

		.add_btn {
			position: fixed;
			width: 90vw;
			height: 80rpx;
			background: $theme-color;
			bottom: 50rpx;
			left: 50%;
			transform: translate(-50%);
			border-radius: 40rpx;
			color: #fff;
		}
	}

	/deep/.u-radio__icon-wrap--disabled {
		background: transparent !important;
	}
</style>