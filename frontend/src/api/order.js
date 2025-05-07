import request from '@/utils/request'

// 创建订单
export function createOrder(data) {
  return request({
    url: '/order/create',
    method: 'post',
    data
  })
}

// 获取订单列表
export function getOrders(params) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

// 获取订单详情
export function getOrderDetail(orderId) {
  console.log('准备获取订单详情，订单ID:', orderId)
  return request({
    url: `/order/detail/${orderId}`,
    method: 'get'
  }).then(response => {
    console.log('获取订单详情响应:', response)
    return response
  }).catch(error => {
    console.error('获取订单详情失败:', error)
    throw error
  })
}

// 取消订单
export function cancelOrder(orderId) {
  return request({
    url: `/order/cancel/${orderId}`,
    method: 'post'
  })
}

// 支付订单
export function payOrder(orderId) {
  return request({
    url: `/order/pay/${orderId}`,
    method: 'post'
  })
}

// 确认收货
export function confirmOrder(orderId) {
  return request({
    url: `/order/confirm/${orderId}`,
    method: 'post'
  })
}

// 获取支付二维码
export function getPaymentQRCode(orderId) {
  return request({
    url: '/order/payment/qrcode',
    method: 'post',
    params: { orderId }
  })
}

// 更新订单状态
export function updateOrderStatus(orderId, data) {
  return request({
    url: '/order/status/update',
    method: 'post',
    data: {
      orderId,
      ...data
    }
  })
}

// 获取企业订单列表
export function getCompanyOrders(companyId) {
  return request({
    url: `/order/company/${companyId}/list`,
    method: 'get'
  })
} 