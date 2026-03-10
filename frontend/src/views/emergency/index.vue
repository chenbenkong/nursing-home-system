<template>
  <div class="emergency-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon" style="background: #f56c6c;">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value" style="color: #f56c6c;">{{ statistics.pendingCount || 0 }}</div>
            <div class="stat-label">待处理事件</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon" style="background: #e6a23c;">
            <el-icon><Loading /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value" style="color: #e6a23c;">{{ statistics.processingCount || 0 }}</div>
            <div class="stat-label">处理中事件</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon" style="background: #67c23a;">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value" style="color: #67c23a;">{{ statistics.resolvedCount || 0 }}</div>
            <div class="stat-label">已解决事件</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon" style="background: #909399;">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value" style="color: #909399;">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">事件总数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="事件类型">
          <el-select v-model="searchForm.eventType" placeholder="选择类型" clearable style="width: 140px">
            <el-option label="跌倒" value="FALL" />
            <el-option label="疾病" value="ILLNESS" />
            <el-option label="受伤" value="INJURY" />
            <el-option label="走失" value="MISSING" />
            <el-option label="火灾" value="FIRE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="紧急程度">
          <el-select v-model="searchForm.severity" placeholder="选择程度" clearable style="width: 140px">
            <el-option label="一般" value="LOW" />
            <el-option label="中等" value="MEDIUM" />
            <el-option label="严重" value="HIGH" />
            <el-option label="危急" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 140px">
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="HANDLING" />
            <el-option label="已解决" value="RESOLVED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="涉及老人">
          <el-input v-model="searchForm.elderName" placeholder="请输入老人姓名" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>紧急事件列表</span>
          <el-button type="danger" @click="handleAdd">
            <el-icon><Plus /></el-icon>上报紧急事件
          </el-button>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="eventType" label="事件类型" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getEventTypeType(row.eventType)">
              {{ getEventTypeText(row.eventType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="severity" label="紧急程度" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getSeverityType(row.severity)" effect="dark">
              {{ getSeverityText(row.severity) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="事件描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="elderName" label="涉及老人" width="120" align="center">
          <template #default="{ row }">{{ row.elderName || '-' }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="上报时间" width="160" align="center">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handlerName" label="处理人" width="100" align="center">
          <template #default="{ row }">
            {{ row.handlerName || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button v-if="row.status === 'PENDING'" link type="warning" @click="handleProcess(row)">处理</el-button>
            <el-button v-if="row.status === 'HANDLING'" link type="success" @click="handleResolve(row)">解决</el-button>
            <el-button v-if="row.status === 'PENDING'" link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="事件类型" prop="eventType">
          <el-select v-model="form.eventType" placeholder="选择事件类型" style="width: 100%">
            <el-option label="跌倒" value="FALL" />
            <el-option label="疾病" value="ILLNESS" />
            <el-option label="受伤" value="INJURY" />
            <el-option label="走失" value="MISSING" />
            <el-option label="火灾" value="FIRE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="紧急程度" prop="severity">
          <el-select v-model="form.severity" placeholder="选择紧急程度" style="width: 100%">
            <el-option label="一般" value="LOW" />
            <el-option label="中等" value="MEDIUM" />
            <el-option label="严重" value="HIGH" />
            <el-option label="危急" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="涉及老人" prop="elderId">
          <el-select v-model="form.elderId" placeholder="选择老人" filterable clearable style="width: 100%">
            <el-option
              v-for="item in elderList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="发生地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入事件发生地点" />
        </el-form-item>
        <el-form-item label="事件描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请详细描述事件情况"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="事件详情"
      width="700px"
      destroy-on-close
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="事件类型">
          <el-tag :type="getEventTypeType(currentRow.eventType)">
            {{ getEventTypeText(currentRow.eventType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="紧急程度">
          <el-tag :type="getSeverityType(currentRow.severity)" effect="dark">
            {{ getSeverityText(currentRow.severity) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="涉及老人">{{ currentRow.elderName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="发生地点">{{ currentRow.location }}</el-descriptions-item>
        <el-descriptions-item label="发生时间">{{ formatDateTime(currentRow.eventTime) }}</el-descriptions-item>
        <el-descriptions-item label="上报时间">{{ formatDateTime(currentRow.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="getStatusType(currentRow.status)">
            {{ getStatusText(currentRow.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人">{{ currentRow.handlerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="事件描述" :span="2">{{ currentRow.description }}</el-descriptions-item>
        <el-descriptions-item v-if="currentRow.measures" label="处理措施" :span="2">{{ currentRow.measures }}</el-descriptions-item>
        <el-descriptions-item v-if="currentRow.result" label="处理结果" :span="2">{{ currentRow.result }}</el-descriptions-item>
        <el-descriptions-item v-if="currentRow.resolveTime" label="解决时间" :span="2">{{ formatDateTime(currentRow.resolveTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 处理事件对话框 -->
    <el-dialog
      v-model="processDialogVisible"
      title="处理紧急事件"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="processFormRef"
        :model="processForm"
        :rules="processRules"
        label-width="100px"
      >
        <el-form-item label="事件描述">
          <div style="color: #606266; padding: 8px 0;">{{ currentRow.description }}</div>
        </el-form-item>
        <el-form-item label="处理措施" prop="measures">
          <el-input
            v-model="processForm.measures"
            type="textarea"
            :rows="4"
            placeholder="请输入处理措施"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleProcessSubmit" :loading="processLoading">开始处理</el-button>
      </template>
    </el-dialog>

    <!-- 解决事件对话框 -->
    <el-dialog
      v-model="resolveDialogVisible"
      title="解决紧急事件"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="resolveFormRef"
        :model="resolveForm"
        :rules="resolveRules"
        label-width="100px"
      >
        <el-form-item label="处理措施">
          <div style="color: #606266; padding: 8px 0;">{{ currentRow.measures }}</div>
        </el-form-item>
        <el-form-item label="处理结果" prop="result">
          <el-input
            v-model="resolveForm.result"
            type="textarea"
            :rows="4"
            placeholder="请输入处理结果"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resolveDialogVisible = false">取消</el-button>
        <el-button type="success" @click="handleResolveSubmit" :loading="resolveLoading">确认解决</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getEmergencyEventList,
  addEmergencyEvent,
  deleteEmergencyEvent,
  startProcess,
  resolveEmergencyEvent,
  getEmergencyStatistics
} from '@/api/emergency'
import { getAllElders } from '@/api/elder'

// 统计数据
const statistics = ref({})

// 搜索表单
const searchForm = reactive({
  eventType: '',
  severity: '',
  status: '',
  elderName: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 老人列表
const elderList = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogType = ref('add')
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  id: null,
  eventType: '',
  severity: '',
  elderId: null,
  location: '',
  description: ''
})

const rules = {
  eventType: [{ required: true, message: '请选择事件类型', trigger: 'change' }],
  severity: [{ required: true, message: '请选择紧急程度', trigger: 'change' }],
  location: [{ required: true, message: '请输入发生地点', trigger: 'blur' }],
  description: [{ required: true, message: '请输入事件描述', trigger: 'blur' }]
}

// 查看详情
const viewDialogVisible = ref(false)
const currentRow = ref({})

// 处理对话框
const processDialogVisible = ref(false)
const processFormRef = ref(null)
const processLoading = ref(false)
const processForm = reactive({
  measures: ''
})
const processRules = {
  measures: [{ required: true, message: '请输入处理措施', trigger: 'blur' }]
}

// 解决对话框
const resolveDialogVisible = ref(false)
const resolveFormRef = ref(null)
const resolveLoading = ref(false)
const resolveForm = reactive({
  result: ''
})
const resolveRules = {
  result: [{ required: true, message: '请输入处理结果', trigger: 'blur' }]
}

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const res = await getEmergencyStatistics()
    if (res.code === 200) {
      statistics.value = res.data
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 获取老人列表
const fetchElderList = async () => {
  try {
    const res = await getAllElders()
    if (res.code === 200) {
      elderList.value = res.data
    }
  } catch (error) {
    console.error('获取老人列表失败:', error)
  }
}

// 获取列表数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      ...searchForm
    }
    const res = await getEmergencyEventList(params)
    if (res.code === 200) {
      tableData.value = res.data.list
      total.value = res.data.total
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pageNum.value = 1
  fetchData()
}

// 重置
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  pageNum.value = 1
  fetchData()
}

// 分页
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  fetchData()
}

// 类型转换
const getEventTypeText = (type) => {
  const map = {
    'FALL': '跌倒',
    'ILLNESS': '疾病',
    'INJURY': '受伤',
    'MISSING': '走失',
    'FIRE': '火灾',
    'OTHER': '其他'
  }
  return map[type] || type
}

const getEventTypeType = (type) => {
  const map = {
    'FALL': 'warning',
    'ILLNESS': 'danger',
    'INJURY': 'warning',
    'MISSING': 'danger',
    'FIRE': 'danger',
    'OTHER': 'info'
  }
  return map[type] || ''
}

const getSeverityText = (severity) => {
  const map = {
    'LOW': '一般',
    'MEDIUM': '中等',
    'HIGH': '严重',
    'CRITICAL': '危急'
  }
  return map[severity] || severity
}

const getSeverityType = (severity) => {
  const map = {
    'LOW': 'info',
    'MEDIUM': 'warning',
    'HIGH': 'danger',
    'CRITICAL': 'danger'
  }
  return map[severity] || ''
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '待处理',
    'HANDLING': '处理中',
    'RESOLVED': '已解决',
    'CLOSED': '已关闭'
  }
  return map[status] || status
}

const getStatusType = (status) => {
  const map = {
    'PENDING': 'danger',
    'HANDLING': 'warning',
    'RESOLVED': 'success',
    'CLOSED': 'info'
  }
  return map[status] || ''
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

// 新增
const handleAdd = () => {
  dialogType.value = 'add'
  dialogTitle.value = '上报紧急事件'
  Object.keys(form).forEach(key => {
    form[key] = key === 'id' || key === 'elderId' ? null : ''
  })
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const res = await addEmergencyEvent(form)
    if (res.code === 200) {
      ElMessage.success('上报成功')
      dialogVisible.value = false
      fetchData()
      fetchStatistics()
    } else {
      ElMessage.error(res.message || '上报失败')
    }
  } catch (error) {
    ElMessage.error('上报失败')
  } finally {
    submitLoading.value = false
  }
}

// 查看
const handleView = (row) => {
  currentRow.value = row
  viewDialogVisible.value = true
}

// 处理
const handleProcess = (row) => {
  currentRow.value = row
  processForm.measures = ''
  processDialogVisible.value = true
}

// 提交处理
const handleProcessSubmit = async () => {
  const valid = await processFormRef.value.validate().catch(() => false)
  if (!valid) return

  processLoading.value = true
  try {
    const res = await startProcess(currentRow.value.id, { measures: processForm.measures })
    if (res.code === 200) {
      ElMessage.success('已开始处理')
      processDialogVisible.value = false
      fetchData()
      fetchStatistics()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    processLoading.value = false
  }
}

// 解决
const handleResolve = (row) => {
  currentRow.value = row
  resolveForm.result = ''
  resolveDialogVisible.value = true
}

// 提交解决
const handleResolveSubmit = async () => {
  const valid = await resolveFormRef.value.validate().catch(() => false)
  if (!valid) return

  resolveLoading.value = true
  try {
    const res = await resolveEmergencyEvent(currentRow.value.id, { result: resolveForm.result })
    if (res.code === 200) {
      ElMessage.success('事件已解决')
      resolveDialogVisible.value = false
      fetchData()
      fetchStatistics()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    resolveLoading.value = false
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该紧急事件记录？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteEmergencyEvent(row.id)
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
  fetchElderList()
  fetchData()
})
</script>

<style scoped>
.emergency-container {
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

.search-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
