<template>
	<view class="plat-page plat-agree-page">
		<view class="plat-head plat-head--immersive">
			<view class="plat-setting-head">
				<view class="plat-setting-head__title">关于我们</view>
				<view class="plat-setting-head__hint">了解平台与服务信息</view>
			</view>
		</view>
		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-card plat-agree-card">
				<up-parse :content="contont"></up-parse>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const contont = ref('');
	const type = ref('');
	onLoad((option) => {
		// if (option.type) {
		// 	type.value = option.type
		// 	switch (option.type) {
		// 		case 509:
		// 			return uni.setNavigationBarTitle({
		// 				title: option.text
		// 			})
		// 	}
		// }
		getContent()
	});
	const getContent = () => {
		proxy.$http.get("/app/common/type/510").then(res => {
			if (res.code == 0 && res.data.value) {
				contont.value = res.data.value
			}
		});
	}
</script>

<style lang="scss">

</style>
