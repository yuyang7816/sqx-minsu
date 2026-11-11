<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="发型师列表" name="first">
        <div style="margin: 0 20px;">
          <div style="display: inline-block;margin: 5px;">
            <span>发型师名称 ：</span>
            <el-input v-model="hairdresserNameT" placeholder="请输入发型师名称" style="width: 150px;"
              @keyup.enter.native="handleSearch()" />
          </div>&nbsp;&nbsp;
          <!-- <div style="display: inline-block;margin: 5px;">
            <span>店铺名称：</span>
            <el-input v-model="nameT" placeholder="请输入店铺名称" style="width: 150px;" />
          </div> -->
          <div style="display: inline-block;margin: 5px;">
            <span>状态 ：</span>
            <el-select v-model="statusT" class="m-2" placeholder="请选择状态" style="width: 150px;" @change="handleSearch">
              <el-option v-for="(item, index) in statusData" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </div>&nbsp;&nbsp;
          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="primary" @click="clearSearch()">重置</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="id" label="编号" width="80" fixed="left">
          </el-table-column>
          <el-table-column prop="name" label="发型师名称" width="120" fixed="left">
          </el-table-column>
          <el-table-column prop="shopName" label="店铺名称" width="160">
          </el-table-column>
          <el-table-column prop="phone" label="手机号" width="160">
          </el-table-column>
          <el-table-column prop="tag" label="标签">
          </el-table-column>
          <el-table-column prop="img" label="展示图片" width="300">
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
          <el-table-column prop="description" label="个人简介" width="300">
            <template #default="scope">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                <template #reference>
                  <div
                    style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
                    {{ scope.row.description }}</div>
                </template>
                <template #default>
                  <div style="width: 400px;">{{ scope.row.description }}</div>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="appointmentCount" label="预约次数">
          </el-table-column>
          <el-table-column prop="productionCount" label="作品数">
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
          </el-table-column>
          <el-table-column prop="status" label="状态" fixed="right">
            <template #default="scope">
              <span v-if="scope.row.status == 0">正常</span>
              <span v-if="scope.row.status == 1">请假</span>
              <span v-if="scope.row.status == 2">禁用</span>
              <span v-if="scope.row.status == 3" style="color: red;">踢出</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" plain @click="zpChangeU(scope.row)"
                style="margin: 5px;">作品管理</el-button>
              <el-button size="small" type="primary" @click="addChangeU(scope.row)" style="margin: 5px;">修改</el-button>
              <el-button size="small" type="danger" plain @click="fwChangeU(scope.row)"
                style="margin: 5px;">服务项目</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)" style="margin: 5px;">删除</el-button>
              <el-button size="small" type="warning" plain @click="plChangeU(scope.row)"
                style="margin: 5px;">评论列表</el-button>
              <el-button size="small" type="warning" plain @click="msChangeU(scope.row)"
                style="margin: 5px;">忙时</el-button>
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
        <span style="width: 200px;display: inline-block;text-align: right;">店铺 ：</span>
        <el-input style="width:50%;" v-model="shopName" type="text" @click="userbtn()" placeholder="请选择店铺"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">发型师名称 ：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入发型师名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="width: 200px;display: inline-block;text-align: right;">发型师展示图片 ：</span>
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
        <span style="width: 200px;display: inline-block;text-align: right;position: relative;bottom: 70px;">发型师简介
          ：</span>
        <el-input style="width:50%;" v-model="description" type="textarea" :rows="4" placeholder="请输入发型师简介"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">标签 ：</span>
        <el-tag v-for="(item, index) in tag" :key="index" class="mx-1" closable :disable-transitions="false"
          @close="handleClose(index)" style="margin: 3px;">
          {{ item }}
        </el-tag>
        <el-input v-model="inputValue" class="ml-1 w-20" size="small" style="width: 150px;margin: 3px;" />
        <el-button class="button-new-tag ml-1" size="small" @click="handleInputConfirm()" style="margin: 3px;">
          + 添加
        </el-button>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 店铺列表 -->
    <el-dialog v-model="dialogFormVisibleDp" title="店铺列表" width="70%" center>
      <div style="margin: 10px;text-align: right;">
        <el-button size="small" class="btn-refresh" @click="handleSearchDp()">刷新</el-button>
      </div>
      <el-table :data="tableDataCy" v-loading="loadingCy" style="width: 100%">
        <el-table-column prop="shopId" label="编号" width="80" fixed="left"></el-table-column>
        <el-table-column prop="userId" label="用户昵称" width="160">
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
        <el-table-column prop="status" label="状态" width="100" fixed="right">
          <template #default="scope">
            <span v-if="scope.row.status === 0">未营业</span>
            <span v-if="scope.row.status == 1">营业中</span>
            <span v-if="scope.row.status == 2">待审核</span>
            <span v-if="scope.row.status == 3">审核拒绝</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button size="small" type="danger" @click="xuanzeChangeDp(scope.row)" style="margin: 5px;">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="shopSelect()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 作品列表 -->
    <el-dialog v-model="dialogFormVisibleZp" title="作品列表" width="80%" center>
      <div style="margin: 10px;text-align: right;">
        <el-button size="small" class="btn-refresh" @click="handleSearchZp()">刷新</el-button>
        <el-button size="small" type="primary" @click="clearSearchZp()">添加</el-button>
      </div>
      <el-table :data="tableDataCy" v-loading="loadingCy" style="width: 100%">
        <el-table-column prop="id" label="编号" width="100"></el-table-column>
        <el-table-column prop="title" label="作品名称" width="200"></el-table-column>
        <el-table-column prop="img" label="图片">
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
        <el-table-column prop="sort" label="排序" width="80"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="clearSearchZp(scope.row)">修改</el-button>
            <el-button size="small" type="danger" @click="deleteChangeZp(scope.row)" style="margin: 0;">删除</el-button>

          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectZp()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 服务列表 -->
    <el-dialog v-model="dialogFormVisibleFw" title="服务列表" width="80%" center>
      <div style="margin: 10px;text-align: right;">
        <el-button size="small" class="btn-refresh" @click="handleSearchFw()">刷新</el-button>
        <el-button size="small" type="primary" @click="clearSearchFw()">添加服务</el-button>
      </div>
      <el-table :data="tableDataCy" v-loading="loadingCy" style="width: 100%">
        <el-table-column prop="id" label="编号" width="100"></el-table-column>
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
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button size="small" type="danger" @click="deleteChangeFw(scope.row)" style="margin: 5px;">删除</el-button>

          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectFw()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 全部服务 列表 -->
    <el-dialog v-model="dialogFormVisibleFwArr" title="全部服务" width="80%" center>
      <div style="margin: 10px;text-align: right;">
        <el-button size="small" class="btn-refresh" @click="handleSearchFwArr()">刷新</el-button>
      </div>
      <el-table :data="tableDataFw" v-loading="loadingFw" style="width: 100%">
        <el-table-column prop="id" label="编号" width="100"></el-table-column>
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
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="addChangeFw(scope.row)" style="margin: 5px;">确认添加</el-button>

          </template>
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectFw()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 评论列表 -->
    <el-dialog v-model="dialogFormVisiblePl" title="评论列表" width="80%" center>
      <div style="margin: 10px;text-align: right;">
        <el-button size="small" class="btn-refresh" @click="handleSearchPl()">刷新</el-button>
      </div>
      <el-table :data="tableDataCy" v-loading="loadingCy" style="width: 100%">
        <el-table-column prop="id" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="userName" label="评论用户" width="120">
          <template #default="scope">
            <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">{{
              scope.row.userName ? scope.row.userName : '未绑定' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="avatar" label="用户头像" width="120">
          <template #default="scope">
            <div v-if="scope.row.avatar">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                v-for="(item, index) in scope.row.avatar.split(',')" :key="index">
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
        <el-table-column prop="orderNo" label="订单编号" width="160">
        </el-table-column>
        <el-table-column prop="content" label="内容">
        </el-table-column>
        <el-table-column prop="img" label="评论图片" width="260">
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

        <el-table-column prop="rate" label="评分" width="160">
          <template #default="scope">
            <el-rate v-model="scope.row.rate" disabled show-score text-color="#ff9900" score-template="" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
        </el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="dataSelectPl()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 添加 修改 作品 -->
    <el-dialog v-model="dialogFormVisibleZpadd" :title="titles" width="70%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">作品标题 ：</span>
        <el-input style="width:50%;" v-model="name" type="text" placeholder="请输入作品标题"></el-input>
      </div>
      <div style="margin-bottom: 10px;display: flex;align-items: center;">
        <span style="width: 200px;display: inline-block;text-align: right;">作品图片 ：</span>
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
        <span style="width: 200px;display: inline-block;text-align: right;">排序 ：</span>
        <el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入排序"></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleZpadd = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToZp" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 忙时数据 -->
    <el-dialog v-model="dialogFormVisibleMs" title="忙时数据" width="80%" center>
      <el-tabs v-model="activeNameMs" type="card" class="demo-tabs" @tab-click="handleClickMs">
        <el-tab-pane v-for="(item, index) in tableDataCy" :key="index" :label="item.date" :name="'first' + index">
          <span v-for="(item1, index1) in item.timeSlots" :key="index1" style="margin: 5px;display: inline-block;">
            <el-tag v-if="item1.busy == false" type="info" class="ml-2" style="font-size: 15px;padding: 5px;">{{
              item1.startTime
            }}--{{
                item1.endTime
              }}</el-tag>
            <el-tag v-else class="ml-2" type="danger" style="font-size: 15px;padding: 5px;">{{ item1.startTime }}--{{
              item1.endTime }}</el-tag>

          </span>

        </el-tab-pane>

      </el-tabs>
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
const activeNameMs = ref('first0');
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
const shopId = ref('');//店铺ID
const shopName = ref('');//店铺名称
const name = ref('');//发型师名称
const img = reactive([]); //发型师展示图片
const description = ref('');//发型师简介
const tag = reactive([]);//标签
const inputValue = ref('');
const percentage2 = ref(0);
const productionList = reactive([]);//作品列表
const appointmentTime = reactive([]);//服务列表
const dialogFormVisibleZp = ref(false);
const dialogFormVisibleFw = ref(false);
const loadingCy = ref(false);
const page1 = ref(1);//当前页
const limit1 = ref(10);//每页条数
const total1 = ref(0);//总条数
const tableDataCy = reactive([]); // 二级集合
const hairdresserId = ref('');
const tableDataFw = reactive([]); // 服务列表
const page2 = ref(1);//当前页
const limit2 = ref(10);//每页条数
const total2 = ref(0);//总条数
const loadingFw = ref(false);
const dialogFormVisibleFwArr = ref(false);
const dialogFormVisiblePl = ref(false);
const dialogFormVisibleDp = ref(false);
const dialogFormVisibleZpadd = ref(false);
const sort = ref('');
const dialogFormVisibleMs = ref(false);
const hairdresserNameT = ref('');
const statusT = ref('');
// 状态类型
const statusData = reactive([ //0: 正常 1: 请假 2: 禁用 3:踢出
  {
    label: '全部',
    value: ''
  },
  {
    label: '正常',
    value: 0
  },
  {
    label: '请假',
    value: 1
  },
  {
    label: '禁用',
    value: 2
  },
  {
    label: '踢出',
    value: 3
  }
]);


onMounted(() => {
  liaotianSelect();
});

// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '发型师列表') {
    classify.value = 1
    liaotianSelect();
  }
}


//分类数据 列表
const liaotianSelect = () => {
  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.post('/admin/hairdresser/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    shopId: '',
    hairdresserName: hairdresserNameT.value,
    status: statusT.value,
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
  hairdresserNameT.value = ''
  statusT.value = ''
  liaotianSelect()
}
const handleClose = (index) => {
  tag.splice(index, 1);
}
const handleInputConfirm = () => {
  tag.push(inputValue.value)
  inputValue.value = ''
}
// 添加/修改 分类 弹框
const addChangeU = (row) => {
  if (row) {
    titles.value = '修改'
    id.value = row.id
    shopId.value = row.shopId
    shopName.value = row.shopName
    name.value = row.name
    description.value = row.description
    inputValue.value = ''
    percentage2.value = 0
    if (row.img) {
      img.splice(0, img.length, ...row.img.split(','))
    } else {
      img.value = []
    }
    if (row.tag) {
      tag.splice(0, tag.length, ...row.tag.split(','))
    } else {
      tag.value = []
    }
  } else {
    titles.value = '添加'
    id.value = ''
    shopId.value = ''
    shopName.value = ''
    name.value = ''
    description.value = ''
    tag.value = []
    img.value = []
    inputValue.value = ''
    percentage2.value = 0

  }
  percentage2.value = 0
  dialogFormVisibleU.value = true
}

const StairNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/hairdresser/save'
  } else {
    routeUrl.value = '/admin/hairdresser/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    shopId: shopId.value,
    name: name.value,
    description: description.value,
    tag: tag.toString(),
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
  proxy.$Request.post('/admin/hairdresser/delete', {
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
const onprogress2 = (event) => {
  percentage2.value = parseInt(event.percent)
}
// 作品 弹框
const zpChangeU = (row) => {
  hairdresserId.value = row.id
  shopId.value = row.shopId
  dialogFormVisibleZp.value = true
  dataSelectZp();
}
/** 作品列表 数据 */
const dataSelectZp = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.post('/admin/production/page', {
    // params: {
    hairdresserId: hairdresserId.value,
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
const handleSearchZp = () => {
  dataSelectZp();
}
// 添加、修改作品弹框
const clearSearchZp = (row) => {
  if (row) {
    titles.value = '修改作品'

    id.value = row.id
    name.value = row.title
    sort.value = row.sort
    percentage2.value = 0
    if (row.img) {
      img.splice(0, img.length, ...row.img.split(','))
    } else {
      img.value = []
    }
  } else {
    titles.value = '添加作品'
    id.value = ''
    name.value = ''
    img.value = []
    percentage2.value = 0
    sort.value = ''


  }
  dialogFormVisibleZpadd.value = true;
}
const StairNoticeToZp = () => {
  if (titles.value == '添加作品') {
    routeUrl.value = '/admin/production/save'
  } else {
    routeUrl.value = '/admin/production/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: id.value,
    shopId: shopId.value,
    hairdresserId: hairdresserId.value,
    title: name.value,
    img: img.toString(),
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
          dialogFormVisibleZpadd.value = false
          dataSelectZp()
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
// 删除作品
const deleteChangeZp = (row) => {
  proxy.$Request.post('/admin/production/delete', {
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
          dataSelectZp()
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

// 服务 弹框
const fwChangeU = (row) => {
  hairdresserId.value = row.id
  appointmentTime.splice(0, appointmentTime.length, ...row.appointmentTime)
  dialogFormVisibleFw.value = true
  dataSelectFw();
}
/** 服务列表 数据 */
const dataSelectFw = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.post('/admin/hairdresserItem/page', {
    // params: {
    hairdresserId: hairdresserId.value,
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
// 添加服务 弹框
const clearSearchFw = () => {
  page2.value = 1
  dialogFormVisibleFwArr.value = true
  dataSelectFwArr();
}
/** 全部服务列表 数据 */
const dataSelectFwArr = () => {
  loadingFw.value = true
  tableDataFw.splice(0, tableDataFw.length)
  proxy.$Request.post('/admin/serviceItem/page', {
    // params: {
    page: page2.value,
    limit: limit2.value,

    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    loadingFw.value = false;
    tableDataFw.splice(0, tableDataFw.length, ...res.data.records)
    total2.value = res.data.total;
  })
}
const handleSearchFwArr = () => {
  page2.value = 1
  dataSelectFwArr();
}
// 确认添加 服务
const addChangeFw = (row) => {
  proxy.$Request.post('/admin/hairdresserItem/save', {
    itemId: row.id,
    hairdresserId: hairdresserId.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleFwArr.value = false
          dataSelectFw()
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
// 删除 服务
const deleteChangeFw = (row) => {
  proxy.$Request.post('/admin/hairdresserItem/delete', {
    id: row.id,
    hairdresserId: hairdresserId.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    if (res.code == 0) {
      ElMessage({
        message: '操作成功',
        type: 'success',
        onClose: () => {
          dataSelectFw()
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


// 店铺 弹框
const userbtn = () => {
  page1.value = 1
  dialogFormVisibleDp.value = true
  shopSelect()
}
// 店铺列表 列表
const shopSelect = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.post('/admin/shopInfo/page', {
    page: page1.value,
    limit: limit1.value,
    // contactName: userNameT.value,
    // typeName: inviteVerifyT.value,
    // status: statusT.value,
    // name: nameT.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataCy.splice(0, tableDataCy.length, ...res.data.records)
    total1.value = res.data.total;
  })
}
// 刷新
const handleSearchDp = () => {
  page1.value = 1
  shopSelect()
}
// 重置
// const clearSearch = () => {
//   page.value = 1
//   userNameT.value = ''
//   inviteVerifyT.value = ''
//   statusT.value = ''
//   nameT.value = ''
//   qunzuSelect()
// }
// 选择店铺
const xuanzeChangeDp = (row) => {
  shopId.value = row.shopId
  shopName.value = row.name
  dialogFormVisibleDp.value = false
}
// 评论列表 弹框
const plChangeU = (row) => {
  hairdresserId.value = row.id
  appointmentTime.splice(0, appointmentTime.length, ...row.appointmentTime)
  dialogFormVisiblePl.value = true
  dataSelectPl();
}
/** 服务列表 数据 */
const dataSelectPl = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.post('/admin/evaluate/page', {
    // params: {
    hairdresserId: hairdresserId.value,
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
const handleSearchPl = () => {
  dataSelectPl();
}
// 忙时 弹框
const msChangeU = (row) => {
  hairdresserId.value = row.id
  dialogFormVisibleMs.value = true
  dataSelectMs();
}
/** 忙时 数据 */
const dataSelectMs = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.post('/admin/hairdresser/busyHour', {
    // params: {
    hairdresserId: hairdresserId.value,
    // page: page1.value,
    // limit: limit1.value,

    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    // loadingCy.value = false;
    tableDataCy.splice(0, tableDataCy.length, ...res.data.dayBusyInfos)
    // total1.value = res.data.total;
  })
}
// 忙时切换tab 事件
const handleClickMs = (tab, event) => {
  if (event.srcElement.innerText == '发型师列表') {
    // classify.value = 1
    // liaotianSelect();
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
