<template>
  <div class="care-plan-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><FirstAidKit /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">护理计划总数</div>
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
            <div class="stat-label">进行中</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.pausedCount || 0 }}</div>
            <div class="stat-label">已暂停</div>
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
            <div class="stat-label">已完成</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="计划名称">
          <el-input
            v-model="searchForm.planName"
            placeholder="请输入计划名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="老人姓名">
          <el-input
            v-model="searchForm.elderName"
            placeholder="请输入老人姓名"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="护理等级">
          <el-select v-model="searchForm.careLevel" placeholder="选择等级" clearable style="width: 130px">
            <el-option label="一级" value="LEVEL1" />
            <el-option label="二级" value="LEVEL2" />
            <el-option label="三级" value="LEVEL3" />
            <el-option label="四级" value="LEVEL4" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 130px">
            <el-option label="进行中" value="ACTIVE" />
            <el-option label="已暂停" value="PAUSED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>查询
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>护理计划列表</span>
          <el-button type="primary" @click="handleAdd">新增计划</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="planNo" label="计划编号" width="120" />
        <el-table-column prop="planName" label="计划名称" width="150" />
        <el-table-column prop="elderName" label="老人姓名" width="100" />
        <el-table-column prop="careLevel" label="护理等级" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.careLevel === 'LEVEL1'">一级</el-tag>
            <el-tag v-else-if="row.careLevel === 'LEVEL2'" type="success">二级</el-tag>
            <el-tag v-else-if="row.careLevel === 'LEVEL3'" type="warning">三级</el-tag>
            <el-tag v-else type="danger">四级</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'ACTIVE'" type="success">进行中</el-tag>
            <el-tag v-else-if="row.status === 'PAUSED'" type="warning">已暂停</el-tag>
            <el-tag v-else-if="row.status === 'COMPLETED'" type="info">已完成</el-tag>
            <el-tag v-else type="danger">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="nurseName" label="负责护士" width="100" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link @click="handlePause(row)" v-if="row.status === 'ACTIVE'">暂停</el-button>
            <el-button type="success" link @click="handleResume(row)" v-if="row.status === 'PAUSED'">恢复</el-button>
            <el-button type="info" link @click="handleComplete(row)" v-if="row.status === 'ACTIVE' || row.status === 'PAUSED'">完成</el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-if="row.status !== 'ACTIVE'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑护理计划弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑护理计划' : '新增护理计划'"
      width="750px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        class="care-plan-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="form.planName" placeholder="请输入计划名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="护理对象" prop="elderId">
              <el-select v-model="form.elderId" placeholder="选择老人" filterable style="width: 100%">
                <el-option
                  v-for="elder in elderList"
                  :key="elder.id"
                  :label="elder.name"
                  :value="elder.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="护理等级" prop="careLevel">
              <el-select v-model="form.careLevel" placeholder="选择护理等级" style="width: 100%">
                <el-option label="一级护理" value="LEVEL1" />
                <el-option label="二级护理" value="LEVEL2" />
                <el-option label="三级护理" value="LEVEL3" />
                <el-option label="四级护理" value="LEVEL4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责护士" prop="nurseId">
              <el-select v-model="form.nurseId" placeholder="选择负责护士" filterable style="width: 100%" @change="handleNurseChange">
                <el-option
                  v-for="nurse in nurseList"
                  :key="nurse.id"
                  :label="nurse.name"
                  :value="nurse.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                placeholder="选择开始日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                placeholder="选择结束日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="饮食计划" prop="dietPlan">
          <el-input
            v-model="form.dietPlan"
            type="textarea"
            :rows="2"
            placeholder="请输入饮食计划安排"
          />
        </el-form-item>

        <el-form-item label="用药计划" prop="medicationPlan">
          <el-input
            v-model="form.medicationPlan"
            type="textarea"
            :rows="2"
            placeholder="请输入用药计划安排"
          />
        </el-form-item>

        <el-form-item label="日常护理" prop="dailyCare">
          <el-input
            v-model="form.dailyCare"
            type="textarea"
            :rows="2"
            placeholder="请输入日常护理内容"
          />
        </el-form-item>

        <el-form-item label="康复计划" prop="rehabilitationPlan">
          <el-input
            v-model="form.rehabilitationPlan"
            type="textarea"
            :rows="2"
            placeholder="请输入康复训练计划"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看护理计划详情弹窗 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="护理计划详情"
      width="700px"
    >
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="计划编号">{{ currentRow.planNo }}</el-descriptions-item>
        <el-descriptions-item label="计划名称">{{ currentRow.planName }}</el-descriptions-item>
        <el-descriptions-item label="老人ID">{{ currentRow.elderId }}</el-descriptions-item>
        <el-descriptions-item label="护理等级">
          <el-tag v-if="currentRow.careLevel === 'LEVEL1'">一级</el-tag>
          <el-tag v-else-if="currentRow.careLevel === 'LEVEL2'" type="success">二级</el-tag>
          <el-tag v-else-if="currentRow.careLevel === 'LEVEL3'" type="warning">三级</el-tag>
          <el-tag v-else type="danger">四级</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ formatDate(currentRow.startDate) }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ formatDate(currentRow.endDate) }}</el-descriptions-item>
        <el-descriptions-item label="负责护士">{{ currentRow.nurseName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentRow.status === 'ACTIVE'" type="success">进行中</el-tag>
          <el-tag v-else-if="currentRow.status === 'PAUSED'" type="warning">已暂停</el-tag>
          <el-tag v-else-if="currentRow.status === 'COMPLETED'" type="info">已完成</el-tag>
          <el-tag v-else type="danger">已取消</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ formatDateTime(currentRow.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="饮食计划" :span="2">{{ currentRow.dietPlan || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="用药计划" :span="2">{{ currentRow.medicationPlan || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="日常护理" :span="2">{{ currentRow.dailyCare || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="康复计划" :span="2">{{ currentRow.rehabilitationPlan || '暂无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, FirstAidKit, CircleCheck, Timer, CircleClose } from '@element-plus/icons-vue'
import {
  getCarePlanList,
  createCarePlan,
  updateCarePlan,
  deleteCarePlan,
  pauseCarePlan,
  resumeCarePlan,
  completeCarePlan
} from '@/api/carePlan'
import { getCheckedInElders } from '@/api/elder'
import { getEmployeesByDepartment } from '@/api/employee'

export default {
  name: 'CarePlan',
  components: {
    Search,
    Refresh,
    Plus,
    FirstAidKit,
    CircleCheck,
    Timer,
    CircleClose
  },
  setup() {
    const loading = ref(false)
    const submitLoading = ref(false)
    const tableData = ref([])
    const total = ref(0)
    const pageNum = ref(1)
    const pageSize = ref(10)
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const isEdit = ref(false)
    const formRef = ref(null)
    const elderList = ref([])
    const nurseList = ref([])
    const currentRow = ref(null)
    
    // 统计数据
    const statistics = ref({
      totalCount: 0,
      activeCount: 0,
      pausedCount: 0,
      completedCount: 0
    })

    const searchForm = reactive({
      planName: '',
      elderName: '',
      careLevel: '',
      status: ''
    })

    const form = reactive({
      id: null,
      planName: '',
      elderId: null,
      careLevel: '',
      nurseId: null,
      nurseName: '',
      startDate: '',
      endDate: '',
      dietPlan: '',
      medicationPlan: '',
      dailyCare: '',
      rehabilitationPlan: ''
    })

    const rules = {
      planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
      elderId: [{ required: true, message: '请选择护理对象', trigger: 'change' }],
      careLevel: [{ required: true, message: '请选择护理等级', trigger: 'change' }],
      nurseId: [{ required: true, message: '请选择负责护士', trigger: 'change' }],
      startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
      endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
    }

    const fetchData = async () => {
      loading.value = true
      try {
        const res = await getCarePlanList({
          pageNum: pageNum.value,
          pageSize: pageSize.value,
          planName: searchForm.planName,
          elderName: searchForm.elderName,
          careLevel: searchForm.careLevel,
          status: searchForm.status
        })
        if (res.code === 200) {
          tableData.value = res.data.list
          total.value = res.data.total
          // 更新统计数据
          calculateStatistics(res.data.list)
        }
      } catch (error) {
        console.error('获取护理计划列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 计算统计数据
    const calculateStatistics = (list) => {
      statistics.value.totalCount = list.length
      statistics.value.activeCount = list.filter(item => item.status === 'ACTIVE').length
      statistics.value.pausedCount = list.filter(item => item.status === 'PAUSED').length
      statistics.value.completedCount = list.filter(item => item.status === 'COMPLETED').length
    }

    const fetchElderList = async () => {
      try {
        const res = await getCheckedInElders()
        if (res.code === 200) {
          elderList.value = res.data
        }
      } catch (error) {
        console.error('获取老人列表失败:', error)
      }
    }

    const fetchNurseList = async () => {
      try {
        const res = await getEmployeesByDepartment('NURSING')
        if (res.code === 200) {
          nurseList.value = res.data
        }
      } catch (error) {
        console.error('获取护士列表失败:', error)
      }
    }

    const handleNurseChange = (nurseId) => {
      const selectedNurse = nurseList.value.find(nurse => nurse.id === nurseId)
      if (selectedNurse) {
        form.nurseName = selectedNurse.name
      }
    }

    const handleSearch = () => {
      pageNum.value = 1
      fetchData()
    }

    const resetSearch = () => {
      searchForm.planName = ''
      searchForm.elderName = ''
      searchForm.careLevel = ''
      searchForm.status = ''
      handleSearch()
    }

    const resetForm = () => {
      form.id = null
      form.planName = ''
      form.elderId = null
      form.careLevel = ''
      form.nurseId = null
      form.nurseName = ''
      form.startDate = ''
      form.endDate = ''
      form.dietPlan = ''
      form.medicationPlan = ''
      form.dailyCare = ''
      form.rehabilitationPlan = ''
    }

    const handleAdd = () => {
      isEdit.value = false
      resetForm()
      fetchElderList()
      fetchNurseList()
      dialogVisible.value = true
    }

    const handleEdit = (row) => {
      isEdit.value = true
      fetchElderList()
      fetchNurseList()
      Object.assign(form, row)
      dialogVisible.value = true
    }

    const handleView = (row) => {
      currentRow.value = row
      viewDialogVisible.value = true
    }

    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      submitLoading.value = true
      try {
        const api = isEdit.value ? updateCarePlan : createCarePlan
        const res = await api(form)
        if (res.code === 200) {
          ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
          dialogVisible.value = false
          fetchData()
        }
      } catch (error) {
        console.error(isEdit.value ? '编辑失败:' : '新增失败:', error)
      } finally {
        submitLoading.value = false
      }
    }

    const handlePause = (row) => {
      ElMessageBox.confirm('确定要暂停该护理计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await pauseCarePlan(row.id)
          if (res.code === 200) {
            ElMessage.success('已暂停')
            fetchData()
          }
        } catch (error) {
          console.error('暂停失败:', error)
        }
      }).catch(() => {})
    }

    const handleResume = (row) => {
      ElMessageBox.confirm('确定要恢复该护理计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }).then(async () => {
        try {
          const res = await resumeCarePlan(row.id)
          if (res.code === 200) {
            ElMessage.success('已恢复')
            fetchData()
          }
        } catch (error) {
          console.error('恢复失败:', error)
        }
      }).catch(() => {})
    }

    const handleComplete = (row) => {
      ElMessageBox.confirm('确定要完成该护理计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const res = await completeCarePlan(row.id)
          if (res.code === 200) {
            ElMessage.success('已完成')
            fetchData()
          }
        } catch (error) {
          console.error('完成失败:', error)
        }
      }).catch(() => {})
    }

    const handleDelete = (row) => {
      ElMessageBox.confirm('确定要删除该护理计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteCarePlan(row.id)
          if (res.code === 200) {
            ElMessage.success('删除成功')
            fetchData()
          }
        } catch (error) {
          console.error('删除失败:', error)
        }
      }).catch(() => {})
    }

    const handleSizeChange = (val) => {
      pageSize.value = val
      fetchData()
    }

    const handleCurrentChange = (val) => {
      pageNum.value = val
      fetchData()
    }

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

    onMounted(() => {
      fetchData()
    })

    return {
      loading,
      submitLoading,
      tableData,
      total,
      pageNum,
      pageSize,
      searchForm,
      dialogVisible,
      viewDialogVisible,
      isEdit,
      formRef,
      form,
      rules,
      elderList,
      nurseList,
      currentRow,
      statistics,
      handleSearch,
      resetSearch,
      handleAdd,
      handleEdit,
      handleView,
      handleSubmit,
      handlePause,
      handleResume,
      handleComplete,
      handleDelete,
      handleSizeChange,
      handleCurrentChange,
      handleNurseChange,
      formatDateTime,
      formatDate
    }
  }
}
</script>

<style scoped lang="scss">
.care-plan-container {
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
    
    .search-form {
      .el-form-item {
        margin-bottom: 10px;
        margin-right: 15px;
      }
      
      .el-input,
      .el-select {
        width: 180px;
      }
    }
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .pagination {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }

  .care-plan-form {
    max-height: 500px;
    overflow-y: auto;
  }
}
</style>
