<template>
  <header class="custom-header">
    <div class="header-container">
      <!-- 左侧Logo -->
      <div class="logo-section">
        <i class="fas fa-paw mr-2"></i>
        <div class="text-xl font-bold tracking-wide">GoGoPet</div>
      </div>

      <!-- 中间导航菜单 -->
      <nav class="nav-section">
        <router-link to="/" class="nav-link">首页</router-link>
        
        <!-- 未登录用户可见的菜单 -->
        <template v-if="!userStore.isLoggedIn">
          <router-link to="/about" class="nav-link">关于我们</router-link>
        </template>
        
        <!-- 已登录普通用户可见的菜单 -->
        <template v-if="userStore.isLoggedIn && userStore.userInfo?.userType==='U'">
          <router-link to="/transport" class="nav-link">宠物托运</router-link>
          <router-link to="/orderManagement" class="nav-link">订单管理</router-link>
          <router-link to="/community" class="nav-link">社区交流</router-link>
          <router-link to="/analysis" class="nav-link">数据分析</router-link>
          <a 
            @click="handleOrderTrackingClick" 
            class="nav-link"
            style="cursor: pointer;"
          >订单追踪</a>
          <router-link to="/about" class="nav-link">关于我们</router-link>
        </template>

        <!-- 已登录托运公司可见的菜单 -->
        <template v-if="userStore.isLoggedIn && userStore.userInfo?.userType==='C'">
          <router-link to="/companyOrderManagement" class="nav-link">订单管理</router-link>
          <router-link to="/community" class="nav-link">社区交流</router-link>
          <router-link to="/analysis" class="nav-link">数据分析</router-link>
          <a 
            @click="handleOrderTrackingClick" 
            class="nav-link"
            style="cursor: pointer;"
          >订单追踪</a>
          <router-link to="/about" class="nav-link">关于我们</router-link>
        </template>
      </nav>

      <!-- 右侧用户信息 -->
      <div class="user-section">
        <template v-if="userStore.isLoggedIn">
          <UserAvatar
            :avatar-url="userStore.userInfo?.avatarUrl"
            :username="userStore.userInfo?.userName"
          />
        </template>
        <template v-else>
          <router-link to="/login" class="nav-link">
            <i class="fas fa-sign-in-alt mr-1"></i>登录
          </router-link>
          <router-link to="/register" class="nav-link">
            <i class="fas fa-user-plus mr-1"></i>注册
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
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()

const handleOrderTrackingClick = async () => {
  if (userStore.userInfo?.userType === 'C') {
    try {
      // 获取公司用户的最新订单
      const response = await request({
        url: `/order/company/${userStore.userInfo.companyId}/list`,
        method: 'get',
        params: {
          pageNum: 0,
          pageSize: 1
        }
      })
      
      if (response.code === 200 && response.data.records && response.data.records.length > 0) {
        // 按开始时间排序，获取最新的订单
        const sortedOrders = response.data.records.sort((a, b) => 
          new Date(b.startTime) - new Date(a.startTime)
        )
        const latestOrder = sortedOrders[0]
        
        // 跳转到订单追踪页面，并传递订单ID
        await router.push({
          name: 'OrderTrackingEnterprise',
          params: { orderId: latestOrder.orderId }
        })
      } else {
        ElMessage.info('暂无订单数据')
      }
    } catch (error) {
      console.error('获取最新订单失败:', error)
      ElMessage.error('获取最新订单失败')
    }
  } else {
    // 普通用户跳转到普通订单追踪页面
    await router.push({
      name: 'OrderTrackingLatest'
    })
  }
}
</script>

<style scoped>
.custom-header {
  background: linear-gradient(90deg, #ff9f43 0%, #e67e22 100%);
  padding: 1.5rem 2rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  position: relative;
  height: 60px;
}

.header-container {
  position: relative;
  height: 100%;
  width: 100%;
}

.logo-section {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
}

.nav-section {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  gap: 1.2rem;
  white-space: nowrap;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
  font-size: 0.95rem;
  padding: 0.2rem 0.4rem;
}

.nav-link:hover {
  color: rgba(255, 255, 255, 0.8);
}

.user-section {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .custom-header {
    padding: 0.75rem 1rem;
  }
  
  .nav-section {
    display: none;
  }
  
  .nav-link {
    padding: 0.4rem 0.8rem;
  }
}

:deep(.user-avatar) {
  display: flex;
  align-items: center;
}

:deep(.avatar-wrapper) {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

:deep(.avatar-content) {
  display: flex;
  align-items: center;
}

:deep(.user-avatar img) {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}

:deep(.user-avatar .username) {
  color: white;
  font-size: 0.9rem;
  white-space: nowrap;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>