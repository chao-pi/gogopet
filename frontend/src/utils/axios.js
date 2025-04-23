import axios from 'axios'
import router from '../router'

// 创建axios实例
const instance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000
})

// 请求拦截器
instance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
instance.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // token过期或无效
          localStorage.removeItem('token')
          localStorage.removeItem('username')
          router.push('/login')
          break
        case 403:
          // 权限不足
          alert('权限不足')
          break
        case 404:
          // 资源不存在
          alert('请求的资源不存在')
          break
        case 500:
          // 服务器错误
          alert('服务器错误，请稍后重试')
          break
        default:
          alert(error.response.data || '请求失败')
      }
    } else if (error.request) {
      // 请求发出但没有收到响应
      alert('无法连接到服务器，请检查网络连接')
    } else {
      // 请求配置出错
      alert('请求出错: ' + error.message)
    }
    return Promise.reject(error)
  }
)

export default instance 