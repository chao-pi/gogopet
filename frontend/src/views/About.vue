<script setup>
// 可以添加必要的逻辑
</script>

<template>
  <div class="about-page">
    <!-- 顶部横幅 -->
    <div class="banner">
      <div class="banner-content">
        <h1 class="text-6xl font-bold text-white mb-8">关于 GoGoPet</h1>
        <p class="text-2xl text-white/90">让宠物托运更安全、更透明、更智能</p>
      </div>
    </div>

    <!-- 项目概述 -->
    <div class="overview-section">
      <div class="section-content">
        <h2 class="section-title">项目概述</h2>
        <div class="overview-content">
          <div class="overview-text">
            <p class="text-gray-600 leading-relaxed text-xl mb-8">
              GoGoPet 是为宠物主人和托运公司设计的智能化管理平台，旨在解决传统宠物托运中信息不透明、服务不规范等问题。系统通过数字化手段提供订单管理、实时追踪、社交互动等功能，提升托运效率和安全性。
            </p>
            <div class="overview-stats">
              <div class="stat-item">
                <div class="stat-number">10,000+</div>
                <div class="stat-label">服务用户</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">500+</div>
                <div class="stat-label">合作公司</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">99.9%</div>
                <div class="stat-label">安全率</div>
              </div>
            </div>
          </div>
          <div class="overview-image">
            <img src="@/assets/images/about-overview.jpg" alt="项目概述">
          </div>
        </div>
      </div>
    </div>

    <!-- 核心功能 -->
    <div class="features-section">
      <div class="section-content">
        <h2 class="section-title">核心功能</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">
              <i class="fas fa-paw"></i>
            </div>
            <div class="feature-content">
              <h3>用户管理</h3>
              <ul>
                <li>支持手机号、验证码及第三方登录</li>
                <li>托运公司资质审核</li>
                <li>完善的个人信息管理</li>
              </ul>
            </div>
            <div class="feature-image">
              <img src="@/assets/images/feature-user.jpg" alt="用户管理">
            </div>
          </div>

          <div class="feature-card">
            <div class="feature-icon">
              <i class="fas fa-map-marker-alt"></i>
            </div>
            <div class="feature-content">
              <h3>托运追踪</h3>
              <ul>
                <li>实时GPS轨迹追踪</li>
                <li>异常偏离预警</li>
                <li>宠物状态实时更新</li>
              </ul>
            </div>
            <div class="feature-image">
              <img src="@/assets/images/feature-tracking.jpg" alt="托运追踪">
            </div>
          </div>

          <div class="feature-card">
            <div class="feature-icon">
              <i class="fas fa-comments"></i>
            </div>
            <div class="feature-content">
              <h3>社交互动</h3>
              <ul>
                <li>订单评价系统</li>
                <li>社区经验分享</li>
                <li>用户互动交流</li>
              </ul>
            </div>
            <div class="feature-image">
              <img src="@/assets/images/feature-social.jpg" alt="社交互动">
            </div>
          </div>

          <div class="feature-card">
            <div class="feature-icon">
              <i class="fas fa-robot"></i>
            </div>
            <div class="feature-content">
              <h3>智能客服</h3>
              <ul>
                <li>智能问答系统</li>
                <li>自动转接人工客服</li>
                <li>7x24小时在线服务</li>
              </ul>
            </div>
            <div class="feature-image">
              <img src="@/assets/images/feature-service.jpg" alt="智能客服">
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 技术架构 -->
    <div class="tech-section">
      <div class="section-content">
        <h2 class="section-title">技术架构</h2>
        <div class="tech-grid">
          <div class="tech-card">
            <div class="tech-icon">
              <i class="fas fa-code"></i>
            </div>
            <h3>前端技术</h3>
            <ul>
              <li>Vue 3</li>
              <li>Vite</li>
              <li>Tailwind CSS</li>
              <li>Element Plus</li>
            </ul>
          </div>
          <div class="tech-card">
            <div class="tech-icon">
              <i class="fas fa-server"></i>
            </div>
            <h3>后端技术</h3>
            <ul>
              <li>Spring Boot</li>
              <li>MyBatis</li>
              <li>Spring Security</li>
              <li>Redis</li>
            </ul>
          </div>
          <div class="tech-card">
            <div class="tech-icon">
              <i class="fas fa-database"></i>
            </div>
            <h3>数据库</h3>
            <ul>
              <li>MySQL 8.0</li>
              <li>MongoDB</li>
              <li>Elasticsearch</li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <!-- 团队介绍 -->
    <div class="team-section">
      <div class="section-content">
        <h2 class="section-title">我们的团队</h2>
        <div class="team-grid">
          <div class="team-member">
            <img src="@/assets/images/team1.jpg" alt="团队成员">
            <h4>张明</h4>
            <p>技术总监</p>
          </div>
          <div class="team-member">
            <img src="@/assets/images/team2.jpg" alt="团队成员">
            <h4>李华</h4>
            <p>产品经理</p>
          </div>
          <div class="team-member">
            <img src="@/assets/images/team3.jpg" alt="团队成员">
            <h4>王芳</h4>
            <p>UI设计师</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.about-page {
  min-height: 100vh;
}

/* 横幅样式 */
.banner {
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('@/assets/images/about-banner.jpg');
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

/* 通用部分样式 */
.section-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 6rem 2rem;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  font-weight: bold;
  color: #1f2937;
  margin-bottom: 3rem;
}

/* 项目概述样式 */
.overview-section {
  background-color: white;
}

.overview-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
}

.overview-text {
  padding-right: 2rem;
}

.overview-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  margin-top: 3rem;
}

.stat-item {
  text-align: center;
  padding: 1.5rem;
  background: #f9fafb;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.stat-number {
  font-size: 2.5rem;
  font-weight: bold;
  color: #f97316;
  margin-bottom: 0.5rem;
}

.stat-label {
  color: #6b7280;
  font-size: 1.125rem;
}

.overview-image img {
  width: 100%;
  border-radius: 1rem;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
}

/* 核心功能样式 */
.features-section {
  background-color: #f9fafb;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: white;
  border-radius: 1rem;
  overflow: hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 2rem auto;
}

.feature-icon i {
  font-size: 1.5rem;
  color: white;
}

.feature-content {
  padding: 0 2rem 2rem;
}

.feature-content h3 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 1rem;
  text-align: center;
}

.feature-content ul {
  list-style: none;
  padding: 0;
}

.feature-content li {
  color: #6b7280;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
}

.feature-content li::before {
  content: '✓';
  color: #10b981;
  margin-right: 0.5rem;
}

.feature-image img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

/* 技术架构样式 */
.tech-section {
  background-color: white;
}

.tech-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.tech-card {
  background: white;
  padding: 2rem;
  border-radius: 1rem;
  text-align: center;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.tech-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
}

.tech-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
}

.tech-icon i {
  font-size: 2rem;
  color: white;
}

.tech-card h3 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 1rem;
}

.tech-card ul {
  list-style: none;
  padding: 0;
}

.tech-card li {
  color: #6b7280;
  margin-bottom: 0.5rem;
}

/* 团队介绍样式 */
.team-section {
  background-color: #f9fafb;
}

.team-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 2rem;
}

.team-member {
  text-align: center;
}

.team-member img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 1rem;
  border: 4px solid white;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.team-member h4 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.team-member p {
  color: #6b7280;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .section-content {
    padding: 4rem 1rem;
  }

  .overview-content {
    grid-template-columns: 1fr;
  }

  .overview-text {
    padding-right: 0;
  }

  .banner h1 {
    font-size: 2.5rem;
  }

  .section-title {
    font-size: 2rem;
  }
}
</style>