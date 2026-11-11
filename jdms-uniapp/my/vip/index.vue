<template>
	<view class="container_back">
		<image src="/my/static/vip/viptitle.png" mode=""
			style="width: calc(100% - 120rpx);margin: auto;height: 60rpx;position: absolute;top: 150rpx;left: 0;right: 0;">
		</image>
		<view class="infoBox">
			<image src="/my/static/vip/zwkt.png" mode="" style="width: 175rpx;height: 35rpx;"></image>
			<view class="f24" style="color: #765542;">开通即可全年享受超值会员特权</view>
			<view class="f24" style="color: #765542;margin-top: 65rpx;" v-if="userInfo.vipEndTime">到期时间:{{userInfo.vipEndTime}}</view>
		</view>

		<view class="listBox">
			<view class="listBox_title">
				<image src="/my/static/vip/left.png" mode="" style="width: 111rpx;height: 14rpx;"></image>
				<view class="f32 text-heavy" style="color: #FEF0DF;">VIP超值权益</view>
				<image src="/my/static/vip/right.png" mode="" style="width: 111rpx;height: 14rpx;"></image>
			</view>

			<view class="qyList">
				<view class="qyList_item">
					<image src="/my/static/vip/jz.png" mode="" style="width: 79rpx;height: 79rpx;border-radius: 50%;"></image>
					<view class="f28" style="color: #EBD2A8;">专享九折</view>
				</view>
				<view class="qyList_item">
					<image src="/my/static/vip/sf.png" mode="" style="width: 79rpx;height: 79rpx;border-radius: 50%;"></image>
					<view class="f28" style="color: #EBD2A8;">身份标识</view>
				</view>
				<view class="qyList_item">
					<image src="/my/static/vip/kf.png" mode="" style="width: 79rpx;height: 79rpx;border-radius: 50%;"></image>
					<view class="f28" style="color: #EBD2A8;">专属客服</view>
				</view>
				<view class="qyList_item">
					<image src="/my/static/vip/jl.png" mode="" style="width: 79rpx;height: 79rpx;border-radius: 50%;"></image>
					<view class="f28" style="color: #EBD2A8;">专属奖励</view>
				</view>
			</view>

			<view class="flList">
				<scroll-view scroll-x="true" class="scroll-view_H">
					<view :class="moneyIndex == index?'flList_item':'flList_item_aic'" v-for="item,index in vipList" :key="index"
						@click="changeMoney(item,index)">
						<view class="f28 text-heavy padding-top-xl padding-bottom-xs">{{item.vipTitle}}</view>
						<view class="f56 text-bold padding-bottom-xl">{{item.vipMoney}}<text class="f20">元</text></view>
					</view>
				</scroll-view>
			</view>

			<view class="btn_Kt" @click="openPopup">立即开通</view>
			<view class="flex justify-center align-center margin-top">
				<up-checkbox size="14" :customStyle="{marginBottom: '8px'}" labelSize="13" shape="circle" label="我已阅读并同意"
					name="agree" usedAlone v-model:checked="aloneChecked"></up-checkbox>
				<view class="f24" style="color:#4B74FF;" @click="routerGo('/package/setting/userAgreement')">
					《用户服务协议》
				</view>
			</view>
		</view>
		<!-- 支付弹框 -->
		<up-popup :show="payShow" @close="payShow = false" :closeOnClickOverlay="true" round="12" :closeable="true">
			<view class="activity_bm  padding">
				<view class="f30 text-bold margin-bottom-lg">支付方式</view>
				<view class="flex align-center" style="margin: auto;margin-top: 20rpx;" v-for="(item, index) in payList"
					:key="index">
					<view class="">
						<image :src="item.image" mode=""
							style="width: 55rpx;height: 55rpx;margin-right: 20rpx;border-radius: 50rpx;">
						</image>
					</view>
					<up-radio-group v-model="payWay" iconPlacement="right" @change='selectWay(item.id)'>
						<up-radio :customStyle="{marginBottom: '8px'}" :label="item.name" :name="item.id" activeColor="#0087FC"
							inactiveColor="#272833">
						</up-radio>
					</up-radio-group>
				</view>
				<view style="display: flex;height: 100rpx;align-items: center;" class="justify-between"
					v-if="walletInfo.canCashMoney > 0">
					<view style="display: flex;width:80%;align-items: center;">
						<image src="/my/static/jb.png" style="width: 55rpx;height: 55rpx;border-radius: 50rpx;"></image>
						<view style="font-size: 30rpx;margin-left: 20rpx;width: 70%;color: #333333;">余额
						</view>
					</view>
					<view class="" @click='selectWay(5)'>
						<image src="/subpackage/static/detailImage/weixuanzhongyuanquan (2).png" mode=""
							style="width: 38rpx;height: 38rpx;border-radius: 50%;" v-if="!isBalance"></image>
						<image src="/my/static/xuanze (1).png" mode="" v-if="isBalance"
							style="width: 38rpx;height: 38rpx;border-radius: 50%;"></image>
					</view>
				</view>
				<view class="payBtn" @click="orderSave">立即支付</view>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	import {
		getPayClassify
	} from '@/common/platform.js';
	import {
		requestMpWxPayment,
		invokeH5WxPay
	} from '@/common/pay.js';
	const {
		proxy
	} = getCurrentInstance();

	const moneyId = ref('');
	const moneyIndex = ref(0);
	const aloneChecked = ref(false);
	const payShow = ref(false); //支付弹框
	const payWay = ref('');
	const isBalance = ref(false);
	const payList = reactive([])
	const vipInfo = reactive({});
	const isFinish = ref(false); // 修复：初始值改为 false，表示"未在执行中"
	const openPopup = () => {
		if (!aloneChecked.value) {
			uni.showToast({
				title: '请阅读并同意协议',
				icon: 'none'
			})
			return
		}
		payShow.value = true
	};

	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	};
	
	// 酒店特权/app/member/getMemberList

	// 立即下单
	const orderSave = () => {
		if (isFinish.value == true) return // 如果正在支付中，直接返回（防重复点击）
		isFinish.value = true // 标记为支付中
		let data = {
			vipId: vipInfo.vipId,
			classify: getPayClassify(payWay.value)
		}
		uni.showLoading({
			title: '支付中...'
		})
		if (payWay.value == 5) { //		余额支付
			proxy.$http.postT('/app/userMoney/payVip', data).then(res => {
				uni.hideLoading()
				if (res.code == 0) {
					uni.showToast({
						title: '支付成功',
						icon: 'none'
					})
					setTimeout(() => {
						getUserInfo()
						payShow.value = false
						isFinish.value = false // 支付完成，解锁
					}, 100)
				} else {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
					isFinish.value = false // 支付失败，解锁
					payShow.value = false
				}
			}).catch(() => {
				uni.hideLoading()
				isFinish.value = false // 请求异常，解锁
			})
		} else if (payWay.value == 1) { //微信支付
			console.log(data, "datadatadatadatadata")
			// #ifdef H5
			proxy.$http.postT('/app/wxPay/wxPayVip', data).then(red => {
				invokeH5WxPay(red, {
					onSuccess: () => {
						uni.hideLoading();
						uni.showToast({
							title: '支付成功',
							icon: 'none'
						});
						setTimeout(() => {
							getUserInfo();
							isFinish.value = false;
							payShow.value = false;
						}, 1000);
					},
					onFail: () => {
						uni.hideLoading();
						isFinish.value = false;
					}
				});
			}).catch(() => {
				uni.hideLoading()
				isFinish.value = false // 请求异常，解锁
			});
			// #endif
			// #ifdef APP-PLUS
			proxy.$http.postT('/app/wxPay/wxPayVip', data).then(red => {
				console.log(red, "redredredAPP")
				setPayment('wxpay', red.data);
			}).catch(() => {
				uni.hideLoading()
				isFinish.value = false // 请求异常，解锁
			});
			// #endif
			// #ifdef MP-WEIXIN
			data.openId = uni.getStorageSync('openId')
			proxy.$http.postT('/app/wxPay/wxPayVip', data).then(red => {
				uni.hideLoading()
				requestMpWxPayment(red, {
					onSuccess: () => {
						uni.showToast({
							title: '支付成功',
							icon: 'none'
						})
						setTimeout(() => {
							getUserInfo()
							isFinish.value = false
							payShow.value = false
						}, 1000)
					},
					onFail: (err) => {
						isFinish.value = false
						console.log(err, 'mp wx pay fail')
						uni.showToast({
							title: '支付失败',
							icon: 'none'
						})
					}
				});
			}).catch(() => {
				uni.hideLoading()
				isFinish.value = false // 请求异常，解锁
			});
			// #endif
		} else if (payWay.value == 2) { //支付宝
			// #ifdef H5
			proxy.$http.postT('/app/aliPay/payVip', data).then(red => {
				const div = document.createElement('div')
				div.innerHTML = red.data //此处form就是后台返回接收到的数据
				document.body.appendChild(div)
				document.forms[0].submit()
				isFinish.value = false // 提交表单后解锁（H5支付宝表单提交会跳转页面）
			}).catch(() => {
				uni.hideLoading()
				isFinish.value = false // 请求异常，解锁
			});
			// #endif
			// #ifdef APP-PLUS
			proxy.$http.postT('/app/aliPay/payVip', data).then(red => {
				setPayment('alipay', red.data);
			}).catch(() => {
				uni.hideLoading()
				isFinish.value = false // 请求异常，解锁
			});
			// #endif
		}
	};

	const setPayment = (name, order) => {
		console.log(order, '*-*-*')
		console.log(name, '*-*namenamename*')
		let paymentData = {};
		if (name === 'wxpay' && order) {
			paymentData = JSON.stringify({
				appid: order.appId, // 微信开放平台 - 应用 - AppId，注意和微信小程序、公众号 AppId 可能不一致
				noncestr: order.nonceStr, // 随机字符串
				package: order.packageValue, // 固定值
				partnerid: order.partnerId, // 微信支付商户号
				prepayid: order.prepayId, // 统一下单订单号
				timestamp: order.timeStamp, // 时间戳（单位：秒）
				sign: order.sign // 签名，这里用的 MD5/RSA 签名}
			})
		} else {
			paymentData = order
		}
		console.log(order, "app data微信支付")
		uni.requestPayment({
			provider: name,
			orderInfo: paymentData, //微信、支付宝订单数据
			success: function(res) {
				console.log(res, "app微信支付")
				uni.hideLoading();
				uni.showToast({
					title: '支付成功',
					icon: 'none'
				})
				setTimeout(() => {
					getUserInfo()
					isFinish.value = false // 支付成功，解锁
					payShow.value = false
				}, 1000)
			},
			fail: function(err) {
				isFinish.value = false // 支付失败，解锁
				uni.hideLoading();
				console.log(err, "app微信支付失败")
				uni.hideLoading();
			},
			complete() {
				isFinish.value = false // 无论成功失败，complete 中解锁
				uni.hideLoading();
			}
		});
	};
	const callPay = (response) => {
		if (typeof WeixinJSBridge === "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady(response), false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady(response));
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(response));
			}
		} else {
			onBridgeReady(response);
		}
	};
	const onBridgeReady = (response) => {
		if (!response.package) {
			isFinish.value = false // 参数异常，解锁
			return;
		}
		console.log(response, '++++++++')
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', {
				"appId": response.appid, //公众号名称，由商户传入
				"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
				"nonceStr": response.noncestr, //随机串
				"package": response.package,
				"signType": response.signType, //微信签名方式：
				"paySign": response.sign //微信签名
			},
			function(res) {
				console.log(res, '/*-/*-/*-')
				if (res.err_msg === "get_brand_wcpay_request:ok") {
					// 使用以上方式判断前端返回,微信团队郑重提示：
					//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
					uni.hideLoading();
					uni.showToast({
						title: '支付成功',
						icon: 'none'
					})
					setTimeout(() => {
						getUserInfo()
						isFinish.value = false // 支付成功，解锁
						payShow.value = false
					}, 1000)
				} else {
					uni.hideLoading();
					isFinish.value = false // 支付失败，解锁
				}
				WeixinJSBridge.log(response.err_msg);
			}
		);
	};



	const selectWay = (id) => {
		payWay.value = id;
		if (id == 5) {
			isBalance.value = true
		} else {
			isBalance.value = false
		}
		console.log(isBalance.value, 'payWay.value ')
	};


	const changeMoney = (item, index) => {
		moneyIndex.value = index
		Object.assign(vipInfo, item)
	};

	const vipList = reactive([]);

	// 获取配置列表
	const configList = () => {
		proxy.$http.getT('/app/vipConfig/getVipConfigList').then(res => {
			if (res.code == 0) {
				vipList.splice(0, vipList.length, ...res.data.records)
				Object.assign(vipInfo, res.data.records[0])
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const userInfo = reactive({});
	const getUserInfo = () => {
		proxy.$http.getT("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				Object.assign(userInfo, res.data)
				uni.setStorageSync('isVip', res.data.isVip); // 0否 1是
			}
			uni.hideLoading();
		});
	};

	const walletInfo = reactive({});

	const getWalletIndo = () => {
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				Object.assign(walletInfo, res.data)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	onLoad(option => {
		getWalletIndo()
		// #ifdef MP-WEIXIN
		payList.splice(0, payList.length);
		payWay.value = 1;
		let data = [];
		let data1 = [{
			id: 1,
			image: 'https://renwu.xiansqx.com/img/20210813/e71382c1b8404e4f96c6d6e89d305329.png',
			name: '微信'
		}]
		payList.splice(0, payList.length, ...data1)
		// #endif


		// #ifdef APP-PLUS
		payList.splice(0, payList.length);
		payWay.value = 1;
		let data1 = [{
			id: 1,
			image: 'https://renwu.xiansqx.com/img/20210813/e71382c1b8404e4f96c6d6e89d305329.png',
			name: '微信'
		}, {
			id: 2,
			image: 'https://renwu.xiansqx.com/img/20210813/56cde02b040e453ead8b5d2c0dc17e18.png',
			name: '支付宝'
		}]
		payList.splice(0, payList.length, ...data1)
		// #endif

		// #ifdef H5
		let target = navigator.userAgent.toLowerCase();
		let isWeixin = (/micromessenger/.test(target)) ? true : false;
		payList.splice(0, payList.length);
		if (isWeixin) {
			payWay.value = 1;
			let data1 = [{
				id: 1,
				image: 'https://renwu.xiansqx.com/img/20210813/e71382c1b8404e4f96c6d6e89d305329.png',
				name: '微信'
			}]
			payList.splice(0, payList.length, ...data1)
		} else {
			payWay.value = 2;
			let data1 = [{
				id: 2,
				image: 'https://renwu.xiansqx.com/img/20210813/56cde02b040e453ead8b5d2c0dc17e18.png',
				name: '支付宝'
			}]
			payList.splice(0, payList.length, ...data1)
		}
		// #endif

		configList()
		getUserInfo()
	});
	onShow(() => {

	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss" scoped>
	page {
		// background-color: #F2F6FC;
		background-image: url('/my/static/vip/vipback.png');
		width: 100%;
		min-height: 100vh;
		background-repeat: no-repeat;
		background-size: 100% 100%;
	}

	:deep().u-checkbox {
		margin: 0rpx !important;
	}

	:deep().u-radio__text {
		font-size: 28rpx !important;
		color: #333333 !important;
	}

	:deep().u-radio {
		// justify-content: space-between;
		width: 100%;
	}

	:deep().u-radio__icon-wrap {
		// background-color: #272833 !important;
		// border: solid #CCCCCC 1rpx !important;
		border-color: #CCCCCC !important;
	}

	.payBtn {
		background-color: #0087FC;
		border-radius: 44rpx;
		height: 88rpx;
		color: #FFFFFF;
		text-align: center;
		line-height: 88rpx;
		width: 100%;
		margin-top: 50rpx;
	}

	.container_back {
		background-image: url('/my/static/vip/vipback.png');
		width: 100%;
		min-height: 100vh;
		background-repeat: no-repeat;
		background-size: 100% 100%;
	}

	.btn_Kt {
		width: calc(100% - 60rpx);
		margin: auto;
		text-align: center;
		background: linear-gradient(122deg, #EFDCB5, #DDB27D);
		border-radius: 50rpx;
		height: 100rpx;
		color: #56260A;
		line-height: 100rpx;
		font-weight: bold;
		margin-top: 70rpx;
	}

	.flList_item_aic {
		background: #3B3938;
		border-radius: 8rpx;
		border: 4rpx solid #FEE3AD;
		width: calc((100% - 70rpx) / 3);
		text-align: center;
		color: #E4C293;
		display: inline-block;
		margin-right: 20rpx;
	}

	.flList_item_box {
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
	}

	.c35 {
		color: #353333;
	}

	.flList_item {
		background: linear-gradient(122deg, #EFDCB5, #DDB27D);
		border-radius: 8rpx;
		border: 4rpx solid #FEE3AD;
		width: calc((100% - 70rpx) / 3);
		text-align: center;
		color: #353333;
		display: inline-block;
		margin-right: 20rpx;
	}

	.flList {
		width: calc(100% - 60rpx);
		margin: auto;
		margin-top: 65rpx;
	}

	.qyList_item {
		width: calc((100% - 112rpx) / 4);
		text-align: center;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}

	.qyList {
		width: calc(100% - 30rpx);
		margin: auto;
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 70rpx;
	}

	.listBox_title {
		width: calc(100% - 320rpx);
		margin: auto;
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 12%;
	}

	.infoBox {
		background-image: url('/my/static/vip/vip.png');
		width: calc(100% - 120rpx);
		margin: auto;
		height: 260rpx;
		background-repeat: no-repeat;
		background-size: 100% 100%;
		position: absolute;
		top: 20%;
		left: 0;
		right: 0;
		padding: 30rpx;
	}

	.listBox {
		background-image: url('/my/static/vip/listBix.png');
		width: 100%;
		margin: auto;
		height: 65vh;
		background-repeat: no-repeat;
		background-size: 100% 100%;
		position: absolute;
		top: 35%;
		left: 0;
		right: 0;
	}
</style>