<template>
	<view class="plat-page plat-form-page">
		<view class="plat-form-head">
			<view class="plat-form-head__title">开票说明</view>
			<view class="plat-form-head__hint">了解电子发票开具规则与注意事项</view>
		</view>
		<view class="plat-form-card">
			<view class="plat-info-content" v-html="content"></view>
		</view>
	</view>
</template>
<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		computed
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

	const content = ref('');

	onLoad(() => {
		// proxy.$http.postJson("/app/config/info", {
		// 	key: 'invoice_instructions'
		// }).then(res => {
		// 	if (res.code == 0) {
		// 		if (res.data && res.data.value) {
		// 			content.value = res.data.value;
		// 		}
		// 	}
		// });
		proxy.$http.get('/app/common/type/511').then(res => {
			if (res.code == 0 && res.data.value) {
				content.value = res.data.value;
			}
		})
	})
</script>


<style lang="scss">
</style>
