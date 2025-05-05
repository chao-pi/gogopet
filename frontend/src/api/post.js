import request from '@/utils/request'

// 创建帖子
export function createPost(data) {
  return request({
    url: '/posts',
    method: 'post',
    data
  })
}

// 获取帖子列表
export function getPostList(params) {
  return request({
    url: '/posts',
    method: 'get',
    params
  })
}

// 获取帖子详情
export function getPostDetail(postId) {
  return request({
    url: `/posts/${postId}`,
    method: 'get'
  })
}

// 删除帖子
export function deletePost(postId) {
  return request({
    url: `/posts/${postId}`,
    method: 'delete'
  })
}

// 更新帖子
export function updatePost(postId, data) {
  return request({
    url: `/posts/${postId}`,
    method: 'put',
    data
  })
}

// 点赞帖子
export function likePost(postId, userId) {
  return request({
    url: `/posts/likes/${postId}`,
    method: 'post',
    params: { userId }
  })
}

// 取消点赞
export function unlikePost(postId, userId) {
  return request({
    url: `/posts/likes/${postId}`,
    method: 'delete',
    params: { userId }
  })
}

// 检查用户是否已点赞
export function checkUserLiked(postId, userId) {
  return request({
    url: `/posts/likes/check/${postId}`,
    method: 'get',
    params: { userId }
  })
}

// 获取最受欢迎的帖子
export function getMostPopularPosts(limit) {
  return request({
    url: '/posts/popular',
    method: 'get',
    params: { limit }
  }).then(response => {
    return response;
  }).catch(error => {
    console.error('获取最受欢迎帖子失败:', error);
    throw error;
  });
} 