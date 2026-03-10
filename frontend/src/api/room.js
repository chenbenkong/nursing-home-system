import request from './request'

// 获取房间列表
export const getRoomList = (params) => {
  return request.get('/room/list', { params })
}

// 获取所有房间
export const getAllRooms = () => {
  return request.get('/room/all')
}

// 获取可用房间（有空闲床位）
export const getAvailableRooms = () => {
  return request.get('/room/available')
}

// 根据ID获取房间
export const getRoomById = (id) => {
  return request.get(`/room/${id}`)
}

// 新增房间
export const addRoom = (data) => {
  return request.post('/room', data)
}

// 更新房间
export const updateRoom = (data) => {
  return request.put('/room', data)
}

// 删除房间
export const deleteRoom = (id) => {
  return request.delete(`/room/${id}`)
}
