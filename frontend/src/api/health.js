import request from './request'

// 健康档案API
export function getHealthRecordList(params) {
  return request({ url: '/health-record/list', method: 'get', params })
}
export function getAllHealthRecords() {
  return request({ url: '/health-record/all', method: 'get' })
}
export function getHealthRecordById(id) {
  return request({ url: `/health-record/${id}`, method: 'get' })
}
export function getRecordsByElderId(elderId) {
  return request({ url: `/health-record/elder/${elderId}`, method: 'get' })
}
export function getActiveRecordByElderId(elderId) {
  return request({ url: `/health-record/elder/${elderId}/active`, method: 'get' })
}
export function addHealthRecord(data) {
  return request({ url: '/health-record', method: 'post', data })
}
export function updateHealthRecord(data) {
  return request({ url: '/health-record', method: 'put', data })
}
export function deleteHealthRecord(id) {
  return request({ url: `/health-record/${id}`, method: 'delete' })
}
export function archiveHealthRecord(id) {
  return request({ url: `/health-record/${id}/archive`, method: 'put' })
}

// 体检记录API
export function getPhysicalExamList(params) {
  return request({ url: '/physical-exam/list', method: 'get', params })
}
export function getAllPhysicalExams() {
  return request({ url: '/physical-exam/all', method: 'get' })
}
export function getPhysicalExamById(id) {
  return request({ url: `/physical-exam/${id}`, method: 'get' })
}
export function getExamsByElderId(elderId) {
  return request({ url: `/physical-exam/elder/${elderId}`, method: 'get' })
}
export function addPhysicalExam(data) {
  return request({ url: '/physical-exam', method: 'post', data })
}
export function updatePhysicalExam(data) {
  return request({ url: '/physical-exam', method: 'put', data })
}
export function deletePhysicalExam(id) {
  return request({ url: `/physical-exam/${id}`, method: 'delete' })
}
