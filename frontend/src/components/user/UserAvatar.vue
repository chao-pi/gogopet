<template>
  <div class="user-avatar flex items-center">
    <div class="avatar-container relative">
      <img
        v-if="avatarUrl"
        :src="avatarUrl"
        :alt="username"
        class="w-10 h-10 rounded-full object-cover"
        @error="handleImageError"
      />
      <div
        v-else
        class="default-avatar w-10 h-10 rounded-full bg-gray-300 flex items-center justify-center"
      >
        <i class="fas fa-user text-gray-600"></i>
      </div>
    </div>
    <span v-if="showUsername" class="ml-2 text-white">{{ username }}</span>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  avatarUrl: {
    type: String,
    default: ''
  },
  username: {
    type: String,
    default: ''
  },
  showUsername: {
    type: Boolean,
    default: true
  }
})

const handleImageError = (e) => {
  e.target.style.display = 'none'
  e.target.parentElement.querySelector('.default-avatar').style.display = 'flex'
}
</script>

<style scoped>
.user-avatar {
  cursor: pointer;
}

.avatar-container {
  transition: transform 0.3s ease;
}

.avatar-container:hover {
  transform: scale(1.05);
}

.default-avatar {
  border: 2px solid rgba(255, 255, 255, 0.2);
}
</style> 