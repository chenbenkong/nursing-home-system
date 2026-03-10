<template>
  <div class="activity-container">
    <!-- 文艺动画背景 -->
    <ArtisticBackground />
    
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="活动标题">
          <el-input v-model="searchForm.title" placeholder="请输入活动标题" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="活动类型">
          <el-select v-model="searchForm.activityType" placeholder="选择类型" clearable style="width: 140px">
            <el-option label="娱乐" value="ENTERTAINMENT" />
            <el-option label="体育" value="SPORTS" />
            <el-option label="教育" value="EDUCATION" />
            <el-option label="健康" value="HEALTH" />
            <el-option label="社交" value="SOCIAL" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 140px">
            <el-option label="待进行" value="PENDING" />
            <el-option label="进行中" value="ONGOING" />
            <el-option label="已完成" value="COMPLETED" />
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
          <span>活动列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增活动
          </el-button>
        </div>
      </template>

      <el-table :data="activityList" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="activityNo" label="活动编号" width="140" />
        <el-table-column prop="title" label="活动标题" width="150" />
        <el-table-column prop="activityType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getTypeType(row.activityType)">{{ getTypeLabel(row.activityType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="地点" width="120" />
        <el-table-column prop="startTime" label="开始时间" width="160">
          <template #default="{ row }">{{ formatDateTime(row.startTime) }}</template>
        </el-table-column>
        <el-table-column prop="currentParticipants" label="参与人数" width="120" align="center">
          <template #default="{ row }">{{ row.currentParticipants }}/{{ row.maxParticipants }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 'PENDING'" type="success" link @click="handleStart(row)">开始</el-button>
            <el-button v-if="row.status === 'ONGOING'" type="warning" link @click="handleComplete(row)">完成</el-button>
            <el-button type="info" link @click="handleRegister(row)">报名</el-button>
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

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="活动标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入活动标题" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="活动类型" prop="activityType">
              <el-select v-model="form.activityType" placeholder="选择类型" style="width: 100%">
                <el-option label="娱乐" value="ENTERTAINMENT" />
                <el-option label="体育" value="SPORTS" />
                <el-option label="教育" value="EDUCATION" />
                <el-option label="健康" value="HEALTH" />
                <el-option label="社交" value="SOCIAL" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大人数" prop="maxParticipants">
              <el-input-number v-model="form.maxParticipants" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入活动地点" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" style="width: 100%" value-format="YYYY-MM-DDTHH:mm:ss" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" style="width: 100%" value-format="YYYY-MM-DDTHH:mm:ss" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="活动描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入活动描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog title="活动报名" v-model="registerDialogVisible" width="500px">
      <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" label-width="100px">
        <el-form-item label="活动"><el-input v-model="currentActivity.title" disabled /></el-form-item>
        <el-form-item label="选择老人" prop="elderId">
          <el-select v-model="registerForm.elderId" placeholder="选择老人" style="width: 100%">
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name" :value="elder.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="registerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRegisterSubmit" :loading="registerLoading">确定报名</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { getActivityList, createActivity, updateActivity, deleteActivity, startActivity, completeActivity, registerActivity } from '@/api/activity'
import { getAllElders } from '@/api/elder'
import ArtisticBackground from '@/components/ArtisticBackground.vue'

const loading = ref(false)
const submitLoading = ref(false)
const registerLoading = ref(false)
const activityList = ref([])
const elderList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

const searchForm = reactive({ title: '', activityType: '', status: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const isEdit = ref(false)

const form = reactive({
  id: null, title: '', activityType: '', location: '', startTime: '', endTime: '',
  maxParticipants: 20, currentParticipants: 0, description: '', status: 'PENDING'
})

const rules = {
  title: [{ required: true, message: '请输入活动标题', trigger: 'blur' }],
  activityType: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
}

const registerDialogVisible = ref(false)
const registerFormRef = ref(null)
const currentActivity = ref({})
const registerForm = reactive({ elderId: null })
const registerRules = { elderId: [{ required: true, message: '请选择老人', trigger: 'change' }] }

const typeMap = { ENTERTAINMENT: '娱乐', SPORTS: '体育', EDUCATION: '教育', HEALTH: '健康', SOCIAL: '社交', OTHER: '其他' }
const typeTypeMap = { ENTERTAINMENT: 'success', SPORTS: 'warning', EDUCATION: 'primary', HEALTH: 'danger', SOCIAL: 'info', OTHER: '' }
const statusMap = { PLANNING: '待进行', ONGOING: '进行中', COMPLETED: '已完成', CANCELLED: '已取消' }
const statusTypeMap = { PLANNING: 'info', ONGOING: 'warning', COMPLETED: 'success', CANCELLED: 'danger' }

const getTypeLabel = (type) => typeMap[type] || type
const getTypeType = (type) => typeTypeMap[type] || ''
const getStatusLabel = (status) => statusMap[status] || status
const getStatusType = (status) => statusTypeMap[status] || ''

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

const fetchActivityList = async () => {
  loading.value = true
  try {
    const res = await getActivityList({ pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm })
    if (res.code === 200) { activityList.value = res.data.list; total.value = res.data.total }
  } catch (error) { console.error('获取活动列表失败:', error) }
  finally { loading.value = false }
}

const fetchElderList = async () => {
  try {
    const res = await getAllElders()
    if (res.code === 200) { elderList.value = res.data || [] }
  } catch (error) { console.error('获取老人列表失败:', error) }
}

const handleSearch = () => { pageNum.value = 1; fetchActivityList() }
const handleReset = () => { searchForm.title = ''; searchForm.activityType = ''; searchForm.status = ''; handleSearch() }
const handleSizeChange = (val) => { pageSize.value = val; fetchActivityList() }
const handleCurrentChange = (val) => { pageNum.value = val; fetchActivityList() }

const resetForm = () => {
  form.id = null; form.title = ''; form.activityType = ''; form.location = ''; form.startTime = ''; form.endTime = ''
  form.maxParticipants = 20; form.currentParticipants = 0; form.description = ''; form.status = 'PLANNING'
}

const handleAdd = () => { isEdit.value = false; dialogTitle.value = '新增活动'; resetForm(); dialogVisible.value = true }
const handleEdit = (row) => { isEdit.value = true; dialogTitle.value = '编辑活动'; Object.assign(form, row); dialogVisible.value = true }

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const api = isEdit.value ? updateActivity : createActivity
    const res = await api(form)
    if (res.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; fetchActivityList() }
    else { ElMessage.error(res.message || '操作失败') }
  } catch (error) { ElMessage.error('操作失败') }
  finally { submitLoading.value = false }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除活动"${row.title}"吗？`, '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await deleteActivity(row.id)
        if (res.code === 200) { ElMessage.success('删除成功'); fetchActivityList() }
        else { ElMessage.error(res.message || '删除失败') }
      } catch (error) { ElMessage.error('删除失败') }
    }).catch(() => {})
}

const handleStart = async (row) => {
  try {
    const res = await startActivity(row.id)
    if (res.code === 200) { ElMessage.success('活动已开始'); fetchActivityList() }
    else { ElMessage.error(res.message || '操作失败') }
  } catch (error) { ElMessage.error('操作失败') }
}

const handleComplete = async (row) => {
  try {
    const res = await completeActivity(row.id)
    if (res.code === 200) { ElMessage.success('活动已完成'); fetchActivityList() }
    else { ElMessage.error(res.message || '操作失败') }
  } catch (error) { ElMessage.error('操作失败') }
}

const handleRegister = (row) => { currentActivity.value = row; registerForm.elderId = null; registerDialogVisible.value = true }

const handleRegisterSubmit = async () => {
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return
  const selectedElder = elderList.value.find(e => e.id === registerForm.elderId)
  if (!selectedElder) return
  registerLoading.value = true
  try {
    const res = await registerActivity(currentActivity.value.id, registerForm.elderId, selectedElder.name)
    if (res.code === 200) { ElMessage.success('报名成功'); registerDialogVisible.value = false; fetchActivityList() }
    else { ElMessage.error(res.message || '报名失败') }
  } catch (error) { ElMessage.error('报名失败') }
  finally { registerLoading.value = false }
}

onMounted(() => { fetchActivityList(); fetchElderList() })
</script>

<style scoped>
.activity-container { padding: 20px; }
.search-card { margin-bottom: 20px; }
.search-form { display: flex; flex-wrap: wrap; gap: 10px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
