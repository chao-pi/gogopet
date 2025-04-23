import request from './request'

// 用户注册
export function register(data) {
    return request({
        url: '/user/register',
        method: 'post',
        data
    })
}

// 用户登录
export function login(data) {
    return request({
        url: '/user/login',
        method: 'post',
        data
    })
}

// 获取用户信息
export function getUserInfo(userId) {
    return request({
        url: `/user/${userId}`,
        method: 'get'
    })
}

// 更新用户信息
export function updateUserInfo(data) {
    return request({
        url: '/user',
        method: 'put',
        data
    })
}

// 删除用户
export function deleteUser(userId) {
    return request({
        url: `/user/${userId}`,
        method: 'delete'
    })
} 