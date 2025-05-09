<template>
  <div class="profile-page">
    <div class="profile-container">
      <!-- 个人信息卡片 -->
      <div class="profile-card">
        <div class="profile-header">
          <div class="avatar-section">
            <div class="avatar-wrapper">
              <img
                v-if="userInfo.avatarUrl"
                :src="userInfo.avatarUrl"
                :alt="userInfo.userName"
                class="profile-avatar"
              />
              <div v-else class="default-avatar">
                <i class="fas fa-user"></i>
              </div>
              <div class="upload-overlay">
                <button class="upload-btn" @click="handleUploadAvatar">
                  <i class="fas fa-camera"></i>
                  <span>更换头像</span>
                </button>
              </div>
            </div>
          </div>
          <h2 class="username">{{ userInfo.userName }}</h2>
          <p class="user-type">{{ userTypeText }}</p>
        </div>

        <div class="profile-content">
          <div class="info-section">
            <h3>基本信息</h3>
            <div v-if="!isEditing && !isChangingPassword" class="info-items">
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
            <div v-else-if="isEditing" class="edit-form">
              <div class="form-group">
                <label>用户名</label>
                <input
                  v-model="editForm.userName"
                  type="text"
                  class="form-input"
                  placeholder="请输入用户名"
                />
              </div>
              <div class="form-group">
                <label>地址</label>
                <input
                  v-model="editForm.userAddress"
                  type="text"
                  class="form-input"
                  placeholder="请输入地址"
                />
              </div>
            </div>
            <div v-else-if="isChangingPassword" class="edit-form">
              <div class="form-group">
                <label>旧密码</label>
                <input
                  v-model="passwordForm.oldPassword"
                  type="password"
                  class="form-input"
                  placeholder="请输入旧密码"
                />
              </div>
              <div class="form-group">
                <label>新密码</label>
                <input
                  v-model="passwordForm.newPassword"
                  type="password"
                  class="form-input"
                  placeholder="请输入新密码"
                />
              </div>
              <div class="form-group">
                <label>确认新密码</label>
                <input
                  v-model="passwordForm.confirmPassword"
                  type="password"
                  class="form-input"
                  placeholder="请再次输入新密码"
                />
              </div>
            </div>
          </div>

          <div class="action-section">
            <template v-if="!isEditing && !isChangingPassword">
              <button class="edit-btn" @click="startEditing">
                <i class="fas fa-edit"></i>
                编辑资料
              </button>
              <button class="change-pwd-btn" @click="startChangePassword">
                <i class="fas fa-key"></i>
                修改密码
              </button>
            </template>
            <template v-else-if="isEditing">
              <button class="save-btn" @click="handleSaveProfile">
                <i class="fas fa-save"></i>
                保存修改
              </button>
              <button class="cancel-btn" @click="cancelEditing">
                <i class="fas fa-times"></i>
                取消
              </button>
            </template>
            <template v-else-if="isChangingPassword">
              <button class="save-btn" @click="submitChangePassword">
                <i class="fas fa-save"></i>
                保存修改
              </button>
              <button class="cancel-btn" @click="cancelChangePassword">
                <i class="fas fa-times"></i>
                取消
              </button>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useUserStore } from '@/stores/user.js'
import { updateUserInfo, changePassword, uploadAvatar } from '@/api/user.js'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)
const isEditing = ref(false)
const isChangingPassword = ref(false)

const editForm = ref({
  userName: '',
  userAddress: ''
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const userTypeText = computed(() => {
  const types = {
    'U': '普通用户',
    'C': '托运公司',
    'A': '管理员'
  }
  return types[userInfo.value?.userType] || '未知'
})

const startEditing = () => {
  editForm.value = {
    userName: userInfo.value.userName,
    userAddress: userInfo.value.userAddress || ''
  }
  isEditing.value = true
  isChangingPassword.value = false
}

const cancelEditing = () => {
  isEditing.value = false
}

const startChangePassword = () => {
  // 重置表单
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
  isChangingPassword.value = true
  isEditing.value = false
}

const cancelChangePassword = () => {
  isChangingPassword.value = false
}

const handleSaveProfile = async () => {
  try {
    // 验证用户名是否为空
    if (!editForm.value.userName.trim()) {
      ElMessage.error('用户名不能为空')
      return
    }

    // 调用API更新用户信息
    await updateUserInfo({
      userId: userInfo.value.id,
      userName: editForm.value.userName,
      userAddress: editForm.value.userAddress
    })

    // 更新本地用户信息
    userStore.setUserInfo({
      ...userInfo.value,
      userName: editForm.value.userName,
      userAddress: editForm.value.userAddress
    })

    ElMessage.success('资料更新成功')
    isEditing.value = false
  } catch (error) {
    console.error('更新用户信息失败:', error)
    ElMessage.error(error.response?.data?.message || '更新失败，请稍后重试')
  }
}

const handleUploadAvatar = () => {
  // 创建文件输入元素
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'image/*'
  
  // 监听文件选择
  input.onchange = async (e) => {
    const file = e.target.files[0]
    if (!file) return
    
    // 验证文件类型
    if (!file.type.startsWith('image/')) {
      ElMessage.error('请选择图片文件')
      return
    }
    
    // 验证文件大小（限制为5MB）
    if (file.size > 5 * 1024 * 1024) {
      ElMessage.error('图片大小不能超过5MB')
      return
    }
    
    try {
      // 上传头像
      const response = await uploadAvatar(file, userInfo.value.id)
      
      // 更新用户信息
      userStore.setUserInfo({
        ...userInfo.value,
        avatarUrl: response.pictureUrl  // 使用 pictureUrl 作为 avatarUrl
      })
      
      ElMessage.success('头像上传成功')
    } catch (error) {
      console.error('上传头像失败:', error)
      ElMessage.error(error.response?.data?.message || '上传失败，请稍后重试')
    }
  }
  
  // 触发文件选择
  input.click()
}

const submitChangePassword = async () => {
  try {
    // 验证表单
    if (!passwordForm.value.oldPassword) {
      ElMessage.error('请输入旧密码')
      return
    }
    if (!passwordForm.value.newPassword) {
      ElMessage.error('请输入新密码')
      return
    }
    if (!passwordForm.value.confirmPassword) {
      ElMessage.error('请确认新密码')
      return
    }
    if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
      ElMessage.error('两次输入的密码不一致')
      return
    }
    if (passwordForm.value.newPassword.length < 6) {
      ElMessage.error('新密码长度不能少于6个字符')
      return
    }

    // 调用API修改密码
    await changePassword({
      userId: userInfo.value.id,
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword,
      confirmPassword: passwordForm.value.confirmPassword
    })

    ElMessage.success('密码修改成功')
    isChangingPassword.value = false
  } catch (error) {
    console.error('修改密码失败:', error)
    ElMessage.error(error.response?.data?.message || '修改失败，请稍后重试')
  }
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

.avatar-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  border: 4px solid white;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.avatar-wrapper:hover {
  transform: scale(1.05);
}

.avatar-wrapper:hover .upload-overlay {
  opacity: 1;
}

.profile-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.default-avatar {
  width: 100%;
  height: 100%;
  background-color: #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
  color: #6b7280;
}

.upload-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.upload-btn {
  background: transparent;
  border: none;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload-btn i {
  font-size: 1.5rem;
}

.upload-btn span {
  font-size: 0.75rem;
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

.info-items {
  display: flex;
  flex-direction: column;
  gap: 1rem;
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

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  color: #6b7280;
  font-size: 0.875rem;
}

.form-input {
  padding: 0.5rem 0.75rem;
  border: 1px solid #e5e7eb;
  border-radius: 0.375rem;
  transition: all 0.3s ease;
}

.form-input:focus {
  outline: none;
  border-color: #f97316;
  box-shadow: 0 0 0 3px rgba(249, 115, 22, 0.1);
}

.action-section {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.edit-btn,
.change-pwd-btn,
.save-btn,
.cancel-btn {
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

.save-btn {
  background-color: #10b981;
  color: white;
}

.cancel-btn {
  background-color: #e5e7eb;
  color: #1f2937;
}

.edit-btn:hover,
.change-pwd-btn:hover,
.save-btn:hover,
.cancel-btn:hover {
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