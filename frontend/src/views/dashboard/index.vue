<template>
  <div class="artistic-dashboard" :class="{ 'dark-mode': isDarkMode }">
    <!-- 文艺动画背景 -->
    <ArtisticBackground />

    <!-- 主内容区 -->
    <div class="dashboard-content">
      <!-- 统计卡片区域 -->
      <div class="stats-section">
        <div class="section-header">
          <span class="section-icon">❧</span>
          <span class="section-title">数据概览</span>
          <span class="section-line"></span>
        </div>
        
        <div class="stats-grid">
          <div class="stat-card blue" :class="{ 'loading': loading }">
            <div class="stat-icon-wrapper">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.elderTotal || 0 }}</div>
              <div class="stat-label">老人总数</div>
            </div>
          </div>

          <div class="stat-card green" :class="{ 'loading': loading }">
            <div class="stat-icon-wrapper">
              <el-icon><HomeFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.roomTotal || 0 }}</div>
              <div class="stat-label">房间总数</div>
            </div>
          </div>

          <div class="stat-card orange" :class="{ 'loading': loading }">
            <div class="stat-icon-wrapper">
              <el-icon><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.employeeTotal || 0 }}</div>
              <div class="stat-label">在职员工</div>
            </div>
          </div>

          <div class="stat-card pink" :class="{ 'loading': loading, 'alert': stats.pendingApplications > 0 }">
            <div class="stat-icon-wrapper">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.pendingApplications || 0 }}</div>
              <div class="stat-label">待审申请</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 快捷操作区域 -->
      <div class="quick-actions-section">
        <div class="section-header">
          <span class="section-icon">✦</span>
          <span class="section-title">快捷操作</span>
          <span class="section-line"></span>
        </div>

        <div class="actions-container">
          <!-- 第一排 -->
          <div class="actions-row">
            <div class="action-item-v2" @click="$router.push('/elder')">
              <div class="action-icon-v2 blue">
                <el-icon><User /></el-icon>
              </div>
              <span class="action-label-v2">老人管理</span>
            </div>

            <div class="action-item-v2" @click="$router.push('/room')">
              <div class="action-icon-v2 purple">
                <el-icon><HomeFilled /></el-icon>
              </div>
              <span class="action-label-v2">房间管理</span>
            </div>

            <div class="action-item-v2" @click="$router.push('/application')">
              <div class="action-icon-v2 green">
                <el-icon><DocumentChecked /></el-icon>
              </div>
              <span class="action-label-v2">入住申请</span>
            </div>

            <div class="action-item-v2" @click="$router.push('/care-plan')">
              <div class="action-icon-v2 yellow">
                <el-icon><Calendar /></el-icon>
              </div>
              <span class="action-label-v2">护理计划</span>
            </div>

            <div class="action-item-v2" @click="$router.push('/employee')">
              <div class="action-icon-v2 teal">
                <el-icon><UserFilled /></el-icon>
              </div>
              <span class="action-label-v2">员工管理</span>
            </div>
          </div>

          <!-- 第二排 -->
          <div class="actions-row">
            <div class="action-item-v2" @click="$router.push('/medicine')">
              <div class="action-icon-v2 red">
                <el-icon><FirstAidKit /></el-icon>
              </div>
              <span class="action-label-v2">药品管理</span>
            </div>

            <div class="action-item-v2" @click="$router.push('/meal')">
              <div class="action-icon-v2 pink">
                <el-icon><Food /></el-icon>
              </div>
              <span class="action-label-v2">膳食管理</span>
            </div>

            <div class="action-item-v2" @click="$router.push('/health')">
              <div class="action-icon-v2 cyan">
                <el-icon><CircleCheck /></el-icon>
              </div>
              <span class="action-label-v2">健康管理</span>
            </div>

            <div class="action-item-v2" @click="$router.push('/inventory')">
              <div class="action-icon-v2 indigo">
                <el-icon><Box /></el-icon>
              </div>
              <span class="action-label-v2">库存管理</span>
            </div>

            <div class="action-item-v2" @click="$router.push('/physical-exam')">
              <div class="action-icon-v2 lime">
                <el-icon><Document /></el-icon>
              </div>
              <span class="action-label-v2">体检记录</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 文艺涂鸦装饰区 -->
      <div class="art-doodles">
        <!-- 手绘装饰线 -->
        <svg class="doodle-lines" viewBox="0 0 1200 100" preserveAspectRatio="xMidYMid meet">
          <path class="wavy-line" d="M0,50 Q100,30 200,50 T400,50 T600,50 T800,50 T1000,50 T1200,50" />
          <path class="wavy-line delay" d="M0,60 Q100,40 200,60 T400,60 T600,60 T800,60 T1000,60 T1200,60" />
          <circle class="doodle-dot" cx="100" cy="30" r="3" />
          <circle class="doodle-dot delay" cx="300" cy="70" r="2" />
          <circle class="doodle-dot delay2" cx="500" cy="25" r="2.5" />
          <circle class="doodle-dot delay3" cx="700" cy="75" r="2" />
          <circle class="doodle-dot delay4" cx="900" cy="35" r="3" />
          <circle class="doodle-dot delay5" cx="1100" cy="65" r="2" />
        </svg>
        
        <!-- 漂浮的文艺元素 -->
        <div class="floating-doodles">
          <span class="doodle-item leaf-1">🍃</span>
          <span class="doodle-item leaf-2">🌿</span>
          <span class="doodle-item flower-1">🌸</span>
          <span class="doodle-item leaf-3">🍂</span>
          <span class="doodle-item flower-2">🌺</span>
          <span class="doodle-item leaf-4">🌱</span>
        </div>
        
        <!-- 手绘小图标 -->
        <div class="hand-drawn-icons">
          <span class="icon-item">✿</span>
          <span class="icon-item">❀</span>
          <span class="icon-item">✾</span>
          <span class="icon-item">❁</span>
          <span class="icon-item">✽</span>
        </div>
      </div>

      <!-- 底部诗意标语 -->
      <div class="poetry-footer">
        <div class="footer-decoration">
          <span class="decoration-left">❧</span>
          <span class="decoration-line"></span>
          <span class="decoration-right">❧</span>
        </div>
        <p class="poetry-text">「 用心守护，温暖相伴 」</p>
        <div class="footer-year">
          <span class="year-text">二〇二六年</span>
          <span class="year-divider">·</span>
          <span class="year-name">智慧养老</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/api/request'
import { inject, ref, watch, onMounted } from 'vue'
import { User, HomeFilled, DocumentChecked, Calendar, UserFilled, FirstAidKit, Food, CircleCheck, Box, Document } from '@element-plus/icons-vue'
import ArtisticBackground from '@/components/ArtisticBackground.vue'

export default {
  name: 'Dashboard',
  components: {
    ArtisticBackground,
    User,
    HomeFilled,
    DocumentChecked,
    Calendar,
    UserFilled,
    FirstAidKit,
    Food,
    CircleCheck,
    Box,
    Document
  },
  setup() {
    // 从 layout 注入主题状态
    const injectedDarkMode = inject('isDarkMode', null)
    const isDarkMode = ref(false)
    
    // 如果注入成功，同步值
    if (injectedDarkMode) {
      watch(injectedDarkMode, (val) => {
        isDarkMode.value = val
      }, { immediate: true })
    }
    
    return { isDarkMode }
  },
  data() {
    return {
      stats: {
        elderTotal: 0,
        elderCheckedIn: 0,
        elderPending: 0,
        employeeTotal: 0,
        employeeActive: 0,
        nursingStaff: 0,
        roomTotal: 0,
        roomAvailable: 0,
        pendingApplications: 0
      },
      prevStats: {},
      loading: false
    }
  },
  computed: {
    elderTrend() {
      if (!this.prevStats.elderTotal) return 0
      return this.stats.elderTotal - this.prevStats.elderTotal
    },
    roomTrend() {
      if (!this.prevStats.roomTotal) return 0
      return this.stats.roomTotal - this.prevStats.roomTotal
    },
    employeeTrend() {
      if (!this.prevStats.employeeTotal) return 0
      return this.stats.employeeTotal - this.prevStats.employeeTotal
    }
  },
  created() {
    this.fetchStats()
    // 定时刷新数据
    this.refreshInterval = setInterval(() => {
      this.fetchStats(false)
    }, 30000)
  },
  beforeUnmount() {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval)
    }
  },
  methods: {
    async fetchStats(showLoading = true) {
      if (showLoading) this.loading = true
      try {
        // 保存上次的数据用于计算趋势
        this.prevStats = { ...this.stats }
        const res = await request.get('/dashboard/statistics')
        if (res.code === 200) {
          this.stats = res.data
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      } finally {
        if (showLoading) this.loading = false
      }
    }
  }
}
</script>

<style scoped lang="scss">
// 文艺配色
$ink-black: #1a1a1a;
$ink-gray: #4a4a4a;
$paper-cream: #f5f2ed;
$paper-warm: #ebe5db;
$seal-red: #b5483c;
$seal-red-light: #c45c48;
$mountain-teal: #4a7a7a;
$mountain-blue: #6a9a9a;
$gold-accent: #b8952e;

.artistic-dashboard {
  min-height: calc(100vh - 60px);
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 50%, #f0f2f5 100%);
  font-family: 'Noto Serif SC', 'Source Han Serif SC', 'SimSun', serif;
  padding: 24px;
  z-index: 0;

  // 黑夜模式
  &.dark-mode {
    background: linear-gradient(135deg, #0f0f1a 0%, #1a1a2e 50%, #16213e 100%);

    // 区块标题 - 深色模式
    .section-header {
      .section-icon { color: $gold-accent; }
      .section-title { color: rgba(255, 255, 255, 0.8); }
      .section-line { background: linear-gradient(90deg, rgba(255,255,255,0.2), transparent); }
    }

    // 统计卡片 - 深色模式
    .stat-card {
      background: rgba(30, 30, 50, 0.8);
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);
      &:hover {
        background: rgba(40, 40, 60, 0.9);
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
      }
      &.alert {
        border-left: 4px solid #F78989;
      }
      &.blue {
        border-left: 4px solid #79BBFF;
        .stat-icon-wrapper {
          background: linear-gradient(135deg, #79BBFF, #A0CFFF);
          border-radius: 12px;
        }
        .stat-value { color: #A0CFFF; }
      }
      &.green {
        border-left: 4px solid #95D475;
        .stat-icon-wrapper {
          background: linear-gradient(135deg, #95D475, #B3E19D);
          border-radius: 12px;
        }
        .stat-value { color: #B3E19D; }
      }
      &.orange {
        border-left: 4px solid #F3D19E;
        .stat-icon-wrapper {
          background: linear-gradient(135deg, #F3D19E, #F5DAB1);
          border-radius: 12px;
        }
        .stat-value { color: #F5DAB1; }
      }
      &.pink {
        border-left: 4px solid #F78989;
        .stat-icon-wrapper {
          background: linear-gradient(135deg, #F78989, #FAB6B6);
          border-radius: 12px;
        }
        .stat-value { color: #FAB6B6; }
      }
      .stat-icon-wrapper {
        .el-icon { color: #fff; }
      }
      .stat-info {
        .stat-label { color: rgba(255, 255, 255, 0.5); }
      }
    }

    // 快捷操作 - 深色模式
    .action-item-v2 {
      background: transparent;
      border: none;
      box-shadow: none;
      &:hover {
        background: transparent;
        box-shadow: none;
        border-color: transparent;
      }
    }
    .action-label-v2 { color: rgba(255, 255, 255, 0.75); }

    // 文艺涂鸦 - 深色模式
    .art-doodles {
      .doodle-lines {
        .wavy-line { stroke: rgba(255, 255, 255, 0.3); }
        .doodle-dot { fill: rgba(255, 255, 255, 0.4); }
      }
      .hand-drawn-icons {
        .icon-item { color: rgba(255, 255, 255, 0.2); }
      }
    }

    // 底部 - 深色模式
    .poetry-footer {
      .footer-decoration {
        .decoration-left, .decoration-right { color: rgba(255, 255, 255, 0.3); }
        .decoration-line { background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent); }
      }
      .poetry-text { color: rgba(255, 255, 255, 0.5); }
      .footer-year { color: rgba(255, 255, 255, 0.3); }
    }
  }
}

// 主内容区
.dashboard-content {
  position: relative;
  z-index: 1;
  max-width: 1400px;
  margin: 0 auto;
}

// 区块标题
.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;

  .section-icon {
    color: $seal-red;
    font-size: 14px;
  }

  .section-title {
    font-size: 15px;
    color: #606266;
    letter-spacing: 2px;
    font-weight: 500;
  }

  .section-line {
    flex: 1;
    height: 1px;
    background: linear-gradient(90deg, rgba(0,0,0,0.1), transparent);
    margin-left: 12px;
  }
}

// 统计区域
.stats-section {
  margin-bottom: 32px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  }

  &.loading {
    opacity: 0.7;
  }

  &.alert {
    border-left: 4px solid #F56C6C;
  }

  &.blue {
    border-left: 4px solid #79BBFF;
    .stat-icon-wrapper {
      background: linear-gradient(135deg, #79BBFF, #A0CFFF);
      border-radius: 12px;
    }
    .stat-value { color: #79BBFF; }
  }

  &.green {
    border-left: 4px solid #95D475;
    .stat-icon-wrapper {
      background: linear-gradient(135deg, #95D475, #B3E19D);
      border-radius: 12px;
    }
    .stat-value { color: #95D475; }
  }

  &.orange {
    border-left: 4px solid #F3D19E;
    .stat-icon-wrapper {
      background: linear-gradient(135deg, #F3D19E, #F5DAB1);
      border-radius: 12px;
    }
    .stat-value { color: #F3D19E; }
  }

  &.pink {
    border-left: 4px solid #F78989;
    .stat-icon-wrapper {
      background: linear-gradient(135deg, #F78989, #FAB6B6);
      border-radius: 12px;
    }
    .stat-value { color: #F78989; }
  }

  .stat-icon-wrapper {
    width: 48px;
    height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    flex-shrink: 0;

    .el-icon {
      font-size: 24px;
      color: #fff;
    }
  }

  .stat-info {
    flex: 1;

    .stat-value {
      font-size: 28px;
      font-weight: 700;
      line-height: 1;
      margin-bottom: 4px;
    }

    .stat-label {
      font-size: 13px;
      color: #909399;
    }
  }
}

// 快捷操作区域
.quick-actions-section {
  margin-bottom: 32px;
}

.actions-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.actions-row {
  display: flex;
  justify-content: center;
  gap: 40px;
  flex-wrap: wrap;
}

.action-item-v2 {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 0;
  border-radius: 0;
  transition: all 0.3s ease;
  background: transparent;
  border: none;
  min-width: auto;
  box-shadow: none;

  &:hover {
    background: transparent;
    transform: translateY(-4px);
    box-shadow: none;
    border-color: transparent;
  }

  &:active {
    transform: translateY(-2px);
  }
}

.action-icon-v2 {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;

  .el-icon {
    font-size: 32px;
  }

  &.blue { 
    background: linear-gradient(135deg, #409EFF, #5B8FF9); 
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  }
  &.purple { 
    background: linear-gradient(135deg, #8B5CF6, #A78BFA); 
    box-shadow: 0 4px 12px rgba(139, 92, 246, 0.3);
  }
  &.green { 
    background: linear-gradient(135deg, #67C23A, #85CE61); 
    box-shadow: 0 4px 12px rgba(103, 194, 58, 0.3);
  }
  &.yellow { 
    background: linear-gradient(135deg, #E6A23C, #EBB563); 
    box-shadow: 0 4px 12px rgba(230, 162, 60, 0.3);
  }
  &.teal { 
    background: linear-gradient(135deg, #14b8a6, #2dd4bf); 
    box-shadow: 0 4px 12px rgba(20, 184, 166, 0.3);
  }
  &.red { 
    background: linear-gradient(135deg, #F56C6C, #F78989); 
    box-shadow: 0 4px 12px rgba(245, 108, 108, 0.3);
  }
  &.pink { 
    background: linear-gradient(135deg, #ec4899, #f472b6); 
    box-shadow: 0 4px 12px rgba(236, 72, 153, 0.3);
  }
  &.cyan { 
    background: linear-gradient(135deg, #06b6d4, #22d3ee); 
    box-shadow: 0 4px 12px rgba(6, 182, 212, 0.3);
  }
  &.indigo { 
    background: linear-gradient(135deg, #6366f1, #818cf8); 
    box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
  }
  &.lime { 
    background: linear-gradient(135deg, #84cc16, #a3e635); 
    box-shadow: 0 4px 12px rgba(132, 204, 22, 0.3);
  }
}

.action-label-v2 {
  font-size: 13px;
  color: #606266;
  text-align: center;
  white-space: nowrap;
  font-weight: 500;
}

// 文艺涂鸦装饰区
.art-doodles {
  position: relative;
  padding: 40px 0;
  margin-bottom: 20px;
  overflow: hidden;

  .doodle-lines {
    width: 100%;
    height: 100px;
    opacity: 0.15;

    .wavy-line {
      fill: none;
      stroke: #909399;
      stroke-width: 1.5;
      stroke-linecap: round;
      stroke-dasharray: 1200;
      stroke-dashoffset: 1200;
      animation: drawWavyLine 3s ease-out forwards;

      &.delay {
        animation-delay: 0.5s;
      }
    }

    @keyframes drawWavyLine {
      to { stroke-dashoffset: 0; }
    }

    .doodle-dot {
      fill: #c0c4cc;
      opacity: 0;
      animation: dotAppear 0.5s ease-out forwards;

      &.delay { animation-delay: 1s; }
      &.delay2 { animation-delay: 1.2s; }
      &.delay3 { animation-delay: 1.4s; }
      &.delay4 { animation-delay: 1.6s; }
      &.delay5 { animation-delay: 1.8s; }
    }

    @keyframes dotAppear {
      to { opacity: 0.6; }
    }
  }

  .floating-doodles {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    pointer-events: none;

    .doodle-item {
      position: absolute;
      font-size: 20px;
      opacity: 0.4;
      animation: floatDoodle 8s ease-in-out infinite;

      &.leaf-1 { left: 10%; top: 20%; animation-delay: 0s; }
      &.leaf-2 { left: 25%; top: 60%; animation-delay: 1s; font-size: 16px; }
      &.flower-1 { left: 40%; top: 30%; animation-delay: 2s; font-size: 18px; }
      &.leaf-3 { left: 55%; top: 50%; animation-delay: 3s; font-size: 14px; }
      &.flower-2 { left: 70%; top: 25%; animation-delay: 4s; font-size: 16px; }
      &.leaf-4 { left: 85%; top: 55%; animation-delay: 5s; font-size: 18px; }
    }

    @keyframes floatDoodle {
      0%, 100% { transform: translateY(0) rotate(0deg); }
      25% { transform: translateY(-10px) rotate(5deg); }
      50% { transform: translateY(0) rotate(0deg); }
      75% { transform: translateY(10px) rotate(-5deg); }
    }
  }

  .hand-drawn-icons {
    display: flex;
    justify-content: center;
    gap: 30px;
    margin-top: 20px;

    .icon-item {
      font-size: 14px;
      color: rgba(0, 0, 0, 0.15);
      animation: iconPulse 3s ease-in-out infinite;

      &:nth-child(2) { animation-delay: 0.5s; }
      &:nth-child(3) { animation-delay: 1s; }
      &:nth-child(4) { animation-delay: 1.5s; }
      &:nth-child(5) { animation-delay: 2s; }
    }

    @keyframes iconPulse {
      0%, 100% { opacity: 0.15; transform: scale(1); }
      50% { opacity: 0.3; transform: scale(1.2); }
    }
  }
}

// 底部诗意区域
.poetry-footer {
  text-align: center;
  padding: 24px 0;

  .footer-decoration {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    margin-bottom: 16px;

    .decoration-left,
    .decoration-right {
      color: rgba(0, 0, 0, 0.2);
      font-size: 14px;
    }

    .decoration-line {
      width: 100px;
      height: 1px;
      background: linear-gradient(90deg, transparent, rgba(0,0,0,0.1), transparent);
    }
  }

  .poetry-text {
    font-size: 16px;
    color: #606266;
    letter-spacing: 6px;
    font-style: italic;
    margin-bottom: 16px;
  }

  .footer-year {
    font-size: 12px;
    color: #909399;
    letter-spacing: 2px;

    .year-divider {
      margin: 0 8px;
    }
  }
}

// 响应式
@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .actions-row {
    gap: 16px;
  }

  .action-item-v2 {
    min-width: 80px;
    padding: 6px 12px;
  }
}

@media (max-width: 768px) {
  .actions-row {
    gap: 12px;
  }

  .action-item-v2 {
    min-width: 70px;
    padding: 6px 10px;
  }

  .action-icon-v2 {
    width: 40px;
    height: 40px;
    font-size: 20px;

    .el-icon {
      font-size: 20px;
    }
  }

  .action-label-v2 {
    font-size: 12px;
  }
}

@media (max-width: 640px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .welcome-section .welcome-text .main-title {
    font-size: 28px;
  }

  .actions-row {
    gap: 8px;
  }

  .action-item-v2 {
    min-width: 60px;
    padding: 4px 8px;
  }

  .action-icon-v2 {
    width: 36px;
    height: 36px;
    font-size: 18px;

    .el-icon {
      font-size: 18px;
    }
  }

  .action-label-v2 {
    font-size: 11px;
  }
}
</style>
