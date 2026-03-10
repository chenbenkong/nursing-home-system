import request from './request'

// 紧急事件API
export function getEmergencyEventList(params) {
  return request({ url: '/emergency/list', method: 'get', params })
}
export function getAllEmergencyEvents() {
  return request({ url: '/emergency/all', method: 'get' })
}
export function getEmergencyEventById(id) {
  return request({ url: `/emergency/${id}`, method: 'get' })
}
export function addEmergencyEvent(data) {
  return request({ url: '/emergency', method: 'post', data })
}
export function updateEmergencyEvent(data) {
  return request({ url: '/emergency', method: 'put', data })
}
export function deleteEmergencyEvent(id) {
  return request({ url: `/emergency/${id}`, method: 'delete' })
}
export function startProcess(id, data) {
  return request({ url: `/emergency/${id}/process`, method: 'put', data })
}
export function resolveEmergencyEvent(id, data) {
  return request({ url: `/emergency/${id}/resolve`, method: 'put', data })
}
export function getEmergencyStatistics() {
  return request({ url: '/emergency/statistics', method: 'get' })
}
