import request from '@/utils/request'

// 获取路由
export const sendMessage = (data) => {
  return request({
    url:'/message/sendMessage',
    method:'post',
    data:data
  })
}

export const messageList = (query) => {
    return request({
        url: "/message/list",
        method: 'get',
        params:query
    })
}

export const messageDetail = (id) => {
    return request({
        url: `/message/${id}`,
        method:'post'
    })
}


export const deleteMessage = () => {
    return request({
        url: '/message',
        method:'delete'
    })
}

export const editIntroduce = (query) => {
    return request({
        url: '/system/user/detail',
        method: 'put',
        params:query
    })
}


export const findIntroduce = (query) => {
    return request({
        url: '/system/user/detail',
        method: 'get',
        params:query
    })
}