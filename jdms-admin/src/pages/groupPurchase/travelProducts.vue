<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="开票列表" name="first">
        <div style="margin: 0 20px;">
          <div style="display: inline-block;margin: 5px;">
            <span>状态：</span>
            <el-select v-model="statusT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="handleSearch">
              <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div>
          <!-- <div style="display: inline-block;margin: 5px;">
            <span>类型：</span>
            <el-select v-model="typeT" class="m-2" placeholder="请选择类型" style="width: 150px;" @change="handleSearch">
              <el-option v-for="(item, index) in typeData" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div> -->
          <div style="display: inline-block;margin: 5px;">
            <span>用户昵称：</span>
            <el-input v-model="userNameT" placeholder="请输入用户昵称" style="width: 150px;" />
          </div>
          <div style="display: inline-block;margin: 5px;">
            <span>标题：</span>
            <el-input v-model="titleT" placeholder="请输入标题" style="width: 150px;" />
          </div>
          <el-button size="small" type="primary" plain @click="handleSearch()">查询</el-button>
          <el-button size="small" type="warning" plain @click="handleClear()">重置</el-button>
          <!-- <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button> -->
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="id" label="编号" width="80" fixed="left">
          </el-table-column>
          <!-- <el-table-column prop="type" label="类型">
            <template #default="scope">
              <span v-if="scope.row.type == 1">个人</span>
              <span v-if="scope.row.type == 2">公司</span>
            </template>
</el-table-column> -->

          <el-table-column prop="amount" label="开票金额">
          </el-table-column>
          <el-table-column prop="indentId" label="开票用户">
            <template #default="scope">
              <div style="color: #4f9dec;cursor: pointer;" v-if="scope.row.userName" @click="updates(scope.row.userId)">
                {{ scope.row.userName }}
              </div>
              <div v-else>-</div>
            </template>
          </el-table-column>
          <el-table-column prop="creditCode" label="纳税人识别号">
          </el-table-column>
          <el-table-column prop="address" label="地址">
          </el-table-column>
          <el-table-column prop="phone" label="手机号">
          </el-table-column>
          <el-table-column prop="openBank" label="开户行">
          </el-table-column>
          <el-table-column prop="bankNumber" label="银行账号">
          </el-table-column>
          <el-table-column prop="receiveEmail" label="接收邮箱">
          </el-table-column>
          <el-table-column prop="receivePhone" label="接收手机号">
          </el-table-column>
          <!-- <el-table-column prop="content" label="内容" width="200">
            <template #default="scope">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                <template #default>
                  <div style="width: 400px;">{{ scope.row.content }}</div>
                </template>
                <template #reference>
                  <div
                    style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
                    {{ scope.row.content }}</div>
                </template>
              </el-popover>
            </template>
          </el-table-column> -->
          <!-- <el-table-column prop="amount" label="金额">
          </el-table-column> -->
          <el-table-column prop="status" label="状态" fixed="right">
            <template #default="scope">
              <span v-if="scope.row.status == 1">待开票</span>
              <span v-if="scope.row.status == 2">已开票</span>
              <el-switch v-model="scope.row.status" class="ml-2" active-color="#13ce66" inactive-color="#ff4949"
                :active-value="1" :inactive-value="2" @click="beforeChange(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="170">
          </el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
              <!-- <el-button size="small" type="primary" @click="routeChangeU(scope.row)"
                style="margin: 5px;">线路详情</el-button> -->
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
        <span style="width: 200px;display: inline-block;text-align: right;">封面图 ：</span>
        <div style="display: inline-block;">
          <el-upload class="avatar-uploader" v-model="imgUrl"
            style="width: 148px;height: 148px;line-height: 148px;text-align: center;"
            action="https://yytcadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess1">
            <img v-if="imgUrl" :src="imgUrl" class="avatar" style="width: 148px;height: 148px;" />
            <el-icon v-else style="font-size: 28px;color: #D9D9D9">
              <Plus />
            </el-icon>
          </el-upload>
        </div>
      </div> -->
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">发布用户 ：</span>
        <el-input style="width:50%;" v-model="userName" type="text" @click="userbtn()" placeholder="请选择发布用户"></el-input>
      </div> -->
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">类型 ：</span>
        <el-radio-group v-model="type" class="ml-4">
          <el-radio :label="1">个人</el-radio>
          <el-radio :label="2">公司</el-radio>
        </el-radio-group>
      </div> -->

      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">开票金额 ：</span>
        <el-input style="width:50%;" v-model="amount" type="text" placeholder="请输入开票金额" disabled></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">纳税人识别号 ：</span>
        <el-input style="width:50%;" v-model="creditCode" type="text" placeholder="请输入纳税人识别号"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">地址 ：</span>
        <el-input style="width:50%;" v-model="address" type="text" placeholder="请输入地址"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">手机号 ：</span>
        <el-input style="width:50%;" v-model="phone" type="text" placeholder="请输入手机号"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">开户行 ：</span>
        <el-input style="width:50%;" v-model="openBank" type="text" placeholder="请输入开户行"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">银行账号 ：</span>
        <el-input style="width:50%;" v-model="bankNumber" type="text" placeholder="请输入银行账号"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">接收邮箱 ：</span>
        <el-input style="width:50%;" v-model="receiveEmail" type="text" placeholder="请输入接收邮箱"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">接收手机号 ：</span>
        <el-input style="width:50%;" v-model="receivePhone" type="text" placeholder="请输入接收手机号"></el-input>
      </div>

      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">状态 ：</span>
        <el-radio-group v-model="status" class="ml-4">
          <el-radio :label="1">正常</el-radio>
          <el-radio :label="2">禁用</el-radio>
        </el-radio-group>
      </div> -->
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">团游标签 ：</span>
        <el-tag v-for="(item, index) in tags" :key="index" class="mx-1" closable :disable-transitions="false"
          @close="handleClose(index)" style="margin: 3px;">
          {{ item }}
        </el-tag>
        <el-input v-model="inputValue" class="ml-1 w-20" size="small" style="width: 150px;margin: 3px;" />
        <el-button class="button-new-tag ml-1" size="small" @click="handleInputConfirm()" style="margin: 3px;">
          + 添加
        </el-button>
      </div> -->
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="dialogFormVisible" title="用户列表" width="70%" center>
      <div style="margin:2% 0;display: inline-block;">
        <span>手机号:</span>
        <el-input style="width: 150px;" clearable placeholder="请输入手机号" v-model="phoneU"></el-input>
      </div>
      <div style="margin:2% 0;display: inline-block;">
        <span>昵称:</span>
        <el-input style="width: 150px;" clearable placeholder="请输入昵称" v-model="campus"></el-input>
      </div>
      <div style="display: inline-block;">
        <el-button style="margin-left:15px;" size="small" type="primary" icon="document" @click="selectU">查询
        </el-button>
        <el-button style="margin-left:15px;" size="small" type="primary" icon="document" @click="cleansU">重置
        </el-button>
      </div>
      <el-table :data="userData" v-loading="tableDataLoadingU" style="width: 100%">
        <el-table-column prop="userId" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="userName" label="用户昵称" width="120">
          <template #default="scope">
            <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{
              scope.row.userName ? scope.row.userName : '未绑定' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="avatar" label="用户头像" width="100">
          <template #default="scope">
            <img :src="scope.row.avatar" width="60" height="60" />
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号">
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
        </el-table-column>
        <el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
          <template #default="scope">
            <el-button size="small" type="primary" @click="userBtns(scope.row)">详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectUser()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 线路详情 -->
    <el-dialog v-model="dialogFormVisibleXl" title="线路详情列表" width="70%" center>
      <div style="text-align: right;margin: 10px;">
        <el-button style="margin-left:15px;" size="small" class="btn-refresh" @click="selectXlU">刷新
        </el-button>
        <el-button size="small" type="warning" plain @click="routeChangeAdd()">添加</el-button>
      </div>
      <el-table :data="routeData" v-loading="tableDataLoadingXlU" style="width: 100%">
        <el-table-column prop="baseInfoId" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="number" label="天数(第几天)" width="120">
        </el-table-column>
        <!-- <el-table-column prop="img" label="景点图片" width="100">
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
        </el-table-column> -->
        <el-table-column prop="remark" label="介绍">
          <template #default="scope">
            <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
              <div>
                {{ scope.row.remark }}</div>
              <template #reference>
                <div
                  style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
                  {{ scope.row.remark }}</div>
              </template>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="scenicSpotVoList" label="景点">
          <template #default="scope">
            <div v-if="scope.row.scenicSpotVoList">
              <span v-for="(item, index) in scope.row.scenicSpotVoList" :key="index">{{ item.name }}；</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
        </el-table-column>
        <el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
          <template #default="scope">
            <el-button size="small" type="warning" plain @click="routeChangeAdd(scope.row)">修改</el-button>
            <el-button size="small" type="danger" @click="routeDelete(scope.row)" style="margin: 5px;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <!-- 线路详情 -->
    <el-dialog v-model="dialogFormVisibleXlR" :title="titles" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">天数（第几天） ：</span>
        <el-input style="width:50%;" v-model="number" type="text" placeholder="请输入天数（第几天）"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">描述 ：</span>
        <el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入描述"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">景点 ：</span>
        <el-tag v-for="(item, index) in scenicSpotVoList" :key="index" class="mx-1" closable
          :disable-transitions="false" @close="handleClose2(index)" style="margin: 3px;">
          {{ item.name }}
        </el-tag>
        <el-button class="button-new-tag ml-1" size="small" @click="scenicBtn()" style="margin: 3px;">
          + 添加
        </el-button>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleXlR = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="routeNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 景点列表 -->
    <el-dialog v-model="dialogFormVisibleJd" title="景点列表" width="70%" center>
      <div style="margin:2% 0;display: inline-block;">
        <span>昵称:</span>
        <el-input style="width: 150px;" clearable placeholder="请输入昵称" v-model="campus"></el-input>
      </div>
      <div style="display: inline-block;">
        <el-button style="margin-left:15px;" size="small" type="primary" icon="document" @click="selectJd">查询
        </el-button>
      </div>
      <el-table :data="userData" v-loading="tableDataLoadingU" style="width: 100%">
        <el-table-column prop="id" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="name" label="景点名称">
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
        <el-table-column prop="location" label="景点位置">
        </el-table-column>
        <el-table-column prop="openHours" label="开放时间" width="180">
        </el-table-column>
        <el-table-column prop="ticketPrice" label="门票价格" width="180">
        </el-table-column>
        <el-table-column prop="intro" label="简介" width="180">
        </el-table-column>
        <el-table-column prop="traffic" label="交通攻略" width="180">
        </el-table-column>
        <el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleInputConfirm2(scope.row)">详情
            </el-button>
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
const {
  proxy
} = getCurrentInstance();
const router = useRouter();
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
const title = ref(''); //路线标题
const imgUrl = ref(''); //封面图
const status = ref(1); //状态 1正常 2禁用
const userId = ref(''); //发布者id
const userName = ref('');
const remark = ref(''); //描述
const days = ref(''); //行程天数
const kilometres = ref(''); //行驶公里数
const times = ref(''); //最佳时间
const tags = reactive([]);//标签集合
const inputValue = ref('');
const type = ref('');
const creditCode = ref('');
const amount = ref('');
const address = ref('');
const phone = ref('');
const openBank = ref('');
const bankNumber = ref('');
const receiveEmail = ref('');
const receivePhone = ref('');

const userData = reactive([]);
const page1 = ref(1);
const limit1 = ref(10);
const total1 = ref(0);
const campus = ref('');
const phoneU = ref('');
const laundryIdU = ref('');
const dialogFormVisible = ref(false);
const tableDataLoadingU = ref(false);
const dialogFormVisibleXl = ref(false);
const tableDataLoadingXlU = ref(false);
const routeData = reactive([]);
const page2 = ref(1);
const limit2 = ref(10);
const baseInfoId = ref('');
const number = ref(''); //天数（第几天）
const scenicSpotId = reactive([]); //景点id，多个，以逗号分隔
const scenicSpotVoList = reactive([]); //景点集合

const dialogFormVisibleXlR = ref(false);
const percentage2 = ref(0);
const dialogFormVisibleJd = ref(false);

const titleT = ref('');
const typeT = ref(1);
const statusT = ref('');
const userNameT = ref('');
// 状态类型
const statusData = reactive([ // 1待开票 2已开票
  {
    label: '全部',
    value: ''
  },
  {
    label: '待开票',
    value: 1
  },
  {
    label: '已开票',
    value: 2
  },
]);
const typeData = reactive([ // 1个人 2公司
  {
    label: '全部',
    value: ''
  },
  {
    label: '个人',
    value: 1
  },
  {
    label: '公司',
    value: 2
  },
]);
onMounted(() => {
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '开票列表') {
    classify.value = 1
    liaotianSelect();
  }
}

//分类数据 列表
const liaotianSelect = () => {
  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.post('/admin/invoice/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    title: titleT.value,
    status: statusT.value,
    type: typeT.value,
    userName: userNameT.value,

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
  typeT.value = 1
  statusT.value = ''
  userNameT.value = ''
  titleT.value = ''
  page.value = 1
  liaotianSelect()
}

// 添加/修改 基本信息 弹框
const addChangeU = (row) => {
  if (row) {
    titles.value = '修改'
    id.value = row.id
    type.value = row.type
    creditCode.value = row.creditCode
    amount.value = row.amount
    address.value = row.address
    phone.value = row.phone
    openBank.value = row.openBank
    bankNumber.value = row.bankNumber
    receiveEmail.value = row.receiveEmail
    receivePhone.value = row.receivePhone
    // if (row.tags) {
    //   tags.splice(0, tags.length, ...row.tags.split(','))
    // } else {
    //   tags.splice(0, tags.length)
    // }
  } else {
    titles.value = '添加'
    id.value = ''
    title.value = ''
    imgUrl.value = ''
    status.value = 1
    userId.value = ''
    userName.value = ''
    remark.value = ''
    days.value = ''
    kilometres.value = ''
    times.value = ''
    tags.splice(0, tags.length)

  }
  dialogFormVisibleU.value = true
}

const StairNoticeToU = () => {
  // if (title.value == '') {
  //   ElMessage({
  //     message: '路线标题不能为空',
  //     type: 'warning',
  //     onClose: () => {
  //     }
  //   })
  //   return
  // }
  // if (imgUrl.value == '') {
  //   ElMessage({
  //     message: '封面图不能为空',
  //     type: 'warning',
  //     onClose: () => {
  //     }
  //   })
  //   return
  // }
  // if (userId.value == '') {
  //   ElMessage({
  //     message: '发布用户不能为空',
  //     type: 'warning',
  //     onClose: () => {
  //     }
  //   })
  //   return
  // }
  // if (remark.value == '') {
  //   ElMessage({
  //     message: '描述不能为空',
  //     type: 'warning',
  //     onClose: () => {
  //     }
  //   })
  //   return
  // }
  // if (days.value == '') {
  //   ElMessage({
  //     message: '行程天数不能为空',
  //     type: 'warning',
  //     onClose: () => {
  //     }
  //   })
  //   return
  // }
  // if (kilometres.value == '') {
  //   ElMessage({
  //     message: '行驶公里数不能为空',
  //     type: 'warning',
  //     onClose: () => {
  //     }
  //   })
  //   return
  // }
  // if (times.value == '') {
  //   ElMessage({
  //     message: '最佳时间不能为空',
  //     type: 'warning',
  //     onClose: () => {
  //     }
  //   })
  //   return
  // }

  // if (titles.value == '添加') {
  //   routeUrl.value = '/admin/baseInfo/save'
  // } else {
  routeUrl.value = '/admin/invoice/update'
  // }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    type: type.value,
    creditCode: creditCode.value,
    address: address.value,
    phone: phone.value,
    openBank: openBank.value,
    bankNumber: bankNumber.value,
    receiveEmail: receiveEmail.value,
    receivePhone: receivePhone.value,
    amount: amount.value,

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
  proxy.$Request.post('/admin/invoice/delete', {
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
  imgUrl.value = file.data
}
const userbtn = () => {

  page1.value = 1
  campus.value = ''
  phoneU.value = ''
  laundryIdU.value = ''
  dialogFormVisible.value = true
  dataSelectUser()
}
// 获取 用户列表 数据
const dataSelectUser = () => {

  tableDataLoadingU.value = true
  userData.splice(0, userData.length)
  proxy.$Request.post('/admin/base/user/list', {
    // params: {
    page: page1.value,
    limit: limit1.value,
    username: campus.value,
    phone: phoneU.value,
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
const selectU = () => {
  page1.value = 1
  dataSelectUser()
}
// 重置
const cleansU = () => {
  page1.value = 1
  campus.value = ''
  phoneU.value = ''
  laundryIdU.value = ''
  dataSelectUser()
}

// 确认选择
const userBtns = (row) => {
  userId.value = row.userId
  userName.value = row.userName
  dialogFormVisible.value = false
}
const handleClose = (index) => {
  tags.splice(index, 1);
}
const handleInputConfirm = () => {
  tags.push(inputValue.value)
  inputValue.value = ''
}
const beforeChange = (row) => {
  proxy.$Request.post('/admin/invoice/updateStatus', {
    id: row.id,
    status: row.status
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
// 线路详情 弹框
const routeChangeU = (row) => {
  baseInfoId.value = row.id
  dialogFormVisibleXl.value = true
  routeSelectUser();
}
// 获取 详情 数据
const routeSelectUser = () => {

  tableDataLoadingXlU.value = true
  routeData.splice(0, routeData.length)
  proxy.$Request.post('/admin/route/page', {
    // params: {
    page: page2.value,
    limit: limit2.value,
    baseInfoId: baseInfoId.value,
    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoadingXlU.value = false;
    routeData.splice(0, routeData.length, ...res.data)
  })
}
// 查询
const selectXlU = () => {
  page2.value = 1
  routeSelectUser()
}
// 添加/修改 线路 弹框
const routeChangeAdd = (row) => {
  if (row) {
    titles.value = '修改'
    id.value = row.id
    number.value = row.number
    remark.value = row.remark
    scenicSpotVoList.splice(0, scenicSpotVoList.length, ...row.scenicSpotVoList)
    if (row.scenicSpotId) {
      scenicSpotId.splice(0, scenicSpotId.length, ...row.scenicSpotId.split(','))
    } else {
      scenicSpotId.value = []
    }
  } else {
    titles.value = '添加'
    id.value = ''
    number.value = ''
    remark.value = ''
    scenicSpotId.value = []
    scenicSpotVoList.value = []

  }
  percentage2.value = 0
  dialogFormVisibleXlR.value = true
}

const routeNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/route/save'
  } else {
    routeUrl.value = '/admin/route/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    number: number.value,
    remark: remark.value,
    scenicSpotId: scenicSpotId.toString(),
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
          routeSelectUser()
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
  proxy.$Request.post('/admin/route/delete', {
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
          routeSelectUser()
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
const scenicBtn = () => {
  dialogFormVisibleJd.value = true
  routeSelectJingdian()

}
// 获取 详情 数据
const routeSelectJingdian = () => {

  tableDataLoadingU.value = true
  userData.splice(0, userData.length)
  proxy.$Request.post('/admin/scenicSpot/page', {
    // params: {
    page: page1.value,
    limit: limit1.value,
    name: campus.value,
    location: '',
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
const handleClose2 = (index) => {
  scenicSpotVoList.splice(index, 1);
  scenicSpotId.splice(index, 1);
}
const handleInputConfirm2 = (row) => {
  scenicSpotVoList.push(row)
  scenicSpotId.push(row.id)
  dialogFormVisibleJd.value = false
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
</style>
