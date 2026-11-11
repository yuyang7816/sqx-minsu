<template>
  <div class="page-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="分类管理" name="first">
        <div style="margin: 0 20px;text-align: right;">

          <el-button size="small" class="btn-refresh" @click="handleSearch()">刷新</el-button>
          <el-button size="small" type="warning" plain @click="addChangeU()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="liaotianData">
          <el-table-column prop="id" label="编号" width="100"></el-table-column>
          <el-table-column prop="clientType" label="类型">
            <template #default="scope">
              <span v-if="scope.row.clientType == 1">用户端</span>
              <span v-if="scope.row.clientType == 2">商家端</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="sort" label="排序" width="100"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
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
      <el-tab-pane label="文章管理" name="second">
        <div style="text-align: right;margin: 0 20px;">
          <div style="display: inline-block;margin-right: 10px;">
            <span>分类：</span>
            <el-select v-model="helpClassifyIdT" class="m-2" placeholder="请选择分类" style="width: 200px;"
              @change="handleSearchW()">
              <el-option v-for="(item, index) in liaotianData" :key="index" :label="item.name" :value="item.id" />
            </el-select>
          </div>


          <el-button size="small" class="btn-refresh" @click="handleSearchW()">刷新</el-button>
          <el-button size="small" type="primary" @click="addChangeW()">添加</el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="wenzhangData">
          <el-table-column prop="id" label="编号" width="100"></el-table-column>
          <el-table-column prop="title" label="标题"></el-table-column>
          <el-table-column prop="content" label="内容">
            <template #default="scope">
              <el-popover placement="top-start" title="" trigger="hover" popper-style="width:420px;">

                <div style="width: 300px;" v-html="scope.row.content"></div>
                <template #reference>
                  <div
                    style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
                    {{ scope.row.content }}</div>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="100"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button size="small" type="primary" @click="addChangeW(scope.row)" style="margin: 5px;">修改</el-button>
              <el-button size="small" type="danger" @click="handleDeleteW(scope.row)"
                style="margin: 5px;">删除</el-button>
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
    <!-- 审核 弹框 -->
    <el-dialog v-model="dialogFormVisibleU" :title="titles" width="80%" center>
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
        <el-radio-group v-model="state" class="ml-4">

          <el-radio :label="2">封禁账号</el-radio>
          <el-radio :label="3">驳回</el-radio>
          <el-radio :label="4">4通过但不做处理</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">审核说明：</span>
        <el-input style="width:50%;" v-model="description" type="textarea" rows="4" placeholder="请输入审核说明"></el-input>
      </div> -->
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
        <el-radio-group v-model="clientType" class="ml-4">
          <el-radio :label="1">用户端</el-radio>
          <el-radio :label="2">商家端</el-radio>
        </el-radio-group>
      </div>

      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
        <el-input style="width:50%;" v-model="helpClassifyName" type="text" placeholder="请输入名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
        <el-input style="width:50%;" v-model="sort" type="text" placeholder="请输入排序"></el-input>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleU = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToU" style="margin: 10px;">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 审核 弹框 -->
    <el-dialog v-model="dialogFormVisibleW" :title="titles" width="80%" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">分类：</span>
        <el-select v-model="helpClassifyId" class="m-2" placeholder="请选择分类" style="width: 50%;">
          <el-option v-for="(item, index) in liaotianData" :key="index" :label="item.name" :value="item.id" />
        </el-select>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
        <el-input style="width:50%;" v-model="helpWordTitle" type="text" placeholder="请输入标题"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
        <el-input style="width:50%;" v-model="sort" type="text" placeholder="请输入排序"></el-input>
      </div>
      <div style="margin-bottom: 10px;" v-if="titles == '添加'">
        <span style="width: 200px;display: inline-block;text-align: right;">内容：</span>
        <div style="width:50%;display: inline-block;">
          <QuillEditor v-model:content="helpWordContent" contentType="html" :options="editorOptions"
            @ready="onEditorReady" style="min-height: 300px;" />
        </div>
      </div>
      <div style="margin-bottom: 10px;" v-if="titles == '修改'">
        <span style="width: 200px;display: inline-block;text-align: right;">内容：</span>
        <div style="width:50%;display: inline-block;">
          <QuillEditor v-model:content="helpWordContent" contentType="html" :options="editorOptions"
            @ready="onEditorReady" style="min-height: 300px;" />
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisibleW = false" style="margin: 10px;">取消</el-button>
          <el-button type="primary" @click="StairNoticeToW" style="margin: 10px;">确认</el-button>
        </span>
      </template>
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
import { QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';
const {
  proxy
} = getCurrentInstance();
const activeName = ref('first'); //默认选中
const liaotianData = reactive([]); //分类 集合
const wenzhangData = reactive([]); //文章 集合
const page = ref(1);//当前页
const limit = ref(10);//每页条数
const total = ref(0);//总条数

const tableDataLoading = ref(false);//列表是否加载中
const dialogFormVisibleU = ref(false);
const routeUrl = ref('');
const titles = ref('添加');
const helpClassifyId = ref('');
const helpClassifyName = ref('');
const clientType = ref(1);
const sort = ref('');

const helpClassifyIdT = ref('');
const dialogFormVisibleW = ref(false);
const helpWordId = ref('');
const helpWordTitle = ref('');
const helpWordContent = ref('');

// 富文本
// const router = useRouter();
// const route = useRoute();
const url = ''
const formData = ref({
  id: null,
  CONT: null
});
const editorOptions = {
  modules: {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'], // 加粗 斜体 下划线 删除线
      [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色
      [{ align: [] }], // 对齐方式
      [{ size: ['small', false, 'large', 'huge'] }], // 字体大小
      [{ font: [] }], // 字体种类
      [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题
      [{ direction: 'ltl' }], // 文本方向
      [{ direction: 'rtl' }], // 文本方向
      [{ indent: '-1' }, { indent: '+1' }], // 缩进
      [{ list: 'ordered' }, { list: 'bullet' }], // 有序、无序列表
      [{ script: 'sub' }, { script: 'super' }], // 上标/下标
      ['blockquote', 'code-block'], // 引用  代码块
      ['clean'], // 清除文本格式
      ['link', 'image', 'video'], // 链接、图片、视频
    ]
  }
}

// 用于存储 Quill 实例
let quillInstance = null;

// 图片上传处理函数
const handleImageUpload = () => {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*';
  input.click();

  input.onchange = async (e) => {
    console.log('图片图片', e);
    const file = e.target.files[0];
    if (file) {

      try {
        // 上传图片axios
        const response = await uploadImg(file);
        const imageUrl = response;
        console.log('response---',)
        // insertImageToEditor(imageUrl);

      } catch (error) {
        console.error('图片上传失败', error);
      }
    }
  };
};
const uploadImg = (file) => {
  const formData = new FormData();
  formData.append('file', file);
  proxy.$Request.post('/alioss/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }
  ).then((res) => {
    console.log(res)
    insertImageToEditor(res.data);
  })
}
const uploadFile = (file) => {
  const formData = new FormData();
  formData.append('file', file);
  proxy.$Request.post('/alioss/upload', formData, {
    // type: 'json'
  }, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 文件上传处理函数
const handleFileUpload = () => {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = '*/*'; // 接受所有类型的文件 .docx,.pdf
  input.click();

  input.onchange = async (e) => {
    const file = e.target.files[0];
    if (file) {
      const maxSize = 10 * 1024 * 1024; // 10MB
      if (file.size > maxSize) {
        toast(t('sys.file-error-msg'), 'error');
        return;
      }
      try {
        const response = await uploadFile(file);
        const fileUrl = response.data.url; // 假设返回的文件 URL 在 response.data
        insertFileToEditor(fileUrl, file.name); // 插入文件链接到编辑器
      } catch (error) {
        console.error('文件上传失败', error);
      }
    }
  };
};

// 将上传的图片插入编辑器
const insertImageToEditor = (imageUrl) => {

  if (quillInstance) {
    const range = quillInstance.getSelection();
    quillInstance.insertEmbed(range.index, 'image', imageUrl);
  }
};

// 将上传的文件插入编辑器
const insertFileToEditor = (fileUrl, fileName) => {
  if (quillInstance) {
    const range = quillInstance.getSelection();
    const link = `<a href="${fileUrl}" target="_blank">${fileName}</a>`;
    quillInstance.clipboard.dangerouslyPasteHTML(range.index, link); // 插入文件链接
  }
};

// 在编辑器准备好后，获取 Quill 实例
const onEditorReady = (quill) => {
  quillInstance = quill; // 保存 quill 实例
  const toolbar = quill.getModule('toolbar');
  toolbar.addHandler('image', handleImageUpload); // 替换默认的图片上传行为
  toolbar.addHandler('link', handleFileUpload); // 添加自定义文件上传处理
};

onMounted(() => {
  liaotianSelect();
});


// 切换tab 事件
const handleClick = (tab, event) => {
  if (event.srcElement.innerText == '分类管理') {
    liaotianSelect();
  }
  if (event.srcElement.innerText == '文章管理') {
    helpClassifyIdT.value = liaotianData[0].helpClassifyId
    wenzhangSelect();
  }

}

//分类 列表
const liaotianSelect = () => {

  tableDataLoading.value = true
  liaotianData.splice(0, liaotianData.length)
  proxy.$Request.post('/admin/helpCategory/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    name: '',
    clientType: '',

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


// 添加/修改 分类 弹框
const addChangeU = (row) => {
  if (row) {
    titles.value = '修改'
    helpClassifyId.value = row.id
    helpClassifyName.value = row.name
    // clientType.value = row.clientType
    sort.value = row.sort
  } else {
    titles.value = '添加'
    helpClassifyId.value = ''
    helpClassifyName.value = ''
    clientType.value = 1
    sort.value = ''
  }
  dialogFormVisibleU.value = true
}
const StairNoticeToU = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/helpCategory/save'
  } else {
    routeUrl.value = '/admin/helpCategory/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: helpClassifyId.value,
    name: helpClassifyName.value,
    clientType: clientType.value,
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
/** 删除 分类*/
const handleDelete = (data) => {
  proxy.$Request.post('/admin/helpCategory/delete', {
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
// 文章 列表
const wenzhangSelect = () => {
  tableDataLoading.value = true
  wenzhangData.splice(0, wenzhangData.length)
  proxy.$Request.post('/admin/helpArticle/page', {
    // params: {
    page: page.value,
    limit: limit.value,
    categoryId: helpClassifyIdT.value,
    // }
  }, {
    type: 'json'
  }).then((res) => {
    console.log(res)
    tableDataLoading.value = false;
    wenzhangData.splice(0, wenzhangData.length, ...res.data.records)
    total.value = res.data.total;
  })
}
// 刷新 文章
const handleSearchW = () => {
  page.value = 1
  wenzhangSelect()
}
// 添加/修改 文章 弹框
const addChangeW = (row) => {
  if (row) {
    titles.value = '修改'
    helpClassifyId.value = row.categoryId
    helpWordId.value = row.id
    helpWordTitle.value = row.title
    helpWordContent.value = row.content
    sort.value = row.sort

  } else {
    titles.value = '添加'
    helpClassifyId.value = helpClassifyIdT.value
    helpWordId.value = ''
    helpWordTitle.value = ''
    helpWordContent.value = ''
    sort.value = ''
    console.log('helpWordContent.value', helpWordContent.value)

  }
  dialogFormVisibleW.value = true
}
const StairNoticeToW = () => {
  if (titles.value == '添加') {
    routeUrl.value = '/admin/helpArticle/save'
  } else {
    routeUrl.value = '/admin/helpArticle/update'
  }
  proxy.$Request.post(routeUrl.value, {
    id: helpWordId.value,
    categoryId: helpClassifyId.value,
    title: helpWordTitle.value,
    content: helpWordContent.value,
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
          dialogFormVisibleW.value = false
          wenzhangSelect()
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
/** 删除 文章*/
const handleDeleteW = (data) => {
  proxy.$Request.post('/admin/helpArticle/delete', {
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

.editor-container {
  width: 50%;
  max-height: 100%;
  overflow-y: auto;
  border: 1px solid #d8d8d8;
}

.ql-.ql-toolbar.ql-snow {
  width: 50% !important;

}

span {
  font-size: 14px;
  color: #303133;
}
</style>
