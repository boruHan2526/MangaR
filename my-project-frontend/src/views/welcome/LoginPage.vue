<template>
  <div style="text-align: center; margin: 0 20px">
    <el-config-provider :locale="langRef?.currentLocale">
    <div style="padding: 20px; margin-bottom: -65px;">
      <lang-switcher ref="langRef" />
      <!-- $t 是 Vue 国际化插件 vue-i18n 提供的一个方法，用于在模板中进行多语言翻译。 -->
      <!-- <p>{{ $t('message.hello') }}</p> -->
      <!-- <el-button type="primary">{{ $t('message.switchLang') }}</el-button>
      <el-date-picker v-model="date" type="date" placeholder="Pick a day" /> -->
    </div>
  </el-config-provider>
    <div style="margin-top: 150px">
      <div
        style="
          font-size: 25px;
          font-weight: bold;
          color: white;
          text-shadow: 0 2px 5px rgba(0, 0, 0, 0.6);
          font-family: Helvetica Neue,Helvetica,Arial,Microsoft Yahei,Hiragino Sans GB,Heiti SC,WenQuanYi Micro Hei,sans-serif;
          margin-bottom: 5px;
        "
      >
      {{ $t('message.loginTitle') }}
      </div>
      <div
        style="
          font-size: 14px;
          color: white;
          text-shadow: 0 2px 5px rgba(0, 0, 0, 0.6);
          font-family: miui,system-ui,-apple-system,BlinkMacSystemFont,Helvetica Neue,Helvetica,sans-serif;
        "
      >
      {{ $t('message.loginDescribe') }}
      </div>
    </div>
    <div style="margin-top: 40px">
      <el-form :model="form" :rules="rule" ref="formRef">
        <el-form-item>
          <el-input
            v-model="form.username"
            maxlength="10"
            type="text"
            :placeholder="$t('message.placeholderUser')"
          >
          <!-- 在 Vue 模板中：
              placeholder="..." → 静态字符串（不会解析 $t()）
              :placeholder="..." → 表达式绑定（会执行 $t()）　=>　意思是你绑定的是一段 JavaScript 表达式，而不是一个普通的字符串。
           -->
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            maxlength="20"
            :placeholder="$t('message.placeholderPassword')"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12" style="text-align: left">
            <el-form-item>
              <el-checkbox
                v-model="form.remember"
                label="Remember Me"
                style="color: white; text-shadow: 0 2px 5px rgba(0, 0, 0, 0.6)"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link
              style="
                color: white;
                text-shadow: 0 2px 5px rgba(0, 0, 0, 0.6);
                font-weight: bold;
              "
              >Forgot Password?</el-link
            >
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div style="margin-top: 40px">
      <el-button @click="userLogin" style="width: 250px" type="success" plain
        >{{ $t('message.loginButton') }}</el-button
      >
    </div>
    <el-divider>
      <span style="font-size: 13px; color: grey">{{ $t('message.noPasswordMsg') }}</span>
    </el-divider>
    <div>
      <el-button style="width: 250px" type="warning" plain>
        {{ $t('message.registerButton') }}
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import { login } from "@/net/index";
import router from "@/router";
import LangSwitcher from '@/components/LangSwitcher.vue';

const langRef = ref(null);  // 确保 langRef 被初始化
const date = ref('');

// 检查 langRef 是否正确赋值
onMounted(() => {
  console.log(langRef.value);  // 检查 langRef
});

const form = reactive({
  username: "",
  password: "",
  remember: false,
});

const rule = {
  username: [{ required: true, message: "请输入用户名" }],
  password: [{ required: true, message: "请输入密码" }],
};

const formRef = ref();

function userLogin() {
  formRef.value.validate((valid) => {
    if (valid) {
      login(form.username, form.password, form.remember, () =>
        router.push("/index")
      );
    }
  });
}
</script>

<style></style>
