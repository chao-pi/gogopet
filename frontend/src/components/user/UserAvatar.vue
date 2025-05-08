<template>
  <div class="user-avatar">
    <div class="avatar-container">
      <div class="avatar-wrapper"
        @click="toggleDropdown"
        @mouseenter="openDropdown"
        @mouseleave="closeDropdown"
      >
        <div class="avatar-content">
          <div v-if="avatarUrl" class="avatar-image">
            <img
              :src="avatarUrl"
              :alt="username"
              @error="handleImageError"
            />
            <div class="default-avatar" style="display: none">
              <i class="fas fa-user"></i>
            </div>
          </div>
          <div v-else class="default-avatar">
            <i class="fas fa-user"></i>
          </div>
          <span class="username">{{ username }}</span>
        </div>
      </div>

      <transition name="fade-dropdown">
        <div v-if="isDropdownOpen" class="dropdown-menu"
          @mouseenter="openDropdown"
          @mouseleave="closeDropdown"
        >
          <div class="dropdown-item" @click="navigateTo('/profile')">
            <i class="fas fa-user mr-2"></i>
            个人中心
          </div>
          <div v-if="userStore.userInfo?.userType==='U'" class="dropdown-item" @click="navigateTo('/pets')">
            <i class="fas fa-paw mr-2"></i>
            我的宠物
          </div>
          <div class="dropdown-item" @click="handleLogout">
            <i class="fas fa-sign-out-alt mr-2"></i>
            退出登录
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const props = defineProps({
  avatarUrl: {
    type: String,
    default: ''
  },
  username: {
    type: String,
    required: true
  }
})

const router = useRouter()
const userStore = useUserStore()
const isDropdownOpen = ref(false)
let hoverTimeout = null

const openDropdown = () => {
  clearTimeout(hoverTimeout)
  isDropdownOpen.value = true
}
const closeDropdown = () => {
  hoverTimeout = setTimeout(() => {
    isDropdownOpen.value = false
  }, 200)
}

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const navigateTo = (path) => {
  isDropdownOpen.value = false
  router.push(path)
}

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
          isDropdownOpen.value = false
          userStore.logout()
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

const handleImageError = (e) => {
  e.target.style.display = 'none'
  e.target.parentElement.querySelector('.default-avatar').style.display = 'flex'
}
</script>

<style scoped>
.user-avatar {
  cursor: pointer;
  transform: translateX(-60px);
}

.avatar-container {
  position: relative;
}

.avatar-wrapper {
  transition: transform 0.3s ease;
}

.avatar-wrapper:hover {
  transform: scale(1.05);
}

.avatar-content {
  display: flex;
  align-items: center;
}

.avatar-image {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 8px;
}

.avatar-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.default-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #f3f4f6;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 8px;
  color: #6b7280;
}

.username {
  color: white;
  font-size: 0.9rem;
  white-space: nowrap;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 50%;
  transform: translateX(50%);
  margin-top: 0.5rem;
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  min-width: 200px;
  z-index: 1000;
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  color: #4b5563;
  cursor: pointer;
  transition: all 0.2s ease;
}

.dropdown-item:hover {
  background-color: #f3f4f6;
}

.dropdown-item i {
  width: 20px;
  text-align: center;
  margin-right: 8px;
}

/* 渐变动画 */
.fade-dropdown-enter-active, .fade-dropdown-leave-active {
  transition: opacity 0.3s;
}
.fade-dropdown-enter-from, .fade-dropdown-leave-to {
  opacity: 0;
}
.fade-dropdown-enter-to, .fade-dropdown-leave-from {
  opacity: 1;
}
</style> 