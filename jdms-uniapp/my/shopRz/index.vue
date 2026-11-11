<template>
	<view class="plat-page plat-form-page">
		<view class="plat-form-head">
			<view class="plat-form-head__title">商家入驻</view>
			<view class="plat-form-head__hint">填写店铺资料，提交审核后即可发布房源</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-section">
				<view class="plat-audit-tip" v-if="auditStatus == 2">拒绝原因：{{auditRemark}}</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">店铺名称</view>
					<view class="plat-form-field__value">
						<input type="text" placeholder="请输入店铺名称" v-model="shopName" class="plat-form-field__input"
							placeholder-class="plat-form-field__placeholder" />
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">联系人电话</view>
					<view class="plat-form-field__value">
						<input type="number" maxlength="11" v-model="phone" placeholder="请输入联系人电话" class="plat-form-field__input"
							placeholder-class="plat-form-field__placeholder" />
					</view>
				</view>
				<view class="plat-form-field" @click="selectCity">
					<view class="plat-form-field__label">酒店地址</view>
					<view class="plat-form-field__value">
						<view v-if="shopAddress.address&&shopAddress.district">
							{{shopAddress.province}}{{shopAddress.city}}{{shopAddress.district}}{{shopAddress.address}}
						</view>
						<view class="plat-form-field__placeholder" v-else>请选择酒店地址</view>
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">店铺类型</view>
					<view class="plat-form-field__value plat-form-field__value--shop-type">
						<up-radio-group v-model="shopType" placement="row" class="plat-radio-group--shop-type">
							<up-radio :name="1" label="酒店" activeColor="#2C8EFE" labelSize="14"></up-radio>
							<up-radio :name="2" label="民宿" activeColor="#2C8EFE" labelSize="14" style="margin-left: 20rpx;"></up-radio>
						</up-radio-group>
					</view>
				</view>
				<view class="plat-form-field plat-form-field--stack">
					<view class="plat-form-field__label">酒店标签</view>
					<view class="plat-form-field__value">
						<view class="flex align-center flex-wrap">
							<view class="plat-tag plat-tag--soft" style="margin-right:16rpx;margin-bottom:16rpx;display:inline-flex;align-items:center;"
								v-for="(tag, index) in tagList" :key="index">
								<text style="margin-right:8rpx;">{{tag}}</text>
								<u-icon name="close" size="20rpx" color="#2c8efe" @click="removeTag(index)"></u-icon>
							</view>
							<view class="plat-tag-input-wrap" v-if="tagList.length < 10">
								<input class="plat-tag-input" type="text" v-model="tagInput" placeholder="输入标签" confirm-type="done"
									@confirm="addTag" @blur="addTag" maxlength="10" />
							</view>
						</view>
						<view class="plat-tag-tip">最多添加10个标签，按回车或失焦确认</view>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">酒店封面图</view>
			<view class="flex align-center flex-wrap">
				<view class="plat-upload-box" @click="chooseImages('封面')" v-if="!coverImage">
					<image src="/my/static/xiangji1 (1).png" mode="" class="plat-upload-box__icon"></image>
					<view class="plat-upload-box__text">上传图片</view>
				</view>
				<view class="plat-upload-box" v-else @click="chooseImages('封面')">
					<image :src="coverImage" mode="aspectFill" class="plat-upload-box__preview"></image>
					<view class="plat-upload-box__del" @click.stop="delImg('封面',0)">
						<u-icon name="close-circle-fill" color="#2c8efe" size="40rpx"></u-icon>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-card__title">酒店轮播图</view>
			<view class="flex align-center flex-wrap">
				<view class="plat-upload-box" v-for="img,index in contentImg" :key="index"
					v-if="contentImg.length > 0">
					<image :src="img" mode="aspectFill" class="plat-upload-box__preview"></image>
					<view class="plat-upload-box__del" @click.stop="delImg('轮播',index)">
						<u-icon name="close-circle-fill" color="#2c8efe" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="plat-upload-box" @click="chooseImages('轮播')" v-if="contentImg.length < 8">
					<image src="/my/static/xiangji1 (1).png" mode="" class="plat-upload-box__icon"></image>
					<view class="plat-upload-box__text">上传图片</view>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot">
			<view class="plat-btn" @click="submitBtn">提交</view>
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

	// 添加标签
	const addTag = () => {
		const val = tagInput.value.trim();
		if (!val) {
			tagInput.value = '';
			return;
		}
		if (tagList.length >= 10) {
			uni.showToast({
				title: '最多添加10个标签',
				icon: 'none'
			});
			tagInput.value = '';
			return;
		}
		if (tagList.includes(val)) {
			uni.showToast({
				title: '标签已存在',
				icon: 'none'
			});
			tagInput.value = '';
			return;
		}
		tagList.push(val);
		tagInput.value = '';
	};

	// 删除标签
	const removeTag = (index) => {
		tagList.splice(index, 1);
	};

	const submitBtn = () => {
		if (!shopName.value) {
			uni.showToast({
				title: '请输入店铺名称',
				icon: 'none'
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: '请输入联系人电话',
				icon: 'none'
			})
			return
		}
		if (!shopAddress.address) {
			uni.showToast({
				title: '请选择酒店地址',
				icon: 'none'
			})
			return
		}
		if (!coverImage.value) {
			uni.showToast({
				title: '请上传酒店封面图',
				icon: 'none'
			})
			return
		}
		if (contentImg.length == 0) {
			uni.showToast({
				title: '请上传酒店轮播图',
				icon: 'none'
			})
			return
		}
		let data = {
			shopName: shopName.value,
			shopPhone: phone.value,
			address: shopAddress.address,
			lat: shopAddress.latitude,
			lng: shopAddress.longitude,
			province: shopAddress.province,
			city: shopAddress.city,
			county: shopAddress.district,
			shopType: shopType.value,
			tags: tagList.join(','),
			titleImg: coverImage.value,
			carouselUrl: contentImg.join(',')
		}
		if (shopId.value) {
			data.shopId = shopId.value;
		}
		proxy.$http.postT('/app/shops/save', data).then(res => {
			if (res.code == 0) {
				uni.showToast({
					title: shopId.value ? '修改成功' : '提交成功',
					icon: 'none'
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 500)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	// 选择地址
	const selectCity = () => {
		uni.chooseLocation({
			success: function(res) {
				getCitys(res.latitude, res.longitude, res.name, res.address)
			}
		});
	};
	const getCitys = (latitude, longitude, name, address) => {
		proxy.$http.get('/app/Login/selectCity', {
			lat: latitude,
			lng: longitude
		}).then(res => {
			if (res.code == 0) {
				const data = res.data || {}
				shopAddress.address = name
				shopAddress.latitude = latitude
				shopAddress.longitude = longitude
				shopAddress.province = data.province || ''
				shopAddress.city = data.city || ''
				shopAddress.district = data.district || ''
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	// 删除
	const delImg = (type, index) => {
		if (type == '封面') {
			coverImage.value = ''
		} else {
			contentImg.splice(index, 1);
		}
	};

	const chooseImages = (type) => {
		uni.chooseImage({
			count: 9,
			sourceType: ['album', 'camera'],
			success: res => {
				for (let i = 0; i < res.tempFilePaths.length; i++) {
					uni.showLoading({
						title: "上传中..."
					})
					uni.uploadFile({
						url: config.UPLOADAPI + '/alioss/upload',
						filePath: res.tempFilePaths[i],
						name: 'file',
						success: (uploadFileRes) => {
							uni.hideLoading()
							let url = JSON.parse(uploadFileRes.data).data;
							if (url) {
								if (isImage(url)) {
									if (type == '封面') {
										coverImage.value = url
									} else {
										contentImg.push(url)
									}
								} else {
									uni.showToast({
										title: '不支持上传当前图片格式',
										icon: 'none'
									})
								}
							}
						}
					});
				}
			}
		})
	};

	const isImage = (url) => {
		return url.endsWith('.jpg') || url.endsWith('.jpeg') || url.endsWith('.png') || url.endsWith('.gif') || url
			.endsWith('.JPG') || url.endsWith('.JPEG') || url.endsWith('.PNG') || url.endsWith('.GIF');
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
	.plat-form-field__value--shop-type {
		display: flex;
		justify-content: flex-end;
	}

	.plat-radio-group--shop-type {
		display: flex;
		justify-content: flex-end;
	}
</style>
