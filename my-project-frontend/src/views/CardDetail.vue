<template>
  <div class="detail-page">
    <n-button
      type="primary"
      strong
      secondary
      @click="back"
      class="back-button-float"
    >
      ← BACK
    </n-button>
    <div class="post-container p-6 max-w-3xl mx-auto">
      <h1 class="text-4xl font-bold text-gray-900 mb-6">{{ blog.title }}</h1>
      <!-- 分割线 -->
      <hr class="border-t-2 border-gray-300 mb-6" />
      <p class="text-gray-500 mb-4">
        分类：<span class="font-medium text-gray-700">{{ blog.category }}</span>
        ｜ 日期：<TimeText :time="blog.date" />
      </p>
      <!-- Description 样式修改 -->
      <div class="description text-lg text-gray-800 mb-8 leading-relaxed">
        {{ blog.description }}
      </div>
      <div
        class="prose prose-lg max-w-none text-gray-900"
        v-html="blog.content"
      ></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { getBlogById } from "@/net"; // 封装的 getBlogById 请求
import TimeText from "@/components/TimeText.vue"; // 时间格式化组件

const route = useRoute(); // 获取 Vue Route 实例
const router = useRouter(); // 获取 Vue Router 实例
const blog = ref({
  title: "",
  description: "",
  category: "",
  date: "",
  content: "",
});

onMounted(() => {
  getBlogById(
    route.params.id,
    (data) => (blog.value = data),
    (message, code, url) => {
      console.warn(`加载文章失败：${message} (状态码：${code}) @ ${url}`);
    }
  );
});

// 后退到首页
const back = () => {
  router.push("/"); // 跳转到首页
};
</script>

<style scoped>
/* 引入 Google 字体 */
@import url("https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&family=Lora:wght@400;700&display=swap");
/* 引入思源黑体字体 */
@import url("https://fonts.googleapis.com/css2?family=Source+Han+Sans+SC:wght@400;500;600&display=swap");
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@400;500;700&display=swap"); /* 引入 Noto Sans 为备用 */
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap");
@import url("https://fonts.googleapis.com/css2?family=Ma+Shan+Zheng&display=swap");

/* 外层容器：固定背景，隐藏溢出 */
.detail-page {
  position: fixed; /* 关键：固定定位 */
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url("/images/main_back.jpg");
  background-size: cover;
  background-position: center;
  overflow: hidden; /* 隐藏外层滚动条 */
}

/* 背景优化 (防止移动端白边) */
.detail-page::after {
  content: "";
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.05);
  z-index: -1;
}

/* 内容容器：固定尺寸 + 内部滚动 */
.post-container {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  max-width: 900px;
  width: 90%;

  /* 居中定位 */
  position: absolute;
  left: 50%;
  transform: translateX(-50%);

  /* 固定高度并启用滚动 */
  height: calc(100vh - 80px); /* 留出顶部按钮空间 */
  top: 20px; /* 与返回按钮间距 */
  overflow-y: auto; /* 关键：垂直滚动 */
}

/* 滚动条美化 (Chrome/Safari) */
.post-container::-webkit-scrollbar {
  width: 8px;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .post-container {
    width: 95%;
    padding: 15px;
    top: 10px;
    height: calc(100vh - 20px);
  }

  .back-button-float {
    padding: 6px 12px;
    font-size: 14px;
  }
}

.post-container::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.1);
}

.post-container::-webkit-scrollbar-thumb {
  background: #42b983;
  border-radius: 4px;
}

h1 {
  font-family: "Ma Shan Zheng", cursive;
  font-style: normal;
  color: #333;
  font-weight: 500;
}

.text-gray-500 {
  font-size: 0.875rem; /* slightly smaller font size for category and date */
}

.text-lg {
  font-size: 1.125rem; /* larger font size for description */
}

.prose {
  line-height: 1.75;
  font-size: 1rem;
  color: #333;
}

.prose img {
  max-width: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.prose a {
  color: #42b983;
  text-decoration: underline;
}

.prose a:hover {
  color: #358e62;
}

.prose code {
  background-color: #f3f4f6;
  padding: 4px 6px;
  border-radius: 4px;
}

.prose pre {
  background-color: #f3f4f6;
  padding: 10px 15px;
  border-radius: 6px;
  overflow-x: auto;
}

.prose ul,
.prose ol {
  margin-left: 1.5rem;
}

.prose li {
  margin-bottom: 0.5rem;
}

.text-lg {
  font-size: 1.125rem;
  color: #4a4a4a;
}

/* Description 样式 */
.description {
  font-family: "Lora", serif;
  background-color: #f1f5f9; /* 淡灰色背景 */
  border-left: 4px solid #42b983; /* 左侧绿色边框 */
  padding: 12px 16px; /* 内边距 */
  margin-bottom: 1.5rem;
  font-weight: 500;
  font-style: italic;
}

/* Back button styles */
/* 返回按钮微调 */
.back-button-float {
  position: fixed;
  top: 20px;
  left: 20px;
  z-index: 1000;
  font-size: 16px;
  padding: 8px 16px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}
</style>
