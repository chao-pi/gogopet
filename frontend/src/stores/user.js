import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  // 从localStorage中获取初始状态
  const initialUserInfo = localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : null
  const userInfo = ref(initialUserInfo)
  const isLoggedIn = ref(!!initialUserInfo)

  // 设置用户信息
  const setUserInfo = (info) => {
    if (info) {
      // 确保用户信息包含必要的字段
      const userData = {
        id: info.id || info.userId,
        userName: info.userName,
        pictureUrl: info.pictureUrl || null
      }
      userInfo.value = userData
      isLoggedIn.value = true
      localStorage.setItem('userInfo', JSON.stringify(userData))
    } else {
      userInfo.value = null
      isLoggedIn.value = false
      localStorage.removeItem('userInfo')
    }
  }

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      // 从localStorage中获取用户ID
      const storedUserInfo = localStorage.getItem('userInfo')
      if (!storedUserInfo) {
        throw new Error('No user info found')
      }
      
      const { id } = JSON.parse(storedUserInfo)
      const response = await getUserInfo(id)
      setUserInfo(response.data)
    } catch (error) {
      console.error('获取用户信息失败:', error)
      // 获取失败时清除所有信息
      clearUserInfo()
    }
  }

  // 清除用户信息
  const clearUserInfo = () => {
    userInfo.value = null
    isLoggedIn.value = false
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')
  }

  // 初始化时检查token是否存在
  const init = () => {
    const token = localStorage.getItem('token')
    const storedUserInfo = localStorage.getItem('userInfo')
    
    if (token && storedUserInfo) {
      // 如果有token和用户信息，恢复用户状态
      const userData = JSON.parse(storedUserInfo)
      setUserInfo(userData)
    } else if (token && !storedUserInfo) {
      // 如果有token但没有用户信息，尝试获取用户信息
      fetchUserInfo()
    } else {
      // 如果没有token，清除所有信息
      clearUserInfo()
    }
  }

  // 执行初始化
  init()

  return {
    userInfo,
    isLoggedIn,
    setUserInfo,
    fetchUserInfo,
    clearUserInfo
  }
}) 