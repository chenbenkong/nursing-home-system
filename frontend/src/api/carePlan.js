import request from './request'

export const getCarePlanList = (params) => {
  return request.get('/care-plan/list', { params })
}

export const getAllCarePlans = () => {
  return request.get('/care-plan/all')
}

export const getCarePlanById = (id) => {
  return request.get(`/care-plan/${id}`)
}

export const getCarePlansByElderId = (elderId) => {
  return request.get(`/care-plan/elder/${elderId}`)
}

export const createCarePlan = (data) => {
  return request.post('/care-plan', data)
}

export const updateCarePlan = (data) => {
  return request.put('/care-plan', data)
}

export const deleteCarePlan = (id) => {
  return request.delete(`/care-plan/${id}`)
}

export const pauseCarePlan = (id) => {
  return request.put(`/care-plan/${id}/pause`)
}

export const resumeCarePlan = (id) => {
  return request.put(`/care-plan/${id}/resume`)
}

export const completeCarePlan = (id) => {
  return request.put(`/care-plan/${id}/complete`)
}
