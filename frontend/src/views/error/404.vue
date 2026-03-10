<template>
  <div class="error-container">
    <div class="error-content">
      <h1>404</h1>
      <p>抱歉，您访问的页面不存在</p>
      <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotFound'
}
</script>

<style scoped>
.error-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f0f2f5;
}

.error-content {
  text-align: center;
}

.error-content h1 {
  font-size: 120px;
  color: #409EFF;
  margin-bottom: 20px;
}

.error-content p {
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
}
</style>
