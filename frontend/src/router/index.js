import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Error from '../views/Error.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'  // 默认重定向到登录页
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/error/:code',
    name: 'Error',
    component: Error
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/error/404'  // 捕获所有未匹配的路由，重定向到404错误页
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 