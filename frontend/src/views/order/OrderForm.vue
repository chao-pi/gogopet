<template>
  <div class="order-form-page">
    <div class="form-layout">
      <!-- 左侧：托运公司信息 -->
      <div class="company-sidebar">
        <div class="company-info" v-if="companyInfo">
          <h3>托运公司信息</h3>
          <div class="company-header">
            <div class="company-logo-container">
              <div class="company-logo" v-if="companyInfo.logoUrl">
                <img :src="companyInfo.logoUrl" />
              </div>
              <div class="company-logo default-logo" v-else>
                <Building2 :size="60" />
              </div>
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
      </div>

      <!-- 中间：订单表单 -->
      <div class="form-container">
        <h2 class="form-title">填写订单信息</h2>
        
        <el-form
          ref="orderFormRef"
          :model="orderForm"
          :rules="orderFormRules"
          label-width="120px"
          class="order-form"
        >
          <!-- 宠物信息 -->
          <el-form-item label="选择宠物" prop="petIds">
            <el-select
              v-model="orderForm.petIds"
              multiple
              collapse-tags
              collapse-tags-tooltip
              placeholder="请选择要托运的宠物"
              @change="handlePetsChange"
            >
              <el-option
                v-for="pet in pets"
                :key="pet.petId"
                :label="pet.petName"
                :value="pet.petId"
              >
                <div class="pet-option">
                  <div class="pet-avatar" v-if="pet.avatarUrl">
                    <img :src="pet.avatarUrl" />
                  </div>
                  <div class="pet-avatar default-avatar" v-else>
                    <PawPrint :size="20" />
                  </div>
                  <span>{{ pet.petName }} ({{ pet.petBreed }})</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>

          <!-- 运输方式 -->
          <el-form-item label="运输方式" prop="transportMethod">
            <div class="transport-types">
              <div 
                v-for="type in transportTypes" 
                :key="type.id"
                class="transport-type-option"
                :class="{ 'selected': orderForm.transportMethod === type.id }"
                @click="orderForm.transportMethod = type.id"
              >
                <div class="transport-icon-container">
                  <component :is="type.icon" class="transport-icon" :size="32" />
                </div>
                <h4 class="transport-name">{{ type.name }}</h4>
                <p class="transport-desc">{{ type.description }}</p>
              </div>
            </div>
          </el-form-item>

          <!-- 起始地点 -->
          <el-form-item label="起始地点" required>
            <el-cascader
              v-model="startArea"
              :options="areaOptions"
              :props="{ expandTrigger: 'hover' }"
              placeholder="请选择省市区"
              style="width: 100%"
              @change="handleStartAreaChange"
            />
            <el-form-item prop="startLocation" style="margin-top: 10px">
              <el-input v-model="orderForm.startLocation" placeholder="请输入详细地址" />
            </el-form-item>
          </el-form-item>

          <!-- 目的地点 -->
          <el-form-item label="目的地点" required>
            <el-cascader
              v-model="endArea"
              :options="areaOptions"
              :props="{ expandTrigger: 'hover' }"
              placeholder="请选择省市区"
              style="width: 100%"
              @change="handleEndAreaChange"
            />
            <el-form-item prop="endLocation" style="margin-top: 10px">
              <el-input v-model="orderForm.endLocation" placeholder="请输入详细地址" />
            </el-form-item>
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

      <!-- 右侧：宠物信息 -->
      <div class="pet-sidebar">
        <div class="pet-info" v-if="selectedPets.length > 0">
          <h3>宠物信息</h3>
          <div class="pets-list">
            <div v-for="pet in selectedPets" :key="pet.petId" class="pet-card">
              <div class="pet-header">
                <div class="pet-avatar-container">
                  <div class="pet-avatar" v-if="pet.avatarUrl">
                    <img :src="pet.avatarUrl" />
                  </div>
                  <div class="pet-avatar default-avatar" v-else>
                    <PawPrint :size="60" />
                  </div>
                  <h4 class="pet-name">{{ pet.petName }}</h4>
                </div>
              </div>
              <el-descriptions :column="1" border>
                <el-descriptions-item label="宠物品种">
                  {{ pet.petBreed }}
                </el-descriptions-item>
                <el-descriptions-item label="宠物年龄">
                  {{ pet.petAge }}岁
                </el-descriptions-item>
                <el-descriptions-item label="宠物性别">
                  {{ pet.petGender === 'MALE' ? '公' : '母' }}
                </el-descriptions-item>
                <el-descriptions-item label="宠物体重">
                  {{ pet.petWeight }}kg
                </el-descriptions-item>
              </el-descriptions>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Location, Money, MapLocation } from '@element-plus/icons-vue'
import { Building2, PawPrint, Car, Truck, Plane } from 'lucide-vue-next'
import { getPets } from '@/api/pet.js'
import { createOrder } from '@/api/order.js'
import { getCompanyCardById } from '@/api/company.js'
import { useUserStore } from '@/stores/user.js'
import { regionData } from 'element-china-area-data'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const orderFormRef = ref(null)
const pets = ref([])
const companyInfo = ref(null)

const orderForm = ref({
  petIds: [],
  transportMethod: 'SPECIAL',
  startProvince: '',
  startCity: '',
  startDistrict: '',
  startLocation: '',
  startLatitude: '',
  startLongitude: '',
  endProvince: '',
  endCity: '',
  endDistrict: '',
  endLocation: '',
  endLatitude: '',
  endLongitude: '',
  transportTime: '',
  receiver: '',
  phone: '',
  remark: ''
})

const orderFormRules = {
  petIds: [
    { required: true, message: '请选择要托运的宠物', trigger: 'change' },
    { type: 'array', min: 1, message: '至少选择一只宠物', trigger: 'change' }
  ],
  transportMethod: [
    { required: true, message: '请选择运输方式', trigger: 'change' }
  ],
  startProvince: [
    { required: true, message: '请选择起始省份', trigger: 'change' }
  ],
  startCity: [
    { required: true, message: '请选择起始城市', trigger: 'change' }
  ],
  startDistrict: [
    { required: true, message: '请选择起始区县', trigger: 'change' }
  ],
  startLocation: [
    { required: true, message: '请输入起始详细地址', trigger: 'blur' }
  ],
  endProvince: [
    { required: true, message: '请选择目的省份', trigger: 'change' }
  ],
  endCity: [
    { required: true, message: '请选择目的城市', trigger: 'change' }
  ],
  endDistrict: [
    { required: true, message: '请选择目的区县', trigger: 'change' }
  ],
  endLocation: [
    { required: true, message: '请输入目的详细地址', trigger: 'blur' }
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

// 地区数据
const areaOptions = regionData

// 选择的地区
const startArea = ref([])
const endArea = ref([])

// 处理起始地区变化
const handleStartAreaChange = (value) => {
  if (value && value.length === 3) {
    orderForm.value.startProvince = value[0]
    orderForm.value.startCity = value[1]
    orderForm.value.startDistrict = value[2]
  }
}

// 处理目的地区变化
const handleEndAreaChange = (value) => {
  if (value && value.length === 3) {
    orderForm.value.endProvince = value[0]
    orderForm.value.endCity = value[1]
    orderForm.value.endDistrict = value[2]
  }
}

// 获取经纬度
const getLocation = async (address) => {
  try {
    // 这里需要调用地图API获取经纬度
    // 暂时返回空值，后续实现
    return {
      latitude: '',
      longitude: ''
    }
  } catch (error) {
    console.error('获取经纬度失败:', error)
    return {
      latitude: '',
      longitude: ''
    }
  }
}

// 运输方式数据
const transportTypes = [
  { 
    id: 'SPECIAL', 
    name: '专车托运', 
    description: '一对一专车服务，全程专人照顾',
    icon: Car
  },
  { 
    id: 'SHARE', 
    name: '拼车托运', 
    description: '多宠物同行，经济实惠',
    icon: Truck
  },
  { 
    id: 'AIR', 
    name: '空运托运', 
    description: '长途首选，快速安全',
    icon: Plane
  }
]

// 选中的宠物信息
const selectedPets = ref([])

// 处理宠物选择变化
const handlePetsChange = (petIds) => {
  selectedPets.value = pets.value.filter(pet => petIds.includes(pet.petId))
}

// 提交订单
const submitOrder = async () => {
  if (!orderFormRef.value) return
  
  try {
    await orderFormRef.value.validate()
    
    // 获取起始地经纬度
    const startLocation = await getLocation(
      `${orderForm.value.startProvince}${orderForm.value.startCity}${orderForm.value.startDistrict}${orderForm.value.startLocation}`
    )
    
    // 获取目的地经纬度
    const endLocation = await getLocation(
      `${orderForm.value.endProvince}${orderForm.value.endCity}${orderForm.value.endDistrict}${orderForm.value.endLocation}`
    )
    
    const orderData = {
      ...orderForm.value,
      startLatitude: startLocation.latitude,
      startLongitude: startLocation.longitude,
      endLatitude: endLocation.latitude,
      endLongitude: endLocation.longitude,
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
  min-height: 100vh;
}

.form-layout {
  display: flex;
  gap: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.company-sidebar {
  width: 320px;
  flex-shrink: 0;
}

.form-container {
  flex: 1;
  min-width: 0;
  background: white;
  padding: 2.5rem;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.pet-sidebar {
  width: 320px;
  flex-shrink: 0;
}

.company-info,
.pet-info {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  position: sticky;
  top: 2rem;
}

.company-info:hover,
.pet-info:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.company-info h3,
.pet-info h3 {
  margin-bottom: 1.5rem;
  color: #303133;
  font-size: 1.4rem;
  font-weight: 600;
  position: relative;
  padding-bottom: 0.8rem;
}

.company-info h3::after,
.pet-info h3::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 40px;
  height: 3px;
  background: linear-gradient(90deg, #409EFF, #67C23A);
  border-radius: 3px;
}

.pet-header {
  margin-bottom: 1.5rem;
}

.pet-avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.2rem;
}

.pet-avatar {
  width: 120px;
  height: 120px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
}

.pet-avatar:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.pet-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.pet-avatar.default-avatar {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.pet-avatar.default-avatar svg {
  color: #909399;
}

.pet-name {
  margin: 0;
  font-size: 1.4rem;
  color: #303133;
  font-weight: 600;
  text-align: center;
}

.pet-option {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.pet-option:hover {
  background-color: #f5f7fa;
}

.pet-avatar.default-avatar svg {
  color: #909399;
}

.order-form {
  max-width: 600px;
  margin: 0 auto;
}

.company-header {
  margin-bottom: 2rem;
}

.company-logo-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.2rem;
  max-width: 400px;
  margin: 0 auto;
}

.company-logo {
  width: 100%;
  height: 180px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
}

.company-logo:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.company-logo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.company-logo.default-logo {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.company-logo.default-logo svg {
  color: #909399;
}

.company-name {
  margin: 0;
  font-size: 1.4rem;
  color: #303133;
  font-weight: 600;
  text-align: center;
}

:deep(.el-descriptions) {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

:deep(.el-descriptions__label) {
  width: 100px;
  color: #606266;
  font-weight: 500;
}

:deep(.el-descriptions__content) {
  color: #303133;
}

:deep(.el-icon) {
  margin-right: 8px;
  color: #409EFF;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
}

:deep(.el-input__inner),
:deep(.el-textarea__inner) {
  border-radius: 8px;
  transition: all 0.3s ease;
}

:deep(.el-input__inner:hover),
:deep(.el-textarea__inner:hover) {
  border-color: #409EFF;
}

:deep(.el-input__inner:focus),
:deep(.el-textarea__inner:focus) {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

:deep(.el-button) {
  border-radius: 8px;
  padding: 12px 24px;
  font-weight: 500;
  transition: all 0.3s ease;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  border: none;
}

:deep(.el-button--primary:hover) {
  background: linear-gradient(135deg, #66b1ff 0%, #409EFF 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

:deep(.el-button--default) {
  background: white;
  border: 1px solid #dcdfe6;
}

:deep(.el-button--default:hover) {
  color: #409EFF;
  border-color: #c6e2ff;
  background-color: #ecf5ff;
  transform: translateY(-2px);
}

:deep(.el-cascader) {
  width: 100%;
}

:deep(.el-cascader .el-input__inner) {
  border-radius: 8px;
}

:deep(.el-radio-group) {
  display: flex;
  gap: 20px;
}

:deep(.el-radio) {
  margin-right: 0;
}

:deep(.el-radio__input.is-checked .el-radio__inner) {
  background: #409EFF;
  border-color: #409EFF;
}

:deep(.el-radio__input.is-checked + .el-radio__label) {
  color: #409EFF;
}

.transport-types {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  width: 100%;
}

.transport-type-option {
  border: 2px solid #e4e7ed;
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  background: white;
}

.transport-type-option:hover {
  border-color: #409EFF;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.1);
}

.transport-type-option.selected {
  border-color: #409EFF;
  background: linear-gradient(135deg, #ecf5ff 0%, #f0f9ff 100%);
}

.transport-icon-container {
  width: 64px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #ecf5ff 0%, #f0f9ff 100%);
  border-radius: 12px;
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.transport-type-option.selected .transport-icon-container {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
}

.transport-type-option.selected .transport-icon {
  color: white;
}

.transport-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.transport-desc {
  font-size: 14px;
  color: #909399;
  line-height: 1.5;
}

:deep(.el-radio) {
  display: none;
}

.pets-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.pet-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.pet-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.pet-header {
  margin-bottom: 1rem;
}

.pet-avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.pet-avatar {
  width: 100px;
  height: 100px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
}

.pet-avatar:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.pet-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.pet-avatar.default-avatar {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.pet-avatar.default-avatar svg {
  color: #909399;
}

.pet-name {
  margin: 0;
  font-size: 1.2rem;
  color: #303133;
  font-weight: 600;
  text-align: center;
}

:deep(.el-select) {
  width: 100%;
}

:deep(.el-select__tags) {
  flex-wrap: nowrap;
  overflow: hidden;
}

:deep(.el-select__tags-text) {
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style> 