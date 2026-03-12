<template>
  <div class="medicine-container">
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
            <div class="stat-label">药品总数</div>
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
            <div class="stat-label">正常药品</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.lowStockCount || 0 }}</div>
            <div class="stat-label">低库存预警</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card red">
          <div class="stat-icon">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.expiredCount || 0 }}</div>
            <div class="stat-label">过期药品</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="药品名称">
          <el-input v-model="searchForm.name" placeholder="请输入药品名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="药品分类">
          <el-select v-model="searchForm.category" placeholder="请选择分类" clearable style="width: 150px">
            <el-option label="心血管类" value="CARDIOVASCULAR" />
            <el-option label="糖尿病类" value="DIABETES" />
            <el-option label="呼吸系统" value="RESPIRATORY" />
            <el-option label="消化系统" value="DIGESTIVE" />
            <el-option label="神经系统" value="NERVOUS" />
            <el-option label="抗生素类" value="ANTIBIOTIC" />
            <el-option label="维生素类" value="VITAMIN" />
            <el-option label="中药类" value="CHINESE" />
            <el-option label="西药类" value="WESTERN" />
            <el-option label="保健品" value="HEALTH_PRODUCT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="正常" value="ACTIVE" />
            <el-option label="禁用" value="DISABLED" />
            <el-option label="过期" value="EXPIRED" />
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
          <span>药品列表</span>
          <div class="header-buttons">
            <el-button type="warning" @click="handleLowStock">
              <el-icon><Warning /></el-icon>低库存预警
            </el-button>
            <el-button type="danger" @click="handleExpired">
              <el-icon><Timer /></el-icon>过期药品
            </el-button>
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>新增药品
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="medicineList" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="medicineNo" label="药品编号" width="140" />
        <el-table-column prop="name" label="药品名称" width="150" />
        <el-table-column prop="category" label="分类" width="120">
          <template #default="{ row }">
            <el-tag :type="getCategoryType(row.category)">{{ getCategoryLabel(row.category) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="specification" label="规格" width="120" />
        <el-table-column prop="manufacturer" label="生产厂家" width="150" show-overflow-tooltip />
        <el-table-column prop="stockQuantity" label="库存" width="100" align="center">
          <template #default="{ row }">
            <span :class="{ 'text-danger': row.stockQuantity <= 10 }">{{ row.stockQuantity }}</span>
            <el-tag v-if="row.stockQuantity <= 10" type="danger" size="small" class="ml-2">低</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="80" align="center" />
        <el-table-column prop="price" label="单价" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="expiryDate" label="有效期至" width="120" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
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
          :page-sizes="[5, 10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="药品名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入药品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="药品分类" prop="category">
              <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
                <el-option label="心血管类" value="CARDIOVASCULAR" />
                <el-option label="糖尿病类" value="DIABETES" />
                <el-option label="呼吸系统" value="RESPIRATORY" />
                <el-option label="消化系统" value="DIGESTIVE" />
                <el-option label="神经系统" value="NERVOUS" />
                <el-option label="抗生素类" value="ANTIBIOTIC" />
                <el-option label="维生素类" value="VITAMIN" />
                <el-option label="中药类" value="CHINESE" />
                <el-option label="西药类" value="WESTERN" />
                <el-option label="保健品" value="HEALTH_PRODUCT" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规格" prop="specification">
              <el-input v-model="form.specification" placeholder="如：0.5mg*30片" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="如：盒、瓶、支" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产厂家" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="请输入生产厂家" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="副作用" prop="sideEffects">
              <el-input v-model="form.sideEffects" placeholder="请输入副作用" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="库存数量" prop="stockQuantity">
              <el-input-number v-model="form.stockQuantity" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批号" prop="batchNo">
              <el-input v-model="form.batchNo" placeholder="请输入批号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单价" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="有效期至" prop="expiryDate">
              <el-date-picker v-model="form.expiryDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="储存条件" prop="storageCondition">
              <el-select v-model="form.storageCondition" placeholder="请选择" style="width: 100%">
                <el-option label="常温" value="常温" />
                <el-option label="阴凉" value="阴凉" />
                <el-option label="冷藏" value="冷藏" />
                <el-option label="冷冻" value="冷冻" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="正常" value="ACTIVE" />
                <el-option label="禁用" value="DISABLED" />
                <el-option label="过期" value="EXPIRED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="用法用量" prop="usageMethod">
          <el-input v-model="form.usageMethod" type="textarea" :rows="2" placeholder="请输入用法用量" />
        </el-form-item>
        <el-form-item label="禁忌" prop="contraindication">
          <el-input v-model="form.contraindication" type="textarea" :rows="2" placeholder="请输入禁忌事项" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 低库存预警对话框 -->
    <el-dialog title="低库存预警" v-model="lowStockDialogVisible" width="800px">
      <el-alert
        title="以下药品库存已低于预警值，请及时补充"
        type="warning"
        :closable="false"
        style="margin-bottom: 15px"
      />
      <el-table :data="lowStockList" border stripe>
        <el-table-column prop="medicineNo" label="药品编号" width="140" />
        <el-table-column prop="name" label="药品名称" width="150" />
        <el-table-column prop="specification" label="规格" width="120" />
        <el-table-column prop="stockQuantity" label="当前库存" width="100" align="center">
          <template #default="{ row }">
            <span class="text-danger" style="font-weight: bold">{{ row.stockQuantity }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="80" align="center" />
      </el-table>
    </el-dialog>

    <!-- 过期药品对话框 -->
    <el-dialog title="过期药品提醒" v-model="expiredDialogVisible" width="800px">
      <el-alert
        title="以下药品已过期或即将过期，请及时处理"
        type="error"
        :closable="false"
        style="margin-bottom: 15px"
      />
      <el-table :data="expiredList" border stripe>
        <el-table-column prop="medicineNo" label="药品编号" width="140" />
        <el-table-column prop="name" label="药品名称" width="150" />
        <el-table-column prop="specification" label="规格" width="120" />
        <el-table-column label="有效期至" width="120">
          <template #default="{ row }">
            <span class="text-danger" style="font-weight: bold">{{ formatDate(row.expiryDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stockQuantity" label="库存" width="100" align="center" />
        <el-table-column prop="unit" label="单位" width="80" align="center" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Warning, Timer, FirstAidKit, CircleCheck } from '@element-plus/icons-vue'
import { getMedicineList, addMedicine, updateMedicine, deleteMedicine, getLowStockMedicines, getExpiredMedicines } from '@/api/medicine'
import ArtisticBackground from '@/components/ArtisticBackground.vue'

const loading = ref(false)
const submitLoading = ref(false)
const medicineList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(5)

// 统计数据
const statistics = ref({
  totalCount: 0,
  activeCount: 0,
  lowStockCount: 0,
  expiredCount: 0
})

const searchForm = reactive({
  name: '',
  category: '',
  status: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const isEdit = ref(false)

const form = reactive({
  id: null,
  name: '',
  category: '',
  specification: '',
  manufacturer: '',
  batchNo: '',
  stockQuantity: 0,
  unit: '',
  price: 0,
  expiryDate: '',
  storageCondition: '',
  usageMethod: '',
  contraindication: '',
  sideEffects: '',
  status: 'ACTIVE',
  remark: ''
})

const rules = {
  name: [{ required: true, message: '请输入药品名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择药品分类', trigger: 'change' }],
  specification: [{ required: true, message: '请输入规格', trigger: 'blur' }],
  unit: [{ required: true, message: '请输入单位', trigger: 'blur' }]
}

const lowStockDialogVisible = ref(false)
const lowStockList = ref([])

const expiredDialogVisible = ref(false)
const expiredList = ref([])

const categoryMap = {
  CARDIOVASCULAR: '心血管类',
  DIABETES: '糖尿病类',
  RESPIRATORY: '呼吸系统',
  DIGESTIVE: '消化系统',
  NERVOUS: '神经系统',
  ANTIBIOTIC: '抗生素类',
  VITAMIN: '维生素类',
  CHINESE: '中药类',
  WESTERN: '西药类',
  HEALTH_PRODUCT: '保健品',
  OTHER: '其他'
}

const categoryTypeMap = {
  CARDIOVASCULAR: 'danger',
  DIABETES: 'warning',
  RESPIRATORY: 'success',
  DIGESTIVE: 'primary',
  NERVOUS: 'info',
  ANTIBIOTIC: 'danger',
  VITAMIN: 'success',
  CHINESE: 'warning',
  WESTERN: 'primary',
  HEALTH_PRODUCT: 'info',
  OTHER: 'info'
}

const statusMap = {
  ACTIVE: '正常',
  DISABLED: '禁用',
  EXPIRED: '过期'
}

const statusTypeMap = {
  ACTIVE: 'success',
  DISABLED: 'info',
  EXPIRED: 'danger'
}

const getCategoryLabel = (category) => categoryMap[category] || category
const getCategoryType = (category) => categoryTypeMap[category] || ''
const getStatusLabel = (status) => statusMap[status] || status
const getStatusType = (status) => statusTypeMap[status] || ''

const fetchMedicineList = async () => {
  loading.value = true
  try {
    const res = await getMedicineList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      ...searchForm
    })
    if (res.code === 200) {
      medicineList.value = res.data.list
      total.value = res.data.total
      // 更新统计数据
      calculateStatistics(res.data.list)
    }
  } catch (error) {
    console.error('获取药品列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 计算统计数据
const calculateStatistics = (list) => {
  statistics.value.totalCount = list.length
  statistics.value.activeCount = list.filter(item => item.status === 'ACTIVE').length
  statistics.value.lowStockCount = list.filter(item => item.stockQuantity < 10).length
  statistics.value.expiredCount = list.filter(item => {
    if (!item.expiryDate) return false
    const expiry = new Date(item.expiryDate)
    return expiry < new Date()
  }).length
}

const handleSearch = () => {
  pageNum.value = 1
  fetchMedicineList()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.category = ''
  searchForm.status = ''
  handleSearch()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchMedicineList()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  fetchMedicineList()
}

const resetForm = () => {
  form.id = null
  form.name = ''
  form.category = ''
  form.specification = ''
  form.manufacturer = ''
  form.batchNo = ''
  form.stockQuantity = 0
  form.unit = ''
  form.price = 0
  form.expiryDate = ''
  form.storageCondition = ''
  form.usageMethod = ''
  form.contraindication = ''
  form.sideEffects = ''
  form.status = 'ACTIVE'
  form.remark = ''
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增药品'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑药品'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const api = isEdit.value ? updateMedicine : addMedicine
    const res = await api(form)
    if (res.code === 200) {
      ElMessage.success(res.message || '操作成功')
      dialogVisible.value = false
      fetchMedicineList()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除药品"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteMedicine(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        fetchMedicineList()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleLowStock = async () => {
  try {
    const res = await getLowStockMedicines()
    if (res.code === 200) {
      lowStockList.value = res.data || []
      lowStockDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取低库存药品失败:', error)
    ElMessage.error('获取低库存药品失败')
  }
}

const handleExpired = async () => {
  try {
    const res = await getExpiredMedicines()
    if (res.code === 200) {
      expiredList.value = res.data || []
      expiredDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取过期药品失败:', error)
    ElMessage.error('获取过期药品失败')
  }
}

onMounted(() => {
  fetchMedicineList()
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
.medicine-container {
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

.stat-card.red {
  border-left: 4px solid #F56C6C;
}

.stat-card.red .stat-icon {
  background: linear-gradient(135deg, #F56C6C, #FAB6B6);
}

.stat-card.red .stat-value {
  color: #F56C6C;
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

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-buttons {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.text-danger {
  color: #f56c6c;
}

.ml-2 {
  margin-left: 8px;
}
</style>
