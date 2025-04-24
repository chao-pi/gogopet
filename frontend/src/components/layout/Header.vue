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
      <div class="ml-6 flex items-center">
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