<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="用户反馈" name="first">
        <div style="margin: 5px;text-align: right;">
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="id" label="编号" width="100" fixed="left"></el-table-column>
          <!-- <el-table-column prop="userName" label="用户昵称" width="150" fixed="left">
            <template #default="scope">
              <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{ scope.row.userName ?
                scope.row.userName : '未绑定' }}</span>
            </template>
</el-table-column> -->
          <el-table-column prop="contact" label="联系方式">
          </el-table-column>
          <el-table-column prop="opinion" label="内容">
          </el-table-column>
          <!-- <el-table-column prop="image" label="图片" width="300">
            <template #default="scope">
              <div v-if="scope.row.image == null || scope.row.image == ''">
                暂无图片
              </div>
              <div v-else-if="scope.row.image" style="display:flex;flex-wrap: wrap;">
                <div v-for="(item, index) in scope.row.image.split(',')" :key="index" style="margin: 2px;">
                  <el-popover placement="top-start" title="" trigger="hover"
                    popper-style="width:420px;max-height:500px;overflow: auto;">
                    <template #reference>
                      <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="">
                    </template>
                    <template #default>
                      <img style="width: 400px; height: auto;" :src="item" alt="">
                    </template>
                  </el-popover>
                </div>
              </div>
            </template>
          </el-table-column> -->
          <!-- <el-table-column prop="contacts" label="意见" width="200">
          </el-table-column> -->
          <el-table-column prop="createTime" label="创建时间" width="160">
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
    <!-- 审核 弹框 -->
    <el-dialog v-model="dialogFormVisibleU" title="审核" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
        <el-radio-group v-model="state" class="ml-4">

          <el-radio :label="2">封禁账号</el-radio>
          <el-radio :label="3">驳回</el-radio>
          <el-radio :label="4">4通过但不做处理</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">审核说明：</span>
        <el-input style="width:50%;" v-model="description" type="textarea" rows="4" placeholder="请输入审核说明"></el-input>
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
import { useRouter } from 'vue-router';
import pagination from '@/components/pagination/pagination.vue'
import {
  useUserStore
} from '@/stores/user'
import {
  ElMessage
} from 'element-plus';
const {
  proxy
} = getCurrentInstance();
const router = useRouter();
const activeName = ref('first'); //默认选中
const liaotianData = reactive([]); //聊天记录 集合
const userStore = useUserStore() //获取 管理员信息
const page = ref(1);//当前页
const limit = ref(10);//每页条数
const total = ref(0);//总条数

//列表是否加载中
const tableDataLoading = ref(false);
const classify = ref(1); //type 1系统公告 2评论消息
const dialogFormVisibleU = ref(false);
const id = ref('');
const targetId = ref('');
const description = ref('');
const state = ref('');

onMounted(() => {
  liaotianSelect();
});
//跳转用户详情
const updates = (userId) => {
  router.push({
    name: 'userDetail',
    query: {
      userId: userId,
    },

  });
};

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '用户反馈') {
    classify.value = 1
    liaotianSelect();
  }

}

// 聊天记录 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.post('/admin/feedback/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    userId: '',

    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    if (classify.value == 1) {
      for (var i in res.data.records) {
        var a = res.data.records[i].opinion.toString()
        var b = JSON.parse(a)
        res.data.records[i].opinion = b.content
        console.log('key', b)
      }
    }
    liaotianData.splice(0, liaotianData.length, ...res.data.records)
    total.value = res.data.total;
  })


}
// 刷新
const handleSearch = () => {
  page.value = 1
  liaotianSelect()
}

// 审核弹框
const addChangeU = (row) => {
  id.value = row.id
  targetId.value = row.targetId
  description.value = ''
  state.value = ''
  dialogFormVisibleU.value = true
}
const StairNoticeToU = () => {

  proxy.$Request.post('/admin/complaint/updateComplaint', {
    id: id.value,
    targetId: targetId.value,
    feedback: description.value,
    status: status.value,
    handlerId: userStore.useTransitionFallthrough.userId,
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

span {
  font-size: 14px;
  color: #303133;
}
</style>
