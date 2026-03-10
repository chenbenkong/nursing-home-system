import request from './request'

// 药品管理API

export function getMedicineList(params) {
  return request({
    url: '/medicine/list',
    method: 'get',
    params
  })
}

export function getAllMedicines() {
  return request({
    url: '/medicine/all',
    method: 'get'
  })
}

export function getMedicineById(id) {
  return request({
    url: `/medicine/${id}`,
    method: 'get'
  })
}

export function addMedicine(data) {
  return request({
    url: '/medicine',
    method: 'post',
    data
  })
}

export function updateMedicine(data) {
  return request({
    url: '/medicine',
    method: 'put',
    data
  })
}

export function deleteMedicine(id) {
  return request({
    url: `/medicine/${id}`,
    method: 'delete'
  })
}

export function getLowStockMedicines() {
  return request({
    url: '/medicine/low-stock',
    method: 'get'
  })
}

export function getExpiredMedicines() {
  return request({
    url: '/medicine/expired',
    method: 'get'
  })
}

export function getMedicineStatistics() {
  return request({
    url: '/medicine/statistics',
    method: 'get'
  })
}

// 老人用药记录API

export function getElderMedicationList(params) {
  return request({
    url: '/elder-medication/list',
    method: 'get',
    params
  })
}

export function getAllElderMedications() {
  return request({
    url: '/elder-medication/all',
    method: 'get'
  })
}

export function getElderMedicationById(id) {
  return request({
    url: `/elder-medication/${id}`,
    method: 'get'
  })
}

export function getMedicationsByElderId(elderId) {
  return request({
    url: `/elder-medication/elder/${elderId}`,
    method: 'get'
  })
}

export function getActiveMedicationsByElderId(elderId) {
  return request({
    url: `/elder-medication/elder/${elderId}/active`,
    method: 'get'
  })
}

export function addElderMedication(data) {
  return request({
    url: '/elder-medication',
    method: 'post',
    data
  })
}

export function updateElderMedication(data) {
  return request({
    url: '/elder-medication',
    method: 'put',
    data
  })
}

export function deleteElderMedication(id) {
  return request({
    url: `/elder-medication/${id}`,
    method: 'delete'
  })
}

export function completeElderMedication(id) {
  return request({
    url: `/elder-medication/${id}/complete`,
    method: 'put'
  })
}

export function pauseElderMedication(id) {
  return request({
    url: `/elder-medication/${id}/pause`,
    method: 'put'
  })
}

export function resumeElderMedication(id) {
  return request({
    url: `/elder-medication/${id}/resume`,
    method: 'put'
  })
}

export function getElderMedicationStatistics() {
  return request({
    url: '/elder-medication/statistics',
    method: 'get'
  })
}

// 用药执行记录API

export function getMedicationRecordList(params) {
  return request({
    url: '/medication-record/list',
    method: 'get',
    params
  })
}

export function getAllMedicationRecords() {
  return request({
    url: '/medication-record/all',
    method: 'get'
  })
}

export function getMedicationRecordById(id) {
  return request({
    url: `/medication-record/${id}`,
    method: 'get'
  })
}

export function getRecordsByElderId(elderId) {
  return request({
    url: `/medication-record/elder/${elderId}`,
    method: 'get'
  })
}

export function getRecordsByDate(date) {
  return request({
    url: `/medication-record/date/${date}`,
    method: 'get'
  })
}

export function getTodayRecords() {
  return request({
    url: '/medication-record/today',
    method: 'get'
  })
}

export function addMedicationRecord(data) {
  return request({
    url: '/medication-record',
    method: 'post',
    data
  })
}

export function updateMedicationRecord(data) {
  return request({
    url: '/medication-record',
    method: 'put',
    data
  })
}

export function deleteMedicationRecord(id) {
  return request({
    url: `/medication-record/${id}`,
    method: 'delete'
  })
}

export function completeMedicationRecord(id, data) {
  return request({
    url: `/medication-record/${id}/complete`,
    method: 'put',
    data
  })
}

export function skipMedicationRecord(id) {
  return request({
    url: `/medication-record/${id}/skip`,
    method: 'put'
  })
}

export function getMedicationRecordStatistics() {
  return request({
    url: '/medication-record/statistics',
    method: 'get'
  })
}
