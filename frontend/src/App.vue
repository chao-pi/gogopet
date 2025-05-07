<script setup>
import BaseLayout from '@/components/layout/BaseLayout.vue'
</script>

<template>
  <BaseLayout>
    <router-view />
  </BaseLayout>
</template>

<style>
#app {
  font-family: Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0;
}
</style>
