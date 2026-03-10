<template>
  <div class="employee-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">员工总数</div>
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
            <div class="stat-label">在职员工</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Briefcase /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.nursingCount || 0 }}</div>
            <div class="stat-label">护理部</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card gray">
          <div class="stat-icon">
            <el-icon><FirstAidKit /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.medicalCount || 0 }}</div>
            <div class="stat-label">医疗部</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input 
            v-model="searchForm.name" 
            placeholder="请输入姓名" 
            clearable 
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="searchForm.department" placeholder="选择部门" clearable style="width: 140px">
            <el-option label="行政部" value="ADMIN" />
            <el-option label="护理部" value="NURSING" />
            <el-option label="医疗部" value="MEDICAL" />
            <el-option label="餐饮部" value="CATERING" />
            <el-option label="后勤部" value="HOUSEKEEPING" />
            <el-option label="安保部" value="SECURITY" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 120px">
            <el-option label="在职" value="ACTIVE" />
            <el-option label="离职" value="RESIGNED" />
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
          <span>员工列表</span>
          <el-button type="primary" @click="handleAdd">新增员工</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="empNo" label="员工编号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.gender === 'MALE'" type="primary">男</el-tag>
            <el-tag v-else type="danger">女</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="department" label="部门" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.department === 'ADMIN'">行政部</el-tag>
            <el-tag v-else-if="row.department === 'NURSING'" type="success">护理部</el-tag>
            <el-tag v-else-if="row.department === 'MEDICAL'" type="warning">医疗部</el-tag>
            <el-tag v-else-if="row.department === 'CATERING'" type="info">餐饮部</el-tag>
            <el-tag v-else-if="row.department === 'HOUSEKEEPING'">后勤部</el-tag>
            <el-tag v-else type="danger">安保部</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="position" label="职位" width="120" />
        <el-table-column prop="entryDate" label="入职日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'ACTIVE'" type="success">在职</el-tag>
            <el-tag v-else-if="row.status === 'LEAVE'" type="warning">休假</el-tag>
            <el-tag v-else type="danger">离职</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleResign(row)" v-if="row.status === 'ACTIVE'">离职</el-button>
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

    <!-- 新增/编辑员工弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑员工' : '新增员工'"
      width="700px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio label="MALE">男</el-radio>
                <el-radio label="FEMALE">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门" prop="department">
              <el-select v-model="form.department" placeholder="选择部门" style="width: 100%">
                <el-option label="行政部" value="ADMIN" />
                <el-option label="护理部" value="NURSING" />
                <el-option label="医疗部" value="MEDICAL" />
                <el-option label="餐饮部" value="CATERING" />
                <el-option label="后勤部" value="HOUSEKEEPING" />
                <el-option label="安保部" value="SECURITY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位" prop="position">
              <el-input v-model="form.position" placeholder="请输入职位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="入职日期" prop="entryDate">
              <el-date-picker
                v-model="form.entryDate"
                type="date"
                placeholder="选择入职日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历" prop="education">
              <el-select v-model="form.education" placeholder="选择学历" style="width: 100%">
                <el-option label="初中及以下" value="MIDDLE_SCHOOL" />
                <el-option label="高中" value="HIGH_SCHOOL" />
                <el-option label="大专" value="COLLEGE" />
                <el-option label="本科" value="BACHELOR" />
                <el-option label="硕士" value="MASTER" />
                <el-option label="博士" value="DOCTOR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="家庭住址" prop="address">
          <el-input v-model="form.address" placeholder="请输入家庭住址" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看员工详情弹窗 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="员工详情"
      width="650px"
    >
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="员工编号">{{ currentRow.empNo }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ currentRow.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <el-tag v-if="currentRow.gender === 'MALE'" type="primary">男</el-tag>
          <el-tag v-else type="danger">女</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ currentRow.idCard }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentRow.phone }}</el-descriptions-item>
        <el-descriptions-item label="部门">
          <el-tag v-if="currentRow.department === 'ADMIN'">行政部</el-tag>
          <el-tag v-else-if="currentRow.department === 'NURSING'" type="success">护理部</el-tag>
          <el-tag v-else-if="currentRow.department === 'MEDICAL'" type="warning">医疗部</el-tag>
          <el-tag v-else-if="currentRow.department === 'CATERING'" type="info">餐饮部</el-tag>
          <el-tag v-else-if="currentRow.department === 'HOUSEKEEPING'">后勤部</el-tag>
          <el-tag v-else type="danger">安保部</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="职位">{{ currentRow.position }}</el-descriptions-item>
        <el-descriptions-item label="入职日期">{{ formatDate(currentRow.entryDate) }}</el-descriptions-item>
        <el-descriptions-item label="学历">{{ formatEducation(currentRow.education) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentRow.status === 'ACTIVE'" type="success">在职</el-tag>
          <el-tag v-else-if="currentRow.status === 'LEAVE'" type="warning">休假</el-tag>
          <el-tag v-else type="danger">离职</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="家庭住址" :span="2">{{ currentRow.address || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系人">{{ currentRow.emergencyContact || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="紧急电话">{{ currentRow.emergencyPhone || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(currentRow.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDateTime(currentRow.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark || '暂无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, User, CircleCheck, Briefcase, FirstAidKit } from '@element-plus/icons-vue'
import { getEmployeeList, addEmployee, updateEmployee, resignEmployee } from '@/api/employee'

export default {
  name: 'Employee',
  components: {
    Search,
    Refresh,
    Plus
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
    const currentRow = ref(null)
    
    // 统计数据
    const statistics = ref({
      totalCount: 0,
      activeCount: 0,
      nursingCount: 0,
      medicalCount: 0
    })

    const searchForm = reactive({
      name: '',
      department: '',
      status: ''
    })

    const form = reactive({
      id: null,
      name: '',
      gender: 'MALE',
      idCard: '',
      phone: '',
      department: '',
      position: '',
      entryDate: '',
      education: '',
      address: '',
      remark: ''
    })

    const rules = {
      name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
      gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
      idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
      phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
      department: [{ required: true, message: '请选择部门', trigger: 'change' }],
      position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
      entryDate: [{ required: true, message: '请选择入职日期', trigger: 'change' }]
    }

    const fetchData = async () => {
      loading.value = true
      try {
        const res = await getEmployeeList({
          pageNum: pageNum.value,
          pageSize: pageSize.value,
          name: searchForm.name,
          department: searchForm.department,
          status: searchForm.status
        })
        if (res.code === 200) {
          tableData.value = res.data.list
          total.value = res.data.total
          // 更新统计数据
          calculateStatistics(res.data.list)
        }
      } catch (error) {
        console.error('获取员工列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 计算统计数据
    const calculateStatistics = (list) => {
      statistics.value.totalCount = list.length
      statistics.value.activeCount = list.filter(item => item.status === 'ACTIVE').length
      statistics.value.nursingCount = list.filter(item => item.department === 'NURSING').length
      statistics.value.medicalCount = list.filter(item => item.department === 'MEDICAL').length
    }

    const handleSearch = () => {
      pageNum.value = 1
      fetchData()
    }

    const resetSearch = () => {
      searchForm.name = ''
      searchForm.department = ''
      searchForm.status = ''
      handleSearch()
    }

    const resetForm = () => {
      form.id = null
      form.name = ''
      form.gender = 'MALE'
      form.idCard = ''
      form.phone = ''
      form.department = ''
      form.position = ''
      form.entryDate = ''
      form.education = ''
      form.address = ''
      form.remark = ''
    }

    const handleAdd = () => {
      isEdit.value = false
      resetForm()
      dialogVisible.value = true
    }

    const handleEdit = (row) => {
      isEdit.value = true
      Object.assign(form, row)
      dialogVisible.value = true
    }

    const handleView = (row) => {
      currentRow.value = row
      viewDialogVisible.value = true
    }

    const formatEducation = (education) => {
      const map = {
        'MIDDLE_SCHOOL': '初中及以下',
        'HIGH_SCHOOL': '高中',
        'COLLEGE': '大专',
        'BACHELOR': '本科',
        'MASTER': '硕士',
        'DOCTOR': '博士'
      }
      return map[education] || education || '暂无'
    }

    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      submitLoading.value = true
      try {
        const api = isEdit.value ? updateEmployee : addEmployee
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

    const handleResign = (row) => {
      ElMessageBox.confirm('确定为该员工办理离职吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await resignEmployee(row.id)
          if (res.code === 200) {
            ElMessage.success('已办理离职')
            fetchData()
          }
        } catch (error) {
          console.error('离职办理失败:', error)
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
      currentRow,
      statistics,
      handleSearch,
      resetSearch,
      handleAdd,
      handleEdit,
      handleView,
      formatEducation,
      handleSubmit,
      handleResign,
      handleSizeChange,
      handleCurrentChange,
      formatDateTime,
      formatDate
    }
  }
}
</script>

<style scoped lang="scss">
.employee-container {
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
}
</style>
