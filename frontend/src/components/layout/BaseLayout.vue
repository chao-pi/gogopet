<script setup>
import Header from './Header.vue'
import Footer from './Footer.vue'
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <Header />
    <main class="flex-1 bg-gray-50 p-6 flex flex-col min-h-[calc(100vh-200px)]">
      <slot />
    </main>
    <Footer />
  </div>
</template>

<style scoped>
.min-h-screen {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1 0 auto;
}

/* 可以添加更多样式 */
</style>
