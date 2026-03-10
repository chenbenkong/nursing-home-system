import request from './request'

export const getApplicationList = (params) => {
  return request.get('/application/list', { params })
}

export const getAllApplications = () => {
  return request.get('/application/all')
}

export const getApplicationById = (id) => {
  return request.get(`/application/${id}`)
}

export const submitApplication = (data) => {
  return request.post('/application', data)
}

export const updateApplication = (data) => {
  return request.put('/application', data)
}

export const deleteApplication = (id) => {
  return request.delete(`/application/${id}`)
}

export const approveApplication = (id, data) => {
  return request.put(`/application/${id}/approve`, data)
}

export const rejectApplication = (id, data) => {
  return request.put(`/application/${id}/reject`, data)
}

export const getPendingCount = () => {
  return request.get('/application/pending/count')
}
