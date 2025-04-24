<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          登录到 GoGoPet
        </h2>
      </div>
      <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
        <div class="rounded-md shadow-sm -space-y-px">
          <div>
            <label for="username" class="sr-only">用户名</label>
            <input
              id="username"
              v-model="loginForm.userName"
              name="username"
              type="text"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-orange-500 focus:border-orange-500 focus:z-10 sm:text-sm"
              placeholder="用户名"
            />
          </div>
          <div>
            <label for="password" class="sr-only">密码</label>
            <input
              id="password"
              v-model="loginForm.password"
              name="password"
              type="password"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-orange-500 focus:border-orange-500 focus:z-10 sm:text-sm"
              placeholder="密码"
            />
          </div>
        </div>

        <div v-if="errorMessage" class="text-red-500 text-sm text-center">
          {{ errorMessage }}
        </div>

        <div>
          <button
            type="submit"
            class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-orange-600 hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500"
            :disabled="loading"
          >
            <span class="absolute left-0 inset-y-0 flex items-center pl-3">
              <i class="fas fa-sign-in-alt text-orange-500 group-hover:text-orange-400"></i>
            </span>
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { login } from '@/api/user'

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
    
    console.log('发送登录请求:', loginForm.value)
    const response = await login(loginForm.value)
    console.log('登录响应:', response)
    
    // 保存 token 到 localStorage
    localStorage.setItem('token', response.token)
    
    // 更新用户状态
    userStore.setUserInfo(response.user)
    
    // 跳转到首页
    router.push('/')
  } catch (error) {
    console.error('登录失败:', error)
    if (error.response) {
      // 服务器返回的错误
      console.error('错误状态码:', error.response.status)
      console.error('错误信息:', error.response.data)
      errorMessage.value = error.response.data.message || '登录失败，请检查用户名和密码'
    } else if (error.request) {
      // 请求发送失败
      console.error('请求失败:', error.request)
      errorMessage.value = '网络错误，请检查网络连接'
    } else {
      // 其他错误
      console.error('其他错误:', error.message)
      errorMessage.value = '登录失败，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}
</script> 