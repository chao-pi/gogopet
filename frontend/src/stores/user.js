import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    userRole: localStorage.getItem('userRole') || ''
  }),

  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },

    setUserInfo(userInfo) {
      this.userInfo = {
        ...userInfo,
        id: userInfo.userId || userInfo.id // 确保 id 字段有值
      }
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
      // 根据用户信息设置角色
      const role = userInfo.companyId ? 'company' : 'user'
      this.setUserRole(role)
    },

    setUserRole(role) {
      this.userRole = role
      localStorage.setItem('userRole', role)
    },

    logout() {
      this.token = ''
      this.userInfo = {}
      this.userRole = ''
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('userRole')
    }
  },

  getters: {
    isLoggedIn: (state) => !!state.token && !!state.userInfo && !!state.userInfo.userName
  }
})