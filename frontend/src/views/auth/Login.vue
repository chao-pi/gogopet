<template>
  <div class="login-container">
    <div class="login-content">
      <!-- 左侧装饰区域 -->
      <div class="login-decoration">
        <div class="decoration-content">
          <h1 class="text-5xl font-bold text-white mb-6">GoGoPet</h1>
          <p class="text-xl text-white/80 mb-8">让宠物托运更安全、更透明、更智能</p>
          <div class="feature-list">
            <div class="feature-item">
              <i class="fas fa-paw text-2xl text-orange-400"></i>
              <span class="text-white">安全可靠的宠物托运</span>
            </div>
            <div class="feature-item">
              <i class="fas fa-map-marker-alt text-2xl text-orange-400"></i>
              <span class="text-white">实时追踪宠物位置</span>
            </div>
            <div class="feature-item">
              <i class="fas fa-comments text-2xl text-orange-400"></i>
              <span class="text-white">专业的客服支持</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-form-container">
        <div class="login-form">
          <h2 class="text-3xl font-bold text-gray-800 mb-2">欢迎回来</h2>
          <!-- <p class="text-gray-600 mb-8">请登录您的账号</p> -->

          <form @submit.prevent="handleLogin" class="space-y-6">
            <div class="form-group">
              <!-- <label for="username" class="form-label">用户名</label> -->
              <div class="input-group">
                <i class="fas fa-user input-icon"></i>
                <input
                  id="username"
                  v-model="loginForm.userName"
                  type="text"
                  class="form-input"
                  placeholder="请输入用户名"
                  required
                />
              </div>
            </div>

            <div class="form-group">
              <!-- <label for="password" class="form-label">密码</label> -->
              <div class="input-group">
                <i class="fas fa-lock input-icon"></i>
                <input
                  id="password"
                  v-model="loginForm.password"
                  type="password"
                  class="form-input"
                  placeholder="请输入密码"
                  required
                />
              </div>
            </div>

            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <input
                  id="remember-me"
                  type="checkbox"
                  class="form-checkbox"
                />
                <label for="remember-me" class="ml-2 text-sm text-gray-600">
                  记住我
                </label>
              </div>
              <a href="#" class="text-sm text-orange-600 hover:text-orange-500">
                忘记密码？
              </a>
            </div>

            <button
              type="submit"
              class="submit-button"
              :disabled="loading"
            >
              <span v-if="loading" class="loading-spinner"></span>
              {{ loading ? '登录中...' : '登录' }}
            </button>

            <div class="text-center">
              <router-link to="/register" class="text-sm text-orange-600 hover:text-orange-500">
                没有账号？立即注册
              </router-link>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loginForm = ref({
  userName: '',
  password: ''
})

const loading = ref(false)
const errorMessage = ref('')

const handleLogin = async () => {
  try {
    loading.value = true
    errorMessage.value = ''
    
    // 打印请求参数
    console.log('登录请求参数:', {
      userName: loginForm.value.userName,
      password: loginForm.value.password
    })
    
    const response = await login(loginForm.value)
    
    // 打印响应数据
    console.log('登录响应:', response)
    
    if (response.token && response.user) {
      try {
        // 保存用户信息和token
        console.log('保存用户信息:', response.user)
        userStore.setUserInfo(response.user)
        console.log('保存token:', response.token)
        userStore.setToken(response.token)
        
        // 显示成功消息
        ElMessage.success('登录成功')
        
        // 跳转到首页
        console.log('准备跳转到首页')
        router.push('/')
      } catch (storeError) {
        console.error('保存用户信息失败:', storeError)
        // 即使保存失败，也尝试跳转到首页
        router.push('/')
      }
    } else {
      errorMessage.value = '登录响应数据格式错误'
      ElMessage.error(errorMessage.value)
    }
  } catch (error) {
    console.error('登录失败:', error)
    // 打印详细的错误信息
    console.error('错误详情:', {
      message: error.message,
      response: error.response,
      request: error.request
    })
    
    if (error.response) {
      // 服务器返回了错误响应
      const errorData = error.response.data
      console.error('服务器错误响应:', errorData)
      errorMessage.value = errorData.message || '登录失败，请检查用户名和密码'
    } else if (error.request) {
      // 请求已发出但没有收到响应
      console.error('网络错误:', error.request)
      errorMessage.value = '网络错误，请检查网络连接'
    } else {
      // 请求配置出错
      console.error('请求配置错误:', error.message)
      errorMessage.value = '请求配置错误，请稍后重试'
    }
    
    ElMessage.error(errorMessage.value)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f9fafb 0%, #f3f4f6 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}

.login-content {
  display: flex;
  width: 100%;
  max-width: 1200px;
  background: white;
  border-radius: 1.5rem;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  overflow: hidden;
}

.login-decoration {
  flex: 1;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  padding: 4rem;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.login-decoration::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.1'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  opacity: 0.5;
}

.decoration-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin-top: 2rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 0.5rem;
  backdrop-filter: blur(5px);
  transition: transform 0.3s ease;
}

.feature-item:hover {
  transform: translateX(10px);
}

.login-form-container {
  flex: 1;
  padding: 4rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-form {
  width: 100%;
  max-width: 400px;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: #4b5563;
}

.input-group {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
}

.form-input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  transition: all 0.3s ease;
}

.form-input:focus {
  outline: none;
  border-color: #f97316;
  box-shadow: 0 0 0 3px rgba(249, 115, 22, 0.1);
}

.form-checkbox {
  width: 1rem;
  height: 1rem;
  border-radius: 0.25rem;
  border: 1px solid #e5e7eb;
  transition: all 0.3s ease;
}

.form-checkbox:checked {
  background-color: #f97316;
  border-color: #f97316;
}

.submit-button {
  width: 100%;
  padding: 0.75rem;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  color: white;
  border: none;
  border-radius: 0.5rem;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(249, 115, 22, 0.1), 0 2px 4px -1px rgba(249, 115, 22, 0.06);
}

.submit-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  display: inline-block;
  width: 1rem;
  height: 1rem;
  border: 2px solid currentColor;
  border-radius: 50%;
  border-top-color: transparent;
  animation: spin 1s linear infinite;
  margin-right: 0.5rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .login-content {
    flex-direction: column;
  }

  .login-decoration {
    padding: 2rem;
  }

  .login-form-container {
    padding: 2rem;
  }
}
</style> 