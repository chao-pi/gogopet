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

      <!-- 状态上传区域 -->
      <div class="order-info-section">
        <el-card class="order-card">
          <template #header>
            <div class="card-header">
              <span>状态上传</span>
            </div>
          </template>
          <div class="order-details">
            <div v-for="(item, idx) in statusList" :key="item.name" class="detail-item">
              <span class="label">{{ item.label }}：</span>
              <template v-if="item.status === 'done'">
                <el-tag type="success">已完成</el-tag>
              </template>
              <template v-else-if="isCurrent(idx)">
                <el-button type="primary" @click="uploadStatus(idx)" :disabled="!canUploadStatus">上传</el-button>
              </template>
              <template v-else>
                <el-tag type="info">未开始</el-tag>
              </template>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 宠物信息区域 -->
      <div class="pet-info-section">
        <el-card class="pet-card">
          <template #header>
            <div class="card-header">
              <span>宠物情绪分析</span>
            </div>
          </template>
          
          <div class="pet-details">
            <div v-if="!previewImage" class="upload-section">
              <el-upload
                class="upload-demo"
                drag
                action="http://localhost:8080/api/ml/analyze"
                :headers="{
                  'Authorization': userStore.token ? `Bearer ${userStore.token}` : '',
                  'Accept': 'application/json'
                }"
                :on-success="handleUploadSuccess"
                :on-error="handleUploadError"
                :before-upload="beforeUpload"
                :show-file-list="false"
                name="file"
                :data="{}"
              >
                <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                <div class="el-upload__text">
                  拖拽图片到此处或 <em>点击上传</em>
                </div>
                <template #tip>
                  <div class="el-upload__tip">
                    请上传清晰的宠物图片，支持 jpg/png 格式
                  </div>
                </template>
              </el-upload>
            </div>

            <div v-if="analysisResult" class="analysis-result">
              <div class="result-header">
                <h3>分析结果</h3>
                <el-button type="primary" @click="resetAnalysis" size="small">
                  重新上传
                </el-button>
              </div>
              <div class="result-content">
                <div class="detail-item">
                  <span class="label">情绪状态：</span>
                  <span class="value">{{ analysisResult.emotion }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">置信度：</span>
                  <span class="value">{{ (analysisResult.confidence * 100).toFixed(2) }}%</span>
                </div>
                <div class="detail-item">
                  <span class="label">分析时间：</span>
                  <span class="value">{{ analysisResult.timestamp }}</span>
                </div>
              </div>
            </div>

            <div v-if="previewImage" class="preview-image">
              <img :src="previewImage" alt="预览图片">
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
import { Position, UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getOrderDetail, updateOrderStatus } from '@/api/order'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import axios from 'axios'

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
  console.log('当前用户信息:', userStore.userInfo)
  console.log('当前用户类型:', userStore.userInfo?.userType)
  if (!userStore.userInfo?.userType || userStore.userInfo.userType !== 'C') {
    ElMessage.error('只有托运公司用户可以访问订单追踪页面')
    router.push('/') // 重定向到首页
    return false
  }
  return true
}

// 状态上传相关
const statusList = ref([
  { name: '1', label: '起点', status: 'done' },
  { name: '2', label: '第一检查点', status: 'pending' },
  { name: '3', label: '第二检查点', status: 'pending' },
  { name: '4', label: '第三检查点', status: 'pending' },
  { name: '5', label: '终点', status: 'pending' }
])

// 上传表单
const uploadForm = ref({
  status: '',
  location: ''
})

// 上传对话框可见性
const uploadDialogVisible = ref(false)

// 判断是否为当前可操作点
const isCurrent = (idx) => {
  const currentStatus = orderInfo.value.orderStatus
  if (!currentStatus) return false
  
  // 如果订单已完成，则禁用所有上传按钮
  if (currentStatus === 'C') return false
  
  // 如果是最后一个状态且已完成，则禁用上传
  if (currentStatus === '5' && idx === 4) return false
  
  // 如果是当前状态的下一个状态，则允许上传
  const currentIdx = statusList.value.findIndex(item => item.name === currentStatus)
  return idx === currentIdx + 1
}

// 上传图片后才能上传状态
const canUploadStatus = ref(false)

// 上传状态
const uploadStatus = async (idx) => {
  try {
    // 只用 analysisResult，不再请求情绪分析
    if (!analysisResult.value || !analysisResult.value.emotion) {
      ElMessage.error('请先上传并分析宠物图片');
      return;
    }
    const petStatus = analysisResult.value.emotion === 'happy' ? 'N' : 'A';
    const currentStatus = statusList.value[idx].name;

    // 更新订单状态（包含宠物状态）
    const response = await updateOrderStatus(orderInfo.value.orderId, currentStatus, petStatus, orderInfo.value.currentLocation || '');

    if (response.code === 200) {
      ElMessage.success('状态更新成功');
      statusList.value[idx].status = 'done';
      await fetchOrderData();
      // 如果是最后一个节点，自动将订单状态设为C（已完成）
      if (idx === 4) {
        const finishResp = await updateOrderStatus(orderInfo.value.orderId, 'C', petStatus, orderInfo.value.currentLocation || '');
        if (finishResp.code === 200) {
          ElMessage.success('订单已完成');
          await fetchOrderData();
        } else {
          ElMessage.error(finishResp.message || '订单完成状态更新失败');
        }
      }
    } else {
      ElMessage.error(response.message || '状态更新失败');
    }
  } catch (error) {
    console.error('上传状态失败:', error);
    ElMessage.error('上传状态失败，请稍后重试');
  }
}

// 订单信息
const orderInfo = ref({
  orderId: 'ORD202405010001',
  orderStatus: '1',
  companyName: '北京宠物托运有限公司',
  departure: '北京市朝阳区建国路88号',
  destination: '上海市浦东新区陆家嘴环路1000号',
  estimatedArrival: '2025-05-02 18:00',
  petName: '旺财',
  petBreed: '金毛',
  petAge: 2,
  petImage: 'https://example.com/pet.jpg',
  startLatitude: 39.9042,
  startLongitude: 116.4074,
  endLatitude: 31.2304,
  endLongitude: 121.4737
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

// 图片上传和分析相关
const previewImage = ref(null)
const analysisResult = ref(null)

// 浮标样式
const startMarkerOptions = {
  icon: new AMap.Icon({
    size: new AMap.Size(19, 31), // 图标大小
    imageSize: new AMap.Size(19, 31),
    image: 'https://webapi.amap.com/theme/v1.3/markers/b/start.png',
    imageOffset: new AMap.Pixel(0, 0)
  }),
  offset: new AMap.Pixel(-9, -31)
}
const endMarkerOptions = {
  icon: new AMap.Icon({
    size: new AMap.Size(19, 31),
    imageSize: new AMap.Size(19, 31),
    image: 'https://webapi.amap.com/theme/v1.3/markers/b/end.png',
    imageOffset: new AMap.Pixel(0, 0)
  }),
  offset: new AMap.Pixel(-9, -31)
}
const midMarkerOptions = {
  icon: new AMap.Icon({
    size: new AMap.Size(19, 31),
    imageSize: new AMap.Size(19, 31),
    image: 'https://webapi.amap.com/theme/v1.3/markers/b/mark_bs.png',
    imageOffset: new AMap.Pixel(0, 0)
  }),
  offset: new AMap.Pixel(-9, -31)
}

// 上传前检查
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB！')
    return false
  }

  // 创建预览
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = (e) => {
    previewImage.value = e.target.result
  }

  return true
}

// 上传成功处理
const handleUploadSuccess = (response) => {
  console.log('上传成功，响应:', response)
  if (response.code === 200) {
    analysisResult.value = {
      emotion: response.data.emotion,
      confidence: response.data.confidence,
      timestamp: new Date().toLocaleString()
    }
    ElMessage.success('图片分析成功')
    canUploadStatus.value = true
  } else {
    ElMessage.error(response.message || '分析失败')
    canUploadStatus.value = false
  }
}

// 上传失败处理
const handleUploadError = (error) => {
  console.error('上传失败:', error)
  let errorMessage = '图片上传失败，请重试'
  
  if (error.response) {
    if (error.response.status === 403) {
      errorMessage = '请先登录后再上传图片'
      router.push('/login')
    } else {
      errorMessage = error.response.data?.message || errorMessage
    }
  }
  
  ElMessage.error(errorMessage)
}

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    'P': 'warning',
    'W': 'info',
    '1': 'primary',
    '2': 'primary',
    '3': 'primary',
    '4': 'primary',
    '5': 'primary',
    'C': 'success',
    'X': 'danger',
    'R': 'info',
    'L': 'info'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    'P': '待支付',
    'W': '待接单',
    '1': '运输中-起点',
    '2': '运输中-第一检查点',
    '3': '运输中-第二检查点',
    '4': '运输中-第三检查点',
    '5': '运输中-终点',
    'C': '已完成',
    'X': '已取消',
    'R': '休息中',
    'L': '装卸中'
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

// 获取订单数据
const fetchOrderData = async () => {
  try {
    const orderId = route.params.orderId
    if (!orderId) {
      console.log('使用默认订单数据')
      return
    }
    
    const response = await getOrderDetail(orderId)
    if (response.data) {
      // 更新订单信息
      orderInfo.value = {
        ...response.data,
        startLatitude: response.data.startLatitude,
        startLongitude: response.data.startLongitude,
        endLatitude: response.data.endLatitude,
        endLongitude: response.data.endLongitude
      }
      
      // 更新状态列表
      updateStatusList()
      
      // 如果地图已经初始化，更新地图显示
      if (map.value) {
        updateMap()
      }
    } else {
      ElMessage.error('获取订单数据失败')
    }
  } catch (error) {
    console.error('获取订单数据失败:', error)
    ElMessage.error('获取订单数据失败')
  }
}

// 更新状态列表
const updateStatusList = () => {
  const currentStatus = orderInfo.value.orderStatus
  if (!currentStatus) return
  
  // 更新已完成的状态
  statusList.value.forEach(item => {
    if (parseInt(item.name) <= parseInt(currentStatus)) {
      item.status = 'done'
    } else {
      item.status = 'pending'
    }
  })
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
  const maxRetries = 3
  let retryCount = 0

  while (retryCount < maxRetries) {
    try {
      const response = await fetch(
        `https://restapi.amap.com/v3/direction/driving?origin=${start.lng},${start.lat}&destination=${end.lng},${end.lat}&key=5f5666c361716d0a3fccf30f74ff66eb&strategy=0&extensions=all`
      )
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      
      const data = await response.json()
      
      if (data.status === '1' && data.route) {
        return data.route
      } else {
        throw new Error(data.info || '获取路线失败')
      }
    } catch (error) {
      console.error(`获取驾车路线失败 (尝试 ${retryCount + 1}/${maxRetries}):`, error)
      retryCount++
      
      if (retryCount === maxRetries) {
        ElMessage.error('获取路线失败，请检查网络连接或稍后重试')
        throw error
      }
      
      // 等待一段时间后重试
      await new Promise(resolve => setTimeout(resolve, 1000 * retryCount))
    }
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
    
    // 均匀放置3个浮标
    if (path.length >= 4) {
      const idx1 = Math.floor(path.length / 4)
      const idx2 = Math.floor(path.length / 2)
      const idx3 = Math.floor(path.length * 3 / 4)
      const marker1 = new window.AMap.Marker({ position: path[idx1], ...midMarkerOptions })
      const marker2 = new window.AMap.Marker({ position: path[idx2], ...midMarkerOptions })
      const marker3 = new window.AMap.Marker({ position: path[idx3], ...midMarkerOptions })

      // 添加浮标点击事件
      marker1.on('click', () => {
        map.value.setCenter(path[idx1])
        map.value.setZoom(15)
      })
      marker2.on('click', () => {
        map.value.setCenter(path[idx2])
        map.value.setZoom(15)
      })
      marker3.on('click', () => {
        map.value.setCenter(path[idx3])
        map.value.setZoom(15)
      })

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
    // 确保 AMap 已加载
    if (!window.AMap) {
      await new Promise((resolve, reject) => {
        const script = document.createElement('script')
        script.src = `https://webapi.amap.com/maps?v=2.0&key=5f5666c361716d0a3fccf30f74ff66eb&callback=initAMap`
        script.async = true
        script.onerror = () => reject(new Error('加载高德地图脚本失败'))
        window.initAMap = () => {
          resolve()
          delete window.initAMap
        }
        document.head.appendChild(script)
      })
    }

    // 初始化地图
    map.value = new AMap.Map('map-container', {
      zoom: 11,
      center: [orderInfo.value.startLongitude, orderInfo.value.startLatitude],
      viewMode: '3D'
    })

    // 加载地图控件插件
    await new Promise((resolve, reject) => {
      AMap.plugin(['AMap.Scale', 'AMap.ToolBar'], () => {
        resolve()
      })
    })

    // 添加地图控件
    map.value.addControl(new AMap.Scale())
    map.value.addControl(new AMap.ToolBar())

    // 获取路线
    try {
      const route = await getDrivingRoute(
        { lng: orderInfo.value.startLongitude, lat: orderInfo.value.startLatitude },
        { lng: orderInfo.value.endLongitude, lat: orderInfo.value.endLatitude }
      )

      // 绘制路线
      const path = route.paths[0].steps.map(step => step.polyline.split(';').map(point => {
        const [lng, lat] = point.split(',').map(Number)
        return [lng, lat]
      })).flat()

      // 创建折线
      const polyline = new AMap.Polyline({
        path: path,
        strokeColor: '#3366FF',
        strokeWeight: 6,
        strokeOpacity: 0.8
      })

      // 将折线添加到地图
      polyline.setMap(map.value)

      // 均匀放置3个浮标
      if (path.length >= 4) {
        const idx1 = Math.floor(path.length / 4)
        const idx2 = Math.floor(path.length / 2)
        const idx3 = Math.floor(path.length * 3 / 4)
        const marker1 = new AMap.Marker({ position: path[idx1], ...midMarkerOptions })
        const marker2 = new AMap.Marker({ position: path[idx2], ...midMarkerOptions })
        const marker3 = new AMap.Marker({ position: path[idx3], ...midMarkerOptions })
        marker1.on('click', () => { map.value.setCenter(path[idx1]); map.value.setZoom(15) })
        marker2.on('click', () => { map.value.setCenter(path[idx2]); map.value.setZoom(15) })
        marker3.on('click', () => { map.value.setCenter(path[idx3]); map.value.setZoom(15) })
        map.value.add([marker1, marker2, marker3])
        midMarkers.value = [marker1, marker2, marker3]
      }

      // 调整地图视野以包含整个路线
      map.value.setFitView([polyline])

      // 添加起点和终点标记
      const startMarker = new AMap.Marker({
        position: [orderInfo.value.startLongitude, orderInfo.value.startLatitude],
        ...startMarkerOptions
      })

      const endMarker = new AMap.Marker({
        position: [orderInfo.value.endLongitude, orderInfo.value.endLatitude],
        ...endMarkerOptions
      })

      // 将标记添加到地图
      startMarker.setMap(map.value)
      endMarker.setMap(map.value)

      // 添加点击事件
      startMarker.on('click', () => {
        map.value.setCenter([orderInfo.value.startLongitude, orderInfo.value.startLatitude])
        map.value.setZoom(15)
      })

      endMarker.on('click', () => {
        map.value.setCenter([orderInfo.value.endLongitude, orderInfo.value.endLatitude])
        map.value.setZoom(15)
      })

    } catch (error) {
      console.error('获取路线失败:', error)
      ElMessage.error('获取路线失败，请稍后重试')
    }

  } catch (error) {
    console.error('初始化地图失败:', error)
    ElMessage.error('初始化地图失败，请刷新页面重试')
  }
}

// 在组件挂载时检查权限
onMounted(async () => {
  if (!checkUserPermission()) {
    return
  }
  
  // 先获取订单数据
  await fetchOrderData()
  
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

// 在 script setup 部分添加 resetAnalysis 方法
const resetAnalysis = () => {
  previewImage.value = null
  analysisResult.value = null
}
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

.upload-section {
  padding: 20px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  text-align: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}

.upload-section:hover {
  border-color: #409EFF;
}

.analysis-result {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.result-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.result-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.preview-image {
  margin-top: 20px;
  text-align: center;
}

.preview-image img {
  max-width: 100%;
  max-height: 200px;
  border-radius: 6px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style> 