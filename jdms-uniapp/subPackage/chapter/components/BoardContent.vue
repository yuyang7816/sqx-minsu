<template>
  <view class="board_content" @click="nonChildren && $emit('click')">
    <u-cell-item @click="expand">
      <view slot="title" class="title">
        <!-- <view class="flex jc alic icon" @click.stop="expand"> -->
        <view class="flex jc alic icon">
          <view :class="{ expand_icon: !nonChildren, item_icon: nonChildren, inactive_: isLaunch }">
            <template v-if="!nonChildren">
              <u-icon name="plus" size="20" color="#fff" v-if="!isLaunch"></u-icon>
              <u-icon name="minus" size="20" color="#999" v-else></u-icon>
            </template>
          </view>
        </view>
        <text>{{ data.name }}</text>
      </view>
			<!-- 题目总数和已完成进度   --修改删除---添加 -->
      <view slot="label" class="ft label">{{ data.finishCount }}<text class="undone">/{{ data.allCount }}</text></view>
    </u-cell-item>
  </view>
</template>


<script>
export default {
  options: { styleIsolation: 'shared' },
  props: {
    data: {
      type: Object,
      required: true
    },
    nonChildren: {
      type: Boolean,
      default: false
    },
    sort: {
      type: Number | String,
      required: true
    }
  },
  data() {
    return {
      isLaunch: false
    }
  },
  methods: {
    // 局部点击展开调用
    expand() {
      this.isLaunch = !this.isLaunch
      this.$emit('expand', { infoId: this.data.infoId, isLaunch: this.isLaunch })
    }
  }
}
</script>


<style lang="scss" scoped>
	/deep/ .u-cell__label{
		margin-bottom: 10rpx !important;
	}
	
	
.title {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.undone {
  color: #999;
}

.inactive_ {
  background: #EBEBEB !important;
}

.icon {
  width: 40rpx;
  height: 40rpx;
  margin-right: 16rpx;

  .expand_icon {
    width: 100%;
    height: 100%;
    background: $theme-color;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .item_icon {
    width: 14rpx;
    height: 14rpx;
    border-radius: 50%;
    background: #ebebeb;
  }
}

.label {
  text-indent: calc(40rpx + 16rpx);
}

/deep/.u-cell {
  padding-left: 0 !important;
  padding-right: 0 !important;
  padding-bottom: 0 !important;
}
</style>