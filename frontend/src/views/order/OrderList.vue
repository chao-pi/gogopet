<template>
  <div class="order-list">
    <el-card>
      <div slot="header">
        <span>我的订单</span>
      </div>
      <el-table :data="orderList" style="width: 100%">
        <el-table-column prop="id" label="订单号" width="180"></el-table-column>
        <el-table-column prop="petName" label="宠物名称" width="120"></el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template slot-scope="scope">
            ¥{{ scope.row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleDetail(scope.row)">查看详情</el-button>
            <el-button 
              v-if="scope.row.status === 0" 
              size="mini" 
              type="primary" 
              @click="handlePay(scope.row)"
            >支付</el-button>
            <el-button 
              v-if="scope.row.status === 0" 
              size="mini" 
              type="danger" 
              @click="handleCancel(scope.row)"
            >取消</el-button>
            <el-button 
              v-if="scope.row.status === 2" 
              size="mini" 
              type="success" 
              @click="handleConfirm(scope.row)"
            >确认收货</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getOrders, cancelOrder, payOrder, confirmOrder } from '@/api/order'

export default {
  name: 'OrderList',
  data() {
    return {
      orderList: [],
      page: 1,
      size: 10,
      total: 0
    }
  },
  created() {
    this.fetchOrders()
  },
  methods: {
    async fetchOrders() {
      try {
        const res = await getOrders({
          page: this.page,
          size: this.size
        })
        this.orderList = res.data.records
        this.total = res.data.total
      } catch (error) {
        this.$message.error('获取订单列表失败')
      }
    },
    getStatusType(status) {
      const types = {
        0: 'warning',
        1: 'success',
        2: 'info',
        3: 'danger',
        4: 'success'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        0: '待支付',
        1: '已支付',
        2: '已发货',
        3: '已取消',
        4: '已完成'
      }
      return texts[status] || '未知状态'
    },
    handleDetail(row) {
      this.$router.push(`/order/detail/${row.id}`)
    },
    async handlePay(row) {
      try {
        await this.$confirm('确认支付该订单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await payOrder(row.id)
        this.$message.success('支付成功')
        this.fetchOrders()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('支付失败')
        }
      }
    },
    async handleCancel(row) {
      try {
        await this.$confirm('确认取消该订单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await cancelOrder(row.id)
        this.$message.success('取消成功')
        this.fetchOrders()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('取消失败')
        }
      }
    },
    async handleConfirm(row) {
      try {
        await this.$confirm('确认已收到货物吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await confirmOrder(row.id)
        this.$message.success('确认成功')
        this.fetchOrders()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('确认失败')
        }
      }
    },
    handleSizeChange(val) {
      this.size = val
      this.fetchOrders()
    },
    handleCurrentChange(val) {
      this.page = val
      this.fetchOrders()
    }
  }
}
</script>

<style scoped>
.order-list {
  padding: 20px;
}
.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style> 