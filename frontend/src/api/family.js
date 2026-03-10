import request from './request'

// 家属关联API
export function getFamilyRelationList(params) {
  return request({
    url: '/family-relation/list',
    method: 'get',
    params
  })
}

export function getAllFamilyRelations() {
  return request({
    url: '/family-relation/all',
    method: 'get'
  })
}

export function getFamilyRelationById(id) {
  return request({
    url: `/family-relation/${id}`,
    method: 'get'
  })
}

export function getRelationsByElderId(elderId) {
  return request({
    url: `/family-relation/elder/${elderId}`,
    method: 'get'
  })
}

export function addFamilyRelation(data) {
  return request({
    url: '/family-relation',
    method: 'post',
    data
  })
}

export function updateFamilyRelation(data) {
  return request({
    url: '/family-relation',
    method: 'put',
    data
  })
}

export function deleteFamilyRelation(id) {
  return request({
    url: `/family-relation/${id}`,
    method: 'delete'
  })
}

export function getFamilyRelationStatistics() {
  return request({
    url: '/family-relation/statistics',
    method: 'get'
  })
}
