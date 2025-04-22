<template>
  <div class="post-editor-container">
    <div class="post-editor">
      <el-card class="form-card">
        <!-- 头部 -->
        <!-- 替换掉原来的 back-link 部分 -->
        <el-button class="close-button" link @click="$router.push('/')"
          >×</el-button
        >

        <h1 class="page-title">新增文章</h1>

        <el-form
          class="form-wrapper"
          label-position="top"
          label-width="100px"
          :model="form"
          :rules="rules"
          ref="formRef"
        >
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入文章标题" />
          </el-form-item>

          <el-form-item label="分类" prop="category">
            <el-input v-model="form.category" placeholder="请输入文章分类" />
          </el-form-item>

          <el-form-item label="概述" prop="description">
            <el-input
              type="textarea"
              v-model="form.description"
              placeholder="请输入文章概述"
              :rows="3"
            />
          </el-form-item>

          <el-form-item label="内容" class="editor-item">
            <RichTextEditor v-model="form.content" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitPost">发布</el-button>
          </el-form-item>

          {{ form.content }}
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import RichTextEditor from "@/components/RichTextEditor.vue";
import { postBlog } from "@/net";
import router from "@/router";

const form = ref({
  title: "",
  category: "",
  description: "",
  content: "",
});

const formRef = ref(null);

const rules = {
  title: [{ required: true, message: "请输入标题", trigger: "blur" }],
  category: [{ required: true, message: "请输入分类", trigger: "blur" }],
  description: [{ required: true, message: "请输入概述", trigger: "blur" }],
};

const submitPost = () => {
  formRef.value?.validate((valid) => {
    if (!valid) return;

    if (!form.value.content) {
      ElMessage.warning("内容不能为空");
      return;
    }

    postBlog(
      {
        title: form.value.title,
        category: form.value.category,
        description: form.value.description,
        content: form.value.content,
      },
      () => {
        // 成功回调中不再需要提示
        // 可执行跳转或清空表单
        console.log("发布后动作，比如跳转首页");
        router.replace("/"); // 替换当前路由为首页
      }
    );
  });
};
</script>

<style scoped>
.post-editor-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  background-image: url("/images/main_back.jpg"); /* 背景图片路径 */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
}

/* 为 post-editor-container 添加遮罩 */
.post-editor-container::before {
  content: "";
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.1); /* 半透明深色遮罩 */
  z-index: 0;
}

.post-editor {
  width: 100%;
  max-width: 800px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1; /* 确保编辑器内容在遮罩层之上 */
}

.form-card {
  padding: 20px;
  border-radius: 10px;
  background-color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.form-wrapper {
  display: flex;
  margin-top: 20px;
  flex-direction: column;
  gap: 20px;
}

.el-form-item {
  margin-bottom: 16px;
}

.el-input,
.el-button {
  width: 100%;
  border-radius: 8px;
  font-size: 14px;
}

.el-button {
  background-color: #42b983;
  border-color: #42b983;
  color: white;
  font-weight: bold;
}

.el-button:hover {
  background-color: #3a9c76;
  border-color: #3a9c76;
}

.editor-item {
  margin-bottom: 16px;
}

.close-button {
  all: unset; /* 清除所有继承样式 */
  position: absolute;
  top: 20px;
  right: 20px;
  font-size: 24px;
  color: #999;
  cursor: pointer;
  z-index: 10;
  line-height: 1;
  padding: 0 8px;
}

.close-button:hover {
  color: #333;
}

/* 防止滚动条出现 */
body,
.post-editor-container {
  overflow: hidden;
}
</style>
