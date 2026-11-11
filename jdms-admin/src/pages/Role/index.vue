<template>
  <div class="Role">
    <el-row>
      <el-col :span="24">
        <div class="topBtn flex align-center">
          <el-input v-model="roleName" @keydown.enter.prevent="getSearch" style="width: 240px" placeholder="请输入角色名称" />
          <el-button style="margin-left: 15px;" @click="getSearch">查询</el-button>
          <el-button type="primary" @click="isShowEditData = true"
            :disabled="!proxy.isAuth('sys:role:save')">新增</el-button>
          <el-button @click="reset">重置</el-button>
          <el-button type="danger" :disabled="multipleSelection.length == 0" @click="deleteUserAll">批量删除</el-button>
        </div>
      </el-col>
    </el-row>
    <el-row class="el-rows">
      <el-col :span="24">
        <div class="Role-list">
          <el-table ref="multipleTableRef" @cell-dblclick="handleCopyVale" :data="roleList"
            @selection-change="handleSelectionChange" border v-loading="loading">
            <el-table-column type="selection" align="center" width="55" />
            <el-table-column prop="roleId" align="center" width="100" header-align="center" label="编号" />
            <el-table-column prop="roleName" align="center" label="角色名称" />
            <el-table-column prop="remark" align="center" label="备注" />
            <el-table-column prop="createTime" min-width="150" align="center" label="创建时间" />

            <el-table-column fixed="right" align="center" label="操作" min-width="100">
              <template #default="scope">
                <el-button type="primary" size="small" @click="updataRole(scope.row)">
                  修改
                </el-button>
                <el-button type="danger" size="small" @click="deleteUser(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <el-row class="el-rows">
      <el-col :span="24">
        <div class="topBtn flex align-center justify-end">
          <pagination :pageSize="limit" v-model:currentPage="page" :total="total" @pageChange="getRoleList()" />
        </div>
      </el-col>
    </el-row>
    <!-- 添加/修改菜单 -->
    <EditDataDialog v-model="isShowEditData" v-if="isShowEditData" @cleanRole="cleanRole" :roleId="roleId"
      @refresh="getRoleList" />
  </div>
</template>

<script setup>
import {
  reactive,
  ref,
  getCurrentInstance,
  onMounted
} from 'vue';
import {
  ElMessageBox,
  ElMessage
} from 'element-plus';
import pagination from '@/components/pagination/pagination.vue'
import EditDataDialog from './components/editDataDialog.vue';
const {
  proxy
} = getCurrentInstance();
//搜索名称
const roleName = ref('');
//列表是否加载中
const loading = ref(false);
//角色列表
const roleList = reactive([]);
//当前页
const page = ref(1);
//每页条数
const limit = ref(10);
//总条数
const total = ref(0);
const multipleSelection = ref([]); //选择的数据
const multipleTableRef = ref(null);
const roleId = ref('');
const isShowEditData = ref(false); //是否显示新增/修改弹窗
onMounted(() => {
  getRoleList();
});
//打开修改弹窗
const updataRole = (item) => {
  console.log(item.roleId)
  roleId.value = item.roleId;
  isShowEditData.value = true
};
//批量删除
const deleteUserAll = () => {
  ElMessageBox.confirm(
    '确认删除角色吗？',
    '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
    beforeClose: (action, instance, done) => {
      if (action === 'confirm') {
        instance.confirmButtonLoading = true
        instance.confirmButtonText = '删除中...';
        let data = []
        multipleSelection.value.map(item => {
          data.push(item.roleId)
        })
        proxy.$Request.post('/sys/role/delete', data, {
          type: 'json'
        }).then(res => {
          if (res.code == 0) {
            done()
            instance.confirmButtonLoading = false
          } else {
            instance.confirmButtonText = '确认'
            instance.confirmButtonLoading = false
            ElMessageBox.alert(res.msg, '提示', {
              confirmButtonText: '确认',
            })
          }
        })
      } else {
        done()
      }
    },
  }
  ).then(() => {
    getRoleList();
    ElMessage({
      type: 'success',
      message: '已删除'
    });

  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消',
    })
  })
};
//删除角色(单个删除)
const deleteUser = (item) => {
  ElMessageBox.confirm(
    '确认删除角色吗？',
    '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
    beforeClose: (action, instance, done) => {
      if (action === 'confirm') {
        instance.confirmButtonLoading = true
        instance.confirmButtonText = '删除中...';
        let data = [item.roleId]
        proxy.$Request.post('/sys/role/delete', data, {
          type: 'json'
        }).then(res => {
          if (res.code == 0) {
            done()
            instance.confirmButtonLoading = false
          } else {
            instance.confirmButtonText = '确认'
            instance.confirmButtonLoading = false
            ElMessageBox.alert(res.msg, '提示', {
              confirmButtonText: '确认',
            })
          }
        })
      } else {
        done()
      }
    },
  }
  ).then(() => {
    getRoleList();
    ElMessage({
      type: 'success',
      message: '已删除'
    });

  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消',
    })
  })
};
//清空选择
const toggleSelection = (rows) => {
  if (rows && multipleTableRef.value) {
    rows.forEach((row) => {
      multipleTableRef.value.toggleRowSelection(row)
    })
  } else if (multipleTableRef.value) {
    multipleTableRef.value.clearSelection()
  }
};
//选中的回调
const handleSelectionChange = (val) => {
  // console.log(val);
  multipleSelection.value = val
};
//重置
const reset = () => {
  roleName.value = '';
  getSearch();
  toggleSelection();
};
//搜索
const getSearch = () => {
  page.value = 1;
  getRoleList();
};
//双击复制
const handleCopyVale = (_, __, ___, event) => {
  proxy.$copyText(event.target.innerText).then(() => {
    ElMessage({
      type: 'success',
      message: '复制成功，内容为：' + event.target.innerText
    });
  })
}
const cleanRole = () => {
  roleId.value = '';
};
//获取角色列表
const getRoleList = () => {
  let data = {
    page: page.value,
    limit: limit.value,
    roleName: roleName.value
  }
  loading.value = true;
  roleList.splice(0, roleList.length)
  proxy.$Request.get('/sys/role/list', {
    params: data
  }).then(res => {
    if (res.code == 0) {
      total.value = res.data.page.totalCount
      roleList.splice(0, roleList.length, ...res.data.page.list)
      loading.value = false
    }
  })
};
</script>

<style lang="scss" scoped>
.Role {
  width: 100%;
  height: 100%;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: var(--el-box-shadow-light);
  display: flow-root; // 创建块格式化上下文，防止外部浮动影响
}

.topBtn {
  width: 100%;
}

.Role-list {
  width: 100%;
}

.el-rows {
  margin-top: 20px;
}
</style>
