import test from 'node:test'
import assert from 'node:assert/strict'
import { getRoomDisplayTitle, getRoomResidueText, getRoomSubline } from './roomDisplay.js'

test('getRoomDisplayTitle strips numeric room suffix', () => {
	assert.equal(getRoomDisplayTitle({ roomName: '极简双床标间-1', roomType: '标间' }), '极简双床标间')
	assert.equal(getRoomDisplayTitle({ roomName: '豪华大床房2', roomType: '大床房' }), '豪华大床房')
})

test('getRoomResidueText shows remaining count', () => {
	assert.equal(getRoomResidueText({ residueCount: 10 }), '剩余10间')
	assert.equal(getRoomResidueText({ roomCount: 3 }), '剩余3间')
})

test('getRoomSubline combines type, area and residue', () => {
	assert.equal(
		getRoomSubline({ roomType: '标间', floorArea: '35', residueCount: 10 }),
		'标间 · 35㎡ · 剩余10间'
	)
})
