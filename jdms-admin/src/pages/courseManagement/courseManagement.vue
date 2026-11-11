<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="服务列表" name="first">
        <div style="margin: 0 20px;">
          <div style="display: inline-block;margin: 5px;">
            <span>店铺名称 ：</span>
            <el-input v-model="shopNameT" placeholder="请输入店铺名称" style="width: 150px;"
              @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>类型 ：</span>
            <el-input v-model="serviceTypeNameT" placeholder="请输入类型" style="width: 150px;"
              @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>服务项目名称 ：</span>
            <el-input v-model="nameT" placeholder="请输入服务项目名称" style="width: 150px;"
              @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <el-button size="small" type="primary" plain @click="handleSearch()">查询</el-button>
          <el-button size="small" type="primary" @click="clearSearch()">重置</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column fixed="left" prop="id" label="编号" width="80"></el-table-column>
          <el-table-column prop="shopName" label="店铺名称" width="160"></el-table-column>
          <el-table-column prop="serviceTypeName" label="类型"></el-table-column>
          <el-table-column prop="name" label="服务项目名称" width="200"></el-table-column>
          <el-table-column prop="img" label="图片" width="100">
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
          <el-table-column prop="slideImg" label="轮播图" width="250">
            <template #default="scope">
              <div v-if="scope.row.slideImg">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                  v-for="(item, index) in scope.row.slideImg.split(',')" :key="index">
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
          <el-table-column prop="itemDuration" label="服务项目时长（分钟）" width="100"></el-table-column>
          <el-table-column prop="description" label="服务项目描述" width="250">
            <template #default="scope">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                <div style="width: 400px;">{{ scope.row.description }}</div>
                <template #reference>
                  <div
                    style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
                    {{ scope.row.description }}</div>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="price" label="原价"></el-table-column>
          <el-table-column prop="vipPrice" label="会员价格"></el-table-column>
          <!-- <el-table-column prop="courseStatus" label="状态">
            <template #default="scope">
              <span v-if="scope.row.courseStatus === 0">正常</span>
              <span v-if="scope.row.courseStatus == 1" style="color: #f56c6c;">下架</span>
              <el-switch v-model="scope.row.courseStatus" @click="changeState(scope.row)"
                :disabled="!isAuth('bannerList:update')" :active-value="0" :inactive-value="1" active-color="#13ce66"
                inactive-color="#ff4949">
              </el-switch>
            </template>
          </el-table-column> -->
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
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
        <span style="width: 200px;display: inline-block;text-align: right;">店铺名称 ：</span>
        <el-select v-model="shopId" placeholder="请选择店铺名称" style="width: 50%;">
          <el-option v-for="(item, index) in shopList" :key="index" :label="item.name" :value="item.shopId" />
        </el-select>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">类型 ：</span>
        <el-select v-model="serviceTypeId" placeholder="请选择类型" style="width: 50%;">
          <el-option v-for="(item, index) in typeList" :key="index" :label="item.name" :value="item.id" />
        </el-select>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">服务项目名称 ：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入服务项目名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">服务项目描述 ：</span>
        <el-input style="width:50%;" v-model="description" type="textarea" rows="4" placeholder="请输入服务项目描述"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">原价 ：</span>
        <el-input style="width:50%;" v-model="price" type="text" placeholder="请输入原价"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">会员价格 ：</span>
        <el-input style="width:50%;" v-model="vipPrice" type="text" placeholder="请输入会员价格"></el-input>
      </div>
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="width: 200px;display: inline-block;text-align: right;">图片 ：</span>
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
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="width: 200px;display: inline-block;text-align: right;">轮播图 ：</span>
        <div style="margin-bottom: 10px;display:flex;flex-direction: row;flex-wrap: wrap;">
          <div class="imgs" v-for="(item, index) in slideImg" :key="index">
            <img width="100%" class="images" height="100%" :src="item" alt="">
              <span class="dels">
                <el-icon style="font-size: 28px;color: #D9D9D9;text-align: center;width: 148px;" @click="clear3(index)">
                  <delete style="width: 1em; height: 1em; margin-right: 8px" />
                </el-icon>
              </span>
          </div>
          <div class="imgs"
            style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;color: #efefef;">
            <el-upload action="https://jdmsadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
              :on-success="handleUploadSuccess3" :on-progress="onprogress3">
              <el-progress v-if="percentage3 > 0 && percentage3 < 100" type="circle"
                :percentage="percentage3"></el-progress>
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
const shopNameT = ref('');
const serviceTypeNameT = ref('');
const nameT = ref('');

const classify = ref('');
const id = ref('');

const shopId = ref('');//店铺ID
const serviceTypeId = ref('');//课程类型ID
const name = ref('');//服务项目名称
const description = ref('');//服务项目描述
const price = ref('');//原价
const vipPrice = ref('');//会员价格
const img = reactive([]);//封面
const percentage2 = ref(0);
const percentage3 = ref(0);
const courseStatus = ref('');//课程状态：0正常 1下架
const remark = ref('');//课程备注
const backgroundImage = ref('');//背景图片
const labelIds = ref([]);//标签id集合
const slideImg = reactive([]);//标签集合
const typeList = reactive([]);//课程类型集合
const shopList = reactive([]);// 商户集合




onMounted(() => {
  liaotianSelect();
});


// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '服务列表') {
    classify.value = ''
    liaotianSelect();
  }
}

//数据 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.post('/admin/serviceItem/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    shopId: '',
    serviceTypeId: '',
    shopName: shopNameT.value,
    serviceTypeName: serviceTypeNameT.value,
    serviceName: nameT.value,
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
const clearSearch = () => {
  page.value = 1
  shopNameT.value = ''
  serviceTypeNameT.value = ''
  nameT.value = ''
  liaotianSelect()
}

// 添加/修改 分类 弹框
const addChangeU = (row) => {
  if (row) {
    titles.value = '修改'
    id.value = row.id
    shopId.value = row.shopId
    serviceTypeId.value = row.serviceTypeId
    name.value = row.name
    description.value = row.description
    price.value = row.price
    vipPrice.value = row.vipPrice
    // img.value = row.img
    img.splice(0, img.length)
    slideImg.splice(0, slideImg.length)
    if (row.img) {
      let imgs = row.img.split(',')

      // img.value = img
      img.splice(0, img.length, ...imgs)
    }
    if (row.slideImg) {
      let slideImgs = row.slideImg.split(',')

      // slideImg.value = slideImgs
      slideImg.splice(0, slideImg.length, ...slideImgs)
      console.log(slideImg.value, '---');
    }



  } else {
    titles.value = '添加'
    id.value = ''

    shopId.value = ''
    serviceTypeId.value = ''
    name.value = ''
    description.value = ''
    price.value = ''
    vipPrice.value = ''
    // img.value = ''
    img.splice(0, img.length)
    slideImg.splice(0, slideImg.length)
    // labelIds.value = [] //标签id集合
  }
  biaoqianSelect()
  dialogFormVisibleU.value = true
}
const StairNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/serviceItem/save'
  } else {
    routeUrl.value = '/admin/serviceItem/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    shopId: shopId.value,
    serviceTypeId: serviceTypeId.value,
    name: name.value,
    description: description.value,
    price: price.value,
    vipPrice: vipPrice.value,
    img: img.toString(),
    slideImg: slideImg.toString(),
    // labelIds: labelIds.value.toString(),
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
  proxy.$Request.post('/admin/serviceItem/delete', {
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

/** 修改状态*/
const changeState = (data) => {
  proxy.$Request.post('/banner/updateBannerStateById', {
    id: data.id,
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
          liaotianSelect()
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
const clear3 = (index) => {
  slideImg.splice(index, 1);
}
const handleUploadSuccess3 = (file) => {
  slideImg.push(file.data)
}
const onprogress3 = (event,) => {
  console.log('event,', parseInt(event.percent))
  percentage3.value = parseInt(event.percent)
}
const onprogress2 = (event,) => {
  console.log('event,', parseInt(event.percent))
  percentage2.value = parseInt(event.percent)
}
//类型 标签 数据 列表
const biaoqianSelect = () => {
  // 类型
  typeList.splice(0, typeList.length)
  proxy.$Request.post('/admin/serviceType/page', {
    // params: {
    page: 1,
    limit: 50,
    // meditationName: '',
    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    typeList.splice(0, typeList.length, ...res.data.records)
  })

  // 标签
  // labelList.splice(0, labelList.length)
  // proxy.$Request.get('/courseLabel/selectByCourseLabelList', {
  //   params: {
  //     pageNum: 1,
  //     pageSize: 50,
  //     meditationName: '',
  //   }
  // }, {
  //   // type: 'json'
  // }).then((res) => {
  //   console.log(res)
  //   labelList.splice(0, labelList.length, ...res.data.list)
  // })

  // 商户
  shopList.splice(0, shopList.length)
  proxy.$Request.post('/admin/shopInfo/page', {
    // params: {
    page: 1,
    limit: 50,
    // meditationName: '',
    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    shopList.splice(0, shopList.length, ...res.data.records)
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

span {
  font-size: 14px;
  color: #303133;
}
</style>
