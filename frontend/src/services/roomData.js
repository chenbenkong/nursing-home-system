// 房间床位数据服务
// 使用 localStorage 持久化存储，模拟后端数据库

const STORAGE_KEY = 'nursing_home_room_data'

// 默认房间数据
const defaultRooms = [
  { 
    id: 1, 
    roomNumber: '101', 
    floor: 1, 
    roomType: 'DOUBLE', 
    capacity: 2, 
    occupied: 1, 
    price: 3000, 
    status: 'PARTIAL', 
    facilities: ['独立卫生间', '空调', '电视'],
    beds: [
      {id: '101-A', bedNumber: 'A床', status: 'OCCUPIED', elderId: 1, elderName: '张大爷'},
      {id: '101-B', bedNumber: 'B床', status: 'AVAILABLE'}
    ]
  },
  { 
    id: 2, 
    roomNumber: '102', 
    floor: 1, 
    roomType: 'SINGLE', 
    capacity: 1, 
    occupied: 0, 
    price: 4500, 
    status: 'AVAILABLE', 
    facilities: ['独立卫生间', '空调', '电视', '阳台'],
    beds: [
      {id: '102-A', bedNumber: 'A床', status: 'AVAILABLE'}
    ]
  },
  { 
    id: 3, 
    roomNumber: '103', 
    floor: 1, 
    roomType: 'TRIPLE', 
    capacity: 3, 
    occupied: 1, 
    price: 2500, 
    status: 'PARTIAL', 
    facilities: ['独立卫生间', '空调'],
    beds: [
      {id: '103-A', bedNumber: 'A床', status: 'OCCUPIED', elderId: 2, elderName: '王大爷'},
      {id: '103-B', bedNumber: 'B床', status: 'AVAILABLE'},
      {id: '103-C', bedNumber: 'C床', status: 'AVAILABLE'}
    ]
  },
  { 
    id: 4, 
    roomNumber: '201', 
    floor: 2, 
    roomType: 'DOUBLE', 
    capacity: 2, 
    occupied: 0, 
    price: 3200, 
    status: 'AVAILABLE', 
    facilities: ['独立卫生间', '空调', '电视', '暖气'],
    beds: [
      {id: '201-A', bedNumber: 'A床', status: 'AVAILABLE'},
      {id: '201-B', bedNumber: 'B床', status: 'AVAILABLE'}
    ]
  },
  { 
    id: 5, 
    roomNumber: '202', 
    floor: 2, 
    roomType: 'SUITE', 
    capacity: 2, 
    occupied: 1, 
    price: 6000, 
    status: 'PARTIAL', 
    facilities: ['独立卫生间', '空调', '电视', '阳台', '暖气'],
    beds: [
      {id: '202-A', bedNumber: 'A床', status: 'OCCUPIED', elderId: 3, elderName: '赵奶奶'},
      {id: '202-B', bedNumber: 'B床', status: 'AVAILABLE'}
    ]
  },
  { 
    id: 6, 
    roomNumber: '301', 
    floor: 3, 
    roomType: 'MULTI', 
    capacity: 4, 
    occupied: 4, 
    price: 2000, 
    status: 'FULL', 
    facilities: ['独立卫生间', '空调'],
    beds: [
      {id: '301-A', bedNumber: 'A床', status: 'OCCUPIED', elderId: 4, elderName: '钱大爷'},
      {id: '301-B', bedNumber: 'B床', status: 'OCCUPIED', elderId: 5, elderName: '孙奶奶'},
      {id: '301-C', bedNumber: 'C床', status: 'OCCUPIED', elderId: 6, elderName: '周大爷'},
      {id: '301-D', bedNumber: 'D床', status: 'OCCUPIED', elderId: 7, elderName: '吴奶奶'}
    ]
  },
  { 
    id: 7, 
    roomNumber: '302', 
    floor: 3, 
    roomType: 'DOUBLE', 
    capacity: 2, 
    occupied: 0, 
    price: 2800, 
    status: 'MAINTENANCE', 
    facilities: ['独立卫生间', '空调', '电视'],
    beds: [
      {id: '302-A', bedNumber: 'A床', status: 'MAINTENANCE'},
      {id: '302-B', bedNumber: 'B床', status: 'MAINTENANCE'}
    ]
  }
]

// 获取所有房间数据
export const getAllRooms = () => {
  const data = localStorage.getItem(STORAGE_KEY)
  if (data) {
    return JSON.parse(data)
  }
  // 首次使用，初始化默认数据
  localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultRooms))
  return defaultRooms
}

// 保存所有房间数据
export const saveAllRooms = (rooms) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(rooms))
}

// 获取可用房间（有空闲床位的房间）
export const getAvailableRooms = () => {
  const rooms = getAllRooms()
  return rooms.filter(room => {
    const availableBeds = room.beds.filter(bed => bed.status === 'AVAILABLE')
    return availableBeds.length > 0 && room.status !== 'MAINTENANCE'
  })
}

// 获取指定房间的可用床位
export const getAvailableBedsByRoom = (roomId) => {
  const rooms = getAllRooms()
  // 处理 roomId 类型不匹配问题（字符串 vs 数字）
  const room = rooms.find(r => r.id == roomId)
  if (room) {
    return room.beds.filter(bed => bed.status === 'AVAILABLE')
  }
  return []
}

// 分配床位给老人
export const assignBedToElder = (roomId, bedId, elderId, elderName) => {
  console.log('assignBedToElder called:', { roomId, bedId, elderId, elderName })
  const rooms = getAllRooms()
  console.log('All rooms:', rooms)
  
  // 处理 roomId 类型不匹配问题（字符串 vs 数字）
  const room = rooms.find(r => r.id == roomId)
  console.log('Found room:', room)
  
  if (room) {
    const bed = room.beds.find(b => b.id == bedId)
    console.log('Found bed:', bed)
    
    if (bed) {
      bed.status = 'OCCUPIED'
      bed.elderId = elderId
      bed.elderName = elderName
      
      // 更新房间占用数和状态
      room.occupied = room.beds.filter(b => b.status === 'OCCUPIED').length
      if (room.occupied === 0) {
        room.status = 'AVAILABLE'
      } else if (room.occupied === room.capacity) {
        room.status = 'FULL'
      } else {
        room.status = 'PARTIAL'
      }
      
      saveAllRooms(rooms)
      console.log('Bed assigned successfully')
      return true
    } else {
      console.error('Bed not found:', bedId)
    }
  } else {
    console.error('Room not found:', roomId)
  }
  return false
}

// 释放床位（老人退住）
export const releaseBed = (roomId, bedId) => {
  const rooms = getAllRooms()
  // 处理 roomId 类型不匹配问题（字符串 vs 数字）
  const room = rooms.find(r => r.id == roomId)
  if (room) {
    const bed = room.beds.find(b => b.id == bedId)
    if (bed) {
      bed.status = 'AVAILABLE'
      delete bed.elderId
      delete bed.elderName
      
      // 更新房间占用数和状态
      room.occupied = room.beds.filter(b => b.status === 'OCCUPIED').length
      if (room.occupied === 0) {
        room.status = 'AVAILABLE'
      } else if (room.occupied === room.capacity) {
        room.status = 'FULL'
      } else {
        room.status = 'PARTIAL'
      }
      
      saveAllRooms(rooms)
      return true
    }
  }
  return false
}

// 添加新房间
export const addRoom = (roomData) => {
  const rooms = getAllRooms()
  const newRoom = {
    ...roomData,
    id: Date.now(),
    occupied: 0,
    status: 'AVAILABLE',
    beds: generateBeds(roomData.id || Date.now(), roomData.capacity)
  }
  rooms.push(newRoom)
  saveAllRooms(rooms)
  return newRoom
}

// 更新房间
export const updateRoom = (roomId, roomData) => {
  const rooms = getAllRooms()
  const index = rooms.findIndex(r => r.id === roomId)
  if (index !== -1) {
    rooms[index] = { ...rooms[index], ...roomData }
    saveAllRooms(rooms)
    return rooms[index]
  }
  return null
}

// 删除房间
export const deleteRoom = (roomId) => {
  const rooms = getAllRooms()
  const index = rooms.findIndex(r => r.id === roomId)
  if (index !== -1) {
    rooms.splice(index, 1)
    saveAllRooms(rooms)
    return true
  }
  return false
}

// 生成床位数据
const generateBeds = (roomId, capacity) => {
  const beds = []
  const bedLetters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
  for (let i = 0; i < capacity; i++) {
    beds.push({
      id: `${roomId}-${bedLetters[i]}`,
      bedNumber: `${bedLetters[i]}床`,
      status: 'AVAILABLE'
    })
  }
  return beds
}

// 重置数据（调试用）
export const resetRoomData = () => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(defaultRooms))
}
