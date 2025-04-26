<script setup>

</script>

<template>
  <div class="home-page">
    <!-- 顶部横幅 -->
    <div class="banner">
      <div class="banner-content">
        <h1 class="text-4xl md:text-5xl font-bold text-white mb-6">让宠物托运更安全、更智能</h1>
        <p class="text-xl text-white/90 mb-8">专业的宠物托运服务，全程实时追踪，让您放心无忧</p>
        <button class="cta-button">
          <i class="fas fa-paw mr-2"></i>
          立即预约
        </button>
      </div>
    </div>

    <!-- 服务特点 -->
    <div class="features-section">
      <h2 class="section-title">我们的服务特点</h2>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-shield-alt"></i>
          </div>
          <h3>安全可靠</h3>
          <p>专业的宠物运输团队，确保您的宠物安全到达目的地</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-map-marker-alt"></i>
          </div>
          <h3>实时追踪</h3>
          <p>全程GPS定位，随时查看宠物位置和状态</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-comments"></i>
          </div>
          <h3>专业客服</h3>
          <p>24小时在线客服，随时解答您的疑问</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-heart"></i>
          </div>
          <h3>贴心服务</h3>
          <p>专业的宠物护理，让您的宠物舒适旅行</p>
        </div>
      </div>
    </div>

    <!-- 服务流程 -->
    <div class="process-section">
      <h2 class="section-title">服务流程</h2>
      <div class="process-steps">
        <div class="process-step">
          <div class="step-number">1</div>
          <div class="step-content">
            <h3>在线预约</h3>
            <p>填写宠物信息和运输需求</p>
          </div>
        </div>
        <div class="process-step">
          <div class="step-number">2</div>
          <div class="step-content">
            <h3>确认订单</h3>
            <p>客服联系确认细节</p>
          </div>
        </div>
        <div class="process-step">
          <div class="step-number">3</div>
          <div class="step-content">
            <h3>上门接宠</h3>
            <p>专业人员上门服务</p>
          </div>
        </div>
        <div class="process-step">
          <div class="step-number">4</div>
          <div class="step-content">
            <h3>安全送达</h3>
            <p>实时追踪，安全送达</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 用户评价 -->
    <div class="testimonials-section">
      <h2 class="section-title">用户评价</h2>
      <div class="testimonials-grid">
        <div class="testimonial-card">
          <div class="testimonial-content">
            <p>"服务非常专业，我的猫咪安全到达，全程可以查看位置，很放心！"</p>
          </div>
          <div class="testimonial-author">
            <img src="../../assets/images/users/avatar1.jpg" alt="用户头像" class="author-avatar">
            <div class="author-info">
              <h4>张小姐</h4>
              <p>上海</p>
            </div>
          </div>
        </div>
        <div class="testimonial-card">
          <div class="testimonial-content">
            <p>"客服态度很好，运输过程很顺利，我的狗狗一点都没有不适，非常满意！"</p>
          </div>
          <div class="testimonial-author">
            <img src="../../assets/images/users/avatar2.jpg" alt="用户头像" class="author-avatar">
            <div class="author-info">
              <h4>李先生</h4>
              <p>北京</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 联系我们 -->
    <div class="contact-section">
      <h2 class="section-title">联系我们</h2>
      <div class="contact-content">
        <div class="contact-info">
          <div class="contact-item">
            <i class="fas fa-phone"></i>
            <p>400-123-4567</p>
          </div>
          <div class="contact-item">
            <i class="fas fa-envelope"></i>
            <p>service@gogopet.com</p>
          </div>
          <div class="contact-item">
            <i class="fas fa-map-marker-alt"></i>
            <p>上海市浦东新区张江高科技园区</p>
          </div>
        </div>
        <div class="contact-image">
          <img src="../../assets/images/common/contact.jpg" alt="联系我们">
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-page {
  min-height: 100vh;
}

/* 横幅样式 */
.banner {
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('@/assets/images/banners/banner.jpg');
  background-size: cover;
  background-position: center;
  height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
  padding: 2rem;
}

.banner-content {
  max-width: 800px;
}

.cta-button {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  color: white;
  padding: 1rem 2rem;
  border-radius: 0.5rem;
  font-size: 1.25rem;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
}

.cta-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(249, 115, 22, 0.1), 0 2px 4px -1px rgba(249, 115, 22, 0.06);
}

/* 通用部分样式 */
.section-title {
  text-align: center;
  font-size: 2.5rem;
  font-weight: bold;
  color: #1f2937;
  margin-bottom: 3rem;
}

/* 服务特点样式 */
.features-section {
  padding: 6rem 2rem;
  background-color: #f9fafb;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  background: white;
  padding: 2rem;
  border-radius: 1rem;
  text-align: center;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
}

.feature-icon i {
  font-size: 2rem;
  color: white;
}

.feature-card h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 1rem;
  color: #1f2937;
}

.feature-card p {
  color: #6b7280;
  line-height: 1.6;
}

/* 服务流程样式 */
.process-section {
  padding: 6rem 2rem;
  background-color: white;
}

.process-steps {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.process-step {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.step-number {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
}

.step-content h3 {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #1f2937;
}

.step-content p {
  color: #6b7280;
}

/* 用户评价样式 */
.testimonials-section {
  padding: 6rem 2rem;
  background-color: #f9fafb;
}

.testimonials-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.testimonial-card {
  background: white;
  padding: 2rem;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.testimonial-content {
  margin-bottom: 1.5rem;
}

.testimonial-content p {
  color: #4b5563;
  line-height: 1.6;
  font-style: italic;
}

.testimonial-author {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.author-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.author-info h4 {
  font-weight: 600;
  color: #1f2937;
}

.author-info p {
  color: #6b7280;
  font-size: 0.875rem;
}

/* 联系我们样式 */
.contact-section {
  padding: 6rem 2rem;
  background-color: white;
}

.contact-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 4rem;
  max-width: 1200px;
  margin: 0 auto;
  align-items: center;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.contact-item i {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.contact-item p {
  color: #4b5563;
  font-size: 1.125rem;
}

.contact-image img {
  width: 100%;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner {
    height: 50vh;
  }

  .section-title {
    font-size: 2rem;
  }

  .contact-content {
    grid-template-columns: 1fr;
  }
}
</style>