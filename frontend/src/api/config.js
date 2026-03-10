import request from './request'

// 系统配置API
export function getConfigList() {
  return request({
    url: '/sys-config/list',
    method: 'get'
  })
}

export function getCustomConfigs() {
  return request({
    url: '/sys-config/custom',
    method: 'get'
  })
}

export function getConfigById(id) {
  return request({
    url: `/sys-config/${id}`,
    method: 'get'
  })
}

export function getConfigByKey(configKey) {
  return request({
    url: `/sys-config/key/${configKey}`,
    method: 'get'
  })
}

export function addConfig(data) {
  return request({
    url: '/sys-config',
    method: 'post',
    data
  })
}

export function updateConfig(data) {
  return request({
    url: '/sys-config',
    method: 'put',
    data
  })
}

export function deleteConfig(id) {
  return request({
    url: `/sys-config/${id}`,
    method: 'delete'
  })
}

export function getConfigStatistics() {
  return request({
    url: '/sys-config/statistics',
    method: 'get'
  })
}
