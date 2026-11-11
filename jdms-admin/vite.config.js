import {
	fileURLToPath,
	URL
} from 'node:url'

import {
	defineConfig
} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
	plugins: [
		vue(),
		vueDevTools(),
	],
	server: {
		host: '0.0.0.0', // 允许外部访问
		port: 8888,
		cors: true, // 允许跨域
		hmr: true, // 开启热更新
		strictPort: false, // 如果端口被占用，自动尝试下一个可用端口
		historyApiFallback: true,
	},
	base: './',
	build: {
		chunkSizeWarningLimit: 1600,
	},
	resolve: {
		alias: {
			'@': fileURLToPath(new URL('./src', import.meta.url))
		},
	},
})