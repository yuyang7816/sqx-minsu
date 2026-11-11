<template>
	<view style="line-height: 26px;padding: 32upx;" class="home1">
		<view style="font-size: 28upx;" v-html="content"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				content: '',
				type: ''
			}
		},
		onLoad({
			type
		}) {
			this.type = type
			this.getGuize();
			switch (this.type) {
				case '175':
					uni.setNavigationBarTitle({
						title: this.$t('repeatUse.bzzx')
					})
					break;
				case '176':
					uni.setNavigationBarTitle({
						title: this.$t('repeatUse.yszc')
					})
					break;
				case '177':
					uni.setNavigationBarTitle({
						title: this.$t('repeatUse.zcxy')
					})
					break;
				case '187':
					uni.setNavigationBarTitle({
						title: this.$t('bar.gywm')
					})
					break;
				case '293':
					uni.setNavigationBarTitle({
						title: this.$t('zx.yhxieyi')
					})
					break;
				case '417':
					uni.setNavigationBarTitle({
						title: this.$t('pageText.hyfwxy')
					})
					break;
				case '418':
					uni.setNavigationBarTitle({
						title: this.$t('pageText.wfsygz')
					})
					break;
			}
		},
		methods: {
			getGuize() {
				this.$Request.getT(`/app/common/type/177`).then(res => {
					this.content = res.data.value;
				})
			}
		}
	}
</script>

<style>
	page {
		background: #FFFFFF;
	}
</style>