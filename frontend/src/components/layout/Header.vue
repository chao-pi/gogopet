<template>
  <header class="bg-blue-500 text-white p-4 flex justify-between items-center shadow-md">
    <div class="text-xl font-bold">GoGoPet</div>
    <nav class="md:flex hidden">
      <router-link to="/" class="mx-4 hover:underline">首页</router-link>
      <router-link to="/about" class="mx-4 hover:underline">关于我们</router-link>
    </nav>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
</script>

<style scoped>
/* 可以添加更多样式 */
</style>