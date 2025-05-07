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
    try {
      console.log('设置用户信息 - 原始数据:', info)
      if (info) {
        // 确保用户信息包含必要的字段
        const userData = {
          id: info.userId || info.id,  // 优先使用 userId
          userName: info.userName,
          userType: info.userType,
          userAddress: info.userAddress,
          avatarUrl: info.avatarUrl || null,  // 使用 avatarUrl 而不是 pictureUrl
          companyId: info.companyId || null  // 添加公司ID
        }
        console.log('设置用户信息 - 处理后的数据:', userData)
        console.log('用户类型:', userData.userType)
        console.log('公司ID:', userData.companyId)
        userInfo.value = userData
        isLoggedIn.value = true
        localStorage.setItem('userInfo', JSON.stringify(userData))
        console.log('用户信息已保存到localStorage:', JSON.parse(localStorage.getItem('userInfo')))
      } else {
        console.log('清除用户信息')
        userInfo.value = null
        isLoggedIn.value = false
        localStorage.removeItem('userInfo')
      }
    } catch (error) {
      console.error('设置用户信息失败:', error)
      throw error
    }
  }

  // 设置token
  const setToken = (token) => {
    try {
      console.log('设置token:', token)
      if (token) {
        localStorage.setItem('token', token)
        console.log('token已保存到localStorage')
      } else {
        console.log('清除token')
        localStorage.removeItem('token')
      }
    } catch (error) {
      console.error('设置token失败:', error)
      throw error
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
    
    console.log('初始化用户store - token:', token)
    console.log('初始化用户store - 存储的用户信息:', storedUserInfo)
    
    if (token && storedUserInfo) {
      // 如果有token和用户信息，恢复用户状态
      const userData = JSON.parse(storedUserInfo)
      console.log('从localStorage恢复用户数据:', userData)
      console.log('恢复的公司ID:', userData.companyId)
      setUserInfo(userData)
    } else if (token && !storedUserInfo) {
      // 如果有token但没有用户信息，尝试获取用户信息
      console.log('有token但没有用户信息，尝试获取用户信息')
      fetchUserInfo()
    } else {
      // 如果没有token，清除所有信息
      console.log('没有token，清除所有信息')
      clearUserInfo()
    }
  }

  // 执行初始化
  init()

  return {
    userInfo,
    isLoggedIn,
    setUserInfo,
    setToken,
    fetchUserInfo,
    clearUserInfo
  }
}) 