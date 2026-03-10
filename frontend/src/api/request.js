import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import store from '../store'

const request = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL || '',
  timeout: 10000
})

// 存储工具，优先使用 localStorage，如果不支持则使用内存存储
const storage = {
  getItem(key) {
    try {
      return localStorage.getItem(key)
    } catch (e) {
      console.warn('localStorage not available')
      return null
    }
  }
}

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 优先从 store 获取 token，如果没有则从 localStorage 获取
    let token = store.getters.token
    if (!token) {
      token = storage.getItem('token')
      // 如果 localStorage 中有 token 但 store 中没有，则初始化 store
      if (token) {
        store.dispatch('initStorage')
      }
    }
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    ElMessage.error('请求发送失败，请检查网络连接')
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    if (res.code !== 200) {
      // 根据错误码显示不同的错误信息
      switch (res.code) {
        case 400:
          ElMessage.warning(res.message || '请求参数错误')
          break
        case 401:
          ElMessage.warning('登录已过期，请重新登录')
          store.dispatch('logout')
          router.push('/login')
          break
        case 403:
          ElMessage.error('没有权限执行此操作')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error(res.message || '服务器内部错误，请稍后重试')
          break
        default:
          ElMessage.error(res.message || '请求失败')
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    return res
  },
  error => {
    // 处理网络错误
    if (!error.response) {
      if (error.code === 'ECONNABORTED') {
        ElMessage.error('请求超时，请检查网络连接')
      } else if (error.message.includes('Network Error')) {
        ElMessage.error('网络连接失败，请检查网络设置')
      } else {
        ElMessage.error('网络错误，请稍后重试')
      }
      return Promise.reject(error)
    }
    
    const status = error.response.status
    const message = error.response?.data?.message
    
    switch (status) {
      case 400:
        ElMessage.warning(message || '请求参数错误')
        break
      case 401:
        ElMessage.warning('登录已过期，请重新登录')
        store.dispatch('logout')
        router.push('/login')
        break
      case 403:
        ElMessage.error('没有权限执行此操作')
        break
      case 404:
        ElMessage.error('请求的资源不存在')
        break
      case 405:
        ElMessage.error('请求方法不被允许')
        break
      case 408:
        ElMessage.error('请求超时，请稍后重试')
        break
      case 500:
        ElMessage.error('服务器内部错误，请稍后重试')
        break
      case 502:
        ElMessage.error('网关错误，请稍后重试')
        break
      case 503:
        ElMessage.error('服务不可用，请稍后重试')
        break
      case 504:
        ElMessage.error('网关超时，请稍后重试')
        break
      default:
        ElMessage.error(message || `请求失败(${status})`)
    }
    
    return Promise.reject(error)
  }
)

export default request
