<template>
  <div class="elder-meal-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><Food /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">用餐记录总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.fullCount || 0 }}</div>
            <div class="stat-label">正常进食</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.partialCount || 0 }}</div>
            <div class="stat-label">进食较少</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card red">
          <div class="stat-icon">
            <el-icon><CircleClose /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.noneCount || 0 }}</div>
            <div class="stat-label">未进食</div>
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
        <el-form-item label="用餐日期">
          <el-date-picker v-model="searchForm.mealDate" type="date" placeholder="选择日期" style="width: 150px" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="餐次">
          <el-select v-model="searchForm.mealType" placeholder="选择餐次" clearable style="width: 120px">
            <el-option label="早餐" value="BREAKFAST" />
            <el-option label="午餐" value="LUNCH" />
            <el-option label="晚餐" value="DINNER" />
            <el-option label="加餐" value="SNACK" />
          </el-select>
        </el-form-item>
        <el-form-item label="摄入量">
          <el-select v-model="searchForm.intakeAmount" placeholder="选择摄入量" clearable style="width: 140px">
            <el-option label="全部" value="FULL" />
            <el-option label="一半" value="HALF" />
            <el-option label="少量" value="LITTLE" />
            <el-option label="未进食" value="NONE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
          <el-button type="success" @click="handleToday">
            <el-icon><Timer /></el-icon>今日记录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>老人膳食记录</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增膳食记录
          </el-button>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="elderName" label="老人姓名" width="120" align="center" />
        <el-table-column prop="mealDate" label="用餐日期" width="110" align="center" />
        <el-table-column prop="mealType" label="餐次" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="getMealTypeType(row.mealType)">
              {{ getMealTypeText(row.mealType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="foodItems" label="实际食物" min-width="200" show-overflow-tooltip />
        <el-table-column prop="intakeAmount" label="摄入量" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getIntakeType(row.intakeAmount)">
              {{ getIntakeText(row.intakeAmount) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="appetite" label="食欲" width="90" align="center">
          <template #default="{ row }">
            {{ getAppetiteText(row.appetite) }}
          </template>
        </el-table-column>
        <el-table-column prop="specialNeeds" label="特殊需求" width="120" show-overflow-tooltip />
        <el-table-column prop="recordByName" label="记录人" width="100" align="center" />
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
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
            <el-form-item label="用餐日期" prop="mealDate">
              <el-date-picker v-model="form.mealDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="餐次" prop="mealType">
              <el-select v-model="form.mealType" placeholder="选择餐次" style="width: 100%">
                <el-option label="早餐" value="BREAKFAST" />
                <el-option label="午餐" value="LUNCH" />
                <el-option label="晚餐" value="DINNER" />
                <el-option label="加餐" value="SNACK" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单">
              <el-select v-model="form.menuId" placeholder="选择菜单（可选）" filterable clearable style="width: 100%">
                <el-option v-for="item in menuList" :key="item.id" :label="item.menuName" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="实际食物" prop="foodItems">
          <el-input v-model="form.foodItems" type="textarea" :rows="3" placeholder="请输入实际食用的食物" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="摄入量">
              <el-select v-model="form.intakeAmount" placeholder="选择摄入量" style="width: 100%">
                <el-option label="全部" value="FULL" />
                <el-option label="一半" value="HALF" />
                <el-option label="少量" value="LITTLE" />
                <el-option label="未进食" value="NONE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="食欲">
              <el-select v-model="form.appetite" placeholder="选择食欲状况" style="width: 100%">
                <el-option label="良好" value="GOOD" />
                <el-option label="一般" value="NORMAL" />
                <el-option label="较差" value="POOR" />
                <el-option label="无食欲" value="NONE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="特殊需求">
          <el-input v-model="form.specialNeeds" placeholder="如：软食、流食、低盐等" />
        </el-form-item>
        <el-form-item label="过敏食物">
          <el-input v-model="form.allergies" placeholder="请输入过敏食物，多个用逗号分隔" />
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
    <el-dialog v-model="viewDialogVisible" title="膳食详情" width="600px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="老人姓名">{{ currentRow.elderName }}</el-descriptions-item>
        <el-descriptions-item label="用餐日期">{{ formatDate(currentRow.mealDate) }}</el-descriptions-item>
        <el-descriptions-item label="餐次">
          <el-tag :type="getMealTypeType(currentRow.mealType)">{{ getMealTypeText(currentRow.mealType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="摄入量">
          <el-tag :type="getIntakeType(currentRow.intakeAmount)">{{ getIntakeText(currentRow.intakeAmount) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="食欲">{{ getAppetiteText(currentRow.appetite) }}</el-descriptions-item>
        <el-descriptions-item label="记录人">{{ currentRow.recordByName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="实际食物" :span="2">{{ currentRow.foodItems || '-' }}</el-descriptions-item>
        <el-descriptions-item label="特殊需求" :span="2">{{ currentRow.specialNeeds || '-' }}</el-descriptions-item>
        <el-descriptions-item label="过敏食物" :span="2">{{ currentRow.allergies || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Timer, Food, CircleCheck, Warning, CircleClose } from '@element-plus/icons-vue'
import {
  getElderMealList,
  addElderMeal,
  updateElderMeal,
  deleteElderMeal
} from '@/api/meal'
import { getAllElders } from '@/api/elder'
import { getAllMealMenus } from '@/api/meal'

// 搜索表单
const searchForm = reactive({
  elderName: '',
  mealDate: '',
  mealType: '',
  intakeAmount: ''
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
  fullCount: 0,
  partialCount: 0,
  noneCount: 0
})

// 老人列表和菜单列表
const elderList = ref([])
const menuList = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogType = ref('add')
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  id: null,
  elderId: null,
  mealDate: '',
  mealType: '',
  menuId: null,
  foodItems: '',
  intakeAmount: 'FULL',
  appetite: 'NORMAL',
  specialNeeds: '',
  allergies: '',
  remark: ''
})

const rules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
  mealDate: [{ required: true, message: '请选择用餐日期', trigger: 'change' }],
  mealType: [{ required: true, message: '请选择餐次', trigger: 'change' }],
  foodItems: [{ required: true, message: '请输入实际食物', trigger: 'blur' }]
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

// 获取菜单列表
const fetchMenuList = async () => {
  try {
    const res = await getAllMealMenus()
    if (res.code === 200) {
      menuList.value = res.data
    }
  } catch (error) {
    console.error('获取菜单列表失败:', error)
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
    const res = await getElderMealList(params)
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
  statistics.value.fullCount = list.filter(item => item.intakeAmount === 'FULL').length
  statistics.value.partialCount = list.filter(item => item.intakeAmount === 'HALF' || item.intakeAmount === 'LITTLE').length
  statistics.value.noneCount = list.filter(item => item.intakeAmount === 'NONE').length
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

// 今日记录
const handleToday = () => {
  const today = new Date().toISOString().split('T')[0]
  searchForm.mealDate = today
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
const getMealTypeText = (type) => {
  const map = {
    'BREAKFAST': '早餐',
    'LUNCH': '午餐',
    'DINNER': '晚餐',
    'SNACK': '加餐'
  }
  return map[type] || type
}

const getMealTypeType = (type) => {
  const map = {
    'BREAKFAST': 'warning',
    'LUNCH': 'success',
    'DINNER': 'primary',
    'SNACK': 'info'
  }
  return map[type] || ''
}

const getIntakeText = (intake) => {
  const map = {
    'FULL': '全部',
    'HALF': '一半',
    'LITTLE': '少量',
    'NONE': '未进食'
  }
  return map[intake] || intake
}

const getIntakeType = (intake) => {
  const map = {
    'FULL': 'success',
    'HALF': 'warning',
    'LITTLE': 'danger',
    'NONE': 'info'
  }
  return map[intake] || ''
}

const getAppetiteText = (appetite) => {
  const map = {
    'GOOD': '良好',
    'NORMAL': '一般',
    'POOR': '较差',
    'NONE': '无食欲'
  }
  return map[appetite] || '-'
}

// 新增
const handleAdd = () => {
  dialogType.value = 'add'
  dialogTitle.value = '新增膳食记录'
  Object.keys(form).forEach(key => {
    form[key] = key === 'id' || key === 'elderId' || key === 'menuId' ? null : ''
  })
  form.intakeAmount = 'FULL'
  form.appetite = 'NORMAL'
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogType.value = 'edit'
  dialogTitle.value = '编辑膳食记录'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const api = dialogType.value === 'add' ? addElderMeal : updateElderMeal
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
  ElMessageBox.confirm('确认删除该膳食记录？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteElderMeal(row.id)
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
  fetchMenuList()
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
.elder-meal-container {
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
