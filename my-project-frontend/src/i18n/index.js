import { createI18n } from 'vue-i18n';
import zhCn from './lang/zh-cn';
import ja from './lang/ja';

const i18n = createI18n({
  legacy: false,
  locale: 'zh-cn',
  fallbackLocale: 'zh-cn',
  messages: {
    'zh-cn': zhCn,
    ja: ja,
  },
});

export default i18n;
