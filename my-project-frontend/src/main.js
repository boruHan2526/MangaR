import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import 'element-plus/theme-chalk/dark/css-vars.css'

axios.defaults.baseURL = 'http://localhost:8080' //每次启动时改这里，本地测试换成本地服务器地址

const app = createApp(App)

app.use(router)
app.mount('#app')
