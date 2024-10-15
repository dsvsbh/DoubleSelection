import request from '@/utils/request'

// 查询学生列表
export function listStudent(query) {
  return request({
    url: '/doubleSelection/student/list',
    method: 'get',
    params: query
  })
}

// 查询学生详细
export function getStudent(studentId) {
  return request({
    url: '/doubleSelection/student/' + studentId,
    method: 'get'
  })
}

// 新增学生
export function addStudent(data) {
  return request({
    url: '/doubleSelection/student',
    method: 'post',
    data: data
  })
}

// 修改学生
export function updateStudent(data) {
  return request({
    url: '/doubleSelection/student',
    method: 'put',
    data: data
  })
}

// 删除学生
export function delStudent(studentId) {
  return request({
    url: '/doubleSelection/student/' + studentId,
    method: 'delete'
  })
}
//查询选择了导师的学生
export function selectedStudent(pageNum, pageSize) {
    return request({
        url: '/selection/' + pageNum + '/' + pageSize,
        method:'get'
    })
}

//同意申请
export function acceptStudent(studentId) {
    return request({
        url: '/selection/doubleSelection/' + studentId,
        method:'put'
    })
}
//拒绝申请
export function rejectStudent(studentId) {
    return request({
        url: '/selection/reject/' + studentId,
        method:'put'
    })
}