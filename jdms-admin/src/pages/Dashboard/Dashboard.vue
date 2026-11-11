<template>
	<div class="dashboard">
		<el-card class="welcome-card">
			<template #header>
				<div class="card-header">
					<span>欢迎使用管理系统</span>
				</div>
			</template>
			<div class="content">
				<p>这是一个基于 Vue 3 + Vite + Element Plus 的后台管理系统框架</p>
			</div>
		</el-card>

		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :span="6">
				<el-card class="stat-card">
					<div class="stat-content">
						<div class="stat-number">128</div>
						<div class="stat-label">用户总数</div>
					</div>
				</el-card>
			</el-col>
			<el-col :span="6">
				<el-card class="stat-card">
					<div class="stat-content">
						<div class="stat-number">24</div>
						<div class="stat-label">今日新增</div>
					</div>
				</el-card>
			</el-col>
			<el-col :span="6">
				<el-card class="stat-card">
					<div class="stat-content">
						<div class="stat-number">8</div>
						<div class="stat-label">在线用户</div>
					</div>
				</el-card>
			</el-col>
			<el-col :span="6">
				<el-card class="stat-card">
					<div class="stat-content">
						<div class="stat-number">99%</div>
						<div class="stat-label">系统正常</div>
					</div>
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>

<script setup>
</script>

<style lang="scss" scoped>
	.dashboard {
		.welcome-card {
			.content {
				p {
					font-size: 16px;
					color: #666;
				}
			}
		}

		.stat-card {
			text-align: center;

				.stat-content {
					.stat-number {
						font-size: 24px;
						font-weight: bold;
						color: var(--admin-brand);
					}

				.stat-label {
					margin-top: 10px;
					color: #666;
				}
			}
		}
	}
</style>
