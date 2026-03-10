<template>
  <div class="settings-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统设置</span>
        </div>
      </template>

      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本设置 -->
        <el-tab-pane label="基本设置" name="basic">
          <el-form
            ref="basicFormRef"
            :model="basicForm"
            label-width="120px"
            class="settings-form"
          >
            <el-form-item label="系统名称">
              <el-input v-model="basicForm.systemName" placeholder="请输入系统名称" />
            </el-form-item>

            <el-form-item label="系统Logo">
              <el-upload
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :before-upload="beforeLogoUpload"
              >
                <img v-if="basicForm.logo" :src="basicForm.logo" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>

            <el-form-item label="版权信息">
              <el-input v-model="basicForm.copyright" placeholder="请输入版权信息" />
            </el-form-item>

            <el-form-item label="系统公告">
              <el-input
                v-model="basicForm.announcement"
                type="textarea"
                :rows="3"
                placeholder="请输入系统公告内容"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveBasicSettings" :loading="basicLoading">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 安全设置 -->
        <el-tab-pane label="安全设置" name="security">
          <el-form
            ref="securityFormRef"
            :model="securityForm"
            label-width="180px"
            class="settings-form"
          >
            <el-form-item label="登录失败锁定次数">
              <el-input-number v-model="securityForm.loginFailLimit" :min="3" :max="10" />
              <span class="form-tip">连续登录失败超过此次数将锁定账户</span>
            </el-form-item>

            <el-form-item label="密码有效期（天）">
              <el-input-number v-model="securityForm.passwordExpireDays" :min="0" :max="365" />
              <span class="form-tip">0表示永不过期</span>
            </el-form-item>

            <el-form-item label="密码最小长度">
              <el-input-number v-model="securityForm.passwordMinLength" :min="6" :max="20" />
            </el-form-item>

            <el-form-item label="启用验证码">
              <el-switch v-model="securityForm.enableCaptcha" />
            </el-form-item>

            <el-form-item label="强制修改初始密码">
              <el-switch v-model="securityForm.forceChangeInitialPassword" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveSecuritySettings" :loading="securityLoading">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 通知设置 -->
        <el-tab-pane label="通知设置" name="notification">
          <el-form
            ref="notifyFormRef"
            :model="notifyForm"
            label-width="150px"
            class="settings-form"
          >
            <el-form-item label="启用邮件通知">
              <el-switch v-model="notifyForm.enableEmail" />
            </el-form-item>

            <template v-if="notifyForm.enableEmail">
              <el-form-item label="SMTP服务器">
                <el-input v-model="notifyForm.smtpServer" placeholder="如: smtp.qq.com" />
              </el-form-item>

              <el-form-item label="SMTP端口">
                <el-input v-model="notifyForm.smtpPort" placeholder="如: 587" />
              </el-form-item>

              <el-form-item label="发件人邮箱">
                <el-input v-model="notifyForm.senderEmail" placeholder="请输入发件人邮箱" />
              </el-form-item>

              <el-form-item label="邮箱密码">
                <el-input v-model="notifyForm.senderPassword" type="password" placeholder="请输入邮箱密码或授权码" show-password />
              </el-form-item>
            </template>

            <el-divider />

            <el-form-item label="启用短信通知">
              <el-switch v-model="notifyForm.enableSms" />
            </el-form-item>

            <template v-if="notifyForm.enableSms">
              <el-form-item label="短信服务商">
                <el-select v-model="notifyForm.smsProvider" placeholder="选择服务商" style="width: 100%">
                  <el-option label="阿里云" value="aliyun" />
                  <el-option label="腾讯云" value="tencent" />
                </el-select>
              </el-form-item>

              <el-form-item label="AccessKey ID">
                <el-input v-model="notifyForm.smsAccessKey" placeholder="请输入AccessKey ID" />
              </el-form-item>

              <el-form-item label="AccessKey Secret">
                <el-input v-model="notifyForm.smsAccessSecret" type="password" placeholder="请输入AccessKey Secret" show-password />
              </el-form-item>

              <el-form-item label="短信签名">
                <el-input v-model="notifyForm.smsSign" placeholder="请输入短信签名" />
              </el-form-item>
            </template>

            <el-form-item>
              <el-button type="primary" @click="saveNotifySettings" :loading="notifyLoading">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 数据备份 -->
        <el-tab-pane label="数据备份" name="backup">
          <div class="backup-section">
            <h4>自动备份设置</h4>
            <el-form
              ref="backupFormRef"
              :model="backupForm"
              label-width="150px"
              class="settings-form"
            >
              <el-form-item label="启用自动备份">
                <el-switch v-model="backupForm.enableAutoBackup" />
              </el-form-item>

              <template v-if="backupForm.enableAutoBackup">
                <el-form-item label="备份周期">
                  <el-select v-model="backupForm.backupCycle" placeholder="选择备份周期" style="width: 100%">
                    <el-option label="每天" value="daily" />
                    <el-option label="每周" value="weekly" />
                    <el-option label="每月" value="monthly" />
                  </el-select>
                </el-form-item>

                <el-form-item label="备份时间">
                  <el-time-picker
                    v-model="backupForm.backupTime"
                    placeholder="选择备份时间"
                    value-format="HH:mm:ss"
                    style="width: 100%"
                  />
                </el-form-item>

                <el-form-item label="保留备份数量">
                  <el-input-number v-model="backupForm.keepBackupCount" :min="1" :max="30" />
                </el-form-item>
              </template>

              <el-form-item>
                <el-button type="primary" @click="saveBackupSettings" :loading="backupLoading">保存设置</el-button>
                <el-button type="success" @click="handleManualBackup" :loading="manualBackupLoading">立即备份</el-button>
              </el-form-item>
            </el-form>
          </div>

          <el-divider />

          <div class="backup-history">
            <h4>备份历史</h4>
            <el-table :data="backupHistory" stripe style="margin-top: 15px;">
              <el-table-column prop="fileName" label="文件名" min-width="200" />
              <el-table-column prop="size" label="大小" width="100" />
              <el-table-column label="备份时间" width="180">
                <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
              </el-table-column>
              <el-table-column label="操作" width="150" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" link @click="handleDownload(row)">下载</el-button>
                  <el-button type="danger" link @click="handleDeleteBackup(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 操作日志 -->
        <el-tab-pane label="操作日志" name="logs">
          <div class="logs-section">
            <el-form :inline="true" :model="logSearchForm" class="search-form">
              <el-form-item label="操作类型">
                <el-select v-model="logSearchForm.operationType" placeholder="选择类型" clearable>
                  <el-option label="登录" value="LOGIN" />
                  <el-option label="新增" value="CREATE" />
                  <el-option label="修改" value="UPDATE" />
                  <el-option label="删除" value="DELETE" />
                  <el-option label="查询" value="QUERY" />
                </el-select>
              </el-form-item>
              <el-form-item label="日期范围">
                <el-date-picker
                  v-model="logSearchForm.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="YYYY-MM-DD"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleLogSearch">查询</el-button>
                <el-button @click="resetLogSearch">重置</el-button>
              </el-form-item>
            </el-form>

            <el-table :data="logList" v-loading="logLoading" stripe>
              <el-table-column prop="username" label="操作用户" width="120" />
              <el-table-column prop="operation" label="操作内容" min-width="200" />
              <el-table-column prop="ip" label="IP地址" width="130" />
              <el-table-column prop="operationType" label="操作类型" width="100">
                <template #default="{ row }">
                  <el-tag v-if="row.operationType === 'LOGIN'" type="primary">登录</el-tag>
                  <el-tag v-else-if="row.operationType === 'CREATE'" type="success">新增</el-tag>
                  <el-tag v-else-if="row.operationType === 'UPDATE'" type="warning">修改</el-tag>
                  <el-tag v-else-if="row.operationType === 'DELETE'" type="danger">删除</el-tag>
                  <el-tag v-else type="info">查询</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作时间" width="180">
                <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
              </el-table-column>
            </el-table>

            <div class="pagination">
              <el-pagination
                v-model:current-page="logPageNum"
                v-model:page-size="logPageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="logTotal"
                layout="total, sizes, prev, pager, next"
                @size-change="handleLogSizeChange"
                @current-change="handleLogCurrentChange"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

export default {
  name: 'Settings',
  components: {
    Plus
  },
  setup() {
    const activeTab = ref('basic')
    
    // 基本设置
    const basicFormRef = ref(null)
    const basicLoading = ref(false)
    const basicForm = reactive({
      systemName: '养老院管理系统',
      logo: '',
      copyright: '© 2024 养老院管理系统 版权所有',
      announcement: ''
    })

    // 安全设置
    const securityFormRef = ref(null)
    const securityLoading = ref(false)
    const securityForm = reactive({
      loginFailLimit: 5,
      passwordExpireDays: 90,
      passwordMinLength: 6,
      enableCaptcha: true,
      forceChangeInitialPassword: false
    })

    // 通知设置
    const notifyFormRef = ref(null)
    const notifyLoading = ref(false)
    const notifyForm = reactive({
      enableEmail: false,
      smtpServer: '',
      smtpPort: '',
      senderEmail: '',
      senderPassword: '',
      enableSms: false,
      smsProvider: 'aliyun',
      smsAccessKey: '',
      smsAccessSecret: '',
      smsSign: ''
    })

    // 备份设置
    const backupFormRef = ref(null)
    const backupLoading = ref(false)
    const manualBackupLoading = ref(false)
    const backupForm = reactive({
      enableAutoBackup: false,
      backupCycle: 'daily',
      backupTime: '02:00:00',
      keepBackupCount: 7
    })
    const backupHistory = ref([
      { fileName: 'backup_20240301_020000.sql', size: '15.2 MB', createTime: '2024-03-01 02:00:00' },
      { fileName: 'backup_20240229_020000.sql', size: '15.1 MB', createTime: '2024-02-29 02:00:00' },
      { fileName: 'backup_20240228_020000.sql', size: '15.0 MB', createTime: '2024-02-28 02:00:00' }
    ])

    // 日志
    const logLoading = ref(false)
    const logPageNum = ref(1)
    const logPageSize = ref(10)
    const logTotal = ref(100)
    const logSearchForm = reactive({
      operationType: '',
      dateRange: []
    })
    const logList = ref([
      { username: 'admin', operation: '登录系统', ip: '192.168.1.100', operationType: 'LOGIN', createTime: '2024-03-01 10:30:00' },
      { username: 'admin', operation: '新增老人信息: 张三', ip: '192.168.1.100', operationType: 'CREATE', createTime: '2024-03-01 10:35:00' },
      { username: 'admin', operation: '修改员工信息: 李四', ip: '192.168.1.100', operationType: 'UPDATE', createTime: '2024-03-01 10:40:00' },
      { username: 'admin', operation: '删除护理计划: 计划001', ip: '192.168.1.100', operationType: 'DELETE', createTime: '2024-03-01 10:45:00' }
    ])

    const beforeLogoUpload = (file) => {
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG && !isPNG) {
        ElMessage.error('上传Logo图片只能是 JPG 或 PNG 格式!')
        return false
      }
      if (!isLt2M) {
        ElMessage.error('上传Logo图片大小不能超过 2MB!')
        return false
      }
      
      // 这里应该上传图片到服务器，现在只是模拟
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = () => {
        basicForm.logo = reader.result
      }
      return false
    }

    const saveBasicSettings = async () => {
      basicLoading.value = true
      try {
        // 调用保存API
        await new Promise(resolve => setTimeout(resolve, 500))
        ElMessage.success('基本设置保存成功')
      } catch (error) {
        console.error('保存失败:', error)
      } finally {
        basicLoading.value = false
      }
    }

    const saveSecuritySettings = async () => {
      securityLoading.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 500))
        ElMessage.success('安全设置保存成功')
      } catch (error) {
        console.error('保存失败:', error)
      } finally {
        securityLoading.value = false
      }
    }

    const saveNotifySettings = async () => {
      notifyLoading.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 500))
        ElMessage.success('通知设置保存成功')
      } catch (error) {
        console.error('保存失败:', error)
      } finally {
        notifyLoading.value = false
      }
    }

    const saveBackupSettings = async () => {
      backupLoading.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 500))
        ElMessage.success('备份设置保存成功')
      } catch (error) {
        console.error('保存失败:', error)
      } finally {
        backupLoading.value = false
      }
    }

    const handleManualBackup = async () => {
      manualBackupLoading.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 1500))
        ElMessage.success('手动备份成功')
        // 刷新备份历史
      } catch (error) {
        console.error('备份失败:', error)
      } finally {
        manualBackupLoading.value = false
      }
    }

    const handleDownload = (row) => {
      ElMessage.success(`开始下载: ${row.fileName}`)
    }

    const handleDeleteBackup = (row) => {
      ElMessageBox.confirm(`确定要删除备份文件 ${row.fileName} 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        ElMessage.success('删除成功')
        // 刷新列表
      }).catch(() => {})
    }

    const handleLogSearch = () => {
      logPageNum.value = 1
      fetchLogList()
    }

    const resetLogSearch = () => {
      logSearchForm.operationType = ''
      logSearchForm.dateRange = []
      handleLogSearch()
    }

    const fetchLogList = async () => {
      logLoading.value = true
      try {
        // 调用API获取日志列表
        await new Promise(resolve => setTimeout(resolve, 300))
      } catch (error) {
        console.error('获取日志失败:', error)
      } finally {
        logLoading.value = false
      }
    }

    const handleLogSizeChange = (val) => {
      logPageSize.value = val
      fetchLogList()
    }

    const handleLogCurrentChange = (val) => {
      logPageNum.value = val
      fetchLogList()
    }

    // 格式化日期时间
    const formatDateTime = (datetime) => {
      if (!datetime) return '-'
      // 处理数组格式 [2026, 3, 7, 7, 0, 0]
      if (Array.isArray(datetime)) {
        const [year, month, day, hour = 0, minute = 0] = datetime
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`
      }
      // 处理字符串格式
      const date = new Date(datetime)
      if (isNaN(date.getTime())) return '-'
      return date.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }).replace(/\//g, '-')
    }

    onMounted(() => {
      fetchLogList()
    })

    return {
      activeTab,
      basicFormRef,
      basicForm,
      basicLoading,
      securityFormRef,
      securityForm,
      securityLoading,
      notifyFormRef,
      notifyForm,
      notifyLoading,
      backupFormRef,
      backupForm,
      backupLoading,
      manualBackupLoading,
      backupHistory,
      logLoading,
      logPageNum,
      logPageSize,
      logTotal,
      logSearchForm,
      logList,
      beforeLogoUpload,
      saveBasicSettings,
      saveSecuritySettings,
      saveNotifySettings,
      saveBackupSettings,
      handleManualBackup,
      handleDownload,
      handleDeleteBackup,
      handleLogSearch,
      resetLogSearch,
      handleLogSizeChange,
      handleLogCurrentChange,
      formatDateTime
    }
  }
}
</script>

<style scoped lang="scss">
.settings-container {
  .card-header {
    font-weight: bold;
  }

  .settings-form {
    max-width: 600px;
    margin-top: 20px;

    .form-tip {
      margin-left: 10px;
      color: #909399;
      font-size: 12px;
    }
  }

  .avatar-uploader {
    :deep(.el-upload) {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: border-color 0.3s;

      &:hover {
        border-color: #409EFF;
      }
    }

    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 100px;
      height: 100px;
      text-align: center;
      line-height: 100px;
    }

    .avatar {
      width: 100px;
      height: 100px;
      display: block;
    }
  }

  .backup-section,
  .backup-history,
  .logs-section {
    h4 {
      margin: 0 0 15px 0;
      color: #303133;
    }
  }

  .search-form {
    margin-bottom: 20px;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
