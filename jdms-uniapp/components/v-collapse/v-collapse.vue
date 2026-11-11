<template>
  <view class="collapse">
    <view class="collapse_board" @click.stop="handleClick">
      <slot name="board"></slot>
    </view>
    <!-- <view class="content_c" :class="{ hidden: !showContent }" :style="[{ height: showDefault ? 'auto' : contentHeight }]"> -->
    <view class="content_c" :style="[{ height: showDefault ? 'auto' : contentHeight }]">
      <view class="content">
        <slot></slot>
      </view>
    </view>
  </view>
</template>


<script>
import { getRect } from '@/utils'
export default {
  /**
   * @property {Boolean} localClick
   * 是否开启局部点击 --点击面板内的某个元素才展开内容   局部点击须父组件中手动调用trigger
   * 
   * @property {Boolean} showDefault
   * 是某默认展开
   */
  props: {
    localClick: {
      type: Boolean,
      default: false
    },
    showDefault: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      hidden: false,
      height: 0,
      showContent: false
    }
  },
  mounted() {
    this.getContentHeight()
  },
  computed: {
    contentHeight() {
      return `${this.showContent ? this.height : 0}px`
    }
  },
  methods: {
    getContentHeight() {
      this.$nextTick(async () => {
        const { height } = await getRect(this, '.content')
        this.height = height
      })
    },
    handleClick() {
      if (this.localClick) return
      this.trigger()
    },
    trigger() {
      if (this.disabled) return
      this.showContent = !this.showContent
      this.$emit('change', this.showContent)
      this.$emit(this.showContent ? 'open' : 'close')
    }
  }
}
</script>


<style lang="scss" scoped>
.collapse {
  .hidden {
    height: 0;
  }

  .content_c {
    overflow: hidden;
    transition: all .3s;

    .content {
      box-sizing: border-box;
      position: relative;
      padding: 24rpx 0;
    }
  }
}
</style>