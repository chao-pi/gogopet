<template>
  <div class="pets-page">
    <div class="pets-container">
      <!-- 宠物列表头部 -->
      <div class="pets-header">
        <h2>我的宠物</h2>
        <button class="add-pet-btn" @click="handleAddPet">
          <i class="fas fa-plus"></i>
          添加宠物
        </button>
      </div>

      <!-- 宠物列表 -->
      <div class="pets-list">
        <div v-if="pets.length === 0" class="empty-state">
          <i class="fas fa-paw"></i>
          <p>您还没有添加任何宠物</p>
          <button class="add-pet-btn" @click="handleAddPet">
            <i class="fas fa-plus"></i>
            添加宠物
          </button>
        </div>

        <div v-else class="pets-grid">
          <div v-for="pet in pets" :key="pet.id" class="pet-card">
            <div class="pet-image">
              <img :src="pet.imageUrl" :alt="pet.name" />
              <div class="pet-status" :class="pet.status">
                {{ petStatusText[pet.status] }}
              </div>
            </div>
            <div class="pet-info">
              <h3>{{ pet.name }}</h3>
              <p class="pet-type">{{ pet.type }}</p>
              <div class="pet-details">
                <div class="detail-item">
                  <i class="fas fa-birthday-cake"></i>
                  <span>{{ pet.age }}岁</span>
                </div>
                <div class="detail-item">
                  <i class="fas fa-venus-mars"></i>
                  <span>{{ pet.gender === 'M' ? '公' : '母' }}</span>
                </div>
              </div>
            </div>
            <div class="pet-actions">
              <button class="action-btn edit" @click="handleEditPet(pet)">
                <i class="fas fa-edit"></i>
              </button>
              <button class="action-btn delete" @click="handleDeletePet(pet)">
                <i class="fas fa-trash"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 模拟宠物数据
const pets = ref([
  {
    id: '1',
    name: '旺财',
    type: '金毛',
    age: 2,
    gender: 'M',
    status: 'healthy',
    imageUrl: 'https://example.com/dog1.jpg'
  },
  {
    id: '2',
    name: '咪咪',
    type: '英短',
    age: 1,
    gender: 'F',
    status: 'healthy',
    imageUrl: 'https://example.com/cat1.jpg'
  }
])

const petStatusText = {
  healthy: '健康',
  sick: '生病',
  pregnant: '怀孕',
  vaccinated: '已接种'
}

const handleAddPet = () => {
  // TODO: 实现添加宠物功能
  console.log('添加宠物')
}

const handleEditPet = (pet) => {
  // TODO: 实现编辑宠物功能
  console.log('编辑宠物:', pet)
}

const handleDeletePet = (pet) => {
  // TODO: 实现删除宠物功能
  console.log('删除宠物:', pet)
}
</script>

<style scoped>
.pets-page {
  min-height: 100vh;
  background-color: #f3f4f6;
  padding: 2rem;
}

.pets-container {
  max-width: 1200px;
  margin: 0 auto;
}

.pets-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.pets-header h2 {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1f2937;
}

.add-pet-btn {
  background-color: #f97316;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
}

.add-pet-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.empty-state {
  text-align: center;
  padding: 4rem;
  background: white;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.empty-state i {
  font-size: 4rem;
  color: #e5e7eb;
  margin-bottom: 1rem;
}

.empty-state p {
  color: #6b7280;
  margin-bottom: 1.5rem;
}

.pets-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.pet-card {
  background: white;
  border-radius: 1rem;
  overflow: hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.pet-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.pet-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.pet-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.pet-status {
  position: absolute;
  top: 1rem;
  right: 1rem;
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.75rem;
  font-weight: 500;
}

.pet-status.healthy {
  background-color: #10b981;
  color: white;
}

.pet-status.sick {
  background-color: #ef4444;
  color: white;
}

.pet-status.pregnant {
  background-color: #8b5cf6;
  color: white;
}

.pet-status.vaccinated {
  background-color: #3b82f6;
  color: white;
}

.pet-info {
  padding: 1rem;
}

.pet-info h3 {
  font-size: 1.25rem;
  font-weight: bold;
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.pet-type {
  color: #6b7280;
  margin-bottom: 1rem;
}

.pet-details {
  display: flex;
  gap: 1rem;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #4b5563;
}

.pet-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  padding: 1rem;
  border-top: 1px solid #e5e7eb;
}

.action-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.action-btn.edit {
  background-color: #e5e7eb;
  color: #1f2937;
}

.action-btn.delete {
  background-color: #fee2e2;
  color: #ef4444;
}

.action-btn:hover {
  transform: scale(1.1);
}

@media (max-width: 640px) {
  .pets-page {
    padding: 1rem;
  }
  
  .pets-grid {
    grid-template-columns: 1fr;
  }
}
</style> 