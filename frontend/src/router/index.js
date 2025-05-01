import { createRouter, createWebHistory } from 'vue-router'
import Error from '../views/base/Error.vue'
import Home from '@/views/base/Home.vue'
import About from '@/views/base/About.vue'
import Login from '@/views/auth/Login.vue'
import Register from '@/views/auth/Register.vue'
import Profile from '@/views/user/Profile.vue'
import Pets from '@/views/user/Pets.vue'
import Transport from '@/views/transport/Transport.vue'
import OrderForm from '@/views/transport/OrderForm.vue'

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
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  {
    path: '/pets',
    name: 'Pets',
    component: Pets,
    meta: { requiresAuth: true }
  },
  {
    path: '/transport',
    name: 'Transport',
    component: Transport
  },
  {
    path: '/order/create',
    name: 'OrderForm',
    component: OrderForm,
    meta: { requiresAuth: true }
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

// 路由守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router