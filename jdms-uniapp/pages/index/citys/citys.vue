<template>
	<view class="plat-page">
		<view class="plat-head plat-head--immersive">
			<view class="plat-sec-head" style="padding-top: 20rpx;">
				<view class="plat-sec-head__bar"></view>
				<view class="plat-sec-head__title">选择城市</view>
			</view>
		</view>

		<view class="plat-body plat-body--overlap">
			<view class="plat-card plat-city-current">
				<view class="plat-city-current__label">当前定位</view>
				<view class="plat-city-current__name">{{ activeCity || '点击获取当前城市' }}</view>
			</view>

			<view class="plat-sec-title">热门城市</view>
			<view class="plat-city-grid">
				<view class="plat-city-item" v-for="(item, index) in hotCity" :key="index" @click="cityClick(item)">
					{{ item.name }}
				</view>
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
		onReachBottom,
		onPageScroll
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	import citys from './citys.js'

	//需要构建索引参数的名称（注意：传递的对象里面必须要有这个名称的参数）
	const formatName = ref('title');
	//当前城市
	const activeCity = ref('');
	//热门城市
	const hotCity = reactive([{
			code: "110100",
			name: "北京"
		},
		{
			code: "310100",
			name: "上海"
		},
		{
			code: "330100",
			name: "杭州"
		},
		{
			code: "440100",
			name: "广州"
		},
		{
			code: "440300",
			name: "深圳"
		},
		{
			code: "320100",
			name: "南京"
		},
		{
			code: "500100",
			name: "重庆"
		},
		{
			code: "510100",
			name: "成都"
		}
	]);
	//显示的城市数据
	const obtainCitys = reactive([]);
	const lat = ref('');
	const lng = ref('');
	const isShowTips = ref(false);

	onLoad(option => {
		//动态更新数据
		setTimeout(() => {
			//修改数据格式
			formatName.value = 'cityName'
			// #ifndef APP
			uni.getLocation({
				type: 'gcj02', //wgs84  gcj02
				success: function(res) {
					console.log(res, '99999999')
					lat.value = res.latitude
					lng.value = res.longitude
					selectCity(res.longitude, res.latitude);
				},
				fail: function() {
					console.log('获取地址失败')
				},
				complete: function() {
					console.log('获取地址失败==')
				}
			})
			// #endif
			// #ifdef APP
			if (uni.getStorageSync('locationCity')) {
				uni.getLocation({
					type: 'gcj02', //wgs84  gcj02
					success: function(res) {
						console.log(res, '99999999')
						lat.value = res.latitude
						lng.value = res.longitude
						selectCity(res.longitude, res.latitude);
					},
					fail: function() {
						console.log('获取地址失败')
					},
					complete: function() {
						console.log('获取地址失败==')
					}
				})
			}
			// #endif
			//修改构建索引数据
			obtainCitys.splice(0, obtainCitys.length, ...citys)

		}, 100)
		activeCity.value = uni.getStorageSync('locationCity') ? uni.getStorageSync('locationCity') : '点击获取当前城市'
	});

	const openAppSettings = () => {
		uni.showModal({
			title: '提示',
			content: '您已拒绝APP获取定位权限,该功能需要定位才能使用',
			confirmText: '开启',
			success: e => {
				if (e.confirm) {
					uni.openAppAuthorizeSetting({
						success(res) {
							console.log(res)
						}
					})
				}
			}
		});
	};

	// 获取城市
	const selectCity = (lng, lat) => {
		let data = {
			lat: lat,
			lng: lng
		}
		proxy.$http.getT('/app/map/reverseGeocode', data).then(res => {
			if (res.code == 0) {
				const cityName = res.data.city || res.data.province || res.data.district
				if (cityName) {
					activeCity.value = cityName
					uni.setStorageSync('city', cityName)
					uni.setStorageSync('locationCity', cityName)
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const cityClick = (item) => {
		if (!uni.getStorageSync('locationCity')) {
			uni.getLocation({
				type: 'gcj02', //wgs84  gcj02
				success: function(res) {
					console.log(res, '99999999')
					lat.value = res.latitude
					lng.value = res.longitude
					selectCity(res.longitude, res.latitude);
				},
				fail: function() {
					console.log('获取地址失败')
				},
				complete: function() {
					console.log('获取地址失败==')
				}
			})
		} else {
			uni.showToast({
				icon: 'none',
				title: '切换成功',
				mask: true
			})
			console.log(item)
			let cityName = item;
			if (item.cityName) {
				cityName = item.cityName;
			}
			uni.setStorageSync('city', cityName)
			setTimeout(function() {
				// uni.navigateBack()
				let pages = getCurrentPages(); // 当前页面
				let beforePage = pages[pages.length - 2]; // 上一页
				console.log(beforePage)
				uni.navigateBack({
					success: function() {
						// beforePage.onShow(); // 执行上一页的onShow方法
					}
				});
			}, 500)
		}
	}
</script>

<style>
</style>
