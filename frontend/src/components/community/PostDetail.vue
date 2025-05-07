<template>
  <div class="post-detail">
    <!-- 帖子内容 -->
    <div class="post-content">
      <div class="post-header">
        <div class="author-info">
          <img :src="postDetail?.userAvatar || '/src/assets/images/default-avatar.svg'" :alt="postDetail?.userName" class="author-avatar">
          <div class="author-details">
            <span class="author-name">{{ postDetail?.userName }}</span>
            <span class="post-time">{{ formatDate(postDetail?.createTime) }}</span>
          </div>
        </div>
        <div class="post-actions">
          <div class="like-count">
            <i class="fas fa-heart text-red-500"></i>
            <span>{{ postDetail?.postLikes }}</span>
          </div>
        </div>
      </div>
      
      <h2 class="post-title">{{ postDetail?.postTitle }}</h2>
      <p class="post-text">{{ postDetail?.postContent }}</p>

      <!-- 图片展示 -->
      <div v-if="postDetail?.images && postDetail.images.length > 0" class="post-images">
        <el-image
          v-for="(image, index) in postDetail.images"
          :key="index"
          :src="image"
          :preview-src-list="postDetail.images"
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

    <!-- 评论区 -->
    <div class="comments-section">
      <h3 class="comments-title">评论 ({{ comments.length }})</h3>
      
      <!-- 评论输入框 -->
      <div class="comment-input">
        <el-input
          v-model="newComment"
          type="textarea"
          :rows="2"
          placeholder="写下你的评论..."
        />
        <el-button type="primary" @click="handleCommentSubmit" :disabled="!newComment.trim()">
          发表评论
        </el-button>
      </div>

      <!-- 评论列表 -->
      <div class="comments-list">
        <div v-for="comment in comments" :key="comment.commentId" class="comment-item">
          <div class="comment-header">
            <div class="comment-author">
              <img :src="comment.userAvatar || '/src/assets/images/default-avatar.svg'" :alt="comment.userName" class="author-avatar">
              <div class="author-details">
              <span class="author-name">{{ comment.userName }}</span>
                <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
              </div>
            </div>
            <el-button 
              v-if="canDeleteComment(comment)"
              type="text" 
              class="delete-comment"
              @click="handleDeleteComment(comment.commentId)"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
          <p class="comment-content">{{ comment.commentContent }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture, Delete } from '@element-plus/icons-vue'
import { getPostDetail } from '@/api/post'
import { createComment, getCommentList, deleteComment } from '@/api/comment'
import { useUserStore } from '@/stores/user'

const props = defineProps({
  post: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close', 'refresh', 'refreshPopular'])
const userStore = useUserStore()
const comments = ref([])
const newComment = ref('')
const postDetail = ref(null)

// 格式化日期
const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return d.toLocaleString()
}

// 监听帖子变化
watch(() => props.post, (newPost) => {
  if (newPost) {
    // 确保 postDetail 被完全更新
    postDetail.value = JSON.parse(JSON.stringify(newPost))
  }
}, { deep: true, immediate: true })

// 获取帖子详情
const fetchPostDetail = async () => {
  try {
    const response = await getPostDetail(props.post.postId)
    // 使用响应式更新
    postDetail.value = JSON.parse(JSON.stringify(response))
  } catch (error) {
    console.error('获取帖子详情失败:', error)
    ElMessage.error('获取帖子详情失败')
  }
}

// 处理评论
const handleCommentSubmit = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('评论内容不能为空')
    return
  }

  try {
    const comment = {
      postId: props.post.postId,
      userId: userStore.userInfo.id,
      commentContent: newComment.value.trim()
    }
    await createComment(comment)
    ElMessage.success('评论成功')
    newComment.value = ''
    // 重新获取评论列表
    await fetchComments()
    // 发出刷新事件
    emit('refresh')
    // 刷新最受欢迎列表
    emit('refreshPopular')
  } catch (error) {
    console.error('发表评论失败:', error)
    ElMessage.error('发表评论失败')
  }
}

// 获取评论列表
const fetchComments = async () => {
  try {
    const response = await getCommentList(props.post.postId, { page: 1, size: 10 })
    comments.value = response.records || []
  } catch (error) {
    ElMessage.error('获取评论列表失败')
  }
}

// 判断是否可以删除评论
const canDeleteComment = (comment) => {
  return userStore.isLoggedIn && 
    (userStore.userInfo.id === comment.userId || userStore.userInfo.id === postDetail.value?.userId)
}

// 处理删除评论
const handleDeleteComment = async (commentId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条评论吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteComment(commentId)
    ElMessage.success('评论删除成功')
    await fetchComments() // 刷新评论列表
    // 刷新最受欢迎列表
    emit('refreshPopular')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除评论失败')
    }
  }
}

onMounted(() => {
  fetchPostDetail()
  fetchComments()
})
</script>

<style scoped>
.post-detail {
  padding: 20px;
}

.post-content {
  margin-bottom: 30px;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-details {
  display: flex;
  flex-direction: column;
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
}

.post-time {
  color: #666;
  font-size: 0.9rem;
}

.post-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 16px;
  color: #2c3e50;
}

.post-text {
  color: #666;
  line-height: 1.6;
  white-space: pre-wrap;
}

.comments-section {
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.comments-title {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 20px;
  color: #2c3e50;
}

.comment-input {
  margin-bottom: 20px;
}

.comment-input .el-button {
  margin-top: 10px;
  float: right;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.comment-author {
  display: flex;
  align-items: center;
  gap: 12px;
}

.comment-author .author-details {
  display: flex;
  flex-direction: column;
}

.comment-author .author-name {
  font-weight: 500;
  color: #333;
}

.comment-author .comment-time {
  color: #666;
  font-size: 0.9rem;
}

.comment-content {
  color: #333;
  line-height: 1.5;
}

:deep(.el-textarea__inner) {
  border-radius: 4px;
  resize: vertical;
}

.post-images {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  margin-top: 20px;
}

.post-image {
  width: 100%;
  height: 200px;
  border-radius: 8px;
  object-fit: cover;
  cursor: pointer;
  transition: transform 0.3s ease;
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
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

.like-count {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #666;
}

.like-count i {
  font-size: 18px;
  color: #ff6b6b;
}

.like-count span {
  font-size: 14px;
}

.delete-comment {
  color: #666;
  padding: 4px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.delete-comment:hover {
  color: #ff6b6b;
  transform: scale(1.1);
}
</style> 