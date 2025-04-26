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
        <router-link to="/about" class="nav-link">关于我们</router-link>
      </nav>

      <!-- 右侧用户信息 -->
      <div class="user-section">
        <template v-if="userStore.isLoggedIn">
          <UserAvatar
            :avatar-url="userStore.userInfo?.pictureUrl"
            :username="userStore.userInfo?.userName"
          />
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

const router = useRouter()
const userStore = useUserStore()
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

:deep(.user-avatar) {
  display: flex;
  align-items: center;
}

:deep(.avatar-wrapper) {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

:deep(.avatar-content) {
  display: flex;
  align-items: center;
}

:deep(.user-avatar img) {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}

:deep(.user-avatar .username) {
  color: white;
  font-size: 0.9rem;
  white-space: nowrap;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>