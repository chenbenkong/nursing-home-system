<template>
  <div class="meal-container">
    <!-- 文艺动画背景 -->
    <ArtisticBackground />
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><Food /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">菜单总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Sunny /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.breakfastCount || 0 }}</div>
            <div class="stat-label">早餐</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <el-icon><Sunrise /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.lunchCount || 0 }}</div>
            <div class="stat-label">午餐</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card purple">
          <div class="stat-icon">
            <el-icon><Moon /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.dinnerCount || 0 }}</div>
            <div class="stat-label">晚餐</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="菜单名称">
          <el-input v-model="searchForm.menuName" placeholder="请输入菜单名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="餐次">
          <el-select v-model="searchForm.mealType" placeholder="请选择餐次" clearable style="width: 150px">
            <el-option label="早餐" value="BREAKFAST" />
            <el-option label="午餐" value="LUNCH" />
            <el-option label="晚餐" value="DINNER" />
            <el-option label="加餐" value="SNACK" />
          </el-select>
        </el-form-item>
        <el-form-item label="菜单日期">
          <el-date-picker
            v-model="searchForm.menuDate"
            type="date"
            placeholder="请选择日期"
            style="width: 150px"
            value-format="YYYY-MM-DD"
          />
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
          <span>膳食菜单列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增菜单
          </el-button>
        </div>
      </template>

      <el-table :data="mealList" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="menuDate" label="菜单日期" width="120" align="center" />
        <el-table-column prop="menuName" label="菜单名称" width="150" />
        <el-table-column prop="mealType" label="餐次" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getMealTypeType(row.mealType)">{{ getMealTypeLabel(row.mealType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dishes" label="菜品列表" min-width="200">
          <template #default="{ row }">
            <div v-if="row.dishes">
              <el-tag v-for="(dish, index) in parseDishes(row.dishes)" :key="index" size="small" class="dish-tag">
                {{ dish.name }}{{ dish.amount ? '(' + dish.amount + ')' : '' }}
              </el-tag>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="calories" label="热量" width="100" align="center">
          <template #default="{ row }">{{ row.calories || 0 }} 卡</template>
        </el-table-column>
        <el-table-column prop="suitableFor" label="适宜人群" width="120" show-overflow-tooltip />
        <el-table-column label="创建时间" width="160">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
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
          :page-sizes="[5, 10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜单日期" prop="menuDate">
              <el-date-picker
                v-model="form.menuDate"
                type="date"
                placeholder="请选择日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="餐次" prop="mealType">
              <el-select v-model="form.mealType" placeholder="请选择餐次" style="width: 100%">
                <el-option label="早餐" value="BREAKFAST" />
                <el-option label="午餐" value="LUNCH" />
                <el-option label="晚餐" value="DINNER" />
                <el-option label="加餐" value="SNACK" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜品列表" prop="dishes">
          <div v-for="(dish, index) in dishList" :key="index" class="dish-item">
            <el-row :gutter="10">
              <el-col :span="10">
                <el-input v-model="dish.name" placeholder="菜品名称" />
              </el-col>
              <el-col :span="8">
                <el-input v-model="dish.amount" placeholder="份量" />
              </el-col>
              <el-col :span="6">
                <el-button type="danger" link @click="removeDish(index)">删除</el-button>
              </el-col>
            </el-row>
          </div>
          <el-button type="primary" link @click="addDish">
            <el-icon><Plus /></el-icon>添加菜品
          </el-button>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="热量" prop="calories">
              <el-input-number v-model="form.calories" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适宜人群" prop="suitableFor">
              <el-input v-model="form.suitableFor" placeholder="如：普通老人、糖尿病患者等" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="营养信息" prop="nutritionInfo">
          <el-input v-model="form.nutritionInfo" type="textarea" :rows="2" placeholder="请输入营养信息" />
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
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Food, Sunny, Sunrise, Moon } from '@element-plus/icons-vue'
import { getMealMenuList, addMealMenu, updateMealMenu, deleteMealMenu } from '@/api/meal'
import ArtisticBackground from '@/components/ArtisticBackground.vue'

const loading = ref(false)
const submitLoading = ref(false)
const mealList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(5)

// 统计数据
const statistics = ref({
  totalCount: 0,
  breakfastCount: 0,
  lunchCount: 0,
  dinnerCount: 0
})

const searchForm = reactive({ menuName: '', mealType: '', menuDate: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const isEdit = ref(false)

const form = reactive({
  id: null,
  menuDate: '',
  mealType: '',
  menuName: '',
  dishes: '',
  calories: 0,
  suitableFor: '',
  nutritionInfo: ''
})

const dishList = ref([])

const rules = {
  menuDate: [{ required: true, message: '请选择菜单日期', trigger: 'change' }],
  mealType: [{ required: true, message: '请选择餐次', trigger: 'change' }],
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }]
}

const mealTypeMap = { BREAKFAST: '早餐', LUNCH: '午餐', DINNER: '晚餐', SNACK: '加餐' }
const mealTypeTypeMap = { BREAKFAST: 'warning', LUNCH: 'success', DINNER: 'primary', SNACK: 'info' }

const getMealTypeLabel = (type) => mealTypeMap[type] || type
const getMealTypeType = (type) => mealTypeTypeMap[type] || ''

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

const parseDishes = (dishesJson) => {
  try {
    return JSON.parse(dishesJson)
  } catch {
    return []
  }
}

const fetchMealList = async () => {
  loading.value = true
  try {
    const params = { pageNum: pageNum.value, pageSize: pageSize.value }
    if (searchForm.menuName) params.name = searchForm.menuName
    if (searchForm.mealType) params.mealType = searchForm.mealType
    if (searchForm.menuDate) params.menuDate = searchForm.menuDate

    const res = await getMealMenuList(params)
    if (res.code === 200) {
      mealList.value = res.data.list
      total.value = res.data.total
      // 更新统计数据
      calculateStatistics(res.data.list)
    }
  } catch (error) {
    console.error('获取膳食菜单列表失败:', error)
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

// 计算统计数据
const calculateStatistics = (list) => {
  statistics.value.totalCount = list.length
  statistics.value.breakfastCount = list.filter(item => item.mealType === 'BREAKFAST').length
  statistics.value.lunchCount = list.filter(item => item.mealType === 'LUNCH').length
  statistics.value.dinnerCount = list.filter(item => item.mealType === 'DINNER').length
}

const handleSearch = () => { pageNum.value = 1; fetchMealList() }
const handleReset = () => {
  searchForm.menuName = ''
  searchForm.mealType = ''
  searchForm.menuDate = ''
  handleSearch()
}
const handleSizeChange = (val) => { pageSize.value = val; fetchMealList() }
const handleCurrentChange = (val) => { pageNum.value = val; fetchMealList() }

const resetForm = () => {
  form.id = null
  form.menuDate = ''
  form.mealType = ''
  form.menuName = ''
  form.dishes = ''
  form.calories = 0
  form.suitableFor = ''
  form.nutritionInfo = ''
  dishList.value = []
}

const addDish = () => { dishList.value.push({ name: '', amount: '' }) }
const removeDish = (index) => { dishList.value.splice(index, 1) }

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增菜单'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑菜单'
  resetForm()
  Object.assign(form, row)
  dishList.value = parseDishes(row.dishes) || []
  dialogVisible.value = true
}

watch(dishList, (newVal) => {
  form.dishes = JSON.stringify(newVal.filter(d => d.name))
}, { deep: true })

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const api = isEdit.value ? updateMealMenu : addMealMenu
    const res = await api(form)
    if (res.code === 200) {
      ElMessage.success(res.message || '操作成功')
      dialogVisible.value = false
      fetchMealList()
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
  ElMessageBox.confirm(`确定要删除菜单"${row.menuName}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        const res = await deleteMealMenu(row.id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          fetchMealList()
        } else {
          ElMessage.error(res.message || '删除失败')
        }
      } catch (error) {
        console.error('删除失败:', error)
        ElMessage.error('删除失败')
      }
    }).catch(() => {})
}

onMounted(() => { fetchMealList() })
</script>

<style scoped>
.meal-container { padding: 20px; }
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
.dish-tag { margin-right: 5px; margin-bottom: 5px; }
.dish-item { margin-bottom: 10px; }
</style>
