<template>
  <el-dialog align-center v-model="props.modelValue" width="600px" :title="props.menuId ? '修改菜单' : '添加菜单'" draggable
    class="editDataDialog" @close="closeDialog">
    <el-form :model="form" :rules="rules" ref="menuFormRef" label-position="right" label-width="100">
      <el-form-item prop="type" label="类型">
        <el-radio-group v-model="form.type">
          <el-radio v-for="(item, index) in typeList" :value="item.value" :key="index">
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="name" label="菜单名称">
        <el-input v-model="form.name" placeholder="请输入菜单名称" />
      </el-form-item>
      <el-form-item prop="parentId" label="上级菜单">
        <el-tree-select v-model="form.parentId" ref="menuListTree" check-strictly :data="menuList"
          :default-expand-all="true" :render-after-expand="false" :props="menuListTreeProps"
          @node-click="menuListTreeCurrentChangeHandle" />
      </el-form-item>
      <el-form-item prop="url" label="菜单路由" v-if="form.type === 1">
        <el-input v-model="form.url" placeholder="请输入菜单路由" />
      </el-form-item>
      <el-form-item prop="perms" label="授权标识" v-if="form.type != 0">
        <el-input v-model="form.perms" placeholder="请输入授权标识" />
      </el-form-item>
      <el-form-item prop="orderNum" label="排序" v-if="form.type != 2">
        <el-input-number v-model="form.orderNum" :min="1" />
      </el-form-item>
      <el-form-item prop="icon" label="菜单图标" v-if="form.type != 2">
        <el-popover placement="top" v-model:visible="visibleIcon" :width="450" trigger="click">
          <template #reference>
            <el-input v-model="form.icon" readonly placeholder="请选择菜单图标" />

          </template>
          <el-scrollbar height="400px">
            <div class="iconLists flex flex-wrap">
              <div class="iconLists-item" @click="selectIcon(iconName)" v-for="(iconName, index) in allIconList"
                :key="index" style="margin: 5px;">
                <el-icon size="30px">
                  <component :is="iconName" />
                </el-icon>
              </div>
            </div>
          </el-scrollbar>
        </el-popover>

      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="saveMenu(menuFormRef)">
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
  nextTick,
  onUnmounted,
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
  //菜单id
  menuId: {
    type: String || Number,
    default: ''
  }
});
const emit = defineEmits(['update:modelValue', 'refresh'])
const form = ref({
  menuId: '', //菜单id
  type: 1, //类型
  name: '', //菜单名称
  parentName: '', //上级菜单
  parentId: 0, //上级id
  perms: '', //授权标识
  url: '', //菜单路由
  orderNum: 1, //排序
  icon: '', //菜单图标
});
//菜单类型
const typeList = reactive([{
  label: '目录',
  value: 0
},
{
  label: '菜单',
  value: 1
},
{
  label: '按钮',
  value: 2
}
]);
const rules = {
  type: [{
    required: true,
    message: '请选择类型',
    trigger: 'blur'
  }],
  name: [{
    required: true,
    message: '请输入菜单名称',
    trigger: 'blur'
  }],
  // parentName: [{
  // 	required: true,
  // 	message: '请选择上级菜单',
  // 	trigger: 'blur'
  // }],
  parentId: [{
    required: true,
    message: '请选择上级菜单',
    trigger: 'blur'
  }]

}
const menuListTreeProps = ref({
  label: 'name',
  value: 'menuId',
  children: 'children'
});

//全部菜单列表
const menuList = reactive([]);
//全部图标
const allIconList = reactive([]);
//是否显示图标选择
const visibleIcon = ref(false);
const menuFormRef = ref(null);
onMounted(() => {
  if (props.menuId) {
    form.value.menuId = props.menuId;
    getMenuInfo()
  }
  getAllMenuList();
  getAllIconList();
});
//获取菜单详情
const getMenuInfo = () => {
  proxy.$Request.get('/sys/menu/info/' + form.value.menuId).then(res => {
    if (res.code == 0 && res.data.menu) {
      form.value.icon = res.data.menu.icon
      form.value.name = res.data.menu.name
      form.value.orderNum = res.data.menu.orderNum
      form.value.parentId = res.data.menu.parentId
      form.value.type = res.data.menu.type
      form.value.perms = res.data.menu.perms
      form.value.url = res.data.menu.url
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
//选择图标
const selectIcon = (iconName) => {
  form.value.icon = iconName;
  visibleIcon.value = false
};
//获取所有图标
const getAllIconList = () => {
  let icons = []
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    icons.push(key)
  }
  allIconList.splice(0, allIconList.length, ...icons)
}
//选择回调
const menuListTreeCurrentChangeHandle = (event) => {
  console.log(event)
  // form.parentId = event.menuId
  // form.parentName = event.name
  // parentName = []
  // parentName.push(value.name)
};
//获取全部菜单
const getAllMenuList = () => {
  menuList.splice(0, menuList.length)
  proxy.$Request.get('/sys/menu/select').then(res => {
    if (res.code == 0) {
      menuList.splice(0, menuList.length, ...treeDataTranslate(res.data.menuList, 'menuId'))
    }
  })
};

//关闭弹窗
const closeDialog = () => {
  emit('update:modelValue', false)
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
