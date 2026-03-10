<template>
  <el-container class="layout-container" :class="{ 'dark-mode': isDarkMode }">
    <!-- 侧边栏 - 完全隐藏模式 -->
    <el-aside v-show="!isCollapsed" width="200px" class="sidebar">
      <div class="logo">
        <div class="logo-seal">
          <span class="seal-text">养</span>
        </div>
        <span class="logo-text">智慧养老</span>
      </div>

      <el-menu
        :default-active="activeMenu"
        router
        :background-color="isDarkMode ? '#1a1a2e' : '#2c3e50'"
        text-color="#bfcbd9"
        active-text-color="#e8d5b5"
      >
        <el-menu-item v-for="item in menuList" :key="item.path" :index="item.path">
          <el-icon>
            <component :is="item.icon" />
          </el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container :class="{ 'full-width': isCollapsed }">
      <!-- 顶部导航栏 -->
      <el-header class="header">
        <!-- 白天模式装饰背景 -->
        <div v-if="!isDarkMode" class="header-art-bg day">
          <svg class="header-svg" viewBox="0 0 1200 60" preserveAspectRatio="none">
            <!-- 左侧装饰线条 -->
            <path class="deco-line" d="M20,45 Q100,40 180,45" />
            <path class="deco-line delay" d="M30,50 Q110,48 190,50" />
            <!-- 手绘云朵 -->
            <path class="cloud-line" d="M150,20 Q170,8 190,20 T230,20" />
            <path class="cloud-line delay" d="M1050,18 Q1070,6 1090,18 T1130,18" />
            <!-- 手绘飞鸟 -->
            <path class="bird-line" d="M280,25 Q290,20 300,25" />
            <path class="bird-line delay" d="M320,35 Q330,30 340,35" />
            <path class="bird-line delay2" d="M900,30 Q910,25 920,30" />
            <!-- 中间装饰图案 -->
            <circle class="sun-dot" cx="600" cy="15" r="3" />
            <path class="wave-line" d="M550,50 Q575,45 600,50 T650,50" />
            <path class="wave-line delay" d="M560,55 Q585,50 610,55 T660,55" />
            <!-- 右侧装饰线条 -->
            <path class="deco-line" d="M1020,45 Q1100,40 1180,45" />
            <path class="deco-line delay" d="M1010,50 Q1090,48 1170,50" />
            <!-- 小装饰点 -->
            <circle class="dot" cx="400" cy="20" r="2" />
            <circle class="dot delay" cx="800" cy="25" r="2" />
            <circle class="dot delay2" cx="450" cy="45" r="1.5" />
            <circle class="dot delay3" cx="750" cy="48" r="1.5" />
          </svg>
        </div>

        <!-- 黑夜模式星空背景 -->
        <div v-else class="header-art-bg night">
          <div class="stars">
            <span v-for="n in 30" :key="n" class="star" :class="'star-' + n"></span>
          </div>
          <svg class="header-svg" viewBox="0 0 1200 60" preserveAspectRatio="none">
            <!-- 月亮 -->
            <path class="moon" d="M580,15 A15,15 0 1,1 580,45 A10,10 0 1,0 580,15" />
            <!-- 星座连线 -->
            <path class="constellation" d="M100,30 L120,25 L140,35 L120,45 Z" />
            <path class="constellation" d="M1000,20 L1020,15 L1040,25" />
          </svg>
        </div>

        <div class="header-left">
          <div class="collapse-btn-wrapper" @click="toggleSidebar">
            <el-icon class="collapse-btn">
              <Fold v-if="!isCollapsed" />
              <Expand v-else />
            </el-icon>
            <span class="btn-text">菜单</span>
          </div>
        </div>

        <!-- 中间区域：日期时间 -->
        <div class="header-center">
          <div class="date-display">
            <span class="date-icon">❋</span>
            <div class="date-content">
              <span class="date-text">{{ currentDate }}</span>
              <span class="week-text">{{ currentWeek }}</span>
            </div>
          </div>
          <div class="divider-line"></div>
          <div class="time-display">
            <span class="time-icon">◈</span>
            <span class="time-text">{{ currentTime }}</span>
          </div>
        </div>

        <div class="header-right">
          <!-- 日夜切换按钮 -->
          <div class="theme-toggle" @click="toggleTheme">
            <div class="toggle-track" :class="{ 'dark': isDarkMode }">
              <div class="toggle-thumb">
                <el-icon v-if="!isDarkMode" class="sun-icon"><Sunny /></el-icon>
                <el-icon v-else class="moon-icon"><Moon /></el-icon>
              </div>
            </div>
            <span class="toggle-text">{{ isDarkMode ? '黑夜' : '白天' }}</span>
          </div>

          <!-- 问候语 -->
          <div class="greeting-text">
            <span class="greeting-icon">❦</span>
            <span>{{ greeting }}</span>
          </div>

          <!-- 用户头像下拉 -->
          <el-dropdown @command="handleCommand" class="user-dropdown">
            <span class="user-info">
              <div class="avatar-wrapper">
                <img v-if="userAvatar" :src="userAvatar" class="user-avatar" alt="avatar" />
                <el-avatar v-else :size="36" :icon="UserFilled" class="default-avatar" />
                <div class="avatar-ring"></div>
              </div>
              <span class="username">{{ userInfo?.realName || userInfo?.username }}</span>
              <el-icon class="dropdown-arrow"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu class="artistic-dropdown">
                <div class="dropdown-header">
                  <img v-if="userAvatar" :src="userAvatar" class="header-avatar" alt="avatar" />
                  <span class="header-username">{{ userInfo?.realName || userInfo?.username }}</span>
                  <span class="header-role">{{ userRole }}</span>
                </div>
                <el-dropdown-item command="profile">
                  <span class="item-icon">✦</span>
                  <span>个人中心</span>
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <span class="item-icon">✧</span>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { computed, ref, onMounted, onUnmounted, provide } from 'vue'
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  HomeFilled,
  UserFilled,
  Document,
  FirstAidKit,
  Avatar,
  Setting,
  Fold,
  Expand,
  ArrowDown,
  Box,
  Food,
  Clock,
  House,
  Sunny,
  Moon
} from '@element-plus/icons-vue'

// 手绘风格头像列表
const avatarList = [
  'https://api.dicebear.com/7.x/notionists/svg?seed=Felix&backgroundColor=e8d5b5',
  'https://api.dicebear.com/7.x/notionists/svg?seed=Aneka&backgroundColor=d4a574',
  'https://api.dicebear.com/7.x/notionists/svg?seed=Zack&backgroundColor=c9b99a',
  'https://api.dicebear.com/7.x/notionists/svg?seed=Bella&backgroundColor=e5d4b8',
  'https://api.dicebear.com/7.x/notionists/svg?seed=Leo&backgroundColor=d9c4a3',
  'https://api.dicebear.com/7.x/notionists/svg?seed=Molly&backgroundColor=c4b59d',
  'https://api.dicebear.com/7.x/notionists/svg?seed=Jack&backgroundColor=e0d0b8',
  'https://api.dicebear.com/7.x/notionists/svg?seed=Luna&backgroundColor=d5c4a8',
  'https://api.dicebear.com/7.x/notionists/svg?seed=Max&backgroundColor=cab99a',
  'https://api.dicebear.com/7.x/notionists/svg?seed=Ruby&backgroundColor=e3d2b5'
]

export default {
  name: 'Layout',
  components: {
    Fold,
    Expand,
    ArrowDown,
    Sunny,
    Moon
  },
  setup() {
    const store = useStore()
    const route = useRoute()
    const router = useRouter()

    const isCollapsed = computed(() => store.getters.sidebarCollapsed)
    const userInfo = computed(() => store.getters.userInfo)
    const currentTime = ref('')
    const currentDate = ref('')
    const currentWeek = ref('')
    const greeting = ref('')
    const userAvatar = ref('')
    const isDarkMode = ref(false)

    // 向子组件提供暗黑模式状态
    provide('isDarkMode', isDarkMode)

    // 根据用户名生成固定的随机头像
    const generateAvatar = () => {
      const username = userInfo.value?.username || 'default'
      let hash = 0
      for (let i = 0; i < username.length; i++) {
        hash = username.charCodeAt(i) + ((hash << 5) - hash)
      }
      const index = Math.abs(hash) % avatarList.length
      userAvatar.value = avatarList[index]
    }

    // 切换日夜模式
    const toggleTheme = () => {
      isDarkMode.value = !isDarkMode.value
      // 保存到本地存储
      localStorage.setItem('theme', isDarkMode.value ? 'dark' : 'light')
      // 同步到 body class，用于下拉框等挂载到 body 的元素
      if (isDarkMode.value) {
        document.body.classList.add('dark-theme')
      } else {
        document.body.classList.remove('dark-theme')
      }
    }

    // 根据时间自动判断主题
    const getThemeByTime = () => {
      const hour = new Date().getHours()
      // 晚上6点到早上6点为黑夜模式
      return hour >= 18 || hour < 6
    }

    // 初始化主题
    const initTheme = () => {
      const savedTheme = localStorage.getItem('theme')
      if (savedTheme) {
        // 使用用户保存的主题
        isDarkMode.value = savedTheme === 'dark'
      } else {
        // 没有保存过主题，根据时间自动判断
        isDarkMode.value = getThemeByTime()
      }
      
      // 同步到 body class
      if (isDarkMode.value) {
        document.body.classList.add('dark-theme')
        localStorage.setItem('theme', 'dark')
      } else {
        document.body.classList.remove('dark-theme')
        localStorage.setItem('theme', 'light')
      }
    }

    // 更新时间和日期
    const updateDateTime = () => {
      const now = new Date()
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      currentTime.value = `${hours}:${minutes}`

      // 日期格式：2026年3月9日
      const year = now.getFullYear()
      const month = now.getMonth() + 1
      const day = now.getDate()
      currentDate.value = `${year}年${month}月${day}日`

      // 星期
      const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
      currentWeek.value = weekDays[now.getDay()]

      // 问候语
      const hour = now.getHours()
      if (hour < 6) {
        greeting.value = '夜深了，注意休息'
      } else if (hour < 9) {
        greeting.value = '早安，开启美好一天'
      } else if (hour < 12) {
        greeting.value = '上午好，工作顺利'
      } else if (hour < 14) {
        greeting.value = '午安，记得休息'
      } else if (hour < 18) {
        greeting.value = '下午好，继续加油'
      } else if (hour < 21) {
        greeting.value = '晚上好，辛苦了'
      } else {
        greeting.value = '晚安，早点休息'
      }
    }

    let timeInterval = null

    const userRole = computed(() => {
      const role = userInfo.value?.role
      const roleMap = {
        'admin': '系统管理员',
        'staff': '工作人员',
        'nurse': '护理人员'
      }
      return roleMap[role] || '用户'
    })

    const activeMenu = computed(() => route.path)

    const menuList = [
      { path: '/dashboard', title: '首页', icon: HomeFilled },
      { path: '/elder', title: '老人管理', icon: UserFilled },
      { path: '/room', title: '房间管理', icon: House },
      { path: '/application', title: '入住申请', icon: Document },
      { path: '/care-plan', title: '护理计划', icon: FirstAidKit },
      { path: '/employee', title: '员工管理', icon: Avatar },
      { path: '/user', title: '系统用户', icon: Setting },
      { path: '/medicine', title: '药品管理', icon: FirstAidKit },
      { path: '/elder-medication', title: '老人用药', icon: FirstAidKit },
      { path: '/medication-record', title: '服药记录', icon: Clock },
      { path: '/meal', title: '膳食菜单', icon: Food },
      { path: '/elder-meal', title: '老人膳食', icon: Food },
      { path: '/health', title: '健康管理', icon: FirstAidKit },
      { path: '/physical-exam', title: '体检记录', icon: FirstAidKit },
      { path: '/inventory', title: '库存管理', icon: Box }
    ]

    const toggleSidebar = () => {
      store.dispatch('toggleSidebar')
    }

    const handleCommand = (command) => {
      switch (command) {
        case 'profile':
          router.push('/profile')
          break
        case 'logout':
          ElMessageBox.confirm('确定要退出登录吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            store.dispatch('logout')
            ElMessage.success('已退出登录')
            router.push('/login')
          }).catch(() => {})
          break
      }
    }

    onMounted(() => {
      generateAvatar()
      initTheme()
      updateDateTime()
      timeInterval = setInterval(updateDateTime, 1000)
    })

    onUnmounted(() => {
      if (timeInterval) {
        clearInterval(timeInterval)
      }
    })

    return {
      isCollapsed,
      userInfo,
      userAvatar,
      userRole,
      currentTime,
      currentDate,
      currentWeek,
      greeting,
      isDarkMode,
      activeMenu,
      menuList,
      UserFilled,
      toggleSidebar,
      handleCommand,
      toggleTheme
    }
  }
}
</script>

<style scoped lang="scss">
// 文艺配色
$ink-black: #1a1a1a;
$paper-cream: #f5f2ed;
$seal-red: #b5483c;
$gold-accent: #b8952e;
$sidebar-bg: #2c3e50;
$sidebar-dark: #1a252f;

// 黑夜模式配色
$dark-bg: #0f0f1a;
$dark-sidebar: #1a1a2e;
$dark-card: #1e1e32;
$dark-text: #e0e0e0;

.layout-container {
  height: 100vh;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Noto Serif SC', serif;

  // 黑夜模式全局样式
  &.dark-mode {
    .sidebar {
      background: linear-gradient(180deg, $dark-sidebar 0%, #0f0f1a 100%);

      .logo {
        background: rgba(0, 0, 0, 0.3);
      }
    }

    .header {
      background: linear-gradient(180deg, $dark-card 0%, $dark-sidebar 100%);
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3);

      .header-left {
        .collapse-btn-wrapper {
          .btn-text {
            color: $dark-text;
          }

          .collapse-btn {
            color: $dark-text;
          }
        }
      }

      .header-center {
        .date-display, .time-display {
          background: rgba(255, 255, 255, 0.1) !important;
          border-color: rgba(255, 255, 255, 0.2) !important;

          .date-content {
            .date-text {
              color: #ffffff !important;
            }

            .week-text {
              color: #c0c0c0 !important;
            }
          }

          .date-text, .time-text {
            color: #ffffff !important;
          }

          .week-text {
            color: #c0c0c0 !important;
          }

          .date-icon, .time-icon {
            color: #ff8a7a !important;
          }
        }

        .divider-line {
          background: linear-gradient(180deg, transparent, rgba(255, 255, 255, 0.4), transparent) !important;
        }
      }

      .header-right {
        .theme-toggle {
          .toggle-text {
            color: #d0d0d0;
          }
        }

        .greeting-text {
          background: rgba($seal-red, 0.2);
          color: #ffb0a0;

          .greeting-icon {
            color: #ff8a7a;
          }
        }

        .user-dropdown {
          .user-info {
            .username {
              color: #ffffff !important;
            }

            .dropdown-arrow {
              color: #b0b0b0 !important;
            }
          }
        }
      }
    }

    .main-content {
      background: linear-gradient(180deg, $dark-bg 0%, #0a0a14 100%);
    }
  }

  .sidebar {
    background: linear-gradient(180deg, $sidebar-bg 0%, $sidebar-dark 100%);
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);

    .logo {
      height: 64px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: rgba(0, 0, 0, 0.2);
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);

      .logo-seal {
        width: 36px;
        height: 36px;
        background: linear-gradient(135deg, $seal-red, #d47562);
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 2px 8px rgba($seal-red, 0.4);
        flex-shrink: 0;

        .seal-text {
          font-size: 20px;
          color: white;
          font-weight: 700;
          font-family: 'Noto Serif SC', serif;
        }
      }

      .logo-text {
        margin-left: 12px;
        font-size: 16px;
        font-weight: 600;
        color: #e8d5b5;
        white-space: nowrap;
        font-family: 'Noto Serif SC', serif;
        letter-spacing: 2px;
      }
    }

    .el-menu {
      border-right: none;
      background: transparent !important;

      :deep(.el-menu-item) {
        height: 48px;
        line-height: 48px;
        margin: 4px 12px;
        padding: 0 16px !important;
        border-radius: 8px;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;

        &:hover {
          background: rgba(255, 255, 255, 0.08) !important;
        }

        &.is-active {
          background: rgba($seal-red, 0.2) !important;
          border-left: 3px solid $seal-red;
        }

        .el-icon {
          font-size: 18px;
          margin-right: 12px;
          width: 18px;
          text-align: center;
          flex-shrink: 0;
        }

        .el-menu-item__title {
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }

  // 全宽模式
  .el-container.full-width {
    width: 100%;
  }

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.06);
    position: relative;
    height: 64px;
    padding: 0 24px;
    transition: all 0.3s ease;

    // 手绘装饰背景 - 白天模式
    .header-art-bg.day {
      position: absolute;
      inset: 0;
      overflow: hidden;
      pointer-events: none;
      opacity: 0.15;

      .header-svg {
        width: 100%;
        height: 100%;

        .deco-line {
          fill: none;
          stroke: #c9b99a;
          stroke-width: 1.5;
          stroke-linecap: round;
          stroke-dasharray: 200;
          stroke-dashoffset: 200;
          animation: drawLine 3s ease-out 0.3s forwards;

          &.delay {
            animation-delay: 0.6s;
          }
        }

        @keyframes drawLine {
          to { stroke-dashoffset: 0; }
        }

        .cloud-line {
          fill: none;
          stroke: #999;
          stroke-width: 1.5;
          stroke-linecap: round;
          stroke-dasharray: 200;
          stroke-dashoffset: 200;
          animation: drawCloud 4s ease-out 1s forwards;

          &.delay {
            animation-delay: 1.5s;
          }
        }

        @keyframes drawCloud {
          to { stroke-dashoffset: 0; }
        }

        .bird-line {
          fill: none;
          stroke: #666;
          stroke-width: 1.5;
          stroke-linecap: round;
          opacity: 0;
          animation: birdAppear 0.5s ease-out 1.5s forwards;

          &.delay {
            animation-delay: 1.8s;
          }

          &.delay2 {
            animation-delay: 2.1s;
          }
        }

        @keyframes birdAppear {
          to { opacity: 0.6; }
        }

        .sun-dot {
          fill: #c9a959;
          opacity: 0;
          animation: sunPulse 2s ease-out 2s forwards;
        }

        @keyframes sunPulse {
          0% { opacity: 0; transform: scale(0); }
          50% { opacity: 0.8; transform: scale(1.2); }
          100% { opacity: 0.5; transform: scale(1); }
        }

        .wave-line {
          fill: none;
          stroke: #b8a090;
          stroke-width: 1;
          stroke-linecap: round;
          stroke-dasharray: 100;
          stroke-dashoffset: 100;
          animation: drawWave 3s ease-out 1.5s forwards;

          &.delay {
            animation-delay: 1.8s;
          }
        }

        @keyframes drawWave {
          to { stroke-dashoffset: 0; }
        }

        .dot {
          fill: #c9a959;
          opacity: 0;
          animation: dotFade 1s ease-out forwards;

          &.delay {
            animation-delay: 0.5s;
          }

          &.delay2 {
            animation-delay: 0.8s;
          }

          &.delay3 {
            animation-delay: 1.1s;
          }
        }

        @keyframes dotFade {
          to { opacity: 0.4; }
        }
      }
    }

    // 黑夜模式星空背景
    .header-art-bg.night {
      position: absolute;
      inset: 0;
      overflow: hidden;
      pointer-events: none;
      background: linear-gradient(180deg, #0a0a14 0%, #1a1a2e 100%);

      .stars {
        position: absolute;
        inset: 0;

        .star {
          position: absolute;
          width: 2px;
          height: 2px;
          background: white;
          border-radius: 50%;
          opacity: 0;
          animation: starTwinkle 2s ease-in-out infinite;

          @for $i from 1 through 30 {
            &.star-#{$i} {
              left: random(100) * 1%;
              top: random(60) * 1%;
              animation-delay: random(20) * 0.1s;
              animation-duration: 1.5s + random(10) * 0.2s;
            }
          }
        }
      }

      @keyframes starTwinkle {
        0%, 100% { opacity: 0.3; transform: scale(1); }
        50% { opacity: 1; transform: scale(1.2); }
      }

      .shooting-stars {
        position: absolute;
        inset: 0;

        .shooting-star {
          position: absolute;
          top: 10px;
          left: -100px;
          width: 100px;
          height: 2px;
          background: linear-gradient(90deg, transparent, white, transparent);
          transform: rotate(-45deg);
          animation: shoot 4s ease-out infinite;

          &.delay {
            animation-delay: 2s;
            top: 30px;
          }
        }
      }

      @keyframes shoot {
        0% {
          left: -100px;
          opacity: 0;
        }
        10% {
          opacity: 1;
        }
        90% {
          opacity: 1;
        }
        100% {
          left: 120%;
          opacity: 0;
        }
      }

      .header-svg {
        width: 100%;
        height: 100%;

        .moon {
          fill: #f5f3ce;
          opacity: 0;
          animation: moonRise 2s ease-out forwards;
          filter: drop-shadow(0 0 10px rgba(245, 243, 206, 0.5));
        }

        @keyframes moonRise {
          from { opacity: 0; transform: translateY(20px); }
          to { opacity: 0.9; transform: translateY(0); }
        }

        .constellation {
          fill: none;
          stroke: rgba(255, 255, 255, 0.3);
          stroke-width: 1;
          stroke-dasharray: 100;
          stroke-dashoffset: 100;
          animation: drawConstellation 3s ease-out 1s forwards;
        }

        @keyframes drawConstellation {
          to { stroke-dashoffset: 0; }
        }

        .meteor-trail {
          fill: none;
          stroke: rgba(255, 255, 255, 0.2);
          stroke-width: 1;
          stroke-dasharray: 50;
          stroke-dashoffset: 50;
          animation: meteorTrail 2s ease-out infinite;

          &.delay {
            animation-delay: 1s;
          }
        }

        @keyframes meteorTrail {
          0% { stroke-dashoffset: 50; opacity: 0; }
          50% { opacity: 1; }
          100% { stroke-dashoffset: -50; opacity: 0; }
        }
      }
    }

    .header-left {
      display: flex;
      align-items: center;
      position: relative;
      z-index: 1;

      .collapse-btn-wrapper {
        display: flex;
        align-items: center;
        gap: 8px;
        cursor: pointer;
        padding: 8px 12px;
        border-radius: 8px;
        transition: all 0.3s ease;

        &:hover {
          background: rgba($seal-red, 0.1);

          .collapse-btn {
            color: $seal-red;
          }
        }

        .collapse-btn {
          font-size: 20px;
          color: #606266;
          transition: color 0.3s ease;
        }

        .btn-text {
          font-size: 14px;
          color: #606266;
          font-weight: 500;
        }
      }
    }

    .header-center {
      display: flex;
      align-items: center;
      gap: 20px;
      position: relative;
      z-index: 1;

      .date-display {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 8px 16px;
        background: linear-gradient(135deg, #faf8f5, #f5f0e8);
        border-radius: 20px;
        border: 1px solid #e8e0d5;

        .date-icon {
          color: $seal-red;
          font-size: 14px;
        }

        .date-content {
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          line-height: 1.3;

          .date-text {
            font-size: 13px;
            color: #303133;
            font-weight: 500;
          }

          .week-text {
            font-size: 11px;
            color: #909399;
          }
        }
      }

      .divider-line {
        width: 1px;
        height: 24px;
        background: linear-gradient(180deg, transparent, #d0c8c0, transparent);
      }

      .time-display {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 8px 16px;
        background: linear-gradient(135deg, #faf8f5, #f5f0e8);
        border-radius: 20px;
        border: 1px solid #e8e0d5;

        .time-icon {
          color: $seal-red;
          font-size: 12px;
        }

        .time-text {
          font-size: 16px;
          color: #303133;
          font-weight: 600;
          font-family: 'Courier New', monospace;
          letter-spacing: 2px;
        }
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 16px;
      position: relative;
      z-index: 1;

      // 日夜切换按钮
      .theme-toggle {
        display: flex;
        align-items: center;
        gap: 8px;
        cursor: pointer;
        padding: 4px;

        .toggle-track {
          width: 48px;
          height: 24px;
          background: linear-gradient(135deg, #87CEEB, #E0F6FF);
          border-radius: 12px;
          position: relative;
          transition: all 0.3s ease;
          box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);

          &.dark {
            background: linear-gradient(135deg, #1a1a2e, #2d2d44);

            .toggle-thumb {
              transform: translateX(24px);
            }
          }

          .toggle-thumb {
            position: absolute;
            top: 2px;
            left: 2px;
            width: 20px;
            height: 20px;
            background: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: transform 0.3s ease;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);

            .sun-icon {
              font-size: 12px;
              color: #f5a623;
            }

            .moon-icon {
              font-size: 12px;
              color: #6b7280;
            }
          }
        }

        .toggle-text {
          font-size: 12px;
          color: #606266;
          font-weight: 500;
        }
      }

      .greeting-text {
        display: flex;
        align-items: center;
        gap: 6px;
        padding: 8px 16px;
        background: linear-gradient(135deg, rgba($seal-red, 0.08), rgba($seal-red, 0.02));
        border-radius: 20px;
        font-size: 13px;
        color: $seal-red;
        font-weight: 500;

        .greeting-icon {
          font-size: 12px;
        }
      }

      .user-dropdown {
        .user-info {
          display: flex;
          align-items: center;
          gap: 10px;
          cursor: pointer;
          padding: 6px 12px;
          border-radius: 24px;
          transition: all 0.3s ease;

          &:hover {
            background: #f5f7fa;
          }

          .avatar-wrapper {
            position: relative;
            width: 36px;
            height: 36px;

            .user-avatar {
              width: 100%;
              height: 100%;
              border-radius: 50%;
              object-fit: cover;
              border: 2px solid #e8d5b5;
            }

            .default-avatar {
              background: linear-gradient(135deg, #e8d5b5, #d4a574);
              color: #fff;
            }

            .avatar-ring {
              position: absolute;
              inset: -3px;
              border: 2px solid transparent;
              border-top-color: $seal-red;
              border-radius: 50%;
              animation: avatarRotate 3s linear infinite;
            }

            @keyframes avatarRotate {
              to { transform: rotate(360deg); }
            }
          }

          .username {
            font-size: 14px;
            color: #606266;
            font-weight: 500;
          }

          .dropdown-arrow {
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }
  }

  .main-content {
    background-color: #f0f0f0;
    padding: 20px;
    overflow-y: auto;
    transition: background 0.3s ease;
  }
}

// ==================== 全局黑夜模式样式 ====================
// 这些样式会应用到所有子页面

.dark-mode {
  // Element Plus 卡片
  :deep(.el-card) {
    background-color: #1e1e32;
    border-color: rgba(255, 255, 255, 0.08);
    color: #e0e0e0;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);

    .el-card__header {
      border-bottom-color: rgba(255, 255, 255, 0.1);
      color: #ffffff;
      font-weight: 600;
      padding: 16px 20px;
    }

    .el-card__body {
      padding: 20px;
    }
  }

  // Element Plus 表格
  :deep(.el-table) {
    background-color: transparent;
    color: #e0e0e0;

    // 表头样式
    th.el-table__cell {
      background-color: #2a2a50;
      color: #ffffff;
      border-bottom-color: rgba(255, 255, 255, 0.15);
      font-weight: 600;
    }

    // 单元格基础样式 - 统一背景色，移除斑马纹
    td.el-table__cell {
      background-color: #1e1e32;
      border-bottom-color: rgba(255, 255, 255, 0.08);
    }

    // 移除斑马纹效果
    tr.el-table__row--striped {
      td.el-table__cell {
        background-color: #1e1e32;
      }
    }

    // 悬停效果 - 使用更明显的颜色
    tr:hover > td.el-table__cell {
      background-color: #353560;
    }

    // 空状态
    .el-table__empty-block {
      background-color: #1e1e32;
    }

    .el-table__empty-text {
      color: #909399;
    }

    // 表格行样式
    .el-table__row {
      transition: background-color 0.3s ease;
    }

    // 选中行样式
    .el-table__row.current-row > td.el-table__cell {
      background-color: #2a3a60;
    }

    // 展开行样式
    .el-table__expanded-cell {
      background-color: #252545;
    }
  }

  // Element Plus 分页
  :deep(.el-pagination) {
    color: #e0e0e0;

    .el-pagination__total,
    .el-pagination__jump {
      color: #a0a0a0;
    }

    .el-pager li {
      background-color: transparent;
      color: #c0c0c0;
      border-radius: 4px;
      transition: all 0.3s ease;

      &.active {
        background-color: #409EFF;
        color: #ffffff;
        font-weight: 600;
      }

      &:hover:not(.active) {
        color: #409EFF;
        background-color: rgba(64, 158, 255, 0.1);
      }
    }

    .btn-prev,
    .btn-next {
      background-color: transparent;
      color: #c0c0c0;
      border-radius: 4px;

      &:hover {
        color: #409EFF;
        background-color: rgba(64, 158, 255, 0.1);
      }

      &:disabled {
        color: #606060;
      }
    }

    .el-input__inner {
      background-color: #2a2a45;
      border-color: rgba(255, 255, 255, 0.1);
      color: #ffffff;
    }
  }

  // Element Plus 表单
  :deep(.el-form) {
    .el-form-item__label {
      color: #c0c0c0;
    }
  }

  // Element Plus 输入框
  :deep(.el-input) {
    .el-input__wrapper {
      background-color: #2a2a45;
      border-color: rgba(255, 255, 255, 0.1);
      box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.1) inset;
      transition: all 0.3s ease;

      &:hover {
        box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.2) inset;
      }

      &.is-focus {
        box-shadow: 0 0 0 1px #409EFF inset;
      }
    }

    .el-input__inner {
      color: #ffffff;
      font-weight: 500;

      &::placeholder {
        color: #808080;
      }
    }

    // 禁用状态
    &.is-disabled .el-input__wrapper {
      background-color: #252540;
    }
  }

  // Element Plus 按钮
  :deep(.el-button) {
    // 默认按钮
    &:not(.el-button--primary):not(.el-button--success):not(.el-button--warning):not(.el-button--danger):not(.el-button--link) {
      background-color: transparent;
      border-color: rgba(255, 255, 255, 0.2);
      color: #e0e0e0;

      &:hover {
        background-color: rgba(255, 255, 255, 0.1);
        border-color: rgba(255, 255, 255, 0.3);
        color: #ffffff;
      }
    }

    // 主要按钮
    &.el-button--primary {
      background-color: #409EFF;
      border-color: #409EFF;
      color: #ffffff;

      &:hover {
        background-color: #66b1ff;
        border-color: #66b1ff;
      }
    }

    // 成功按钮
    &.el-button--success {
      background-color: #67C23A;
      border-color: #67C23A;
      color: #ffffff;

      &:hover {
        background-color: #85ce61;
        border-color: #85ce61;
      }
    }

    // 危险按钮
    &.el-button--danger {
      background-color: #F56C6C;
      border-color: #F56C6C;
      color: #ffffff;

      &:hover {
        background-color: #f78989;
        border-color: #f78989;
      }
    }

    // 警告按钮
    &.el-button--warning {
      background-color: #E6A23C;
      border-color: #E6A23C;
      color: #ffffff;

      &:hover {
        background-color: #ebb563;
        border-color: #ebb563;
      }
    }

    // 纯文本按钮和链接按钮
    &.el-button--text {
      color: #409EFF;
      background-color: transparent;
      border-color: transparent;

      &:hover {
        color: #66b1ff;
        background-color: rgba(64, 158, 255, 0.1);
      }
    }

    // link 类型的按钮（表格操作列常用）
    &.el-button--link {
      background-color: transparent;
      border-color: transparent;

      // primary link
      &.el-button--primary {
        color: #79BBFF;

        &:hover {
          color: #a0cfff;
          background-color: rgba(64, 158, 255, 0.1);
        }
      }

      // success link
      &.el-button--success {
        color: #95D475;

        &:hover {
          color: #b3e19d;
          background-color: rgba(103, 194, 58, 0.1);
        }
      }

      // warning link
      &.el-button--warning {
        color: #F3D19E;

        &:hover {
          color: #f5dab1;
          background-color: rgba(230, 162, 60, 0.1);
        }
      }

      // danger link
      &.el-button--danger {
        color: #FAB6B6;

        &:hover {
          color: #fcd3d3;
          background-color: rgba(245, 108, 108, 0.1);
        }
      }
    }
  }

  // Element Plus 选择器
  :deep(.el-select) {
    .el-input__wrapper {
      background-color: #2a2a45;
      box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.1) inset;

      &:hover {
        box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.2) inset;
      }

      &.is-focus {
        box-shadow: 0 0 0 1px #409EFF inset;
      }
    }

    .el-input__inner {
      color: #ffffff;
      font-weight: 500;
    }
  }



  // Element Plus 对话框
  :deep(.el-dialog) {
    background-color: #1e1e32;

    .el-dialog__title {
      color: #ffffff;
    }

    .el-dialog__header {
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    }

    .el-dialog__body {
      color: #e0e0e0;
    }
  }

  // Element Plus 下拉菜单
  :deep(.el-dropdown-menu) {
    background-color: #1e1e32;
    border-color: rgba(255, 255, 255, 0.1);

    .el-dropdown-menu__item {
      color: #e0e0e0;

      &:hover {
        background-color: #2a2a45;
        color: #409EFF;
      }
    }
  }

  // Element Plus 标签
  :deep(.el-tag) {
    // 信息标签
    &.el-tag--info {
      background-color: rgba(255, 255, 255, 0.1);
      border-color: rgba(255, 255, 255, 0.15);
      color: #c0c0c0;
    }

    // 成功标签
    &.el-tag--success {
      background-color: rgba(103, 194, 58, 0.15);
      border-color: rgba(103, 194, 58, 0.3);
      color: #95D475;
    }

    // 警告标签
    &.el-tag--warning {
      background-color: rgba(230, 162, 60, 0.15);
      border-color: rgba(230, 162, 60, 0.3);
      color: #F3D19E;
    }

    // 危险标签
    &.el-tag--danger {
      background-color: rgba(245, 108, 108, 0.15);
      border-color: rgba(245, 108, 108, 0.3);
      color: #FAB6B6;
    }

    // 主要标签
    &.el-tag--primary {
      background-color: rgba(64, 158, 255, 0.15);
      border-color: rgba(64, 158, 255, 0.3);
      color: #79BBFF;
    }
  }

  // 搜索卡片
  :deep(.search-card) {
    background-color: #1e1e32;
    border-color: rgba(255, 255, 255, 0.08);
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);

    .el-card__body {
      background-color: #1e1e32;
      padding: 20px;
    }

    // 搜索表单标签
    .el-form-item__label {
      color: #c0c0c0;
      font-weight: 500;
    }
  }

  // 通用统计卡片样式（用于各模块）
  :deep(.stat-card) {
    background: linear-gradient(135deg, #252545, #2a2a50) !important;
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.3) !important;
    border: 1px solid rgba(255, 255, 255, 0.05);
    border-radius: 12px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px 0 rgba(0, 0, 0, 0.4) !important;
    }

    .stat-value {
      color: #ffffff !important;
      font-weight: 700;
      font-size: 28px;
    }

    .stat-label {
      color: #a0a0a0 !important;
      font-size: 14px;
    }

    .stat-icon {
      opacity: 0.9;
    }
  }

  // 房间管理模块 - 楼层视图
  :deep(.floor-view) {
    .floor-section {
      .floor-title {
        color: #e0e0e0;
        border-left-color: #409EFF;
      }
    }

    .room-card {
      background: linear-gradient(135deg, #252545, #2a2a50);
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.3);
      border: 1px solid rgba(255, 255, 255, 0.05);

      &:hover {
        box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.4);
      }

      .room-number {
        color: #ffffff;
      }

      .room-type {
        color: #a0a0a0;
      }

      .room-capacity {
        color: #c0c0c0;
      }
    }
  }

  // 单选按钮组（列表视图/楼层视图切换）
  :deep(.el-radio-group) {
    .el-radio-button {
      .el-radio-button__inner {
        background-color: #2a2a45;
        border-color: rgba(255, 255, 255, 0.2);
        color: #c0c0c0;

        &:hover {
          color: #409EFF;
        }
      }

      &.is-active {
        .el-radio-button__inner {
          background-color: #409EFF;
          border-color: #409EFF;
          color: #ffffff;
          box-shadow: none;
        }
      }
    }
  }

  // 复选框
  :deep(.el-checkbox) {
    .el-checkbox__label {
      color: #c0c0c0;
    }

    .el-checkbox__input {
      .el-checkbox__inner {
        background-color: #2a2a45;
        border-color: rgba(255, 255, 255, 0.2);
      }

      &.is-checked {
        .el-checkbox__inner {
          background-color: #409EFF;
          border-color: #409EFF;
        }

        + .el-checkbox__label {
          color: #409EFF;
        }
      }
    }
  }

  // 数字输入框
  :deep(.el-input-number) {
    .el-input__wrapper {
      background-color: #2a2a45;
    }

    .el-input-number__decrease,
    .el-input-number__increase {
      background-color: #353560;
      border-color: rgba(255, 255, 255, 0.1);
      color: #c0c0c0;

      &:hover {
        color: #409EFF;
      }
    }
  }

  // 描述列表
  :deep(.el-descriptions) {
    .el-descriptions__label {
      background-color: #252545;
      color: #a0a0a0;
    }

    .el-descriptions__content {
      background-color: #1e1e32;
      color: #e0e0e0;
    }
  }
}

// 全局下拉框样式 - 用于挂载到 body 的元素
// 注意：这些样式已移至 App.vue 中，以避免 scoped 样式限制
// 以下样式保留作为备份
:global(body.dark-theme) {
  // 日期选择器下拉框
  .el-picker-panel {
    background-color: #1e1e32 !important;
    border-color: rgba(255, 255, 255, 0.1) !important;
    color: #e0e0e0;

    .el-picker-panel__content {
      .el-date-table {
        th {
          color: #a0a0a0;
        }

        td {
          &.available {
            color: #e0e0e0;
          }

          &.current:not(.disabled) {
            span {
              background-color: #409EFF;
              color: #ffffff;
            }
          }

          &:hover {
            span {
              background-color: #2a2a45;
            }
          }
        }
      }
    }

    .el-picker-panel__sidebar {
      background-color: #252545;
      border-right-color: rgba(255, 255, 255, 0.1);

      .el-picker-panel__shortcut {
        color: #c0c0c0;

        &:hover {
          background-color: #2a2a45;
          color: #409EFF;
        }
      }
    }
  }

  // 消息提示
  .el-message {
    background-color: #1e1e32;
    border-color: rgba(255, 255, 255, 0.1);

    .el-message__content {
      color: #e0e0e0;
    }
  }

  // 确认框
  .el-message-box {
    background-color: #1e1e32;
    border-color: rgba(255, 255, 255, 0.1);

    .el-message-box__title {
      color: #ffffff;
    }

    .el-message__content {
      color: #e0e0e0;
    }
  }

  // 通知
  .el-notification {
    background-color: #1e1e32;
    border-color: rgba(255, 255, 255, 0.1);

    .el-notification__title {
      color: #ffffff;
    }

    .el-notification__content {
      color: #e0e0e0;
    }
  }
}

// 下拉菜单样式
:deep(.artistic-dropdown) {
  padding: 8px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

  .dropdown-header {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid #e8e8e8;
    margin-bottom: 8px;

    .header-avatar {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      object-fit: cover;
      border: 3px solid #e8d5b5;
      margin-bottom: 12px;
    }

    .header-username {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 4px;
    }

    .header-role {
      font-size: 12px;
      color: #909399;
      padding: 2px 10px;
      background: #f5f7fa;
      border-radius: 10px;
    }
  }

  .el-dropdown-menu__item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 16px;
    border-radius: 8px;
    margin: 4px 0;

    .item-icon {
      color: $seal-red;
      font-size: 12px;
    }

    &:hover {
      background: #f5f7fa;
      color: $seal-red;
    }
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
