import axios from 'axios'

const service = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    console.log('发送请求:', {
      url: config.url,
      method: config.method,
      data: config.data,
      headers: config.headers
    })
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    console.log('收到响应:', {
      status: response.status,
      data: response.data,
      headers: response.headers
    })
    return response
  },
  error => {
    console.error('响应错误:', {
      status: error.response?.status,
      data: error.response?.data,
      message: error.message
    })
    if (error.response) {
      if (error.response.status === 401) {
        // Token 过期或无效，清除本地存储并刷新页面
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        window.location.reload()
      }
    }
    return Promise.reject(error)
  }
)

export default service 