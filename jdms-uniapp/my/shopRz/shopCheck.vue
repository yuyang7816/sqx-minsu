<template>
	<view class="plat-page plat-form-page">
		<view class="plat-form-head">
			<view class="plat-form-head__title">商家信息</view>
			<view class="plat-form-head__hint">查看已入驻店铺资料</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-section">
				<view class="plat-form-field">
					<view class="plat-form-field__label">店铺名称</view>
					<view class="plat-form-field__value">
						<input type="text" disabled placeholder="请输入店铺名称" v-model="shopName" class="plat-form-field__input"
							placeholder-class="input-placeholder" />
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">联系人电话</view>
					<view class="plat-form-field__value">
						<input type="number" disabled maxlength="11" v-model="phone" placeholder="请输入联系人电话" class="plat-form-field__input"
							placeholder-class="input-placeholder" />
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">酒店地址</view>
					<view class="plat-form-field__value">
						<view v-if="shopAddress.address&&shopAddress.district">
							{{shopAddress.province}}{{shopAddress.city}}{{shopAddress.district}}{{shopAddress.address}}
						</view>
						<view style="color:var(--plat-text-sub);" v-else>请选择酒店地址</view>
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">店铺类型</view>
					<view class="plat-form-field__value plat-form-field__value--shop-type">
						<up-radio-group v-model="shopType" placement="row" disabled class="plat-radio-group--shop-type">
							<up-radio :name="1" label="酒店" activeColor="#2C8EFE" labelSize="14"></up-radio>
							<up-radio :name="2" label="民宿" activeColor="#2C8EFE" labelSize="14" style="margin-left: 20rpx;"></up-radio>
						</up-radio-group>
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">酒店标签</view>
					<view class="plat-form-field__value">
						<view class="flex align-center flex-wrap">
							<view class="plat-tag plat-tag--soft" style="margin-right:12rpx;margin-bottom:12rpx;" v-for="(tag, index) in tagList" :key="index">
								<text>{{tag}}</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">酒店封面图</view>
			<view class="flex align-center flex-wrap">
				<view class="plat-upload-box" v-if="!coverImage">
					<image src="/my/static/xiangji1 (1).png" mode="" class="plat-upload-box__icon"></image>
					<view class="plat-upload-box__text">上传图片</view>
				</view>
				<view class="plat-upload-box" v-else>
					<image :src="coverImage" mode="aspectFill" class="plat-upload-box__preview"></image>
				</view>
			</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">酒店轮播图</view>
			<view class="flex align-center flex-wrap">
				<view class="plat-upload-box" v-for="img,index in contentImg" :key="index"
					v-if="contentImg.length > 0">
					<image :src="img" mode="aspectFill" class="plat-upload-box__preview"></image>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot">
			<view class="plat-btn" @click="submitBtn">修改入驻信息</view>
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
	import config from '@/common/config.js'
	const {
		proxy
	} = getCurrentInstance();

	const shopId = ref('');
	const shopName = ref('');
	const phone = ref('');
	const shopAddress = reactive({
		address: '',
		latitude: '',
		longitude: '',
		province: '',
		city: '',
		district: ''
	});
	const coverImage = ref('');
	const contentImg = reactive([]);
	const shopType = ref(1);
	const auditRemark = ref('');
	const auditStatus = ref(null);
	const tagList = reactive([]);
	const tagInput = ref('');



	const submitBtn = () => {
		let SelKeFu = uni.getStorageSync('SelKeFu')
		if (SelKeFu + '' == '1') { //手机号
			uni.makePhoneCall({
				phoneNumber: uni.getStorageSync('kefuPhone')
			});
		} else if (SelKeFu + '' == '2') { //企业微信
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
		} else { //客服二维码页面
			uni.navigateTo({
				url: '/my/kefu/kefu'
			})
		}
	};


	const getShopInfo = (id) => {
		proxy.$http.get('/app/shops/getShopById', {
			shopId: id
		}).then(res => {
			if (res.code == 0 && res.data) {
				const data = res.data;
				shopName.value = data.shopName || '';
				phone.value = data.shopPhone || '';
				shopAddress.address = data.address || '';
				shopAddress.latitude = data.lat || '';
				shopAddress.longitude = data.lng || '';
				shopAddress.province = data.province || '';
				shopAddress.city = data.city || '';
				shopAddress.district = data.county || '';
				shopType.value = data.shopType || 1;
				if (data.tags) {
					tagList.splice(0, tagList.length, ...data.tags.split(',').filter(t => t));
				}
				coverImage.value = data.titleImg || '';
				if (data.carouselUrl) {
					contentImg.splice(0, contentImg.length, ...data.carouselUrl.split(','));
				}
				auditStatus.value = data.auditStatus || null;
				auditRemark.value = data.auditRemark || '';
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	onLoad(option => {
		if (option.shopId) {
			shopId.value = option.shopId;
			getShopInfo(option.shopId);
		}
	});
</script>

<style lang="scss">
	.input-placeholder {
		color: var(--plat-text-sub);
		font-size: 26rpx;
	}

	.plat-form-field__value--shop-type {
		display: flex;
		justify-content: flex-end;
	}

	.plat-radio-group--shop-type {
		display: flex;
		justify-content: flex-end;
	}
</style>
