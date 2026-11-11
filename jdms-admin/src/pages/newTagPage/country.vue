<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="申请列表" name="first">
        <div style="text-align:rightz;margin: 5px;">
          <div style="display: inline-block;margin: 5px;">
            <span>类型 ：</span>
            <el-select v-model="shopTypeT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="handleSearch">
              <el-option v-for="(item, index) in shopTypeData" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>状态 ：</span>
            <el-select v-model="statusT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="handleSearch">
              <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>酒店名称 ：</span>
            <el-input v-model="nameT" placeholder="请输入姓名" @keydown.enter.native="handleSearch" style="width: 150px;" />
          </div>&nbsp;&nbsp;

          <!-- <div style="display: inline-block;margin: 5px;">
            <span>工作年限 ：</span>
            <el-input v-model="workYearT" placeholder="请输入工作年限" @keydown.enter.native="handleSearch"
              style="width: 150px;" />
          </div>&nbsp;&nbsp; -->
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="handleClear()">重置</el-button>
        </div>
        <el-table :data="liaotianData" v-loading="tableDataLoading" style="width: 100%">
          <el-table-column prop="shopId" label="编号" width="80" fixed="left">
          </el-table-column>
          <el-table-column prop="shopType" label="类型">
            <template #default="scope">
              <span v-if="scope.row.shopType == 1">酒店</span>
              <span v-if="scope.row.shopType == 2">民宿</span>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="name" label="姓名" width="120" fixed="left">
            <template #default="scope">
              <div style="color: #4f9dec;cursor: pointer;" v-if="scope.row.name" @click="updates(scope.row.userId)">
                {{ scope.row.name }}
              </div>
              <div v-else>-</div>
            </template>
          </el-table-column> -->
          <el-table-column prop="shopName" label="酒店名称" width="160">
          </el-table-column>
          <el-table-column prop="shopPhone" label="酒店电话" width="160">
          </el-table-column>
          <el-table-column prop="titleImg" label="封面图" width="100">
            <template #default="scope">
              <div v-if="scope.row.titleImg" style="display:flex;flex-wrap: wrap;">
                <el-popover placement="top-start" title="" trigger="hover"
                  popper-style="width:450px;max-height:500px;overflow: auto;"
                  v-for="(item, index) in scope.row.titleImg.split(',')" :key="index">
                  <template #reference>
                    <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="">
                  </template>
                  <template #default>
                    <img style="width: 400px; height: auto;" :src="item" alt="">
                  </template>
                </el-popover>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="carouselUrl" label="轮播图" width="300">
            <template #default="scope">
              <div v-if="scope.row.carouselUrl" style="display:flex;flex-wrap: wrap;">
                <el-popover placement="top-start" title="" trigger="hover"
                  popper-style="width:450px;max-height:500px;overflow: auto;"
                  v-for="(item, index) in scope.row.carouselUrl.split(',')" :key="index">
                  <template #reference>
                    <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="">
                  </template>
                  <template #default>
                    <img style="width: 400px; height: auto;" :src="item" alt="">
                  </template>
                </el-popover>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="rate" label="分成比例">
          </el-table-column>
          <el-table-column prop="score" label="评分">
          </el-table-column>
          <el-table-column prop="tags" label="标签">
          </el-table-column>
          <el-table-column prop="province" label="酒店地址" width="200">
            <template #default="scope">
              {{ scope.row.province }}{{ scope.row.city }}{{ scope.row.region }}{{ scope.row.address }}
            </template>
          </el-table-column>
          <el-table-column prop="lng" label="经度" width="200"></el-table-column>
          <el-table-column prop="lat" label="纬度" width="200"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
          </el-table-column>
          <el-table-column prop="auditStatus" label="状态" fixed="right">
            <template #default="scope">
              <span v-if="scope.row.auditStatus == 1">已通过</span>
              <span v-if="scope.row.auditStatus == 2">已拒绝</span>
              <span v-if="scope.row.auditStatus == 3">待审核</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" @click="shenheChange(scope.row)" style="margin: 5px;"
                v-if="scope.row.auditStatus == 3">审核</el-button>
              <!-- <el-button size="small" type="primary" @click="addChangeU(scope.row)" style="margin: 5px;"
                :disabled="!isAuth('vipPrivilege:update')">修改</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)" style="margin: 5px;"
                :disabled="!isAuth('vipPrivilege:delete')">删除</el-button> -->
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
    <el-dialog v-model="dialogFormVisibleU" title="审核" width="60%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
        <el-radio-group v-model="status" class="ml-4">
          <el-radio :label="1">通过</el-radio>
          <el-radio :label="2">拒绝</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;position: relative;bottom: 70px;">审核说明：</span>
        <el-input style="width:50%;" v-model="name" type="textarea" :rows="4" placeholder="请输入审核说明"></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
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
import { useRouter, useRoute } from 'vue-router';
import pagination from '@/components/pagination/pagination.vue'
import {
  ElMessageBox,
  ElMessage
} from 'element-plus';
const router = useRouter();
const route = useRoute();
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
const titles = ref('审核');

const classify = ref('');
const id = ref('');
const name = ref('');
const imageUrl = ref('');
const sort = ref('');
const status = ref(1);
const statusT = ref('');
const nameT = ref('');
const phoneT = ref('');
const workYearT = ref('');
const shopTypeT = ref('');
const statusData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '已通过',
    value: 1
  },
  {
    label: '已拒绝',
    value: 2
  },
  {
    label: '待审核',
    value: 3
  }
])
const shopTypeData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '酒店',
    value: 1
  },
  {
    label: '民宿',
    value: 2
  }
])

onMounted(() => {
  if (route.query.status != undefined && route.query.status) {
    statusT.value = Number(route.query.status)
  } else {
    statusT.value = ''
  }
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '申请列表') {
    classify.value = 1
    liaotianSelect();
  }
}

//分类数据 列表
const liaotianSelect = () => {
  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.get('/admin/shops/getShopList', {
    params: {
      page: page.value,
      limit: limit.value,
      shopName: nameT.value,
      shopType: phoneT.value,
      shopType: shopTypeT.value,
      auditStatus: statusT.value,


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
// 重置
const handleClear = () => {
  page.value = 1
  nameT.value = ''
  phoneT.value = ''
  workYearT.value = ''
  statusT.value = ''
  shopTypeT.value = ''
  liaotianSelect()
}

// 添加/修改 分类 弹框
const addChangeU = (row) => {
  if (row) {
    titles.value = '修改'
    id.value = row.id
    name.value = row.memberName
    imageUrl.value = row.memberImg
    sort.value = row.sort
  } else {
    titles.value = '添加'
    id.value = ''
    name.value = ''
    imageUrl.value = ''
    sort.value = ''
  }
  dialogFormVisibleU.value = true
}

const StairNoticeToU = () => {

  proxy.$Request.post('/admin/shops/auditShop', {
    shopId: id.value,
    auditRemark: name.value,
    auditStatus: status.value,
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
const handleshenhe = (data) => {
  ElMessageBox.confirm(`确定要更改用户状态吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    proxy.$Request.post('/app/hairdresserApply/updateStatus', {
      id: data.id,
      status: 2,
    }, {
      type: 'json'
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

  })

}

function handleAvatarSuccess1(file) {
  imageUrl.value = file.data.path
}
//跳转用户详情
const updates = (userId) => {
  router.push({
    name: 'userDetail1',
    query: {
      userId: userId,
    },

  });
};
// 审核 弹框
const shenheChange = (row) => {
  id.value = row.shopId
  status.value = ''
  name.value = ''
  dialogFormVisibleU.value = true

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

span {
  font-size: 14px;
  color: #303133;
}
</style>
