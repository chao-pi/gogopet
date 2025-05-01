import request from '@/utils/request';

export function sendMessage(message, sessionId) {
    return request({
        url: '/chat/send',
        method: 'post',
        params: {
            message,
            sessionId
        }
    });
}

export function getHistory(sessionId) {
    return request({
        url: '/chat/history',
        method: 'get',
        params: {
            sessionId
        }
    });
} 