import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    // 添加详细的请求日志
    console.log('请求配置:', {
      url: config.url,
      method: config.method,
      headers: config.headers,
      data: config.data
    })
    
    // 如果是创建订单的请求，特别记录transportMethod
    if (config.url === '/order/create' && config.method === 'post') {
      console.log('创建订单请求数据:', config.data)
      console.log('运输方式:', config.data.transportMethod)
    }
    
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 打印响应数据
    console.log('响应数据:', response.data)
    // 返回响应数据
    return response.data
  },
  error => {
    console.error('响应错误:', error)
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // token无效或过期，清除用户信息并跳转到登录页
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          router.push('/login')
          break
        case 403:
          ElMessage.error('权限不足')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误')
          break
        default:
          ElMessage.error(error.response.data?.message || '请求失败')
      }
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }
    return Promise.reject(error)
  }
)

export default request 