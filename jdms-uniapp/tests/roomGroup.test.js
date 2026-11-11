import test from 'node:test';
import assert from 'node:assert/strict';
import { groupRoomsByType } from '../shopCenter/rooms/roomGroup.js';

test('groupRoomsByType groups by roomTypeId and counts free rooms', () => {
	const list = [
		{ roomId: 1, roomTypeId: 10, roomType: '大床房', roomStatus: 1, money: 288, roomName: '801' },
		{ roomId: 2, roomTypeId: 10, roomType: '大床房', roomStatus: 1, money: 288, roomName: '802' },
		{ roomId: 3, roomTypeId: 11, roomType: '双床房', roomStatus: 2, money: 328, roomName: '901' }
	];
	const groups = groupRoomsByType(list);
	assert.equal(groups.length, 2);
	assert.equal(groups[0].totalCount, 2);
	assert.equal(groups[0].freeCount, 2);
	assert.equal(groups[0].minPrice, 288);
	assert.equal(groups[1].totalCount, 1);
	assert.equal(groups[1].freeCount, 0);
});

test('groupRoomsByType returns empty array for empty input', () => {
	assert.deepEqual(groupRoomsByType([]), []);
});
