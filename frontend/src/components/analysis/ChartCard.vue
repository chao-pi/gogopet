<template>
    <div class="chart-card" :style="{ height }">
      <div class="card-header">
        <h3 class="title">{{ title }}</h3>
        <div class="actions">
          <slot name="actions"></slot>
        </div>
      </div>
      <div class="card-body">
        <slot></slot>
      </div>
      <div v-if="$slots.footer" class="card-footer">
        <slot name="footer"></slot>
      </div>
    </div>
</template>
  
<script>
  export default {
    name: 'ChartCard',
    props: {
      title: {
        type: String,
        default: ''
      },
      height: {
        type: String,
        default: 'auto'
      }
    }
  }
</script>
  
<style lang="scss" scoped>
  .chart-card {
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: column;
    overflow: hidden;
    transition: all 0.3s ease;
  
    &:hover {
      box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
      transform: translateY(-2px);
    }
  
    .card-header {
      padding: 16px 20px;
      border-bottom: 1px solid #f0f0f0;
      display: flex;
      align-items: center;
      justify-content: space-between;
  
      .title {
        font-size: 16px;
        font-weight: 600;
        color: #333;
        margin: 0;
      }
  
      .actions {
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }
  
    .card-body {
      flex: 1;
      padding: 16px;
      position: relative;
    }
  
    .card-footer {
      padding: 12px 20px;
      border-top: 1px solid #f0f0f0;
      background-color: #fafafa;
      font-size: 12px;
      color: #666;
    }
  }
</style>