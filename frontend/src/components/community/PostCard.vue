<template>
  <div class="post-card" @click="$emit('click')">
    <div class="post-header">
      <div class="author-info">
        <div class="avatar-wrapper">
          <img :src="post.userAvatar || '/src/assets/images/default-avatar.svg'" :alt="post.userName" class="author-avatar">
        </div>
        <div class="author-details">
          <span class="author-name">{{ post.userName }}</span>
          <span class="post-time">{{ formatDate(post.createTime) }}</span>
        </div>
      </div>
      <el-dropdown v-if="isPostOwner" trigger="click" @command="handleCommand">
        <el-button type="text" class="more-actions" @click.stop>
          <el-icon><More /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="edit">编辑帖子</el-dropdown-item>
            <el-dropdown-item command="delete" divided>删除帖子</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    
    <div class="post-content">
      <h3 class="post-title">{{ post.postTitle }}</h3>
      <p class="post-text">{{ post.postContent }}</p>
      
      <!-- 图片展示 -->
      <div v-if="post.images && post.images.length > 0" class="post-images">
        <el-image
          v-for="(image, index) in post.images"
          :key="index"
          :src="image"
          :preview-src-list="post.images"
          fit="cover"
          class="post-image"
          loading="lazy"
        >
          <template #placeholder>
            <div class="image-placeholder">
              <el-icon><Picture /></el-icon>
            </div>
          </template>
        </el-image>
      </div>
    </div>
    
    <div class="post-footer">
      <div class="post-stats">
        <div 
          class="stat-item" 
          :class="{ 'liked': post.isLiked }"
          @click.stop="handleLike"
        >
          <i class="fas fa-heart" :class="{ 'liked': post.isLiked }"></i>
          <span class="stat-count">{{ post.postLikes }}</span>
        </div>
        <div class="stat-item">
          <i class="fas fa-comment"></i>
          <span class="stat-count">{{ post.postComment }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Picture, More } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { likePost, unlikePost, checkUserLiked } from '@/api/post'
import { useUserStore } from '@/stores/user'

const props = defineProps({
  post: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['click', 'edit', 'delete', 'like', 'unlike'])
const userStore = useUserStore()
const isLiked = ref(false)

// 判断是否是帖子所有者
const isPostOwner = computed(() => {
  return userStore.isLoggedIn && userStore.userInfo?.id === props.post.userId
})

// 处理更多操作
const handleCommand = async (command) => {
  switch (command) {
    case 'edit':
      emit('edit', props.post)
      break
    case 'delete':
      try {
        await ElMessageBox.confirm(
          '确定要删除这条帖子吗？',
          '删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        emit('delete', props.post.postId)
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
      break
  }
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return d.toLocaleString()
}

// 处理点赞
const handleLike = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }

  try {
    if (props.post.isLiked) {
      emit('unlike', props.post.postId)
    } else {
      emit('like', props.post.postId)
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 检查是否已点赞
const checkLiked = async () => {
  if (!userStore.isLoggedIn) return
  try {
    const result = await checkUserLiked(props.post.postId, userStore.userInfo.id)
    props.post.isLiked = result.data
  } catch (error) {
    console.error('检查点赞状态失败', error)
  }
}

onMounted(() => {
  checkLiked()
})
</script>

<style scoped>
.post-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.post-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.post-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(45deg, #ff9f43, #e67e22);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.post-card:hover::before {
  opacity: 1;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar-wrapper {
  position: relative;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.author-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.post-card:hover .author-avatar {
  transform: scale(1.1);
}

.author-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.author-name {
  font-weight: 600;
  color: #2c3e50;
  font-size: 1.1rem;
}

.post-time {
  color: #666;
  font-size: 0.9rem;
}

.more-actions {
  padding: 8px;
  font-size: 1.2rem;
  color: #666;
  transition: all 0.3s ease;
}

.more-actions:hover {
  color: #e67e22;
  transform: scale(1.1);
}

.post-content {
  margin-bottom: 20px;
}

.post-title {
  font-size: 1.4rem;
  font-weight: 600;
  margin-bottom: 12px;
  color: #2c3e50;
  line-height: 1.4;
}

.post-text {
  color: #666;
  line-height: 1.6;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-images {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
  margin-top: 16px;
}

.post-image {
  width: 100%;
  height: 200px;
  border-radius: 12px;
  object-fit: cover;
  transition: transform 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.post-image:hover {
  transform: scale(1.02);
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
  color: #666;
  font-size: 2rem;
}

.post-footer {
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid #eee;
  padding-top: 16px;
}

.post-stats {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.stat-item i {
  font-size: 18px;
  color: #666;
  transition: all 0.3s ease;
}

.stat-item i.liked {
  color: #ff6b6b;
  animation: likeAnimation 0.5s ease;
}

.stat-item:hover i {
  transform: scale(1.1);
}

.stat-count {
  font-size: 14px;
  color: #666;
}

@keyframes likeAnimation {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

@media (max-width: 768px) {
  .post-card {
    padding: 16px;
  }
  
  .post-images {
    grid-template-columns: 1fr;
  }
  
  .post-image {
    height: 250px;
  }
  
  .stat-item {
    padding: 6px 10px;
  }
}
</style> 