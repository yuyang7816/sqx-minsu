<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="景点列表" name="first">
        <div style="margin: 0 20px;text-align: right;">
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="routeChangeAdd()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="id" label="编号" width="80">
          </el-table-column>
          <el-table-column prop="name" label="景点名称" width="120">
          </el-table-column>
          <el-table-column prop="img" label="景点图片" width="200">
            <template #default="scope">
              <div v-if="scope.row.img">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                  v-for="(item, index) in scope.row.img.split(',')" :key="index">
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
          <el-table-column prop="openHours" label="开放时间">
          </el-table-column>
          <el-table-column prop="location" label="景点位置" width="180">
          </el-table-column>
          <el-table-column prop="ticketPrice" label="门票价格" width="180">
          </el-table-column>
          <el-table-column prop="intro" label="简介" width="180">
          </el-table-column>
          <el-table-column prop="traffic" label="交通攻略" width="180">
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180">
          </el-table-column>
          <el-table-column label="操作" prop="id" width="160" fixed='right' align="center">
            <template #default="scope">
              <el-button size="small" type="primary" plain @click="scenicSpotBtn(scope.row)">门票管理</el-button>
              <el-button size="small" type="warning" plain @click="routeChangeAdd(scope.row)">修改</el-button>
              <el-button size="small" type="danger" @click="routeDelete(scope.row)" style="margin: 5px;">删除</el-button>
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
    <el-dialog v-model="dialogFormVisibleXlR" :title="titles" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">景点名称 ：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入景点名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="width: 200px;display: inline-block;text-align: right;">景点图片 ：</span>
        <div style="margin-bottom: 10px;display:flex;flex-direction: row;flex-wrap: wrap;">
          <div class="imgs" v-for="(item, index) in img" :key="index">
            <img width="100%" class="images" height="100%" :src="item" alt="">
              <span class="dels">
                <el-icon style="font-size: 28px;color: #D9D9D9;text-align: center;width: 148px;" @click="clear2(index)">
                  <delete style="width: 1em; height: 1em; margin-right: 8px" />
                </el-icon>
              </span>
          </div>
          <div class="imgs"
            style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;color: #efefef;">
            <el-upload action="https://jdmsadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
              :on-success="handleUploadSuccess4" :on-progress="onprogress2">
              <el-progress v-if="percentage2 > 0 && percentage2 < 100" type="circle"
                :percentage="percentage2"></el-progress>
              <el-icon v-else style="font-size: 28px;color: #D9D9D9">
                <Plus />
              </el-icon>
            </el-upload>
          </div>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">开放时间 ：</span>
        <el-input style="width:50%;" v-model="openHours" type="text" placeholder="请输入开放时间"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">景点位置 ：</span>
        <el-input style="width:50%;" v-model="location" type="text" placeholder="请输入景点位置"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">门票价格 ：</span>
        <el-input style="width:50%;" v-model="ticketPrice" type="text" placeholder="请输入门票价格"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">简介 ：</span>
        <el-input style="width:50%;" v-model="intro" type="text" placeholder="请输入简介"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">交通攻略 ：</span>
        <el-input style="width:50%;" v-model="traffic" type="text" placeholder="请输入交通攻略"></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleXlR = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="routeNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 门票列表 -->
    <el-dialog v-model="dialogFormVisibleJd" title="门票列表" width="70%" center>

      <div style="display: inline-block;">
        <el-button style="margin-left:15px;" size="small" class="btn-refresh" @click="selectJd">刷新
        </el-button>
        <el-button size="small" type="warning" plain @click="scenicSpotChangeAdd()">添加</el-button>
      </div>
      <el-table :data="userData" v-loading="tableDataLoadingU" style="width: 100%">
        <el-table-column prop="id" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="name" label="门票名称">
        </el-table-column>
        <el-table-column prop="nameIntro" label="门票介绍">
        </el-table-column>
        <el-table-column prop="startTime" label="起始时间" width="180">
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="180">
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="180">
        </el-table-column>
        <el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
          <template #default="scope">
            <el-button size="small" type="warning" plain @click="scenicSpotChangeAdd(scope.row)">修改</el-button>
            <el-button size="small" type="danger" @click="scenicSpotDelete(scope.row)"
              style="margin: 5px;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1"
              @pageChange="routeSelectJingdian()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 添加、修改 门票弹框 -->
    <el-dialog v-model="dialogFormVisibleSs" :title="titles" width="80%" center>

      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">门票名称 ：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入门票名称"></el-input>
      </div>

      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">门票介绍 ：</span>
        <el-input style="width:50%;" v-model="nameIntro" type="text" placeholder="请输入门票介绍"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">起始时间 ：</span>
        <!-- <el-input style="width:50%;" v-model="startTime" type="text" placeholder="请输入起始时间"></el-input> -->
        <el-date-picker v-model="startTime" type="datetime" format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss" time-format="HH:mm:ss" placeholder="选择起始时间" style="width:50%;" />
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">结束时间 ：</span>
        <!-- <el-input style="width:50%;" v-model="endTime" type="text" placeholder="请输入结束时间"></el-input> -->
        <el-date-picker v-model="endTime" type="datetime" format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss" time-format="HH:mm:ss" placeholder="选择结束时间" style="width:50%;" />
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序 ：</span>
        <el-input style="width:50%;" v-model="sort" type="text" placeholder="请输入排序"></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleSs = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="ssNoticeTo" style="margin: 10px;">确认</el-button>
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
const routeUrl = ref('');
const titles = ref('添加');

const classify = ref('');
const id = ref('');
const baseInfoId = ref('');
const name = ref(''); //景点名称
const img = reactive([]); //景点图片，以逗号分隔
const openHours = ref(''); //开放时间
const location = ref(''); //景点位置
const ticketPrice = ref(''); //门票价格
const intro = ref(''); //简介
const traffic = ref(''); //交通攻略
const dialogFormVisibleXlR = ref(false);
const percentage2 = ref(0);

const dialogFormVisibleJd = ref(false);
const tableDataLoadingU = ref(false);
const userData = reactive([]);
const page1 = ref(1);
const limit1 = ref(10);
const total1 = ref(0);
const scenicSpotId = ref('');

const startTime = ref(''); //起始时间
const endTime = ref(''); //结束时间
const nameIntro = ref(''); //门票介绍
const sort = ref(''); //排序
const dialogFormVisibleSs = ref(false);





onMounted(() => {
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '景点列表') {
    classify.value = 1
    liaotianSelect();
  }
}

//景点数据 列表
const liaotianSelect = () => {
  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.post('/admin/scenicSpot/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    location: '',
    name: '',
    // }
  }, {
    type: 'json'
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


// 添加/修改 景点 弹框
const routeChangeAdd = (row) => {
  if (row) {
    titles.value = '修改'

    id.value = row.id
    name.value = row.name
    openHours.value = row.openHours
    location.value = row.location
    ticketPrice.value = row.ticketPrice
    intro.value = row.intro
    traffic.value = row.traffic
    if (row.img) {
      img.splice(0, img.length, ...row.img.split(','))
    } else {
      img.value = []
    }
  } else {
    titles.value = '添加'
    id.value = ''
    name.value = ''
    openHours.value = ''
    location.value = ''
    ticketPrice.value = ''
    intro.value = ''
    traffic.value = ''
    img.value = []
  }
  percentage2.value = 0
  dialogFormVisibleXlR.value = true
}

const routeNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/scenicSpot/save'
  } else {
    routeUrl.value = '/admin/scenicSpot/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    name: name.value,
    traffic: traffic.value,
    intro: intro.value,
    ticketPrice: ticketPrice.value,
    location: location.value,
    openHours: openHours.value,
    img: img.toString(),
    baseInfoId: baseInfoId.value,

  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleXlR.value = false
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
const routeDelete = (data) => {
  proxy.$Request.post('/admin/scenicSpot/delete', {
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
const clear2 = (index) => {
  img.splice(index, 1);
}
const handleUploadSuccess4 = (file) => {
  img.push(file.data)
}
const onprogress2 = (event,) => {
  console.log('event,', parseInt(event.percent))
  percentage2.value = parseInt(event.percent)
}

// 门票管理 弹框
const scenicSpotBtn = (row) => {
  scenicSpotId.value = row.id
  page1.value = 1
  dialogFormVisibleJd.value = true
  routeSelectJingdian();
}
// 获取 详情 数据
const routeSelectJingdian = () => {

  tableDataLoadingU.value = true
  userData.splice(0, userData.length)
  proxy.$Request.post('/admin/scenicSpotTicket/page', {
    // params: {
    page: page1.value,
    limit: limit1.value,
    scenicSpotId: scenicSpotId.value,
    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoadingU.value = false;
    userData.splice(0, userData.length, ...res.data.records)
    total1.value = res.data.total;
  })
}
// 查询
const selectJd = () => {
  page1.value = 1
  routeSelectJingdian()
}
// 添加/修改 景点 弹框
const scenicSpotChangeAdd = (row) => {
  if (row) {
    titles.value = '修改'

    id.value = row.id
    name.value = row.name
    startTime.value = row.startTime
    endTime.value = row.endTime
    nameIntro.value = row.nameIntro
    sort.value = row.sort
  } else {
    titles.value = '添加'
    id.value = ''
    name.value = ''
    startTime.value = ''
    endTime.value = ''
    nameIntro.value = ''
    sort.value = ''
  }
  percentage2.value = 0
  dialogFormVisibleSs.value = true
}

const ssNoticeTo = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/scenicSpotTicket/save'
  } else {
    routeUrl.value = '/admin/scenicSpotTicket/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    name: name.value,
    startTime: startTime.value,
    endTime: endTime.value,
    nameIntro: nameIntro.value,
    sort: sort.value,
    scenicSpotId: scenicSpotId.value,

  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleSs.value = false
          routeSelectJingdian()
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
const scenicSpotDelete = (data) => {
  proxy.$Request.post('/admin/scenicSpotTicket/delete', {
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
          routeSelectJingdian()
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
</style>
