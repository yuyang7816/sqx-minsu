/**
 * 是否有权限
 * @param {*} key
 */
export function isAuth(key) {
	return JSON.parse(sessionStorage.getItem('permissions') || '[]').indexOf(key) !== -1 || false
}
//处理菜单列表为树形
export function treeDataTranslate(data, id = 'id', pid = 'parentId') {
	var res = []
	var temp = {}
	for (var i = 0; i < data.length; i++) {
		temp[data[i][id]] = data[i]
	}
	for (var k = 0; k < data.length; k++) {
		if (temp[data[k][pid]] && data[k][id] !== data[k][pid]) {
			if (!temp[data[k][pid]]['children']) {
				temp[data[k][pid]]['children'] = []
			}
			if (!temp[data[k][pid]]['_level']) {
				temp[data[k][pid]]['_level'] = 1
			}
			data[k]['_level'] = temp[data[k][pid]]._level + 1
			temp[data[k][pid]]['children'].push(data[k])
		} else {
			res.push(data[k])
		}
	}
	return res
}
//获取验证码所需要的uuid
export function getUUID() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
		return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
	})
}
// 复制文本
export function copyText(text) {
	if (navigator.clipboard) {
		// 新API
		return navigator.clipboard.writeText(text)
	} else {
		// 兼容旧浏览器
		const input = document.createElement('input')
		input.value = text
		document.body.appendChild(input)
		input.select()
		document.execCommand('copy')
		document.body.removeChild(input)
		return Promise.resolve()
	}
}
/**
 * 金额保留两位小数
 * @param {number|string} money
 * @returns {string}
 */
export function countMoney(money) {
	money = Number(money)
	return money > 0 ? money.toFixed(2) + '' : '0.00'
}
/**
 * 校验手机号
 * @param {number|string} phone
 * @returns {string}
 */
export function validatePhoneNumber(phone) {
	// 正则表达式，匹配以 1 开头，第二位为 3、4、5、6、7、8、9 的 11 位数字
	const regex = /^1[3-9]\d{9}$/;
	return regex.test(phone);
}