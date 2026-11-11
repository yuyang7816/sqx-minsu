<template>
	<view class="plat-page plat-form-page">
		<view class="plat-form-head">
			<view class="plat-form-head__title">个人信息</view>
			<view class="plat-form-head__hint">完善资料，便于预订与联系</view>
		</view>

		<view class="plat-form-card">
			<view class="plat-form-section">
				<view class="plat-form-field">
					<view class="plat-form-field__label">头像</view>
					<view class="plat-form-field__value">
						<!-- #ifdef MP-WEIXIN -->
						<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar" class="avatar-btn">
							<image :src="avatar?avatar:'../../static/logo.png'" mode="" class="avatar-img"></image>
						</button>
						<!-- #endif -->
						<!-- #ifndef MP-WEIXIN -->
						<image @click="uploadImg()" :src="avatar?avatar:'/static/logo.png'" mode="" class="avatar-img"></image>
						<!-- #endif -->
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">用户名</view>
					<view class="plat-form-field__value">
						<input v-model="userName" type="nickname" placeholder="请输入用户名" class="plat-form-field__input" />
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">年龄</view>
					<view class="plat-form-field__value">
						<input v-model="age" class="plat-form-field__input" />
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">手机</view>
					<view class="plat-form-field__value">
						<input :disabled="true" v-model="phone" placeholder="请输入联系电话" class="plat-form-field__input" />
					</view>
				</view>
				<view class="plat-form-field">
					<view class="plat-form-field__label">性别</view>
					<view class="plat-form-field__value plat-form-field__value--gender">
						<up-radio-group v-model="sex" class="gender-radio-group">
							<up-radio shape="circle" active-color="#2c8efe" :name="1" label="男"></up-radio>
							<up-radio shape="circle" active-color="#2c8efe" :name="2" label="女"></up-radio>
						</up-radio-group>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot">
			<view class="plat-btn" @click="messagebtn()">保存</view>
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
	import config from '../../common/config.js'
	const {
		proxy
	} = getCurrentInstance();
	const avatar = ref(''); //头像
	const userName = ref(''); //用户名
	const age = ref(0); //年龄
	const phone = ref(''); //手机号
	const sex = ref(1); //性别
	onLoad(option => {
		getUserInfo();
	});
	const getUserInfo = () => {
		proxy.$http.getT("/app/user/selectUserById").then(res => {
			if (res.code == 0) {
				uni.setStorageSync('avatar', res.data.avatar);
				uni.setStorageSync('userId', res.data.userId);
				uni.setStorageSync('userName', res.data.userName);
				uni.setStorageSync('phone', res.data.phone);
				uni.setStorageSync('age', res.data.age);
				sex.value = res.data.sex
				age.value = res.data.age
				phone.value = res.data.phone;
				avatar.value = res.data.avatar;
				userName.value = res.data.userName;
				if (userName.value == null) {
					userName.value = res.data.nickName;
				} else {
					userName.value = res.data.userName;
				}
			}
			uni.hideLoading();
		});
	};
	const messagebtn = () => {
		if (!userName.value) {
			uni.showToast({
				title: "用户名不能为空",
				icon: "none"
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: "联系电话不能为空",
				icon: "none"
			})
			return
		}
		uni.showModal({
			title: '温馨提示',
			content: '确定保存信息',
			success: e => {
				if (e.confirm) {
					proxy.$http.postJson("/app/user/updateUser", {
						userName: userName.value,
						avatar: avatar.value,
						phone: phone.value,
						sex: sex.value,
						age: age.value
					}).then(res => {
						if (res.code === 0) {
							uni.showToast({
								title: '保存成功',
								icon: "none"
							})
							setTimeout(function() {
								uni.navigateBack()
							}, 1000)
						} else {
							uni.showToast({
								title: res.msg,
								icon: "none"
							})
						}
					})
				}
			}
		});
	};
	//上传头像
	const uploadImg = () => {
		var url = null;
		uni.showActionSheet({
			// itemList按钮的文字接受的是数组
			itemList: ["查看头像", "从相册选择图片"],
			success(e) {
				var index = e.tapIndex
				if (index === 0) {
					// 用户点击了预览当前图片
					// 可以自己实现当前头像链接的读取
					let url = avatar.value;
					let arr = []
					arr.push(url)
					uni.previewImage({
						// 预览功能图片也必须是数组的
						urls: arr
					})
				} else if (index === 1) {
					uni.chooseImage({
						count: 1, //默认9
						sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
						sourceType: ['album'], //从相册选择
						success: function(res) {
							uni.showLoading({
								title: '上传中...'
							});
							let token = uni.getStorageSync('token');
							let userId = uni.getStorageSync('userId');
							uni.uploadFile({
								// url: 'https://peizhen.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
								url: config.UPLOADAPI + '/alioss/upload',
								filePath: res.tempFilePaths[0],
								header: {
									token: uni.getStorageSync('token')
								},
								name: 'file',
								success: uploadFileRes => {
									url = JSON.parse(uploadFileRes.data);
									avatar.value = url.data
									uni.hideLoading();
								}
							});
						}
					});
				}
			}
		})
	};
	// 微信头像能力
	const onChooseAvatar = (e) => {
		uni.showLoading({
			title: '上传中...'
		});
		uni.uploadFile({
			// url: config.APIHOST1 + '/alioss/upload', //仅为示例，非真实的接口地址
			url: config.UPLOADAPI + '/alioss/upload', //仅为示例，非真实的接口地址
			filePath: e.detail.avatarUrl,
			header: {
				token: uni.getStorageSync('token')
			},
			name: 'file',
			success: uploadFileRes => {
				let url = JSON.parse(uploadFileRes.data).data;
				proxy.$http.postT('/app/user/updateUserImageUrl?avatar=' + url).then(res => {
					uni.hideLoading();
					if (res.code === 0) {
						uni.showToast({
							title: '更新成功',
							icon: 'none'
						})
						getUserInfo()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			}
		});
	};
</script>

<style lang="scss">
	button {
		padding: 0 !important;
	}

	button::after {
		border: none;
	}

	.avatar-btn {
		width: 111rpx;
		height: 111rpx;
		border-radius: 50%;
		background: transparent;
	}

	.avatar-img {
		width: 111rpx;
		height: 111rpx;
		border-radius: 50%;
	}

	.plat-form-field__value--gender {
		display: flex;
		justify-content: flex-end;
	}

	.gender-radio-group {
		display: flex;
		justify-content: flex-end;
	}
</style>
