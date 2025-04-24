<template>
  <header class="custom-header text-white flex justify-between items-center">
    <div class="flex items-center">
      <i class="fas fa-paw mr-2"></i> <!-- 宠物爪印图标 -->
      <div class="text-xl font-bold tracking-wide">GoGoPet</div>
    </div>
    <div class="flex items-center">
      <nav class="md:flex hidden">
        <router-link to="/" class="nav-link">首页</router-link>
        <router-link to="/about" class="nav-link">关于我们</router-link>
      </nav>
      <div class="ml-6">
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
  padding: 1.5rem 2rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
}

.custom-header:hover {
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.nav-link {
  color: white;
  margin-left: 1.5rem;
  padding: 0.5rem 1rem;
  border-radius: 0.25rem;
  transition: all 0.3s ease;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.1);
  text-decoration: none;
  transform: translateY(-2px);
}
</style>