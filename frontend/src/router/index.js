import { createRouter, createWebHistory } from 'vue-router'
import Error from '../components/Error.vue'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import Login from '../views/Login.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
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