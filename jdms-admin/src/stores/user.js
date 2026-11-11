import {
	defineStore
} from 'pinia'

export const useUserStore = defineStore('user', {
	state: () => ({
		userInfo: null,
		userId: localStorage.getItem('token') || '',
		token: localStorage.getItem('token') || ''
	}),

	actions: {
		setUserInfo(data) {
			this.userInfo = data
			localStorage.setItem('userInfo', data)
		},
		setToken(token) {
			this.token = token
			localStorage.setItem('token', token)
		},
		logout() {
			this.token = ''
			this.userInfo = null
			localStorage.removeItem('token')
		}
	}
})