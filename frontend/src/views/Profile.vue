<template>
  <div class="profile-page">
    <div class="profile-container">
      <!-- 个人信息卡片 -->
      <div class="profile-card">
        <div class="profile-header">
          <div class="avatar-section">
            <img
              v-if="userInfo.pictureUrl"
              :src="userInfo.pictureUrl"
              :alt="userInfo.userName"
              class="profile-avatar"
            />
            <div v-else class="default-avatar">
              <i class="fas fa-user"></i>
            </div>
            <button class="upload-btn" @click="handleUploadAvatar">
              <i class="fas fa-camera"></i>
            </button>
          </div>
          <h2 class="username">{{ userInfo.userName }}</h2>
          <p class="user-type">{{ userTypeText }}</p>
        </div>

        <div class="profile-content">
          <div class="info-section">
            <h3>基本信息</h3>
            <div class="info-item">
              <label>用户名</label>
              <span>{{ userInfo.userName }}</span>
            </div>
            <div class="info-item">
              <label>用户类型</label>
              <span>{{ userTypeText }}</span>
            </div>
            <div class="info-item">
              <label>地址</label>
              <span>{{ userInfo.userAddress || '未设置' }}</span>
            </div>
          </div>

          <div class="action-section">
            <button class="edit-btn" @click="handleEditProfile">
              <i class="fas fa-edit"></i>
              编辑资料
            </button>
            <button class="change-pwd-btn" @click="handleChangePassword">
              <i class="fas fa-key"></i>
              修改密码
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)

const userTypeText = computed(() => {
  const types = {
    'U': '普通用户',
    'C': '托运公司',
    'A': '管理员'
  }
  return types[userInfo.value?.userType] || '未知'
})

const handleUploadAvatar = () => {
  // TODO: 实现头像上传功能
  console.log('上传头像')
}

const handleEditProfile = () => {
  // TODO: 实现编辑资料功能
  console.log('编辑资料')
}

const handleChangePassword = () => {
  // TODO: 实现修改密码功能
  console.log('修改密码')
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background-color: #f3f4f6;
  padding: 2rem;
}

.profile-container {
  max-width: 800px;
  margin: 0 auto;
}

.profile-card {
  background: white;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.profile-header {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  padding: 2rem;
  text-align: center;
  color: white;
}

.avatar-section {
  position: relative;
  display: inline-block;
  margin-bottom: 1rem;
}

.profile-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 4px solid white;
  object-fit: cover;
}

.default-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 4px solid white;
  background-color: #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
  color: #6b7280;
}

.upload-btn {
  position: absolute;
  bottom: 0;
  right: 0;
  background: white;
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.upload-btn:hover {
  transform: scale(1.1);
}

.username {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.user-type {
  font-size: 0.875rem;
  opacity: 0.8;
}

.profile-content {
  padding: 2rem;
}

.info-section {
  margin-bottom: 2rem;
}

.info-section h3 {
  font-size: 1.25rem;
  font-weight: bold;
  margin-bottom: 1rem;
  color: #1f2937;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 0.75rem 0;
  border-bottom: 1px solid #e5e7eb;
}

.info-item label {
  color: #6b7280;
}

.info-item span {
  color: #1f2937;
  font-weight: 500;
}

.action-section {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.edit-btn,
.change-pwd-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 0.5rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
}

.edit-btn {
  background-color: #f97316;
  color: white;
}

.change-pwd-btn {
  background-color: #e5e7eb;
  color: #1f2937;
}

.edit-btn:hover,
.change-pwd-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

@media (max-width: 640px) {
  .profile-page {
    padding: 1rem;
  }
  
  .action-section {
    flex-direction: column;
  }
}
</style> 