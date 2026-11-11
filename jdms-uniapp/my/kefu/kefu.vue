<template>
	<view class="plat-page plat-im-page">
		<view class="plat-head plat-im-head">
			<view class="plat-im-head__inner">
				<view class="plat-im-avatar plat-im-avatar--cs">
					<text class="plat-im-avatar__text">客</text>
				</view>
				<view class="plat-im-head__info">
					<view class="plat-im-head__name">在线客服</view>
					<view class="plat-im-head__status">添加客服微信咨询</view>
				</view>
			</view>
		</view>

		<view class="plat-im-stream">
			<view class="plat-im-time">今天</view>

			<view class="plat-im-row plat-im-row--left">
				<view class="plat-im-avatar plat-im-avatar--cs">
					<text class="plat-im-avatar__text">客</text>
				</view>
				<view class="plat-im-bubble plat-im-bubble--left">
					<view class="plat-im-bubble__text">您好，欢迎咨询！请添加客服微信或扫描下方二维码，我们将尽快为您服务。</view>
				</view>
			</view>

			<view class="plat-im-row plat-im-row--left">
				<view class="plat-im-avatar plat-im-avatar--cs">
					<text class="plat-im-avatar__text">客</text>
				</view>
				<view class="plat-im-bubble plat-im-bubble--left">
					<view class="plat-im-bubble__label">客服微信号</view>
					<view class="plat-im-bubble__wx">{{weixin}}</view>
				</view>
			</view>

			<view class="plat-im-row plat-im-row--left" v-if="image">
				<view class="plat-im-avatar plat-im-avatar--cs">
					<text class="plat-im-avatar__text">客</text>
				</view>
				<view class="plat-im-bubble plat-im-bubble--left plat-im-bubble--qr">
					<image @click="prewImg(image)" mode="aspectFit" class="plat-im-bubble__qr" :src="image"></image>
					<view class="plat-im-bubble__tip" v-if="isWeiXin">{{ isWeiXin ? '长按识别上方二维码' : '' }}</view>
					<view class="plat-im-bubble__refresh" v-if="isWeiXin" @click="rests">无法识别？</view>
				</view>
			</view>
		</view>

		<view class="plat-im-foot">
			<view class="plat-im-foot__bar">
				<view class="plat-im-foot__wx">微信号：{{weixin}}</view>
				<view class="plat-btn plat-btn--sm plat-im-foot__copy" @click="copyHref">一键复制</view>
			</view>
		</view>
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
	const image = ref('');
	const isWeiXin = ref(false);
	const weixin = ref('710070994');
	const webviewStyles = reactive({
		progress: {
			color: '#1A1929 '
		}
	});
	const SelKeFu = ref('2');
	onLoad(option => {
		SelKeFu.value = uni.getStorageSync('SelKeFu');
		// #ifdef H5
		let ua = navigator.userAgent.toLowerCase();
		if (ua.indexOf('micromessenger') !== -1) {
			isWeiXin.value = true;
		}
		// #endif
		//客服微信号
		proxy.$http.postJson("/app/config/info", {
			key: 'sys_customer_service_wx'
		}).then(res => {
			if (res.code == 0 && res.data.value) {
				image.value = res.data.value;
			}
		});
		//客服二维码
		proxy.$http.postJson("/app/config/info", {
			key: 'sys_customer_service_qrcode'
		}).then(res => {
			if (res.code == 0 && res.data.value) {
				weixin.value = res.data.value;
			}
		});
	});
	//邀请码复制
	const copyHref = () => {
		uni.setClipboardData({
			data: weixin.value,
			success: r => {
				uni.showToast({
					title: '复制成功'
				})
			}
		});
	};
	const prewImg = (img) => {
		uni.previewImage({
			urls: [img],
			current: 0
		})
	};
	const rests = () => {
		uni.showToast({
			title: '已刷新请再次长按识别',
			mask: false,
			duration: 1500,
			icon: 'none'
		});
		window.location.reload();
	};
</script>
