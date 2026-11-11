<template>
	<view>
		<!-- <u-navbar :border-bottom="false" :title-bold="true" title="选择国家">
		</u-navbar> -->
		<view class="search-input-wrap">
			<view class="search-input-view">
				<image class="search-text-icon" src="/static/rindex/sou.png"></image>
				<input class="search-input" v-model="countryZone" :placeholder="$t('index.qh')"
					placeholder-style="color:#B4BDD9" />
				<image v-if="filterName.length > 0" class="close-text-icon" src="/static/img/search/del.png"
					@tap="clearInput()"></image>
			</view>
		</view>
		<view class="contract-list">
			<view class="table-tr" v-for="(item, index) in countryItem" :key="index" v-if="countryItem.length > 0">
				<text class="list-showName" style="white-space:nowrap;" @tap="gobackToRegister(item.zone)">{{item.en}}</text>
				<text class="flex-view" @tap="gobackToRegister(item.zone)">+{{item.zone}}</text>
			</view>
			<view class="table-tr" v-if="countryItem.length <= 0">
				<text class="list-showName">{{$t('index.zwssjg')}}！</text>
			</view>
		</view>
	</view>
</template>

<script>
	var countryList = require("../../common/country-code.js");
	export default {
		data() {
			return {
				//搜索内容
				filterName: '',
				//国际区号
				countryItem: countryList.list,
				//区号
				countryZone: '',
				pageType: '',
				zyType: 'zh',
				type: ''
			}
		},
		onLoad(option) {
			if (option.type) {
				this.type = option.type
			}
			uni.setNavigationBarTitle({
				title: this.$t('bar.xzgj')
			})
		},
		onShow() {

		},
		computed: {},
		methods: {
			clearInput() {
				this.countryZone = '';
			},
			gobackToRegister(zone) {
				this.$queue.setData('zone', '+' + zone);
				this.$queue.setData('zoneType', this.type);
				uni.navigateBack({
					delta: 1
				})
			},
			renderCountryData() {
				var newData = [];
				var array = countryList.list;
				if (this.filterName && this.filterName.length > 0) {
					for (var i = 0; i < array.length; i++) {
						var zone = array[i].zone;
						var chinese = array[i].cn;
						var english = array[i].en;
						if ((english && (english.toUpperCase().startsWith(this.filterName.toUpperCase()) || english
								.toUpperCase().indexOf(this.filterName.toUpperCase()) > 0)) ||
							(zone && (zone.startsWith(this.filterName) || zone.indexOf(this.filterName) > 0)) ||
							(chinese && (chinese.startsWith(this.filterName) || chinese.indexOf(this.filterName) > 0))) {
							var index = newData.length;
							newData[index] = {};
							newData[index].zone = array[i].zone;
							newData[index].cn = array[i].cn;
							newData[index].en = array[i].en;
						}
					}
				} else {
					newData = array;
				}
				this.countryItem = newData;
			},
		},
		watch: {
			countryZone(newValue, oldValue) {
				this.filterName = newValue.toUpperCase();
				this.renderCountryData();
			}
		}
	}
</script>

<style>
	page {
		background-color: #FFFFFF;
	}

	.search-input-wrap {
		background-color: #FFFFFF;
		position: fixed;
		height: 76upx;
		width: 100%;
		padding-bottom: 20upx;
		/* border-bottom: 2upx solid #EAEFFA; */
		padding-top: 10upx;
	}

	.search-input-view {
		margin: 0 auto;
		padding: 5upx 0;
		display: flex;
		color: #203763;
		background: #EAEFFA;
		border-radius: 40upx;
		width: 90%;
		justify-content: center;
		align-items: center;
		height: 70upx;
	}

	.contract-list {
		margin: 100upx 38upx;
		display: inline-block;
		width: 90%;
	}

	.table-tr {
		border-bottom: 1upx solid #EAEFFA;
		display: flex;
		font-size: 24upx;
		padding: 20upx;
	}

	.list-showName {
		color: #203763;
		margin-right: 20upx;
	}

	.search-input {
		flex: 1;
		font-size: 28upx;
		margin-top: 4upx;
		text-indent: 20upx;
	}

	.search-text-icon {
		width: 30upx;
		height: 30upx;
		color: #B4BDD9;
		padding-left: 30upx;
		padding-top: 8upx;
	}

	.close-text-icon {
		width: 30upx;
		height: 30upx;
		color: #B4BDD9;
		padding-top: 8upx;
		padding-right: 30upx;
	}

	.star-text-icon {
		font-family: iconfont;
		width: 40upx;
		color: #B4BDD9;
		font-size: 38upx;
	}

	.star-text-icon.fill {
		color: #5D8FE9;
	}

	.flex-view {
		flex: 1;
		text-align: right;
	}
</style>