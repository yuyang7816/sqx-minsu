<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="聊天会话" name="first">
        <div style="margin: 20px;">
          <div style="display: inline-block;margin: 5px;">
            <span>用户昵称：</span>
            <el-input v-model="userNameT" placeholder="请输入用户昵称" style="width: 150px;" />
          </div>
          <div style="display: inline-block;margin: 5px;">
            <span>内容：</span>
            <el-input v-model="contentT" placeholder="请输入内容" style="width: 150px;" />
          </div>
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="clearSearch()">重置</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="id" label="编号" width="100">
            <template #default="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="senderUserName" label="用户昵称">
            <template #default="scope">
              <div v-if="scope.row.senderUserName" style="color: #13ce66;cursor: pointer;"
                @click="updates(scope.row.senderUserId)">
                {{ scope.row.senderUserName ? scope.row.senderUserName : '未绑定' }}
              </div>
              <div v-if="scope.row.receiverUserName" style="color: #13ce66;cursor: pointer;"
                @click="updates(scope.row.receiverUserId)">
                {{ scope.row.receiverUserName ? scope.row.receiverUserName : '未绑定' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="senderAvatar" label="用户头像">
            <template #default="scope">
              <img v-if="scope.row.senderAvatar" :src="scope.row.senderAvatar"
                style="width: 60px;height: 60px;margin: 3px;" />
              <img v-if="scope.row.receiverAvatar" :src="scope.row.receiverAvatar"
                style="width: 60px;height: 60px;margin: 3px;" />
            </template>
          </el-table-column>
          <el-table-column prop="messageCount" label="信息数量" width="180"></el-table-column>

          <el-table-column prop="lastMessageTime" label="创建时间" width="180"></el-table-column>
          <el-table-column fixed='right' label="操作" width="150">
            <template #default="scope">
              <el-button type="primary" @click="complainDetails(scope.row)">聊天内容
              </el-button>
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

    <!-- 聊天内容 -->
    <el-dialog title="聊天内容" v-model="dialogFormVisible4" center width="70%">
      <el-table v-loading="tableDataLoading1" :data="huihuaData">
        <el-table-column prop="id" label="编号" width="100">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="senderUserName" label="用户昵称">
          <template #default="scope">
            <span v-if="scope.row.senderUserName" style="color: #13ce66;cursor: pointer;"
              @click="updates(scope.row.senderUserId)">
              {{ scope.row.senderUserName ? scope.row.senderUserName : '未绑定' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="senderAvatar" label="用户头像">
          <template #default="scope">
            <img v-if="scope.row.senderAvatar" :src="scope.row.senderAvatar" style="width: 60px;height: 60px;" />
          </template>
        </el-table-column>
        <el-table-column prop="type" label="内容类型">
          <template #default="scope">
            <span v-if="scope.row.type == 1">普通消息</span>
            <span v-if="scope.row.type == 2">表情</span>
            <span v-if="scope.row.type == 3">图片</span>
            <span v-if="scope.row.type == 4">视频</span>
          </template>
        </el-table-column>
        <el-table-column prop="messageCount" label="聊天内容">
          <template #default="scope">
            <span v-if="scope.row.type == 1">{{ scope.row.messageCount }}</span>
            <span v-if="scope.row.type == 2">
              <img style="width: 20px; height: 20px;margin: 3px;"
                :src="'https://im.xianmaxiong.com/emoji/' + scope.row.contemessageCountnt" alt="">
            </span>
            <span v-if="scope.row.type == 3">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                <template #reference>
                  <img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.content" alt="">
                </template>
                <template #default>
                  <img style="width: 400px; height: auto;" :src="scope.row.content" alt="">
                </template>
              </el-popover>
            </span>
            <span v-if="scope.row.type == 4">
              <video style="width: 180px;height: 100px;" :src="scope.row.content" type="audio/wav"
                controls="controls"></video>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="lastMessageTime" label="创建时间" width="180"></el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="neirongSelect()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!--聊天记录 弹框 -->
    <el-dialog v-model="dialogFormVisibleLt" title="聊天记录" width="80%" center>
      <div style="margin: 10px;">
        <div style="display: inline-block;margin: 5px;">
          <span>用户昵称：</span>
          <!-- @keydown.enter.native="handleSearchLt" -->
          <el-input v-model="userNameC" placeholder="请输入用户昵称" style="width: 150px;" />
        </div>
        <div style="display: inline-block;margin: 5px;">
          <span>手机号：</span>
          <el-input v-model="phoneC" placeholder="请输入手机号" style="width: 150px;" />
        </div>
        <div style="display: inline-block;margin: 5px;">
          <span>内容类型：</span>
          <el-select v-model="typeC" class="m-2" placeholder="请选择内容类型" style="width: 150px;" @change="handleSearchLt">
            <el-option v-for="(item, index) in typeData" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </div>
        <div style="display: inline-block;margin: 5px;">
          <span>聊天内容：</span>
          <el-input v-model="contentC" placeholder="请输入聊天内容" style="width: 150px;" />
        </div>

        <el-button size="small" type="primary" plain @click="handleSearchLt()">查询</el-button>
        <el-button size="small" type="primary" plain @click="clearSearchLt()">查询</el-button>
      </div>
      <el-table :data="tableDataLt" v-loading="loadingCy" style="width: 100%">
        <el-table-column prop="userId" label="编号" width="100"></el-table-column>
        <el-table-column prop="userName" label="用户昵称">
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
        <el-table-column prop="type" label="内容类型">
          <template #default="scope">
            <span v-if="scope.row.type == 1">普通消息</span>
            <span v-if="scope.row.type == 2">表情</span>
            <span v-if="scope.row.type == 3">图片</span>
            <span v-if="scope.row.type == 4">视频</span>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="聊天内容">
          <template #default="scope">
            <span v-if="scope.row.type == 1">{{ scope.row.content }}</span>
            <span v-if="scope.row.type == 2">
              <img style="width: 20px; height: 20px;margin: 3px;"
                :src="'https://im.xianmaxiong.com/emoji/' + scope.row.content" alt="">
              <!-- <el-popover placement="top-start" title="" trigger="hover"  popper-style="width:420px;">
                                    <template #reference>
                                        <img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.content" alt="">
                                    </template>
      <template #default style="width: 420px;">
                                        <img style="width: 400px; height: auto;" :src="scope.row.content" alt="">
                                    </template>
      </el-popover> -->
            </span>
            <span v-if="scope.row.type == 3">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                <template #reference>
                  <img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.content" alt="">
                </template>
                <template #default>
                  <img style="width: 400px; height: auto;" :src="scope.row.content" alt="">
                </template>
              </el-popover>
            </span>
            <span v-if="scope.row.type == 4">
              <video style="width: 180px;height: 100px;" :src="scope.row.content" type="audio/wav"
                controls="controls"></video>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
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
import { useRouter } from 'vue-router';
import pagination from '@/components/pagination/pagination.vue'
const {
  proxy
} = getCurrentInstance();
const router = useRouter();
const activeName = ref('first'); //默认选中
const liaotianData = reactive([]); //聊天记录 集合
const huihuaData = reactive([]); //聊天内容 集合



const page = ref(1);//当前页
const limit = ref(10);//每页条数
const total = ref(0);//总条数
const page1 = ref(1);//当前页
const limit1 = ref(10);//每页条数
const total1 = ref(0);//总条数

//列表是否加载中
const tableDataLoading = ref(false);
const tableDataLoading1 = ref(false);
const dialogFormVisible4 = ref(false);

const loadingCy = ref(false);
const receiverUserId = ref('');
const senderUserId = ref('');
const userNameC = ref('');
const phoneC = ref('');
const typeC = ref('');
const contentC = ref('');
const userNameT = ref('');
const contentT = ref('');



//聊天类型 集合
const typeData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '普通消息',
    value: 1
  },
  {
    label: '表情',
    value: 2
  },
  {
    label: '图片',
    value: 3
  },
  {
    label: '视频',
    value: 4
  }
]);



onMounted(() => {
  liaotianSelect();
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

// 切换tab 事件
const handleClick = (tab, event) => {
  console.log(event.srcElement.innerText)


  if (event.srcElement.innerText == '聊天会话') {
    liaotianSelect();
  }
}

// 聊天记录 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.post('/admin/history/chatHistory', {
    userName: userNameT.value,
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
// 刷新
const handleSearch = () => {
  page.value = 1
  liaotianSelect()
}
// 重置
const clearSearch = () => {
  page.value = 1
  userNameT.value = ''
  contentT.value = ''
  liaotianSelect()
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
  huihuaData.splice(0, huihuaData.length)
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
