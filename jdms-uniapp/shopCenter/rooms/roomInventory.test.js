import assert from 'assert'
import { buildSaveRoomPayload } from './roomInventory.js'

function testBuildSaveRoomPayload() {
	const payload = buildSaveRoomPayload({
		roomName: '豪华大床房',
		roomCount: 5,
		floorArea: '28',
		floor: '8',
		money: 288,
		memberMoney: 258,
		foodPolicy: '无',
		petRule: '不可',
		hotelTips: '提示'
	}, {
		shopId: '1',
		roomTypeId: '10',
		checkInTime: '14:00',
		checkOutTime: '12:00',
		roomTitleImg: 'cover.jpg',
		roomImg: ['a.jpg'],
		classifyIds: ['1']
	})

	assert.strictEqual(payload.roomCount, 5)
	assert.strictEqual(payload.roomName, '豪华大床房')
	assert.strictEqual(payload.batchCount, undefined)
}

function testBuildSaveRoomPayloadEdit() {
	const payload = buildSaveRoomPayload({
		roomName: '803',
		roomCount: 2,
		floorArea: '28',
		floor: '9',
		money: 328,
		memberMoney: 298,
		foodPolicy: '无',
		petRule: '不可',
		hotelTips: '提示'
	}, {
		roomId: '100',
		shopId: '1',
		roomTypeId: '10',
		checkInTime: '14:00',
		checkOutTime: '12:00',
		roomTitleImg: 'cover.jpg',
		roomImg: ['a.jpg'],
		classifyIds: ['1']
	})

	assert.strictEqual(payload.roomName, '803')
	assert.strictEqual(payload.roomCount, 2)
	assert.strictEqual(payload.roomId, '100')
}

testBuildSaveRoomPayload()
testBuildSaveRoomPayloadEdit()
console.log('frontend unit tests passed')
