<template>
	<view class="modify">
		<u-cell-group>
			<u-cell-item :title="$t('repeatUse.tx')" :arrow="false">
				<view slot="right-icon">
					<!-- 之前 -->
					<!-- <button class="avatar" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
						<image :src="model.avatar || defaultAvatar" mode="widthFix" />
					</button> -->
					<!-- 修改 -->
					<button class="avatar" @click="onChooseAvatar">
						<image :src="model.avatar || defaultAvatar" mode="widthFix" />
					</button>
				</view>
			</u-cell-item>
			<u-cell-item :title="$t('repeatUse.nc')" :arrow="false" hover-class="none">
				<view slot="right-icon">
					<u-input v-model="model.userName" placeholder=" " input-align="right" :clearable="false"></u-input>
				</view>
			</u-cell-item>
			<u-cell-item :title="$t('repeatUse.sex')" :arrow="false" hover-class="none">
				<view slot="right-icon">
					<u-radio-group v-model="gender" @change="radioChange">
						<u-radio v-for="(item, index) in list" :key="index" :name="item.name">
							{{ item.name }}
						</u-radio>
					</u-radio-group>
				</view>
			</u-cell-item>
		</u-cell-group>
		<v-button @click="modify">{{$t('repeatUse.qrxg')}}</v-button>
	</view>
</template>


<script>
	import config from '../../config.js';
	export default {
		data() {
			return {
				model: {
					avatar: '',
					userName: '',
					sex: ''
				},
				gender: '',
				defaultAvatar: 'https://dati.xianmxkj.com/file/uploadPath/2024/08/14/c962f9ac274f10916197c4b83337db67.png',
				list: [{
						name: this.$t('pagesText.nan'),
						value: 1
					},
					{
						name: this.$t('pagesText.nv'),
						value: 2
					}
				],
				user: uni.getStorageSync('userInfo')
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('bar.xgxx')
			})
		},
		methods: {
			radioChange(e) {
				console.log(e);
			},
			onChooseAvatar() {
				// 修改
				let that = this;
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album', 'camera'], //从相册选择
					success: function(res) {
						uni.showLoading({
							title: that.$t('loading.scz') + '...'
						});
						uni.uploadFile({
							url: config.BASE_URL + '/alioss/upload', //真实的接口地址
							// url: 'https://dati.xianmxkj.com/alioss/upload',
							filePath: res.tempFilePaths[0],
							name: 'file',
							success: (uploadFileRes) => {
								const avatarUrl = JSON.parse(uploadFileRes.data);
								that.model.avatar = avatarUrl.data
								uni.hideLoading();
							}
						});
					}
				});
				// 之前
				// const {
				// 	avatarUrl
				// } = e.detail
				// this.$set(this.model, 'avatar', avatarUrl)
			},
			modify() {
				this.$set(this.model, 'sex', this.list.find(item => item.name === this.gender)?.value)
				this.$Request.postJson('/app/user/updateUser', this.model).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('loading.xgcg'),
							icon: 'success'
						})
						uni.navigateBack()
					}
				})
			}
		},
		watch: {
			user: {
				handler(newv) {
					newv?.userName && this.$set(this.model, 'userName', newv.userName)
					newv?.avatar && this.$set(this.model, 'avatar', newv.avatar)
					newv?.sex && (this.gender = !newv.sex ? this.$t('pagesText.nan') : (newv.sex == 1 ? this.$t('pagesText.nan') :
						this.$t('pagesText.nv')))
				},
				immediate: true
			}
		}
	}
</script>


<style lang="scss" scoped>
	page {
		background: #fff;
	}

	.modify {
		/deep/.btn {
			width: 686rpx;
			height: 88rpx;
			margin: 0 auto;
			border-radius: 88rpx;
			margin-top: 200rpx;
		}
	}

	.avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		background-color: transparent;
		border: none;
		margin: 0;
		padding: 0;

		&::after {
			display: none;
		}

		image {
			width: 120rpx;
			height: 120rpx;
		}
	}
</style>