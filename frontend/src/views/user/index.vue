<template>
  <div class="user-container">
    <!-- 文艺动画背景 -->
    <ArtisticBackground />
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><UserIcon /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">用户总数</div>
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
            <div class="stat-label">启用用户</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><UserFilled /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.adminCount || 0 }}</div>
            <div class="stat-label">管理员</div>
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
            <div class="stat-label">医护用户</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input 
            v-model="searchForm.username" 
            placeholder="请输入用户名" 
            clearable 
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input 
            v-model="searchForm.realName" 
            placeholder="请输入真实姓名" 
            clearable 
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input 
            v-model="searchForm.phone" 
            placeholder="请输入手机号" 
            clearable 
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="选择角色" clearable style="width: 150px">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="经理" value="MANAGER" />
            <el-option label="护士" value="NURSE" />
            <el-option label="医生" value="DOCTOR" />
            <el-option label="员工" value="STAFF" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 150px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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
          <span>系统用户列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增用户
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.role === 'ADMIN'" type="danger">管理员</el-tag>
            <el-tag v-else-if="row.role === 'MANAGER'" type="warning">经理</el-tag>
            <el-tag v-else-if="row.role === 'NURSE'" type="success">护士</el-tag>
            <el-tag v-else-if="row.role === 'DOCTOR'" type="primary">医生</el-tag>
            <el-tag v-else>员工</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最后登录" width="150">
          <template #default="{ row }">{{ formatDateTime(row.lastLoginTime) }}</template>
        </el-table-column>
        <el-table-column label="创建时间" width="150">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
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

    <!-- 新增/编辑用户弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      width="600px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>

        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>

        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="选择角色" style="width: 100%">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="经理" value="MANAGER" />
            <el-option label="护士" value="NURSE" />
            <el-option label="医生" value="DOCTOR" />
            <el-option label="员工" value="STAFF" />
          </el-select>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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

    <!-- 查看用户详情弹窗 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="用户详情"
      width="550px"
    >
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="用户名">{{ currentRow.username }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ currentRow.realName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentRow.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentRow.email || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="角色">
          <el-tag v-if="currentRow.role === 'ADMIN'" type="danger">管理员</el-tag>
          <el-tag v-else-if="currentRow.role === 'MANAGER'" type="warning">经理</el-tag>
          <el-tag v-else-if="currentRow.role === 'NURSE'" type="success">护士</el-tag>
          <el-tag v-else-if="currentRow.role === 'DOCTOR'" type="primary">医生</el-tag>
          <el-tag v-else>员工</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentRow.status === 1" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最后登录时间">{{ formatDateTime(currentRow.lastLoginTime) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(currentRow.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark || '暂无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, User as UserIcon, CircleCheck, UserFilled, FirstAidKit } from '@element-plus/icons-vue'
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user'
import ArtisticBackground from '@/components/ArtisticBackground.vue'

export default {
  name: 'User',
  components: {
    ArtisticBackground,
    Search,
    Refresh,
    Plus,
    UserIcon,
    CircleCheck,
    UserFilled,
    FirstAidKit
  },
  setup() {
    const loading = ref(false)
    const submitLoading = ref(false)
    const tableData = ref([])
    const total = ref(0)
    const pageNum = ref(1)
    const pageSize = ref(5)
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const isEdit = ref(false)
    const formRef = ref(null)
    const currentRow = ref(null)
    
    // 统计数据
    const statistics = ref({
      totalCount: 0,
      activeCount: 0,
      adminCount: 0,
      medicalCount: 0
    })

    const searchForm = reactive({
      username: '',
      realName: '',
      phone: '',
      role: '',
      status: null
    })

    const form = reactive({
      id: null,
      username: '',
      password: '',
      realName: '',
      phone: '',
      email: '',
      role: 'STAFF',
      status: 1,
      remark: ''
    })

    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
      phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
      ],
      email: [
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
      ],
      role: [{ required: true, message: '请选择角色', trigger: 'change' }]
    }

    const fetchData = async () => {
      loading.value = true
      try {
        const res = await getUserList({
          pageNum: pageNum.value,
          pageSize: pageSize.value,
          username: searchForm.username,
          realName: searchForm.realName,
          phone: searchForm.phone,
          role: searchForm.role,
          status: searchForm.status
        })
        if (res.code === 200) {
          tableData.value = res.data.list || res.data
          total.value = res.data.total || res.data.length
          // 更新统计数据
          calculateStatistics(res.data.list || res.data)
        }
      } catch (error) {
        console.error('获取用户列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 计算统计数据
    const calculateStatistics = (list) => {
      statistics.value.totalCount = list.length
      statistics.value.activeCount = list.filter(item => item.status === 1).length
      statistics.value.adminCount = list.filter(item => item.role === 'ADMIN').length
      statistics.value.medicalCount = list.filter(item => item.role === 'DOCTOR' || item.role === 'NURSE').length
    }

    const handleSearch = () => {
      pageNum.value = 1
      fetchData()
    }

    const resetSearch = () => {
      searchForm.username = ''
      searchForm.realName = ''
      searchForm.phone = ''
      searchForm.role = ''
      searchForm.status = null
      handleSearch()
    }

    const resetForm = () => {
      form.id = null
      form.username = ''
      form.password = ''
      form.realName = ''
      form.phone = ''
      form.email = ''
      form.role = 'STAFF'
      form.status = 1
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

    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      submitLoading.value = true
      try {
        const api = isEdit.value ? updateUser : addUser
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

    const handleDelete = (row) => {
      ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteUser(row.id)
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
      handleSubmit,
      handleDelete,
      handleSizeChange,
      handleCurrentChange,
      formatDateTime
    }
  }
}
</script>

<style scoped lang="scss">
.user-container {
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
    
    &.orange {
      border-left: 4px solid #E6A23C;
      .stat-icon { background: linear-gradient(135deg, #E6A23C, #F3D19E); }
      .stat-value { color: #E6A23C; }
    }
    
    &.purple {
      border-left: 4px solid #8B5CF6;
      .stat-icon { background: linear-gradient(135deg, #8B5CF6, #A78BFA); }
      .stat-value { color: #8B5CF6; }
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
