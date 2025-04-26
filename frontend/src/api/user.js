import request from './request'

// 用户注册
export function register(data) {
    return request({
        url: '/auth/register',
        method: 'post',
        data
    })
}

// 用户登录
export function login(data) {
    return request({
        url: '/auth/login',
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

// 修改密码
export function changePassword(data) {
    return request({
        url: '/user/password',
        method: 'put',
        data
    })
}

// 上传头像
export function uploadAvatar(file, userId) {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('userId', userId)
    return request({
        url: '/picture/avatar',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

// 删除用户
export function deleteUser(userId) {
    return request({
        url: `/user/${userId}`,
        method: 'delete'
    })
} 