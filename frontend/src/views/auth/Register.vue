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

      <!-- 右侧注册表单 -->
      <div class="login-form-container">
        <div class="login-form">
          <h2 class="text-3xl font-bold text-gray-800 mb-2">创建账号</h2>
          <p class="text-gray-600 mb-8">加入 GoGoPet，开启您的宠物托运之旅</p>

          <form @submit.prevent="handleRegister" class="space-y-6">
            <div class="form-group">
              <div class="input-group">
                <i class="fas fa-user input-icon"></i>
                <input
                  id="username"
                  v-model="registerForm.userName"
                  type="text"
                  class="form-input"
                  placeholder="请输入用户名"
                  required
                />
              </div>
            </div>

            <div class="form-group">
              <div class="input-group">
                <i class="fas fa-lock input-icon"></i>
                <input
                  id="password"
                  v-model="registerForm.password"
                  type="password"
                  class="form-input"
                  placeholder="请输入密码"
                  required
                />
              </div>
            </div>

            <div class="form-group">
              <div class="input-group">
                <i class="fas fa-lock input-icon"></i>
                <input
                  id="confirm-password"
                  v-model="registerForm.confirmPassword"
                  type="password"
                  class="form-input"
                  placeholder="请再次输入密码"
                  required
                />
              </div>
            </div>

            <div class="form-group">
              <div class="input-group">
                <i class="fas fa-user-tag input-icon"></i>
                <select
                  id="user-type"
                  v-model="registerForm.userType"
                  class="form-input"
                  required
                >
                  <option value="">请选择用户类型</option>
                  <option value="U">普通用户</option>
                  <option value="C">托运公司</option>
                </select>
              </div>
            </div>

            <div class="form-group">
              <div class="input-group">
                <i class="fas fa-map-marker-alt input-icon"></i>
                <input
                  id="user-address"
                  v-model="registerForm.userAddress"
                  type="text"
                  class="form-input"
                  placeholder="请输入地址（可选）"
                />
              </div>
            </div>

            <button
              type="submit"
              class="submit-button"
              :disabled="loading"
            >
              <span v-if="loading" class="loading-spinner"></span>
              {{ loading ? '注册中...' : '注册' }}
            </button>

            <div class="text-center">
              <router-link to="/login" class="text-sm text-orange-600 hover:text-orange-500">
                已有账号？立即登录
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
import { register } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()

const registerForm = ref({
  userName: '',
  password: '',
  confirmPassword: '',
  userType: '',
  userAddress: ''
})

const loading = ref(false)

const handleRegister = async () => {
  // 验证密码是否匹配
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }

  // 验证用户类型是否已选择
  if (!registerForm.value.userType) {
    ElMessage.error('请选择用户类型')
    return
  }

  try {
    loading.value = true
    
    const response = await register({
      userName: registerForm.value.userName,
      password: registerForm.value.password,
      confirmPassword: registerForm.value.confirmPassword,
      userType: registerForm.value.userType,
      userAddress: registerForm.value.userAddress || null
    })
    
    // 注册成功后显示成功消息
    ElMessage.success('注册成功')
    
    // 跳转到登录页
    router.push('/login')
  } catch (error) {
    console.error('注册失败:', error)
    if (error.response) {
      // 处理验证错误
      if (error.response.data && error.response.data.errors) {
        // 如果有具体的验证错误信息
        const errors = error.response.data.errors
        ElMessage.error(errors.map(err => err.defaultMessage).join('\n'))
      } else {
        // 其他错误信息
        ElMessage.error(error.response.data.message || '注册失败，请稍后重试')
      }
    } else if (error.request) {
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      ElMessage.error('注册失败，请稍后重试')
    }
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