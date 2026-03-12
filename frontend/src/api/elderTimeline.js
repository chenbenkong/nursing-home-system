import request from './request'

/**
 * 获取老人时间轴事件列表
 * @param {number} elderId 老人ID
 * @returns {Promise}
 */
export const getElderTimeline = (elderId) => {
  return request.get(`/elder-timeline/${elderId}`)
}
