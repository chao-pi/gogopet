<template>
  <div class="transport-page">
    <!-- 顶部搜索和筛选区域 -->
    <div class="search-section">
      <div class="search-container">
        <el-input
          v-model="searchQuery"
          placeholder="搜索托运公司"
          class="search-input"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon class="search-icon"><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-section">
        <el-select v-model="selectedRating" placeholder="评分" class="filter-item">
          <el-option label="所有评分" value="" />
          <el-option label="4.5分以上" value="4.5" />
          <el-option label="4.0分以上" value="4.0" />
          <el-option label="3.5分以上" value="3.5" />
          <el-option label="3.0分以上" value="3.0" />
        </el-select>

        <el-select v-model="selectedTransportCount" placeholder="托运次数" class="filter-item">
          <el-option label="所有次数" value="" />
          <el-option label="100次以上" value="100" />
          <el-option label="500次以上" value="500" />
          <el-option label="1000次以上" value="1000" />
        </el-select>

        <el-select v-model="selectedServiceArea" placeholder="服务区域" class="filter-item">
          <el-option label="所有服务区域" value="" />
          <el-option label="省内" value="P" />
          <el-option label="国内" value="D" />
          <el-option label="国际" value="I" />
        </el-select>

        <el-select v-model="selectedPriceRange" placeholder="价格/公里" class="filter-item">
          <el-option label="所有价格" value="" />
          <el-option label="0.5元以下" value="0.5" />
          <el-option label="0.5-1.0元" value="1.0" />
          <el-option label="1.0-1.5元" value="1.5" />
          <el-option label="1.5元以上" value="2.0" />
        </el-select>

        <el-button 
          type="info" 
          plain 
          class="clear-filter-btn"
          @click="clearFilters"
          :disabled="!hasActiveFilters"
        >
          清除筛选
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 托运公司列表 -->
      <div class="company-list">
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else-if="error" class="error-container">
          <el-alert
            title="加载失败"
            type="error"
            :description="error"
            show-icon
          />
        </div>
        <template v-else>
          <div v-for="company in filteredCompanies" :key="company.companyId" class="company-card">
            <div class="company-header">
              <div v-if="company.logoUrl" class="company-logo">
                <img :src="company.logoUrl" :alt="company.companyName">
              </div>
              <div v-else class="company-logo default-logo">
                <el-icon><OfficeBuilding /></el-icon>
              </div>
              <div class="company-info">
                <h3>{{ company.companyName }}</h3>
                <div class="company-rating">
                  <el-rate v-model="company.rating" disabled />
                  <span class="rating-text">{{ company.rating }}分</span>
                </div>
              </div>
            </div>
            
            <div class="company-details">
              <div class="detail-row">
                <div class="detail-item">
                  <el-icon class="detail-icon"><Van /></el-icon>
                  <div class="detail-content">
                    <span class="detail-label">托运次数</span>
                    <span class="detail-value">{{ company.transportCount }}次</span>
                  </div>
                </div>
                
                <div class="detail-item">
                  <el-icon class="detail-icon"><Location /></el-icon>
                  <div class="detail-content">
                    <span class="detail-label">服务区域</span>
                    <span class="detail-value">{{ getServiceAreaText(company.serviceArea) }}</span>
                  </div>
                </div>
                
                <div class="detail-item">
                  <el-icon class="detail-icon"><Money /></el-icon>
                  <div class="detail-content">
                    <span class="detail-label">价格/公里</span>
                    <span class="detail-value price">¥{{ company.transportPricePerKm }}</span>
                  </div>
                </div>
              </div>
              
              <div class="detail-item address-item">
                <el-icon class="detail-icon"><House /></el-icon>
                <div class="detail-content">
                  <span class="detail-label">公司地址</span>
                  <span class="detail-value">{{ company.address }}</span>
                </div>
              </div>
            </div>

            <div class="company-actions">
              <el-button type="primary" @click="viewCompanyDetail(company.companyId)">查看详情</el-button>
              <el-button @click="bookService(company.companyId)">立即预约</el-button>
            </div>
          </div>
        </template>
      </div>

      <!-- 智能助手面板 -->
      <div class="ai-assistant-panel">
        <div class="assistant-header">
          <h3>智能助手</h3>
        </div>

        <div class="assistant-content">
          <div class="quick-questions">
            <h4>快速问答</h4>
            <el-button 
              v-for="question in quickQuestions" 
              :key="question.id"
              type="default"
              @click="handleQuickQuestion(question)"
            >
              {{ question.text }}
            </el-button>
          </div>

          <div class="chat-area" ref="chatArea">
            <div v-for="(message, index) in chatMessages" :key="index" class="message" :class="message.type">
              {{ message.content }}
            </div>
            <div v-if="isTyping" class="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>

          <div class="input-area">
            <el-input
              v-model="userInput"
              placeholder="请输入您的问题"
              @keyup.enter.prevent="handleSendMessage"
            >
              <template #append>
                <el-button type="default" @click.prevent="handleSendMessage" class="send-button">
                  <el-icon><Position /></el-icon>
                </el-button>
              </template>
            </el-input>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, watch, onUnmounted } from 'vue'
import { Search, OfficeBuilding, 
  Van, 
  Location, 
  Money, 
  House,
  Position } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAllCompanyCards } from '@/api/company'
import { useRouter } from 'vue-router'
import { sendMessage as sendChatMessage, getHistory } from '@/api/chat'
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'

const router = useRouter()

// 搜索和筛选数据
const searchQuery = ref('')
const selectedRating = ref('')
const selectedTransportCount = ref('')
const selectedPriceRange = ref('')
const selectedServiceArea = ref('')

// 公司数据
const companies = ref([])
const loading = ref(false)
const error = ref(null)

// 智能助手状态
const userInput = ref('')
const chatMessages = ref([])
const stompClient = ref(null)
const sessionId = ref('')

// 快速问题列表
const quickQuestions = ref([
  { 
    id: 1, 
    text: '如何选择托运公司？需要考虑哪些因素？' 
  },
  { 
    id: 2, 
    text: '宠物托运的价格是如何计算的？' 
  },
  { 
    id: 3, 
    text: '如何实时跟踪宠物的托运状态？' 
  }
])

// 预设问题回答
const quickAnswers = {
  1: `选择托运公司时，建议考虑以下因素：
1. 公司资质：查看是否有正规的营业执照和宠物托运资质
2. 服务评价：参考其他用户的评价和评分
3. 价格透明：了解具体的收费标准和项目
4. 服务范围：确认是否覆盖您的出发地和目的地
5. 安全保障：了解公司的安全措施和保险情况
6. 专业程度：考察工作人员的专业知识和经验
7. 应急处理：了解突发情况的处理方案`,

  2: `宠物托运的价格通常由以下因素决定：
1. 距离：运输距离越远，价格越高
2. 宠物大小：根据宠物体重和体型计算
3. 运输方式：空运、陆运等不同方式价格不同
4. 附加服务：如上门接送、临时寄养等
5. 保险费用：根据保额计算
6. 证件办理：如需代办检疫证明等
建议在选择服务前详细了解各项收费明细`,

  3: `宠物托运需要准备以下证件和材料：
1. 宠物免疫证明：狂犬疫苗等接种记录
2. 动物检疫证明：由当地动物卫生监督所出具
3. 宠物身份证明：如芯片或身份牌
4. 运输箱：符合航空或陆运标准的专用运输箱
5. 宠物照片：近期清晰照片
6. 主人身份证明：身份证复印件
7. 联系方式：确保能及时联系到主人
建议提前准备，避免临时办理延误行程`
}

// 添加打字状态
const isTyping = ref(false)

// 过滤后的公司列表
const filteredCompanies = computed(() => {
  return companies.value.filter(company => {
    const matchesSearch = company.companyName.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesRating = !selectedRating.value || company.rating >= parseFloat(selectedRating.value)
    const matchesTransportCount = !selectedTransportCount.value ||
      (selectedTransportCount.value === '100' && company.transportCount >= 100 && company.transportCount < 500) ||
      (selectedTransportCount.value === '500' && company.transportCount >= 500 && company.transportCount < 1000) ||
      (selectedTransportCount.value === '1000' && company.transportCount >= 1000)
    const matchesPrice = !selectedPriceRange.value || 
      (selectedPriceRange.value === '0.5' && company.transportPricePerKm <= 0.5) ||
      (selectedPriceRange.value === '1.0' && company.transportPricePerKm > 0.5 && company.transportPricePerKm <= 1.0) ||
      (selectedPriceRange.value === '1.5' && company.transportPricePerKm > 1.0 && company.transportPricePerKm <= 1.5) ||
      (selectedPriceRange.value === '2.0' && company.transportPricePerKm > 1.5)
    const matchesServiceArea = !selectedServiceArea.value || company.serviceArea === selectedServiceArea.value
    
    return matchesSearch && matchesRating && matchesTransportCount && matchesPrice && matchesServiceArea
  })
})

// 辅助函数：获取服务区域文本
const getServiceAreaText = (area) => {
  switch (area) {
    case 'P': return '省内'
    case 'D': return '国内'
    case 'I': return '国际'
    default: return '未知'
  }
}

// 加载公司数据
const loadCompanies = async () => {
  loading.value = true
  error.value = null
  try {
    const response = await getAllCompanyCards()
    companies.value = response
  } catch (err) {
    error.value = err.message || '加载公司数据失败'
    ElMessage.error(error.value)
  } finally {
    loading.value = false
  }
}

// 方法
const handleSearch = () => {
  // 实现搜索逻辑
  console.log('搜索:', searchQuery.value)
}

const chatArea = ref(null)

// 滚动到底部的方法
const scrollToBottom = () => {
  nextTick(() => {
    if (chatArea.value) {
      chatArea.value.scrollTop = chatArea.value.scrollHeight
    }
  })
}

// 监听消息变化
watch(chatMessages, () => {
  scrollToBottom()
}, { deep: true })

// 连接 WebSocket
const connectWebSocket = () => {
  try {
    const socket = new SockJS('/ws', null, {
      transports: ['websocket', 'xhr-streaming', 'xhr-polling'],
      timeout: 5000,
      debug: true
    })
    
    stompClient.value = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {},
      debug: function (str) {
        console.log('STOMP:', str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onStompError: (frame) => {
        console.error('STOMP 错误:', frame)
        ElMessage.error('聊天服务连接失败，正在重试...')
      },
      onWebSocketClose: () => {
        console.log('WebSocket 连接关闭')
        ElMessage.warning('聊天服务连接断开，正在重连...')
      },
      onWebSocketError: (event) => {
        console.error('WebSocket 错误:', event)
        ElMessage.error('聊天服务连接错误，正在重试...')
      }
    })

    stompClient.value.onConnect = () => {
      console.log('WebSocket 连接成功')
      ElMessage.success('已连接到聊天服务')
      stompClient.value.subscribe('/topic/public', onMessageReceived)
    }

    stompClient.value.activate()
  } catch (error) {
    console.error('WebSocket 初始化失败:', error)
    ElMessage.error('聊天服务初始化失败，请刷新页面重试')
  }
}

// 修改消息处理函数
const onMessageReceived = (payload) => {
  try {
    const message = JSON.parse(payload.body)
    if (message.type === 'CHAT') {
      if (message.role === 'user') {
        chatMessages.value.push({
          type: 'user',
          content: message.content
        })
      } else if (message.role === 'assistant') {
        isTyping.value = true
        // 如果是新的 AI 消息，添加新消息
        if (chatMessages.value.length === 0 || 
            chatMessages.value[chatMessages.value.length - 1].type !== 'assistant') {
          chatMessages.value.push({
            type: 'assistant',
            content: message.content
          })
        } else {
          // 如果是正在接收的 AI 消息，更新最后一条消息
          chatMessages.value[chatMessages.value.length - 1].content = message.content
        }
        
        // 如果消息结束，关闭打字状态
        if (message.content.endsWith('。') || message.content.endsWith('！') || message.content.endsWith('？')) {
          isTyping.value = false
        }
      }
    } else if (message.type === 'SYSTEM') {
      ElMessage.error(message.content)
    }
  } catch (error) {
    console.error('消息处理错误:', error)
  }
}

// 修改发送消息函数
const handleSendMessage = () => {
  if (!userInput.value.trim()) return
  
  if (!stompClient.value || !stompClient.value.connected) {
    ElMessage.error('聊天服务未连接，请刷新页面重试')
    return
  }

  try {
    const message = {
      type: 'CHAT',
      content: userInput.value,
      sessionId: sessionId.value,
      role: 'user'
    }

    stompClient.value.publish({
      destination: '/app/chat.send',
      body: JSON.stringify(message)
    })

    userInput.value = ''
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送消息失败，请重试')
  }
}

const handleQuickQuestion = (question) => {
  // 添加用户消息
  chatMessages.value.push({
    type: 'user',
    content: question.text
  })
  
  // 添加预设回答
  chatMessages.value.push({
    type: 'assistant',
    content: quickAnswers[question.id] || '抱歉，暂时没有相关问题的预设回答。'
  })
}

const viewCompanyDetail = (companyId) => {
  // 实现查看公司详情逻辑
  ElMessage.info('查看公司详情: ' + companyId)
}

const bookService = (companyId) => {
  // 跳转到订单创建页面，并传递公司ID
  router.push({
    path: '/order/create',
    query: { companyId }
  })
}

// 组件挂载时加载数据
onMounted(() => {
  loadCompanies()
  connectWebSocket()
})

// 组件卸载时断开连接
onUnmounted(() => {
  if (stompClient.value && stompClient.value.connected) {
    stompClient.value.deactivate()
  }
})

const hasActiveFilters = computed(() => {
  return selectedRating.value || selectedTransportCount.value || selectedPriceRange.value || selectedServiceArea.value
})

const clearFilters = () => {
  selectedRating.value = ''
  selectedTransportCount.value = ''
  selectedPriceRange.value = ''
  selectedServiceArea.value = ''
}
</script>

<style scoped>
.transport-page {
  max-width: 1400px;
  margin: 0 auto;
  min-height: 100vh;
  box-sizing: border-box;
  padding: 20px;
  min-height: 100vh;
  box-sizing: border-box;
}

.search-section {
  margin-bottom: 20px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 100%;
}

.search-container {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  position: relative;
  width: 80%;
  margin: 0 auto;
}

.search-input {
  width: 100%;
  border-radius: 20px;
  padding-left: 40px;
  height: 40px;
  font-size: 14px;
}

.search-input :deep(.el-input__wrapper) {
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.05);
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
  background-color: #f5f7fa;
  border-radius: 20px;
}

.search-input :deep(.el-input__wrapper:hover) {
  border-color: #c0c4cc;
  background-color: #ffffff;
}

.search-input :deep(.el-input__wrapper.is-focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64,158,255,0.1);
  background-color: #ffffff;
}

.search-icon {
  color: #409eff;
  font-size: 18px;
  margin-right: 8px;
}

.filter-section {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  padding: 15px;
  /* background: #f8f9fa; */
  border-radius: 12px;
  /* box-shadow: 0 2px 8px 0 rgba(0,0,0,0.05); */
  margin-top: 10px;
}

.filter-item {
  width: 160px;
  border-radius: 20px;
  height: 40px;
  transition: all 0.3s ease;
}

.filter-item :deep(.el-input__wrapper) {
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.05);
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
  background-color: #ffffff;
  border-radius: 20px;
}

.filter-item :deep(.el-input__wrapper:hover) {
  border-color: #409eff;
  box-shadow: 0 2px 12px 0 rgba(64,158,255,0.1);
}

.filter-item :deep(.el-input__wrapper.is-focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64,158,255,0.1);
}

.main-content {
  display: flex;
  gap: 20px;
  min-height: 600px;
}

.company-list {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
  min-width: 0;
  align-content: start;
}

.company-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  display: flex;
  flex-direction: column;
  gap: 15px;
  border: 1px solid #ebeef5;
  height: fit-content;
}

.company-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px 0 rgba(0,0,0,0.15);
}

.company-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.company-logo {
  width: 100%;
  height: 200px;
  border-radius: 8px;
  object-fit: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #909399;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.05);
}

.company-logo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.default-logo {
  font-size: 80px;
}

.company-info {
  width: 100%;
  text-align: center;
}

.company-info h3 {
  margin: 0;
  font-size: 1.4rem;
  color: #333;
  font-weight: 600;
}

.company-rating {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 5px;
  justify-content: center;
}

.rating-text {
  color: #666;
  font-size: 0.9rem;
}

.company-details {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-row {
  display: flex;
  gap: 12px;
}

.detail-item {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px;
  background: white;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.address-item {
  width: 100%;
}

.detail-item:hover {
  transform: translateX(5px);
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.05);
}

.detail-icon {
  font-size: 22px;
  color: #409eff;
  padding: 10px;
  background: #ecf5ff;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 44px;
  min-height: 44px;
}

.detail-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.detail-label {
  font-size: 0.85rem;
  color: #909399;
}

.detail-value {
  font-size: 0.95rem;
  color: #303133;
  font-weight: 500;
}

.detail-value.price {
  color: #f56c6c;
  font-weight: 600;
}

.company-actions {
  display: flex;
  gap: 12px;
  margin-top: auto;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
}

.company-actions .el-button {
  flex: 1;
  border-radius: 20px;
  height: 40px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.company-actions .el-button:first-child {
  background-color: #f0f2f5;
  border-color: #dcdfe6;
  color: #606266;
}

.company-actions .el-button:first-child:hover {
  background-color: #e4e7ed;
  border-color: #c0c4cc;
  color: #409eff;
}

.company-actions .el-button:last-child {
  background-color: #409eff;
  border-color: #409eff;
  color: #fff;
}

.company-actions .el-button:last-child:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

.loading-container, .error-container {
  grid-column: 1 / -1;
  padding: 20px;
  text-align: center;
}

.ai-assistant-panel {
  width: 350px;
  min-width: 350px;
  max-width: 350px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  position: sticky;
  top: 20px;
  height: fit-content;
  border: 1px solid #ebeef5;
  transition: all 0.3s ease;
}

.ai-assistant-panel:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.assistant-header {
  padding: 20px;
  background: linear-gradient(135deg, #409eff 0%, #36cfc9 100%);
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  gap: 12px;
}

.assistant-header h3 {
  margin: 0;
  font-size: 1.2rem;
  color: white;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.assistant-header h3::before {
  content: '';
  display: inline-block;
  width: 8px;
  height: 8px;
  background: #fff;
  border-radius: 50%;
  animation: pulse 1.5s infinite;
}

.assistant-content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.quick-questions {
  margin-bottom: 10px;
}

.quick-questions h4 {
  margin: 0 0 12px 0;
  font-size: 1rem;
  color: #606266;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.quick-questions h4::before {
  content: '💡';
  font-size: 1.2em;
}

.quick-questions .el-button {
  width: 100%;
  text-align: left;
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 12px;
  background: #f5f7fa;
  border: 1px solid #e4e7ed;
  color: #606266;
  transition: all 0.3s ease;
  font-size: 0.95rem;
  line-height: 1.4;
}

.quick-questions .el-button:hover {
  background: #ecf5ff;
  border-color: #409eff;
  color: #409eff;
  transform: translateX(4px);
}

.chat-area {
  height: 400px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 12px;
  border: 1px solid #ebeef5;
  scroll-behavior: smooth;
}

.chat-area::-webkit-scrollbar {
  width: 6px;
}

.chat-area::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.chat-area::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 3px;
}

.chat-area::-webkit-scrollbar-thumb:hover {
  background: #909399;
}

.message {
  margin-bottom: 16px;
  padding: 12px 16px;
  border-radius: 12px;
  max-width: 85%;
  font-size: 0.95rem;
  line-height: 1.5;
  position: relative;
  animation: messageSlide 0.3s ease;
}

@keyframes messageSlide {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.7;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

.message.user {
  background: #ecf5ff;
  color: #409eff;
  margin-left: auto;
  border-bottom-right-radius: 4px;
}

.message.assistant {
  background: white;
  color: #303133;
  margin-right: auto;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.input-area {
  display: flex;
  gap: 12px;
  position: relative;
}

.input-area .el-input {
  border-radius: 12px;
}

.input-area .el-input :deep(.el-input__wrapper) {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
  background-color: white;
  border-radius: 12px;
  padding: 8px 16px;
}

.input-area .el-input :deep(.el-input__wrapper:hover) {
  border-color: #409eff;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.1);
}

.input-area .el-input :deep(.el-input__wrapper.is-focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.send-button {
  color: #409eff;
  padding: 0 20px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  background: none;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.send-button:hover {
  color: #66b1ff;
  transform: scale(1.05);
}

.send-button:active {
  transform: scale(0.95);
}

/* 添加打字机效果 */
.message.assistant {
  white-space: pre-wrap;
  word-break: break-word;
}

/* 添加加载动画 */
.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 8px 12px;
  background: white;
  border-radius: 12px;
  margin-bottom: 16px;
  width: fit-content;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background: #409eff;
  border-radius: 50%;
  animation: typing 1s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-4px);
  }
}

.clear-filter-btn {
  height: 40px;
  border-radius: 20px;
  padding: 0 25px;
  margin-left: 5px;
  font-weight: 500;
  transition: all 0.3s ease;
  background: #f0f2f5;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.clear-filter-btn:hover {
  background: #e4e7ed;
  border-color: #c0c4cc;
  color: #409eff;
}

.clear-filter-btn:disabled {
  background: #f5f7fa;
  border-color: #e4e7ed;
  color: #c0c4cc;
  cursor: not-allowed;
}

.clear-filter-btn:not(:disabled):active {
  background: #d3d4d6;
  border-color: #b1b3b8;
}
</style> 