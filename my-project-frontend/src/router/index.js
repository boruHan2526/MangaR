import { unauthorized } from '@/net'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/WelcomeView.vue'),
            children: [
                {
                    path: '',
                    name: 'welcome-login',
                    component: () => import('@/views/welcome/LoginPage.vue')
                }
            ]
        },
        {
            path: '/index',
            name: 'index',
            component: () => import('@/views/indexView.vue'),
            meta: { keepAlive: true },
        },
        {
            path: '/card/:id',  // 🆕 独立路由，不嵌套
            name: 'CardDetail',
            component: () => import('@/views/CardDetail.vue'),
            props: true
        },
        {
            path: '/post',
            name: 'PostEditor',
            component: () => import('@/views/PostEditor.vue')
        }
    ]
})

router.beforeEach((to, from, next) => {
    const isUnauthorized = unauthorized()
    if(to.name.startsWith('welcome-') && !isUnauthorized)       // 用户如果已经登录了，访问登录页面时会自动跳转到主页面（/index）。
        next('/index')
    else if(to.fullPath.startsWith('/index') && isUnauthorized) // 用户未登录时，尝试访问主页面会被重定向到登录页面。    
        next('/')
    else
        next()
})

export default router