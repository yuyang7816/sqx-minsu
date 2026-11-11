const STORAGE_KEY = 'userAddressInfo'

/** 微信/H5 开发者工具默认模拟坐标（香港布力径） */
export function isDevMockLocation(info) {
	if (!info?.latitude || !info?.longitude) {
		return false
	}
	const lat = Number(info.latitude)
	const lng = Number(info.longitude)
	if (Math.abs(lat - 22.27534) < 0.002 && Math.abs(lng - 114.16546) < 0.002) {
		return true
	}
	const text = `${info.province || ''}${info.city || ''}${info.district || ''}${info.building || ''}`
	return text.includes('布力径')
}

export function clearAddressInfo() {
	uni.removeStorageSync(STORAGE_KEY)
}

export function saveAddressInfo(info) {
	if (!info?.longitude || !info?.latitude || isDevMockLocation(info)) {
		return
	}
	uni.setStorageSync(STORAGE_KEY, JSON.stringify(info))
}

export function loadAddressInfo() {
	try {
		const raw = uni.getStorageSync(STORAGE_KEY)
		if (!raw) {
			return null
		}
		const info = JSON.parse(raw)
		if (info?.longitude && info?.latitude) {
			return info
		}
	} catch (e) {}
	return null
}

export function restoreAddressInfo(target) {
	const cached = loadAddressInfo()
	if (!cached) {
		return false
	}
	if (isDevMockLocation(cached)) {
		clearAddressInfo()
		return false
	}
	Object.assign(target, cached)
	return true
}
