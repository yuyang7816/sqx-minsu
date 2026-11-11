/**
 * 商户端房间列表按房型分组
 */
export function groupRoomsByType(list = []) {
	const map = new Map();
	list.forEach(item => {
		const key = String(item.roomTypeId || item.roomType || 'unknown');
		if (!map.has(key)) {
			map.set(key, {
				key,
				roomType: item.roomType,
				rooms: [],
				totalCount: 0,
				freeCount: 0,
				minPrice: item.money || 0
			});
		}
		const group = map.get(key);
		group.rooms.push(item);
		group.totalCount += 1;
		if (item.roomStatus == 1) {
			group.freeCount += 1;
		}
		const price = Number(item.money) || 0;
		if (price < Number(group.minPrice)) {
			group.minPrice = price;
		}
	});
	return Array.from(map.values());
}
