<template>
  <div class="error-container">
    <div class="error-content">
      <div class="error-icon">
        <i class="fas fa-exclamation-circle"></i>
      </div>
      <h1 class="error-title">{{ errorTitle }}</h1>
      <p class="error-message">{{ errorMessage }}</p>
      <div class="error-actions">
        <button class="btn-primary" @click="goBack">返回上一页</button>
        <button class="btn-secondary" @click="goHome">返回首页</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ErrorPage',
  data() {
    return {
      errorCode: null,
      errorTitle: '页面未找到',
      errorMessage: '您访问的页面不存在或已被移除。'
    }
  },
  created() {
    // 从路由参数中获取错误代码
    this.errorCode = this.$route.params.code || '404';
    
    // 根据错误代码设置不同的错误信息
    switch(this.errorCode) {
      case '403':
        this.errorTitle = '访问被拒绝';
        this.errorMessage = '您没有权限访问此页面。';
        break;
      case '404':
        this.errorTitle = '页面未找到';
        this.errorMessage = '您访问的页面不存在或已被移除。';
        break;
      case '500':
        this.errorTitle = '服务器错误';
        this.errorMessage = '服务器内部错误，请稍后再试。';
        break;
      default:
        this.errorTitle = '发生错误';
        this.errorMessage = '抱歉，处理您的请求时发生错误。';
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    goHome() {
      this.$router.push('/');
    }
  }
}
</script>

<style scoped>
.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f8f9fa;
  padding: 20px;
}

.error-content {
  text-align: center;
  max-width: 500px;
  padding: 40px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.error-icon {
  font-size: 64px;
  color: #dc3545;
  margin-bottom: 20px;
}

.error-title {
  font-size: 28px;
  color: #343a40;
  margin-bottom: 16px;
}

.error-message {
  font-size: 16px;
  color: #6c757d;
  margin-bottom: 24px;
}

.error-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
}

button {
  padding: 10px 20px;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #007bff;
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: #0069d9;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
  border: none;
}

.btn-secondary:hover {
  background-color: #5a6268;
}
</style> 