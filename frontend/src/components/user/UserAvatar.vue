<template>
  <div class="user-avatar">
    <div class="avatar-container">
      <div class="avatar-wrapper" @click="toggleDropdown">
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

      <div v-if="isDropdownOpen" class="dropdown-menu">
        <div class="dropdown-item" @click="navigateTo('/profile')">
          <i class="fas fa-user mr-2"></i>
          个人中心
        </div>
        <div class="dropdown-item" @click="navigateTo('/pets')">
          <i class="fas fa-paw mr-2"></i>
          我的宠物
        </div>
        <div class="dropdown-item" @click="navigateTo('/orderManagement')">
          <i class="fas fa-clipboard-list mr-2"></i>
          订单管理
        </div>
        <div class="dropdown-item" @click="handleLogout">
          <i class="fas fa-sign-out-alt mr-2"></i>
          退出登录
        </div>
      </div>
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

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const navigateTo = (path) => {
  isDropdownOpen.value = false
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  const userType = userInfo.userType

  if (path === '/orderManagement' && userType === 'C') {
    router.push('/companyOrderManagement')
  } else {
    router.push(path)
  }
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
  right: 0;
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
</style> 