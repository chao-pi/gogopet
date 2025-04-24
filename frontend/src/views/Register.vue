<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          注册 GoGoPet 账号
        </h2>
      </div>
      <form class="mt-8 space-y-6" @submit.prevent="handleRegister">
        <div class="rounded-md shadow-sm -space-y-px">
          <div>
            <label for="username" class="sr-only">用户名</label>
            <input
              id="username"
              v-model="registerForm.userName"
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
              v-model="registerForm.password"
              name="password"
              type="password"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-orange-500 focus:border-orange-500 focus:z-10 sm:text-sm"
              placeholder="密码"
            />
          </div>
          <div>
            <label for="confirm-password" class="sr-only">确认密码</label>
            <input
              id="confirm-password"
              v-model="registerForm.confirmPassword"
              name="confirm-password"
              type="password"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-orange-500 focus:border-orange-500 focus:z-10 sm:text-sm"
              placeholder="确认密码"
            />
          </div>
          <div>
            <label for="user-type" class="sr-only">用户类型</label>
            <select
              id="user-type"
              v-model="registerForm.userType"
              name="user-type"
              required
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-orange-500 focus:border-orange-500 focus:z-10 sm:text-sm"
            >
              <option value="">请选择用户类型</option>
              <option value="U">普通用户</option>
              <option value="C">托运公司</option>
            </select>
          </div>
          <div>
            <label for="user-address" class="sr-only">用户地址</label>
            <input
              id="user-address"
              v-model="registerForm.userAddress"
              name="user-address"
              type="text"
              class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-orange-500 focus:border-orange-500 focus:z-10 sm:text-sm"
              placeholder="用户地址（可选）"
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
              <i class="fas fa-user-plus text-orange-500 group-hover:text-orange-400"></i>
            </span>
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </div>

        <div class="text-center">
          <router-link to="/login" class="text-sm text-orange-600 hover:text-orange-500">
            已有账号？立即登录
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/user'

const router = useRouter()

const registerForm = ref({
  userName: '',
  password: '',
  confirmPassword: '',
  userType: '',
  userAddress: ''
})

const loading = ref(false)
const errorMessage = ref('')

const handleRegister = async () => {
  // 验证密码是否匹配
  if (registerForm.value.password !== registerForm.value.confirmPassword) {
    errorMessage.value = '两次输入的密码不一致'
    return
  }

  // 验证用户类型是否已选择
  if (!registerForm.value.userType) {
    errorMessage.value = '请选择用户类型'
    return
  }

  try {
    loading.value = true
    errorMessage.value = ''
    
    const response = await register({
      userName: registerForm.value.userName,
      password: registerForm.value.password,
      confirmPassword: registerForm.value.confirmPassword,
      userType: registerForm.value.userType,
      userAddress: registerForm.value.userAddress || null
    })
    
    // 注册成功后跳转到登录页
    router.push('/login')
  } catch (error) {
    console.error('注册失败:', error)
    if (error.response) {
      // 处理验证错误
      if (error.response.data && error.response.data.errors) {
        // 如果有具体的验证错误信息
        const errors = error.response.data.errors
        errorMessage.value = errors.map(err => err.defaultMessage).join('\n')
      } else {
        // 其他错误信息
        errorMessage.value = error.response.data.message || '注册失败，请稍后重试'
      }
    } else if (error.request) {
      errorMessage.value = '网络错误，请检查网络连接'
    } else {
      errorMessage.value = '注册失败，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}
</script> 