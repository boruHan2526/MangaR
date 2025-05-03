<template>
  <div
    style="
      width: 100vw;
      height: 100vh;
      overflow: hidden;
      display: flex;
      position: relative;
    "
  >
    <!-- 背景图片容器 -->
    <div style="flex: 1; position: absolute; top: 0; left: 0; z-index: -1">
      <el-image
        style="width: 100%; height: 100%; object-fit: cover"
        fit="cover"
        src="/images/BKimage.jpg"
      ></el-image>
    </div>

    <!-- 欢迎标题 -->
    <div class="welcome-title">
      <div class="title-main">{{ $t('message.welcomeTitle') }}</div>
      <div class="title-sub01">{{ $t('message.welcomeContent01') }}</div>
      <div class="title-sub02">{{ $t('message.welcomeContent02') }}</div>
    </div>

    <!-- 右侧卡片区域 -->
    <div class="right-card">
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Long+Cang&family=Ma+Shan+Zheng&family=ZCOOL+XiaoWei&display=swap");
.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
  z-index: 1; /* 确保标题位于前面 */
}

.right-card {
  position: relative;
  width: 400px;
  z-index: 2; /* 确保卡片位于标题和背景上面 */
  background: rgba(255, 255, 255, 0.4); /* 调整背景透明度 */
  backdrop-filter: blur(10px); /* 增强模糊效果 */
  border-top-left-radius: 20px; /* 只设置左上圆角 */
  border-bottom-left-radius: 20px; /* 只设置左下圆角 */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3); /* 增强阴影效果 */
  padding: 20px;
  /* 添加渐变背景，使过渡更平滑 */
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.3),
    rgba(255, 255, 255, 0.6)
  );
  margin-left: auto; /* 让卡片右对齐 */
}

.title-main {
  font-family: "Long Cang", cursive;
  font-weight: 400;
  font-style: normal;
  font-size: 30px;
}

.title-sub01 {
  margin-top: 10px;
  font-family: "ZCOOL XiaoWei", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 20px;
}

.title-sub02 {
  margin-top: 5px;
  font-family: "ZCOOL XiaoWei", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 20px;
}
</style>
