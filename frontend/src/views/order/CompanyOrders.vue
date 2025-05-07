<template>
  <div class="company-orders">
    <div class="page-header">
      <h2>订单管理</h2>
    </div>

    <!-- 搜索和筛选区域 -->
    <div class="search-section">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable>
            <el-option label="待支付" value="P" />
            <el-option label="待接单" value="W" />
            <el-option label="运输中" value="T" />
            <el-option label="已完成" value="C" />
            <el-option label="已取消" value="X" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 订单列表 -->
    <div class="orders-table">
      <el-table
        v-loading="loading"
        :data="orders"
        style="width: 100%"
        border
      >
        <el-table-column prop="orderId" label="订单编号" width="180" />
        <el-table-column prop="userName" label="客户名称" width="120" />
        <el-table-column prop="petName" label="宠物名称" width="120" />
        <el-table-column prop="petBreed" label="宠物品种" width="120" />
        <el-table-column prop="startLocation" label="出发地" min-width="200" />
        <el-table-column prop="endLocation" label="目的地" min-width="200" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.orderStatus)">
              {{ getStatusText(scope.row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button
              size="small"
              type="primary"
              @click="viewOrderDetail(scope.row.orderId)"
            >
              查看详情
            </el-button>
            <el-button
              v-if="scope.row.orderStatus === 'W'"
              size="small"
              type="success"
              @click="startTransport(scope.row.orderId)"
            >
              开始运输
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getCompanyOrders, updateOrderStatus } from '@/api/order'

const router = useRouter()
const userStore = useUserStore()

// 数据列表
const orders = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 搜索表单
const searchForm = ref({
  status: ''
})

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const response = await getCompanyOrders({
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      status: searchForm.value.status
    })
    if (response.data) {
      orders.value = response.data.records
      total.value = response.data.total
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchOrders()
}

// 重置搜索
const resetSearch = () => {
  searchForm.value.status = ''
  handleSearch()
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchOrders()
}

// 页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchOrders()
}

// 查看订单详情
const viewOrderDetail = (orderId) => {
  router.push(`/OrderTrackingE/${orderId}`)
}

// 开始运输
const startTransport = async (orderId) => {
  try {
    const response = await updateOrderStatus(orderId, '1')
    if (response.data) {
      ElMessage.success('订单状态更新成功')
      fetchOrders()
    }
  } catch (error) {
    console.error('更新订单状态失败:', error)
    ElMessage.error('更新订单状态失败')
  }
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
    '2': '运输中-途中1',
    '3': '运输中-途中2',
    '4': '运输中-途中3',
    '5': '运输中-终点',
    'C': '已完成',
    'X': '已取消',
    'R': '休息中',
    'L': '装卸中'
  }
  return texts[status] || '未知状态'
}

// 组件挂载时获取数据
onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.company-orders {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.search-section {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.orders-table {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 