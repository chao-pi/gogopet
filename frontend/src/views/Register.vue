<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-md mx-auto px-4 py-16">
      <div class="bg-white shadow-lg rounded-xl overflow-hidden">
        <div class="p-8">
          <div class="text-center mb-8">
            <h1 class="text-4xl font-bold text-primary-600">GoGoPet</h1>
            <p class="mt-2 text-gray-600">宠物托运智能服务系统</p>
          </div>
          
          <div class="mb-8">
            <h2 class="text-2xl font-semibold text-gray-800 text-center">创建账号</h2>
          </div>
          
          <form @submit.prevent="handleRegister" class="space-y-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
              <input 
                v-model="form.userName" 
                type="text" 
                placeholder="请输入用户名" 
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              >
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">密码</label>
              <input 
                v-model="form.password" 
                type="password" 
                placeholder="请输入密码" 
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              >
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">用户类型</label>
              <select 
                v-model="form.userType" 
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              >
                <option value="U">普通用户</option>
                <option value="C">托运公司</option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">地址</label>
              <input 
                v-model="form.userAddress" 
                type="text" 
                placeholder="请输入地址"
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              >
            </div>
            
            <button 
              type="submit" 
              class="w-full bg-primary-600 text-white py-2 px-4 rounded-md hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
            >
              注册
            </button>
          </form>
          
          <div class="mt-6 text-center">
            <p class="text-gray-600">
              已有账号？
              <router-link to="/login" class="text-primary-600 hover:text-primary-700 font-medium">
                立即登录
              </router-link>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { register } from '../api/user'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = ref({
  userName: '',
  password: '',
  userType: 'U',
  userAddress: ''
})
const error = ref('')

const handleRegister = async () => {
  try {
    await register(form.value)
    router.push('/login')
  } catch (err) {
    error.value = err.message || '注册失败'
  }
}
</script>

<style>
:root {
  --color-primary-50: #fff1f0;
  --color-primary-100: #ffe4e0;
  --color-primary-500: #ff7e5f;
  --color-primary-600: #ff6347;
  --color-primary-700: #e5573e;
}

.bg-primary-600 { background-color: var(--color-primary-600); }
.bg-primary-700 { background-color: var(--color-primary-700); }

.text-primary-600 { color: var(--color-primary-600); }
.text-primary-700 { color: var(--color-primary-700); }

.focus\:ring-primary-500:focus { --tw-ring-color: var(--color-primary-500); }
.focus\:border-primary-500:focus { border-color: var(--color-primary-500); }
</style> 