import request from '@/utils/request'

// 创建评论
export function createComment(data) {
  return request({
    url: '/comments',
    method: 'post',
    data
  })
}

// 获取帖子的评论列表
export function getCommentList(postId, params) {
  return request({
    url: `/comments/post/${postId}`,
    method: 'get',
    params
  })
}

// 获取评论的回复列表
export function getReplyList(commentId, params) {
  return request({
    url: `/comments/reply/${commentId}`,
    method: 'get',
    params
  })
}

// 删除评论
export function deleteComment(commentId) {
  return request({
    url: `/comments/${commentId}`,
    method: 'delete'
  })
} 