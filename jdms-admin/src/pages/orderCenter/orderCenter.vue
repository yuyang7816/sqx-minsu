<template>
  <div class="page-container">
    <div>
      <div style="display: inline-block;margin: 5px;">
        <span>下单用户 ：</span>
        <el-input v-model="userNameT" placeholder="请输入用户名称" style="width: 150px;" @keyup.enter.native="getUserList()" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>用户手机号 ：</span>
        <el-input v-model="phoneT" placeholder="请输入手机号" style="width: 150px;" @keyup.enter.native="getUserList()" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>订单编号 ：</span>
        <el-input v-model="orderNoT" placeholder="请输入订单编号" style="width: 150px;" @keyup.enter.native="getUserList()" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>酒店名称 ：</span>
        <el-input v-model="shopNameT" placeholder="请输入门店名称" style="width: 150px;" @keyup.enter.native="getUserList()" />
      </div>&nbsp;&nbsp;

      <div style="display: inline-block;margin: 5px;">
        <span>状态 ：</span>
        <el-select v-model="stateT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="getUserList">
          <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
        </el-select>
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>开始时间 ：</span>
        <el-date-picker v-model="startTime" type="datetime" placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss" style="width: 150px;" />
      </div>&nbsp;&nbsp;
      <div style="display: inline-block;margin: 5px;">
        <span>结束时间 ：</span>
        <el-date-picker v-model="endTime" type="datetime" placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss" style="width: 150px;" />
      </div>&nbsp;&nbsp;
      <el-button type="primary" size="small" @click="getUserList">查询</el-button>
      <el-button type="warning" size="small" @click="clearBtn">重置</el-button>
    </div>

    <el-row class="el-rows">
      <el-col :span="24">
        <div class="menu-list">
          <el-table :data="userList" v-loading="menuLoading">
            <el-table-column prop="orderId" align="center" label="编号" width="60" fixed="left" />
            <el-table-column label="下单用户" align="center" min-width="170" prop="userName" fixed="left">
              <template #default="scope">
                <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{ scope.row.userName ?
                  scope.row.userName : '未绑定' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="phone" label="用户手机号" width="160"></el-table-column>
            <el-table-column prop="shopName" label="酒店名称" width="160"></el-table-column>
            <el-table-column prop="roomName" label="房间名称"></el-table-column>

            <el-table-column prop="orderNumber" label="订单编号" width="180"></el-table-column>
            <el-table-column prop="orderMoney" label="订单金额"></el-table-column>
            <el-table-column prop="payMoney" label="支付金额"></el-table-column>
            <el-table-column prop="couponMoney" label="优惠券抵扣金额"></el-table-column>
            <el-table-column prop="shopMoney" label="酒店收入"></el-table-column>
            <el-table-column prop="platformMoney" label="平台收入" width="80">
              <template #default="scope">
                <span style="color: red;">{{ scope.row.platformMoney ? scope.row.platformMoney : 0 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="userScaleMoney" label="一级佣金"></el-table-column>
            <el-table-column prop="userTwoMoney" label="二级佣金"></el-table-column>
            <el-table-column prop="refundMoney" label="退款金额"></el-table-column>
            <el-table-column prop="payWay" label="支付方式">
              <template #default="scope"> <!-- 1 微信 2 支付宝 0 余额 -->
                <div v-if="scope.row.payWay == 1">微信</div>
                <div v-if="scope.row.payWay == 2">支付宝</div>
                <div v-if="scope.row.payWay == 0">余额</div>
              </template>
            </el-table-column>

            <el-table-column prop="checkInvoice" label="是否勾选发票信息" width="180">
              <template #default="scope">
                <div v-if="scope.row.checkInvoice == 1">未开票</div>
                <div v-if="scope.row.checkInvoice == 2">开票中</div>
                <div v-if="scope.row.checkInvoice == 3">已开票</div>
              </template>
            </el-table-column>
            <el-table-column prop="isVipOrder" label="是否是会员订单" width="180">
              <template #default="scope">
                <div v-if="scope.row.isVipOrder == 1">是</div>
                <div v-else>否</div>
              </template>
            </el-table-column>
            <el-table-column prop="payTime" label="支付时间" width="180"></el-table-column>
            <el-table-column prop="arrivalTime" label="到店时间" width="180"></el-table-column>
            <el-table-column prop="realName" label="用户姓名" width="180"></el-table-column>
            <el-table-column prop="startDate" label="入住时间" width="180"></el-table-column>
            <el-table-column prop="endDate" label="离店时间" width="180"></el-table-column>
            <el-table-column prop="roomCount" label="房间数量" width="180"></el-table-column>
            <el-table-column prop="warmReminder" label="温馨提示" width="180"></el-table-column>
            <el-table-column prop="remarks" label="备注" width="180"></el-table-column>
            <el-table-column prop="verificationTime" label="核销时间" width="180"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
            <el-table-column prop="orderStatus" label="状态" fixed="right">
              <template #default="scope"> <!-- 0:待支付 1:待核销 2:进行中 3:已完成 4:已取消 5:已退款 -->
                <div v-if="scope.row.orderStatus == 0">待支付</div>
                <div v-if="scope.row.orderStatus == 1">待核销</div>
                <div v-if="scope.row.orderStatus == 2">进行中</div>
                <div v-if="scope.row.orderStatus == 3">已完成</div>
                <div v-if="scope.row.orderStatus == 4">已取消</div>
                <div v-if="scope.row.orderStatus == 5">已退款</div>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="160">
              <template #default="scope">
                <el-button type="primary" size="small" v-if="scope.row.status == 1"
                  @click="wanchengBtn(scope.row)">完成订单</el-button>
                <!-- <el-button type="warning" size="small" plain @click="quxiaoBtn(scope.row)">取消订单</el-button>
                <el-button type="warning" size="small" @click="tuikuanBtn(scope.row)">退款</el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <el-row class="el-rows">
      <el-col :span="24">
        <div class="topBtn flex align-center justify-end">
          <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="getUserList()" />
        </div>
      </el-col>
    </el-row>
    <!-- 添加/修改菜单 -->
    <!-- <EditDataDialog v-model="isShowEditData" v-if="isShowEditData" :menuId="menuId" @refresh="getMenuList" /> -->
  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  onMounted
} from 'vue';
import { useRouter } from 'vue-router';
import pagination from '@/components/pagination/pagination.vue'
import {
  ElMessageBox,
  ElMessage
} from 'element-plus';
const {
  proxy
} = getCurrentInstance();
const router = useRouter();
//用户列表
const userList = reactive([]);
//列表是否加载中
const menuLoading = ref(false);
//是否显示添加/修改弹窗
const userNameT = ref(''); //用户昵称
const phoneT = ref(''); //手机号
const orderNoT = ref('');
const shopNameT = ref('');
const hairdresserNameT = ref('');
const itemNameT = ref('');
const startTime = ref('');
const endTime = ref('');
const stateT = ref('');
const classifyT = ref('');
//当前页
const page = ref(1);
//每页条数
const limit = ref(10);
//总条数
const total = ref(0);
// classify ：1 app微信 2 微信公众号 3 微信小程序 4 支付宝
const classifyData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: 'app微信',
    value: 1
  },
  {
    label: '微信公众号',
    value: 2
  },
  {
    label: '微信小程序',
    value: 3
  },
  {
    label: '支付宝',
    value: 4
  }
]);
// 状态类型 ：  <!-- 0:待支付 1:已支付 2:已完成 3:已取消 4:退款中 5:已退款 -->
const statusData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '待支付',
    value: 0
  },
  {
    label: '已支付',
    value: 1
  },
  {
    label: '已完成',
    value: 2
  },
  {
    label: '已取消',
    value: 3
  },
  {
    label: '退款中',
    value: 4
  },
  {
    label: '已退款',
    value: 5
  }
]);
onMounted(() => {
  getUserList();
});
//跳转用户详情
const updates = (userId) => {
  router.push({
    name: 'userDetail',
    query: {
      userId: userId,
    },

  });
  // userId.value = item.userId
  // isShowEditData.value = true
};
// 修改用户状态
const changeBtn = (rows) => {
  proxy.$Request.get('/user/updateUserStatusByUserId', {
    params: {
      userId: rows.userId,
      // status: rows.status
    }
  }, {
    type: 'json'
  }).then(res => {
    if (res.code == 0) {
      ElMessage({
        type: 'success',
        message: '操作成功'
      });
      getUserList();
    } else {
      ElMessage({
        type: 'warning',
        message: res.msg
      });
      getUserList();
    }
  })
}
//删除用户
const deleteMenu = (rows) => {
  ElMessageBox.confirm(
    '确认删除此用户吗？',
    '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
    beforeClose: (action, instance, done) => {
      if (action === 'confirm') {
        instance.confirmButtonLoading = true
        instance.confirmButtonText = '删除中...';
        proxy.$Request.post('/user/deleteUserByUserId/' + rows.userId, {
          // params: {
          //   userId: rows.userId
          // }
        }, {
          // type: 'json'
        }).then(res => {
          if (res.code == 0) {
            done()
            instance.confirmButtonLoading = false
          } else {
            instance.confirmButtonText = '确认'
            instance.confirmButtonLoading = false
            ElMessageBox.alert(res.msg, '提示', {
              confirmButtonText: '确认',
            })
          }
        })
      } else {
        done()
      }
    },
  }
  ).then(() => {
    getUserList()
    ElMessage({
      type: 'success',
      message: '已删除'
    });

  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消',
    })
  })
};
//获取订单列表
const getUserList = () => {
  menuLoading.value = true
  userList.splice(0, userList.length)
  proxy.$Request.get('/admin/orders/getOrderList', {
    params: {
      page: page.value,
      limit: limit.value,
      userName: userNameT.value,
      phone: phoneT.value,
      orderNo: orderNoT.value,
      shopName: shopNameT.value,
      hairdresserName: hairdresserNameT.value,
      itemName: itemNameT.value,
      type: '',
      // type: classifyT.value,
      status: stateT.value,
      startTime: startTime.value,
      endTime: endTime.value,


    }
  }, {
    // type: 'json'
  }).then(res => {
    // console.log(res)
    userList.splice(0, userList.length, ...res.data.records)
    total.value = res.data.total;
    menuLoading.value = false
  })
};
const clearBtn = () => {
  page.value = 1
  userNameT.value = ''
  phoneT.value = ''
  classifyT.value = ''
  stateT.value = ''
  orderNoT.value = ''
  shopNameT.value = ''
  hairdresserNameT.value = ''
  itemNameT.value = ''
  startTime.value = ''
  endTime.value = ''
  getUserList()
}
// 完成订单
const wanchengBtn = (row) => {
  ElMessageBox.confirm(
    '确认完成订单？',
    '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    proxy.$Request.post('/admin/order/verify', {
      // params: {
      id: row.id
      // }
    }, {
      type: 'json'
    }).then(res => {
      if (res.code == 0) {
        ElMessage({
          type: 'success',
          message: '操作成功'
        });
        getUserList();
      } else {
        ElMessage({
          type: 'warning',
          message: res.msg
        });
      }
    })

  })

}
// 取消订单
const quxiaoBtn = (row) => {

}
// 退款订单
const tuikuanBtn = (row) => {

}

</script>

<style lang="scss" scoped>
.page-container {
  width: 100%;
  min-height: calc(100vh - 130px);
  background-color: var(--admin-workspace-soft, #F8FBFE);
  padding: 20px;
  border-radius: 8px;
  border: 1px solid var(--admin-border, #E4EDF7);
  box-shadow: none;
  display: flow-root; // 创建块格式化上下文，防止外部浮动影响
}

.topBtn {
  width: 100%;
}

.menu-list {
  width: 100%;
}

.el-rows {
  margin-top: 20px;
}

span {
  font-size: 14px;
  color: #303133;
}
</style>
