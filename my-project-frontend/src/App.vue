<template>
  <div>
    <router-view v-slot="{ Component, route }">
      <!-- 
      只有当该路由 meta.keepAlive === true 时，才用 keep-alive，
      include 用来限定只有 name 为 'index' 的组件会被缓存
    -->
      <keep-alive include="index" v-if="route.meta.keepAlive">
        <component :is="Component" />
      </keep-alive>
      <!-- 其他路由正常渲染且不缓存 -->
      <component v-else :is="Component" />
    </router-view>
  </div>
</template>

<script setup lang="ts"></script>

<style></style>
