<template>
	<view class="guest-page">
		<!-- #ifndef H5 -->
		<up-navbar :leftIcon="leftIcon" title="我的" :bgColor="bgColor" placeholder></up-navbar>
		<!-- #endif -->

		<view class="guest-hero">
			<view class="guest-profile">
				<image class="guest-profile__avatar" :src="avatar" @click="routerGo('/pages/my/userInfo')"></image>
				<view class="guest-profile__info" v-if="isLogin">
					<view class="guest-profile__name">{{userName}}</view>
					<view class="guest-profile__id">ID：{{invitationCode}}</view>
				</view>
				<view class="guest-profile__name" style="margin-left: 20rpx;" v-if="!isLogin" @click="goLogin()">点击登录</view>
			</view>

			<view class="guest-vip" @click="routerGo('/my/vip/index')">
				<view class="guest-vip__text">
					<image src="/static/image/my/vip.png" mode="" style="width: 138rpx;height: 28rpx;"></image>
					<view style="margin-top: 10rpx; font-size: 24rpx; opacity: 0.9;">开通会员享专属权益</view>
				</view>
				<view class="guest-vip__btn">{{userInfo.isVip == 1?'续费':'开通'}}</view>
			</view>

			<view class="guest-card guest-stats">
				<view class="guest-stats__row">
					<view class="guest-stats__item" @click="routerGo('/my/ticket/myTicket')">
						<image src="/static/image/my/yhq.png" mode="" style="width: 36rpx;height: 36rpx;margin-right: 8rpx;">
						</image>
						优惠券<text>{{numData.couponNum||'0'}}</text>
					</view>
					<view class="guest-stats__sep"></view>
					<view class="guest-stats__item" @click="routerGo('/my/collection/collection')">
						<image src="/static/image/my/sc.png" mode="" style="width: 36rpx;height: 36rpx;margin-right: 8rpx;"></image>
						收藏<text>{{numData.collectNum ||'0'}}</text>
					</view>
					<view class="guest-stats__sep"></view>
					<view class="guest-stats__item" @click="routerGo('/my/footprint/footprint')">
						<image src="/static/image/my/zj.png" mode="" style="width: 36rpx;height: 36rpx;margin-right: 8rpx;"></image>
						足迹<text>{{numData.footprintNum ||'0'}}</text>
					</view>
				</view>
				<view class="guest-order-grid">
					<view class="guest-order-grid__item" @click="routerGoTab('/pages/order/index',1)">
						<image src="/static/image/my/dfk_.png" mode=""></image>
						<view style="margin-top: 6rpx;">待付款</view>
					</view>
					<view class="guest-order-grid__item" @click="routerGoTab('/pages/order/index',2)">
						<image src="/static/image/my/dcx.png"></image>
						<view style="margin-top: 6rpx;">待出行</view>
					</view>
					<view class="guest-order-grid__item" @click="routerGoTab('/pages/order/index',3)">
						<image src="/static/image/my/ddp.png"></image>
						<view style="margin-top: 6rpx;">待点评</view>
					</view>
					<view class="guest-order-grid__item" @click="routerGoTab('/pages/order/index',4)">
						<image src="/static/image/my/tkd.png"></image>
						<view style="margin-top: 6rpx;">退款单</view>
					</view>
					<view class="guest-order-grid__item" @click="routerGoTab('/pages/order/index')">
						<image src="/static/image/my/wddd.png"></image>
						<view style="margin-top: 6rpx;">全部订单</view>
					</view>
				</view>
			</view>
		</view>

		<view class="guest-card guest-wallet" @click="routerGo('/my/wallet/wallet')">
			<view class="guest-wallet__nums">
				<view>
					<view class="guest-wallet__num">
						{{formatMoney(moneyData.totalMoney).num}}<text v-if="formatMoney(moneyData.totalMoney).unit"
							style="font-size: 24rpx;">{{formatMoney(moneyData.totalMoney).unit}}</text>
					</view>
					<view class="guest-wallet__label">全部资金</view>
				</view>
				<view>
					<view class="guest-wallet__num">
						{{formatMoney(moneyData.userMoney).num}}<text v-if="formatMoney(moneyData.userMoney).unit"
							style="font-size: 24rpx;">{{formatMoney(moneyData.userMoney).unit}}</text>
					</view>
					<view class="guest-wallet__label">可用资金</view>
				</view>
				<view>
					<view class="guest-wallet__num">
						{{formatMoney(moneyData.canCashMoney).num}}<text v-if="formatMoney(moneyData.canCashMoney).unit"
							style="font-size: 24rpx;">{{formatMoney(moneyData.canCashMoney).unit}}</text>
					</view>
					<view class="guest-wallet__label">可提现</view>
				</view>
			</view>
			<view class="guest-wallet__entry">
				<image src="/static/image/my/qb.png"></image>
				<view style="margin-top: 8rpx;">钱包</view>
			</view>
		</view>

		<view class="guest-card guest-menu">
			<view class="guest-menu__title">更多服务</view>
			<view class="guest-menu__grid">
				<view class="guest-menu__item" @click="routerGo('/subpackage/invoice/index')">
					<image src="/static/image/my/fp.png"></image>
					<view style="margin-top: 8rpx;">发票管理</view>
				</view>
				<view class="guest-menu__item" @click="routerGo('/my/ticket/ticket')">
					<image src="/static/image/my/youhuiquan.png"></image>
					<view style="margin-top: 8rpx;">优惠券</view>
				</view>
				<view class="guest-menu__item" @click="routerGo('/pages/my/invitationUser')">
					<image src="/static/image/my/yqhy.png"></image>
					<view style="margin-top: 8rpx;">邀请好友</view>
				</view>
				<view class="guest-menu__item" @click="routerGoRz()" v-if="shopStatus != 1">
					<image src="/static/image/my/sjrz.png"></image>
					<view style="margin-top: 8rpx;">商家入驻</view>
				</view>
				<view class="guest-menu__item" @click="routerGo('/shopCenter/index/index')"
					v-if="shopStatus == 1&&isShopRider == 3">
					<image src="/static/image/my/sjzx.png"></image>
					<view style="margin-top: 8rpx;">商家中心</view>
				</view>
				<view class="guest-menu__item" @click="routerGo('/shopCenter/index/index')"
					v-if="shopStatus == 1&&isShopRider == 1">
					<image src="/static/image/my/ygzx.png"></image>
					<view style="margin-top: 8rpx;">员工中心</view>
				</view>
				<view class="guest-menu__item" @click="routerGo('/package/team/team')" v-if="userInfo.zhiRate > 0">
					<image src="/static/image/my/tjzx.png"></image>
					<view style="margin-top: 8rpx;">推广中心</view>
				</view>
				<view class="guest-menu__item" @click="routerGo('/my/cyMsg/index')">
					<image src="/static/image/my/cyxx.png"></image>
					<view style="margin-top: 8rpx;">常用信息</view>
				</view>
				<view class="guest-menu__item" @click="selectKeFu">
					<image src="/static/image/my/zxkf.png"></image>
					<view style="margin-top: 8rpx;">在线客服</view>
				</view>
				<view class="guest-menu__item" @click="routerGo('/my/feedbackIndex/feedbackIndex')">
					<image src="/static/image/my/bzzx.png"></image>
					<view style="margin-top: 8rpx;">帮助中心</view>
				</view>
				<view class="guest-menu__item" @click="routerGo('/my/feedback/index')">
					<image src="/static/image/my/yjfk.png"></image>
					<view style="margin-top: 8rpx;">意见反馈</view>
				</view>
				<view class="guest-menu__item" @click="routerGo('/package/setting/setting')">
					<image src="/static/image/my/szzx.png"></image>
					<view style="margin-top: 8rpx;">设置</view>
				</view>
			</view>
		</view>

		<view style="height: 40rpx;"></view>
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
	const {
		proxy
	} = getCurrentInstance();
	const leftIcon = ref('');
	const bgColor = ref('#56adfe');
	const isLogin = ref(false);
	const avatar = ref('');
	const userName = ref('');
	const invitationCode = ref('');
	const moneyData = reactive({});
	const numData = reactive({});

	const clearReactiveObject = (target) => {
		Object.keys(target).forEach(key => {
			delete target[key];
		});
	};

	const resetMyPageState = () => {
		isLogin.value = false;
		avatar.value = '/static/logo.png';
		userName.value = '游客';
		invitationCode.value = '';
		shopStatus.value = null;
		isShopRider.value = null;
		shopId.value = null;
		clearReactiveObject(moneyData);
		clearReactiveObject(numData);
		clearReactiveObject(userInfo);
	};

	const formatMoney = (val) => {
		if (val === undefined || val === null || val === '') return {
			num: '0.00',
			unit: ''
		};
		let num = parseFloat(val);
		if (isNaN(num)) return {
			num: '0.00',
			unit: ''
		};
		if (num >= 100000000) {
			return {
				num: (num / 100000000).toFixed(2),
				unit: '亿'
			};
		} else if (num >= 10000) {
			return {
				num: (num / 10000).toFixed(2),
				unit: '万'
			};
		} else {
			return {
				num: num.toFixed(2),
				unit: ''
			};
		}
	};

	const selectKeFu = () => {
		let SelKeFu = uni.getStorageSync('SelKeFu')
		if (SelKeFu + '' == '1') {
			uni.makePhoneCall({
				phoneNumber: uni.getStorageSync('kefuPhone')
			});
		} else if (SelKeFu + '' == '2') {
			// #ifdef MP-WEIXIN
			wx.openCustomerServiceChat({
				extInfo: {
					url: uni.getStorageSync('kefu')
				},
				corpId: uni.getStorageSync('kefuAppId'),
				success(res) {}
			})
			// #endif
			// #ifdef H5
			window.location.href = uni.getStorageSync('kefu')
			// #endif
			// #ifdef APP
			let kefu = uni.getStorageSync('kefu')
			console.log(kefu)
			plus.runtime.openURL(kefu, function(res) {});
			// #endif
		} else {
			uni.navigateTo({
				url: '/my/kefu/kefu'
			})
		}
	};

	const routerGo = (url) => {
		if (uni.getStorageSync('token')) {
			uni.navigateTo({
				url
			})
		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
	};
	const routerGoTab = (url, type) => {
		if (uni.getStorageSync('token')) {
			uni.switchTab({
				url
			})
		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
		uni.setStorageSync('index', type)
	};
	const routerGoRz = () => {
		if (uni.getStorageSync('token')) {
			if (shopId.value) {
				uni.navigateTo({
					url: '/my/shopRz/index?shopId=' + shopId.value
				})
			} else {
				uni.navigateTo({
					url: '/my/shopRz/index'
				})
			}
		} else {
			uni.showModal({
				title: '提示',
				content: '您还未登录,请先登录',
				success: function(res) {
					if (res.confirm) {
						console.log('用户点击确定');
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else if (res.cancel) {
						console.log('用户点击取消');
					}
				}
			})
		}
	};

	onLoad(option => {

	});
	onShow(() => {
		let token = uni.getStorageSync('token')
		if (token) {
			isLogin.value = true
			getUserInfo();
			getDataInfo()
		} else {
			resetMyPageState();
		}
	});

	const getDataInfo = () => {
		proxy.$http.get('/app/user/selectUserMoney').then(res => {
			if (res.code == 0) {
				Object.assign(moneyData, res.data)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
		proxy.$http.get('/app/user/selectUserNum').then(res => {
			if (res.code == 0) {
				Object.assign(numData, res.data)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}


	const goLogin = () => {
		uni.navigateTo({
			url: '/pages/public/login'
		})
	};
	const isShopRider = ref(null);
	const shopStatus = ref(null);
	const userInfo = reactive({});
	const shopId = ref(null);

	const getUserInfo = () => {
		let userId = uni.getStorageSync('userId');
		if (!userId) {
			resetMyPageState();
			uni.navigateTo({
				url: '/pages/public/login'
			});
			return;
		}
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				Object.assign(userInfo, res.data)
				avatar.value = res.data.avatar ? res.data.avatar : '/static/logo.png';
				userName.value = res.data.userName ? res.data.userName : '游客';
				uni.setStorageSync('userName', res.data.userName)
				uni.setStorageSync('userPhone', res.data.phone)
				uni.setStorageSync('myInvitationCode', res.data.invitationCode)
				invitationCode.value = res.data.invitationCode
				shopStatus.value = res.data.shopStatus
				isShopRider.value = res.data.isShopRider
				if (res.data.shopId) {
					shopId.value = res.data.shopId
					uni.setStorageSync('shopId', res.data.shopId)
				}
				if (res.data.shopStatus == 2) {
					uni.showModal({
						title: '提示',
						content: '您的入驻审核未通过，请重新提交',
						success: function(red) {
							if (red.confirm) {
								uni.navigateTo({
									url: '/my/shopRz/index?shopId=' + res.data.shopId
								})
							}
						}
					})
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	}
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
</script>

<style lang="scss">
	page {
		background: var(--g-bg, #F0F4F8);
	}

	.guest-hero {
		padding-bottom: 0;
	}

	.guest-stats {
		margin: 24rpx 28rpx 0;
		position: relative;
		z-index: 5;
	}

	.guest-wallet {
		position: relative;
		z-index: 5;
	}

	.guest-menu {
		position: relative;
		z-index: 5;
	}
</style>
