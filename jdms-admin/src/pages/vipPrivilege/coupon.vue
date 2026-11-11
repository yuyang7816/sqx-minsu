<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="配置列表" name="first">
        <div class="page-toolbar">
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column fixed prop="vipId" label="编号" width="100"></el-table-column>
          <el-table-column prop="vipTitle" label="会员名称"></el-table-column>
          <el-table-column prop="monthCount" label="月数"></el-table-column>
          <!-- <el-table-column prop="memberImg" label="图片">
            <template #default="scope">
              <img :src="scope.row.memberImg" width="60" height="60" />
            </template>
</el-table-column> -->
          <el-table-column prop="vipMoney" label="会员价格"></el-table-column>
          <!-- <el-table-column prop="originalAmount" label="原价"></el-table-column> -->
          <el-table-column prop="sort" label="排序"></el-table-column>
          <el-table-column prop="isEnable" label="是否启用" width="100">
            <template #default="scope">
              <el-switch v-model="scope.row.isEnable" @change="changeEnable(scope.row)" :active-value="1"
                :inactive-value="0" active-color="#13ce66" inactive-color="#ff4949">
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
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">会员类型：</span>
        <el-radio-group v-model="type" class="ml-4">
          <el-radio :label="1">月会员</el-radio>
          <el-radio :label="2">季会员</el-radio>
          <el-radio :label="3">年会员</el-radio>
        </el-radio-group>
      </div> -->
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">会员名称：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入会员名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">月数：</span>
        <el-input style="width:50%;" v-model="type" type="text" placeholder="请输入月数"></el-input>
      </div>
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">图片 ：</span>
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
      </div> -->
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">会员价格：</span>
        <el-input style="width:50%;" v-model="price" type="number" :min="0" placeholder="请输入会员价格"></el-input>
      </div>
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">原价：</span>
        <el-input style="width:50%;" v-model="rate" type="number" :min="0" placeholder="请输入原价"></el-input>
      </div> -->
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
        <el-input style="width:50%;" v-model="sort" type="number" :min="0" placeholder="请输入排序"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">是否启用：</span>
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

const classify = ref('');
const id = ref('');
const name = ref('');
const type = ref(1);
const price = ref('');
const rate = ref('');
const sort = ref('');
const isEnable = ref(1);
const imageUrl = ref('');



onMounted(() => {
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '配置列表') {
    classify.value = 1
    liaotianSelect();
  }
}

//分类数据 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.get('/admin/vipConfig/getVipConfigList', {
    params: {
      page: page.value,
      limit: limit.value,
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
    id.value = row.vipId
    name.value = row.vipTitle
    type.value = row.monthCount
    price.value = row.vipMoney
    rate.value = row.originalAmount
    sort.value = row.sort
    isEnable.value = row.isEnable ?? 1

  } else {
    titles.value = '添加'
    id.value = ''
    name.value = ''
    type.value = 1
    price.value = ''
    rate.value = ''
    sort.value = ''
    isEnable.value = 1

  }
  dialogFormVisibleU.value = true
}
const StairNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/vipConfig/saveVipConfig'
  } else {
    routeUrl.value = '/admin/vipConfig/saveVipConfig'
  }
  proxy.$Request.post(routeUrl.value, {
    vipId: id.value,
    vipTitle: name.value,
    monthCount: type.value,
    vipMoney: price.value,
    // originalAmount: rate.value,
    sort: sort.value,
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
/** 修改启用状态 */
const changeEnable = (data) => {
  proxy.$Request.post('/admin/vipConfig/saveVipConfig', {
    vipId: data.vipId,
    isEnable: data.isEnable,
  }, {
    type: 'json'
  }).then((res) => {
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
      })
      liaotianSelect()
    }
  })
}

/** 删除 */
const handleDelete = (data) => {
  proxy.$Request.get('/admin/vipConfig/deleteVipConfig', {
    params: {
      vipId: data.vipId,
    }
  }, {
    // type: 'json'
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
