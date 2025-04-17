<template>
  <div class="post-editor">
    <h2>发布新文章</h2>
    <!-- 表单内容可以慢慢扩展 -->
    <el-form label-position="top" label-width="100px">
      <el-form-item label="标题">
        <el-input v-model="title" placeholder="请输入文章标题" />
      </el-form-item>

      <el-form-item label="分类">
        <el-select v-model="category" placeholder="请选择分类">
          <el-option label="生活" value="生活" />
          <el-option label="技术" value="技术" />
          <!-- 可根据需要添加更多分类 -->
        </el-select>
      </el-form-item>

      <el-form-item label="内容">
        <!-- <el-input
          v-model="content"
          type="textarea"
          rows="10"
          placeholder="请输入文章内容"
        /> -->
        <RichTextEditor v-model="content"></RichTextEditor>
        <!-- 相当于： <RichTextEditor :modelValue="content" @update:modelValue="val => content = val" /> -->
        <!-- v-on = @; @ 符号是用来监听自定义事件的简写 -->
        <!-- 对父组件意味着：
             1. :modelValue="content" 是父组件将 content 作为 prop 传递给子组件 RichTextEditor; 
             2. @update:modelValue="val => content = val" 是父组件监听子组件发出的 update:modelValue 事件，并将传递过来的 val 赋值给 content。 -->
        <!-- 对子组件，这意味着：1.子组件要接收一个 modelValue 的 prop； 2. 子组件要通过 emit('update:modelValue', 新值) 来通知父组件更新值 -->
        <!-- :modelValue="content" 是父组件出的 prop -->
        <!-- 所以你子组件必须用 defineProps 来接收这个 modelValue -->
        <!-- 这样，父组件和子组件之间就实现了双向绑定，父组件的 content 变量会随着子组件中内容的变化而自动更新。 -->
      </el-form-item>

      <!-- <el-button type="primary" @click="submitPost">发布</el-button> -->
    </el-form>
    {{ content }}
  </div>
</template>

<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import RichTextEditor from "@/components/RichTextEditor.vue";
// import { postBlog } from "@/net"; // 你需要自己实现这个 API

const title = ref("");
const category = ref("");
const content = ref("");

const submitPost = () => {
  if (!title.value || !content.value) {
    ElMessage.warning("标题和内容不能为空");
    return;
  }

  postBlog(
    {
      title: title.value,
      category: category.value,
      content: content.value,
    },
    () => {
      ElMessage.success("发布成功");
      // 你可以选择跳转回主页或清空表单
    }
  );
};
</script>

<style scoped>
.post-editor {
  max-width: 800px;
  margin: 40px auto;
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}
</style>
