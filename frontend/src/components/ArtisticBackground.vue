<template>
  <div class="artistic-bg" :class="{ 'dark-mode': isDarkMode }">
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
    </div>

    <!-- 下雪动画 - 仅黑夜模式 -->
    <div v-if="isDarkMode" class="snow-container">
      <span v-for="n in 80" :key="'snow-' + n" class="snowflake" :class="'snow-' + n"></span>
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
    
    return { isDarkMode }
  }
}
</script>

<style scoped lang="scss">
// 文艺配色
$mountain-teal: #4a7a7a;
$seal-red-light: #c45c48;
$gold-accent: #b8952e;

.artistic-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
  overflow: hidden;
  pointer-events: none;
  background: transparent;

  // 水彩晕染效果
  .watercolor {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.08;
    animation: watercolorMove 20s ease-in-out infinite;

    &-1 {
      width: 600px;
      height: 600px;
      background: radial-gradient(circle, #409EFF 0%, transparent 70%);
      top: -200px;
      right: -100px;
      animation-delay: 0s;
    }

    &-2 {
      width: 500px;
      height: 500px;
      background: radial-gradient(circle, #67C23A 0%, transparent 70%);
      bottom: -150px;
      left: -100px;
      animation-delay: -7s;
    }

    &-3 {
      width: 400px;
      height: 400px;
      background: radial-gradient(circle, #E6A23C 0%, transparent 70%);
      top: 40%;
      left: 30%;
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
    opacity: 0.06;

    .draw-line {
      fill: none;
      stroke: #000;
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

    .float-item {
      position: absolute;
      width: 20px;
      height: 20px;
      opacity: 0.3;

      &::before {
        content: '🍃';
        font-size: 16px;
        position: absolute;
      }

      @for $i from 1 through 12 {
        &.item-#{$i} {
          left: random(100) * 1%;
          top: -30px;
          animation: floatDown 15s + random(10) * 1s linear infinite;
          animation-delay: random(15) * 1s;
          transform: rotate(random(360) * 1deg);

          &::before {
            content: if($i % 3 == 0, '🌸', if($i % 3 == 1, '🍃', '🌿'));
            font-size: 12px + random(10) * 1px;
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
    10% { opacity: 0.3; }
    90% { opacity: 0.3; }
    100% {
      transform: translateY(110vh) rotate(360deg) translateX(50px);
      opacity: 0;
    }
  }

  // 星空背景
  .night-sky {
    position: absolute;
    inset: 0;
    overflow: hidden;

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
