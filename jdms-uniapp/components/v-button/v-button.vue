<template>
  <view class="flex jc alic btn inactive_ active" :style="[{ background: bgColor, color, borderColor, width, height }]"
    @click="$emit('click')">
    <slot></slot>
  </view>
</template>


<script>
export default {
  props: {
    width: {
      type: String
    },
    height: {
      type: String
    },
    bgColor: {
      type: String
    },
    color: {
      type: String
    },
    borderColor: {
      type: String
    }
  }
}
</script>


<style lang="scss" scoped>
.btn {
  width: 180rpx;
  height: 72rpx;
  box-sizing: border-box;
  font-size: 28rpx;
  margin-left: 21rpx;
  border-radius: 36rpx;
  border: 2rpx solid $theme-color;
  background: $theme-color;
  color: #fff;
}
</style>