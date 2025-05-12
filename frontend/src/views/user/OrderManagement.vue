<template>
  <div class="order-page">
    <div class="header">
      <h1 class="page-title">订单管理</h1>
      <div class="status-tabs">
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="全部" name="all"></el-tab-pane>
          <el-tab-pane label="待支付" name="P"></el-tab-pane>
          <el-tab-pane label="待接单" name="W"></el-tab-pane>
          <el-tab-pane label="运输中" name="Transport"></el-tab-pane>
          <el-tab-pane label="已完成" name="C"></el-tab-pane>
          <el-tab-pane label="待评价" name="toEvaluate"></el-tab-pane>
          <el-tab-pane label="已评价" name="evaluated"></el-tab-pane>
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
              v-if="order.orderStatus === 'P'"
              class="btn-cancel"
              @click="cancelOrder(order.orderId)"
            >
              <i class="fas fa-times"></i>
              取消订单
            </button>
            <button 
              v-if="order.orderStatus === 'P'"
              class="btn-pay"
              @click="goToPayment(order.orderId)"
            >
              <i class="fas fa-credit-card"></i>
              支付订单
            </button>
            <button 
              v-else-if="order.orderStatus !== 'W' && order.orderStatus !== 'X' && order.orderStatus !== 'C'"
              class="btn-track"
              @click="trackOrder(order.orderId)"
            >
              <i class="fas fa-map-marker-alt"></i>
              托运追踪
            </button>
            <button 
              v-else-if="order.orderStatus === 'C' && !order.evaluated"
              class="btn-evaluate"
              @click="openEvaluateDialog(order)"
            >
              <i class="fas fa-star"></i>
              评价
            </button>
            <span v-else-if="order.orderStatus === 'C' && order.evaluated" class="evaluate-done">评价完成</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 支付弹窗 -->
    <el-dialog
      v-model="showPaymentPopup"
      title="订单支付"
      width="30%"
      :close-on-click-modal="false"
      class="payment-dialog"
    >
      <div class="payment-content">
        <div class="qrcode-container">
          <img :src="`https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=gogopet:payment:${currentOrder?.orderId}`" 
               alt="支付二维码" 
               class="qrcode-image" />
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closePaymentPopup">取消</el-button>
          <el-button type="primary" @click="handlePaymentSuccess">
            支付完成
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 评价弹窗 -->
    <el-dialog v-model="evaluateDialogVisible" title="订单评价" width="400px" :close-on-click-modal="false">
      <div class="evaluate-dialog-content">
        <div class="score-section">
          <span>评分：</span>
          <el-rate v-model="evaluateForm.score" :max="5" allow-half show-score />
        </div>
        <el-form :model="evaluateForm" :rules="evaluateRules" ref="evaluateFormRef" label-width="0">
          <el-form-item prop="content">
            <el-input
              v-model="evaluateForm.content"
              type="textarea"
              :rows="4"
              placeholder="请输入评价内容（不少于10字）"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="evaluateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEvaluate">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getOrders, getOrderDetail, getOrderPets, addOrderEvaluation, updateOrderStatus, cancelOrder as cancelOrderApi } from '@/api/order'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const orders = ref([])
const showPaymentPopup = ref(false)
const currentOrder = ref(null)

// 评价弹窗相关
const evaluateDialogVisible = ref(false)
const evaluateForm = ref({ score: 0, content: '', orderId: '' })
const evaluateFormRef = ref(null)
const evaluateRules = {
  score: [
    { required: true, message: '请评分', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入评价内容', trigger: 'blur' },
    { min: 10, message: '评价内容不少于10字', trigger: 'blur' }
  ]
}

const activeTab = ref('all')

// 根据当前选中的标签筛选订单
const filteredOrders = computed(() => {
  if (activeTab.value === 'all') {
    return orders.value
  } else if (activeTab.value === 'toEvaluate') {
    return orders.value.filter(order => order.orderStatus === 'C' && !order.evaluated)
  } else if (activeTab.value === 'evaluated') {
    return orders.value.filter(order => order.orderStatus === 'C' && order.evaluated)
  } else if(activeTab.value === 'Transport') {
    return orders.value.filter(order => order.orderStatus === 'T' || order.orderStatus === '1'
        || order.orderStatus === '2' || order.orderStatus === '3' || order.orderStatus === '4'
        || order.orderStatus === '5' || order.orderStatus === 'R' || order.orderStatus === 'L'|| order.orderStatus === 'D')
  } else {
    return orders.value.filter(order => order.orderStatus === activeTab.value)
  }
})

// 处理标签点击事件
const handleTabClick = () => {
  // 可以在这里添加额外的逻辑，如果需要的话
}

// 生命周期钩子
onMounted(() => {
  fetchOrders()
})

// 获取订单列表
const fetchOrders = async () => {
  try {
    const response = await getOrders({ userId: userStore.userInfo.id })
    orders.value = response.data.map(order => ({
      ...order,
      evaluated: order.rating != null
    }))
    
    // 为每个订单获取宠物信息
    for (const order of orders.value) {
      try {
        const petsResponse = await getOrderPets(order.orderId)
        order.pets = petsResponse.data
        console.log('获取到宠物信息:', order.pets)
      } catch (error) {
        console.error(`Failed to fetch pets for order ${order.orderId}:`, error)
        order.pets = []
      }
    }
  } catch (error) {
    console.error('Failed to fetch orders:', error)
  }
}

// 打开支付弹窗
const openPaymentPopup = (order) => {
  currentOrder.value = order
  showPaymentPopup.value = true
}

// 关闭支付弹窗
const closePaymentPopup = () => {
  showPaymentPopup.value = false
  currentOrder.value = null
}

// 处理支付成功
const handlePaymentSuccess = async () => {
  if (currentOrder.value) {
    try {
      // 调用API更新订单状态
      await updateOrderStatus(currentOrder.value.orderId, 'W', null, null)
      
      // 更新本地订单状态
      const order = orders.value.find(o => o.orderId === currentOrder.value.orderId)
      if (order) {
        order.orderStatus = 'W'
        currentOrder.value.orderStatus = 'W'
        ElMessage.success('支付成功！')
        closePaymentPopup()
        // 刷新订单列表以确保数据同步
        fetchOrders()
      }
    } catch (error) {
      console.error('Failed to update order status:', error)
      ElMessage.error('更新订单状态失败，请重试')
    }
  }
}

// 修改goToPayment方法
const goToPayment = (orderId) => {
  const order = orders.value.find(o => o.orderId === orderId)
  if (order) {
    openPaymentPopup(order)
  }
}

// 托运追踪
const trackOrder = (orderId) => {
  const userType = userStore.userInfo?.userType
  if (userType === 'C') {
    router.push(`/OrderTrackingE/${orderId}`)
  } else {
    router.push(`/ordertracking/${orderId}`)
  }
}

// 查看订单详情
const viewOrderDetail = (orderId) => {
  router.push(`/order/detail/${orderId}`)
}

// 日期格式化
const formatDate = (date) => {
  return new Date(date).toLocaleString()
}

// 状态样式类
const getStatusClass = (status) => {
  const statusMap = {
    'P': 'status-pending',    // 待支付
    'W': 'status-waiting',    // 待接单
    'T': 'status-transporting', // 运输中
    'C': 'status-completed',  // 已完成
    'X': 'status-cancelled',  // 已取消
    '1': 'status-start',
    '2': 'status-first',
    '3': 'status-second',
    '4': 'status-third',
    '5': 'status-destination',
    'R': 'status-rest',
    'L': 'status-loading',
    'D': 'status-finish'
  }
  return statusMap[status] || ''
}

// 状态文本
const getStatusText = (status) => {
  const statusMap = {
    'P': '待支付',
    'W': '待接单',
    'T': '运输中',
    'C': '已完成',
    'X': '已取消',
    '1': '运输中-起点',
    '2': '运输中-第一检查点',
    '3': '运输中-第二检查点',
    '4': '运输中-第三检查点',
    '5': '运输中-终点',
    'R': '休息中',
    'L': '装卸中',
    'D': '已送达'
  }
  return statusMap[status] || status
}

// 运输方式文本
const getTransportMethodText = (method) => {
  const methodMap = {
    'SPECIAL': '专车托运',
    'SHARE': '拼车托运',
    'AIR': '空运托运'
  }
  return methodMap[method] || method
}

const cancelOrder = async (orderId) => {
  try {
    await cancelOrderApi(orderId)
    const order = orders.value.find(o => o.orderId === orderId)
    if (order) order.orderStatus = 'X'
    ElMessage.success('订单已取消')
  } catch (error) {
    ElMessage.error('取消订单失败，请重试')
  }
}

// 新增评价方法
const openEvaluateDialog = (order) => {
  evaluateForm.value = { score: 0, content: '', orderId: order.orderId }
  evaluateDialogVisible.value = true
}

const submitEvaluate = () => {
  evaluateFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      await addOrderEvaluation({
        orderId: evaluateForm.value.orderId,
        score: evaluateForm.value.score,
        content: evaluateForm.value.content
      })
      // 标记该订单已评价
      const order = orders.value.find(o => o.orderId === evaluateForm.value.orderId)
      if (order) order.evaluated = true
      evaluateDialogVisible.value = false
      ElMessage.success('评价成功！')
    } catch (error) {
      ElMessage.error('评价失败，请重试')
    }
  })
}

// 获取空状态描述文本
const getEmptyDescription = (tab) => {
  const descriptions = {
    'all': '暂无订单',
    'P': '暂无待支付订单',
    'W': '暂无待接单订单',
    'T': '暂无运输中订单',
    'C': '暂无已完成订单',
    'toEvaluate': '暂无待评价订单',
    'evaluated': '暂无已评价订单',
    'X': '暂无已取消订单'
  }
  return descriptions[tab] || '暂无订单'
}
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

.status-start {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-first {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-second {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-third {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-destination {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-rest {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-loading {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-finish {
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

.location .label {
  color: #999;
  font-size: 12px;
  margin-bottom: 4px;
}

.location .value {
  color: #333;
  font-size: 16px;
  font-weight: 500;
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
  flex-direction: row;
  gap: 18px;
  width: 100%;
  max-width: 260px;
  justify-content: flex-end;
  align-items: center;
}

.btn-pay,
.btn-cancel {
  white-space: nowrap;
  min-width: 120px;
}

.btn-pay {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 10px 24px;
  font-weight: 600;
  font-size: 15px;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.10);
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 8px;
}
.btn-pay:hover {
  background: linear-gradient(135deg, #66b1ff 0%, #409EFF 100%);
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.18);
  transform: translateY(-2px) scale(1.04);
}

.btn-cancel {
  background: #fff;
  color: #f56c6c;
  border: 1.5px solid #f56c6c;
  border-radius: 8px;
  padding: 10px 22px;
  font-weight: 500;
  font-size: 15px;
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.08);
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 8px;
}
.btn-cancel:hover {
  background: #fff0f0;
  color: #fff;
  border-color: #f56c6c;
  background: linear-gradient(135deg, #ffb6b6 0%, #ff8787 100%);
  box-shadow: 0 4px 16px rgba(245, 108, 108, 0.18);
  transform: translateY(-2px) scale(1.04);
}

.btn-track {
  background: linear-gradient(135deg, #ffa726, #ffb74d);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 10px 22px;
  font-weight: 600;
  font-size: 15px;
  box-shadow: 0 2px 8px rgba(255, 167, 38, 0.12);
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  white-space: nowrap;
  margin: 0 auto;
}
.btn-track:hover {
  background: linear-gradient(135deg, #ffb74d, #ffa726);
  box-shadow: 0 4px 16px rgba(255, 167, 38, 0.18);
  transform: translateY(-2px) scale(1.04);
}

.btn-evaluate {
  background: linear-gradient(135deg, #ffd600, #ffe082);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 10px 22px;
  font-weight: 600;
  font-size: 15px;
  box-shadow: 0 2px 8px rgba(255, 214, 0, 0.12);
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  white-space: nowrap;
  margin: 0 auto;
}
.btn-evaluate:hover {
  background: linear-gradient(135deg, #ffe082, #ffd600);
  box-shadow: 0 4px 16px rgba(255, 214, 0, 0.18);
  transform: translateY(-2px) scale(1.04);
}

/* 支付弹窗样式 */
.payment-dialog {
  :deep(.el-dialog__header) {
    border-bottom: none !important;
    padding-bottom: 0;
  }

  :deep(.el-dialog__footer) {
    border-top: none !important;
    padding-top: 0;
  }
}

.qrcode-image {
  width: 200px;
  height: 200px;
  display: block;
  margin: 0 auto;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 8px;
}

.payment-content {
  padding: 20px;
  text-align: center;
}

.qrcode-container {
  margin: 20px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.qrcode-image {
  width: 200px;
  height: 200px;
  object-fit: contain;
}

.payment-tips {
  color: #666;
  font-size: 14px;
  margin-top: 20px;
}

.payment-tips p {
  margin: 5px 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.evaluate-dialog-content {
  padding: 10px 0 0 0;
}
.score-section {
  margin-bottom: 15px;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.evaluate-done {
  color: #67C23A;
  font-weight: 600;
  font-size: 16px;
  padding: 10px 22px;
  border-radius: 8px;
  background: #e8f5e9;
  box-shadow: 0 2px 8px rgba(255, 214, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  min-width: 120px;
  margin: 0 auto;
  letter-spacing: 2px;
  transition: all 0.2s;
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
</style>