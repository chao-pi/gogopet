<template>
  <div class="login-container">
    <div class="login-box">
      <h2>登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            type="text" 
            id="username" 
            v-model="username" 
            required 
            placeholder="请输入用户名"
          >
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="password" 
            required 
            placeholder="请输入密码"
          >
        </div>
        <div class="form-group">
          <button type="submit" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </div>
        <div class="form-group">
          <router-link to="/register">还没有账号？立即注册</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from '../utils/axios'

export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true
      try {
        const response = await axios.post('/api/auth/login', {
          username: this.username,
          password: this.password
        })
        
        // 保存token和用户信息
        const token = response.data.token
        localStorage.setItem('token', token)
        localStorage.setItem('username', this.username)
        
        // 跳转到首页
        this.$router.push('/')
      } catch (error) {
        console.error('登录错误:', error)
        if (error.response) {
          // 服务器返回了错误响应
          console.error('错误响应:', error.response.data)
          alert(error.response.data || '登录失败')
        } else if (error.request) {
          // 请求发出但没有收到响应
          console.error('没有收到响应:', error.request)
          alert('无法连接到服务器，请检查网络连接')
        } else {
          // 请求配置出错
          console.error('请求错误:', error.message)
          alert('请求出错: ' + error.message)
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.login-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

h2 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 2rem;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
}

input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

button {
  width: 100%;
  padding: 0.75rem;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:disabled {
  background-color: #a8a8a8;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #3aa876;
}

a {
  color: #42b983;
  text-decoration: none;
  display: block;
  text-align: center;
  margin-top: 1rem;
}

a:hover {
  text-decoration: underline;
}
</style>
