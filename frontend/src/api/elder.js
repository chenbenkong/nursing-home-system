import request from './request'

export const getElderList = (params) => {
  return request.get('/elder/list', { params })
}

export const getAllElders = () => {
  return request.get('/elder/all')
}

export const getCheckedInElders = () => {
  return request.get('/elder/checked-in')
}

export const getElderById = (id) => {
  return request.get(`/elder/${id}`)
}

export const addElder = (data) => {
  return request.post('/elder', data)
}

export const updateElder = (data) => {
  return request.put('/elder', data)
}

export const deleteElder = (id) => {
  return request.delete(`/elder/${id}`)
}

export const checkIn = (id, data) => {
  return request.put(`/elder/${id}/checkin`, data)
}

export const checkOut = (id) => {
  return request.put(`/elder/${id}/checkout`)
}

export const getElderStatistics = () => {
  return request.get('/elder/statistics')
}
