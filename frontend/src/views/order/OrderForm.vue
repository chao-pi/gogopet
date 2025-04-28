<template>
  <div class="order-form-page">
    <div class="form-container">
      <h2 class="form-title">填写订单信息</h2>
      
      <!-- 托运公司信息 -->
      <div class="company-info" v-if="companyInfo">
        <h3>托运公司信息</h3>
        <div class="company-header">
          <div class="company-logo-container">
            <img :src="companyInfo.logoUrl || '/default-avatar.png'" class="company-logo" />
            <h4 class="company-name">{{ companyInfo.companyName }}</h4>
          </div>
        </div>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="公司地址">
            <el-icon><Location /></el-icon>
            {{ companyInfo.address }}
          </el-descriptions-item>
          <el-descriptions-item label="托运价格">
            <el-icon><Money /></el-icon>
            {{ companyInfo.transportPricePerKm }} 元/公里
          </el-descriptions-item>
          <el-descriptions-item label="服务区域">
            <el-icon><MapLocation /></el-icon>
            {{ getServiceAreaText(companyInfo.serviceArea) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <el-form
        ref="orderFormRef"
        :model="orderForm"
        :rules="orderFormRules"
        label-width="120px"
        class="order-form"
      >
        <!-- 宠物信息 -->
        <el-form-item label="选择宠物" prop="petId">
          <el-select v-model="orderForm.petId" placeholder="请选择要托运的宠物">
            <el-option
              v-for="pet in pets"
              :key="pet.petId"
              :label="pet.petName"
              :value="pet.petId"
            >
              <div class="pet-option">
                <img :src="pet.avatarUrl" class="pet-avatar" />
                <span>{{ pet.petName }} ({{ pet.petBreed }})</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>

        <!-- 运输方式 -->
        <el-form-item label="运输方式" prop="transportMethod">
          <el-radio-group v-model="orderForm.transportMethod">
            <el-radio label="SPECIAL">专车托运</el-radio>
            <el-radio label="SHARE">拼车托运</el-radio>
            <el-radio label="AIR">空运托运</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 起始地点 -->
        <el-form-item label="起始地点" prop="startLocation">
          <el-input v-model="orderForm.startLocation" placeholder="请输入起始地点" />
        </el-form-item>

        <!-- 目的地点 -->
        <el-form-item label="目的地点" prop="endLocation">
          <el-input v-model="orderForm.endLocation" placeholder="请输入目的地点" />
        </el-form-item>

        <!-- 运输时间 -->
        <el-form-item label="运输时间" prop="transportTime">
          <el-date-picker
            v-model="orderForm.transportTime"
            type="datetime"
            placeholder="请选择运输时间"
            :disabled-date="disabledDate"
          />
        </el-form-item>

        <!-- 联系人信息 -->
        <el-form-item label="联系人" prop="receiver">
          <el-input v-model="orderForm.receiver" placeholder="请输入联系人姓名" />
        </el-form-item>

        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="orderForm.phone" placeholder="请输入联系电话" />
        </el-form-item>

        <!-- 备注信息 -->
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="orderForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入特殊要求或备注信息"
          />
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" @click="submitOrder">提交订单</el-button>
          <el-button @click="cancelOrder">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Location, Money, MapLocation } from '@element-plus/icons-vue'
import { getPets } from '@/api/pet.js'
import { createOrder } from '@/api/order.js'
import { getCompanyCardById } from '@/api/company.js'
import { useUserStore } from '@/stores/user.js'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const orderFormRef = ref(null)
const pets = ref([])
const companyInfo = ref(null)

const orderForm = ref({
  petId: '',
  transportMethod: 'SPECIAL',
  startLocation: '',
  endLocation: '',
  transportTime: '',
  receiver: '',
  phone: '',
  remark: ''
})

const orderFormRules = {
  petId: [
    { required: true, message: '请选择要托运的宠物', trigger: 'change' }
  ],
  transportMethod: [
    { required: true, message: '请选择运输方式', trigger: 'change' }
  ],
  startLocation: [
    { required: true, message: '请输入起始地点', trigger: 'blur' }
  ],
  endLocation: [
    { required: true, message: '请输入目的地点', trigger: 'blur' }
  ],
  transportTime: [
    { required: true, message: '请选择运输时间', trigger: 'change' }
  ],
  receiver: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 获取宠物列表
const fetchPets = async () => {
  try {
    const userId = userStore.userInfo?.id
    console.log('User ID:', userId) // 添加调试信息
    if (!userId) {
      ElMessage.error('用户未登录')
      router.push('/login')
      return
    }
    const response = await getPets(userId)
    console.log('Pets:', response) // 添加调试信息
    pets.value = response
  } catch (error) {
    console.error('获取宠物列表失败:', error)
    ElMessage.error('获取宠物列表失败')
  }
}

// 获取公司信息
const fetchCompanyInfo = async () => {
  try {
    const companyId = route.query.companyId
    console.log('Company ID:', companyId)
    if (!companyId) {
      ElMessage.error('未选择托运公司')
      router.push('/transport')
      return
    }
    const response = await getCompanyCardById(companyId)
    console.log('Company Info:', response)
    console.log('Rating value:', response.rating, 'Type:', typeof response.rating)
    companyInfo.value = response
  } catch (error) {
    console.error('获取公司信息失败:', error)
    ElMessage.error('获取公司信息失败')
    router.push('/transport')
  }
}

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
}

// 获取服务区域文本
const getServiceAreaText = (area) => {
  switch (area) {
    case 'P':
      return '省内服务'
    case 'D':
      return '国内服务'
    case 'I':
      return '国际服务'
    default:
      return '未知'
  }
}

// 提交订单
const submitOrder = async () => {
  if (!orderFormRef.value) return
  
  try {
    await orderFormRef.value.validate()
    const orderData = {
      ...orderForm.value,
      userId: userStore.userInfo.id,
      companyId: route.query.companyId
    }
    
    await createOrder(orderData)
    ElMessage.success('订单创建成功')
    router.push('/orders')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('创建订单失败:', error)
      ElMessage.error('创建订单失败')
    }
  }
}

// 取消订单
const cancelOrder = () => {
  router.back()
}

onMounted(() => {
  fetchPets()
  fetchCompanyInfo()
})
</script>

<style scoped>
.order-form-page {
  padding: 2rem;
  max-width: 800px;
  margin: 0 auto;
}

.form-container {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.form-title {
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
}

.pet-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pet-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  object-fit: cover;
}

.order-form {
  max-width: 600px;
  margin: 0 auto;
}

.company-info {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background-color: #f5f7fa;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.company-info h3 {
  margin-bottom: 1.5rem;
  color: #333;
  font-size: 1.25rem;
  font-weight: 600;
}

.company-header {
  margin-bottom: 1.5rem;
}

.company-logo-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  max-width: 400px;
  margin: 0 auto;
}

.company-logo {
  width: 100%;
  height: 160px;
  border-radius: 12px;
  object-fit: cover;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.company-name {
  margin: 0;
  font-size: 1.2rem;
  color: #333;
  font-weight: 600;
}

:deep(.el-descriptions__label) {
  width: 100px;
  color: #666;
}

:deep(.el-descriptions__content) {
  color: #333;
}

:deep(.el-icon) {
  margin-right: 8px;
  color: #409EFF;
}
</style> 