<template>
  <view class="branch_com" :style="[{ height: listHeight }]" @click="toDetail">
    <view :class="['branch', { isChildren }]" :style="[{ transform: `translateZ(${transformZ + 1}px)` }]">
      <view class="this">
        <u-cell-item :arrow="true" :border-bottom="false">
          <view slot="title" class="flex alic">
            <view :class="['controller flex jc alic', { nonChildren, isLaunch }]" @click="showMore">
              <template v-if="!nonChildren">
                <u-icon name="plus" size="20" color="#fff" v-if="!isLaunch"></u-icon>
                <u-icon name="minus" size="20" color="#999" v-else></u-icon>
              </template>
            </view>
            <text class="title">{{ tree.name }}</text>
          </view>
          <view class="label" slot="label"><text class="ft">0</text><text class="undone">/100</text></view>
        </u-cell-item>
      </view>
    </view>
    <view :class="['children_list', { isChildren: tree.pid, hidden: !isLaunch, childrenWidth: !tree.pid }]"
      :style="[{ transform: `translateY(${!isLaunch ? '-100%' : '0%'}) translateZ(${transformZ}px)` }]"
      v-if="tree.hasOwnProperty('children') && tree.children.length">
      <Branch v-for="(children, index) in tree.children" :key="index" :tree="children" :transform-z="transformZ - 10"
        @load-more="loadMore" />
    </view>
  </view>
</template>


<script>
import Branch from './Branch.vue'
export default {
  name: 'Branch',
  options: { styleIsolation: 'shared' },
  props: {
    tree: {
      type: Object,
      required: true
    },
    transformZ: {
      type: Number,
      default: 0
    }
  },
  components: {
    Branch
  },
  data() {
    return {
      isLaunch: false,
      componentHeight: 0,
      recurrence_children_launch_length: 0
    }
  },
  computed: {
    nonChildren() {
      return !(this.tree.hasOwnProperty('children') && this.tree.children.length)
    },
    listHeight() {
      console.log(this.tree.children?.length);
      return `${this.componentHeight * (this.isLaunch ? (this.tree.children?.length + this.recurrence_children_launch_length) : 1)}px`
    }
  },
  methods: {
    toDetail() {
      if (!this.tree.hasOwnProperty('children') || this.tree.children.length == 0) {
        this.navigate('/subPackage/chapter/chapterDetail')
      }
    },
    showMore() {
      this.isLaunch = !this.isLaunch
    },
    loadMore(e) {
      if (this.tree.pid) {
        this.$emit('loadMore', e)
      } else {
        this.recurrence_children_launch_length += e
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      uni.createSelectorQuery().in(this).select('.this').boundingClientRect(data => {
        this.componentHeight = data.height
      }).exec()
    });
  },
  watch: {
    isLaunch(newv) {
      if (newv) {
        if (this.tree.pid) {
          this.$emit('loadMore', this.tree.children.length)
        } else {
          this.recurrence_children_launch_length += this.tree.children.length
        }
      } else {
        if (this.tree.pid) {
          this.$emit('loadMore', -this.tree.children.length)
        } else {
          this.recurrence_children_launch_length -= this.tree.children.length
        }
      }
    },
    recurrence_children_launch_length(newv) {
      console.log(newv, 'chang');
    }
  }
}
</script>


<style lang="scss" scoped>
$controller_width: 40rpx;
$controller_margin: 16rpx;

.branch_com {
  width: 100%;
  position: relative;
}

.branch {
  width: 100%;
  position: relative;
  transform: translateY(0%);
  transition: all .3s;
  background: #fff;

  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    right: 0;
    width: calc(100% - #{$controller_width + $controller_margin});
    height: 2rpx;
    background: #E6E6E6;
  }

  u-cell-item {
    width: 100%;

    /deep/.u-cell {
      padding: 0 !important;
      padding-top: 36rpx !important;
      padding-bottom: 11rpx !important;
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
  }

  .controller {
    width: $controller_width;
    height: 40rpx;
    margin-right: 16rpx;
    border-radius: 50%;
    background: $theme-color;
    transition: background .3s;
  }
}

.children_list {
  // float: right;
  width: 100%;
  transition: all .3s;
  position: absolute;
  right: 0;
}

.childrenWidth {
  width: calc(100% - #{$controller_width + $controller_margin}) !important;
}

.isLaunch {
  background: #EBEBEB !important;
}

.nonChildren {
  width: 14rpx !important;
  height: 14rpx !important;
  background: #ebebeb !important;
}

.isChildren {
  position: absolute !important;
}

.hidden {
  pointer-events: none !important;
  opacity: 0;
}
</style>