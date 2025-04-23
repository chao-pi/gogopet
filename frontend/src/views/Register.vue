<template>
  <div class="register-container">
    <div class="register-box">
      <h2>注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="userName">用户名</label>
          <input 
            type="text" 
            id="userName" 
            v-model="userName" 
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
          <label for="confirmPassword">确认密码</label>
          <input 
            type="password" 
            id="confirmPassword" 
            v-model="confirmPassword" 
            required 
            placeholder="请再次输入密码"
          >
        </div>
        <div class="form-group">
          <label for="userType">用户类型</label>
          <select id="userType" v-model="userType" required>
            <option value="U">普通用户</option>
            <option value="C">托运公司</option>
          </select>
        </div>
        <div class="form-group">
          <label for="userAddress">地址</label>
          <input 
            type="text" 
            id="userAddress" 
            v-model="userAddress" 
            placeholder="请输入地址"
          >
        </div>
        <div class="form-group">
          <button type="submit" :disabled="loading">
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </div>
        <div class="form-group">
          <router-link to="/login">已有账号？立即登录</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from '../utils/axios'

export default {
  name: 'Register',
  data() {
    return {
      userName: '',
      password: '',
      confirmPassword: '',
      userType: 'U',
      userAddress: '',
      loading: false
    }
  },
  methods: {
    async handleRegister() {
      if (this.password !== this.confirmPassword) {
        alert('两次输入的密码不一致')
        return
      }

      this.loading = true
      try {
        const response = await axios.post('/api/auth/register', {
          userName: this.userName,
          password: this.password,
          confirmPassword: this.confirmPassword,
          userType: this.userType,
          userAddress: this.userAddress
        })
        
        console.log('注册响应:', response.data)
        alert('注册成功！')
        
        // 注册成功，跳转到登录页
        this.$router.push('/login')
      } catch (error) {
        console.error('注册错误:', error)
        if (error.response) {
          // 服务器返回了错误响应
          console.error('错误响应:', error.response.data)
          const errorMessage = typeof error.response.data === 'string' 
            ? error.response.data 
            : '注册失败，请稍后重试'
          alert(errorMessage)
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
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.register-box {
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

input, select {
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
