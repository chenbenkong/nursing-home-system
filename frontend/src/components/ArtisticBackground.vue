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
    
    <!-- 底部积雪层 - 仅黑夜模式 -->
    <div v-if="isDarkMode" class="snow-ground">
      <div class="snow-layer layer-1"></div>
      <div class="snow-layer layer-2"></div>
      <div class="snow-layer layer-3"></div>
      <div class="snow-piles">
        <span v-for="n in 20" :key="'pile-' + n" class="snow-pile" :class="'pile-' + n"></span>
      </div>
    </div>

    <!-- 下雨动画 - 仅白天模式 -->
    <div v-if="!isDarkMode" class="rain-container">
      <span v-for="n in 100" :key="'rain-' + n" class="raindrop" :class="'rain-' + n"></span>
    </div>
    
    <!-- 底部积水层 - 仅白天模式 -->
    <div v-if="!isDarkMode" class="rain-ground">
      <div class="water-layer layer-1"></div>
      <div class="water-layer layer-2"></div>
      <div class="water-ripples">
        <span v-for="n in 15" :key="'ripple-' + n" class="ripple" :class="'ripple-' + n"></span>
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

  // 下雪动画
  .snow-container {
    position: absolute;
    inset: 0;
    overflow: hidden;
    pointer-events: none;

    .snowflake {
      position: absolute;
      width: 4px;
      height: 4px;
      background: rgba(255, 255, 255, 0.6);
      border-radius: 50%;
      opacity: 0;
      animation: snowfall linear infinite;

      @for $i from 1 through 80 {
        &.snow-#{$i} {
          left: random(100) * 1%;
          animation-delay: random(10) * 0.5s;
          animation-duration: 8s + random(10) * 0.5s;
          opacity: random(8) * 0.1;
          transform: scale(random(5) * 0.2);
        }
      }
    }

    @keyframes snowfall {
      0% {
        transform: translateY(-10px) translateX(0) rotate(0deg);
        opacity: 0;
      }
      10% {
        opacity: 0.8;
      }
      90% {
        opacity: 0.6;
      }
      100% {
        transform: translateY(calc(100vh - 60px)) translateX(20px) rotate(360deg);
        opacity: 0;
      }
    }
  }

  // 底部积雪层
  .snow-ground {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 80px;
    pointer-events: none;
    overflow: hidden;

    .snow-layer {
      position: absolute;
      bottom: 0;
      left: -10%;
      right: -10%;
      background: rgba(255, 255, 255, 0.15);
      border-radius: 50% 50% 0 0;
      animation: snowAccumulate 30s ease-out forwards;

      &.layer-1 {
        height: 20px;
        bottom: 0;
        animation-delay: 0s;
      }

      &.layer-2 {
        height: 35px;
        bottom: 5px;
        left: -5%;
        right: -5%;
        background: rgba(255, 255, 255, 0.12);
        animation-delay: 5s;
      }

      &.layer-3 {
        height: 50px;
        bottom: 10px;
        left: 0;
        right: 0;
        background: rgba(255, 255, 255, 0.08);
        animation-delay: 10s;
      }
    }

    @keyframes snowAccumulate {
      0% {
        transform: translateY(20px);
        opacity: 0;
      }
      100% {
        transform: translateY(0);
        opacity: 1;
      }
    }

    // 雪堆
    .snow-piles {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 60px;

      .snow-pile {
        position: absolute;
        bottom: 0;
        width: 40px;
        height: 30px;
        background: rgba(255, 255, 255, 0.2);
        border-radius: 50% 50% 0 0;
        animation: pileGrow 20s ease-out forwards;

        @for $i from 1 through 20 {
          &.pile-#{$i} {
            left: ($i - 1) * 5% + random(3) * 1%;
            width: 30px + random(30) * 1px;
            height: 20px + random(25) * 1px;
            animation-delay: 5s + $i * 0.3s;
            opacity: 0.1 + random(5) * 0.05;
          }
        }
      }

      @keyframes pileGrow {
        0% {
          transform: scale(0);
          opacity: 0;
        }
        100% {
          transform: scale(1);
          opacity: 0.2;
        }
      }
    }
  }

  // 下雨动画
  .rain-container {
    position: absolute;
    inset: 0;
    overflow: hidden;
    pointer-events: none;

    .raindrop {
      position: absolute;
      width: 2px;
      height: 15px;
      background: linear-gradient(to bottom, transparent, rgba(100, 149, 237, 0.6));
      border-radius: 0 0 2px 2px;
      opacity: 0;
      animation: rainfall linear infinite;

      @for $i from 1 through 100 {
        &.rain-#{$i} {
          left: random(100) * 1%;
          animation-delay: random(20) * 0.1s;
          animation-duration: 0.5s + random(10) * 0.05s;
          opacity: random(6) * 0.1;
          transform: scaleY(random(5) * 0.3 + 0.5);
        }
      }
    }

    @keyframes rainfall {
      0% {
        transform: translateY(-20px);
        opacity: 0;
      }
      10% {
        opacity: 0.6;
      }
      90% {
        opacity: 0.6;
      }
      100% {
        transform: translateY(calc(100vh - 40px));
        opacity: 0;
      }
    }
  }

  // 底部积水层
  .rain-ground {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 60px;
    pointer-events: none;
    overflow: hidden;

    .water-layer {
      position: absolute;
      bottom: 0;
      left: -5%;
      right: -5%;
      background: linear-gradient(to top, rgba(100, 149, 237, 0.15), transparent);
      border-radius: 50% 50% 0 0;
      animation: waterAccumulate 25s ease-out forwards;

      &.layer-1 {
        height: 15px;
        bottom: 0;
        animation-delay: 0s;
      }

      &.layer-2 {
        height: 30px;
        bottom: 5px;
        background: linear-gradient(to top, rgba(100, 149, 237, 0.1), transparent);
        animation-delay: 8s;
      }
    }

    @keyframes waterAccumulate {
      0% {
        transform: translateY(15px);
        opacity: 0;
      }
      100% {
        transform: translateY(0);
        opacity: 1;
      }
    }

    // 水波纹
    .water-ripples {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 40px;

      .ripple {
        position: absolute;
        bottom: 5px;
        width: 20px;
        height: 8px;
        border: 1px solid rgba(100, 149, 237, 0.3);
        border-radius: 50%;
        animation: rippleExpand 3s ease-out infinite;

        @for $i from 1 through 15 {
          &.ripple-#{$i} {
            left: random(90) * 1% + 5%;
            animation-delay: random(30) * 0.2s;
            opacity: 0.3;
          }
        }
      }

      @keyframes rippleExpand {
        0% {
          transform: scale(0.5);
          opacity: 0.5;
        }
        100% {
          transform: scale(2);
          opacity: 0;
        }
      }
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
