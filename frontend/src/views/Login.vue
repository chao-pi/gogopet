<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="flex flex-col lg:flex-row items-center justify-between gap-12">
        <!-- 品牌展示区 -->
        <div class="w-full lg:w-1/2 space-y-8">
          <div class="flex items-center gap-3">
            <svg class="w-12 h-12 text-indigo-600" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-8.707l-3-3a1 1 0 00-1.414 0l-3 3a1 1 0 001.414 1.414L9 9.414V13a1 1 0 102 0V9.414l1.293 1.293a1 1 0 001.414-1.414z" clip-rule="evenodd"></path>
            </svg>
            <h1 class="text-4xl font-bold text-gray-900">GoGoPet</h1>
          </div>
          
          <div class="space-y-4">
            <h2 class="text-3xl font-bold text-gray-900">专业宠物托运服务</h2>
            <p class="text-lg text-gray-600">让您的爱宠旅行更安全、更舒适</p>
          </div>
          
          <div class="grid grid-cols-2 gap-4">
            <div class="p-4 bg-white rounded-xl shadow-sm border border-gray-100">
              <div class="flex items-center gap-3">
                <div class="p-2 bg-indigo-50 rounded-lg">
                  <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
                  </svg>
                </div>
                <span class="font-medium text-gray-700">安全可靠</span>
              </div>
            </div>
            
            <div class="p-4 bg-white rounded-xl shadow-sm border border-gray-100">
              <div class="flex items-center gap-3">
                <div class="p-2 bg-indigo-50 rounded-lg">
                  <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
                  </svg>
                </div>
                <span class="font-medium text-gray-700">实时追踪</span>
              </div>
            </div>
            
            <div class="p-4 bg-white rounded-xl shadow-sm border border-gray-100">
              <div class="flex items-center gap-3">
                <div class="p-2 bg-indigo-50 rounded-lg">
                  <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path>
                  </svg>
                </div>
                <span class="font-medium text-gray-700">隐私保护</span>
              </div>
            </div>
            
            <div class="p-4 bg-white rounded-xl shadow-sm border border-gray-100">
              <div class="flex items-center gap-3">
                <div class="p-2 bg-indigo-50 rounded-lg">
                  <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 15a4 4 0 004 4h9a5 5 0 10-.1-9.999 5.002 5.002 0 10-9.78 2.096A4.001 4.001 0 003 15z"></path>
                  </svg>
                </div>
                <span class="font-medium text-gray-700">全程监控</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 登录表单区 -->
        <div class="w-full lg:w-1/2 max-w-md">
          <div class="bg-white rounded-2xl shadow-xl overflow-hidden">
            <div class="p-8 sm:p-10">
              <div class="text-center mb-8">
                <h2 class="text-2xl font-bold text-gray-900">欢迎回来</h2>
                <p class="mt-2 text-sm text-gray-600">请登录您的账户</p>
              </div>
              
              <form @submit.prevent="handleLogin" class="space-y-6">
                <div>
                  <label for="email" class="block text-sm font-medium text-gray-700 mb-1">用户名或邮箱</label>
                  <div class="relative rounded-md shadow-sm">
                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
                      </svg>
                    </div>
                    <input 
                      id="email"
                      v-model="form.userName"
                      type="text"
                      required
                      class="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200"
                      placeholder="请输入用户名或邮箱"
                    />
                  </div>
                </div>

                <div>
                  <label for="password" class="block text-sm font-medium text-gray-700 mb-1">密码</label>
                  <div class="relative rounded-md shadow-sm">
                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd" />
                      </svg>
                    </div>
                    <input 
                      id="password"
                      v-model="form.password"
                      type="password"
                      required
                      class="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200"
                      placeholder="请输入密码"
                    />
                  </div>
                  <div class="flex items-center justify-between mt-2">
                    <div class="flex items-center">
                      <input id="remember-me" name="remember-me" type="checkbox" class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded" />
                      <label for="remember-me" class="ml-2 block text-sm text-gray-700">记住我</label>
                    </div>
                    <div class="text-sm">
                      <a href="#" class="font-medium text-indigo-600 hover:text-indigo-500">忘记密码?</a>
                    </div>
                  </div>
                </div>

                <div>
                  <button 
                    type="submit" 
                    class="w-full flex justify-center items-center py-3 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200"
                  >
                    <svg class="-ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                      <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd" />
                    </svg>
                    登录
                  </button>
                </div>
              </form>

              <div class="mt-6">
                <div class="relative">
                  <div class="absolute inset-0 flex items-center">
                    <div class="w-full border-t border-gray-300"></div>
                  </div>
                  <div class="relative flex justify-center text-sm">
                    <span class="px-2 bg-white text-gray-500">或使用其他方式登录</span>
                  </div>
                </div>

                <div class="mt-6 grid grid-cols-2 gap-3">
                  <div>
                    <a href="#" class="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-lg shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200">
                      <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" aria-hidden="true">
                        <path fill-rule="evenodd" d="M10 0C4.477 0 0 4.477 0 10c0 4.42 2.865 8.166 6.839 9.489.5.092.682-.217.682-.482 0-.237-.008-.866-.013-1.7-2.782.603-3.369-1.342-3.369-1.342-.454-1.155-1.11-1.462-1.11-1.462-.908-.62.069-.608.069-.608 1.003.07 1.531 1.03 1.531 1.03.892 1.529 2.341 1.087 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.11-4.555-4.943 0-1.091.39-1.984 1.029-2.683-.103-.253-.446-1.27.098-2.647 0 0 .84-.269 2.75 1.025A9.564 9.564 0 0110 4.844c.85.004 1.705.114 2.504.336 1.909-1.294 2.747-1.025 2.747-1.025.546 1.377.203 2.394.1 2.647.64.699 1.028 1.592 1.028 2.683 0 3.842-2.339 4.687-4.566 4.933.359.309.678.919.678 1.852 0 1.336-.012 2.415-.012 2.743 0 .267.18.578.688.48C17.14 18.163 20 14.418 20 10c0-5.523-4.477-10-10-10z" clip-rule="evenodd" />
                      </svg>
                    </a>
                  </div>

                  <div>
                    <a href="#" class="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-lg shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200">
                      <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" aria-hidden="true">
                        <path fill-rule="evenodd" d="M10 0C4.477 0 0 4.477 0 10c0 5.523 4.477 10 10 10 5.523 0 10-4.477 10-10 0-5.523-4.477-10-10-10zm6.563 14.423c-.175.417-.575.7-1.038.7h-10.5c-.463 0-.863-.283-1.038-.7-.175-.417-.088-.888.22-1.213l5.25-5.5c.292-.306.767-.306 1.06 0l5.25 5.5c.307.325.394.796.218 1.213z" clip-rule="evenodd" />
                      </svg>
                    </a>
                  </div>
                </div>
              </div>
              
              <div class="mt-6 text-center">
                <p class="text-sm text-gray-600">
                  还没有账号？
                  <router-link to="/register" class="font-medium text-indigo-600 hover:text-indigo-500 transition-colors duration-200">
                    立即注册
                  </router-link>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { login } from '../api/user'
import { useRouter } from 'vue-router'

const router = useRouter()
const form = ref({
  userName: '',
  password: ''
})
const error = ref('')

const handleLogin = async () => {
  try {
    const response = await login(form.value)
    localStorage.setItem('token', response.token)
    router.push('/')
  } catch (err) {
    error.value = err.message || '登录失败'
  }
}
</script>

<style>
/* 自定义动画效果 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.animate-fade-in {
  animation: fadeIn 0.5s ease-out forwards;
}

/* 微交互增强 */
button, a {
  transition: all 0.2s ease;
}

button:hover {
  transform: translateY(-1px);
}

input:focus {
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2);
}
</style>