<template>
	<div class="menu">
		<el-row>
			<el-col :span="24">
				<div class="topBtn flex align-center justify-end">
					<el-button class="btn-refresh" @click="getMenuList">刷新</el-button>
					<el-button type="primary" @click="isShowEditData = true">新增</el-button>
				</div>
			</el-col>
		</el-row>
		<el-row class="el-rows">
			<el-col :span="24">
				<div class="menu-list">
					<el-table :data="menuList" border v-loading="menuLoading" row-key="menuId"
						:tree-props="{children: 'children',}">
						<el-table-column prop="name" header-align="center" min-width="150" label="名称" />
						<el-table-column prop="parentName" align="center" width="120" label="上级菜单" />
						<el-table-column prop="date" align="center" label="图标">
							<template #default="scope">
								<el-icon size="30px">
									<component :is="scope.row.icon" />
								</el-icon>
							</template>
						</el-table-column>


						<el-table-column prop="date" align="center" label="类型">
							<template #default="scope">
								<el-tag v-if="scope.row.type === 0" size="small">目录</el-tag>
								<el-tag v-else-if="scope.row.type === 1" size="small" type="success">菜单</el-tag>
								<el-tag v-else-if="scope.row.type === 2" size="small" type="info">按钮</el-tag>
							</template>
						</el-table-column>
						<el-table-column prop="orderNum" align="center" label="排序" />
						<el-table-column prop="url" align="center" min-width="150" label="菜单URL" />
						<el-table-column prop="perms" align="center" width="150" label="授权标识">
						</el-table-column>
						<el-table-column fixed="right" label="操作" min-width="120">
							<template #default="scope">
							<el-button type="primary" size="small" @click="updata(scope.row)">
								修改
							</el-button>
							<el-button type="danger" size="small" @click="deleteMenu(scope.row)">
								删除
							</el-button>
							</template>
						</el-table-column>

					</el-table>
				</div>
			</el-col>
		</el-row>
		<!-- 添加/修改菜单 -->
		<EditDataDialog v-model="isShowEditData" v-if="isShowEditData" :menuId="menuId" @refresh="getMenuList" />
	</div>
</template>

<script setup>
	import EditDataDialog from './components/editDataDialog.vue';
	import {
		ref,
		reactive,
		getCurrentInstance,
		onMounted
	} from 'vue';
	import {
		treeDataTranslate
	} from '/comment/utils.js'
	import {
		ElMessageBox,
		ElMessage
	} from 'element-plus';
	const {
		proxy
	} = getCurrentInstance();
	//菜单列表
	const menuList = reactive([]);
	//列表是否加载中
	const menuLoading = ref(false);
	//是否显示添加/修改弹窗
	const isShowEditData = ref(false);
	const menuId = ref(''); //菜单id
	onMounted(() => {
		getMenuList();
	});
	//修改菜单
	const updata = (item) => {
		menuId.value = item.menuId
		isShowEditData.value = true
	};
	//删除菜单
	const deleteMenu = (item) => {
		ElMessageBox.confirm(
			'确认删除此菜单吗？',
			'提示', {
				confirmButtonText: '确认',
				cancelButtonText: '取消',
				type: 'warning',
				beforeClose: (action, instance, done) => {
					if (action === 'confirm') {
						instance.confirmButtonLoading = true
						instance.confirmButtonText = '删除中...';
						proxy.$Request.post('/sys/menu/delete/' + item.menuId, {}, {
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
			getMenuList()
			ElMessage({
				type: 'success',
				message: '已删除'
			});

		}).catch(() => {
			ElMessage({
				type: 'info',
				message: '已取消',
			})
		})
	};
	//获取菜单列表
	const getMenuList = () => {
		menuLoading.value = true
		menuList.splice(0, menuList.length)
		proxy.$Request.get('/sys/menu/list').then(res => {
			// console.log(res)
			menuList.splice(0, menuList.length, ...treeDataTranslate(res, 'menuId'))
			menuLoading.value = false
		})
	};
</script>

<style lang="scss" scoped>
	.menu {
		width: 100%;
		// height: 100%;
		background-color: #ffffff;
		padding: 20px;
		border-radius: 8px;
		box-shadow: var(--el-box-shadow-light);
		display: flow-root; // 创建块格式化上下文，防止外部浮动影响
	}

	.topBtn {
		width: 100%;
	}

	.menu-list {
		width: 100%;
	}

	.el-rows {
		margin-top: 20px;
	}
</style>