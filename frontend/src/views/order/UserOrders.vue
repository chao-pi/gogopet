<template>
  <div class="orders-container">
    <div class="page-header">
      <h2>{{ userStore.userInfo?.userType === 'C' ? '公司订单' : '我的订单' }}</h2>
    </div>
    
    <el-table
      v-loading="loading"
      :data="orders"
      style="width: 100%"
      border
    >
      <el-table-column
        v-for="col in tableColumns"
        :key="col.prop || col.label"
        v-bind="col"
      >
        <template #default="scope">
          <template v-if="col.formatter">
            {{ col.formatter(scope.row) }}
          </template>
          <template v-else-if="col.slot === 'status'">
            <el-tag :type="getStatusType(scope.row.orderStatus)">
              {{ getStatusText(scope.row.orderStatus) }}
            </el-tag>
          </template>
          <template v-else-if="col.slot === 'operation'">
            <div class="operation-buttons">
              <el-button
                type="primary"
                size="small"
                @click="viewOrderDetails(scope.row.orderId)"
              >
                查看详情
              </el-button>
              <el-button
                type="success"
                size="small"
                @click="viewOrderTracking(scope.row.orderId)"
              >
                查看追踪
              </el-button>
            </div>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()

const orders = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const getStatusType = (status) => {
  const statusMap = {
    'P': 'info',    // 待支付
    'W': 'warning', // 待接单
    'T': 'warning', // 进行中
    'C': 'success', // 已完成
    'D': 'danger',  // 已取消
    'X': 'danger'   // 已取消
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'P': '待支付',
    'W': '待接单',
    'T': '进行中',
    'C': '已完成',
    'D': '已取消',
    'X': '已取消'
  }
  return statusMap[status] || status
}

const viewOrderDetails = (orderId) => {
  router.push(`/order/detail/${orderId}`)
}

const viewOrderTracking = (orderId) => {
  const userType = userStore.userInfo?.userType
  if (userType === 'C') {
    router.push(`/OrderTrackingE/${orderId}`)
  } else {
    router.push(`/ordertracking/${orderId}`)
  }
}

const fetchOrders = async () => {
  loading.value = true
  try {
    // 获取用户ID
    const userId = userStore.userInfo?.id
    console.log('当前用户信息:', userStore.userInfo)
    console.log('用户ID:', userId)
    console.log('用户类型:', userStore.userInfo?.userType)
    console.log('公司ID:', userStore.userInfo?.companyId)
    
    if (!userId) {
      ElMessage.error('用户未登录或ID无效')
      router.push('/login')
      return
    }

    // 根据用户类型选择不同的API
    const isCompany = userStore.userInfo?.userType === 'C'
    const companyId = userStore.userInfo?.companyId
    
    if (isCompany && !companyId) {
      console.error('公司用户没有公司ID')
      ElMessage.error('公司信息不完整，请重新登录')
      router.push('/login')
      return
    }

    const url = isCompany 
      ? `/order/company/${companyId}/list`
      : `/order/user/${userId}/list`
    
    console.log('请求URL:', url)
    console.log('请求参数:', {
      pageNum: currentPage.value - 1,
      pageSize: pageSize.value
    })

    const response = await request({
      url,
      method: 'get',
      params: {
        pageNum: currentPage.value - 1,
        pageSize: pageSize.value
      }
    })
    
    console.log('订单列表完整响应:', response)
    
    if (response && response.code === 200) {
      console.log('响应数据:', response.data)
      
      // 检查返回的数据格式
      if (response.data.records) {
        // MyBatis-Plus 分页格式
        orders.value = response.data.records
        total.value = response.data.total
        // 打印每个订单的公司名称
        orders.value.forEach(order => {
          console.log('订单ID:', order.orderId, '公司名称:', order.companyName)
        })
      } else if (response.data.content) {
        // Spring Data 分页格式
        orders.value = response.data.content
        total.value = response.data.totalElements
      } else if (Array.isArray(response.data)) {
        // 普通数组格式
        orders.value = response.data
        total.value = response.data.length
      } else {
        // 其他情况
        orders.value = []
        total.value = 0
        ElMessage.warning('数据格式不正确')
      }
      
      console.log('处理后的订单列表:', orders.value)
      console.log('处理后的总数:', total.value)
      
      if (!orders.value || orders.value.length === 0) {
        ElMessage.info('暂无订单数据')
      }
    } else {
      console.log('响应数据为空或格式不正确')
      orders.value = []
      total.value = 0
      ElMessage.warning('暂无订单数据')
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
    console.error('错误详情:', {
      status: error.response?.status,
      data: error.response?.data,
      message: error.message,
      config: error.config
    })
    const errorMsg = error.response?.data?.message || '获取订单列表失败'
    ElMessage.error(errorMsg)
    orders.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchOrders()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchOrders()
}

onMounted(() => {
  fetchOrders()
})

const tableColumns = computed(() => {
  const isCompany = userStore.userInfo?.userType === 'C'
  const baseColumns = [
    {
      prop: 'orderId',
      label: '订单编号',
      width: '180'
    },
    {
      prop: 'transportMethod',
      label: '运输方式',
      width: '120',
      formatter: (row) => {
        const methodMap = {
          'SPECIAL': '专车托运',
          'SHARE': '拼车托运',
          'AIR': '空运托运'
        }
        return methodMap[row.transportMethod] || row.transportMethod
      }
    },
    {
      prop: 'startLocation',
      label: '出发地'
    },
    {
      prop: 'endLocation',
      label: '目的地'
    },
    {
      prop: 'price',
      label: '价格',
      width: '120',
      formatter: (row) => `¥${row.price}`
    },
    {
      prop: 'orderStatus',
      label: '状态',
      width: '120',
      slot: 'status'
    },
    {
      prop: 'createTime',
      label: '创建时间',
      width: '180',
      formatter: (row) => new Date(row.createTime).toLocaleString()
    }
  ]

  // 根据用户类型添加不同的列
  if (isCompany) {
    baseColumns.splice(1, 0, {
      prop: 'userName',
      label: '用户名称',
      width: '120'
    })
  } else {
    baseColumns.splice(1, 0, {
      prop: 'companyName',
      label: '公司名称',
      width: '120'
    })
  }

  // 添加操作列
  baseColumns.push({
    label: '操作',
    width: '200',
    fixed: 'right',
    slot: 'operation',
    align: 'center'
  })

  return baseColumns
})
</script>

<style scoped>
.orders-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.operation-buttons {
  display: flex;
  gap: 8px;
  justify-content: center;
}
</style> 