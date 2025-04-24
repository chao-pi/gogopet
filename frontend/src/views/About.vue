<script setup>
// 可以添加必要的逻辑
</script>

<template>
  <div class="about-container">
    <div class="max-w-6xl mx-auto px-8 py-16">
      <!-- 项目标题 -->
      <div class="text-center mb-20">
        <h1 class="text-6xl font-bold text-orange-600 mb-8">GoGoPet 宠物托运智能服务系统</h1>
        <p class="text-3xl text-gray-600">让宠物托运更安全、更透明、更智能</p>
      </div>

      <!-- 项目概述 -->
      <div class="bg-white rounded-xl shadow-xl p-12 mb-16 relative overflow-hidden">
        <div class="absolute top-0 right-0 w-32 h-32 bg-orange-100 rounded-full -mr-16 -mt-16"></div>
        <div class="absolute bottom-0 left-0 w-32 h-32 bg-orange-100 rounded-full -ml-16 -mb-16"></div>
        <h2 class="text-4xl font-bold text-gray-800 mb-8 relative">项目概述</h2>
        <p class="text-gray-600 leading-relaxed text-xl relative">
          GoGoPet 是为宠物主人和托运公司设计的智能化管理平台，旨在解决传统宠物托运中信息不透明、服务不规范等问题。系统通过数字化手段提供订单管理、实时追踪、社交互动等功能，提升托运效率和安全性。
        </p>
      </div>

      <!-- 核心功能 -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-12 mb-16">
        <div class="bg-white rounded-xl shadow-xl p-10 relative overflow-hidden">
          <div class="absolute top-0 right-0 w-24 h-24 bg-orange-100 rounded-full -mr-12 -mt-12"></div>
          <div class="flex items-center mb-8">
            <i class="fas fa-paw text-4xl text-orange-500 mr-5"></i>
            <h3 class="text-3xl font-semibold text-gray-800">用户管理</h3>
          </div>
          <ul class="text-gray-600 space-y-4 text-xl">
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>支持手机号、验证码及第三方登录</span>
            </li>
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>托运公司资质审核</span>
            </li>
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>完善的个人信息管理</span>
            </li>
          </ul>
        </div>

        <div class="bg-white rounded-xl shadow-xl p-10 relative overflow-hidden">
          <div class="absolute bottom-0 left-0 w-24 h-24 bg-orange-100 rounded-full -ml-12 -mb-12"></div>
          <div class="flex items-center mb-8">
            <i class="fas fa-map-marker-alt text-4xl text-orange-500 mr-5"></i>
            <h3 class="text-3xl font-semibold text-gray-800">托运追踪</h3>
          </div>
          <ul class="text-gray-600 space-y-4 text-xl">
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>实时GPS轨迹追踪</span>
            </li>
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>异常偏离预警</span>
            </li>
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>宠物状态实时更新</span>
            </li>
          </ul>
        </div>

        <div class="bg-white rounded-xl shadow-xl p-10 relative overflow-hidden">
          <div class="absolute top-0 right-0 w-24 h-24 bg-orange-100 rounded-full -mr-12 -mt-12"></div>
          <div class="flex items-center mb-8">
            <i class="fas fa-comments text-4xl text-orange-500 mr-5"></i>
            <h3 class="text-3xl font-semibold text-gray-800">社交互动</h3>
          </div>
          <ul class="text-gray-600 space-y-4 text-xl">
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>订单评价系统</span>
            </li>
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>社区经验分享</span>
            </li>
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>用户互动交流</span>
            </li>
          </ul>
        </div>

        <div class="bg-white rounded-xl shadow-xl p-10 relative overflow-hidden">
          <div class="absolute bottom-0 left-0 w-24 h-24 bg-orange-100 rounded-full -ml-12 -mb-12"></div>
          <div class="flex items-center mb-8">
            <i class="fas fa-robot text-4xl text-orange-500 mr-5"></i>
            <h3 class="text-3xl font-semibold text-gray-800">智能客服</h3>
          </div>
          <ul class="text-gray-600 space-y-4 text-xl">
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>智能问答系统</span>
            </li>
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>自动转接人工客服</span>
            </li>
            <li class="flex items-start">
              <i class="fas fa-check text-green-500 mt-2 mr-4"></i>
              <span>7x24小时在线服务</span>
            </li>
          </ul>
        </div>
      </div>

      <!-- 技术架构 -->
      <div class="bg-white rounded-xl shadow-xl p-12 relative overflow-hidden">
        <div class="absolute top-0 right-0 w-32 h-32 bg-orange-100 rounded-full -mr-16 -mt-16"></div>
        <div class="absolute bottom-0 left-0 w-32 h-32 bg-orange-100 rounded-full -ml-16 -mb-16"></div>
        <h2 class="text-4xl font-bold text-gray-800 mb-10 relative">技术架构</h2>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-10">
          <div class="text-center relative">
            <i class="fas fa-code text-6xl text-orange-500 mb-6"></i>
            <h3 class="text-2xl font-semibold text-gray-800 mb-4">前端</h3>
            <p class="text-gray-600 text-xl">Vue 3 + Vite + Ant Design</p>
          </div>
          <div class="text-center relative">
            <i class="fas fa-server text-6xl text-orange-500 mb-6"></i>
            <h3 class="text-2xl font-semibold text-gray-800 mb-4">后端</h3>
            <p class="text-gray-600 text-xl">Spring Boot + MyBatis</p>
          </div>
          <div class="text-center relative">
            <i class="fas fa-database text-6xl text-orange-500 mb-6"></i>
            <h3 class="text-2xl font-semibold text-gray-800 mb-4">数据库</h3>
            <p class="text-gray-600 text-xl">MySQL 8.0</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.about-container {
  background: linear-gradient(135deg, #f9fafb 0%, #f3f4f6 100%);
  min-height: calc(100vh - 64px);
  position: relative;
  overflow: hidden;
}

.about-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23f97316' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  opacity: 0.5;
}

/* 添加动画效果 */
.bg-white {
  transition: all 0.3s ease;
  position: relative;
}

.bg-white:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}
</style>