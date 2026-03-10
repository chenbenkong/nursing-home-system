import request from './request'

export const getDashboardStatistics = () => {
  return request.get('/dashboard/statistics')
}
