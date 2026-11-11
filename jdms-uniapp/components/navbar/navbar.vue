<template>
	<view class="navbar">
		<view class="fixed-content">
            <!-- 状态栏高度 -->
			<view :style="{'height': geStatusBarHeight + 'px'}"></view>
            <!-- 导航栏高度 -->
			<view class="bar-content" :style="{'height': getNavBarHeight()+'px'}">
				<slot>
					<!-- <image @tap="navigateBack()" class="nav-left" src="/static/img/common/arrow-left.png" mode=""></image> -->
					<view class="nav-title">{{ title }}</view>
				</slot>
			</view>
		</view>
		<!-- 占位高度，状态栏高度+导航栏高度，父组件就不需要计算导航栏高度 -->
		<view :style="{'height': geStatusBarHeight + getNavBarHeight() + 'px'}"></view>
	</view>
</template>

<script>
	export default {
		name:'Navbar',
		props:{
			title:{
				type: String,
				default:''
			}
		},
        methods: {
            // 获取状态栏高度
			geStatusBarHeight(){
			    return uni.getSystemInfoSync()['statusBarHeight']
			},
            // 获取导航栏高度
            getNavBarHeight(){
                // #ifdef MP-WEIXIN
		        let menuButtonInfo = uni.getMenuButtonBoundingClientRect()
                // 导航栏高度 = 胶囊高度 + 上间距 + 下间距 + 微调	（menuButtonInfo.top - uni.getSystemInfoSync()['statusBarHeight'] = 上间距）	        
                let navbarHeight = menuButtonInfo.height + (menuButtonInfo.top - uni.getSystemInfoSync()['statusBarHeight']) * 2 + 2
		        // #endif
		        // #ifdef APP-PLUS || H5
		        let navbarHeight = 44
		        // #endif
		        return navbarHeight
            },
            navigateBack(){
                uni.navigateBack()
            }
        }
	}
</script>

<style lang="scss" scoped>
	.navbar{
		
		.fixed-content {
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			// padding: 0 24rpx;
			z-index: 1996;
			.bar-content{
				display: flex;
				align-items: center;
				position: relative;
				background-color: #fff;
				.nav-left{
					width: 22px;
					height:22px;
					position: absolute;
					z-index: 2;
					top: 50%;
					transform: translateY(-50%);
				}
				.nav-title{
					color: #fff;
					font-weight: bold;
					font-size: 16px;
					width: 100%;
					position: absolute;
					top: 50%;
					transform: translateY(-50%);
					text-align: center;
				}
			}
		}
	}
</style>