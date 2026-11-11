<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="评论列表" name="first">
        <div style="text-align:left;margin: 5px;">
          <div style="display: inline-block;margin: 5px;">
            <span>评论用户 ：</span>
            <el-input v-model="userNameT" placeholder="请输入评论用户" style="width: 150px;"
              @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>评论酒店 ：</span>
            <el-input v-model="nameT" placeholder="请输入评论酒店" style="width: 150px;"
              @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>订单编号 ：</span>
            <el-input v-model="orderNoT" placeholder="请输入订单编号" style="width: 150px;"
              @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>关键字 ：</span>
            <el-input v-model="cityT" placeholder="请输入关键字" style="width: 150px;" @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <el-button size="small" type="primary" plain @click="handleSearch()">查询</el-button>
          <el-button size="small" type="primary" @click="clearSearch()">重置</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="evaluateId" label="编号" width="80">
          </el-table-column>
          <el-table-column prop="userName" label="评论用户" width="120">
            <template #default="scope">
              <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{
                scope.row.userName ? scope.row.userName : '未绑定' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="avatar" label="用户头像" width="120">
            <template #default="scope">
              <div v-if="scope.row.avatar">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                  v-for="(item, index) in scope.row.avatar.split(',')" :key="index">
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
          <el-table-column prop="shopName" label="评论酒店" width="160">
          </el-table-column>
          <el-table-column prop="titleImg" label="酒店图片" width="120">
            <template #default="scope">
              <div v-if="scope.row.titleImg">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
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

          <el-table-column prop="orderNumber" label="订单编号" width="160">
          </el-table-column>
          <el-table-column prop="evaluateMessage" label="内容">
          </el-table-column>
          <el-table-column prop="evaluateImg" label="评论图片">
            <template #default="scope">
              <div v-if="scope.row.evaluateImg">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                  v-for="(item, index) in scope.row.evaluateImg.split(',')" :key="index">
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

          <el-table-column prop="satisfactionFlag" label="评分" width="160">
            <template #default="scope">
              <el-rate v-model="scope.row.satisfactionFlag" disabled show-score text-color="#ff9900"
                score-template="" />
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
          </el-table-column>
          <!-- <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" @click="addChangeU(scope.row)" style="margin: 5px;">修改</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)" style="margin: 5px;">删除</el-button>
            </template>
</el-table-column> -->
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
  ElMessage
} from 'element-plus';
import { useRouter } from 'vue-router';
const router = useRouter();
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
const city = ref('');//城市
const cover = ref('');//封面
const popular = ref(1); //热门 1=是 2=否
const choiceness = ref(1); //精选 1=是 2=否
const description = ref(''); //描述
const sort = ref(''); //排序
const img = reactive([]); //相册



const percentage2 = ref(0);
const popularT = ref('');
const choicenessT = ref('');
const overseas = ref('');
const cityT = ref('');
const userNameT = ref('');
const nameT = ref('');
const orderNoT = ref('');
const statesnum = reactive([{
  value: '',
  label: '全部'
}, {
  value: 1,
  label: '是'
}, {
  value: 2,
  label: '否'
}]);


onMounted(() => {
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '评论列表') {
    classify.value = 1
    liaotianSelect();
  }
}

//分类数据 列表
const liaotianSelect = () => {
  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.get('/admin/evaluate/getUserEvaluate', {
    params: {
      page: page.value,
      limit: limit.value,
      content: cityT.value,
      userName: userNameT.value,
      shopName: nameT.value,
      orderNo: orderNoT.value,


      // type: 1,
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
const clearSearch = () => {
  page.value = 1
  cityT.value = ''
  userNameT.value = ''
  nameT.value = ''
  orderNoT.value = ''
  liaotianSelect()
}

// 添加/修改 分类 弹框
const addChangeU = (row) => {
  if (row) {
    titles.value = '修改'
    id.value = row.id
    city.value = row.city
    cover.value = row.cover
    popular.value = row.popular
    choiceness.value = row.choiceness
    description.value = row.description
    sort.value = row.sort
    if (row.album) {
      img.splice(0, img.length, ...row.album.split(','))
    } else {
      img.value = []
    }
    location.value = row.location
  } else {
    titles.value = '添加'
    id.value = ''
    city.value = ''
    cover.value = ''
    popular.value = 1
    choiceness.value = 1
    description.value = ''
    sort.value = ''
    img.value = []


  }
  percentage2.value = 0
  dialogFormVisibleU.value = true
}

const StairNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/destination/save'
  } else {
    routeUrl.value = '/admin/destination/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    city: city.value,
    album: img.toString(),
    cover: cover.value,
    popular: popular.value,
    choiceness: choiceness.value,
    description: description.value,
    sort: sort.value,

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
  proxy.$Request.post('/admin/destination/delete', {
    id: data.id,
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
}

function handleAvatarSuccess1(file) {
  cover.value = file.data.path
}
const clear2 = (index) => {
  img.splice(index, 1);
}
const handleUploadSuccess4 = (file) => {
  img.push(file.data.path)
}
const onprogress2 = (event,) => {
  console.log('event,', parseInt(event.percent))
  percentage2.value = parseInt(event.percent)
}
/** 跳转用户详情 */
const updates = (userId) => {
  router.push({
    name: 'userDetail',
    query: {
      userId: userId,
    },

  });
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

.imgs {
  position: relative;
  border-radius: 6px;
  width: 148px;
  height: 148px;
  margin-right: 10px;
  display: inline-block;
}

.dels {
  position: absolute;
  top: 0;
  left: 0;
  display: none;
}

.dels .el-icon-delete {
  line-height: 148px;
  padding-left: 58px;
  font-size: 25px;
  color: #fff;

}

.imgs:hover .dels {
  width: 100%;
  height: 100%;
  background: #000;
  display: block;
  opacity: 0.5;
  line-height: 148px;
}

span {
  font-size: 14px;
  color: #303133;
}
</style>
