<template>
  <div class="community-container">
    <!-- 头部区域 -->
    <div class="community-header">
      <div class="header-content">
        <h1 class="text-4xl font-bold mb-4 text-gradient">社区交流</h1>
        <p class="text-gray-600 mb-6 text-lg">分享你的宠物故事，与更多宠物爱好者交流</p>
        <el-button type="primary" class="publish-btn" @click="showPostDialog = true">
          <i class="fas fa-plus mr-2"></i>发布新帖
        </el-button>
      </div>
    </div>

    <div class="community-content">
      <!-- 帖子列表 -->
      <div class="posts-list">
        <transition-group name="post-list">
          <PostCard
            v-for="post in posts"
            :key="post.postId"
            :post="post"
            @click="viewPost(post)"
            @edit="handleEditPost"
            @delete="handleDeletePost"
            @like="handlePostLike"
            @unlike="handlePostUnlike"
          />
        </transition-group>
      </div>

      <!-- 返回顶部按钮 -->
      <div 
        class="back-to-top" 
        :class="{ 'show': showBackToTop }"
        @click="scrollToTop"
      >
        <i class="fas fa-arrow-up"></i>
      </div>

      <!-- 右侧栏 -->
      <div class="right-sidebar">
        <!-- 最受欢迎帖子 -->
        <div class="popular-posts">
          <div class="popular-posts-header">
            <h2 class="text-xl font-bold flex items-center">
              <i class="fas fa-fire text-red-500 mr-2"></i>
              最受欢迎
            </h2>
            <p class="text-gray-500 text-sm">点赞数最高的10条帖子</p>
          </div>
          <div class="popular-posts-list">
            <div v-for="post in popularPosts" :key="post.postId" class="popular-post-item" @click="viewPost(post)">
              <div class="popular-post-content">
                <h3 class="popular-post-title">{{ post.postTitle }}</h3>
                <div class="popular-post-stats">
                  <span class="like-count">
                    <i class="fas fa-heart text-red-500"></i>
                    {{ post.postLikes }}
                  </span>
                  <span class="comment-count">
                    <i class="fas fa-comment text-blue-500"></i>
                    {{ post.postComment }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 发帖对话框 -->
    <el-dialog
      v-model="showPostDialog"
      :title="isEditing ? '编辑帖子' : '发布新帖'"
      width="50%"
      :before-close="handleClose"
      class="post-dialog"
    >
      <PostForm 
        v-if="isEditing"
        :post="selectedPost"
        @submit="handlePostUpdate"
        @cancel="showPostDialog = false" 
      />
      <PostForm 
        v-else
        @submit="handlePostSubmit"
        @cancel="showPostDialog = false" 
      />
    </el-dialog>

    <!-- 帖子详情对话框 -->
    <el-dialog
      v-model="showPostDetail"
      :title="selectedPost?.postTitle"
      width="70%"
      :before-close="handleClose"
      class="post-detail-dialog"
    >
      <PostDetail
        v-if="selectedPost"
        :post="selectedPost"
        @close="showPostDetail = false"
        @refresh="handlePostRefresh"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import PostCard from '@/components/community/PostCard.vue'
import PostForm from '@/components/community/PostForm.vue'
import PostDetail from '@/components/community/PostDetail.vue'
import { getPostList, createPost, updatePost, deletePost, getMostPopularPosts, likePost, unlikePost } from '@/api/post'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const posts = ref([])
const popularPosts = ref([])
const showPostDialog = ref(false)
const showPostDetail = ref(false)
const selectedPost = ref(null)
const isEditing = ref(false)
const showBackToTop = ref(false)

// 获取帖子列表
const fetchPosts = async () => {
  try {
    console.log('开始获取帖子列表...')
    const response = await getPostList()
    console.log('获取帖子列表响应:', response)
    if (response) {
      posts.value = response
      console.log('成功设置帖子列表:', posts.value)
    } else {
      console.error('响应数据格式不正确:', response)
      ElMessage.error('获取帖子列表失败：数据格式不正确')
    }
  } catch (error) {
    console.error('获取帖子列表失败:', error)
    console.error('错误详情:', {
      message: error.message,
      response: error.response,
      request: error.request
    })
    ElMessage.error(error.response?.data?.message || '获取帖子列表失败')
  }
}

// 监听滚动事件
const handleScroll = () => {
  showBackToTop.value = window.scrollY > 300
}

// 返回顶部
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

// 获取最受欢迎帖子
const fetchPopularPosts = async () => {
  try {
    const response = await getMostPopularPosts(10)
    if (response) {
      popularPosts.value = response
    }
  } catch (error) {
    console.error('获取最受欢迎帖子失败:', error)
    ElMessage.error('获取最受欢迎帖子失败')
  }
}

// 查看帖子详情
const viewPost = (post) => {
  selectedPost.value = post
  showPostDetail.value = true
}

// 提交新帖子
const handlePostSubmit = async (postData) => {
  try {
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      return
    }

    if (!userStore.userInfo?.id) {
      ElMessage.error('用户信息不完整，请重新登录')
      return
    }

    const post = {
      ...postData,
      userId: userStore.userInfo.id
    }
    console.log('提交的帖子数据:', post) // 添加日志
    const response = await createPost(post)
    console.log('服务器响应:', response) // 添加日志
    ElMessage.success('发布成功')
    showPostDialog.value = false
    await fetchPosts() // 刷新帖子列表
  } catch (error) {
    console.error('发布帖子失败:', error) // 添加详细错误日志
    ElMessage.error(error.response?.data?.message || '发布失败')
  }
}

// 处理编辑帖子
const handleEditPost = (post) => {
  selectedPost.value = post
  isEditing.value = true
  showPostDialog.value = true
}

// 处理更新帖子
const handlePostUpdate = async (postData) => {
  try {
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      return
    }

    if (!userStore.userInfo?.id) {
      ElMessage.error('用户信息不完整，请重新登录')
      return
    }

    const post = {
      ...postData,
      userId: userStore.userInfo.id,
      postId: selectedPost.value.postId
    }
    await updatePost(post.postId, post)
    ElMessage.success('更新成功')
    showPostDialog.value = false
    isEditing.value = false
    selectedPost.value = null
    await fetchPosts() // 刷新帖子列表
  } catch (error) {
    console.error('更新帖子失败:', error)
    ElMessage.error(error.response?.data?.message || '更新失败')
  }
}

// 处理对话框关闭
const handleClose = (done) => {
  selectedPost.value = null
  isEditing.value = false
  done()
}

// 处理删除帖子
const handleDeletePost = async (postId) => {
  try {
    await deletePost(postId)
    ElMessage.success('删除成功')
    await fetchPosts() // 刷新帖子列表
  } catch (error) {
    console.error('删除帖子失败:', error)
    ElMessage.error(error.response?.data?.message || '删除失败')
  }
}

// 处理帖子点赞
const handlePostLike = async (postId) => {
  try {
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      return
    }

    await likePost(postId, userStore.userInfo.id)
    
    // 更新帖子列表中的点赞状态
    const post = posts.value.find(p => p.postId === postId)
    if (post) {
      post.postLikes++
      post.isLiked = true
    }
    
    // 重新获取最受欢迎帖子
    await fetchPopularPosts()
    ElMessage.success('点赞成功')
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('点赞失败')
  }
}

// 处理取消点赞
const handlePostUnlike = async (postId) => {
  try {
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      return
    }

    await unlikePost(postId, userStore.userInfo.id)
    
    // 更新帖子列表中的点赞状态
    const post = posts.value.find(p => p.postId === postId)
    if (post) {
      post.postLikes--
      post.isLiked = false
    }
    
    // 重新获取最受欢迎帖子
    await fetchPopularPosts()
    ElMessage.success('取消点赞成功')
  } catch (error) {
    console.error('取消点赞失败:', error)
    ElMessage.error('取消点赞失败')
  }
}

// 处理帖子刷新
const handlePostRefresh = async () => {
  await fetchPosts()
  await fetchPopularPosts()
}

onMounted(() => {
  fetchPosts()
  fetchPopularPosts()
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.community-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.community-header {
  background: linear-gradient(135deg, #f6f9fc 0%, #eef2f7 100%);
  border-radius: 16px;
  padding: 40px;
  margin-bottom: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
}

.text-gradient {
  background: linear-gradient(135deg, #ff9f43 0%, #e67e22 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.publish-btn {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(135deg, #ff9f43 0%, #e67e22 100%);
  border: none;
  transition: all 0.3s ease;
}

.publish-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(230, 126, 34, 0.3);
}

.community-content {
  display: grid;
  grid-template-columns: 1fr 350px;
  gap: 40px;
}

.posts-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.right-sidebar {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.popular-posts {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  margin-left: 20px;
}

.popular-posts-header {
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
}

.popular-posts-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.popular-post-item {
  padding: 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f8f9fa;
}

.popular-post-item:hover {
  background: #f0f2f5;
  transform: translateX(4px);
}

.popular-post-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.popular-post-title {
  font-size: 14px;
  font-weight: 500;
  color: #2c3e50;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.popular-post-stats {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 12px;
}

.like-count, .comment-count {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
}

/* 动画效果 */
.post-list-enter-active,
.post-list-leave-active {
  transition: all 0.5s ease;
}

.post-list-enter-from,
.post-list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .community-content {
    grid-template-columns: 1fr;
  }

  .right-sidebar {
    margin-top: 24px;
  }
}

@media (max-width: 768px) {
  .community-header {
    padding: 24px;
  }

  .header-content h1 {
    font-size: 28px;
  }
}

@media (max-width: 480px) {
  .community-header {
    padding: 20px;
  }

  .header-content h1 {
    font-size: 24px;
  }
}

/* 返回顶部按钮 */
.back-to-top {
  position: fixed;
  right: 30px;
  bottom: 30px;
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #ff9f43 0%, #e67e22 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.back-to-top.show {
  opacity: 1;
  visibility: visible;
}

.back-to-top i {
  color: white;
  font-size: 20px;
}

.back-to-top:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 15px rgba(230, 126, 34, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .back-to-top {
    right: 20px;
    bottom: 20px;
    width: 40px;
    height: 40px;
  }
  
  .back-to-top i {
    font-size: 16px;
  }
}
</style> 