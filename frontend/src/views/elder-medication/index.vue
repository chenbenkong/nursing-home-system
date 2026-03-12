<template>
  <div class="elder-medication-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><FirstAidKit /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">用药记录总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.activeCount || 0 }}</div>
            <div class="stat-label">进行中用药</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card gray">
          <div class="stat-icon">
            <el-icon><CircleClose /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.completedCount || 0 }}</div>
            <div class="stat-label">已完成用药</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.todayCount || 0 }}</div>
            <div class="stat-label">今日需服药</div>
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
        <el-form-item label="用药状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 140px">
            <el-option label="进行中" value="ACTIVE" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已停止" value="STOPPED" />
          </el-select>
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
          <span>老人用药记录</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增用药记录
          </el-button>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="elderName" label="老人姓名" width="120" align="center" />
        <el-table-column prop="medicineName" label="药品名称" width="150" />
        <el-table-column prop="dosage" label="剂量" width="100" align="center" />
        <el-table-column prop="frequency" label="服用频率" width="120" align="center" />
        <el-table-column label="服用时间" width="150">
          <template #default="{ row }">{{ formatDateTime(row.administrationTime) }}</template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="110" align="center">
          <template #default="{ row }">
            {{ formatDate(row.startDate) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="endDate" label="结束日期" width="110" align="center">
          <template #default="{ row }">
            {{ formatDate(row.endDate) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="prescribedByName" label="开药医生" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 'ACTIVE'" link type="warning" @click="handlePause(row)">暂停</el-button>
            <el-button v-if="row.status === 'STOPPED'" link type="success" @click="handleResume(row)">恢复</el-button>
            <el-button v-if="row.status === 'ACTIVE'" link type="success" @click="handleComplete(row)">完成</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50, 100]"
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
        <el-form-item label="选择老人" prop="elderId">
          <el-select v-model="form.elderId" placeholder="选择老人" filterable style="width: 100%" :disabled="dialogType === 'edit'">
            <el-option
              v-for="item in elderList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="选择药品" prop="medicineId">
          <el-select v-model="form.medicineId" placeholder="选择药品" filterable style="width: 100%" @change="handleMedicineChange">
            <el-option
              v-for="item in medicineList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="剂量" prop="dosage">
          <el-input v-model="form.dosage" placeholder="如：10mg、1片" />
        </el-form-item>
        <el-form-item label="服用频率" prop="frequency">
          <el-select v-model="form.frequency" placeholder="选择服用频率" filterable allow-create style="width: 100%">
            <el-option label="每日1次" value="每日1次" />
            <el-option label="每日2次" value="每日2次" />
            <el-option label="每日3次" value="每日3次" />
            <el-option label="每日4次" value="每日4次" />
            <el-option label="隔日1次" value="隔日1次" />
            <el-option label="每周1次" value="每周1次" />
            <el-option label="必要时" value="必要时" />
          </el-select>
        </el-form-item>
        <el-form-item label="服用时间" prop="administrationTime">
          <el-input v-model="form.administrationTime" placeholder="如：早8点、午12点、晚6点" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期（可选）" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="开药医生">
          <el-input v-model="form.prescribedByName" placeholder="请输入开药医生姓名" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="用药详情" width="600px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="老人姓名">{{ currentRow.elderName }}</el-descriptions-item>
        <el-descriptions-item label="药品名称">{{ currentRow.medicineName }}</el-descriptions-item>
        <el-descriptions-item label="剂量">{{ currentRow.dosage }}</el-descriptions-item>
        <el-descriptions-item label="服用频率">{{ currentRow.frequency }}</el-descriptions-item>
        <el-descriptions-item label="服用时间" :span="2">{{ formatDateTime(currentRow.administrationTime) }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ formatDate(currentRow.startDate) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ formatDate(currentRow.endDate) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="开药医生">{{ currentRow.prescribedByName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentRow.status)">{{ getStatusText(currentRow.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ formatDateTime(currentRow.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getElderMedicationList,
  addElderMedication,
  updateElderMedication,
  deleteElderMedication,
  completeElderMedication,
  pauseElderMedication,
  resumeElderMedication,
  getElderMedicationStatistics
} from '@/api/medicine'
import { getAllElders } from '@/api/elder'
import { getAllMedicines } from '@/api/medicine'

// 统计数据
const statistics = ref({})

// 搜索表单
const searchForm = reactive({
  elderName: '',
  medicineName: '',
  status: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)

// 老人列表和药品列表
const elderList = ref([])
const medicineList = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogType = ref('add')
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  id: null,
  elderId: null,
  medicineId: null,
  medicineName: '',
  dosage: '',
  frequency: '',
  administrationTime: '',
  startDate: '',
  endDate: '',
  prescribedByName: '',
  remark: ''
})

const rules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
  medicineId: [{ required: true, message: '请选择药品', trigger: 'change' }],
  dosage: [{ required: true, message: '请输入剂量', trigger: 'blur' }],
  frequency: [{ required: true, message: '请输入服用频率', trigger: 'blur' }],
  administrationTime: [{ required: true, message: '请输入服用时间', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }]
}

// 查看详情
const viewDialogVisible = ref(false)
const currentRow = ref({})

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const res = await getElderMedicationStatistics()
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

// 获取药品列表
const fetchMedicineList = async () => {
  try {
    const res = await getAllMedicines()
    if (res.code === 200) {
      medicineList.value = res.data
    }
  } catch (error) {
    console.error('获取药品列表失败:', error)
  }
}

// 药品选择变化
const handleMedicineChange = (val) => {
  const medicine = medicineList.value.find(item => item.id === val)
  if (medicine) {
    form.medicineName = medicine.name
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
    const res = await getElderMedicationList(params)
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

// 日期格式化
const formatDate = (date) => {
  if (!date) return ''
  // 处理数组格式 [2026, 3, 26]
  if (Array.isArray(date)) {
    return date.join('-')
  }
  // 处理字符串格式
  if (typeof date === 'string') {
    return date.split('T')[0]
  }
  return date
}

// 日期时间格式化
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

// 状态转换
const getStatusText = (status) => {
  const map = {
    'ACTIVE': '进行中',
    'COMPLETED': '已完成',
    'STOPPED': '已停止'
  }
  return map[status] || status
}

const getStatusType = (status) => {
  const map = {
    'ACTIVE': 'success',
    'COMPLETED': 'info',
    'STOPPED': 'warning'
  }
  return map[status] || ''
}

// 新增
const handleAdd = () => {
  dialogType.value = 'add'
  dialogTitle.value = '新增用药记录'
  Object.keys(form).forEach(key => {
    form[key] = key === 'id' || key === 'elderId' || key === 'medicineId' ? null : ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogType.value = 'edit'
  dialogTitle.value = '编辑用药记录'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const api = dialogType.value === 'add' ? addElderMedication : updateElderMedication
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

// 查看
const handleView = (row) => {
  currentRow.value = row
  viewDialogVisible.value = true
}

// 暂停用药
const handlePause = (row) => {
  ElMessageBox.confirm('确认暂停该用药记录？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await pauseElderMedication(row.id)
      if (res.code === 200) {
        ElMessage.success('已暂停')
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

// 恢复用药
const handleResume = (row) => {
  ElMessageBox.confirm('确认恢复该用药记录？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await resumeElderMedication(row.id)
      if (res.code === 200) {
        ElMessage.success('已恢复')
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

// 完成用药
const handleComplete = (row) => {
  ElMessageBox.confirm('确认标记该用药记录为已完成？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await completeElderMedication(row.id)
      if (res.code === 200) {
        ElMessage.success('已标记为完成')
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

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该用药记录？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteElderMedication(row.id)
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
  fetchMedicineList()
  fetchData()
})
</script>

<style scoped>
.elder-medication-container {
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
  
  &.gray {
    border-left: 4px solid #909399;
    .stat-icon { background: linear-gradient(135deg, #909399, #C0C4CC); }
    .stat-value { color: #909399; }
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
