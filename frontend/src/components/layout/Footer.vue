<template>
  <footer class="custom-footer text-white text-center">
    <div class="mb-4">
      <i class="fas fa-paw mb-2"></i> <!-- 宠物爪印图标 -->
      <br>联系我们
    </div>
    <div>电话: 123-456-7890</div>
    <div>邮箱: contact@gogopet.com</div>
    <div class="mt-4 social-icons">
      <a href="#" class="icon-link"><i class="fab fa-facebook-f"></i></a>
      <a href="#" class="icon-link"><i class="fab fa-twitter"></i></a>
      <a href="#" class="icon-link"><i class="fab fa-instagram"></i></a>
    </div>
    <div class="mt-4">© 2025 GoGoPet. 关爱每一只宠物。</div>
  </footer>
</template>

<script setup>
// 可以添加更多逻辑
</script>

<style scoped>
.custom-footer {
  background: linear-gradient(180deg, #e67e22 0%, #d35400 100%); /* 橙色系渐变 */
  padding: 3rem 2rem;
  box-shadow: 0 -4px 6px -1px rgba(0, 0, 0, 0.1), 0 -2px 4px -1px rgba(0, 0, 0, 0.06);
}

.social-icons {
  margin-top: 1.5rem;
}

.icon-link {
  color: white;
  margin: 0 0.75rem;
  font-size: 1.25rem;
  transition: color 0.3s ease;
}

.icon-link:hover {
  color: #f1c40f;
}

@media (max-width: 640px) {
  .custom-footer {
    padding: 2rem 1rem;
  }
}
</style>

export default {
  name: "FooterComponent",
}
