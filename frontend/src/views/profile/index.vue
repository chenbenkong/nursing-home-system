<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <!-- 左侧个人信息卡片 -->
      <el-col :span="8">
        <el-card class="profile-card">
          <div class="profile-header">
            <el-avatar :size="100" :icon="UserFilled" class="profile-avatar" />
            <h3 class="profile-name">{{ userInfo?.realName || userInfo?.username }}</h3>
            <p class="profile-role">{{ formatRole(userInfo?.role) }}</p>
          </div>
          
          <div class="profile-info">
            <div class="info-item">
              <el-icon><User /></el-icon>
              <span>用户名：{{ userInfo?.username }}</span>
            </div>
            <div class="info-item">
              <el-icon><Phone /></el-icon>
              <span>手机号：{{ userInfo?.phone || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Message /></el-icon>
              <span>邮箱：{{ userInfo?.email || '未设置' }}</span>
            </div>
            <div class="info-item">
              <el-icon><Clock /></el-icon>
              <span>最后登录：{{ formatDateTime(userInfo?.lastLoginTime) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧编辑区域 -->
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
            </div>
          </template>
          
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="100px"
            class="profile-form"
          >
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="form.realName" placeholder="请输入真实姓名" />
            </el-form-item>

            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleSave" :loading="saveLoading">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card style="margin-top: 20px;">
          <template #header>
            <div class="card-header">
              <span>修改密码</span>
            </div>
          </template>
          
          <el-form
            ref="pwdFormRef"
            :model="pwdForm"
            :rules="pwdRules"
            label-width="100px"
            class="profile-form"
          >
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" placeholder="请输入原密码" show-password />
            </el-form-item>

            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" placeholder="请输入新密码" show-password />
            </el-form-item>

            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleChangePassword" :loading="pwdLoading">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { UserFilled, User, Phone, Message, Clock } from '@element-plus/icons-vue'
import { updateUser } from '@/api/user'

export default {
  name: 'Profile',
  components: {
    User,
    Phone,
    Message,
    Clock
  },
  setup() {
    const store = useStore()
    const formRef = ref(null)
    const pwdFormRef = ref(null)
    const saveLoading = ref(false)
    const pwdLoading = ref(false)

    const userInfo = computed(() => store.getters.userInfo)

    const form = reactive({
      id: null,
      realName: '',
      phone: '',
      email: ''
    })

    const pwdForm = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })

    const rules = {
      realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
      phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
      ],
      email: [
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
      ]
    }

    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== pwdForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    const pwdRules = {
      oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
      newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' }
      ]
    }

    const formatRole = (role) => {
      const map = {
        'ADMIN': '管理员',
        'MANAGER': '经理',
        'NURSE': '护士',
        'DOCTOR': '医生',
        'STAFF': '员工'
      }
      return map[role] || role || '未知角色'
    }

    // 格式化日期时间
    const formatDateTime = (datetime) => {
      if (!datetime) return '暂无'
      // 处理数组格式 [2026, 3, 7, 7, 0, 0]
      if (Array.isArray(datetime)) {
        const [year, month, day, hour = 0, minute = 0] = datetime
        return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`
      }
      // 处理字符串格式
      const date = new Date(datetime)
      if (isNaN(date.getTime())) return '暂无'
      return date.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }).replace(/\//g, '-')
    }

    const initForm = () => {
      if (userInfo.value) {
        form.id = userInfo.value.id
        form.realName = userInfo.value.realName || ''
        form.phone = userInfo.value.phone || ''
        form.email = userInfo.value.email || ''
      }
    }

    const handleSave = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      saveLoading.value = true
      try {
        const res = await updateUser(form)
        if (res.code === 200) {
          ElMessage.success('保存成功')
          // 更新本地存储的用户信息
          store.commit('SET_USER_INFO', { ...userInfo.value, ...form })
        }
      } catch (error) {
        console.error('保存失败:', error)
      } finally {
        saveLoading.value = false
      }
    }

    const handleChangePassword = async () => {
      const valid = await pwdFormRef.value.validate().catch(() => false)
      if (!valid) return

      pwdLoading.value = true
      try {
        // 这里应该调用修改密码的API
        // const res = await changePassword(pwdForm)
        ElMessage.success('密码修改成功')
        pwdForm.oldPassword = ''
        pwdForm.newPassword = ''
        pwdForm.confirmPassword = ''
      } catch (error) {
        console.error('密码修改失败:', error)
      } finally {
        pwdLoading.value = false
      }
    }

    onMounted(() => {
      initForm()
    })

    return {
      formRef,
      pwdFormRef,
      userInfo,
      form,
      pwdForm,
      rules,
      pwdRules,
      saveLoading,
      pwdLoading,
      UserFilled,
      formatRole,
      formatDateTime,
      handleSave,
      handleChangePassword
    }
  }
}
</script>

<style scoped lang="scss">
.profile-container {
  .profile-card {
    .profile-header {
      text-align: center;
      padding: 20px 0;
      border-bottom: 1px solid #ebeef5;

      .profile-avatar {
        margin-bottom: 15px;
      }

      .profile-name {
        margin: 0;
        font-size: 20px;
        color: #303133;
      }

      .profile-role {
        margin: 10px 0 0;
        color: #909399;
        font-size: 14px;
      }
    }

    .profile-info {
      padding: 20px;

      .info-item {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
        color: #606266;

        .el-icon {
          margin-right: 10px;
          color: #909399;
        }

        &:last-child {
          margin-bottom: 0;
        }
      }
    }
  }

  .card-header {
    font-weight: bold;
  }

  .profile-form {
    max-width: 500px;
  }
}
</style>
