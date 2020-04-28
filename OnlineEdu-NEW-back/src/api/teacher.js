import request from '@/utils/request'


export function getPageList(page, limit, searchObj) {
  return request({
    url: `/admin/edu/teacher/conditionList/${page}/${limit}`,
    method: 'post',
    // 传递条件对象，如果传递Json数据，使用data，如果不是json使用params 
    data: searchObj
  })
}

export function addTeahcer(teahcer) {
  return request({
    url: '/admin/edu/teacher/add',
    method: "post",
    data: teahcer
  })
}

export function getById(id) {
  return request({
    url: '/admin/edu/teacher/' + id,
    method: 'get'
  })
}

export function updateTeacher(id, teacher) {
  return request({
    url: '/admin/edu/teacher/' + id,
    method: 'put',
    data: teacher
  })
}

export function removeById(id) {
  return request({
    url: '/admin/edu/teacher/' + id,
    method: 'delete'
  })
}

export function getAllTeacher(){
  return request({
    url:'/admin/edu/teacher',
    method:'get'
  })
}