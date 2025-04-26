<template>
  <div class="pets-page">
    <div class="pets-container">
      <!-- 添加宠物按钮 -->
      <div class="add-pet-section">
        <el-button type="primary" @click="showAddPetDialog">
          <el-icon><Plus /></el-icon>
          添加宠物
        </el-button>
      </div>

      <!-- 宠物列表 -->
      <div class="pets-list" v-loading="loading">
        <el-empty v-if="pets.length === 0" description="暂无宠物" />
        <el-row :gutter="24" v-else>
          <el-col :span="8" v-for="pet in pets" :key="pet.petId">
            <el-card class="pet-card" shadow="hover">
              <div class="pet-avatar">
                <el-avatar :size="120" :src="pet.avatarUrl">
                  <el-icon><Picture /></el-icon>
                </el-avatar>
              </div>
              <div class="pet-info">
                <h3 class="pet-name">{{ pet.petName }}</h3>
                <div class="info-item">
                  <el-icon><Collection /></el-icon>
                  <span>品种：{{ pet.petBreed }}</span>
                </div>
                <div class="info-item">
                  <el-icon><ScaleToOriginal /></el-icon>
                  <span>体重：{{ pet.petWeight }}kg</span>
                </div>
                <div class="info-item">
                  <el-icon><Calendar /></el-icon>
                  <span>年龄：{{ pet.petAge }}岁</span>
                </div>
                <div class="info-item">
                  <el-icon><User /></el-icon>
                  <span>性别：{{ pet.petGender === 'M' ? '雄性' : '雌性' }}</span>
                </div>
                <div class="info-item">
                  <el-icon><FirstAidKit /></el-icon>
                  <span>健康状态：{{ pet.petHealthStatus || '良好' }}</span>
                </div>
              </div>
              <div class="pet-actions">
                <el-button type="primary" size="large" @click="editPet(pet)">
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button type="danger" size="large" @click="handleDeletePet(pet.petId)">
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 添加/编辑宠物对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="isEdit ? '编辑宠物' : '添加宠物'"
        width="500px"
      >
        <el-form
          ref="petFormRef"
          :model="petForm"
          :rules="petFormRules"
          label-width="100px"
        >
          <el-form-item label="宠物名称" prop="petName">
            <el-input v-model="petForm.petName" placeholder="请输入宠物名称" />
          </el-form-item>
          <el-form-item label="宠物品种" prop="petBreed">
            <el-input v-model="petForm.petBreed" placeholder="请输入宠物品种" />
          </el-form-item>
          <el-form-item label="宠物体重" prop="petWeight">
            <el-input-number
              v-model="petForm.petWeight"
              :min="0.01"
              :precision="2"
              :step="0.1"
            />
          </el-form-item>
          <el-form-item label="宠物年龄" prop="petAge">
            <el-input-number
              v-model="petForm.petAge"
              :min="0"
              :precision="0"
              :step="1"
            />
          </el-form-item>
          <el-form-item label="宠物性别" prop="petGender">
            <el-radio-group v-model="petForm.petGender">
              <el-radio label="M">雄性</el-radio>
              <el-radio label="F">雌性</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="健康状态" prop="petHealthStatus">
            <el-input
              v-model="petForm.petHealthStatus"
              type="textarea"
              :rows="3"
              placeholder="请输入宠物健康状态描述"
            />
          </el-form-item>
          <el-form-item label="宠物照片">
            <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :http-request="handleAvatarUpload"
            >
              <img v-if="petForm.avatarUrl" :src="petForm.avatarUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitPetForm">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Picture, Edit, Delete, Collection, ScaleToOriginal, FirstAidKit, Calendar, User } from '@element-plus/icons-vue'
import { addPet, updatePet, deletePet as deletePetApi, getPets, uploadPetPhoto } from '@/api/pet.js'
import { useUserStore } from '@/stores/user.js'

const userStore = useUserStore()
const loading = ref(false)
const pets = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const petFormRef = ref(null)

const petForm = ref({
  petName: '',
  petBreed: '',
  petWeight: 0.01,
  petAge: 0,
  petGender: 'M',
  petHealthStatus: '',
  avatarUrl: ''
})

const petFormRules = {
  petName: [
    { required: true, message: '请输入宠物名称', trigger: 'blur' },
    { max: 63, message: '长度不能超过63个字符', trigger: 'blur' }
  ],
  petBreed: [
    { required: true, message: '请输入宠物品种', trigger: 'blur' },
    { max: 63, message: '长度不能超过63个字符', trigger: 'blur' }
  ],
  petWeight: [
    { required: true, message: '请输入宠物体重', trigger: 'blur' }
  ],
  petAge: [
    { required: true, message: '请输入宠物年龄', trigger: 'blur' }
  ],
  petGender: [
    { required: true, message: '请选择宠物性别', trigger: 'change' }
  ]
}

// 获取宠物列表
const fetchPets = async () => {
  loading.value = true
  try {
    const response = await getPets(userStore.userInfo.id)
    pets.value = response
  } catch (error) {
    console.error('获取宠物列表失败:', error)
    ElMessage.error('获取宠物列表失败')
  } finally {
    loading.value = false
  }
}

// 显示添加宠物对话框
const showAddPetDialog = () => {
  isEdit.value = false
  petForm.value = {
    petName: '',
    petBreed: '',
    petWeight: 0.01,
    petAge: 0,
    petGender: 'M',
    petHealthStatus: '',
    avatarUrl: ''
  }
  dialogVisible.value = true
}

// 编辑宠物
const editPet = (pet) => {
  isEdit.value = true
  petForm.value = { ...pet }
  dialogVisible.value = true
}

// 删除宠物
const handleDeletePet = async (petId) => {
  try {
    await ElMessageBox.confirm('确定要删除这只宠物吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deletePetApi(petId)
    ElMessage.success('删除成功')
    fetchPets()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除宠物失败:', error)
      ElMessage.error('删除宠物失败')
    }
  }
}

// 上传宠物照片前的验证
const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('上传文件只能是图片格式!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('上传图片大小不能超过 5MB!')
    return false
  }
  return true
}

// 处理宠物照片上传
const handleAvatarUpload = async ({ file }) => {
  try {
    const response = await uploadPetPhoto(file, petForm.value.petId)
    petForm.value.avatarUrl = response.avatarUrl
    ElMessage.success('上传成功')
  } catch (error) {
    console.error('上传宠物照片失败:', error)
    ElMessage.error('上传宠物照片失败')
  }
}

// 提交宠物表单
const submitPetForm = async () => {
  if (!petFormRef.value) return
  
  await petFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const petData = {
          ...petForm.value,
          userId: userStore.userInfo.id
        }
        
        if (isEdit.value) {
          await updatePet(petData)
          ElMessage.success('更新成功')
        } else {
          await addPet(petData)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        fetchPets()
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error(error.response?.data?.message || '操作失败')
      }
    }
  })
}

onMounted(() => {
  fetchPets()
})
</script>

<style scoped>
.pets-page {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.pets-container {
  max-width: 1400px;
  margin: 0 auto;
}

.add-pet-section {
  margin-bottom: 30px;
  text-align: right;
}

.pets-list {
  min-height: 200px;
}

.pet-card {
  margin-bottom: 24px;
  text-align: center;
  transition: all 0.3s;
  border-radius: 12px;
  overflow: hidden;
}

.pet-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.pet-avatar {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}

.pet-avatar :deep(.el-avatar) {
  border: 4px solid #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.pet-info {
  margin: 20px 0;
  padding: 0 20px;
}

.pet-name {
  margin: 0 0 20px 0;
  font-size: 24px;
  color: #303133;
  font-weight: 600;
}

.info-item {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 12px 0;
  color: #606266;
  font-size: 16px;
}

.info-item .el-icon {
  margin-right: 8px;
  font-size: 18px;
}

.pet-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin: 20px 0;
  padding: 0 20px;
}

.pet-actions .el-button {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.avatar-uploader {
  text-align: center;
}

.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
  margin: 0 auto;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-uploader .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .avatar-uploader-icon:hover {
  border-color: var(--el-color-primary);
}
</style>
