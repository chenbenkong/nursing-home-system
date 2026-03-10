<template>
  <div class="medication-record-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.todayTotal || 0 }}</div>
            <div class="stat-label">今日待服药</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.todayTaken || 0 }}</div>
            <div class="stat-label">今日已服用</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card red">
          <div class="stat-icon">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.todayPending || 0 }}</div>
            <div class="stat-label">今日未服用</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Close /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.todayMissed || 0 }}</div>
            <div class="stat-label">今日漏服</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="老人姓名">
          <el-input v-model="searchForm.elderName" placeholder="请输入老人姓名" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="药品名称">
          <el-input v-model="searchForm.medicineName" placeholder="请输入药品名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="服药状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 140px">
            <el-option label="待服药" value="PENDING" />
            <el-option label="已服用" value="TAKEN" />
            <el-option label="漏服" value="MISSED" />
            <el-option label="跳过" value="SKIPPED" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="searchForm.date" type="date" placeholder="选择日期" style="width: 150px" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
          <el-button type="success" @click="handleToday">
            <el-icon><Timer /></el-icon>今日任务
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>服药记录列表</span>
          <div>
            <el-button type="warning" @click="handleBatchSkip" :disabled="!selectedRecords.length">
              <el-icon><Close /></el-icon>批量跳过
            </el-button>
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>新增记录
            </el-button>
          </div>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="elderName" label="老人姓名" width="120" align="center" />
        <el-table-column prop="medicineName" label="药品名称" width="150" />
        <el-table-column prop="dosage" label="剂量" width="100" align="center" />
        <el-table-column prop="scheduledTime" label="计划时间" width="150" align="center">
          <template #default="{ row }">
            {{ formatDateTime(row.scheduledTime) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="actualTime" label="实际时间" width="150" align="center">
          <template #default="{ row }">
            {{ formatDateTime(row.actualTime) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executorName" label="执行人" width="100" align="center">
          <template #default="{ row }">
            {{ row.executorName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="result" label="服用结果" width="100" align="center">
          <template #default="{ row }">
            {{ getResultText(row.result) }}
          </template>
        </el-table-column>
        <el-table-column prop="notes" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button v-if="row.status === 'PENDING'" link type="success" @click="handleComplete(row)">确认服用</el-button>
            <el-button v-if="row.status === 'PENDING'" link type="warning" @click="handleSkip(row)">跳过</el-button>
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择老人" prop="elderId">
          <el-select v-model="form.elderId" placeholder="选择老人" filterable style="width: 100%" @change="handleElderChange">
            <el-option v-for="item in elderList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择用药" prop="elderMedicationId">
          <el-select v-model="form.elderMedicationId" placeholder="选择用药记录" filterable style="width: 100%" :disabled="!form.elderId" @change="handleMedicationChange">
            <el-option v-for="item in elderMedicationList" :key="item.id" :label="item.medicineName + ' - ' + item.dosage" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划时间" prop="scheduledTime">
          <el-date-picker v-model="form.scheduledTime" type="datetime" placeholder="选择计划时间" style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.notes" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 确认服用对话框 -->
    <el-dialog v-model="completeDialogVisible" title="确认服药" width="500px" destroy-on-close>
      <el-form ref="completeFormRef" :model="completeForm" :rules="completeRules" label-width="100px">
        <el-form-item label="老人姓名">
          <span>{{ currentRow.elderName }}</span>
        </el-form-item>
        <el-form-item label="药品名称">
          <span>{{ currentRow.medicineName }}</span>
        </el-form-item>
        <el-form-item label="剂量">
          <span>{{ currentRow.dosage }}</span>
        </el-form-item>
        <el-form-item label="服用结果" prop="result">
          <el-select v-model="completeForm.result" placeholder="选择服用结果" style="width: 100%">
            <el-option label="正常服用" value="SUCCESS" />
            <el-option label="拒绝服用" value="REFUSED" />
            <el-option label="服用后呕吐" value="VOMITED" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="completeForm.notes" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="success" @click="handleCompleteSubmit" :loading="completeLoading">确认</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="服药详情" width="600px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="老人姓名">{{ currentRow.elderName }}</el-descriptions-item>
        <el-descriptions-item label="药品名称">{{ currentRow.medicineName }}</el-descriptions-item>
        <el-descriptions-item label="剂量">{{ currentRow.dosage }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentRow.status)">{{ getStatusText(currentRow.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="计划时间">{{ formatDateTime(currentRow.scheduledTime) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="实际时间">{{ formatDateTime(currentRow.actualTime) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="执行人">{{ currentRow.executorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="服用结果">{{ getResultText(currentRow.result) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.notes || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getMedicationRecordList,
  addMedicationRecord,
  deleteMedicationRecord,
  completeMedicationRecord,
  skipMedicationRecord,
  getMedicationRecordStatistics,
  getTodayRecords
} from '@/api/medicine'
import { getAllElders } from '@/api/elder'
import { getActiveMedicationsByElderId } from '@/api/medicine'

// 统计数据
const statistics = ref({})

// 搜索表单
const searchForm = reactive({
  elderName: '',
  medicineName: '',
  status: '',
  date: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 选中的记录
const selectedRecords = ref([])

// 老人列表和用药列表
const elderList = ref([])
const elderMedicationList = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  elderId: null,
  elderMedicationId: null,
  medicineId: null,
  medicineName: '',
  dosage: '',
  scheduledTime: '',
  notes: ''
})

const rules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
  elderMedicationId: [{ required: true, message: '请选择用药记录', trigger: 'change' }],
  scheduledTime: [{ required: true, message: '请选择计划时间', trigger: 'change' }]
}

// 确认服用对话框
const completeDialogVisible = ref(false)
const completeFormRef = ref(null)
const completeLoading = ref(false)
const completeForm = reactive({
  result: 'SUCCESS',
  notes: ''
})
const completeRules = {
  result: [{ required: true, message: '请选择服用结果', trigger: 'change' }]
}

// 查看详情
const viewDialogVisible = ref(false)
const currentRow = ref({})

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const res = await getMedicationRecordStatistics()
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

// 老人选择变化
const handleElderChange = async (val) => {
  form.elderMedicationId = null
  if (val) {
    try {
      const res = await getActiveMedicationsByElderId(val)
      if (res.code === 200) {
        elderMedicationList.value = res.data
      }
    } catch (error) {
      console.error('获取用药记录失败:', error)
    }
  } else {
    elderMedicationList.value = []
  }
}

// 用药选择变化
const handleMedicationChange = (val) => {
  const medication = elderMedicationList.value.find(item => item.id === val)
  if (medication) {
    form.medicineId = medication.medicineId
    form.medicineName = medication.medicineName
    form.dosage = medication.dosage
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
    const res = await getMedicationRecordList(params)
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

// 获取今日记录
const fetchTodayRecords = async () => {
  loading.value = true
  try {
    const res = await getTodayRecords()
    if (res.code === 200) {
      tableData.value = res.data
      total.value = res.data.length
    }
  } catch (error) {
    ElMessage.error('获取今日记录失败')
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

// 今日任务
const handleToday = () => {
  fetchTodayRecords()
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

// 选择变化
const handleSelectionChange = (selection) => {
  selectedRecords.value = selection
}

// 日期时间格式化
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  // 处理数组格式 [2024, 3, 1, 8, 0]
  if (Array.isArray(dateTime)) {
    const [year, month, day, hour, minute] = dateTime
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`
  }
  // 处理字符串格式
  if (typeof dateTime === 'string') {
    return dateTime.replace('T', ' ').substring(0, 16)
  }
  return dateTime
}

// 状态转换
const getStatusText = (status) => {
  const map = {
    'PENDING': '待服药',
    'TAKEN': '已服用',
    'MISSED': '漏服',
    'SKIPPED': '跳过'
  }
  return map[status] || status
}

const getStatusType = (status) => {
  const map = {
    'PENDING': 'warning',
    'TAKEN': 'success',
    'MISSED': 'danger',
    'SKIPPED': 'info'
  }
  return map[status] || ''
}

const getResultText = (result) => {
  const map = {
    'SUCCESS': '正常',
    'REFUSED': '拒绝',
    'VOMITED': '呕吐',
    'OTHER': '其他'
  }
  return map[result] || '-'
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增服药记录'
  Object.keys(form).forEach(key => {
    form[key] = key === 'elderId' || key === 'elderMedicationId' || key === 'medicineId' ? null : ''
  })
  elderMedicationList.value = []
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const res = await addMedicationRecord(form)
    if (res.code === 200) {
      ElMessage.success('新增成功')
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

// 确认服用
const handleComplete = (row) => {
  currentRow.value = row
  completeForm.result = 'SUCCESS'
  completeForm.notes = ''
  completeDialogVisible.value = true
}

// 提交确认服用
const handleCompleteSubmit = async () => {
  const valid = await completeFormRef.value.validate().catch(() => false)
  if (!valid) return

  completeLoading.value = true
  try {
    const res = await completeMedicationRecord(currentRow.value.id, completeForm)
    if (res.code === 200) {
      ElMessage.success('确认成功')
      completeDialogVisible.value = false
      fetchData()
      fetchStatistics()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    completeLoading.value = false
  }
}

// 跳过
const handleSkip = (row) => {
  ElMessageBox.confirm('确认跳过该服药记录？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await skipMedicationRecord(row.id)
      if (res.code === 200) {
        ElMessage.success('已跳过')
        fetchData()
        fetchStatistics()
      } else {
        ElMessage.error(res.message || '操作失败')
      }
    } catch (error) {
      ElMessage.error('操作失败')
    }
  })
}

// 批量跳过
const handleBatchSkip = () => {
  if (!selectedRecords.value.length) {
    ElMessage.warning('请选择要跳过的记录')
    return
  }
  ElMessageBox.confirm(`确认跳过选中的 ${selectedRecords.value.length} 条记录？`, '提示', {
    type: 'warning'
  }).then(async () => {
    let successCount = 0
    for (const row of selectedRecords.value) {
      try {
        if (row.status === 'PENDING') {
          await skipMedicationRecord(row.id)
          successCount++
        }
      } catch (error) {
        console.error('跳过失败:', error)
      }
    }
    ElMessage.success(`成功跳过 ${successCount} 条记录`)
    fetchData()
    fetchStatistics()
  })
}

// 查看
const handleView = (row) => {
  currentRow.value = row
  viewDialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该服药记录？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteMedicationRecord(row.id)
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
.medication-record-container {
  padding: 20px;
}

.statistics-row {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  transition: all 0.3s;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  cursor: pointer;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.1);
  }

  &.blue {
    border-left: 4px solid #409EFF;
    .stat-icon { background: linear-gradient(135deg, #409EFF, #79BBFF); }
    .stat-value { color: #409EFF; }
  }

  &.green {
    border-left: 4px solid #67C23A;
    .stat-icon { background: linear-gradient(135deg, #67C23A, #95D475); }
    .stat-value { color: #67C23A; }
  }

  &.red {
    border-left: 4px solid #F56C6C;
    .stat-icon { background: linear-gradient(135deg, #F56C6C, #FAB6B6); }
    .stat-value { color: #F56C6C; }
  }

  &.orange {
    border-left: 4px solid #E6A23C;
    .stat-icon { background: linear-gradient(135deg, #E6A23C, #F3D19E); }
    .stat-value { color: #E6A23C; }
  }
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;

  .el-icon {
    font-size: 28px;
    color: #fff;
  }
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
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
