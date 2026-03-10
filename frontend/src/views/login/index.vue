<template>
  <div class="artistic-login" :class="{ 'dark-mode': isDarkMode }">
    <!-- 黑夜模式切换按钮 -->
    <div class="theme-toggle-fixed" @click="toggleTheme">
      <div class="toggle-track" :class="{ 'dark': isDarkMode }">
        <div class="toggle-thumb">
          <el-icon v-if="!isDarkMode" class="sun-icon"><Sunny /></el-icon>
          <el-icon v-else class="moon-icon"><Moon /></el-icon>
        </div>
      </div>
      <span class="toggle-text">{{ isDarkMode ? '黑夜' : '白天' }}</span>
    </div>

    <!-- 手绘风格背景层 -->
    <div class="canvas-bg">
      <!-- 水彩晕染效果 -->
      <div class="watercolor watercolor-1"></div>
      <div class="watercolor watercolor-2"></div>
      <div class="watercolor watercolor-3"></div>
      
      <!-- 手绘线条装饰 -->
      <svg class="hand-drawn-lines" viewBox="0 0 1920 1080" preserveAspectRatio="xMidYMid slice">
        <path class="draw-line" d="M-100,200 Q400,150 800,300 T1600,200" />
        <path class="draw-line" d="M-50,600 Q300,500 600,650 T1400,550" />
        <path class="draw-line" d="M200,-50 Q250,300 150,600 T200,1100" />
        <path class="draw-line" d="M1600,-30 Q1550,400 1650,700 T1600,1130" />
      </svg>
      
      <!-- 飘落的叶子/花瓣 -->
      <div class="floating-elements">
        <span v-for="n in 12" :key="n" class="float-item" :class="'item-' + n"></span>
      </div>

      <!-- 黑夜模式星空背景 -->
      <div v-if="isDarkMode" class="night-sky">
        <div class="stars">
          <span v-for="n in 50" :key="n" class="star" :class="'star-' + n"></span>
        </div>
        <div class="shooting-stars">
          <span class="shooting-star"></span>
          <span class="shooting-star delay"></span>
        </div>
      </div>
    </div>

    <div class="main-container">
      <!-- 左侧诗意品牌区 -->
      <div class="poetry-section">
        <div class="content-wrapper">
          <!-- 印章式Logo -->
          <div class="seal-logo">
            <div class="seal-inner">
              <span class="seal-text">养</span>
            </div>
            <div class="seal-glow"></div>
          </div>
          
          <!-- 主标题 - 书法风格 -->
          <div class="title-group">
            <h1 class="main-title">
              <span class="char" v-for="(char, i) in '智慧养老'" :key="i" :style="{ animationDelay: i * 0.15 + 's' }">{{ char }}</span>
            </h1>
            <div class="title-divider">
              <span class="divider-line"></span>
              <span class="divider-dot">◆</span>
              <span class="divider-line"></span>
            </div>
            <p class="sub-title">以科技之智 · 护长者安康</p>
          </div>
          
          <!-- 诗意描述 -->
          <div class="poetry-verse">
            <p class="verse-line" v-for="(line, i) in verses" :key="i" :style="{ animationDelay: (i + 4) * 0.3 + 's' }">
              {{ line }}
            </p>
          </div>
          
          <!-- 手绘装饰图案 -->
          <div class="decoration-illustration">
            <svg viewBox="0 0 400 200" class="illustration-svg">
              <!-- 手绘山峦 -->
              <path class="mountain" d="M20,180 Q80,80 150,140 T280,100 L380,180" />
              <path class="mountain mountain-back" d="M50,180 Q120,60 200,120 T350,90" />
              <!-- 手绘太阳/月 -->
              <circle class="sun" cx="320" cy="50" r="25" />
              <!-- 手绘飞鸟 -->
              <path class="bird" d="M100,60 Q110,50 120,60 M110,60 Q120,50 130,60" />
              <path class="bird bird-2" d="M140,80 Q150,70 160,80 M150,80 Q160,70 170,80" />
              <!-- 手绘云朵 -->
              <path class="cloud" d="M200,40 Q220,20 240,40 T280,40" />
            </svg>
          </div>
        </div>
      </div>

      <!-- 右侧表单区 -->
      <div class="form-section">
        <div class="paper-card">
          <!-- 纸张纹理 -->
          <div class="paper-texture"></div>
          
          <!-- 书签式Tab -->
          <div class="bookmark-tabs">
            <div 
              class="bookmark" 
              :class="{ active: !isRegister }"
              @click="isRegister = false"
            >
              <span class="bookmark-text">登录</span>
              <div class="bookmark-ribbon"></div>
            </div>
            <div 
              class="bookmark" 
              :class="{ active: isRegister }"
              @click="isRegister = true"
            >
              <span class="bookmark-text">注册</span>
              <div class="bookmark-ribbon"></div>
            </div>
          </div>

          <!-- 表单内容 -->
          <div class="form-content">
            <div class="form-header">
              <h2 class="form-title">{{ isRegister ? '新友来访' : '老友归来' }}</h2>
              <p class="form-desc">{{ isRegister ? '请填写信息，开启温馨之旅' : '请输入账号密码，继续您的守护' }}</p>
            </div>

            <el-form
              ref="formRef"
              :model="form"
              :rules="rules"
              class="artistic-form"
              @keyup.enter="handleSubmit"
            >
              <div class="input-flow">
                <el-form-item prop="username">
                  <div class="ink-input-wrapper">
                    <label class="input-label">
                      <span class="label-icon">✦</span>
                      <span>用户名</span>
                    </label>
                    <div class="input-field">
                      <el-input
                        v-model="form.username"
                        placeholder="请输入您的用户名"
                        size="large"
                        class="ink-input"
                      />
                      <div class="input-underline">
                        <span class="underline-base"></span>
                        <span class="underline-focus"></span>
                      </div>
                    </div>
                  </div>
                </el-form-item>

                <el-form-item prop="password">
                  <div class="ink-input-wrapper">
                    <label class="input-label">
                      <span class="label-icon">✦</span>
                      <span>密码</span>
                    </label>
                    <div class="input-field">
                      <el-input
                        v-model="form.password"
                        type="password"
                        placeholder="请输入您的密码"
                        size="large"
                        show-password
                        class="ink-input"
                      />
                      <div class="input-underline">
                        <span class="underline-base"></span>
                        <span class="underline-focus"></span>
                      </div>
                    </div>
                  </div>
                </el-form-item>

                <el-form-item v-if="isRegister" prop="confirmPassword">
                  <div class="ink-input-wrapper">
                    <label class="input-label">
                      <span class="label-icon">✦</span>
                      <span>确认密码</span>
                    </label>
                    <div class="input-field">
                      <el-input
                        v-model="form.confirmPassword"
                        type="password"
                        placeholder="请再次输入密码"
                        size="large"
                        show-password
                        class="ink-input"
                      />
                      <div class="input-underline">
                        <span class="underline-base"></span>
                        <span class="underline-focus"></span>
                      </div>
                    </div>
                  </div>
                </el-form-item>
              </div>

              <div class="submit-area">
                <button
                  type="button"
                  class="ink-button"
                  :class="{ loading: loading }"
                  :disabled="loading"
                  @click="handleSubmit"
                >
                  <span class="btn-text">{{ isRegister ? '开启旅程' : '进入系统' }}</span>
                  <span class="btn-ink"></span>
                  <span class="btn-shine"></span>
                </button>
              </div>
            </el-form>

            <div class="form-footer-artistic">
              <div class="footer-line">
                <span class="line-left"></span>
                <span class="line-text">{{ isRegister ? '已有账号' : '尚无账号' }}</span>
                <span class="line-right"></span>
              </div>
              <a class="switch-link" @click="isRegister = !isRegister">
                <span class="link-text">{{ isRegister ? '直接登录 →' : '立即注册 →' }}</span>
                <span class="link-underline"></span>
              </a>
            </div>
          </div>

          <!-- 角落装饰 -->
          <div class="corner-decoration top-left">❧</div>
          <div class="corner-decoration top-right">❧</div>
          <div class="corner-decoration bottom-left">❧</div>
          <div class="corner-decoration bottom-right">❧</div>
        </div>

        <!-- 底部版权 -->
        <div class="artistic-footer">
          <span class="footer-year">二〇二六年</span>
          <span class="footer-divider">·</span>
          <span class="footer-name">智慧养老</span>
          <span class="footer-divider">·</span>
          <span class="footer-rights">All Rights Reserved</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '@/api/user'
import { Sunny, Moon } from '@element-plus/icons-vue'

export default {
  name: 'Login',
  components: {
    Sunny,
    Moon
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const formRef = ref(null)
    const loading = ref(false)
    const isRegister = ref(false)
    const isDarkMode = ref(false)

    const verses = [
      '岁月温柔，科技相伴',
      '守护每一位长者的安康',
      '让爱与关怀，触手可及'
    ]

    const form = reactive({
      username: '',
      password: '',
      confirmPassword: ''
    })

    const validateConfirmPassword = (rule, value, callback) => {
      if (isRegister.value && value !== form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在3-20个字符之间', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在6-20个字符之间', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur', validator: validateConfirmPassword }
      ]
    }

    const handleLogin = async () => {
      if (!formRef.value) return

      await formRef.value.validate(async (valid) => {
        if (valid) {
          loading.value = true
          try {
            const res = await login({
              username: form.username,
              password: form.password
            })
            if (res.code === 200) {
              const { user, token } = res.data
              store.dispatch('login', { userInfo: user, token })
              ElMessage.success('欢迎回来')
              router.push('/')
            }
          } catch (error) {
            console.error('登录失败:', error)
          } finally {
            loading.value = false
          }
        }
      })
    }

    const handleRegister = async () => {
      if (!formRef.value) return

      await formRef.value.validate(async (valid) => {
        if (valid) {
          loading.value = true
          try {
            const res = await register({
              username: form.username,
              password: form.password,
              role: 'staff',
              status: 1
            })
            if (res.code === 200) {
              ElMessage.success('注册成功，请登录')
              isRegister.value = false
              form.password = ''
              form.confirmPassword = ''
            }
          } catch (error) {
            console.error('注册失败:', error)
          } finally {
            loading.value = false
          }
        }
      })
    }

    const handleSubmit = () => {
      if (isRegister.value) {
        handleRegister()
      } else {
        handleLogin()
      }
    }

    // 根据时间自动判断主题
    const getThemeByTime = () => {
      const hour = new Date().getHours()
      // 晚上6点到早上6点为黑夜模式
      return hour >= 18 || hour < 6
    }

    // 切换日夜模式
    const toggleTheme = () => {
      isDarkMode.value = !isDarkMode.value
      localStorage.setItem('loginTheme', isDarkMode.value ? 'dark' : 'light')
    }

    // 初始化主题
    onMounted(() => {
      const savedTheme = localStorage.getItem('loginTheme')
      if (savedTheme) {
        // 使用用户保存的主题
        isDarkMode.value = savedTheme === 'dark'
      } else {
        // 没有保存过主题，根据时间自动判断
        isDarkMode.value = getThemeByTime()
      }
    })

    return {
      formRef,
      form,
      rules,
      loading,
      isRegister,
      isDarkMode,
      verses,
      handleSubmit,
      toggleTheme
    }
  }
}
</script>

<style scoped lang="scss">
// 文艺配色 - 加深色调
$ink-black: #1a1a1a;
$ink-gray: #4a4a4a;
$paper-cream: #f5f2ed;
$paper-warm: #ebe5db;
$seal-red: #b5483c;
$seal-red-light: #c45c48;
$mountain-teal: #4a7a7a;
$mountain-blue: #6a9a9a;
$gold-accent: #b8952e;

.artistic-login {
  width: 100vw;
  height: 100vh;
  position: relative;
  overflow: hidden;
  background: $paper-cream;
  font-family: 'Noto Serif SC', 'Source Han Serif SC', 'SimSun', serif;

  // 黑夜模式样式
  &.dark-mode {
    background: #0f0f1a;

    // 切换按钮
    .theme-toggle-fixed {
      background: rgba(255, 255, 255, 0.1);
      border-color: rgba(255, 255, 255, 0.2);

      .toggle-text {
        color: #d0d0d0;
      }
    }

    // 左侧诗意区域
    .poetry-section {
      .seal-logo {
        .seal-inner {
          background: linear-gradient(135deg, #8b3a30 0%, #a0453a 100%);
          box-shadow: 0 10px 30px rgba(139, 58, 48, 0.4);
        }
      }

      .title-group {
        .main-title {
          color: #f0f0f0;
        }

        .sub-title {
          color: #b0b0b0;
        }

        .title-divider {
          .divider-line {
            background: linear-gradient(90deg, transparent, #606060, transparent);
          }
        }
      }

      .poetry-verse {
        .verse-line {
          color: #a0a0a0;
        }
      }

      .decoration-illustration {
        .illustration-svg {
          .mountain {
            stroke: #4a6a6a;
          }

          .sun {
            stroke: #d4a574;
          }

          .bird, .cloud {
            stroke: #707070;
          }
        }
      }
    }

    // 右侧表单区域
    .form-section {
      .paper-card {
        background: linear-gradient(135deg, #1e1e32, #2a2a45);
        box-shadow: 0 6px 30px rgba(0, 0, 0, 0.4);

        .paper-texture {
          opacity: 0.02;
        }

        .corner-decoration {
          color: #ff8a7a;
          opacity: 0.6;
        }
      }

      .bookmark-tabs {
        .bookmark {
          background: #2a2a40;

          .bookmark-text {
            color: #a0a0a0;
          }

          &:hover {
            background: #3a3a50;
            .bookmark-text { color: #ff8a7a; }
          }

          &.active {
            background: linear-gradient(135deg, #1e1e32, #2a2a45);

            .bookmark-text {
              color: #ff8a7a;
            }

            .bookmark-ribbon {
              border-top-color: #ff8a7a;
            }
          }
        }
      }

      .form-header {
        .form-title {
          color: #f0f0f0;
        }

        .form-desc {
          color: #a0a0a0;
        }
      }

      .artistic-form {
        .ink-input-wrapper {
          .input-label {
            color: #b0b0b0;

            .label-icon {
              color: #ff8a7a;
            }
          }

          .input-field {
            .ink-input {
              :deep(.el-input__inner) {
                color: #f0f0f0;

                &::placeholder {
                  color: #707070;
                }
              }
            }

            .input-underline {
              .underline-base {
                background: #404050;
              }

              .underline-focus {
                background: #ff8a7a;
              }
            }
          }
        }

        .ink-button {
          border-color: #ff8a7a;
          color: #ff8a7a;

          .btn-ink {
            background: #ff8a7a;
          }

          &:hover {
            color: white;
            border-color: #ff8a7a;
          }
        }
      }

      .form-footer-artistic {
        .footer-line {
          .line-left, .line-right {
            background: linear-gradient(90deg, transparent, #505060, transparent);
          }

          .line-text {
            color: #707070;
          }
        }

        .switch-link {
          .link-text {
            color: #ff8a7a;
          }

          .link-underline {
            background: #ff8a7a;
          }
        }
      }

      .artistic-footer {
        color: rgba(255, 255, 255, 0.4);

        .footer-divider {
          color: rgba(255, 255, 255, 0.2);
        }
      }
    }
  }
}

// 日夜切换按钮 - 固定在右上角
.theme-toggle-fixed {
  position: fixed;
  top: 24px;
  right: 24px;
  z-index: 100;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 24px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.05);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  .toggle-track {
    width: 44px;
    height: 22px;
    background: linear-gradient(135deg, #87CEEB, #E0F6FF);
    border-radius: 11px;
    position: relative;
    transition: all 0.3s ease;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);

    &.dark {
      background: linear-gradient(135deg, #1a1a2e, #2d2d44);

      .toggle-thumb {
        transform: translateX(22px);
      }
    }

    .toggle-thumb {
      position: absolute;
      top: 2px;
      left: 2px;
      width: 18px;
      height: 18px;
      background: white;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: transform 0.3s ease;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);

      .sun-icon {
        font-size: 10px;
        color: #f5a623;
      }

      .moon-icon {
        font-size: 10px;
        color: #6b7280;
      }
    }
  }

  .toggle-text {
    font-size: 13px;
    color: #606266;
    font-weight: 500;
  }
}

// 手绘风格背景
.canvas-bg {
  position: fixed;
  inset: 0;
  z-index: 0;
  overflow: hidden;

  // 水彩晕染效果 - 加深
  .watercolor {
    position: absolute;
    border-radius: 50%;
    filter: blur(60px);
    opacity: 0.22;
    animation: watercolorMove 20s ease-in-out infinite;

    &-1 {
      width: 550px;
      height: 550px;
      background: radial-gradient(circle, $mountain-teal 0%, transparent 70%);
      top: -180px;
      right: -80px;
      animation-delay: 0s;
    }

    &-2 {
      width: 450px;
      height: 450px;
      background: radial-gradient(circle, $seal-red-light 0%, transparent 70%);
      bottom: -120px;
      left: -80px;
      animation-delay: -7s;
    }

    &-3 {
      width: 350px;
      height: 350px;
      background: radial-gradient(circle, $gold-accent 0%, transparent 70%);
      top: 45%;
      left: 25%;
      animation-delay: -14s;
    }
  }

  @keyframes watercolorMove {
    0%, 100% { transform: translate(0, 0) scale(1); }
    33% { transform: translate(30px, -30px) scale(1.1); }
    66% { transform: translate(-20px, 20px) scale(0.95); }
  }

  // 手绘线条
  .hand-drawn-lines {
    position: absolute;
    inset: 0;
    width: 100%;
    height: 100%;
    opacity: 0.12;

    .draw-line {
      fill: none;
      stroke: $ink-black;
      stroke-width: 1.5;
      stroke-linecap: round;
      stroke-dasharray: 2000;
      stroke-dashoffset: 2000;
      animation: drawLine 8s ease-out forwards;

      &:nth-child(2) { animation-delay: 0.5s; }
      &:nth-child(3) { animation-delay: 1s; }
      &:nth-child(4) { animation-delay: 1.5s; }
    }
  }

  @keyframes drawLine {
    to { stroke-dashoffset: 0; }
  }

  // 飘落的叶子/花瓣
  .floating-elements {
    position: absolute;
    inset: 0;
    pointer-events: none;

    .float-item {
      position: absolute;
      width: 20px;
      height: 20px;
      opacity: 0.4;

      &::before {
        content: '🍃';
        font-size: 16px;
        position: absolute;
      }

      @for $i from 1 through 12 {
        &.item-#{$i} {
          left: random(100) * 1%;
          top: -30px;
          animation: floatDown 12s + random(8) * 1s linear infinite;
          animation-delay: random(10) * 1s;
          transform: rotate(random(360) * 1deg);

          &::before {
            content: if($i % 3 == 0, '🌸', if($i % 3 == 1, '🍃', '🌿'));
            font-size: 12px + random(8) * 1px;
          }
        }
      }
    }
  }

  @keyframes floatDown {
    0% {
      transform: translateY(0) rotate(0deg) translateX(0);
      opacity: 0;
    }
    10% { opacity: 0.4; }
    90% { opacity: 0.4; }
    100% {
      transform: translateY(110vh) rotate(360deg) translateX(50px);
      opacity: 0;
    }
  }

  // 黑夜模式星空背景
  .night-sky {
    position: absolute;
    inset: 0;
    overflow: hidden;
    pointer-events: none;

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

        @for $i from 1 through 50 {
          &.star-#{$i} {
            left: random(100) * 1%;
            top: random(100) * 1%;
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
        top: 10%;
        left: -100px;
        width: 100px;
        height: 2px;
        background: linear-gradient(90deg, transparent, white, transparent);
        transform: rotate(-45deg);
        animation: shoot 4s ease-out infinite;

        &.delay {
          animation-delay: 2s;
          top: 30%;
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
  }
}

// 主容器
.main-container {
  position: relative;
  z-index: 1;
  width: 100%;
  height: 100%;
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
}

// 左侧诗意品牌区
.poetry-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;

  .content-wrapper {
    max-width: 480px;
    text-align: center;
  }

  // 印章式Logo
  .seal-logo {
    position: relative;
    width: 100px;
    height: 100px;
    margin: 0 auto 40px;
    cursor: pointer;

    .seal-inner {
      width: 100%;
      height: 100%;
      background: linear-gradient(135deg, $seal-red 0%, $seal-red-light 100%);
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 
        0 10px 30px rgba($seal-red, 0.3),
        inset 0 2px 4px rgba(255, 255, 255, 0.3);
      animation: sealStamp 0.6s ease-out;

      .seal-text {
        font-size: 48px;
        color: white;
        font-weight: 700;
        font-family: 'Noto Serif SC', serif;
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
      }
    }

    .seal-glow {
      position: absolute;
      inset: -10px;
      background: radial-gradient(circle, rgba($seal-red, 0.2) 0%, transparent 70%);
      border-radius: 20px;
      animation: sealGlow 2s ease-in-out infinite;
    }

    &:hover .seal-inner {
      transform: scale(1.05);
      transition: transform 0.3s ease;
    }
  }

  @keyframes sealStamp {
    0% { transform: scale(2); opacity: 0; }
    50% { transform: scale(0.9); }
    100% { transform: scale(1); opacity: 1; }
  }

  @keyframes sealGlow {
    0%, 100% { opacity: 0.5; transform: scale(1); }
    50% { opacity: 0.8; transform: scale(1.1); }
  }

  // 主标题
  .title-group {
    margin-bottom: 40px;

    .main-title {
      font-size: 56px;
      font-weight: 700;
      color: $ink-black;
      margin-bottom: 20px;
      letter-spacing: 12px;
      font-family: 'Noto Serif SC', serif;

      .char {
        display: inline-block;
        opacity: 0;
        transform: translateY(20px);
        animation: charFadeIn 0.6s ease forwards;
      }
    }

    @keyframes charFadeIn {
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .title-divider {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 16px;
      margin-bottom: 16px;

      .divider-line {
        width: 60px;
        height: 1px;
        background: linear-gradient(90deg, transparent, $ink-gray, transparent);
      }

      .divider-dot {
        color: $seal-red;
        font-size: 12px;
        animation: dotPulse 2s ease-in-out infinite;
      }
    }

    @keyframes dotPulse {
      0%, 100% { opacity: 0.5; transform: scale(1); }
      50% { opacity: 1; transform: scale(1.2); }
    }

    .sub-title {
      font-size: 18px;
      color: $ink-gray;
      letter-spacing: 6px;
      font-weight: 400;
      opacity: 0;
      animation: fadeInUp 0.8s ease 0.8s forwards;
    }
  }

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(10px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  // 诗意描述
  .poetry-verse {
    margin-bottom: 40px;

    .verse-line {
      font-size: 16px;
      color: $ink-gray;
      line-height: 2;
      letter-spacing: 2px;
      opacity: 0;
      transform: translateX(-20px);
      animation: verseSlideIn 0.8s ease forwards;
    }
  }

  @keyframes verseSlideIn {
    to {
      opacity: 1;
      transform: translateX(0);
    }
  }

  // 手绘装饰图案
  .decoration-illustration {
    width: 100%;
    max-width: 320px;
    margin: 0 auto;
    opacity: 0.6;

    .illustration-svg {
      width: 100%;
      height: auto;

      .mountain {
        fill: none;
        stroke: $mountain-teal;
        stroke-width: 2;
        stroke-linecap: round;
        stroke-dasharray: 500;
        stroke-dashoffset: 500;
        animation: mountainDraw 3s ease-out 1s forwards;

        &-back {
          stroke: $mountain-blue;
          opacity: 0.5;
          animation-delay: 1.3s;
        }
      }

      @keyframes mountainDraw {
        to { stroke-dashoffset: 0; }
      }

      .sun {
        fill: none;
        stroke: $gold-accent;
        stroke-width: 2;
        stroke-dasharray: 200;
        stroke-dashoffset: 200;
        animation: sunDraw 2s ease-out 1.5s forwards;
      }

      @keyframes sunDraw {
        to { stroke-dashoffset: 0; }
      }

      .bird {
        fill: none;
        stroke: $ink-gray;
        stroke-width: 1.5;
        stroke-linecap: round;
        opacity: 0;
        animation: birdAppear 0.5s ease-out 2s forwards;

        &-2 {
          animation-delay: 2.2s;
        }
      }

      @keyframes birdAppear {
        to { opacity: 0.6; }
      }

      .cloud {
        fill: none;
        stroke: $ink-gray;
        stroke-width: 1.5;
        opacity: 0.3;
        stroke-dasharray: 100;
        stroke-dashoffset: 100;
        animation: cloudDraw 2s ease-out 1.8s forwards;
      }

      @keyframes cloudDraw {
        to { stroke-dashoffset: 0; }
      }
    }
  }
}

// 右侧表单区
.form-section {
  width: 460px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 40px;

  .paper-card {
    width: 100%;
    min-height: 580px; // 增加卡片高度，防止溢出
    background: white;
    border-radius: 8px;
    padding: 48px 40px 40px;
    position: relative;
    box-shadow: 
      0 6px 30px rgba(0, 0, 0, 0.08),
      0 2px 8px rgba(0, 0, 0, 0.06);
    overflow: visible;
    display: flex;
    flex-direction: column;

    // 纸张纹理
    .paper-texture {
      position: absolute;
      inset: 0;
      background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 400 400' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
      opacity: 0.03;
      pointer-events: none;
    }

    // 角落装饰
    .corner-decoration {
      position: absolute;
      font-size: 20px;
      color: $seal-red;
      opacity: 0.4;

      &.top-left { top: 16px; left: 16px; }
      &.top-right { top: 16px; right: 16px; transform: rotate(90deg); }
      &.bottom-left { bottom: 16px; left: 16px; transform: rotate(-90deg); }
      &.bottom-right { bottom: 16px; right: 16px; transform: rotate(180deg); }
    }
  }

  // 书签式Tab
  .bookmark-tabs {
    position: absolute;
    top: -36px;
    left: 40px;
    display: flex;
    gap: 8px;

    .bookmark {
      position: relative;
      padding: 12px 28px;
      background: $paper-warm;
      cursor: pointer;
      transition: all 0.3s ease;
      clip-path: polygon(0 0, 100% 0, 100% 85%, 50% 100%, 0 85%);

      .bookmark-text {
        font-size: 15px;
        color: $ink-gray;
        font-weight: 500;
        letter-spacing: 2px;
        transition: color 0.3s ease;
      }

      .bookmark-ribbon {
        position: absolute;
        bottom: -12px;
        left: 50%;
        transform: translateX(-50%);
        width: 0;
        height: 0;
        border-left: 8px solid transparent;
        border-right: 8px solid transparent;
        border-top: 12px solid $seal-red;
        opacity: 0;
        transition: opacity 0.3s ease;
      }

      &:hover {
        background: white;
        .bookmark-text { color: $seal-red; }
      }

      &.active {
        background: white;
        padding-top: 16px;
        margin-top: -4px;
        box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.05);

        .bookmark-text {
          color: $seal-red;
          font-weight: 600;
        }

        .bookmark-ribbon {
          opacity: 1;
        }
      }
    }
  }

  // 表单内容
  .form-content {
    position: relative;
    z-index: 1;
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .form-header {
    text-align: center;
    margin-bottom: 32px;
    flex-shrink: 0;

    .form-title {
      font-size: 28px;
      color: $ink-black;
      font-weight: 600;
      margin-bottom: 8px;
      letter-spacing: 4px;
      font-family: 'Noto Serif SC', serif;
    }

    .form-desc {
      font-size: 14px;
      color: $ink-gray;
      letter-spacing: 1px;
    }
  }

  // 文艺表单
  .artistic-form {
    min-height: 360px; // 增加表单区域高度
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .input-flow {
      display: flex;
      flex-direction: column;
      gap: 12px;
      margin-bottom: 16px;
      flex: 1;
      justify-content: center;
    }

    .ink-input-wrapper {
      .input-label {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 14px;
        color: $ink-gray;
        margin-bottom: 10px;
        letter-spacing: 1px;

        .label-icon {
          color: $seal-red;
          font-size: 10px;
        }
      }

      .input-field {
        position: relative;

        .ink-input {
          width: 100%;

          :deep(.el-input__wrapper) {
            background: transparent;
            border: none;
            border-radius: 0;
            box-shadow: none;
            padding: 0;

            .el-input__inner {
              height: 40px;
              font-size: 15px;
              color: $ink-black;
              padding: 0 4px;
              font-family: inherit;

              &::placeholder {
                color: #999;
                font-size: 14px;
              }
            }
          }
        }

        .input-underline {
          position: relative;
          height: 2px;
          margin-top: 4px;

          .underline-base {
            position: absolute;
            inset: 0;
            background: #e0d8d0;
          }

          .underline-focus {
            position: absolute;
            left: 50%;
            right: 50%;
            height: 100%;
            background: $seal-red;
            transition: all 0.4s ease;
          }
        }

        &:focus-within {
          .underline-focus {
            left: 0;
            right: 0;
          }
        }
      }
    }

    // 提交按钮
    .submit-area {
      text-align: center;
      margin-top: 8px;
    }

    .ink-button {
      position: relative;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      padding: 14px 56px;
      background: transparent;
      border: 2px solid $seal-red;
      border-radius: 24px;
      font-size: 16px;
      color: $seal-red;
      font-weight: 500;
      letter-spacing: 3px;
      cursor: pointer;
      overflow: hidden;
      transition: all 0.4s ease;
      font-family: inherit;

      .btn-text {
        position: relative;
        z-index: 2;
        transition: color 0.4s ease;
      }

      .btn-ink {
        position: absolute;
        inset: 0;
        background: $seal-red;
        transform: scaleX(0);
        transform-origin: right;
        transition: transform 0.4s ease;
        z-index: 1;
      }

      .btn-shine {
        position: absolute;
        top: 0;
        left: -100%;
        width: 100%;
        height: 100%;
        background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
        transition: left 0.6s ease;
        z-index: 3;
      }

      &:hover {
        color: white;
        border-color: $seal-red;
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba($seal-red, 0.25);

        .btn-ink {
          transform: scaleX(1);
          transform-origin: left;
        }

        .btn-shine {
          left: 100%;
        }
      }

      &:active {
        transform: translateY(0);
      }

      &.loading {
        opacity: 0.7;
        cursor: not-allowed;
      }
    }
  }

  // 表单底部
  .form-footer-artistic {
    margin-top: auto; // 推到底部
    padding-top: 24px;
    text-align: center;

    .footer-line {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 12px;
      margin-bottom: 12px;

      .line-left,
      .line-right {
        width: 40px;
        height: 1px;
        background: linear-gradient(90deg, transparent, #d0c8c0, transparent);
      }

      .line-text {
        font-size: 13px;
        color: #999;
        letter-spacing: 1px;
      }
    }

    .switch-link {
      display: inline-block;
      position: relative;
      cursor: pointer;

      .link-text {
        font-size: 14px;
        color: $seal-red;
        letter-spacing: 1px;
        transition: color 0.3s ease;
      }

      .link-underline {
        position: absolute;
        bottom: -2px;
        left: 0;
        width: 0;
        height: 1px;
        background: $seal-red;
        transition: width 0.3s ease;
      }

      &:hover {
        .link-text {
          color: darken($seal-red, 10%);
        }
        .link-underline {
          width: 100%;
        }
      }
    }
  }

  // 文艺底部
  .artistic-footer {
    margin-top: 32px;
    text-align: center;
    font-size: 12px;
    color: rgba($ink-black, 0.4);
    letter-spacing: 2px;

    .footer-divider {
      margin: 0 8px;
      color: rgba($ink-black, 0.2);
    }

    .footer-year {
      font-family: 'Noto Serif SC', serif;
    }
  }
}

// 响应式
@media (max-width: 1024px) {
  .poetry-section {
    display: none;
  }

  .form-section {
    width: 100%;
    padding: 20px;

    .paper-card {
      padding: 40px 24px;
    }

    .bookmark-tabs {
      left: 24px;
    }
  }
}
</style>
