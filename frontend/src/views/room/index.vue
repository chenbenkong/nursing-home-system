<template>
  <div class="room-container">
    <!-- 文艺动画背景 -->
    <ArtisticBackground />
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <el-icon><House /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">房间总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.availableCount || 0 }}</div>
            <div class="stat-label">空闲房间</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.occupiedCount || 0 }}</div>
            <div class="stat-label">已住满</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card red">
          <div class="stat-icon">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statistics.maintenanceCount || 0 }}</div>
            <div class="stat-label">维修中</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="房间号">
          <el-input v-model="searchForm.roomNumber" placeholder="请输入房间号" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="楼层">
          <el-select v-model="searchForm.floor" placeholder="选择楼层" clearable style="width: 120px">
            <el-option v-for="floor in floorOptions" :key="floor" :label="floor + '楼'" :value="floor" />
          </el-select>
        </el-form-item>
        <el-form-item label="房间类型">
          <el-select v-model="searchForm.roomType" placeholder="选择类型" clearable style="width: 140px">
            <el-option label="单人间" value="SINGLE" />
            <el-option label="双人间" value="DOUBLE" />
            <el-option label="三人间" value="TRIPLE" />
            <el-option label="多人间" value="MULTI" />
            <el-option label="套房" value="SUITE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable style="width: 120px">
            <el-option label="空闲" value="AVAILABLE" />
            <el-option label="部分占用" value="PARTIAL" />
            <el-option label="已满" value="FULL" />
            <el-option label="维修中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>查询
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
          <el-button type="success" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增房间
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 房间列表 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>房间列表</span>
          <el-radio-group v-model="viewMode" size="small">
            <el-radio-button label="list">列表视图</el-radio-button>
            <el-radio-button label="floor">楼层视图</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <!-- 列表视图 -->
      <el-table v-if="viewMode === 'list'" :data="roomList" v-loading="loading" stripe>
        <el-table-column prop="roomNumber" label="房间号" width="100" />
        <el-table-column prop="floor" label="楼层" width="80">
          <template #default="{ row }">{{ row.floor }}楼</template>
        </el-table-column>
        <el-table-column prop="roomType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.roomType === 'SINGLE'">单人间</el-tag>
            <el-tag v-else-if="row.roomType === 'DOUBLE'" type="success">双人间</el-tag>
            <el-tag v-else-if="row.roomType === 'TRIPLE'" type="warning">三人间</el-tag>
            <el-tag v-else-if="row.roomType === 'MULTI'" type="info">多人间</el-tag>
            <el-tag v-else type="danger">套房</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="capacity" label="容量" width="80">
          <template #default="{ row }">{{ row.occupied }}/{{ row.capacity }}床</template>
        </el-table-column>
        <el-table-column prop="facilities" label="设施" min-width="200">
          <template #default="{ row }">
            <el-tag v-for="facility in row.facilities" :key="facility" size="small" class="facility-tag">
              {{ facility }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'AVAILABLE'" type="success">空闲</el-tag>
            <el-tag v-else-if="row.status === 'PARTIAL'" type="warning">部分占用</el-tag>
            <el-tag v-else-if="row.status === 'FULL' || row.status === 'OCCUPIED'" type="danger">已满</el-tag>
            <el-tag v-else-if="row.status === 'MAINTENANCE'" type="info">维修中</el-tag>
            <el-tag v-else type="info">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">¥{{ row.price }}/月</template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 楼层视图 -->
      <div v-else class="floor-view">
        <div v-for="floor in floorList" :key="floor" class="floor-section">
          <div class="floor-title">{{ floor }}楼</div>
          <div class="floor-rooms">
            <div 
              v-for="room in getRoomsByFloor(floor)" 
              :key="room.id"
              class="room-card"
              :class="room.status.toLowerCase()"
              @click="handleView(room)"
            >
              <div class="room-number">{{ room.roomNumber }}</div>
              <div class="room-type">{{ getRoomTypeText(room.roomType) }}</div>
              <div class="room-capacity">{{ room.occupied }}/{{ room.capacity }}床</div>
              <div class="room-status">
                <el-tag :type="getStatusType(room.status)" size="small">
                  {{ getStatusText(room.status) }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>

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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房间号" prop="roomNumber">
              <el-input v-model="form.roomNumber" placeholder="如：101" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="楼层" prop="floor">
              <el-input-number v-model="form.floor" :min="1" :max="20" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房间类型" prop="roomType">
              <el-select v-model="form.roomType" placeholder="选择类型" style="width: 100%" @change="handleRoomTypeChange">
                <el-option label="单人间" value="SINGLE" />
                <el-option label="双人间" value="DOUBLE" />
                <el-option label="三人间" value="TRIPLE" />
                <el-option label="多人间" value="MULTI" />
                <el-option label="套房" value="SUITE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="容量" prop="capacity">
              <el-input-number 
                v-model="form.capacity" 
                :min="1" 
                :max="10" 
                style="width: 100%" 
                :disabled="isFixedCapacity"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="选择状态" style="width: 100%">
                <el-option label="空闲" value="AVAILABLE" />
                <el-option label="维修中" value="MAINTENANCE" />
                <!-- 编辑时如果状态是OCCUPIED/PARTIAL/FULL，显示但禁用选择 -->
                <el-option v-if="form.status === 'OCCUPIED'" label="已占用" value="OCCUPIED" disabled />
                <el-option v-if="form.status === 'PARTIAL'" label="部分占用" value="PARTIAL" disabled />
                <el-option v-if="form.status === 'FULL'" label="已满" value="FULL" disabled />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="设施" prop="facilities">
          <el-checkbox-group v-model="form.facilities">
            <el-checkbox label="独立卫生间">独立卫生间</el-checkbox>
            <el-checkbox label="空调">空调</el-checkbox>
            <el-checkbox label="电视">电视</el-checkbox>
            <el-checkbox label="阳台">阳台</el-checkbox>
            <el-checkbox label="暖气">暖气</el-checkbox>
            <el-checkbox label="紧急呼叫">紧急呼叫</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="房间详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="房间号">{{ currentRow.roomNumber }}</el-descriptions-item>
        <el-descriptions-item label="楼层">{{ currentRow.floor }}楼</el-descriptions-item>
        <el-descriptions-item label="类型">{{ getRoomTypeText(currentRow.roomType) }}</el-descriptions-item>
        <el-descriptions-item label="容量">{{ currentRow.occupied }}/{{ currentRow.capacity }}床</el-descriptions-item>
        <el-descriptions-item label="价格">¥{{ currentRow.price }}/月</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(getComputedRoomStatus(currentRow))">{{ getStatusText(getComputedRoomStatus(currentRow)) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="设施" :span="2">
          <el-tag v-for="facility in currentRow.facilities" :key="facility" size="small" class="facility-tag">
            {{ facility }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark || '暂无' }}</el-descriptions-item>
      </el-descriptions>
      
      <div class="bed-section">
        <div class="bed-title">床位分布</div>
        <div class="bed-list">
          <div 
            v-for="bed in currentRow.beds" 
            :key="bed.id"
            class="bed-item"
            :class="bed.status.toLowerCase()"
          >
            <div class="bed-number">{{ bed.bedNumber }}</div>
            <div class="bed-status">{{ getBedStatusText(bed.status) }}</div>
            <div v-if="bed.elderName" class="bed-occupant">
              <el-button type="primary" link @click="showElderDetail(bed.elderId)">{{ bed.elderName }}</el-button>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 老人详情卡片对话框 -->
    <el-dialog
      v-model="elderDetailVisible"
      title="老人详情"
      width="700px"
    >
      <el-descriptions :column="2" border v-if="currentElder">
        <el-descriptions-item label="老人编号">{{ currentElder.elderNo }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ currentElder.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ currentElder.gender === 'MALE' ? '男' : '女' }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ currentElder.age }}岁</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ formatDate(currentElder.birthDate) }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ currentElder.idCard }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentElder.phone }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ currentElder.contactName }}</el-descriptions-item>
        <el-descriptions-item label="联系人电话">{{ currentElder.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="与老人关系">{{ currentElder.contactRelation }}</el-descriptions-item>
        <el-descriptions-item label="健康状况">
          <el-tag v-if="currentElder.healthStatus === 'HEALTHY'" type="success">健康</el-tag>
          <el-tag v-else-if="currentElder.healthStatus === 'SUBHEALTH'" type="warning">亚健康</el-tag>
          <el-tag v-else-if="currentElder.healthStatus === 'CHRONIC'" type="info">慢性病</el-tag>
          <el-tag v-else type="danger">失能</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="护理等级">
          <el-tag v-if="currentElder.careLevel === 'LEVEL1'">一级</el-tag>
          <el-tag v-else-if="currentElder.careLevel === 'LEVEL2'" type="success">二级</el-tag>
          <el-tag v-else-if="currentElder.careLevel === 'LEVEL3'" type="warning">三级</el-tag>
          <el-tag v-else type="danger">四级</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentElder.status === 'PENDING'" type="info">待入住</el-tag>
          <el-tag v-else-if="currentElder.status === 'CHECKED_IN'" type="success">已入住</el-tag>
          <el-tag v-else-if="currentElder.status === 'CHECKED_OUT'" type="danger">已退住</el-tag>
          <el-tag v-else type="warning">暂停</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="入住日期">{{ formatDate(currentElder.checkInDate) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="床号">{{ currentElder.bedNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ currentElder.address }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="elderDetailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, House, CircleCheck, User, Warning } from '@element-plus/icons-vue'
import { getRoomList, addRoom, updateRoom, deleteRoom, getAvailableRooms, getRoomById } from '@/api/room'
import ArtisticBackground from '@/components/ArtisticBackground.vue'

export default {
  name: 'Room',
  components: {
    ArtisticBackground,
    Search, Refresh, Plus, House, CircleCheck, User, Warning
  },
  setup() {
    const loading = ref(false)
    const submitLoading = ref(false)
    const roomList = ref([])
    const total = ref(0)
    const pageNum = ref(1)
    const pageSize = ref(10)
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const dialogTitle = ref('')
    const formRef = ref(null)
    const isEdit = ref(false)
    const currentRow = ref({})
    const viewMode = ref('list')
    
    // 老人详情对话框
    const elderDetailVisible = ref(false)
    const currentElder = ref(null)

    // 房间类型与容量的映射关系
    const roomTypeCapacityMap = {
      'SINGLE': 1,   // 单人间固定1人
      'DOUBLE': 2,   // 双人间固定2人
      'TRIPLE': 3,   // 三人间固定3人
      'MULTI': 4,    // 多人间默认4人（可调整）
      'SUITE': 2     // 套房默认2人（可调整）
    }

    // 计算是否固定容量（单人间、双人间、三人间固定，多人间和套房可调整）
    const isFixedCapacity = computed(() => {
      return ['SINGLE', 'DOUBLE', 'TRIPLE'].includes(form.roomType)
    })

    const statistics = ref({
      totalCount: 0,
      availableCount: 0,
      occupiedCount: 0,
      maintenanceCount: 0
    })

    const searchForm = reactive({
      roomNumber: '',
      floor: '',
      roomType: '',
      status: ''
    })

    const form = reactive({
      id: null,
      roomNumber: '',
      floor: 1,
      roomType: 'DOUBLE',
      capacity: 2,
      price: 3000,
      status: 'AVAILABLE',
      facilities: [],
      remark: ''
    })

    const rules = {
      roomNumber: [{ required: true, message: '请输入房间号', trigger: 'blur' }],
      floor: [{ required: true, message: '请输入楼层', trigger: 'blur' }],
      roomType: [{ required: true, message: '请选择房间类型', trigger: 'change' }],
      capacity: [{ required: true, message: '请输入容量', trigger: 'blur' }],
      price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
    }

    // 楼层选项
    const floorOptions = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
    }

    // 计算楼层列表
    const floorList = computed(() => {
      const floors = [...new Set(roomList.value.map(r => r.floor))]
      return floors.sort((a, b) => a - b)
    })

    // 获取房间类型文本
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

    // 获取状态文本
    const getStatusText = (status) => {
      const map = {
        'AVAILABLE': '空闲',
        'PARTIAL': '部分占用',
        'FULL': '已满',
        'OCCUPIED': '已满',
        'MAINTENANCE': '维修中'
      }
      return map[status] || status
    }

    // 获取状态类型
    const getStatusType = (status) => {
      const map = {
        'AVAILABLE': 'success',
        'PARTIAL': 'warning',
        'FULL': 'danger',
        'OCCUPIED': 'danger',
        'MAINTENANCE': 'info'
      }
      return map[status] || ''
    }

    // 根据实际入住情况计算房间状态
    const getComputedRoomStatus = (room) => {
      if (!room) return 'AVAILABLE'
      const occupied = room.occupied || 0
      const capacity = room.capacity || 0
      
      if (occupied === 0) {
        return 'AVAILABLE'
      } else if (occupied >= capacity) {
        return 'FULL'
      } else {
        return 'PARTIAL'
      }
    }

    // 获取床位状态文本
    const getBedStatusText = (status) => {
      const map = {
        'AVAILABLE': '空闲',
        'OCCUPIED': '已占用',
        'MAINTENANCE': '维修中'
      }
      return map[status] || status
    }

    // 显示老人详情卡片
    const showElderDetail = (elderId) => {
      if (elderId && currentRow.value.elders) {
        const elder = currentRow.value.elders.find(e => e.id === elderId)
        if (elder) {
          currentElder.value = elder
          elderDetailVisible.value = true
        }
      }
    }

    // 按楼层获取房间
    const getRoomsByFloor = (floor) => {
      return roomList.value.filter(r => r.floor === floor)
    }

    // 模拟数据
    const mockData = [
      { id: 1, roomNumber: '101', floor: 1, roomType: 'DOUBLE', capacity: 2, occupied: 2, price: 3000, status: 'FULL', facilities: ['独立卫生间', '空调', '电视'], beds: [{id: 1, bedNumber: 'A床', status: 'OCCUPIED', elderName: '张大爷'}, {id: 2, bedNumber: 'B床', status: 'OCCUPIED', elderName: '李奶奶'}] },
      { id: 2, roomNumber: '102', floor: 1, roomType: 'SINGLE', capacity: 1, occupied: 0, price: 4500, status: 'AVAILABLE', facilities: ['独立卫生间', '空调', '电视', '阳台'], beds: [{id: 3, bedNumber: 'A床', status: 'AVAILABLE'}] },
      { id: 3, roomNumber: '103', floor: 1, roomType: 'TRIPLE', capacity: 3, occupied: 1, price: 2500, status: 'PARTIAL', facilities: ['独立卫生间', '空调'], beds: [{id: 4, bedNumber: 'A床', status: 'OCCUPIED', elderName: '王大爷'}, {id: 5, bedNumber: 'B床', status: 'AVAILABLE'}, {id: 6, bedNumber: 'C床', status: 'AVAILABLE'}] },
      { id: 4, roomNumber: '201', floor: 2, roomType: 'DOUBLE', capacity: 2, occupied: 0, price: 3200, status: 'AVAILABLE', facilities: ['独立卫生间', '空调', '电视', '暖气'], beds: [{id: 7, bedNumber: 'A床', status: 'AVAILABLE'}, {id: 8, bedNumber: 'B床', status: 'AVAILABLE'}] },
      { id: 5, roomNumber: '202', floor: 2, roomType: 'SUITE', capacity: 2, occupied: 1, price: 6000, status: 'PARTIAL', facilities: ['独立卫生间', '空调', '电视', '阳台', '暖气'], beds: [{id: 9, bedNumber: 'A床', status: 'OCCUPIED', elderName: '赵奶奶'}, {id: 10, bedNumber: 'B床', status: 'AVAILABLE'}] },
      { id: 6, roomNumber: '301', floor: 3, roomType: 'MULTI', capacity: 4, occupied: 4, price: 2000, status: 'FULL', facilities: ['独立卫生间', '空调'], beds: [{id: 11, bedNumber: 'A床', status: 'OCCUPIED', elderName: '钱大爷'}, {id: 12, bedNumber: 'B床', status: 'OCCUPIED', elderName: '孙奶奶'}, {id: 13, bedNumber: 'C床', status: 'OCCUPIED', elderName: '周大爷'}, {id: 14, bedNumber: 'D床', status: 'OCCUPIED', elderName: '吴奶奶'}] },
      { id: 7, roomNumber: '302', floor: 3, roomType: 'DOUBLE', capacity: 2, occupied: 0, price: 2800, status: 'MAINTENANCE', facilities: ['独立卫生间', '空调', '电视'], beds: [{id: 15, bedNumber: 'A床', status: 'MAINTENANCE'}, {id: 16, bedNumber: 'B床', status: 'MAINTENANCE'}] }
    ]

    const fetchData = async () => {
      loading.value = true
      try {
        // 从后端API获取真实数据
        const res = await getRoomList({
          pageNum: pageNum.value,
          pageSize: pageSize.value,
          ...searchForm
        })
        
        if (res.code === 200) {
          // 处理后端返回的数据，将字段名转换为前端使用的格式
          const rooms = (res.data.list || res.data).map(room => ({
            id: room.id,
            roomNumber: room.roomNo,
            floor: room.floor,
            roomType: room.roomType,
            capacity: room.bedCount,
            occupied: room.occupiedBeds || 0,
            price: room.price,
            status: room.status,
            facilities: room.facilities ? room.facilities.split(/[,，、]/).map(f => f.trim()).filter(f => f) : [],
            remark: room.remark,
            // 生成床位数据（根据实际老人信息）
            beds: generateBedsWithElders(room.id, room.roomNo, room.bedCount, room.occupiedBeds || 0, room.elders || [])
          }))
          
          roomList.value = rooms
          total.value = res.data.total || rooms.length
          calculateStatistics(rooms)
        } else {
          ElMessage.error(res.message || '获取房间列表失败')
        }
      } catch (error) {
        console.error('获取房间列表失败:', error)
        ElMessage.error('获取房间列表失败')
      } finally {
        loading.value = false
      }
    }
    
    // 生成床位数据
    const generateBeds = (roomId, roomNo, capacity, occupied) => {
      const beds = []
      const bedLetters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
      for (let i = 0; i < capacity; i++) {
        beds.push({
          id: `${roomId}-${bedLetters[i]}`,
          bedNumber: `${bedLetters[i]}床`,
          status: i < occupied ? 'OCCUPIED' : 'AVAILABLE'
        })
      }
      return beds
    }

    // 生成床位数据并关联老人信息
    const generateBedsWithElders = (roomId, roomNo, capacity, occupied, elders) => {
      const beds = []
      const bedLetters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
      for (let i = 0; i < capacity; i++) {
        const bedNo = `${bedLetters[i]}床`
        const bedLetter = bedLetters[i]
        // 查找该床位的老人（支持多种格式：A、A床、8-A、1-A床等）
        const elder = elders ? elders.find(e => {
          if (!e.bedNo) return false
          const bn = e.bedNo.toString().trim()
          // 匹配：A、A床、8-A、1-A床、8A等格式
          return bn === bedLetter || 
                 bn === bedNo || 
                 bn.endsWith(`-${bedLetter}`) ||
                 bn.endsWith(`-${bedNo}`) ||
                 (bn.endsWith(bedLetter) && bn.includes('-'))
        }) : null
        // 根据是否找到老人来确定床位状态，而不是根据occupied数量
        beds.push({
          id: `${roomId}-${bedLetters[i]}`,
          bedNumber: bedNo,
          status: elder ? 'OCCUPIED' : 'AVAILABLE',
          elderName: elder ? elder.name : null,
          elderId: elder ? elder.id : null
        })
      }
      return beds
    }

    const calculateStatistics = (list) => {
      statistics.value.totalCount = list.length
      statistics.value.availableCount = list.filter(item => item.status === 'AVAILABLE').length
      statistics.value.occupiedCount = list.filter(item => item.status === 'FULL').length
      statistics.value.maintenanceCount = list.filter(item => item.status === 'MAINTENANCE').length
    }

    const handleSearch = () => {
      pageNum.value = 1
      fetchData()
    }

    const resetSearch = () => {
      searchForm.roomNumber = ''
      searchForm.floor = ''
      searchForm.roomType = ''
      searchForm.status = ''
      handleSearch()
    }

    const resetForm = () => {
      form.id = null
      form.roomNumber = ''
      form.floor = 1
      form.roomType = 'DOUBLE'
      form.capacity = roomTypeCapacityMap['DOUBLE']  // 使用映射值
      form.price = 3000
      form.status = 'AVAILABLE'
      form.facilities = []
      form.remark = ''
    }

    const handleAdd = () => {
      isEdit.value = false
      dialogTitle.value = '新增房间'
      resetForm()
      dialogVisible.value = true
    }

    const handleView = async (row) => {
      // 获取房间详情（包含老人信息）
      try {
        const res = await getRoomById(row.id)
        if (res.code === 200) {
          const roomData = res.data
          // 转换数据格式
          currentRow.value = {
            id: roomData.id,
            roomNumber: roomData.roomNo,
            floor: roomData.floor,
            roomType: roomData.roomType,
            capacity: roomData.bedCount,
            occupied: roomData.occupiedBeds,
            price: roomData.price,
            status: roomData.status,
            facilities: roomData.facilities ? roomData.facilities.split(/[,，、]/).map(f => f.trim()).filter(f => f) : [],
            remark: roomData.remark,
            elders: roomData.elders || [],
            // 生成床位数据并关联老人信息
            beds: generateBedsWithElders(roomData.id, roomData.roomNo, roomData.bedCount, roomData.occupiedBeds, roomData.elders)
          }
        } else {
          // 如果接口失败，使用原有数据
          currentRow.value = row
        }
      } catch (error) {
        console.error('获取房间详情失败:', error)
        currentRow.value = row
      }
      viewDialogVisible.value = true
    }

    // 房间类型改变时自动设置容量
    const handleRoomTypeChange = (type) => {
      if (roomTypeCapacityMap[type]) {
        form.capacity = roomTypeCapacityMap[type]
      }
    }

    const handleEdit = (row) => {
      isEdit.value = true
      dialogTitle.value = '编辑房间'
      
      // 深拷贝避免引用问题，并确保设施是数组，去除空格
      // 支持英文逗号和中文顿号两种分隔符
      let facilities = []
      if (Array.isArray(row.facilities)) {
        facilities = row.facilities.map(f => f.trim()).filter(f => f)
      } else if (typeof row.facilities === 'string') {
        facilities = row.facilities.split(/[,，、]/).map(f => f.trim()).filter(f => f)
      }
      
      Object.assign(form, {
        id: row.id,
        roomNumber: row.roomNumber,
        floor: row.floor,
        roomType: row.roomType,
        capacity: row.capacity,
        price: row.price,
        status: row.status,
        facilities: facilities,
        remark: row.remark
      })
      
      dialogVisible.value = true
    }

    const handleManageBeds = (row) => {
      // 跳转到床位管理页面
      // this.$router.push(`/bed?roomId=${row.id}`)
      ElMessage.info('床位管理功能开发中')
    }

    const handleDelete = (row) => {
      ElMessageBox.confirm('确定要删除该房间吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteRoom(row.id)
          if (res.code === 200) {
            ElMessage.success('删除成功')
            fetchData()
          } else {
            ElMessage.error(res.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败')
        }
      }).catch(() => {})
    }

    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      submitLoading.value = true
      try {
        // 转换表单数据为后端格式
        const roomData = {
          roomNo: form.roomNumber,
          roomType: form.roomType,
          floor: form.floor,
          bedCount: form.capacity,
          price: form.price,
          status: form.status,
          facilities: form.facilities.join('、'),
          remark: form.remark
        }
        
        if (isEdit.value) {
          roomData.id = form.id
          const res = await updateRoom(roomData)
          if (res.code === 200) {
            ElMessage.success('编辑成功')
            dialogVisible.value = false
            fetchData()
          } else {
            ElMessage.error(res.message || '编辑失败')
          }
        } else {
          const res = await addRoom(roomData)
          if (res.code === 200) {
            ElMessage.success('新增成功')
            dialogVisible.value = false
            fetchData()
          } else {
            ElMessage.error(res.message || '新增失败')
          }
        }
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      } finally {
        submitLoading.value = false
      }
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

    return {
      loading,
      submitLoading,
      roomList,
      total,
      pageNum,
      pageSize,
      dialogVisible,
      viewDialogVisible,
      dialogTitle,
      formRef,
      isEdit,
      currentRow,
      viewMode,
      elderDetailVisible,
      currentElder,
      statistics,
      searchForm,
      form,
      rules,
      floorOptions,
      floorList,
      formatDate,
      getRoomTypeText,
      getStatusText,
      getStatusType,
      getComputedRoomStatus,
      getBedStatusText,
      showElderDetail,
      generateBedsWithElders,
      getRoomsByFloor,
      handleSearch,
      resetSearch,
      handleAdd,
      handleView,
      handleEdit,
      handleManageBeds,
      handleDelete,
      handleSubmit,
      handleSizeChange,
      handleCurrentChange,
      handleRoomTypeChange,
      isFixedCapacity
    }
  }
}
</script>

<style scoped lang="scss">
.room-container {
  padding: 20px;

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

    &.blue { border-left: 4px solid #409EFF; .stat-icon { background: linear-gradient(135deg, #409EFF, #79BBFF); } .stat-value { color: #409EFF; } }
    &.green { border-left: 4px solid #67C23A; .stat-icon { background: linear-gradient(135deg, #67C23A, #95D475); } .stat-value { color: #67C23A; } }
    &.orange { border-left: 4px solid #E6A23C; .stat-icon { background: linear-gradient(135deg, #E6A23C, #F3D19E); } .stat-value { color: #E6A23C; } }
    &.red { border-left: 4px solid #F56C6C; .stat-icon { background: linear-gradient(135deg, #F56C6C, #FAB6B6); } .stat-value { color: #F56C6C; } }
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
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .facility-tag {
      margin-right: 5px;
      margin-bottom: 5px;
    }
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }

  // 楼层视图
  .floor-view {
    .floor-section {
      margin-bottom: 30px;

      .floor-title {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 15px;
        padding-left: 10px;
        border-left: 4px solid #409EFF;
      }

      .floor-rooms {
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
      }
    }
  }

  .room-card {
    width: 150px;
    padding: 15px;
    border-radius: 12px;
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    cursor: pointer;
    transition: all 0.3s;
    text-align: center;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.1);
    }

    &.available {
      border-top: 4px solid #67C23A;
    }

    &.partial {
      border-top: 4px solid #E6A23C;
    }

    &.full {
      border-top: 4px solid #F56C6C;
    }

    &.maintenance {
      border-top: 4px solid #909399;
      opacity: 0.7;
    }

    .room-number {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 5px;
    }

    .room-type {
      font-size: 12px;
      color: #909399;
      margin-bottom: 5px;
    }

    .room-capacity {
      font-size: 14px;
      color: #606266;
      margin-bottom: 8px;
    }

    .room-status {
      margin-top: 5px;
    }
  }

  // 床位展示
  .bed-section {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #EBEEF5;

    .bed-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 15px;
    }

    .bed-list {
      display: flex;
      flex-wrap: wrap;
      gap: 15px;
    }

    .bed-item {
      width: 100px;
      padding: 15px;
      border-radius: 8px;
      text-align: center;

      &.available {
        background: #f0f9eb;
        border: 1px solid #67C23A;
      }

      &.occupied {
        background: #fef0f0;
        border: 1px solid #F56C6C;
      }

      &.maintenance {
        background: #f4f4f5;
        border: 1px solid #909399;
      }

      .bed-number {
        font-size: 16px;
        font-weight: 600;
        margin-bottom: 5px;
      }

      .bed-status {
        font-size: 12px;
        color: #909399;
        margin-bottom: 5px;
      }

      .bed-occupant {
        font-size: 12px;
        color: #F56C6C;
      }
    }
  }
}
</style>