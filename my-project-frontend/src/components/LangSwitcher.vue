<template>
    <el-select v-model="currentLang" @change="changeLang" style="width: 160px; background: rgba(255, 255, 255, 0.2); border-radius: 8px; padding: 5px 10px; color: white; border: none;">
        <el-option label="中国語" value="zh-cn" />
        <el-option label="日本語" value="ja" />
    </el-select>
</template>

<script setup>
import { ref, defineExpose } from 'vue';
import { useI18n } from 'vue-i18n';
import zhCn from 'element-plus/dist/locale/zh-cn.mjs';
import ja from 'element-plus/dist/locale/ja.mjs';
import { ElConfigProvider } from 'element-plus';

const { locale } = useI18n();
const currentLang = ref(locale.value);

const changeLang = (lang) => {
    locale.value = lang;
    // 动态切换 ElementPlus 的语言（必须包裹 ElConfigProvider）
    if (lang === 'ja') {
        currentLocale.value = ja;
    } else {
        currentLocale.value = zhCn;
    }
};

const currentLocale = ref(locale.value === 'ja' ? ja : zhCn);

// 在这里暴露 currentLocale 给父组件使用
defineExpose({ currentLocale });
</script>

<style scoped>
/* 输入框样式 */
.lang-select {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  color: white;
  padding: 5px 10px;
  border: none;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
}

/* 穿透处理下拉框（Vue3 推荐 :deep() 替代 ::v-deep） */
:deep(.el-select__wrapper) {
  background: transparent !important;
  box-shadow: none !important;
}

:deep(.el-select__selected-item) {
  color: white !important;
}

/* 下拉框主体 */
:deep(.el-select-dropdown) {
  background: rgba(0, 0, 0, 0.6) !important;
  border: none;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(10px);
}

/* 下拉项 */
:deep(.el-select-dropdown__item) {
  color: white !important;
  background: rgba(255, 255, 255, 0.1);
}

/* 下拉项悬停效果 */
:deep(.el-select-dropdown__item:hover),
:deep(.el-select-dropdown__item.selected) {
  background: rgba(255, 255, 255, 0.3) !important;
}

/* 下拉箭头颜色 */
:deep(.el-icon) {
  --el-icon-color: white !important;
}
</style>