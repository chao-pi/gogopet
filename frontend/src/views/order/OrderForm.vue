<template>
  <div class="order-form-page">
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
import { getPets } from '@/api/pet.js'
import { createOrder } from '@/api/order.js'
import { useUserStore } from '@/stores/user.js'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const orderFormRef = ref(null)
const pets = ref([])

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
    const response = await getPets(userStore.userInfo.id)
    pets.value = response
  } catch (error) {
    console.error('获取宠物列表失败:', error)
    ElMessage.error('获取宠物列表失败')
  }
}

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7
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
</style> 