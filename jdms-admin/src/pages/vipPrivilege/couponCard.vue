<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="房间配置" name="first">
        <div style="margin: 0 20px;text-align: right;">
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column fixed prop="classifyId" label="编号" width="100"></el-table-column>
          <el-table-column prop="title" label="名称"></el-table-column>
          <el-table-column prop="img" label="封面图">
            <template #default="scope">
              <img :src="scope.row.img" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <!-- <el-button size="small" type="primary" @click="guigeChange(scope.row)" plain
                style="margin: 5px;">规格管理</el-button> -->
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
      <el-tab-pane label="房间类型配置" name="second">
        <div style="margin: 0 20px;text-align: right;">
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeGg()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="id" label="编号" width="100"></el-table-column>
          <el-table-column prop="typeName" label="名称"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <!-- <el-button size="small" type="primary" @click="guigeChange(scope.row)" plain
                style="margin: 5px;">规格管理</el-button> -->
              <el-button size="small" type="primary" @click="addChangeGg(scope.row)" style="margin: 5px;">修改</el-button>
              <el-button size="small" type="danger" @click="handleDeleteGg(scope.row)"
                style="margin: 5px;">删除</el-button>
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
        <span style="width: 200px;display: inline-block;text-align: right;">名称 ：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">封面图 ：</span>
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
        <span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
        <el-input style="width:50%;" v-model="sort" type="number" :min="0" placeholder="请输入排序"></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 规格列表 弹框 -->
    <el-dialog v-model="dialogFormVisibleGg" title="规格列表" width="80%" center>
      <div style="margin: 0 20px;text-align: right;">
        <el-button size="small" class="btn-refresh" @click="handleSearchGg()">刷新</el-button>
        <el-button size="small" type="warning" plain @click="addChangeGg()">添加</el-button>
      </div>
      <el-table v-loading="tableDataLoading1" :data="guigeData">
        <el-table-column fixed prop="id" label="编号" width="100"></el-table-column>
        <el-table-column prop="price" label="价格"></el-table-column>
        <el-table-column prop="faceValue" label="面值"></el-table-column>
        <!-- <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column> -->
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="addChangeGg(scope.row)" style="margin: 5px;">修改</el-button>
            <el-button size="small" type="danger" @click="handleDeleteGg(scope.row)" style="margin: 5px;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit" v-model:currentPage="page1" :total="total1" @pageChange="guigeSelect()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 添加/修改 类型 弹框 -->
    <el-dialog v-model="dialogFormVisibleGa" :title="titles" width="70%" center>

      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
        <el-input style="width:50%;" v-model="price" placeholder="请输入名称"></el-input>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleGa = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToGg" style="margin: 10px;">确认</el-button>
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
const sort = ref(1);
const price = ref('');
const rate = ref('');
const imageUrl = ref('');
const cardId = ref('');
const dialogFormVisibleGg = ref(false);
const tableDataLoading1 = ref(false);
const dialogFormVisibleGa = ref(false);
const guigeData = reactive([]); //数据 集合
const page1 = ref(1);//当前页
const limit1 = ref(10);//每页条数
const total1 = ref(0);//总条数



onMounted(() => {
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '房间配置') {
    classify.value = 1
    liaotianSelect();
  }
  if (event.srcElement.innerText == '房间类型配置') {
    classify.value = 2
    liaotianSelect();
  }

}

//数据 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  if (classify.value == 1) {
    proxy.$Request.get('/admin/roomDeviceClassify/getClassifyList', {
      params: {
        page: page.value,
        limit: limit.value,
        // name: '',
      }
    }, {
      // type: 'json'
    }).then((res) => {
      console.log(res)
      tableDataLoading.value = false;
      liaotianData.splice(0, liaotianData.length, ...res.data.records)
      total.value = res.data.total;
    })
  } else {
    proxy.$Request.get('/admin/roomType/getRoomTypeList', {
      params: {
        // name: '',
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



}
// 刷新
const handleSearch = () => {
  page.value = 1
  liaotianSelect()
}
// 添加/修改 分类 弹框
const addChangeU = (row) => {
  if (row) {

    titles.value = '修改'
    id.value = row.classifyId
    name.value = row.title
    imageUrl.value = row.img
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
  if (titles.value == '添加') {
    routeUrl.value = '/admin/roomDeviceClassify/getClassifyList'
  } else {
    routeUrl.value = '/admin/roomDeviceClassify/getClassifyList'
  }
  proxy.$Request.post(routeUrl.value, {
    classifyId: id.value,
    title: name.value,
    img: imageUrl.value,
    sort: sort.value,
  }, {
    type: 'form-data'
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
  proxy.$Request.post('/admin/roomDeviceClassify/deleteClassify', {
    classifyIds: data.classifyId,
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
// 规格 弹框
const guigeChange = (row) => {
  cardId.value = row.id
  dialogFormVisibleGg.value = true
  guigeSelect()
}
//规格 列表
const guigeSelect = () => {

  tableDataLoading1.value = true
  guigeData.splice(0, guigeData.length)
  proxy.$Request.post('/admin/cardValue/page', {
    // params: {
    page: page1.value,
    limit: limit1.value,
    cardId: cardId.value,
    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading1.value = false;
    guigeData.splice(0, guigeData.length, ...res.data.records)
    total1.value = res.data.total;
  })


}
// 刷新
const handleSearchGg = () => {
  page.value = 1
  guigeSelect()
}
// 添加/修改 规格 弹框
const addChangeGg = (row) => {
  if (row) {

    titles.value = '修改'
    id.value = row.id
    price.value = row.typeName


  } else {
    titles.value = '添加'
    id.value = ''
    price.value = ''

  }
  dialogFormVisibleGa.value = true
}
const StairNoticeToGg = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/roomType/addRoomType'
  } else {
    routeUrl.value = '/admin/roomType/updateRoomType'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    typeName: price.value,
  }, {
    type: 'form-data'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleGa.value = false
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
const handleDeleteGg = (data) => {
  proxy.$Request.post('/admin/roomType/deleteRoomType', {
    roomTypeIds: data.id,
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
