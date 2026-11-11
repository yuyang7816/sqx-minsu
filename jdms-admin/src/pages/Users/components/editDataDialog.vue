<template>
  <el-dialog align-center v-model="props.modelValue" width="600px" :title="props.userId ? '修改角色' : '添加角色'" draggable
    class="editDataDialog" @close="closeDialog">
    <el-form :model="form" :rules="rules" ref="roleFormRef" label-position="right" label-width="100">
      <el-form-item prop="username" label="用户名">
        <el-input v-model="form.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item prop="password" label="密码">
        <el-input v-model="form.password" placeholder="请输入密码" type="password" />
      </el-form-item>
      <el-form-item prop="comfirmPassword" label="确认密码">
        <el-input v-model="form.comfirmPassword" placeholder="请输入确认密码" type="password" />
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="form.email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item prop="mobile" label="手机号">
        <el-input v-model="form.mobile" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="角色标识">
        <el-checkbox-group v-model="form.roleIdList">
          <el-checkbox v-for="role in roleList" :key="role.roleId" :label="role.roleId">{{ role.roleName
            }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.status" class="ml-4">
          <el-radio :label="1" size="large">启用</el-radio>
          <el-radio :label="0" size="large">禁用</el-radio>
        </el-radio-group>
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
  userId: {
    type: String || Number,
    default: ''
  }
});
const emit = defineEmits(['update:modelValue', 'refresh', 'cleanRole'])
const form = ref({
  userId: '',
  username: '', //用户名
  password: '', //密码
  comfirmPassword: '',//确认输入密码
  email: '',//邮箱
  mobile: '',//手机号
  roleIdList: [],
  salt: '',
  inviterCode: '',
  status: 1,

});
const rules = {
  username: [{
    required: true,
    message: '请输入用户名',
    trigger: 'blur'
  }],
  password: [{
    required: true,
    message: '请输入密码',
    trigger: 'blur'
  }],
  comfirmPassword: [{
    required: true,
    message: '请确认输入密码',
    trigger: 'blur'
  }],
  email: [{
    required: true,
    message: '请输入邮箱',
    trigger: 'blur'
  }],
  mobile: [{
    required: true,
    message: '请输入手机号',
    trigger: 'blur'
  }],
}
//全部角色列表
const roleList = reactive([]);
const roleFormRef = ref(null);
const loading = ref(false);
onMounted(() => {
  getAllRoleList();
});
//提交/修改
const saveRole = async (roleFormRef) => {
  if (!roleFormRef) return
  await roleFormRef.validate((valid, fields) => {
    if (valid) { //校验通过，提交
      console.log('submit!')
      let url = '';
      if (form.value.userId) { //修改
        url = '/sys/user/update'
      } else { //新建
        url = '/sys/user/save'
      }
      ElMessageBox.confirm(
        form.value.userId ? '确认修改吗？' : '确认提交吗？',
        '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true
            instance.confirmButtonText = form.value.userId ? '修改中...' :
              '提交中...'
            proxy.$Request.post(url, form.value, {
              type: 'json'
            }).then(res => {
              if (res.code == 0) {
                done()
                instance.confirmButtonLoading = false
                ElMessage({
                  message: '操作成功',
                  type: 'success',
                  onClose: () => {
                  }
                })
              } else {
                instance.confirmButtonText = '确认'
                instance.confirmButtonLoading = false
                ElMessage({
                  message: res.msg,
                  type: 'warning',
                  onClose: () => {
                  }
                });
                // ElMessage.alert(res.msg, '提示', {
                //   confirmButtonText: '确认',
                // })
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
      }).catch(() => {
      })
    }
  })
};
//获取全部角色列表
const getAllRoleList = () => {
  roleList.splice(0, roleList.length)
  loading.value = true
  proxy.$Request.get('/sys/role/list').then(res => {
    loading.value = false
    roleList.splice(0, roleList.length, ...res.data.page.list)

    if (props.userId) {
      selectInfo(props.userId)
    }
  })
};
//获取当前管理员信息
const selectInfo = (id) => {
  proxy.$Request.get('/sys/user/info/' + id).then(res => {
    if (res.code == 0) {
      form.value.username = res.data.user.username
      form.value.password = res.data.user.password
      form.value.comfirmPassword = res.data.user.password
      form.value.userId = res.data.user.userId
      form.value.email = res.data.user.email
      form.value.mobile = res.data.user.mobile
      form.value.roleIdList = res.data.user.roleIdList
      form.value.salt = res.data.user.salt
      form.value.inviterCode = res.data.user.inviterCode
      form.value.status = res.data.user.status


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
