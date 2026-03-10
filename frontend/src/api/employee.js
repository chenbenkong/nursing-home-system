import request from './request'

export const getEmployeeList = (params) => {
  return request.get('/employee/list', { params })
}

export const getAllEmployees = () => {
  return request.get('/employee/all')
}

export const getEmployeeById = (id) => {
  return request.get(`/employee/${id}`)
}

export const getEmployeesByDepartment = (department) => {
  return request.get(`/employee/department/${department}`)
}

export const addEmployee = (data) => {
  return request.post('/employee', data)
}

export const updateEmployee = (data) => {
  return request.put('/employee', data)
}

export const deleteEmployee = (id) => {
  return request.delete(`/employee/${id}`)
}

export const resignEmployee = (id) => {
  return request.put(`/employee/${id}/resign`)
}

export const getEmployeeStatistics = () => {
  return request.get('/employee/statistics')
}
