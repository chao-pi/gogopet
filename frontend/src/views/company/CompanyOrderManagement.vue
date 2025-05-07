<template>
  <div class="order-page">
    <div class="header">
      <h1 class="page-title">公司订单管理</h1>
      <div class="status-tabs">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="全部" name="all"></el-tab-pane>
          <el-tab-pane label="待支付" name="P"></el-tab-pane>
          <el-tab-pane label="待接单" name="W"></el-tab-pane>
          <el-tab-pane label="运输中" name="T"></el-tab-pane>
          <el-tab-pane label="已完成" name="C"></el-tab-pane>
          <el-tab-pane label="已取消" name="X"></el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <div class="orders-container">
      <el-empty 
        v-if="filteredOrders.length === 0" 
        :description="getEmptyDescription(activeTab)" 
      />
      <div v-for="order in filteredOrders" :key="order.orderId" class="order-card">
        <div class="order-left">
          <div class="order-status" :class="getStatusClass(order.orderStatus)">{{ getStatusText(order.orderStatus) }}</div>
          <div class="order-number">订单号: {{ order.orderId }}</div>
          <div class="order-time">下单时间: {{ formatDate(order.createTime) }}</div>
          <div class="user-info">
            <i class="fas fa-user"></i>
            <span>用户: {{ order.userName }}</span>
          </div>
        </div>
        <div class="order-middle">
          <div class="order-route">
            <div class="location">
              <span class="label">出发地</span>
              <span class="value">{{ order.startProvince }}{{ order.startCity }}{{ order.startDistrict }}</span>
            </div>
            <div class="arrow">→</div>
            <div class="location">
              <span class="label">目的地</span>
              <span class="value">{{ order.endProvince }}{{ order.endCity }}{{ order.endDistrict }}</span>
            </div>
          </div>
          <div class="transport-info">
            <div class="transport-method">
              <i class="fas fa-truck"></i>
              {{ getTransportMethodText(order.transportMethod) }}
            </div>
            <div class="pets-info">
              <i class="fas fa-paw"></i>
              <div class="pets-list">
                <template v-if="order.pets && order.pets.length > 0">
                  <span v-for="pet in order.pets" :key="pet.petId" class="pet-item">
                    {{ pet.petName }} ({{ pet.petBreed }}) - {{ pet.petAge }}岁/{{ pet.petWeight }}kg
                  </span>
                </template>
                <span v-else class="pet-count">加载中...</span>
              </div>
            </div>
          </div>
        </div>
        <div class="order-right">
          <div class="order-amount">¥{{ order.price }}</div>
          <div class="order-actions">
            <button 
              v-if="order.orderStatus === 'W'"
              class="btn-accept"
              @click="acceptOrder(order.orderId)">
              <i class="fas fa-check"></i>
              接单
            </button>
            <button 
              v-if="order.orderStatus === 'T'"
              class="btn-complete"
              @click="completeOrder(order.orderId)">
              <i class="fas fa-flag-checkered"></i>
              完成
            </button>
            <button 
              v-if="order.orderStatus !== 'W' && order.orderStatus !== 'T' && order.orderStatus !== 'X' && order.orderStatus !== 'C' && order.orderStatus !== 'P'"
              class="btn-track"
              @click="trackOrder(order.orderId)">
              <i class="fas fa-map-marker-alt"></i>
              托运追踪
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {getOrders, getOrderPets, updateOrderStatus, getOrdersByCompanyId, updateOrderEndTime} from '@/api/order'
import { getUserInfo } from '@/api/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const orders = ref([])
const activeTab = ref('all')

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') {
    return orders.value
  } else {
    return orders.value.filter(order => order.orderStatus === activeTab.value)
  }
})

onMounted(() => {
  fetchOrders()
})

const fetchOrders = async () => {
  try {
    const response = await getOrdersByCompanyId({ companyId: userStore.userInfo.companyId })
    orders.value = response.data
    for (const order of orders.value) {
      try {
        // 获取宠物信息
        const petsResponse = await getOrderPets(order.orderId)
        order.pets = petsResponse.data
      } catch (error) {
        order.pets = []
      }
      try {
        // 获取用户名
        const userResponse = await getUserInfo(order.userId)
        order.userName = userResponse.userName
      } catch (error) {
        order.userName = '未知用户'
      }
    }
  } catch (error) {
    console.error('Failed to fetch orders:', error)
  }
}

const acceptOrder = async (orderId) => {
  try {
    await updateOrderStatus(orderId, 'T')
    ElMessage.success('接单成功！')
    fetchOrders()
  } catch (error) {
    ElMessage.error('接单失败，请重试')
  }
}

const completeOrder = async (orderId) => {
  try {
    await updateOrderStatus(orderId, 'C');
    await updateOrderEndTime(orderId);
    ElMessage.success('订单已完成！')
    fetchOrders()
  } catch (error) {
    ElMessage.error('操作失败，请重试')
  }
}

const trackOrder = (orderId) => {
  router.push(`/order/track/${orderId}`)
}

const formatDate = (date) => {
  return new Date(date).toLocaleString()
}

const getStatusClass = (status) => {
  const statusMap = {
    'P': 'status-pending',
    'W': 'status-waiting',
    'T': 'status-transporting',
    'C': 'status-completed',
    'X': 'status-cancelled'
  }
  return statusMap[status] || ''
}

const getStatusText = (status) => {
  const statusMap = {
    'P': '待支付',
    'W': '待接单',
    'T': '运输中',
    'C': '已完成',
    'X': '已取消'
  }
  return statusMap[status] || status
}

const getTransportMethodText = (method) => {
  const methodMap = {
    'SPECIAL': '专车托运',
    'SHARE': '拼车托运',
    'AIR': '空运托运'
  }
  return methodMap[method] || method
}

// 获取空状态描述文本
const getEmptyDescription = (tab) => {
  const descriptions = {
    'all': '暂无订单',
    'P': '暂无待支付订单',
    'W': '暂无待接单订单',
    'T': '暂无运输中订单',
    'C': '暂无已完成订单',
    'X': '暂无已取消订单'
  }
  return descriptions[tab] || '暂无订单'
}

const handleTabClick = () => {}
</script>

<style scoped>
.order-page {
  padding: 2rem;
  background: linear-gradient(135deg, #f9fafb 0%, #f3f4f6 100%);
  min-height: calc(100vh - 60px);
}

.header {
  margin-bottom: 30px;
  text-align: center;
}

.page-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.status-tabs {
  margin-top: 20px;
  background: #fff;
  padding: 0;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  width: 100%;
  max-width: 100%;
  margin-left: 0;
  margin-right: 0;
}
:deep(.el-tabs__nav-wrap) {
  display: flex;
  justify-content: center;
}
:deep(.el-tabs__nav) {
  display: flex;
  justify-content: center;
}
:deep(.el-tabs__item) {
  position: relative;
  z-index: 1;
  height: 56px;
  line-height: 56px;
  font-size: 18px;
  color: #8a8a8a;
  font-weight: 500;
  padding: 0 36px;
  border-radius: 12px 12px 0 0;
  transition: color 0.2s, background 0.2s, font-weight 0.2s;
}
:deep(.el-tabs__item:first-child.is-active) {
  border-top-left-radius: 16px;
  border-bottom-left-radius: 0;
}
:deep(.el-tabs__item:last-child.is-active) {
  border-top-right-radius: 16px;
  border-bottom-right-radius: 0;
}
:deep(.el-tabs__item:not(.is-active)) {
  background: transparent !important;
  box-shadow: none !important;
}
:deep(.el-tabs__item.is-active) {
  border-radius: 16px 16px 0 0 !important;
  background: linear-gradient(90deg, #e3f0ff 0%, #f6fbff 100%);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.10);
  z-index: 2;
}

.orders-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.order-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.order-left {
  flex: 1;
  padding-right: 20px;
  border-right: 1px solid #eee;
}

.order-middle {
  flex: 2;
  padding: 0 20px;
  border-right: 1px solid #eee;
}

.order-right {
  flex: 1;
  padding-left: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.order-status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 10px;
}

.status-pending {
  background-color: #fff3e0;
  color: #f57c00;
}

.status-waiting {
  background-color: #e3f2fd;
  color: #1976d2;
}

.status-transporting {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-completed {
  background-color: #f5f5f5;
  color: #616161;
}

.status-cancelled {
  background-color: #ffebee;
  color: #c62828;
}

.order-number {
  color: #666;
  font-size: 14px;
  margin-bottom: 5px;
}

.order-time {
  color: #999;
  font-size: 13px;
}

.user-info {
  margin-top: 10px;
  color: #666;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-info i {
  color: #2196f3;
}

.order-route {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 15px;
}

.location {
  display: flex;
  flex-direction: column;
}

.arrow {
  color: #999;
  font-size: 20px;
}

.transport-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.transport-method, .pets-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  font-size: 14px;
}

.transport-method i, .pets-info i {
  color: #2196f3;
}

.pets-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.pet-item {
  background: #f5f5f5;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 13px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 4px;
}

.pet-count {
  background: #f5f5f5;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 13px;
  color: #666;
}

.order-amount {
  font-size: 26px;
  font-weight: 700;
  color: #ff6b6b;
  margin-bottom: 20px;
  letter-spacing: 0.5px;
  position: relative;
  padding: 8px 0;
}

.order-amount::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 2px;
  background: #eee;
}

.order-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
  max-width: 160px;
}

.btn-accept, .btn-complete, .btn-track {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s ease;
  width: 100%;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.btn-accept {
  background: linear-gradient(135deg, #4caf50, #66bb6a);
  color: white;
}

.btn-complete {
  background: linear-gradient(135deg, #2196f3, #42a5f5);
  color: white;
}

.btn-track {
  background: linear-gradient(135deg, #ffa726, #ffb74d);
  color: white;
}

.btn-accept:hover, .btn-complete:hover, .btn-track:hover {
  opacity: 0.95;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.btn-accept i, .btn-complete i, .btn-track i {
  font-size: 15px;
}
</style> 