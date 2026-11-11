<template>
  <view class="price flex" :style="[customStyle]">
    <view class="unit">{{$t('unit.yuan')}}</view>
    <view>
      <slot></slot>
    </view>
  </view>
</template>


<script>
export default {
  props: {
    customStyle: {
      type: Object
    }
  }
}
</script>


<style lang="scss" scoped>
.price {
  font-weight: 800;
  .unit {
    font-weight: 0 !important;
    position: relative;
    transform-origin: center bottom;
    transform: scale(.75) translateY(-5%);
    margin-right: -5%;
  }

  view {
    vertical-align: text-bottom;
    align-self: baseline;
  }
}
</style>