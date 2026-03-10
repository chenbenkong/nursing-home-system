import request from './request'

// 活动管理API
export function getActivityList(params) {
  return request({ url: '/activity/list', method: 'get', params })
}
export function getAllActivities() {
  return request({ url: '/activity/all', method: 'get' })
}
export function getActivityById(id) {
  return request({ url: `/activity/${id}`, method: 'get' })
}
export function createActivity(data) {
  return request({ url: '/activity', method: 'post', data })
}
export function updateActivity(data) {
  return request({ url: '/activity', method: 'put', data })
}
export function deleteActivity(id) {
  return request({ url: `/activity/${id}`, method: 'delete' })
}
export function startActivity(id) {
  return request({ url: `/activity/${id}/start`, method: 'put' })
}
export function completeActivity(id) {
  return request({ url: `/activity/${id}/complete`, method: 'put' })
}
export function getActivityParticipants(activityId) {
  return request({ url: `/activity/${activityId}/participants`, method: 'get' })
}
export function registerActivity(activityId, elderId, elderName) {
  return request({ 
    url: `/activity/${activityId}/register`, 
    method: 'post', 
    params: { elderId, elderName }
  })
}
export function checkIn(activityId, elderId) {
  return request({ 
    url: `/activity/${activityId}/check-in`, 
    method: 'put', 
    params: { elderId }
  })
}
