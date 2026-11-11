<template>
	<view class="plat-page plat-agree-page plat-help-detail-page">
		<view class="plat-head plat-head--immersive">
			<view class="plat-setting-head">
				<view class="plat-setting-head__title">{{ resultData.helpWordTitle }}</view>
				<view class="plat-setting-head__hint">帮助详情</view>
			</view>
		</view>
		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-card plat-agree-card">
				<up-parse :content="content"></up-parse>
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
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const resultData = reactive({});
	const content = ref('');
	const helpWordId = ref('');
	onLoad(option => {

		uni.setNavigationBarTitle({
			title: option.title
		});
		if (option.helpWordId) {
			helpWordId.value = option.helpWordId
			getContent()
		}
	});
	const getContent = () => {
		let data = {
			id: helpWordId.value,
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.postJson("/app/help/article/info", data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				Object.assign(resultData, res.data)
				content.value = res.data.content;
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	}
</script>

<style lang="scss">
</style>
