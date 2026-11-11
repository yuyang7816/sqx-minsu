<script setup>
	import {
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance();
	import {
		onLaunch,
		onShow,
		onHide
	} from '@dcloudio/uni-app';
	import {
		parseInviteCodeFromOptions,
		saveInviteCode
	} from '/common/queue.js';

	const captureInviteCode = (option = {}) => {
		const code = parseInviteCodeFromOptions(option);
		if (code) {
			saveInviteCode(code);
		}
	};

	onLaunch((option) => {
		captureInviteCode(option?.query || option);
		// #ifdef H5
		try {
			const params = new URLSearchParams(window.location.search);
			const h5Code = params.get('otherInviterCode') || params.get('inviterCode');
			if (h5Code) {
				saveInviteCode(h5Code);
			}
		} catch (e) {}
		// #endif
		//企业微信客服链接
		proxy.$http.get('/app/common/type/513').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefu', res.data.value)
			}
		})
		//企业微信客服appid
		proxy.$http.get('/app/common/type/514').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefuAppId', res.data.value)
			}
		})
		//客服类型 
		proxy.$http.get('/app/common/type/512').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('SelKeFu', res.data.value)
			}
		})
		//客服电话  
		proxy.$http.get('/app/common/type/252').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefuPhone', res.data.value)
			}
		})
		//客服二维码
		proxy.$http.get('/app/common/type/1').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefuImage', res.data.value)
			}
		})
		//官方邀请码
		proxy.$http.get('/app/common/type/419').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('gfInvitationCode', res.data.value)
			}
		})
	});
	onShow(() => {
		console.log('onShow')
	});
	onHide(() => {
		console.log('onHide')
	});
</script>

<style lang="scss">
	/*每个页面公共css */
	//uview-plus公共样式
	@import "@/uni_modules/uview-plus/index.scss";
	//自定义公共样式
	@import "@/static/css/main.css";
	@import "@/style/main.scss";
	@import "@/static/css/guest.css";
	@import "@/static/css/host.css";
	@import "@/static/css/theme-soft.css";
	@import "@/static/css/toolkit.css";
	@import "@/static/css/platform.css";

	page {
		background-color: #F4F6FA;
	}
</style>