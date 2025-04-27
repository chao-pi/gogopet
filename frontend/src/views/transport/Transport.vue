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
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-section">
        <el-select v-model="selectedServiceType" placeholder="服务类型" class="filter-item">
          <el-option label="普通托运" value="normal" />
          <el-option label="专车托运" value="special" />
          <el-option label="空运" value="air" />
        </el-select>

        <el-select v-model="selectedPriceRange" placeholder="价格区间" class="filter-item">
          <el-option label="0-500元" value="0-500" />
          <el-option label="500-1000元" value="500-1000" />
          <el-option label="1000元以上" value="1000+" />
        </el-select>

        <el-select v-model="selectedDistance" placeholder="距离" class="filter-item">
          <el-option label="5km以内" value="5" />
          <el-option label="10km以内" value="10" />
          <el-option label="20km以内" value="20" />
        </el-select>
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
              <img :src="company.logoUrl || 'https://via.placeholder.com/50'" :alt="company.companyName" class="company-logo">
              <div class="company-info">
                <h3>{{ company.companyName }}</h3>
                <div class="company-rating">
                  <el-rate v-model="company.rating" disabled />
                  <span class="rating-text">{{ company.rating }}分</span>
                </div>
              </div>
            </div>
            
            <div class="company-details">
              <p class="service-range">服务范围：{{ company.serviceRange }}</p>
              <p class="address">公司地址：{{ company.address }}</p>
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
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAllCompanyCards } from '@/api/company'

// 搜索和筛选数据
const searchQuery = ref('')
const selectedServiceType = ref('')
const selectedPriceRange = ref('')
const selectedDistance = ref('')

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
    const matchesServiceType = !selectedServiceType.value || company.serviceRange.includes(selectedServiceType.value)
    const matchesPrice = !selectedPriceRange.value || checkPriceRange(company.startingPrice, selectedPriceRange.value)
    const matchesDistance = !selectedDistance.value || company.distance <= parseInt(selectedDistance.value)
    
    return matchesSearch && matchesServiceType && matchesPrice && matchesDistance
  })
})

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

// 辅助函数
const checkPriceRange = (price, range) => {
  const [min, max] = range.split('-').map(Number)
  return price >= min && price <= max
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
</script>

<style scoped>
.transport-page {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.search-section {
  margin-bottom: 20px;
}

.search-container {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.search-input {
  flex: 1;
}

.filter-section {
  display: flex;
  gap: 10px;
}

.filter-item {
  width: 150px;
}

.main-content {
  display: flex;
  gap: 20px;
}

.company-list {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.company-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.company-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px 0 rgba(0,0,0,0.15);
}

.company-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.company-logo {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.company-info {
  flex: 1;
}

.company-info h3 {
  margin: 0;
  font-size: 1.2rem;
  color: #333;
}

.company-rating {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 5px;
}

.rating-text {
  color: #666;
  font-size: 0.9rem;
}

.company-details {
  margin-bottom: 20px;
}

.service-range, .address {
  margin: 5px 0;
  color: #666;
  font-size: 0.9rem;
}

.company-actions {
  display: flex;
  gap: 10px;
}

.loading-container, .error-container {
  grid-column: 1 / -1;
  padding: 20px;
  text-align: center;
}

.ai-assistant-panel {
  width: 300px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  overflow: hidden;
}

.assistant-header {
  padding: 15px;
  background: #f5f5f5;
  border-bottom: 1px solid #eee;
}

.assistant-header h3 {
  margin: 0;
  font-size: 1.1rem;
  color: #333;
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
}

.chat-area {
  height: 300px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 10px;
  background: #f9f9f9;
  border-radius: 4px;
}

.message {
  margin-bottom: 10px;
  padding: 8px 12px;
  border-radius: 4px;
  max-width: 80%;
}

.message.user {
  background: #e3f2fd;
  margin-left: auto;
}

.message.assistant {
  background: #f5f5f5;
  margin-right: auto;
}

.input-area {
  display: flex;
  gap: 10px;
}
</style> 