/**
 * 发布页参数组装
 */
export function buildSaveRoomPayload(formData, options = {}) {
	const {
		roomId = null,
		shopId = '',
		roomTypeId = '',
		checkInTime = '',
		checkOutTime = '',
		isEnable = '1',
		roomTitleImg = '',
		roomImg = [],
		classifyIds = []
	} = options

	const data = {
		roomName: formData.roomName,
		roomCount: Number(formData.roomCount) || 1,
		floorArea: formData.floorArea,
		floor: formData.floor,
		money: formData.money,
		memberMoney: formData.memberMoney,
		checkInTime,
		checkOutTime,
		isEnable,
		foodPolicy: formData.foodPolicy,
		petRule: formData.petRule,
		hotelTips: formData.hotelTips,
		roomTitleImg,
		roomImg: roomImg.join(','),
		classifyIds: classifyIds.join(','),
		shopId,
		roomTypeId
	}

	if (roomId) {
		data.roomId = roomId
	}
	return data
}
