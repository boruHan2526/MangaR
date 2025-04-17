<template>
  <span>{{ formattedTime }}</span>
</template>

<script setup>
import { computed } from "vue";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";

// 扩展插件
dayjs.extend(relativeTime);

// 接收参数
const props = defineProps({
  time: {
    type: String,
    required: true,
  },
  format: {
    type: String,
    default: "standard", // 可选值: 'standard' | 'chinese' | 'relative'
    // 🕓 标准格式：2025-04-15 17:35:36
    // 🇨🇳 中文格式：2025年04月15日 17:35
    // 🧭 相对时间：5分钟前
  },
});

// 格式化输出
const formattedTime = computed(() => {
  const t = dayjs(props.time);
  if (!t.isValid()) return "无效时间";

  switch (props.format) {
    case "chinese":
      return t.format("YYYY年MM月DD日 HH:mm");
    case "relative":
      return t.fromNow();
    default:
      return t.format("YYYY-MM-DD HH:mm:ss");
  }
});
</script>
