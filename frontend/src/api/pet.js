import request from './request'

// 添加宠物
export function addPet(data) {
    return request({
        url: '/pet/add',
        method: 'post',
        data
    })
}

// 更新宠物信息
export function updatePet(data) {
    return request({
        url: '/pet/update',
        method: 'put',
        data
    })
}

// 删除宠物
export function deletePet(petId) {
    return request({
        url: `/pet/delete/${petId}`,
        method: 'delete'
    })
}

// 获取宠物信息
export function getPetById(petId) {
    return request({
        url: `/pet/${petId}`,
        method: 'get'
    })
}

// 获取用户的所有宠物
export function getPets() {
    return request({
        url: '/pet/list',
        method: 'get'
    })
}

// 上传宠物照片
export function uploadPetPhoto(file, petId) {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('petId', petId)
    return request({
        url: '/pet/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
} 