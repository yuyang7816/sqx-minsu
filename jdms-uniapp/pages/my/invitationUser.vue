<template>
	<view class="plat-page plat-form-page plat-invite-page">
		<!-- <view class="plat-head plat-invite-head">
			<view class="plat-invite-head__title">邀请好友</view>
			<view class="plat-invite-head__hint">分享专属邀请码，好友注册即绑定</view>
		</view> -->

		<view class="plat-body plat-body--pad plat-body--overlap">
			<view class="plat-list-feed plat-invite-feed">
				<view class="plat-invite-stage">
					<view class="plat-card plat-invite-poster">
						<image :src="backgroundImage?backgroundImage:'/static/logo.png'" mode="widthFix"
							class="plat-invite-poster__img"></image>
						<view class="plat-invite-poster__mask"></view>
						<view class="plat-invite-poster__meta">
							<view class="plat-invite-poster__badge">邀请有礼</view>
							<view class="plat-invite-poster__title">分享你的专属邀请码</view>
							<view class="plat-invite-poster__hint">好友扫码注册后自动绑定邀请关系</view>
						</view>
					</view>

					<view class="plat-card plat-invite-profile">
						<view class="plat-invite-profile__top">
							<view class="plat-invite-profile__label">专属邀请卡</view>
							<view class="plat-invite-profile__sub">可截图或生成海报分享给好友</view>
						</view>
						<view class="plat-invite-profile__row">
							<view class="plat-invite-profile__user">
								<view class="plat-invite-profile__avatar-wrap">
									<image :src="avatar?avatar:'/static/logo.png'" mode="aspectFill" class="plat-invite-profile__avatar">
									</image>
									<view class="plat-invite-profile__avatar-tag">邀请人</view>
								</view>
								<view class="plat-invite-profile__info">
									<view class="plat-invite-profile__caption">我的专属邀请码</view>
									<view class="plat-invite-profile__name">{{userName?userName:'匿名用户'}}</view>
									<view class="plat-invite-profile__id">ID:{{myInvitationCode}}</view>
								</view>
							</view>
							<view class="plat-invite-profile__qr-wrap">
								<image class="plat-invite-profile__qr" v-if="qrImg" :src="qrImg" mode="aspectFill"></image>
								<view class="plat-invite-profile__qr plat-invite-profile__qr--loading" v-else>
									<text>二维码</text>
									<text>生成中</text>
								</view>
								<view class="plat-invite-profile__qr-tip">扫码注册并绑定邀请关系</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="plat-fixed-foot plat-invite-foot">
			<view class="plat-invite-foot__bar">
				<view @click="sharurl()" class="plat-btn plat-btn--ghost plat-invite-foot__btn">文案推广</view>
				<!-- #ifdef MP-WEIXIN -->
				<button open-type="share" class="plat-btn plat-invite-foot__btn plat-invite-foot__share-btn">分享好友</button>
				<!-- #endif -->
				<view @click="getShareImg()" class="plat-btn plat-invite-foot__btn">分享海报</view>
			</view>
		</view>

		<!-- #ifndef MP-WEIXIN -->
		<view class="plat-invite-hidden" v-if="shareH5Url">
			<up-qrcode :size="200" @result="resultQr" :val="shareH5Url"></up-qrcode>
		</view>
		<!-- #endif -->
		<!-- #ifndef H5 -->
		<canvas canvas-id="myShareImg" id="myShareImg" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}"
			class="plat-invite-hidden"></canvas>
		<!-- #endif -->

		<up-popup :show="showShareImg" @close="closeShareImg" mode="center" :round="16">
			<view class="plat-invite-share-popup">
				<image :src="canvasImgSrc" :style="{width:shareImgWidth+'px',height:shareImgHeight+'px'}" mode="widthFix"
					class="plat-invite-share-popup__img"></image>
				<view class="plat-invite-share-popup__actions">
					<view @click="savePoster()" class="plat-btn plat-invite-share-popup__btn">保存海报</view>
					<!-- #ifdef MP-WEIXIN -->
					<button open-type="share" class="plat-btn plat-invite-share-popup__btn plat-invite-foot__share-btn">分享给好友</button>
					<!-- #endif -->
				</view>
				<!-- #ifdef H5 -->
				<view class="plat-invite-share-popup__tip">也可长按图片保存到相册</view>
				<!-- #endif -->
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShareAppMessage
	} from '@dcloudio/uni-app';
	import config from '../../common/config.js';
	import {
		buildInviteShareUrl
	} from '../../common/queue.js';

	const {
		proxy
	} = getCurrentInstance();
	const backgroundImage = ref('');
	const myInvitationCode = ref('');
	const userName = ref('');
	const avatar = ref('');
	const tuiguang1 = ref('');
	const url = ref('');
	const h5InviteBase = ref('');
	const qrImg = ref('');
	const shareH5Url = ref('');
	const shareImgWidth = ref(uni.upx2px(686));
	const shareImgHeight = ref(uni.upx2px(900));
	const canvasImgSrc = ref('');
	const showShareImg = ref(false);
	const qrReadyPromise = ref(null);
	let qrReadyResolve = null;

	onLoad(() => {
		getBackImageList();
		if (uni.getStorageSync('token')) {
			getUserInfo();
		} else {
			uni.showToast({
				title: '请先登录',
				icon: 'none'
			});
		}
	});

	onShareAppMessage(() => {
		const relationId = myInvitationCode.value;
		return {
			title: tuiguang1.value || `${userName.value || '好友'}邀请你加入`,
			path: `/pages/index/index?otherInviterCode=${relationId}`,
			imageUrl: backgroundImage.value || '/static/logo.png'
		};
	});

	const resetQrReadyPromise = () => {
		qrReadyPromise.value = new Promise((resolve) => {
			qrReadyResolve = resolve;
		});
	};

	const markQrReady = () => {
		if (qrReadyResolve) {
			qrReadyResolve();
			qrReadyResolve = null;
		}
	};

	const getInviteLinkBase = () => {
		return h5InviteBase.value || url.value || proxy.$publicYuMing();
	};

	const updateShareH5Url = () => {
		shareH5Url.value = buildInviteShareUrl(getInviteLinkBase(), myInvitationCode.value);
	};

	const initInviteQrCode = () => {
		if (!myInvitationCode.value) {
			return;
		}
		resetQrReadyPromise();
		updateShareH5Url();
		// #ifdef MP-WEIXIN
		qrImg.value = `${config.APIHOST}/app/invite/mpCreateQr?invitationCode=${myInvitationCode.value}&page=pages/public/register&t=${Date.now()}`;
		markQrReady();
		// #endif
	};

	const closeShareImg = () => {
		showShareImg.value = false;
	};

	const ensureQrReady = async () => {
		if (qrImg.value) {
			return;
		}
		if (!qrReadyPromise.value) {
			initInviteQrCode();
		}
		await Promise.race([
			qrReadyPromise.value,
			new Promise((_, reject) => {
				setTimeout(() => reject(new Error('二维码生成超时')), 12000);
			})
		]);
		if (!qrImg.value) {
			throw new Error('二维码未生成');
		}
	};

	const getShareImg = async () => {
		if (!myInvitationCode.value) {
			uni.showToast({
				title: '邀请码加载中，请稍后',
				icon: 'none'
			});
			return;
		}
		if (canvasImgSrc.value) {
			showShareImg.value = true;
			return;
		}
		await shareImg();
	};

	const shareImg = async () => {
		try {
			uni.showLoading({
				title: '生成中...'
			});
			await ensureQrReady();
			if (!shareImgHeight.value) {
				const bgInfo = await getImageInfo(backgroundImage.value || '/static/logo.png');
				shareImgHeight.value = (shareImgWidth.value * bgInfo.height) / bgInfo.width;
			}
			const paddingNum = 20;
			const avatarSize = 60;
			const qrSize = 80;
			const infoBlockHeight = Math.max(avatarSize, qrSize) + paddingNum * 2;

			// #ifdef H5
			await shareImgH5({
				bgSrc: backgroundImage.value || '/static/logo.png',
				qrSrc: qrImg.value,
				avatarSrc: avatar.value || '/static/logo.png',
				paddingNum,
				avatarSize,
				qrSize,
				infoBlockHeight
			});
			return;
			// #endif

			const bgInfo = await getImageInfo(backgroundImage.value || '/static/logo.png');
			const qrInfo = await getImageInfo(qrImg.value);
			const avatarSrc = avatar.value || '/static/logo.png';
			const avatarInfo = await getImageInfo(avatarSrc);

			const ctx = uni.createCanvasContext('myShareImg');
			ctx.setFillStyle('#ffffff');
			ctx.fillRect(0, 0, shareImgWidth.value, shareImgHeight.value);
			ctx.drawImage(bgInfo.path, 0, 0, shareImgWidth.value, shareImgHeight.value - infoBlockHeight);

			ctx.setFillStyle('#ffffff');
			ctx.fillRect(0, shareImgHeight.value - infoBlockHeight, shareImgWidth.value, infoBlockHeight);

			const avatarCenterX = paddingNum + avatarSize / 2;
			const avatarCenterY = shareImgHeight.value - paddingNum - avatarSize / 2;
			ctx.save();
			ctx.beginPath();
			ctx.arc(avatarCenterX, avatarCenterY, avatarSize / 2, 0, 2 * Math.PI);
			ctx.clip();
			ctx.drawImage(
				avatarInfo.path,
				paddingNum,
				shareImgHeight.value - paddingNum - avatarSize,
				avatarSize,
				avatarSize
			);
			ctx.restore();

			const textX = paddingNum + avatarSize + 12;
			ctx.setFontSize(18);
			ctx.setFillStyle('#333333');
			ctx.fillText(userName.value || '匿名用户', textX, shareImgHeight.value - paddingNum - avatarSize / 2 - 8);
			ctx.setFontSize(14);
			ctx.setFillStyle('#666666');
			ctx.fillText(`ID:${myInvitationCode.value}`, textX, shareImgHeight.value - paddingNum - 8);

			const qrX = shareImgWidth.value - paddingNum - qrSize;
			const qrY = shareImgHeight.value - paddingNum - qrSize;
			ctx.setFillStyle('#ffffff');
			ctx.fillRect(qrX - 4, qrY - 4, qrSize + 8, qrSize + 8);
			ctx.drawImage(qrInfo.path, qrX, qrY, qrSize, qrSize);

			ctx.draw(false, () => {
				setTimeout(() => {
					getCanvasNewImage();
				}, 200);
			});
		} catch (err) {
			uni.hideLoading();
			uni.showToast({
				title: err.message || '海报生成失败',
				icon: 'none'
			});
		}
	};

	// #ifdef H5
	const isCrossOriginImage = (src) => {
		if (!src || src.startsWith('data:') || src.startsWith('blob:')) {
			return false;
		}
		if (src.startsWith('/')) {
			return false;
		}
		try {
			return new URL(src, window.location.origin).origin !== window.location.origin;
		} catch (e) {
			return false;
		}
	};

	const loadDomImage = (src) => {
		return new Promise((resolve, reject) => {
			const renderImage = (url, needRevoke = false) => {
				const img = new Image();
				img.crossOrigin = 'anonymous';
				img.onload = () => {
					if (needRevoke) {
						URL.revokeObjectURL(url);
					}
					resolve(img);
				};
				img.onerror = () => {
					if (needRevoke) {
						URL.revokeObjectURL(url);
					}
					reject(new Error('图片加载失败'));
				};
				img.src = url;
			};

			if (!isCrossOriginImage(src)) {
				renderImage(src);
				return;
			}

			uni.request({
				url: `${config.APIHOST}/app/common/proxyImage`,
				method: 'GET',
				data: {
					url: src
				},
				responseType: 'arraybuffer',
				success: (res) => {
					if (res.statusCode !== 200 || !res.data) {
						reject(new Error('图片代理失败'));
						return;
					}
					const contentType = res.header?.['content-type'] || res.header?.['Content-Type'] || 'image/png';
					const blob = new Blob([res.data], {
						type: contentType.split(';')[0]
					});
					renderImage(URL.createObjectURL(blob), true);
				},
				fail: () => {
					reject(new Error('图片代理失败'));
				}
			});
		});
	};

	const shareImgH5 = async ({
		bgSrc,
		qrSrc,
		avatarSrc,
		paddingNum,
		avatarSize,
		qrSize,
		infoBlockHeight
	}) => {
		const width = shareImgWidth.value;
		const height = shareImgHeight.value;
		const scale = 2;
		const canvasEl = document.createElement('canvas');
		canvasEl.width = width * scale;
		canvasEl.height = height * scale;
		const ctx = canvasEl.getContext('2d', {
			willReadFrequently: true
		});
		ctx.scale(scale, scale);

		const [bgImage, avatarImage, qrImage] = await Promise.all([
			loadDomImage(bgSrc),
			loadDomImage(avatarSrc),
			loadDomImage(qrSrc)
		]);

		ctx.fillStyle = '#ffffff';
		ctx.fillRect(0, 0, width, height);
		ctx.drawImage(bgImage, 0, 0, width, height - infoBlockHeight);
		ctx.fillRect(0, height - infoBlockHeight, width, infoBlockHeight);

		const avatarCenterX = paddingNum + avatarSize / 2;
		const avatarCenterY = height - paddingNum - avatarSize / 2;
		ctx.save();
		ctx.beginPath();
		ctx.arc(avatarCenterX, avatarCenterY, avatarSize / 2, 0, Math.PI * 2);
		ctx.clip();
		ctx.drawImage(avatarImage, paddingNum, height - paddingNum - avatarSize, avatarSize, avatarSize);
		ctx.restore();

		const textX = paddingNum + avatarSize + 12;
		ctx.fillStyle = '#333333';
		ctx.font = '18px sans-serif';
		ctx.fillText(userName.value || '匿名用户', textX, height - paddingNum - avatarSize / 2 - 8);
		ctx.fillStyle = '#666666';
		ctx.font = '14px sans-serif';
		ctx.fillText(`ID:${myInvitationCode.value}`, textX, height - paddingNum - 8);

		const qrX = width - paddingNum - qrSize;
		const qrY = height - paddingNum - qrSize;
		ctx.fillStyle = '#ffffff';
		ctx.fillRect(qrX - 4, qrY - 4, qrSize + 8, qrSize + 8);
		ctx.drawImage(qrImage, qrX, qrY, qrSize, qrSize);

		canvasImgSrc.value = canvasEl.toDataURL('image/png', 1);
		showShareImg.value = true;
		uni.hideLoading();
	};
	// #endif

	const getCanvasNewImage = () => {
		uni.canvasToTempFilePath({
			canvasId: 'myShareImg',
			width: shareImgWidth.value,
			height: shareImgHeight.value,
			destWidth: shareImgWidth.value * 2,
			destHeight: shareImgHeight.value * 2,
			quality: 1,
			success: (res) => {
				canvasImgSrc.value = res.tempFilePath;
				showShareImg.value = true;
				uni.hideLoading();
			},
			fail: () => {
				uni.hideLoading();
				uni.showToast({
					title: '海报生成失败',
					icon: 'none'
				});
			}
		});
	};

	const getImageInfo = (imgSrc) => {
		return new Promise((resolve, reject) => {
			uni.getImageInfo({
				src: imgSrc,
				success: resolve,
				fail: reject
			});
		});
	};

	const resultQr = (e) => {
		qrImg.value = e;
		markQrReady();
	};

	const fetchH5InviteBase = () => {
		return proxy.$http.getT('/app/common/type/19').then(res => {
			if (res.code === 0 && res.data && res.data.value) {
				h5InviteBase.value = res.data.value;
			} else {
				h5InviteBase.value = proxy.$publicYuMing();
			}
			updateShareH5Url();
		});
	};

	const getTuiguang1 = async () => {
		proxy.$http.postJson('/app/config/info', {
			key: 'biz_invite_share_title'
		}).then(res => {
			if (res.code == 0 && res.data.value) {
				tuiguang1.value = res.data.value;
			}
		});

		// #ifndef H5
		proxy.$http.getT('/app/common/type/25').then(res => {
			if (res.code === 0 && res.data && res.data.value) {
				url.value = res.data.value;
			}
		});
		// #endif

		// #ifdef H5
		await proxy.$http.getT('/app/common/type/19').then(res => {
			if (res.code === 0 && res.data && res.data.value) {
				url.value = res.data.value;
				h5InviteBase.value = res.data.value;
			} else {
				url.value = proxy.$publicYuMing();
				h5InviteBase.value = proxy.$publicYuMing();
			}
		});
		// #endif

		// #ifndef H5
		await fetchH5InviteBase();
		// #endif

		initInviteQrCode();
	};

	const getUserInfo = () => {
		proxy.$http.getT('/app/user/selectUserById').then(res => {
			if (res.code == 0) {
				myInvitationCode.value = res.data.invitationCode;
				userName.value = res.data.userName;
				avatar.value = res.data.avatar;
				uni.setStorageSync('avatar', res.data.avatar);
				uni.setStorageSync('myInvitationCode', res.data.invitationCode);
				getTuiguang1();
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				});
			}
		});
	};

	const getBackImageList = async () => {
		try {
			const res = await proxy.$http.get('/app/common/type/516');
			if (res.code == 0 && res.data && res.data.value) {
				backgroundImage.value = res.data.value;
				const imgInfo = await getImageInfo(backgroundImage.value);
				shareImgHeight.value = (shareImgWidth.value * imgInfo.height) / imgInfo.width;
			}
		} catch (e) {
			console.error(e);
		}
	};

	const sharurl = () => {
		const relationId = myInvitationCode.value;
		const shareBase = url.value || getInviteLinkBase();
		const modalContent = `${tuiguang1.value}\n${buildInviteShareUrl(shareBase, relationId)}`;
		uni.showModal({
			title: '文案推广',
			content: modalContent,
			showCancel: true,
			cancelText: '关闭',
			confirmText: '一键复制',
			confirmColor: '#2C8EFE',
			success: (res) => {
				if (res.confirm) {
					uni.setClipboardData({
						data: modalContent,
						success: () => {
							uni.showToast({
								title: '复制成功',
								icon: 'none'
							});
						}
					});
				}
			}
		});
	};

	const savePoster = () => {
		if (!canvasImgSrc.value) {
			uni.showToast({
				title: '请先生成海报',
				icon: 'none'
			});
			return;
		}
		// #ifdef H5
		const link = document.createElement('a');
		link.href = canvasImgSrc.value;
		link.download = `invite-${myInvitationCode.value || 'poster'}.png`;
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
		uni.showToast({
			title: '已开始下载',
			icon: 'none'
		});
		// #endif
		// #ifndef H5
		uni.saveImageToPhotosAlbum({
			filePath: canvasImgSrc.value,
			success: () => {
				uni.showToast({
					title: '已保存到相册'
				});
			},
			fail: (err) => {
				const needAuth = err.errMsg && (err.errMsg.includes('auth') || err.errMsg.includes('authorize'));
				if (needAuth) {
					uni.showModal({
						title: '提示',
						content: '需要相册权限才能保存海报',
						confirmText: '去设置',
						success: (res) => {
							if (res.confirm) {
								uni.openSetting();
							}
						}
					});
				} else {
					uni.showToast({
						title: '保存失败',
						icon: 'none'
					});
				}
			}
		});
		// #endif
	};
</script>

<style lang="scss" scoped>
	.plat-invite-page {
		min-height: 100vh;
		padding-bottom: calc(172rpx + env(safe-area-inset-bottom));
		background:
			radial-gradient(circle at top, rgba(255, 219, 160, 0.18) 0, rgba(255, 219, 160, 0) 24%),
			linear-gradient(180deg, #f8fafe 0%, #eef3fb 100%);
	}

	.plat-invite-feed {
		padding: 0;
	}

	.plat-body--overlap {
		margin-top: 0;
		padding-top: calc(20rpx + env(safe-area-inset-top));
	}

	.plat-invite-stage {
		position: relative;
		padding-top: 8rpx;
	}

	.plat-invite-poster {
		position: relative;
		overflow: hidden;
		padding: 0;
		border-radius: 36rpx;
		box-shadow: 0 20rpx 48rpx rgba(147, 28, 28, 0.14);
	}

	.plat-invite-poster__img {
		display: block;
		width: 100%;
	}

	.plat-invite-poster__mask {
		position: absolute;
		left: 0;
		right: 0;
		top: 0;
		height: 120rpx;
		background: linear-gradient(180deg, rgba(95, 19, 19, 0.28) 0%, rgba(95, 19, 19, 0) 100%);
	}

	.plat-invite-poster__meta {
		position: absolute;
		left: 24rpx;
		top: 42rpx;
		z-index: 2;
		display: inline-flex;
		flex-direction: column;
		align-items: flex-start;
	}

	.plat-invite-poster__badge {
		display: inline-flex;
		align-items: center;
		height: 44rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		background: rgba(255, 255, 255, 0.22);
		border: 1rpx solid rgba(255, 255, 255, 0.2);
		color: #fff6dc;
		font-size: 22rpx;
		backdrop-filter: blur(10rpx);
	}

	.plat-invite-poster__title {
		margin-top: 14rpx;
		padding: 10rpx 18rpx;
		border-radius: 18rpx;
		background: rgba(95, 19, 19, 0.24);
		font-size: 26rpx;
		font-weight: 700;
		line-height: 1.3;
		color: #ffffff;
		backdrop-filter: blur(10rpx);
	}

	.plat-invite-poster__hint {
		margin-top: 10rpx;
		padding-left: 6rpx;
		font-size: 20rpx;
		line-height: 1.6;
		color: rgba(255, 255, 255, 0.78);
	}

	.plat-invite-profile {
		position: relative;
		z-index: 3;
		margin: -210rpx 0 0;
		padding: 28rpx 28rpx 30rpx;
		border-radius: 32rpx;
		background: rgba(255, 255, 255, 0.98);
		border: 1rpx solid rgba(255, 255, 255, 0.78);
		box-shadow: 0 20rpx 50rpx rgba(30, 41, 59, 0.1);
		backdrop-filter: blur(16rpx);
	}

	.plat-invite-profile__top {
		padding-bottom: 18rpx;
		border-bottom: 1rpx solid #edf2f8;
	}

	.plat-invite-profile__label {
		font-size: 28rpx;
		font-weight: 700;
		color: #0f172a;
	}

	.plat-invite-profile__sub {
		margin-top: 8rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: #94a3b8;
		max-width: 420rpx;
	}

	.plat-invite-profile__row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 20rpx;
		padding-top: 22rpx;
	}

	.plat-invite-profile__user {
		flex: 1;
		min-width: 0;
		display: flex;
		align-items: center;
	}

	.plat-invite-profile__avatar-wrap {
		position: relative;
		flex-shrink: 0;
	}

	.plat-invite-profile__avatar {
		width: 104rpx;
		height: 104rpx;
		border-radius: 32rpx;
		background: linear-gradient(180deg, #edf4ff 0%, #dceafe 100%);
		border: 4rpx solid rgba(44, 142, 254, 0.12);
	}

	.plat-invite-profile__avatar-tag {
		position: absolute;
		left: 50%;
		bottom: -14rpx;
		transform: translateX(-50%);
		height: 34rpx;
		line-height: 34rpx;
		padding: 0 14rpx;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #79b7ff 0%, #2c8efe 100%);
		box-shadow: 0 10rpx 22rpx rgba(44, 142, 254, 0.26);
		font-size: 18rpx;
		color: #ffffff;
		white-space: nowrap;
	}

	.plat-invite-profile__info {
		min-width: 0;
		margin-left: 22rpx;
	}

	.plat-invite-profile__caption {
		font-size: 22rpx;
		color: #94a3b8;
		line-height: 1.3;
	}

	.plat-invite-profile__name {
		margin-top: 14rpx;
		font-size: 38rpx;
		font-weight: 700;
		line-height: 1.2;
		color: #0f172a;
		word-break: break-all;
	}

	.plat-invite-profile__id {
		margin-top: 12rpx;
		font-size: 26rpx;
		color: #64748b;
	}

	.plat-invite-profile__qr-wrap {
		width: 168rpx;
		flex-shrink: 0;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding-left: 16rpx;
		border-left: 1rpx solid #edf2f8;
	}

	.plat-invite-profile__qr {
		width: 122rpx;
		height: 122rpx;
		border-radius: 24rpx;
		border: 1rpx solid #e5edf7;
		background: #ffffff;
		box-shadow: inset 0 0 0 8rpx #f8fbff;
	}

	.plat-invite-profile__qr--loading {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		font-size: 22rpx;
		line-height: 1.6;
		color: #94a3b8;
		background: linear-gradient(180deg, #f8fbff 0%, #f2f6fb 100%);
	}

	.plat-invite-profile__qr-tip {
		margin-top: 10rpx;
		font-size: 20rpx;
		line-height: 1.5;
		color: #94a3b8;
		text-align: center;
	}

	.plat-invite-foot {
		padding-bottom: calc(18rpx + env(safe-area-inset-bottom));
	}

	.plat-invite-foot__bar {
		display: flex;
		align-items: center;
		gap: 18rpx;
		padding: 10rpx;
		border-radius: 999rpx;
		background: rgba(255, 255, 255, 0.94);
		box-shadow: 0 18rpx 40rpx rgba(15, 23, 42, 0.1);
		backdrop-filter: blur(16rpx);
	}

	.plat-invite-foot__btn {
		flex: 1;
		border-radius: 999rpx;
		font-size: 30rpx;
		font-weight: 600;
	}

	.plat-btn--ghost.plat-invite-foot__btn {
		background: #ffffff;
		color: #2c8efe;
		border: 1rpx solid rgba(44, 142, 254, 0.3);
	}

	.plat-invite-foot__share-btn {
		margin: 0;
		padding: 0;
		border: none;
		line-height: normal;
		font-size: inherit;
		background: linear-gradient(135deg, #7ebaff 0%, #2c8efe 100%);
		color: #fff;
	}

	.plat-invite-foot__share-btn::after {
		border: none;
	}

	.plat-invite-hidden {
		position: fixed;
		left: -1000px;
	}

	.plat-invite-share-popup {
		max-width: 92vw;
		padding: 28rpx;
		background: #ffffff;
		border-radius: 28rpx;
	}

	.plat-invite-share-popup__img {
		display: block;
		border-radius: 24rpx;
		box-shadow: 0 18rpx 40rpx rgba(15, 23, 42, 0.08);
	}

	.plat-invite-share-popup__actions {
		display: flex;
		gap: 16rpx;
		margin-top: 24rpx;
	}

	.plat-invite-share-popup__btn {
		flex: 1;
		height: 84rpx;
		border-radius: 22rpx;
		text-align: center;
		font-size: 28rpx;
		font-weight: 600;
	}

	.plat-invite-share-popup__tip {
		margin-top: 16rpx;
		font-size: 24rpx;
		color: #94a3b8;
		text-align: center;
	}
</style>
