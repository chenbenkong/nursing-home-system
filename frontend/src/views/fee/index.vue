<template>
  <div class="fee-management">
    <el-card class="statistics-card">
      <template #header>
        <div class="card-header">
          <span>费用统计</span>
          <el-date-picker
            v-model="selectedMonth"
            type="month"
            placeholder="选择月份"
            format="YYYY-MM"
            value-format="YYYY-MM"
            @change="loadStatistics"
          />
        </div>
      </template>
      <!-- 缴费统计 -->
      <el-row :gutter="20" class="stat-row">
        <el-col :span="6">
          <div class="stat-item primary">
            <div class="stat-value">{{ statistics.totalAmount || '0.00' }}</div>
            <div class="stat-label">应收总额（元）</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item success">
            <div class="stat-value">{{ statistics.paidAmount || '0.00' }}</div>
            <div class="stat-label">已收金额（元）</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item warning">
            <div class="stat-value">{{ statistics.unpaidAmount || '0.00' }}</div>
            <div class="stat-label">未收金额（元）</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item danger">
            <div class="stat-value">{{ statistics.overdueElders || 0 }}</div>
            <div class="stat-label">欠费老人数</div>
          </div>
        </el-col>
      </el-row>
      <!-- 退款统计 -->
      <el-row :gutter="20" class="stat-row refund-stat-row">
        <el-col :span="6">
          <div class="stat-item refund">
            <div class="stat-value">{{ statistics.refundAmount || '0.00' }}</div>
            <div class="stat-label">应退总额（元）</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item refund-success">
            <div class="stat-value">{{ statistics.refundedAmount || '0.00' }}</div>
            <div class="stat-label">已退金额（元）</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item refund-warning">
            <div class="stat-value">{{ statistics.unrefundedAmount || '0.00' }}</div>
            <div class="stat-label">未退金额（元）</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item refund-danger">
            <div class="stat-value">{{ statistics.unrefundedElders || 0 }}</div>
            <div class="stat-label">未退款老人数</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>费用记录</span>
          <el-button type="primary" @click="handleAdd">新增费用</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="费用类型">
          <el-select v-model="queryParams.feeType" placeholder="全部" clearable popper-class="fee-select-dropdown" style="width: 130px">
            <el-option label="住宿费" value="ACCOMMODATION" />
            <el-option label="医疗费" value="MEDICAL" />
            <el-option label="物资费" value="SUPPLY" />
            <el-option label="其他费用" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="缴费状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable popper-class="fee-select-dropdown" style="width: 130px">
            <el-option label="未缴费" value="UNPAID" />
            <el-option label="部分缴费" value="PARTIAL" />
            <el-option label="已缴清" value="PAID" />
            <el-option label="已逾期" value="OVERDUE" />
            <el-option label="未退款" value="UNREFUNDED" />
            <el-option label="已退款" value="REFUNDED" />
            <el-option label="部分退款" value="PARTIAL_REFUNDED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadFeeList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="feeList" v-loading="loading" stripe>
        <el-table-column prop="recordNo" label="记录编号" width="150" v-if="false" />
        <el-table-column prop="elderName" label="老人姓名" width="100" />
        <el-table-column prop="feeType" label="费用类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getFeeTypeTag(row.feeType)">{{ getFeeTypeLabel(row.feeType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="feeMonth" label="费用月份" width="100" v-if="false" />
        <!-- 缴费列 -->
        <el-table-column prop="amount" label="应收金额" width="100">
          <template #default="{ row }">
            <span v-if="parseFloat(row.amount) > 0">¥{{ row.amount }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已收金额" width="100">
          <template #default="{ row }">
            <span v-if="parseFloat(row.amount) > 0">¥{{ row.paidAmount || '0.00' }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <!-- 退款列 -->
        <el-table-column label="应退金额" width="100">
          <template #default="{ row }">
            <span v-if="parseFloat(row.amount) < 0" class="refund-amount">¥{{ Math.abs(parseFloat(row.amount)).toFixed(2) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="已退金额" width="100">
          <template #default="{ row }">
            <span v-if="parseFloat(row.amount) < 0" class="refund-amount">¥{{ row.paidAmount || '0.00' }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="截止日期" width="120" />
        <el-table-column prop="payTime" label="缴费/退款时间" width="160" />
        <el-table-column label="操作" fixed="right" width="220">
          <template #default="{ row }">
            <el-button type="info" link @click="handleView(row)">查看</el-button>
            <el-button 
              type="primary" 
              link 
              @click="handlePay(row)" 
              v-if="row.status !== 'PAID' && parseFloat(row.amount) > 0"
            >缴费</el-button>
            <el-button 
              type="success" 
              link 
              @click="handleRefund(row)" 
              v-if="(row.status === 'UNREFUNDED' || row.status === 'PARTIAL_REFUNDED' || (row.status === 'UNPAID' && parseFloat(row.amount) < 0))"
            >退款</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="老人" prop="elderId">
          <el-select v-model="form.elderId" placeholder="请选择老人" filterable @change="handleElderChange">
            <el-option
              v-for="elder in elderList"
              :key="elder.id"
              :label="elder.name"
              :value="elder.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="费用类型" prop="feeType">
          <el-select v-model="form.feeType" placeholder="请选择费用类型" popper-class="fee-select-dropdown">
            <el-option label="住宿费" value="ACCOMMODATION" />
            <el-option label="医疗费" value="MEDICAL" />
            <el-option label="物资费" value="SUPPLY" />
            <el-option label="其他费用" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="费用月份" prop="feeMonth">
          <el-date-picker
            v-model="form.feeMonth"
            type="month"
            placeholder="选择月份"
            format="YYYY-MM"
            value-format="YYYY-MM"
          />
        </el-form-item>
        <!-- 缴费记录显示 -->
        <template v-if="parseFloat(form.amount) > 0">
          <el-form-item label="应收金额">
            <span class="amount-display">¥{{ form.amount }}</span>
          </el-form-item>
          <el-form-item label="已收金额" prop="paidAmount">
            <el-input-number 
              v-model="form.paidAmount" 
              :min="0" 
              :max="parseFloat(form.amount) || 0" 
              :precision="2" 
            />
          </el-form-item>
        </template>
        <!-- 退款记录显示 -->
        <template v-else>
          <el-form-item label="应退金额">
            <span class="amount-display refund-amount">¥{{ Math.abs(parseFloat(form.amount) || 0).toFixed(2) }}</span>
          </el-form-item>
          <el-form-item label="已退金额" prop="paidAmount">
            <el-input-number 
              v-model="form.paidAmount" 
              :min="0" 
              :max="Math.abs(parseFloat(form.amount) || 0)" 
              :precision="2" 
            />
          </el-form-item>
        </template>
        <el-form-item label="截止日期" prop="dueDate">
          <el-date-picker v-model="form.dueDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="payDialogVisible" title="缴费" width="400px">
      <el-form :model="payForm" ref="payFormRef" label-width="100px">
        <el-form-item label="应收金额">
          <span>¥{{ currentFee?.amount }}</span>
        </el-form-item>
        <el-form-item label="已收金额">
          <span>¥{{ currentFee?.paidAmount || '0.00' }}</span>
        </el-form-item>
        <el-form-item label="欠费金额">
          <span class="warning-text">¥{{ getUnpaidAmount() }}</span>
        </el-form-item>
        <el-form-item label="缴费金额" prop="payAmount">
          <el-input-number v-model="payForm.payAmount" :min="0" :max="getUnpaidAmount()" :precision="2" />
        </el-form-item>
        <el-form-item label="支付方式" prop="payMethod">
          <el-select v-model="payForm.payMethod" placeholder="请选择支付方式" popper-class="fee-select-dropdown">
            <el-option label="现金" value="CASH" />
            <el-option label="银行转账" value="BANK_TRANSFER" />
            <el-option label="支付宝" value="ALIPAY" />
            <el-option label="微信支付" value="WECHAT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="payDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPay">确认缴费</el-button>
      </template>
    </el-dialog>

    <!-- 退款弹窗 -->
    <el-dialog v-model="refundDialogVisible" title="退款" width="400px">
      <el-form :model="refundForm" ref="refundFormRef" label-width="100px">
        <el-form-item label="老人姓名">
          <span>{{ currentFee?.elderName }}</span>
        </el-form-item>
        <el-form-item label="应退金额">
          <span class="amount-display">¥{{ Math.abs(parseFloat(currentFee?.amount) || 0).toFixed(2) }}</span>
        </el-form-item>
        <el-form-item label="已退金额">
          <span>¥{{ currentFee?.paidAmount || '0.00' }}</span>
        </el-form-item>
        <el-form-item label="未退金额">
          <span class="warning-text">¥{{ getUnrefundedAmount() }}</span>
        </el-form-item>
        <el-form-item label="退款金额" prop="refundAmount">
          <el-input-number 
            v-model="refundForm.refundAmount" 
            :min="0.01" 
            :max="getUnrefundedAmount()" 
            :precision="2" 
          />
        </el-form-item>
        <el-form-item label="退款方式" prop="refundMethod">
          <el-select v-model="refundForm.refundMethod" placeholder="请选择退款方式" popper-class="fee-select-dropdown">
            <el-option label="现金" value="CASH" />
            <el-option label="银行转账" value="BANK_TRANSFER" />
            <el-option label="支付宝" value="ALIPAY" />
            <el-option label="微信支付" value="WECHAT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="refundDialogVisible = false">取消</el-button>
        <el-button type="success" @click="submitRefund">确认退款</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="viewDialogVisible" title="费用详情" width="500px">
      <el-descriptions :column="1" border v-if="currentFee">
        <el-descriptions-item label="记录编号">{{ currentFee.recordNo }}</el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ currentFee.elderName }}</el-descriptions-item>
        <el-descriptions-item label="费用类型">
          <el-tag :type="getFeeTypeTag(currentFee.feeType)">{{ getFeeTypeLabel(currentFee.feeType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="费用月份">{{ currentFee.feeMonth }}</el-descriptions-item>
        <el-descriptions-item label="应收金额">
          <span class="amount-display">¥{{ currentFee.amount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="已收金额">
          <span :class="{ 'success-text': currentFee.paidAmount > 0 }">¥{{ currentFee.paidAmount || '0.00' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="欠费金额">
          <span class="warning-text">¥{{ (currentFee.amount - (currentFee.paidAmount || 0)).toFixed(2) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="缴费状态">
          <el-tag :type="getStatusTag(currentFee.status)">{{ getStatusLabel(currentFee.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="截止日期">{{ currentFee.dueDate }}</el-descriptions-item>
        <el-descriptions-item label="缴费时间">{{ currentFee.payTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="支付方式">{{ currentFee.payMethod ? getPayMethodLabel(currentFee.payMethod) : '-' }}</el-descriptions-item>
        <el-descriptions-item label="费用来源">{{ getSourceLabel(currentFee.sourceType) }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ currentFee.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFeeList, addFee, updateFee, deleteFee, payFee, getFeeStatistics } from '@/api/fee'
import { getElderList } from '@/api/elder'

export default {
  name: 'FeeManagement',
  setup() {
    const loading = ref(false)
    const feeList = ref([])
    const elderList = ref([])
    const selectedMonth = ref(new Date().toISOString().slice(0, 7))
    const statistics = ref({})
    const dialogVisible = ref(false)
    const dialogTitle = ref('新增费用')
    const payDialogVisible = ref(false)
    const refundDialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const currentFee = ref(null)
    const formRef = ref(null)
    const payFormRef = ref(null)
    const refundFormRef = ref(null)
    const total = ref(0)

    const queryParams = reactive({
      pageNum: 1,
      pageSize: 10,
      feeType: '',
      status: ''
    })

    const form = reactive({
      id: null,
      elderId: null,
      elderName: '',
      feeType: '',
      feeMonth: '',
      amount: 0,
      paidAmount: 0,
      dueDate: '',
      remark: ''
    })

    const payForm = reactive({
      payAmount: 0,
      payMethod: 'CASH'
    })

    const refundForm = reactive({
      refundAmount: 0,
      refundMethod: 'CASH'
    })

    const rules = {
      elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
      feeType: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
      feeMonth: [{ required: true, message: '请选择费用月份', trigger: 'change' }],
      amount: [{ required: true, message: '请输入应收金额', trigger: 'blur' }],
      dueDate: [{ required: true, message: '请选择截止日期', trigger: 'change' }]
    }

    const loadStatistics = async () => {
      try {
        const res = await getFeeStatistics(selectedMonth.value)
        if (res.code === 200) {
          statistics.value = res.data
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    }

    const loadFeeList = async () => {
      loading.value = true
      try {
        const res = await getFeeList(queryParams)
        if (res.code === 200) {
          // 支持分页数据格式
          if (res.data && res.data.list) {
            feeList.value = res.data.list
            total.value = res.data.total || 0
          } else {
            feeList.value = res.data || []
            total.value = res.data ? res.data.length : 0
          }
        }
      } catch (error) {
        console.error('加载费用列表失败', error)
      } finally {
        loading.value = false
      }
    }

    // 分页大小变化
    const handleSizeChange = (val) => {
      queryParams.pageSize = val
      loadFeeList()
    }

    // 页码变化
    const handleCurrentChange = (val) => {
      queryParams.pageNum = val
      loadFeeList()
    }

    const loadElderList = async () => {
      try {
        const res = await getElderList({ pageNum: 1, pageSize: 1000 })
        if (res.code === 200) {
          elderList.value = res.data.list || res.data
        }
      } catch (error) {
        console.error('加载老人列表失败', error)
      }
    }

    const handleAdd = () => {
      dialogTitle.value = '新增费用'
      Object.assign(form, {
        id: null,
        elderId: null,
        elderName: '',
        feeType: '',
        feeMonth: selectedMonth.value,
        amount: 0,
        paidAmount: 0,
        dueDate: '',
        remark: ''
      })
      dialogVisible.value = true
    }

    const handleEdit = (row) => {
      // 根据金额判断是缴费还是退款
      if (parseFloat(row.amount) < 0) {
        dialogTitle.value = '编辑退款'
      } else {
        dialogTitle.value = '编辑费用'
      }
      Object.assign(form, row)
      dialogVisible.value = true
    }

    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm('确定要删除该费用记录吗？', '提示', { type: 'warning' })
        const res = await deleteFee(row.id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          loadFeeList()
          loadStatistics()
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    const handleView = (row) => {
      currentFee.value = row
      viewDialogVisible.value = true
    }

    const handlePay = (row) => {
      currentFee.value = row
      payForm.payAmount = getUnpaidAmount()
      payForm.payMethod = 'CASH'
      payDialogVisible.value = true
    }

    const handleRefund = (row) => {
      currentFee.value = row
      refundForm.refundAmount = getUnrefundedAmount()
      refundForm.refundMethod = 'CASH'
      refundDialogVisible.value = true
    }

    const getUnrefundedAmount = () => {
      if (!currentFee.value) return 0
      const totalAmount = Math.abs(parseFloat(currentFee.value.amount) || 0)
      const paid = parseFloat(currentFee.value.paidAmount) || 0
      return Math.max(0, (totalAmount - paid).toFixed(2))
    }

    const submitRefund = async () => {
      try {
        const res = await payFee(currentFee.value.id, refundForm.refundAmount, refundForm.refundMethod)
        if (res.code === 200) {
          ElMessage.success('退款成功')
          refundDialogVisible.value = false
          loadFeeList()
          loadStatistics()
        }
      } catch (error) {
        ElMessage.error('退款失败')
      }
    }

    const getUnpaidAmount = () => {
      if (!currentFee.value) return 0
      const amount = parseFloat(currentFee.value.amount) || 0
      const paid = parseFloat(currentFee.value.paidAmount) || 0
      return Math.max(0, (amount - paid).toFixed(2))
    }

    const submitPay = async () => {
      try {
        const res = await payFee(currentFee.value.id, payForm.payAmount, payForm.payMethod)
        if (res.code === 200) {
          ElMessage.success('缴费成功')
          payDialogVisible.value = false
          loadFeeList()
          loadStatistics()
        }
      } catch (error) {
        ElMessage.error('缴费失败')
      }
    }

    const submitForm = async () => {
      try {
        await formRef.value.validate()
        const api = form.id ? updateFee : addFee
        const res = await api(form)
        if (res.code === 200) {
          ElMessage.success(form.id ? '更新成功' : '添加成功')
          dialogVisible.value = false
          loadFeeList()
          loadStatistics()
        }
      } catch (error) {
        console.error('提交失败', error)
      }
    }

    const handleElderChange = (elderId) => {
      const elder = elderList.value.find(e => e.id === elderId)
      if (elder) {
        form.elderName = elder.name
      }
    }

    const resetQuery = () => {
      queryParams.feeType = ''
      queryParams.status = ''
      loadFeeList()
    }

    const getFeeTypeLabel = (type) => {
      const map = {
        ACCOMMODATION: '住宿费',
        MEDICAL: '医疗费',
        SUPPLY: '物资费',
        OTHER: '其他费用'
      }
      return map[type] || type
    }

    const getFeeTypeTag = (type) => {
      const map = {
        ACCOMMODATION: 'primary',
        MEDICAL: 'danger',
        SUPPLY: 'warning',
        OTHER: 'info'
      }
      return map[type] || 'info'
    }

    const getStatusLabel = (status) => {
      const map = {
        UNPAID: '未缴费',
        PARTIAL: '部分缴费',
        PAID: '已缴清',
        OVERDUE: '已逾期',
        UNREFUNDED: '未退款',
        REFUNDED: '已退款',
        PARTIAL_REFUNDED: '部分退款'
      }
      return map[status] || status
    }

    const getStatusTag = (status) => {
      const map = {
        UNPAID: 'warning',
        PARTIAL: 'info',
        PAID: 'success',
        OVERDUE: 'danger',
        UNREFUNDED: 'warning',
        REFUNDED: 'success',
        PARTIAL_REFUNDED: 'info'
      }
      return map[status] || 'info'
    }

    const getPayMethodLabel = (method) => {
      const map = {
        CASH: '现金',
        BANK_TRANSFER: '银行转账',
        ALIPAY: '支付宝',
        WECHAT: '微信支付',
        OTHER: '其他'
      }
      return map[method] || method
    }

    const getSourceLabel = (sourceType) => {
      const map = {
        CHECK_IN_APPLICATION: '入住申请',
        ELDER_MEDICATION: '老人用药',
        INVENTORY_OUT: '物资出库',
        ROOM_CHANGE: '换房调整',
        MANUAL: '手动录入'
      }
      return map[sourceType] || (sourceType || '手动录入')
    }

    onMounted(() => {
      loadStatistics()
      loadFeeList()
      loadElderList()
    })

    return {
      loading,
      feeList,
      elderList,
      selectedMonth,
      statistics,
      dialogVisible,
      dialogTitle,
      payDialogVisible,
      refundDialogVisible,
      viewDialogVisible,
      currentFee,
      formRef,
      payFormRef,
      refundFormRef,
      queryParams,
      form,
      payForm,
      refundForm,
      rules,
      total,
      loadStatistics,
      loadFeeList,
      handleAdd,
      handleEdit,
      handleDelete,
      handleView,
      handlePay,
      handleRefund,
      getUnpaidAmount,
      getUnrefundedAmount,
      submitPay,
      submitRefund,
      submitForm,
      handleElderChange,
      resetQuery,
      handleSizeChange,
      handleCurrentChange,
      getFeeTypeLabel,
      getPayMethodLabel,
      getSourceLabel,
      getFeeTypeTag,
      getStatusLabel,
      getStatusTag
    }
  }
}
</script>

<style scoped>
.fee-management {
  padding: 20px;
}

.statistics-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
}

.stat-item.primary {
  background: #f0f9ff;
  border: 1px solid #409eff;
}

.stat-item.success {
  background: #f0f9eb;
  border: 1px solid #67c23a;
}

.stat-item.warning {
  background: #fdf6ec;
  border: 1px solid #e6a23c;
}

.stat-item.danger {
  background: #fef0f0;
  border: 1px solid #f56c6c;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-item.primary .stat-value {
  color: #409eff;
}

.stat-item.success .stat-value {
  color: #67c23a;
}

.stat-item.warning .stat-value {
  color: #e6a23c;
}

.stat-item.danger .stat-value {
  color: #f56c6c;
}

.stat-label {
  margin-top: 10px;
  color: #909399;
}

/* 黑夜模式适配 */
.dark-mode .stat-item {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(232, 213, 181, 0.2);
}

.dark-mode .stat-item.primary {
  background: rgba(64, 158, 255, 0.1);
  border-color: rgba(64, 158, 255, 0.3);
}

.dark-mode .stat-item.success {
  background: rgba(103, 194, 58, 0.1);
  border-color: rgba(103, 194, 58, 0.3);
}

.dark-mode .stat-item.warning {
  background: rgba(230, 162, 60, 0.1);
  border-color: rgba(230, 162, 60, 0.3);
}

.dark-mode .stat-item.danger {
  background: rgba(245, 108, 108, 0.1);
  border-color: rgba(245, 108, 108, 0.3);
}

.dark-mode .stat-value {
  color: #e8d5b5;
}

.dark-mode .stat-item.primary .stat-value {
  color: #409eff;
}

.dark-mode .stat-item.success .stat-value {
  color: #67c23a;
}

.dark-mode .stat-item.warning .stat-value {
  color: #e6a23c;
}

.dark-mode .stat-item.danger .stat-value {
  color: #f56c6c;
}

/* 退款统计样式 */
.stat-row {
  margin-bottom: 20px;
}

.stat-row:last-child {
  margin-bottom: 0;
}

.refund-stat-row {
  padding-top: 20px;
  border-top: 1px dashed #dcdfe6;
}

.stat-item.refund {
  background: #f0f9ff;
  border: 1px solid #409eff;
}

.stat-item.refund-success {
  background: #f0f9eb;
  border: 1px solid #67c23a;
}

.stat-item.refund-warning {
  background: #fdf6ec;
  border: 1px solid #e6a23c;
}

.stat-item.refund-danger {
  background: #fef0f0;
  border: 1px solid #f56c6c;
}

.stat-item.refund .stat-value {
  color: #409eff;
}

.stat-item.refund-success .stat-value {
  color: #67c23a;
}

.stat-item.refund-warning .stat-value {
  color: #e6a23c;
}

.stat-item.refund-danger .stat-value {
  color: #f56c6c;
}

/* 黑夜模式退款统计 */
.dark-mode .refund-stat-row {
  border-top-color: rgba(232, 213, 181, 0.2);
}

.dark-mode .stat-item.refund {
  background: rgba(64, 158, 255, 0.1);
  border-color: rgba(64, 158, 255, 0.3);
}

.dark-mode .stat-item.refund-success {
  background: rgba(103, 194, 58, 0.1);
  border-color: rgba(103, 194, 58, 0.3);
}

.dark-mode .stat-item.refund-warning {
  background: rgba(230, 162, 60, 0.1);
  border-color: rgba(230, 162, 60, 0.3);
}

.dark-mode .stat-item.refund-danger {
  background: rgba(245, 108, 108, 0.1);
  border-color: rgba(245, 108, 108, 0.3);
}

.dark-mode .stat-item.refund .stat-value {
  color: #409eff;
}

/* 退款金额样式 */
.refund-amount {
  color: #f56c6c;
  font-weight: bold;
}

.dark-mode .stat-label {
  color: #bfcbd9;
}

.search-form {
  margin-bottom: 20px;
}

.warning-text {
  color: #e6a23c;
  font-weight: bold;
}

.success-text {
  color: #67c23a;
  font-weight: bold;
}

/* 修复深色主题下下拉框文字颜色 */
:deep(.el-select .el-input__inner) {
  color: #303133;
}

:deep(.el-select .el-input__wrapper) {
  background-color: #fff;
}

:deep(.fee-select-dropdown .el-select-dropdown__item) {
  color: #303133;
}

:deep(.fee-select-dropdown .el-select-dropdown__item.selected) {
  color: #409eff;
  font-weight: bold;
}

:deep(.fee-select-dropdown .el-select-dropdown__item:hover) {
  background-color: #f5f7fa;
}

/* 修复表单中的下拉框 */
:deep(.el-form .el-select .el-input__inner) {
  color: #303133;
}

/* 应收金额显示样式 - 适配黑夜模式 */
.amount-display {
  font-size: 16px;
  color: #e8d5b5;
  font-weight: bold;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
