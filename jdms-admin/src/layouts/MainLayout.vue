<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">省钱兄民宿酒店预约系统</div>
      <div class="menu-wrapper">
          <el-menu :default-active="route.path" router class="menu" :unique-opened="true">
          <template v-for="item in menNavList" :key="item.menuId">
            <!-- 一级菜单且没有子菜单 -->
            <el-menu-item v-if="item.type === 1 && (!item.list || item.list.length === 0)" :index="'/' + item.url">
              <el-icon>
                <component :is="item.icon" />
              </el-icon>
              <template v-if="item.menuId == 34 && numberDt != 0">
                <span>{{ item.name }}</span>
                <el-badge class="menu-badge" :value="numberDt" />
              </template>
              <template v-else-if="item.menuId == 384 && numberZx != 0">
                <span>{{ item.name }}</span>
                <el-badge class="menu-badge" :value="numberZx" />
              </template>
              <template v-else-if="item.menuId == 341 && numberSh != 0">
                <span>{{ item.name }}</span>
                <el-badge class="menu-badge" :value="numberSh" />
              </template>
              <template v-else-if="item.menuId == 340 && numberFxs != 0">
                <span>{{ item.name }}</span>
                <el-badge class="menu-badge" :value="numberFxs" />
              </template>
              <span v-else>{{ item.name }}</span>
            </el-menu-item>

            <!-- 一级菜单且有子菜单 -->
            <el-sub-menu v-else-if="item.type === 0 || (item.list && item.list.length > 0)"
              :index="item.menuId.toString()">
              <template #title>
                <el-icon>
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.name }}</span>
              </template>
              <!-- 二级菜单 -->
              <el-menu-item v-for="subItem in item.list" :key="subItem.menuId" :index="'/' + subItem.url">
                <el-icon>
                  <component :is="subItem.icon" />
                </el-icon>
                <span>{{ subItem.name }}</span>
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </div>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <!-- 添加标签页 -->
          <div class="tabs-container">
            <el-tabs v-model="activeTab" closable @tab-click="handleTabClick" @tab-remove="handleTabRemove"
              class="tabs">
              <el-tab-pane v-for="tab in tabs" :key="tab.path" :label="tab.title" :name="tab.path">
              </el-tab-pane>
            </el-tabs>
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <!-- <el-avatar :size="30" /> -->
                <img src="../assets/logo.png" width="30px"
                  style="width: 30px;height: 30px;border-radius: 50%;display: inline-block;"></img>
                <span>{{ userStore.userInfo?.username || '管理员' }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  onMounted,
  onUnmounted,
  watch
} from 'vue';
import {
  useUserStore
} from '@/stores/user'
import {
  useRouter,
  useRoute
} from 'vue-router'

const {
  proxy
} = getCurrentInstance()
const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

const menNavList = reactive([]);
// 从 sessionStorage 恢复标签页或使用默认值
const tabs = ref([]);
const activeTab = ref('/home')
const numberDt = ref(0);//提现
const numberZx = ref(0);//注销
const numberSh = ref(0);//商户
const numberFxs = ref(0);//发型师
let listNumTimer = null

onMounted(() => {
  // 从 sessionStorage 恢复标签页状态
  const savedTabs = sessionStorage.getItem('layout-tabs')
  if (savedTabs) {
    tabs.value = JSON.parse(savedTabs)
  } else {
    tabs.value = [{
      title: '数据中心',
      path: '/home',
      name: 'home'
    }]
  }

  // 恢复当前激活的标签页
  const savedActiveTab = sessionStorage.getItem('layout-active-tab')
  if (savedActiveTab) {
    activeTab.value = savedActiveTab
  }

  // 动态获取菜单
  getMenNavList()
  // 获取待审核数据（仅启动一次定时器，避免菜单切换时重复创建导致卡顿）
  getListNum()
  listNumTimer = setInterval(() => {
    getListNum()
  }, 6000)
});

onUnmounted(() => {
  if (listNumTimer) {
    clearInterval(listNumTimer)
    listNumTimer = null
  }
});

// 递归查找菜单标题
const findMenuTitle = (path, menus) => {
  for (const menu of menus) {
    if (menu.url && `/${menu.url}` === path) {
      return menu.name
    }
    if (menu.list && menu.list.length > 0) {
      const title = findMenuTitle(path, menu.list)
      if (title) return title
    }
  }
  return null
}

// 获取菜单列表
const getMenNavList = () => {
  menNavList.splice(0, menNavList.length)
  proxy.$Request.get('/sys/menu/nav').then(res => {
    if (res.code == 0) {
      menNavList.splice(0, menNavList.length, ...res.data.menuList)
      sessionStorage.setItem('permissions', JSON.stringify(res.data.permissions || '[]'))
    }
  })
};
// 获取待审核数据
const getListNum = () => {
  // 提现待审核 numberDt
  proxy.$Request.get('/cash/selectAdminHelpProfit', {
    params: {
      page: 1,
      limit: 10,
      state: 0,
    }
  }, {
    // type: 'json'
  }).then(res => {
    if (res.code == 0) {
      numberDt.value = res.data.total
      sessionStorage.setItem('numberDt', res.data.total)
      console.log(numberDt.value, 'numberDt--------', sessionStorage.getItem('numberDt'));
    }
  })
  // 注销 numberZx
  proxy.$Request.post('/admin/cancelAccount/page', {
    // params: {
    page: 1,
    limit: 10,
    status: 1,
    // }
  }, {
    type: 'json'
  }).then(res => {
    if (res.code == 0) {
      numberZx.value = res.data.total
      sessionStorage.setItem('numberZx', res.data.total)
    }
  })
  // 商户 numberSh
  proxy.$Request.get('/admin/shops/getShopList', {
    params: {
      page: 1,
      limit: 10,
      auditStatus: 3,
    }
  }, {
    // type: 'json'
  }).then(res => {
    if (res.code == 0) {
      numberSh.value = res.data.total
      sessionStorage.setItem('numberSh', res.data.total)
    }
  })
  // 发型师 numberFxs
  // proxy.$Request.post('/admin/hairdresserApply/page', {
  //   // params: {
  //   page: 1,
  //   limit: 10,
  //   status: 1,
  //   // }
  // }, {
  //   type: 'json'
  // }).then(res => {
  //   if (res.code == 0) {
  //     numberFxs.value = res.data.total
  //     sessionStorage.setItem('numberFxs', res.data.total)
  //   }
  // })
}

// 监听标签页变化，保存到 sessionStorage
watch(tabs, (newTabs) => {
  sessionStorage.setItem('layout-tabs', JSON.stringify(newTabs))
}, {
  deep: true
})

// 监听当前激活标签页变化，保存到 sessionStorage
watch(activeTab, (newActiveTab) => {
  sessionStorage.setItem('layout-active-tab', newActiveTab)
})

// 监听路由变化，自动添加标签页
watch(route, (to) => {
  activeTab.value = to.path
  // 查找是否已存在该标签页
  const existingTab = tabs.value.find(tab => tab.path === to.path)
  if (!existingTab) {
    // 查找菜单项获取标题
    const menuTitle = findMenuTitle(to.path, menNavList)
    if (menuTitle) {
      tabs.value.push({
        title: menuTitle,
        path: to.path,
        name: to.name
      })
    }
  }
}, {
  immediate: true
})

// 标签页点击事件
const handleTabClick = (tab) => {
  router.push(tab.props.name)
}

// 标签页关闭事件
const handleTabRemove = (targetName) => {
  // 不能关闭当前激活的标签页和首页标签页
  if (targetName === activeTab.value) {
    // 如果关闭的是当前页，跳转到下一个或上一个标签页
    const currentIndex = tabs.value.findIndex(tab => tab.path === targetName)
    if (currentIndex > 0) {
      const nextTab = tabs.value[currentIndex + 1] || tabs.value[currentIndex - 1]
      if (nextTab) {
        router.push(nextTab.path)
      }
    }
  }

  // 过滤掉被关闭的标签页
  tabs.value = tabs.value.filter(tab => tab.path !== targetName)
}

// 退出登录
const handleLogout = () => {
  userStore.logout()
  // 清除 sessionStorage 中的标签页信息
  sessionStorage.removeItem('layout-tabs')
  sessionStorage.removeItem('layout-active-tab')
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  background: linear-gradient(180deg, #F8FBFF 0%, var(--admin-workspace-soft, #EDF4FD) 42%, #DFEAF7 100%);

  > .el-container {
    background: linear-gradient(180deg, #F8FBFF 0%, var(--admin-workspace-soft, #EDF4FD) 42%, #DFEAF7 100%);
    min-width: 0;
  }

  .el-aside {
    background-color: var(--admin-shell-bg, #1E293B);
    border-right: 1px solid var(--admin-shell-border, #334155);
    box-shadow: 6px 0 22px rgba(41, 61, 98, 0.16);
  }

  .logo {
    height: 60px;
    line-height: 60px;
    text-align: center;
    color: #FFFFFF;
    font-size: 18px;
    font-weight: 700;
    background: linear-gradient(180deg, rgba(65, 93, 136, 0.96) 0%, rgba(76, 105, 152, 0.98) 100%);
    border-bottom: 1px solid rgba(255, 255, 255, 0.12);
    letter-spacing: 1px;
  }

  .menu-wrapper {
    height: calc(100% - 60px);
    overflow-y: auto;

    &::-webkit-scrollbar {
      display: none;
    }

    scrollbar-width: none;
    -ms-overflow-style: none;
  }

  .menu {
    border: none;
    min-height: 100%;
    background-color: var(--admin-shell-bg, #1E293B);

    :deep(.el-menu-item),
    :deep(.el-sub-menu__title) {
      color: var(--admin-shell-text, #CBD5E1);
      font-weight: 500;
      height: 48px;
      line-height: 48px;
      border-radius: 10px;
      margin: 4px 10px 4px 8px;
      transition: all 0.2s ease;
      background-color: transparent;

      .el-icon {
        color: var(--admin-shell-text-muted, #C5D5EE);
      }
    }

    :deep(.el-menu-item:hover),
    :deep(.el-sub-menu__title:hover) {
      color: #FFFFFF;
      background-color: rgba(255, 255, 255, 0.08);

      .el-icon {
        color: #F7FAFF;
      }
    }

    :deep(.el-menu-item.is-active) {
      color: #FFFFFF;
      background: linear-gradient(135deg, var(--admin-brand) 0%, var(--admin-brand-dark) 100%);
      font-weight: 600;
      box-shadow: 0 8px 18px rgba(32, 85, 169, 0.24);

      .el-icon {
        color: #FFFFFF;
      }
    }

    :deep(.el-sub-menu.is-active .el-sub-menu__title) {
      color: #FFFFFF;
      background-color: rgba(255, 255, 255, 0.08);
    }

    :deep(.el-sub-menu__title) {
      color: var(--admin-shell-text, #CBD5E1);
    }

    :deep(.el-menu--inline) {
      background-color: transparent;
    }

    :deep(.el-sub-menu .el-menu-item) {
      color: var(--admin-shell-text-muted, #94A3B8);
    }

    .el-menu-item {
      .menu-badge {
        margin-left: 6px;

        :deep(.el-badge__content) {
          position: static;
          transform: none;
        }
      }
    }
  }

  .header {
    display: flex;
    align-items: center;
    padding: 0;
    background-color: rgba(255, 255, 255, 0.78);
    border-bottom: 1px solid rgba(221, 231, 244, 0.95);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    height: 50px;

    .header-content {
      display: flex;
      align-items: center;
      width: 100%;
      height: 100%;
    }

    .tabs-container {
      flex: 1;
      height: 100%;
      width: 88%;

      .tabs {
        height: 100%;
        width: 100%;

        :deep(.el-tabs__header) {
          margin: 0;
          border-bottom: 1px solid var(--admin-workspace-border, #EAF1F8);
        }

        :deep(.el-tabs__nav) {
          border: none;
          height: 100%;
        }

        :deep(.el-tabs__item) {
          height: 100%;
          line-height: 50px;
          padding: 0 20px;
          font-size: 14px;
          color: var(--admin-workspace-text-muted, #7C8CA0);
        }

        :deep(.el-tabs__item.is-active) {
          color: var(--admin-brand);
          background-color: rgba(226, 237, 252, 0.96);
        }

        :deep(.el-tabs__item:hover) {
          color: var(--admin-brand);
        }

        :deep(.el-tabs__active-bar) {
          background-color: var(--admin-brand);
        }

        :deep(.el-tabs__nav-wrap::after) {
          background-color: transparent;
        }

        :deep(.el-tabs__item .is-icon-close) {
          color: var(--admin-workspace-text-muted, #7C8CA0);
        }

        :deep(.el-tabs__item .is-icon-close:hover) {
          background-color: rgba(36, 116, 208, 0.12);
          color: var(--admin-brand);
        }
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      padding: 0 20px;
      height: 100%;
      border-left: 1px solid var(--admin-workspace-border, #EAF1F8);

      .user-info {
        display: flex;
        align-items: center;
        cursor: pointer;
        height: 100%;

        span {
          margin-left: 10px;
          color: var(--admin-workspace-text, #334155);
          font-weight: 500;
        }
      }
    }
  }

  .el-main {
    background: linear-gradient(180deg, rgba(248, 251, 255, 0.94) 0%, rgba(237, 244, 253, 0.94) 42%, rgba(223, 234, 247, 0.92) 100%);
    padding: 20px;
  }
}
</style>
