import { createRouter, createWebHashHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import store from '../store'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue'),
    meta: { public: true }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../views/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'elder',
        name: 'Elder',
        component: () => import('../views/elder/index.vue'),
        meta: { title: '老人管理', icon: 'UserFilled' }
      },
      {
        path: 'room',
        name: 'Room',
        component: () => import('../views/room/index.vue'),
        meta: { title: '房间管理', icon: 'House' }
      },
      {
        path: 'application',
        name: 'Application',
        component: () => import('../views/application/index.vue'),
        meta: { title: '入住申请', icon: 'Document' }
      },
      {
        path: 'care-plan',
        name: 'CarePlan',
        component: () => import('../views/care-plan/index.vue'),
        meta: { title: '护理计划', icon: 'FirstAidKit' }
      },
      {
        path: 'employee',
        name: 'Employee',
        component: () => import('../views/employee/index.vue'),
        meta: { title: '员工管理', icon: 'Avatar' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/user/index.vue'),
        meta: { title: '系统用户', icon: 'Setting' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'User' }
      },
      {
        path: 'medicine',
        name: 'Medicine',
        component: () => import('../views/medicine/index.vue'),
        meta: { title: '药品管理', icon: 'FirstAidKit' }
      },
      {
        path: 'meal',
        name: 'Meal',
        component: () => import('../views/meal/index.vue'),
        meta: { title: '膳食管理', icon: 'Food' }
      },
      {
        path: 'inventory',
        name: 'Inventory',
        component: () => import('../views/inventory/index.vue'),
        meta: { title: '库存管理', icon: 'Box' }
      },
      {
        path: 'health',
        name: 'Health',
        component: () => import('../views/health/index.vue'),
        meta: { title: '健康管理', icon: 'FirstAidKit' }
      },
      {
        path: 'elder-medication',
        name: 'ElderMedication',
        component: () => import('../views/elder-medication/index.vue'),
        meta: { title: '老人用药', icon: 'FirstAidKit' }
      },
      {
        path: 'medication-record',
        name: 'MedicationRecord',
        component: () => import('../views/medication-record/index.vue'),
        meta: { title: '服药记录', icon: 'Clock' }
      },
      {
        path: 'elder-meal',
        name: 'ElderMeal',
        component: () => import('../views/elder-meal/index.vue'),
        meta: { title: '老人膳食', icon: 'Food' }
      },
      {
        path: 'physical-exam',
        name: 'PhysicalExam',
        component: () => import('../views/physical-exam/index.vue'),
        meta: { title: '体检记录', icon: 'FirstAidKit' }
      },
      {
        path: 'family-relation',
        name: 'FamilyRelation',
        component: () => import('../views/family-relation/index.vue'),
        meta: { title: '家属关联', icon: 'User' }
      },
      {
        path: 'fee',
        name: 'Fee',
        component: () => import('../views/fee/index.vue'),
        meta: { title: '费用管理', icon: 'Money' }
      },

    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/error/404.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 初始化存储（页面刷新时从 localStorage 恢复状态）
  store.dispatch('initStorage')
  
  const token = store.getters.token
  
  if (to.meta.public) {
    // 公开页面直接放行
    next()
  } else if (!token) {
    // 未登录跳转到登录页
    ElMessage.warning('请先登录')
    next('/login')
  } else {
    // 已登录放行
    next()
  }
})

export default router
