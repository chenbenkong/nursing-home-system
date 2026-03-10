import { createStore } from 'vuex'

// 存储工具，优先使用 localStorage，如果不支持则使用内存存储
const storage = {
  getItem(key) {
    try {
      return localStorage.getItem(key)
    } catch (e) {
      console.warn('localStorage not available, using memory storage')
      return memoryStorage[key] || null
    }
  },
  setItem(key, value) {
    try {
      localStorage.setItem(key, value)
    } catch (e) {
      console.warn('localStorage not available, using memory storage')
      memoryStorage[key] = value
    }
  },
  removeItem(key) {
    try {
      localStorage.removeItem(key)
    } catch (e) {
      console.warn('localStorage not available, using memory storage')
      delete memoryStorage[key]
    }
  }
}

// 内存存储备用
const memoryStorage = {}

export default createStore({
  state: {
    userInfo: (() => {
      const data = storage.getItem('userInfo')
      try {
        return data ? JSON.parse(data) : null
      } catch (e) {
        console.error('Failed to parse userInfo:', e)
        return null
      }
    })(),
    token: storage.getItem('token') || '',
    sidebarCollapsed: false
  },
  getters: {
    isLoggedIn: state => !!state.token,
    userInfo: state => state.userInfo,
    token: state => state.token,
    sidebarCollapsed: state => state.sidebarCollapsed
  },
  mutations: {
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      if (userInfo) {
        storage.setItem('userInfo', JSON.stringify(userInfo))
      } else {
        storage.removeItem('userInfo')
      }
    },
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        storage.setItem('token', token)
      } else {
        storage.removeItem('token')
      }
    },
    CLEAR_USER_INFO(state) {
      state.userInfo = null
      state.token = ''
      storage.removeItem('userInfo')
      storage.removeItem('token')
    },
    TOGGLE_SIDEBAR(state) {
      state.sidebarCollapsed = !state.sidebarCollapsed
    }
  },
  actions: {
    login({ commit }, { userInfo, token }) {
      commit('SET_USER_INFO', userInfo)
      commit('SET_TOKEN', token)
    },
    logout({ commit }) {
      commit('CLEAR_USER_INFO')
    },
    toggleSidebar({ commit }) {
      commit('TOGGLE_SIDEBAR')
    },
    // 初始化存储（页面刷新时调用）
    initStorage({ commit }) {
      const token = storage.getItem('token')
      const userInfo = storage.getItem('userInfo')
      if (token) {
        commit('SET_TOKEN', token)
      }
      if (userInfo) {
        try {
          commit('SET_USER_INFO', JSON.parse(userInfo))
        } catch (e) {
          console.error('Failed to parse userInfo:', e)
          commit('SET_USER_INFO', null)
        }
      }
    }
  }
})
