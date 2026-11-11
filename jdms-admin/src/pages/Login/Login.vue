<template>
  <div class="login-page">
    <div class="login-bg">
      <img src="../../assets/login-imgs/login-bg.png" class="bg-image" alt="login-bg" />
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
    </div>

    <div class="login-wrapper">
      <div class="login-card">
        <div class="card-body">
          <!-- 左侧：功能介绍 -->
          <div class="card-section feature-section">
            <div class="section-header">
              <div class="login-logo">
                <img src="../../assets/logo.png" alt="logo" />
              </div>
              <h1 class="login-title">省钱兄民宿酒店预约系统</h1>
              <p class="login-subtitle">智慧管理后台 · 让运营更简单</p>
            </div>

            <ul class="feature-list">
              <li v-for="item in features" :key="item.title">
                <div class="feature-icon">
                  <el-icon :size="18"><component :is="item.icon" /></el-icon>
                </div>
                <div class="feature-text">
                  <strong>{{ item.title }}</strong>
                  <span>{{ item.desc }}</span>
                </div>
              </li>
            </ul>
          </div>

          <!-- 右侧：登录表单 -->
          <div class="card-section form-section">
            <div class="form-header">
              <h2 class="form-title">欢迎登录</h2>
              <p class="form-subtitle">请输入账号信息进入管理后台</p>
            </div>

            <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="login-form">
              <el-form-item prop="username">
                <el-input
                  v-model="loginForm.username"
                  placeholder="请输入账号"
                  size="large"
                  :prefix-icon="User"
                  clearable
                />
              </el-form-item>

              <el-form-item prop="password">
                <el-input
                  v-model="loginForm.password"
                  type="password"
                  placeholder="请输入密码"
                  size="large"
                  show-password
                  :prefix-icon="Lock"
                />
              </el-form-item>

              <el-form-item prop="code">
                <el-row :gutter="12">
                  <el-col :span="16">
                    <el-input
                      @keydown.enter.prevent="handleLogin"
                      v-model="loginForm.captcha"
                      placeholder="请输入验证码"
                      size="large"
                      :prefix-icon="Key"
                      clearable
                    />
                  </el-col>
                  <el-col :span="8">
                    <div class="captcha-container" @click="refreshCaptcha">
                      <img :src="captchaUrl" alt="验证码" class="captcha-image" />
                    </div>
                  </el-col>
                </el-row>
              </el-form-item>

              <el-form-item>
                <el-button
                  type="primary"
                  size="large"
                  class="login-btn"
                  @click="handleLogin"
                  :loading="loading"
                >
                  登 录
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="login-tips">
          <el-icon><InfoFilled /></el-icon>
          <span>请使用管理员分配的账号登录，如有问题请联系系统管理员</span>
        </div>

        <div class="login-footer">
          <span>© 2026 酒店民宿管理系统 · 安全登录</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  ref,
  onMounted,
  getCurrentInstance
} from 'vue'
import {
  ElMessage
} from 'element-plus'
import {
  User,
  Lock,
  Key,
  InfoFilled,
  DataAnalysis,
  House,
  Tickets,
  Setting
} from '@element-plus/icons-vue'
import {
  useRouter
} from 'vue-router'
import {
  useUserStore
} from '@/stores/user'
import {
  getUUID
} from '/comment/utils.js'
import {
  ROOTPATH
} from '/comment/httpUrl'

const features = [
  { icon: DataAnalysis, title: '数据中心', desc: '实时掌握经营数据与趋势分析' },
  { icon: House, title: '酒店管理', desc: '门店、房间、价格一站式管理' },
  { icon: Tickets, title: '订单中心', desc: '订单全流程跟踪与财务结算' },
  { icon: Setting, title: '系统配置', desc: '灵活配置会员、优惠券与权限' }
]

const {
  proxy
} = getCurrentInstance();
const loginFormRef = ref()
const loading = ref(false)
const captchaUrl = ref('');
const captchaKey = ref('');

const loginForm = ref({
  username: '',
  password: '',
  captcha: '',
  uuid: '',
});

const rules = {
  username: [{
    required: true,
    message: '请输入用户名',
    trigger: 'blur'
  }],
  password: [{
    required: true,
    message: '请输入密码',
    trigger: 'blur'
  }],
  captcha: [{
    required: true,
    message: '请输入验证码',
    trigger: 'blur'
  }]
}

const router = useRouter()
const userStore = useUserStore()

const generateCaptchaUrl = () => {
  try {
    captchaKey.value = getUUID()
    loginForm.value.uuid = captchaKey.value
    const url = `${ROOTPATH}/captcha.jpg?uuid=${captchaKey.value}`
    return url
  } catch (error) {
    ElMessage.error('获取验证码失败')
    return ''
  }
}

const refreshCaptcha = () => {
  captchaUrl.value = generateCaptchaUrl()
}

onMounted(() => {
  refreshCaptcha()
})

const handleLogin = async () => {
  try {
    await loginFormRef.value.validate()
    loading.value = true
    proxy.$Request.post('/sys/login', loginForm.value, {
      type: 'json'
    }).then(res => {
      if (res.code == 0) {
        userStore.setToken(res.data.token);
        userStoreBtn()
        router.push('/');
        loading.value = false;
      } else {
        ElMessage.error(res.msg)
        loading.value = false;
        refreshCaptcha();
      }
    })
  } catch (error) {
    console.log('表单验证失败:', error)
    refreshCaptcha()
  }
}

const userStoreBtn = async () => {
  proxy.$Request.get('/sys/user/info', {}).then((res) => {
    userStore.setUserInfo(res.data.user)
  })
}
</script>

<style lang="scss" scoped>
.login-page {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  overflow: hidden;
  background: linear-gradient(0deg, #5A97EC 0%, #A9C9F8 46%, #EEF5FF 100%);

  .login-bg {
    position: absolute;
    inset: 0;
    z-index: 0;
    pointer-events: none;
    overflow: hidden;

    .bg-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
      opacity: 0.28;
    }

    .bg-shape {
      position: absolute;
      border-radius: 50%;
      filter: blur(60px);
    }

    .bg-shape-1 {
      width: 420px;
      height: 420px;
      top: -120px;
      right: -80px;
      background: rgba(44, 142, 254, 0.28);
    }

    .bg-shape-2 {
      width: 360px;
      height: 360px;
      bottom: -100px;
      left: -60px;
      background: rgba(44, 142, 254, 0.18);
    }

    .bg-shape-3 {
      width: 200px;
      height: 200px;
      top: 40%;
      left: 15%;
      background: rgba(36, 116, 208, 0.18);
    }
  }

  .login-wrapper {
    position: relative;
    z-index: 1;
    width: 100%;
    max-width: 920px;
    padding: 24px;
    border-radius: 24px;
    background: rgba(255, 255, 255, 0.18);
    border: 1px solid rgba(255, 255, 255, 0.35);
    box-shadow: 0 18px 48px rgba(36, 116, 208, 0.14);
    backdrop-filter: blur(18px);
    -webkit-backdrop-filter: blur(18px);
  }

  .login-card {
    width: 100%;
    padding: 0 0 28px;
    background: rgba(255, 255, 255, 0.82);
    border-radius: 18px;
    box-shadow: 0 12px 36px rgba(36, 116, 208, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.5);
    overflow: hidden;
  }

  .card-body {
    display: flex;
    align-items: stretch;
  }

  .card-section {
    flex: 1;
    min-width: 0;
  }

  .feature-section {
    padding: 40px 36px;
    background: linear-gradient(180deg, #E0ECFF 0%, #F2F7FF 100%);
    border-right: 1px solid #EAF1F8;

    .section-header {
      margin-bottom: 28px;

      .login-logo {
        width: 56px;
        height: 56px;
        margin-bottom: 16px;
        border-radius: 14px;
        background: linear-gradient(135deg, #2C8EFE 0%, #2474D0 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 6px 18px rgba(36, 116, 208, 0.16);

        img {
          width: 34px;
          height: 34px;
          object-fit: contain;
        }
      }

      .login-title {
        font-size: 22px;
        font-weight: 700;
        color: #303133;
        margin-bottom: 6px;
        letter-spacing: 1px;
      }

      .login-subtitle {
        font-size: 13px;
        color: #909399;
        line-height: 1.5;
      }
    }

    .feature-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        display: flex;
        align-items: flex-start;
        gap: 12px;
        margin-bottom: 16px;

        &:last-child {
          margin-bottom: 0;
        }

        .feature-icon {
          width: 36px;
          height: 36px;
          border-radius: 10px;
          background: #FFFFFF;
          color: #2474D0;
          display: flex;
          align-items: center;
          justify-content: center;
          flex-shrink: 0;
          box-shadow: none;
          border: 1px solid #E4EDF7;
        }

        .feature-text {
          display: flex;
          flex-direction: column;
          gap: 3px;
          min-width: 0;

          strong {
            font-size: 14px;
            font-weight: 600;
            color: #303133;
          }

          span {
            font-size: 12px;
            color: #909399;
            line-height: 1.5;
          }
        }
      }
    }
  }

  .form-section {
    padding: 40px 40px 36px;
    display: flex;
    flex-direction: column;
    justify-content: center;

    .form-header {
      margin-bottom: 28px;

      .form-title {
        font-size: 22px;
        font-weight: 700;
        color: #303133;
        margin-bottom: 6px;
      }

      .form-subtitle {
        font-size: 13px;
        color: #909399;
      }
    }
  }

  .login-form {
    :deep(.el-input__wrapper) {
      border-radius: 10px;
      padding: 4px 14px;
      background-color: #FFFFFF;
      box-shadow: 0 0 0 1px #DCDFE6 inset;
      transition: box-shadow 0.2s;
    }

    :deep(.el-input__wrapper.is-focus) {
      box-shadow: 0 0 0 1px #2474D0 inset, 0 0 0 4px rgba(36, 116, 208, 0.08);
    }

    :deep(.el-input__inner) {
      height: 44px;
      line-height: 44px;
      font-size: 14px;
      color: #303133;
    }

    .captcha-container {
      height: 52px;
      border-radius: 10px;
      overflow: hidden;
      cursor: pointer;
      border: 1px solid #DCDFE6;
      background: #F6F8FB;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: border-color 0.2s;

      &:hover {
        border-color: #2474D0;
      }

      .captcha-image {
        width: 100%;
        height: 100%;
        object-fit: contain;
        padding: 2px;
      }
    }

    .login-btn {
      width: 100%;
      height: 48px;
      border-radius: 10px;
      font-size: 16px;
      font-weight: 600;
      letter-spacing: 2px;
      margin-top: 4px;
      background: linear-gradient(135deg, #2C8EFE 0%, #2474D0 100%);
      border: none;
      box-shadow: 0 8px 20px rgba(36, 116, 208, 0.18);
      transition: transform 0.2s, box-shadow 0.2s;
    }

    .login-btn:hover {
      transform: translateY(-1px);
      box-shadow: 0 10px 24px rgba(36, 116, 208, 0.24);
    }
  }

  .login-tips {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    margin: 0 40px;
    padding: 10px 12px;
    background: #F6F8FB;
    border-radius: 8px;
    font-size: 12px;
    color: #909399;
    line-height: 1.5;

    .el-icon {
      margin-top: 2px;
      color: #2474D0;
      flex-shrink: 0;
    }
  }

  .login-footer {
    margin-top: 16px;
    text-align: center;
    font-size: 12px;
    color: #C0C4CC;
  }
}

@media (max-width: 768px) {
  .login-page {
    .login-wrapper {
      max-width: 480px;
      padding: 16px;
    }

    .card-body {
      flex-direction: column;
    }

    .feature-section {
      padding: 32px 24px;
      border-right: none;
      border-bottom: 1px solid #EAF1F8;
    }

    .form-section {
      padding: 32px 24px;
    }

    .login-tips {
      margin: 0 24px;
    }
  }
}
</style>
