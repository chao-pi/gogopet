<template>
  <div class="order-detail-page">
    <div class="page-header">
      <h2>订单详情</h2>
    </div>

    <div class="main-content">
      <!-- 订单基本信息 -->
      <el-card class="order-card">
        <template #header>
          <div class="card-header">
            <span>订单信息</span>
            <el-tag :type="getStatusType(orderInfo.orderStatus)">{{ getStatusText(orderInfo.orderStatus) }}</el-tag>
          </div>
        </template>
        
        <div class="order-details">
          <div class="detail-item">
            <span class="label">订单编号：</span>
            <span class="value">{{ orderInfo.orderId }}</span>
          </div>
          <div class="detail-item">
            <span class="label">托运公司：</span>
            <span class="value">{{ orderInfo.companyName }}</span>
          </div>
          <div class="detail-item">
            <span class="label">运输方式：</span>
            <span class="value">{{ getTransportMethodText(orderInfo.transportMethod) }}</span>
          </div>
          <div class="detail-item">
            <span class="label">出发地：</span>
            <span class="value">{{ orderInfo.startLocation }}</span>
          </div>
          <div class="detail-item">
            <span class="label">目的地：</span>
            <span class="value">{{ orderInfo.endLocation }}</span>
          </div>
          <div class="detail-item">
            <span class="label">订单价格：</span>
            <span class="value">¥{{ orderInfo.price }}</span>
          </div>
          <div class="detail-item">
            <span class="label">创建时间：</span>
            <span class="value">{{ formatDateTime(orderInfo.createTime) }}</span>
          </div>
          <div class="detail-item">
            <span class="label">订单备注：</span>
            <span class="value">{{ orderInfo.orderRemark || '无' }}</span>
          </div>
        </div>
      </el-card>

      <!-- 宠物信息 -->
      <el-card class="pet-card">
        <template #header>
          <div class="card-header">
            <span>宠物信息</span>
          </div>
        </template>
        
        <div class="pet-details">
          <div class="pet-image">
            <img :src="orderInfo.petImage" :alt="orderInfo.petName">
          </div>
          <div class="pet-info">
            <div class="detail-item">
              <span class="label">宠物名称：</span>
              <span class="value">{{ orderInfo.petName }}</span>
            </div>
            <div class="detail-item">
              <span class="label">宠物品种：</span>
              <span class="value">{{ orderInfo.petBreed }}</span>
            </div>
            <div class="detail-item">
              <span class="label">宠物年龄：</span>
              <span class="value">{{ orderInfo.petAge }}岁</span>
            </div>
            <div class="detail-item">
              <span class="label">宠物状态：</span>
              <span class="value">{{ getPetStatusText(orderInfo.petStatus) }}</span>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button type="primary" @click="viewOrderTracking">查看追踪</el-button>
        <el-button @click="goBack">返回</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrderDetail } from '@/api/order.js'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 订单信息
const orderInfo = ref({
  orderId: '',
  orderStatus: '',
  companyName: '',
  transportMethod: '',
  startLocation: '',
  endLocation: '',
  price: 0,
  createTime: '',
  orderRemark: '',
  petName: '',
  petBreed: '',
  petAge: 0,
  petStatus: '',
  petImage: '',
  startProvince: '',
  startCity: '',
  startDistrict: '',
  endProvince: '',
  endCity: '',
  endDistrict: '',
  distance: 0,
  companyId: '',
  completeTime: null,
  endLatitude: 0,
  endLongitude: 0,
  endTime: null,
  petIds: null,
  rating: null,
  ratingComment: null,
  startLatitude: 0,
  startLongitude: 0,
  startTime: '',
  updateTime: '',
  userId: ''
})

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    'P': 'info',    // 待支付
    'W': 'warning', // 待接单
    'T': 'warning', // 进行中
    'C': 'success', // 已完成
    'X': 'danger'   // 已取消
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    'P': '待支付',
    'W': '待接单',
    'T': '进行中',
    'C': '已完成',
    'X': '已取消'
  }
  return texts[status] || status
}

// 获取运输方式文本
const getTransportMethodText = (method) => {
  const methods = {
    'SPECIAL': '专车托运',
    'SHARE': '拼车托运',
    'AIR': '空运托运'
  }
  return methods[method] || method
}

// 获取宠物状态文本
const getPetStatusText = (status) => {
  const statuses = {
    'N': '正常',
    'A': '异常'
  }
  return statuses[status] || status
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString()
}

// 获取订单数据
const fetchOrderData = async () => {
  try {
    const orderId = route.params.orderId
    if (!orderId) {
      ElMessage.error('订单ID无效')
      return
    }
    
    console.log('正在获取订单详情，订单ID:', orderId)
    const response = await getOrderDetail(orderId)
    console.log('获取到的原始响应:', response)
    
    if (response && response.data) {
      const orderData = response.data
      orderInfo.value = orderData
      
      console.log('完整的orderInfo内容:', JSON.stringify(orderInfo.value, null, 2))
      console.log('地址信息:', {
        startLocation: orderData.startLocation,
        endLocation: orderData.endLocation
      })
      console.log('宠物信息:', {
        petName: orderData.petName,
        petBreed: orderData.petBreed,
        petAge: orderData.petAge,
        petImage: orderData.petImage
      })
      console.log('公司信息:', {
        companyId: orderData.companyId,
        companyName: orderData.companyName
      })
    } else {
      console.error('获取订单数据失败: 响应数据为空或格式不正确')
      ElMessage.error('获取订单数据失败')
    }
  } catch (error) {
    console.error('获取订单数据失败:', error)
    ElMessage.error('获取订单数据失败')
  }
}

// 查看订单追踪
const viewOrderTracking = () => {
  const userType = userStore.userInfo?.userType
  if (userType === 'C') {
    router.push(`/OrderTrackingE/${orderInfo.value.orderId}`)
  } else {
    router.push(`/orderTracking/${orderInfo.value.orderId}`)
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchOrderData()
})
</script>

<style scoped>
.order-detail-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card,
.pet-card {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-details,
.pet-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-item {
  display: flex;
  align-items: center;
}

.label {
  color: #666;
  width: 100px;
  flex-shrink: 0;
}

.value {
  color: #333;
  font-weight: 500;
}

.pet-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 12px;
}

.pet-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .order-detail-page {
    padding: 10px;
  }
  
  .label {
    width: 80px;
  }
}
</style> 