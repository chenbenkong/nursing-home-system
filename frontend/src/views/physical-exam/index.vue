<template>
  <div class="physical-exam-container">
    <!-- 文艺动画背景 -->
    <ArtisticBackground />
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><FirstAidKit /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">体检记录总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.routineCount || 0 }}</div>
            <div class="stat-label">常规体检</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Star /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.comprehensiveCount || 0 }}</div>
            <div class="stat-label">综合体检</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card purple">
          <div class="stat-icon">
            <el-icon><Target /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.specialCount || 0 }}</div>
            <div class="stat-label">专项体检</div>
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
        <el-form-item label="体检机构">
          <el-input v-model="searchForm.examOrg" placeholder="请输入体检机构" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="体检类型">
          <el-select v-model="searchForm.examType" placeholder="选择类型" clearable style="width: 140px">
            <el-option label="常规体检" value="ROUTINE" />
            <el-option label="综合体检" value="COMPREHENSIVE" />
            <el-option label="专项体检" value="SPECIAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="体检日期">
          <el-date-picker v-model="searchForm.examDate" type="date" placeholder="选择日期" style="width: 150px" value-format="YYYY-MM-DD" />
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
          <span>体检记录列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增体检记录
          </el-button>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="elderName" label="老人姓名" width="120" align="center" />
        <el-table-column prop="examDate" label="体检日期" width="110" align="center" />
        <el-table-column prop="examOrg" label="体检机构" width="150" />
        <el-table-column prop="examType" label="体检类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getExamTypeType(row.examType)">
              {{ getExamTypeText(row.examType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="visionLeft" label="左眼视力" width="90" align="center" />
        <el-table-column prop="visionRight" label="右眼视力" width="90" align="center" />
        <el-table-column prop="hearing" label="听力" width="90" align="center">
          <template #default="{ row }">
            {{ getHearingText(row.hearing) }}
          </template>
        </el-table-column>
        <el-table-column prop="bloodSugarFasting" label="空腹血糖" width="100" align="center">
          <template #default="{ row }">
            {{ row.bloodSugarFasting ? row.bloodSugarFasting + ' mmol/L' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="overallAssessment" label="综合评估" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="选择老人" prop="elderId">
              <el-select v-model="form.elderId" placeholder="选择老人" filterable style="width: 100%" :disabled="dialogType === 'edit'">
                <el-option v-for="item in elderList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体检日期" prop="examDate">
              <el-date-picker v-model="form.examDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体检机构" prop="examOrg">
              <el-input v-model="form.examOrg" placeholder="请输入体检机构" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体检类型" prop="examType">
              <el-select v-model="form.examType" placeholder="选择体检类型" style="width: 100%">
                <el-option label="常规体检" value="ROUTINE" />
                <el-option label="综合体检" value="COMPREHENSIVE" />
                <el-option label="专项体检" value="SPECIAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">主要指标</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="左眼视力">
              <el-input v-model="form.visionLeft" placeholder="如：0.8" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="右眼视力">
              <el-input v-model="form.visionRight" placeholder="如：0.8" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="听力">
              <el-select v-model="form.hearing" placeholder="选择听力状况" style="width: 100%">
                <el-option label="正常" value="NORMAL" />
                <el-option label="轻度减退" value="MILD" />
                <el-option label="中度减退" value="MODERATE" />
                <el-option label="重度减退" value="SEVERE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="心电图">
              <el-input v-model="form.ecgResult" placeholder="心电图检查结果" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="空腹血糖">
              <el-input v-model="form.bloodSugarFasting" placeholder="mmol/L">
                <template #append>mmol/L</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="餐后血糖">
              <el-input v-model="form.bloodSugarPostprandial" placeholder="mmol/L">
                <template #append>mmol/L</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血常规">
              <el-input v-model="form.bloodRoutine" placeholder="血常规检查结果" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="尿常规">
              <el-input v-model="form.urineRoutine" placeholder="尿常规检查结果" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="肝功能">
              <el-input v-model="form.liverFunction" placeholder="肝功能检查结果" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="肾功能">
              <el-input v-model="form.kidneyFunction" placeholder="肾功能检查结果" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血脂">
              <el-input v-model="form.lipidProfile" placeholder="血脂检查结果" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">影像检查</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="B超结果">
              <el-input v-model="form.ultrasound" type="textarea" :rows="2" placeholder="B超检查结果" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="X光结果">
              <el-input v-model="form.xRay" type="textarea" :rows="2" placeholder="X光检查结果" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="CT/MRI">
          <el-input v-model="form.ctMri" type="textarea" :rows="2" placeholder="CT/MRI检查结果" />
        </el-form-item>

        <el-divider content-position="left">综合评估</el-divider>
        <el-form-item label="综合评估">
          <el-input v-model="form.overallAssessment" type="textarea" :rows="3" placeholder="请输入综合评估" />
        </el-form-item>
        <el-form-item label="健康建议">
          <el-input v-model="form.healthSuggestions" type="textarea" :rows="3" placeholder="请输入健康建议" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="体检详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="老人姓名">{{ currentRow.elderName }}</el-descriptions-item>
        <el-descriptions-item label="体检日期">{{ formatDate(currentRow.examDate) }}</el-descriptions-item>
        <el-descriptions-item label="体检机构">{{ currentRow.examOrg }}</el-descriptions-item>
        <el-descriptions-item label="体检类型">
          <el-tag :type="getExamTypeType(currentRow.examType)">{{ getExamTypeText(currentRow.examType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="左眼视力">{{ currentRow.visionLeft || '-' }}</el-descriptions-item>
        <el-descriptions-item label="右眼视力">{{ currentRow.visionRight || '-' }}</el-descriptions-item>
        <el-descriptions-item label="听力">{{ getHearingText(currentRow.hearing) }}</el-descriptions-item>
        <el-descriptions-item label="心电图">{{ currentRow.ecgResult || '-' }}</el-descriptions-item>
        <el-descriptions-item label="空腹血糖">{{ currentRow.bloodSugarFasting ? currentRow.bloodSugarFasting + ' mmol/L' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="餐后血糖">{{ currentRow.bloodSugarPostprandial ? currentRow.bloodSugarPostprandial + ' mmol/L' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="血常规" :span="2">{{ currentRow.bloodRoutine || '-' }}</el-descriptions-item>
        <el-descriptions-item label="尿常规" :span="2">{{ currentRow.urineRoutine || '-' }}</el-descriptions-item>
        <el-descriptions-item label="肝功能" :span="2">{{ currentRow.liverFunction || '-' }}</el-descriptions-item>
        <el-descriptions-item label="肾功能" :span="2">{{ currentRow.kidneyFunction || '-' }}</el-descriptions-item>
        <el-descriptions-item label="血脂" :span="2">{{ currentRow.lipidProfile || '-' }}</el-descriptions-item>
        <el-descriptions-item label="B超结果" :span="2">{{ currentRow.ultrasound || '-' }}</el-descriptions-item>
        <el-descriptions-item label="X光结果" :span="2">{{ currentRow.xRay || '-' }}</el-descriptions-item>
        <el-descriptions-item label="CT/MRI" :span="2">{{ currentRow.ctMri || '-' }}</el-descriptions-item>
        <el-descriptions-item label="综合评估" :span="2">{{ currentRow.overallAssessment || '-' }}</el-descriptions-item>
        <el-descriptions-item label="健康建议" :span="2">{{ currentRow.healthSuggestions || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, FirstAidKit, CircleCheck, Star, Target } from '@element-plus/icons-vue'
import {
  getPhysicalExamList,
  addPhysicalExam,
  updatePhysicalExam,
  deletePhysicalExam
} from '@/api/health'
import ArtisticBackground from '@/components/ArtisticBackground.vue'
import { getAllElders } from '@/api/elder'

// 搜索表单
const searchForm = reactive({
  elderName: '',
  examOrg: '',
  examType: '',
  examDate: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 统计数据
const statistics = ref({
  totalCount: 0,
  routineCount: 0,
  comprehensiveCount: 0,
  specialCount: 0
})

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
  elderId: null,
  examDate: '',
  examOrg: '',
  examType: 'ROUTINE',
  visionLeft: '',
  visionRight: '',
  hearing: null,
  ecgResult: '',
  bloodRoutine: '',
  urineRoutine: '',
  liverFunction: '',
  kidneyFunction: '',
  lipidProfile: '',
  bloodSugarFasting: '',
  bloodSugarPostprandial: '',
  ultrasound: '',
  xRay: '',
  ctMri: '',
  overallAssessment: '',
  healthSuggestions: ''
})

const rules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
  examDate: [{ required: true, message: '请选择体检日期', trigger: 'change' }],
  examOrg: [{ required: true, message: '请输入体检机构', trigger: 'blur' }],
  examType: [{ required: true, message: '请选择体检类型', trigger: 'change' }]
}

// 查看详情
const viewDialogVisible = ref(false)
const currentRow = ref({})

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
    const res = await getPhysicalExamList(params)
    if (res.code === 200) {
      tableData.value = res.data.list
      total.value = res.data.total
      // 更新统计数据
      calculateStatistics(res.data.list)
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 计算统计数据
const calculateStatistics = (list) => {
  statistics.value.totalCount = list.length
  statistics.value.routineCount = list.filter(item => item.examType === 'ROUTINE').length
  statistics.value.comprehensiveCount = list.filter(item => item.examType === 'COMPREHENSIVE').length
  statistics.value.specialCount = list.filter(item => item.examType === 'SPECIAL').length
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
const getExamTypeText = (type) => {
  const map = {
    'ROUTINE': '常规体检',
    'COMPREHENSIVE': '综合体检',
    'SPECIAL': '专项体检'
  }
  return map[type] || type
}

const getExamTypeType = (type) => {
  const map = {
    'ROUTINE': 'success',
    'COMPREHENSIVE': 'primary',
    'SPECIAL': 'warning'
  }
  return map[type] || ''
}

const getHearingText = (hearing) => {
  const map = {
    'NORMAL': '正常',
    'MILD': '轻度减退',
    'MODERATE': '中度减退',
    'SEVERE': '重度减退'
  }
  return map[hearing] || '-'
}

// 新增
const handleAdd = () => {
  dialogType.value = 'add'
  dialogTitle.value = '新增体检记录'
  Object.keys(form).forEach(key => {
    form[key] = key === 'id' || key === 'elderId' || key === 'hearing' ? null : ''
  })
  form.examType = 'ROUTINE'
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogType.value = 'edit'
  dialogTitle.value = '编辑体检记录'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const api = dialogType.value === 'add' ? addPhysicalExam : updatePhysicalExam
    const res = await api(form)
    if (res.code === 200) {
      ElMessage.success(dialogType.value === 'add' ? '新增成功' : '更新成功')
      dialogVisible.value = false
      fetchData()
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

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该体检记录？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deletePhysicalExam(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchData()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

onMounted(() => {
  fetchElderList()
  fetchData()
})

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  // 处理数组格式 [2026, 3, 7]
  if (Array.isArray(date)) {
    const [year, month, day] = date
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
  }
  // 处理字符串格式
  if (typeof date === 'string') {
    return date.split('T')[0]
  }
  return date
}
</script>

<style scoped>
.physical-exam-container {
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
  margin-bottom: 20px;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.1);
}

.stat-card.blue {
  border-left: 4px solid #409EFF;
}

.stat-card.blue .stat-icon {
  background: linear-gradient(135deg, #409EFF, #79BBFF);
}

.stat-card.blue .stat-value {
  color: #409EFF;
}

.stat-card.green {
  border-left: 4px solid #67C23A;
}

.stat-card.green .stat-icon {
  background: linear-gradient(135deg, #67C23A, #95D475);
}

.stat-card.green .stat-value {
  color: #67C23A;
}

.stat-card.orange {
  border-left: 4px solid #E6A23C;
}

.stat-card.orange .stat-icon {
  background: linear-gradient(135deg, #E6A23C, #F3D19E);
}

.stat-card.orange .stat-value {
  color: #E6A23C;
}

.stat-card.purple {
  border-left: 4px solid #8B5CF6;
}

.stat-card.purple .stat-icon {
  background: linear-gradient(135deg, #8B5CF6, #A78BFA);
}

.stat-card.purple .stat-value {
  color: #8B5CF6;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.stat-icon .el-icon {
  font-size: 28px;
  color: #fff;
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
