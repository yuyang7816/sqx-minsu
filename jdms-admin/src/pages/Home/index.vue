<template>
  <div class="dashboard">
    <!-- 页面标题与筛选 -->
    <div class="page-header">
      <div class="page-title">经营数据总览</div>
      <div class="filter-bar">
        <el-radio-group v-model="quickDate" size="small" @change="handleQuickDateChange">
          <el-radio-button label="today">今日</el-radio-button>
          <el-radio-button label="last7">近7日</el-radio-button>
          <el-radio-button label="last30">近30日</el-radio-button>
          <el-radio-button label="month">本月</el-radio-button>
          <el-radio-button label="custom">自定义</el-radio-button>
        </el-radio-group>
        <el-date-picker v-if="quickDate === 'custom'" v-model="chartDateRange" type="daterange" range-separator="至"
          start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" format="YYYY-MM-DD"
          size="small" style="width: 240px; margin-left: 12px;" @change="handleDateRangeChange" />
        <el-button size="small" :icon="Refresh" style="margin-left: 12px;" @click="refreshData">刷新数据</el-button>
        <el-button size="small" :icon="Download" type="primary" @click="exportReport">导出报表</el-button>
      </div>
    </div>

    <!-- 核心指标卡 -->
    <el-row :gutter="20" style="margin-top: 20px;" v-loading="menuLoading">
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card stat-card-primary">
          <div class="stat-icon">
            <el-icon :size="28" color="#2C8EFE">
              <Money />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ tableData.todayRevenue ? tableData.todayRevenue : 0 }}</div>
            <div class="stat-label">今日收入</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card stat-card-success">
          <div class="stat-icon">
            <el-icon :size="28" color="#10B981">
              <Document />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ tableData.todayOrderCount ? tableData.todayOrderCount : 0 }}</div>
            <div class="stat-label">今日订单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card stat-card-warning">
          <div class="stat-icon">
            <el-icon :size="28" color="#F59E0B">
              <User />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ tableData.newToday ? tableData.newToday : 0 }}</div>
            <div class="stat-label">今日新增</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card stat-card-danger">
          <div class="stat-icon">
            <el-icon :size="28" color="#EF4444">
              <Bell />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ pendingTotal }}</div>
            <div class="stat-label">待办事项</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 次要指标卡 -->
    <el-row :gutter="20" style="margin-top: 20px;" v-loading="menuLoading">
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card stat-card-simple">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.totalUsers ? tableData.totalUsers : 0 }}</div>
            <div class="stat-label">总用户数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card stat-card-simple">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.totalRevenue ? tableData.totalRevenue : 0 }}</div>
            <div class="stat-label">总收入</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card stat-card-simple">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.monthRevenue ? tableData.monthRevenue : 0 }}</div>
            <div class="stat-label">本月收入</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" class="stat-col">
        <el-card class="stat-card stat-card-simple">
          <div class="stat-content">
            <div class="stat-number">{{ tableData.yearRevenue ? tableData.yearRevenue : 0 }}</div>
            <div class="stat-label">本年收入</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;" v-loading="chartLoading">
      <el-col :span="24">
        <el-card class="chart-card">
          <div class="chart-title">订单与营收趋势</div>
          <div ref="trendChartRef" class="chart-container chart-lg"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;" v-loading="chartLoading">
      <el-col :span="8">
        <el-card class="chart-card">
          <div class="chart-title">用户增长趋势</div>
          <div ref="userChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <div class="chart-title">平台营收趋势</div>
          <div ref="platformChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <div class="chart-title">订单状态分布</div>
          <div ref="statusChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细分析 Tabs -->
    <div class="detail-section">
      <div class="detail-header">
        <el-select v-model="flag" style="width:150px;" @change="orderfenxi()">
          <el-option v-for="(item, index) in flags" :key="index" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <el-date-picker v-model="info" type="date" placeholder="选择时间" format="YYYY-MM-DD" date-format="MMM DD, YYYY"
          time-format="HH:mm" @change="orderfenxi()" style="margin-left: 12px;" />
      </div>
      <el-tabs v-model="activeName" @tab-click="handleClick" style="margin-left: 10px;">
        <el-tab-pane label="用户分析" name="first">
          <el-row :gutter="20" style="margin-top: 20px;" v-loading="menuLoadingUser">
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ userStat.totalNumber ? userStat.totalNumber : 0 }}</div>
                  <div class="stat-label">总用户数</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ userStat.userNumber ? userStat.userNumber : 0 }}</div>
                  <div class="stat-label">普通用户</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ userStat.vipUserNumber ? userStat.vipUserNumber : 0 }}</div>
                  <div class="stat-label">vip用户</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="支付统计" name="two">
          <el-row :gutter="20" style="margin-top: 20px;" v-loading="menuLoadingUser">
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ zongMoney.sumMoney ? zongMoney.sumMoney : 0 }}</div>
                  <div class="stat-label">支付总金额</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ zongMoney.weiXinGZHMoney ? zongMoney.weiXinGZHMoney : 0 }}</div>
                  <div class="stat-label">微信公众号支付金额</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ zongMoney.weiXinXCXMoney ? zongMoney.weiXinXCXMoney : 0 }}</div>
                  <div class="stat-label">微信小程序支付金额</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="订单统计" name="four">
          <el-row :gutter="20" style="margin-top: 20px;" v-loading="menuLoadingUser">
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumAllCount ? orderStat.sumAllCount : 0 }}</div>
                  <div class="stat-label">全部订单数</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumAllMoney ? orderStat.sumAllMoney : 0 }}</div>
                  <div class="stat-label">全部金额</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumDzfCount ? orderStat.sumDzfCount : 0 }}</div>
                  <div class="stat-label">待支付数量</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumDzfMoney ? orderStat.sumDzfMoney : 0 }}</div>
                  <div class="stat-label">待支付金额</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumDhxCount ? orderStat.sumDhxCount : 0 }}</div>
                  <div class="stat-label">待核销数量</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumDhxMoney ? orderStat.sumDhxMoney : 0 }}</div>
                  <div class="stat-label">待核销金额</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumJxzCount ? orderStat.sumJxzCount : 0 }}</div>
                  <div class="stat-label">进行中数量</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumJxzMoney ? orderStat.sumJxzMoney : 0 }}</div>
                  <div class="stat-label">进行中金额</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumYwcCount ? orderStat.sumYwcCount : 0 }}</div>
                  <div class="stat-label">已完成数量</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumYwcMoney ? orderStat.sumYwcMoney : 0 }}</div>
                  <div class="stat-label">已完成金额</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumYqxCount ? orderStat.sumYqxCount : 0 }}</div>
                  <div class="stat-label">已取消数量</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number">{{ orderStat.sumYqxMoney ? orderStat.sumYqxMoney : 0 }}</div>
                  <div class="stat-label">已取消金额</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane label="待审核统计" name="six">
          <el-row :gutter="20" style="margin-top: 20px;" v-loading="menuLoading">
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number" style="color: red;cursor: pointer;" @click="Btns(1)">{{ numberDt ? numberDt : 0
                  }}</div>
                  <div class="stat-label">提现待审核</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number" style="color: red;cursor: pointer;" @click="Btns(2)">{{ numberZx ? numberZx : 0
                  }}</div>
                  <div class="stat-label">注销待审核</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6" class="stat-col">
              <el-card class="stat-card stat-card-simple">
                <div class="stat-content">
                  <div class="stat-number" style="color: red;cursor: pointer;" @click="Btns(3)">{{ numberSh ? numberSh : 0
                  }}</div>
                  <div class="stat-label">入驻待审核</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import {
  reactive,
  ref,
  getCurrentInstance,
  onMounted,
  onUnmounted,
  computed,
  nextTick
} from 'vue';
import {
  ElMessage
} from 'element-plus';
import { useRouter } from 'vue-router';
import * as echarts from 'echarts';
import { Money, Document, User, Bell, Refresh, Download } from '@element-plus/icons-vue';
const router = useRouter();
const {
  proxy
} = getCurrentInstance();

// 数据加载状态
const menuLoading = ref(false);
const menuLoadingUser = ref(false);
const chartLoading = ref(false);

// 用户统计
const tableData = ref({});
// 用户分析
const userStat = ref({});
// 订单统计
const orderStat = ref({});
// 支付统计
const zongMoney = ref({});
// 支付统计合集
const withdrawDataZf = reactive([])
// 绑定当前时间
const info = ref(null);
// 首次选择的绑定值
const activeName = ref('first');
// 时间类型
const flag = ref('1');
// 时间类型集合
const flags = reactive([
  {
    value: '1',
    label: '按天查询'
  }, {
    value: '2',
    label: '按月查询'
  }, {
    value: '3',
    label: '按年查询'
  }
]);
const numberDt = ref(0);//提现
const numberZx = ref(0);//注销
const numberSh = ref(0);//商户
const numberFxs = ref(0);//发型师

// 图表相关
const trendChartRef = ref(null);
const userChartRef = ref(null);
const platformChartRef = ref(null);
const statusChartRef = ref(null);
let trendChart = null;
let userChart = null;
let platformChart = null;
let statusChart = null;

const quickDate = ref('last7');
const chartDateRange = ref([]);
const chartData = ref({
  dates: [],
  orderCountList: [],
  orderAmountList: [],
  newUserCountList: [],
  platformIncomeList: [],
  statusDistribution: []
});

const pendingTotal = computed(() => {
  const dt = Number(sessionStorage.getItem('numberDt') || 0);
  const zx = Number(sessionStorage.getItem('numberZx') || 0);
  const sh = Number(sessionStorage.getItem('numberSh') || 0);
  return dt + zx + sh;
});

// 日期格式化
const formatDate = (date) => {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

// 计算日期范围
const computeDateRange = (type) => {
  const today = new Date();
  let start = new Date();
  let end = new Date();
  switch (type) {
    case 'today':
      start = today;
      end = today;
      break;
    case 'last7':
      start = new Date(today.getTime() - 6 * 24 * 60 * 60 * 1000);
      end = today;
      break;
    case 'last30':
      start = new Date(today.getTime() - 29 * 24 * 60 * 60 * 1000);
      end = today;
      break;
    case 'month':
      start = new Date(today.getFullYear(), today.getMonth(), 1);
      end = today;
      break;
    default:
      return;
  }
  chartDateRange.value = [formatDate(start), formatDate(end)];
};

// 快捷日期切换
const handleQuickDateChange = (val) => {
  if (val !== 'custom') {
    computeDateRange(val);
    getChartData();
  }
};

// 自定义日期切换
const handleDateRangeChange = () => {
  if (chartDateRange.value && chartDateRange.value.length === 2) {
    quickDate.value = 'custom';
    getChartData();
  }
};

// 刷新数据
const refreshData = () => {
  if (quickDate.value !== 'custom') {
    computeDateRange(quickDate.value);
  }
  getMenuList();
  getChartData();
  userData();
};

// 导出报表
const exportReport = () => {
  ElMessage.success('导出功能开发中');
};

// 获取图表数据
const getChartData = () => {
  if (!chartDateRange.value || chartDateRange.value.length !== 2) {
    return;
  }
  chartLoading.value = true;
  proxy.$Request.get('/admin/dataCenter/overview', {
    params: {
      startTime: chartDateRange.value[0],
      endTime: chartDateRange.value[1]
    }
  }).then(res => {
    if (res.code == 0) {
      const trend = res.data.trend || {};
      chartData.value = {
        dates: trend.dates || [],
        orderCountList: trend.orderCountList || [],
        orderAmountList: trend.orderAmountList || [],
        newUserCountList: trend.newUserCountList || [],
        platformIncomeList: trend.platformIncomeList || [],
        statusDistribution: res.data.statusDistribution || []
      };
      nextTick(() => {
        renderCharts();
      });
    }
    chartLoading.value = false;
  }).catch(() => {
    chartLoading.value = false;
  });
};

// 初始化图表
const initCharts = () => {
  if (trendChartRef.value) trendChart = echarts.init(trendChartRef.value);
  if (userChartRef.value) userChart = echarts.init(userChartRef.value);
  if (platformChartRef.value) platformChart = echarts.init(platformChartRef.value);
  if (statusChartRef.value) statusChart = echarts.init(statusChartRef.value);
  window.addEventListener('resize', resizeCharts);
};

// 渲染图表
const renderCharts = () => {
  const dates = chartData.value.dates;
  if (!dates || dates.length === 0) return;

  const brandColor = '#3B8FE8';
  const successColor = '#10B981';
  const warningColor = '#F59E0B';
  const dangerColor = '#EF4444';
  const purpleColor = '#A78BFA';
  const chartColors = [brandColor, warningColor, successColor, dangerColor, purpleColor];

  // 订单与营收趋势
  const trendOption = {
    color: chartColors,
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#EEF1F6',
      textStyle: { color: '#334155' }
    },
    legend: {
      data: ['订单数', '营收金额'],
      right: 0,
      top: 0,
      textStyle: { color: '#94A3B8' }
    },
    grid: {
      left: '2%',
      right: '2%',
      bottom: '3%',
      top: '40px',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLine: { lineStyle: { color: '#334155' } },
      axisLabel: { color: '#94A3B8' }
    },
    yAxis: [
      {
        type: 'value',
        name: '订单数',
        position: 'left',
        axisLine: { show: false },
        axisLabel: { color: '#94A3B8' },
        splitLine: { lineStyle: { color: '#334155' } }
      },
      {
        type: 'value',
        name: '金额（元）',
        position: 'right',
        axisLine: { show: false },
        axisLabel: { color: '#94A3B8' },
        splitLine: { show: false }
      }
    ],
    series: [
      {
        name: '订单数',
        type: 'line',
        yAxisIndex: 0,
        smooth: true,
        showSymbol: false,
        data: chartData.value.orderCountList,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(44, 142, 254, 0.2)' },
            { offset: 1, color: 'rgba(44, 142, 254, 0.01)' }
          ])
        },
        lineStyle: { width: 3 }
      },
      {
        name: '营收金额',
        type: 'line',
        yAxisIndex: 1,
        smooth: true,
        showSymbol: false,
        data: chartData.value.orderAmountList,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(245, 158, 11, 0.2)' },
            { offset: 1, color: 'rgba(245, 158, 11, 0.01)' }
          ])
        },
        lineStyle: { width: 3 }
      }
    ]
  };

  // 用户增长趋势
  const userOption = {
    color: [successColor],
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#EEF1F6',
      textStyle: { color: '#334155' }
    },
    grid: {
      left: '2%',
      right: '4%',
      bottom: '3%',
      top: '30px',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLine: { lineStyle: { color: '#334155' } },
      axisLabel: { color: '#94A3B8' }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisLabel: { color: '#94A3B8' },
      splitLine: { lineStyle: { color: '#334155' } }
    },
    series: [
      {
        type: 'line',
        smooth: true,
        showSymbol: false,
        data: chartData.value.newUserCountList,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(16, 185, 129, 0.2)' },
            { offset: 1, color: 'rgba(16, 185, 129, 0.01)' }
          ])
        },
        lineStyle: { width: 3 }
      }
    ]
  };

  // 平台营收趋势
  const platformOption = {
    color: [purpleColor],
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#EEF1F6',
      textStyle: { color: '#334155' }
    },
    grid: {
      left: '2%',
      right: '4%',
      bottom: '3%',
      top: '30px',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLine: { lineStyle: { color: '#334155' } },
      axisLabel: { color: '#94A3B8' }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisLabel: { color: '#94A3B8' },
      splitLine: { lineStyle: { color: '#334155' } }
    },
    series: [
      {
        type: 'line',
        smooth: true,
        showSymbol: false,
        data: chartData.value.platformIncomeList,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(139, 92, 246, 0.2)' },
            { offset: 1, color: 'rgba(139, 92, 246, 0.01)' }
          ])
        },
        lineStyle: { width: 3 }
      }
    ]
  };

  // 订单状态分布
  const statusOption = {
    color: chartColors,
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#EEF1F6',
      textStyle: { color: '#334155' }
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'center',
      textStyle: { color: '#94A3B8' }
    },
    series: [
      {
        name: '订单状态',
        type: 'pie',
        radius: ['45%', '70%'],
        center: ['65%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 6,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold',
            color: '#334155'
          }
        },
        labelLine: { show: false },
        data: chartData.value.statusDistribution
      }
    ]
  };

  trendChart && trendChart.setOption(trendOption);
  userChart && userChart.setOption(userOption);
  platformChart && platformChart.setOption(platformOption);
  statusChart && statusChart.setOption(statusOption);
};

// 图表自适应
const resizeCharts = () => {
  trendChart && trendChart.resize();
  userChart && userChart.resize();
  platformChart && platformChart.resize();
  statusChart && statusChart.resize();
};

//  初始加载
onMounted(() => {
  info.value = getNowTime()
  computeDateRange('last7');
  getMenuList();
  getNowTime();
  userData();
  initCharts();
  getChartData();
});

// 页面卸载时释放图表
onUnmounted(() => {
  window.removeEventListener('resize', resizeCharts);
  trendChart && trendChart.dispose();
  userChart && userChart.dispose();
  platformChart && platformChart.dispose();
  statusChart && statusChart.dispose();
});

// 获取当前时间
const getNowTime = () => {
  const now = new Date();
  const year = now.getFullYear(); //得到年份
  const month = now.getMonth(); //得到月份
  const date = now.getDate(); //得到日期
  const month1 = month + 1;
  const month2 = month1.toString().padStart(2, "0");
  const date1 = date.toString().padStart(2, "0");
  var defaultDate = `${year}-${month2}-${date1}`;
  return defaultDate;
}
//获取用户统计
const getMenuList = () => {
  menuLoading.value = true
  proxy.$Request.get('/user/homeMessage').then(res => {
    tableData.value = res.data
    menuLoading.value = false
  })
};
// 获取 用户分析
const userData = () => {
  const datas = {
    time: info.value,
    flag: flag.value
  }
  menuLoadingUser.value = true
  proxy.$Request.get('/user/userMessage', {
    params: {
      date: info.value,
      type: flag.value
    }
  }, {
    // type: 'json',
  }).then(res => {
    if (res.code == 0) {
      userStat.value = res.data;
      console.log(userStat)
    }
    menuLoadingUser.value = false
  })

};
// 获取 支付统计
const rechSelect = () => {
  const datas = {
    findDate: info.value,
    findType: flag.value
  }
  menuLoadingUser.value = true
  withdrawDataZf.splice(0, withdrawDataZf.length)
  proxy.$Request.get('/cash/payMember', {
    params: {
      time: info.value,
      flag: flag.value,
      payClassify: '',
    }
  }, {
    type: 'json',
  }
  ).then(res => {
    if (res.code == 0) {
      withdrawDataZf.value = []
      zongMoney.value = res.data
    }
    menuLoadingUser.value = false
  })
}
// 订单统计
const orderSelect = () => {
  const datas = {
    params: {
      time: info.value,
      type: flag.value
    }
  }
  menuLoadingUser.value = true
  proxy.$Request.get('/admin/dataCenter/selectOrdersDate', datas, {
    // type: 'json',
  }
  ).then(res => {
    if (res.code == 0) {
      orderStat.value = res.data;
    }
    menuLoadingUser.value = false
  })
}
// 类型、时间筛选
const orderfenxi = () => {
  userData()
  rechSelect()
  orderSelect()
}
//tab 切换
const handleClick = (tab, event) => {
  console.log(event.srcElement.innerText)
  if (event.srcElement.innerText == '用户分析') {
    info.value = getNowTime()
    userData()
  }
  if (event.srcElement.innerText == '支付统计') {
    info.value = getNowTime()
    rechSelect()
  }
  if (event.srcElement.innerText == '订单统计') {
    info.value = getNowTime()
    orderSelect()
  }

  if (event.srcElement.innerText == '待审核统计') {
    numberDt.value = sessionStorage.getItem('numberDt')
    numberZx.value = sessionStorage.getItem('numberZx')
    numberSh.value = sessionStorage.getItem('numberSh')
    numberFxs.value = sessionStorage.getItem('numberFxs')
  }

};
const Btns = (index) => {
  if (index == 1) {
    router.push({
      name: 'financeList',
      query: {
        state: 0,
      },

    });
  }
  if (index == 2) {
    router.push({
      name: 'messageZx',
      query: {
        status: 1,
      },

    });
  }
  if (index == 3) {
    router.push({
      name: 'country',
      query: {
        status: 3,
      },

    });
  }
  if (index == 4) {
    router.push({
      name: 'country',
      query: {
        status: 1,
      },

    });
  }
}

</script>

<style lang="scss" scoped>
.dashboard {
  .page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 16px;
    padding-bottom: 8px;

    .page-title {
      font-size: 18px;
      font-weight: 600;
      color: var(--admin-text);
    }

    .filter-bar {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 8px;
    }
  }

  .stat-col {
    margin-bottom: 0;
  }

  .stat-card {
    text-align: left;
    border-radius: var(--admin-radius-lg);
    transition: transform 0.2s ease, box-shadow 0.2s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: var(--admin-shadow);
    }

    :deep(.el-card__body) {
      display: flex;
      align-items: center;
      padding: 20px;
    }

    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      flex-shrink: 0;
    }

    .stat-content {
      flex: 1;
      min-width: 0;

      .stat-number {
        font-size: 24px;
        font-weight: 700;
        color: var(--admin-text);
        line-height: 1.2;
      }

      .stat-label {
        margin-top: 6px;
        font-size: 13px;
        color: var(--admin-text-2);
      }
    }
  }

  .stat-card-primary {
    .stat-icon {
      background-color: var(--admin-brand-light);
    }
  }

  .stat-card-success {
    .stat-icon {
      background-color: #ECFDF5;
    }
  }

  .stat-card-warning {
    .stat-icon {
      background-color: #FFFBEB;
    }
  }

  .stat-card-danger {
    .stat-icon {
      background-color: #FEF2F2;
    }
  }

  .stat-card-simple {
    text-align: center;

    :deep(.el-card__body) {
      display: block;
      padding: 20px 0;
    }

    .stat-content {
      .stat-number {
        font-size: 22px;
        color: var(--admin-text);
      }

      .stat-label {
        margin-top: 8px;
        color: var(--admin-text-2);
      }
    }
  }

  .chart-card {
    border-radius: var(--admin-radius-lg);
    overflow: hidden;

    :deep(.el-card__body) {
      padding: 16px;
    }

    .chart-title {
      font-size: 15px;
      font-weight: 600;
      color: var(--admin-text);
      margin-bottom: 12px;
    }

    .chart-container {
      width: 100%;
      height: 260px;
    }

    .chart-lg {
      height: 320px;
    }
  }

  .detail-section {
    margin-top: 24px;
    padding-top: 20px;
    border-top: 1px solid var(--admin-border);

    .detail-header {
      display: flex;
      align-items: center;
      margin-bottom: 12px;
    }
  }
}
</style>
