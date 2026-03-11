import request from './request'

// 获取费用记录列表
export function getFeeList(params) {
  return request({
    url: '/fee/list',
    method: 'get',
    params
  })
}

// 根据ID获取费用记录
export function getFeeById(id) {
  return request({
    url: `/fee/${id}`,
    method: 'get'
  })
}

// 根据老人ID获取费用记录
export function getFeesByElderId(elderId) {
  return request({
    url: `/fee/elder/${elderId}`,
    method: 'get'
  })
}

// 新增费用记录
export function addFee(data) {
  return request({
    url: '/fee',
    method: 'post',
    data
  })
}

// 更新费用记录
export function updateFee(data) {
  return request({
    url: '/fee',
    method: 'put',
    data
  })
}

// 删除费用记录
export function deleteFee(id) {
  return request({
    url: `/fee/${id}`,
    method: 'delete'
  })
}

// 缴费
export function payFee(id, payAmount, payMethod) {
  return request({
    url: `/fee/${id}/pay`,
    method: 'post',
    params: { payAmount, payMethod }
  })
}

// 获取费用统计
export function getFeeStatistics(feeMonth) {
  return request({
    url: '/fee/statistics',
    method: 'get',
    params: { feeMonth }
  })
}

// 获取本月费用统计
export function getCurrentMonthStatistics() {
  return request({
    url: '/fee/statistics/current-month',
    method: 'get'
  })
}
