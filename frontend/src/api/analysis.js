import request from './request'

// 获取宠物信息
export function getAnalysisData() {
    return request({
        url: '/analysis/data',
        method: 'get'
    })
}