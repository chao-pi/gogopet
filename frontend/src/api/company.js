import request from './request'

// 获取所有托运公司卡片信息
export function getAllCompanyCards() {
    return request({
        url: '/company/cards',
        method: 'get'
    })
}

// 根据公司ID获取托运公司卡片信息
export function getCompanyCardById(companyId) {
    return request({
        url: `/company/card/${companyId}`,
        method: 'get'
    })
} 