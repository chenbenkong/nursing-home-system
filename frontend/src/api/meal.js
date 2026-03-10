import request from './request'

// 膳食菜单API
export function getMealMenuList(params) {
  return request({ url: '/meal-menu/list', method: 'get', params })
}
export function getAllMealMenus() {
  return request({ url: '/meal-menu/all', method: 'get' })
}
export function getMealMenuById(id) {
  return request({ url: `/meal-menu/${id}`, method: 'get' })
}
export function addMealMenu(data) {
  return request({ url: '/meal-menu', method: 'post', data })
}
export function updateMealMenu(data) {
  return request({ url: '/meal-menu', method: 'put', data })
}
export function deleteMealMenu(id) {
  return request({ url: `/meal-menu/${id}`, method: 'delete' })
}

// 老人膳食API
export function getElderMealList(params) {
  return request({ url: '/elder-meal/list', method: 'get', params })
}
export function getAllElderMeals() {
  return request({ url: '/elder-meal/all', method: 'get' })
}
export function getElderMealById(id) {
  return request({ url: `/elder-meal/${id}`, method: 'get' })
}
export function getMealsByElderId(elderId) {
  return request({ url: `/elder-meal/elder/${elderId}`, method: 'get' })
}
export function addElderMeal(data) {
  return request({ url: '/elder-meal', method: 'post', data })
}
export function updateElderMeal(data) {
  return request({ url: '/elder-meal', method: 'put', data })
}
export function deleteElderMeal(id) {
  return request({ url: `/elder-meal/${id}`, method: 'delete' })
}
export function completeElderMeal(id) {
  return request({ url: `/elder-meal/${id}/complete`, method: 'put' })
}
