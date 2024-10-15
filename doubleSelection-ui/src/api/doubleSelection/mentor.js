import request from '@/utils/request'

// 查询导师列表
export function listMentor(query) {
  return request({
    url: '/doubleSelection/mentor/list',
    method: 'get',
    params: query
  })
}

// 查询导师详细
export function getMentor(mentorId) {
  return request({
    url: '/doubleSelection/mentor/' + mentorId,
    method: 'get'
  })
}

// 新增导师
export function addMentor(data) {
  return request({
    url: '/doubleSelection/mentor',
    method: 'post',
    data: data
  })
}

// 修改导师
export function updateMentor(data) {
  return request({
    url: '/doubleSelection/mentor',
    method: 'put',
    data: data
  })
}

// 删除导师
export function delMentor(mentorId) {
  return request({
    url: '/doubleSelection/mentor/' + mentorId,
    method: 'delete'
  })
}
//选择导师
export function selectionMentor(mentorId) {
  return request({
    url: '/selection/' + mentorId,
    method: 'post'
  })
}
//取消选中
export function cancelSelectionMentor(mentorId) {
    return request({
      url: '/selection/' + mentorId,
      method: 'delete'
    })
  }
  