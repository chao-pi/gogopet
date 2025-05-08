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
      if (userInfo) {
        // 确保用户信息包含必要的字段
        const userData = {
          id: userInfo.userId || userInfo.id,  // 优先使用 userId
          userName: userInfo.userName,
          userType: userInfo.userType,
          userAddress: userInfo.userAddress,
          avatarUrl: userInfo.avatarUrl || null,  // 使用 avatarUrl 而不是 pictureUrl
          companyId: userInfo.companyId || null  // 添加公司ID
        }
        this.userInfo = userData
        localStorage.setItem('userInfo', JSON.stringify(userData))
        // 根据用户信息设置角色
        const role = userInfo.companyId ? 'company' : 'user'
        this.setUserRole(role)
      } else {
        this.userInfo = {}
        localStorage.removeItem('userInfo')
      }
    },

    setUserRole(role) {
      this.userRole = role
      localStorage.setItem('userRole', role)
    },

    async fetchUserInfo() {
      try {
        // 从localStorage中获取用户ID
        const storedUserInfo = localStorage.getItem('userInfo')
        if (!storedUserInfo) {
          throw new Error('No user info found')
        }
        
        const { id } = JSON.parse(storedUserInfo)
        const response = await getUserInfo(id)
        this.setUserInfo(response.data)
      } catch (error) {
        console.error('获取用户信息失败:', error)
        // 获取失败时清除所有信息
        this.logout()
      }
    },

    logout() {
      this.token = ''
      this.userInfo = {}
      this.userRole = ''
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('userRole')
    },

    init() {
      const token = localStorage.getItem('token')
      const storedUserInfo = localStorage.getItem('userInfo')
      
      if (token && storedUserInfo) {
        // 如果有token和用户信息，恢复用户状态
        const userData = JSON.parse(storedUserInfo)
        this.setUserInfo(userData)
      } else if (token && !storedUserInfo) {
        // 如果有token但没有用户信息，尝试获取用户信息
        this.fetchUserInfo()
      } else {
        // 如果没有token，清除所有信息
        this.logout()
      }
    }
  },

  getters: {
    isLoggedIn: (state) => !!state.token && !!state.userInfo && !!state.userInfo.userName
  }
})
