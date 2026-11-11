<template>
	<view class="plat-detail-page plat-order-detail-page">
		<view class="plat-order-status">
			<view class="plat-order-status__main">

				<view class="plat-order-status__title">{{ orderStatusText(orderInfo.orderStatus) || '订单详情' }}</view>
				<view class="plat-order-status__desc">{{ orderStatusText1(orderInfo.orderStatus) }}</view>
			</view>
			<view class="plat-order-status__badge">履约详情</view>
		</view>

		<view class="plat-detail-section plat-detail-section--first">
			<view class="plat-card plat-order-hotel">
				<view class="plat-order-hotel__head">
					<view class="plat-order-hotel__text">
						<view class="plat-order-hotel__type">{{ orderInfo.orderType == 1 ? '酒店订单' : '民宿订单' }}</view>
						<view class="plat-cover-title">{{ orderInfo.roomName || '' }}</view>
						<view class="plat-cover-sub plat-order-hotel__sub">{{ orderInfo.classifyNames || '暂无房型信息' }}</view>
						<view class="plat-order-hotel__chips">
							<view class="plat-order-hotel__chip">房间数 {{ orderInfo.roomCount || '暂无' }}</view>
							<view class="plat-order-hotel__chip">住客 {{ orderInfo.realName || '暂无' }}</view>
						</view>
					</view>
					<image :src="orderInfo.coverImage || '/static/logo.png'" mode="aspectFill" class="plat-order-hotel__cover">
					</image>
				</view>

				<view class="plat-order-stay" v-if="orderInfo.startDate || orderInfo.endDate">
					<view class="plat-order-stay__item">
						<view class="plat-order-stay__val">{{ formatDate(orderInfo.startDate) || '--' }}</view>
						<view class="plat-order-stay__label">入住时间</view>
					</view>
					<view class="plat-order-stay__dash-wrap">
						<view class="plat-order-stay__dash"></view>
						<view class="plat-order-stay__nights">{{ getStayNights(orderInfo.startDate, orderInfo.endDate) }}</view>
						<view class="plat-order-stay__dash"></view>
					</view>
					<view class="plat-order-stay__item plat-order-stay__item--end">
						<view class="plat-order-stay__val">{{ formatDate(orderInfo.endDate) || '--' }}</view>
						<view class="plat-order-stay__label">离店时间</view>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-detail-section">
			<view class=" plat-order-block">
				<view class="plat-order-block__head">
					<view class="plat-form-card__title">入住信息</view>
					<view class="plat-order-block__hint">核验入住人身份与联系方式后再办理入住</view>
				</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">姓名</view>
					<view class="plat-order-info-row__value">{{ orderInfo.realName || '暂无' }}</view>
				</view>
				<view class="plat-order-info-row plat-order-info-row--action" @click="makePhoneCall(orderInfo.phone)">
					<view class="plat-order-info-row__label">联系方式</view>
					<view class="plat-order-info-row__copy">
						<text>{{ hideMiddleDigits(orderInfo.phone) || '暂无' }}</text>
						<up-icon name="phone" color="#64748B" size="18"></up-icon>
					</view>
				</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">房间数量</view>
					<view class="plat-order-info-row__value">{{ orderInfo.roomCount || '暂无' }}</view>
				</view>
			</view>
		</view>

		<view class="plat-detail-section">
			<view class=" plat-order-block">
				<view class="plat-order-block__head">
					<view class="plat-form-card__title">订单信息</view>
					<view class="plat-order-block__hint">支持复制订单号，便于客服或前台协同处理</view>
				</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">订单金额</view>
					<view class="plat-order-info-row__value plat-order-info-row__value--price">
						￥{{ orderInfo.orderMoney || '0.00' }}</view>
				</view>
				<view class="plat-order-info-row" v-if="orderInfo.couponId && Number(orderInfo.couponMoney) > 0">
					<view class="plat-order-info-row__label">优惠券</view>
					<view class="plat-order-info-row__value plat-order-info-row__value--discount plat-order-info-row__coupon">
						<text class="plat-order-info-row__coupon-name">{{ orderInfo.couponName || '优惠券' }}</text>
						<text class="plat-order-info-row__coupon-amount">-￥{{ orderInfo.couponMoney }}</text>
					</view>
				</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">实付金额</view>
					<view class="plat-order-info-row__value plat-order-info-row__value--price">
						￥{{ orderInfo.payMoney || '0.00' }}</view>
				</view>
				<view class="plat-order-info-row plat-order-info-row--action" @click="copyClick(orderInfo.orderNumber)">
					<view class="plat-order-info-row__label">订单编号</view>
					<view class="plat-order-info-row__copy">
						<text>{{ orderInfo.orderNumber || '暂无' }}</text>
						<up-icon name="order" color="#64748B" size="18"></up-icon>
					</view>
				</view>
				<view class="plat-order-info-row">
					<view class="plat-order-info-row__label">下单时间</view>
					<view class="plat-order-info-row__value">{{ orderInfo.createTime || '暂无' }}</view>
				</view>
			</view>
		</view>

		<view class="plat-detail-spacer"></view>
		<view class="plat-fixed-foot plat-order-detail-foot"
			v-if="orderInfo.orderStatus == 1 || orderInfo.orderStatus == 2">
			<view class="plat-order-detail-actions">
				<view class="plat-order-detail-actions__text">
					<view class="plat-order-detail-actions__title">{{ orderInfo.orderStatus == 1 ? '准备办理入住' : '准备完成退房' }}</view>
					<view class="plat-order-detail-actions__desc">{{ orderInfo.orderStatus == 1 ? '到店后可直接核销订单' : '确认住客离店后再提交退房' }}
					</view>
				</view>
				<view class="plat-order-detail-actions__buttons">
					<view class="plat-btn plat-btn--sm" v-if="orderInfo.orderStatus == 1"
						@click.stop="routerGo('/shopCenter/order/sacnCode?orderNumber=' + orderInfo.orderNumber)">去核销</view>
					<view class="plat-btn plat-btn--sm" v-if="orderInfo.orderStatus == 2" @click.stop="finishOrder()">确认退房</view>
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
		onReachBottom
	} from '@dcloudio/uni-app';

	const {
		proxy
	} = getCurrentInstance();

	const orderInfo = reactive({});
	const orderId = ref('');

	const routerGo = (url) => {
		uni.navigateTo({
			url
		});
	};

	const finishOrder = () => {
		uni.showModal({
			title: '提示',
			content: '确认用户已退房吗？',
			success: function(res) {
				if (res.confirm) {
					proxy.$http.postT('/app/orders/staffFinishOrder', {
						orderId: orderId.value
					}).then(red => {
						if (red.code == 0) {
							uni.showToast({
								title: '退房成功',
								icon: 'none'
							});
							setTimeout(() => {
								getOrderDetail();
							});
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							});
						}
					});
				}
			}
		});
	};

	const getOrderDetail = () => {
		let data = {
			orderId: orderId.value,
			shopId: uni.getStorageSync('shopId')
		};
		uni.showLoading({
			title: '加载中...'
		});
		proxy.$http.get('/app/shops/selectOrderById', data).then(res => {
			uni.hideLoading();
			uni.stopPullDownRefresh();
			if (res.code == 0) {
				Object.assign(orderInfo, res.data);
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		}).catch(() => {
			uni.hideLoading();
			uni.stopPullDownRefresh();
		});
	};

	const copyClick = (title) => {
		if (!title) {
			return;
		}
		uni.setClipboardData({
			data: title,
			success: function() {
				uni.showToast({
					title: '复制成功',
					icon: 'none'
				});
			}
		});
	};

	const makePhoneCall = (phone) => {
		if (!phone) {
			return;
		}
		uni.makePhoneCall({
			phoneNumber: phone
		});
	};

	const isPhoneNumber = (phone) => {
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone || '');
	};

	const hideMiddleDigits = (phone) => {
		if (!phone) {
			return '';
		}
		if (isPhoneNumber(phone)) {
			return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
		}
		return phone;
	};

	const formatDate = (dateStr) => {
		if (!dateStr) {
			return '';
		}
		const date = new Date(dateStr.replace(/-/g, '/'));
		if (Number.isNaN(date.getTime())) {
			return '';
		}
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${month}月${day}日`;
	};

	const formatDateTimeDate = (dateTimeStr) => {
		if (!dateTimeStr) {
			return '';
		}
		const parts = String(dateTimeStr).split(' ');
		return parts[0] || '';
	};

	const formatDateTimeTime = (dateTimeStr) => {
		if (!dateTimeStr) {
			return '';
		}
		const parts = String(dateTimeStr).split(' ');
		return parts[1] || '';
	};

	const getStayNights = (startDate, endDate) => {
		if (!startDate || !endDate) {
			return '--';
		}
		const start = new Date(startDate.replace(/-/g, '/'));
		const end = new Date(endDate.replace(/-/g, '/'));
		if (Number.isNaN(start.getTime()) || Number.isNaN(end.getTime())) {
			return '--';
		}
		const diff = Math.round((end - start) / (24 * 60 * 60 * 1000));
		return diff > 0 ? `${diff}晚` : '--';
	};

	const orderStatusText = (val) => {
		switch (val) {
			case 0:
				return '待支付';
			case 1:
				return '待核销';
			case 2:
				return '进行中';
			case 3:
				return '已完成';
			case 4:
				return '已取消';
			default:
				return '订单详情';
		}
	};

	const orderStatusText1 = (val) => {
		switch (val) {
			case 0:
				return '请尽快完成支付，超时未支付订单将自动取消';
			case 1:
				return '订单已支付成功，请到店后完成核销办理入住';
			case 2:
				return '住客当前在住中，请按实际情况完成退房处理';
			case 3:
				return '订单已完成，本次履约流程已结束';
			case 4:
				return '订单已取消，如需入住请重新创建订单';
			default:
				return '查看订单详情与履约信息';
		}
	};

	onLoad(option => {
		if (option.orderId) {
			orderId.value = option.orderId;
		}
	});

	onShow(() => {
		if (orderId.value) {
			getOrderDetail();
		}
	});

	onPullDownRefresh(() => {
		if (orderId.value) {
			getOrderDetail();
		} else {
			uni.stopPullDownRefresh();
		}
	});

	onReachBottom(() => {});
</script>

<style lang="scss" scoped>
	page {
		background: #f3f6fb;
	}

	.plat-order-detail-page {
		min-height: 100vh;
		padding-bottom: calc(160rpx + env(safe-area-inset-bottom));
		background: linear-gradient(180deg, #dbeafe 0, #eef3f8 240rpx, #f8fafc 100%);
	}

	.plat-detail-section {
		// margin: 20rpx 24rpx 0;
	}

	.plat-detail-section--first {
		margin-top: 0;
	}

	.plat-order-status {
		padding: 28rpx 24rpx 22rpx;
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 18rpx;
	}

	.plat-order-status__main {
		flex: 1;
		min-width: 0;
	}

	.plat-order-status__eyebrow {
		font-size: 20rpx;
		font-weight: 600;
		color: #2563eb;
	}

	.plat-order-status__title {
		margin-top: 10rpx;
		font-size: 44rpx;
		font-weight: 700;
		line-height: 1.15;
		color: #FFFFFF;
	}

	.plat-order-status__desc {
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.55;
		color: #FFFFFF;
	}

	.plat-order-status__badge {
		flex-shrink: 0;
		height: 50rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		background: rgba(255, 255, 255, 0.92);
		box-shadow: 0 10rpx 30rpx rgba(37, 99, 235, 0.12);
		font-size: 22rpx;
		font-weight: 600;
		color: #1d4ed8;
		display: inline-flex;
		align-items: center;
	}

	.plat-order-hotel,
	.plat-order-block {
		padding: 22rpx;
		border-radius: 26rpx;
		background: rgba(255, 255, 255, 0.98);
		border: 1rpx solid rgba(226, 232, 240, 0.9);
		box-shadow: 0 12rpx 30rpx rgba(15, 23, 42, 0.05);
	}

	.plat-order-hotel__head {
		display: flex;
		align-items: center;
		gap: 18rpx;
	}

	.plat-order-hotel__text {
		flex: 1;
		min-width: 0;
	}

	.plat-order-hotel__type {
		display: inline-flex;
		align-items: center;
		height: 42rpx;
		padding: 0 14rpx;
		border-radius: 999rpx;
		background: #eff6ff;
		font-size: 21rpx;
		font-weight: 600;
		color: #1d4ed8;
	}

	.plat-order-hotel__sub {
		margin-top: 8rpx;
	}

	.plat-order-hotel__chips {
		display: flex;
		flex-wrap: wrap;
		gap: 8rpx;
		margin-top: 12rpx;
	}

	.plat-order-hotel__chip {
		height: 38rpx;
		padding: 0 12rpx;
		border-radius: 12rpx;
		background: #f8fafc;
		font-size: 21rpx;
		color: #475569;
		display: inline-flex;
		align-items: center;
		max-width: 100%;
	}

	.plat-order-hotel__cover {
		width: 132rpx;
		height: 132rpx;
		border-radius: 18rpx;
		flex-shrink: 0;
	}

	.plat-order-stay {
		margin-top: 18rpx;
		padding: 18rpx 20rpx;
		border-radius: 22rpx;
		background: linear-gradient(135deg, #f8fbff, #f1f6ff);
		border: 1rpx solid rgba(219, 234, 254, 0.9);
		display: flex;
		align-items: center;
		gap: 16rpx;
	}

	.plat-order-stay__item {
		flex: 1;
		min-width: 0;
	}

	.plat-order-stay__item--end {
		text-align: right;
	}

	.plat-order-stay__val {
		font-size: 28rpx;
		font-weight: 700;
		color: #0f172a;
	}

	.plat-order-stay__label {
		margin-top: 6rpx;
		font-size: 22rpx;
		color: #64748b;
	}

	.plat-order-stay__dash-wrap {
		width: 128rpx;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		gap: 8rpx;
	}

	.plat-order-stay__dash {
		flex: 1;
		height: 2rpx;
		background: rgba(37, 99, 235, 0.18);
	}

	.plat-order-stay__nights {
		flex-shrink: 0;
		font-size: 20rpx;
		font-weight: 600;
		color: #2563eb;
	}

	.plat-order-block__head {
		margin-bottom: 10rpx;
	}

	.plat-order-block__hint {
		margin-top: 8rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: #64748b;
	}

	.plat-form-card__title {
		font-size: 30rpx;
		font-weight: 700;
		color: #0f172a;
	}

	.plat-order-info-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 20rpx;
		min-height: 86rpx;
		padding: 0 2rpx;
		border-bottom: 1rpx solid #eef2f7;
	}

	.plat-order-info-row:last-child {
		border-bottom: 0;
	}

	.plat-order-info-row--action {
		cursor: pointer;
	}

	.plat-order-info-row__label {
		flex-shrink: 0;
		font-size: 24rpx;
		color: #64748b;
	}

	.plat-order-info-row__value,
	.plat-order-info-row__copy {
		min-width: 0;
		font-size: 24rpx;
		font-weight: 600;
		line-height: 1.5;
		color: #0f172a;
		text-align: right;
		word-break: break-all;
	}

	.plat-order-info-row__value--price {
		color: #ef4444;
	}

	.plat-order-info-row__value--discount {
		color: #ef4444;
	}

	.plat-order-info-row__copy {
		display: inline-flex;
		align-items: center;
		justify-content: flex-end;
		gap: 8rpx;
	}

	.plat-detail-spacer {
		height: calc(148rpx + env(safe-area-inset-bottom));
	}

	.plat-order-detail-foot {
		padding: 18rpx 24rpx calc(18rpx + env(safe-area-inset-bottom));
		background: linear-gradient(180deg, rgba(248, 250, 252, 0), rgba(248, 250, 252, 0.94) 28%, #f8fafc 100%);
	}

	.plat-order-detail-actions {
		padding: 18rpx 20rpx;
		border-radius: 24rpx;
		background: rgba(15, 23, 42, 0.94);
		box-shadow: 0 18rpx 40rpx rgba(15, 23, 42, 0.18);
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.plat-order-detail-actions__text {
		flex: 1;
		min-width: 0;
	}

	.plat-order-detail-actions__title {
		font-size: 28rpx;
		font-weight: 700;
		color: #ffffff;
	}

	.plat-order-detail-actions__desc {
		margin-top: 6rpx;
		font-size: 22rpx;
		line-height: 1.45;
		color: rgba(255, 255, 255, 0.72);
	}

	.plat-order-detail-actions__buttons {
		flex-shrink: 0;
	}

	.plat-order-detail-page .plat-btn--sm {
		min-width: 148rpx;
		height: 58rpx;
		padding: 0 22rpx;
		border-radius: 999rpx;
		font-size: 24rpx;
		box-sizing: border-box;
	}
</style>
