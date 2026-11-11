<template>
  <view>
    <v-collapse :local-click="true" :ref="`collapse${node.infoId}`" v-for="node in nodeData" :key="node[uniqueKey]"
      @change="change(node.parentId, node.sort, $event)">
      <board-content slot="board" :ref="`board${node.parentId}${node.sort}`" :sort="node.sort" :data="node"
        :non-children="!node[childrenName]" @click="$emit('childClick', { infoId: node[uniqueKey], bankId: node['bankId'] })"
        @expand="expand" />
      <Tree v-if="!!node[childrenName]" :node-data="node[childrenName]" :children-name="childrenName"
        :unique-key="uniqueKey" @childClick="$emit('childClick', $event)" />
				
    </v-collapse>
  </view>
</template>


<script>
import Tree from './Tree.vue'
import BoardContent from './BoardContent.vue';
export default {
  options: { styleIsolation: 'shared' },
  name: 'Tree',
  components: { Tree, BoardContent },
  data() {
    return {
      isLaunch: false
    }
  },
  props: {
    nodeData: {
      type: Array,
      default: () => []
    },
    childrenName: {
      type: String,
      required: true
    },
    uniqueKey: {
      type: String,
      required: true
    }
  },
  methods: {
    // parentId+sort确认需要改变的board-content组件
    change(parentId, sort, e) {
      this.$refs[`board${parentId}${sort}`][0].isLaunch = e
    },
    // 局部点击展开调用
    expand({ infoId, isLaunch }) {
      this.$refs[`collapse${infoId}`][0].showContent = isLaunch
    }
  }
}
</script>


<style lang="scss" scoped>
$controller_width: 40rpx;
$controller_margin: 16rpx;

.controller {
  width: $controller_width;
  height: 40rpx;
  margin-right: 16rpx;
  border-radius: 50%;
  background: $theme-color;
  transition: background .3s;
}

.title {
  color: #333;
  font-size: 28rpx;
  font-weight: 800;
}

.label {
  padding-left: calc(#{$controller_width + $controller_margin});
  font-size: 28rpx;

  .undone {
    color: #999;
  }
}

.nonChildren {
  width: 14rpx !important;
  height: 14rpx !important;
  background: #ebebeb !important;
}

.isLaunch {
  background: #EBEBEB !important;
}

/deep/.collapse {
  .content {
    padding: 0 !important;
  }
}
</style>