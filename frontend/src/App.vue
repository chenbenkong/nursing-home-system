<template>
  <router-view />
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 黑夜模式 CSS 变量覆盖 */
body.dark-theme {
  --el-bg-color: #1e1e32;
  --el-bg-color-page: #0f0f1a;
  --el-bg-color-overlay: #1e1e32;
  --el-text-color-primary: #ffffff;
  --el-text-color-regular: #e0e0e0;
  --el-text-color-secondary: #a0a0a0;
  --el-text-color-placeholder: #808080;
  --el-border-color: rgba(255, 255, 255, 0.1);
  --el-border-color-light: rgba(255, 255, 255, 0.08);
  --el-fill-color: #2a2a45;
  --el-fill-color-light: #353560;
  --el-fill-color-lighter: #404060;
  --el-fill-color-extra-light: #505070;
  --el-fill-color-dark: #252540;
  --el-fill-color-darker: #1e1e32;
  --el-fill-color-blank: #2a2a45;
}

/* 黑夜模式全局样式 - 用于挂载到 body 的元素 */
body.dark-theme .el-popper {
  background-color: #1e1e32 !important;
  border-color: rgba(255, 255, 255, 0.1) !important;
}

body.dark-theme .el-popper .el-popper__arrow::before {
  background-color: #1e1e32 !important;
  border-color: rgba(255, 255, 255, 0.1) !important;
}

body.dark-theme .el-select-dropdown {
  background-color: #1e1e32 !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
}

body.dark-theme .el-select-dropdown__list {
  background-color: #1e1e32 !important;
}

body.dark-theme .el-select-dropdown__item {
  color: #e0e0e0 !important;
  background-color: transparent !important;
}

body.dark-theme .el-select-dropdown__item:hover,
body.dark-theme .el-select-dropdown__item.hover {
  background-color: #2a2a45 !important;
}

body.dark-theme .el-select-dropdown__item.selected {
  background-color: rgba(64, 158, 255, 0.15) !important;
  color: #409EFF !important;
}

body.dark-theme .el-select-dropdown__empty {
  color: #909399;
}

/* 选择器输入框样式 - 使用更高优先级 */
body.dark-theme .el-select .el-input__wrapper,
body.dark-theme .el-select .el-input.is-focus .el-input__wrapper,
body.dark-theme .el-select:hover .el-input__wrapper {
  background-color: #2a2a45 !important;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.15) inset !important;
}

body.dark-theme .el-select .el-input__inner {
  color: #ffffff !important;
  background-color: transparent !important;
}

body.dark-theme .el-select .el-input__inner::placeholder {
  color: #808080 !important;
}

body.dark-theme .el-select .el-input .el-icon,
body.dark-theme .el-select .el-input__suffix-inner .el-icon {
  color: #a0a0a0 !important;
}

/* 确保选择器容器也是深色 */
body.dark-theme .el-select {
  --el-select-input-color: #ffffff;
  --el-select-input-focus-border-color: #409EFF;
  --el-input-bg-color: #2a2a45;
  --el-input-text-color: #ffffff;
  --el-input-border-color: rgba(255, 255, 255, 0.15);
}

/* 直接针对 el-input 组件 */
body.dark-theme .el-input .el-input__wrapper,
body.dark-theme .el-input.is-focus .el-input__wrapper,
body.dark-theme .el-input:hover .el-input__wrapper {
  background-color: #2a2a45 !important;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.15) inset !important;
}

body.dark-theme .el-input__inner {
  color: #ffffff !important;
  caret-color: #ffffff !important;
  background-color: transparent !important;
}

body.dark-theme .el-input__inner::placeholder {
  color: #808080 !important;
}

/* 分页器中的输入框 */
body.dark-theme .el-pagination .el-input .el-input__wrapper,
body.dark-theme .el-pagination .el-input.is-focus .el-input__wrapper {
  background-color: #2a2a45 !important;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.15) inset !important;
}

body.dark-theme .el-pagination .el-input__inner {
  color: #ffffff !important;
  background-color: transparent !important;
}

/* 搜索表单中的输入框 */
body.dark-theme .search-card .el-input .el-input__wrapper,
body.dark-theme .el-form .el-input .el-input__wrapper {
  background-color: #2a2a45 !important;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.15) inset !important;
}

body.dark-theme .search-card .el-input__inner,
body.dark-theme .el-form .el-input__inner {
  color: #ffffff !important;
}
</style>
