<template>
  <div class="artistic-bg" :class="{ 'dark-mode': isDarkMode }">
    <!-- 水彩晕染效果 -->
    <div v-for="n in 3" :key="'wc-'+n" class="watercolor" :class="'watercolor-'+n"></div>
    
    <!-- 手绘线条装饰 -->
    <svg class="hand-drawn-lines" viewBox="0 0 1920 1080" preserveAspectRatio="xMidYMid slice">
      <path v-for="(d, i) in linePaths" :key="i" class="draw-line" :d="d" :style="{ animationDelay: i * 0.5 + 's' }" />
    </svg>
    
    <!-- 飘落的叶子/花瓣 -->
    <div class="floating-elements">
      <span v-for="n in 12" :key="n" class="float-item" :class="'item-'+n" :style="getFloatStyle(n)"></span>
    </div>

    <!-- 黑夜模式星空背景 -->
    <div v-if="isDarkMode" class="night-sky">
      <div class="stars">
        <span v-for="n in 50" :key="n" class="star" :class="'star-'+n" :style="getStarStyle(n)"></span>
      </div>
    </div>
  </div>
</template>

<script>
import { inject, ref, watch } from 'vue'

export default {
  name: 'ArtisticBackground',
  setup() {
    const injectedDarkMode = inject('isDarkMode', null)
    const isDarkMode = ref(false)
    
    if (injectedDarkMode) {
      watch(injectedDarkMode, (val) => {
        isDarkMode.value = val
      }, { immediate: true })
    }

    // 手绘线条路径
    const linePaths = [
      'M-100,200 Q400,150 800,300 T1600,200',
      'M-50,600 Q300,500 600,650 T1400,550',
      'M200,-50 Q250,300 150,600 T200,1100',
      'M1600,-30 Q1550,400 1650,700 T1600,1130'
    ]

    // 飘落叶子的emoji
    const leafEmojis = ['🍃', '🌸', '🌿']

    // 获取飘落叶子的样式
    const getFloatStyle = (n) => {
      const emoji = leafEmojis[n % 3]
      const fontSize = 12 + Math.random() * 10
      const duration = 15 + Math.random() * 10
      const delay = Math.random() * 15
      const rotate = Math.random() * 360
      
      return {
        left: `${Math.random() * 100}%`,
        animationDuration: `${duration}s`,
        animationDelay: `${delay}s`,
        transform: `rotate(${rotate}deg)`,
        '--emoji': `"${emoji}"`,
        '--font-size': `${fontSize}px`
      }
    }

    // 获取星星的样式
    const getStarStyle = (n) => ({
      left: `${Math.random() * 100}%`,
      top: `${Math.random() * 100}%`,
      animationDelay: `${Math.random() * 2}s`,
      animationDuration: `${1.5 + Math.random() * 2}s`
    })
    
    return { isDarkMode, linePaths, getFloatStyle, getStarStyle }
  }
}
</script>

<style scoped lang="scss">
// 文艺配色变量
$mountain-teal: #4a7a7a;
$seal-red-light: #c45c48;
$gold-accent: #b8952e;

// 混合器 - 水彩晕染
@mixin watercolor($color, $size, $top, $left, $delay) {
  position: absolute;
  width: $size;
  height: $size;
  background: radial-gradient(circle, $color 0%, transparent 70%);
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.08;
  top: $top;
  left: $left;
  animation: watercolorMove 20s ease-in-out infinite;
  animation-delay: $delay;
}

// 混合器 - 绝对定位填充
@mixin absolute-fill {
  position: absolute;
  inset: 0;
}

.artistic-bg {
  @include absolute-fill;
  z-index: 0;
  overflow: hidden;
  pointer-events: none;

  // 水彩晕染效果
  .watercolor-1 { @include watercolor(#409EFF, 600px, -200px, auto, 0s); right: -100px; }
  .watercolor-2 { @include watercolor(#67C23A, 500px, auto, -100px, -7s); bottom: -150px; }
  .watercolor-3 { @include watercolor(#E6A23C, 400px, 40%, 30%, -14s); }

  @keyframes watercolorMove {
    0%, 100% { transform: translate(0, 0) scale(1); }
    33% { transform: translate(30px, -30px) scale(1.1); }
    66% { transform: translate(-20px, 20px) scale(0.95); }
  }

  // 手绘线条
  .hand-drawn-lines {
    @include absolute-fill;
    opacity: 0.06;

    .draw-line {
      fill: none;
      stroke: #000;
      stroke-width: 1.5;
      stroke-linecap: round;
      stroke-dasharray: 2000;
      stroke-dashoffset: 2000;
      animation: drawLine 8s ease-out forwards;
    }
  }

  @keyframes drawLine {
    to { stroke-dashoffset: 0; }
  }

  // 飘落的叶子/花瓣
  .floating-elements {
    @include absolute-fill;

    .float-item {
      position: absolute;
      width: 20px;
      height: 20px;
      opacity: 0.3;
      top: -30px;
      animation: floatDown linear infinite;

      &::before {
        content: var(--emoji);
        font-size: var(--font-size);
        position: absolute;
      }
    }
  }

  @keyframes floatDown {
    0% { transform: translateY(0) rotate(0deg) translateX(0); opacity: 0; }
    10% { opacity: 0.3; }
    90% { opacity: 0.3; }
    100% { transform: translateY(110vh) rotate(360deg) translateX(50px); opacity: 0; }
  }

  // 星空背景
  .night-sky {
    @include absolute-fill;
    overflow: hidden;

    .stars {
      @include absolute-fill;

      .star {
        position: absolute;
        width: 2px;
        height: 2px;
        background: white;
        border-radius: 50%;
        opacity: 0;
        animation: starTwinkle 2s ease-in-out infinite;
      }
    }
  }

  @keyframes starTwinkle {
    0%, 100% { opacity: 0.3; transform: scale(1); }
    50% { opacity: 1; transform: scale(1.2); }
  }

  // 黑夜模式样式
  &.dark-mode {
    .watercolor {
      opacity: 0.15;
      &-1 { background: radial-gradient(circle, $mountain-teal 0%, transparent 70%); }
      &-2 { background: radial-gradient(circle, $seal-red-light 0%, transparent 70%); }
      &-3 { background: radial-gradient(circle, $gold-accent 0%, transparent 70%); }
    }

    .hand-drawn-lines {
      opacity: 0.08;
      .draw-line { stroke: #fff; }
    }
  }
}
</style>
