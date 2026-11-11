<template>
	<view>
		<image style="width: 100%;position: fixed;top: 0;" src="../../static/images/public/downApp.png" mode="widthFix">
		</image>
		<view style="text-align: center;position: relative;z-index: 100;margin-right: 5rpx;margin-top: 25%;">
			<image style="width: 160upx;height: 160upx;margin-top: 200upx;border-radius:20upx;" src="../../static/logo.png">
			</image>
			<view style="font-size: 46upx;margin-top: 20upx;color: #FFFFFF;font-weight: 500;margin-right: 5rpx;">答题APP</view>
			<view style="font-size: 28upx;margin-top: 8upx;color: #FFFFFF;font-weight: 300;">{{message}}</view>
			<button class="confirm-btn" @click="taobaoLogin">下载答题APP</button>
			<view style="margin-top: 300upx;text-align: center"><text
					style="color: #666666;font-size: 32upx;font-weight: 400;"></text></view>
			<view style="color: #333333;margin-top: 20upx;font-weight: 600;font-size: 38upx;"></view>
			<!-- <view style="margin-top: 40upx;background: #0096FF ;height: 10upx;width: 60upx;margin-left: 46%;"></view> -->
		</view>
		<view id="shareit" v-if="show_share" @tap="closeShare">
			<image class="arrow" src="https://renwu.xiansqx.com/img/20210813/9aa484db4c60458f86deb2a8ab6fe400.png">
			</image>
			<view id="follow">点击右上角按钮，选择浏览器打开下载！</view>
		</view>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				mobile: '',
				code: '',
				message: '',
				show_share: false,
				openShare: false,
				openShares: false,
				relationId: '',
				sending: false,
			};
		},
		onLoad(e) {


		},
		methods: {
			closeShare() {
				this.show_share = false;
			},
			taobaoLogin() {
				uni.setClipboardData({
					data: this.relationId,
					success: r => {
						// this.$queue.showToast('邀请码复制成功');
					}
				});
				var u = navigator.userAgent;
				if (u.indexOf('Android') > -1 || u.indexOf('Adr') > -1) {
					this.$Request.get('/app/common/type/49').then(res => {
						if (res.code == 0) {
							if (res.data && res.data.value) {
								if (this.openShare) {
									let ua = navigator.userAgent.toLowerCase();
									if (ua.indexOf('micromessenger') === -1) {
										this.show_share = false;
										// #ifndef H5
										plus.runtime.openURL(res.data.value, function(res) {

										});
										// #endif
										// #ifdef H5
										window.location.href = res.data.value;
										// #endif
									} else {
										this.show_share = true;
									}
								} else {
									// #ifndef H5
									plus.runtime.openURL(res.data.value, function(res) {

									});
									// #endif
									// #ifdef H5
									window.location.href = res.data.value;
									// #endif
								}
							}
						}
					});
				} else {
					this.$Request.get('/app/common/type/50').then(res => {
						if (res.code == 0) {
							if (res.data && res.data.value) {
								if (this.openShares) {
									let ua = navigator.userAgent.toLowerCase();
									if (ua.indexOf('micromessenger') === -1) {
										this.show_share = false;
										// #ifndef H5
										plus.runtime.openURL(res.data.value, function(res) {

										});
										// #endif
										// #ifdef H5
										window.location.href = res.data.value;
										// #endif
									} else {
										this.show_share = true;
									}
								} else {
									// #ifndef H5
									plus.runtime.openURL(res.data.value, function(res) {

									});
									// #endif
									// #ifdef H5
									window.location.href = res.data.value;
									// #endif
								}
							}
						}
					});
				}
			}
		}
	};
</script>

<style lang="scss">
	#shareit {
		-webkit-user-select: none;
		position: fixed;
		/*width: 100%;*/
		height: 2000px;
		background: rgba(0, 0, 0, 0.85);
		text-align: center;
		top: 0;
		left: 0;
		z-index: 999;
	}

	#shareit img {
		max-width: 100%;
	}

	.arrow {
		width: 100px;
		height: 150px;
		position: absolute;
		right: 5%;
		top: 1%;
	}

	#follow {
		margin-right: 60px;
		margin-left: 30px;
		width: 90%;
		height: 50px;
		line-height: 50px;
		text-align: left;
		text-decoration: none;
		font-size: 18px;
		color: white;
		float: left;
		margin-top: 160px;
	}

	.footer {
		padding-left: 140upx;
		margin-top: 32upx;
		font-size: 24upx;
		color: #666666;
		text-align: center;
		display: flex;
	}

	page {
		background: #fff;
	}

	.send-msg {
		border-radius: 30px;
		color: black;
		background: white;
		height: 30px;
		font-size: 14px;
		line-height: 30px;
	}

	.container {
		top: 0;
		padding-top: 50px;
		position: relative;
		width: 100%;
		height: 100%;
		overflow: hidden;
		background: #fff;
	}

	.wrapper {
		position: relative;
		z-index: 90;
		background: #fff;
		padding-bottom: 20px;
	}


	.right-top-sign {
		position: absolute;
		top: 40px;
		right: -15px;
		z-index: 95;

		&:before,
		&:after {
			display: block;
			content: '';
			width: 20px;
			height: 40px;
			background: #e10a07;
		}

		&:before {
			transform: rotate(50deg);
			border-radius: 0 50px 0 0;
		}

		&:after {
			position: absolute;
			right: -198px;
			top: 0;
			transform: rotate(-50deg);
			border-radius: 50px 0 0 0;
			/* background: pink; */
		}
	}

	.left-bottom-sign {
		position: absolute;
		left: -270px;
		bottom: -320px;
		/*border: 100upx solid #d0d1fd;*/
		border-radius: 50%;
		padding: 90px;
	}

	.welcome {
		position: relative;
		left: 30px;
		top: -55px;
		font-size: 28px;
		color: #555;
		text-shadow: 1px 0px 1px rgba(0, 0, 0, 0.3);
	}

	.input-content {
		padding: 0 20px;
	}

	.confirm-btn {
		width: 500upx;
		height: 100upx;
		line-height: 100upx;
		margin-top: 65%;
		color: #FFFFFF;
		font-size: 38upx;
		font-weight: 500;
		background-color: #0096FF;
		// &:after {
		// 	border-radius: 60px;
		// }
	}
</style>