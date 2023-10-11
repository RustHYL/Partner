import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import { VantResolver } from '@vant/auto-import-resolver';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    Components({
      resolvers: [VantResolver()],
    }),
  ],
  // server: {
  //   proxy: {// 跨域代理
  //     '/api': {
  //       // target: 'http://' + env.VUE_APP_BASE_API,
  //       target: 'http://localhost:8080', //
  //       changeOrigin: true,
  //       rewrite: (path) => path.replace(/^\/api/, '')
  //     },
  //     // 代理 WebSocket 或 socket
  //     // '/socket.io': {
  //     //   target: 'ws://localhost:3000',
  //     //   ws: true
  //     //  }
  //   },
  // }
})
