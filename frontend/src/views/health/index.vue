<template>
  <div class="health-container">
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
            <div class="stat-label">健康记录总数</div>
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
            <div class="stat-label">日常记录</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.emergencyCount || 0 }}</div>
            <div class="stat-label">急诊记录</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card purple">
          <div class="stat-icon">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.followupCount || 0 }}</div>
            <div class="stat-label">随访记录</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="老人姓名">
          <el-input v-model="searchForm.elderName" placeholder="请输入老人姓名" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="记录日期">
          <el-date-picker
            v-model="searchForm.recordDate"
            type="date"
            placeholder="选择日期"
            style="width: 150px"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="记录类型">
          <el-select v-model="searchForm.recordType" placeholder="选择类型" clearable style="width: 120px">
            <el-option label="日常记录" value="ROUTINE" />
            <el-option label="急诊记录" value="EMERGENCY" />
            <el-option label="体检记录" value="CHECKUP" />
            <el-option label="随访记录" value="FOLLOWUP" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>健康档案列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增记录
          </el-button>
        </div>
      </template>

      <el-table :data="healthList" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="elderName" label="老人姓名" width="100" />
        <el-table-column prop="recordDate" label="记录日期" width="110" />
        <el-table-column prop="recordType" label="记录类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getRecordTypeType(row.recordType)">{{ getRecordTypeText(row.recordType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="temperature" label="体温" width="80" align="center">
          <template #default="{ row }">{{ row.temperature ? row.temperature + '°C' : '-' }}</template>
        </el-table-column>
        <el-table-column prop="bloodPressure" label="血压" width="120" align="center">
          <template #default="{ row }">
            {{ row.bloodPressureHigh && row.bloodPressureLow ? row.bloodPressureHigh + '/' + row.bloodPressureLow + 'mmHg' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="heartRate" label="心率" width="80" align="center">
          <template #default="{ row }">{{ row.heartRate ? row.heartRate + '次/分' : '-' }}</template>
        </el-table-column>
        <el-table-column prop="bloodSugar" label="血糖" width="90" align="center">
          <template #default="{ row }">{{ row.bloodSugar ? row.bloodSugar + 'mmol/L' : '-' }}</template>
        </el-table-column>
        <el-table-column prop="diagnosis" label="诊断结果" min-width="150" show-overflow-tooltip />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="800px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="老人" prop="elderId">
              <el-select v-model="form.elderId" placeholder="选择老人" style="width: 100%" filterable>
                <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name" :value="elder.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="记录日期" prop="recordDate">
              <el-date-picker
                v-model="form.recordDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="记录类型" prop="recordType">
              <el-select v-model="form.recordType" placeholder="选择类型" style="width: 100%">
                <el-option label="日常记录" value="ROUTINE" />
                <el-option label="急诊记录" value="EMERGENCY" />
                <el-option label="体检记录" value="CHECKUP" />
                <el-option label="随访记录" value="FOLLOWUP" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医生">
              <el-input v-model="form.doctorName" placeholder="请输入医生姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="体温(°C)">
              <el-input-number v-model="form.temperature" :min="30" :max="45" :precision="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="收缩压">
              <el-input-number v-model="form.bloodPressureHigh" :min="0" :max="300" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="舒张压">
              <el-input-number v-model="form.bloodPressureLow" :min="0" :max="200" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="心率(次/分)">
              <el-input-number v-model="form.heartRate" :min="0" :max="200" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="呼吸频率">
              <el-input-number v-model="form.respiratoryRate" :min="0" :max="60" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="血糖(mmol/L)">
              <el-input-number v-model="form.bloodSugar" :min="0" :max="30" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体重(kg)">
              <el-input-number v-model="form.weight" :min="0" :max="200" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身高(cm)">
              <el-input-number v-model="form.height" :min="0" :max="250" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="主诉">
          <el-input v-model="form.chiefComplaint" type="textarea" :rows="2" placeholder="请输入主诉" />
        </el-form-item>
        <el-form-item label="现病史">
          <el-input v-model="form.presentIllness" type="textarea" :rows="2" placeholder="请输入现病史" />
        </el-form-item>
        <el-form-item label="体格检查">
          <el-input v-model="form.physicalExam" type="textarea" :rows="2" placeholder="请输入体格检查结果" />
        </el-form-item>
        <el-form-item label="诊断结果">
          <el-input v-model="form.diagnosis" type="textarea" :rows="2" placeholder="请输入诊断结果" />
        </el-form-item>
        <el-form-item label="治疗方案">
          <el-input v-model="form.treatmentPlan" type="textarea" :rows="2" placeholder="请输入治疗方案" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
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
import { Search, Refresh, Plus, FirstAidKit, CircleCheck, Warning, Calendar } from '@element-plus/icons-vue'
import { getHealthRecordList, addHealthRecord, updateHealthRecord, deleteHealthRecord } from '@/api/health'
import { getAllElders } from '@/api/elder'
import ArtisticBackground from '@/components/ArtisticBackground.vue'

const loading = ref(false)
const submitLoading = ref(false)
const healthList = ref([])
const elderList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 统计数据
const statistics = ref({
  totalCount: 0,
  routineCount: 0,
  emergencyCount: 0,
  followupCount: 0
})

const searchForm = reactive({ elderName: '', recordDate: '', recordType: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const isEdit = ref(false)

const form = reactive({
  id: null,
  elderId: null,
  recordDate: '',
  recordType: 'ROUTINE',
  temperature: null,
  bloodPressureHigh: null,
  bloodPressureLow: null,
  heartRate: null,
  respiratoryRate: null,
  bloodSugar: null,
  weight: null,
  height: null,
  chiefComplaint: '',
  presentIllness: '',
  physicalExam: '',
  diagnosis: '',
  treatmentPlan: '',
  doctorName: '',
  remark: ''
})

const rules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
  recordDate: [{ required: true, message: '请选择记录日期', trigger: 'change' }],
  recordType: [{ required: true, message: '请选择记录类型', trigger: 'change' }]
}

const recordTypeMap = {
  'ROUTINE': '日常记录',
  'EMERGENCY': '急诊记录',
  'CHECKUP': '体检记录',
  'FOLLOWUP': '随访记录'
}

const recordTypeTypeMap = {
  'ROUTINE': 'success',
  'EMERGENCY': 'danger',
  'CHECKUP': 'primary',
  'FOLLOWUP': 'warning'
}

const getRecordTypeText = (type) => recordTypeMap[type] || type
const getRecordTypeType = (type) => recordTypeTypeMap[type] || ''

const fetchHealthList = async () => {
  loading.value = true
  try {
    const res = await getHealthRecordList({ pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm })
    if (res.code === 200) {
      healthList.value = res.data.list
      total.value = res.data.total
      // 更新统计数据
      calculateStatistics(res.data.list)
    }
  } catch (error) {
    console.error('获取健康档案失败:', error)
  } finally {
    loading.value = false
  }
}

// 计算统计数据
const calculateStatistics = (list) => {
  statistics.value.totalCount = list.length
  statistics.value.routineCount = list.filter(item => item.recordType === 'ROUTINE').length
  statistics.value.emergencyCount = list.filter(item => item.recordType === 'EMERGENCY').length
  statistics.value.followupCount = list.filter(item => item.recordType === 'FOLLOWUP').length
}

const fetchElderList = async () => {
  try {
    const res = await getAllElders()
    if (res.code === 200) { elderList.value = res.data || [] }
  } catch (error) { console.error('获取老人列表失败:', error) }
}

const handleSearch = () => { pageNum.value = 1; fetchHealthList() }
const handleReset = () => {
  searchForm.elderName = ''
  searchForm.recordDate = ''
  searchForm.recordType = ''
  handleSearch()
}
const handleSizeChange = (val) => { pageSize.value = val; fetchHealthList() }
const handleCurrentChange = (val) => { pageNum.value = val; fetchHealthList() }

const resetForm = () => {
  form.id = null
  form.elderId = null
  form.recordDate = ''
  form.recordType = 'ROUTINE'
  form.temperature = null
  form.bloodPressureHigh = null
  form.bloodPressureLow = null
  form.heartRate = null
  form.respiratoryRate = null
  form.bloodSugar = null
  form.weight = null
  form.height = null
  form.chiefComplaint = ''
  form.presentIllness = ''
  form.physicalExam = ''
  form.diagnosis = ''
  form.treatmentPlan = ''
  form.doctorName = ''
  form.remark = ''
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增记录'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑记录'
  resetForm()
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  const selectedElder = elderList.value.find(e => e.id === form.elderId)
  if (selectedElder) form.elderName = selectedElder.name

  submitLoading.value = true
  try {
    const api = isEdit.value ? updateHealthRecord : addHealthRecord
    const res = await api(form)
    if (res.code === 200) {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      fetchHealthList()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除该健康记录吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        const res = await deleteHealthRecord(row.id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          fetchHealthList()
        } else {
          ElMessage.error(res.message || '删除失败')
        }
      } catch (error) {
        ElMessage.error('删除失败')
      }
    }).catch(() => {})
}

onMounted(() => { fetchHealthList(); fetchElderList() })
</script>

<style scoped>
.health-container { padding: 20px; }
.statistics-row { margin-bottom: 20px; }
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
.stat-card.blue { border-left: 4px solid #409EFF; }
.stat-card.blue .stat-icon { background: linear-gradient(135deg, #409EFF, #79BBFF); }
.stat-card.blue .stat-value { color: #409EFF; }
.stat-card.green { border-left: 4px solid #67C23A; }
.stat-card.green .stat-icon { background: linear-gradient(135deg, #67C23A, #95D475); }
.stat-card.green .stat-value { color: #67C23A; }
.stat-card.orange { border-left: 4px solid #E6A23C; }
.stat-card.orange .stat-icon { background: linear-gradient(135deg, #E6A23C, #F3D19E); }
.stat-card.orange .stat-value { color: #E6A23C; }
.stat-card.purple { border-left: 4px solid #8B5CF6; }
.stat-card.purple .stat-icon { background: linear-gradient(135deg, #8B5CF6, #A78BFA); }
.stat-card.purple .stat-value { color: #8B5CF6; }
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}
.stat-icon .el-icon { font-size: 28px; color: #fff; }
.stat-info { flex: 1; }
.stat-value { font-size: 28px; font-weight: 600; line-height: 1; margin-bottom: 8px; }
.stat-label { font-size: 14px; color: #909399; }
.search-card { margin-bottom: 20px; }
.search-form { display: flex; flex-wrap: wrap; gap: 10px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
