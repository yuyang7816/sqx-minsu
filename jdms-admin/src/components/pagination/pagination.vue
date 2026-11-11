<template>
  <div class="">
    <el-pagination :size="size" @current-change="onPageChange" :page-size="pageSize" :background="true"
      :current-page="currentPage" layout="total,prev, pager, next" :total="total" />
  </div>

</template>

<script setup>
import {
  defineProps,
  defineEmits
} from 'vue';
const props = defineProps({
  //每页显示的数量
  pageSize: {
    type: Number,
    default: 10
  },
  //总页数
  total: {
    type: Number,
    default: 1
  },
  //当前页数
  currentPage: {
    type: Number,
    default: 1
  },
  //组件大小
  size: {
    type: String,
    default: 'small'
  }
});
const emit = defineEmits(['update:currentPage', 'pageChange']);
const onPageChange = (page) => {
  emit('update:currentPage', page)
  emit('pageChange', page)
};
</script>
