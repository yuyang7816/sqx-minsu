<template>
  <el-dialog align-center v-model="props.modelValue" width="600px" :title="props.roleId ? '修改角色' : '添加角色'" draggable
    class="editDataDialog" @close="closeDialog">
    <el-form :model="form" :rules="rules" ref="roleFormRef" label-position="right" label-width="100">
      <el-form-item prop="roleName" label="角色名称">
        <el-input v-model="form.roleName" placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item prop="remark" label="角色描述">
        <el-input v-model="form.remark" placeholder="请输入角色描述" />
      </el-form-item>
      <el-form-item prop="menu" label="菜单权限">
        <el-tree style="width: 100%;" ref="menuListTree" v-loading="loading" :data="roleList" node-key="menuId"
          default-expand-all :default-checked-keys="form.menuIdList" :props="menuListTreeProps" show-checkbox />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="saveRole(roleFormRef)">
          提交
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import {
  ref,
  reactive,
  defineProps,
  defineEmits,
  onMounted,
  getCurrentInstance,
} from 'vue';
import {
  treeDataTranslate
} from '/comment/utils.js'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {
  ElMessageBox,
  ElMessage
} from 'element-plus';
const {
  proxy
} = getCurrentInstance();
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  //角色id
  roleId: {
    type: String || Number,
    default: ''
  }
});
const emit = defineEmits(['update:modelValue', 'refresh', 'cleanRole'])
const form = ref({
  roleId: '',
  roleName: '', //角色名称
  remark: '', //角色描述
  menuIdList: [],
});
const rules = {
  roleName: [{
    required: true,
    message: '请输入角色名称',
    trigger: 'blur'
  }],
}
const menuListTreeProps = ref({
  label: 'name',
  value: 'menuId',
  children: 'children'
});
//全部权限列表
const roleList = reactive([]);
const roleFormRef = ref(null);
const loading = ref(false);
const menuListTree = ref(null);
const tempKey = ref(-666666);
onMounted(() => {
  getAllRoleList();
});
//提交/修改权限
const saveRole = async (roleFormRef) => {
  if (!roleFormRef) return
  await roleFormRef.validate((valid, fields) => {
    if (valid) { //校验通过，提交
      console.log('submit!')
      let url = '';
      if (form.value.roleId) { //修改
        url = '/sys/role/update'
      } else { //新建
        url = '/sys/role/save'
      }
      ElMessageBox.confirm(
        form.value.roleId ? '确认修改吗？' : '确认提交吗？',
        '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true
            instance.confirmButtonText = form.value.roleId ? '修改中...' :
              '提交中...'
            let data = form.value;
            data.menuIdList = [].concat(
              menuListTree.value.getCheckedKeys(),
              [tempKey.value],
              menuListTree.value.getHalfCheckedKeys()
            )
            proxy.$Request.post(url, form.value, {
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
        closeDialog()
        emit('refresh')
        ElMessage({
          type: 'success',
          message: form.value.roleId ? '已修改' : '已提交'
        });

      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消',
        })
      })
    } else {
      console.log('error submit!', fields)
    }
  })
};
//获取全部权限列表
const getAllRoleList = () => {
  roleList.splice(0, roleList.length)
  loading.value = true
  proxy.$Request.get('/sys/menu/list').then(res => {
    loading.value = false
    roleList.splice(0, roleList.length, ...treeDataTranslate(res, 'menuId'));
    menuListTree.value.getCheckedKeys(false)
    if (props.roleId) {
      selectInfo(props.roleId)
    }
  })
};
//获取当前角色的权限
const selectInfo = (id) => {
  proxy.$Request.get('/sys/role/info/' + id).then(res => {
    if (res.code == 0) {
      form.value.roleName = res.data.role.roleName
      form.value.remark = res.data.role.remark
      form.value.roleId = res.data.role.roleId
      const idx = res.data.role.menuIdList.indexOf(tempKey.value)
      if (idx !== -1) {
        res.data.role.menuIdList.splice(idx, res.data.role.menuIdList.length - idx)
      }
      menuListTree.value.setCheckedKeys(res.data.role.menuIdList)
    }
  })
};
//提交菜单
const saveMenu = async (menuFormRef) => {
  if (!menuFormRef) return
  console.log(menuFormRef, '222')
  await menuFormRef.validate((valid, fields) => {
    if (valid) { //校验通过，提交
      console.log('submit!')
      let url = '';
      if (form.value.menuId) { //修改
        url = '/sys/menu/update'
      } else { //新建
        url = '/sys/menu/save'
      }
      ElMessageBox.confirm(
        form.value.menuId ? '确认修改吗？' : '确认提交吗？',
        '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true
            instance.confirmButtonText = form.value.menuId ? '修改中...' :
              '提交中...'
            proxy.$Request.post(url, form.value, {
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
        closeDialog()
        emit('refresh')
        ElMessage({
          type: 'success',
          message: form.value.menuId ? '已修改' : '已提交'
        });

      }).catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消',
        })
      })
    } else {
      console.log('error submit!', fields)
    }
  })
};

//关闭弹窗
const closeDialog = () => {
  emit('update:modelValue', false)
  emit('cleanRole')
};
</script>
<style lang="scss">
.dialogDesk {
  border-radius: 20px;
}
</style>
<style lang="scss" scoped>
.iconLists {
  width: 100%;
  padding: 20px 0;

  .iconLists-item:hover {
    color: var(--el-color-primary);
  }

  .iconLists-item {
    cursor: pointer;
  }
}
</style>
