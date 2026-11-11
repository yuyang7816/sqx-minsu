<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="酒店列表" name="first">
        <div style="margin: 5px;">
          <div style="display: inline-block;margin: 5px;">
            <span>酒店类型 ：</span>
            <el-select v-model="shopTypeT" class="m-2" placeholder="请选择酒店类型" style="width: 150px;"
              @change="handleSearch">
              <el-option v-for="(item, index) in inviteVerifyData" :key="index" :label="item.label"
                :value="item.value" />
            </el-select>
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>绑定用户 ：</span>
            <el-input v-model="userNameT" placeholder="请输入绑定用户" style="width: 150px;" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>酒店名称 ：</span>
            <el-input v-model="nameT" placeholder="请输入酒店名称" style="width: 150px;" />
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>状态 ：</span>
            <el-select v-model="statusT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="handleSearch">
              <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div>&nbsp;&nbsp;
          <div style="display: inline-block;margin: 5px;">
            <span>城市 ：</span>
            <el-input v-model="cityT" placeholder="请输入城市" style="width: 150px;" />
          </div>&nbsp;&nbsp;
          <el-button size="small" type="primary" plain @click="handleSearch()">查询</el-button>
          <el-button size="small" type="warning" plain @click="clearSearch()">重置</el-button>
          <el-button size="small" type="primary" @click="addChangeFl()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="qunzuData">
          <el-table-column prop="shopId" label="编号" width="80" fixed="left"></el-table-column>
          <el-table-column prop="shopType" label="酒店类型" width="100" fixed="left">
            <template #default="scope">
              <span v-if="scope.row.shopType == 1">酒店</span>
              <span v-if="scope.row.shopType == 2">民宿</span>
            </template>
          </el-table-column>

          <el-table-column prop="isRecommend" label="是否推荐" width="100" fixed="left">
            <template #default="scope">
              <span v-if="scope.row.isRecommend == 1">是</span>
              <span v-else>否</span>
              <!-- <el-switch v-model="scope.row.isRecommend" class="ml-2" :active-value="1" :inactive-value="0"
                @change="changeBtn(scope.row)" style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" /> -->
            </template>
          </el-table-column>
          <el-table-column prop="userName" label="绑定用户" fixed="left">
            <template #default="scope">
              <span v-if="scope.row.userId" style="color: #13ce66;cursor: pointer;" @click="updates(scope.row.userId)">
                {{ scope.row.userName }}
              </span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="avatar" label="群主头像" width="100">
            <template #default="scope">
              <img v-if="scope.row.avatar" :src="scope.row.avatar" style="width: 60px;height: 60px;" />
              <span v-else> - </span>
            </template>
          </el-table-column> -->
          <el-table-column prop="shopName" label="酒店名称" width="160" fixed="left"></el-table-column>
          <el-table-column prop="titleImg" label="封面图" width="100">
            <template #default="scope">
              <img v-if="scope.row.titleImg" :src="scope.row.titleImg" style="width: 50px;height: 50px;" />
              <span v-else> - </span>
            </template>
          </el-table-column>
          <el-table-column prop="carouselUrl" label="轮播图" width="300">
            <template #default="scope">
              <div v-if="scope.row.carouselUrl">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                  v-for="(item, index) in scope.row.carouselUrl.split(',')" :key="index">
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
          <el-table-column prop="rate" label="分成比例"></el-table-column>
          <el-table-column prop="allMoney" label="总收益"></el-table-column>
          <el-table-column prop="cashMoney" label="可提现金额"></el-table-column>
          <el-table-column prop="cashDownMoney" label="已提现金额"></el-table-column>
          <el-table-column prop="province" label="酒店地址" width="200">
            <template #default="scope">
              {{ scope.row.province }}{{ scope.row.city }}{{ scope.row.region }}{{ scope.row.address }}
            </template>
          </el-table-column>
          <el-table-column prop="lng" label="经度" width="200"></el-table-column>
          <el-table-column prop="lat" label="纬度" width="200"></el-table-column>
          <el-table-column prop="tags" label="标签" width="200">
            <!-- <template #default="scope">
              <div v-if="scope.row.tags">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                  v-for="(item, index) in scope.row.tags.split(',')" :key="index">
                  <template #reference>
                    <img style="width: 50px; height: 50px;margin: 3px;" :src="item" alt="">
                  </template>
                  <template #default>
                    <img style="width: 400px; height: auto;" :src="item" alt="">
                  </template>
                </el-popover>
              </div>
            </template> -->
          </el-table-column>
          <el-table-column prop="isEnable" label="状态" width="100" fixed="right">
            <template #default="scope">
              <span v-if="scope.row.isEnable == 1">上架</span>
              <span v-else>下架</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" @click="cyChange(scope.row)" style="margin: 5px;">员工管理</el-button>
              <el-button size="small" type="primary" @click="addChangeFl(scope.row)" style="margin: 5px;">修改</el-button>
              <el-button size="small" type="primary" @click="fjChange(scope.row)" style="margin: 5px;">房间管理</el-button>
              <el-button size="small" type="danger" @click="flDelete(scope.row)" style="margin: 5px;">删除</el-button>
              <el-button size="small" type="primary" @click="ltChange(scope.row)" style="margin: 5px;">评价记录</el-button>
              <el-button size="small" type="warning" @click="tjChange(scope.row)" style="margin: 5px;">统计</el-button>
              <el-button size="small" type="primary" @click="ordChange(scope.row)" style="margin: 5px;">订单列表</el-button>
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
    </el-tabs>
    <!-- 弹框 -->

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
    <!--员工 弹框 -->
    <el-dialog v-model="dialogFormVisibleCy" title="员工列表" width="80%" center>
      <div style="margin: 10px;">
        <div style="display: inline-block;margin: 5px;">
          <span>用户昵称：</span>
          <el-input v-model="userNameC" placeholder="请输入用户昵称" style="width: 150px;" />
        </div>
        <!-- @keydown.enter.native="handleSearchCy" -->
        <div style="display: inline-block;margin: 5px;">
          <span>手机号：</span>
          <el-input v-model="phoneC" placeholder="请输入手机号" style="width: 150px;" />
        </div>
        <el-button size="small" type="primary" plain @click="handleSearchCy()">查询</el-button>
        <el-button size="small" type="primary" @click="clearSearchCy()">重置</el-button>
        <el-button size="small" type="primary" plain @click="addSearchCy(1)">新增</el-button>
      </div>
      <el-table :data="tableDataCy" v-loading="loadingCy" style="width: 100%">
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
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column prop="createTime" label="入群时间" width="180"></el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button size="small" type="danger" @click="ygDelete(scope.row)" style="margin: 5px;">删除</el-button>
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
    <!--房间管理 弹框 -->
    <el-dialog v-model="dialogFormVisibleSq" title="房间管理" width="80%" center>
      <div style="display: inline-block;margin: 5px;">
        <span>类型 ：</span>
        <el-select v-model="userNameC" class="m-2" placeholder="请选择房间类型" style="width: 150px;" @change="handleSearchSq">
          <el-option v-for="(item, index) in roomTypeList" :key="index" :label="item.roomType"
            :value="item.roomTypeId" />
        </el-select>
      </div>
      <el-button size="small" type="primary" plain @click="handleSearchSq()">查询</el-button>
      <el-button size="small" type="primary" plain @click="addSearchFj()">新增</el-button>
      <el-table :data="tableDataCy" v-loading="loadingCy" style="width: 100%">
        <el-table-column prop="roomId" label="编号" width="80" fixed="left"></el-table-column>
        <el-table-column prop="roomName" label="房间名称" width="150" fixed="left">
        </el-table-column>
        <el-table-column prop="roomTitleImg" label="房间封面图" width="100">
          <template #default="scope">
            <img :src="scope.row.roomTitleImg" style="width: 50px;height: 50px;" />
          </template>
        </el-table-column>
        <el-table-column prop="roomImg" label="房间轮播图" width="200">
          <template #default="scope">
            <div v-if="scope.row.roomImg">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                v-for="(item, index) in scope.row.roomImg.split(',')" :key="index">
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
        <el-table-column prop="roomCount" label="房间数量"></el-table-column>
        <el-table-column prop="money" label="价格"></el-table-column>
        <el-table-column prop="classifyIds" label=" 配置" width="180">
          <template #default="scope">
            <div v-if="scope.row.classifyList && scope.row.classifyList.length > 0">
              <el-tag type="info" v-for="(item, index) in scope.row.classifyList" :key="index" style="margin:3xp">{{
                item.title }};</el-tag>

            </div>
          </template>
        </el-table-column>
        <el-table-column prop="checkInTime" label="最早入住时间" width="180"></el-table-column>
        <el-table-column prop="checkOutTime" label="最晚离店时间" width="180"></el-table-column>
        <el-table-column prop="foodPolicy" label="餐食规则" width="180"></el-table-column>
        <el-table-column prop="petRule" label="宠物规则" width="180"></el-table-column>
        <el-table-column prop="roomType" label="房间类型" width="180"></el-table-column>
        <el-table-column prop="isEnable" label="是否启用" fixed="right">
          <template #default="scope">
            <span v-if="scope.row.isEnable == 1">启用</span>
            <span v-else>下架</span>
            <!-- <el-switch v-model="scope.row.isEnable"
                class="ml-2" :active-value="1" :inactive-value="0" @change="changeBtn(scope.row)"
                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" /> -->
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="addSearchFj(scope.row)" style="margin: 5px;">修改</el-button>
            <el-button size="small" type="danger" @click="flDeleteFl(scope.row)" style="margin: 5px;">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectFj()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!--聊天记录 弹框 -->
    <el-dialog v-model="dialogFormVisibleLt" title="评论列表" width="80%" center>
      <div style="margin: 10px;">
        <div style="display: inline-block;margin: 5px;">
          <span>用户昵称：</span>
          <!-- @keydown.enter.native="handleSearchLt" -->
          <el-input v-model="userNameC" placeholder="请输入用户昵称" style="width: 150px;" />
        </div>
        <!-- <div style="display: inline-block;margin: 5px;">
          <span>手机号：</span>
          <el-input v-model="phoneC" placeholder="请输入手机号" style="width: 150px;" />
        </div>
        <div style="display: inline-block;margin: 5px;">
          <span>内容类型：</span>
          <el-select v-model="typeC" class="m-2" placeholder="请选择内容类型" style="width: 150px;" @change="handleSearchLt">
            <el-option v-for="(item, index) in typeData" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </div> -->
        <div style="display: inline-block;margin: 5px;">
          <span>评论内容：</span>
          <el-input v-model="contentC" placeholder="请输入评论内容" style="width: 150px;" />
        </div>

        <el-button size="small" type="primary" plain @click="dataSelectLt()">查询</el-button>
        <el-button size="small" type="primary" plain @click="clearSearchLt()">查询</el-button>
      </div>
      <el-table :data="tableDataLt" v-loading="loadingCy" style="width: 100%">
        <el-table-column prop="evaluateId" label="编号" width="80">
        </el-table-column>
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


        <el-table-column prop="orderNumber" label="订单编号" width="160">
        </el-table-column>
        <el-table-column prop="evaluateMessage" label="内容">
        </el-table-column>
        <el-table-column prop="evaluateImg" label="评论图片">
          <template #default="scope">
            <div v-if="scope.row.evaluateImg">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                v-for="(item, index) in scope.row.evaluateImg.split(',')" :key="index">
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

        <el-table-column prop="satisfactionFlag" label="评分" width="160">
          <template #default="scope">
            <el-rate v-model="scope.row.satisfactionFlag" disabled show-score text-color="#ff9900" score-template="" />
          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectLt()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 二维码审核 弹框 -->
    <el-dialog v-model="loadingerweima" title="群二维码" width="600px" center>
      <div style="text-align: center;">
        <img :src="qrCodeData" alt="QR Code" style="width: 400px;height: auto;">
      </div>

    </el-dialog>
    <!-- 添加/修改 酒店 弹框 -->
    <el-dialog v-model="dialogFormVisibleU" :title="titles" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">酒店类型 ：</span>
        <el-radio-group v-model="shopType" class="ml-4">
          <el-radio :label="1">酒店</el-radio>
          <el-radio :label="2">民宿</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">绑定用户 ：</span>
        <el-input style="width:50%;" v-model="userName" type="text" placeholder="请输入绑定用户"></el-input>
        <el-button size="small" type="primary" plain @click="addSearchCy(2)">选择用户</el-button>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">酒店名称 ：</span>
        <el-input style="width:50%;" v-model="shopName" type="text" placeholder="请输入酒店名称"></el-input>
      </div>

      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">封面图 ：</span>
        <el-upload class="avatar-uploader" v-model="titleImg"
          style="display: inline-block;width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;color: #efefef;"
          action="https://jdmsadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
          :on-success="handleAvatarSuccess1">
          <img v-if="titleImg" :src="titleImg" class="avatar" style="width: 148px;height: 148px;" />
          <el-icon v-else style="font-size: 28px;color: #D9D9D9">
            <Plus />
          </el-icon>
        </el-upload>
      </div>
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="width: 200px;display: inline-block;text-align: right;">轮播图 ：</span>
        <div style="margin-bottom: 10px;display:flex;flex-direction: row;flex-wrap: wrap;">
          <div class="imgs" v-for="(item, index) in carouselUrl" :key="index">
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
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">标签 ：</span>
        <el-tag v-for="(item, index) in tags" :key="index" class="mx-1" closable :disable-transitions="false"
          @close="handleClose(index)" style="margin: 3px;">
          {{ item }}
        </el-tag>
        <el-input v-model="inputValue" class="ml-1 w-20" size="small" style="width: 150px;margin: 3px;" />
        <el-button class="button-new-tag ml-1" size="small" @click="handleInputConfirm()" style="margin: 3px;">
          + 添加
        </el-button>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">分成比例 ：</span>
        <el-input style="width:50%;" v-model="rate" type="text" placeholder="请输入分成比例"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">是否是推荐 ：</span>
        <el-radio-group v-model="isRecommend" class="ml-4">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">状态 ：</span>
        <el-radio-group v-model="isEnable" class="ml-4">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
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
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 用户列表 弹框 -->
    <el-dialog v-model="dialogFormVisibleUser" title="用户列表" width="70%" center>
      <div>
        <div style="display: inline-block;margin: 5px;">
          <span>用户名称 ：</span>
          <el-input v-model="userNameU" placeholder="请输入用户名称" style="width: 150px;"
            @keyup.enter.native="getUserList()" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>手机号码 ：</span>
          <el-input v-model="phoneU" placeholder="请输入手机号码" style="width: 150px;" @keyup.enter.native="getUserList()" />
        </div>&nbsp;&nbsp;
        <el-button type="primary" size="small" @click="getUserList">查询</el-button>
        <el-button type="primary" size="small" plain @click="clearBtn()">重置</el-button>
        <el-button type="primary" size="small" plain @click="querenBtn()" v-if="typeIndex == 1"
          :disabled="checkBoxData.value.length <= 0">确认添加</el-button>
      </div>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="menu-list">
            <el-table :data="userList" v-loading="menuLoading" @selection-change="changeFun">
              <el-table-column type="selection">
              </el-table-column>
              <el-table-column prop="userId" align="center" label="编号" width="60" fixed="left" />
              <el-table-column label="用户名称" align="center" min-width="170" prop="userName" fixed="left" />
              <el-table-column prop="sex" label="性别">
                <template #default="scope">
                  <span v-if="scope.row.sex == 1">男</span>
                  <span v-else-if="scope.row.sex == 2">女</span>
                  <span v-else>未设置</span>
                </template>
              </el-table-column>
              <el-table-column label="图像">
                <template #default="scope">
                  <img v-if="scope.row.avatar == null" src="../../assets/logo.png" alt="" width="40" height="40">
                  <img v-else :src="scope.row.avatar" alt="" width="40" height="40">
                </template>
              </el-table-column>
              <el-table-column prop="phone" label="手机号">
              </el-table-column>

              <el-table-column prop="platform" label="渠道来源"></el-table-column>
              <el-table-column prop="invitationCode" label="邀请码"></el-table-column>
              <el-table-column label="邀请人邀请码"></el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
              <el-table-column label="操作" v-if="typeIndex == 2">
                <template #default="scope">
                  <el-button type="primary" size="small" plain @click="xuaneBtn(scope.row)">选择</el-button>
                </template>
              </el-table-column>

            </el-table>
          </div>
        </el-col>
      </el-row>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit2" v-model:currentPage="page2" :total="total2" @pageChange="getUserList()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 添加/修改 房间 弹框 -->
    <el-dialog v-model="dialogFormVisibleUFl" :title="titles" width="70%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">房间类型 ：</span>
        <el-select v-model="roomType" class="m-2" placeholder="请选择房间类型" style="width: 50%;">
          <el-option v-for="(item, index) in roomTypeList" :key="index" :label="item.roomType"
            :value="item.roomTypeId" />
        </el-select>
        <!-- <el-input style="width:50%;" v-model="roomType" type="text" placeholder="请输入房间类型"></el-input> -->
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">房间名称 ：</span>
        <el-input style="width:50%;" v-model="roomName" type="text" placeholder="请输入房间名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">房间封面图 ：</span>
        <el-upload class="avatar-uploader" v-model="titleImg"
          style="display: inline-block;width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;color: #efefef;"
          action="https://jdmsadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
          :on-success="handleAvatarSuccess1">
          <img v-if="titleImg" :src="titleImg" class="avatar" style="width: 148px;height: 148px;" />
          <el-icon v-else style="font-size: 28px;color: #D9D9D9">
            <Plus />
          </el-icon>
        </el-upload>
      </div>
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="width: 200px;display: inline-block;text-align: right;">房间封面图 ：</span>
        <div style="margin-bottom: 10px;display:flex;flex-direction: row;flex-wrap: wrap;">
          <div class="imgs" v-for="(item, index) in carouselUrl" :key="index">
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
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">房间数量 ：</span>
        <el-input style="width:50%;" v-model="roomCount" type="text" placeholder="请输入房间数量"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">价格 ：</span>
        <el-input style="width:50%;" v-model="money" type="text" placeholder="请输入价格"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">配置 ：</span>
        <!-- <el-input style="width:50%;" v-model="classifyIds" type="text" placeholder="请输入配置"></el-input> -->
        <el-checkbox-group v-model="classifyIds" style="display: inline-block;">
          <el-checkbox v-for="(item, index) in classifyList" :key="index" :value="item.classifyId">
            {{ item.title }}
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">最早入住时间 ：</span>
        <!-- <el-input style="width:50%;" v-model="checkInTime" type="text" placeholder="请输入最早入住时间"></el-input> -->
        <el-time-select v-model="checkInTime" style="width: 50%" start="00:00" step="00:30" end="23:30"
          placeholder="请选择最早入住时间" />
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">最晚离店时间 ：</span>
        <!-- <el-input style="width:50%;" v-model="checkOutTime" type="text" placeholder="请输入最晚离店时间"></el-input> -->
        <el-time-select v-model="checkOutTime" style="width: 50%" start="00:00" step="00:30" end="23:30"
          placeholder="请选择最晚离店时间" />
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">餐食规则 ：</span>
        <el-input style="width:50%;" v-model="foodPolicy" type="text" placeholder="请输入餐食规则"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">宠物规则 ：</span>
        <el-input style="width:50%;" v-model="petRule" type="text" placeholder="请输入宠物规则"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序 ：</span>
        <el-input style="width:50%;" v-model="sort" type="text" placeholder="请输入排序"></el-input>
      </div>

      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">是否启用 ：</span>
        <el-radio-group v-model="isEnable" class="ml-4">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleUFl = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToUFl" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!--酒店统计 弹框 -->
    <el-dialog v-model="dialogFormVisibleTj" title="酒店统计" width="80%" center>
      <div class="dashboard">
        <el-row :gutter="20" style="margin-top: 40px;" v-loading="loadingCy">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataSytj.allMoney ? tableDataSytj.allMoney : 0 }}</div>
                <div class="stat-label">总收入</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataSytj.todayMoney ? tableDataSytj.todayMoney : 0 }}</div>
                <div class="stat-label">今日收入</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataSytj.monthMoney ? tableDataSytj.monthMoney : 0 }}</div>
                <div class="stat-label">本月收入</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataSytj.cashMoney ? tableDataSytj.cashMoney : 0 }}</div>
                <div class="stat-label">可提现金额</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataSytj.allUser ? tableDataSytj.allUser : 0 }}</div>
                <div class="stat-label">员工数量</div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <div style="font-size: 18px;font-weight: 600;margin: 10px;">订单统计</div>
        <el-row :gutter="20" style="margin-top: 40px;" v-loading="loadingCy">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataDdtj.pendingVerifyCount ? tableDataDdtj.pendingVerifyCount : 0 }}
                </div>
                <div class="stat-label">待核销数量</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataDdtj.completedCount ? tableDataDdtj.completedCount : 0 }}</div>
                <div class="stat-label">已完成数量</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataDdtj.refundedCount ? tableDataDdtj.refundedCount : 0 }}</div>
                <div class="stat-label">已取消数量</div>
              </div>
            </el-card>
          </el-col>

        </el-row>
        <div style="font-size: 18px;font-weight: 600;margin: 10px;">房间统计</div>
        <el-row :gutter="20" style="margin-top: 40px;" v-loading="loadingCy">
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataFjtj.enabledCount ? tableDataFjtj.enabledCount : 0 }}</div>
                <div class="stat-label">上架数量</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6" class="stat-col">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-number">{{ tableDataFjtj.disabledCount ? tableDataFjtj.disabledCount : 0 }}</div>
                <div class="stat-label">下架数量</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
    <!--酒店订单 弹框 -->
    <el-dialog v-model="dialogFormVisibleDd" title="酒店订单" width="80%" center>

      <div>
        <div style="display: inline-block;margin: 5px;">
          <span>下单用户 ：</span>
          <el-input v-model="userNameT" placeholder="请输入用户名称" style="width: 150px;" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>用户手机号 ：</span>
          <el-input v-model="phoneT" placeholder="请输入手机号" style="width: 150px;" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>订单编号 ：</span>
          <el-input v-model="orderNoT" placeholder="请输入订单编号" style="width: 150px;" />
        </div>&nbsp;&nbsp;


        <div style="display: inline-block;margin: 5px;">
          <span>状态 ：</span>
          <el-select v-model="stateT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="dataSelectDd">
            <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>开始时间 ：</span>
          <el-date-picker v-model="startTime" type="datetime" placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss" style="width: 150px;" />
        </div>&nbsp;&nbsp;
        <div style="display: inline-block;margin: 5px;">
          <span>结束时间 ：</span>
          <el-date-picker v-model="endTime" type="datetime" placeholder="选择时间" format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss" style="width: 150px;" />
        </div>&nbsp;&nbsp;
        <el-button type="primary" size="small" @click="dataSelectDd">查询</el-button>
        <el-button type="warning" size="small" @click="dataSelectclearBtn">重置</el-button>
      </div>

      <el-row class="el-rows">
        <el-col :span="24">
          <div class="menu-list">
            <el-table :data="tableDataCy" v-loading="menuLoading">
              <el-table-column prop="orderId" align="center" label="编号" width="60" fixed="left" />
              <el-table-column label="下单用户" align="center" min-width="170" prop="userName" fixed="left">
                <template #default="scope">
                  <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{
                    scope.row.userName ?
                      scope.row.userName : '未绑定' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="phone" label="用户手机号" width="160"></el-table-column>
              <el-table-column prop="shopName" label="酒店名称" width="160"></el-table-column>
              <el-table-column prop="roomName" label="房间名称"></el-table-column>

              <el-table-column prop="orderNumber" label="订单编号" width="180"></el-table-column>
              <el-table-column prop="orderMoney" label="订单金额"></el-table-column>
              <el-table-column prop="payMoney" label="支付金额"></el-table-column>
              <el-table-column prop="couponMoney" label="优惠券抵扣金额"></el-table-column>
              <el-table-column prop="shopMoney" label="酒店收入"></el-table-column>
              <el-table-column prop="platformMoney" label="平台收入" width="80">
                <template #default="scope">
                  <span style="color: red;">{{ scope.row.platformMoney ? scope.row.platformMoney : 0 }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="userScaleMoney" label="一级佣金"></el-table-column>
              <el-table-column prop="userTwoMoney" label="二级佣金"></el-table-column>
              <el-table-column prop="refundMoney" label="退款金额"></el-table-column>
              <el-table-column prop="payWay" label="支付方式">
                <template #default="scope"> <!-- 1 微信 2 支付宝 0 余额 -->
                  <div v-if="scope.row.payWay == 1">微信</div>
                  <div v-if="scope.row.payWay == 2">支付宝</div>
                  <div v-if="scope.row.payWay == 0">余额</div>
                </template>
              </el-table-column>

              <el-table-column prop="checkInvoice" label="是否勾选发票信息" width="180">
                <template #default="scope">
                  <div v-if="scope.row.checkInvoice == 1">未开票</div>
                  <div v-if="scope.row.checkInvoice == 2">开票中</div>
                  <div v-if="scope.row.checkInvoice == 3">已开票</div>
                </template>
              </el-table-column>
              <el-table-column prop="isVipOrder" label="是否是会员订单" width="180">
                <template #default="scope">
                  <div v-if="scope.row.isVipOrder == 1">是</div>
                  <div v-else>否</div>
                </template>
              </el-table-column>
              <el-table-column prop="payTime" label="支付时间" width="180"></el-table-column>
              <el-table-column prop="arrivalTime" label="到店时间" width="180"></el-table-column>
              <el-table-column prop="realName" label="用户姓名" width="180"></el-table-column>
              <el-table-column prop="startDate" label="入住时间" width="180"></el-table-column>
              <el-table-column prop="endDate" label="离店时间" width="180"></el-table-column>
              <el-table-column prop="roomCount" label="房间数量" width="180"></el-table-column>
              <el-table-column prop="warmReminder" label="温馨提示" width="180"></el-table-column>
              <el-table-column prop="remarks" label="备注" width="180"></el-table-column>
              <el-table-column prop="verificationTime" label="核销时间" width="180"></el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
              <el-table-column prop="orderStatus" label="状态" fixed="right">
                <template #default="scope"> <!-- 0:待支付 1:待核销 2:进行中 3:已完成 4:已取消 5:已退款 -->
                  <div v-if="scope.row.orderStatus == 0">待支付</div>
                  <div v-if="scope.row.orderStatus == 1">待核销</div>
                  <div v-if="scope.row.orderStatus == 2">进行中</div>
                  <div v-if="scope.row.orderStatus == 3">已完成</div>
                  <div v-if="scope.row.orderStatus == 4">已取消</div>
                  <div v-if="scope.row.orderStatus == 5">已退款</div>
                </template>
              </el-table-column>
              <el-table-column label="操作" fixed="right" width="160">
                <template #default="scope">
                  <el-button type="primary" size="small" v-if="scope.row.status == 1"
                    @click="wanchengBtn(scope.row)">完成订单</el-button>
                  <!-- <el-button type="warning" size="small" plain @click="quxiaoBtn(scope.row)">取消订单</el-button>
                <el-button type="warning" size="small" @click="tuikuanBtn(scope.row)">退款</el-button> -->
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectDd()" />
          </div>
        </el-col>
      </el-row>
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
const page2 = ref(1);//当前页
const limit2 = ref(10);//每页条数
const total2 = ref(0);//总条数

//列表是否加载中
const dialogFormVisible4 = ref(false);
const dialogFormVisiblePl = ref(false);
const dialogFormVisibleCy = ref(false);
const dialogFormVisibleLt = ref(false);
const dialogFormVisibleSq = ref(false);
const tableDataLoading = ref(false);
const tableDataLoading1 = ref(false);
const loadingCy = ref(false);
const receiverUserId = ref('');
const senderUserId = ref('');
const id = ref('');
const userNameC = ref('');
const phoneC = ref('');
const typeC = ref('');
const contentC = ref('');
const targetId = ref('');
const qrCodeData = ref('');
const loadingerweima = ref(false);
const userNameT = ref('');
const cityT = ref('');
const statusT = ref('');
const nameT = ref('');
const shopTypeT = ref('');
const routeUrl = ref('');
const percentage2 = ref(0);
const percentage3 = ref(0);

//是否开启认证： 集合
const inviteVerifyData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '酒店',
    value: 1
  },
  {
    label: '民宿',
    value: 2
  }
]);
// 状态类型
const statusData = reactive([ //0下架 1上架
  {
    label: '全部',
    value: ''
  },
  {
    label: '上架',
    value: 1
  },
  {
    label: '下架',
    value: 0
  },

]);
const dialogFormVisibleU = ref(false);
const titles = ref('');
const name = ref(''); //名称
const remark = ref(''); //备注
const maxPeopleNum = ref(''); //最大人数
const maxAmount = ref(''); //最大金额
const minAmount = ref(''); //最小金额

// 酒店信息
const shopId = ref('');
const shopName = ref(''); //酒店名称
const titleImg = ref(''); //封面图
const carouselUrl = reactive([]); //轮播图
const isEnable = ref(''); //状态 0下架 1上架
const isRecommend = ref(''); //是否是推荐0否 1是
const tags = reactive([]); //标签
const rate = ref(''); //分成比例
const shopType = ref(''); //酒店类型 1酒店 2民宿
const province = ref(''); //省
const city = ref(''); //市
const county = ref(''); //区/县
const address = ref(''); //详细地址
const lng = ref(''); //经度
const lat = ref(''); //纬度
const options = pcaTextArr;
const storeAddress = reactive([]);
const storeAddre = ref('请选择城市')
const inputValue = ref('');

const dialogFormVisibleUser = ref(false);
const tableDataLoadingUser = ref(false);
//用户列表
const userList = reactive([]);
//列表是否加载中
const menuLoading = ref(false);
//是否显示添加/修改弹窗
const userNameU = ref(''); //用户昵称
const phoneU = ref(''); //用户昵称
const checkBoxData = reactive([]);//多选数据
const roomTypeList = reactive([]);
const classifyList = reactive([]);

// 房间信息
const dialogFormVisibleUFl = ref(false);
const roomId = ref(''); // 房间Id
const roomName = ref(''); // 房间名称
const roomCount = ref(''); // 房间数量
const roomTitleImg = ref(''); // 房间封面图
const roomImg = ref(''); // 房间轮播图
const money = ref(''); // 价格
const classifyIds = ref([]); // 配置ids
const checkInTime = ref(''); // 入住时间
const checkOutTime = ref(''); // 离店时间
const foodPolicy = ref(''); // 餐食规则
const petRule = ref(''); // 宠物规则
// const isEnable = ref(''); // 是否启用 0否 1是
const roomType = ref(''); // 房间类型 (自定义)
const sort = ref(''); // 排序

const dialogFormVisibleTj = ref(false);
const tableDataSytj = ref({});
const tableDataDdtj = ref({});
const tableDataFjtj = ref({});
const dialogFormVisibleDd = ref(false);
const orderNoT = ref('');
const shopNameT = ref('');
const startTime = ref('');
const endTime = ref('');
const stateT = ref('');
const userId = ref('');
const userName = ref('');
const typeIndex = ref('');

onMounted(() => {
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

  if (event.srcElement.innerText == '酒店列表') {
    qunzuSelect();
  }
  if (event.srcElement.innerText == '群分类') {
    qunzufenleiSelect();
  }


}

// 酒店列表 列表
const qunzuSelect = () => {
  tableDataLoading.value = true
  qunzuData.splice(0, qunzuData.length)
  proxy.$Request.get('/admin/shops/getShopList', {
    params: {
      page: page.value,
      limit: limit.value,
      shopType: shopTypeT.value,
      city: cityT.value,
      auditStatus: statusT.value,
      shopName: nameT.value,
      userName: userNameT.value,
      // region: '',
    }
  }, {
    // type: 'json'
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
  shopTypeT.value = ''
  statusT.value = ''
  nameT.value = ''
  cityT.value = ''
  qunzuSelect()
}
// 修改状态
const changeBtn = (row) => {
  proxy.$Request.post('/admin/shops/save', row, {
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
// 添加/修改 酒店 弹框
const addChangeFl = (row) => {
  if (row) {
    titles.value = '修改'
    shopId.value = row.shopId
    shopName.value = row.shopName
    titleImg.value = row.titleImg
    userId.value = row.userId
    userName.value = row.userName
    if (row.carouselUrl) {
      carouselUrl.splice(0, carouselUrl.length, ...row.carouselUrl.split(','))
    } else {
      carouselUrl.value = []
    }
    isEnable.value = row.isEnable
    minAmount.value = row.minAmount
    isRecommend.value = row.isRecommend
    if (row.tags) {
      tags.splice(0, tags.length, ...row.tags.split(','))
    } else {
      tags.value = []
    }
    rate.value = row.rate
    shopType.value = row.shopType
    province.value = row.province
    city.value = row.city
    county.value = row.county
    address.value = row.address
    lat.value = row.lat
    lng.value = row.lng
    storeAddre.value = province.value + '/' + city.value + '/' + county.value
  } else {
    titles.value = '添加'
    shopId.value = ''
    shopName.value = ''
    titleImg.value = ''
    carouselUrl.value = []
    isEnable.value = 1
    minAmount.value = ''
    isRecommend.value = 1
    tags.value = []
    rate.value = ''
    shopType.value = 1
    province.value = ''
    city.value = ''
    county.value = ''
    address.value = ''
    lat.value = ''
    lng.value = ''
    storeAddre.value = '请选择城市'
    userId.value = ''
    userName.value = ''
  }
  percentage2.value = 0
  getMyLocation()
  dialogFormVisibleU.value = true
}
// 确认
const StairNoticeToU = () => {

  proxy.$Request.post('admin/shops/save', {
    shopId: shopId.value,
    shopName: shopName.value,
    titleImg: titleImg.value,
    carouselUrl: carouselUrl.toString(),
    isEnable: isEnable.value,
    minAmount: minAmount.value,
    isRecommend: isRecommend.value,
    tags: tags.toString(),
    rate: rate.value,
    shopType: shopType.value,
    province: province.value,
    city: city.value,
    county: county.value,
    address: address.value,
    lat: lat.value,
    lng: lng.value,
    userId: userId.value,
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
// 删除 酒店
const flDelete = (row) => {
  proxy.$Request.post('/admin/shops/deleteShopId?shopId=' + row.shopId, {
    // params: {
    // shopId: row.shopId,
    // }
  }, {
    // type: 'json'
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
/** 员工 弹框 */
const cyChange = (row) => {
  id.value = row.shopId
  userNameC.value = ''
  phoneC.value = ''
  checkBoxData.value = []
  dialogFormVisibleCy.value = true
  dataSelectCy();

}
/** 员工 数据 */
const dataSelectCy = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.get('/admin/shopsUserRole/getUserList', {
    params: {
      shopId: id.value,
      userName: userNameC.value,
      // phone: phoneC.value,
      page: page1.value,
      limit: limit1.value,

    }
  }, {
    // type: 'json'
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
// 新增 员工
const addSearchCy = (index) => {
  typeIndex.value = index
  page2.value = 1
  userNameU.value = ''
  phoneU.value = ''
  getUserList();
  dialogFormVisibleUser.value = true

}
// 重置
const clearBtn = () => {
  page2.value = 1
  userNameU.value = ''
  phoneU.value = ''
  getUserList();
}
//获取用户列表
const getUserList = () => {
  menuLoading.value = true
  userList.splice(0, userList.length)
  proxy.$Request.get('/user/selectUserList', {
    params: {
      page: page2.value,
      limit: limit2.value,
      userName: userNameU.value,
      phone: phoneU.value,
      isVip: '',
      status: '',
      platform: '',
      userType: '',
      inviterCode: '',

    }
  }, {
    // type: 'json'
  }).then(res => {
    // console.log(res)
    userList.splice(0, userList.length, ...res.data.list)
    total2.value = res.data.totalCount;
    menuLoading.value = false
  })
};
// 多选
const changeFun = (val) => {
  checkBoxData.value = val
  console.log('checkBoxData', checkBoxData.value);

}
// 确认添加 员工
const querenBtn = () => {
  var ids = []
  for (var i in checkBoxData.value) {
    ids.push(checkBoxData.value[i].userId)
  }
  console.log(ids, '--', ids.toString());

  proxy.$Request.post('/admin/shopsUserRole/saveShopUser', {
    // params: {
    shopId: id.value,
    userIds: ids.toString()
    // }
  }, {
    type: 'form-data'
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

}
// 删除员工
const ygDelete = (row) => {
  proxy.$Request.post('/admin/shopsUserRole/deleteShopUser', {
    // params: {
    shopId: id.value,
    userIds: row.userId
    // }
  }, {
    type: 'form-data'
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
}
// 选择用户
const xuaneBtn = (row) => {
  userId.value = row.userId
  userName.value = row.userName
  dialogFormVisibleUser.value = false
}
/** 房间 弹框 */
const fjChange = (row) => {
  id.value = row.shopId
  shopId.value = row.shopId
  userNameC.value = ''
  phoneC.value = ''
  checkBoxData.value = []
  dialogFormVisibleSq.value = true
  roomTypSearch();
  dataSelectFj();

}
/** 房间 数据 */
const dataSelectFj = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.get('/admin/shopsRoom/getShopRoomList', {
    params: {
      shopId: id.value,
      roomType: userNameC.value,
      // phone: phoneC.value,
      page: page1.value,
      limit: limit1.value,

    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataCy.splice(0, tableDataCy.length, ...res.data.records)
    total1.value = res.data.total;
  })
}
const roomTypSearch = () => {
  // 房间类型
  roomTypeList.splice(0, roomTypeList.length)
  proxy.$Request.get('/admin/shopsRoom/getRoomTypeList', {
    params: {
    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    roomTypeList.splice(0, roomTypeList.length, ...res.data)
  })
  // 房间配置
  classifyList.splice(0, classifyList.length)
  proxy.$Request.get('/admin/roomDeviceClassify/getClassifyList', {
    params: {
      page: 1,
      limit: 100,
      // name: '',
    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    classifyList.splice(0, classifyList.length, ...res.data.records)
    total.value = res.data.total;
  })
}
const handleSearchSq = () => {
  page1.value = 1
  dataSelectFj();
}
const addSearchFj = (row) => {
  if (row) {
    titles.value = '修改房间'
    roomId.value = row.roomId
    roomName.value = row.roomName
    roomCount.value = row.roomCount
    money.value = row.money
    if (row.classifyIds) {
      classifyIds.value = row.classifyIds.split(',').map(item => Number(item))
    } else {
      classifyIds.value = []
    }
    // classifyIds.value = row.classifyIds
    checkInTime.value = row.checkInTime
    checkOutTime.value = row.checkOutTime
    foodPolicy.value = row.foodPolicy
    petRule.value = row.petRule
    isEnable.value = row.isEnable
    roomType.value = row.roomTypeId ?? row.roomType
    sort.value = row.sort
    titleImg.value = row.roomTitleImg
    if (row.roomImg) {
      carouselUrl.splice(0, carouselUrl.length, ...row.roomImg.split(','))
    } else {
      carouselUrl.value = []
    }

  } else {
    titles.value = '添加房间'
    roomId.value = '' // 房间Id
    roomName.value = '' // 房间名称
    roomCount.value = '' // 房间数量
    money.value = '' // 价格
    classifyIds.value = [] // 配置ids
    checkInTime.value = '' // 入住时间
    checkOutTime.value = '' // 离店时间
    foodPolicy.value = '' // 餐食规则
    petRule.value = '' // 宠物规则
    isEnable.value = 1 // 是否启用 0否 1是
    roomType.value = '' // 房间类型 (自定义)
    sort.value = '' // 排序
    titleImg.value = ''
    carouselUrl.value = []

  }
  dialogFormVisibleUFl.value = true
}
// 确认 添加、修改 房间
const StairNoticeToUFl = () => {

  proxy.$Request.post('admin/shopsRoom/saveShopRoom', {
    shopId: shopId.value,
    roomId: roomId.value,
    roomName: roomName.value,
    roomCount: roomCount.value,
    money: money.value,
    classifyIds: classifyIds.value.toString(),
    checkInTime: checkInTime.value,
    checkOutTime: checkOutTime.value,
    foodPolicy: foodPolicy.value,
    petRule: petRule.value,
    isEnable: isEnable.value,
    roomType: roomType.value,
    sort: sort.value,
    roomTitleImg: titleImg.value,
    roomImg: carouselUrl.toString(),
  }, {
    type: 'form-data'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleUFl.value = false
          dataSelectFj()
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
// 删除 房间
const flDeleteFl = (row) => {
  proxy.$Request.post('/admin/shopsRoom/deleteRoom?roomIds=' + row.roomId, {
    // params: {
    // shopId: row.shopId,
    // }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dataSelectFj()
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



/** 评论 弹框 */
const ltChange = (row) => {
  id.value = row.shopId
  page1.value = 1
  userNameC.value = ''
  phoneC.value = ''
  typeC.value = ''
  contentC.value = ''
  dataSelectLt()
  dialogFormVisibleLt.value = true
}
const clearSearchLt = () => {
  page1.value = 1
  userNameC.value = ''
  phoneC.value = ''
  typeC.value = ''
  contentC.value = ''
  dataSelectLt()
}
/** 评论 数据 */
const dataSelectLt = () => {
  loadingCy.value = true
  tableDataLt.splice(0, tableDataLt.length)
  proxy.$Request.get('/admin/evaluate/getUserEvaluate', {
    params: {
      page: page1.value,
      limit: limit1.value,
      shopId: id.value,
      userName: userNameC.value,
      // phone: phoneC.value,
      // targetType: 2,
      // type: typeC.value,
      evaluateMessage: contentC.value,

    }
  }, {}).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataLt.splice(0, tableDataLt.length, ...res.data.records)
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
  proxy.$Request.post('/admin/groupType/page', {
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
function handleAvatarSuccess1(file) {
  titleImg.value = file.data
}
const clear3 = (index) => {
  carouselUrl.splice(index, 1);
}
const handleUploadSuccess3 = (file) => {
  carouselUrl.push(file.data)
}
const onprogress3 = (event,) => {
  console.log('event,', parseInt(event.percent))
  percentage3.value = parseInt(event.percent)
}
// 酒店统计弹框
const tjChange = (row) => {
  id.value = row.shopId
  dialogFormVisibleTj.value = true
  tongjiSearch();
}
const tongjiSearch = () => {
  loadingCy.value = true;
  // 收益、可提现、员工
  proxy.$Request.get('/admin/shops/statisticsShop', {
    params: {
      shopId: id.value,
    }
  }, {}).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataSytj.value = res.data
  })
  // 订单数量统计
  proxy.$Request.get('/admin/shops/statisticsShopOrder', {
    params: {
      shopId: id.value,
    }
  }, {}).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataDdtj.value = res.data
  })
  // 房间数量统计
  proxy.$Request.get('/admin/shops/statisticsShopRoom', {
    params: {
      shopId: id.value,
    }
  }, {}).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataFjtj.value = res.data
  })

  //


}

/** 订单 弹框 */
const ordChange = (row) => {
  id.value = row.shopId
  userNameC.value = ''
  phoneC.value = ''
  dialogFormVisibleDd.value = true
  dataSelectDd();

}
const dataSelectclearBtn = () => {
  userNameC.value = ''
  phoneC.value = ''
  orderNoT.value = ''
  stateT.value = ''
  startTime.value = ''
  endTime.value = ''
  dataSelectDd();

}
/** 订单 数据 */
const dataSelectDd = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.get('/admin/orders/getOrderList', {
    params: {
      page: page1.value,
      limit: limit1.value,
      userName: userNameC.value,
      phone: phoneC.value,
      orderNo: orderNoT.value,
      shopId: id.value,
      status: stateT.value,
      startTime: startTime.value,
      endTime: endTime.value,

    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataCy.splice(0, tableDataCy.length, ...res.data.records)
    total1.value = res.data.total;
  })
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
      county.value = response.result.ad_info.district

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

  console.log('搜索地址：', province.value + city.value + county.value + address.value)
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
  county.value = value[2]
  if (city.value == '市辖区') {
    city.value = province.value
  }
}
const handleClose = (index) => {
  tags.splice(index, 1);
}
const handleInputConfirm = () => {
  tags.push(inputValue.value)
  inputValue.value = ''
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

span {
  font-size: 14px;
  color: #303133;
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
</style>
