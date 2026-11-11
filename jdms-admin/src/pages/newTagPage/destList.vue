<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="目的地列表" name="first">
        <div style="text-align:left;margin: 5px;">
          <div style="display: inline-block;margin: 5px;">
            <span>是否热门：</span>
            <el-select v-model="popularT" class="m-2" placeholder="请选择是否热门" style="width: 150px;"
              @change="handleSearch">
              <el-option v-for="(item, index) in statesnum" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div>
          <div style="display: inline-block;margin: 5px;">
            <span>是否精选：</span>
            <el-select v-model="choicenessT" class="m-2" placeholder="请选择是否精选" style="width: 150px;"
              @change="handleSearch">
              <el-option v-for="(item, index) in statesnum" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div>
          <div style="display: inline-block;margin: 5px;">
            <span>城市：</span>
            <el-input v-model="cityT" placeholder="请输入城市" style="width: 150px;" />
          </div>
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="handleClear()">重置</el-button>
          <el-button size="small" type="primary" @click="addChangeU()"
            :disabled="!isAuth('destination:add')">新增</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="id" label="编号" width="80">
          </el-table-column>
          <el-table-column prop="city" label="城市">
          </el-table-column>
          <el-table-column prop="cover" label="封面">
            <template #default="scope">
              <img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.cover" alt="">
            </template>
          </el-table-column>
          <el-table-column prop="album" label="相册" width="300">
            <template #default="scope">
              <div v-if="scope.row.album == null || scope.row.album == ''">
                暂无图片
              </div>
              <div v-if="scope.row.album" style="display:flex;flex-wrap: wrap;">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                  v-for="(item, index) in scope.row.album.split(',')" :key="index">
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
          <el-table-column prop="description" label="描述" width="300">
          </el-table-column>
          <el-table-column prop="popular" label="是否热门">
            <template #default="scope">
              <span v-if="scope.row.popular == 1">是</span>
              <span v-if="scope.row.popular == 2">否</span>
            </template>
          </el-table-column>
          <el-table-column prop="choiceness" label="是否精选">
            <template #default="scope">
              <span v-if="scope.row.choiceness == 1">是</span>
              <span v-if="scope.row.choiceness == 2">否</span>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="overseas" label="是否境外">
            <template #default="scope">
              <span v-if="scope.row.overseas == 1">是</span>
              <span v-if="scope.row.overseas == 2">否</span>
            </template>
          </el-table-column> -->
          <el-table-column prop="sort" label="排序">
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
          </el-table-column>
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
        <span style="width: 200px;display: inline-block;text-align: right;">城市 ：</span>
        <el-input style="width:50%;" v-model="city" type="text" placeholder="请输入城市"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">描述 ：</span>
        <el-input style="width:50%;" v-model="description" type="text" placeholder="请输入描述"></el-input>
      </div>

      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">是否热门 ：</span>
        <el-radio-group v-model="popular" class="ml-4">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="2">否</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">是否精选 ：</span>
        <el-radio-group v-model="choiceness" class="ml-4">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="2">否</el-radio>
        </el-radio-group>
      </div>


      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序 ：</span>
        <el-input style="width:50%;" v-model="sort" type="text" placeholder="请输入排序"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">图片 ：</span>
        <div style="display: inline-block;">
          <el-upload class="avatar-uploader" v-model="cover"
            style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;color: #efefef;"
            action="https://jdmsadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess1">
            <img v-if="cover" :src="cover" class="avatar" style="width: 148px;height: 148px;" />
            <SvgIcon v-else style="width:23px;height:23px;color: #efefef;" name="svg:plus.svg">
            </SvgIcon>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="width: 200px;display: inline-block;text-align: right;">相册 ：</span>
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
  if (event.srcElement.innerText == '目的地列表') {
    classify.value = 1
    liaotianSelect();
  }
}

//分类数据 列表
const liaotianSelect = () => {
  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.post('/admin/destination/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    popular: popularT.value,
    choiceness: choicenessT.value,
    city: cityT.value,
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
const handleClear = () => {
  page.value = 1
  popularT.value = ''
  cityT.value = ''
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
  cover.value = file.data
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
