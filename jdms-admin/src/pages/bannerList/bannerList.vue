<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="首页轮播图" name="first">
        <div class="page-toolbar">
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column fixed prop="id" label="编号" width="100"></el-table-column>
          <el-table-column prop="imageUrl" label="图片">
            <template #default="scope">
              <img :src="scope.row.imageUrl" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="name" label="轮播图名称"></el-table-column>
          <el-table-column prop="state" label="状态">
            <template #default="scope">
              <span v-if="scope.row.state == 1">显示</span>
              <span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
            </template>
          </el-table-column>
          <el-table-column prop="state" label="是否启用">
            <template #default="scope">
              <el-switch v-model="scope.row.state" @change="changeState(scope.row)"
                :disabled="!isAuth('bannerList:update')" :active-value="1" :inactive-value="2" active-color="#13ce66"
                inactive-color="#ff4949">
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
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
      <el-tab-pane label="首页分类" name="second">
        <div class="page-toolbar">
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column fixed prop="id" label="编号" width="100"></el-table-column>
          <el-table-column prop="imageUrl" label="图片">
            <template #default="scope">
              <img :src="scope.row.imageUrl" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="name" label="轮播图名称"></el-table-column>
          <el-table-column prop="state" label="状态">
            <template #default="scope">
              <span v-if="scope.row.state == 1">显示</span>
              <span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
            </template>
          </el-table-column>
          <el-table-column prop="state" label="是否启用">
            <template #default="scope">
              <el-switch v-model="scope.row.state" @change="changeState(scope.row)"
                :disabled="!isAuth('bannerList:update')" :active-value="1" :inactive-value="2" active-color="#13ce66"
                inactive-color="#ff4949">
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
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
        <span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
        <div style="display: inline-block;text-align: center;line-height: 148px;">
          <el-upload class="avatar-uploader" v-model="imageUrl"
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
        <span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
        <el-input style="width:50%;" v-model="url" type="text" placeholder="请输入链接"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
        <el-input style="width:50%;" v-model="sort" type="number" :min="0" placeholder="数字越大越靠前"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
        <el-input style="width:50%;" v-model="describes" type="text" placeholder="请输入备注"></el-input>
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
import pagination from '@/components/pagination/pagination.vue'
import {
  ElMessage
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

const classify = ref(1);
const id = ref('');
const name = ref('');
const imageUrl = ref('');
const url = ref('');
const describes = ref('');
const sort = ref(1);


onMounted(() => {
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '首页轮播图') {
    classify.value = 1
    liaotianSelect();
  }
  if (event.srcElement.innerText == '首页分类') {
    classify.value = 2
    liaotianSelect();
  }

}

//数据 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.get('/banner/selectBannerPage', {
    params: {
      page: page.value,
      limit: limit.value,
      classify: classify.value,
      state: '',
    }
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    liaotianData.splice(0, liaotianData.length, ...(res.data.list || res.data.records || []))
    total.value = res.data.totalCount || res.data.total || 0;
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
    id.value = row.id
    name.value = row.name
    classify.value = row.classify
    imageUrl.value = row.imageUrl
    url.value = row.url
    describes.value = row.describess || row.describes || ''
    sort.value = row.sort || 1
  } else {
    titles.value = '添加'
    id.value = ''
    name.value = ''
    imageUrl.value = ''
    url.value = ''
    describes.value = ''
    sort.value = 1
  }
  dialogFormVisibleU.value = true
}
const StairNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/banner/insertBanner'
  } else {
    routeUrl.value = '/banner/updateBannerById'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    name: name.value,
    classify: classify.value,
    imageUrl: imageUrl.value,
    url: url.value,
    describess: describes.value,
    sort: sort.value,
    state: 1,
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
  proxy.$Request.get('/banner/deleteBannerById', {
    params: {
      ids: data.id,
    }
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

/** 修改状态*/
const changeState = (data) => {
  proxy.$Request.post('/banner/updateBannerById', {
    id: data.id,
    state: data.state,
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
          liaotianSelect()
        }
      })
    }
  })
}
function handleAvatarSuccess1(file) {
  imageUrl.value = file.data
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
