import axios from 'axios'

// 创建axios实例
const request = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api', // 使用环境变量配置baseURL
    timeout: 5000, // 请求超时时间
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
        // 打印请求数据，用于调试
        console.log('Request:', {
            url: config.url,
            method: config.method,
            data: config.data,
            headers: config.headers
        })
        return config
    },
    error => {
        console.error('Request Error:', error)
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    response => {
        // 打印响应数据，用于调试
        console.log('Response:', {
            status: response.status,
            data: response.data,
            headers: response.headers
        })
        return response.data
    },
    error => {
        // 打印错误信息，用于调试
        console.error('Response Error:', {
            status: error.response?.status,
            data: error.response?.data,
            message: error.message
        })

        // 处理错误响应
        if (error.response) {
            switch (error.response.status) {
                case 400:
                    // 请求参数错误
                    console.error('请求参数错误:', error.response.data)
                    break
                case 401:
                    // token无效或过期，清除用户信息并跳转到登录页
                    localStorage.removeItem('token')
                    localStorage.removeItem('userInfo')
                    window.location.href = '/login'
                    break
                case 403:
                    // 权限不足
                    console.error('权限不足')
                    break
                case 404:
                    // 资源不存在
                    console.error('请求的资源不存在')
                    break
                case 500:
                    // 服务器错误
                    console.error('服务器错误')
                    break
                default:
                    console.error('请求失败:', error.response.status)
            }
        }
        return Promise.reject(error)
    }
)

export default request 