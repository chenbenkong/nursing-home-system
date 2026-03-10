<template>
  <div class="family-relation-container">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="老人姓名">
          <el-input v-model="searchForm.elderName" placeholder="请输入老人姓名" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="家属姓名">
          <el-input v-model="searchForm.familyName" placeholder="请输入家属姓名" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="关系">
          <el-select v-model="searchForm.relation" placeholder="选择关系" clearable style="width: 140px">
            <el-option label="儿子" value="儿子" />
            <el-option label="女儿" value="女儿" />
            <el-option label="配偶" value="配偶" />
            <el-option label="孙子" value="孙子" />
            <el-option label="孙女" value="孙女" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
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
          <span>家属关联列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增关联
          </el-button>
        </div>
      </template>

      <!-- 数据表格 -->
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="elderName" label="老人姓名" width="120" align="center" />
        <el-table-column prop="elderRoomNo" label="房间号" width="100" align="center" />
        <el-table-column prop="familyRealName" label="家属姓名" width="120" align="center" />
        <el-table-column prop="familyPhone" label="联系电话" width="130" align="center" />
        <el-table-column prop="relation" label="关系" width="100" align="center" />
        <el-table-column prop="isPrimary" label="主要联系人" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.isPrimary === 1" type="success">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="权限" min-width="200">
          <template #default="{ row }">
            <el-checkbox v-model="row.canViewHealth" :true-label="1" :false-label="0" disabled>健康</el-checkbox>
            <el-checkbox v-model="row.canViewFee" :true-label="1" :false-label="0" disabled>费用</el-checkbox>
            <el-checkbox v-model="row.canViewLocation" :true-label="1" :false-label="0" disabled>位置</el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择老人" prop="elderId">
          <el-select v-model="form.elderId" placeholder="选择老人" filterable style="width: 100%" :disabled="dialogType === 'edit'">
            <el-option v-for="item in elderList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择家属" prop="familyUserId">
          <el-select v-model="form.familyUserId" placeholder="选择家属账户" filterable style="width: 100%" :disabled="dialogType === 'edit'">
            <el-option v-for="item in familyUserList" :key="item.id" :label="item.realName || item.username" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="关系" prop="relation">
          <el-select v-model="form.relation" placeholder="选择关系" style="width: 100%">
            <el-option label="儿子" value="儿子" />
            <el-option label="女儿" value="女儿" />
            <el-option label="配偶" value="配偶" />
            <el-option label="孙子" value="孙子" />
            <el-option label="孙女" value="孙女" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="主要联系人">
          <el-radio-group v-model="form.isPrimary">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="查看权限">
          <el-checkbox v-model="form.canViewHealth" :true-label="1" :false-label="0">健康信息</el-checkbox>
          <el-checkbox v-model="form.canViewFee" :true-label="1" :false-label="0">费用信息</el-checkbox>
          <el-checkbox v-model="form.canViewLocation" :true-label="1" :false-label="0">位置信息</el-checkbox>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="关联详情" width="500px" destroy-on-close>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="老人姓名">{{ currentRow.elderName }}</el-descriptions-item>
        <el-descriptions-item label="房间号">{{ currentRow.elderRoomNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="家属姓名">{{ currentRow.familyRealName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentRow.familyPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="关系">{{ currentRow.relation }}</el-descriptions-item>
        <el-descriptions-item label="主要联系人">{{ currentRow.isPrimary === 1 ? '是' : '否' }}</el-descriptions-item>
        <el-descriptions-item label="查看权限">
          健康信息: {{ currentRow.canViewHealth === 1 ? '✓' : '✗' }} |
          费用信息: {{ currentRow.canViewFee === 1 ? '✓' : '✗' }} |
          位置信息: {{ currentRow.canViewLocation === 1 ? '✓' : '✗' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentRow.status === 1 ? 'success' : 'danger'">
            {{ currentRow.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getFamilyRelationList,
  addFamilyRelation,
  updateFamilyRelation,
  deleteFamilyRelation
} from '@/api/family'
import { getAllElders } from '@/api/elder'
import { getUserList } from '@/api/user'

// 搜索表单
const searchForm = reactive({
  elderName: '',
  familyName: '',
  relation: '',
  status: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)

// 老人列表和家属用户列表
const elderList = ref([])
const familyUserList = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogType = ref('add')
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  id: null,
  elderId: null,
  familyUserId: null,
  relation: '',
  isPrimary: 0,
  canViewHealth: 1,
  canViewFee: 1,
  canViewLocation: 1,
  status: 1
})

const rules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
  familyUserId: [{ required: true, message: '请选择家属账户', trigger: 'change' }],
  relation: [{ required: true, message: '请选择关系', trigger: 'change' }]
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

// 获取家属用户列表（角色为家属的用户）
const fetchFamilyUserList = async () => {
  try {
    const res = await getUserList({ pageNum: 1, pageSize: 1000 })
    if (res.code === 200) {
      // 过滤出家属角色的用户，如果没有则显示所有用户
      familyUserList.value = res.data.list.filter(u => u.role === 'FAMILY' || !u.role)
      if (familyUserList.value.length === 0) {
        familyUserList.value = res.data.list
      }
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

// 获取列表数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm }
    const res = await getFamilyRelationList(params)
    if (res.code === 200) {
      tableData.value = res.data.list
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  fetchData()
}

// 重置
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = key === 'status' ? null : ''
  })
  fetchData()
}

// 新增
const handleAdd = () => {
  dialogType.value = 'add'
  dialogTitle.value = '新增家属关联'
  Object.keys(form).forEach(key => {
    form[key] = key === 'id' || key === 'elderId' || key === 'familyUserId' ? null : 
                key === 'isPrimary' ? 0 : 
                key === 'status' ? 1 : 
                key === 'canViewHealth' || key === 'canViewFee' || key === 'canViewLocation' ? 1 : ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogType.value = 'edit'
  dialogTitle.value = '编辑家属关联'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 提交
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const api = dialogType.value === 'add' ? addFamilyRelation : updateFamilyRelation
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
  ElMessageBox.confirm('确认删除该家属关联？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteFamilyRelation(row.id)
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
  fetchFamilyUserList()
  fetchData()
})
</script>

<style scoped>
.family-relation-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
