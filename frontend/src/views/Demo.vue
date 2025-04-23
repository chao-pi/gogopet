<template>
    <div class="min-h-screen bg-gray-50">
      <!-- 导航栏 -->
      <nav class="bg-white shadow-md">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex justify-between h-16">
            <div class="flex">
              <div class="flex-shrink-0 flex items-center">
                <img class="h-10 w-auto" src="/placeholder.svg?height=40&width=40" alt="宠物托运" />
                <span class="ml-2 text-xl font-bold text-primary-600">宠宠出行</span>
              </div>
              <div class="hidden sm:ml-6 sm:flex sm:space-x-8">
                <a 
                  v-for="(item, index) in navItems" 
                  :key="index"
                  @click="currentPage = item.page"
                  :class="[
                    currentPage === item.page 
                      ? 'border-primary-500 text-gray-900' 
                      : 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700',
                    'inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium cursor-pointer'
                  ]"
                >
                  {{ item.name }}
                </a>
              </div>
            </div>
            <div class="hidden sm:ml-6 sm:flex sm:items-center">
              <button class="bg-white p-1 rounded-full text-gray-400 hover:text-gray-500 focus:outline-none">
                <span class="sr-only">查看通知</span>
                <BellIcon class="h-6 w-6" />
              </button>
              <div class="ml-3 relative">
                <div>
                  <button @click="userMenuOpen = !userMenuOpen" class="flex text-sm border-2 border-transparent rounded-full focus:outline-none focus:border-gray-300">
                    <img class="h-8 w-8 rounded-full" src="/placeholder.svg?height=32&width=32" alt="用户头像" />
                  </button>
                </div>
                <div v-if="userMenuOpen" class="origin-top-right absolute right-0 mt-2 w-48 rounded-md shadow-lg py-1 bg-white ring-1 ring-black ring-opacity-5 z-10">
                  <a @click="currentPage = 'profile'" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 cursor-pointer">个人中心</a>
                  <a @click="currentPage = 'orders'" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 cursor-pointer">订单管理</a>
                  <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">退出登录</a>
                </div>
              </div>
            </div>
            <div class="-mr-2 flex items-center sm:hidden">
              <button @click="mobileMenuOpen = !mobileMenuOpen" class="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none">
                <MenuIcon v-if="!mobileMenuOpen" class="h-6 w-6" />
                <XIcon v-else class="h-6 w-6" />
              </button>
            </div>
          </div>
        </div>
  
        <!-- 移动端菜单 -->
        <div v-if="mobileMenuOpen" class="sm:hidden">
          <div class="pt-2 pb-3 space-y-1">
            <a 
              v-for="(item, index) in navItems" 
              :key="index"
              @click="currentPage = item.page; mobileMenuOpen = false"
              :class="[
                currentPage === item.page 
                  ? 'bg-primary-50 border-primary-500 text-primary-700' 
                  : 'border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-gray-700',
                'block pl-3 pr-4 py-2 border-l-4 text-base font-medium cursor-pointer'
              ]"
            >
              {{ item.name }}
            </a>
          </div>
          <div class="pt-4 pb-3 border-t border-gray-200">
            <div class="flex items-center px-4">
              <div class="flex-shrink-0">
                <img class="h-10 w-10 rounded-full" src="/placeholder.svg?height=40&width=40" alt="用户头像" />
              </div>
              <div class="ml-3">
                <div class="text-base font-medium text-gray-800">王小明</div>
                <div class="text-sm font-medium text-gray-500">wangxm@example.com</div>
              </div>
            </div>
            <div class="mt-3 space-y-1">
              <a @click="currentPage = 'profile'; mobileMenuOpen = false" class="block px-4 py-2 text-base font-medium text-gray-500 hover:text-gray-800 hover:bg-gray-100 cursor-pointer">个人中心</a>
              <a @click="currentPage = 'orders'; mobileMenuOpen = false" class="block px-4 py-2 text-base font-medium text-gray-500 hover:text-gray-800 hover:bg-gray-100 cursor-pointer">订单管理</a>
              <a href="#" class="block px-4 py-2 text-base font-medium text-gray-500 hover:text-gray-800 hover:bg-gray-100">退出登录</a>
            </div>
          </div>
        </div>
      </nav>
  
      <!-- 主内容区域 -->
      <main>
        <!-- 首页 -->
        <section v-if="currentPage === 'home'" class="py-10">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <!-- 欢迎标语 -->
            <div class="bg-gradient-to-r from-primary-500 to-secondary-500 rounded-xl shadow-lg overflow-hidden mb-10">
              <div class="md:flex">
                <div class="p-8 md:w-1/2">
                  <h1 class="text-3xl font-bold text-white mb-4">让爱宠的旅行更安心</h1>
                  <p class="text-white text-opacity-90 mb-6">专业宠物托运服务，全程监控，安全可靠，让您和爱宠无忧出行</p>
                  <button @click="currentPage = 'transport'" class="bg-white text-primary-600 font-bold py-3 px-6 rounded-lg shadow-md hover:bg-gray-100 transition duration-300">
                    立即下单
                  </button>
                </div>
                <div class="md:w-1/2">
                  <img src="/placeholder.svg?height=300&width=500" alt="宠物托运" class="w-full h-full object-cover" />
                </div>
              </div>
            </div>
  
            <!-- 特色服务 -->
            <div class="mb-16">
              <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">我们的特色服务</h2>
              <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div v-for="(feature, index) in features" :key="index" class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition duration-300">
                  <div class="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center mb-4">
                    <component :is="feature.icon" class="h-6 w-6 text-primary-600" />
                  </div>
                  <h3 class="text-lg font-semibold text-gray-800 mb-2">{{ feature.title }}</h3>
                  <p class="text-gray-600">{{ feature.description }}</p>
                </div>
              </div>
            </div>
  
            <!-- 服务流程 -->
            <div class="mb-16">
              <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">服务流程</h2>
              <div class="flex flex-col md:flex-row justify-between items-start">
                <div v-for="(step, index) in serviceSteps" :key="index" class="flex flex-col items-center mb-6 md:mb-0 w-full md:w-1/4 px-4">
                  <div class="w-16 h-16 bg-secondary-100 rounded-full flex items-center justify-center mb-4">
                    <span class="text-xl font-bold text-secondary-600">{{ index + 1 }}</span>
                  </div>
                  <h3 class="text-lg font-semibold text-gray-800 mb-2 text-center">{{ step.title }}</h3>
                  <p class="text-gray-600 text-center">{{ step.description }}</p>
                </div>
              </div>
            </div>
  
            <!-- 用户评价 -->
            <div>
              <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">用户评价</h2>
              <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div v-for="(review, index) in reviews" :key="index" class="bg-white rounded-lg shadow-md p-6">
                  <div class="flex items-center mb-4">
                    <img :src="`/placeholder.svg?height=40&width=40&text=${review.user.charAt(0)}`" alt="用户头像" class="w-10 h-10 rounded-full mr-4" />
                    <div>
                      <h4 class="font-semibold text-gray-800">{{ review.user }}</h4>
                      <div class="flex">
                        <StarIcon v-for="i in 5" :key="i" :class="[i <= review.rating ? 'text-yellow-400' : 'text-gray-300', 'h-5 w-5']" />
                      </div>
                    </div>
                  </div>
                  <p class="text-gray-600">{{ review.comment }}</p>
                </div>
              </div>
            </div>
          </div>
        </section>
  
        <!-- 个人中心 -->
        <section v-if="currentPage === 'profile'" class="py-10">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h1 class="text-2xl font-bold text-gray-800 mb-6">个人中心</h1>
            
            <div class="bg-white shadow rounded-lg overflow-hidden mb-8">
              <div class="p-6">
                <h2 class="text-lg font-semibold text-gray-800 mb-4">个人信息</h2>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
                    <input type="text" v-model="userProfile.username" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">手机号码</label>
                    <input type="tel" v-model="userProfile.phone" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">邮箱</label>
                    <input type="email" v-model="userProfile.email" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">常用地址</label>
                    <input type="text" v-model="userProfile.address" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                  </div>
                </div>
                <div class="mt-6">
                  <button class="bg-primary-600 text-white px-4 py-2 rounded-md hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500">
                    保存信息
                  </button>
                </div>
              </div>
            </div>
            
            <div class="bg-white shadow rounded-lg overflow-hidden">
              <div class="p-6">
                <div class="flex justify-between items-center mb-4">
                  <h2 class="text-lg font-semibold text-gray-800">宠物档案</h2>
                  <button @click="showAddPetForm = !showAddPetForm" class="bg-secondary-600 text-white px-3 py-1 rounded-md text-sm hover:bg-secondary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-secondary-500">
                    {{ showAddPetForm ? '取消' : '添加宠物' }}
                  </button>
                </div>
                
                <!-- 添加宠物表单 -->
                <div v-if="showAddPetForm" class="bg-gray-50 p-4 rounded-md mb-6">
                  <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">宠物名称</label>
                      <input type="text" v-model="newPet.name" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">宠物类型</label>
                      <select v-model="newPet.type" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500">
                        <option value="dog">狗</option>
                        <option value="cat">猫</option>
                        <option value="other">其他</option>
                      </select>
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">品种</label>
                      <input type="text" v-model="newPet.breed" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">年龄</label>
                      <input type="number" v-model="newPet.age" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                    </div>
                  </div>
                  <div class="mb-4">
                    <label class="block text-sm font-medium text-gray-700 mb-1">上传疫苗记录</label>
                    <div class="flex items-center">
                      <label class="block w-full">
                        <span class="sr-only">选择文件</span>
                        <input type="file" class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-primary-50 file:text-primary-700 hover:file:bg-primary-100" />
                      </label>
                    </div>
                  </div>
                  <div class="flex justify-end">
                    <button @click="addPet" class="bg-primary-600 text-white px-4 py-2 rounded-md hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500">
                      保存宠物信息
                    </button>
                  </div>
                </div>
                
                <!-- 宠物列表 -->
                <div v-if="pets.length > 0" class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div v-for="(pet, index) in pets" :key="index" class="border border-gray-200 rounded-md p-4 flex">
                    <img :src="`/placeholder.svg?height=80&width=80&text=${pet.type}`" alt="宠物照片" class="w-20 h-20 rounded-md object-cover mr-4" />
                    <div class="flex-1">
                      <h3 class="font-semibold text-gray-800">{{ pet.name }}</h3>
                      <p class="text-sm text-gray-600">{{ pet.breed }}，{{ pet.age }}岁</p>
                      <div class="mt-2 flex space-x-2">
                        <button class="text-secondary-600 hover:text-secondary-800 text-sm">编辑</button>
                        <button @click="removePet(index)" class="text-red-600 hover:text-red-800 text-sm">删除</button>
                      </div>
                    </div>
                  </div>
                </div>
                
                <div v-else-if="!showAddPetForm" class="text-center py-8 text-gray-500">
                  您还没有添加宠物信息，点击"添加宠物"开始创建宠物档案
                </div>
              </div>
            </div>
          </div>
        </section>
  
        <!-- 订单管理 -->
        <section v-if="currentPage === 'orders'" class="py-10">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h1 class="text-2xl font-bold text-gray-800 mb-6">订单管理</h1>
            
            <!-- 订单分类标签 -->
            <div class="bg-white shadow rounded-lg mb-6">
              <div class="border-b border-gray-200">
                <nav class="flex -mb-px">
                  <a 
                    v-for="(tab, index) in orderTabs" 
                    :key="index"
                    @click="currentOrderTab = tab.id"
                    :class="[
                      currentOrderTab === tab.id 
                        ? 'border-primary-500 text-primary-600' 
                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300',
                      'whitespace-nowrap py-4 px-6 border-b-2 font-medium text-sm cursor-pointer'
                    ]"
                  >
                    {{ tab.name }}
                    <span 
                      v-if="tab.count > 0" 
                      :class="[
                        currentOrderTab === tab.id 
                          ? 'bg-primary-100 text-primary-600' 
                          : 'bg-gray-100 text-gray-900',
                        'ml-2 py-0.5 px-2.5 rounded-full text-xs'
                      ]"
                    >
                      {{ tab.count }}
                    </span>
                  </a>
                </nav>
              </div>
            </div>
            
            <!-- 订单列表 -->
            <div v-if="filteredOrders.length > 0" class="space-y-4">
              <div 
                v-for="(order, index) in filteredOrders" 
                :key="index" 
                class="bg-white shadow rounded-lg overflow-hidden"
              >
                <div class="p-6">
                  <div class="flex justify-between items-start mb-4">
                    <div>
                      <h3 class="text-lg font-semibold text-gray-800">订单号：{{ order.id }}</h3>
                      <p class="text-sm text-gray-500">下单时间：{{ order.date }}</p>
                    </div>
                    <div :class="{
                      'bg-yellow-100 text-yellow-800': order.status === 'pending',
                      'bg-blue-100 text-blue-800': order.status === 'in_transit',
                      'bg-green-100 text-green-800': order.status === 'completed'
                    }" class="px-3 py-1 rounded-full text-sm font-medium">
                      {{ getStatusText(order.status) }}
                    </div>
                  </div>
                  
                  <div class="border-t border-b border-gray-200 py-4 mb-4">
                    <div class="flex items-center mb-2">
                      <MapPinIcon class="h-5 w-5 text-gray-400 mr-2" />
                      <div>
                        <p class="text-sm text-gray-500">出发地</p>
                        <p class="font-medium text-gray-800">{{ order.from }}</p>
                      </div>
                      <ArrowRightIcon class="h-5 w-5 text-gray-400 mx-4" />
                      <div>
                        <p class="text-sm text-gray-500">目的地</p>
                        <p class="font-medium text-gray-800">{{ order.to }}</p>
                      </div>
                    </div>
                    
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4">
                      <div>
                        <p class="text-sm text-gray-500">托运方式</p>
                        <p class="font-medium text-gray-800">{{ order.transportType }}</p>
                      </div>
                      <div>
                        <p class="text-sm text-gray-500">宠物信息</p>
                        <p class="font-medium text-gray-800">{{ order.petInfo }}</p>
                      </div>
                      <div>
                        <p class="text-sm text-gray-500">订单金额</p>
                        <p class="font-medium text-primary-600">¥{{ order.price.toFixed(2) }}</p>
                      </div>
                    </div>
                  </div>
                  
                  <div class="flex justify-between items-center">
                    <button @click="showOrderDetail(order)" class="text-secondary-600 hover:text-secondary-800 text-sm font-medium">
                      查看详情
                    </button>
                    <div class="space-x-2">
                      <button 
                        v-if="order.status === 'pending'" 
                        class="bg-white border border-red-600 text-red-600 px-3 py-1 rounded-md text-sm hover:bg-red-50"
                      >
                        取消订单
                      </button>
                      <button 
                        v-if="order.status === 'in_transit'" 
                        @click="currentPage = 'tracking'; trackingOrderId = order.id"
                        class="bg-secondary-600 text-white px-3 py-1 rounded-md text-sm hover:bg-secondary-700"
                      >
                        追踪订单
                      </button>
                      <button 
                        v-if="order.status === 'completed'" 
                        class="bg-primary-600 text-white px-3 py-1 rounded-md text-sm hover:bg-primary-700"
                      >
                        评价订单
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div v-else class="bg-white shadow rounded-lg p-8 text-center">
              <InboxIcon class="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <h3 class="text-lg font-medium text-gray-900 mb-2">暂无订单</h3>
              <p class="text-gray-500 mb-4">您当前没有{{ getStatusText(currentOrderTab) }}的订单</p>
              <button 
                @click="currentPage = 'transport'" 
                class="bg-primary-600 text-white px-4 py-2 rounded-md hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
              >
                立即下单
              </button>
            </div>
          </div>
        </section>
  
        <!-- 宠物托运 -->
        <section v-if="currentPage === 'transport'" class="py-10">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h1 class="text-2xl font-bold text-gray-800 mb-6">宠物托运</h1>
            
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
              <!-- 订单表单 -->
              <div class="lg:col-span-2">
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">填写托运信息</h2>
                    
                    <div class="space-y-6">
                      <!-- 地址信息 -->
                      <div>
                        <h3 class="text-md font-medium text-gray-700 mb-3">地址信息</h3>
                        <div class="grid grid-cols-1 gap-4">
                          <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">出发地</label>
                            <input type="text" v-model="transportForm.from" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                          </div>
                          <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">目的地</label>
                            <input type="text" v-model="transportForm.to" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                          </div>
                        </div>
                      </div>
                      
                      <!-- 宠物信息 -->
                      <div>
                        <h3 class="text-md font-medium text-gray-700 mb-3">宠物信息</h3>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                          <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">选择宠物</label>
                            <select v-model="transportForm.petId" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500">
                              <option v-for="(pet, index) in pets" :key="index" :value="index">
                                {{ pet.name }} ({{ pet.breed }})
                              </option>
                            </select>
                          </div>
                          <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">宠物重量 (kg)</label>
                            <input type="number" v-model="transportForm.weight" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                          </div>
                        </div>
                      </div>
                      
                      <!-- 托运方式 -->
                      <div>
                        <h3 class="text-md font-medium text-gray-700 mb-3">托运方式</h3>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                          <div 
                            v-for="(type, index) in transportTypes" 
                            :key="index" 
                            @click="transportForm.type = type.id"
                            :class="[
                              transportForm.type === type.id ? 'border-primary-500 bg-primary-50' : 'border-gray-200',
                              'border-2 rounded-lg p-4 cursor-pointer hover:border-primary-300 transition duration-200'
                            ]"
                          >
                            <div class="flex items-center justify-center mb-2">
                              <component :is="type.icon" class="h-8 w-8 text-primary-600" />
                            </div>
                            <h4 class="font-medium text-gray-800 text-center">{{ type.name }}</h4>
                            <p class="text-sm text-gray-500 text-center mt-1">{{ type.description }}</p>
                          </div>
                        </div>
                      </div>
                      
                      <!-- 托运时间 -->
                      <div>
                        <h3 class="text-md font-medium text-gray-700 mb-3">托运时间</h3>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                          <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">日期</label>
                            <input type="date" v-model="transportForm.date" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                          </div>
                          <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">时间</label>
                            <input type="time" v-model="transportForm.time" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" />
                          </div>
                        </div>
                      </div>
                      
                      <!-- 备注信息 -->
                      <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">备注信息</label>
                        <textarea v-model="transportForm.notes" rows="3" class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"></textarea>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 价格计算和AI客服 -->
              <div class="space-y-6">
                <!-- 价格计算 -->
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">价格计算</h2>
                    
                    <div class="space-y-3">
                      <div class="flex justify-between">
                        <span class="text-gray-600">基础运费</span>
                        <span class="font-medium">¥{{ calculateBasePrice() }}</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-600">重量费用</span>
                        <span class="font-medium">¥{{ calculateWeightPrice() }}</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-600">托运方式附加费</span>
                        <span class="font-medium">¥{{ calculateTypePrice() }}</span>
                      </div>
                      <div class="border-t border-gray-200 pt-3 flex justify-between">
                        <span class="font-medium text-gray-800">总计</span>
                        <span class="font-bold text-primary-600">¥{{ calculateTotalPrice() }}</span>
                      </div>
                    </div>
                    
                    <button 
                      @click="submitTransportOrder" 
                      class="w-full mt-6 bg-primary-600 text-white py-2 px-4 rounded-md hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
                    >
                      确认下单
                    </button>
                  </div>
                </div>
                
                <!-- AI客服 -->
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">AI智能客服</h2>
                    
                    <div class="bg-gray-50 rounded-lg p-4 h-64 overflow-y-auto mb-4">
                      <div v-for="(message, index) in chatMessages" :key="index" :class="[
                        message.isUser ? 'ml-auto bg-primary-100 text-primary-800' : 'mr-auto bg-gray-200 text-gray-800',
                        'max-w-xs rounded-lg p-3 mb-2'
                      ]">
                        {{ message.text }}
                      </div>
                    </div>
                    
                    <div class="flex">
                      <input 
                        type="text" 
                        v-model="chatInput" 
                        placeholder="输入您的问题..." 
                        class="flex-1 px-3 py-2 border border-gray-300 rounded-l-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" 
                        @keyup.enter="sendChatMessage"
                      />
                      <button 
                        @click="sendChatMessage" 
                        class="bg-secondary-600 text-white px-4 py-2 rounded-r-md hover:bg-secondary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-secondary-500"
                      >
                        发送
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
  
        <!-- 托运追踪 -->
        <section v-if="currentPage === 'tracking'" class="py-10">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h1 class="text-2xl font-bold text-gray-800 mb-6">托运追踪</h1>
            
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
              <!-- 地图和状态 -->
              <div class="lg:col-span-2 space-y-6">
                <!-- 地图 -->
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">实时位置</h2>
                    <div class="bg-gray-100 rounded-lg h-80 flex items-center justify-center">
                      <img src="/placeholder.svg?height=320&width=640&text=地图" alt="地图" class="w-full h-full object-cover" />
                    </div>
                  </div>
                </div>
                
                <!-- 宠物状态监控 -->
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">宠物状态监控</h2>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div class="bg-gray-100 rounded-lg h-48 flex items-center justify-center">
                        <img src="/placeholder.svg?height=192&width=320&text=摄像头1" alt="摄像头1" class="w-full h-full object-cover" />
                      </div>
                      <div class="bg-gray-100 rounded-lg h-48 flex items-center justify-center">
                        <img src="/placeholder.svg?height=192&width=320&text=摄像头2" alt="摄像头2" class="w-full h-full object-cover" />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 订单信息和聊天 -->
              <div class="space-y-6">
                <!-- 订单信息 -->
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">订单信息</h2>
                    
                    <div class="space-y-4">
                      <div>
                        <p class="text-sm text-gray-500">订单号</p>
                        <p class="font-medium text-gray-800">{{ trackingOrder.id }}</p>
                      </div>
                      <div>
                        <p class="text-sm text-gray-500">托运方式</p>
                        <p class="font-medium text-gray-800">{{ trackingOrder.transportType }}</p>
                      </div>
                      <div>
                        <p class="text-sm text-gray-500">司机信息</p>
                        <div class="flex items-center mt-1">
                          <img src="/placeholder.svg?height=40&width=40" alt="司机头像" class="w-10 h-10 rounded-full mr-3" />
                          <div>
                            <p class="font-medium text-gray-800">{{ trackingOrder.driver.name }}</p>
                            <p class="text-sm text-gray-500">{{ trackingOrder.driver.phone }}</p>
                          </div>
                        </div>
                      </div>
                      <div>
                        <p class="text-sm text-gray-500">预计到达时间</p>
                        <p class="font-medium text-gray-800">{{ trackingOrder.estimatedArrival }}</p>
                      </div>
                      <div>
                        <p class="text-sm text-gray-500">当前状态</p>
                        <div class="mt-2">
                          <div class="relative">
                            <div class="absolute inset-0 flex items-center">
                              <div class="h-0.5 w-full bg-gray-200"></div>
                            </div>
                            <div class="relative flex justify-between">
                              <div v-for="(step, index) in trackingSteps" :key="index" class="flex flex-col items-center">
                                <div :class="[
                                  index < trackingOrder.currentStep ? 'bg-green-500' : index === trackingOrder.currentStep ? 'bg-primary-500' : 'bg-gray-300',
                                  'h-5 w-5 rounded-full flex items-center justify-center'
                                ]">
                                  <CheckIcon v-if="index < trackingOrder.currentStep" class="h-3 w-3 text-white" />
                                </div>
                                <div class="text-xs text-gray-500 mt-1 w-16 text-center">{{ step }}</div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 在线沟通 -->
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">在线沟通</h2>
                    
                    <div class="bg-gray-50 rounded-lg p-4 h-64 overflow-y-auto mb-4">
                      <div v-for="(message, index) in driverMessages" :key="index" :class="[
                        message.isUser ? 'ml-auto bg-primary-100 text-primary-800' : 'mr-auto bg-gray-200 text-gray-800',
                        'max-w-xs rounded-lg p-3 mb-2'
                      ]">
                        {{ message.text }}
                      </div>
                    </div>
                    
                    <div class="flex">
                      <input 
                        type="text" 
                        v-model="driverChatInput" 
                        placeholder="输入消息..." 
                        class="flex-1 px-3 py-2 border border-gray-300 rounded-l-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500" 
                        @keyup.enter="sendDriverMessage"
                      />
                      <button 
                        @click="sendDriverMessage" 
                        class="bg-secondary-600 text-white px-4 py-2 rounded-r-md hover:bg-secondary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-secondary-500"
                      >
                        发送
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
  
        <!-- 社区交流 -->
        <section v-if="currentPage === 'community'" class="py-10">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <h1 class="text-2xl font-bold text-gray-800 mb-6">社区交流</h1>
            
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
              <!-- 用户分享 -->
              <div class="lg:col-span-2 space-y-6">
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <div class="flex justify-between items-center mb-6">
                      <h2 class="text-lg font-semibold text-gray-800">用户分享</h2>
                      <button class="bg-primary-600 text-white px-3 py-1 rounded-md text-sm hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500">
                        发布分享
                      </button>
                    </div>
                    
                    <div class="space-y-6">
                      <div v-for="(post, index) in communityPosts" :key="index" class="border-b border-gray-200 pb-6 last:border-b-0 last:pb-0">
                        <div class="flex items-center mb-3">
                          <img :src="`/placeholder.svg?height=40&width=40&text=${post.user.charAt(0)}`" alt="用户头像" class="w-10 h-10 rounded-full mr-3" />
                          <div>
                            <p class="font-medium text-gray-800">{{ post.user }}</p>
                            <p class="text-xs text-gray-500">{{ post.date }}</p>
                          </div>
                        </div>
                        <h3 class="text-md font-medium text-gray-800 mb-2">{{ post.title }}</h3>
                        <p class="text-gray-600 mb-4">{{ post.content }}</p>
                        
                        <div class="grid grid-cols-2 gap-2 mb-4">
                          <img v-for="(img, imgIndex) in post.images" :key="imgIndex" :src="img" alt="分享图片" class="rounded-lg w-full h-32 object-cover" />
                        </div>
                        
                        <div class="flex space-x-4">
                          <button class="flex items-center text-gray-500 hover:text-primary-600">
                            <ThumbsUpIcon class="h-4 w-4 mr-1" />
                            <span>{{ post.likes }}</span>
                          </button>
                          <button class="flex items-center text-gray-500 hover:text-primary-600">
                            <MessageSquareIcon class="h-4 w-4 mr-1" />
                            <span>{{ post.comments }}</span>
                          </button>
                          <button class="flex items-center text-gray-500 hover:text-primary-600">
                            <ShareIcon class="h-4 w-4 mr-1" />
                            <span>分享</span>
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 推荐路线和评价 -->
              <div class="space-y-6">
                <!-- 推荐路线 -->
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">推荐路线</h2>
                    
                    <div class="space-y-4">
                      <div v-for="(route, index) in recommendedRoutes" :key="index" class="border border-gray-200 rounded-lg p-4 hover:border-primary-300 hover:bg-primary-50 transition duration-200 cursor-pointer">
                        <div class="flex items-center mb-2">
                          <MapPinIcon class="h-5 w-5 text-gray-400 mr-2" />
                          <div>
                            <p class="text-sm text-gray-500">出发地</p>
                            <p class="font-medium text-gray-800">{{ route.from }}</p>
                          </div>
                          <ArrowRightIcon class="h-5 w-5 text-gray-400 mx-2" />
                          <div>
                            <p class="text-sm text-gray-500">目的地</p>
                            <p class="font-medium text-gray-800">{{ route.to }}</p>
                          </div>
                        </div>
                        <div class="flex justify-between items-center">
                          <div class="flex items-center">
                            <StarIcon class="h-4 w-4 text-yellow-400" />
                            <span class="text-sm text-gray-600 ml-1">{{ route.rating }}</span>
                          </div>
                          <span class="text-primary-600 font-medium">¥{{ route.price }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 评价排行 -->
                <div class="bg-white shadow rounded-lg overflow-hidden">
                  <div class="p-6">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">司机评价排行</h2>
                    
                    <div class="space-y-4">
                      <div v-for="(driver, index) in topDrivers" :key="index" class="flex items-center">
                        <div class="flex-shrink-0 mr-4">
                          <div class="w-8 h-8 bg-primary-100 rounded-full flex items-center justify-center text-primary-600 font-bold">
                            {{ index + 1 }}
                          </div>
                        </div>
                        <img :src="`/placeholder.svg?height=40&width=40&text=${driver.name.charAt(0)}`" alt="司机头像" class="w-10 h-10 rounded-full mr-3" />
                        <div class="flex-1">
                          <p class="font-medium text-gray-800">{{ driver.name }}</p>
                          <div class="flex items-center">
                            <div class="flex">
                              <StarIcon v-for="i in 5" :key="i" :class="[i <= driver.rating ? 'text-yellow-400' : 'text-gray-300', 'h-4 w-4']" />
                            </div>
                            <span class="text-sm text-gray-500 ml-1">({{ driver.trips }}次托运)</span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      </main>
  
      <!-- 页脚 -->
      <footer class="bg-gray-800 text-white py-10">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div>
              <h3 class="text-lg font-semibold mb-4">宠宠出行</h3>
              <p class="text-gray-400">专业宠物托运服务，让爱宠的旅行更安心</p>
            </div>
            <div>
              <h3 class="text-lg font-semibold mb-4">服务</h3>
              <ul class="space-y-2">
                <li><a href="#" class="text-gray-400 hover:text-white">宠物托运</a></li>
                <li><a href="#" class="text-gray-400 hover:text-white">宠物接送</a></li>
                <li><a href="#" class="text-gray-400 hover:text-white">宠物寄养</a></li>
              </ul>
            </div>
            <div>
              <h3 class="text-lg font-semibold mb-4">关于我们</h3>
              <ul class="space-y-2">
                <li><a href="#" class="text-gray-400 hover:text-white">公司简介</a></li>
                <li><a href="#" class="text-gray-400 hover:text-white">联系我们</a></li>
                <li><a href="#" class="text-gray-400 hover:text-white">加入我们</a></li>
              </ul>
            </div>
            <div>
              <h3 class="text-lg font-semibold mb-4">联系方式</h3>
              <ul class="space-y-2">
                <li class="flex items-center text-gray-400">
                  <PhoneIcon class="h-5 w-5 mr-2" />
                  <span>400-123-4567</span>
                </li>
                <li class="flex items-center text-gray-400">
                  <MailIcon class="h-5 w-5 mr-2" />
                  <span>contact@pettravel.com</span>
                </li>
                <li class="flex items-center text-gray-400">
                  <MapPinIcon class="h-5 w-5 mr-2" />
                  <span>北京市朝阳区宠物大厦</span>
                </li>
              </ul>
            </div>
          </div>
          <div class="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
            <p>© 2025 宠宠出行. 保留所有权利.</p>
          </div>
        </div>
      </footer>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'
  import { 
    BellIcon, MenuIcon, XIcon, MapPinIcon, ArrowRightIcon, 
    InboxIcon, StarIcon, CheckIcon, ThumbsUpIcon, MessageSquareIcon, 
    ShareIcon, PhoneIcon, MailIcon, TruckIcon, PlaneIcon, UsersIcon
  } from 'lucide-vue-next'
  
  // 导航菜单
  const navItems = [
    { name: '首页', page: 'home' },
    { name: '宠物托运', page: 'transport' },
    { name: '托运追踪', page: 'tracking' },
    { name: '社区交流', page: 'community' }
  ]
  
  // 页面状态
  const currentPage = ref('home')
  const mobileMenuOpen = ref(false)
  const userMenuOpen = ref(false)
  
  // 首页数据
  const features = [
    { 
      title: '专业托运', 
      description: '专业宠物托运团队，经验丰富，确保宠物安全舒适', 
      icon: TruckIcon 
    },
    { 
      title: '全程监控', 
      description: '实时视频监控，随时查看宠物状态，安心无忧', 
      icon: 'div' 
    },
    { 
      title: '多种方式', 
      description: '专车、拼车、空运多种方式，满足不同需求', 
      icon: PlaneIcon 
    }
  ]
  
  const serviceSteps = [
    { title: '提交订单', description: '填写宠物和行程信息，选择托运方式' },
    { title: '确认接单', description: '专业托运人员确认接单，准备托运' },
    { title: '安全运输', description: '全程监控，确保宠物安全舒适' },
    { title: '顺利交付', description: '按时到达目的地，完成宠物交接' }
  ]
  
  const reviews = [
    { user: '王小明', rating: 5, comment: '服务非常好，全程可以看到我家猫咪的状态，很放心。司机也很专业，会安抚宠物情绪。' },
    { user: '李华', rating: 4, comment: '第二次使用了，比第一次更满意。价格合理，服务周到，推荐给有需要的朋友。' },
    { user: '张三', rating: 5, comment: '托运过程中可以随时联系司机，了解宠物状态，非常贴心。到达时间也很准确。' }
  ]
  
  // 个人中心数据
  const userProfile = ref({
    username: '王小明',
    phone: '13812345678',
    email: 'wangxm@example.com',
    address: '北京市朝阳区阳光小区'
  })
  
  const showAddPetForm = ref(false)
  const newPet = ref({
    name: '',
    type: 'dog',
    breed: '',
    age: 1
  })
  
  const pets = ref([
    { name: '球球', type: 'dog', breed: '金毛', age: 3 },
    { name: '咪咪', type: 'cat', breed: '英短', age: 2 }
  ])
  
  const addPet = () => {
    pets.value.push({...newPet.value})
    newPet.value = { name: '', type: 'dog', breed: '', age: 1 }
    showAddPetForm.value = false
  }
  
  const removePet = (index) => {
    pets.value.splice(index, 1)
  }
  
  // 订单管理数据
  const orderTabs = [
    { id: 'all', name: '全部订单', count: 5 },
    { id: 'pending', name: '待支付', count: 1 },
    { id: 'in_transit', name: '运输中', count: 2 },
    { id: 'completed', name: '已完成', count: 2 }
  ]
  
  const currentOrderTab = ref('all')
  
  const orders = [
    { 
      id: 'ORD20250301001', 
      date: '2025-03-01', 
      status: 'pending', 
      from: '北京市朝阳区', 
      to: '上海市浦东新区', 
      transportType: '专车托运', 
      petInfo: '金毛，3岁，30kg', 
      price: 1200,
      driver: { name: '张师傅', phone: '13987654321' },
      estimatedArrival: '2025-03-03 14:00'
    },
    { 
      id: 'ORD20250228001', 
      date: '2025-02-28', 
      status: 'in_transit', 
      from: '北京市海淀区', 
      to: '天津市和平区', 
      transportType: '拼车托运', 
      petInfo: '英短，2岁，5kg', 
      price: 600,
      driver: { name: '李师傅', phone: '13876543210' },
      estimatedArrival: '2025-03-01 16:00'
    },
    { 
      id: 'ORD20250227001', 
      date: '2025-02-27', 
      status: 'in_transit', 
      from: '北京市西城区', 
      to: '广州市天河区', 
      transportType: '空运托运', 
      petInfo: '泰迪，4岁，8kg', 
      price: 1800,
      driver: { name: '王师傅', phone: '13765432109' },
      estimatedArrival: '2025-02-28 10:00'
    },
    { 
      id: 'ORD20250225001', 
      date: '2025-02-25', 
      status: 'completed', 
      from: '北京市东城区', 
      to: '深圳市南山区', 
      transportType: '专车托运', 
      petInfo: '柯基，1岁，12kg', 
      price: 1500,
      driver: { name: '赵师傅', phone: '13654321098' },
      estimatedArrival: '2025-02-26 18:00'
    },
    { 
      id: 'ORD20250220001', 
      date: '2025-02-20', 
      status: 'completed', 
      from: '北京市丰台区', 
      to: '成都市武侯区', 
      transportType: '空运托运', 
      petInfo: '哈士奇，2岁，25kg', 
      price: 2000,
      driver: { name: '钱师傅', phone: '13543210987' },
      estimatedArrival: '2025-02-22 09:00'
    }
  ]
  
  const filteredOrders = computed(() => {
    if (currentOrderTab.value === 'all') {
      return orders
    }
    return orders.filter(order => order.status === currentOrderTab.value)
  })
  
  const getStatusText = (status) => {
    switch (status) {
      case 'pending': return '待支付'
      case 'in_transit': return '运输中'
      case 'completed': return '已完成'
      default: return status
    }
  }
  
  const showOrderDetail = (order) => {
    // 实现查看订单详情的逻辑
    console.log('查看订单详情', order.id)
  }
  
  // 宠物托运数据
  const transportForm = ref({
    from: '',
    to: '',
    petId: 0,
    weight: 0,
    type: 'private',
    date: '',
    time: '',
    notes: ''
  })
  
  const transportTypes = [
    { 
      id: 'private', 
      name: '专车托运', 
      description: '一对一专车服务，全程专人照顾', 
      icon: TruckIcon,
      priceRate: 1.5
    },
    { 
      id: 'shared', 
      name: '拼车托运', 
      description: '多宠物同行，经济实惠', 
      icon: UsersIcon,
      priceRate: 1.0
    },
    { 
      id: 'air', 
      name: '空运托运', 
      description: '长途首选，快速安全', 
      icon: PlaneIcon,
      priceRate: 2.0
    }
  ]
  
  const calculateBasePrice = () => {
    // 简单的价格计算逻辑
    return 500
  }
  
  const calculateWeightPrice = () => {
    return transportForm.value.weight * 10
  }
  
  const calculateTypePrice = () => {
    const selectedType = transportTypes.find(t => t.id === transportForm.value.type)
    return selectedType ? 200 * selectedType.priceRate : 0
  }
  
  const calculateTotalPrice = () => {
    return calculateBasePrice() + calculateWeightPrice() + calculateTypePrice()
  }
  
  const submitTransportOrder = () => {
    // 实现提交订单的逻辑
    console.log('提交订单', transportForm.value)
    // 跳转到订单管理页面
    currentPage.value = 'orders'
  }
  
  // AI客服聊天
  const chatInput = ref('')
  const chatMessages = ref([
    { text: '您好，我是AI智能客服，有什么可以帮助您的吗？', isUser: false }
  ])
  
  const sendChatMessage = () => {
    if (chatInput.value.trim()) {
      chatMessages.value.push({ text: chatInput.value, isUser: true })
      
      // 模拟AI回复
      setTimeout(() => {
        let response = '感谢您的咨询，我们的客服人员会尽快回复您。'
        
        if (chatInput.value.includes('价格')) {
          response = '我们的托运价格根据距离、宠物重量和托运方式而定，您可以在下单页面查看详细价格计算。'
        } else if (chatInput.value.includes('时间')) {
          response = '托运时间取决于距离和交通状况，一般城市内1-3小时，城际间需要1-2天，跨省托运需要2-5天。'
        } else if (chatInput.value.includes('安全')) {
          response = '我们非常重视宠物安全，所有托运车辆都配备温控系统和监控设备，全程可以通过APP查看宠物状态。'
        }
        
        chatMessages.value.push({ text: response, isUser: false })
      }, 1000)
      
      chatInput.value = ''
    }
  }
  
  // 托运追踪数据
  const trackingOrderId = ref('')
  const trackingSteps = ['已接单', '已取件', '运输中', '即将到达', '已送达']
  
  const trackingOrder = computed(() => {
    return orders.find(o => o.id === trackingOrderId.value) || orders[1] // 默认显示第二个订单
  })
  
  // 司机聊天
  const driverChatInput = ref('')
  const driverMessages = ref([
    { text: '您好，我是您的托运司机，已经接到您的宠物，请放心。', isUser: false },
    { text: '好的，谢谢您，请照顾好我的宠物。', isUser: true },
    { text: '请问宠物有什么特殊习惯需要注意的吗？', isUser: false }
  ])
  
  const sendDriverMessage = () => {
    if (driverChatInput.value.trim()) {
      driverMessages.value.push({ text: driverChatInput.value, isUser: true })
      
      // 模拟司机回复
      setTimeout(() => {
        driverMessages.value.push({ 
          text: '收到，我会注意的。宠物状态良好，请您放心。', 
          isUser: false 
        })
      }, 1000)
      
      driverChatInput.value = ''
    }
  }
  
  // 社区交流数据
  const communityPosts = [
    {
      user: '王小明',
      date: '2025-03-01',
      title: '第一次使用宠物托运，非常满意',
      content: '昨天第一次使用宠物托运服务，从北京到上海，全程可以通过APP查看我家金毛的状态，非常放心。司机也很专业，会定时给宠物喂水，照顾得很周到。',
      images: ['/placeholder.svg?height=200&width=300&text=Image1', '/placeholder.svg?height=200&width=300&text=Image2'],
      likes: 24,
      comments: 8
    },
    {
      user: '李华',
      date: '2025-02-28',
      title: '推荐北京到广州的空运路线',
      content: '上周用空运把我家猫咪从北京送到广州，全程只用了5个小时，比我想象中快很多。猫咪到达后状态也很好，没有太大应激反应，推荐大家选择空运。',
      images: ['/placeholder.svg?height=200&width=300&text=Image3', '/placeholder.svg?height=200&width=300&text=Image4'],
      likes: 36,
      comments: 12
    }
  ]
  
  const recommendedRoutes = [
    { from: '北京', to: '上海', rating: 4.8, price: 1200 },
    { from: '北京', to: '广州', rating: 4.7, price: 1800 },
    { from: '北京', to: '深圳', rating: 4.9, price: 1900 },
    { from: '北京', to: '成都', rating: 4.6, price: 1500 }
  ]
  
  const topDrivers = [
    { name: '张师傅', rating: 5.0, trips: 328 },
    { name: '李师傅', rating: 4.9, trips: 256 },
    { name: '王师傅', rating: 4.8, trips: 189 },
    { name: '赵师傅', rating: 4.7, trips: 142 },
    { name: '钱师傅', rating: 4.6, trips: 98 }
  ]
  </script>
  
  <style>
  :root {
    --color-primary-50: #fff1f0;
    --color-primary-100: #ffe4e0;
    --color-primary-500: #ff7e5f;
    --color-primary-600: #ff6347;
    --color-primary-700: #e5573e;
    
    --color-secondary-50: #f0f4ff;
    --color-secondary-100: #e0e9ff;
    --color-secondary-500: #6b7fd7;
    --color-secondary-600: #5a6dc0;
    --color-secondary-700: #4a5ba0;
  }
  
  .bg-primary-50 { background-color: var(--color-primary-50); }
  .bg-primary-100 { background-color: var(--color-primary-100); }
  .bg-primary-500 { background-color: var(--color-primary-500); }
  .bg-primary-600 { background-color: var(--color-primary-600); }
  .bg-primary-700 { background-color: var(--color-primary-700); }
  
  .bg-secondary-50 { background-color: var(--color-secondary-50); }
  .bg-secondary-100 { background-color: var(--color-secondary-100); }
  .bg-secondary-500 { background-color: var(--color-secondary-500); }
  .bg-secondary-600 { background-color: var(--color-secondary-600); }
  .bg-secondary-700 { background-color: var(--color-secondary-700); }
  
  .text-primary-600 { color: var(--color-primary-600); }
  .text-primary-700 { color: var(--color-primary-700); }
  .text-primary-800 { color: var(--color-primary-700); }
  
  .text-secondary-600 { color: var(--color-secondary-600); }
  .text-secondary-700 { color: var(--color-secondary-700); }
  .text-secondary-800 { color: var(--color-secondary-700); }
  
  .border-primary-300 { border-color: var(--color-primary-100); }
  .border-primary-500 { border-color: var(--color-primary-500); }
  
  .hover\:bg-primary-100:hover { background-color: var(--color-primary-100); }
  .hover\:bg-primary-700:hover { background-color: var(--color-primary-700); }
  
  .hover\:bg-secondary-700:hover { background-color: var(--color-secondary-700); }
  
  .focus\:ring-primary-500:focus { --tw-ring-color: var(--color-primary-500); }
  .focus\:border-primary-500:focus { border-color: var(--color-primary-500); }
  
  .focus\:ring-secondary-500:focus { --tw-ring-color: var(--color-secondary-500); }
  .focus\:border-secondary-500:focus { border-color: var(--color-secondary-500); }
  
  .from-primary-500 { --tw-gradient-from: var(--color-primary-500); }
  .to-secondary-500 { --tw-gradient-to: var(--color-secondary-500); }
  </style>