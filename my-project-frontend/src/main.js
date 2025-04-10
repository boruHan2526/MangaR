import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import 'element-plus/theme-chalk/dark/css-vars.css'

axios.defaults.baseURL = 'http://38.22.90.169:8081'

const app = createApp(App)

app.use(router)
app.mount('#app')
