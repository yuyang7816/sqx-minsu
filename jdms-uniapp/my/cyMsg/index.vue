<template>
	<view class="plat-page plat-info-page">
		<view class="plat-info-card" v-for="item,index in orderList" :key="item.id || index">
			<view>
				<view class="plat-info-card__name">{{item.infoName||''}}</view>
				<view class="plat-info-card__phone">手机 {{item.infoPhone||''}}</view>
			</view>
			<view class="plat-info-card__foot">
				<view class="flex align-center" v-if="choose == false">
					<up-icon name="checkmark-circle-fill" color="#EEF1F6" size="19" v-if="item.isDefault == 2"></up-icon>
					<up-icon name="checkmark-circle-fill" color="#2C8EFE" size="19" v-if="item.isDefault == 1"></up-icon>
					<view class="f28 margin-left-xs"> 默认</view>
				</view>
				<view class="flex align-center" v-if="choose == true" @click="selUser(item)">
					<up-icon name="checkmark-circle-fill" color="#EEF1F6" size="19" v-if="item.isSel == false"></up-icon>
					<up-icon name="checkmark-circle-fill" color="#2C8EFE" size="19" v-if="item.isSel == true"></up-icon>
					<view class="f28 margin-left-xs"> 选择</view>
				</view>
				<view class="flex align-center">
					<view class="flex align-center margin-right-sm" @click="delUser(item)">
						<up-icon name="trash" color="#64748B" size="19"></up-icon>
						<view class="f28 margin-left-xs" style="color:#64748B;">删除</view>
					</view>
					<view class="flex align-center" @click="openEditPopup(item)">
						<up-icon name="edit-pen" color="#64748B" size="19"></up-icon>
						<view class="f28 margin-left-xs" style="color:#64748B;">编辑</view>
					</view>
				</view>
			</view>
		</view>

		<up-loadmore :status="status" v-if="orderList.length>=10" />
		<up-empty mode="order" style="margin-top: 70%;" v-if="orderList.length == 0" text="暂无常用人信息"></up-empty>

		<view class="plat-fixed-foot">
			<view class="plat-btn" @click="showAddPopup">添加</view>
		</view>

		<up-popup :show="showPopup" @close="closePopup" round="12" :closeable="true">
			<view class="plat-form-card" style="margin: 0; border-radius: 24rpx 24rpx 0 0;">
				<view class="plat-form-head__title text-center" style="padding-bottom: 24rpx;">{{ editId ? '编辑入住人' : '添加入住人' }}</view>
				<view>
					<view class="plat-form-card__title">姓名</view>
					<input type="text" placeholder="请输入姓名" v-model="userName" class="plat-feedback__input" style="margin-bottom: 0;"
						placeholder-style="color:#64748B;" />
				</view>
				<view class="plat-info-card__divider"></view>
				<view>
					<view class="plat-form-card__title">手机</view>
					<input type="text" placeholder="请输入手机号" v-model="userPhone" class="plat-feedback__input" style="margin-bottom: 0;"
						placeholder-style="color:#64748B;" />
				</view>
				<view class="plat-btn" style="margin-top: 40rpx;" @click="saveUser">{{ editId ? '保存' : '添加' }}</view>
			</view>
		</up-popup>
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
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();

	const showPopup = ref(false);
	const page = ref(1);
	const limit = ref(10);
	const totalPages = ref(1);
	const status = ref('loadmore');
	const orderList = reactive([]);
	const userName = ref('');
	const userPhone = ref('');
	const choose = ref(false);
	const preSelectId = ref(null);
	const editId = ref(null);

	const closePopup = () => {
		showPopup.value = false;
		editId.value = null;
		userName.value = '';
		userPhone.value = '';
	};

	const selUser = (item) => {
		item.isSel = !item.isSel
		const seluserInfo = orderList.filter(item => item.isSel == true);
		uni.$emit('selUserInfo', seluserInfo)
		uni.navigateBack()
	};

	const showAddPopup = () => {
		editId.value = null;
		userName.value = '';
		userPhone.value = '';
		showPopup.value = true;
	};

	const openEditPopup = (item) => {
		if (!item || !item.id) {
			uni.showToast({
				title: '信息异常，请刷新后重试',
				icon: 'none'
			});
			return;
		}
		editId.value = item.id;
		userName.value = item.infoName || '';
		userPhone.value = item.infoPhone || '';
		showPopup.value = true;
	};

	const saveUser = () => {
		if (!userName.value) {
			uni.showToast({
				title: '请输入姓名',
				icon: 'none'
			})
			return
		}
		if (!userPhone.value) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		if (userPhone.value.length != 11) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			})
			return
		}
		let data = {
			infoName: userName.value,
			infoPhone: userPhone.value
		}
		let url = '';
		let loadingText = '';
		let successText = '';

		if (!editId.value) {
			url = '/app/userAndInfo/addUserAndInfo'
			loadingText = '添加中...'
			successText = '添加成功'
		} else {
			url = '/app/userAndInfo/updateUserAndInfo'
			data.id = editId.value
			loadingText = '修改中...'
			successText = '修改成功'
		}

		uni.showLoading({
			title: loadingText
		})

		proxy.$http.postJson(url, data).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				closePopup()
				uni.showToast({
					title: successText,
					icon: 'none'
				})
				setTimeout(() => {
					page.value = 1
					getUserList()
				}, 500)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	const delUser = (item) => {
		uni.showModal({
			title: '提示',
			content: '确认删除当前信息吗？',
			success: function(res) {
				if (res.confirm) {
					proxy.$http.postT('/app/userAndInfo/deleteUserAndInfo', {
						id: item.id
					}).then(red => {
						if (red.code == 0) {
							uni.showToast({
								title: '删除成功'
							})
							setTimeout(() => {
								page.value = 1
								getUserList()
							}, 500)
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
				}
			}
		})
	};

	const getUserList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/userAndInfo/getUserAndInfoList', data).then(res => {
			if (res.code == 0) {
				if (choose.value == true) {
					res.data.records.map(item => {
						if (preSelectId.value && preSelectId.value == item.id) {
							item.isSel = true
						} else {
							item.isSel = false
						}
					})
				}
				totalPages.value = res.data.pages
				if (page.value < totalPages.value) {
					status.value = 'loadmore'
				} else {
					status.value = 'nomore'
				}
				if (page.value == 1) {
					orderList.splice(0, orderList.length, ...res.data.records)
				} else {
					orderList.push(...res.data.records)
				}
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};

	onLoad(option => {
		if (option.choose) {
			choose.value = true
		}
		if (option.ydUserId) {
			preSelectId.value = option.ydUserId
		}
	});
	onShow(() => {
		getUserList()
	});
	onPullDownRefresh(() => {
		page.value = 1;
		getUserList();
	});
	onReachBottom(() => {
		if (page.value < totalPages.value) {
			page.value += 1
			status.value = 'loading';
			getUserList();
		}
	});
</script>

<style lang="scss" scoped>
</style>
