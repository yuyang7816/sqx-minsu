export const getWxAppid = () => {
	return 'wxbf1429781694727b';
};
export const getInvitation = () => {
	return uni.getStorageSync('publicRelation');
};
export const getAppDownUrl = () => {
	return uni.getStorageSync('appurl');
};
export const publicYuMing = () => {
	return 'https://peizhen.xianmaxiong.com';
};

export const buildInviteShareUrl = (baseUrl, invitationCode) => {
	if (!baseUrl || !invitationCode) {
		return '';
	}
	return `${String(baseUrl).replace(/\/$/, '')}/?otherInviterCode=${invitationCode}`;
};

export const saveInviteCode = (code) => {
	if (!code) {
		return;
	}
	uni.setStorageSync('otherInviterCode', code);
	uni.setStorageSync('inviterCode', code);
};

export const getStoredInviterCode = () => {
	return uni.getStorageSync('otherInviterCode') || uni.getStorageSync('inviterCode') || '';
};

export const parseInviteCodeFromOptions = (option = {}) => {
	if (option.otherInviterCode) {
		return option.otherInviterCode;
	}
	if (option.inviterCode) {
		return option.inviterCode;
	}
	// #ifdef MP-WEIXIN
	if (option.scene) {
		try {
			return decodeURIComponent(option.scene);
		} catch (e) {
			return option.scene;
		}
	}
	// #endif
	return '';
};