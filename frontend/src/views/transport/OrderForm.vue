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
              placeholder="请选择要托运的宠物（可选择多只）"
              @change="handlePetsChange"
              :disabled="isFormDisabled"
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

          <!-- 运输时间 -->
          <el-form-item label="运输时间" prop="transportTime">
            <el-date-picker
              v-model="orderForm.transportTime"
              type="datetime"
              placeholder="请选择运输时间"
              :disabled-date="disabledDate"
              :disabled="isFormDisabled"
            />
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
              :disabled="isFormDisabled"
            />
            <el-form-item prop="startLocation" style="margin-top: 10px">
              <el-input v-model="orderForm.startLocation" placeholder="请输入详细地址" :disabled="isFormDisabled" />
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
              :disabled="isFormDisabled"
            />
            <el-form-item prop="endLocation" style="margin-top: 10px">
              <el-input v-model="orderForm.endLocation" placeholder="请输入详细地址" :disabled="isFormDisabled" />
            </el-form-item>
          </el-form-item>

          <!-- 运输方式 -->
          <el-form-item label="运输方式" prop="transportMethod">
            <div class="transport-types">
              <div 
                v-for="type in transportTypes" 
                :key="type.id"
                class="transport-type-option"
                :class="{ 'selected': orderForm.transportMethod === type.id }"
                @click="!isFormDisabled && (orderForm.transportMethod = type.id)"
              >
                <div class="transport-icon-container">
                  <component :is="type.icon" class="transport-icon" :size="32" />
                </div>
                <h4 class="transport-name">{{ type.name }}</h4>
                <p class="transport-desc">{{ type.description }}</p>
              </div>
            </div>
          </el-form-item>

          <!-- 备注信息 -->
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="orderForm.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入特殊要求或备注信息"
              :disabled="isFormDisabled"
            />
          </el-form-item>

          <!-- 提交按钮 -->
          <el-form-item>
            <div class="form-buttons">
              <el-button class="cancel-btn" @click="cancelOrder" :disabled="isFormDisabled">
                <span class="btn-text">取消</span>
                <span class="btn-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M18 6 6 18"></path>
                    <path d="m6 6 12 12"></path>
                  </svg>
                </span>
              </el-button>
              <el-button 
                type="primary" 
                class="submit-btn" 
                @click="handleSubmit"
                :loading="isSubmitting"
                :disabled="isFormDisabled"
              >
                <span class="btn-text">{{ isSubmitting ? '创建订单中...' : '提交订单' }}</span>
                <span class="btn-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M5 12h14"></path>
                    <path d="m12 5 7 7-7 7"></path>
                  </svg>
                </span>
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>

      <!-- 右侧：宠物信息 -->
      <div class="pet-sidebar">
        <div class="pet-info" v-if="selectedPets.length > 0">
          <h3>宠物信息</h3>
          <div class="pets-list">
            <div class="pet-header">
              <div class="pet-avatar-container">
                <div class="pet-avatar" v-if="currentPet.avatarUrl">
                  <img :src="currentPet.avatarUrl" />
                </div>
                <div class="pet-avatar default-avatar" v-else>
                  <PawPrint :size="60" />
                </div>
                <h4 class="pet-name">{{ currentPet.petName }}</h4>
              </div>
            </div>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="宠物品种">
                {{ currentPet.petBreed }}
              </el-descriptions-item>
              <el-descriptions-item label="宠物年龄">
                {{ currentPet.petAge }}岁
              </el-descriptions-item>
              <el-descriptions-item label="宠物性别">
                {{ currentPet.petGender === 'MALE' ? '公' : '母' }}
              </el-descriptions-item>
              <el-descriptions-item label="宠物体重">
                {{ currentPet.petWeight }}kg
              </el-descriptions-item>
            </el-descriptions>
            <div class="pet-switch" v-if="selectedPets.length > 1">
              <el-button 
                type="primary" 
                circle 
                @click="switchPet('prev')"
                :icon="ChevronLeft"
              >
              </el-button>
              <span class="pet-counter">{{ currentPetIndex + 1 }}/{{ selectedPets.length }}</span>
              <el-button 
                type="primary" 
                circle 
                @click="switchPet('next')"
                :icon="ChevronRight"
              >
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 确认框 -->
    <el-dialog
      v-model="confirmDialogVisible"
      title="确认订单信息"
      width="45%"
      :close-on-click-modal="false"
      class="confirm-dialog"
      :show-close="false"
    >
      <div class="confirm-info">
        <div class="confirm-header">
          <div class="header-left">
            <h3>订单基本信息</h3>
            <div class="order-status">待支付</div>
          </div>
          <el-icon class="close-icon" @click="confirmDialogVisible = false"><Close /></el-icon>
        </div>
        <div class="info-section">
          <div class="info-item">
            <el-icon class="info-icon"><Van /></el-icon>
            <div class="info-content">
              <span class="info-label">运输方式</span>
              <span class="info-value">{{ orderData.transportMethod === 'SPECIAL' ? '专车托运' : 
                orderData.transportMethod === 'SHARE' ? '拼车托运' : '空运托运' }}</span>
            </div>
          </div>
          <div class="info-item">
            <el-icon class="info-icon"><Location /></el-icon>
            <div class="info-content">
              <span class="info-label">出发地</span>
              <span class="info-value">{{ getAreaName(orderData.startProvince) }}{{ getAreaName(orderData.startCity) }}{{ getAreaName(orderData.startDistrict) }}{{ orderData.startLocation }}</span>
            </div>
          </div>
          <div class="info-item">
            <el-icon class="info-icon"><Location /></el-icon>
            <div class="info-content">
              <span class="info-label">目的地</span>
              <span class="info-value">{{ getAreaName(orderData.endProvince) }}{{ getAreaName(orderData.endCity) }}{{ getAreaName(orderData.endDistrict) }}{{ orderData.endLocation }}</span>
            </div>
          </div>
          <div class="info-item">
            <el-icon class="info-icon"><PawPrint /></el-icon>
            <div class="info-content">
              <span class="info-label">宠物信息</span>
              <div class="pets-list">
                <div v-for="pet in selectedPets" :key="pet.petId" class="pet-item">
                  <span class="pet-name">{{ pet.petName }}</span>
                  <span class="pet-breed">({{ pet.petBreed }})</span>
                </div>
              </div>
            </div>
          </div>
          <div class="info-item">
            <el-icon class="info-icon"><ChatDotRound /></el-icon>
            <div class="info-content">
              <span class="info-label">备注</span>
              <span class="info-value">{{ orderData.orderRemark || '无' }}</span>
            </div>
          </div>
          <div class="info-item price-item">
            <el-icon class="info-icon"><Money /></el-icon>
            <div class="info-content">
              <span class="info-label">支付金额</span>
              <span class="price-value">¥{{ orderData.price.toFixed(2) }}</span>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button class="cancel-btn" @click="confirmDialogVisible = false">取消</el-button>
          <el-button type="primary" class="confirm-btn" @click="handleConfirm">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 支付框 -->
    <el-dialog
      v-model="paymentDialogVisible"
      title="支付订单"
      width="30%"
      :close-on-click-modal="false"
    >
      <div class="payment-info">
        <div class="order-info">
          <h3>订单信息</h3>
          <p><strong>运输方式：</strong>{{ orderData.transportMethod === 'SPECIAL' ? '专车托运' : 
            orderData.transportMethod === 'SHARE' ? '拼车托运' : '空运托运' }}</p>
          <p><strong>出发地：</strong>{{ getAreaName(orderData.startProvince) }}{{ getAreaName(orderData.startCity) }}{{ getAreaName(orderData.startDistrict) }}{{ orderData.startLocation }}</p>
          <p><strong>目的地：</strong>{{ getAreaName(orderData.endProvince) }}{{ getAreaName(orderData.endCity) }}{{ getAreaName(orderData.endDistrict) }}{{ orderData.endLocation }}</p>
          <p><strong>宠物信息：</strong></p>
          <ul>
            <li v-for="pet in selectedPets" :key="pet.petId">
              {{ pet.petName }} ({{ pet.petBreed }})
            </li>
          </ul>
        </div>
        <div class="qr-code">
          <img src="https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=gogopet:payment:{{ orderData.orderId }}" alt="支付二维码" style="width: 200px; height: 200px; object-fit: contain;" />
        </div>
        <div class="amount">
          <h3>支付金额：¥{{ orderData.price.toFixed(2) }}</h3>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handlePaymentCancel">取消</el-button>
          <el-button type="primary" @click="handlePaymentConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Location, Money, MapLocation, Van, Close, ChatDotRound } from '@element-plus/icons-vue'
import { Building2, PawPrint, Car, Truck, Plane, ChevronLeft, ChevronRight } from 'lucide-vue-next'
import { getPets } from '@/api/pet.js'
import { createOrder, getPaymentQRCode, updateOrderStatus } from '@/api/order.js'
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
    { required: true, message: '请选择运输方式', trigger: 'change' },
    { validator: (rule, value, callback) => {
      if (!value || !['SPECIAL', 'SHARE', 'AIR'].includes(value)) {
        callback(new Error('请选择有效的运输方式'))
      } else {
        callback()
      }
    }, trigger: 'change' }
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

// 获取地区名称
const getAreaName = (code) => {
  if (!code) return ''
  
  // 遍历所有省份
  for (const province of areaOptions) {
    // 如果找到匹配的省份
    if (province.value === code) {
      return province.label
    }
    
    // 遍历该省份下的所有城市
    if (province.children) {
      for (const city of province.children) {
        // 如果找到匹配的城市
        if (city.value === code) {
          return city.label
        }
        
        // 遍历该城市下的所有区县
        if (city.children) {
          for (const district of city.children) {
            // 如果找到匹配的区县
            if (district.value === code) {
              return district.label
            }
          }
        }
      }
    }
  }
  
  // 如果没有找到匹配的地区，返回原始编码
  return code
}

// 获取经纬度
const getLocation = async (address) => {
  try {
    // 这里使用高德地图API进行地址解析
    const apiKey = '1bafd5bd7575abdf3e63dca161765613'
    const formattedAddress = address.replace(/\s+/g, '') // 移除所有空格
    console.log('正在解析地址:', formattedAddress)
    
    const response = await fetch(`https://restapi.amap.com/v3/geocode/geo?address=${encodeURIComponent(formattedAddress)}&key=${apiKey}`)
    const data = await response.json()
    
    console.log('高德地图API返回数据:', data)
    
    if (data.status === '1' && data.geocodes && data.geocodes.length > 0) {
      const location = data.geocodes[0].location.split(',')
    return {
        latitude: location[1],
        longitude: location[0],
        formattedAddress: data.geocodes[0].formatted_address
      }
    } else {
      console.error('地址解析失败，错误信息:', data.info || '未知错误')
      throw new Error(`无法获取地址的经纬度: ${data.info || '未知错误'}`)
    }
  } catch (error) {
    console.error('获取经纬度失败:', error)
    throw error
  }
}

// 计算两点之间的距离（使用高德地图API）
const calculateDistance = async (startLocation, endLocation) => {
  try {
    // 这里使用高德地图API计算距离
    // 注意：需要替换成你的高德地图API Key
    const apiKey = '1bafd5bd7575abdf3e63dca161765613'
    const response = await fetch(
      `https://restapi.amap.com/v3/distance?origins=${startLocation.longitude},${startLocation.latitude}&destination=${endLocation.longitude},${endLocation.latitude}&key=${apiKey}`
    )
    const data = await response.json()
    
    if (data.status === '1' && data.results && data.results.length > 0) {
      // 返回距离（单位：公里）
      return data.results[0].distance / 1000
    } else {
      console.error('距离计算失败:', data)
      throw new Error('无法计算距离')
    }
  } catch (error) {
    console.error('计算距离失败:', error)
    throw error
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

// 当前显示的宠物索引
const currentPetIndex = ref(0)

// 当前显示的宠物
const currentPet = computed(() => {
  return selectedPets.value[currentPetIndex.value] || {}
})

// 切换宠物
const switchPet = (direction) => {
  if (direction === 'next') {
    currentPetIndex.value = (currentPetIndex.value + 1) % selectedPets.value.length
  } else {
    currentPetIndex.value = (currentPetIndex.value - 1 + selectedPets.value.length) % selectedPets.value.length
  }
}

// 处理宠物选择变化
const handlePetsChange = (petIds) => {
  selectedPets.value = pets.value.filter(pet => petIds.includes(pet.petId))
  currentPetIndex.value = 0 // 重置索引
}

// 添加新的响应式变量
const confirmDialogVisible = ref(false)
const paymentDialogVisible = ref(false)
const paymentQRCode = ref('')
const orderData = ref(null)
const isSubmitting = ref(false)
const isFormDisabled = ref(false)

// 修改提交订单方法
const handleSubmit = async () => {
  if (!orderFormRef.value) return
  
  try {
    isSubmitting.value = true
    isFormDisabled.value = true
    
    await orderFormRef.value.validate()
    
    // 构建完整的起始地址
    const startFullAddress = [
      getAreaName(orderForm.value.startProvince),
      getAreaName(orderForm.value.startCity),
      getAreaName(orderForm.value.startDistrict),
      orderForm.value.startLocation
    ].filter(Boolean).join('')
    
    // 构建完整的目的地址
    const endFullAddress = [
      getAreaName(orderForm.value.endProvince),
      getAreaName(orderForm.value.endCity),
      getAreaName(orderForm.value.endDistrict),
      orderForm.value.endLocation
    ].filter(Boolean).join('')
    
    // 获取起始地经纬度
    const startLocation = await getLocation(startFullAddress)
    
    // 获取目的地经纬度
    const endLocation = await getLocation(endFullAddress)
    
    // 计算实际距离
    const distance = await calculateDistance(startLocation, endLocation)
    
    // 计算基础价格
    const basePrice = distance * companyInfo.value.transportPricePerKm
    
    // 根据运输方式计算额外费用
    let additionalFee = 0
    switch (orderForm.value.transportMethod) {
      case 'SPECIAL':
        additionalFee = basePrice * 0.3 // 专车额外30%费用
        break
      case 'AIR':
        additionalFee = basePrice * 0.4 // 空运额外40%费用
        break
      case 'SHARE':
        additionalFee = basePrice * 0.1 // 拼车额外10%费用
        break
    }
    
    // 计算总价格
    const totalPrice = basePrice + additionalFee
    
    // 初始化订单数据
    orderData.value = {
      ...orderForm.value,
      userId: userStore.userInfo.id,
      companyId: route.query.companyId,
      transportMethod: orderForm.value.transportMethod || 'SPECIAL',
      startProvince: getAreaName(orderForm.value.startProvince),
      startCity: getAreaName(orderForm.value.startCity),
      startDistrict: getAreaName(orderForm.value.startDistrict),
      endProvince: getAreaName(orderForm.value.endProvince),
      endCity: getAreaName(orderForm.value.endCity),
      endDistrict: getAreaName(orderForm.value.endDistrict),
      startLatitude: startLocation.latitude,
      startLongitude: startLocation.longitude,
      endLatitude: endLocation.latitude,
      endLongitude: endLocation.longitude,
      price: totalPrice,
      distance: distance.toString(),
      orderRemark: orderForm.value.remark,
      petIds: selectedPets.value.map(pet => pet.petId)
    }
    
    // 创建订单
    const response = await createOrder(orderData.value)
    orderData.value = response.data
    
    // 获取支付二维码
    const qrResponse = await getPaymentQRCode(orderData.value.orderId)
    console.log('二维码响应:', qrResponse) // 添加调试信息
    if (qrResponse.data) {
      paymentQRCode.value = qrResponse.data
      console.log('二维码URL:', paymentQRCode.value) // 添加调试信息
    } else {
      console.error('未获取到二维码数据')
      ElMessage.error('获取支付二维码失败')
      return
    }
    
    // 显示支付框
    paymentDialogVisible.value = true
  } catch (error) {
    console.error('创建订单失败:', error)
    ElMessage.error('创建订单失败，请重试')
  } finally {
    isSubmitting.value = false
    isFormDisabled.value = false
  }
}

// 确认订单处理方法
const handleConfirm = async () => {
  try {
    // 关闭确认框
    confirmDialogVisible.value = false
    
    // 计算距离和价格
    const response = await createOrder({
      ...orderData.value,
      petIds: selectedPets.value.map(pet => pet.petId)
    })
    
    // 更新订单数据
    orderData.value = response.data
    
    // 获取支付二维码
    const qrResponse = await getPaymentQRCode(orderData.value.orderId)
    paymentQRCode.value = qrResponse.data
    
    // 显示支付框
    paymentDialogVisible.value = true
  } catch (error) {
    console.error('创建订单失败:', error)
    ElMessage.error('创建订单失败，请重试')
  }
}

// 支付取消处理方法
const handlePaymentCancel = async () => {
  try {
    // 关闭支付框
    paymentDialogVisible.value = false
    
    ElMessage.info('订单已创建，请及时支付')
    router.push('/order/list')
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 支付确认处理方法
const handlePaymentConfirm = async () => {
  try {
    // 关闭支付框
    paymentDialogVisible.value = false
    
    // 更新订单状态为待接单
    await updateOrderStatus(orderData.value.orderId, 'W')
    
    ElMessage.success('支付成功，等待接单')
    router.push('/order/list')
  } catch (error) {
    console.error('支付失败:', error)
    ElMessage.error('支付失败，请重试')
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
  padding: 1rem;
  min-height: 100vh;
  width: 100%;
  box-sizing: border-box;
}

.form-layout {
  display: flex;
  gap: 1rem;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  flex-wrap: wrap;
}

.company-sidebar,
.pet-sidebar {
  width: 320px;
  flex-shrink: 0;
}

.form-container {
  flex: 1;
  min-width: 0;
  background: white;
  padding: 1.5rem;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  min-width: 400px;
}

/* 响应式布局 */
@media screen and (max-width: 1200px) {
  .form-layout {
    flex-direction: column;
    align-items: center;
}

  .company-sidebar,
.pet-sidebar {
    width: 100%;
    max-width: 600px;
  }

  .form-container {
    width: 100%;
    max-width: 600px;
  }
}

@media screen and (max-width: 768px) {
  .order-form-page {
    padding: 0.5rem;
  }

  .form-layout {
    gap: 0.5rem;
  }

  .company-sidebar,
  .pet-sidebar,
  .form-container {
    padding: 1rem;
  }

  .transport-types {
    grid-template-columns: 1fr;
  }
}

/* 确保表单内容在小屏幕上也能正常显示 */
:deep(.el-form) {
  width: 100%;
}

:deep(.el-form-item) {
  margin-bottom: 1rem;
  width: 100%;
}

:deep(.el-input),
:deep(.el-select),
:deep(.el-cascader),
:deep(.el-date-picker) {
  width: 100%;
}

:deep(.el-textarea__inner) {
  width: 100%;
  box-sizing: border-box;
}

/* 确保卡片内容在小屏幕上也能正常显示 */
.company-info,
.pet-info {
  width: 100%;
  box-sizing: border-box;
}

.company-logo-container,
.pet-avatar-container {
  width: 100%;
  max-width: 100%;
}

.company-logo,
.pet-avatar {
  width: 100%;
  max-width: 100%;
  height: auto;
  aspect-ratio: 1;
}

.company-logo:hover,
.pet-avatar:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.company-logo img,
.pet-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.company-logo.default-logo,
.pet-avatar.default-avatar {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.company-logo.default-logo svg,
.pet-avatar.default-avatar svg {
  color: #909399;
}

.company-name,
.pet-name {
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

:deep(.el-form-item) {
  margin-bottom: 1rem;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
  padding-bottom: 0.5rem;
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
  gap: 0.5rem;
}

.pet-card {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  position: relative;
  min-height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.pet-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.pet-option {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  padding: 0 0.8rem;
  height: 100%;
  box-sizing: border-box;
}

.pet-option:hover {
  background-color: #f5f7fa;
}

.pet-option .pet-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  flex-shrink: 0;
}

.pet-option .pet-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.pet-option .pet-avatar.default-avatar {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.pet-option .pet-avatar.default-avatar svg {
  width: 16px;
  height: 16px;
}

.pet-option span {
  font-size: 0.9rem;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

:deep(.el-select-dropdown__item) {
  height: 48px !important;
  line-height: 48px !important;
  padding: 0 !important;
}

:deep(.el-select-dropdown__item.selected) {
  background-color: #f5f7fa;
  color: #409EFF;
}

:deep(.el-select-dropdown__item.hover) {
  background-color: #f5f7fa;
}

.btn-icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-text {
  font-size: 0.95rem;
}

.confirm-dialog {
  border-radius: 16px;
  overflow: hidden;
  max-height: 90vh;
}

:deep(.el-dialog) {
  margin: 0 !important;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  max-height: 90vh;
  width: 90% !important;
  max-width: 500px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

:deep(.el-dialog__header) {
  margin: 0 !important;
  padding: 20px 24px !important;
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  color: white;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-dialog__title) {
  color: white;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

:deep(.el-dialog__body) {
  padding: 0;
  background: #f8f9fa;
}

:deep(.el-dialog__footer) {
  padding: 16px 24px !important;
  border-top: 1px solid #ebeef5;
  background: white;
}

.confirm-info {
  padding: 24px;
}

.confirm-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.close-icon {
  font-size: 20px;
  cursor: pointer;
  color: white;
  transition: all 0.3s ease;
  opacity: 0.8;
}

.close-icon:hover {
  opacity: 1;
  transform: rotate(90deg);
}

.confirm-header h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
  font-weight: 600;
}

.order-status {
  padding: 4px 12px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border-radius: 16px;
  font-size: 13px;
  font-weight: 500;
  backdrop-filter: blur(4px);
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background: white;
  border-radius: 12px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.info-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.info-icon {
  font-size: 20px;
  color: #409EFF;
  margin-top: 2px;
  background: #ecf5ff;
  padding: 8px;
  border-radius: 8px;
}

.info-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-label {
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.info-value {
  color: #303133;
  font-size: 14px;
  line-height: 1.5;
}

.pets-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.pet-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.pet-item:hover {
  background: #f0f9ff;
}

.pet-name {
  font-weight: 500;
  color: #303133;
  font-size: 14px;
}

.pet-breed {
  color: #909399;
  font-size: 13px;
}

.price-item {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
  background: white !important;
}

.price-value {
  color: #f56c6c;
  font-size: 24px;
  font-weight: 600;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.cancel-btn {
  background: #f5f7fa;
  border: none;
  color: #606266;
  padding: 10px 20px;
  font-weight: 500;
  font-size: 14px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  background: #e4e7ed;
  color: #303133;
  transform: translateY(-2px);
}

.confirm-btn {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  border: none;
  padding: 10px 20px;
  font-weight: 500;
  font-size: 14px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.confirm-btn:hover {
  background: linear-gradient(135deg, #66b1ff 0%, #409EFF 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.payment-info {
  text-align: center;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: auto;
  width: 100%;
  box-sizing: border-box;
}

.order-info {
  text-align: left;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
  width: 100%;
  box-sizing: border-box;
}

.order-info h3 {
  margin-bottom: 15px;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.order-info p {
  margin: 8px 0;
  color: #606266;
  line-height: 1.5;
  font-size: 15px;
  word-break: break-all;
}

.order-info ul {
  margin: 8px 0;
  padding-left: 20px;
  color: #606266;
  font-size: 15px;
  word-break: break-all;
}

.qr-code {
  padding: 15px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: center;
  align-items: center;
  width: fit-content;
  margin: 0 auto;
  max-width: 100%;
}

.qr-code img {
  width: 200px;
  height: 200px;
  object-fit: contain;
  max-width: 100%;
}

.amount {
  margin-top: 10px;
  width: 100%;
  box-sizing: border-box;
}

.amount h3 {
  color: #f56c6c;
  font-size: 24px;
  font-weight: 600;
  word-break: break-all;
}

/* 响应式布局调整 */
@media screen and (max-width: 768px) {
  :deep(.el-dialog) {
    width: 95% !important;
  }

  .payment-info {
    padding: 15px;
    gap: 15px;
  }

  .order-info {
    padding: 12px;
  }

  .order-info h3 {
    font-size: 16px;
    margin-bottom: 12px;
  }

  .order-info p,
  .order-info ul {
    font-size: 14px;
  }

  .qr-code {
    padding: 12px;
  }

  .qr-code img {
    width: 180px;
    height: 180px;
  }

  .amount h3 {
    font-size: 20px;
  }
}

@media screen and (max-width: 480px) {
  :deep(.el-dialog) {
    width: 98% !important;
  }

  .payment-info {
    padding: 12px;
    gap: 12px;
  }

  .qr-code img {
    width: 160px;
    height: 160px;
  }
}

.form-buttons {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 1rem;
  margin-right: 0;
  padding-right: 0;
  width: 100%;
}

:deep(.submit-btn),
:deep(.cancel-btn) {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.8rem 1.2rem;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
  min-width: 100px;
  justify-content: center;
}

@media screen and (max-width: 768px) {
  .form-buttons {
    gap: 0.5rem;
  }
  
  :deep(.submit-btn),
  :deep(.cancel-btn) {
    padding: 0.6rem 1rem;
    min-width: 90px;
  }
}

.company-info,
.pet-info {
  background: white;
  padding: 1.5rem;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  position: sticky;
  top: 2rem;
  height: fit-content;
  display: flex;
  flex-direction: column;
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
  margin-bottom: 0.5rem;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.pet-avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.pet-avatar {
  width: 80px;
  height: 80px;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
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
  font-size: 1.1rem;
  color: #303133;
  font-weight: 600;
  text-align: center;
}

:deep(.el-descriptions) {
  width: 100%;
  background: white;
  padding: 0.8rem;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 0.5rem;
}

:deep(.el-descriptions__label) {
  width: 80px;
  color: #606266;
  font-weight: 500;
}

:deep(.el-descriptions__content) {
  color: #303133;
}

.pet-switch {
  margin-top: 0.3rem;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1.2rem;
  padding: 0.6rem 0;
  border-top: 1px solid #ebeef5;
  border-radius: 0 0 12px 12px;
}

.pet-counter {
  font-size: 0.8rem;
  color: #606266;
  font-weight: 500;
  min-width: 50px;
  text-align: center;
  background: white;
  padding: 0.2rem 0.8rem;
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

:deep(.el-button.is-circle) {
  width: 32px;
  height: 32px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

:deep(.el-button.is-circle:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

:deep(.el-button.is-circle svg) {
  width: 16px;
  height: 16px;
  color: #409EFF;
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
  width: 80%;
  height: 140px;
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
</style> 