<template>
  <div class="inventory-container">
    <!-- 文艺动画背景 -->
    <ArtisticBackground />
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><Box /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">物资总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.normalCount || 0 }}</div>
            <div class="stat-label">库存正常</div>
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
        <div class="stat-card purple">
          <div class="stat-icon">
            <el-icon><FirstAidKit /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.medicalCount || 0 }}</div>
            <div class="stat-label">医疗物资</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="物资名称">
          <el-input v-model="searchForm.name" placeholder="请输入物资名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.category" placeholder="选择分类" clearable style="width: 140px">
            <el-option label="医疗" value="MEDICAL" />
            <el-option label="护理" value="NURSING" />
            <el-option label="办公" value="OFFICE" />
            <el-option label="食品" value="FOOD" />
            <el-option label="日用品" value="DAILY" />
            <el-option label="其他" value="OTHER" />
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
          <span>物资库存列表</span>
          <div class="header-buttons">
            <el-button type="warning" @click="handleLowStock">
              <el-icon><Warning /></el-icon>低库存预警
            </el-button>
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>新增物资
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="inventoryList" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="itemNo" label="物资编号" width="120" />
        <el-table-column prop="name" label="物资名称" width="150" />
        <el-table-column prop="category" label="分类" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getCategoryType(row.category)">{{ getCategoryLabel(row.category) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" align="center">
          <template #default="{ row }">
            <span :class="{ 'text-danger': row.stock <= row.stockWarning }">{{ row.stock }}</span>
            <el-tag v-if="row.stock <= row.stockWarning" type="danger" size="small" class="ml-2">低</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="80" align="center" />
        <el-table-column prop="price" label="单价" width="100" align="right">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="supplier" label="供应商" width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link @click="handleStockIn(row)">入库</el-button>
            <el-button type="warning" link @click="handleStockOut(row)">出库</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="物资名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入物资名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-select v-model="form.category" placeholder="选择分类" style="width: 100%">
                <el-option label="医疗" value="MEDICAL" />
                <el-option label="护理" value="NURSING" />
                <el-option label="办公" value="OFFICE" />
                <el-option label="食品" value="FOOD" />
                <el-option label="日用品" value="DAILY" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="库存" prop="stock">
              <el-input-number v-model="form.stock" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警值" prop="stockWarning">
              <el-input-number v-model="form.stockWarning" :min="0" style="width: 100%" />
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
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="如：个、箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="供应商" prop="supplier">
          <el-input v-model="form.supplier" placeholder="请输入供应商" />
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

    <!-- 入库对话框 -->
    <el-dialog title="物资入库" v-model="stockInDialogVisible" width="500px">
      <el-form ref="stockInFormRef" :model="stockInForm" :rules="stockInRules" label-width="100px">
        <el-form-item label="物资"><el-input v-model="currentItem.name" disabled /></el-form-item>
        <el-form-item label="当前库存"><el-input v-model="currentItem.stock" disabled /></el-form-item>
        <el-form-item label="入库数量" prop="quantity">
          <el-input-number v-model="stockInForm.quantity" :min="1" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="stockInDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleStockInSubmit" :loading="stockInLoading">确定入库</el-button>
      </template>
    </el-dialog>

    <!-- 出库对话框 -->
    <el-dialog title="物资出库" v-model="stockOutDialogVisible" width="500px">
      <el-form ref="stockOutFormRef" :model="stockOutForm" :rules="stockOutRules" label-width="100px">
        <el-form-item label="物资"><el-input v-model="currentItem.name" disabled /></el-form-item>
        <el-form-item label="当前库存"><el-input v-model="currentItem.stock" disabled /></el-form-item>
        <el-form-item label="关联老人" prop="elderId">
          <el-select v-model="stockOutForm.elderId" placeholder="请选择老人（可选）" clearable filterable style="width: 100%">
            <el-option
              v-for="elder in elderList"
              :key="elder.id"
              :label="elder.name"
              :value="elder.id"
            />
          </el-select>
          <div class="form-tip">选择老人后，会自动生成该老人的物资费用</div>
        </el-form-item>
        <el-form-item label="出库数量" prop="quantity">
          <el-input-number v-model="stockOutForm.quantity" :min="1" :max="currentItem.stock" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="stockOutDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleStockOutSubmit" :loading="stockOutLoading">确定出库</el-button>
      </template>
    </el-dialog>

    <!-- 低库存预警对话框 -->
    <el-dialog title="低库存预警" v-model="lowStockDialogVisible" width="800px">
      <el-alert title="以下物资库存已低于预警值，请及时补充" type="warning" :closable="false" style="margin-bottom: 15px" />
      <el-table :data="lowStockList" border stripe>
        <el-table-column prop="itemNo" label="物资编号" width="120" />
        <el-table-column prop="name" label="物资名称" width="150" />
        <el-table-column prop="stock" label="当前库存" width="100" align="center">
          <template #default="{ row }"><span class="text-danger" style="font-weight: bold">{{ row.stock }}</span></template>
        </el-table-column>
        <el-table-column prop="stockWarning" label="预警值" width="100" align="center" />
        <el-table-column prop="unit" label="单位" width="80" align="center" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Warning, Box, CircleCheck, FirstAidKit } from '@element-plus/icons-vue'
import { getInventoryList, addInventory, updateInventory, deleteInventory, stockIn, stockOut, getLowStockInventories } from '@/api/inventory'
import { getElderList } from '@/api/elder'
import ArtisticBackground from '@/components/ArtisticBackground.vue'

const loading = ref(false)
const submitLoading = ref(false)
const stockInLoading = ref(false)
const stockOutLoading = ref(false)
const inventoryList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 统计数据
const statistics = ref({
  totalCount: 0,
  normalCount: 0,
  lowStockCount: 0,
  medicalCount: 0
})

const searchForm = reactive({ name: '', category: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const isEdit = ref(false)

const form = reactive({ id: null, name: '', category: '', stock: 0, stockWarning: 10, price: 0, unit: '', supplier: '', status: 'NORMAL', remark: '' })
const rules = { name: [{ required: true, message: '请输入物资名称', trigger: 'blur' }], category: [{ required: true, message: '请选择分类', trigger: 'change' }] }

const stockInDialogVisible = ref(false)
const stockInFormRef = ref(null)
const currentItem = ref({})
const stockInForm = reactive({ itemId: null, quantity: 1, purpose: '' })
const stockInRules = { quantity: [{ required: true, message: '请输入入库数量', trigger: 'blur' }] }

const stockOutDialogVisible = ref(false)
const stockOutFormRef = ref(null)
const stockOutForm = reactive({ itemId: null, quantity: 1, elderId: null, purpose: '' })
const stockOutRules = { quantity: [{ required: true, message: '请输入出库数量', trigger: 'blur' }] }

const lowStockDialogVisible = ref(false)
const lowStockList = ref([])

// 老人列表（用于出库关联）
const elderList = ref([])

const categoryMap = { MEDICAL: '医疗', NURSING: '护理', OFFICE: '办公', FOOD: '食品', DAILY: '日用品', OTHER: '其他' }
const categoryTypeMap = { MEDICAL: 'danger', NURSING: 'warning', OFFICE: 'primary', FOOD: 'success', DAILY: 'info', OTHER: '' }
const getCategoryLabel = (category) => categoryMap[category] || category
const getCategoryType = (category) => categoryTypeMap[category] || ''

const fetchInventoryList = async () => {
  loading.value = true
  try {
    const res = await getInventoryList({ pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm })
    if (res.code === 200) { 
      inventoryList.value = res.data.list; 
      total.value = res.data.total
      // 更新统计数据
      calculateStatistics(res.data.list)
    }
  } catch (error) { console.error('获取库存列表失败:', error) }
  finally { loading.value = false }
}

// 计算统计数据
const calculateStatistics = (list) => {
  statistics.value.totalCount = list.length
  statistics.value.normalCount = list.filter(item => item.stock >= item.stockWarning).length
  statistics.value.lowStockCount = list.filter(item => item.stock < item.stockWarning).length
  statistics.value.medicalCount = list.filter(item => item.category === 'MEDICAL').length
}

const handleSearch = () => { pageNum.value = 1; fetchInventoryList() }
const handleReset = () => { searchForm.name = ''; searchForm.category = ''; handleSearch() }
const handleSizeChange = (val) => { pageSize.value = val; fetchInventoryList() }
const handleCurrentChange = (val) => { pageNum.value = val; fetchInventoryList() }

const resetForm = () => { form.id = null; form.name = ''; form.category = ''; form.stock = 0; form.stockWarning = 10; form.price = 0; form.unit = ''; form.supplier = ''; form.status = 'NORMAL'; form.remark = '' }
const handleAdd = () => { isEdit.value = false; dialogTitle.value = '新增物资'; resetForm(); dialogVisible.value = true }
const handleEdit = (row) => { isEdit.value = true; dialogTitle.value = '编辑物资'; Object.assign(form, row); dialogVisible.value = true }

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const api = isEdit.value ? updateInventory : addInventory
    const res = await api(form)
    if (res.code === 200) { ElMessage.success(isEdit.value ? '编辑成功' : '新增成功'); dialogVisible.value = false; fetchInventoryList() }
    else { ElMessage.error(res.message || '操作失败') }
  } catch (error) { ElMessage.error('操作失败') }
  finally { submitLoading.value = false }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除物资"${row.name}"吗？`, '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await deleteInventory(row.id)
        if (res.code === 200) { ElMessage.success('删除成功'); fetchInventoryList() }
        else { ElMessage.error(res.message || '删除失败') }
      } catch (error) { ElMessage.error('删除失败') }
    }).catch(() => {})
}

const handleStockIn = (row) => { currentItem.value = row; stockInForm.itemId = row.id; stockInForm.quantity = 1; stockInDialogVisible.value = true }
const handleStockInSubmit = async () => {
  const valid = await stockInFormRef.value.validate().catch(() => false)
  if (!valid) return
  stockInLoading.value = true
  try {
    const res = await stockIn(stockInForm)
    if (res.code === 200) { ElMessage.success('入库成功'); stockInDialogVisible.value = false; fetchInventoryList() }
    else { ElMessage.error(res.message || '入库失败') }
  } catch (error) { ElMessage.error('入库失败') }
  finally { stockInLoading.value = false }
}

const handleStockOut = (row) => {
  currentItem.value = row
  stockOutForm.itemId = row.id
  stockOutForm.quantity = 1
  stockOutForm.elderId = null
  // 加载老人列表
  loadElderList()
  stockOutDialogVisible.value = true
}

// 加载老人列表
const loadElderList = async () => {
  try {
    const res = await getElderList({ pageNum: 1, pageSize: 1000 })
    if (res.code === 200) {
      elderList.value = res.data.list || res.data || []
    }
  } catch (error) {
    console.error('加载老人列表失败', error)
  }
}
const handleStockOutSubmit = async () => {
  const valid = await stockOutFormRef.value.validate().catch(() => false)
  if (!valid) return
  if (stockOutForm.quantity > currentItem.value.stock) { ElMessage.error('出库数量不能大于当前库存'); return }
  stockOutLoading.value = true
  try {
    const res = await stockOut(stockOutForm)
    if (res.code === 200) { ElMessage.success('出库成功'); stockOutDialogVisible.value = false; fetchInventoryList() }
    else { ElMessage.error(res.message || '出库失败') }
  } catch (error) { ElMessage.error('出库失败') }
  finally { stockOutLoading.value = false }
}

const handleLowStock = async () => {
  try {
    const res = await getLowStockInventories()
    if (res.code === 200) { lowStockList.value = res.data || []; lowStockDialogVisible.value = true }
  } catch (error) { ElMessage.error('获取低库存物资失败') }
}

onMounted(() => { fetchInventoryList() })
</script>

<style scoped>
.inventory-container { padding: 20px; }
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
.header-buttons { display: flex; gap: 10px; }
.pagination-container { margin-top: 20px; display: flex; justify-content: flex-end; }
.text-danger { color: #f56c6c; }
.ml-2 { margin-left: 8px; }
.form-tip { font-size: 12px; color: #909399; margin-top: 5px; }
</style>
