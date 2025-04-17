<template>
  <div class="common-layout">
    <!-- el-container 是 Element Plus 提供的布局容器 -->
    <el-container class="container">
      <!-- 顶部 header，固定高度 -->
      <el-header class="header">
        <!-- 
          :default-active="activeIndex"         // 当前激活项的 index
          class="el-menu-demo"                  // 自定义的类名（可以写样式）
          mode="horizontal"                     // 水平排列的菜单
          :ellipsis="false"                     // 防止菜单项自动省略（默认是 true，会缩略）
          menu-trigger="click"                  // 子菜单打开的触发方式，只在 mode 为 horizontal 时有效。
          @select="handleSelect"                // 选中菜单项时触发的方法
        -->
        <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          :ellipsis="false"
          menu-trigger="click"
          @select="handleSelect"
        >
          <!-- 显示“Home”字样; 点击回到主页 -->
          <!-- index="0" 是它的标识（每个项都需要有 index） -->
          <el-menu-item index="0" @click="resetFilter">Home</el-menu-item>

          <!-- 这个是一个普通的 div，目的是拉开左右两边的间距; 它利用 CSS 的 flex-grow: 1; 来在 LOGO 和右边菜单之间“推开空间”; 相当于中间加了个弹簧 -->
          <div class="flex-grow"></div>

          <!-- 新建文章菜单，暂时写在这里，之后的版本移动到管理系统里 -->
          <el-menu-item index="Tem3" @click="goPost">发表</el-menu-item>

          <!-- 多级下拉菜单:分类菜单 -->
          <el-sub-menu index="1">
            <template #title>分类</template>
            <el-menu-item
              v-for="cat in categories"
              :key="cat"
              :index="cat"
              @click="filterByCategory(cat)"
            >
              {{ cat }}
            </el-menu-item>
          </el-sub-menu>

          <!-- 多级下拉菜单：系统菜单； el-sub-menu 表示下拉菜单，有一个 title，里面可以再嵌套菜单项或子菜单 -->
          <el-sub-menu index="2">
            <template #title>
              <span>
                <el-icon><More /></el-icon>
              </span>
            </template>
            <el-menu-item index="2-1">进入管理系统</el-menu-item>
            <el-menu-item index="2-2" @click="userLogout()"
              >退出系统</el-menu-item
            >
          </el-sub-menu>
        </el-menu>
      </el-header>

      <!-- 主体内容部分，占据剩余空间 -->
      <!-- 博客列表 -->
      <el-main class="main">
        <!-- el-row 是栅格系统的一行，用来放多个列（el-col）; :gutter="20" 表示每个列之间有 20 像素的水平间距（左右 margin）; 作用：可以让多个博客卡片在同一行均匀分布，有间距 -->
        <el-row :gutter="20">
          <!-- el-col 是列，占据一行的 8/24 = 1/3（Element Plus 一行是 24 格）; v-for="post in filteredPosts" 是 Vue 的 for 循环，遍历博客列表; :key="post.id" 是为了提高渲染性能，必须加（Vue 要求） -->
          <el-col
            :span="8"
            v-for="post in filteredPosts"
            :key="post.id"
            class="card-wrapper"
          >
            <!-- el-card 是 Element Plus 提供的卡片组件; shadow="hover" 表示鼠标悬停时会有阴影效果（很常用）; class="blog-card" 可以自己定义卡片样式（如宽度、内边距等） -->
            <el-card shadow="hover" class="blog-card">
              <!-- 显示文章标题和描述; 使用了 Vue 的插值语法 {{ }} 绑定数据; class="desc" 是描述样式，可能是自定义字体大小、颜色、行距等 -->
              <h3>{{ post.title }}</h3>
              <p class="desc">{{ post.description }}</p>
              <!-- 卡片底部区域（标签 + 日期） -->
              <div class="card-footer">
                <!-- el-tag 是标签组件，用来显示文章的分类，type="info" 是样式类型; span.date 是自定义类名，用来显示时间，例如“2025-04-10” -->
                <el-tag type="info">{{ post.category }}</el-tag>
                <span class="date"><TimeText :time="post.date" /></span>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>

      <!-- 底部 footer，固定高度 -->
      <el-footer class="footer">Made by BoruHan</el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { More } from "@element-plus/icons-vue";
import { logout, getBlogs } from "@/net";
import router from "@/router";
import TimeText from "@/components/TimeText.vue"; // 时间格式化组件

// activeIndex 是当前激活菜单项（默认选中 index 为 "1" 的菜单项）
const activeIndex = ref("0");

// 选择的 分类（默认为空）
const selectedCategory = ref(null);

// 获取的博客
const blogPosts = ref([]);

// 页面加载后自动拉取全部博客数据
onMounted(() => {
  getBlogs((data) => {
    blogPosts.value = data;
  });
});

// 提取所有分类
const categories = computed(() => [
  /*
    .map() 是 JavaScript 数组的一个非常常用的方法，用来**“映射”数组中的每一项，生成一个新的数组**。
    const nums = [1, 2, 3];
    const doubled = nums.map(n => n * 2);
    console.log(doubled); // [2, 4, 6]
  */
  ...new Set(blogPosts.value.map((post) => post.category)),
]);

// 当点击菜单项时会触发; key 是当前点击的菜单项的 index; keyPath 是这个菜单项的路径（从顶层到子项）
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath);
};

// 根据分类 + 关键字过滤文章
// computed() 是 Vue 3 提供的响应式计算属性; 它的值会自动跟随依赖的响应式变量（如 blogPosts、selectedCategory 等）变化而更新; 简单说：只要你改变了分类，filteredPosts 就会自动重新计算，无需手动更新
const filteredPosts = computed(() =>
  // .filter(...) 是原生 JavaScript 方法，返回符合条件的子数组
  blogPosts.value.filter((post) => {
    // const matchKeyword =
    //   post.title.includes(searchKeyword.value) ||
    //   post.description.includes(searchKeyword.value);

    const matchCategory = selectedCategory.value
      ? post.category === selectedCategory.value
      : true;

    return matchCategory;
  })
);

// 按照分类筛选
const filterByCategory = (category) => {
  selectedCategory.value = category;
  activeIndex.value = category;
};

// 重置分类筛选
const resetFilter = () => {
  selectedCategory.value = null;
  activeIndex.value = 0; // 选中Home
};

// 跳转到文章发布页面
function goPost() {
  router.push("/post");
}

// 登出系统
function userLogout() {
  logout(() => router.push("/"));
}
</script>

<style>
/* 这三行是必须的，否则页面高度默认不够，会导致布局不正常 */
html,
body,
#app {
  height: 100%; /* 设置 html/body/#app 的高度为100%，为后面撑满全屏做准备 */
  margin: 0; /* 去掉默认的外边距 */
}

/* 为整个页面设置背景图 */
.common-layout {
  height: 100%;
  width: 100%;
  background-image: url("/images/main_back.jpg");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  position: relative;
}

/* 添加黑色遮罩（提升文字可读性） */
.common-layout::before {
  content: "";
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.1); /* 半透明深色遮罩 */
  z-index: 0;
}

/* 设置 el-container 的高度为整屏高度; 给 .container 加 position: relative; z-index: 1; 避免被遮罩盖住： */
.container {
  height: 100vh; /* vh 是视口单位，100vh = 一整个屏幕高度 */
  display: flex; /* 启用 flex 布局 */
  flex-direction: column; /* 垂直排列子元素（从上到下） */
  position: relative;
  z-index: 1;
}

/* 
设置 header 的高度 
.header {
  height: 60px; 高度固定为60px
  background-color: brown; 背景色为棕色 
}
*/

.header {
  height: 60px;
  backdrop-filter: blur(10px);
  background-color: rgba(0, 0, 0, 0.3); /* 深色透明背景 */
  color: white;
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  z-index: 10;
}

/* 设置 main 主体部分自动占据剩余空间 */
.main {
  flex: 1;
  background-position: center; /* 居中显示 */
  overflow: auto;
}

/* 设置 footer 的高度 */
.footer {
  height: 40px;
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0.15);
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  color: #fff;
  font-weight: 500;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  z-index: 10;
}

/* 用来让中间的 div 拉伸，推开左右菜单项，实现 LOGO 左侧、菜单右侧的布局 */
/* 要确保 el-menu 的容器是 display: flex;，Element Plus 的 el-menu 默认是 flex，所以这能生效 */
.flex-grow {
  flex-grow: 1;
}

/* 取消菜单栏下方诡异的黑线 */
.el-menu-demo {
  background-color: transparent !important;
  color: white;
  border-bottom: none !important;
  box-shadow: none !important;
}

/* 菜单项、子菜单标题 背景透明 + 文字白色 */
.el-menu-demo .el-menu-item,
.el-menu-demo .el-sub-menu__title {
  background-color: transparent !important;
  color: white !important;
}

/* hover 时高亮一点 */
.el-menu-demo .el-menu-item:hover,
.el-menu-demo .el-sub-menu__title:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

/* 移除 el-menu 横向菜单项的蓝色下划线 */
.el-menu--horizontal > .el-menu-item.is-active {
  border-bottom: none !important; /* 去除蓝色下划线 */
  background-color: rgba(255, 255, 255, 0.35) !important; /* 替代选中背景 */
  color: white !important;
}

/* 同样适用于子菜单选中 */
.el-menu--horizontal > .el-sub-menu.is-active .el-sub-menu__title {
  border-bottom: none !important;
  background-color: rgba(255, 255, 255, 0.35) !important;
  color: white !important;
}

/* 让每个卡片之间在“垂直方向（上下）”也有间距 */
.card-wrapper {
  margin-bottom: 20px; /* 每个卡片底部留出 20px 空间 */
  display: flex; /* 这样卡片就能适配高度 */
  flex-direction: column; /* 这样卡片就能适配高度 */
}

/* 改变card样式 */
.blog-card {
  background-color: rgba(255, 255, 255, 0.85); /* 半透明白色背景 */
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2); /* 更柔和的阴影 */
  transition: all 0.3s ease-in-out;
  padding: 20px;
  backdrop-filter: blur(4px);
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 让标题、描述和 footer 分布 */
  height: 190px; /* 填满父容器 */
}

/* card在hover时更明显 */
.blog-card:hover {
  transform: translateY(-4px); /* 悬浮上升一点 */
  box-shadow: 0 8px 30px rgb(255, 255, 255); /* 更强的阴影 */
}

/* 配合的标题样式和描述微调（让文字也更优雅） */
.blog-card h3 {
  margin-top: -10px;
  margin-bottom: 10px;
  font-size: 22px;
  font-weight: 600;
  font-family: "Merriweather", serif;
  color: #2c2c2c;
  line-height: 1.3;
}

/* 配合的标题样式和描述微调（让文字也更优雅） */
.desc {
  color: #444;
  font-size: 14px;
  line-height: 1.6;
  margin: 25px 0 0;

  /* 使文本支持多行省略号 */
  display: -webkit-box; /* 使用 Webkit 的多行布局 */
  -webkit-line-clamp: 3; /* 限制显示3行 */
  -webkit-box-orient: vertical; /* 设置纵向排列 */
  overflow: hidden; /* 隐藏超出的文本 */
  text-overflow: ellipsis; /* 超出部分显示省略号 */

  min-height: 67px; /* 设置最小高度，确保至少显示三行 */
}

/* 卡片底部容器; justify-content: space-between 子元素左右两端对齐，一个靠左一个靠右，中间拉满 */
.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 40px; /* 让 footer 贴底部 */
}

.card-footer .el-tag {
  font-size: 12px;
}

.date {
  font-size: 12px;
  color: #777;
}

/* 为最底部 footer 的文字添加荧光效果 */
.footer {
  height: 40px;
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0.15);
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  color: #fff;
  font-weight: 500;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  z-index: 10;
  /* 添加绿色荧光效果 */
  text-shadow: 0 0 5px #32cd32, 0 0 10px #32cd32, 0 0 15px #00ff00,
    0 0 20px #00ff00, 0 0 25px #00ff7f, 0 0 30px #00ff7f;
}
</style>
