import { createApp, provide } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import 'element-plus/theme-chalk/dark/css-vars.css'

axios.defaults.baseURL = 'http://localhost:8080' //每次启动时改这里，本地测试换成本地服务器地址 //RichTextEditor的图片上传地址也要改
                                                    //http://38.22.90.169:8081
                                                    //删除之前的包：（windows）Remove-Item -Recurse -Force dist
                                                    //项目打包：`npm run build`

const app = createApp(App)

app.provide("server_url", axios.defaults.baseURL);

app.use(router)
app.mount('#app')
