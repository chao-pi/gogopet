<template>
  <div class="post-form">
    <div class="author-info">
      <img :src="userStore.userInfo?.userAvatar || '/src/assets/images/default-avatar.svg'" :alt="userStore.userInfo?.userName" class="author-avatar">
      <span class="author-name">{{ userStore.userInfo?.userName }}</span>
    </div>

    <el-form :model="formData" :rules="rules" ref="formRef" class="form-container">
      <el-form-item label="标题" prop="postTitle">
        <el-input 
          v-model="formData.postTitle" 
          placeholder="请输入帖子标题"
          class="custom-input"
        />
      </el-form-item>
      
      <el-form-item label="内容" prop="postContent">
        <el-input
          v-model="formData.postContent"
          type="textarea"
          :rows="6"
          placeholder="分享你的宠物故事..."
          class="custom-textarea"
        />
      </el-form-item>

      <el-form-item label="图片" class="upload-section">
        <div class="upload-tip">
          <i class="fas fa-image"></i>
          <span>最多上传9张图片</span>
        </div>
        <el-upload
          v-model:file-list="fileList"
          action="#"
          list-type="picture-card"
          :auto-upload="false"
          :on-preview="handlePictureCardPreview"
          :on-remove="handleRemove"
          :on-change="handleChange"
          :limit="9"
          multiple
          class="custom-upload"
        >
          <template #default>
            <div class="upload-trigger">
              <el-icon><Plus /></el-icon>
              <span>点击上传</span>
            </div>
          </template>
          <template #file="{ file }">
            <div class="upload-item">
              <img :src="file.url" class="upload-image" />
              <div class="upload-actions">
                <el-icon class="upload-action" @click="handlePictureCardPreview(file)">
                  <ZoomIn />
                </el-icon>
                <el-icon class="upload-action" @click="handleRemove(file, fileList)">
                  <Delete />
                </el-icon>
              </div>
            </div>
          </template>
        </el-upload>
      </el-form-item>
      
      <el-form-item>
        <div class="form-actions">
          <el-button @click="$emit('cancel')" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="handleSubmit" class="submit-btn">{{ isEditing ? '保存修改' : '发布' }}</el-button>
        </div>
      </el-form-item>
    </el-form>

    <!-- 图片预览 -->
    <el-dialog v-model="dialogVisible" title="预览" class="preview-dialog">
      <img w-full :src="dialogImageUrl" alt="Preview Image" class="preview-image" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, ZoomIn, Delete } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { uploadImage } from '@/api/upload'

const props = defineProps({
  post: {
    type: Object,
    default: null
  }
})

const formRef = ref(null)
const fileList = ref([])
const dialogImageUrl = ref('')
const dialogVisible = ref(false)
const uploading = ref(false)
const isEditing = ref(false)

const userStore = useUserStore()

const formData = reactive({
  postTitle: '',
  postContent: '',
  postImages: ''
})

const rules = {
  postTitle: [
    { required: true, message: '请输入帖子标题', trigger: 'blur' },
    { min: 2, max: 50, message: '标题长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  postContent: [
    { required: true, message: '请输入帖子内容', trigger: 'blur' },
    { min: 10, max: 1000, message: '内容长度在 10 到 1000 个字符', trigger: 'blur' }
  ]
}

// 初始化表单数据
const initFormData = () => {
  if (props.post) {
    isEditing.value = true
    formData.postTitle = props.post.postTitle
    formData.postContent = props.post.postContent
    formData.postImages = props.post.postImages || ''
    
    // 初始化图片列表
    if (props.post.postImages) {
      fileList.value = props.post.postImages.split(',').map(url => ({
        name: url.split('/').pop(),
        url: url
      }))
    }
  }
}

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

const handleRemove = (file, fileList) => {
  formData.postImages = fileList.map(file => file.url).join(',')
}

const handleChange = async (file, fileList) => {
  if (uploading.value) return
  
  try {
    uploading.value = true
    // 创建FormData对象
    const formData = new FormData()
    formData.append('file', file.raw)
    formData.append('userId', userStore.userInfo?.id)
    
    // 调用上传API
    const response = await uploadImage(formData)
    file.url = response.data.imageUrl
    formData.postImages = fileList.map(file => file.url).join(',')
  } catch (error) {
    console.error('图片上传失败:', error)
    ElMessage.error('图片上传失败')
  } finally {
    uploading.value = false
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    // 确保数据格式正确
    const submitData = {
      postTitle: formData.postTitle,
      postContent: formData.postContent,
      postImages: formData.postImages,
      userId: userStore.userInfo?.id
    }
    emit('submit', submitData)
    formData.postTitle = ''
    formData.postContent = ''
    formData.postImages = ''
    fileList.value = []
  } catch (error) {
    ElMessage.error('请检查表单填写是否正确')
  }
}

const emit = defineEmits(['submit', 'cancel'])

onMounted(() => {
  initFormData()
})
</script>

<style scoped>
.post-form {
  padding: 20px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
}

.author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.author-name {
  font-weight: 500;
  color: #333;
  font-size: 1.1rem;
}

.form-container {
  max-width: 800px;
  margin: 0 auto;
}

.custom-input :deep(.el-input__inner) {
  border-radius: 8px;
  padding: 12px 16px;
  font-size: 1.1rem;
  border: 2px solid #eee;
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__inner:focus) {
  border-color: #e67e22;
  box-shadow: 0 0 0 3px rgba(230, 126, 34, 0.1);
}

.custom-textarea :deep(.el-textarea__inner) {
  border-radius: 8px;
  padding: 12px 16px;
  font-size: 1.1rem;
  border: 2px solid #eee;
  transition: all 0.3s ease;
  resize: vertical;
}

.custom-textarea :deep(.el-textarea__inner:focus) {
  border-color: #e67e22;
  box-shadow: 0 0 0 3px rgba(230, 126, 34, 0.1);
}

.upload-section {
  margin-top: 20px;
}

.upload-tip {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  margin-bottom: 12px;
}

.upload-tip i {
  color: #e67e22;
}

.custom-upload {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.upload-trigger {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  height: 100%;
  color: #666;
  transition: all 0.3s ease;
}

.upload-trigger:hover {
  color: #e67e22;
}

.upload-item {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
}

.upload-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-actions {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.upload-item:hover .upload-actions {
  opacity: 1;
}

.upload-action {
  color: white;
  font-size: 1.2rem;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.upload-action:hover {
  transform: scale(1.2);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 24px;
}

.cancel-btn {
  padding: 12px 24px;
  font-size: 1.1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.submit-btn {
  padding: 12px 24px;
  font-size: 1.1rem;
  border-radius: 8px;
  background: linear-gradient(45deg, #ff9f43, #e67e22);
  border: none;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(230, 126, 34, 0.2);
}

.preview-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.preview-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  font-size: 1.1rem;
  color: #2c3e50;
}

@media (max-width: 768px) {
  .post-form {
    padding: 10px;
  }
  
  .custom-upload {
    justify-content: center;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .cancel-btn,
  .submit-btn {
    width: 100%;
  }
}
</style> 