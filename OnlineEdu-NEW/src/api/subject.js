import request from '@/utils/request'

const api_name = '/admin/edu/subject/'

export default {
  getNestedTreeList() {
    return request({
      url: `${api_name}`,
      method: 'get'
    })
  },
  removeById(id){
    return request({
        url:`${api_name}` + id,
        method:'delete'
    })
  },
  saveLevelOne(subject){
    return request({
      url:`${api_name}save-level-one`,
      method:'post',
      data:subject
    })
  },
  saveLevelTwo(subject) {
    return request({
      url: `${api_name}/save-level-two`,
      method: 'post',
      data: subject
    })
  }
}