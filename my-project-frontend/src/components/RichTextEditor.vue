<template>
  <div>
    <!-- 工具栏组件，绑定编辑器实例 -->
    <!-- 
        :editor="editorRef"                 绑定编辑器实例
        :defaultConfig="toolbarConfig"      工具栏配置（此处为空）
        :mode="mode"                        模式设置，默认"default"
    -->
    <Toolbar
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
      style="border-bottom: 1px solid #ccc"
    />

    <!-- 编辑器组件 -->
    <!-- 
    :defaultConfig="editorConfig"           编辑器配置，比如 placeholder    
    :mode="mode"                            模式：default 或 simple
    v-model="valueHtml"                     双向绑定内容
    @onCreated="handleCreated"              编辑器创建完的回调
    @onChange="handleChange"                编辑器内容变化时的回调
    -->
    <Editor
      :defaultConfig="editorConfig"
      :mode="mode"
      v-model="valueHtml"
      style="height: 400px; overflow-y: hidden"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />
  </div>
</template>

<script setup>
/* 引入 wangEditor 的样式文件（必须） */
import "@wangeditor/editor/dist/css/style.css";
/* vue 的响应式工具 */
// shallowRef 是 Vue 3 提供的一个浅层响应式引用。它只追踪最外层的变化，不会递归地让里面的对象或属性变成响应式的。
import {
  ref,
  reactive,
  inject,
  onMounted,
  shallowRef,
  onBeforeUnmount,
} from "vue";
/* 引入编辑器与工具栏组件 */
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";

const editorRef = shallowRef();
const toolbarConfig = {};
const editorConfig = { placeholder: "请输入内容..." };
const mode = ref("default");

const valueHtml = ref("");

/* 接收父组件传递的 modelValue（如果用 v-model 绑定） */
/* 
prop 是 Vue 中一个非常核心的概念，它用来 父组件向子组件传递数据。你可以把 prop 理解为一个变量，父组件通过它将数据传递给子组件，而子组件则通过 props 来接收这些数据。
prop 是一个 Vue 组件的选项，它允许父组件传递数据给子组件。通过 props，子组件能够接收到父组件传递过来的值。prop 的值可以是字符串、数字、对象，甚至是函数或组件等。
父组件通过绑定 prop 的形式将数据传递给子组件。通过 v-bind 指令（或简写的 :）来进行绑定。
例如，父组件通过 v-bind:modelValue="content" 向子组件传递一个名为 modelValue 的 prop：
    !-- 父组件 -->
    <template>
        <child-component :modelValue="content"></child-component>
    </template>
子组件通过 props 来接收父组件传递的数据。在 Vue 3 中，使用 defineProps 来声明组件需要接收的 prop。例如，子组件接收父组件传递的 modelValue：
    <!-- 子组件 -->
    <template>
    <div>{{ modelValue }}</div> <!-- 显示接收到的 prop 值 -->
    </template>

    JS
    // 使用 defineProps 来接收父组件传递的 prop
    const props = defineProps({
    modelValue: {
        type: String,
        required: true
    }
    });
    JS
*/
const props = defineProps({
  modelValue: {
    type: String,
    default: "",
  },
});

/* 定义事件，用于向父组件发送更新（如 update:model-value） */
// defineEmits(['update:model-value'])：声明子组件可以触发 update:model-value 事件。
// update:model-value 是 Vue 3 中用于双向绑定的默认事件名（v-model 底层会触发这个事件）。你可以自定义事件名称，但 Vue 默认使用这个名称。
// 通过 defineEmits，子组件告诉 Vue 它会触发 update:model-value 事件，并可以在父组件中捕获该事件。
/*
  第一处：defineEmits(['update:model-value'])
  作用：声明子组件可以发出哪些事件
  就像你用 defineProps 告诉组件你会接收什么 prop。
*/
const emit = defineEmits(["update:model-value"]);

// 用来判断容器是否正常初始化完成（挂载完成），完成后再抛出emit事件
let initFinished = false;

// 等待10s后再初期化挂载，确保挂载的是父组件传来的值，而不是自身的默认内容（<p><br></p>）
onMounted(() => {
  setTimeout(() => {
    valueHtml.value = props.modelValue;
    initFinished = true;
  }, 10)
})

// emit 是子组件向父组件“汇报数据变更”的唯一合法通道，是实现双向绑定的关键角色。
// emit("update:model-value", valueHtml.value)：触发 update:model-value 事件，并将子组件的数据传递给父组件。
// 通过 emit，子组件通知父组件“值已经更新”，并通过事件将更新后的值（valueHtml.value）传递给父组件。
// 父组件会捕获这个事件，并根据事件中传递的数据更新它的 modelValue（在这里是 content）
/*
  第二处：emit('update:model-value', valueHtml.value)
  作用：真正触发事件并向父组件发送数据。
  这是你代码里调用 emit 函数的地方。
*/
const handleChange = (editor) => {
  if(initFinished)
    emit("update:model-value", valueHtml.value);
};

const handleCreated = (editor) => {
  console.log("created", editor);
  editorRef.value = editor;
};

onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;

  editor.destroy();
});
</script>

<style></style>
