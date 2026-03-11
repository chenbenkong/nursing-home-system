<template>
  <div class="elder-container">
    <!-- 文艺动画背景 -->
    <ArtisticBackground />
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">老人总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <el-icon><HomeFilled /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.checkedInCount || 0 }}</div>
            <div class="stat-label">已入住</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.pendingCount || 0 }}</div>
            <div class="stat-label">待入住</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card gray">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.checkedOutCount || 0 }}</div>
            <div class="stat-label">已退住</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索栏 -->
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
        <el-form-item label="身份证号">
          <el-input 
            v-model="searchForm.idCard" 
            placeholder="请输入身份证号" 
            clearable 
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input 
            v-model="searchForm.phone" 
            placeholder="请输入手机号" 
            clearable 
            style="width: 160px"
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
            <el-option label="待入住" value="PENDING" />
            <el-option label="已入住" value="CHECKED_IN" />
            <el-option label="已退住" value="CHECKED_OUT" />
            <el-option label="暂停" value="SUSPENDED" />
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
    
    <!-- 操作栏 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>老人列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增老人
          </el-button>
        </div>
      </template>
      
      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="elderNo" label="老人编号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.gender === 'MALE'" type="primary">男</el-tag>
            <el-tag v-else type="danger">女</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="healthStatus" label="健康状况" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.healthStatus === 'HEALTHY'" type="success">健康</el-tag>
            <el-tag v-else-if="row.healthStatus === 'SUBHEALTH'" type="warning">亚健康</el-tag>
            <el-tag v-else-if="row.healthStatus === 'CHRONIC'" type="info">慢性病</el-tag>
            <el-tag v-else type="danger">失能</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="careLevel" label="护理等级" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.careLevel === 'LEVEL1'">一级</el-tag>
            <el-tag v-else-if="row.careLevel === 'LEVEL2'" type="success">二级</el-tag>
            <el-tag v-else-if="row.careLevel === 'LEVEL3'" type="warning">三级</el-tag>
            <el-tag v-else type="danger">四级</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'PENDING'" type="info">待入住</el-tag>
            <el-tag v-else-if="row.status === 'CHECKED_IN'" type="success">已入住</el-tag>
            <el-tag v-else-if="row.status === 'CHECKED_OUT'" type="danger">已退住</el-tag>
            <el-tag v-else type="warning">暂停</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="入住日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.checkInDate) || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="房间床位" width="120">
          <template #default="{ row }">
            <div v-if="row.roomNumber" class="room-info">
              <el-tag size="small" type="info">{{ row.roomNumber }}</el-tag>
              <span v-if="row.bedNumber" class="bed-number">{{ row.bedNumber }}</span>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link @click="handleApplyCheckIn(row)" v-if="row.status === 'PENDING' || row.status === 'CHECKED_OUT'">申请入住</el-button>
            <el-button type="warning" link @click="handleChangeRoom(row)" v-if="row.status === 'CHECKED_IN'">换房</el-button>
            <el-button type="danger" link @click="handleCheckOut(row)" v-if="row.status === 'CHECKED_IN'">退住</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
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
    
    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
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
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                v-model="form.birthDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="健康状况" prop="healthStatus">
              <el-select v-model="form.healthStatus" placeholder="选择健康状况" style="width: 100%">
                <el-option label="健康" value="HEALTHY" />
                <el-option label="亚健康" value="SUBHEALTH" />
                <el-option label="慢性病" value="CHRONIC" />
                <el-option label="失能" value="DISABLED" />
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
            <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="选择状态" style="width: 100%">
            <el-option label="待入住" value="PENDING" />
            <el-option label="已入住" value="CHECKED_IN" />
            <el-option label="已退住" value="CHECKED_OUT" />
            <el-option label="暂停" value="SUSPENDED" />
          </el-select>
        </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="家庭住址" prop="address">
          <el-input v-model="form.address" placeholder="请输入家庭住址" />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="紧急联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入紧急联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系人电话" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="与老人关系" prop="contactRelation">
          <el-input v-model="form.contactRelation" placeholder="请输入与老人关系" />
        </el-form-item>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
    
    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="老人详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="姓名">{{ currentRow.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ currentRow.gender === 'MALE' ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ formatDate(currentRow.birthDate) }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ currentRow.idCard }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentRow.phone }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系人">{{ currentRow.emergencyContact }}</el-descriptions-item>
        <el-descriptions-item label="紧急电话">{{ currentRow.emergencyPhone }}</el-descriptions-item>
        <el-descriptions-item label="健康状况">
          <el-tag v-if="currentRow.healthStatus === 'HEALTHY'" type="success">健康</el-tag>
          <el-tag v-else-if="currentRow.healthStatus === 'SUBHEALTH'" type="warning">亚健康</el-tag>
          <el-tag v-else-if="currentRow.healthStatus === 'CHRONIC'" type="info">慢性病</el-tag>
          <el-tag v-else type="danger">失能</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="护理等级">
          <el-tag v-if="currentRow.careLevel === 'LEVEL1'">一级</el-tag>
          <el-tag v-else-if="currentRow.careLevel === 'LEVEL2'" type="success">二级</el-tag>
          <el-tag v-else-if="currentRow.careLevel === 'LEVEL3'" type="warning">三级</el-tag>
          <el-tag v-else type="danger">四级</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentRow.status === 'PENDING'" type="info">待入住</el-tag>
          <el-tag v-else-if="currentRow.status === 'CHECKED_IN'" type="success">已入住</el-tag>
          <el-tag v-else-if="currentRow.status === 'CHECKED_OUT'" type="danger">已退住</el-tag>
          <el-tag v-else type="warning">暂停</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="入住日期">{{ formatDate(currentRow.checkInDate) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="房间号">{{ currentRow.roomNumber || (currentRow.roomId ? '房间' + currentRow.roomId : '-') }}</el-descriptions-item>
        <el-descriptions-item label="床号">{{ currentRow.bedNo || currentRow.bedNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ currentRow.address }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark || '暂无' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
    
    <!-- 换房对话框 -->
    <el-dialog
      v-model="changeRoomDialogVisible"
      title="老人换房"
      width="500px"
    >
      <el-form label-width="100px">
        <el-form-item label="当前老人">
          <span>{{ changeRoomForm.elderName }}</span>
        </el-form-item>
        <el-form-item label="当前房间">
          <el-tag type="info">{{ changeRoomForm.currentRoom }}</el-tag>
        </el-form-item>
        <el-form-item label="当前床位">
          <el-tag type="info">{{ changeRoomForm.currentBed }}</el-tag>
        </el-form-item>
        <el-divider />
        <el-form-item label="新房间" required>
          <el-select 
            v-model="changeRoomForm.newRoomId" 
            placeholder="选择新房间" 
            style="width: 100%"
            @change="handleChangeRoomSelect"
          >
            <el-option 
              v-for="room in availableRoomsForChange" 
              :key="room.id" 
              :label="room.roomNumber + ' - ' + getRoomTypeText(room.roomType) + ' (' + room.occupied + '/' + room.capacity + '床)'" 
              :value="room.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="新床位" required v-if="changeRoomForm.newRoomId">
          <el-select v-model="changeRoomForm.newBedId" placeholder="选择新床位" style="width: 100%">
            <el-option 
              v-for="bed in availableBedsForChange" 
              :key="bed.id" 
              :label="bed.bedNumber" 
              :value="bed.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="changeRoomDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChangeRoomSubmit">确认换房</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { getElderList, addElder, updateElder, deleteElder, checkIn, checkOut } from '@/api/elder'
import { getAvailableRooms } from '@/api/room'
import ArtisticBackground from '@/components/ArtisticBackground.vue'

export default {
  name: 'Elder',
  components: {
    ArtisticBackground,
    Search,
    Refresh,
    Plus
  },
  setup() {
    const router = useRouter()
    const loading = ref(false)
    const submitLoading = ref(false)
    const tableData = ref([])
    const total = ref(0)
    const pageNum = ref(1)
    const pageSize = ref(10)
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const dialogTitle = ref('')
    const formRef = ref(null)
    const isEdit = ref(false)
    const currentRow = ref({})
    
    // 统计数据
    const statistics = ref({
      totalCount: 0,
      checkedInCount: 0,
      pendingCount: 0,
      checkedOutCount: 0
    })
    
    const searchForm = reactive({
      name: '',
      idCard: '',
      phone: '',
      careLevel: '',
      status: ''
    })
    
    const form = reactive({
      id: null,
      name: '',
      gender: 'MALE',
      birthDate: '',
      idCard: '',
      phone: '',
      address: '',
      contactName: '',
      contactPhone: '',
      contactRelation: '',
      healthStatus: 'HEALTHY',
      careLevel: 'LEVEL1',
      status: 'PENDING',
      remark: ''
    })
    
    const rules = {
      name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
      gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
      phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
      healthStatus: [{ required: true, message: '请选择健康状况', trigger: 'change' }],
      careLevel: [{ required: true, message: '请选择护理等级', trigger: 'change' }]
    }
    
    const fetchData = async () => {
      loading.value = true
      try {
        // 构建查询参数，只传递非空值
        const params = {
          pageNum: pageNum.value,
          pageSize: pageSize.value
        }
        if (searchForm.name) params.name = searchForm.name
        if (searchForm.idCard) params.idCard = searchForm.idCard
        if (searchForm.phone) params.phone = searchForm.phone
        if (searchForm.careLevel) params.careLevel = searchForm.careLevel
        if (searchForm.status) params.status = searchForm.status
        
        const res = await getElderList(params)
        if (res.code === 200) {
          tableData.value = res.data.list
          total.value = res.data.total
          // 更新统计数据
          calculateStatistics(res.data.list)
        }
      } catch (error) {
        console.error('获取老人列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 计算统计数据
    const calculateStatistics = (list) => {
      statistics.value.totalCount = list.length
      statistics.value.checkedInCount = list.filter(item => item.status === 'CHECKED_IN').length
      statistics.value.pendingCount = list.filter(item => item.status === 'PENDING').length
      statistics.value.checkedOutCount = list.filter(item => item.status === 'CHECKED_OUT').length
    }
    
    const handleSearch = () => {
      pageNum.value = 1
      fetchData()
    }
    
    const resetSearch = () => {
      searchForm.name = ''
      searchForm.idCard = ''
      searchForm.phone = ''
      searchForm.careLevel = ''
      searchForm.status = ''
      handleSearch()
    }
    
    const handleAdd = () => {
      isEdit.value = false
      dialogTitle.value = '新增老人'
      resetForm()
      dialogVisible.value = true
    }
    
    const handleView = (row) => {
      currentRow.value = row
      viewDialogVisible.value = true
    }
    
    // 格式化日期为字符串
    const formatDateToString = (date) => {
      if (!date) return ''
      if (Array.isArray(date)) {
        const [year, month, day] = date
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      }
      if (typeof date === 'string') {
        return date.split('T')[0]
      }
      return date
    }
    
    const handleEdit = (row) => {
      isEdit.value = true
      dialogTitle.value = '编辑老人'
      // 处理日期格式
      const rowData = { ...row }
      rowData.birthDate = formatDateToString(row.birthDate)
      rowData.checkInDate = formatDateToString(row.checkInDate)
      Object.assign(form, rowData)
      dialogVisible.value = true
    }
    
    const handleDelete = (row) => {
      ElMessageBox.confirm('确定要删除该老人信息吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteElder(row.id)
          if (res.code === 200) {
            ElMessage.success('删除成功')
            fetchData()
          } else {
            ElMessage.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('服务器错误')
        }
      }).catch(() => {})
    }
    
    // 申请入住 - 跳转到入住申请页面
    const handleApplyCheckIn = (row) => {
      // 将老人信息存储到 sessionStorage，供入住申请页面使用
      const elderInfo = {
        elderId: row.id,
        elderName: row.name,
        gender: row.gender,
        birthDate: row.birthDate,
        idCard: row.idCard,
        phone: row.phone,
        careLevel: row.careLevel,
        healthStatus: row.healthStatus
      }
      sessionStorage.setItem('applyCheckInElder', JSON.stringify(elderInfo))
      // 跳转到入住申请页面
      router.push('/application')
      ElMessage.success('已跳转到入住申请页面，请完善申请信息')
    }
    
    // 换房功能
    const changeRoomDialogVisible = ref(false)
    const changeRoomForm = reactive({
      elderId: '',
      elderName: '',
      currentRoom: '',
      currentBed: '',
      newRoomId: '',
      newBedId: ''
    })
    const availableRoomsForChange = ref([])
    const availableBedsForChange = ref([])
    
    const handleChangeRoom = async (row) => {
      // 打开换房对话框
      changeRoomForm.elderId = row.id
      changeRoomForm.elderName = row.name
      changeRoomForm.currentRoom = row.roomNumber || '未分配'
      // 格式化当前床位显示
      const currentBed = row.bedNo || row.bedNumber
      if (currentBed) {
        const bn = currentBed.toString().trim()
        if (bn.endsWith('床')) {
          changeRoomForm.currentBed = bn
        } else if (bn.length === 1 && /^[A-H]$/i.test(bn)) {
          changeRoomForm.currentBed = `${bn}床`
        } else if (bn.includes('-')) {
          const parts = bn.split('-')
          const letter = parts[parts.length - 1]
          if (letter && letter.length === 1 && /^[A-H]$/i.test(letter)) {
            changeRoomForm.currentBed = `${letter}床`
          } else {
            changeRoomForm.currentBed = bn
          }
        } else {
          changeRoomForm.currentBed = bn
        }
      } else {
        changeRoomForm.currentBed = '未分配'
      }
      changeRoomForm.newRoomId = ''
      changeRoomForm.newBedId = ''

      // 获取可用房间（排除当前房间）
      await fetchAvailableRoomsForChange(row.roomId)

      changeRoomDialogVisible.value = true
    }
    
    // 获取可用房间（换房用）
    const fetchAvailableRoomsForChange = async (currentRoomId) => {
      try {
        const res = await getAvailableRooms()
        if (res.code === 200) {
          // 转换后端数据为前端格式
          const rooms = (res.data || []).map(room => ({
            id: room.id,
            roomNumber: room.roomNo,
            roomType: room.roomType,
            capacity: room.bedCount,
            occupied: room.occupiedBeds || 0
          }))
          availableRoomsForChange.value = rooms
        }
      } catch (error) {
        console.error('获取可用房间失败:', error)
      }
    }
    
    // 房间选择变化时获取床位
    const handleChangeRoomSelect = (roomId) => {
      changeRoomForm.newBedId = ''
      // 根据选中的房间生成可用床位
      const room = availableRoomsForChange.value.find(r => r.id == roomId)
      if (room) {
        const beds = []
        const bedLetters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
        const occupiedCount = room.occupied || 0
        for (let i = occupiedCount; i < room.capacity; i++) {
          beds.push({
            id: bedLetters[i],
            bedNumber: `${bedLetters[i]}床`
          })
        }
        availableBedsForChange.value = beds
      }
    }
    
    // 提交换房
    const handleChangeRoomSubmit = async () => {
      if (!changeRoomForm.newRoomId) {
        ElMessage.warning('请选择新房间')
        return
      }
      if (!changeRoomForm.newBedId) {
        ElMessage.warning('请选择新床位')
        return
      }

      try {
        // 将床位ID转换为标准格式（A -> A床）
        const bedNo = changeRoomForm.newBedId.length === 1 
          ? `${changeRoomForm.newBedId}床` 
          : changeRoomForm.newBedId
        
        // 直接调用API更新老人的房间和床位信息
        const res = await updateElder({
          id: changeRoomForm.elderId,
          roomId: changeRoomForm.newRoomId,
          bedNo: bedNo
        })

        if (res.code === 200) {
          // 显示费用差异信息
          const priceDiff = res.data?.priceDiff || 0
          if (priceDiff > 0) {
            ElMessage.success(`换房成功，新房比旧房贵${priceDiff}元，请补缴差价`)
          } else if (priceDiff < 0) {
            ElMessage.success(`换房成功，新房比旧房便宜${Math.abs(priceDiff)}元，已生成退款记录`)
          } else {
            ElMessage.success('换房成功')
          }
          changeRoomDialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(res.message || '换房失败')
        }
      } catch (error) {
        console.error('换房失败:', error)
        ElMessage.error('换房失败: ' + (error.message || '未知错误'))
      }
    }
    
    const handleCheckOut = (row) => {
      ElMessageBox.confirm('确定为该老人办理退住吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await checkOut(row.id)
          if (res.code === 200) {
            ElMessage.success('退住办理成功')
            fetchData()
          } else {
            ElMessage.error(res.message || '退住办理失败')
          }
        } catch (error) {
          console.error('退住办理失败:', error)
          ElMessage.error('服务器错误')
        }
      }).catch(() => {})
    }
    
    const handleSubmit = async () => {
      if (!formRef.value) return
      
      await formRef.value.validate(async (valid) => {
        if (valid) {
          submitLoading.value = true
          try {
            // 处理表单数据，移除空字符串的字段
            const submitData = { ...form }
            
            // 如果身份证为空字符串，则删除该字段，避免数据库唯一约束冲突
            if (!submitData.idCard || submitData.idCard.trim() === '') {
              delete submitData.idCard
            }
            
            const api = isEdit.value ? updateElder : addElder
            const res = await api(submitData)
            if (res.code === 200) {
              ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
              dialogVisible.value = false
              fetchData()
            } else {
              ElMessage.error(res.message || '操作失败')
            }
          } catch (error) {
            console.error('提交失败:', error)
            ElMessage.error('提交失败: ' + (error.message || '未知错误'))
          } finally {
            submitLoading.value = false
          }
        }
      })
    }
    
    const resetForm = () => {
      form.id = null
      form.name = ''
      form.gender = 'MALE'
      form.birthDate = ''
      form.idCard = ''
      form.phone = ''
      form.address = ''
      form.contactName = ''
      form.contactPhone = ''
      form.contactRelation = ''
      form.healthStatus = 'HEALTHY'
      form.careLevel = 'LEVEL1'
      form.status = 'PENDING'
      form.remark = ''
    }
    
    // 日期格式化
    const formatDate = (date) => {
      if (!date) return ''
      // 处理数组格式 [2026, 3, 26]
      if (Array.isArray(date)) {
        return date.join('-')
      }
      // 处理字符串格式
      if (typeof date === 'string') {
        return date.split('T')[0]
      }
      return date
    }
    
    const handleSizeChange = (val) => {
      pageSize.value = val
      fetchData()
    }
    
    const handleCurrentChange = (val) => {
      pageNum.value = val
      fetchData()
    }
    
    onMounted(() => {
      fetchData()
    })
    
    // 房间类型文本转换
    const getRoomTypeText = (type) => {
      const map = {
        'SINGLE': '单人间',
        'DOUBLE': '双人间',
        'TRIPLE': '三人间',
        'MULTI': '多人间',
        'SUITE': '套房'
      }
      return map[type] || type
    }
    
    return {
      loading,
      submitLoading,
      tableData,
      total,
      pageNum,
      pageSize,
      dialogVisible,
      viewDialogVisible,
      changeRoomDialogVisible,
      dialogTitle,
      formRef,
      searchForm,
      form,
      rules,
      statistics,
      currentRow,
      changeRoomForm,
      availableRoomsForChange,
      availableBedsForChange,
      formatDate,
      formatDateToString,
      getRoomTypeText,
      handleSearch,
      resetSearch,
      handleAdd,
      handleView,
      handleEdit,
      handleDelete,
      handleApplyCheckIn,
      handleChangeRoom,
      handleChangeRoomSelect,
      handleChangeRoomSubmit,
      handleCheckOut,
      handleSubmit,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped lang="scss">
.elder-container {
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
    
    .room-info {
      display: flex;
      flex-direction: column;
      gap: 4px;
      
      .bed-number {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}
</style>
