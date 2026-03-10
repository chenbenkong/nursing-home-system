const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    port: 8080,
    historyApiFallback: true,
    proxy: {
      '/user': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/elder': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/employee': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/application': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/care-plan': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/dashboard': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/medicine': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/elder-medication': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/medication-record': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/meal-menu': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/elder-meal': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/inventory': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/activity': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/emergency': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/health-record': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/physical-exam': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/family-relation': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/operation-log': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/sys-config': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': require('path').resolve(__dirname, 'src')
      }
    }
  }
})
