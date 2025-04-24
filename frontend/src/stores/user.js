import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null)
  const isLoggedIn = ref(false)

  // 设置用户信息
  const setUserInfo = (info) => {
    userInfo.value = info
    isLoggedIn.value = !!info
  }

  // 获取用户信息
  const fetchUserInfo = async (userId) => {
    try {
      const response = await getUserInfo(userId)
      setUserInfo(response.data)
    } catch (error) {
      console.error('获取用户信息失败:', error)
      setUserInfo(null)
    }
  }

  // 清除用户信息
  const clearUserInfo = () => {
    setUserInfo(null)
  }

  return {
    userInfo,
    isLoggedIn,
    setUserInfo,
    fetchUserInfo,
    clearUserInfo
  }
}) 