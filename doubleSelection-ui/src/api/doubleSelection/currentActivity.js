import request from '@/utils/request'

// 查询活动列表
export function listActivity(query) {
  return request({
    url: '/activity',
    method: 'get',
    params: query
  })
}
// 新增活动
export function addActivity(data) {
  return request({
    url: '/activity',
    method: 'post',
    data: data
  })
}
//获取当前活动

export function getCurrentActivity(data) {
  return request({
    url: '/activity/current',
    method: 'get',
    data: data
  })
}

export function getResult({pageNum,pageSize}) {
    return request({
        url: '/selection/result',
        method: 'get',
        params: {
            pageNum,
            pageSize
        }
    })
}
// 获取推荐列表


export function getRecommend() {
    return request({
        url: '/introduction',
        method: 'get',
    })
}