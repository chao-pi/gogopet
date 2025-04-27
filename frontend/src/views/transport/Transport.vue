<template>
  <div class="transport-page">
    <!-- 顶部搜索和筛选区域 -->
    <div class="search-section">
      <div class="search-container">
        <el-input
          v-model="searchQuery"
          placeholder="搜索目的地或托运公司"
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

        <el-select v-model="selectedTransportMethod" placeholder="运输方式" class="filter-item">
          <el-option label="所有运输方式" value="" />
          <el-option label="空运" value="A" />
          <el-option label="陆运" value="L" />
          <el-option label="海运" value="S" />
        </el-select>

        <el-select v-model="selectedServiceArea" placeholder="服务区域" class="filter-item">
          <el-option label="所有服务区域" value="" />
          <el-option label="省内" value="P" />
          <el-option label="国内" value="D" />
          <el-option label="国际" value="I" />
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
                    <span class="detail-label">运输方式</span>
                    <span class="detail-value">{{ getTransportMethodsText(company.transportMethods) }}</span>
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
              type="text"
              @click="handleQuickQuestion(question)"
            >
              {{ question.text }}
            </el-button>
          </div>

          <div class="chat-area">
            <div v-for="(message, index) in chatMessages" :key="index" class="message" :class="message.type">
              {{ message.content }}
            </div>
          </div>

          <div class="input-area">
            <el-input
              v-model="userInput"
              placeholder="请输入您的问题"
              @keyup.enter="sendMessage"
            >
              <template #append>
                <el-button @click="sendMessage">发送</el-button>
              </template>
            </el-input>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Search, OfficeBuilding, 
  Van, 
  Location, 
  Money, 
  House } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAllCompanyCards } from '@/api/company'

// 搜索和筛选数据
const searchQuery = ref('')
const selectedRating = ref('')
const selectedTransportMethod = ref('')
const selectedServiceArea = ref('')

// 公司数据
const companies = ref([])
const loading = ref(false)
const error = ref(null)

// 智能助手状态
const userInput = ref('')
const chatMessages = ref([])

// 快速问题列表
const quickQuestions = ref([
  { id: 1, text: '如何选择托运公司？' },
  { id: 2, text: '托运价格如何计算？' },
  { id: 3, text: '需要准备哪些材料？' },
  { id: 4, text: '如何跟踪托运状态？' }
])

// 过滤后的公司列表
const filteredCompanies = computed(() => {
  return companies.value.filter(company => {
    const matchesSearch = company.companyName.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesRating = !selectedRating.value || company.rating >= parseFloat(selectedRating.value)
    const matchesTransportMethod = !selectedTransportMethod.value || company.transportMethods.includes(selectedTransportMethod.value)
    const matchesServiceArea = !selectedServiceArea.value || company.serviceArea === selectedServiceArea.value
    
    return matchesSearch && matchesRating && matchesTransportMethod && matchesServiceArea
  })
})

// 辅助函数：获取运输方式文本
const getTransportMethodsText = (methods) => {
  const texts = []
  if (methods.includes('A')) texts.push('空运')
  if (methods.includes('L')) texts.push('陆运')
  if (methods.includes('S')) texts.push('海运')
  return texts.join('、')
}

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

const handleQuickQuestion = (question) => {
  // 处理快速问题
  chatMessages.value.push({
    type: 'user',
    content: question.text
  })
  // 模拟AI回复
  setTimeout(() => {
    chatMessages.value.push({
      type: 'assistant',
      content: '这是关于"' + question.text + '"的回复...'
    })
  }, 500)
}

const sendMessage = () => {
  if (!userInput.value.trim()) return
  
  chatMessages.value.push({
    type: 'user',
    content: userInput.value
  })
  
  // 模拟AI回复
  setTimeout(() => {
    chatMessages.value.push({
      type: 'assistant',
      content: '这是关于"' + userInput.value + '"的回复...'
    })
  }, 500)
  
  userInput.value = ''
}

const viewCompanyDetail = (companyId) => {
  // 实现查看公司详情逻辑
  ElMessage.info('查看公司详情: ' + companyId)
}

const bookService = (companyId) => {
  // 实现预约服务逻辑
  ElMessage.success('预约服务: ' + companyId)
}

// 组件挂载时加载数据
onMounted(() => {
  loadCompanies()
})

const hasActiveFilters = computed(() => {
  return selectedRating.value || selectedTransportMethod.value || selectedServiceArea.value
})

const clearFilters = () => {
  selectedRating.value = ''
  selectedTransportMethod.value = ''
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
  background: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,0.05);
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
  gap: 10px;
  margin-top: auto;
}

.company-actions .el-button {
  flex: 1;
  border-radius: 20px;
}

.loading-container, .error-container {
  grid-column: 1 / -1;
  padding: 20px;
  text-align: center;
}

.ai-assistant-panel {
  width: 300px;
  min-width: 300px;
  max-width: 300px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  overflow: hidden;
  position: sticky;
  top: 20px;
  height: fit-content;
  border: 1px solid #ebeef5;
}

.assistant-header {
  padding: 15px;
  background: #f5f7fa;
  border-bottom: 1px solid #eee;
}

.assistant-header h3 {
  margin: 0;
  font-size: 1.1rem;
  color: #333;
  font-weight: 600;
}

.assistant-content {
  padding: 15px;
}

.quick-questions {
  margin-bottom: 20px;
}

.quick-questions h4 {
  margin: 0 0 10px 0;
  font-size: 1rem;
  color: #666;
  font-weight: 500;
}

.quick-questions .el-button {
  width: 100%;
  text-align: left;
  padding: 8px 15px;
  margin-bottom: 5px;
  border-radius: 8px;
}

.chat-area {
  height: 300px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 10px;
  background: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #ebeef5;
}

.message {
  margin-bottom: 10px;
  padding: 8px 12px;
  border-radius: 8px;
  max-width: 80%;
  font-size: 0.9rem;
  line-height: 1.5;
}

.message.user {
  background: #e3f2fd;
  color: #1a73e8;
  margin-left: auto;
}

.message.assistant {
  background: #f5f5f5;
  color: #333;
  margin-right: auto;
}

.input-area {
  display: flex;
  gap: 10px;
}

.input-area .el-input {
  border-radius: 20px;
}

.input-area .el-button {
  border-radius: 20px;
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