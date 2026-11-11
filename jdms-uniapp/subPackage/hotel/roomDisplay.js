/** C 端房型卡片展示：聚合后标题与剩余库存文案 */

export function getRoomDisplayTitle(item) {
	if (!item) return ''
	const name = item.roomName || item.roomType || ''
	if (!name) return item.roomType || ''
	const normalized = String(name).replace(/[-_]?\d+$/, '')
	return normalized || item.roomType || name
}

export function getRoomResidueText(item) {
	if (!item) return ''
	const count = Number(item.residueCount ?? item.availableRoomCount ?? item.roomCount ?? 0)
	if (!count || count <= 0) return ''
	return `剩余${count}间`
}

export function getRoomSubline(item) {
	const typePart = [item?.roomType, item?.floorArea ? `${item.floorArea}㎡` : ''].filter(Boolean).join(' · ')
	const residuePart = getRoomResidueText(item)
	return [typePart, residuePart].filter(Boolean).join(' · ')
}
