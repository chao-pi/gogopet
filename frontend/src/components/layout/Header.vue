<template>
  <header class="custom-header">
    <div class="header-container">
      <!-- 左侧Logo -->
      <div class="logo-section">
        <i class="fas fa-paw mr-2"></i>
        <div class="text-xl font-bold tracking-wide">GoGoPet</div>
      </div>

      <!-- 中间导航菜单 -->
      <nav class="nav-section">
        <router-link to="/" class="nav-link">首页</router-link>
        <router-link to="/community" class="nav-link">社区交流</router-link>
        <router-link to="/about" class="nav-link">关于我们</router-link>
      </nav>

      <!-- 右侧用户信息 -->
      <div class="user-section">
        <template v-if="userStore.isLoggedIn">
          <UserAvatar
            :avatar-url="userStore.userInfo?.pictureUrl"
            :username="userStore.userInfo?.userName"
          />
          <button @click="handleLogout" class="ml-4 nav-link">
            <i class="fas fa-sign-out-alt mr-1"></i>退出
          </button>
        </template>
        <template v-else>
          <router-link to="/login" class="nav-link">
            <i class="fas fa-sign-in-alt mr-1"></i>登录
          </router-link>
          <router-link to="/register" class="nav-link">
            <i class="fas fa-user-plus mr-1"></i>注册
          </router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import UserAvatar from '@/components/user/UserAvatar.vue'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const handleLogout = async () => {
  try {
    await ElMessageBox({
      title: '提示',
      message: '确定要退出登录吗？',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      center: true,
      customClass: 'logout-confirm',
      distinguishCancelAndClose: true,
      roundButton: true,
      confirmButtonClass: 'el-button--danger',
      beforeClose: (action, instance, done) => {
        if (action === 'confirm') {
          // 清除本地存储的token和用户信息
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          // 清除用户状态
          userStore.clearUserInfo()
          // 跳转到登录页
          router.push('/login')
        }
        done()
      }
    })
  } catch (error) {
    // 用户点击了取消按钮
    console.log('用户取消了退出操作')
  }
}
</script>

<style scoped>
.custom-header {
  background: linear-gradient(90deg, #ff9f43 0%, #e67e22 100%);
  padding: 1rem 2rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  position: relative;
  height: 60px;
}

.header-container {
  position: relative;
  height: 100%;
  width: 100%;
}

.logo-section {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
}

.nav-section {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  align-items: center;
  gap: 2rem;
}

.user-section {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-link {
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 0.25rem;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.1);
  text-decoration: none;
  transform: translateY(-2px);
}

/* 响应式布局 */
@media (max-width: 768px) {
  .custom-header {
    padding: 0.75rem 1rem;
  }
  
  .nav-section {
    display: none;
  }
  
  .nav-link {
    padding: 0.4rem 0.8rem;
  }
}

:deep(.logout-confirm) {
  .el-message-box__header {
    padding: 20px 20px 10px;
  }
  .el-message-box__content {
    padding: 20px;
  }
  .el-message-box__btns {
    padding: 10px 20px 20px;
    display: flex;
    flex-direction: row-reverse;
    justify-content: flex-start;
    gap: 10px;
  }
  .el-button {
    padding: 8px 20px;
  }
  .el-button--danger {
    background-color: #f56c6c;
    border-color: #f56c6c;
  }
  .el-button--danger:hover {
    background-color: #e74c3c;
    border-color: #e74c3c;
  }
}

:deep(.user-avatar) {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

:deep(.user-avatar img) {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}

:deep(.user-avatar .username) {
  color: white;
  font-size: 0.9rem;
}
</style>