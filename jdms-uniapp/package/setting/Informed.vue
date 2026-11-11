<template>
	<view class="plat-page plat-agree-page">
		<view class="plat-head plat-head--immersive">
			<view class="plat-setting-head">
				<view class="plat-setting-head__title">知情同意书</view>
				<view class="plat-setting-head__hint">请确认已了解相关内容</view>
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
	onLoad((option) => {
		getContent()
	});
	const getContent = () => {
		proxy.$http.get('/app/common/type/361').then(res => {
			contont.value = res.data.value;
		})
	}
</script>

<style lang="scss">

</style>
