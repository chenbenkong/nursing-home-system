import request from './request'

// 库存管理API
export function getInventoryList(params) {
  return request({ url: '/inventory/list', method: 'get', params })
}
export function getAllInventories() {
  return request({ url: '/inventory/all', method: 'get' })
}
export function getInventoryById(id) {
  return request({ url: `/inventory/${id}`, method: 'get' })
}
export function addInventory(data) {
  return request({ url: '/inventory', method: 'post', data })
}
export function updateInventory(data) {
  return request({ url: '/inventory', method: 'put', data })
}
export function deleteInventory(id) {
  return request({ url: `/inventory/${id}`, method: 'delete' })
}
export function stockIn(data) {
  return request({ url: '/inventory/in', method: 'post', data })
}
export function stockOut(data) {
  return request({ url: '/inventory/out', method: 'post', data })
}
export function getLowStockInventories() {
  return request({ url: '/inventory/low-stock', method: 'get' })
}
export function getInventoryRecords(params) {
  return request({ url: '/inventory/records', method: 'get', params })
}
