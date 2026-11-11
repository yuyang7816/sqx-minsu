<template>
	<view>

	</view>
</template>

<script>
	import config from '../../common/config'
	export default {
		data() {
			return {
				province: '',
				city: '',
				county: '',
				cityaddress: '',
				detailaddress: '',
				latitude: '',
				longitude: '',
				userId: ''
			}
		},
		onLoad(option) {
			console.log('---------')

			if (option.userId) {
				this.userId = option.userId
			}
			console.log(option, "console.log(option.userId)")
			if (option.uuid) {
				this.uuid = option.uuid
			}
			if (option.type) {
				this.type = option.type
			}
			if (option.language) {
				this.language = option.language
			} else {
				this.language = uni.getStorageSync('language')
			}
			if (option.tokens) {
				this.tokens = option.tokens
			}
			let that = this
			uni.chooseLocation({
				success: function(res) {
					console.log(res, '5555555555555' + this.userId)
					console.log('位置名称：' + res.name);
					console.log('详细地址：' + res.address);
					console.log('纬度：' + res.latitude);
					console.log('经度：' + res.longitude);
					that.detailaddress = res.name
					that.latitude = res.latitude
					that.longitude = res.longitude
					that.shengcheng(res.longitude, res.latitude)
				},
				fail: function(res) {
					console.log('地图加载失败88888')
					let data = {
						content: '',
						userId: that.userId,
						uuid: that.uuid,
						type: 2
					}
					that.$Request.postJson('/app/message/saveMessageUser', data).then(red => {
						if (red.code == 0) {
							console.log('地址保存失败')
							uni.hideLoading()
							that.$queue.showToast(that.$t('toastText.bccg') + "!");
						} else {
							uni.hideLoading()
							that.$queue.showToast(red.msg);
						}
					});
				}
			});
		},
		onShow() {

		},
		methods: {
			shengcheng(longitude, latitude) {
				let that = this
				let data = {
					lat: latitude,
					lng: longitude
				}
				uni.request({
					url: config.APIHOST1 + '/app/Login/selectCity', //仅为示例，并非真实接口地址。
					data: data,
					header: {
						'Accept': 'application/json',
						'token': that.tokens,
						"Accept-Language": that.language,
						"content-type": 'application/x-www-form-urlencoded',
					},
					success: (res) => {
						let response = res.data
						if (response.code == 0) {
							console.log(response.data, '获取长沙')
							this.city = response.data.city
							let add = {
								city: this.city,
								lat: this.latitude,
								lng: this.longitude,
								userId: this.userId,
								country: response.data.country,
								district: response.data.district,
								province: response.data.province,
								address: response.data.address
							}
							let data = {
								content: add,
								userId: this.userId,
								uuid: this.uuid,
								type: this.type
							}
							console.log(data, '22222222', JSON.stringify(data))
							this.$Request.postJson('/app/message/saveMessageUser', data).then(red => {
								if (red.code == 0) {
									console.log('地址保存成功')
									uni.hideLoading()
									this.$queue.showToast(this.$t('Tips.bcok') + "!");
								} else {
									uni.hideLoading()
									this.$queue.showToast(red.msg);
									// uni.navigateBack()
								}
							});

						}
					}
				});
			},
		}
	}
</script>

<style lang="less">

</style>