<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="商户列表" name="first">
        <div style="margin: 5px;">
          <div style="display: inline-block;margin: 5px;">
            <span>用户昵称 ：</span>
            <el-input v-model="userNameT" placeholder="请输入用户昵称" style="width: 150px;"
              @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>店铺名称 ：</span>
            <el-input v-model="nameT" placeholder="请输入店铺名称" style="width: 150px;"
              @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>状态 ：</span>
            <el-select v-model="statusT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="handleSearch">
              <el-option v-for="(item, index) in inviteVerifyData" :key="index" :label="item.label"
                :value="item.value" />
            </el-select>
          </div>&nbsp;&nbsp;
          <el-button size="small" type="primary" plain @click="handleSearch()">查询</el-button>
          <el-button size="small" type="warning" plain @click="clearSearch()">重置</el-button>
          <!-- <el-button size="small" type="primary" @click="addChangeU()">添加</el-button> -->
        </div>
        <el-table v-loading="tableDataLoading" :data="qunzuData">
          <el-table-column prop="shopId" label="编号" width="80" fixed="left"></el-table-column>
          <el-table-column prop="userName" label="用户昵称" width="160">
            <template #default="scope">
              <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{ scope.row.userName ?
                scope.row.userName : '未绑定' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="店铺名称" fixed="left">
          </el-table-column>
          <el-table-column prop="contact" label="联系电话"></el-table-column>
          <el-table-column prop="businessHours" label="营业时间"></el-table-column>
          <el-table-column prop="province" label="店铺地址" width="200">
            <template #default="scope">
              {{ scope.row.province }}{{ scope.row.city }}{{ scope.row.district }}{{ scope.row.address }}
            </template>
          </el-table-column>
          <el-table-column prop="description" label="店铺介绍" width="200"></el-table-column>
          <el-table-column prop="img" label="店铺图片" width="200">
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
          <el-table-column prop="reason" label="审核理由" width="200"></el-table-column>
          <!-- <el-table-column prop="inviteVerify" label="是否开启认证" width="100">
            <template #default="scope">
              <span v-if="scope.row.inviteVerify == 1">是</span>
              <span v-else>否</span>
            </template>
          </el-table-column> -->
          <!-- <el-table-column prop="auditRemark" label="审核备注" width="200"></el-table-column> -->
          <!-- <el-table-column prop="auditStatus" label="审核状态" width="100">
            <template #default="scope">
              <span v-if="scope.row.auditStatus == 0">待审核</span>
              <span v-if="scope.row.auditStatus == 1">审核通过</span>
              <span v-if="scope.row.auditStatus == 2">审核拒绝</span>
            </template>
          </el-table-column>-->
          <el-table-column prop="status" label="状态" width="100" fixed="right">
            <template #default="scope">
              <span v-if="scope.row.status === 0">未营业</span>
              <span v-if="scope.row.status == 1">营业中</span>
              <span v-if="scope.row.status == 2">待审核</span>
              <span v-if="scope.row.status == 3">审核拒绝</span>
              <!-- <el-switch v-if="scope.row.status === 0 || scope.row.status === 1" v-model="scope.row.status" class="ml-2"
                :active-value="1" :inactive-value="0" @change="changeBtn(scope.row)"
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" /> -->
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" type="warning" @click="shenheChange(scope.row)" style="margin: 5px;"
                v-if="scope.row.status == 2" :disabled="!isAuth('vipPrivilege:update')">审核处理</el-button>

              <el-button size="small" type="warning" plain @click="orderChange(scope.row)"
                style="margin: 5px;">订单</el-button>

              <el-button size="small" type="primary" @click="fwChange(scope.row)" style="margin: 5px;">服务管理</el-button>
              <el-button size="small" type="primary" plain @click="addChangeU(scope.row)"
                style="margin: 5px;">修改</el-button>
              <el-button size="small" type="primary" @click="tjChange(scope.row)" style="margin: 5px;"
                plain>门店统计</el-button>
              <el-button size="small" type="danger" @click="shopDelete(scope.row)" style="margin: 5px;">删除</el-button>
              <el-button size="small" type="primary" @click="fxsChange(scope.row)" style="margin: 5px;"
                plain>发型师</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="qunzuSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <!-- <el-tab-pane label="商户分类" name="second">
        <div style="margin: 0 20px;text-align: right;">
          <el-button size="small" class="btn-refresh" @click="flSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeFl()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="fenleiData">
          <el-table-column fixed prop="id" label="编号" width="100"></el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" @click="addChangeFl(scope.row)" style="margin: 5px;">修改</el-button>
              <el-button size="small" type="danger" @click="flDelete(scope.row)" style="margin: 5px;">删除</el-button>
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
      </el-tab-pane> -->
    </el-tabs>
    <!-- 弹框 -->
    <!-- 添加、修改 店铺弹框 -->
    <el-dialog :title="titles" v-model="dialogFormVisible4" center width="70%">
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">用户昵称 ：</span>
        <el-input style="width:50%;" v-model="userName" type="text" @click="userbtn()" placeholder="请选择用户昵称"></el-input>
        <!-- <el-select v-model="typeId" class="m-2" placeholder="请选择店铺类型" style="width: 50%;">
          <el-option v-for="(item, index) in fenleiData" :key="index" :label="item.name" :value="item.id" />
        </el-select> -->
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">店铺名称：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入店铺名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">联系方式：</span>
        <el-input style="width:50%;" v-model="contact" type="text" placeholder="请输入联系方式"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">简介：</span>
        <el-input style="width:50%;" v-model="description" type="text" placeholder="请输入简介"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">营业时间：</span>
        <el-input style="width:50%;" v-model="businessHours" type="text" placeholder="请输入营业时间"></el-input>
      </div>
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="width: 200px;display: inline-block;text-align: right;">店铺图片：</span>
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
        <span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
        <el-radio-group v-model="status" class="ml-4">
          <el-radio :label="1">营业</el-radio>
          <el-radio :label="0">未营业</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">省市区：</span>
        <el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" v-model="storeAddress"
          :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
        </el-cascader>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">详细地址：</span>
        <el-input style="width:50%;" v-model="address" type="text" placeholder="请输入详细地址"></el-input>
        <el-button type="info" size="small" plain @click="selectDz" style="margin-left: 10px;">获取经纬度</el-button>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
        <el-input style="width:50%;" v-model="lat" type="text" placeholder="请输入纬度"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
        <el-input style="width:50%;" v-model="lng" type="text" placeholder="请输入经度"></el-input>
      </div>
      <div>
        <div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible4 = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeTo" style="margin: 10px;">确认</el-button>
        </span>
      </template>
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
    <!--发型师列表 弹框 -->
    <el-dialog v-model="dialogFormVisibleCy" title="发型师列表" width="80%" center>
      <div style="margin: 10px;">
        <div style="display: inline-block;margin: 5px;">
          <span>用户昵称 ：</span>
          <el-input v-model="userNameC" placeholder="请输入用户昵称" style="width: 150px;"
            @keyup.enter.native="handleSearchCy()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>手机号 ：</span>
          <el-input v-model="phoneC" placeholder="请输入手机号" style="width: 150px;"
            @keyup.enter.native="handleSearchCy()" />
        </div>&nbsp;&nbsp;
        <el-button size="small" type="primary" plain @click="handleSearchCy()">查询</el-button>
        <el-button size="small" type="primary" plain @click="clearSearchCy()">重置</el-button>
      </div>
      <el-table :data="tableDataCy" v-loading="loadingCy" style="width: 100%">
        <el-table-column prop="id" label="编号" width="100"></el-table-column>
        <el-table-column prop="name" label="发型师名称">
          <template #default="scope">
            <span v-if="scope.row.name" style="color: #13ce66;cursor: pointer;" @click="updates(scope.row.userId)">
              {{ scope.row.name ? scope.row.name : '未绑定' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="img" label="展示图片">
          <template #default="scope">
            <div v-if="scope.row.img" style="display:flex;flex-wrap: wrap;">
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
        <el-table-column prop="description" label="发型师简介"></el-table-column>
        <el-table-column prop="tag" label="标签"></el-table-column>
        <el-table-column prop="appointmentCount" label="预约次数"></el-table-column>
        <el-table-column prop="productionCount" label="作品数"></el-table-column>


        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <span v-if="scope.row.status == 0">正常</span>
            <span v-if="scope.row.status == 1">请假</span>
            <span v-if="scope.row.status == 2">禁用</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button size="small" type="danger" @click="fxsDelete(scope.row)" style="margin: 5px;">踢出</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectCy()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!--服务列表 弹框 -->
    <el-dialog v-model="dialogFormVisibleFw" title="服务列表" width="80%" center>
      <div style="margin: 10px;">
        <div style="display: inline-block;margin: 5px;">
          <span>用户昵称 ：</span>
          <el-input v-model="userNameC" placeholder="请输入用户昵称" style="width: 150px;"
            @keyup.enter.native="handleSearchFw()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>手机号 ：</span>
          <el-input v-model="phoneC" placeholder="请输入手机号" style="width: 150px;"
            @keyup.enter.native="handleSearchFw()" />
        </div>&nbsp;&nbsp;
        <el-button size="small" type="primary" plain @click="handleSearchFw()">查询</el-button>
        <el-button size="small" type="primary" plain @click="clearSearchFw()">重置</el-button>
      </div>
      <el-table :data="tableDataCy" v-loading="loadingCy" style="width: 100%">
        <el-table-column prop="id" label="编号" width="100"></el-table-column>
        <el-table-column prop="serviceTypeName" label="类型"></el-table-column>
        <el-table-column prop="name" label="服务项目名称"></el-table-column>
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
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectFw()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!--申请信息 弹框 -->
    <el-dialog v-model="dialogFormVisibleSq" title="申请信息" width="80%" center>
      <div style="margin: 10px;">
        <div style="display: inline-block;margin: 5px;">
          <span>用户昵称 ：</span>
          <el-input v-model="userNameC" placeholder="请输入用户昵称" style="width: 150px;"
            @keyup.enter.native="handleSearchSq()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>手机号 ：</span>
          <el-input v-model="phoneC" placeholder="请输入手机号" style="width: 150px;"
            @keyup.enter.native="handleSearchSq()" />
        </div>&nbsp;&nbsp;
        <el-button size="small" type="primary" plain @click="handleSearchSq()">查询</el-button>
        <el-button size="small" type="primary" plain @click="clearSearchSq()">查询</el-button>
      </div>
      <el-table :data="tableDataSp" v-loading="loadingCy" style="width: 100%">
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
        <el-table-column prop="createTime" label="申请时间" width="180"></el-table-column>
      </el-table>
    </el-dialog>
    <!--聊天记录 弹框 -->
    <el-dialog v-model="dialogFormVisibleLt" title="聊天记录" width="80%" center>
      <div style="margin: 10px;">
        <div style="display: inline-block;margin: 5px;">
          <span>用户昵称 ：</span>
          <!-- @keydown.enter.native="handleSearchLt" -->
          <el-input v-model="userNameC" placeholder="请输入用户昵称" style="width: 150px;"
            @keyup.enter.native="handleSearchLt()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>手机号 ：</span>
          <el-input v-model="phoneC" placeholder="请输入手机号" style="width: 150px;"
            @keyup.enter.native="handleSearchLt()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>内容类型 ：</span>
          <el-select v-model="typeC" class="m-2" placeholder="请选择内容类型" style="width: 150px;" @change="handleSearchLt">
            <el-option v-for="(item, index) in typeData" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>聊天内容 ：</span>
          <el-input v-model="contentC" placeholder="请输入聊天内容" style="width: 150px;" />
        </div>&nbsp;&nbsp;
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
    <!-- 二维码审核 弹框 -->
    <el-dialog v-model="loadingerweima" title="群二维码" width="600px" center>
      <div style="text-align: center;">
        <img :src="qrCodeData" alt="QR Code" style="width: 400px;height: auto;">
      </div>

    </el-dialog>
    <!-- 添加/修改 分类 弹框 -->
    <el-dialog v-model="dialogFormVisibleU" :title="titles" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入名称"></el-input>
      </div>
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
            <el-button size="small" type="primary" @click="userBtns(scope.row)">确定
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
    <el-dialog v-model="dialogFormVisibleSh" title="审核" width="60%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
        <el-radio-group v-model="status" class="ml-4">
          <el-radio :label="1">通过</el-radio>
          <el-radio :label="3">拒绝</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;position: relative;bottom: 70px;">审核说明：</span>
        <el-input style="width:50%;" v-model="reason" type="textarea" :rows="4" placeholder="请输入审核说明"></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleSh = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToSh" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 订单列表 -->
    <el-dialog v-model="dialogFormVisibleOrder" title="店铺订单" width="80%" center>
      <div>
        <div style="display: inline-block;margin: 5px;">
          <span>用户名称 ：</span>
          <el-input v-model="userNameT" placeholder="请输入用户名称" style="width: 150px;"
            @keyup.enter.native="getUserList()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>手机号 ：</span>
          <el-input v-model="phoneT" placeholder="请输入手机号" style="width: 150px;" @keyup.enter.native="getUserList()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>订单编号 ：</span>
          <el-input v-model="orderNoT" placeholder="请输入订单编号" style="width: 150px;"
            @keyup.enter.native="getUserList()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>发型师名称 ：</span>
          <el-input v-model="hairdresserNameT" placeholder="请输入发型师名称" style="width: 150px;"
            @keyup.enter.native="getUserList()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>服务名称 ：</span>
          <el-input v-model="itemNameT" placeholder="请输入服务名称" style="width: 150px;"
            @keyup.enter.native="getUserList()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>状态 ：</span>
          <el-select v-model="stateT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="getUserList">
            <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </div>&nbsp;&nbsp;
        <!-- <div style="display: inline-block;margin: 5px;">
        <span>开始时间：</span>
        <el-date-picker v-model="startTime" type="datetime" placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss" />
      </div>
      <div style="display: inline-block;margin: 5px;">
        <span>结束时间：</span>
        <el-date-picker v-model="endTime" type="datetime" placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss" />
      </div> -->
        <el-button type="primary" size="small" class="btn-refresh" @click="getUserList">刷新</el-button>
        <el-button type="warning" size="small" @click="clearBtn">重置</el-button>
      </div>

      <el-row class="el-rows">
        <el-col :span="24">
          <div class="menu-list">
            <el-table :data="userList" v-loading="menuLoading">
              <el-table-column prop="id" align="center" label="编号" width="60" fixed="left" />
              <el-table-column label="预约用户" align="center" min-width="170" prop="userName" fixed="left">
                <template #default="scope">
                  <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{ scope.row.userName
                    ?
                    scope.row.userName : '未绑定' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="phone" label="手机号"></el-table-column>
              <!-- <el-table-column prop="shopName" label="店铺名称"></el-table-column> -->
              <el-table-column prop="hairdresserName" label="发型师姓名"></el-table-column>
              <el-table-column prop="itemName" label="服务项目名称"></el-table-column>
              <el-table-column prop="appointmentDate" label="预约日期" width="160">
                <template #default="scope">
                  <div>{{ scope.row.appointmentDate }} {{ scope.row.startTime }} ~ {{ scope.row.endTime }}</div>
                </template>
              </el-table-column>
              <el-table-column prop="orderNo" label="订单编号" width="160"></el-table-column>
              <el-table-column prop="money" label="金额" width="80"></el-table-column>
              <el-table-column prop="couponAmount" label="优惠券抵扣金额"></el-table-column>
              <el-table-column prop="cardAmount" label="u礼卡抵扣金额"></el-table-column>
              <el-table-column prop="payableAmount" label="应付金额"></el-table-column>
              <el-table-column prop="platform" label="支付方式">
                <template #default="scope"> <!-- 1 微信 2 支付宝 3 paypal支付 4 stripe支付 5 余额 -->
                  <div v-if="scope.row.platform == 1">微信</div>
                  <div v-if="scope.row.platform == 2">支付宝</div>
                  <div v-if="scope.row.platform == 3">paypal支付</div>
                  <div v-if="scope.row.platform == 4">stripe支付</div>
                  <div v-if="scope.row.platform == 5">余额</div>
                </template>
              </el-table-column>
              <el-table-column prop="verifyTime" label="核销时间" width="160"></el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
              <el-table-column prop="status" label="状态" fixed="right">
                <template #default="scope"> <!-- 0:待支付 1:已支付 2:已完成 3:已取消 4:退款中 5:已退款 -->
                  <div v-if="scope.row.status == 0">待支付</div>
                  <div v-if="scope.row.status == 1">已支付</div>
                  <div v-if="scope.row.status == 2">已完成</div>
                  <div v-if="scope.row.status == 3">已取消</div>
                  <div v-if="scope.row.status == 4">退款中</div>
                  <div v-if="scope.row.status == 5">已退款</div>
                </template>
              </el-table-column>
              <!-- <el-table-column label="操作" fixed="right" width="160">
                <template #default="scope">
                  <el-button type="primary" size="small" v-if="scope.row.status == 1"
                    @click="wanchengBtn(scope.row)">完成订单</el-button>
                  <el-button type="warning" size="small" plain @click="quxiaoBtn(scope.row)">取消订单</el-button>
                <el-button type="warning" size="small" @click="tuikuanBtn(scope.row)">退款</el-button>
                </template>
              </el-table-column> -->
            </el-table>
          </div>
        </el-col>
      </el-row>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="getUserList()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 订单列表 -->
    <el-dialog v-model="dialogFormVisibleTj" title="门店统计" width="80%" center>
      <div class="dashboard">
        <div style="margin: 20px;font-size: 16px;font-weight: 600;color: #999;">收益统计</div>
        <el-row :gutter="20" style="margin-bottom: 20px;" v-loading="menuLoading">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ shouyitongji.totalProfit ? shouyitongji.totalProfit : 0 }}</div>
                <div class="stat-label">总收益</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ shouyitongji.monthProfit ? shouyitongji.monthProfit : 0 }}</div>
                <div class="stat-label">月收益</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ shouyitongji.dayProfit ? shouyitongji.dayProfit : 0 }}</div>
                <div class="stat-label">今日收益</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <!-- 订单数量 -->
        <div style="margin: 20px;font-size: 16px;font-weight: 600;color: #999;">订单数量统计</div>
        <el-row :gutter="20" style="margin-bottom: 20px;" v-loading="menuLoading">
          <el-col :span="6" class="stat-col" v-for="(item, index) in orderNum" :key="index">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ item.count ? item.count : '0' }}</div>
                <div class="stat-label" v-if="item.status === 0">待支付</div>
                <div class="stat-label" v-if="item.status === 1">已支付</div>
                <div class="stat-label" v-if="item.status === 2">已完成</div>
                <div class="stat-label" v-if="item.status === 3">已取消</div>
                <div class="stat-label" v-if="item.status === 4">退款中</div>
                <div class="stat-label" v-if="item.status === 5">已退款</div>
              </div>
            </el-card>
          </el-col>

        </el-row>
        <!-- 服务数量 fuwuNum -->
        <div style="margin: 20px;font-size: 16px;font-weight: 600;color: #999;">服务数量</div>
        <el-row :gutter="20" style="margin-bottom: 20px;" v-loading="menuLoading">
          <el-col :span="6" class="stat-col" v-for="(item, index) in fuwuNum" :key="index">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ item.count ? item.count : '0' }}</div>
                <div class="stat-label" v-if="item.status === 1">上架服务</div>
                <div class="stat-label" v-if="item.status === 2">下架服务</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

    </el-dialog>
  </div>
</template>

<script setup>
import {
  jsonp
} from 'vue-jsonp'
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
  ElMessage,
  ElMessageBox,
  ElNotification,
} from 'element-plus';
import {
  pcaTextArr
} from 'element-china-area-data'

let geocoder = reactive([]);
let map = reactive([]);
let markersArray = reactive([]);

const {
  proxy
} = getCurrentInstance();
const router = useRouter();
const route = useRoute();
const activeName = ref('first'); //默认选中
const qunzuData = reactive([]); //我的群组 集合
const tousuData = reactive([]); //好友请求 集合
const huihuaData = reactive([]); //聊天内容 集合
const tableDataPl = reactive([]); //评论 集合
const tableDataCy = reactive([]); //群成员 集合
const tableDataLt = reactive([]); //群聊天 集合
const fenleiData = reactive([]);


const page = ref(1);//当前页
const limit = ref(10);//每页条数
const total = ref(0);//总条数
const page1 = ref(1);//当前页
const limit1 = ref(10);//每页条数
const total1 = ref(0);//总条数

//列表是否加载中
const dialogFormVisible4 = ref(false);
const dialogFormVisiblePl = ref(false);
const dialogFormVisibleCy = ref(false);
const dialogFormVisibleLt = ref(false);
const dialogFormVisibleFw = ref(false);
const tableDataLoading = ref(false);
const tableDataLoading1 = ref(false);
const loadingCy = ref(false);
const receiverUserId = ref('');
const senderUserId = ref('');
const shopId = ref('');

const userId = ref(''); //用户id
const name = ref(''); //商户名称
const description = ref(''); //简介
const businessHours = ref(''); //营业时间
const status = ref(''); //是否营业中 0未营业 1营业中 2待审核 3:审核拒绝
const contact = ref(''); //联系方式
const img = reactive([]); //图片，都好分隔

const province = ref(''); //省
const city = ref(''); //市
const district = ref(''); //区
const address = ref('');//详细地址
const lat = ref(''); //纬度
const lng = ref(''); //经度
const userName = ref('');//联系人姓名

const tags = reactive([]); //标签, 逗号分隔
const inputValue = ref('');
const percentage2 = ref(0);

const userNameC = ref('');
const phoneC = ref('');
const typeC = ref('');
const contentC = ref('');
const targetId = ref('');
const qrCodeData = ref('');
const loadingerweima = ref(false);
const userNameT = ref('');
const inviteVerifyT = ref('');
const statusT = ref('');
const nameT = ref('');
const routeUrl = ref('');

const userData = reactive([]);
// const page1 = ref(1);
// const limit1 = ref(10);
// const total1 = ref(0);
const campus = ref('');
const phoneU = ref('');
const laundryIdU = ref('');
const dialogFormVisible = ref(false);
const tableDataLoadingU = ref(false);
const dialogFormVisibleXl = ref(false);
const tableDataLoadingXlU = ref(false);

//是否开启认证： 集合
const inviteVerifyData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '营业中',
    value: 1
  },
  {
    label: '未营业',
    value: 0
  },
  {
    label: '待审核',
    value: 2
  },
  {
    label: '审核拒绝',
    value: 3
  }
]);
// 状态类型
const statusData = reactive([ //0正常 1禁用 2过期 3人数已达最大
  {
    label: '全部',
    value: ''
  },
  {
    label: '正常',
    value: 0
  },
  {
    label: '禁用',
    value: 1
  },
  {
    label: '过期',
    value: 2
  },
  {
    label: '人数已达最大',
    value: 3
  }
]);
const dialogFormVisibleU = ref(false);
const titles = ref('');


const dialogFormVisibleSh = ref(false);
const reason = ref('');
const dialogFormVisibleOrder = ref(false);
const userList = reactive([]);
const dialogFormVisibleTj = ref(false);

//列表是否加载中
const menuLoading = ref(false);
//是否显示添加/修改弹窗
const userNameO = ref(''); //用户昵称
const phoneO = ref(''); //手机号
const orderNoO = ref('');
const shopNameO = ref('');
const hairdresserNameO = ref('');
const itemNameO = ref('');
const startTimeO = ref('');
const endTimeO = ref('');
const stateO = ref('');
const classifyO = ref('');
const shouyitongji = ref({});//收益统计
const orderNum = reactive([]);//订单数量统计
const fuwuNum = reactive([]);//服务统计
const lifashiNum = ref({});//理发师统计
const options = pcaTextArr;
const storeAddress = reactive([]);
const storeAddre = ref('请选择城市')
onMounted(() => {
  if (route.query.status != undefined && route.query.status) {
    statusT.value = Number(route.query.status)
  } else {
    statusT.value = ''
  }
  qunzuSelect();
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
  page.value = 1
  if (event.srcElement.innerText == '商户列表') {
    qunzuSelect();
  }
  if (event.srcElement.innerText == '商户分类') {
    qunzufenleiSelect();
  }


}

// 店铺列表 列表
const qunzuSelect = () => {
  tableDataLoading.value = true
  qunzuData.splice(0, qunzuData.length)
  proxy.$Request.post('/admin/shopInfo/page', {
    page: page.value,
    limit: limit.value,
    userName: userNameT.value,
    // typeName: inviteVerifyT.value,
    status: statusT.value,
    name: nameT.value,
    // province: '',
    // city: '',
    // district: '',
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    qunzuData.splice(0, qunzuData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
// 刷新
const handleSearch = () => {
  page.value = 1
  qunzuSelect()
}
// 重置
const clearSearch = () => {
  page.value = 1
  userNameT.value = ''
  inviteVerifyT.value = ''
  statusT.value = ''
  nameT.value = ''
  qunzuSelect()
}
// 添加、修改 商铺 弹框
const addChangeU = (row) => {
  if (row) {
    titles.value = '修改'
    shopId.value = row.shopId
    userId.value = row.userId
    name.value = row.name
    description.value = row.description
    businessHours.value = row.businessHours
    status.value = row.status
    contact.value = row.contact
    province.value = row.province
    city.value = row.city
    district.value = row.district
    address.value = row.address
    lat.value = row.lat
    lng.value = row.lng
    storeAddre.value = province.value + '/' + city.value + '/' + district.value
    userName.value = row.userName
    if (row.img) {
      img.splice(0, img.length, ...row.img.split(','))
    } else {
      img.value = []
    }
  } else {
    titles.value = '添加'
    shopId.value = ''
    userId.value = ''
    name.value = ''
    description.value = ''
    businessHours.value = ''
    status.value = 0
    contact.value = ''
    img.value = []
    province.value = ''
    city.value = ''
    district.value = ''
    address.value = ''
    lat.value = ''
    lng.value = ''
    storeAddre.value = '请选择城市'
    userName.value = ''
  }
  inputValue.value = ''
  percentage2.value = 0
  // qunzufenleiSelect()
  getMyLocation()
  dialogFormVisible4.value = true
}
// 确认
const StairNoticeTo = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/shopInfo/save'
  } else {
    routeUrl.value = '/admin/shopInfo/update'
  }
  proxy.$Request.post(routeUrl.value, {
    shopId: shopId.value,
    userId: userId.value,
    name: name.value,
    description: description.value,
    businessHours: businessHours.value,
    status: status.value,
    contact: contact.value,
    province: province.value,
    city: city.value,
    district: district.value,
    address: address.value,
    lat: lat.value,
    lng: lng.value,
    img: img.toString(),
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisible4.value = false
          qunzuSelect()
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
// 删除 商铺
const shopDelete = (row) => {
  proxy.$Request.post('/admin/shopInfo/delete', {
    shopId: row.shopId,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          qunzuSelect()
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
const handleClose = (index) => {
  tags.splice(index, 1);
}
const handleInputConfirm = () => {
  tags.push(inputValue.value)
  inputValue.value = ''
}
const clear2 = (index) => {
  img.splice(index, 1);
}
const handleUploadSuccess4 = (file) => {
  img.push(file.dateTableEmits)
}
const onprogress2 = (event,) => {
  console.log('event,', parseInt(event.percent))
  percentage2.value = parseInt(event.percent)
}

// 修改状态
const changeBtn = (row) => {
  proxy.$Request.post('/admin/groupInfo/updateStatus', {

    id: row.id,
    status: row.status,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '修改成功',
        type: 'success',
        onClose: () => {
          qunzuSelect()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
          qunzuSelect()
        }
      })
    }
  })
}
/** 发型师列表 弹框 */
const fxsChange = (row) => {
  shopId.value = row.shopId
  userNameC.value = ''
  phoneC.value = ''
  dialogFormVisibleCy.value = true
  dataSelectCy();

}
/** 发型师列表 数据 */
const dataSelectCy = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.post('/admin/hairdresser/page', {
    // params: {
    shopId: shopId.value,
    hairdresserName: userNameC.value,
    // phone: phoneC.value,
    page: page1.value,
    limit: limit1.value,

    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataCy.splice(0, tableDataCy.length, ...res.data.records)
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
// 服务列表
const fwChange = (row) => {
  shopId.value = row.shopId
  userNameC.value = ''
  phoneC.value = ''
  dialogFormVisibleFw.value = true
  dataSelectFw();

}
/** 发型师列表 数据 */
const dataSelectFw = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.post('/admin/serviceItem/page', {
    // params: {
    shopId: shopId.value,
    serviceTypeId: '',
    // phone: phoneC.value,
    page: page1.value,
    limit: limit1.value,

    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataCy.splice(0, tableDataCy.length, ...res.data.records)
    total1.value = res.data.total;
  })
}
const handleSearchFw = () => {
  dataSelectFw();
}
const clearSearchFw = () => {
  userNameC.value = ''
  phoneC.value = ''
  dataSelectFw();
}


/** 聊天记录 弹框 */
const ltChange = (row) => {
  shopId.value = row.shopId
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
      groupId: shopId.value,
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
// 群分类 列表
const qunzufenleiSelect = () => {
  tableDataLoading.value = true
  fenleiData.splice(0, fenleiData.length)
  proxy.$Request.post('/admin/shopType/page', {
    page: page.value,
    limit: limit.value,
    name: nameT.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    fenleiData.splice(0, fenleiData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
// 刷新 分类
const flSearch = () => {
  page.value = 1
  qunzufenleiSelect()
}
// 添加/修改 分类 弹框
const addChangeFl = (row) => {
  if (row) {
    titles.value = '修改'
    shopId.value = row.shopId
    name.value = row.name

  } else {
    titles.value = '添加'
    shopId.value = ''
    name.value = ''

  }
  dialogFormVisibleU.value = true
}
// 确认
const StairNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/shopType/save'
  } else {
    routeUrl.value = '/admin/shopType/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: shopId.value,
    name: name.value,

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
          qunzufenleiSelect()
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
// 删除 分类
const flDelete = (row) => {
  proxy.$Request.post('/admin/shopType/delete', {
    id: row.id,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          qunzufenleiSelect()
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
  proxy.$Request.post('/admin/groupType/updateStatus', {
    id: data.id,
    status: data.status
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          qunzufenleiSelect()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
          qunzufenleiSelect()
        }
      })
    }
  })
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
    userType: '',
    status: '',
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
// 审核 弹框
const shenheChange = (row) => {
  shopId.value = row.shopId
  reason.value = ''
  status.value = ''
  dialogFormVisibleSh.value = true

}
const StairNoticeToSh = () => {
  if (status.value == '') {
    ElNotification({
      title: '提示',
      message: '请选择审核结果',
      type: 'warning',
    })
    return
  }
  if (status.value == 3) {
    if (reason.value == '') {
      ElNotification({
        title: '提示',
        message: '请输入审核说明',
        type: 'warning',
      })
      return
    }
  }

  proxy.$Request.post('/admin/shopInfo/audit', {
    shopId: shopId.value,
    status: status.value,
    reason: reason.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleSh.value = false
          qunzuSelect()
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
// 订单弹框
const orderChange = (row) => {
  shopId.value = row.shopId
  userNameO.value = ''
  phoneO.value = ''
  classifyO.value = ''
  stateO.value = ''
  orderNoO.value = ''
  shopNameO.value = ''
  hairdresserNameO.value = ''
  itemNameO.value = ''
  startTimeO.value = ''
  endTimeO.value = ''
  page1.value = 1
  getUserList();
  dialogFormVisibleOrder.value = true

}
//获取订单列表
const getUserList = () => {
  menuLoading.value = true
  userList.splice(0, userList.length)
  proxy.$Request.post('/admin/order/page', {
    // params: {
    page: page1.value,
    limit: limit1.value,
    userName: userNameO.value,
    phone: phoneO.value,
    orderNo: orderNoO.value,
    shopId: shopId.value,
    hairdresserName: hairdresserNameO.value,
    itemName: itemNameO.value,
    type: '',
    // type: classifyT.value,
    status: stateO.value,
    startTime: startTimeO.value,
    endTime: endTimeO.value,


    // }
  }, {
    type: 'json'
  }).then(res => {
    // console.log(res)
    userList.splice(0, userList.length, ...res.data.records)
    total.value = res.data.total;
    menuLoading.value = false
  })
};
const clearBtn = () => {
  page1.value = 1
  userNameO.value = ''
  phoneO.value = ''
  classifyO.value = ''
  stateO.value = ''
  orderNoO.value = ''
  shopNameO.value = ''
  hairdresserNameO.value = ''
  itemNameO.value = ''
  startTimeO.value = ''
  endTimeO.value = ''
  getUserList()
}
// 踢出发型师
const fxsDelete = (row) => {
  ElMessageBox.confirm('确定要踢出发型师?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      proxy.$Request.post('/admin/hairdresser/remove', {
        hairdresserId: row.id,
      }, {
        type: 'json'
      }).then((res) => {
        console.log(res)
        if (res.code == 0) {
          ElMessage({
            message: '操作成功',
            type: 'success',
            onClose: () => {
              dataSelectCy()
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
    }).catch(() => { })




}
// 店铺统计
const tjChange = (row) => {
  shopId.value = row.shopId
  getTjChange();
  dialogFormVisibleTj.value = true
}
// 获取 门店统计数据
const getTjChange = () => {
  menuLoading.value = true
  // 收益统计
  proxy.$Request.get('/admin/statistics/shopProfit', {
    params: {
      shopId: shopId.value,
      // type: classifyT.value,
    }
  }, {
    // type: 'json'
  }).then(res => {
    // console.log(res)
    shouyitongji.value = res.data;
    menuLoading.value = false
  })
  // 订单数量 统计
  orderNum.splice(0, orderNum.length)
  proxy.$Request.get('/admin/order/shopOrderCount', {
    params: {
      shopId: shopId.value,
      // type: classifyT.value,
    }
  }, {
    // type: 'json'
  }).then(res => {
    // console.log(res)
    orderNum.splice(0, orderNum.length, ...res.data)
    menuLoading.value = false
  })
  // 服务统计 fuwuNum
  fuwuNum.splice(0, fuwuNum.length)
  proxy.$Request.get('/admin/serviceItem/shopServiceItemCount', {
    params: {
      shopId: shopId.value,
      // type: classifyT.value,
    }
  }, {
    // type: 'json'
  }).then(res => {
    // console.log(res)
    // fuwuNum.value = res.data;
    fuwuNum.splice(0, fuwuNum.length, ...res.data)
    menuLoading.value = false
  })

  // 理发师统计 lifashiNum
}


//定位获得当前位置信息
const getMyLocation = () => {
  var geolocation = new qq.maps.Geolocation("IUPBZ-YYY6Z-A7NXB-TXWBO-CZIPE-ICF2P", "用户端");
  geolocation.getIpLocation(showPosition, showErr);
  // geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
}
const showPosition = (position) => {
  console.log(position);
  setMap();
}
const showErr = (e) => {
  console.log("定位失败", e);
  getMyLocation(); //定位失败再请求定位，测试使用
}
//位置信息在地图上展示
const setMap = () => {
  //步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
  //设置地图中心点
  var myLatlng = new qq.maps.LatLng(lat.value, lng.value);
  //定义工厂模式函数
  var myOptions = {
    zoom: 13, //设置地图缩放级别
    center: myLatlng, //设置中心点样式
    mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
  }
  // //获取dom元素添加地图信息
  var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
  //给地图添加点击事件

  //给定位的位置添加图片标注
  var marker = new qq.maps.Marker({
    position: myLatlng,
    map: map
  });
  // `````````````


  if (lat.value == '') {
    var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
  } else {
    var center = new qq.maps.LatLng(lat.value, lng.value);
  }
  var map = new qq.maps.Map(document.getElementById("container1"), {
    center: center,
    zoom: 13
  });
  var marker = new qq.maps.Marker({
    position: center,
    map: map
  });

  var latlngBounds = new qq.maps.LatLngBounds();
  qq.maps.event.addListener(map, "click", function (event) {
    console.log(event, qq.maps);
    lng.value = event.latLng.getLng(); // 经度
    lat.value = event.latLng.getLat(); // 纬度
    jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
      .latLng
      .getLng() + '&key=IUPBZ-YYY6Z-A7NXB-TXWBO-CZIPE-ICF2P&get_poi=1&output=jsonp', {
      myCustomUrlParam: 'veryNice'
    }).then(response => {
      console.log('response', response)
      address.value = response.result.formatted_addresses.recommend
      province.value = response.result.ad_info.province
      city.value = response.result.ad_info.city
      district.value = response.result.ad_info.district

    }).catch(error => {
      // handle error
    }).then(() => {
      // always executed
    });
    if (markersArray) {
      for (let i in markersArray) {
        markersArray[i].setMap(null);
      }
    }
    var marker = new qq.maps.Marker({
      map: map,
      position: event.latLng
    });
    markersArray.push(marker);
  });
  geocoder = new qq.maps.Geocoder({
    complete: function (result) {
      console.log(result);
      lng.value = result.detail.location.lng;
      lat.value = result.detail.location.lat;
      map.setCenter(result.detail.location);
      var marker = new qq.maps.Marker({
        map: map,
        position: result.detail.location
      });
      markersArray.push(marker);
    }
  });
}
// 地图定位
const selectDz = () => {

  console.log('搜索地址：', province.value + city.value + district.value + address.value)
  var addr = province.value + city.value + district.value + address.value //

  jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + addr +
    '&key=IUPBZ-YYY6Z-A7NXB-TXWBO-CZIPE-ICF2P&get_poi=1&output=jsonp', {
    myCustomUrlParam: 'veryNice'
  }).then(response => {
    // handle success
    console.log('response', response)
    lng.value = response.result.location.lng; // 经度
    lat.value = response.result.location.lat; // 纬度
    setMap()
  }).catch(error => {
    // handle error
  }).then(() => {
    // always executed
  });
}
const handleChange55 = (value) => {
  console.log('=============', value)

  // value = ref['cascaderAddr'].currentLabels
  province.value = value[0]
  city.value = value[1]
  district.value = value[2]
  if (city.value == '市辖区') {
    city.value = province.value
  }
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

.dashboard {
  .welcome-card {
    .content {
      p {
        font-size: 16px;
        color: #666;
      }
    }
  }

  .stat-col {
    margin-bottom: 20px;
  }

  .stat-card {
    text-align: center;

    .stat-content {
      padding: 30px 0;

      .stat-number {
        font-size: 24px;
        font-weight: bold;
        color: var(--admin-brand);
      }

      .stat-label {
        margin-top: 10px;
        color: #666;
      }
    }
  }
}

span {
  font-size: 14px;
  color: #303133;
}
</style>
