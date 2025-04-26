<template>
  <div class="user-avatar flex items-center">
    <div class="avatar-container relative">
      <div class="avatar-wrapper flex items-center gap-2" @click="toggleDropdown">
        <div class="avatar-content">
          <img
            v-if="avatarUrl"
            :src="avatarUrl"
            :alt="username"
            class="w-10 h-10 rounded-full object-cover"
            @error="handleImageError"
          />
          <div
            v-else
            class="default-avatar w-10 h-10 rounded-full bg-gray-300 flex items-center justify-center"
          >
            <i class="fas fa-user text-gray-600"></i>
          </div>
        </div>
        <span v-if="showUsername" class="username">{{ username }}</span>
      </div>
      
      <!-- 下拉菜单 -->
      <div v-if="isDropdownOpen" class="dropdown-menu">
        <div class="dropdown-item" @click="navigateTo('/profile')">
          <i class="fas fa-user-circle"></i>
          <span>个人中心</span>
        </div>
        <div class="dropdown-item" @click="navigateTo('/pets')">
          <i class="fas fa-paw"></i>
          <span>宠物中心</span>
        </div>
        <div class="dropdown-item" @click="handleLogout">
          <i class="fas fa-sign-out-alt"></i>
          <span>退出登录</span>
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
    default: ''
  },
  showUsername: {
    type: Boolean,
    default: true
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
          userStore.clearUserInfo()
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

.default-avatar {
  border: 2px solid rgba(255, 255, 255, 0.2);
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
  color: #1f2937;
}

.dropdown-item i {
  margin-right: 0.75rem;
  width: 1.25rem;
  text-align: center;
}

.dropdown-item:not(:last-child) {
  border-bottom: 1px solid #e5e7eb;
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
</style> 