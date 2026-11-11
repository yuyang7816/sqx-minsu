<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="优惠券列表" name="first">
        <div style="margin: 0 20px;text-align: right;">
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column fixed prop="couponId" label="编号" width="80"></el-table-column>
          <el-table-column prop="couponName" label="优惠券名称" width="150"></el-table-column>
          <el-table-column prop="couponPicture" label="优惠券图片" width="100">
            <template #default="scope">
              <img :src="scope.row.couponPicture" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="minMoney" label="最低使用金额"></el-table-column>
          <el-table-column prop="money" label="抵扣金额"></el-table-column>
          <el-table-column prop="couponType" label="优惠券类型">
            <template #default="scope">
              <div v-if="scope.row.isEnable == 1" style="color: cornflowerblue;">已启用</div>
              <div v-else style="color: red;">未启用</div>
            </template>
          </el-table-column>
          <el-table-column prop="maxReceive" label="可购买数量(0为不限制数量)"></el-table-column>
          <el-table-column prop="couponNum" label="数量"></el-table-column>
          <el-table-column prop="validDays" label="有效期天数"></el-table-column>
          <el-table-column prop="isEnable" label="状态" width="160">
            <template #default="scope">
              <div v-if="scope.row.isEnable == 1" style="color: cornflowerblue;">已启用</div>
              <div v-else style="color: red;">未启用</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="scope">
              <el-button size="small" type="warning" @click="zengsongChangeU(scope.row)"
                style="margin: 5px;">赠送优惠券</el-button>
              <el-button size="small" type="primary" @click="addChangeU(scope.row)" style="margin: 5px;">修改</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)" style="margin: 5px;">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="liaotianSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
    <!-- 弹框 -->
    <!-- 添加/修改 弹框 -->
    <el-dialog v-model="dialogFormVisibleU" :title="titles" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">优惠券类型：</span>
        <el-radio-group v-model="couponType" class="ml-4">
          <el-radio :label="1">酒店</el-radio>
          <el-radio :label="2">民宿</el-radio>
          <el-radio :label="0">全部可用</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">优惠券名称 ：</span>
        <el-input style="width:50%;" v-model="couponName" type="text" placeholder="请输入优惠券名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">优惠券图片 ：</span>
        <div style="display: inline-block;">
          <el-upload class="avatar-uploader" v-model="imageUrl"
            style="width: 148px;height: 148px;line-height: 148px;text-align: center;"
            action="https://jdmsadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess1">
            <img v-if="imageUrl" :src="imageUrl" class="avatar" style="width: 148px;height: 148px;" />
            <el-icon v-else style="font-size: 28px;color: #D9D9D9">
              <Plus />
            </el-icon>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">抵扣金额 ：</span>
        <el-input style="width:50%;" v-model="money" type="number" :min="0" placeholder="请输入抵扣金额"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">最低使用金额 ：</span>
        <el-input style="width:50%;" v-model="minMoney" type="number" :min="0" placeholder="请输入最低使用金额"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">有效期天数 ：</span>
        <el-input style="width:50%;" v-model="validDays" type="number" :min="0" placeholder="请输入有效期天数"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">优惠券数量 ：</span>
        <el-input style="width:50%;" v-model="couponNum" type="number" :min="0" placeholder="请输入优惠券数量"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">最多购买数量(0为不限制数量) ：</span>
        <el-input style="width:50%;" v-model="maxReceive" type="number" :min="0" placeholder="请输入最多购买数量"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
        <el-radio-group v-model="isEnable" class="ml-4">
          <el-radio :label="1">已启用</el-radio>
          <el-radio :label="0">未启用</el-radio>
        </el-radio-group>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 赠送 弹框 -->
    <el-dialog v-model="dialogFormVisibleZs" title="赠送优惠券" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">选择用户：</span>
        <el-input style="width:50%;" v-model="userName" type="text" @click="couponIdBtn1()"
          placeholder="请选择用户昵称"></el-input>
        <!-- <el-tag :key="j" v-for="(tag, j) in userNameList" closable :disable-transitions="false" style="margin: 5px;"
          @close="couponIdClose1(tag, j)">
          {{ tag }}
        </el-tag>
        <el-button class="button-new-tag" size="small" @click="couponIdBtn1()">添加</el-button> -->
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">赠送数量：</span>
        <el-input style="width:50%;" v-model="zsNumber" type="text" placeholder="请输入赠送数量"></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleZs = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="shenheNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="dialogFormVisible" title="用户列表" width="70%" center>
      <div style="margin:2% 0;display: inline-block;">
        <span>手机号:</span>
        <el-input style="width: 150px;" clearable placeholder="请输入手机号" v-model="phoneU"></el-input>
      </div>
      <div style="margin:2% 0;display: inline-block;">
        <span>昵称:</span>
        <el-input style="width: 150px;" clearable placeholder="请输入昵称" v-model="campus"></el-input>
      </div>
      <div style="display: inline-block;">
        <el-button style="margin-left:15px;" size="small" type="primary" icon="document" @click="selectU">查询
        </el-button>
        <el-button style="margin-left:15px;" size="small" type="primary" icon="document" @click="cleansU">重置
        </el-button>
      </div>
      <el-table :data="userData" v-loading="tableDataLoadingU" style="width: 100%">
        <el-table-column prop="userId" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="userName" label="用户昵称" width="120">
          <template #default="scope">
            <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{
              scope.row.userName ? scope.row.userName : '未绑定' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="avatar" label="用户头像" width="100">
          <template #default="scope">
            <img :src="scope.row.avatar" width="60" height="60" />
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号">
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
        </el-table-column>
        <el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
          <template #default="scope">
            <el-button size="small" type="primary" @click="userBtns(scope.row)">确定
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectUser()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>

  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  onMounted
} from 'vue';
import pagination from '@/components/pagination/pagination.vue'
import {
  ElMessage,
  ElNotification
} from 'element-plus';
const {
  proxy
} = getCurrentInstance();
const activeName = ref('first'); //默认选中
const liaotianData = reactive([]); //数据 集合
const page = ref(1);//当前页
const limit = ref(10);//每页条数
const total = ref(0);//总条数

const tableDataLoading = ref(false);//列表是否加载中
const dialogFormVisibleU = ref(false);
const routeUrl = ref('');
const titles = ref('添加');

const classify = ref('');
const id = ref('');
const name = ref('');
const expireDay = ref('');
const minAmount = ref('');
const amount = ref('');
const imageUrl = ref('');
const dialogFormVisibleZs = ref(false);
const userNameList = reactive([]);
const zsNumber = ref('');
const dialogFormVisible = ref(false);
const tableDataLoadingU = ref(false);
const userData = reactive([]);
const page1 = ref(1);
const limit1 = ref(10);
const total1 = ref(0);
const campus = ref('');
const phoneU = ref('');
const laundryIdU = ref('');
const userId = ref(''); //用户id
const userName = ref('');

const couponType = ref('');
const couponName = ref('');
const validDays = ref('');
const minMoney = ref('');
const money = ref('');
const maxReceive = ref('');
const couponNum = ref('');
const isEnable = ref('');

onMounted(() => {
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '优惠券列表') {
    classify.value = 1
    liaotianSelect();
  }
}

//分类数据 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.get('/admin/coupon/getCouponPageList', {
    params: {
      page: page.value,
      limit: limit.value,
      // name: '',
      // status: '',
    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    liaotianData.splice(0, liaotianData.length, ...res.data.records)
    total.value = res.data.total;
  })


}
// 刷新 分类
const handleSearch = () => {
  page.value = 1
  liaotianSelect()
}


// 添加/修改 分类 弹框
const addChangeU = (row) => {
  if (row) {

    titles.value = '修改'
    id.value = row.couponId
    couponName.value = row.couponName
    imageUrl.value = row.img
    couponType.value = row.couponType
    validDays.value = row.validDays
    minMoney.value = row.minMoney
    money.value = row.money
    maxReceive.value = row.maxReceive
    couponNum.value = row.couponNum
    amount.value = row.amount
    isEnable.value = row.isEnable



  } else {
    titles.value = '添加'
    id.value = ''
    couponName.value = ''
    couponType.value = 1
    validDays.value = ''
    minMoney.value = ''
    imageUrl.value = ''
    money.value = ''
    maxReceive.value = ''
    couponNum.value = ''
    isEnable.value = 1




  }
  dialogFormVisibleU.value = true
}
const StairNoticeToU = () => {
  if (couponName.value == '') {
    ElNotification({
      title: '提示',
      message: '优惠券名称不能为空',
      duration: 3000,
      type: 'warning'
    })
    return
  }
  if (Number(money.value) === '') {
    ElNotification({
      title: '提示',
      message: '抵扣金额不能为空',
      duration: 3000,
      type: 'warning'
    })
    return
  }
  if (Number(minMoney.value) === '') {
    ElNotification({
      title: '提示',
      message: '最低使用金额不能为空',
      duration: 3000,
      type: 'warning'
    })
    return
  }
  if (Number(minMoney.value) <= Number(money.value)) {
    ElNotification({
      title: '提示',
      message: '最低使用金额必须大于抵扣金额',
      duration: 3000,
      type: 'warning'
    })
    return
  }

  if (Number(validDays.value) === '') {
    ElNotification({
      title: '提示',
      message: '有效天数不能为空',
      duration: 3000,
      type: 'warning'
    })
    return
  }
  if (titles.value == '添加') {
    routeUrl.value = '/admin/coupon/addCoupon'
  } else {
    routeUrl.value = '/admin/coupon/updateCoupon'
  }
  proxy.$Request.post(routeUrl.value, {
    couponId: id.value,
    couponName: couponName.value,
    couponType: couponType.value,
    validDays: validDays.value,
    minMoney: minMoney.value,
    img: imageUrl.value,
    money: money.value,
    maxReceive: maxReceive.value,
    couponNum: couponNum.value,
    isEnable: isEnable.value,


  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleU.value = false
          liaotianSelect()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
        }
      })
    }
  })

}
/** 删除 */
const handleDelete = (data) => {
  proxy.$Request.post('/admin/coupon/deleteCoupon', {
    couponId: data.couponId,
  }, {
    type: 'form-data'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          liaotianSelect()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
        }
      })
    }
  })

}

const handleAvatarSuccess1 = (file) => {
  imageUrl.value = file.data
}
// 赠送优惠券
const zengsongChangeU = (row) => {
  id.value = row.couponId
  zsNumber.value = ''
  userId.value = ''
  userName.value = ''
  dialogFormVisibleZs.value = true
}
const couponIdBtn1 = () => {
  page1.value = 1
  campus.value = ''
  phoneU.value = ''
  laundryIdU.value = ''
  dialogFormVisible.value = true
  dataSelectUser()
}
// 获取 用户列表 数据
const dataSelectUser = () => {

  tableDataLoadingU.value = true
  userData.splice(0, userData.length)


  proxy.$Request.get('/user/selectUserList', {
    params: {
      page: page1.value,
      limit: limit1.value,
      userName: campus.value,
      phone: phoneU.value,
      isVip: '',
      status: '',
      platform: '',
      userType: '',
      inviterCode: '',

    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoadingU.value = false;
    userData.splice(0, userData.length, ...res.data.list)
    total1.value = res.data.total;
  })
}
// 查询
const selectU = () => {
  page1.value = 1
  dataSelectUser()
}
// 重置
const cleansU = () => {
  page1.value = 1
  campus.value = ''
  phoneU.value = ''
  laundryIdU.value = ''
  dataSelectUser()
}

// 确认选择
const userBtns = (row) => {
  userId.value = row.userId
  userName.value = row.userName
  dialogFormVisible.value = false
}

const couponIdClose1 = () => {

}
const shenheNoticeToU = () => {

  proxy.$Request.post('/admin/couponUser/giveUserCoupon', {
    couponId: id.value,
    userIds: userId.value,
    num: zsNumber.value,
  }, {
    type: 'form-data'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleZs.value = false
          liaotianSelect()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
        }
      })
    }
  })
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

.el-rows {
  margin-top: 20px;
}

span {
  color: #8c939d;
}

.main-view {
  display: flex;
  flex-direction: column;
  width: 100%;

  .container {
    // background-color: white;
    width: 100%;
    // min-height: 800px;
    height: fit-content;
    border-radius: 5px;
    padding: 30px;
    box-sizing: border-box;

    >* {
      margin: 0 0 30px 0;

      &:last-child {
        margin: 0;
      }
    }
  }
}

.detailtabel h2 {
  margin-top: 0;
}

.detailtabel .table_main table {
  width: 100%;
}

.detailtabel .table_main {
  border: 1px solid #e8e8e8;
}

.detailtabel table tr {
  border-bottom: 1px solid #e8e8e8;
}

.detailtabel table tr th {
  background-color: #fafafa;
  padding: 16px 24px;
  border-right: 1px solid #e8e8e8;
  border-bottom: 1px solid #e8e8e8;
}

.detailtabel table tr td {
  padding: 16px 24px;
  border-right: 1px solid #e8e8e8;
  border-bottom: 1px solid #e8e8e8;
}

.detailtabel table th {
  color: rgba(0, 0, 0, 0.85);
  font-weight: 400;
  font-size: 14px;
  line-height: 1.5;
}

.border-rt {
  border-right: none !important;
}

.border-bt {
  border-bottom: none !important;
}

.el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-color: #409EFF;
  width: 148px;
  height: 148px;
}

.el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 148px;
  height: 148px;
  line-height: 148px;
  text-align: center;
}

.avatar {
  width: 148px;
  height: 148px;
  display: block;
}
</style>
