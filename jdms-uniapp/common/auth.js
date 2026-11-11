export const saveUserSession = (res) => {
	if (!res) return;
	const token = res.token || res.data?.token;
	const user = res.user || res.data?.user;
	if (token) uni.setStorageSync('token', token);
	if (!user) return;
	if (user.userId) uni.setStorageSync('userId', user.userId);
	if (user.phone) uni.setStorageSync('phone', user.phone);
	if (user.userName) uni.setStorageSync('userName', user.userName);
	if (user.invitationCode) uni.setStorageSync('invitationCode', user.invitationCode);
	if (user.avatar) uni.setStorageSync('avatar', user.avatar);
	if (user.sex !== undefined && user.sex !== null) uni.setStorageSync('sex', user.sex);
	if (user.openId) uni.setStorageSync('openId', user.openId);
};

export const normalizeWxLoginData = (data = {}) => {
	const openId = data.open_id || data.openid || data.openId || '';
	const sessionKey = data.session_key || data.sessionKey || '';
	const unionId = data.unionid || data.unionId || '';
	const hasPhone = data.isPhone === '1' || data.isPhone === 1 || data.bindPhone === true;
	return {
		openId,
		sessionKey,
		unionId,
		hasPhone
	};
};

export const clearUserSession = () => {
	uni.removeStorageSync('token');
	uni.removeStorageSync('userId');
	uni.removeStorageSync('phone');
	uni.removeStorageSync('userName');
	uni.removeStorageSync('avatar');
	uni.removeStorageSync('openId');
};
