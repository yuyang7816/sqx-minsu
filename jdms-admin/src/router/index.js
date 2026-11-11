import {
  createRouter,
  createWebHistory
} from 'vue-router'
import Layout from '@/layouts/MainLayout.vue'

const routes = [{
  path: '/',
  component: Layout,
  redirect: '/home',
  children: [{
    path: '/home',
    name: 'Home',
    component: () => import('@/pages/Home/index.vue'),
    meta: {
      title: '数据中'
    }
  }, {
    path: '/menu',
    name: 'menu',
    component: () => import('@/pages/Menu/index.vue'),
    meta: {
      title: '菜单管理',
    },
  },
  {
    path: '/role',
    name: 'role',
    component: () => import('@/pages/Role/index.vue'),
    meta: {
      title: '角色管理',
    },
  },
  {
    path: '/user',
    name: 'user',
    component: () => import('@/pages/Users/index.vue'),
    meta: {
      title: '管理员列表',
    },
  },
  {
    path: '/userList',
    component: () => import('@/pages/userList/index.vue'),
    name: 'userList',
    meta: {
      title: '用户列表',
    },
  },
  {
    path: '/userDetail',
    component: () => import('@/pages/userList/userDetail.vue'),
    name: 'userDetail',
    meta: {
      title: '用户详情',
    },
  },
  {
    path: '/userDetail1',
    component: () => import('@/pages/userList/userDetail1.vue'),
    name: 'userDetail1',
    meta: {
      title: '用户详情',
    },
  },
  {
    path: '/user',
    name: 'user',
    component: () => import('@/pages/Users/index.vue'),
    meta: {
      title: '管理员列表',
    },
  },
  {
    path: '/financeList',
    name: 'financeList',
    component: () => import('@/pages/financeList/financeList.vue'),
    meta: {
      title: '财务中心',
    },
  },


  {
    path: '/shopManage',
    name: 'shopManage',
    component: () => import('@/pages/dynamic/shopManage.vue'),
    meta: {
      title: '动态管理',
    },
  },
  {
    path: '/locality',
    name: 'locality',
    component: () => import('@/pages/dynamic/locality.vue'),
    meta: {
      title: '酒店管理',
    },
  },
  {
    path: '/complaint',
    name: 'complaint',
    component: () => import('@/pages/message/complaint.vue'),
    meta: {
      title: '投诉中心',
    },
  },
  {
    path: '/message',
    name: 'message',
    component: () => import('@/pages/message/message.vue'),
    meta: {
      title: '消息中心',
    },
  },
  {
    path: '/messageZx',
    name: 'messageZx',
    component: () => import('@/pages/message/messageZx.vue'),
    meta: {
      title: '消息中心',
    },
  },
  {
    path: '/materialsList',
    name: 'materialsList',
    component: () => import('@/pages/materials/materialsList.vue'),
    meta: {
      title: '帮助中心',
    },
  },
  {
    path: '/comment',
    name: 'comment',
    component: () => import('@/pages/comment/comment.vue'),
    meta: {
      title: '聊天会话',
    },
  },
  {
    path: '/chatRecord',
    name: 'chatRecord',
    component: () => import('@/pages/comment/chatRecord.vue'),
    meta: {
      title: '聊天记录',
    },
  },
  {
    path: '/allocationList',
    component: () => import('@/pages/allocationList/allocationList.vue'),
    name: 'allocationList',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '系统配置',
      hasTag: true,
    },
  },
  {
    path: '/bannerList',
    component: () => import('@/pages/bannerList/bannerList.vue'),
    name: 'bannerList',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '首页装修',
      hasTag: true,
    },
  },

  {
    path: '/courseManagement',
    name: 'courseManagement',
    component: () => import('@/pages/courseManagement/courseManagement.vue'),
    meta: {
      title: '服务管理',
    },
  },
  {
    path: '/courseType',
    name: 'courseType',
    component: () => import('@/pages/courseManagement/courseType.vue'),
    meta: {
      title: '类型配置',
    },
  },
  {
    path: '/orderCenter',
    name: 'orderCenter',
    component: () => import('@/pages/orderCenter/orderCenter.vue'),
    meta: {
      title: '订单中心',
    },
  },
  {
    path: '/vipPrivilege',
    name: 'vipPrivilege',
    component: () => import('@/pages/vipPrivilege/vipPrivilege.vue'),
    meta: {
      title: '会员特权',
    },
  },
  {
    path: '/coupon',
    name: 'coupon',
    component: () => import('@/pages/vipPrivilege/coupon.vue'),
    meta: {
      title: '会员配置',
    },
  },
  {
    path: '/couponCard',
    name: 'couponCard',
    component: () => import('@/pages/vipPrivilege/couponCard.vue'),
    meta: {
      title: '配置管理',
    },
  },

  {
    path: '/country',
    component: () => import('@/pages/newTagPage/country.vue'),
    name: 'country',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '商家入驻',
      hasTag: true,
    },
  },
  {
    path: '/destination',
    component: () => import('@/pages/newTagPage/destination.vue'),
    name: 'destination',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '发型师管理',
      hasTag: true,
    },
  },

  {
    path: 'attraction',
    component: () => import('@/pages/groupPurchase/attraction.vue'),
    name: 'attraction',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '景点管理',
      hasTag: true,
    },
  },
  {
    path: '/merchant',
    name: 'merchant',
    component: () => import('@/pages/dynamic/merchant.vue'),
    meta: {
      title: '商户管理',
    },
  },
  {
    path: 'destList',
    component: () => import('@/pages/newTagPage/destList.vue'),
    name: 'destList',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '目的地管理',
      hasTag: true,
    },
  },
  {
    path: 'search',
    component: () => import('@/pages/newTagPage/search.vue'),
    name: 'search',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '搜索记录',
      hasTag: true,
    },
  },
  {
    path: 'commentList',
    component: () => import('@/pages/dynamic/commentList.vue'),
    name: 'commentList',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '评论记录',
      hasTag: true,
    },
  },
  {
    path: 'couponYhq',
    component: () => import('@/pages/campus/couponYhq.vue'),
    name: 'couponYhq',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '优惠券管理',
      hasTag: true,
    },
  },
  {
    path: 'couponuser',
    component: () => import('@/pages/campus/couponuser.vue'),
    name: 'couponuser',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '优惠券领取记录',
      hasTag: true,
    },
  },
  {
    path: '/agent',
    component: () => import('@/pages/newTagPage/agent.vue'),
    name: 'agent',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '推广申请',
      hasTag: true,
    },
  },
  {
    path: 'travelProducts',
    component: () => import('@/pages/groupPurchase/travelProducts.vue'),
    name: 'travelProducts',
    meta: {
      layoutName: 'main',
      redirectName: 'main-redirect',
      title: '发票管理',
      hasTag: true,
    },
  },





  ]
},
{
  path: '/login',
  name: 'Login',
  component: () => import('@/pages/Login/Login.vue'),
  meta: {
    title: '登录'
  }
}
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})
// 路由守卫：判断 token
router.beforeEach((to, from, next) => {
  // 如果去的是登录页，直接放行
  if (to.name === 'Login') {
    return next()
  }
  // 检查本地是否有 token
  const token = localStorage.getItem('token')
  if (token) {
    next()
  } else {
    // next()
    next({
      name: 'Login'
    })
  }
})

export default router
