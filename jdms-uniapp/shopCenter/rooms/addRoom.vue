<template>
	<view class="plat-page plat-form-page room-add-page">
		<view class="room-add-head">
			<view class="room-add-head__title">{{pageTitle}}</view>
			<view class="room-add-head__desc">{{pageDesc}}</view>
		</view>

		<view v-if="copyFromRoomId && copySourceName" class="room-add-copy-tip">
			已复制「{{copySourceName}}」，请修改房号等信息后保存
		</view>

		<view class="plat-form-card room-add-card">
			<view class="plat-form-card__title room-add-card__title">房间信息</view>
			<view class="plat-form-section">
				<view v-for="item in textInputs" :key="item.key" class="plat-form-field plat-form-field--stack">
					<view class="plat-form-field__label">
						<text class="plat-form-required">*</text>
						<text>{{item.label}}</text>
					</view>
					<view class="plat-form-field__input-wrap">
						<input v-model="formData[item.key]" :type="item.type" placeholder="请填写" class="plat-form-field__input" />
						<text v-if="item.unit" class="plat-form-field__unit">{{item.unit}}</text>
					</view>
				</view>

				<view class="plat-form-field plat-form-field--stack">
					<view class="plat-form-field__label">
						<text class="plat-form-required">*</text>
						<text>可售数量</text>
					</view>
					<view class="plat-form-field__input-wrap">
						<input v-model="formData.roomCount" type="number" placeholder="请输入可售房间数量" class="plat-form-field__input" />
						<text class="plat-form-field__unit">间</text>
					</view>
				</view>

				<view class="plat-form-field" @click="openRoomTypePicker">
					<view class="plat-form-field__label">
						<text class="plat-form-required">*</text>
						<text>房间类型</text>
					</view>
					<view class="plat-form-field__value" :class="roomTypeId ? '' : 'plat-form-field__placeholder'">
						<text>{{roomTypeName||'请选择房间类型'}}</text>
						<up-icon name="arrow-right" color="#C0C4CC" size="24rpx"></up-icon>
					</view>
				</view>

				<view class="plat-form-field plat-form-field--stack" v-for="(item, index) in radioConfigs" :key="item.key">
					<view class="plat-form-field__label">
						<text class="plat-form-required">*</text>
						<text>{{item.label}}</text>
					</view>
					<up-radio-group v-model="formData[item.key]" placement="row">
						<up-radio v-for="(opt, idx) in item.options" :key="idx" :label="opt" :name="String(idx + 1)"></up-radio>
					</up-radio-group>
				</view>

				<view class="plat-form-field" @click="openTimePicker('checkIn')">
					<view class="plat-form-field__label">
						<text class="plat-form-required">*</text>
						<text>最早入住时间</text>
					</view>
					<view class="plat-form-field__value" :class="checkInTime ? '' : 'plat-form-field__placeholder'">
						<text>{{checkInTime||'请选择入住时间'}}</text>
						<up-icon name="arrow-right" color="#C0C4CC" size="24rpx"></up-icon>
					</view>
				</view>
				<view class="plat-form-field" @click="openTimePicker('checkOut')">
					<view class="plat-form-field__label">
						<text class="plat-form-required">*</text>
						<text>最晚退房时间</text>
					</view>
					<view class="plat-form-field__value" :class="checkOutTime ? '' : 'plat-form-field__placeholder'">
						<text>{{checkOutTime||'请选择退房时间'}}</text>
						<up-icon name="arrow-right" color="#C0C4CC" size="24rpx"></up-icon>
					</view>
				</view>

				<view class="plat-form-field">
					<view class="plat-form-field__label">
						<text class="plat-form-required">*</text>
						<text>是否上架</text>
					</view>
					<up-switch v-model="isEnable" activeValue="1" inactiveValue="0" activeColor="#2C8EFE"></up-switch>
				</view>

				<view class="plat-form-field plat-form-field--stack" v-for="item in priceInputs" :key="item.key">
					<view class="plat-form-field__label">
						<text class="plat-form-required">*</text>
						<text>{{item.label}}</text>
					</view>
					<view class="plat-form-field__input-wrap">
						<input v-model="formData[item.key]" type="number" placeholder="请填写" class="plat-form-field__input" />
						<text v-if="item.unit" class="plat-form-field__unit">{{item.unit}}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-form-card room-add-card">
			<view class="plat-form-card__title room-add-card__title">预定须知</view>
			<view v-for="item in textareaConfigs" :key="item.key" class="plat-form-textarea">
				<view class="plat-form-textarea__label">{{item.label}}</view>
				<up-textarea v-model="formData[item.key]" :placeholder="'请输入' + item.label" border="none"
					class="plat-form-textarea__field" placeholder-style="font-size: 26rpx;"></up-textarea>
			</view>
		</view>

		<view class="plat-form-card room-add-card">
			<view class="plat-form-card__title room-add-card__title">房间封面图片</view>
			<view class="plat-form-tip">遵守图片上传规范，杜绝房间下架风险</view>
			<view class="flex align-center flex-wrap room-add-uploads">
				<view class="plat-form-upload" v-if="roomTitleImg">
					<image :src="roomTitleImg" mode="aspectFill" class="plat-form-upload__img"></image>
					<view class="plat-form-upload__del" @click.stop="delImg('封面', 0)">
						<u-icon name="close-circle-fill" color="#2C8EFE" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="plat-form-upload plat-form-upload--add" @click="chooseImages('封面')" v-if="!roomTitleImg">
					<image src="/my/static/xiangji1 (1).png" mode="" class="plat-form-upload__icon"></image>
					<view class="plat-form-upload__text">上传图片</view>
				</view>
			</view>
		</view>

		<view class="plat-form-card room-add-card">
			<view class="plat-form-card__title room-add-card__title">房间轮播图片</view>
			<view class="plat-form-tip">遵守图片上传规范，杜绝房间下架风险</view>
			<view class="flex align-center flex-wrap room-add-uploads">
				<view class="plat-form-upload" v-for="(img, index) in roomImg" :key="index">
					<image :src="img" mode="aspectFill" class="plat-form-upload__img"></image>
					<view class="plat-form-upload__del" @click.stop="delImg('轮播', index)">
						<u-icon name="close-circle-fill" color="#2C8EFE" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="plat-form-upload plat-form-upload--add" @click="chooseImages('轮播')" v-if="roomImg.length < 8">
					<image src="/my/static/xiangji1 (1).png" mode="" class="plat-form-upload__icon"></image>
					<view class="plat-form-upload__text">上传图片</view>
				</view>
			</view>
		</view>

		<view class="plat-sec">
			<view class="flex justify-center align-center">
				<up-checkbox size="14" :customStyle="{marginBottom: '8px'}" labelSize="13" shape="circle" label="我承诺房源真实并同意"
					name="agree" usedAlone v-model:checked="aloneChecked"></up-checkbox>
				<view class="plat-form-agree" @click="routerGo('/package/setting/about?type=509&text=酒店服务协议')">
					《酒店服务协议》
				</view>
			</view>
		</view>

		<view class="plat-detail-spacer"></view>

		<view class="plat-fixed-foot room-add-foot">
			<view class="plat-btn room-add-submit" @click="submit">
				{{roomId ? '保存修改' : '发布'}}
			</view>
		</view>

		<up-datetime-picker :show="showTimePicker" mode="time" @cancel="showTimePicker = false"
			@confirm="confirmTimePicker"></up-datetime-picker>

		<up-picker :show="showRoomTypePicker" :columns="[roomTypeColumns]" keyName="label"
			@cancel="showRoomTypePicker = false" @confirm="confirmRoomType"></up-picker>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		computed
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import config from '@/common/config.js'

	const {
		proxy
	} = getCurrentInstance()

	const roomTitleImg = ref(''); //封面图片
	const roomImg = reactive([]); //轮播图片
	const checkInTime = ref(''); //入住时间
	const checkOutTime = ref(''); //退房时间
	const isEnable = ref('1'); //是否启用0否1是
	const classifyIds = reactive(
		[]
	); //radioConfigs选择的每项的classifyId，给后端传逗号拼接
	const roomId = ref(null);
	const copyFromRoomId = ref(null);
	const copySourceName = ref('');
	const aloneChecked = ref(false);
	const showTimePicker = ref(false);
	const pickerValue = ref(Date.now());
	const currentPickerType = ref(
		''); // 'checkIn' 或 'checkOut'

	const roomTypeId = ref(''); // 房间类型ID
	const roomTypeList = reactive([]); // 房间类型列表
	const showRoomTypePicker = ref(
		false); // 房间类型选择器显示

	// 计算房间类型名称
	const roomTypeName = computed(() => {
		const item = roomTypeList.find(item =>
			item.value == roomTypeId.value
		);
		return item ? item.label : '';
	});

	// 房间类型选择器列数据
	const roomTypeColumns = computed(() => {
		return roomTypeList.map(item => ({
			label: item.label,
			value: item.value
		}));
	});

	// 文本输入配置
	const textInputs = [{
			label: '房间名称',
			key: 'roomName',
			type: 'text'
		},
		{
			label: '房屋面积',
			key: 'floorArea',
			type: 'number',
			unit: 'm²'
		}
		// ,
		// {
		// 	label: '楼层',
		// 	key: 'floor',
		// 	type: 'number',
		// 	unit: '层'
		// }
	]

	// 单选配置
	const radioConfigs = reactive([]);

	// 价格输入
	const priceInputs = [{
			label: '售价',
			key: 'money',
			unit: '元'
		},
		{
			label: '会员价',
			key: 'memberMoney',
			unit: '元'
		}
	]

	// 文本域配置
	const textareaConfigs = [{
			label: '餐食政策',
			key: 'foodPolicy'
		},
		{
			label: '宠物政策',
			key: 'petRule'
		},
		{
			label: '酒店提示',
			key: 'hotelTips'
		}
	]

	// 表单数据
	const formData = reactive({})

	const pageTitle = computed(() => {
		if (roomId.value) return '编辑房间'
		if (copyFromRoomId.value) return '复制并新增'
		return '添加房间'
	})

	const pageDesc = computed(() => {
		if (copyFromRoomId.value) return '已带入原房间信息，修改差异项后保存'
		return '完善房间信息，即可上架售卖'
	})

	// 打开房间类型选择器
	const openRoomTypePicker = () => {
		if (roomTypeList.length === 0) {
			uni.showToast({
				title: '暂无房间类型',
				icon: 'none'
			})
			return
		}
		showRoomTypePicker.value = true;
	};

	// 确认房间类型选择
	const confirmRoomType = (e) => {
		const selected = e.value[0];
		roomTypeId.value = selected.value;
		showRoomTypePicker.value = false;
	};

	// 打开时间选择器
	const openTimePicker = (type) => {
		currentPickerType.value = type;
		// 如果有已选时间，设置默认值
		if (type === 'checkIn' && checkInTime
			.value) {
			const today = new Date();
			const [hours, minutes] = checkInTime
				.value.split(':');
			today.setHours(parseInt(hours),
				parseInt(minutes), 0, 0);
			pickerValue.value = today.getTime();
		} else if (type === 'checkOut' &&
			checkOutTime.value) {
			const today = new Date();
			const [hours, minutes] = checkOutTime
				.value.split(':');
			today.setHours(parseInt(hours),
				parseInt(minutes), 0, 0);
			pickerValue.value = today.getTime();
		} else {
			pickerValue.value = Date.now();
		}
		showTimePicker.value = true;
	};

	// 确认时间选择
	const confirmTimePicker = (e) => {
		const timeValue = e
			.value; // up-datetime-picker 返回格式如 "14:30"
		if (currentPickerType.value ===
			'checkIn') {
			checkInTime.value = timeValue;
		} else {
			checkOutTime.value = timeValue;
		}
		showTimePicker.value = false;
	};

	// 删除图片
	const delImg = (type, index) => {
		if (type == '封面') {
			roomTitleImg.value = '';
		} else {
			roomImg.splice(index, 1);
		}
	};

	// 选择图片
	const chooseImages = (type) => {
		const count = type === '封面' ? 1 : 9;
		uni.chooseImage({
			count: count,
			sourceType: ['album', 'camera'],
			success: res => {
				for (let i = 0; i < res
					.tempFilePaths.length; i++) {
					uni.showLoading({
						title: "上传中..."
					})
					uni.uploadFile({
						url: config.UPLOADAPI +
							'/alioss/upload',
						filePath: res
							.tempFilePaths[i],
						name: 'file',
						success: (
							uploadFileRes) => {
							uni.hideLoading()
							let url = JSON
								.parse(
									uploadFileRes
									.data).data;
							if (url) {
								if (isImage(
										url)) {
									if (type ==
										'封面') {
										roomTitleImg
											.value =
											url;
									} else {
										if (roomImg
											.length < 8
										) {
											roomImg
												.push(
													url);
										}
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
		return url.endsWith('.jpg') || url
			.endsWith('.jpeg') || url.endsWith(
				'.png') || url.endsWith('.gif') ||
			url
			.endsWith('.JPG') || url.endsWith(
				'.JPEG') || url.endsWith('.PNG') ||
			url.endsWith('.GIF');
	};

	// 获取分类列表
	const getClassifyList = () => {
		proxy.$http.get(
			'/app/roomDeviceClassify/getClassifyList', {
				page: 1,
				limit: 999
			}).then(res => {
			if (res.code == 0) {
				radioConfigs.splice(0,
					radioConfigs.length, ...res
					.data.records.map(item => ({
						key: 'classify_' + item
							.classifyId,
						label: item.title,
						classifyId: item
							.classifyId,
						options: item
							.options || ['有',
								'无'
							]
					})));
				if (roomId.value) {
					getRoomInfo()
				} else if (copyFromRoomId.value) {
					getRoomInfo()
				}
				console.log(radioConfigs,
					'radioConfigs')
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	// 获取房间类型列表
	const getRoomTypeList = () => {
		proxy.$http.get(
			'/app/roomType/getRoomTypeList', {
				page: 1,
				limit: 999
			}).then(res => {
			if (res.code == 0) {
				roomTypeList.splice(0,
					roomTypeList.length, ...res
					.data.records.map(item => ({
						value: item.id,
						label: item.typeName
					})))
				console.log(roomTypeList,
					"roomTypeList")
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	// 获取房间详情回显
	const getRoomInfo = () => {
		const params = roomId.value ? {
			roomId: roomId.value
		} : {
			copyFromRoomId: copyFromRoomId.value
		}
		proxy.$http.get('/app/shopsRoom/getShopRoomInfo', params).then(res => {
			if (res.code == 0 && res.data) {
				const data = res.data;
				if (copyFromRoomId.value && !roomId.value) {
					copySourceName.value = data.copySourceRoomName || data.roomName || '';
				}
				// 回显文本输入
				formData.roomName = copyFromRoomId.value && !roomId.value ? '' : (data.roomName || '');
				formData.roomCount = data.roomCount || 1;
				formData.floorArea = data.floorArea || '';
				formData.floor = data.floor || '';
				// 回显价格
				formData.money = data.money || '';
				formData.memberMoney = data.memberMoney || '';
				// 回显时间
				checkInTime.value = data.checkInTime || '';
				checkOutTime.value = data.checkOutTime || '';
				// 回显启用状态
				isEnable.value = String(data
					.isEnable ?? '1');
				// 回显textarea
				formData.foodPolicy = data
					.foodPolicy || '';
				formData.petRule = data
					.petRule || '';
				formData.hotelTips = data
					.hotelTips || '';
				// 回显图片
				roomTitleImg.value = data
					.roomTitleImg || '';
				if (data.roomImg) {
					const imgs = data.roomImg
						.split(',');
					roomImg.splice(0, roomImg
						.length, ...imgs);
				}
				// 回显radio
				if (data.classifyIds) {
					const ids = data.classifyIds
						.split(',');
					radioConfigs.forEach(item => {
						if (ids.includes(String(
								item.classifyId))) {
							formData[item.key] =
								'1';
						} else {
							formData[item.key] =
								'2';
						}
					});
				}
				// 回显房间类型
				roomTypeId.value = data
					.roomTypeId || '';
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	}

	// 发布/修改
	const submit = () => {
		// 校验房间名称
		if (!formData.roomName) {
			uni.showToast({
				title: copyFromRoomId.value ? '请填写新的房间名称' : '请填写房间名称',
				icon: 'none'
			})
			return
		}
		const roomCountNum = Number(formData.roomCount)
		if (!roomCountNum || roomCountNum < 1) {
			uni.showToast({
				title: '可售数量至少为 1 间',
				icon: 'none'
			})
			return
		}
		// 校验房屋面积
		if (!formData.floorArea || formData
			.floorArea <= 0) {
			uni.showToast({
				title: '请填写房屋面积',
				icon: 'none'
			})
			return
		}
		// 校验楼层
		// if (!formData.floor || formData.floor <=
		// 	0) {
		// 	uni.showToast({
		// 		title: '请填写楼层',
		// 		icon: 'none'
		// 	})
		// 	return
		// }
		// 校验房间类型
		if (!roomTypeId.value) {
			uni.showToast({
				title: '请选择房间类型',
				icon: 'none'
			})
			return
		}
		// 校验单选配置
		for (let i = 0; i < radioConfigs
			.length; i++) {
			const item = radioConfigs[i];
			if (!formData[item.key]) {
				uni.showToast({
					title: '请选择' + item.label,
					icon: 'none'
				})
				return
			}
		}
		// 校验入住时间
		if (!checkInTime.value) {
			uni.showToast({
				title: '请选择入住时间',
				icon: 'none'
			})
			return
		}
		// 校验退房时间
		if (!checkOutTime.value) {
			uni.showToast({
				title: '请选择退房时间',
				icon: 'none'
			})
			return
		}
		// 校验售价
		if (!formData.money || formData.money <=
			0) {
			uni.showToast({
				title: '请填写售价',
				icon: 'none'
			})
			return
		}
		// 校验会员价
		if (!formData.memberMoney || formData
			.memberMoney <= 0) {
			uni.showToast({
				title: '请填写会员价',
				icon: 'none'
			})
			return
		}
		// 校验餐食政策
		if (!formData.foodPolicy) {
			uni.showToast({
				title: '请输入餐食政策',
				icon: 'none'
			})
			return
		}
		// 校验宠物政策
		if (!formData.petRule) {
			uni.showToast({
				title: '请输入宠物政策',
				icon: 'none'
			})
			return
		}
		// 校验酒店提示
		if (!formData.hotelTips) {
			uni.showToast({
				title: '请输入酒店提示',
				icon: 'none'
			})
			return
		}
		// 校验封面图
		if (!roomTitleImg.value) {
			uni.showToast({
				title: '请上传房间封面图',
				icon: 'none'
			})
			return
		}
		// 校验轮播图
		if (roomImg.length == 0) {
			uni.showToast({
				title: '请上传房间轮播图',
				icon: 'none'
			})
			return
		}
		// 校验协议
		if (!aloneChecked.value) {
			uni.showToast({
				title: '请同意协议',
				icon: 'none'
			})
			return
		}

		// 收集classifyIds
		const selectedClassifyIds = [];
		radioConfigs.forEach(item => {
			if (formData[item.key] === '1' ||
				formData[item.key] === 1) {
				selectedClassifyIds.push(item
					.classifyId);
			}
		});

		uni.showLoading({
			title: roomId.value ? '修改中...' : '添加中...'
		})

		// 组装数据
		let data = {
			roomName: formData.roomName,
			roomCount: Number(formData.roomCount) || 1,
			floorArea: formData.floorArea,
			floor: formData.floor,
			money: formData.money,
			memberMoney: formData.memberMoney,
			checkInTime: checkInTime.value,
			checkOutTime: checkOutTime.value,
			isEnable: isEnable.value,
			foodPolicy: formData.foodPolicy,
			petRule: formData.petRule,
			hotelTips: formData.hotelTips,
			roomTitleImg: roomTitleImg.value,
			roomImg: roomImg.join(','),
			classifyIds: selectedClassifyIds
				.join(','),
			shopId: uni.getStorageSync('shopId'),
			roomTypeId: roomTypeId.value
		}

		if (roomId.value) {
			data.roomId = roomId.value;
		}

		proxy.$http.postJson(
				'/app/shopsRoom/saveShopRoom', data)
			.then(res => {
				uni.hideLoading()
				if (res.code == 0) {
					const savedRoomName = formData.roomName
					if (!roomId.value && copyFromRoomId.value) {
						uni.showModal({
							title: '添加成功',
							content: `已添加「${savedRoomName}」`,
							confirmText: '继续添加相似房间',
							cancelText: '返回列表',
							success: (modalRes) => {
								if (modalRes.confirm) {
									formData.roomName = ''
								} else {
									uni.navigateBack()
								}
							}
						})
						return
					}
					uni.showToast({
						title: roomId.value ? '修改成功' : (res.msg || '添加成功'),
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
	}
	const routerGo = (url) => {
		uni.navigateTo({
			url
		})
	}
	onLoad((option) => {
		if (option.roomId) {
			roomId.value = option.roomId
		}
		if (option.copyFromRoomId) {
			copyFromRoomId.value = option.copyFromRoomId
		}
		getRoomTypeList()
		getClassifyList()
	})
</script>

<style lang="scss" scoped>
	/* 页面头 */
	.room-add-page {
		background: linear-gradient(180deg, #E8F3FF 0%, #F4F6FA 240rpx);
	}

	.room-add-head {
		padding: 22rpx 22rpx 20rpx;
		margin: 24rpx 24rpx 0;
		border-radius: 20rpx;
		background: linear-gradient(135deg, #2B6FE8 0%, #4B9DFF 100%);
		box-shadow: 0 10rpx 24rpx rgba(43, 111, 232, 0.14);
	}

	.room-add-head__title {
		font-size: 34rpx;
		font-weight: 700;
		color: #ffffff;
		line-height: 1.25;
	}

	.room-add-head__desc {
		margin-top: 8rpx;
		font-size: 22rpx;
		color: rgba(255, 255, 255, 0.82);
		line-height: 1.5;
	}

	.room-add-copy-tip {
		margin: 0 24rpx 16rpx;
		padding: 18rpx 22rpx;
		border-radius: 16rpx;
		background: #fff7e6;
		color: #ad6800;
		font-size: 24rpx;
		line-height: 1.5;
	}

	/* 卡片 */
	.room-add-card {
		margin-top: 0;
		padding: 26rpx 22rpx;
		background: #FFFFFF;
		border-radius: var(--plat-radius-card);
		box-shadow: 0 8rpx 24rpx rgba(15, 23, 42, 0.04);
	}

	.room-add-card__title {
		font-size: 28rpx;
		font-weight: 700;
		margin-bottom: 6rpx;
		padding-bottom: 16rpx;
		border-bottom: 1rpx solid var(--plat-line);
	}

	/* 必填红点 */
	.plat-form-required {
		margin-left: 0;
		margin-right: 4rpx;
	}

	/* 纵向表单项 */
	.plat-form-field--stack {
		align-items: stretch;
		padding: 20rpx 0;
	}

	.plat-form-field--stack .plat-form-field__label {
		margin-bottom: 12rpx;
		font-size: 26rpx;
		font-weight: 500;
		color: var(--plat-text);
	}

	.plat-form-field__input-wrap {
		display: flex;
		align-items: center;
		gap: 12rpx;
		flex: 1;
		min-width: 0;
		background: #F8FAFD;
		border-radius: 14rpx;
		padding: 16rpx 20rpx;
		border: 1rpx solid transparent;
		transition: border-color 0.2s;
	}

	.plat-form-field__input-wrap:focus-within {
		border-color: var(--plat-brand);
	}

	.plat-form-field__input {
		flex: 1;
		min-width: 0;
		text-align: left;
		font-size: 26rpx;
		color: var(--plat-text);
		height: 40rpx;
		line-height: 40rpx;
	}

	.plat-form-field__unit {
		flex-shrink: 0;
		font-size: 24rpx;
		color: var(--plat-text-sub);
	}

	/* 行内选择项（房间类型、时间、开关） */
	.plat-form-field:not(.plat-form-field--stack) {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 24rpx;
		padding: 22rpx 0;
	}

	.plat-form-field:not(.plat-form-field--stack) .plat-form-field__label {
		display: flex;
		align-items: center;
		font-size: 26rpx;
		font-weight: 500;
		color: var(--plat-text);
	}

	.plat-form-field__value {
		display: flex;
		align-items: center;
		justify-content: flex-end;
		gap: 10rpx;
		flex: 1;
		min-width: 0;
		font-size: 26rpx;
		color: var(--plat-text);
	}

	.plat-form-field__placeholder {
		color: var(--plat-text-sub);
	}

	/* 单选项改为胶囊标签 */
	.plat-form-field--stack :deep(.u-radio-group) {
		display: flex;
		flex-wrap: wrap;
		gap: 12rpx;
	}

	.plat-form-field--stack :deep(.u-radio) {
		margin: 0 !important;
		padding: 10rpx 18rpx;
		background: #F4F6FA;
		border-radius: 999rpx;
		border: 1rpx solid var(--plat-line);
		align-items: center;
		min-height: 56rpx;
	}

	.plat-form-field--stack :deep(.u-radio__icon-wrap) {
		margin-right: 8rpx !important;
		width: 28rpx !important;
		height: 28rpx !important;
		flex-shrink: 0;
	}

	.plat-form-field--stack :deep(.u-radio__icon-wrap__icon) {
		font-size: 20rpx !important;
	}

	.plat-form-field--stack :deep(.u-radio__text) {
		font-size: 24rpx !important;
		color: var(--plat-text) !important;
	}

	/* 文本域 */
	.plat-form-textarea {
		margin-top: 18rpx;
	}

	.plat-form-textarea__label {
		font-size: 26rpx;
		font-weight: 500;
		color: var(--plat-text);
		margin-bottom: 12rpx;
	}

	.plat-form-textarea__field {
		background: #F8FAFD !important;
		border-radius: 14rpx !important;
		padding: 16rpx 20rpx !important;
	}

	.plat-form-textarea__field :deep(.u-textarea__field) {
		font-size: 26rpx !important;
		color: var(--plat-text) !important;
		line-height: 1.6 !important;
	}

	/* 上传图片 */
	.room-add-uploads {
		display: grid !important;
		grid-template-columns: repeat(2, minmax(0, 1fr));
		gap: 16rpx;
		align-items: stretch;
	}

	.plat-form-upload {
		width: 100%;
		height: 0;
		padding-bottom: 100%;
		border-radius: 18rpx;
		margin: 0;
		position: relative;
		overflow: hidden;
		background: #F8FAFD;
		border: 1rpx dashed var(--plat-line);
	}

	.plat-form-upload__img {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		width: 100%;
		height: 100%;
		border-radius: 18rpx;
	}

	.plat-form-upload--add {
		position: relative;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding-bottom: 100%;
	}

	.plat-form-upload__icon {
		position: absolute;
		width: 52rpx;
		height: 52rpx;
		margin: 0;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -70%);
	}

	.plat-form-upload__text {
		position: absolute;
		left: 50%;
		bottom: 26%;
		transform: translateX(-50%);
		font-size: 22rpx;
		color: var(--plat-text-sub);
		white-space: nowrap;
	}

	.plat-form-upload__del {
		position: absolute;
		top: 8rpx;
		right: 8rpx;
		width: 44rpx;
		height: 44rpx;
		border-radius: 50%;
		background: #FFFFFF;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.08);
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.plat-form-tip {
		font-size: 22rpx;
		color: var(--plat-text-sub);
		margin: 6rpx 0 16rpx;
	}

	/* 协议 */
	.plat-sec {
		margin: 20rpx 24rpx;
	}

	.plat-form-agree {
		font-size: 22rpx;
		color: var(--plat-brand);
		font-weight: 500;
	}

	/* 底部 */
	.room-add-foot {
		background: #FFFFFF;
		padding: 16rpx 24rpx;
		padding-bottom: calc(16rpx + env(safe-area-inset-bottom));
		box-shadow: 0 -4rpx 18rpx rgba(15, 23, 42, 0.05);
	}

	.room-add-submit {
		// height: 84rpx;
		font-size: 30rpx;
		font-weight: 700;
		border-radius: 999rpx;
		box-shadow: 0 8rpx 18rpx rgba(43, 111, 232, 0.18);
	}

	.plat-detail-spacer {
		height: 150rpx;
	}
</style>
