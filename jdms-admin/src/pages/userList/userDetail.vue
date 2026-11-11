<template>
  <div class="page-container">
    <div style="display: inline-block;font-size:18px;margin: 10px;">
      <a href="#" @click="prev" style="text-decoration:none;font-size: 14px;">
        返回
      </a>
      <span style="display: inline-block;margin: 0 15px;color: #D9D9D9;">|</span>
      <span>用户详情</span>
    </div>
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="用户详情" name="first">
        <div class="detailtabel">
          <div class="table_main">
            <table>
              <tbody>
                <tr>
                  <th>累计提现</th>
                  <td>
                    {{ tableData.totalCashMoney }}

                  </td>
                  <th>可提现金额</th>
                  <td style="color: rgb(245, 108, 108)">
                    {{ tableData.canCashMoney ? `${tableData.canCashMoney}元` : '0元' }}
                    <el-button size="small" style="margin-left: 10px;" @click="rechargenone()">修改金额</el-button>
                  </td>
                  <th>钱包金额</th>
                  <td class="border-rt">
                    {{ tableData.userMoney ? `${tableData.userMoney}元` : '0元' }}
                    <!-- <el-button size="small" style="margin-left: 10px;" @click="rechargenone()">修改金额</el-button> -->
                  </td>
                  <!-- <th>总收入</th>
                  <td class="border-rt">
                    {{ tableData.balance ? tableData.balance : '0元' }}</td> -->
                </tr>
                <tr>

                  <th>收益</th>
                  <td style="color: rgb(245, 108, 108)">
                    {{ tableData.balance ? `${tableData.balance}元` : '0元' }}

                  </td>
                  <th>一级佣金比例</th>
                  <td>
                    {{ tableData.zhiRate ? tableData.zhiRate : '-' }}
                    <el-button size="small" style="margin-left: 10px;" @click="ageChangeU()">修改</el-button>
                  </td>
                  <th>二级佣金比例</th>
                  <td class="border-rt">
                    {{ tableData.feiRate ? tableData.feiRate : '-' }}
                  </td>
                  <!-- <th>总收入</th>
                  <td class="border-rt">
                    {{ tableData.balance ? tableData.balance : '0元' }}</td> -->
                </tr>
                <tr>
                  <th>邀请人邀请码</th>
                  <td>
                    {{ tableData.inviterCode }}
                    <el-button size="small" style="margin-left: 10px;" @click="ageChangeU()">修改</el-button>
                  </td>

                  <th>收款二维码</th>
                  <td>
                    <div style="display:flex;flex-wrap: wrap;">
                      <div style="margin: 2px;" v-if="tableData.wxImg">
                        <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                          <template #reference>
                            <img style="width: 50px; height: 50px;margin: 3px;" :src="tableData.wxImg" alt="">
                          </template>
                          <template #default>
                            <img style="width: 400px; height: auto;" :src="tableData.wxImg" alt="">
                          </template>
                        </el-popover>

                      </div>
                    </div>
                  </td>
                  <th>图像</th>
                  <td class="border-rt">
                    <img v-if="tableData.avatar" :src="tableData.avatar" width="80" height="80" />
                    <img v-else src="../../assets/logo.png" alt="" width="80" height="80">

                    <el-button size="small" style="margin-left: 10px;" @click="ageChangeU()">修改</el-button>
                  </td>
                </tr>
                <tr>
                  <th>微信名称</th>
                  <td>
                    {{ tableData.userName }}
                    <el-button size="small" style="margin-left: 10px;" @click="ageChangeU()">修改</el-button>
                  </td>
                  <th>手机号</th>
                  <td>
                    <span v-if="tableData.phone">
                      {{ tableData.phone ? tableData.phone : '未绑定' }}
                    </span>

                    <el-button size="small" style="margin-left: 10px;" @click="ageChangeU()">修改</el-button>
                  </td>
                  <th>支付宝姓名</th>
                  <td class="border-rt">
                    {{ tableData.zhiFuBaoName ? tableData.zhiFuBaoName : '未绑定' }}
                  </td>
                </tr>
                <tr>
                  <th>邀请码</th>
                  <td>
                    {{ tableData.invitationCode }}
                    <el-button size="small" style="margin-left: 10px;" @click="ageChangeU()">修改</el-button>
                  </td>
                  <th>支付宝账号</th>
                  <td>
                    <span v-if="tableData.zhiFuBao">
                      {{ tableData.zhiFuBao ? tableData.zhiFuBao : '未绑定' }}
                    </span>
                  </td>
                  <th>用户状态</th>
                  <td class=" border-rt">
                    <span v-if="tableData.status == 1">正常</span>
                    <span v-if="tableData.status == 2" style="color: #f56c6c;">禁用</span>
                    <el-button size="small" style="margin-left: 10px;" @click="stateChange(tableData)">更改状态</el-button>
                  </td>
                </tr>
                <tr>
                  <th>用户类型</th>
                  <td>
                    <span v-if="tableData.userType == 1">用户</span>
                    <span v-else-if="tableData.userType == 2">员工</span>
                  </td>
                  <th>性别</th>
                  <td>
                    <span v-if="tableData.sex == 1">男</span>
                    <span v-if="tableData.sex == 2">女</span>
                    <el-button size="small" style="margin-left: 10px;" @click="ageChangeU()">修改</el-button>
                  </td>
                  <th>年龄</th>
                  <td class="border-rt">
                    <span v-if="tableData.age">{{ tableData.age }}</span>
                    <span v-else>未设置</span>
                    <el-button size="small" style="margin-left: 10px;" @click="ageChangeU()">修改</el-button>
                  </td>
                </tr>
                <tr>
                  <th>渠道来源</th>
                  <td>
                    <span>{{ tableData.platform }}</span>
                  </td>
                  <th>是否是会员</th>
                  <td>
                    <span v-if="tableData.isVip == 1">是</span>
                    <span v-else>否</span>
                  </td>
                  <th>会员到期时间</th>
                  <td class="border-rt">

                    {{ tableData.isVip == 1 ? tableData.expireTime : '-' }}
                  </td>
                </tr>
                <tr>
                  <th class="border-bt">创建时间</th>
                  <td class="border-bt"> {{ tableData.createTime }}</td>
                  <th class="border-bt"></th>
                  <td class="border-bt"></td>
                  <th class="border-bt"></th>
                  <td class="border-bt border-rt"></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="钱包明细" name="second">
        <el-table :data="walletData" v-loading="tableDataLoading" style="width: 100%">
          <el-table-column prop="id" label="编号" width="100"></el-table-column>
          <el-table-column prop="title" label="标题"></el-table-column>
          <el-table-column prop="content" label="内容"></el-table-column>
          <el-table-column prop="money" label="金额" width="100">
            <template #default="scope">
              <span style="color: #f56c6c;" v-if="scope.row.type == 2">- {{ scope.row.money }}</span>
              <span style="color: #006600;" v-if="scope.row.type == 1">+ {{ scope.row.money }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        </el-table>
        <!-- <div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeQbmx" @current-change="handleCurrentChangeQbmx"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="dataContainer.limit"  :current-page="dataContainer.page"
						layout="total, sizes, prev, pager, next, jumper" :total="dataContainer.walletData.total">
					</el-pagination>
				</div> -->
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="walletSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="提现记录" name="third">
        <el-table v-loading="tableDataLoading" :data="withdrawData">
          <el-table-column prop="id" label="编号" width="100" fixed="left"></el-table-column>
          <el-table-column prop="accountName" label="支付宝名称" width="150">
            <template #default="scope">
              <span>{{ scope.row.accountName ? scope.row.accountName : '暂无' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="accountNumber" label="支付宝账号" width="100">
            <template #default="scope">
              <span v-if="scope.row.type == 1">{{ scope.row.accountNumber }}</span>
              <span v-else> - </span>
            </template>
          </el-table-column>
          <el-table-column prop="accountNumber" label="收款二维码" width="150">
            <template #default="scope">
              <div v-if="scope.row.type == 2 && scope.row.accountNumber" style="display:flex;flex-wrap: wrap;">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:220px;">
                  <template #reference>
                    <img style="width: 50px; height: 50px" :src="scope.row.accountNumber" alt="">
                  </template>
                  <template #default>
                    <img style="width: 200px; height: auto" :src="scope.row.accountNumber" alt="">
                  </template>
                </el-popover>
              </div>
              <div v-else>
                暂无图片
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="提现金额" width="100"></el-table-column>
          <el-table-column prop="outAmount" label="实际到账金额" width="100"></el-table-column>
          <el-table-column prop="serviceFee" label="平台服务费" width="100"></el-table-column>
          <el-table-column prop="refund" label="审核说明" width="100"></el-table-column>

          <el-table-column prop="type" label="提现方式" width="100">
            <template #default="scope">
              <span v-if="scope.row.type == 1">支付宝</span>
              <span v-else>微信</span>
            </template>
          </el-table-column>
          <el-table-column prop="state" label="状态" width="100" fixed="right">
            <template #default="scope">
              <span v-if="scope.row.state == 0">待转账</span>
              <span v-if="scope.row.state == 1">转账成功</span>
              <span v-if="scope.row.state == -1">拒绝转账</span>
              <!-- <el-switch v-model="scope.row.status" class="ml-2" :active-value="dataContainer.active"
                                :inactive-value="dataContainer.inactive" @change="changeBtn(scope.row)"
                                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" /> -->

            </template>
          </el-table-column>
          <el-table-column prop="outTime" label="转账时间" width="180"></el-table-column>
          <el-table-column prop="createTime" label="申请时间" width="180"></el-table-column>
        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="withdrawSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="支付明细" name="fourth">
        <div style="display: inline-block;">
          <span>开始时间：</span>
          <el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right" type="date"
            format="YYYY-MM-DD" date-format="MMM DD, YYYY" placeholder="选择开始时间">
          </el-date-picker>&nbsp;&nbsp;&nbsp;
          <span>截止时间：</span>
          <el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="date"
            format="YYYY-MM-DD" date-format="MMM DD, YYYY" placeholder="选择截止时间">
          </el-date-picker>
          <el-button style="margin-left:15px;" size="small" type="primary" icon="document"
            @click="timeDate2">查询</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="rechargeData" style="width: 100%">
          <el-table-column prop="id" label="编号" width="100" fixed="left"></el-table-column>

          <!-- <el-table-column prop="orderNo" label="订单编号"></el-table-column> -->
          <el-table-column prop="amount" label="支付金额"></el-table-column>

          <el-table-column prop="platform" label="支付方式" width="100">
            <template #default="scope">
              <span v-if="scope.row.platform == 1">微信</span>
              <span v-if="scope.row.platform == 2">支付宝</span>
              <span v-if="scope.row.platform == 3">paypal支付</span>
              <span v-if="scope.row.platform == 4">stripe支付</span>
              <span v-if="scope.row.platform == 5">余额</span>
            </template>
          </el-table-column>
          <el-table-column prop="state" label="状态" width="100" fixed="right">
            <template #default="scope">
              <span v-if="scope.row.state == 0">待支付</span>
              <span v-if="scope.row.state == 1">支付成功</span>
              <span v-if="scope.row.state == 2">支付失败</span>
            </template>
          </el-table-column>
          <el-table-column prop="payTime" label="支付时间"></el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="rechargeSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="我的订单" name="five">
        <el-table v-loading="tableDataLoading" :data="dongtaiData" style="width: 100%">
          <el-table-column prop="orderId" align="center" label="编号" width="60" fixed="left" />
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
        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="dongtaiSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="我的优惠券" name="six">
        <el-table v-loading="tableDataLoading" :data="friendData">
          <el-table-column fixed="left" prop="id" label="编号" width="80"></el-table-column>
          <el-table-column prop="couponName" label="优惠券名称" width="120"></el-table-column>
          <el-table-column prop="couponPicture" label="优惠券图片" width="120">
            <template #default="scope">
              <img :src="scope.row.couponPicture" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="money" label="优惠券金额"></el-table-column>
          <el-table-column prop="minMoney" label="最低消费金额"></el-table-column>
          <el-table-column prop="validDays" label="有效天数"></el-table-column>
          <el-table-column prop="expirationTime" label="优惠券过期时间"></el-table-column>
          <el-table-column prop="useTime" label="优惠券使用时间"></el-table-column>
          <el-table-column prop="createTime" label="领取时间"></el-table-column>
          <el-table-column prop="status" label="使用状态">
            <template #default="scope">
              <div v-if="scope.row.status == 1">已使用</div>
              <div v-if="scope.row.status == 2">已失效</div>
              <div v-if="scope.row.status == 0">待使用</div>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>

        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="haoyouSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="我的评论" name="seven">
        <el-table v-loading="tableDataLoading" :data="qunzuData">
          <el-table-column prop="evaluateId" label="编号" width="80">
          </el-table-column>
          <el-table-column prop="shopName" label="评论酒店" width="160">
          </el-table-column>
          <el-table-column prop="titleImg" label="酒店图片" width="120">
            <template #default="scope">
              <div v-if="scope.row.titleImg">
                <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;"
                  v-for="(item, index) in scope.row.titleImg.split(',')" :key="index">
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
              <el-rate v-model="scope.row.satisfactionFlag" disabled show-score text-color="#ff9900"
                score-template="" />
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
      <el-tab-pane label="我的团队" name="nine">
        <div style="display: inline-block;margin: 5px;">
          <span>等级：</span>
          <el-select v-model="typeC" class="m-2" placeholder="请选择等级：" style="width: 150px;" @change="qingqiuSelect">
            <el-option v-for="(item, index) in typeData2" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </div>
        <el-table v-loading="tableDataLoading" :data="tousuData">
          <el-table-column prop="userId" label="编号" width="100"></el-table-column>
          <el-table-column prop="userName" label="用户昵称">
            <template #default="scope">
              <span v-if="scope.row.userName" style="color: #13ce66;cursor: pointer;"
                @click="updates(scope.row.senderUserId)">
                {{ scope.row.userName ? scope.row.userName : '未绑定' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="avatar" label="用户头像">
            <template #default="scope">
              <img :src="scope.row.avatar" style="width: 60px;height: 60px;" />
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间">
          </el-table-column>
        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="qingqiuSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="我的发票" name="scdongtai">
        <el-table v-loading="tableDataLoading" :data="friendData">
          <el-table-column prop="id" label="编号" width="80" fixed="left">
          </el-table-column>
          <el-table-column prop="type" label="类型">
            <template #default="scope">
              <span v-if="scope.row.type == 1">个人</span>
              <span v-if="scope.row.type == 2">公司</span>
            </template>
          </el-table-column>
          <el-table-column prop="indentId" label="开票用户">
            <template #default="scope">
              <div style="color: #4f9dec;cursor: pointer;" v-if="scope.row.publisherName"
                @click="updates(scope.row.userId)">
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
          <!-- <el-table-column prop="amount" label="金额">
          </el-table-column> -->
          <el-table-column prop="status" label="状态" fixed="right">
            <template #default="scope">
              <span v-if="scope.row.status == 1">待开票</span>
              <span v-if="scope.row.status == 2">已开票</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="170">
          </el-table-column>

        </el-table>
        <el-row class="el-rows">
          <el-col :span="24">
            <div class="topBtn flex align-center justify-end">
              <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="fapiaoSelect()" />
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <!-- <el-tab-pane label="聊天记录" name="ten">
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="id" label="编号" width="100">
            <template #default="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="senderUserName" label="用户昵称">
            <template #default="scope">
              <div v-if="scope.row.senderUserName" style="color: #13ce66;cursor: pointer;"
                @click="updates(scope.row.senderUserId)">
                {{ scope.row.senderUserName ? scope.row.senderUserName : '未绑定' }}
              </div>
              <div v-if="scope.row.receiverUserName" style="color: #13ce66;cursor: pointer;"
                @click="updates(scope.row.receiverUserId)">
                {{ scope.row.receiverUserName ? scope.row.receiverUserName : '未绑定' }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="senderAvatar" label="用户头像">
            <template #default="scope">
              <img v-if="scope.row.senderAvatar" :src="scope.row.senderAvatar"
                style="width: 60px;height: 60px;margin: 3px;" />
              <img v-if="scope.row.receiverAvatar" :src="scope.row.receiverAvatar"
                style="width: 60px;height: 60px;margin: 3px;" />
            </template>
          </el-table-column>
          <el-table-column prop="messageCount" label="信息数量" width="180"></el-table-column>

          <el-table-column prop="lastMessageTime" label="创建时间" width="180"></el-table-column>
          <el-table-column fixed='right' label="操作" width="150">
            <template #default="scope">
              <el-button type="primary" @click="complainDetails(scope.row)">聊天内容
              </el-button>
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
    <!-- 修改用户信息 弹框 -->
    <el-dialog v-model="dialogFormVisibleU" title="用户信息" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">微信名称：</span>
        <el-input style="width:50%;" v-model="userNameU" type="text" placeholder="请输入微信名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
        <el-input style="width:50%;" v-model="phoneU" type="text" placeholder="请输入手机号"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">邀请人邀请码：</span>
        <el-input style="width:50%;" v-model="inviterCodeU" type="text" placeholder="请输入邀请人邀请码"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">邀请码：</span>
        <el-input style="width:50%;" v-model="invitationCodeU" type="text" placeholder="请输入邀请码"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">年龄：</span>
        <el-input style="width:50%;" v-model="ageU" type="number" :min="0" :controls="false"
          placeholder="请输入年龄"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">性别：</span>
        <el-radio-group v-model="sexU" class="ml-4">
          <el-radio :label="1" size="large">男</el-radio>
          <el-radio :label="2" size="large">女</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">一级佣金比例：</span>
        <el-input style="width:50%;" v-model="zhiRateU" type="number" :min="0" :controls="false"
          placeholder="输入佣金比例"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">二级佣金比例：</span>
        <el-input style="width:50%;" v-model="feiRateU" type="number" :min="0" :controls="false"
          placeholder="输入佣金比例"></el-input>
      </div>
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">个性签名</span>
        <el-input style="width:50%;" v-model="msgU" type="text" placeholder="请输入个性签名"></el-input>
      </div> -->
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">图像：</span>
        <div style="display: inline-block;">
          <el-upload class="avatar-uploader" v-model="avatarU"
            action="https://jdmsadmin.xianmaxiong.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess1">
            <img v-if="avatarU" :src="avatarU" class="avatar" style="width: 148px;height: 148px;" />
            <el-icon v-else style="font-size: 28px;color: #D9D9D9">
              <Plus />
            </el-icon>

            <!-- <i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i> -->
          </el-upload>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 动态评论 弹框 -->
    <el-dialog v-model="dialogTableVisible2" title="动态评论" center width="80%">
      <el-table v-loading="tableDataLoading1" :data="pinglunDatas.records">
        <el-table-column prop="commentId" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="nickName" label="用户昵称">
          <template #default="scope">
            <div style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">
              {{ scope.row.nickName ? scope.row.nickName : '未绑定' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="imageUrl" label="用户头像">
          <template #default="scope">
            <div v-if="scope.row.imageUrl == null || scope.row.imageUrl == ''">
              暂无图片
            </div>
            <div v-else>
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">
                <template #reference>
                  <img style="width: 50px; height: 50px;margin: 3px;" :src="scope.row.imageUrl" alt="">
                </template>
                <template #default>
                  <img style="width: 400px; height: auto;" :src="scope.row.imageUrl" alt="">
                </template>
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="信息内容" width="280">
          <template #default="scope">
            <div>{{ scope.row.content }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150">
        </el-table-column>
      </el-table>

    </el-dialog>
    <!-- 聊天内容 -->
    <el-dialog title="聊天内容" v-model="dialogFormVisible4" center width="70%">
      <el-table v-loading="tableDataLoading1" :data="huihuaData">
        <el-table-column prop="id" label="编号" width="100">
          <template #default="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="senderUserName" label="用户昵称">
          <template #default="scope">
            <span v-if="scope.row.senderUserName" style="color: #13ce66;cursor: pointer;"
              @click="updates(scope.row.senderUserId)">
              {{ scope.row.senderUserName ? scope.row.senderUserName : '未绑定' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="senderAvatar" label="用户头像">
          <template #default="scope">
            <img v-if="scope.row.senderAvatar" :src="scope.row.senderAvatar" style="width: 60px;height: 60px;" />
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
        <el-table-column prop="messageCount" label="聊天内容">
          <template #default="scope">
            <span v-if="scope.row.type == 1">{{ scope.row.messageCount }}</span>
            <span v-if="scope.row.type == 2">
              <img style="width: 20px; height: 20px;margin: 3px;"
                :src="'https://im.xianmaxiong.com/emoji/' + scope.row.contemessageCountnt" alt="">
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
        <el-table-column prop="lastMessageTime" label="创建时间" width="180"></el-table-column>
      </el-table>
      <el-row class="el-rows">
        <el-col :span="24">
          <div class="topBtn flex align-center justify-end">
            <pagination :pageSize="limit1" v-model:currentPage="page1" :total="total1" @pageChange="neirongSelect()" />
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <!-- 修改钱包  -->
    <el-dialog title="修改钱包" v-model="dialogFormVisible1" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
        <el-radio-group v-model="type">
          <el-radio label="1">添加</el-radio>
          <el-radio label="2">减少</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
        <el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
          placeholder="请输入金额"></el-input>
      </div>
      <template #footer>
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
      </template>
    </el-dialog>
    <!-- 点赞列表 -->
    <el-dialog v-model="dialogFormVisibleDz" title="点赞列表" width="80%" center>
      <el-table :data="tableDataDz" v-loading="loading" style="width: 100%">
        <el-table-column prop="userId" label="编号" width="100"></el-table-column>
        <el-table-column prop="userName" label="点赞用户">
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
        <el-table-column prop="createTime" label="点赞时间" width="180"></el-table-column>
      </el-table>
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
    <!--群成员 弹框 -->
    <el-dialog v-model="dialogFormVisibleCy" title="群成员列表" width="80%" center>
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
        <el-button size="small" type="primary" plain @click="clearSearchCy()">重置</el-button>
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
      </el-table>

    </el-dialog>
    <!--申请信息 弹框 -->
    <el-dialog v-model="dialogFormVisibleSq" title="申请信息" width="80%" center>
      <div style="margin: 10px;">
        <div style="display: inline-block;margin: 5px;">
          <span>用户昵称：</span>
          <el-input v-model="userNameC" placeholder="请输入用户昵称" style="width: 150px;" />
        </div>
        <!-- @keydown.enter.native="handleSearchSq" -->
        <div style="display: inline-block;margin: 5px;">
          <span>手机号：</span>
          <el-input v-model="phoneC" placeholder="请输入手机号" style="width: 150px;" />
        </div>
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
          <span>用户昵称：</span>
          <!-- @keydown.enter.native="handleSearchLt" -->
          <el-input v-model="userNameC" placeholder="请输入用户昵称" style="width: 150px;" />
        </div>
        <div style="display: inline-block;margin: 5px;">
          <span>手机号：</span>
          <el-input v-model="phoneC" placeholder="请输入手机号" style="width: 150px;" />
        </div>
        <div style="display: inline-block;margin: 5px;">
          <span>内容类型：</span>
          <el-select v-model="typeC" class="m-2" placeholder="请选择内容类型" style="width: 150px;" @change="handleSearchLt">
            <el-option v-for="(item, index) in typeData" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </div>
        <div style="display: inline-block;margin: 5px;">
          <span>聊天内容：</span>
          <el-input v-model="contentC" placeholder="请输入聊天内容" style="width: 150px;" />
        </div>

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
  </div>
</template>

<script setup>
import {
  ref,
  reactive,
  getCurrentInstance,
  onMounted
} from 'vue';
import { useRouter, useRoute } from 'vue-router';
import pagination from '@/components/pagination/pagination.vue'
import {
  ElMessageBox,
  ElMessage
} from 'element-plus';
import qrcode from 'qrcode';
const {
  proxy
} = getCurrentInstance();
const route = useRoute();
const router = useRouter();
const activeName = ref('first'); //默认选中
const userList = reactive([]);//列表集合
const tableData = ref({}); //用户详情
const walletData = reactive([]); //钱包明细 集合
const withdrawData = reactive([]); //提现记录 集合
const rechargeData = reactive([]); //支付明细 集合
const dongtaiData = reactive([]); //我的订单 集合
const friendData = reactive([]); //我的优惠券 集合
const qunzuData = reactive([]); //我的U礼卡 集合
const tousuData = reactive([]); //好友请求 集合
const liaotianData = reactive([]); //聊天记录 集合
const huihuaData = reactive([]); //聊天内容 集合
const tableDataDz = reactive([]); //点赞 集合
const tableDataPl = reactive([]); //评论 集合
const tableDataCy = reactive([]); //群成员 集合
const tableDataLt = reactive([]); //群聊天 集合



const page = ref(1);//当前页
const limit = ref(10);//每页条数
const total = ref(0);//总条数
const page1 = ref(1);//当前页
const limit1 = ref(10);//每页条数
const total1 = ref(0);//总条数

//列表是否加载中
const dialogFormVisibleU = ref(false);
const dialogFormVisible4 = ref(false);
const dialogFormVisibleDz = ref(false);
const dialogFormVisiblePl = ref(false);
const dialogFormVisibleCy = ref(false);
const dialogFormVisibleLt = ref(false);
const tableDataLoading = ref(false);
const tableDataLoading1 = ref(false);
const loadingCy = ref(false);
const startTime = ref('');
const endTime = ref('');
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
const stas = ref(1);




//聊天类型 集合
const typeData = reactive([
  {
    label: '全部',
    value: ''
  },
  {
    label: '普通消息',
    value: 1
  },
  {
    label: '表情',
    value: 2
  },
  {
    label: '图片',
    value: 3
  },
  {
    label: '视频',
    value: 4
  }
]);
const typeData2 = reactive([

  {
    label: '一级',
    value: 1
  },
  {
    label: '二级',
    value: 2
  },

]);



// 修改用户信息
const loading = ref(false);
const userNameU = ref('');
const phoneU = ref('');
const inviterCodeU = ref('');
const invitationCodeU = ref('');
const avatarU = ref('');
const ageU = ref('');
const sexU = ref('');
const zhiRateU = ref('');
const feiRateU = ref('');
const msgU = ref('');
// 修改钱包弹框的
const dialogFormVisible1 = ref(false);
const type = ref('');
const money = ref('');


onMounted(() => {
  getUserList();
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
//获取用户详情
const getUserList = () => {
  loading.value = true
  const userId = route.query.userId
  userList.splice(0, userList.length)
  proxy.$Request.get('/user/selectUserById', {
    params: {
      userId: userId,
    }
  }, {
    type: 'json'
  }).then(res => {
    // console.log(res)
    loading.value = false;
    if (res.code == 0) {
      let returnData = res.data
      tableData.value = returnData
    }
  })
};
// 切换tab 事件
const handleClick = (tab, event) => {
  console.log(event.srcElement.innerText)
  typeC.value = ''
  if (event.srcElement.innerText == '用户详情') {
    getUserList();
  }
  if (event.srcElement.innerText == '钱包明细') {
    walletSelect();
  }
  if (event.srcElement.innerText == '提现记录') {
    withdrawSelect();
  }
  if (event.srcElement.innerText == '支付明细') {
    rechargeSelect();
  }
  if (event.srcElement.innerText == '我的订单') {
    dongtaiSelect();
  }
  if (event.srcElement.innerText == '我的优惠券') {
    haoyouSelect();
  }
  if (event.srcElement.innerText == '我的评论') {
    qunzuSelect();
  }
  if (event.srcElement.innerText == '我的消息') {
    tousuSelect();
  }
  if (event.srcElement.innerText == '我的团队') {
    typeC.value = 1
    qingqiuSelect();
  }
  if (event.srcElement.innerText == '我的发票') {
    fapiaoSelect();
  }
  if (event.srcElement.innerText == '聊天记录') {
    liaotianSelect();
  }
}
// 钱包明细 列表
const walletSelect = () => {
  tableDataLoading.value = true
  walletData.splice(0, walletData.length)
  let userId = route.query.userId
  proxy.$Request.get('/admin/moneyDetails/getMoneyDetailsList', {
    params: {
      userId: userId,
      page: page.value,
      limit: limit.value,

    }
  }, {}).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    walletData.splice(0, walletData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
// 提现记录 列表
const withdrawSelect = () => {
  tableDataLoading.value = true
  withdrawData.splice(0, withdrawData.length)
  let userId = route.query.userId
  proxy.$Request.get('/cash/selectPayDetails', {
    params: {
      userId: userId,
      page: page.value,
      limit: limit.value,

    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    withdrawData.splice(0, withdrawData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
// 支付明细 列表
const rechargeSelect = () => {
  tableDataLoading.value = true
  rechargeData.splice(0, rechargeData.length)
  let userId = route.query.userId
  proxy.$Request.get('/cash/selectUserRecharge', {
    params: {
      userId: userId,
      page: page.value,
      limit: limit.value,
      startTime: startTime.value,
      endTime: endTime.value,

    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    rechargeData.splice(0, rechargeData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
const timeDate2 = () => {
  page.value = 1
  rechargeSelect()
}
// 我的订单 列表
const dongtaiSelect = () => {
  tableDataLoading.value = true
  dongtaiData.splice(0, dongtaiData.length)
  let userId = route.query.userId
  proxy.$Request.get('/admin/orders/getOrderList', {
    params: {
      page: page.value,
      limit: limit.value,
      userName: '',
      phone: '',
      orderNo: '',
      shopName: '',
      hairdresserName: '',
      itemName: '',
      type: '',
      status: '',
      startTime: '',
      endTime: '',
      userId: userId,


    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    dongtaiData.splice(0, dongtaiData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
/** 点赞用户 弹框 */
const dianzanChange = (row) => {

  tableDataDz.splice(0, tableDataDz.length, ...row.imPostsLikeDataList)
  dialogFormVisibleDz.value = true

}
/** 评论用户 弹框 */
const pinglunChange = (row) => {

  tableDataPl.splice(0, tableDataPl.length, ...row.imPostsCommentDataList)
  dialogFormVisiblePl.value = true
}

// 我的优惠券 列表
const haoyouSelect = () => {
  tableDataLoading.value = true
  friendData.splice(0, friendData.length)
  let userId = route.query.userId
  proxy.$Request.get('/admin/couponUser/getMyCouponList', {
    params: {
      page: page.value,
      limit: limit.value,
      name: '',
      status: '',
      userId: userId,
    }
  }, {
    // type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    friendData.splice(0, friendData.length, ...res.data.records)
    total.value = res.data.total;
  })

}
// 我的评论 列表
const qunzuSelect = () => {
  tableDataLoading.value = true
  qunzuData.splice(0, qunzuData.length)
  let userId = route.query.userId
  proxy.$Request.get('/admin/evaluate/getUserEvaluate', {
    params: {
      userId: userId,
      page: page.value,
      limit: limit.value,

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
/** 群成员 弹框 */
const cyChange = (row) => {
  id.value = row.id
  userNameC.value = ''
  phoneC.value = ''
  dialogFormVisibleCy.value = true
  dataSelectCy();

}
/** 群成员 数据 */
const dataSelectCy = () => {
  loadingCy.value = true
  tableDataCy.splice(0, tableDataCy.length)
  proxy.$Request.get('admin/group/groupUserList', {
    params: {
      groupId: id.value,
      userName: userNameC.value,
      phone: phoneC.value,

    }
  }, {}).then((res) => {
    console.log(res)
    loadingCy.value = false;
    tableDataCy.splice(0, tableDataCy.length, ...res.data)
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
/** 聊天记录 弹框 */
const ltChange = (row) => {
  id.value = row.id
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
      groupId: id.value,
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




// 我的消息 列表
const tousuSelect = () => {
  tableDataLoading.value = true
  tousuData.splice(0, tousuData.length)
  let userId = route.query.userId
  proxy.$Request.post('/admin/messageInfo/page', {
    userId: userId,
    page: page.value,
    limit: limit.value,
    type: 1,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    tousuData.splice(0, tousuData.length, ...res.data.records)
    total.value = res.data.total;
  })


}
// 我的团队 列表
const qingqiuSelect = () => {
  tableDataLoading.value = true
  tousuData.splice(0, tousuData.length)
  let userId = route.query.userId
  proxy.$Request.get('admin/invite/getExtensionList', {
    params: {
      userId: userId,
      page: page.value,
      limit: limit.value,
      type: typeC.value,

    }
  }, {}).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    tousuData.splice(0, tousuData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
// 我的发票 列表
const fapiaoSelect = () => {
  tableDataLoading.value = true
  friendData.splice(0, friendData.length)
  let userId = route.query.userId
  proxy.$Request.post('/admin/invoice/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    userId: userId,

    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    friendData.splice(0, friendData.length, ...res.data.records)
    total.value = res.data.total;
  })

}
// 聊天记录 列表
const liaotianSelect = () => {
  tableDataLoading.value = true
  liaotianData.splice(0, tousuData.length)
  let userId = route.query.userId
  proxy.$Request.post('/admin/history/chatHistory', {
    userId: userId,
    page: page.value,
    limit: limit.value,
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    liaotianData.splice(0, liaotianData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
//         /**  聊天内容*/
const complainDetails = (row) => {


  page1.value = 1
  dialogFormVisible4.value = true
  receiverUserId.value = row.receiverUserId
  senderUserId.value = row.senderUserId
  neirongSelect()
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

// 修改钱包
const rechargenone = () => {
  type.value = ''
  money.value = ''
  dialogFormVisible1.value = true
}
const StairNoticeTo1 = () => {
  let userId = route.query.userId
  if (type.value == '') {
    ElMessage({
      message: '请选择修改类型',
      type: 'warning',
      onClose: () => {
      }
    })

    return
  }
  if (money.value == '') {
    ElMessage({
      message: '请输入金额',
      type: 'warning',
      onClose: () => {
      }
    })
    return
  }
  proxy.$Request.post('/admin/userMoney/updateUserMoney', {
    // params: {
    userId: userId,
    money: money.value,
    type: type.value
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
          getUserList()
          dialogFormVisible1.value = false
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
/** 修改信息弹框 */
const ageChangeU = () => {
  userNameU.value = tableData.value.userName
  phoneU.value = tableData.value.phone
  inviterCodeU.value = tableData.value.inviterCode
  invitationCodeU.value = tableData.value.invitationCode
  avatarU.value = tableData.value.avatar
  ageU.value = tableData.value.age
  sexU.value = tableData.value.sex
  zhiRateU.value = tableData.value.zhiRate
  feiRateU.value = tableData.value.feiRate
  // msgU.value = tableData.value.signature
  dialogFormVisibleU.value = true
  console.log(avatarU.value)
}
/** 修改性别 */
const StairNoticeToU = () => {
  let userId = route.query.userId
  proxy.$Request.post('/user/updateUserByUserId', {
    userId: userId,
    inviterCode: inviterCodeU.value,
    avatar: avatarU.value,
    userName: userNameU.value,
    phone: phoneU.value,
    invitationCode: invitationCodeU.value,
    age: ageU.value,
    sex: sexU.value,
    zhiRate: zhiRateU.value,
    feiRate: feiRateU.value,
    // signature: msgU.value,
  }, { type: 'json' }).then((res) => {
    console.log(res)
    loading.value = false;
    if (res.code == 0) {
      ElMessage({
        message: '修改成功',
        type: 'success',
        onClose: () => {
          dialogFormVisibleU.value = false
          getUserList()
        }
      })
    } else {
      ElMessage({
        message: res.msg,
        type: 'warning',
        onClose: () => {
          getUserList()
        }
      })
    }
  })
}
/** 更改状态 */
const stateChange = (val) => {

  if (val.status == 1) {
    stas.value = 2

  } else {
    stas.value = 1

  }
  ElMessageBox.confirm(`确定要更改用户状态吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    proxy.$Request.get('/user/updateUserStatusByUserId', {
      params: {
        userId: val.userId,
        // status: stas.value,
      }
    }, {
      // type: 'json'
    }).then((res) => {
      if (res.code == 0) {
        ElMessage({
          message: '修改成功',
          type: 'success',
          onClose: () => {
            getUserList()
          }
        })
      } else {
        ElMessage({
          message: res.msg,
          type: 'warning',
          onClose: () => {
            getUserList()
          }
        })
      }
    })

  })
}
function handleAvatarSuccess1(file) {
  avatarU.value = file.data
}
// 返回上一页
const prev = () => {
  router.back()
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
</style>
