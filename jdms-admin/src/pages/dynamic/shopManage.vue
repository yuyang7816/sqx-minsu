<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">

      <el-tab-pane label="动态管理" name="first">
        <div style="margin: 20px;">
          <div style="display: inline-block;margin: 5px;">
            <span>发布用户</span>
            <el-input v-model="userNameT" placeholder="请输入发布用户" style="width: 150px;" />
          </div>
          <!-- <div style="display: inline-block;margin: 5px;">
                        <span>位置：</span>
                        <el-input v-model="locationT" placeholder="请输入位置"
                            @keydown.enter.native="handleSearch" style="width: 150px;" />
                    </div> -->
          <div style="display: inline-block;margin: 5px;">
            <span>权限设置：</span>
            <el-select v-model="privacyT" class="m-2" placeholder="请选择权限设置" style="width: 150px;"
              @change="handleSearch">
              <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div>
          <el-button size="small" type="primary" plain @click="handleSearch()">查询</el-button>
          <el-button size="small" type="warning" plain @click="clearSearch()">重置</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="dongtaiData" style="width: 100%">
          <el-table-column prop="id" label="编号" width="100" fixed="left"></el-table-column>
          <el-table-column prop="userName" label="发布用户" fixed="left" width="150">
            <template #default="scope">
              <span v-if="scope.row.userName" style="color: #13ce66;cursor: pointer;"
                @click="updates(scope.row.userId)">
                {{ scope.row.userName ? scope.row.userName : '未绑定' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="avatar" label="头像" width="100">
            <template #default="scope">
              <img :src="scope.row.avatar" style="width: 60px;height: 60px;" />
            </template>
          </el-table-column>
          <el-table-column prop="imgUrlList" label="图片" width="300">
            <template #default="scope">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                v-for="(item, index) in scope.row.imgUrlList" :key="index">
                <template #reference>
                  <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="">
                </template>
                <template #default>
                  <img style="width: 400px; height: auto;" :src="item" alt="">
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="content" label="文字描述"></el-table-column>
          <el-table-column prop="privacy" label="权限设置" width="100" fixed="right">
            <template #default="scope">
              <span v-if="scope.row.privacy == 0">仅自己可见</span>
              <span v-if="scope.row.privacy == 1">仅好友</span>
              <span v-if="scope.row.privacy == 2">公开</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" @click="dianzanChange(scope.row)"
                style="margin: 5px;">点赞列表</el-button>
              <el-button size="small" type="primary" @click="pinglunChange(scope.row)"
                style="margin: 5px;">评论列表</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)" style="margin: 5px;">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="dongtaiSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>

    </el-tabs>
    <!-- 弹框 -->

    <!-- 动态评论 弹框 -->
    <el-dialog v-model="dialogTableVisible2" title="动态评论" center width="80%">
      <el-table v-loading="tableDataLoading1" :data="pinglunDatas.records">
        <el-table-column prop="commentId" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="nickName" label="用户昵称">
          <template #default="scope">
            <div style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">
              {{ scope.row.nickName ? scope.row.nickName : '未绑定' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="imageUrl" label="用户头像">
          <template #default="scope">
            <div v-if="scope.row.imageUrl == null || scope.row.imageUrl == ''">
              暂无图片
            </div>
            <div v-else>
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                <template #reference>
                  <img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.imageUrl" alt="">
                </template>
                <template #default>
                  <img style="width: 400px; height: auto;" :src="scope.row.imageUrl" alt="">
                </template>
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="信息内容" width="280">
          <template #default="scope">
            <div>{{ scope.row.content }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150">
        </el-table-column>
      </el-table>

    </el-dialog>

    <!-- 点赞列表 -->
    <el-dialog v-model="dialogFormVisibleDz" title="点赞列表" width="80%" center>
      <el-table :data="tableDataDz" v-loading="loading" style="width: 100%">
        <el-table-column prop="userId" label="编号" width="100"></el-table-column>
        <el-table-column prop="userName" label="点赞用户">
          <template #default="scope">
            <span v-if="scope.row.userName" style="color: #13ce66;cursor: pointer;" @click="updates(scope.row.userId)">
              {{ scope.row.userName ? scope.row.userName : '未绑定' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="avatar" label="用户头像">
          <template #default="scope">
            <img :src="scope.row.avatar" style="width: 60px;height: 60px;" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="点赞时间" width="180"></el-table-column>
      </el-table>
    </el-dialog>
    <!-- 评论列表 -->
    <el-dialog v-model="dialogFormVisiblePl" title="评论列表" width="80%" center>
      <el-table :data="tableDataPl" v-loading="loading" style="width: 100%">
        <el-table-column prop="userId" label="编号" width="100"></el-table-column>
        <el-table-column prop="userName" label="评论用户" width="160">
          <template #default="scope">
            <span v-if="scope.row.userName" style="color: #13ce66;cursor: pointer;" @click="updates(scope.row.userId)">
              {{ scope.row.userName ? scope.row.userName : '未绑定' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="avatar" label="用户头像" width="100">
          <template #default="scope">
            <img :src="scope.row.avatar" style="width: 60px;height: 60px;" />
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评论内容"></el-table-column>
        <el-table-column prop="createTime" label="评论时间" width="180"></el-table-column>
      </el-table>
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
import qrcode from 'qrcode';
import {
  ElMessageBox,
  ElMessage
} from 'element-plus';
const {
  proxy
} = getCurrentInstance();
const route = useRoute();
const router = useRouter();
const activeName = ref('first'); //默认选中
const userList = reactive([]);//列表集合
const tableData = ref({}); //用户详情
const dongtaiData = reactive([]); //我的动态 集合
const friendData = reactive([]); //我的通讯录 集合
const qunzuData = reactive([]); //我的群组 集合
const tousuData = reactive([]); //好友请求 集合
const liaotianData = reactive([]); //聊天记录 集合
const huihuaData = reactive([]); //聊天内容 集合
const tableDataDz = reactive([]); //点赞 集合
const tableDataPl = reactive([]); //评论 集合
const tableDataCy = reactive([]); //群成员 集合
const tableDataLt = reactive([]); //群聊天 集合



const page = ref(1);//当前页
const limit = ref(10);//每页条数
const total = ref(0);//总条数
const page1 = ref(1);//当前页
const limit1 = ref(10);//每页条数
const total1 = ref(0);//总条数

//列表是否加载中
const dialogFormVisibleU = ref(false);
const dialogFormVisible4 = ref(false);
const dialogFormVisibleDz = ref(false);
const dialogFormVisiblePl = ref(false);
const dialogFormVisibleCy = ref(false);
const dialogFormVisibleLt = ref(false);
const tableDataLoading = ref(false);
const tableDataLoading1 = ref(false);
const loadingCy = ref(false);
const receiverUserId = ref('');
const senderUserId = ref('');
const id = ref('');
const userNameC = ref('');
const phoneC = ref('');
const typeC = ref('');
const contentC = ref('');
const targetId = ref('');
const qrCodeData = ref('');
const loadingerweima = ref(false);
const userNameT = ref('');
const locationT = ref('');
const privacyT = ref('');

//聊天类型 集合
const statusData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '仅自己可见',
    value: 0
  },
  {
    label: '仅好友',
    value: 1
  },
  {
    label: '公开',
    value: 2
  }
]);



// 修改用户信息
const loading = ref(false);
const userNameU = ref('');
const phoneU = ref('');
const inviterCodeU = ref('');
const invitationCodeU = ref('');
const avatarU = ref('');
const ageU = ref('');
const sexU = ref('');
const msgU = ref('');
// 修改钱包弹框的
const dialogFormVisible1 = ref(false);
const type = ref('');
const money = ref('');


onMounted(() => {
  dongtaiSelect();
});
//跳转用户详情
const updates = (userId) => {
  router.push({
    name: 'userDetail1',
    query: {
      userId: userId,
    },

  });
};
//获取用户详情
const getUserList = () => {
  loading.value = true
  const userId = route.query.userId
  userList.splice(0, userList.length)
  proxy.$Request.get('/admin/base/user/info', {
    params: {
      userId: userId,
    }
  }, {
    type: 'json'
  }).then(res => {
    // console.log(res)
    loading.value = false;
    if (res.code == 0) {
      let returnData = res.data
      tableData.value = returnData
    }
  })
};
// 切换tab 事件
const handleClick = (tab, event) => {
  console.log(event.srcElement.innerText)


  if (event.srcElement.innerText == '动态管理') {
    dongtaiSelect();
  }
  if (event.srcElement.innerText == '我的通讯录') {
    haoyouSelect();
  }
  if (event.srcElement.innerText == '我的群组') {
    qunzuSelect();
  }
  if (event.srcElement.innerText == '好友请求') {
    qingqiuSelect();
  }
  if (event.srcElement.innerText == '聊天记录') {
    liaotianSelect();
  }
}

// 我的动态 列表
const dongtaiSelect = () => {
  tableDataLoading.value = true
  dongtaiData.splice(0, dongtaiData.length)
  proxy.$Request.get('admin/posts/page', {
    params: {
      page: page.value,
      limit: limit.value,
      userName: userNameT.value,
      location: locationT.value,
      privacy: privacyT.value,

    }
  }, {}).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    dongtaiData.splice(0, dongtaiData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
const handleSearch = () => {
  page.value = 1
  dongtaiSelect()
}
const clearSearch = () => {
  userNameT.value = ''
  locationT.value = ''
  privacyT.value = ''
  page.value = 1
  dongtaiSelect()
}
/** 点赞用户 弹框 */
const dianzanChange = (row) => {

  tableDataDz.splice(0, tableDataDz.length, ...row.imPostsLikeDataList)
  dialogFormVisibleDz.value = true

}
/** 评论用户 弹框 */
const pinglunChange = (row) => {

  tableDataPl.splice(0, tableDataPl.length, ...row.imPostsCommentDataList)
  dialogFormVisiblePl.value = true
}
// 删除动态
const handleDelete = (row) => {
  proxy.$Request.get('/admin/posts/delete', {
    params: {
      postsId: row.id,
      userId: row.userId,
    }

  }, {}).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        type: 'success',
        message: '操作成功'
      });
      dongtaiSelect();
    } else {
      ElMessage({
        type: 'warning',
        message: resizeBy.msg,
      })
    }
  })
}

// 我的通讯录 列表
const haoyouSelect = () => {
  tableDataLoading.value = true
  friendData.splice(0, friendData.length)
  let userId = route.query.userId
  proxy.$Request.get('admin/friend/page', {
    params: {
      userId: userId,
      page: page.value,
      limit: limit.value,

    }
  }, {}).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    friendData.splice(0, friendData.length, ...res.data.records)
    total.value = res.data.total;
  })

}
// 我的群组 列表
const qunzuSelect = () => {
  tableDataLoading.value = true
  qunzuData.splice(0, qunzuData.length)
  let userId = route.query.userId
  proxy.$Request.get('admin/group/getMyGroupList', {
    params: {
      userId: userId,
      page: page.value,
      limit: limit.value,

    }
  }, {}).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    qunzuData.splice(0, qunzuData.length, ...res.data)
    total.value = res.data.total;
  })
}
/** 群成员 弹框 */
const cyChange = (row) => {
  id.value = row.id
  userNameC.value = ''
  phoneC.value = ''
  dialogFormVisibleCy.value = true
  dataSelectCy();

}
/** 群成员 数据 */
const dataSelectCy = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.get('admin/group/groupUserList', {
    params: {
      groupId: id.value,
      userName: userNameC.value,
      phone: phoneC.value,

    }
  }, {}).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataCy.splice(0, tableDataCy.length, ...res.data)
    total1.value = res.data.total;
  })
}
const handleSearchCy = () => {
  dataSelectCy();
}
const clearSearchCy = () => {
  userNameC.value = ''
  phoneC.value = ''
  dataSelectCy();
}
/** 聊天记录 弹框 */
const ltChange = (row) => {
  id.value = row.id
  page1.value = 1
  userNameC.value = ''
  phoneC.value = ''
  typeC.value = ''
  contentC.value = ''
  dataSelectLt()
  dialogFormVisibleLt.value = true
}
/** 聊天记录 数据 */
const dataSelectLt = () => {
  loadingCy.value = true
  tableDataLt.splice(0, tableDataLt.length)
  proxy.$Request.get('admin/history/page', {
    params: {
      page: page1.value,
      limit: limit1.value,
      groupId: id.value,
      userName: userNameC.value,
      phone: phoneC.value,
      targetType: 2,
      type: typeC.value,
      content: contentC.value,

    }
  }, {}).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataLt.splice(0, tableDataLt.length, ...res.data)
    total1.value = res.data.total;
  })




}
const erweimaChange = (row) => {
  targetId.value = row.id
  var qrCodeDatas = targetId.value + '-getQrCode';
  qrcode.toDataURL(qrCodeDatas, (err, url) => {
    if (err) {
      console.error(err);
    } else {
      qrCodeData.value = url;
    }
  });
  loadingerweima.value = true;
}


// 好友请求 列表
const qingqiuSelect = () => {
  tableDataLoading.value = true
  tousuData.splice(0, tousuData.length)
  let userId = route.query.userId
  proxy.$Request.get('admin/friendRequest/list', {
    params: {
      userId: userId,
      page: page.value,
      limit: limit.value,

    }
  }, {}).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    tousuData.splice(0, tousuData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
// 聊天记录 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, tousuData.length)
  let userId = route.query.userId
  proxy.$Request.post('/admin/history/chatHistory', {
    userId: userId,
    page: page.value,
    limit: limit.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    liaotianData.splice(0, liaotianData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
//         /**  聊天内容*/
const complainDetails = (row) => {


  page1.value = 1
  dialogFormVisible4.value = true
  receiverUserId.value = row.receiverUserId
  senderUserId.value = row.senderUserId
  neirongSelect()
}
/**  获取聊天内容列表*/
const neirongSelect = () => {
  tableDataLoading1.value = true
  huihuaData.splice(0, tousuData.length)
  proxy.$Request.post('/admin/history/chatHistory', {
    receiverUserId: receiverUserId.value,
    senderUserId: senderUserId.value,
    page: page.value,
    limit: limit.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading1.value = false;
    huihuaData.splice(0, huihuaData.length, ...res.data.records)
    total1.value = res.data.total;
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
