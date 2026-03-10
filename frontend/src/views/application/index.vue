<template>
  <div class="application-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">申请总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.pendingCount || 0 }}</div>
            <div class="stat-label">待审批</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.approvedCount || 0 }}</div>
            <div class="stat-label">已通过</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card red">
          <div class="stat-icon">
            <el-icon><CircleClose /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.rejectedCount || 0 }}</div>
            <div class="stat-label">已拒绝</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="老人姓名">
          <el-input 
            v-model="searchForm.elderName" 
            placeholder="请输入老人姓名" 
            clearable 
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 130px">
            <el-option label="待审批" value="PENDING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
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
          <span>入住申请列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增申请
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="applicationNo" label="申请编号" width="120" />
        <el-table-column prop="elderName" label="老人姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.gender === 'MALE'" type="primary">男</el-tag>
            <el-tag v-else type="danger">女</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="careLevel" label="护理等级" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.careLevel === 'LEVEL1'">一级</el-tag>
            <el-tag v-else-if="row.careLevel === 'LEVEL2'" type="success">二级</el-tag>
            <el-tag v-else-if="row.careLevel === 'LEVEL3'" type="warning">三级</el-tag>
            <el-tag v-else type="danger">四级</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="期望入住日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.expectedDate) || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'PENDING'" type="info">待审批</el-tag>
            <el-tag v-else-if="row.status === 'APPROVED'" type="success">已通过</el-tag>
            <el-tag v-else-if="row.status === 'REJECTED'" type="danger">已拒绝</el-tag>
            <el-tag v-else type="warning">已取消</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="申请时间" width="150">
          <template #default="{ row }">
            {{ formatDateTime(row.applyTime) || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)" v-if="row.status === 'PENDING'">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
            <el-button type="success" link @click="handleApprove(row)" v-if="row.status === 'PENDING'">通过</el-button>
            <el-button type="danger" link @click="handleReject(row)" v-if="row.status === 'PENDING'">拒绝</el-button>
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

    <!-- 新增/编辑申请对话框 -->
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
            <el-form-item label="老人姓名" prop="elderName">
              <el-input v-model="form.elderName" placeholder="请输入老人姓名" />
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
              <el-input v-model="form.healthStatus" placeholder="请输入健康状况描述" />
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
            <el-form-item label="期望入住日期" prop="expectedDate">
              <el-date-picker
                v-model="form.expectedDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房型" prop="roomType">
              <el-select v-model="form.roomType" placeholder="选择房型" style="width: 100%">
                <el-option label="单人间" value="SINGLE" />
                <el-option label="双人间" value="DOUBLE" />
                <el-option label="三人间" value="TRIPLE" />
                <el-option label="套房" value="SUITE" />
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

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请人姓名" prop="applicantName">
              <el-input v-model="form.applicantName" placeholder="请输入申请人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请人电话" prop="applicantPhone">
              <el-input v-model="form.applicantPhone" placeholder="请输入申请人电话" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">提交申请</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="申请详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="申请编号">{{ currentRow.applicationNo }}</el-descriptions-item>
        <el-descriptions-item label="申请状态">
          <el-tag v-if="currentRow.status === 'PENDING'" type="info">待审批</el-tag>
          <el-tag v-else-if="currentRow.status === 'APPROVED'" type="success">已通过</el-tag>
          <el-tag v-else-if="currentRow.status === 'REJECTED'" type="danger">已拒绝</el-tag>
          <el-tag v-else type="warning">已取消</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ currentRow.elderName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ currentRow.gender === 'MALE' ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentRow.phone }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ currentRow.idCard || '-' }}</el-descriptions-item>
        <el-descriptions-item label="护理等级">{{ formatCareLevel(currentRow.careLevel) }}</el-descriptions-item>
        <el-descriptions-item label="期望入住日期">{{ formatDate(currentRow.expectedDate) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="房型">{{ formatRoomType(currentRow.roomType) }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ formatDateTime(currentRow.applyTime) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="家庭住址" :span="2">{{ currentRow.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系人">{{ currentRow.contactName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系人电话">{{ currentRow.contactPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="与老人关系">{{ currentRow.contactRelation || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请人姓名">{{ currentRow.applicantName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请人电话">{{ currentRow.applicantPhone || '-' }}</el-descriptions-item>
      </el-descriptions>
      
      <!-- 审批通过后显示房间床位分配信息 -->
      <div v-if="currentRow.status === 'APPROVED'" style="margin-top: 20px;">
        <el-divider content-position="left">房间床位分配信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="分配房间">{{ currentRow.roomId ? getRoomNameById(currentRow.roomId) : '-' }}</el-descriptions-item>
          <el-descriptions-item label="分配床位">{{ formatBedNo(currentRow.bedId) || '-' }}</el-descriptions-item>
          <el-descriptions-item label="实际入住日期">{{ formatDate(currentRow.checkInDate) || '-' }}</el-descriptions-item>
          <el-descriptions-item label="审批时间">{{ formatDateTime(currentRow.approveTime) || '-' }}</el-descriptions-item>
          <el-descriptions-item label="审批备注" :span="2">{{ currentRow.approveRemark || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog
      v-model="approveDialogVisible"
      title="审批申请"
      width="600px"
    >
      <el-form :model="approveForm" label-width="100px">
        <el-form-item label="申请信息">
          <div class="apply-info">
            <span><strong>老人：</strong>{{ currentRow.elderName }}</span>
            <span><strong>护理等级：</strong>{{ formatCareLevel(currentRow.careLevel) }}</span>
            <span><strong>期望房型：</strong>{{ formatRoomType(currentRow.roomType) }}</span>
          </div>
        </el-form-item>
        <el-form-item label="审批结果">
          <el-radio-group v-model="approveForm.action">
            <el-radio label="approve">通过</el-radio>
            <el-radio label="reject">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <!-- 通过时显示房间床位分配 -->
        <template v-if="approveForm.action === 'approve'">
          <el-form-item label="分配房间" required>
            <el-select 
              v-model="approveForm.roomId" 
              placeholder="选择房间" 
              style="width: 100%"
              @change="handleRoomChange"
            >
              <el-option 
                v-for="room in availableRooms" 
                :key="room.id" 
                :label="room.roomNumber + ' - ' + getRoomTypeText(room.roomType) + ' (' + room.occupied + '/' + room.capacity + '床)'" 
                :value="room.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="分配床位" required v-if="approveForm.roomId">
            <el-select v-model="approveForm.bedId" placeholder="选择床位" style="width: 100%">
              <el-option 
                v-for="bed in availableBeds" 
                :key="bed.id" 
                :label="bed.bedNumber" 
                :value="bed.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="入住日期" required>
            <el-date-picker 
              v-model="approveForm.checkInDate" 
              type="date" 
              placeholder="选择入住日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
        </template>
        
        <el-form-item label="审批备注">
          <el-input
            v-model="approveForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入审批备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleApproveSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Document, Timer, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import { getApplicationList, submitApplication, updateApplication, approveApplication, rejectApplication, deleteApplication } from '@/api/application'
import { getAvailableRooms } from '@/api/room'

export default {
  name: 'Application',
  components: {
    Search,
    Refresh,
    Plus,
    Document,
    Timer,
    CircleCheck,
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
    const approveDialogVisible = ref(false)
    const formRef = ref(null)
    const currentRow = ref({})
    const currentAction = ref('')
    const isEdit = ref(false)
    const dialogTitle = ref('新增申请')
    
    // 统计数据
    const statistics = ref({
      totalCount: 0,
      pendingCount: 0,
      approvedCount: 0,
      rejectedCount: 0
    })

    const searchForm = reactive({
      elderName: '',
      status: ''
    })

    const form = reactive({
      elderName: '',
      gender: 'MALE',
      birthDate: '',
      idCard: '',
      phone: '',
      address: '',
      contactName: '',
      contactPhone: '',
      contactRelation: '',
      healthStatus: '',
      careLevel: 'LEVEL1',
      expectedDate: '',
      roomType: 'SINGLE',
      applicantName: '',
      applicantPhone: ''
    })

    const approveForm = reactive({
      action: 'approve',
      remark: '',
      roomId: '',
      bedId: '',
      checkInDate: ''
    })
    
    // 可用房间列表
    const availableRooms = ref([])
    // 可用床位列表
    const availableBeds = ref([])

    const rules = {
      elderName: [{ required: true, message: '请输入老人姓名', trigger: 'blur' }],
      gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
      phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
      careLevel: [{ required: true, message: '请选择护理等级', trigger: 'change' }],
      expectedDate: [{ required: true, message: '请选择期望入住日期', trigger: 'change' }],
      roomType: [{ required: true, message: '请选择房型', trigger: 'change' }]
    }

    const fetchData = async () => {
      loading.value = true
      try {
        const params = {
          pageNum: pageNum.value,
          pageSize: pageSize.value
        }
        if (searchForm.elderName) params.elderName = searchForm.elderName
        if (searchForm.status) params.status = searchForm.status
        const res = await getApplicationList(params)
        if (res.code === 200) {
          tableData.value = res.data.list
          total.value = res.data.total
          // 更新统计数据
          calculateStatistics(res.data.list)
        }
      } catch (error) {
        console.error('获取申请列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    // 计算统计数据
    const calculateStatistics = (list) => {
      statistics.value.totalCount = list.length
      statistics.value.pendingCount = list.filter(item => item.status === 'PENDING').length
      statistics.value.approvedCount = list.filter(item => item.status === 'APPROVED').length
      statistics.value.rejectedCount = list.filter(item => item.status === 'REJECTED').length
    }

    // 日期格式化
    const formatDate = (date) => {
      if (!date) return ''
      // 处理数组格式 [2026, 3, 1]
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

    // 为日期选择器格式化日期
    const formatDateForPicker = (date) => {
      if (!date) return ''
      // 处理数组格式 [2026, 3, 1]
      if (Array.isArray(date)) {
        const [year, month, day] = date
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
      }
      // 处理字符串格式
      if (typeof date === 'string') {
        // 如果已经是 YYYY-MM-DD 格式，直接返回
        if (/^\d{4}-\d{2}-\d{2}$/.test(date)) {
          return date
        }
        // 处理 ISO 格式 2026-03-01T00:00:00
        return date.split('T')[0]
      }
      // 处理 Date 对象
      if (date instanceof Date) {
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
      }
      return date
    }

    // 日期时间格式化
    const formatDateTime = (dateTime) => {
      if (!dateTime) return ''
      // 处理数组格式 [2026, 3, 1, 13, 6, 38]
      if (Array.isArray(dateTime)) {
        const [year, month, day, hour, minute, second] = dateTime
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:${String(second).padStart(2, '0')}`
      }
      // 处理字符串格式
      if (typeof dateTime === 'string') {
        return dateTime.replace('T', ' ').substring(0, 19)
      }
      return dateTime
    }

    // 护理等级格式化
    const formatCareLevel = (level) => {
      const map = {
        'LEVEL1': '一级护理',
        'LEVEL2': '二级护理',
        'LEVEL3': '三级护理',
        'LEVEL4': '四级护理'
      }
      return map[level] || level
    }

    // 房型格式化
    const formatRoomType = (type) => {
      const map = {
        'SINGLE': '单人间',
        'DOUBLE': '双人间',
        'TRIPLE': '三人间',
        'MULTI': '多人间',
        'SUITE': '套房'
      }
      return map[type] || type
    }
    
    // 获取房间类型文本（用于审批对话框）
    const getRoomTypeText = (type) => {
      return formatRoomType(type)
    }
    
    // 根据房间ID获取房间名称（用于详情显示）
    const getRoomNameById = (roomId) => {
      const room = availableRooms.value.find(r => r.id == roomId)
      if (room) {
        return `${room.roomNumber} - ${getRoomTypeText(room.roomType)}`
      }
      return roomId
    }
    
    // 格式化床位号（统一显示为 "A床" 格式）
    const formatBedNo = (bedNo) => {
      if (!bedNo) return '-'
      const bn = bedNo.toString().trim()
      // 如果已经是 "A床" 格式，直接返回
      if (bn.endsWith('床')) return bn
      // 如果是 "A" 格式，转换为 "A床"
      if (bn.length === 1 && /^[A-H]$/i.test(bn)) return `${bn}床`
      // 如果是 "8-A" 或 "1-A" 格式，提取字母并转换为 "A床"
      if (bn.includes('-')) {
        const parts = bn.split('-')
        const letter = parts[parts.length - 1]
        if (letter && letter.length === 1 && /^[A-H]$/i.test(letter)) {
          return `${letter}床`
        }
      }
      return bn
    }
    
    // 获取可用房间列表
    const fetchAvailableRooms = async () => {
      try {
        // 从后端API获取真实可用房间
        const res = await getAvailableRooms()
        if (res.code === 200) {
          // 转换后端数据为前端格式
          let rooms = (res.data || []).map(room => ({
            id: room.id,
            roomNumber: room.roomNo,
            roomType: room.roomType,
            capacity: room.bedCount,
            occupied: room.occupiedBeds || 0
          }))
          
          // 如果有当前申请，根据期望房型筛选房间
          if (currentRow.value && currentRow.value.roomType) {
            const expectedRoomType = currentRow.value.roomType
            rooms = rooms.filter(room => room.roomType === expectedRoomType)
          }
          
          availableRooms.value = rooms
        } else {
          ElMessage.error(res.message || '获取可用房间失败')
        }
      } catch (error) {
        console.error('获取可用房间失败:', error)
        ElMessage.error('获取可用房间失败')
      }
    }
    
    // 房间选择变化时获取床位
    const handleRoomChange = (roomId) => {
      approveForm.bedId = ''
      // 根据选中的房间生成可用床位
      const room = availableRooms.value.find(r => r.id == roomId)
      if (room) {
        const beds = []
        const bedLetters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
        const occupiedCount = room.occupied || 0
        for (let i = occupiedCount; i < room.capacity; i++) {
          beds.push({
            id: bedLetters[i],  // 只存储床位字母，如 "A"
            bedNumber: `${bedLetters[i]}床`  // 显示为 "A床"
          })
        }
        availableBeds.value = beds
      }
    }

    const handleSearch = () => {
      pageNum.value = 1
      fetchData()
    }

    const resetSearch = () => {
      searchForm.elderName = ''
      searchForm.status = ''
      handleSearch()
    }

    const handleAdd = () => {
      resetForm()
      isEdit.value = false
      dialogTitle.value = '新增申请'
      dialogVisible.value = true
    }

    const handleView = (row) => {
      currentRow.value = row
      // 如果已审批，加载房间数据以显示房间名称
      if (row.status === 'APPROVED' && row.roomId) {
        fetchAvailableRooms()
      }
      viewDialogVisible.value = true
    }

    const handleEdit = (row) => {
      currentRow.value = row
      // 将行数据复制到表单
      Object.assign(form, row)
      // 确保日期格式正确
      if (row.birthDate) {
        form.birthDate = formatDateForPicker(row.birthDate)
      }
      if (row.expectedDate) {
        form.expectedDate = formatDateForPicker(row.expectedDate)
      }
      dialogVisible.value = true
      dialogTitle.value = '编辑申请'
      isEdit.value = true
    }

    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm('确认删除该申请吗？', '提示', {
          type: 'warning'
        })
        const res = await deleteApplication(row.id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          fetchData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
        }
      }
    }

    const handleApprove = (row) => {
      currentRow.value = row
      currentAction.value = 'approve'
      approveForm.action = 'approve'
      approveForm.remark = ''
      approveForm.roomId = ''
      approveForm.bedId = ''
      approveForm.checkInDate = ''
      fetchAvailableRooms()
      approveDialogVisible.value = true
    }

    const handleReject = (row) => {
      currentRow.value = row
      currentAction.value = 'reject'
      approveForm.action = 'reject'
      approveForm.remark = ''
      approveForm.roomId = ''
      approveForm.bedId = ''
      approveForm.checkInDate = ''
      approveDialogVisible.value = true
    }

    const handleApproveSubmit = async () => {
      // 如果是通过，验证必填项
      if (approveForm.action === 'approve') {
        if (!approveForm.roomId) {
          ElMessage.warning('请选择房间')
          return
        }
        if (!approveForm.bedId) {
          ElMessage.warning('请选择床位')
          return
        }
        if (!approveForm.checkInDate) {
          ElMessage.warning('请选择入住日期')
          return
        }
      }
      
      submitLoading.value = true
      try {
        const api = approveForm.action === 'approve' ? approveApplication : rejectApplication
        const params = {
          approveRemark: approveForm.remark
        }
        // 如果是通过，添加房间床位信息
        if (approveForm.action === 'approve') {
          params.roomId = approveForm.roomId
          // 将床位ID转换为标准格式（A -> A床）
          params.bedId = approveForm.bedId.length === 1 
            ? `${approveForm.bedId}床` 
            : approveForm.bedId
          params.checkInDate = approveForm.checkInDate
        }
        const res = await api(currentRow.value.id, params)
        if (res.code === 200) {
          ElMessage.success(approveForm.action === 'approve' ? '审批通过，房间床位已分配' : '已拒绝')
          approveDialogVisible.value = false
          fetchData()
        } else {
          ElMessage.error(res.message || '操作失败')
        }
      } catch (error) {
        console.error('审批失败:', error)
        ElMessage.error('审批失败: ' + (error.message || '未知错误'))
      } finally {
        submitLoading.value = false
      }
    }

    const handleSubmit = async () => {
      if (!formRef.value) return

      await formRef.value.validate(async (valid) => {
        if (valid) {
          submitLoading.value = true
          try {
            let res
            if (isEdit.value) {
              res = await updateApplication(form)
              if (res.code === 200) {
                ElMessage.success('申请更新成功')
              }
            } else {
              res = await submitApplication(form)
              if (res.code === 200) {
                ElMessage.success('申请提交成功')
              }
            }
            if (res.code === 200) {
              dialogVisible.value = false
              fetchData()
            }
          } catch (error) {
            console.error('提交失败:', error)
          } finally {
            submitLoading.value = false
          }
        }
      })
    }

    const resetForm = () => {
      form.elderName = ''
      form.gender = 'MALE'
      form.birthDate = ''
      form.idCard = ''
      form.phone = ''
      form.address = ''
      form.contactName = ''
      form.contactPhone = ''
      form.contactRelation = ''
      form.healthStatus = ''
      form.careLevel = 'LEVEL1'
      form.expectedDate = ''
      form.roomType = 'SINGLE'
      form.applicantName = ''
      form.applicantPhone = ''
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
      // 检查是否有从老人管理传递过来的入住申请信息
      const elderInfoStr = sessionStorage.getItem('applyCheckInElder')
      if (elderInfoStr) {
        const elderInfo = JSON.parse(elderInfoStr)
        // 自动填充老人信息到表单
        form.elderName = elderInfo.elderName || ''
        form.gender = elderInfo.gender || 'MALE'
        form.birthDate = elderInfo.birthDate || ''
        form.idCard = elderInfo.idCard || ''
        form.phone = elderInfo.phone || ''
        form.careLevel = elderInfo.careLevel || 'LEVEL1'
        form.healthStatus = elderInfo.healthStatus || ''
        // 清除 sessionStorage
        sessionStorage.removeItem('applyCheckInElder')
        // 打开新增申请对话框
        isEdit.value = false
        dialogTitle.value = '新增入住申请'
        dialogVisible.value = true
        ElMessage.success('已自动填充老人信息，请完善申请')
      }
    })

    return {
      loading,
      submitLoading,
      tableData,
      total,
      pageNum,
      pageSize,
      dialogVisible,
      viewDialogVisible,
      approveDialogVisible,
      formRef,
      searchForm,
      form,
      rules,
      currentRow,
      approveForm,
      isEdit,
      dialogTitle,
      statistics,
      availableRooms,
      availableBeds,
      formatDate,
      formatDateForPicker,
      formatDateTime,
      formatCareLevel,
      formatRoomType,
      getRoomTypeText,
      getRoomNameById,
      formatBedNo,
      handleRoomChange,
      handleSearch,
      resetSearch,
      handleAdd,
      handleView,
      handleEdit,
      handleDelete,
      handleApprove,
      handleReject,
      handleApproveSubmit,
      handleSubmit,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped lang="scss">
.application-container {
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
    
    &.red {
      border-left: 4px solid #F56C6C;
      .stat-icon { background: linear-gradient(135deg, #F56C6C, #FAB6B6); }
      .stat-value { color: #F56C6C; }
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
  
  // 审批对话框样式
  .apply-info {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    padding: 10px;
    background: #f5f7fa;
    border-radius: 8px;
    
    span {
      font-size: 14px;
      color: #606266;
      
      strong {
        color: #303133;
      }
    }
  }
}
</style>
