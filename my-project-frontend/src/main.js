import { createApp, provide } from "vue";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";
import axios from "axios";
import "element-plus/theme-chalk/dark/css-vars.css";
import i18n from "./i18n";
import ElementPlus from "element-plus";
import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import ja from "element-plus/dist/locale/ja.mjs";

axios.defaults.baseURL = "http://38.22.90.169:8081"; 
//每次启动时改这里，本地测试换成本地服务器地址 //RichTextEditor的图片上传地址也要改
//http://38.22.90.169:8081
//删除之前的包：（windows）Remove-Item -Recurse -Force dist
//项目打包：`npm run build`

const app = createApp(App);

const getLocale = () => {
  return i18n.global.locale.value === "ja" ? ja : zhCn;
};

app.provide("server_url", axios.defaults.baseURL);

app.use(ElementPlus, {
  locale: getLocale(),
});
app.use(i18n);
app.use(createPinia()); // 注册 Pinia
app.use(router);
app.mount("#app");
