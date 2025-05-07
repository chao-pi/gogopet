<template>
  <div class="order-tracking-page">
    <div class="page-header">
      <h2>订单追踪</h2>
    </div>

    <div class="main-content">
      <!-- 地图区域 -->
      <div class="map-section">
        <div class="map-container">
          <div id="map-container" class="map"></div>
        </div>
      </div>

      <!-- 订单信息区域 -->
      <div class="order-info-section">
        <el-card class="order-card">
          <template #header>
            <div class="card-header">
              <span>{{ getStatusHeaderText(orderInfo.orderStatus) }}</span>
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
              <span class="label">出发地：</span>
              <span class="value">{{ orderInfo.departure }}</span>
            </div>
            <div class="detail-item">
              <span class="label">目的地：</span>
              <span class="value">{{ orderInfo.destination }}</span>
            </div>
            <div class="detail-item">
              <span class="label">预计到达：</span>
              <span class="value">{{ orderInfo.estimatedArrival }}</span>
            </div>
            <div class="detail-item">
              <span class="label">宠物状态：</span>
              <span class="value">
                <el-tag :type="getPetStatusType(orderInfo.petStatus)">
                  {{ getPetStatusText(orderInfo.petStatus) }}
                </el-tag>
              </span>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 宠物信息区域 -->
      <div class="pet-info-section">
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
            </div>
          </div>
        </el-card>
      </div>

      <!-- 聊天区域 -->
      <div class="chat-section">
        <el-card class="chat-card">
          <template #header>
            <div class="card-header">
              <span>实时沟通</span>
            </div>
          </template>
          
          <div class="chat-container">
            <div class="chat-messages" ref="chatMessages">
              <div v-for="(message, index) in messages" 
                   :key="index" 
                   class="message"
                   :class="message.type">
                <div class="message-content">
                  {{ message.content }}
                </div>
                <div class="message-time">
                  {{ message.time }}
                </div>
              </div>
            </div>
            
            <div class="chat-input">
              <el-input
                v-model="newMessage"
                placeholder="输入消息..."
                @keyup.enter="sendMessage"
              >
                <template #append>
                  <el-button @click="sendMessage">
                    <el-icon><Position /></el-icon>
                  </el-button>
                </template>
              </el-input>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { Position } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getOrderDetail, getOrders, getCompanyOrders } from '@/api/order.js'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

// 高德地图配置
const AMAP_CONFIG = {
  key: '5f5666c361716d0a3fccf30f74ff66eb',
  version: '2.0',
  apiUrl: 'https://restapi.amap.com/v3'
}

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 检查用户权限
const checkUserPermission = () => {
  console.log('当前用户信息:', userStore.userInfo) // 添加调试日志
  console.log('当前用户类型:', userStore.userInfo?.userType) // 修改为从 userInfo 中获取
  if (!userStore.userInfo?.userType) {
    ElMessage.error('用户未登录')
    router.push('/') // 重定向到首页
    return false
  }
  return true
}

// 订单信息
const orderInfo = ref({
  orderId: '',
  orderStatus: '',
  companyName: '',
  departure: '',
  destination: '',
  estimatedArrival: '',
  petName: '',
  petBreed: '',
  petAge: '',
  petImage: '',
  startLatitude: 0,
  startLongitude: 0,
  endLatitude: 0,
  endLongitude: 0,
  petStatus: 'S'
})

// 地图相关变量
const map = ref(null)
const startMarker = ref(null)
const endMarker = ref(null)
const polyline = ref(null)
const midMarkers = ref([]) // 新增：中间浮标数组

// 聊天消息
const messages = ref([
  {
    type: 'system',
    content: '订单已开始运输',
    time: '2025-05-02 10:00'
  },
  {
    type: 'driver',
    content: '您好，我是负责本次运输的司机，有任何问题随时联系我。',
    time: '2025-05-02 10:05'
  }
])

const newMessage = ref('')
const chatMessages = ref(null)

// 获取状态文本
const getStatusText = (status) => {
  console.log('Current status:', status, 'Type:', typeof status) // 添加调试日志
  
  // 确保状态值是字符串类型
  const statusStr = String(status)
  
  const texts = {
    '1': '运输中-起点',
    '2': '运输中-第一检查点',
    '3': '运输中-第二检查点',
    '4': '运输中-第三检查点',
    '5': '运输中-终点',
    'R': '休息中',
    'L': '装卸中',
    'D': '已送达',
    'P': '待支付',
    'W': '待接单',
    'C': '已完成',
    'X': '已取消'
  }
  return texts[statusStr] || '未知状态'
}

// 获取状态类型
const getStatusType = (status) => {
  // 确保状态值是字符串类型
  const statusStr = String(status)
  
  const types = {
    '1': 'info',
    '2': 'warning',
    '3': 'warning',
    '4': 'warning',
    '5': 'success',
    'R': 'info',
    'L': 'warning',
    'D': 'success',
    'P': 'warning',
    'W': 'info',
    'C': 'success',
    'X': 'danger'
  }
  return types[statusStr] || 'info'
}

// 获取状态头部文本
const getStatusHeaderText = (status) => {
  // 确保状态值是字符串类型
  const statusStr = String(status)
  
  const texts = {
    '1': '在起点',
    '2': '已到达第一检查点',
    '3': '已到达第二检查点',
    '4': '已到达第三检查点',
    '5': '已到达终点',
    'R': '休息中',
    'L': '装卸中',
    'D': '已送达',
    'P': '等待支付',
    'W': '等待接单',
    'C': '订单完成',
    'X': '订单已取消'
  }
  return texts[statusStr] || '未知状态'
}

// 获取宠物状态类型
const getPetStatusType = (status) => {
  const types = {
    'N': 'success',    // 正常
    'A': 'warning',    // 异常
    'S': 'info'        // 待确认
  }
  return types[status] || 'info'
}

// 获取宠物状态文本
const getPetStatusText = (status) => {
  const texts = {
    'N': '正常',
    'A': '异常',
    'S': '待确认'
  }
  return texts[status] || '未知状态'
}

// 发送消息
const sendMessage = () => {
  if (!newMessage.value.trim()) return
  
  messages.value.push({
    type: 'user',
    content: newMessage.value,
    time: new Date().toLocaleString()
  })
  
  newMessage.value = ''
  
  // 滚动到底部
  nextTick(() => {
    if (chatMessages.value) {
      chatMessages.value.scrollTop = chatMessages.value.scrollHeight
    }
  })
}

// 获取订单详情
const fetchOrderDetail = async (orderId) => {
  try {
    const response = await getOrderDetail(orderId)
    if (response.code === 200 && response.data) {
      const orderData = response.data
      orderInfo.value = {
        orderId: orderData.orderId,
        orderStatus: orderData.orderStatus,
        companyName: orderData.companyName,
        departure: orderData.startLocation,
        destination: orderData.endLocation,
        estimatedArrival: orderData.estimatedArrival,
        petName: orderData.petName,
        petBreed: orderData.petBreed,
        petAge: orderData.petAge,
        petImage: orderData.petImage,
        startLatitude: orderData.startLatitude,
        startLongitude: orderData.startLongitude,
        endLatitude: orderData.endLatitude,
        endLongitude: orderData.endLongitude,
        petStatus: orderData.petStatus || 'S'
      }
      // 更新地图
      if (map.value) {
        updateMap()
      }
    } else {
      ElMessage.error('获取订单详情失败')
    }
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('获取订单详情失败')
  }
}

// 获取最新订单
const fetchLatestOrder = async () => {
  try {
    // 从用户store中获取用户信息
    if (!userStore.userInfo) {
      ElMessage.error('用户未登录')
      return
    }

    let response
    if (userStore.userInfo.userType === 'C') {
      // 公司用户使用公司ID查询
      if (!userStore.userInfo.companyId) {
        ElMessage.error('未找到公司信息')
        return
      }
      console.log('使用公司ID查询订单:', userStore.userInfo.companyId)
      response = await getCompanyOrders(userStore.userInfo.companyId)
    } else {
      // 普通用户使用用户ID查询
      response = await getOrders({
        userId: userStore.userInfo.id,
        pageNum: 0,
        pageSize: 1
      })
    }

    console.log('获取订单响应:', response) // 添加调试日志

    if (response.code === 200 && response.data && response.data.length > 0) {
      // 按创建时间排序，获取最新的订单
      const sortedOrders = response.data.sort((a, b) => 
        new Date(b.createTime) - new Date(a.createTime)
      )
      const latestOrder = sortedOrders[0]
      await fetchOrderDetail(latestOrder.orderId)
    } else {
      ElMessage.info('暂无订单数据')
    }
  } catch (error) {
    console.error('获取最新订单失败:', error)
    ElMessage.error('获取最新订单失败')
  }
}

// 更新地图显示
const updateMap = () => {
  // 更新地图中心点
  map.value.setCenter([orderInfo.value.startLongitude, orderInfo.value.startLatitude])
  
  // 更新起点标记
  if (startMarker.value) {
    startMarker.value.setPosition([orderInfo.value.startLongitude, orderInfo.value.startLatitude])
  }
  
  // 更新终点标记
  if (endMarker.value) {
    endMarker.value.setPosition([orderInfo.value.endLongitude, orderInfo.value.endLatitude])
  }
  
  // 更新路径规划
  window.AMap.plugin('AMap.Driving', function() {
    const driving = new window.AMap.Driving({
      map: map.value,
      policy: window.AMap.DrivingPolicy.LEAST_DISTANCE
    })
    driving.search(
      [orderInfo.value.startLongitude, orderInfo.value.startLatitude],
      [orderInfo.value.endLongitude, orderInfo.value.endLatitude],
      function(status, result) {
        if (status === 'complete') {
          console.log('最短路径规划成功', result)
        } else {
          console.error('最短路径规划失败', result)
        }
      }
    )
  })
}

// 获取驾车路线
const getDrivingRoute = async (start, end) => {
  try {
    const apiKey = '5f5666c361716d0a3fccf30f74ff66eb'
    const response = await fetch(
      `https://restapi.amap.com/v3/direction/driving?origin=${start.longitude},${start.latitude}&destination=${end.longitude},${end.latitude}&key=${apiKey}&strategy=0&extensions=all`
    )
    const data = await response.json()
    
    if (data.status === '1' && data.route && data.route.paths && data.route.paths.length > 0) {
      return data.route.paths[0]
    } else {
      console.error('路径规划失败:', data)
      throw new Error(data.info || '路径规划失败')
    }
  } catch (error) {
    console.error('获取驾车路线失败:', error)
    throw error
  }
}

// 绘制路线
const drawRoute = (route) => {
  try {
    if (!map.value || !route) return
    
    // 清除现有路线
    if (polyline.value) {
      map.value.remove(polyline.value)
    }
    // 清除中间浮标
    if (midMarkers.value && midMarkers.value.length > 0) {
      midMarkers.value.forEach(marker => map.value.remove(marker))
      midMarkers.value = []
    }
    
    // 解析路线坐标
    const path = route.steps.map(step => {
      return step.polyline.split(';').map(point => {
        const [lng, lat] = point.split(',')
        return [parseFloat(lng), parseFloat(lat)]
      })
    }).flat()
    
    // 创建路线
    polyline.value = new window.AMap.Polyline({
      path: path,
      strokeColor: '#409EFF',
      strokeWeight: 6,
      strokeOpacity: 0.8,
      strokeStyle: 'solid',
      showDir: true
    })
    
    // 添加到地图
    map.value.add(polyline.value)
    
    // 浮标样式
    const midMarkerOptions = {
      icon: new window.AMap.Icon({
        size: new window.AMap.Size(19, 31),
        imageSize: new window.AMap.Size(19, 31),
        image: 'https://webapi.amap.com/theme/v1.3/markers/b/mark_bs.png',
        imageOffset: new window.AMap.Pixel(0, 0)
      }),
      offset: new window.AMap.Pixel(-9, -31)
    }
    
    // 均匀放置3个浮标
    if (path.length >= 4) {
      const idx1 = Math.floor(path.length / 4)
      const idx2 = Math.floor(path.length / 2)
      const idx3 = Math.floor(path.length * 3 / 4)
      const marker1 = new window.AMap.Marker({ position: path[idx1], ...midMarkerOptions })
      const marker2 = new window.AMap.Marker({ position: path[idx2], ...midMarkerOptions })
      const marker3 = new window.AMap.Marker({ position: path[idx3], ...midMarkerOptions })
      marker1.on('click', () => { map.value.setCenter(path[idx1]); map.value.setZoom(15) })
      marker2.on('click', () => { map.value.setCenter(path[idx2]); map.value.setZoom(15) })
      marker3.on('click', () => { map.value.setCenter(path[idx3]); map.value.setZoom(15) })
      map.value.add([marker1, marker2, marker3])
      midMarkers.value = [marker1, marker2, marker3]
    }
    
    // 调整地图视野以包含整个路线
    map.value.setFitView()
    
    // 更新预计到达时间
    if (route.duration) {
      const duration = Math.ceil(route.duration / 60) // 转换为分钟
      const arrivalTime = new Date(Date.now() + route.duration * 1000)
      orderInfo.value.estimatedArrival = arrivalTime.toLocaleString()
      orderInfo.value.duration = `${duration}分钟`
    }
  } catch (error) {
    console.error('绘制路线失败:', error)
    throw error
  }
}

// 初始化地图
const initMap = async () => {
  try {
    if (!window.AMap) {
      console.error('高德地图 API 未加载')
      return
    }
    
    // 创建地图实例
    map.value = new window.AMap.Map('map-container', {
      zoom: 12,
      center: [orderInfo.value.startLongitude, orderInfo.value.startLatitude],
      resizeEnable: true,
      viewMode: '3D',
      pitch: 50,
      rotation: 0
    })
    
    // 添加起点标记
    startMarker.value = new window.AMap.Marker({
      position: [orderInfo.value.startLongitude, orderInfo.value.startLatitude],
      title: '起点',
      icon: new window.AMap.Icon({
        size: new window.AMap.Size(19, 31), // 图标大小
        imageSize: new window.AMap.Size(19, 31),
        image: 'https://webapi.amap.com/theme/v1.3/markers/b/start.png',
        imageOffset: new window.AMap.Pixel(0, 0)
      }),
      offset: new window.AMap.Pixel(-9, -31)
    })
    map.value.add(startMarker.value)
    
    // 添加终点标记
    endMarker.value = new window.AMap.Marker({
      position: [orderInfo.value.endLongitude, orderInfo.value.endLatitude],
      title: '终点',
      icon: new window.AMap.Icon({
        size: new window.AMap.Size(19, 31),
        imageSize: new window.AMap.Size(19, 31),
        image: 'https://webapi.amap.com/theme/v1.3/markers/b/end.png',
        imageOffset: new window.AMap.Pixel(0, 0)
      }),
      offset: new window.AMap.Pixel(-9, -31)
    })
    map.value.add(endMarker.value)

    // 添加标记点击事件
    startMarker.value.on('click', () => {
      map.value.setCenter([orderInfo.value.startLongitude, orderInfo.value.startLatitude])
      map.value.setZoom(15)
    })

    endMarker.value.on('click', () => {
      map.value.setCenter([orderInfo.value.endLongitude, orderInfo.value.endLatitude])
      map.value.setZoom(15)
    })
    
    // 获取并绘制路线
    const route = await getDrivingRoute(
      { longitude: orderInfo.value.startLongitude, latitude: orderInfo.value.startLatitude },
      { longitude: orderInfo.value.endLongitude, latitude: orderInfo.value.endLatitude }
    )
    drawRoute(route)
  } catch (error) {
    console.error('初始化地图失败:', error)
    ElMessage.error('地图初始化失败，请稍后重试')
  }
}

// 在组件挂载时检查权限并获取数据
onMounted(async () => {
  if (!checkUserPermission()) return
  
  const orderId = route.params.orderId
  if (orderId) {
    await fetchOrderDetail(orderId)
  } else {
    await fetchLatestOrder()
  }
  
  // 等待 DOM 更新完成
  await nextTick()
  
  // 确保地图容器存在
  const mapContainer = document.getElementById('map-container')
  if (!mapContainer) {
    console.error('地图容器未找到')
    return
  }
  
  // 确保AMap已经加载
  if (window.AMap) {
    initMap()
  } else {
    // 动态加载高德地图
    const script = document.createElement('script')
    script.src = `https://webapi.amap.com/maps?v=${AMAP_CONFIG.version}&key=${AMAP_CONFIG.key}`
    script.async = true
    script.onload = initMap
    script.onerror = () => {
      ElMessage.error('地图加载失败，请刷新页面重试')
    }
    document.head.appendChild(script)
  }
})
</script>

<style scoped>
.order-tracking-page {
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
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: auto auto auto;
  gap: 20px;
}

.map-section {
  grid-column: 1 / -1;
  grid-row: 1;
}

.map-container {
  background: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
}

.map {
  width: 100%;
  height: 400px;
  background: #e0e0e0;
}

.order-info-section,
.pet-info-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: 100%;
}

.order-card,
.pet-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.order-details,
.pet-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-item {
  display: flex;
  align-items: center;
}

.label {
  color: #666;
  width: 80px;
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

.chat-section {
  grid-column: 1 / -1;
  grid-row: 3;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 300px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message {
  max-width: 80%;
  padding: 8px 12px;
  border-radius: 8px;
  margin-bottom: 8px;
}

.message.system {
  align-self: center;
  background: #f0f0f0;
  color: #666;
}

.message.driver {
  align-self: flex-start;
  background: #e6f7ff;
  color: #333;
}

.message.user {
  align-self: flex-end;
  background: #1890ff;
  color: white;
}

.message-time {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.chat-input {
  padding: 12px;
  border-top: 1px solid #eee;
}

@media (max-width: 768px) {
  .main-content {
    grid-template-columns: 1fr;
  }
  
  .order-info-section,
  .pet-info-section {
    grid-column: 1;
  }
  
  .chat-section {
    grid-column: 1;
  }
}
</style> 