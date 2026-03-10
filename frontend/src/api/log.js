import request from './request'

// 操作日志API
export function getOperationLogList(params) {
  return request({
    url: '/operation-log/list',
    method: 'get',
    params
  })
}

export function getOperationLogById(id) {
  return request({
    url: `/operation-log/${id}`,
    method: 'get'
  })
}

export function deleteOperationLog(id) {
  return request({
    url: `/operation-log/${id}`,
    method: 'delete'
  })
}

export function cleanOperationLog(days) {
  return request({
    url: `/operation-log/clean/${days}`,
    method: 'delete'
  })
}

export function getOperationLogStatistics() {
  return request({
    url: '/operation-log/statistics',
    method: 'get'
  })
}

// 获取失败日志类型统计（饼状图数据）
export function getFailStatistics(startTime, endTime) {
  return request({
    url: '/operation-log/fail-statistics',
    method: 'get',
    params: { startTime, endTime }
  })
}
