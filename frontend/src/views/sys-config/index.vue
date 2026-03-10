<template>
  <div class="sys-config-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="8">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon" style="background: #409EFF;">
            <el-icon><Setting /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value" style="color: #409EFF;">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">配置总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon" style="background: #909399;">
            <el-icon><Lock /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value" style="color: #909399;">{{ statistics.systemCount || 0 }}</div>
            <div class="stat-label">系统配置</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon" style="background: #67c23a;">
            <el-icon><Edit /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value" style="color: #67c23a;">{{ statistics.customCount || 0 }}</div>
            <div class="stat-label">自定义配置</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 操作按钮 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>系统配置列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增配置
          </el-button>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="configKey" label="配置键" min-width="200" />
        <el-table-column prop="configValue" label="配置值" min-width="200" show-overflow-tooltip />
        <el-table-column prop="configType" label="配置类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.configType === 'SYSTEM' ? 'danger' : 'success'">
              {{ row.configType === 'SYSTEM' ? '系统' : '自定义' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="250" show-overflow-tooltip />
        <el-table-column prop="isSystem" label="是否系统" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isSystem === 1 ? 'danger' : 'info'">
              {{ row.isSystem === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="更新时间" width="160" align="center">
          <template #default="{ row }">{{ formatDateTime(row.updateTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.isSystem === 0" link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="配置键" prop="configKey">
          <el-input v-model="form.configKey" placeholder="请输入配置键" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="配置值" prop="configValue">
          <el-input v-model="form.configValue" type="textarea" :rows="3" placeholder="请输入配置值" />
        </el-form-item>
        <el-form-item label="配置类型" prop="configType">
          <el-select v-model="form.configType" placeholder="选择配置类型" style="width: 100%" :disabled="dialogType === 'edit'">
            <el-option label="字符串" value="STRING" />
            <el-option label="数字" value="NUMBER" />
            <el-option label="布尔值" value="BOOLEAN" />
            <el-option label="JSON" value="JSON" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入配置描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getConfigList,
  addConfig,
  updateConfig,
  deleteConfig,
  getConfigStatistics
} from '@/api/config'

// 统计数据
const statistics = ref({})

// 表格数据
const tableData = ref([])
const loading = ref(false)

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogType = ref('add')
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  id: null,
  configKey: '',
  configValue: '',
  configType: 'STRING',
  description: ''
})

const rules = {
  configKey: [{ required: true, message: '请输入配置键', trigger: 'blur' }],
  configValue: [{ required: true, message: '请输入配置值', trigger: 'blur' }],
  configType: [{ required: true, message: '请选择配置类型', trigger: 'change' }]
}

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const res = await getConfigStatistics()
    if (res.code === 200) {
      statistics.value = res.data
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 获取列表数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getConfigList()
    if (res.code === 200) {
      tableData.value = res.data.list
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 新增
const handleAdd = () => {
  dialogType.value = 'add'
  dialogTitle.value = '新增配置'
  Object.keys(form).forEach(key => {
    form[key] = key === 'id' ? null : key === 'configType' ? 'STRING' : ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogType.value = 'edit'
  dialogTitle.value = '编辑配置'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const api = dialogType.value === 'add' ? addConfig : updateConfig
    const res = await api(form)
    if (res.code === 200) {
      ElMessage.success(dialogType.value === 'add' ? '新增成功' : '更新成功')
      dialogVisible.value = false
      fetchData()
      fetchStatistics()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    submitLoading.value = false
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该配置？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteConfig(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchData()
        fetchStatistics()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  fetchStatistics()
  fetchData()
})

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
</script>

<style scoped>
.sys-config-container {
  padding: 20px;
}

.statistics-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
}

.stat-info {
  margin-left: 15px;
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.table-card {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
